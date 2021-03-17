package org.g2.boot.inf.infra.authenticate.impl;

import org.apache.commons.lang3.StringUtils;
import org.g2.boot.inf.data.InfSysConf;
import org.g2.boot.inf.infra.authenticate.Authenticator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-17
 */
public class BasicAuthenticator implements Authenticator {

    @Override
    public String authenticate(InfSysConf infSysConf, Object... args) {
        if (!StringUtils.isBlank(infSysConf.getIdentity()) && !StringUtils.isBlank(infSysConf.getPassword())) {
            String basicIdentity = infSysConf.getIdentity() + ":" + infSysConf.getPassword();
            return "Basic " + Base64.getEncoder().encodeToString(basicIdentity.getBytes(StandardCharsets.UTF_8));
        } else {
            return null;
        }
    }
}
