package org.g2.starter.core.base;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Locale;

/**
 * 公共常量
 *
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
public interface BaseConstants {

    Long DEFAULT_TENANT_ID = 0L;
    Long ANONYMOUS_USER_ID = -1L;
    String ANONYMOUS_USER_NAME = "ANONYMOUS";
    String PAGE = "0";
    String SIZE = "10";
    String PAGE_FIELD_NAME = "page";
    String SIZE_FIELD_NAME = "size";
    int NEGATIVE_ONE = -1;
    int PAGE_NUM = 0;
    int PAGE_SIZE = 10;
    String FIELD_BODY = "body";
    String FIELD_CONTENT = "content";
    Locale DEFAULT_LOCALE = Locale.CHINA;
    String DEFAULT_LOCALE_STR = Locale.CHINA.toString();
    String FIELD_MSG = "message";
    String FIELD_FAILED = "failed";
    String FIELD_SUCCESS = "success";
    String FIELD_ERROR_MSG = "errorMsg";
    String DEFAULT_CHARSET = "UTF-8";
    String DEFAULT_ENV = "dev";
    ObjectMapper MAPPER = new ObjectMapper();
    String DEFAULT_CROWN_CODE = "+86";
    String DEFAULT_TIME_ZONE = "GMT+8";
    String PATH_SEPARATOR = "|";
    String DEFAULT_USER_TYPE = "P";

    interface Symbol {
        String SIGH = "!";
        String AT = "@";
        String WELL = "#";
        String DOLLAR = "$";
        String RMB = "￥";
        String SPACE = " ";
        String LB = System.getProperty("line.separator");
        String PERCENTAGE = "%";
        String AND = "&";
        String STAR = "*";
        String MIDDLE_LINE = "-";
        String LOWER_LINE = "_";
        String EQUAL = "=";
        String PLUS = "+";
        String COLON = ":";
        String SEMICOLON = ";";
        String COMMA = ",";
        String POINT = ".";
        String SLASH = "/";
        String VERTICAL_BAR = "|";
        String DOUBLE_SLASH = "//";
        String BACKSLASH = "\\";
        String QUESTION = "?";
        String LEFT_BIG_BRACE = "{";
        String RIGHT_BIG_BRACE = "}";
        String LEFT_MIDDLE_BRACE = "[";
        String RIGHT_MIDDLE_BRACE = "]";
        String BACKQUOTE = "`";
    }

    interface HeaderParam {
        String REQUEST_HEADER_PARAM_PREFIX = "param-";
    }

    interface ErrorCode {
        String DATA_INVALID = "error.data_invalid";
        String NOT_FOUND = "error.not_found";
        String ERROR = "error.error";
        String ERROR_NET = "error.network";
        String OPTIMISTIC_LOCK = "error.optimistic_lock";
        String DATA_EXISTS = "error.data_exists";
        String DATA_NOT_EXISTS = "error.data_not_exists";
        String FORBIDDEN = "error.forbidden";
        String ERROR_CODE_REPEAT = "error.code_repeat";
        String ERROR_NUMBER_REPEAT = "error.number_repeat";
        String ERROR_SQL_EXCEPTION = "error.sql_exception";
        String NOT_LOGIN = "error.not_login";
        String NOT_NULL = "error.not_null";
    }

    interface Digital {
        int NEGATIVE_ONE = -1;
        int ZERO = 0;
        int ONE = 1;
        int TWO = 2;
        int FOUR = 4;
        int EIGHT = 8;
        int SIXTEEN = 16;
    }

    interface Flag {
        Integer YES = 1;
        Integer NO = 0;
    }

    interface Pattern {
        String DATE = "yyyy-MM-dd";
        String DATETIME = "yyyy-MM-dd HH:mm:ss";
        String DATETIME_MM = "yyyy-MM-dd HH:mm";
        String DATETIME_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
        String TIME = "HH:mm";
        String TIME_SS = "HH:mm:ss";
        String SYS_DATE = "yyyy/MM/dd";
        String SYS_DATETIME = "yyyy/MM/dd HH:mm:ss";
        String SYS_DATETIME_MM = "yyyy/MM/dd HH:mm";
        String SYS_DATETIME_SSS = "yyyy/MM/dd HH:mm:ss.SSS";
        String NONE_DATE = "yyyyMMdd";
        String NONE_DATETIME = "yyyyMMddHHmmss";
        String NONE_DATETIME_MM = "yyyyMMddHHmm";
        String NONE_DATETIME_SSS = "yyyyMMddHHmmssSSS";
        String CST_DATETIME = "EEE MMM dd HH:mm:ss 'CST' yyyy";
        String NONE_DECIMAL = "0";
        String ONE_DECIMAL = "0.0";
        String TWO_DECIMAL = "0.00";
        String TB_NONE_DECIMAL = "#,##0";
        String TB_ONE_DECIMAL = "#,##0.0";
        String TB_TWO_DECIMAL = "#,##0.00";
    }
}
