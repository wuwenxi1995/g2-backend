package org.g2.scheduler.domain.entity;

import lombok.Data;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
@Data
public class Executor {

    public static final String FIELD_EXECUTOR_ID = "executorId";
    public static final String FIELD_EXECUTOR_CODE = "executorCode";
    public static final String FIELD_EXECUTOR_NAME = "executorName";
    public static final String FIELD_EXECUTOR_TYPE = "executorType";
    public static final String FIELD_ADDRESS_LIST = "addressList";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_SERVER_NAME = "serverName";

    /**
     * 执行器id，主键
     */
    private Long executorId;
    /**
     * 执行器编码
     */
    private String executorCode;
    /**
     * 执行器名称
     */
    private String executorName;
    /**
     * 执行器类型，0自动注册，1手动注册
     */
    private Integer executorType;
    /**
     * 执行器地址，多个使用逗号分割
     */
    private String addressList;
    /**
     * 执行器状态
     */
    private String status;
    /**
     * 服务名
     */
    private String serverName;
}
