package org.g2.core.captcha.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-04
 */
@ConfigurationProperties(prefix = "g2.captcha")
public class CaptchaProperties {
    public static final String PREFIX = "g2.captcha";

    private boolean isEnable = false;

    private Sms sms = new Sms();

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public Sms getSms() {
        return sms;
    }

    public void setSms(Sms sms) {
        this.sms = sms;
    }

    public static class Sms {
        /**
         * 验证码过期时间(分)
         */
        private Integer expire = 5;
        /**
         * 验证码字符
         */
        private String source = "0123456789";
        /**
         * 验证码长度
         */
        private Integer length = 6;
        /**
         * 验证码发送间隔时间(秒)
         */
        private Integer interval = 60;
        /**
         * 限制次数
         */
        private Integer limitTime = 5;
        /**
         * 次数限制的间隔时间(时)
         */
        private Integer limitInterval = 12;
        /**
         * 最大错误次数
         */
        private Integer maxErrorTime = 5;

        public Integer getExpire() {
            return expire;
        }

        public void setExpire(Integer expire) {
            this.expire = expire;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public Integer getInterval() {
            return interval;
        }

        public void setInterval(Integer interval) {
            this.interval = interval;
        }

        public Integer getLimitTime() {
            return limitTime;
        }

        public void setLimitTime(Integer limitTime) {
            this.limitTime = limitTime;
        }

        public Integer getLimitInterval() {
            return limitInterval;
        }

        public void setLimitInterval(Integer limitInterval) {
            this.limitInterval = limitInterval;
        }

        public Integer getMaxErrorTime() {
            return maxErrorTime;
        }

        public void setMaxErrorTime(Integer maxErrorTime) {
            this.maxErrorTime = maxErrorTime;
        }
    }
}
