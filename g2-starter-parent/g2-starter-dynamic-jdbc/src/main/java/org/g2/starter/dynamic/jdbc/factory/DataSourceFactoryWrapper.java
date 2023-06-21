package org.g2.starter.dynamic.jdbc.factory;

import org.springframework.core.Ordered;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2023-02-09
 */
public class DataSourceFactoryWrapper implements DataSourceFactory {

    private final DataSourceFactory dataSourceFactory;
    private List<DataSourceProcessor> dataSourceProcessors = new ArrayList<>();

    public DataSourceFactoryWrapper(DataSourceFactory dataSourceFactory, List<DataSourceProcessor> dataSourceProcessors) {
        this.dataSourceFactory = dataSourceFactory;
        if (dataSourceProcessors != null && dataSourceProcessors.size() > 1) {
            this.dataSourceProcessors = dataSourceProcessors.stream().sorted(Comparator.comparing(Ordered::getOrder)).collect(Collectors.toList());
        }
    }

    @Override
    public DataSource create(Object dataSourceInfo) {
        DataSource dataSource = this.dataSourceFactory.create(dataSourceInfo);
        dataSourceProcessors.forEach(item -> item.process(dataSource, dataSourceInfo));
        return dataSource;
    }
}
