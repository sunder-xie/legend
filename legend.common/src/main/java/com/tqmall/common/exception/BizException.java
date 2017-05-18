package com.tqmall.common.exception;

/**
 * 业务异常
 * Created by Minutch on 14-8-28.
 */
@SuppressWarnings("serial")
public class BizException extends RuntimeException {

    private String code;//错误码

    private String errorMessage;//错误消息

    private Object[] param;//灾难现场入参

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
        this.errorMessage = message;
    }


    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
    }

    public BizException(String message, String code, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
        this.code = code;
    }

    public BizException(String message, String code, Object[] param, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
        this.code = code;
        this.param = param;
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    protected BizException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object[] getParam() {
        return param;
    }

    public void setParam(Object[] param) {
        this.param = param;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
