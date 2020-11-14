package org.g2.boot.scheduler.app.server;

/**
 * @author wenxi.wu@hand-china.com 2020-11-03
 */
public interface JobExecuteService {

    void execute(String jobHandlerCode);
}
