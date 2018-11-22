package com.github.kostrovik.useful.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-20
 * github:  https://github.com/kostrovik/useful-utils
 */
public class FileSystemUtil {
    private static Logger logger = InstanceLocatorUtil.getLocator().getLogger(FileSystemUtil.class.getName());

    public Path getCurrentDirectory() {
        return getCurrentDirectory(FileSystemUtil.class);
    }

    public Path getCurrentDirectory(Class<?> entity) {
        Path fileDirectory = Paths.get(entity.getProtectionDomain().getCodeSource().getLocation().getPath());
        if (fileDirectory.getParent().toString().equalsIgnoreCase(File.separator)) {
            fileDirectory = Paths.get(System.getProperty("java.home"));
        }
        if (fileDirectory.getFileName().toString().contains(".jar")) {
            fileDirectory = fileDirectory.getParent();
        }
        return fileDirectory;
    }

    public void createPath(Path filePath) throws IOException {
        if (Files.notExists(filePath)) {
            Files.createDirectories(filePath);
        }
    }

    public void copyDirectory(Path source, Path destination) throws IOException {
        Files.walkFileTree(source, new CopyFileVisitor(source, destination));
    }

    public void removeDirectory(Path root) throws IOException {
        try (Stream<Path> directory = Files.list(root)) {
            directory.forEach(this::removeDirectories);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка удаления директории.", e);
            throw e;
        }
    }

    private void removeDirectories(Path directory) {
        if (Objects.nonNull(directory)) {
            try (Stream<Path> filesStream = Files.walk(directory)) {
                filesStream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Ошибка удаления директории или файла.", e);
            }
        }
    }

    public void setPermissionsOnDirectory(Path directory, String filePosixPermissions) throws IOException {
        try (Stream<Path> filesDir = Files.list(directory)) {
            filesDir.forEach(path -> {
                try {
                    Files.setPosixFilePermissions(path, PosixFilePermissions.fromString(filePosixPermissions));
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Ошибка установки прав для файла. ", e);
                }
            });
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка чтения директории.", e);
            throw e;
        }
    }
}