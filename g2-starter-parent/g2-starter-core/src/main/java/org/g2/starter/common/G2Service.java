package org.g2.starter.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
public final class G2Service {


    //
    //                              基础服务
    // ==========================================================================


    //
    //                              平台服务
    // ==========================================================================

    @Component
    public static class G2Scheduler {
        public static final String NAME = "${g2.service.scheduler.name:g2-scheduler}";
        public static final String CODE = "gsdr";
        public static Integer PORT = 8100;
        public static Integer REDIS_DB = 1;

        @Value("${g2.service.scheduler.port:8100}")
        public void setPort(int port) {
            PORT = port;
        }

        @Value("${g2.service.scheduler.redis-db:1}")
        public void setRedisDb(int redisDb) {
            REDIS_DB = redisDb;
        }
    }
}
