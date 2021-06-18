package org.g2.oms.product.domain.entity;

import org.g2.starter.elasticsearch.domain.entity.BaseEntity;
import org.g2.starter.elasticsearch.infra.annotation.Document;

import java.io.Serializable;

/**
 * @author wuwenxi 2021-05-30
 */
@Document(indexName = "g2_platform_product")
public class EsPlatformProduct extends BaseEntity implements Serializable {
}
