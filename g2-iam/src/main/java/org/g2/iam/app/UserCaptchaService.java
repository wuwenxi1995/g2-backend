package org.g2.iam.app;

import org.g2.core.captcha.CaptchaResult;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-05
 */
public interface UserCaptchaService {

    /**
     * 发送验证码
     *
     * @param crownCode 国际简码
     * @param mobile    手机号
     * @return 验证码结果
     */
    CaptchaResult sendCaptcha(String crownCode, String mobile);
}
