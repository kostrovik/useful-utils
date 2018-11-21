package com.github.kostrovik.useful.utils;

import com.github.kostrovik.useful.interfaces.Listener;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-20
 * github:  https://github.com/kostrovik/useful-utils
 */
public class ObservableByteChannel implements ReadableByteChannel {
    private long size;
    private ReadableByteChannel rbc;
    private long sizeRead;
    private Listener<Double> listener;

    public ObservableByteChannel(long size, ReadableByteChannel rbc) {
        this.size = size;
        this.rbc = rbc;
    }

    public ObservableByteChannel(long size, ReadableByteChannel rbc, Listener<Double> listener) {
        this.size = size;
        this.rbc = rbc;
        this.listener = listener;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        int n;
        double progress;
        if ((n = rbc.read(dst)) > 0) {
            sizeRead += n;
            progress = size > 0
                    ? (double) sizeRead / (double) size * 100.0
                    : -1.0;
            if (Objects.nonNull(listener)) {
                listener.handle(progress);
            }
        }
        return n;
    }

    public void close() throws IOException {
        rbc.close();
    }

    public boolean isOpen() {
        return rbc.isOpen();
    }
}