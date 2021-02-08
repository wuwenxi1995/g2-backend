package org.g2.core.captcha;

import lombok.Data;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-04
 */
@Data
public class CaptchaResult {

    private boolean success;
    private String code;
    private String message;
    private String captcha;
    private String captchaKey;
    private String lastCheckKey;
    private long interval;
    private int errorTimes;
    private int surplusTimes;


    public void clear() {
        this.captcha = null;
    }

}
