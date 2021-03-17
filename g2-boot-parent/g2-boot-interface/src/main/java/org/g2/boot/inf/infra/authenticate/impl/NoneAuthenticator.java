package org.g2.boot.inf.infra.authenticate.impl;

import org.g2.boot.inf.data.InfSysConf;
import org.g2.boot.inf.infra.authenticate.Authenticator;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-17
 */
public class NoneAuthenticator implements Authenticator {

    @Override
    public String authenticate(InfSysConf infSysConf, Object... args) {
        return null;
    }
}
