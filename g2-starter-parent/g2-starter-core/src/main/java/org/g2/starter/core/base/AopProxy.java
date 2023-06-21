package org.g2.starter.core.base;

import org.springframework.aop.framework.AopContext;

/**
 * proxy()便于获取自身接口代理类，常用在一个事务方法里调用当前类的其它事务方法，
 * 如果不使用代理对象调用方法，本质使用的是原始对象，因而可能导致事务或AOP拦截不生效
 *
 * @author wenxi.wu@hand-chian.com 2021-03-01
 */
public interface AopProxy<T> {


    /**
     * 获取aop代理对象
     *
     * @return aop代理对象
     */
    @SuppressWarnings("unchecked")
    default T proxy() {
        return (T) AopContext.currentProxy();
    }
}
