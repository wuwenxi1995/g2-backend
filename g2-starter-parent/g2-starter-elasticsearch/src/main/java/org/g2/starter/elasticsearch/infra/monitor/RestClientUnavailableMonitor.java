package org.g2.starter.elasticsearch.infra.monitor;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.g2.core.util.StringUtil;
import org.g2.core.util.ThreadFactoryBuilder;
import org.g2.starter.elasticsearch.config.ElasticsearchProperties;
import org.g2.starter.elasticsearch.config.spring.builder.RestClientBuildFactory;
import org.g2.starter.elasticsearch.infra.exception.RestClientUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.TimerTask;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * restHighLevelClient 状态监测
 * <p>
 * httpClient 在高并发下容易产生I/O异常，导致服务与es连接断开
 * {@link RestClientUnavailableMonitor}主要监测服务与es连接状态，
 * 如果发生异常将重新构建连接{@link RestClientBuildFactory#restClientSelfHealing()}
 * </p>
 *
 * @author wuwenxi 2021-05-29
 */
public class RestClientUnavailableMonitor implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(RestClientUnavailableMonitor.class);

    private ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1,
            new ThreadFactoryBuilder()
                    .setDaemon(true)
                    .setNameFormat("scheduledMonitor-d%").build());

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 0L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            new ThreadFactoryBuilder().setNameFormat("restClientMonitor-d%").build());

    private final RestClientBuildFactory restClientBuildFactory;
    private final ElasticsearchProperties.Monitor monitor;

    public RestClientUnavailableMonitor(RestClientBuildFactory restClientBuildFactory) {
        this.restClientBuildFactory = restClientBuildFactory;
        this.monitor = restClientBuildFactory.getProperties().getMonitor();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        RestClientSchedulerMonitor restClientSchedulerMonitor = new RestClientSchedulerMonitor(scheduled,
                executor,
                new MonitorTask(restClientBuildFactory),
                monitor.getRenewalIntervalSeconds(),
                TimeUnit.SECONDS);
        scheduled.schedule(restClientSchedulerMonitor, monitor.getRenewalIntervalSeconds(), TimeUnit.SECONDS);
    }

    private void shutdown() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
        if (!scheduled.isShutdown()) {
            executor.shutdown();
        }
    }

    /**
     * restClient 状态监控线程
     */
    private static class MonitorTask implements Runnable {

        private final RestClientBuildFactory restClientBuildFactory;

        private MonitorTask(RestClientBuildFactory restClientBuildFactory) {
            this.restClientBuildFactory = restClientBuildFactory;
        }

        @Override
        public void run() {
            RestHighLevelClient restHighLevelClient = restClientBuildFactory.restHighLevelClient();
            try {
                ClusterHealthResponse health = restHighLevelClient.cluster().health(Requests.clusterHealthRequest(), RequestOptions.DEFAULT);
                log.info("the elasticsearch cluster get num of node : {} , and cluster health : {}", health.getNumberOfNodes(), health.getStatus());
            } catch (Exception e) {
                if (e instanceof NoNodeAvailableException) {
                    log.warn("the elasticsearch cluster all node unavailable, msg : {}", StringUtil.exceptionString(e));
                } else if (e instanceof RestClientUnavailableException) {
                    log.warn("rest client unavailable , msg : {}", StringUtil.exceptionString(e));
                    // 重置restClient
                    restClientBuildFactory.restClientSelfHealing();
                    // 重新监测
                    run();
                } else {
                    log.warn("this elasticsearch cluster undefine error : {}", StringUtil.exceptionString(e));
                }
            }
        }
    }

    /**
     * restClient 状态监控调度器
     */
    private static class RestClientSchedulerMonitor extends TimerTask {

        private final ScheduledThreadPoolExecutor scheduled;
        private final ThreadPoolExecutor executor;
        private final Runnable task;
        private final long delay;
        private final TimeUnit timeUnit;

        private RestClientSchedulerMonitor(ScheduledThreadPoolExecutor scheduled, ThreadPoolExecutor executor, Runnable task, long delay, TimeUnit timeUnit) {
            this.scheduled = scheduled;
            this.executor = executor;
            this.task = task;
            this.delay = delay;
            this.timeUnit = timeUnit;
        }

        @Override
        public void run() {
            Future<?> future;
            try {
                future = executor.submit(task);
                future.get();
            } catch (Exception e) {
                log.error("the elasticsearch scheduled monitor has error : {}", StringUtil.exceptionString(e));
            } finally {
                if (!scheduled.isShutdown()) {
                    scheduled.schedule(this, delay, timeUnit);
                }
            }
        }
    }
}
