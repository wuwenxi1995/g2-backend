package org.g2.message.listener.config.processor;

import org.g2.message.listener.RedisMessageListener;
import org.g2.message.listener.factory.RedisMessageListenerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

/**
 * @author wuwenxi 2022-12-06
 */
public class RedisMessageListenerBeanProcessor implements BeanPostProcessor {

    private final RedisMessageListenerFactory redisMessageListenerFactory;

    public RedisMessageListenerBeanProcessor(RedisMessageListenerFactory redisMessageListenerFactory) {
        this.redisMessageListenerFactory = redisMessageListenerFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, String beanName) throws BeansException {
        if (bean instanceof RedisMessageListener) {
            redisMessageListenerFactory.add(beanName, (RedisMessageListener) bean);
        }
        return bean;
    }
}
