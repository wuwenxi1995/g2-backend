package org.g2.inv.trigger.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wuwenxi 2022-04-14
 */
@Data
public class TriggerMessage<T> {

    /**
     * 消息内容
     */
    private T content;
    /**
     * 消息类型
     */
    private String type;
    /**
     * 触发时间
     */
    private Date triggerDate;
}
