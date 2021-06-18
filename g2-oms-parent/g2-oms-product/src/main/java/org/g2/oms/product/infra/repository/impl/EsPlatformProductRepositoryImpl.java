package org.g2.oms.product.infra.repository.impl;

import org.g2.boot.elasticsearch.infra.repository.impl.BaseRepositoryImpl;
import org.g2.oms.product.domain.entity.EsPlatformProduct;
import org.g2.oms.product.domain.repository.EsPlatformProductRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wuwenxi 2021-05-30
 */
@Repository
public class EsPlatformProductRepositoryImpl extends BaseRepositoryImpl<EsPlatformProduct> implements EsPlatformProductRepository {
}
