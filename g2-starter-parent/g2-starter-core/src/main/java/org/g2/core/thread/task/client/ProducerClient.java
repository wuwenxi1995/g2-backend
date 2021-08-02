package org.g2.core.thread.task.client;

/**
 * @author wuwenxi 2021-08-02
 */
public interface ProducerClient extends ConsumerClient {

    /**
     * 启动客户端
     */
    void start();
}
