package org.g2.starter.mq.subject.publish;

import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * redis 消息发布
 *
 * @author wuwenxi 2021-05-18
 */
public class Publisher {

    @Autowired
    private RedisCacheClient redisCacheClient;

    public void publish(String channel, Object object) {
        redisCacheClient.convertAndSend(channel, object);
    }
}
