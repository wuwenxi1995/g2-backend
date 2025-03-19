package org.g2.boot.inf.data;

import lombok.Data;
import org.g2.starter.core.helper.FastJsonHelper;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-17
 */
@Data
public class InfSysConf {

    private String code;
    private long tenantId;
    private long version;
    private String url;
    private String identity;
    private String password;
    private Long readTimeout;
    private Long writeTimeout;
    private Long connectTimeout;
    private String remark;
    private Long maxIdle;
    private Long keepAliveDuration;
    private Integer autoRetryConnect;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;

    public static String cacheKey(final String code) {
        return String.format("o2inf:inf_sys:%d:%s", 0, code);
    }

    public static InfSysConf valueOf(final String var) {
        return FastJsonHelper.stringConvertObject(var, InfSysConf.class);
    }

    public boolean isRetry() {
        return 1 == this.autoRetryConnect;
    }

}
