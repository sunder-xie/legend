package com.tqmall.legend.common.fileImport;

/**
 * Created by twg on 16/12/2.
 */
public interface ImportFailedMessages {
    static final String DEFAULT_FAILED_MESSAGE = "第【%d】行【%d】列，%s，填写不正确";
    static final String DEFAULT_REPEAT_MESSAGE = "第【%d】行【%s】，%s重复或已存在";
    static final String FAILED_REPEAT_MESSAGE = "【%s】，%s重复或已存在";
    static final String DEFAULT_MOBILE_NOT_ACCOUNT = "第【%d】行【%s】，无此有效的账户信息";
    static final String FAILED_MOBILE_NOT_ACCOUNT = "【%s】，无此有效的账户信息";
    static final String DEFAULT_MOBILE_HAS_MEMBER = "第【%d】行【%s】，账户已办【%d】张卡";
    static final String FAILED_MOBILE_HAS_MEMBER = "【%s】，账户已办【%d】张卡";
    static final String DEFAULT_MOBILE_MORE_ACCOUNT = "第【%d】行【%s】，有多个的账户信息";
    static final String FAILED_MOBILE_MORE_ACCOUNT = "【%s】，有多个的账户信息";

    static final String DEFAULT_MOBILE_HAS_MEMBER_TYPE = "第【%d】行【%s】，账户已办【%s】类型卡";
    static final String FAILED_MOBILE_HAS_MEMBER_TYPE = "【%s】，账户已办【%s】类型卡";
    static final String DEFAULT_MEMBER_NOT_TYPE = "第【%d】行【%s】，无此会员卡类型";
    static final String FAILED_MEMBER_NOT_TYPE = "【%s】，无此会员卡类型";
    static final String DEFAULT_COUPON_NOT_TYPE = "第【%d】行【%s】，无此优惠券类型";
    static final String FAILED_COUPON_NOT_TYPE = "【%s】，无此优惠券类型";
    static final String DEFAULT_COMBO_NOT_TYPE = "第【%d】行【%s】，无此计次卡类型";
    static final String FAILED_COMBO_NOT_TYPE = "【%s】，无此计次卡类型";
    static final String DEFAULT_COMBOINFO_NOT_SERVICE = "第【%d】行【%s】，无此【%s】服务项目";
    static final String FAILED_COMBOINFO_NOT_SERVICE = "【%s】，无此【%s】服务项目";
    static final String DEFAULT_COMBOINFO_SERVICE_COUNT = "第【%d】行【%s】的【%s】项目数量为【%d】大于值 %d ";
    static final String FAILED_COMBOINFO_SERVICE_COUNT = "【%s】的【%s】项目数量为【%d】大于值 %d ";
}
