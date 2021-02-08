package org.g2.core.captcha;

import cn.hutool.core.util.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.g2.core.base.BaseConstants;
import org.g2.core.user.UserType;
import org.g2.core.captcha.config.properties.CaptchaProperties;
import org.g2.starter.redis.client.RedisCacheClient;

import java.util.concurrent.TimeUnit;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-04
 */
public class CaptchaMessageHandler {

    private static final String CAPTCHA = ":captcha:";
    private static final String USER_TYPE_PREFIX = "user_type_";

    private int expire;
    private char[] source;
    private int length;
    private int interval;
    private int limitTime;
    private int limitInterval;
    private int maxErrorTime;

    private final RedisCacheClient redisCacheClient;

    public CaptchaMessageHandler(CaptchaProperties captchaProperties, RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
        CaptchaProperties.Sms sms = captchaProperties.getSms();
        this.expire = sms.getExpire();
        String source = sms.getSource();
        this.source = (StringUtils.isNotBlank(source) ? source : "0123456789").toCharArray();
        this.length = sms.getLength();
        this.interval = sms.getInterval();
        this.limitTime = sms.getLimitTime();
        this.limitInterval = sms.getLimitInterval();
        this.maxErrorTime = sms.getMaxErrorTime();
    }

    //
    //                                  生成验证码
    // ==========================================================================================

    public CaptchaResult generateMobileCaptcha(String crownCode, String mobile, String captchaCachePrefix) {
        return generateMobileCaptcha(crownCode + BaseConstants.Symbol.MIDDLE_LINE + mobile, captchaCachePrefix);
    }

    public CaptchaResult generateMobileCaptcha(String crownCode, String mobile, UserType userType, String captchaCachePrefix) {
        return this.generateMobileCaptcha(crownCode + BaseConstants.Symbol.MIDDLE_LINE + mobile, userType, captchaCachePrefix);
    }

    public CaptchaResult generateMobileCaptcha(String mobile, String captchaCachePrefix) {
        return this.generateMobileCaptcha(mobile, UserType.ofDefault(), captchaCachePrefix);
    }

    //
    //                                  校验验证码
    // ==========================================================================================

    public CaptchaResult checkCaptcha(String captcha, String captchaKey, UserType userType, String captchaCachePrefix, boolean cacheCheckResult) {
        return this.checkCaptchaWithNumber(captcha, captchaKey, null, userType, captchaCachePrefix, cacheCheckResult);
    }

    public CaptchaResult checkCaptcha(String captcha, String captchaKey, String captchaCachePrefix, boolean cacheCheckResult) {
        return this.checkCaptchaWithNumber(captcha, captchaKey, null, UserType.ofDefault(), captchaCachePrefix, cacheCheckResult);
    }

    public CaptchaResult checkCaptcha(String captcha, String captchaKey, String mobile, UserType userType, String captchaCachePrefix, boolean cacheCheckResult) {
        if (StringUtils.isBlank(mobile) || !PhoneUtil.isPhone(mobile)) {
            CaptchaResult result = new CaptchaResult();
            result.setSuccess(false);
            result.setCode("phone.format.incorrect");
            result.setMessage("请输入正确的手机号");
            return result;
        }
        return this.checkCaptchaWithNumber(captcha, captchaKey, mobile, userType, captchaCachePrefix, cacheCheckResult);
    }

    //
    //                                  私有方法
    // ===========================================================================================

    private CaptchaResult generateMobileCaptcha(String mobile, UserType userType, String captchaCachePrefix) {
        CaptchaResult result = new CaptchaResult();
        String phone = mobile;
        if (mobile.contains(BaseConstants.Symbol.MIDDLE_LINE)) {
            phone = StringUtils.substringAfter(mobile, BaseConstants.Symbol.MIDDLE_LINE);
        }
        if (!PhoneUtil.isPhone(mobile)) {
            result.setSuccess(false);
            result.setCode("phone.format.incorrect");
            result.setMessage("请输入正确的手机号");
            return result;
        }
        if (checkInterval(captchaCachePrefix, userType, phone, result)) {
            result.setSuccess(false);
            return result;
        }
        if (checkLimitTime(captchaCachePrefix, userType, phone)) {
            result.setSuccess(false);
            result.setCode("captcha.send.time-over");
            result.setMessage(format("发送验证码次数到达%s限制", this.limitTime));
            return result;
        }
        String captcha = CaptchaGenerator.generateNumberCaptcha(this.length, this.source);
        String captchaKey = CaptchaGenerator.generateCaptchaKey();
        String value = captcha + BaseConstants.Symbol.LOWER_LINE + phone;
        // 保存验证码
        redisCacheClient.opsForValue().set(getPrefixKey(captchaCachePrefix, userType, "code:") + captchaKey, value, this.expire, TimeUnit.MINUTES);
        // 保存验证码发送时间限制
        redisCacheClient.opsForValue().set(getPrefixKey(captchaCachePrefix, userType, "interval:") + phone, "1", this.interval, TimeUnit.SECONDS);
        if (this.limitTime > 0) {
            // 保存验证码发送次数限制
            redisCacheClient.opsForValue().increment(getPrefixKey(captchaCachePrefix, userType, "times") + phone, 1);
            // 设置验证码次数限制失效时间
            redisCacheClient.expire(getPrefixKey(captchaCachePrefix, userType, "times") + phone, this.limitInterval, TimeUnit.SECONDS);
        }
        result.setSuccess(true);
        result.setCode("captcha.send.phone");
        result.setMessage("验证码已发送");
        result.setCaptcha(captcha);
        result.setCaptchaKey(captchaKey);
        result.setInterval(this.interval);
        return result;
    }

    private CaptchaResult checkCaptchaWithNumber(String captcha, String captchaKey, String mobile, UserType userType, String captchaCachePrefix, boolean cacheCheckResult) {
        CaptchaResult result = new CaptchaResult();

        String key = getPrefixKey(captchaCachePrefix, userType, "code:") + captchaKey;
        String value = redisCacheClient.opsForValue().get(key);
        String[] groupArr = StringUtils.split(value, "_", 2);
        if (groupArr == null) {
            result.setSuccess(false);
            result.setCode("captcha.validate.overdue");
            result.setMessage("验证码已过期");
            return result;
        }
        if (mobile != null && !StringUtils.equals(mobile, groupArr[1])) {
            result.setSuccess(false);
            result.setCode("captcha.validate.number-not-match");
            result.setMessage(format("手机号%s不匹配", mobile));
            return result;
        }
        if (!StringUtils.equals(captcha, groupArr[0])) {
            result.setSuccess(false);
            result.setCode("captcha.validate.captcha-not-match");
            result.setMessage("验证码不匹配");
            checkErrorTime(captcha, captchaCachePrefix, result);
            return result;
        }
        // 删除验证码
        redisCacheClient.delete(key);
        // 删除验证码发送时间限制
        String interval = getPrefixKey(captchaCachePrefix, userType, "interval:") + mobile;
        redisCacheClient.delete(interval);
        if (cacheCheckResult) {
            //
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 校验验证码错误次数
     *
     * @param captcha            验证码
     * @param captchaCachePrefix 前缀
     * @param result             返回结果
     */
    private void checkErrorTime(String captcha, String captchaCachePrefix, CaptchaResult result) {
        String key = getPrefixKey(captchaCachePrefix, null, "error-time:") + captcha;
        String errorTimes = redisCacheClient.opsForValue().get(key);
        int errorTime = 0;
        if (StringUtils.isNotBlank(errorTimes)) {
            errorTime = Integer.parseInt(errorTimes);
        }
        // 如果达到最大错误次数
        if (++errorTime >= this.maxErrorTime) {
            // todo 删除验证码 如果次数到达上限前端做再次发送限制
            // 删除错误次数缓存
            redisCacheClient.delete(key);
            result.setCode("captcha.validate.error.too-many-time");
            result.setMessage("验证码错误次数达到上线");
            return;
        }
        redisCacheClient.opsForValue().set(key, String.valueOf(errorTime), this.expire, TimeUnit.MINUTES);
        result.setErrorTimes(errorTime);
        result.setSurplusTimes(this.maxErrorTime - errorTime);
    }

    /**
     * 校验验证码限制次数
     *
     * @param captchaCachePrefix 验证码缓存前缀
     * @param userType           用户类型
     * @param phone              手机号
     * @return 是否超过验证码发送限制次数
     */
    private boolean checkLimitTime(String captchaCachePrefix, UserType userType, String phone) {
        String key = getPrefixKey(captchaCachePrefix, userType, "times:") + phone;
        String limitTime = redisCacheClient.opsForValue().get(key);
        if (this.limitTime <= 0) {
            return false;
        }
        return StringUtils.isNotBlank(limitTime) && Integer.parseInt(limitTime) >= this.limitTime;
    }

    /**
     * @param captchaCachePrefix 验证码缓存前缀
     * @param userType           用户类型
     * @param phone              手机号
     * @param result             验证码返回信息
     * @return 校验是否存在验证码（避免重复发送）
     */
    private boolean checkInterval(String captchaCachePrefix, UserType userType, String phone, CaptchaResult result) {
        String interval = getPrefixKey(captchaCachePrefix, userType, "interval:") + phone;
        Long expire = redisCacheClient.getExpire(interval);
        if (null != expire && expire > 0) {
            result.setInterval(expire);
            result.setCode("captcha.send.interval");
            result.setMessage("请勿重复发送");
            return true;
        }
        return false;
    }

    private String getPrefixKey(String prefix, UserType userType, String key) {
        return userType == null ? prefix + CAPTCHA + key : prefix + CAPTCHA + USER_TYPE_PREFIX + StringUtils.lowerCase(userType.userType()) + ":" + key;
    }

    private String format(String format, Object... arg) {
        return String.format(format, arg);
    }
}
