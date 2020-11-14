package org.g2.boot.scheduler.app.server.impl;

import java.util.HashMap;
import java.util.Map;

import org.g2.boot.scheduler.app.server.JobExecuteService;
import org.g2.boot.scheduler.infra.handler.IJobHandler;
import org.g2.boot.scheduler.infra.register.JobRegisterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author wenxi.wu@hand-china.com 2020-11-03
 */
@Service
public class JobExecuteServiceImpl implements JobExecuteService {

    private static final Logger log = LoggerFactory.getLogger(JobExecuteServiceImpl.class);

    @Override
    public void execute(String jobHandlerCode) {
        Object jobHandler = JobRegisterFactory.getJobHandler(jobHandlerCode);
        IJobHandler iJobHandler;
        if (jobHandler == null) {
            log.error("");
        } else {
            try {
                iJobHandler = (IJobHandler) jobHandler;
                doJobExecute(iJobHandler);
            } catch (Exception e) {
                log.error("job execute error : ", e);
                ((IJobHandler) jobHandler).onException();
            }
        }
    }

    private void doJobExecute(IJobHandler iJobHandler) {
        log.debug("job init");
        iJobHandler.onCreate();

        Map<String, String> param = new HashMap<>();
        param.put("test", "test");

        iJobHandler.execute(param);

        iJobHandler.onFinish();
    }
}
