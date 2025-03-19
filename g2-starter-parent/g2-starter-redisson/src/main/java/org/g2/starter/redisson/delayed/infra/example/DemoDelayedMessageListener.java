package org.g2.starter.redisson.delayed.infra.example;

import lombok.extern.slf4j.Slf4j;
import org.g2.starter.redisson.delayed.infra.listener.DelayedMessageListener;

/**
 * @author wuwenxi 2022-05-11
 */
// @Component
@Slf4j
public class DemoDelayedMessageListener implements DelayedMessageListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("messageListener onMessage:{}", message);
    }

    @Override
    public String queue() {
        return "demoDelayedMessageListener";
    }
}
