package org.g2.oms.product.infra;

import org.g2.oms.start.core.constants.CoreConstants;
import org.g2.starter.mq.subject.annotation.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;

/**
 * @author wuwenxi 2021-05-18
 */
@Subject(values = {CoreConstants.Publisher.SEND_MESSAGE})
public class SubjectTest implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(SubjectTest.class);

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        log.info("收到消息，消息内容：{}", new String(message.getBody()));
    }
}
