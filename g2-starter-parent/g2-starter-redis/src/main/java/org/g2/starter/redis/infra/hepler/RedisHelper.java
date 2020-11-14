package org.g2.starter.redis.infra.hepler;

import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
public class RedisHelper {

    @Autowired
    private RedisCacheClient redisCacheClient;

    public RedisHelper() {
    }

    public void setDataBase(Integer index) {
        LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory) redisCacheClient.getConnectionFactory();
        connectionFactory.setDatabase(index);
        redisCacheClient.setConnectionFactory(connectionFactory);
    }
}
