package org.g2.starter.dynamic.jdbc.factory;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.g2.starter.dynamic.jdbc.refresh.RedisRefresh;
import org.g2.starter.dynamic.jdbc.refresh.Refreshable;
import org.g2.starter.dynamic.redis.hepler.RedisHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Map;

/**
 * @author wuwenxi 2023-02-09
 */
public class RedisDatasourceFactory implements RedisRefresh, ApplicationListener<ApplicationReadyEvent> {

    @Value(value = "${spring.application:name}")
    private String serviceName;

    private static final String DATA_SOURCE_CACHE_CODE = "gpfm:data:source";

    private Collection<Refreshable> refreshableList;
    private final RedisHelper redisHelper;

    public RedisDatasourceFactory(RedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    @Override
    public void refresh2Redis(Map<String, Object> datasourceConfig) {
        redisHelper.setCurrentDataBase(1);
        try {
            redisHelper.hshPut(DATA_SOURCE_CACHE_CODE, serviceName, JSONObject.toJSONString(datasourceConfig));
        } finally {
            redisHelper.clearCurrentDataBase();
        }
        this.refresh(datasourceConfig);
    }

    @Override
    public void refresh(Map<String, Object> datasourceConfig) {
        for (Refreshable refreshable : this.refreshableList) {
            if (refreshable instanceof RedisRefresh) {
                continue;
            }
            refreshable.refresh(datasourceConfig);
        }
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        Map<String, Refreshable> refreshableMap = event.getApplicationContext().getBeansOfType(Refreshable.class);
        if (refreshableMap.isEmpty()) {
            return;
        }
        this.refreshableList = refreshableMap.values();
        redisHelper.setCurrentDataBase(1);
        try {
            String config = redisHelper.hshGet(DATA_SOURCE_CACHE_CODE, serviceName);
            if (StringUtils.isNotBlank(config)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> datasourceConfig = JSONObject.parseObject(config, Map.class);
                this.refresh(datasourceConfig);
            }
        } finally {
            redisHelper.clearCurrentDataBase();
        }
    }
}
