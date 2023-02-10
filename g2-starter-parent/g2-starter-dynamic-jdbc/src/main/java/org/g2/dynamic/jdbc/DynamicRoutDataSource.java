package org.g2.dynamic.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.g2.core.util.StringUtil;
import org.g2.dynamic.jdbc.config.properties.DynamicJdbcProperties;
import org.g2.dynamic.jdbc.context.DataRoutHolder;
import org.g2.dynamic.jdbc.context.DynamicRoutContextHolder;
import org.g2.dynamic.jdbc.factory.DataSourceFactory;
import org.g2.dynamic.jdbc.factory.MemoryDataSourceKeyFactory;
import org.g2.dynamic.jdbc.refresh.Refreshable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源
 * <p>
 * 多数据源, 实现分库分表
 * </p>
 *
 * @author wuwenxi 2023-02-09
 */
public class DynamicRoutDataSource extends AbstractRoutingDataSource implements Refreshable {

    private static final Logger log = LoggerFactory.getLogger(DynamicRoutDataSource.class);

    private volatile Map<Object, Object> datasourceCache;
    private final DataSourceFactory dataSourceFactory;
    private final DynamicJdbcProperties properties;
    private final MemoryDataSourceKeyFactory memoryDataSourceKeyFactory;

    public DynamicRoutDataSource(DataSource dataSource, DataSourceFactory dataSourceFactory, DynamicJdbcProperties properties, MemoryDataSourceKeyFactory memoryDataSourceKeyFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.properties = properties;
        this.memoryDataSourceKeyFactory = memoryDataSourceKeyFactory;
        this.setDefaultTargetDataSource(dataSource);
        this.setTargetDataSources(Collections.emptyMap());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        // 通过线程变量获取
        String dsKey;
        if (StringUtils.isNotBlank(dsKey = DataRoutHolder.get())) {
            return this.getCurDsRout(dsKey);
        }
        // 通过请求上下文获取
        DynamicRoutContextHolder.DynamicRoutContext dynamicRoutContext = DynamicRoutContextHolder.get();
        if (dynamicRoutContext == null) {
            return "NULL_KEY";
        }
        return this.memoryDataSourceKeyFactory.getDataSourceKey(dynamicRoutContext.getOrganizationId(), dynamicRoutContext.getUri(), dynamicRoutContext.getMethod());
    }

    @Override
    public void refresh(Map<String, Object> config) {
        this.datasourceCache = new HashMap<>(16);
        if (!config.isEmpty()) {
            this.close(config);
            Map<Object, Object> datasource = DynamicRoutResolver.resolveDatasource(config);
            // 初始化数据源
            datasource.forEach((key, value) -> datasourceCache.put(key, this.dataSourceFactory.create(value)));
        }
        this.setTargetDataSources(datasourceCache);
        this.afterPropertiesSet();
    }

    private String getCurDsRout(String dsKey) {
        return dsKey.startsWith(properties.getDsPrefix()) ? dsKey : properties.getDsPrefix() + dsKey;
    }

    private void close(Map<String, Object> config) {
        config.values().forEach(datasource -> {
            if (datasource instanceof Closeable) {
                try {
                    ((Closeable) datasource).close();
                } catch (IOException e) {
                    log.error("close datasource error, errorMsg :{}", StringUtil.exceptionString(e));
                }
            }
        });
    }
}
