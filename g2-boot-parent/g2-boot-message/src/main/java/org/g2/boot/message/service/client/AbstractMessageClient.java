package org.g2.boot.message.service.client;

import org.g2.boot.message.service.EmailSender;
import org.g2.boot.message.service.MessageReceiver;
import org.g2.boot.message.service.Publisher;
import org.g2.boot.message.service.RelSender;
import org.g2.boot.message.service.SmsSender;
import org.g2.boot.message.service.WeChatMessageSender;
import org.g2.boot.message.service.WebMessageSender;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public abstract class AbstractMessageClient implements EmailSender, MessageReceiver, SmsSender, WebMessageSender, WeChatMessageSender, RelSender, Publisher {

    /**
     * 异步发送消息
     */
    boolean async = false;

    @Value("${spring.application.name}")
    protected String serviceName;


    public AbstractMessageClient sync() {
        this.async = false;
        return this;
    }

    public AbstractMessageClient async() {
        this.async = true;
        return this;
    }
}
