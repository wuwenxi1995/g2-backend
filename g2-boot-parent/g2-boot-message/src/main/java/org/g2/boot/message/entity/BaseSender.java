package org.g2.boot.message.entity;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-08
 */
public class BaseSender {

    protected String messageCode;

    public String getMessageCode() {
        return messageCode;
    }

    public BaseSender setMessageCode(String messageCode) {
        this.messageCode = messageCode;
        return this;
    }
}
