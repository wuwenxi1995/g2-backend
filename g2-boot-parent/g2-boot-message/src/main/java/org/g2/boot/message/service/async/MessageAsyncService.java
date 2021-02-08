package org.g2.boot.message.service.async;

import org.g2.boot.message.entity.MessageSender;
import org.g2.boot.message.feign.MessageRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-08
 */
public class MessageAsyncService {

    private static final Logger log = LoggerFactory.getLogger(MessageAsyncService.class);

    @Autowired
    private MessageRemoteService messageRemoteService;

    @Async("messageAsyncTask")
    public void sendMessage(MessageSender messageSender) {
        messageRemoteService.sendMessage(messageSender);
        if (log.isDebugEnabled()) {
            log.debug("send message async");
        }
    }
}
