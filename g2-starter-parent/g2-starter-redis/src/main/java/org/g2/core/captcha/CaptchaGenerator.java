package org.g2.core.captcha;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-05
 */
public class CaptchaGenerator {

    /**
     * 生成验证码
     *
     * @param count  验证码长度
     * @param source 验证码内容
     * @return 验证码
     */
    public static String generateNumberCaptcha(int count, char[] source) {
        return RandomStringUtils.random(count, 0, source.length - 1, false, true, source);
    }

    /**
     * 随机生成验证码key
     *
     * @return 验证码key，用于校验验证码
     */
    public static String generateCaptchaKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
