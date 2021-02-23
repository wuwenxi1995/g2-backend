package org.g2.starter.redisson.infra.factory;

import org.g2.starter.redisson.infra.enums.LockType;
import org.g2.starter.redisson.infra.service.LockStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
@Component
public class LockStrategyFactory implements BeanPostProcessor {

    private Map<LockType, LockStrategy> map = new HashMap<>(16);

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        if (bean instanceof LockStrategy) {
            LockStrategy lockStrategy = (LockStrategy) bean;
            map.put(lockStrategy.lockType(), lockStrategy);
        }
        return bean;
    }

    public LockStrategy getLockStrategy(LockType lockType) {
        return map.get(lockType);
    }
}
