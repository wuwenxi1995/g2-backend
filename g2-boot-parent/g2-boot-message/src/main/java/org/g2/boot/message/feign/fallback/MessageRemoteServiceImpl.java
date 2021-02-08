package org.g2.boot.message.feign.fallback;

import feign.hystrix.FallbackFactory;
import org.g2.boot.message.entity.MessageSender;
import org.g2.boot.message.feign.MessageRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-05
 */
@Component
public class MessageRemoteServiceImpl implements FallbackFactory<MessageRemoteService> {

    private static final Logger log = LoggerFactory.getLogger(MessageRemoteServiceImpl.class);

    @Override
    public MessageRemoteService create(Throwable throwable) {
        return new MessageRemoteService() {
            @Override
            public ResponseEntity sendMessage(MessageSender messageSender) {
                log.error("");
                return null;
            }
        };
    }
}
