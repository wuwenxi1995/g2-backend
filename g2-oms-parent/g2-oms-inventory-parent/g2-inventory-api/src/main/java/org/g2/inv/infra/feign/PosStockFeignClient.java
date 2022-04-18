package org.g2.inv.infra.feign;

import org.g2.core.util.Result;
import org.g2.inv.api.dto.PosStockOperationDTO;
import org.g2.inv.api.dto.PosStockQueryDTO;
import org.g2.inv.core.domain.vo.StockQueryResponseVO;
import org.g2.inv.infra.feign.fallback.PosStockFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author wuwenxi 2022-04-09
 */
@FeignClient(name = "g2-Inventory", fallbackFactory = PosStockFeignClientFallback.class)
public interface PosStockFeignClient {

    /**
     * 获取服务点库存操作token
     *
     * @return result
     */
    @GetMapping("/idempotent-token")
    Result<String> token();

    /**
     * 服务点库存查询
     *
     * @param posStockQueryDTO 查询信息
     * @return 查询结果
     */
    @PostMapping("/pos-stock-api/query")
    List<StockQueryResponseVO> queryPosStock(@RequestBody PosStockQueryDTO posStockQueryDTO);

    /**
     * 库存占用
     *
     * @param posStockOperationDTO 库存操作信息
     * @return 请求结果
     */
    @PostMapping("/pos-stock-api/occupy")
    Result<?> occupy(@RequestBody PosStockOperationDTO posStockOperationDTO);

    /**
     * 库存释放
     *
     * @param posStockOperationDTO 库存操作信息
     * @return 请求结果
     */
    @PostMapping("/pos-stock-api/release")
    Result<?> release(@RequestBody PosStockOperationDTO posStockOperationDTO);

    /**
     * 库存扣减
     *
     * @param posStockOperationDTO 库存操作信息
     * @return 请求结果
     */
    @PostMapping("/pos-stock-api/reduce")
    Result<?> reduce(@RequestBody PosStockOperationDTO posStockOperationDTO);
}
