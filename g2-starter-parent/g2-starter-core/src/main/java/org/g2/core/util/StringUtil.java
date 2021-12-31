package org.g2.core.util;

import org.g2.core.base.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author wuwenxi 2021-05-29
 */
public class StringUtil {


    private static final Logger LOG = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 将异常栈中的内容转化为String
     *
     * @param throwable exception-string
     * @return string
     */
    public static String exceptionString(final Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static String getBeanName(String prefix, Object beanName) {
        return prefix + BaseConstants.Symbol.WELL + beanName;
    }
}
