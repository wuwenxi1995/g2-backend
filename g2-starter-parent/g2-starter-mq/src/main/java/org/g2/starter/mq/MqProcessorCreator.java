package org.g2.starter.mq;

import org.g2.starter.mq.constants.MqConstants;
import org.g2.starter.mq.util.MqUtil;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.lang.NonNull;

import java.util.Collection;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
public class MqProcessorCreator implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        if (findAnnotation(bean)) {
            // 获取messageListerAdapter
            String messageListerBeanName = MqUtil.getBeanName(MqConstants.MESSAGE_LISTENER_ADAPTER, beanName);
            MessageListenerAdapter messageListenerAdapter = applicationContext.getBean(messageListerBeanName, MessageListenerAdapter.class);
            messageListenerAdapter.setDelegate(bean);
            // 获取 RedisMessageListenerContainer
            String containerBeanName = MqUtil.getBeanName(MqConstants.REDIS_MESSAGE_LISTENER_CONTAINER, beanName);
            RedisMessageListenerContainer container = applicationContext.getBean(containerBeanName, RedisMessageListenerContainer.class);
            container.addMessageListener(messageListenerAdapter, getTopic(bean));
            container.setConnectionFactory(redisConnectionFactory);
        }
        return bean;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.redisConnectionFactory = applicationContext.getBean(RedisCacheClient.BEAN_NAME, RedisCacheClient.class).getRequiredConnectionFactory();
    }

    /**
     * 是否为目标对象
     *
     * @param bean bean
     * @return true
     */
    protected boolean findAnnotation(Object bean) {
        return false;
    }

    /**
     * 生成topic
     *
     * @param bean bean对象
     * @return topic
     */
    protected Collection<? extends Topic> getTopic(Object bean) {
        return null;
    }
}
