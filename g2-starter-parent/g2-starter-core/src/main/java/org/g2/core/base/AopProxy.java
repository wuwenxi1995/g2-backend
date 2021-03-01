package org.g2.core.base;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * <p>
 * 场景：
 * Service中A方法调用B方法，A方法未使用@Transactional注解而B方法使用@Transactional注解，调用过程中发生异常，B方法事务不生效.
 * <p>
 * 原理解析：
 * spring 在扫描bean的时候会扫描方法上是否包含{@link Transactional}注解，
 * spring会为包含该注解的bean动态地生成一个子类（即代理类，proxy），代理类是继承原来那个bean的。
 * a.当标有@Transactional注解的方法被外部直接调用时，实际上是代理类负责调用(查看
 * {@link org.springframework.aop.framework.CglibAopProxy.DynamicAdvisedInterceptor}调用过程)
 * 代理类在调用目标方法执行前会开启事务{@link TransactionInterceptor#invoke(MethodInvocation)}，并在目标方法结束后提交事务。
 * b.当有@Transactional注解的方法被同类中不带@Transactional注解的方法调用时，实际上是注入IOC容器中的bean负责调用，因此不开启事务
 * <p>
 * 使用：Service中不带@Transactional注解的方法调用同类中带有@Transactional注解的方法
 * </p>
 *
 * @author wenxi.wu@hand-chian.com 2021-03-01
 */
public interface AopProxy<T> {


    /**
     * 生成aop代理对象
     *
     * @return aop代理对象
     */
    @SuppressWarnings("unchecked")
    default T proxy() {
        return (T) AopContext.currentProxy();
    }
}
