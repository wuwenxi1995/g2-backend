package org.g2.starter.mq.config.spring.register;

import org.apache.commons.lang3.StringUtils;
import org.g2.starter.mq.EnableMq;
import org.g2.starter.mq.listener.config.ListenerProcessor;
import org.g2.starter.mq.config.spring.processor.MqBeanDefinitionRegisterProcessor;
import org.g2.starter.mq.subject.config.SubjectProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.HashSet;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-17
 */
public class MqProcessorRegister implements ImportBeanDefinitionRegistrar {

    private static final String VALUE = "basePackages";
    private static final String INIT_MQ_PROCESSOR = "initMqProcessor";
    private static final String LISTENER_PROCESSOR = "listenerProcessor";

    private HashSet<String> basePackages = new HashSet<>(16);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableMq.class.getName()));
        if (annotationAttributes != null) {
            String[] basePackages = annotationAttributes.getStringArray(VALUE);
            for (String basePackage : basePackages) {
                if (StringUtils.isNotBlank(basePackage)) {
                    this.basePackages.add(basePackage);
                }
            }

            if (this.basePackages.isEmpty()) {
                this.basePackages.add(getDefaultBasePackage(annotationMetadata));
            }

            RootBeanDefinition definition = new RootBeanDefinition();
            definition.setBeanClass(MqBeanDefinitionRegisterProcessor.class);
            definition.getPropertyValues().add("basePackages", this.basePackages.toArray(new String[0]));
            registry.registerBeanDefinition(INIT_MQ_PROCESSOR, definition);

            RootBeanDefinition listenerProcessor = new RootBeanDefinition();
            listenerProcessor.setBeanClass(ListenerProcessor.class);
            registry.registerBeanDefinition(LISTENER_PROCESSOR, listenerProcessor);

            RootBeanDefinition subjectProcessor = new RootBeanDefinition();
            listenerProcessor.setBeanClass(SubjectProcessor.class);
            registry.registerBeanDefinition(LISTENER_PROCESSOR, subjectProcessor);
        }
    }

    private String getDefaultBasePackage(AnnotationMetadata annotationMetadata) {
        return ClassUtils.getPackageName(annotationMetadata.getClassName());
    }
}
