package org.g2.inv.trigger.config;

import org.g2.inv.trigger.app.service.InvCalculateTriggerService;
import org.g2.inv.trigger.app.service.impl.InvCalculateTriggerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author wuwenxi 2022-04-08
 */
@Configuration
public class InventoryTriggerAutoConfiguration {

    @Bean
    public InvCalculateTriggerService invCalculateTriggerService(KafkaTemplate<String, String> kafkaTemplate) {
        return new InvCalculateTriggerServiceImpl(kafkaTemplate);
    }
}
