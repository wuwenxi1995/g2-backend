package org.g2.boot.scheduler.infra.handler;

import java.util.Map;

/**
 * @author wenxi.wu@hand-china.com 2020-11-05
 */
public abstract class IJobLockHandler implements IJobHandler {

    /**
     * 定时任务执行入口
     *
     * @param param 执行参数
     */
    @Override
    public abstract void execute(Map<String, String> param);

    @Override
    public void onCreate() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onException() {

    }
}
