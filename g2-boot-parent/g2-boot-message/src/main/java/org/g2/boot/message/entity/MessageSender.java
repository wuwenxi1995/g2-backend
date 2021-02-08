package org.g2.boot.message.entity;

import java.util.List;
import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-08
 */
public class MessageSender extends BaseSender {

    private String lang;
    private List<Receiver> receiverAddressList;
    private Map<String, String> args;
    private List<String> typeCodeList;
    private Map<String, Message> messageMap;
    private List<Attachment> attachmentList;

    @Override
    public MessageSender setMessageCode(String messageCode) {
        this.messageCode = messageCode;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public MessageSender setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public List<Receiver> getReceiverAddressList() {
        return receiverAddressList;
    }

    public MessageSender setReceiverAddressList(List<Receiver> receiverAddressList) {
        this.receiverAddressList = receiverAddressList;
        return this;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public MessageSender setArgs(Map<String, String> args) {
        this.args = args;
        return this;
    }

    public List<String> getTypeCodeList() {
        return typeCodeList;
    }

    public MessageSender setTypeCodeList(List<String> typeCodeList) {
        this.typeCodeList = typeCodeList;
        return this;
    }

    public Map<String, Message> getMessageMap() {
        return messageMap;
    }

    public MessageSender setMessageMap(Map<String, Message> messageMap) {
        this.messageMap = messageMap;
        return this;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public MessageSender setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
        return this;
    }
}
