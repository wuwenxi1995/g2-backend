package org.g2.starter.dynamic.jdbc.factory;

import javax.sql.DataSource;

/**
 * @author wuwenxi 2023-02-09
 */
public interface DataSourceFactory {

    /**
     * 创建数据源
     *
     * @param dataSourceInfo 数据源信息
     * @return 数据源
     */
    DataSource create(Object dataSourceInfo);
}
