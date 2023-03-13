package org.g2.dynamic.jdbc.aop.annotation;

/**
 * @author wuwenxi 2023-03-13
 */
public @interface DataRout {

    String dsKey() default "NULL_KEY";
}
