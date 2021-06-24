package org.g2.boot.inf.config.spring.scan;

import org.apache.commons.collections4.CollectionUtils;
import org.g2.boot.inf.config.spring.bean.InfClientFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Set;

/**
 * @author wuwenxi 2021-06-24
 */
public class ClassPathInfClientScanner extends ClassPathBeanDefinitionScanner {

    private static final Logger log = LoggerFactory.getLogger(ClassPathInfClientScanner.class);

    private static final String APPLICATION_CONTEXT = "applicationContext";
    private static final String BEAN_CLASS = "beanClass";

    private final ApplicationContext applicationContext;

    public ClassPathInfClientScanner(BeanDefinitionRegistry registry, ApplicationContext applicationContext) {
        super(registry);
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
        if (CollectionUtils.isEmpty(holders)) {
            log.warn("No InfClient was found in {}", Arrays.toString(basePackages));
        } else {
            for (BeanDefinitionHolder holder : holders) {
                GenericBeanDefinition beanDefinition = (GenericBeanDefinition) holder.getBeanDefinition();
                beanDefinition.setBeanClass(InfClientFactoryBean.class);
                beanDefinition.getPropertyValues().add(BEAN_CLASS, beanDefinition.getBeanClassName());
                beanDefinition.getPropertyValues().add(APPLICATION_CONTEXT, applicationContext);
            }
        }
        return holders;
    }
}
