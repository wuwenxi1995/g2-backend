package org.g2.starter.mybatis.page;

import lombok.Data;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wenxi.wu@hand-china.com 2020-07-14
 */
@Data
public class Page<E> extends AbstractList<E> {

    /**
     * 总页数
     */
    private int totalPages;
    /**
     * 总元素
     */
    private long totalElement;
    /**
     * 当前页码元素个数
     */
    private int pageOfElements;
    /**
     * 查询页元素
     */
    private int size;
    /**
     * 页码
     */
    private int page;
    /**
     * 返回元素
     */
    private List<E> content;

    public Page() {
        content = new ArrayList<>();
    }

    public Page(List<E> content, PageInfo pageInfo, long total) {
        this.content = content;
        this.page = pageInfo.getPage();
        this.size = pageInfo.getSize();
        this.totalElement = total;
        this.totalPages = (int) total - 1 / size + 1;
        this.pageOfElements = content.size();
    }

    @Override
    public E get(int index) {
        return content.get(index);
    }

    @Override
    public int size() {
        return content.size();
    }
}
