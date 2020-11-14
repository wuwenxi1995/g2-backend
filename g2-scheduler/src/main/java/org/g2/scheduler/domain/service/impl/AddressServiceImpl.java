package org.g2.scheduler.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.g2.scheduler.domain.entity.Executor;
import org.g2.scheduler.domain.service.IAddressService;
import org.g2.scheduler.infra.constants.SchedulerConstants;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
@Component
public class AddressServiceImpl implements IAddressService {

    private final DiscoveryClient discoveryClient;

    public AddressServiceImpl(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public List<String> getServiceAddressList(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (CollectionUtils.isEmpty(instances)) {
            return new ArrayList<>();
        }
        return instances.stream().map(ServiceInstance::getUri).map(e -> e.getHost().concat(String.valueOf(e.getPort()))).collect(Collectors.toList());
    }

    @Override
    public List<String> getServiceAddressList(Executor executor) {
        if (Objects.equals(executor.getExecutorType(), SchedulerConstants.ExecutorType.AUTO)) {
            return getServiceAddressList(executor.getServerName());
        }
        String address = executor.getAddressList();
        return StringUtils.hasText(address) ? new ArrayList<>(StringUtils.commaDelimitedListToSet(address)) : new ArrayList<>();
    }
}
