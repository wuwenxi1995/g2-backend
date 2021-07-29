package org.g2.starter.aop.config.spring.register;

import org.apache.commons.lang3.StringUtils;
import org.g2.starter.aop.annotation.EnableInterceptor;
import org.g2.starter.aop.config.spring.processor.InterceptorRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.HashSet;

/**
 * @author wuwenxi 2021-07-29
 */
public class InterceptorRegister implements ImportBeanDefinitionRegistrar {
    private static final String VALUE = "basePackages";
    private static final String BASE_PACKAGE = "basePackages";
    private static final String INTERCEPTOR_REGISTRY_POST_PROCESSOR = "InterceptorRegistryPostProcessor";
    private HashSet<String> basePackages = new HashSet<>(16);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableInterceptor.class.getName()));
        if (annotationAttributes == null) {
            return;
        }

        String[] basePackages = annotationAttributes.getStringArray(VALUE);
        for (String basePackage : basePackages) {
            if (StringUtils.isNotBlank(basePackage)) {
                this.basePackages.add(basePackage);
            }
        }

        if (this.basePackages.isEmpty()) {
            this.basePackages.add(this.getDefaultBasePackage(annotationMetadata));
        }

        // 注入Interceptor注册器
        RootBeanDefinition registerProcessor = new RootBeanDefinition();
        registerProcessor.setBeanClass(InterceptorRegistryPostProcessor.class);
        registerProcessor.getPropertyValues().add(BASE_PACKAGE, this.basePackages.toArray(new String[0]));
        registry.registerBeanDefinition(INTERCEPTOR_REGISTRY_POST_PROCESSOR, registerProcessor);
    }

    private String getDefaultBasePackage(AnnotationMetadata annotationMetadata) {
        return ClassUtils.getPackageName(annotationMetadata.getClassName());
    }
}
