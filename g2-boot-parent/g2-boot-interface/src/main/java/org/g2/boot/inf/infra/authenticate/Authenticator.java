package org.g2.boot.inf.infra.authenticate;

import org.g2.boot.inf.data.InfSysConf;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-17
 */
public interface Authenticator {

    String authenticate(InfSysConf infSysConf, Object... args);

    default boolean autoRefreshWhenUnAuthorized() {
        return false;
    }
}
