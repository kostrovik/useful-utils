package com.github.kostrovik.useful.utils;

import com.github.kostrovik.useful.interfaces.LoggerConfigInterface;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-20
 * github:  https://github.com/kostrovik/useful-utils
 */
public class InstanceLocatorUtil {
    private static volatile InstanceLocatorUtil locator;

    private InstanceLocatorUtil() {
    }

    public static InstanceLocatorUtil provider() {
        return getLocator();
    }

    public static InstanceLocatorUtil getLocator() {
        if (locator == null) {
            synchronized (InstanceLocatorUtil.class) {
                if (locator == null) {
                    locator = new InstanceLocatorUtil();
                }
            }
        }
        return locator;
    }

    public <E> Optional<E> getFirstLoadedImplementation(Class<E> type) {
        return ServiceLoader.load(ModuleLayer.boot(), type).findFirst();
    }

    public Logger getLogger(String className) {
        Optional<LoggerConfigInterface> instances = getFirstLoadedImplementation(LoggerConfigInterface.class);

        if (instances.isPresent()) {
            return instances.get().getLogger(className);
        }

        LoggerConfigImpl loggerConfig = new LoggerConfigImpl();

        return loggerConfig.getLogger(className);
    }

    public Logger getLogger(String className, Class<?> specialLogger) {
        Iterator<LoggerConfigInterface> instances = ServiceLoader.load(ModuleLayer.boot(), LoggerConfigInterface.class).iterator();
        while (instances.hasNext()) {
            LoggerConfigInterface instance = instances.next();
            if (instance.getClass().isAssignableFrom(specialLogger)) {
                return instance.getLogger(className);
            }
        }

        return getLogger(className);
    }
}
