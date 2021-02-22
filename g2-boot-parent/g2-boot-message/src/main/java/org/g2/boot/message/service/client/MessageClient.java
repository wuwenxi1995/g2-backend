package org.g2.boot.message.service.client;

import org.g2.boot.message.config.properties.MessageClientProperties;
import org.g2.boot.message.entity.Attachment;
import org.g2.boot.message.entity.MessageSender;
import org.g2.boot.message.entity.Msg;
import org.g2.boot.message.entity.Receiver;
import org.g2.boot.message.feign.MessageRemoteService;
import org.g2.boot.message.service.EmailSender;
import org.g2.boot.message.service.MessageReceiver;
import org.g2.boot.message.service.Publisher;
import org.g2.boot.message.service.RelSender;
import org.g2.boot.message.service.SmsSender;
import org.g2.boot.message.service.WeChatMessageSender;
import org.g2.boot.message.service.WebMessageSender;
import org.g2.boot.message.service.async.MessageAsyncService;
import org.g2.core.exception.CommonException;
import org.g2.core.helper.FastJsonHelper;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-05
 */
public class MessageClient implements EmailSender, MessageReceiver, SmsSender, WebMessageSender, WeChatMessageSender, RelSender, Publisher {

    /**
     * 异步发送消息
     */
    private boolean async = false;

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private MessageRemoteService messageRemoteService;

    @Autowired
    private MessageAsyncService messageAsyncService;

    @Autowired
    private RedisCacheClient redisCacheClient;

    @Autowired
    private MessageClientProperties messageClientProperties;

    //
    //                      RelSender接口实现
    // ==================================================================

    @Override
    public void sendMessage(String messageCode, List<Receiver> receiverList, Map<String, String> args, List<String> typeCodeList, Attachment... attachments) {
        if (attachments == null) {
            attachments = new Attachment[0];
        }
        MessageSender messageSender = new MessageSender()
                .setMessageCode(messageCode)
                .setLang(messageClientProperties.getDefaultLang())
                .setReceiverAddressList(receiverList)
                .setArgs(args)
                .setTypeCodeList(typeCodeList);
        if (attachments.length > 0) {
            messageSender.setAttachmentList(Arrays.asList(attachments));
        }
        if (this.async) {
            messageAsyncService.sendMessage(messageSender);
            return;
        }
        messageRemoteService.sendMessage(messageSender);
    }

    @Override
    public void sendMessage(String messageCode, List<Receiver> receiverList, Map<String, String> args, Attachment... attachments) {
        if (attachments == null) {
            attachments = new Attachment[0];
        }
        MessageSender messageSender = new MessageSender().setMessageCode(messageCode).setLang(messageClientProperties.getDefaultLang()).setReceiverAddressList(receiverList).setArgs(args).setTypeCodeList(null);
        if (attachments.length > 0) {
            messageSender.setAttachmentList(Arrays.asList(attachments));
        }
        if (this.async) {
            messageAsyncService.sendMessage(messageSender);
            return;
        }
        messageRemoteService.sendMessage(messageSender);
    }

    @Override
    public void sendMessage(String messageCode, String lang, List<Receiver> receiverList, Map<String, String> args, List<String> typeCodeList, Attachment... attachments) {
        if (attachments == null) {
            attachments = new Attachment[0];
        }
        MessageSender messageSender = new MessageSender().setMessageCode(messageCode).setLang(lang).setReceiverAddressList(receiverList).setArgs(args).setTypeCodeList(typeCodeList);
        if (attachments.length > 0) {
            messageSender.setAttachmentList(Arrays.asList(attachments));
        }
        if (this.async) {
            messageAsyncService.sendMessage(messageSender);
            return;
        }
        messageRemoteService.sendMessage(messageSender);
    }

    @Override
    public void sendMessage(String messageCode, String lang, List<Receiver> receiverList, Map<String, String> args, Attachment... attachments) {
        if (attachments == null) {
            attachments = new Attachment[0];
        }
        MessageSender messageSender = new MessageSender().setMessageCode(messageCode).setLang(lang).setReceiverAddressList(receiverList).setArgs(args).setTypeCodeList(null);
        if (attachments.length > 0) {
            messageSender.setAttachmentList(Arrays.asList(attachments));
        }
        if (this.async) {
            messageAsyncService.sendMessage(messageSender);
            return;
        }
        messageRemoteService.sendMessage(messageSender);
    }

    //
    //                        Publisher接口实现
    // ==================================================================

    @Override
    public void sendByUser(Long userId, String key, String message) {
        Msg msg = new Msg().setUserId(userId).setMessage(message).setKey(key).setType("U").setService(this.serviceName);
        try {
            redisCacheClient.convertAndSend("g2-webSocket", FastJsonHelper.objectConvertString(msg));
        } catch (Exception e) {
            throw new CommonException(e);
        }
    }

    @Override
    public void sendBySession(String sessionId, String key, String message) {
        Msg msg = new Msg().setSessionId(sessionId).setMessage(message).setKey(key).setMessage(message).setType("S").setService(this.serviceName);
        try {
            redisCacheClient.convertAndSend("g2-webSocket", FastJsonHelper.objectConvertString(msg));
        } catch (Exception e) {
            throw new CommonException(e);
        }
    }

    @Override
    public void sendToAll(String key, String message) {
        Msg msg = new Msg().setMessage(message).setKey(key).setType("A").setService(this.serviceName);
        try {
            redisCacheClient.convertAndSend("g2-webSocket", FastJsonHelper.objectConvertString(msg));
        } catch (Exception e) {
            throw new CommonException(e);
        }
    }

    public MessageClient sync() {
        this.async = false;
        return this;
    }

    public MessageClient async() {
        this.async = true;
        return this;
    }
}
