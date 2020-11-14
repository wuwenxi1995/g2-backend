package org.g2.starter.redis.config.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@ConfigurationProperties(
        prefix = "g2.redis.shard"
)
public class RedisShardingProperties extends RedisProperties {

    /**
     * 是否开启多实例
     */
    private boolean enable = false;

    /**
     * 多实例配置
     */
    private Map<String, RedisShardingConnection> instances;


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<String, RedisShardingConnection> getInstances() {
        return instances;
    }

    public void setInstances(Map<String, RedisShardingConnection> instances) {
        this.instances = instances;
    }

    public static class RedisShardingConnection {

        /**
         * 主机地址
         */
        private String host;
        /**
         * 端口
         */
        private int port;
        /**
         * 连接密码
         */
        private String password;
        /**
         * 库
         */
        private int dbIndex;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getDbIndex() {
            return dbIndex;
        }

        public void setDbIndex(int dbIndex) {
            this.dbIndex = dbIndex;
        }
    }
}
