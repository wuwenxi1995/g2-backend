package org.g2.iam.app.impl;

import org.g2.boot.message.service.client.MessageClient;
import org.g2.core.captcha.CaptchaMessageHandler;
import org.g2.core.captcha.CaptchaResult;
import org.g2.starter.core.user.CustomerType;
import org.g2.starter.core.user.UserType;
import org.g2.iam.app.UserCaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-05
 */
@Service
public class UserCaptchaServiceImpl implements UserCaptchaService {

    private static final Logger log = LoggerFactory.getLogger(UserCaptchaServiceImpl.class);

    private final CaptchaMessageHandler captchaMessageHandler;
    private final MessageClient messageClient;

    public UserCaptchaServiceImpl(CaptchaMessageHandler captchaMessageHandler, MessageClient messageClient) {
        this.captchaMessageHandler = captchaMessageHandler;
        this.messageClient = messageClient;
    }

    @Override
    public CaptchaResult sendCaptcha(String crownCode, String mobile) {
        CaptchaResult captchaResult = captchaMessageHandler.generateMobileCaptcha(crownCode, mobile, UserType.ofDefault(CustomerType.CUSTOMER_TYPE), "giam:");
        if (!captchaResult.isSuccess()) {

        }
        // 发送验证码
        String captcha = captchaResult.getCaptcha();

        captchaResult.clear();
        return captchaResult;
    }
}
