package org.g2.dynamic.jdbc.refresh;

import java.util.Map;

/**
 * @author wuwenxi 2023-02-09
 */
public interface Refreshable {

    /**
     * 数据刷新
     *
     * @param obj 数据
     */
    void refresh(Map<String, Object> obj);
}
