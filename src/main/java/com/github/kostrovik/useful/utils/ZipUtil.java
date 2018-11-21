package com.github.kostrovik.useful.utils;

import com.github.kostrovik.useful.interfaces.Listener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-20
 * github:  https://github.com/kostrovik/useful-utils
 */
public class ZipUtil {
    private static Logger logger = InstanceLocatorUtil.getLocator().getLogger(ZipUtil.class.getName());
    private static final int BUFFER_SIZE = 4096;
    private FileSystemUtil fileSystemUtil;
    private Listener<String> listener;
    private static final String ERROR_CREATE_ZIP = "Ошибка создания архива.";

    public ZipUtil() {
        this.fileSystemUtil = new FileSystemUtil();
    }

    public void setListener(Listener<String> listener) {
        this.listener = listener;
    }

    public void extract(Path archive, Path destination) throws IOException {
        if (!Files.exists(destination)) {
            NotDirectoryException e = new NotDirectoryException(destination.toString());
            logger.log(Level.SEVERE, "Ошибка распаковки архива.", e);
            throw e;
        }

        try (ZipInputStream source = new ZipInputStream(new FileInputStream(archive.toString()))) {
            ZipEntry entry = source.getNextEntry();
            while (Objects.nonNull(entry)) {
                Path filePath = Paths.get(destination.toString(), entry.getName());
                if (!entry.isDirectory()) {
                    extractFile(source, filePath);
                } else {
                    fileSystemUtil.createPath(filePath);
                }
                source.closeEntry();
                entry = source.getNextEntry();
            }
        }
    }

    private void extractFile(ZipInputStream zipInputStream, Path filePath) throws IOException {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath.toString()))) {
            if (Objects.nonNull(listener)) {
                listener.handle(String.format("Извлечение: %s", filePath));
            }
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipInputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
        }
    }

    public void compress(Path directory, Path resultFile) {
        try (FileOutputStream outputStream = new FileOutputStream(resultFile.toString())) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                compressDirectory(directory, zipOutputStream, resultFile, Paths.get(File.separator));
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Файл не найден.", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, ERROR_CREATE_ZIP, e);
        }
    }

    private void compressDirectory(Path source, ZipOutputStream zipOutputStream, Path resultFile, Path zipRoot) throws IOException {
        try (Stream<Path> files = Files.list(source)) {
            files.forEach(file -> {
                if (Files.isDirectory(file)) {
                    try {
                        if (file.endsWith(File.separator)) {
                            zipOutputStream.putNextEntry(new ZipEntry(zipRoot.toString()));
                            zipOutputStream.closeEntry();
                        } else {
                            zipOutputStream.putNextEntry(new ZipEntry(zipRoot.toString() + File.separator + file.getFileName().toString() + File.separator));
                            zipOutputStream.closeEntry();
                        }
                        compressDirectory(file, zipOutputStream, resultFile, Paths.get(zipRoot.toString(), file.getFileName().toString()));
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, ERROR_CREATE_ZIP, e);
                    }
                } else {
                    try {
                        compressFile(file, zipOutputStream, resultFile, zipRoot);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, ERROR_CREATE_ZIP, e);
                    }
                }
            });
        }
    }

    private void compressFile(Path source, ZipOutputStream zipOutputStream, Path resultFile, Path zipRoot) throws IOException {
        if (!source.equals(resultFile) && !Files.isHidden(source)) {
            try (FileInputStream fileInputStream = new FileInputStream(source.toString())) {
                if (Objects.nonNull(listener)) {
                    listener.handle(String.format("Сжатие: %s", source));
                }
                byte[] buffer = new byte[BUFFER_SIZE];
                zipOutputStream.putNextEntry(new ZipEntry(Paths.get(zipRoot.toString(), source.getFileName().toString()).toString()));
                int n;
                while ((n = fileInputStream.read(buffer)) >= 0) {
                    zipOutputStream.write(buffer, 0, n);
                }
                zipOutputStream.closeEntry();
            }
        }
    }
}