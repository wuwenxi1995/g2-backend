package org.g2.inv.api.dto.pos;

import lombok.Data;

import java.util.List;

/**
 * @author wuwenxi 2022-04-15
 */
@Data
public class PosStockQueryDTO {

    /**
     * 服务点编码
     */
    private String posCode;
    /**
     * 查询项
     */
    private List<PosStockQueryItemDTO> items;
}
