package org.g2.dynamic.jdbc.factory;

import org.springframework.core.Ordered;

import javax.sql.DataSource;

/**
 * @author wuwenxi 2023-02-09
 */
public interface DataSourceProcessor extends Ordered {

    /**
     * 对数据源进行处理
     *
     * @param dataSource       数据源
     * @param datasourceConfig 数据配置
     */
    void process(DataSource dataSource, Object datasourceConfig);
}
