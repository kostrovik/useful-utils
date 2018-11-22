package com.github.kostrovik.useful.models;

import com.github.kostrovik.useful.interfaces.Listener;
import com.github.kostrovik.useful.interfaces.Observable;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-22
 * github:  https://github.com/kostrovik/useful-utils
 */
public abstract class AbstractObservable implements Observable {
    protected List<Listener<EventObject>> listeners;

    protected AbstractObservable() {
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addListener(Listener<EventObject> listener) {
        if (Objects.nonNull(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(Listener<EventObject> listener) {
        listeners.remove(listener);
    }

    protected void notifyLlisteners(Object source) {
        listeners.forEach(listener -> listener.handle(new EventObject(source)));
    }
}