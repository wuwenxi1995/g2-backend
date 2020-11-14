package org.g2.scheduler.infra.constants;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public interface SchedulerConstants {

    interface ExecutorStatue {
        // 在线状态
        String ONLINE = "ONLINE";
        // 自动下线
        String OFFLINE = "OFFLINE";
    }

    interface ExecutorType {
        int AUTO = 0;
        int MANUAL = 1;
    }

    interface ExecutorStrategy {
        // 轮询
        String ROUND = "ROUND";
        // 随机
        String RANDOM = "RANDOM";
        // 权重
        String WEIGHT = "WEIGHT";
        // 任务权重
        String JOB_WEIGHT = "JOB_WEIGHT";
    }

    interface Cache {
        /**
         * redis key
         */
        interface Key {
            String ROUND_INDEX = "scheduler:round:%s:%S";

            String EXECUTOR_CONFIG = "scheduler:executor-config:%s";
        }

        /**
         * 脚本
         */
        interface RedisScript {
            ResourceScriptSource ROUND_INDEX = new ResourceScriptSource(new ClassPathResource("script/lua/round_index.lua"));
        }
    }
}
