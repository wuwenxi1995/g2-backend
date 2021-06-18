package org.g2.oms.product;

import org.g2.boot.inf.annotation.InfClient;
import org.g2.core.helper.FastJsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Map;

/**
 * @author wuwenxi 2021-05-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductTest {

    private static final Logger log = LoggerFactory.getLogger(ProductTest.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test01() {
//        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(InfClient.class);
//        log.info(FastJsonHelper.mapConvertString(beansWithAnnotation));

        Map<String, MessageListener> beansOfType = applicationContext.getBeansOfType(MessageListener.class);
        log.info(FastJsonHelper.mapConvertString(beansOfType));
    }
}
