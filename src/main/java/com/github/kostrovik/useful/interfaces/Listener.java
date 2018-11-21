package com.github.kostrovik.useful.interfaces;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-21
 * github:  https://github.com/kostrovik/useful-utils
 */
public interface Listener<T> {
    void handle(T result);

    void error(Throwable error);
}
