package com.tqmall.common.exception;

/**
 * Created by jason on 15/9/29.
 * 自定义权限异常
 */
public class PermissionException extends RuntimeException{

    // exception's code
    public String code;
    // exception's message
    public String message;

    public PermissionException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
