package org.g2.oms.order.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.g2.core.helper.AsyncTaskHelper;
import org.g2.core.helper.FastJsonHelper;
import org.g2.core.helper.TransactionalHelper;
import org.g2.core.util.ThreadPoolExecutorUtil;
import org.g2.oms.order.app.service.OrderService;
import org.g2.oms.order.entity.Person;
import org.g2.oms.order.infra.feign.ProductFeignClient;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final AsyncTaskHelper asyncTaskHelper;
    private final TransactionalHelper transactionalHelper;
    private final ProductFeignClient productFeignClient;
    private final ThreadPoolTaskExecutor executor;

    public OrderServiceImpl(AsyncTaskHelper asyncTaskHelper, TransactionalHelper transactionalHelper,
                            ProductFeignClient productFeignClient, ThreadPoolTaskExecutor executor) {
        this.asyncTaskHelper = asyncTaskHelper;
        this.transactionalHelper = transactionalHelper;
        this.productFeignClient = productFeignClient;
        this.executor = executor;
    }

    @Override
    public String test() {
        long start = System.currentTimeMillis();
        log.info("request ...");
        Person person = new Person();
        person.setAge(10L).setName("wwx");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        asyncTaskHelper.operation(() -> {
            try {
                Thread.sleep(10000);
                log.info("异步线程结束...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        log.info("return , times:{}", System.currentTimeMillis() - start);
        log.info("用户信息：{}", FastJsonHelper.objectConvertString(person));
        return FastJsonHelper.objectConvertString(person);
    }

    @Override
    public String test01() {
        String info = null;
        try {
            info = productFeignClient.get();
            Thread.sleep(1000);
        } catch (Exception e) {
            log.error("响应超时...");
        }
        if (null == info) {
            info = "返回信息为空";
        }
        log.debug("返回信息：{}", info);
        return info;
    }

    @Override
    public void test02(Long size) {
        Map<Integer, Object> map = new HashMap<>(size.intValue());
        for (int i = 0; i < size; i++) {
            map.put(i, UUID.randomUUID());
        }

        ThreadPoolExecutorUtil.runTask(map, 5, 200, executor, consumer -> {
            if (log.isDebugEnabled()) {
                log.debug("csgUpdateProductStockJob : current thread {}, handler data {}", Thread.currentThread().getName(), consumer.size());
            }
            consumer.forEach((key, value) -> {
                log.info("-----id:{},value:{}-----", key, value);
            });
            log.info("当前线程[{}]任务结束", Thread.currentThread().getName());
            // ThreadPoolExecutorUtil.INDEX.decrementAndGet();
        });

        try {

        }catch (Exception e){

        }
    }
}
