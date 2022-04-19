package org.g2.inv.infra.feign;

import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.infra.feign.fallback.InvConsoleFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author wuwenxi 2022-04-18
 */
@FeignClient(name = "g2-inventory-console", fallbackFactory = InvConsoleFeignClientFallback.class)
public interface InvConsoleFeignClient {

    /**
     * 插入库存事务
     *
     * @param invTransactions 库存事务
     */
    @PostMapping("/inv-transaction/create")
    void save(@RequestBody List<InvTransaction> invTransactions);
}
