package org.g2.inv.core.domain.vo;

import java.util.Date;

/**
 * @author wuwenxi 2022-05-05
 */
public class TriggerMessage<T> {

    private T content;
    private String triggerType;
    private Date triggerDate;

    public TriggerMessage(T content, String triggerType) {
        this.content = content;
        this.triggerType = triggerType;
        this.triggerDate = new Date();
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public Date getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(Date triggerDate) {
        this.triggerDate = triggerDate;
    }
}
