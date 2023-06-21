package org.g2.starter.dynamic.jdbc.factory;

import com.zaxxer.hikari.HikariDataSource;
import org.g2.starter.dynamic.jdbc.DynamicRoutResolver;

import javax.sql.DataSource;

/**
 * @author wuwenxi 2023-03-16
 */
public class DefaultDataSourceProcessor implements DataSourceProcessor {
    @Override
    public void process(DataSource dataSource, Object datasourceConfig) {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            DynamicRoutResolver.resolve(datasourceConfig, "");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
