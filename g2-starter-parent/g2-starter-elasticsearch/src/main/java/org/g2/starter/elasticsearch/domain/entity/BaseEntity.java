package org.g2.starter.elasticsearch.domain.entity;

import org.g2.starter.elasticsearch.infra.annotation.Field;
import org.g2.starter.elasticsearch.infra.annotation.Fields;
import org.g2.starter.elasticsearch.infra.annotation.Id;
import org.g2.starter.elasticsearch.infra.constants.OmsElasticsearchConstants;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
public class BaseEntity {

    public static final String INDEX_NAME = "indexName";
    public static final String ID = "id";

    @Id
    @Fields({
            @Field(type = OmsElasticsearchConstants.FiledType.TEXT),
            @Field(type = OmsElasticsearchConstants.FiledType.KEYWORD, fields = true)
    })
    private String id;

    @Fields({
            @Field(type = OmsElasticsearchConstants.FiledType.TEXT),
            @Field(type = OmsElasticsearchConstants.FiledType.KEYWORD, fields = true)
    })
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
