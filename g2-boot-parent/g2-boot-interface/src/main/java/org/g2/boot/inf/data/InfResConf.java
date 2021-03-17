package org.g2.boot.inf.data;

import lombok.Data;
import org.g2.core.helper.FastJsonHelper;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-17
 */
@Data
public class InfResConf {

    private String code;
    private long tenantId;
    private long version;
    private String resourceUrl;
    private Long retryCount;
    private Integer active;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String sysCode;
    private Integer recordExecLog;
    private Integer logExpireDays;
    private String remark;

    public static String cacheKey(final String code) {
        return String.format("o2inf:inf_conf:%d:%s", 0, code);
    }

    public static InfResConf valueOf(final String var) {
        return FastJsonHelper.stringConvertObject(var, InfResConf.class);
    }

    public boolean isRecordExecLog() {
        return 1 == this.recordExecLog;
    }
}
