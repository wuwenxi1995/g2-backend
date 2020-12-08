package org.g2.scheduler.infra.init;

import org.g2.scheduler.app.service.JobInfoService;
import org.g2.scheduler.domain.entity.JobInfo;
import org.g2.scheduler.domain.service.IJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * @author wenxi.wu@hand-china.com 2020-11-05
 */
public class ExecutorServiceInitJob implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ExecutorServiceInitJob.class);

    @Autowired
    private JobInfoService jobInfoService;

    @Override
    public void run(String... args) throws Exception {
        log.info("init job start ...");
        jobInfoService.initJob();
    }
}
