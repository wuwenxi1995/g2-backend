package org.g2.dynamic.jdbc.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.g2.dynamic.jdbc.aop.annotation.DataRout;
import org.g2.dynamic.jdbc.context.DataRoutHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuwenxi 2023-03-13
 */
@Aspect
public class DataRoutInterceptor {

    private static final Logger log = LoggerFactory.getLogger(DataRoutInterceptor.class);

    @Around(value = "@annotation(dataRout)")
    public Object around(ProceedingJoinPoint joinPoint, DataRout dataRout) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("aspect @DataRout, switch ds-route to {}", dataRout.dsKey());
        }
        try {
            DataRoutHolder.set(dataRout.dsKey());
            return joinPoint.proceed();
        } finally {
            DataRoutHolder.clear();
        }
    }
}
