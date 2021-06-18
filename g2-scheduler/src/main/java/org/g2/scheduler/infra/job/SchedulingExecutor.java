package org.g2.scheduler.infra.job;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wenxi.wu@hand-chian.com 2020-12-08
 */
@Component
public class SchedulingExecutor implements SchedulingConfigurer {

    @Override
    public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
    }
}
