package org.g2.inv.trigger.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wuwenxi 2022-04-14
 */
@Data
public class TriggerMessage<T> {

    private T content;
    private String type;
    private Date triggerDate;
}
