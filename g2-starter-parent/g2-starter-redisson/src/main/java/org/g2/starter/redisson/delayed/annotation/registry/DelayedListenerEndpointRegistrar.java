package org.g2.starter.redisson.delayed.annotation.registry;

import org.g2.starter.redisson.delayed.annotation.DelayedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author wuwenxi 2021-12-27
 */
public class DelayedListenerEndpointRegistrar implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(DelayedListenerEndpointRegistrar.class);

    private final List<DelayedListenerEndpoint> listenerEndpoints = new ArrayList<>();

    private BeanFactory beanFactory;

    private DelayedListenerEndpointRegistry endpointRegistry;

    private Executor executor;

    private String defaultExecutorBeanName;

    private boolean startImmediately;

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setDefaultExecutorBeanName(String defaultExecutorBeanName) {
        this.defaultExecutorBeanName = defaultExecutorBeanName;
    }

    public String getDefaultExecutorBeanName() {
        return defaultExecutorBeanName;
    }

    public void setEndpointRegistry(DelayedListenerEndpointRegistry endpointRegistry) {
        this.endpointRegistry = endpointRegistry;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public DelayedListenerEndpointRegistry getEndpointRegistry() {
        return endpointRegistry;
    }

    public void registerEndpoint(DelayedListenerEndpoint delayedListenerEndpoint) {
        Assert.state(delayedListenerEndpoint != null, "Endpoint must be set");
        synchronized (this.listenerEndpoints) {
            if (this.startImmediately) {
                this.endpointRegistry.registerListenerContainer(delayedListenerEndpoint,
                        resolveExecutor(delayedListenerEndpoint.getDelayedListener()), true);
            } else {
                this.listenerEndpoints.add(delayedListenerEndpoint);
            }
        }
    }

    private Executor resolveExecutor(DelayedListener delayedListener) {
        Executor executor;
        if (!"".equals(delayedListener.executor())) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set to obtain container factory by bean name");
            executor = this.beanFactory.getBean(delayedListener.executor(), Executor.class);
        } else if (this.executor != null) {
            executor = this.executor;
        } else if (this.defaultExecutorBeanName != null) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set to obtain container factory by bean name");
            executor = this.beanFactory.getBean(defaultExecutorBeanName, Executor.class);
        } else {
            executor = null;
            log.warn("delayed listener no executor was given and no default is set");
        }
        return executor;
    }

    @Override
    public void afterPropertiesSet() {
        registerAllEndpoint();
    }

    private void registerAllEndpoint() {
        synchronized (this.listenerEndpoints) {
            for (DelayedListenerEndpoint endpoint : this.listenerEndpoints) {
                this.endpointRegistry.registerListenerContainer(endpoint, resolveExecutor(endpoint.getDelayedListener()));
            }
            startImmediately = true;
        }
    }
}
