package org.g2.starter.redis.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wuwenxi 2021-08-23
 */
@ConfigurationProperties(prefix = "g2.redis.mq")
public class RedisMqConfigurationProperties {

    private boolean enable;

    public RedisMqConfigurationProperties() {
        this.enable = false;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
