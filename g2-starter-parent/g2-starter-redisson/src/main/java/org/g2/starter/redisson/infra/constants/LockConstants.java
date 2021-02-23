package org.g2.starter.redisson.infra.constants;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public interface LockConstants {

    String LOCK_CLIENT_NAME = "Lock";
    String REDIS_URL_PREFIX = "redis://";
    String JDK = "JDK";

    interface SubReadMode {
        String SLAVE = "SLAVE";
        String MASTER = "MASTER";
        String MASTER_SLAVE = "MASTER_SLAVE";
    }

    interface LoadBalancer {
        String RANDOM_LOAD_BALANCER = "RandomLoadBalancer";
        String ROUND_ROBIN_LOAD_BALANCER = "RoundRobinLoadBalancer";
        String WEIGHTED_ROUND_ROBIN_BALANCER = "WeightedRoundRobinBalancer";
    }

}
