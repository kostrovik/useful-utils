package com.github.kostrovik.useful.formatters;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-11-20
 * github:  https://github.com/kostrovik/useful-utils
 */
public class LogMessageFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
        String result = super.format(record);
        result = result + "\n";
        return result;
    }
}
