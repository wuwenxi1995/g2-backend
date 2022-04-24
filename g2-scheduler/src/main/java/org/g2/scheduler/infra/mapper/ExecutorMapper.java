package org.g2.scheduler.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.g2.scheduler.domain.entity.Executor;
import io.choerodon.mybatis.common.BaseMapper;

/**
 * @author wenxi.wu@hand-chian.com 2020-11-26
 */
@Mapper
public interface ExecutorMapper extends BaseMapper<Executor> {
}
