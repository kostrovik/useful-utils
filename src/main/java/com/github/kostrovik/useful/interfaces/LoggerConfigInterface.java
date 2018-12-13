package com.github.kostrovik.useful.interfaces;

import java.util.logging.Logger;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-20
 * github:  https://github.com/kostrovik/useful-utils
 */
public interface LoggerConfigInterface {
    Logger getLogger(String className);

    Logger getLogger(Class clazz);
}
