package org.g2.starter.redis.mq.listener.config;

import org.g2.starter.redis.infra.constants.MqConstants;
import org.g2.starter.redis.mq.config.processor.MqProcessorCreator;
import org.g2.starter.redis.mq.listener.annotation.Listener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
public class ListenerProcessor extends MqProcessorCreator {

    public ListenerProcessor(ThreadPoolTaskExecutor executor) {
        super(executor);
    }

    @Override
    protected Collection<? extends Topic> getTopic(Annotation annotation) {
        Listener listener = (Listener) annotation;
        String db = listener.db() == -1 ? "*" : String.valueOf(listener.db());
        return Collections.singletonList(new PatternTopic(String.format(MqConstants.KEY_EXPIRATION_EVENT, db)));
    }

    @Override
    protected Annotation findAnnotation(Object bean) {
        return AnnotationUtils.findAnnotation(bean.getClass(), Listener.class);
    }
}
