package org.g2.starter.mybatis.page;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wenxi.wu@hand-china.com 2020-07-14
 */
@Data
public class PageInfo implements Serializable {

    private int page;

    private int size;

    private int begin;

    private int end;

    private long total;

    private int pages;

    private boolean count = true;

    public PageInfo(int page, int size) {
        this(page, size, true);
    }

    /**
     * 分页信息类构造函数
     *
     * @param page  page
     * @param size  size
     * @param count count
     */
    public PageInfo(int page, int size, boolean count) {
        this.page = page;
        this.size = size;
        this.begin = page * size;
        this.end = begin + size;
        this.count = count;
    }
}
