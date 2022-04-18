package org.g2.inv.infra.feign.fallback;

import org.g2.core.util.Result;
import org.g2.core.util.StringUtil;
import org.g2.inv.api.dto.PosStockOperationDTO;
import org.g2.inv.api.dto.PosStockQueryDTO;
import org.g2.inv.core.domain.vo.StockQueryResponseVO;
import org.g2.inv.infra.feign.PosStockFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @author wuwenxi 2022-04-09
 */
public class PosStockFeignClientFallback implements FallbackFactory<PosStockFeignClient> {
    private static final Logger logger = LoggerFactory.getLogger(PosStockFeignClientFallback.class);
    private static final String COMMON_MSG = "posStock feign client fallback error, msg: {}";

    @Override
    public PosStockFeignClient create(Throwable cause) {
        return new PosStockFeignClient() {
            @Override
            public Result<String> token() {
                logger.error(COMMON_MSG, StringUtil.exceptionString(cause));
                return null;
            }

            @Override
            public List<StockQueryResponseVO> queryPosStock(PosStockQueryDTO posStockQueryDTO) {
                logger.error(COMMON_MSG, StringUtil.exceptionString(cause));
                return null;
            }

            @Override
            public Result<?> occupy(PosStockOperationDTO posStockOperationDTO) {
                logger.error(COMMON_MSG, StringUtil.exceptionString(cause));
                return null;
            }

            @Override
            public Result<?> release(PosStockOperationDTO posStockOperationDTO) {
                logger.error(COMMON_MSG, StringUtil.exceptionString(cause));
                return null;
            }

            @Override
            public Result<?> reduce(PosStockOperationDTO posStockOperationDTO) {
                logger.error(COMMON_MSG, StringUtil.exceptionString(cause));
                return null;
            }
        };
    }
}
