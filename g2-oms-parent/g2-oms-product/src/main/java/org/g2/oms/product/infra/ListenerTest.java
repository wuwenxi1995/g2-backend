package org.g2.oms.product.infra;

import org.g2.starter.redis.mq.listener.annotation.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;

/**
 * @author wuwenxi 2021-05-19
 */
@Listener(db = 0)
public class ListenerTest implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(ListenerTest.class);

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        log.info("redis expire key is : {}", new String(message.getBody()));
    }
}
