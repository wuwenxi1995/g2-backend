package org.g2.starter.mq.publisher.config;

import org.g2.core.helper.ApplicationContextHelper;
import org.g2.starter.mq.publisher.annotation.Publisher;
import org.g2.starter.mq.publisher.annotation.Topic;
import org.g2.starter.redis.client.RedisCacheClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;

/**
 * @author wuwenxi 2021-05-21
 */
public class PublisherProxyCreator implements InstantiationAwareBeanPostProcessor, InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(PublisherProxyCreator.class);

    private static volatile RedisCacheClient redisCacheClient;

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (AnnotationUtils.findAnnotation(beanClass, Publisher.class) != null) {
            if (!beanClass.isInterface()) {
                log.warn("Skip creating a proxy , {} has @Publisher but it's not an interface", beanClass.getSimpleName());
                return null;
            }
            // 创建代理对象
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setCallback(this);
            return enhancer.create();
        }
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Topic topic;
        if ((topic = AnnotationUtils.findAnnotation(method, Topic.class)) == null) {
            return method.invoke(proxy, args);
        }
        if (args.length != 1) {
            throw new IllegalArgumentException(String.format("method %s need one args", method.getName()));
        }
        String channel = topic.topic();
        if (redisCacheClient == null) {
            redisCacheClient = ApplicationContextHelper.getApplicationContext().getBean(RedisCacheClient.class);
        }
        Object message = args[0];
        redisCacheClient.convertAndSend(channel, message);
        return message;
    }
}
