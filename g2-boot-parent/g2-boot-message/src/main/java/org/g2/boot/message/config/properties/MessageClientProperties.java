package org.g2.boot.message.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-19
 */
@Component
@ConfigurationProperties(prefix = "g2.message")
public class MessageClientProperties {

    private Integer messageRedisDatabase = 1;
    private String defaultLang = "zh_CN";

    public Integer getMessageRedisDatabase() {
        return messageRedisDatabase;
    }

    public MessageClientProperties setMessageRedisDatabase(Integer messageRedisDatabase) {
        this.messageRedisDatabase = messageRedisDatabase;
        return this;
    }

    public String getDefaultLang() {
        return defaultLang;
    }

    public MessageClientProperties setDefaultLang(String defaultLang) {
        this.defaultLang = defaultLang;
        return this;
    }
}
