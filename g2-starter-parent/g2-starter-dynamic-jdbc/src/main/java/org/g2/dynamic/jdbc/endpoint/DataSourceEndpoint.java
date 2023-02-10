package org.g2.dynamic.jdbc.endpoint;

import org.g2.dynamic.jdbc.factory.RedisDatasourceFactory;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author wuwenxi 2023-02-10
 */
@RestControllerEndpoint(
        id = "datasource"
)
public class DataSourceEndpoint {

    private final RedisDatasourceFactory redisDatasourceFactory;

    public DataSourceEndpoint(RedisDatasourceFactory redisDatasourceFactory) {
        this.redisDatasourceFactory = redisDatasourceFactory;
    }

    @PostMapping
    public void publish(@RequestBody Map<String, Object> datasourceConfig) {
        redisDatasourceFactory.refresh2Redis(datasourceConfig);
    }
}
