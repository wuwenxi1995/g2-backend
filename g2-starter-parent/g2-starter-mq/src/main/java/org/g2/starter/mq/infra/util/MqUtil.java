package org.g2.starter.mq.infra.util;

import org.g2.core.base.BaseConstants;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-17
 */
public class MqUtil {

    private MqUtil() {
    }

    public static String getBeanName(String prefix, String beanName) {
        return prefix + BaseConstants.Symbol.WELL + beanName;
    }
}
