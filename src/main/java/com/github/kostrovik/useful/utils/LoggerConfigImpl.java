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

        try {
            createLoggerFile();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка создания файла логирования.", e);
        }

        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        logger.addHandler(fileHandler);

        return logger;
    }

    protected String prepareLogDirectoryName() {
        return "logs";
    }

    private void createLoggerFile() throws IOException {
        if (fileHandler == null) {
            synchronized (LoggerConfigImpl.class) {
                if (fileHandler == null) {
                    Path applicationDirectory = Paths.get(LoggerConfigImpl.class.getProtectionDomain().getCodeSource().getLocation().getPath());

                    if (applicationDirectory.getParent().toString().equals(File.separator)) {
                        applicationDirectory = Paths.get(System.getProperty("java.home"));
                    } else {
                        applicationDirectory = applicationDirectory.getParent();
                    }

                    Path logPath = Paths.get(applicationDirectory + File.separator + prepareLogDirectoryName(), String.format("%s.log", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

                    if (Files.notExists(logPath.getParent())) {
                        Files.createDirectory(logPath.getParent());
                    }

                    fileHandler = new FileHandler(logPath.toString(), true);
                    fileHandler.setLevel(Level.ALL);
                    fileHandler.setFormatter(new LogMessageFormatter());
                }
            }
        }
    }


}
