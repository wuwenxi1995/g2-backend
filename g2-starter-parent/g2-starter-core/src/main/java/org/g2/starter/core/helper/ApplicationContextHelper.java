package org.g2.starter.core.helper;

import org.g2.starter.core.util.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wenxi.wu@hand-china.com 2020-11-05
 */
public class ApplicationContextHelper implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(ApplicationContextHelper.class);

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * 异步从IOC容器中获取bean设置到目标对象中，在某些启动期间需要初始化的bean可采用此方法
     * 适用于实例方法注入
     *
     * @param clazz        需要设置的对象
     * @param target       目标对象
     * @param setterMethod 目标对象中的setter方法
     */
    public static void asyncInstanceSetter(Class<?> clazz, Object target, String setterMethod) {
        if (ApplicationContextHelper.context != null) {
            try {
                Method method = target.getClass().getDeclaredMethod(setterMethod, clazz);
                method.setAccessible(true);
                method.invoke(target, ApplicationContextHelper.context.getBean(clazz));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setDaemon(true).setNameFormat("async-setter").build());
            executorService.scheduleAtFixedRate(() -> {
                if (ApplicationContextHelper.context != null) {
                    try {
                        Method method = target.getClass().getDeclaredMethod(setterMethod, clazz);
                        method.setAccessible(true);
                        method.invoke(target, ApplicationContextHelper.context.getBean(clazz));
                    } catch (Exception e) {
                        log.error("ApplicationContextHelper setter {} to {} failure.", clazz.getName(), target.getClass().getName());
                    }
                    executorService.shutdown();
                }
            }, 0, 200, TimeUnit.MILLISECONDS);
        }
        log.info("ApplicationContextHelper asyncInstanceSetter setter {} to {} success.", clazz.getName(), target.getClass().getName());
    }

    /**
     * 异步从IOC容器中获取bean设置到目标对象中，在某些启动期间需要初始化的bean可采用此方法
     * 适用于静态方法注入
     *
     * @param clazz        需要设置的对象
     * @param target       目标对象
     * @param setterMethod 目标对象中的setter方法
     */
    public static void asyncStaticSetter(Class<?> clazz, Class<?> target, String setterMethod) {
        if (ApplicationContextHelper.context != null) {
            try {
                Method method = target.getDeclaredMethod(setterMethod, clazz);
                method.setAccessible(true);
                method.invoke(null, ApplicationContextHelper.context.getBean(clazz));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setDaemon(true).setNameFormat("async-setter").build());
            executorService.scheduleAtFixedRate(() -> {
                if (ApplicationContextHelper.context != null) {
                    try {
                        Method method = target.getDeclaredMethod(setterMethod, clazz);
                        method.setAccessible(true);
                        method.invoke(null, ApplicationContextHelper.context.getBean(clazz));
                    } catch (Exception e) {
                        log.error("ApplicationContextHelper asyncStaticSetter setter {} to {} failure.", clazz.getName(), target.getName());
                    }
                    executorService.shutdown();
                }
            }, 0, 200, TimeUnit.MILLISECONDS);
        }
        log.info("ApplicationContextHelper asyncStaticSetter setter {} to {} success.", clazz.getName(), target.getName());
    }
}
