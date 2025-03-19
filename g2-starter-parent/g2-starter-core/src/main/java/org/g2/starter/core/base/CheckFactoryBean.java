package org.g2.starter.core.base;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author wuwenxi 2021-08-02
 */
public abstract class CheckFactoryBean implements InitializingBean {

    @Override
    public final void afterPropertiesSet() throws IllegalArgumentException, BeanInitializationException {
        boolean success = checkBean();
        if (success) {
            try {
                initBean();
            } catch (Exception e) {
                throw new BeanInitializationException("Initialization of FactoryBean failed", e);
            }
        }
    }

    /**
     * 检查bean信息
     *
     * @return boolean
     * @throws IllegalArgumentException 异常信息
     */
    protected abstract boolean checkBean() throws IllegalArgumentException;

    protected void initBean() throws Exception {
    }
}
