package org.g2.boot.elasticsearch.domain.repository;

import org.g2.starter.elasticsearch.domain.entity.BaseEntity;
import org.g2.starter.elasticsearch.domain.repository.ElasticsearchBaseRepository;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
public interface BaseRepository<T extends BaseEntity> extends
        ElasticsearchBaseRepository,
        BaseDocumentRepository<T>,
        BulkRepository<T>,
        SearchRepository<T> {
}
