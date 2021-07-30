package org.g2.starter.redis.mq.config.processor;

import org.g2.core.base.BaseConstants;
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
import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
public class MqProcessorCreator implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        if (bean instanceof RedisMessageListenerContainer
                && beanName.contains(BaseConstants.Symbol.WELL)) {
            // 获取 RedisMessageListenerContainer
            RedisMessageListenerContainer container = (RedisMessageListenerContainer) bean;
            Map<String, MessageListenerAdapter> messageListenerAdapterMap = applicationContext.getBeansOfType(MessageListenerAdapter.class);
            messageListenerAdapterMap.forEach((name, messageListenerAdapter) -> {
                if (name.contains(BaseConstants.Symbol.WELL)) {
                    MessageListener messageListener = applicationContext.getBean(name.split(BaseConstants.Symbol.WELL)[1], MessageListener.class);
                    Annotation annotation = findAnnotation(messageListener);
                    if (annotation == null) {
                        return;
                    }
                    Collection<? extends Topic> topic = getTopic(annotation);
                    Assert.notEmpty(topic, "at least one topic required");
                    container.addMessageListener(messageListenerAdapter, topic);
                }
            });
            if (container.getConnectionFactory() == null) {
                RedisCacheClient redisCacheClient = applicationContext.getBean(RedisCacheClient.class);
                container.setConnectionFactory(redisCacheClient.getRequiredConnectionFactory());
            }
        }
        return null;
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
