package org.g2.starter.mq.config.spring.scan;

import org.g2.starter.mq.infra.constants.MqConstants;
import org.g2.starter.mq.infra.util.MqUtil;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.lang.NonNull;

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

    @NonNull
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : holders) {
            RootBeanDefinition messageListenerAdapterBean = new RootBeanDefinition();
            messageListenerAdapterBean.setBeanClass(MessageListenerAdapter.class);
            registry.registerBeanDefinition(MqUtil.getBeanName(MqConstants.MESSAGE_LISTENER_ADAPTER, holder.getBeanName()), messageListenerAdapterBean);

            RootBeanDefinition containerBean = new RootBeanDefinition();
            containerBean.setBeanClass(RedisMessageListenerContainer.class);
            registry.registerBeanDefinition(MqUtil.getBeanName(MqConstants.REDIS_MESSAGE_LISTENER_CONTAINER, holder.getBeanName()), containerBean);
        }
        return holders;
    }
}
