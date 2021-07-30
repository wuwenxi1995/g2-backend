package org.g2.starter.redis.infra.constants;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-17
 */
public interface MqConstants {

    String MESSAGE_LISTENER_ADAPTER = "messageListenerAdapter";
    String REDIS_MESSAGE_LISTENER_CONTAINER = "redisMessageListenerContainer";

    String KEY_EXPIRATION_EVENT = "__keyevent@%s__:expired";
}
