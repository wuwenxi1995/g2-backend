package org.g2.starter.core.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author wenxi.wu@hand-china.com 2020-08-07
 */
public class Results {

    private static final ResponseEntity NO_CONTENT;
    private static final ResponseEntity ERROR;
    private static final ResponseEntity INVALID;

    static {
        NO_CONTENT = new ResponseEntity(HttpStatus.NO_CONTENT);
        ERROR = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        INVALID = new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<?> success(T data) {
        return data == null ? NO_CONTENT : ResponseEntity.ok(data);
    }

    public static ResponseEntity success() {
        return NO_CONTENT;
    }

    public static ResponseEntity error() {
        return ERROR;
    }

    public static <T> ResponseEntity<?> error(T data) {
        return data == null ? error() : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
    }

}
