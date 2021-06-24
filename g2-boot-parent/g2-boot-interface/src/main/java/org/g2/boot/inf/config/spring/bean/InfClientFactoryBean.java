package org.g2.boot.inf.config.spring.bean;

import org.g2.boot.inf.infra.proxy.InfClientProxy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * @author wuwenxi 2021-06-24
 */
public class InfClientFactoryBean implements FactoryBean {

    private Class<?> beanClass;
    private ApplicationContext applicationContext;

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object getObject() throws Exception {
        return new InfClientProxy(beanClass, applicationContext).proxy();
    }

    @Override
    public Class<?> getObjectType() {
        return beanClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
