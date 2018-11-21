package com.github.kostrovik.useful.utils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-20
 * github:  https://github.com/kostrovik/useful-utils
 */
public class CopyFileVisitor extends SimpleFileVisitor<Path> {
    private static Logger logger = InstanceLocatorUtil.getLocator().getLogger(FileSystemUtil.class.getName());

    private Path source;
    private Path destination;

    public CopyFileVisitor(Path source, Path destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path newDirectory = destination.resolve(source.relativize(dir));

        logger.log(Level.INFO, "Создание раздела. {0}", newDirectory);

        Files.createDirectory(newDirectory);

        logger.log(Level.INFO, "Раздел создан.");

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path newFile = destination.resolve(source.relativize(file));

        logger.log(Level.INFO, "Копирование файла. {0}", file);

        Files.copy(file, newFile, StandardCopyOption.REPLACE_EXISTING);

        logger.log(Level.INFO, "Файл скопирован. {0}", newFile);

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
        logger.log(Level.SEVERE, "Ошибка обработки файла или директории.", e);

        return FileVisitResult.CONTINUE;
    }
}