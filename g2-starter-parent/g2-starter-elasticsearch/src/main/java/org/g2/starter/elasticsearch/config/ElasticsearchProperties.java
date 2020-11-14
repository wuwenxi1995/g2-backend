package org.g2.starter.elasticsearch.config;

import java.time.Duration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wenxi.wu@hand-china.com 2020/4/18
 */
@ConfigurationProperties(
        prefix = "g2.elasticsearch"
)
@Data
public class ElasticsearchProperties {

    /**
     * es 节点 集群节点以 “，” 分开
     */
    private String clusterNodes = "127.0.0.1:9200";
    /**
     * 默认协议
     */
    private String scheme = "http";
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接配置
     */
    private Pool pool;
    /**
     * 是否开启嗅探器
     */
    private boolean enableSniff = false;

    @Data
    public static class Pool {
        /**
         * 最大连接数
         */
        private int maxConnectionNum;
        /**
         * 最大连接
         */
        private int maxConnectionPerRoute;
        /**
         * 连接超时
         */
        private Duration connectionTimeout;
        /**
         * 读取超时时间
         */
        private Duration readTimeout;
    }
}
