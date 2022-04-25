package org.g2.starter.elasticsearch.domain.entity;

import org.g2.starter.elasticsearch.infra.annotation.Field;
import org.g2.starter.elasticsearch.infra.enums.FieldType;
import org.springframework.data.annotation.Id;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
public class BaseEntity {

    @Id
    @Field(type = FieldType.TEXT)
    private String id;

    @Field(type = FieldType.KEYWORD)
    private String indexName;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
