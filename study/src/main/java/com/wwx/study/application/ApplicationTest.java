package com.wwx.study.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author wuwenxi 2022-05-11
 */
@Component
@Slf4j
public class ApplicationTest implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ApplicationProperties properties = event.getApplicationContext().getBean(ApplicationProperties.class);
        log.info("properties : {}", properties.getTimeout().getSeconds());
    }
}
