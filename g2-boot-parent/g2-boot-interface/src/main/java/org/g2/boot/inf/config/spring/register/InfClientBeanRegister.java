package org.g2.boot.inf.config.spring.register;

import org.apache.commons.lang3.StringUtils;
import org.g2.boot.inf.annotation.EnableInfClient;
import org.g2.boot.inf.config.spring.InfClientBeanProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.HashSet;

/**
 * @author wuwenxi 2021-06-24
 */
public class InfClientBeanRegister implements ImportBeanDefinitionRegistrar {

    private static final String VALUE = "basePackages";
    private static final String INF_CLIENT_BEAN_PROCESSOR = "infClientBeanProcessor";

    private HashSet<String> basePackages = new HashSet<>(16);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableInfClient.class.getName()));
        if (annotationAttributes != null) {
            String[] basePackages = annotationAttributes.getStringArray(VALUE);
            for (String basePackage : basePackages) {
                if (StringUtils.isNotBlank(basePackage)) {
                    this.basePackages.add(basePackage);
                }
            }

            if (this.basePackages.isEmpty()) {
                this.basePackages.add(getDefaultBasePackage(importingClassMetadata));
            }

            RootBeanDefinition definition = new RootBeanDefinition();
            definition.setBeanClass(InfClientBeanProcessor.class);
            definition.getPropertyValues().add(VALUE, this.basePackages.toArray(new String[0]));
            registry.registerBeanDefinition(INF_CLIENT_BEAN_PROCESSOR, definition);
        }
    }

    private String getDefaultBasePackage(AnnotationMetadata annotationMetadata) {
        return ClassUtils.getPackageName(annotationMetadata.getClassName());
    }
}
