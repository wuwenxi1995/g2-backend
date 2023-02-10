package org.g2.dynamic.jdbc.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wuwenxi 2023-02-10
 */
@ConfigurationProperties(prefix = "g2.dynamic.jdbc")
@Getter
@Setter
public class DynamicJdbcProperties {

    private String dsPrefix;
    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
}
