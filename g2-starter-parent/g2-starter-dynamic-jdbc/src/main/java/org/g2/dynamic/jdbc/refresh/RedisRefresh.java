package org.g2.dynamic.jdbc.refresh;

import java.util.Map;

/**
 * @author wuwenxi 2023-02-09
 */
public interface RedisRefresh extends Refreshable {

    /**
     * 刷新缓存
     *
     * @param datasourceConfig datasource数据
     */
    void refresh2Redis(Map<String, Object> datasourceConfig);
}
