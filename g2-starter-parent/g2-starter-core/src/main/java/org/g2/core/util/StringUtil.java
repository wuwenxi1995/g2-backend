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
     * @param e exception-string
     * @return string
     */
    public static String exceptionString(final Exception e) {
        try {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "exceptionString> " + sw.toString() + "\r\n";
        } catch (final Exception e2) {
            final String errorMsg = "Common exception util error occured :" + e2.getMessage();
            LOG.error(errorMsg, e);
            return errorMsg;
        }
    }

    public static String getBeanName(String prefix, String beanName) {
        return prefix + BaseConstants.Symbol.WELL + beanName;
    }
}
