package org.g2.boot.scheduler.infra.handler;

import java.util.Map;

/**
 * 调度任务执行器
 *
 * @author wenxi.wu@hand-china.com 2020-11-02
 */
public interface IJobHandler {

    /**
     * 调度任务执行入口
     *
     * @param param 执行参数
     */
    void execute(Map<String, String> param);

    default void onCreate() {
    }

    default void onFinish() {
    }

    default void onException() {
    }
}
