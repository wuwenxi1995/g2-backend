package org.g2.starter.mq;

import org.g2.starter.mq.infra.constants.MqConstants;
import org.g2.starter.mq.infra.util.MqUtil;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
public class MqProcessorCreator implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private volatile RedisCacheClient redisCacheClient;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        Annotation annotation;
        if (bean instanceof MessageListener && (annotation = findAnnotation(bean)) != null) {
            // 获取messageListerAdapter
            String messageListerBeanName = MqUtil.getBeanName(MqConstants.MESSAGE_LISTENER_ADAPTER, beanName);
            MessageListenerAdapter messageListenerAdapter = applicationContext.getBean(messageListerBeanName, MessageListenerAdapter.class);
            messageListenerAdapter.setDelegate(bean);
            // 获取 RedisMessageListenerContainer
            String containerBeanName = MqUtil.getBeanName(MqConstants.REDIS_MESSAGE_LISTENER_CONTAINER, beanName);
            RedisMessageListenerContainer container = applicationContext.getBean(containerBeanName, RedisMessageListenerContainer.class);
            Collection<? extends Topic> topic = getTopic(annotation);
            Assert.notEmpty(topic, "at least one topic required");
            container.addMessageListener(messageListenerAdapter, topic);
            if (redisCacheClient == null) {
                redisCacheClient = applicationContext.getBean(RedisCacheClient.class);
            }
            container.setConnectionFactory(redisCacheClient.getRequiredConnectionFactory());
        }
        return bean;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 是否为目标对象
     *
     * @param bean bean
     * @return 返回注解
     */
    protected Annotation findAnnotation(Object bean) {
        return null;
    }

    /**
     * 生成topic
     *
     * @param bean bean对象
     * @return topic
     */
    protected Collection<? extends Topic> getTopic(Annotation bean) {
        return null;
    }
}
