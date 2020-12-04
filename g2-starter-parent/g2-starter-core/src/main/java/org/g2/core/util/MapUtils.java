package org.g2.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2020-11-27
 */
public final class MapUtils {

    private MapUtils() {
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.size() == 0;
    }

    public static <K, V> List<Map<K, V>> splitMap(Map<K, V> map, int num) {
        if (isEmpty(map)) {
            return null;
        }
        List<Map<K, V>> result = new ArrayList<>();
        if (num < 1) {
            result.add(map);
            return result;
        }
        Map<K, V> split = new HashMap<>(num);
        int index = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            index++;
            split.put(entry.getKey(), entry.getValue());
            if (index == num) {
                result.add(split);
                split = new HashMap<>(num);
                index = 0;
            }
        }
        if (isNotEmpty(split)) {
            result.add(split);
        }
        return result;
    }
}
