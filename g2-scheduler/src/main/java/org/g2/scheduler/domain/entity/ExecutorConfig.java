package org.g2.scheduler.domain.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.g2.scheduler.infra.constants.SchedulerConstants;
import org.g2.core.helper.FastJsonHelper;
import org.g2.starter.redis.client.RedisCacheClient;

/**
 * 执行器配置,表g2_scheduler.gsdr_executor_config
 *
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
@Data
public class ExecutorConfig {

    public static final String FIELD_CONFIG_ID = "configId";
    public static final String FIELD_EXECUTOR_ID = "executorId";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_MAX_CONCURRENT = "maxConcurrent";
    public static final String FIELD_WEIGHT = "weight";

    /**
     * 执行器配置主键
     */
    private Long configId;
    /**
     * 执行器id
     */
    private Long executorId;
    /**
     * 执行器地址
     */
    private String address;
    /**
     * 执行器最大并发任务量
     */
    private int maxConcurrent;
    /**
     * 执行器权重
     */
    private int weight;

    private static String getCacheKey(Long executorId) {
        return String.format(SchedulerConstants.Cache.Key.EXECUTOR_CONFIG, executorId);
    }

    public static List<String> getCache(RedisCacheClient redisCacheClient, Long executorId, String address) {
        String json = redisCacheClient.<String, String>opsForHash().get(getCacheKey(executorId), address);
        return StringUtils.isBlank(json) ? new ArrayList<>() : FastJsonHelper.stringConvertCollection(json, String.class);
    }

    public static boolean addExecutorCache(RedisCacheClient redisCacheClient, Long executorId, String address) {
        return true;
    }

}
