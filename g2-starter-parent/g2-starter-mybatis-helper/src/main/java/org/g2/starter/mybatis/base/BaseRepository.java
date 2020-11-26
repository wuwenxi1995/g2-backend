package org.g2.starter.mybatis.base;


import org.g2.starter.mybatis.service.BaseService;

import java.util.List;

/**
 * @author wenxi.wu@hand-china.com 2020-07-14
 */
public interface BaseRepository<T> extends BaseService<T> {

    /**
     * 批量插入
     *
     * @param list 准备插入的list
     * @return 插入之后的list
     */
    List<T> batchInsert(List<T> list);

    /**
     * 批量插入
     *
     * @param list 准备插入的list
     * @return 插入之后的list
     */
    List<T> batchInsertSelective(List<T> list);

    /**
     * 按主键批量更新
     *
     * @param list 准备更新的list
     * @return 更新之后的list
     */
    List<T> batchUpdateByPrimaryKey(List<T> list);

    /**
     * 按主键选择性批量更新
     *
     * @param list 准备更新的list
     * @return 更新之后的list
     */
    List<T> batchUpdateByPrimaryKeySelective(List<T> list);
}
