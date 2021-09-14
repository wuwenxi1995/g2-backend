package org.g2.starter.redis.mq.config.processor;

import org.g2.core.util.StringUtil;
import org.g2.starter.redis.infra.constants.MqConstants;
import org.g2.starter.redis.mq.listener.annotation.Listener;
import org.g2.starter.redis.mq.listener.config.ListenerProcessor;
import org.g2.starter.redis.mq.subject.annotation.Subject;
import org.g2.starter.redis.mq.subject.config.SubjectProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-17
 */
public class MqBeanDefinitionRegisterProcessor implements BeanDefinitionRegistryPostProcessor {

    private static final String DELEGATE = "delegate";

    private BeanDefinitionRegistry registry;

    private ThreadPoolTaskExecutor executor;

    public MqBeanDefinitionRegisterProcessor(ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, Object> subjectBeans = beanFactory.getBeansWithAnnotation(Subject.class);
        Map<String, Object> listenerBeans = beanFactory.getBeansWithAnnotation(Listener.class);
        listenerBeans.putAll(subjectBeans);
        if (listenerBeans.size() > 0) {
            listenerBeans.forEach((beanName, bean) -> {
                RootBeanDefinition messageListenerAdapterBean = new RootBeanDefinition();
                messageListenerAdapterBean.setBeanClass(MessageListenerAdapter.class);
                messageListenerAdapterBean.getPropertyValues().add(DELEGATE, bean);
                registry.registerBeanDefinition(StringUtil.getBeanName(MqConstants.MESSAGE_LISTENER_ADAPTER, beanName), messageListenerAdapterBean);
            });
            RootBeanDefinition containerBean = new RootBeanDefinition();
            containerBean.setBeanClass(RedisMessageListenerContainer.class);
            registry.registerBeanDefinition(StringUtil.getBeanName(MqConstants.REDIS_MESSAGE_LISTENER_CONTAINER, listenerBeans.size()), containerBean);
            // 加入后置处理器
            beanFactory.addBeanPostProcessor(new ListenerProcessor(executor));
            beanFactory.addBeanPostProcessor(new SubjectProcessor(executor));
        }
    }
}
