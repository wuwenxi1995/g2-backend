package org.g2.boot.inf.infra.authenticate.adapter;

import org.g2.boot.inf.data.InfSysConf;
import org.g2.boot.inf.infra.authenticate.Authenticator;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-17
 */
public class AuthenticatorAdapter {

    private volatile String token;
    private Authenticator authenticator;

    public AuthenticatorAdapter(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public String authenticate(InfSysConf infSysConf, Object... args) {
        if (token == null) {
            this.token = authenticator.authenticate(infSysConf, args);
        }
        return token;
    }

    public void clear() {
        this.token = null;
    }

    public boolean autoRefreshWhenUnAuthorized() {
        return authenticator.autoRefreshWhenUnAuthorized();
    }
}
