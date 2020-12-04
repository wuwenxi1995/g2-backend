package org.g2.starter.mybatis.page;

/**
 * @author wenxi.wu@hand-chian.com 2020-12-01
 */
public class PageRequest {

    private int page;
    private int size;


    public PageRequest() {
    }

    public PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
