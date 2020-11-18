package org.g2.boot.elasticsearch.domain.entity;

import java.util.List;

import lombok.Data;
import org.elasticsearch.search.aggregations.Aggregations;
import org.g2.starter.elasticsearch.domain.entity.BaseEntity;

/**
 * @author wenxi.wu@hand-china.com 2020-09-24
 */
@Data
public class Page<T extends BaseEntity> {

    public Page() {
    }

    public Page(int page, int size) {
        this.page = page;
        this.size = size;
    }

    /**
     * 分页，默认第一页
     */
    private int page = 0;
    /**
     * 分页大小,默认20
     */
    private int size = 20;
    /**
     * 总量
     */
    private long total;
    /**
     * 聚合信息
     */
    private Aggregations aggregations;
    /**
     * 数据
     */
    private List<T> data;
}
