package com.tqmall.legend.common;

/**
<<<<<<< Updated upstream
 * Created by zwb on 14/10/15.
 */
public enum LegendError {
    PERMISSION_ERROR("-10000", "无权限访问"),
    PARAM_ERROR("-10001", "提交参数错误"),
    COMMON_ERROR("10000", "应用程序异常"),
    COMMON_EMPTY_LIST("10001", "请求获取的数据不存在"),
    CAR_CHECK_ERROR("10002", "车辆信息和客户信息不匹配"),
    PRECHECK_DEL_ERROR("10003", "删除车况预检单失败"),
    ORDER_LOCK("11110", "订单处于锁定状态"),
    SHOP_IS_NOT_B_ACCOUNT("10004", "门店不是B账户");

    private String code;
    private String message;

    private LegendError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
