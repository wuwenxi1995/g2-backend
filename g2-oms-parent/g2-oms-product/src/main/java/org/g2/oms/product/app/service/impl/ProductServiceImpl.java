package org.g2.oms.product.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.g2.core.helper.ApplicationContextHelper;
import org.g2.core.helper.FastJsonHelper;
import org.g2.oms.product.app.service.ProductService;
import org.g2.oms.product.domain.repository.EsPlatformProductRepository;
import org.g2.oms.start.core.constants.CoreConstants;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RedisCacheClient redisCacheClient;

    @Autowired
    private EsPlatformProductRepository esPlatformProductRepository;

    @Override
    public String get() {
        log.info("request ... ");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("return ...");
        return "get from product";
    }

    @Override
    public void publish() {
        ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
        Map<String, MessageListener> messageListenerMap = applicationContext.getBeansOfType(MessageListener.class);
        log.info("messageListener : {}", FastJsonHelper.mapConvertString(messageListenerMap));
        Map<String, MessageListenerAdapter> messageListenerAdapterMap = applicationContext.getBeansOfType(MessageListenerAdapter.class);
        log.info("messageListenerAdapter : {}", FastJsonHelper.mapConvertString(messageListenerAdapterMap));
        Map<String, RedisMessageListenerContainer> containerMap = applicationContext.getBeansOfType(RedisMessageListenerContainer.class);
        log.info("redisMessageListenerContainer : {}", FastJsonHelper.collectionConvertString(containerMap.keySet()));
        redisCacheClient.convertAndSend(CoreConstants.Publisher.SEND_MESSAGE, "this is a pub/sub test message");
    }

    @Override
    public void listener() {
        redisCacheClient.opsForValue().set("test", "test", 5, TimeUnit.SECONDS);
    }
}
