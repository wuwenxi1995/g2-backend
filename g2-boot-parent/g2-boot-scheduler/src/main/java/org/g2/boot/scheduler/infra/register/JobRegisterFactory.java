package org.g2.boot.scheduler.infra.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wenxi.wu@hand-china.com 2020-11-02
 */
public class JobRegisterFactory {

    private static Map<String, Object> jobMap = new ConcurrentHashMap<>();

    private JobRegisterFactory() {
    }

    public static void register(String jobCode, Object object) {
        jobMap.computeIfPresent(jobCode, (oldValue, newValue) -> object);
    }

    public static Object getJobHandler(String jobCode) {
        return jobMap.get(jobCode);
    }
}
