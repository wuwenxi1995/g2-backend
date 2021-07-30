package org.g2.starter.redis.mq.subject.config;

import org.g2.starter.redis.mq.config.processor.MqProcessorCreator;
import org.g2.starter.redis.mq.subject.annotation.Subject;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
public class SubjectProcessor extends MqProcessorCreator {

    @Override
    protected Collection<? extends Topic> getTopic(Annotation annotation) {
        Subject subject = (Subject) annotation;
        String[] values = subject.values();
        Assert.notEmpty(values, "at least one values required in @Subject");
        List<PatternTopic> patternTopics = new ArrayList<>();
        for (String value : values) {
            PatternTopic patternTopic = new PatternTopic(value);
            patternTopics.add(patternTopic);
        }
        return patternTopics;
    }

    @Override
    protected Annotation findAnnotation(Object bean) {
        return AnnotationUtils.findAnnotation(bean.getClass(), Subject.class);
    }
}
