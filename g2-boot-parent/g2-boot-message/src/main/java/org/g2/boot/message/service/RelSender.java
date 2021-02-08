package org.g2.boot.message.service;

import org.g2.boot.message.entity.Attachment;
import org.g2.boot.message.entity.Receiver;

import java.util.List;
import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-08
 */
public interface RelSender {

    /**
     * 发送消息（默认语言类型）
     *
     * @param messageCode  消息模版
     * @param receiverList 消息接收人
     * @param args         消息参数
     * @param typeCodeList 消息类型
     * @param attachments  附件
     */
    default void sendMessage(String messageCode, List<Receiver> receiverList, Map<String, String> args, List<String> typeCodeList, Attachment... attachments) {
        this.sendMessage(messageCode, "zh_CN", receiverList, args, typeCodeList, attachments);
    }

    /**
     * 发送消息
     *
     * @param messageCode  消息模版
     * @param lang         语言类型
     * @param receiverList 消息接收人
     * @param args         消息参数
     * @param typeCodeList 消息类型
     * @param attachments  附件
     */
    void sendMessage(String messageCode, String lang, List<Receiver> receiverList, Map<String, String> args, List<String> typeCodeList, Attachment... attachments);
}
