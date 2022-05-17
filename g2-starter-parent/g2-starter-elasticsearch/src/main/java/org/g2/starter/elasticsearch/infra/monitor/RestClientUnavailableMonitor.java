package org.g2.starter.elasticsearch.infra.monitor;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.g2.core.task.TaskHandler;
import org.g2.core.thread.scheduler.ScheduledTask;
import org.g2.core.util.StringUtil;
import org.g2.core.util.ThreadFactoryBuilder;
import org.g2.starter.elasticsearch.config.properties.ElasticsearchProperties;
import org.g2.starter.elasticsearch.infra.exception.RestClientUnavailableException;
import org.g2.starter.elasticsearch.infra.factory.RestClientBuildFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
@Component
public class RestClientUnavailableMonitor extends TaskHandler {

    private static final Logger log = LoggerFactory.getLogger(RestClientUnavailableMonitor.class);

    private ScheduledThreadPoolExecutor scheduled;
    private ThreadPoolExecutor executor;
    private ScheduledTask task;

    private final RestClientBuildFactory restClientBuildFactory;
    private final ElasticsearchProperties.Monitor monitor;

    public RestClientUnavailableMonitor(RestClientBuildFactory restClientBuildFactory) {
        this.restClientBuildFactory = restClientBuildFactory;
        this.monitor = restClientBuildFactory.getProperties().getMonitor();
    }

    @Override
    protected void run() {
        scheduled.schedule(task, 0, TimeUnit.NANOSECONDS);
    }

    @Override
    protected void doStart() {
        this.scheduled = new ScheduledThreadPoolExecutor(1,
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("scheduledMonitor-d%").build());
        this.executor = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 0L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(999),
                new ThreadFactoryBuilder().setNameFormat("restClientMonitor-d%").build());
        this.task = new ScheduledTask("restClientMonitor", scheduled, executor, this::doTask,
                monitor.getRenewalIntervalSeconds().intValue(), TimeUnit.SECONDS, false);
    }

    @Override
    protected void doStop() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
        if (!scheduled.isShutdown()) {
            executor.shutdown();
        }
        task.cancel();
        while (true) {
            if (executor.getActiveCount() == 0) {
                break;
            }
        }
    }

    private void doTask() {
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
                doTask();
            } else {
                log.warn("this elasticsearch cluster undefine error : {}", StringUtil.exceptionString(e));
            }
        }
    }
}
