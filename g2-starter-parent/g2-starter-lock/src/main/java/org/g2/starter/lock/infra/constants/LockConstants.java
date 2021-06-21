package org.g2.starter.lock.infra.constants;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public interface LockConstants {

    String LOCK_CLIENT_NAME = "Lock";
    String REDIS_URL_PREFIX = "redis://";
    String JDK = "JDK";
    String ZK_LOCK_NAME_SPACE = "/mylock";

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

    interface TransportMode {
        String NIO = "NIO";
        String EPOLL = "EPOLL";
        String KQUEUE = "KQUEUE";

        /**
         * 获取传输模式
         *
         * @param mode 传输模式
         * @return TransportMode
         */
        static org.redisson.config.TransportMode getTransportMode(String mode) {
            switch (mode) {
                case EPOLL:
                    return org.redisson.config.TransportMode.EPOLL;
                case KQUEUE:
                    return org.redisson.config.TransportMode.KQUEUE;
                default:
                    return org.redisson.config.TransportMode.NIO;
            }
        }
    }

}
