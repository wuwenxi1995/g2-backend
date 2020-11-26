package org.g2.starter.mybatis.base.impl;

import org.g2.starter.mybatis.base.BaseRepository;
import org.g2.starter.mybatis.common.BaseMapper;
import org.g2.starter.mybatis.page.Page;
import org.g2.starter.mybatis.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author wenxi.wu@hand-china.com 2020-07-14
 */
public class BaseRepositoryImpl<T> extends BaseServiceImpl<T> implements BaseRepository<T> {

    @Autowired
    private BaseMapper<T> mapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<T> batchInsert(List<T> list) {
        this.batchDml(list, this::insert);
        this.test(list,this::insertSelective);
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<T> batchInsertSelective(List<T> list) {
        this.batchDml(list, this::insertSelective);
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<T> batchUpdateByPrimaryKey(List<T> list) {
        this.batchDml(list, this::updateByPrimaryKey);
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<T> batchUpdateByPrimaryKeySelective(List<T> list) {
        this.batchDml(list, this::updateByPrimaryKeySelective);
        return list;
    }

    @Override
    public T selectByPrimaryKey(Object obj) {
        return null;
    }

    @Override
    public List<T> selectAll() {
        return null;
    }

    @Override
    public List<T> select(T entity) {
        return null;
    }

    @Override
    public T selectOne(T entity) {
        return null;
    }

    @Override
    public int selectCount(T entity) {
        return 0;
    }

    @Override
    public int insert(T entity) {
        return 0;
    }

    @Override
    public int insertSelective(T entity) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(T entity) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(T entity) {
        return 0;
    }

    @Override
    public int delete(T entity) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Object key) {
        return 0;
    }

    @Override
    public Page<T> pageAll(int page, int size) {
        return null;
    }

    @Override
    public Page<T> page(T entity, int page, int size) {
        return null;
    }

    @Override
    public int insertOptional(T entity, String... optionals) {
        return 0;
    }

    @Override
    public int updateOptional(T entity, String... optionals) {
        return 0;
    }

    private int batchDml(List<T> list, Function<T, Integer> function) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        int handlerCount = 0;
        for (T entity : list) {
            handlerCount += function.apply(entity);
        }
        return handlerCount;
    }

    private void test(List<T> list, Consumer<T> consumer) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (T obj : list) {
            consumer.accept(obj);
        }
    }
}
