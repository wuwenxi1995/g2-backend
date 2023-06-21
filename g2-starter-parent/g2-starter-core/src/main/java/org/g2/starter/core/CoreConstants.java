package org.g2.starter.core;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * 核心常量
 *
 * @author wuwenxi 2022-04-09
 */
public interface CoreConstants {

    interface ErrorCode {
        String BATCH_OPERATION = "error.batch_operation";
    }

    interface PlatformType {
        String TM = "TM";
        String JD = "JD";
        String OW = "OW";
        String JFSC = "JFSC";

        String SAP = "SAP";
        String POS = "POS";

        String LOV_CODE = "O2MD.PLATFORM_TYPE";
    }

    interface ProcessStatus {
        String PENDING = "PENDING";
        String SUCCESS = "SUCCESS";
        String ERROR = "ERROR";
        String SKIP = "SKIP";

        String LOV_CODE = "O2MD.PROCESSING_STATUS";
    }

    interface BooleanFlag {
        int ENABLE = 1;
        int NOT_ENABLE = 0;
        int ACTIVE = 1;
        int NOT_ACTIVE = 0;
        String YES = "Y";
        String NO = "N";
    }

    /**
     * 地址类型
     */
    interface AddressType {
        /**
         * 国家
         */
        String COUNTRY = "COUNTRY";
        /**
         * 大区
         */
        String AREA = "AREA";
        /**
         * 省
         */
        String REGION = "REGION";
        /**
         * 市
         */
        String CITY = "CITY";
        /**
         * 区
         */
        String DISTRICT = "DISTRICT";

        String LOV_CODE = "O2MD.ADDRESS_TYPE";
    }

    /**
     * 区间类型
     */
    interface RangeType {
        /**
         * 开区间，如 ()
         */
        int BLANK = 0;

        /**
         * 半开半闭区间，如 (]
         */
        int SEMI_CLOSED = 1;

        /**
         * 闭合区间，如 []
         */
        int CLOSED = 2;
    }

    /**
     * Terminal 终端类型
     */
    interface TerminalType {
        /**
         * PC端
         */
        String PC = "PC";
        /**
         * 移动应用
         */
        String APP = "APP";
        /**
         * 移动WEB
         */
        String H5 = "H5";
        /**
         * 微信
         */
        String WE_CHAT = "WE_CHAT";

        String POS = "POS";
        String WECHAT = "WECHAT";
        String MINI_PROGRAM = "MINI_PROGRAM";
        String JD = "JD";
        String TMALL = "MALL";

        String LOV_CODE = "O2MD.TERMINAL_TYPE";
    }

    interface DomainRegex {

        /**
         * url 正则表达式
         */
        String URL_REGEX = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        /**
         * 匹配域名正则表达式
         */
        String DOMAIN_REGEX = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+(:[0-9]+)?|(?:ww\u200C\u200Bw.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?\u200C\u200B(?:[\\w]*))?)";

        /**
         * 根据域名绝对路径改相对路径（目录结构）
         *
         * @param domainUrl 域名绝对路径
         * @return 获取相对路径（目录结构）
         */
        static String domainReplace(final String domainUrl) {
            return StringUtils.removeAll(domainUrl, DOMAIN_REGEX);
        }
    }

    interface CsgLockRegex {
        /**
         * Consignment LOCK KEY
         */
        String CONSIGNMENT_LOCK_FORMAT = "csg:lock:consignment:%s";

        /**
         * 锁等待时间，单位：秒 TimeUnit.SECONDS
         */
        Integer LOCK_WAIT_TIME = 1;

        /**
         * 锁释放时间，单位：秒 TimeUnit.SECONDS
         */
        Integer LOCK_RELEASE_TIME = 180;
    }

    /**
     * 角色类型类型
     */
    interface RoleType {
        /**
         * 供应商
         */
        String SUPPLIER = "SUPPLIER";

        /**
         * 公司
         */
        String COMPANY = "COMPANY";

        /**
         * 平台
         */
        String PLATFORM = "PLATFORM";
    }
}
