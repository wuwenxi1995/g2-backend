package org.g2.dynamic.jdbc;

import java.util.Map;

/**
 * @author wuwenxi 2023-02-09
 */
public final class DynamicRoutResolver {

    private DynamicRoutResolver() {
    }

    @SuppressWarnings("unchecked")
    public static Map<Object, Object> resolveDatasource(Map<String, Object> datasourceConfig) {
        return (Map) datasourceConfig.get("dsInfo");
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Map<String, Map<String, String>>> resolveMap(Map<String, Object> datasourceConfig) {
        return (Map) datasourceConfig.get("dsMap");
    }

    public static String resolve(Object dataSourceInfo, String resolveName) {
        return (String) ((Map) dataSourceInfo).get(resolveName);
    }
}
