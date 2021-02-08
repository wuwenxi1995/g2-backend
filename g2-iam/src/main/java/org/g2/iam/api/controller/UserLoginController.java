package org.g2.iam.api.controller;

import org.g2.core.captcha.CaptchaResult;
import org.g2.core.util.Results;
import org.g2.iam.app.UserCaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-04
 */
@RestController("userLoginController.v1")
@RequestMapping("/v1/user-login")
public class UserLoginController {

    private final UserCaptchaService userCaptchaService;

    public UserLoginController(UserCaptchaService userCaptchaService) {
        this.userCaptchaService = userCaptchaService;
    }

    @GetMapping({"/send-captcha"})
    public ResponseEntity<?> sendCaptcha(@RequestParam(defaultValue = "+86") String internationalTelCode, @RequestParam final String phone) {
        CaptchaResult captchaResult = userCaptchaService.sendCaptcha(internationalTelCode, phone);
        Map<String, Object> result = new HashMap<>(16);
        result.put("captchaKey", captchaResult.getCaptchaKey());
        result.put("interval", captchaResult.getInterval());
        result.put("message", captchaResult.getMessage());
        return Results.success(result);
    }
}
