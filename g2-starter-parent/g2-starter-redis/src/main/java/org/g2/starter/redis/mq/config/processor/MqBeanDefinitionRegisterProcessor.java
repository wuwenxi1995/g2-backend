package org.g2.starter.redis.mq.config.processor;

import org.g2.core.util.StringUtil;
import org.g2.starter.redis.client.RedisCacheClient;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-17
 */
public class MqBeanDefinitionRegisterProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private BeanDefinitionRegistry registry;

    private ThreadPoolTaskExecutor executor;

    private MqProcessorCreator listenerProcessor;
    private MqProcessorCreator subjectProcessor;

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
            RootBeanDefinition containerBean = new RootBeanDefinition();
            containerBean.setBeanClass(RedisMessageListenerContainer.class);
            // 必须设置监听线程池
            containerBean.getPropertyValues().add("taskExecutor", executor);
            containerBean.getPropertyValues().add("connectionFactory", beanFactory.getBean(RedisCacheClient.class).getRequiredConnectionFactory());
            registry.registerBeanDefinition(StringUtil.getBeanName(MqConstants.REDIS_MESSAGE_LISTENER_CONTAINER, listenerBeans.size()), containerBean);
            // 加入后置处理器
            beanFactory.addBeanPostProcessor(listenerProcessor);
            beanFactory.addBeanPostProcessor(subjectProcessor);
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.listenerProcessor = new ListenerProcessor().setApplicationContext(applicationContext);
        this.subjectProcessor = new SubjectProcessor().setApplicationContext(applicationContext);
    }
}
