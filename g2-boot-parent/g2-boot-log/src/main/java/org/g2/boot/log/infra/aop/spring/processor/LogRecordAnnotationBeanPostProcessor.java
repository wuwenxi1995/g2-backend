package org.g2.boot.log.infra.aop.spring.processor;

import org.g2.boot.log.infra.annotation.LogRecord;
import org.g2.boot.log.infra.aop.spring.advisor.LogRecordAnnotationAdvisor;
import org.g2.core.aop.base.BaseAnnotationBeanPostProcessor;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactory;

/**
 * 日志记录后置处理器
 * <p>
 * 主要目的：给spring增强器中添加一个自定义增强器，用于扫描标有{@link LogRecord}注解的方法，
 * </p>
 *
 * @author wuwenxi 2021-10-13
 */
public class LogRecordAnnotationBeanPostProcessor extends BaseAnnotationBeanPostProcessor {

    @Override
    protected Advisor buildAdvisor(BeanFactory beanFactory) {
        LogRecordAnnotationAdvisor advisor = new LogRecordAnnotationAdvisor();
        advisor.setBeanFactory(beanFactory);
        return advisor;
    }
}
