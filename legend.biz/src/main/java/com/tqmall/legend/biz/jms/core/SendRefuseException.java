package com.tqmall.legend.biz.jms.core;

public class SendRefuseException extends Exception {
    private static final long serialVersionUID = -8150027102635597118L;

    public SendRefuseException(String msg) {
        super(msg);
    }

    public SendRefuseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SendRefuseException(Throwable cause) {
        super(cause);
    }
}