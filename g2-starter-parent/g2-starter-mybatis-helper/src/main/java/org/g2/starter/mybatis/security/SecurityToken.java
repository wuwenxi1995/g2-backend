package org.g2.starter.mybatis.security;

/**
 * @author wenxi.wu@hand-china.com 2020-08-07
 */
public interface SecurityToken {

    /**
     * 获取token
     *
     * @return token
     */
    String getToken();

    /**
     * 设置token
     *
     * @param token token
     */
    void setToken(String token);
}
