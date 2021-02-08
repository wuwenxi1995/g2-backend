package org.g2.boot.message.entity;

import java.util.Objects;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-08
 */
public class Msg {
    private Long userId;
    private String sessionId;
    private String service;
    private String key;
    private String message;
    private String type;

    public Long getUserId() {
        return userId;
    }

    public Msg setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Msg setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getService() {
        return service;
    }

    public Msg setService(String service) {
        this.service = service;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Msg setKey(String key) {
        this.key = key;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Msg setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getType() {
        return type;
    }

    public Msg setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Msg msg = (Msg) o;
        return Objects.equals(userId, msg.userId) &&
                Objects.equals(sessionId, msg.sessionId) &&
                Objects.equals(service, msg.service) &&
                Objects.equals(key, msg.key) &&
                Objects.equals(message, msg.message) &&
                Objects.equals(type, msg.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, sessionId, service, key, message, type);
    }
}
