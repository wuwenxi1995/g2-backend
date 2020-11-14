package org.g2.core.helper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 *  JSON 操作工具类
 *
 * @author wenxi.wu@hand-china.com 2020-08-07
 */
public final class FastJsonHelper {

    public static <T> String objectConvertString(T data) {
        return JSONObject.toJSONString(data);
    }

    public static <T extends Collection> String collectionConvertString(T data) {
        return JSONObject.toJSONString(data);
    }

    public static <K, V> String mapConvertString(Map<K, V> data) {
        return JSONObject.toJSONString(data);
    }

    public static <T> T stringConvertObject(String data, Class<T> clazz) {
        return JSONObject.parseObject(data, clazz);
    }

    public static <T> List<T> stringConvertCollection(String data, Class<T> clazz) {
        return JSONArray.parseArray(data, clazz);
    }

    public static <K, V> Map<K, V> stringConvertMap(String data) {
        return JSONObject.parseObject(data.getBytes(), Map.class);
    }

    public static <T> byte[] objectConvertByte(T data) {
        return JSONObject.toJSONString(data).getBytes();
    }

    public static <T extends Collection> byte[] collectionConvertByte(T data) {
        return JSONObject.toJSONString(data).getBytes();
    }

    public static <K, V> byte[] mapConvertByte(Map<K, V> data) {
        return JSONObject.toJSONString(data).getBytes();
    }

    public static <T> T byteConvertObject(byte[] data, Class<T> clazz) {
        return JSONObject.parseObject(data, clazz);
    }

    public static <T> List<T> byteConvertArray(byte[] data, Class<T> clazz) {
        return JSONObject.parseObject(data, clazz);
    }

    public static <K, V> Map<K, V> byteConvertMap(byte[] data) {
        return JSONObject.parseObject(data, Map.class);
    }

    public static JSONObject jsonConvertJsonObject(String json) {
        return JSONObject.parseObject(json);
    }
}
