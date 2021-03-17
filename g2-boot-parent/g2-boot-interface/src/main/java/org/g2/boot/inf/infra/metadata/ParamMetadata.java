package org.g2.boot.inf.infra.metadata;

import lombok.Data;
import org.g2.boot.inf.infra.constant.ParamType;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-17
 */
@Data
public class ParamMetadata {

    private int index;
    private ParamType paramType;
    private String name;
}
