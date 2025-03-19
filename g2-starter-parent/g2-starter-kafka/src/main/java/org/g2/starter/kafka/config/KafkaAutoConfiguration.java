package org.g2.starter.kafka.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.transaction.KafkaTransactionManager;

/**
 * @author wuwenxi 2022-02-28
 */
@Configuration
@EnableConfigurationProperties(value = {KafkaProperties.class})
public class KafkaAutoConfiguration {

    private final KafkaProperties properties;

    public KafkaAutoConfiguration(KafkaProperties kafkaProperties) {
        this.properties = kafkaProperties;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> kafkaProducerFactory,
                                                       ProducerListener<String, String> kafkaProducerListener,
                                                       ObjectProvider<RecordMessageConverter> messageConverter) {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        return kafkaTemplate;
    }

    @Bean
    public ProducerListener<String, String> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }

    @Bean
    public ProducerFactory<String, String> kafkaProducerFactory(ObjectProvider<DefaultKafkaProducerFactoryCustomizer> customizers) {
        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(this.properties.buildProducerProperties());
        String transactionIdPrefix = this.properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        customizers.orderedStream().forEach((customizer) -> {
            customizer.customize(factory);
        });
        return factory;
    }

    @Bean
    @ConditionalOnProperty(
            name = {"spring.kafka.producer.transaction-id-prefix"}
    )
    public KafkaTransactionManager<String, String> kafkaTransactionManager(ProducerFactory<String, String> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    @Bean
    public ConsumerFactory<String, String> kafkaConsumerFactory(ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers) {
        DefaultKafkaConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(this.properties.buildConsumerProperties());
        customizers.orderedStream().forEach((customizer) -> {
            customizer.customize(factory);
        });
        return factory;
    }

}
