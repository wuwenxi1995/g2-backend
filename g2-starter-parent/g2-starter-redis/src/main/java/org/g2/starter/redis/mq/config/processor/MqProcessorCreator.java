package org.g2.starter.redis.mq.config.processor;

import org.g2.core.base.BaseConstants;
import org.g2.starter.redis.client.RedisCacheClient;
import org.g2.starter.redis.infra.constants.MqConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

/**
 * RedisMessageListenerContainer 必须设置线程池，
 * <p>
 * spring-data-redis监听默认线程池{@link SimpleAsyncTaskExecutor}没有使用池化，
 * 会一直创建线程，可能导致OOM
 * </p>
 *
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
public class MqProcessorCreator implements BeanPostProcessor {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        if (bean instanceof RedisMessageListenerContainer
                && beanName.equals(MqConstants.REDIS_MESSAGE_LISTENER_CONTAINER)) {
            // 获取 RedisMessageListenerContainer
            RedisMessageListenerContainer container = (RedisMessageListenerContainer) bean;
            Map<String, MessageListener> messageListenerMap = applicationContext.getBeansOfType(MessageListener.class);
            messageListenerMap.forEach((name, messageListener) -> {
                Annotation annotation = findAnnotation(messageListener);
                if (annotation == null) {
                    return;
                }
                Collection<? extends Topic> topic = getTopic(annotation);
                Assert.notEmpty(topic, "at least one topic required");
                container.addMessageListener(messageListener, topic);
            });
        }
        return null;
    }

    public MqProcessorCreator setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        return this;
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
