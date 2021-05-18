package org.g2.starter.mq.listener.config;

import org.g2.starter.mq.MqProcessorCreator;
import org.g2.starter.mq.constants.MqConstants;
import org.g2.starter.mq.listener.annotation.Listener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;

import java.util.Collection;
import java.util.Collections;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
public class ListenerProcessor extends MqProcessorCreator {

    @Override
    protected Collection<? extends Topic> getTopic(Object bean) {
        Listener listener = AnnotationUtils.findAnnotation(bean.getClass(), Listener.class);
        String db = listener.db() == -1 ? "*" : "" + listener.db();
        return Collections.singletonList(new PatternTopic(String.format(MqConstants.KEY_EXPIRATION_EVENT, db)));
    }

    @Override
    protected boolean findAnnotation(Object bean) {
        return AnnotationUtils.findAnnotation(bean.getClass(), Listener.class) != null && bean instanceof MessageListener;
    }
}
