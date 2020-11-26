package org.g2.starter.mybatis.service;

import org.g2.starter.mybatis.page.Page;

import java.util.List;

/**
 * @author wenxi.wu@hand-china.com 2020-07-14
 */
public interface BaseService<T> {

    /**
     * 根据主键查询
     *
     * @param obj 主键
     * @return 查询结果
     */
    T selectByPrimaryKey(Object obj);

    /**
     * 查询全部数据
     *
     * @return list
     */
    List<T> selectAll();

    /**
     * 按条件查询
     *
     * @param entity 查询条件
     * @return list
     */
    List<T> select(T entity);

    /**
     * 按条件查询一条数据
     *
     * @param entity 查询条件
     * @return T
     */
    T selectOne(T entity);

    /**
     * 返回条数
     *
     * @param entity 查询条件
     * @return int
     */
    int selectCount(T entity);

    /**
     * 插入
     *
     * @param entity 插入数据
     * @return int
     */
    int insert(T entity);

    /**
     * 插入，只插入不为空的字段
     *
     * @param entity 插入数据
     * @return int
     */
    int insertSelective(T entity);

    /**
     * 通过主键更新数据
     *
     * @param entity 更新数据
     * @return int
     */
    int updateByPrimaryKey(T entity);

    /**
     * 通过主键更新，只更新传入不为空的字段
     *
     * @param entity 更新字段
     * @return int
     */
    int updateByPrimaryKeySelective(T entity);

    /**
     * 按条件删除数据
     *
     * @param entity 删除条件
     * @return int
     */
    int delete(T entity);

    /**
     * 按主键删除数据
     *
     * @param key 主键
     * @return int
     */
    int deleteByPrimaryKey(Object key);

    /**
     * 分页查询数据
     *
     * @param page 页码
     * @param size 每页数量
     * @return page
     */
    Page<T> pageAll(int page, int size);

    /**
     * 按条件分页查询数据
     *
     * @param entity 查询条件
     * @param page   页码
     * @param size   每页数量
     * @return page
     */
    Page<T> page(T entity, int page, int size);

    /**
     * 按字段插入数据
     *
     * @param entity    插入数据
     * @param optionals 插入字段
     * @return int
     */
    int insertOptional(T entity, String... optionals);

    /**
     * 按字段更新数据
     *
     * @param entity    更新数据
     * @param optionals 更新字段
     * @return int
     */
    int updateOptional(T entity, String... optionals);

}
