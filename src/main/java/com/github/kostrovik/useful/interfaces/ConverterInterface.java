package com.github.kostrovik.useful.interfaces;

import java.util.Map;

/**
 * project: useful-utils
 * author:  kostrovik
 * date:    2018-12-13
 * github:  https://github.com/kostrovik/useful-utils
 */
public interface ConverterInterface<E> {
    String toJsonString(E entity);

    String toJsonString(Map<String, String> map);

    String toXmlString(E entity);

    String toXmlString(Map<String, String> map);

    E fromJsonString(String json);

    E fromXmlString(String xml);

    E fromMap(Map map);
}
