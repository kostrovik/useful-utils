package com.github.kostrovik.useful.interfaces;

import java.util.EventObject;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-22
 * github:  https://github.com/kostrovik/useful-utils
 */
public interface Observable {
    void addListener(Listener<EventObject> listener);

    void removeListener(Listener<EventObject> listener);
}
