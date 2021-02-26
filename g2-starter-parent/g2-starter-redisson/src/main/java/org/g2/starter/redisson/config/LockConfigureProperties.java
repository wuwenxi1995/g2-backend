package org.g2.starter.redisson.config;

import lombok.Getter;
import lombok.Setter;
import org.g2.starter.redisson.infra.constants.LockConstants;
import org.g2.starter.redisson.infra.enums.ServerPattern;
import org.redisson.config.TransportMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * 配置参考redisson官方文档 https://github.com/redisson/redisson/wiki/2.-Configuration
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
@ConfigurationProperties(prefix = "g2.lock")
@Getter
@Setter
public class LockConfigureProperties {

    /**
     * redis模式
     *
     * @see ServerPattern
     */
    private String pattern;
    private String clientName;
    /**
     * 所有redis节点共享线程数量，默认值: 当前处理核数量 * 2
     */
    private int threads;
    /**
     * 一个Redisson实例中创建的共享线程池里保存的线程数量，默认值：当前处理核数量 * 2
     */
    private int nettyThreads;
    /**
     * 默认值：NIO
     * 可选参数：
     * NIO,
     * EPOLL - 需要依赖里有netty-transport-native-epoll包（Linux）
     * KQUEUE - 需要依赖里有 netty-transport-native-kqueue包（macOS）
     */
    private String transportMode;
    /**
     * 监控锁的看门狗超时，该参数只适用于分布式锁的加锁请求中未明确使用leaseTimeout参数的情况，单位毫秒
     */
    private long lockWatchdogTimeout;
    /**
     * 默认true，通过该参数来修改是否按订阅发布消息的接收顺序出来消息，如果选否将对消息实行并行处理，该参数只适用于订阅发布消息的情况
     */
    private boolean keepPubSubOrder;
    /**
     * 默认false，是否启用脚本缓存
     */
    private boolean useScriptCache;

    /**
     * 开启SSL终端识别能力 默认开启
     */
    private boolean sslEnableEndpointIdentification;
    /**
     * 确定采用哪种方式（JDK或OPENSSL）来实现SSL连接。默认采用JDK
     */
    private String sslProvider;
    /**
     * 指定SSL信任证书库的路径
     */
    private String sslTruststore;
    /**
     * 指定SSL信任证书库的密码
     */
    private String sslTruststorePassword;
    /**
     * 指定SSL钥匙库的路径
     */
    private String sslKeystore;
    /**
     * 指定SSL钥匙库的密码
     */
    private String sslKeystorePassword;
    /**
     * 基本时间配置信息
     */
    private Property property;
    /**
     * 单节点配置
     */
    private SingleConfig singleConfig;
    /**
     * 集群配置
     */
    private ClusterConfig clusterConfig;
    /**
     * 哨兵配置
     */
    private SentinelConfig sentinelConfig;
    /**
     * 主从配置
     */
    private MasterSlaveConfig masterSlaveConfig;
    /**
     * 云托管配置
     */
    private ReplicatedConfig replicatedConfig;

    public LockConfigureProperties() {
        this.pattern = ServerPattern.SINGLE.getPattern();
        this.transportMode = LockConstants.TransportMode.NIO;
        this.clientName = LockConstants.LOCK_CLIENT_NAME;
        this.sslEnableEndpointIdentification = true;
        this.sslProvider = LockConstants.JDK;
        this.threads = 0;
        this.nettyThreads = 0;
        this.lockWatchdogTimeout = 30000L;
        this.keepPubSubOrder = true;
        this.useScriptCache = false;
        this.property = new Property();
    }

    public static class Property {
        private long waitTime = 60L;
        private long leaseTime = 60L;
        private TimeUnit timeUnit;

        private Property() {
            timeUnit = TimeUnit.SECONDS;
        }

        public long getWaitTime() {
            return waitTime;
        }

        public void setWaitTime(long waitTime) {
            this.waitTime = waitTime;
        }

        public long getLeaseTime() {
            return leaseTime;
        }

        public void setLeaseTime(long leaseTime) {
            this.leaseTime = leaseTime;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }
    }

    //
    //                            redis模式配置
    // ===================================================================

    @Setter
    @Getter
    public static class SingleConfig {
        // 节点地址
        private String address;
        // 节点端口
        private long port;
        // 发布和订阅连接的最小空闲连接数
        private int subConnMinIdleSize = 1;
        // 发布和订阅连接池大小
        private long subConnPoolSize = 50;
        // 最小空闲连接数
        private int connMinIdleSize = 32;
        // 连接池大小
        private int connPoolSize = 64;
        // 是否启用DNS监测
        private boolean dnsMonitoring = false;
        // DNS监测时间间隔，单位：毫秒，该配置需要dnsMonitoring设为true
        private long dnsMonitoringInterval = 5000;
        // 连接空闲超时，单位：毫秒
        private long idleConnTimeout = 10000;
        private boolean keepAlive = false;
        // 连接超时，单位：毫秒
        private long connTimeout = 10000;
        // 命令等待超时，单位：毫秒
        private long timeout = 3000;
        // 命令失败重试次数 如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时，将抛出错误。如果尝试在此限制之内发送成功，则开始启用timeout（命令等待超时） 计时。
        private int retryAttempts = 3;
        // 命令重试发送时间间隔，单位：毫秒
        private long retryInterval = 1500;
        // 数据库编号
        private int database = 0;
        // 密码
        private String password;
        // 单个连接最大订阅数量
        private int subPerConn = 5;
    }

    /**
     * 集群模式
     */
    @Getter
    @Setter
    public static class ClusterConfig extends BaseConfig {
        // 集群节点地址，可以通过host:port的格式来添加Redis集群节点的地址
        private String nodeAddresses;
        // 集群扫描间隔时间
        private long scanInterval = 1000;
    }

    @Getter
    @Setter
    public static class SentinelConfig extends BaseConfig {
        // DNS监控间隔，单位：毫秒；用-1来禁用该功能。
        private long dnsMonitoringInterval = 5000;
        // 主服务器的名称
        private String masterName;
        // 哨兵节点地址，多个节点可以一次性批量添加。
        private String sentinelAddresses;
        // 数据库编号
        private int database = 0;
    }

    @Getter
    @Setter
    public static class MasterSlaveConfig extends BaseConfig {
        // DNS监控间隔，单位:毫秒
        private long dnsMonitoringInterval = 5000;
        // 主节点地址，可以通过host:port的格式来指定主节点地址。
        private String masterAddress;
        // 从节点地址
        private String slaveAddresses;
        // 重新连接时间间隔，单位：毫秒
        private int reconnectionTimeout = 3000;
        // 执行失败最大次数
        private int failedAttempts = 3;
        // 数据库编号
        private int database = 0;
    }

    /**
     * 云托管模式适用于任何由云计算运营商提供的Redis云服务
     */
    @Getter
    @Setter
    public static class ReplicatedConfig extends BaseConfig {
        // 集群节点地址
        private String nodeAddresses;
        // 主节点变化扫描间隔时间
        private int scanInterval = 1000;
        // DNS监控间隔，单位：毫秒；用-1来禁用该功能
        private long dnsMonitoringInterval = 5000L;
        // 数据库编号
        private int database = 0;
    }

    @Getter
    @Setter
    public static class BaseConfig {
        // 读取操作的负载均衡模式 默认值： SLAVE（只在从服务节点里读取）
        // 在从服务节点里读取的数据说明已经至少有两个节点保存了该数据，确保了数据的高可用性
        private String readMode = LockConstants.SubReadMode.SLAVE;
        // 订阅操作的负载均衡模式 默认值：SLAVE（只在从服务节点里订阅）
        private String subscriptionMode = LockConstants.SubReadMode.SLAVE;
        // 负载均衡算法类的选择，默认：轮询调度算法
        private String loadBalancer = LockConstants.LoadBalancer.ROUND_ROBIN_LOAD_BALANCER;
        // 默认权重值，当负载均衡算法是权重轮询调度算法时该属性有效
        private int defaultWeight = 0;
        // 权重值设置，格式为 host1:port1;host2:port2 当负载均衡算法是权重轮询调度算法时该属性有效
        private String weightMaps;
        // 从节点发布和订阅连接的最小空闲连接数
        private int subscriptionConnectionMinimumIdleSize = 1;
        // 从节点发布和订阅连接池大小
        private int subscriptionConnectionPoolSize = 50;
        // 从节点最小空闲连接数
        private int slaveConnectionMinimumIdleSize = 32;
        // 从节点连接池大小
        private int slaveConnectionPoolSize = 64;
        // 主节点最小空闲连接数
        private int masterConnectionMinimumIdleSize = 32;
        // 主节点连接池大小
        private int masterConnectionPoolSize = 64;
        // 连接空闲超时，单位：毫秒
        private long idleConnectionTimeout = 10000;
        // 连接超时，单位：毫秒
        private long connectTimeout = 10000;
        // 命令等待超时，单位：毫秒
        private long timeout = 3000;
        // 命令失败重试次数
        private int retryAttempts = 3;
        // 命令重试发送时间间隔，单位：毫秒
        private int retryInterval = 1500;
        // 密码，用于节点身份验证的密码
        private String password;
        // 单个连接最大订阅数量
        private int subscriptionsPerConnection = 5;
    }
}
