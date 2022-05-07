package org.g2.inv.calculate.app.handler.transaction;

import org.g2.core.chain.invoker.ChainInvoker;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.StockLevelRepository;

import java.util.List;
import java.util.Map;

/**
 * @author wuwenxi 2022-05-07
 */
public abstract class AbstractTransactionHandler implements TransactionHandler {

    private final StockLevelRepository stockLevelRepository;

    protected AbstractTransactionHandler(StockLevelRepository stockLevelRepository) {
        this.stockLevelRepository = stockLevelRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(ChainInvoker chainInvoker, Object... param) throws Exception {
        Map<String, Map<String, List<InvTransaction>>> transactionListMap = (Map<String, Map<String, List<InvTransaction>>>) param[0];
        transactionListMap.get(type()).forEach(this::handler);
        return chainInvoker.proceed(param);
    }

    protected StockLevel selectOneBySkuAndPos(String skuCode, String posCode) {
        StockLevel stockLevel = new StockLevel();
        stockLevel.setSkuCode(skuCode);
        stockLevel.setPosCode(posCode);
        return stockLevelRepository.selectOne(stockLevel);
    }

    protected Long defValue(Long value) {
        return value != null ? value : 0;
    }
}
