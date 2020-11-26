package org.g2.starter.mybatis.entity;

import org.g2.starter.mybatis.security.SecurityToken;

import java.util.Date;

/**
 * @author wenxi.wu@hand-china.com 2020-08-07
 */
public class AuditDomain implements SecurityToken {

    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";

    //
    //                          数据库表默认字段
    //------------------------------------------------------------------

    /**
     * 创建时间
     */
    private Date creationDate;
    /**
     * 创建人
     */
    private Long createdBy;
    /**
     * 最近一次更新时间
     */
    private Date lastUpdateDate;
    /**
     * 最近一次更新人
     */
    private Long lastUpdatedBy;
    /**
     * 版本号（数据库乐观锁）
     */
    private Long objectVersionNumber;
    /**
     * 接口更新token校验
     */
    private String _token;

    @Override
    public String getToken() {
        return this._token;
    }

    @Override
    public void setToken(String token) {
        this._token = token;
    }
}
