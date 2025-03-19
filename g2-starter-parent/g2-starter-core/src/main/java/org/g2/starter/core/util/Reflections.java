package org.g2.starter.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
public final class Reflections {

    private static final Logger log = LoggerFactory.getLogger(Reflections.class);

    private Reflections() {
    }

    public static Class<?> getClassGenericType(Class<?> clazz) {
        return getClassGenericType(clazz, 0);
    }

    public static Class<?> getClassGenericType(Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();

        if (genType instanceof Class) {
            if (ParameterizedType.class != genType) {
                getClassGenericType(clazz, index);
            }
        }

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            log.error("index : {},maybe ArrayIndexOutOfBoundsException，size of :{} ParameterizedType :{}", index, clazz.getSimpleName(), params.length);
            return Object.class;
        }

        if (!(params[index] instanceof Class)) {
            log.error("");
            return Object.class;
        }

        return (Class) params[index];
    }
}
