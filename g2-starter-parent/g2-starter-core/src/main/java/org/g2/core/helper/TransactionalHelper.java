package org.g2.core.helper;

import org.g2.core.bean.Operation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务操作
 *
 * @author wenxi.wu@hand-chian.com 2020-12-10
 */
public class TransactionalHelper {

    @Transactional(rollbackFor = Exception.class)
    public void operation(Operation operation) {
        operation.operation();
    }
}
