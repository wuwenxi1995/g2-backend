package org.g2.boot.message.feign;

import org.g2.boot.message.entity.MessageSender;
import org.g2.boot.message.feign.fallback.MessageRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-05
 */
@FeignClient(value = "g2-message",
        path = "/v1",
        fallbackFactory = MessageRemoteServiceImpl.class)
public interface MessageRemoteService {

    /**
     * 通用发送消息
     *
     * @param messageSender 消息内容
     * @return 返回内容
     */
    @PostMapping("/message/relevance")
    ResponseEntity sendMessage(@RequestBody MessageSender messageSender);
}
