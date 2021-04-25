package org.g2.starter.redis.infra.customizer;

import java.util.List;
import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-04-22
 */
public interface CustomizerRedisStreamCommand<K, V> {

    String xAdd(K key, Map<K, V> content);

    List<Map<K, V>> xRead();

}
