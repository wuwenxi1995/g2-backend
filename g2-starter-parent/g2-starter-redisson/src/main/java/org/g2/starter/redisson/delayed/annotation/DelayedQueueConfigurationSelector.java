package org.g2.starter.redisson.delayed.annotation;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author wuwenxi 2021-12-27
 */
public class DelayedQueueConfigurationSelector implements DeferredImportSelector {

    @Override
    @NonNull
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {
        return new String[]{DelayedQueueBootstrapConfiguration.class.getSimpleName()};
    }
}
