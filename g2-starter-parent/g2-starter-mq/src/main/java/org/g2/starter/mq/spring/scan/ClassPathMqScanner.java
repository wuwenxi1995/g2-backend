package org.g2.starter.mq.spring.scan;

import org.g2.starter.mq.constants.MqConstants;
import org.g2.starter.mq.listener.annotation.Listener;
import org.g2.starter.mq.subject.annotation.Subject;
import org.g2.starter.mq.util.MqUtil;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Set;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-17
 */
public class ClassPathMqScanner extends ClassPathBeanDefinitionScanner {

    private BeanDefinitionRegistry registry;

    public ClassPathMqScanner(BeanDefinitionRegistry registry) {
        super(registry);
        this.registry = registry;
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : holders) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) holder.getBeanDefinition();

            RootBeanDefinition messageListenerAdapterBean = new RootBeanDefinition();
            messageListenerAdapterBean.setBeanClass(MessageListenerAdapter.class);
            registry.registerBeanDefinition(MqUtil.getBeanName(MqConstants.MESSAGE_LISTENER_ADAPTER, beanDefinition.getBeanClassName()), messageListenerAdapterBean);

            RootBeanDefinition containerBean = new RootBeanDefinition();
            containerBean.setBeanClass(RedisMessageListenerContainer.class);
            registry.registerBeanDefinition(MqUtil.getBeanName(MqConstants.REDIS_MESSAGE_LISTENER_CONTAINER, beanDefinition.getBeanClassName()), containerBean);
        }
        return holders;
    }

    @Override
    protected boolean isCandidateComponent(@NonNull MetadataReader metadataReader) throws IOException {
        return (metadataReader.getAnnotationMetadata().hasAnnotation(Listener.class.getSimpleName())
                || metadataReader.getAnnotationMetadata().hasAnnotation(Subject.class.getSimpleName())
                && metadataReader.getClassMetadata().getClass().isAssignableFrom(MessageListener.class));
    }
}
