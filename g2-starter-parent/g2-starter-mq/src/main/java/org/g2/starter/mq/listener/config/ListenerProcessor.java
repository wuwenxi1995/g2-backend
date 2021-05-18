package org.g2.starter.mq.listener.config;

import org.g2.starter.mq.MqProcessorCreator;
import org.g2.starter.mq.infra.constants.MqConstants;
import org.g2.starter.mq.listener.annotation.Listener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
public class ListenerProcessor extends MqProcessorCreator {

    @Override
    protected Collection<? extends Topic> getTopic(Annotation annotation) {
        if (annotation instanceof Listener) {
            Listener listener = (Listener) annotation;
            String db = listener.db() == -1 ? "*" : "" + listener.db();
            return Collections.singletonList(new PatternTopic(String.format(MqConstants.KEY_EXPIRATION_EVENT, db)));
        }
        return null;
    }

    @Override
    protected Annotation findAnnotation(Object bean) {
        if (bean instanceof MessageListener) {
            return AnnotationUtils.findAnnotation(bean.getClass(), Listener.class);
        }
        return null;
    }
}
