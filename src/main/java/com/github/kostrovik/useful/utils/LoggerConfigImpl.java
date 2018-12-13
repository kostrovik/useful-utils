package com.github.kostrovik.useful.utils;

import com.github.kostrovik.useful.formatters.LogMessageFormatter;
import com.github.kostrovik.useful.interfaces.LoggerConfigInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-20
 * github:  https://github.com/kostrovik/useful-utils
 */
public class LoggerConfigImpl implements LoggerConfigInterface {
    private static volatile FileHandler fileHandler;

    @Override
    public Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        FileHandler handler = getFileHandler();
        if (Objects.nonNull(handler)) {
            logger.addHandler(handler);
        }

        return logger;
    }

    @Override
    public Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    protected synchronized FileHandler getFileHandler() {
        if (Objects.isNull(fileHandler)) {
            fileHandler = createLoggerFile("logs");
        }
        return fileHandler;
    }

    protected FileHandler createLoggerFile(String directoryName) {
        try {
            Path applicationDirectory = Paths.get(LoggerConfigImpl.class.getProtectionDomain().getCodeSource().getLocation().getPath());

            if (applicationDirectory.getParent().toString().equals(File.separator)) {
                applicationDirectory = Paths.get(System.getProperty("java.home"));
            } else {
                applicationDirectory = applicationDirectory.getParent();
            }
            if (applicationDirectory.getFileName().toString().contains(".jar")) {
                applicationDirectory = applicationDirectory.getParent();
            }

            Path logPath = Paths.get(applicationDirectory + File.separator + directoryName, String.format("%s.log", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

            if (Files.notExists(logPath.getParent())) {
                Files.createDirectory(logPath.getParent());
            }
            FileHandler handler = new FileHandler(logPath.toString(), true);
            handler.setLevel(Level.ALL);
            handler.setFormatter(new LogMessageFormatter());

            return handler;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
