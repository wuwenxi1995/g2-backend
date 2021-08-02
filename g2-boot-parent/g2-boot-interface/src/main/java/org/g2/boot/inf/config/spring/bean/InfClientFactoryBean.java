package org.g2.boot.inf.config.spring.bean;

import org.g2.boot.inf.infra.exception.InfClientException;
import org.g2.boot.inf.infra.proxy.InfClientProxy;
import org.g2.core.base.CheckFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuwenxi 2021-06-24
 */
public class InfClientFactoryBean extends CheckFactoryBean implements FactoryBean {

    private static final Logger log = LoggerFactory.getLogger(InfClientFactoryBean.class);

    private Class<?> beanClass;
    private ApplicationContext applicationContext;
    private ConcurrentHashMap<Class<?>, InfClientProxy> proxyMap;

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setProxyMap(ConcurrentHashMap<Class<?>, InfClientProxy> proxyMap) {
        this.proxyMap = proxyMap;
    }

    @Override
    protected boolean checkBean() throws IllegalArgumentException {
        Assert.notNull(beanClass, "Property 'beanClass' is required");
        if (!beanClass.isInterface()) {
            log.warn("Skip put proxyMap. Cause bean " + beanClass + " is not an interface");
            return false;
        }
        return true;
    }

    @Override
    protected void initBean() throws Exception {
        if (!proxyMap.containsKey(this.beanClass)) {
            proxyMap.put(this.beanClass, new InfClientProxy(beanClass, applicationContext));
        }
    }

    @Override
    public Object getObject() {
        InfClientProxy infClientProxy = proxyMap.get(this.beanClass);
        if (infClientProxy == null) {
            throw new InfClientException("Bean " + beanClass + " is not known to proxyMap");
        }
        try {
            return infClientProxy.proxy();
        } catch (Exception e) {
            throw new InfClientException("Error create infClient proxy. Cause:", e);
        }
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
