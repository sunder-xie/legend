package com.tqmall.common.exception;

/**
 * 可查业务异常、强制捕获、记录日志
 * <p/>
 * Created by dongc on 15/7/23.
 */
public class BusinessCheckedException extends Exception {

    // exception's code
    public String code;
    // exception's message
    public String message;

    public BusinessCheckedException(String code, String message) {
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

    @Override
    public String toString() {
        return "BusinessException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
