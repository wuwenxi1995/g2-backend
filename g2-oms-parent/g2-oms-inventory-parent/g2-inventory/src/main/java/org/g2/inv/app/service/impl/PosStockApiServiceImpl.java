package org.g2.inv.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.g2.inv.api.dto.pos.PosStockOperateDTO;
import org.g2.inv.api.dto.pos.PosStockQueryDTO;
import org.g2.inv.api.dto.pos.PosStockQueryItemDTO;
import org.g2.inv.app.service.PosStockApiService;
import org.g2.inv.core.domain.repository.PosStockRedisRepository;
import org.g2.inv.core.domain.vo.StockQueryResponseVO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2022-04-15
 */
@Service
public class PosStockApiServiceImpl implements PosStockApiService {

    private final PosStockRedisRepository posStockRedisRepository;

    public PosStockApiServiceImpl(PosStockRedisRepository posStockRedisRepository) {
        this.posStockRedisRepository = posStockRedisRepository;
    }

    @Override
    public List<StockQueryResponseVO> query(PosStockQueryDTO posStockQueryDTO) {
        Map<String, Long> skuList;
        if (StringUtils.isBlank(posStockQueryDTO.getPosCode()) || MapUtils.isEmpty(skuList = getItems(posStockQueryDTO.getItems()))) {
            return Collections.emptyList();
        }
        return posStockRedisRepository.readPosStock(posStockQueryDTO.getPosCode(), skuList);
    }

    @Override
    public boolean occupy(PosStockOperateDTO posStockOperateDTO) {
        if (posStockOperateDTO.getPosCode() == null || MapUtils.isEmpty(posStockOperateDTO.getItems())) {
            return false;
        }
        return posStockRedisRepository.occupy(posStockOperateDTO.getPosCode(), posStockOperateDTO.getItems());
    }

    @Override
    public boolean release(PosStockOperateDTO posStockOperateDTO) {
        if (posStockOperateDTO.getPosCode() == null || MapUtils.isEmpty(posStockOperateDTO.getItems())) {
            return false;
        }
        return posStockRedisRepository.release(posStockOperateDTO.getPosCode(), posStockOperateDTO.getItems());
    }

    @Override
    public boolean reduce(PosStockOperateDTO posStockOperateDTO) {
        if (posStockOperateDTO.getPosCode() == null || MapUtils.isEmpty(posStockOperateDTO.getItems())) {
            return false;
        }
        return posStockRedisRepository.reduce(posStockOperateDTO.getPosCode(), posStockOperateDTO.getItems());
    }

    private Map<String, Long> getItems(List<PosStockQueryItemDTO> items) {
        return items.stream().collect(Collectors.toMap(PosStockQueryItemDTO::getMasterSkuCode, PosStockQueryItemDTO::getQuantity));
    }
}
