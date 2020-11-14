package org.g2.boot.scheduler.infra.init;

import org.g2.boot.scheduler.infra.handler.IJobHandler;
import org.g2.boot.scheduler.infra.annotation.JobHandler;
import org.g2.boot.scheduler.infra.register.JobRegisterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

/**
 * @author wenxi.wu@hand-china.com 2020-11-02
 */
public class JobExecuteInit implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(JobExecuteInit.class);

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        if (bean instanceof IJobHandler) {
            JobHandler jobHandler = bean.getClass().getAnnotation(JobHandler.class);
            if (jobHandler != null) {
                JobRegisterFactory.register(jobHandler.value(), bean);
            } else {
                log.warn("could not get target bean , jobHandler : {}", bean);
            }
        }
        return bean;
    }
}
