package org.g2.starter.dynamic.jdbc.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.g2.starter.dynamic.jdbc.aop.annotation.DataRoute;
import org.g2.starter.dynamic.jdbc.context.DataRoutHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author wuwenxi 2023-03-13
 */
@Aspect
public class DataRoutInterceptor {

    private static final Logger log = LoggerFactory.getLogger(DataRoutInterceptor.class);

    private final DataSourceTransactionManager transactionManager;

    public DataRoutInterceptor(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Around(value = "@annotation(dataRout)")
    public Object around(ProceedingJoinPoint joinPoint, DataRoute dataRout) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("aspect @DataRout, switch ds-route to {}", dataRout.dsKey());
        }
        TransactionStatus transaction = null;
        try {
            DataRoutHolder.set(dataRout.dsKey());
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            // 避免分布式事务，新开启事务只允许为只读
            definition.setReadOnly(true);
            definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            transaction = transactionManager.getTransaction(definition);
            return joinPoint.proceed();
        } finally {
            DataRoutHolder.clear();
            if (transaction != null) {
                transactionManager.commit(transaction);
            }
        }
    }
}
