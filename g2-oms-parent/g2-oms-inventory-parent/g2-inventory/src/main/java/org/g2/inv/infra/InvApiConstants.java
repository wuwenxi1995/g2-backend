package org.g2.inv.infra;

import lombok.Data;

/**
 * @author wuwenxi 2022-04-15
 */
public interface InvApiConstants {

    interface ApiResponseCode {

        /**
         * 成功
         */
        ResponseInfo SUCCESS = new ResponseInfo(200, null);

        /**
         * 失败
         */
        ResponseInfo ERROR = new ResponseInfo(500, "服务异常");

        /**
         * 不合法的token
         */
        ResponseInfo INVALID_TOKE = new ResponseInfo(401, "无效的token");

        /**
         * 非法的参数
         */
        ResponseInfo INVALID_PARAM = new ResponseInfo(400, "非法的参数");

    }

    @Data
    class ResponseInfo {

        Integer code;
        String msg;

        ResponseInfo(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
