package org.g2.inv.trigger.config;

import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.g2.inv.trigger.app.service.InvCalculateTriggerService;
import org.g2.inv.trigger.app.service.impl.InvCalculateTriggerServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2022-04-08
 */
@Configuration
public class InventoryTriggerAutoConfiguration {

    @Bean
    @ConditionalOnBean(DynamicRedisHelper.class)
    public InvCalculateTriggerService invCalculateTriggerService(DynamicRedisHelper redisHelper) {
        return new InvCalculateTriggerServiceImpl(redisHelper);
    }
}
