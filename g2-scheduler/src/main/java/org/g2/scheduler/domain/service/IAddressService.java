package org.g2.scheduler.domain.service;

import java.util.List;

import org.g2.scheduler.domain.entity.Executor;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public interface IAddressService {

    /**
     * 获取服务地址
     *
     * @param serviceName 服务名
     * @return 服务地址
     */
    List<String> getServiceAddressList(String serviceName);

    /**
     * 获取服务地址
     *
     * @param executor 执行器信息
     * @return 服务地址
     */
    List<String> getServiceAddressList(Executor executor);
}
