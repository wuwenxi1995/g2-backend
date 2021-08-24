package org.g2.core.aop.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wuwenxi 2021-08-24
 */
@ConfigurationProperties("g2.starter.interceptor")
public class InterceptorConfigurationProperties {

    private boolean enable;

    public InterceptorConfigurationProperties() {
        this.enable = false;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
