package org.g2.dynamic.jdbc.factory;

import org.g2.dynamic.jdbc.DynamicRoutResolver;
import org.g2.dynamic.jdbc.loadbanlance.LoadBalance;
import org.g2.dynamic.jdbc.loadbanlance.LoadBalanceBuildFactory;
import org.g2.dynamic.jdbc.refresh.Refreshable;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuwenxi 2023-02-10
 */
public class MemoryDataSourceKeyFactory implements Refreshable {

    private static final String LOAD_BALANCE_KEY = "%s:%s:%s";

    private volatile Map<String, Map<String, Map<String, String>>> datasourceMapping;
    private Map<String, LoadBalance> loadBalanceMap;
    private final AntPathMatcher matcher = new AntPathMatcher();

    private final LoadBalanceBuildFactory loadBalanceBuildFactory;

    public MemoryDataSourceKeyFactory(LoadBalanceBuildFactory loadBalanceBuildFactory) {
        this.loadBalanceBuildFactory = loadBalanceBuildFactory;
    }

    public Object getDataSourceKey(String organizationId, String url, String method) {
        Map<String, Map<String, String>> second = datasourceMapping.getOrDefault(organizationId, datasourceMapping.get("*"));
        if (second == null) {
            return "NULL_KEY";
        }
        Map<String, String> third = second.get("*");
        for (Map.Entry<String, Map<String, String>> entry : second.entrySet()) {
            String key = entry.getKey();
            Map<String, String> value = entry.getValue();
            if (this.matcher.match(key, url)) {
                third = value;
            }
        }
        if (third == null) {
            return "NULL_KEY";
        }
        String datasourceKey = third.getOrDefault(method, third.get("*"));
        if (datasourceKey == null) {
            return "NULL_KEY";
        }
        String loadBalanceKey = String.format(LOAD_BALANCE_KEY, organizationId, url, method);
        LoadBalance loadBalance = this.loadBalanceMap.computeIfAbsent(loadBalanceKey, s -> {
            String[] keys = datasourceKey.split(",");
            return loadBalanceBuildFactory.create(keys);
        });
        Object chooseKey;
        return (chooseKey = loadBalance.choose()) == null ? "NULL_KEY" : chooseKey;
    }

    @Override
    public void refresh(Map<String, Object> datasourceConfig) {
        if (datasourceConfig.isEmpty()) {
            this.datasourceMapping = new HashMap<>(0);
        } else {
            this.datasourceMapping = DynamicRoutResolver.resolveMap(datasourceConfig);
        }
        this.loadBalanceMap = new ConcurrentHashMap<>(32);
    }
}
