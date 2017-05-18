package com.tqmall.common;

import java.math.BigDecimal;

/**
 * Created by guangxue on 14-10-17.
 */
public interface Constants {

    /**
     * session中存放的信息
     */
    String SESSION_USER_ID = "SESSION_USER_ID";

    String SESSION_USER_NAME = "SESSION_USER_NAME";

    String SESSION_USER_ROLE = "SESSION_USER_ROLE";

    String SESSION_USER_ROLE_ID = "SESSION_USER_ROLE_ID";

    String SESSION_SHOP_ID = "SESSION_SHOP_ID";

    String SESSION_USER_ROLE_FUNC = "SESSION_USER_ROLE_FUNC";

    String SESSION_USER_IS_ADMIN = "SESSION_USER_IS_ADMIN";

    String SESSION_UUID = "UUID";

    String SESSION_LOGIN_UUID = "LOGIN_UUID";

    String SESSION_REFERER = "REFERER";

    String SESSION_VC_UUID = "VC_UUID";

    String SESSION_SHOP_LEVEL = "SESSION_SHOP_LEVEL";

    String SESSION_USER_GLOBAL_ID = "SESSION_USER_GLOBAL_ID";

    String SESSION_MANAGER_LOGIN_ID = "SESSION_MANAGER_LOGIN_ID";

    String SESSION_ACCOUNT = "SESSION_ACCOUNT";

    String SESSION_ANXIN_INSURANCE_CITY_IS_OPEN = "SESSION_ANXIN_INSURANCE_CITY_IS_OPEN";
    /**
     * 共享中心,0不参加 1参加体系
     */
    String SESSION_SHOP_JOIN_STATUS = "SESSION_SHOP_JOIN_STATUS";
    /**
     * 门店是否使用车间,0：不使用车间 1：使用车间
     */
    String SESSION_SHOP_WORKSHOP_STATUS = "SESSION_SHOP_WORKSHOP_STATUS";
    /**
     * 是否是钣喷中心 true/false
     */
    String BPSHARE = "BPSHARE";

    /**
     * 门店是否为档口版
     */
    String SESSION_SHOP_IS_TQMALL_VERSION = "SESSION_SHOP_IS_TQMALL_VERSION";

    /**
     * 灰度发布模块
     */
    String SESSION_MODULE_KEY_LIST = "SESSION_MODULE_KEY_LIST";
    /**
     * 灰度发布模块信息主键
     */
    String SESSION_MODULE_KEY_VERSION = "SESSION_MODULE_KEY_VERSION";
    /**
     * 灰度发布模块版本是否可切换
     */
    String SESSION_MODULE_KEY_CHOOSE = "SESSION_MODULE_KEY_CHOOSE";
    /**
     * 安心保险：门店选择的模式
     * 模式一：1
     * 模式二：2（包含买服务包送保险、买保险送服务包）
     * 0（包含模式1，2）
     */
    String SESSION_ANXIN_INSURANCE_MODEL = "SESSION_ANXIN_INSURANCE_MODEL";
    String ANXIN_INSURANCE_MODEL_ZERO = "0";//安心保险：模式一、二
    String ANXIN_INSURANCE_MODEL_ONE = "1";//安心保险：模式一
    String ANXIN_INSURANCE_MODEL_TWO = "2";//安心保险：模式一
    Integer ANXIN_INSURANCE_ONE = 1;//安心保险：奖励金模式
    Integer ANXIN_INSURANCE_TWO = 2;//安心保险：买保险送服务包模式
    Integer ANXIN_INSURANCE_THRESS = 3;//安心保险：买服务包送保险模式
    String ANXIN_INSURANCE_MOBILE_CODE = "ANXIN_INSURANCE_MOBILE_CODE";//安心保险服务券核销发送短信验证码
    String MARKETING_GATHER_COUPON_MOBILE_CODE = "MARKETING_GATHER_COUPON_MOBILE_CODE";//集客营销送优惠券短信验证码redis key前缀(加mobile是完整key)

    /**
     * 手机号
     */
    String SESSION_MOBILE = "SESSION_MOBILE";

    /**
     * 角色名
     */
    String SESSION_PVGROLE_NAME = "SESSION_PVGROLE_NAME";

    /**
     * 是否认证
     */
    String SESSION_READYCHECK = "SESSION_READYCHECK";

    /**
     * 出库编码前缀
     */
    String BLUE_OUT_WARHOUSE = "LC";

    /**
     * 出库编码前缀
     */
    String RED_OUT_WARHOUSE = "HC";

    /**
     * 判断对DB的操作是否成功的阀值
     */
    int FAIL_FLAG = 0;

    /**
     * 判断输入值的最小长度
     */
    int STRING_LENGTH_MIN = 3;

    /**
     * 判断输入值的最小长度
     */
    int STRING_LENGTH_MIN_LICENSE = 10;

    /**
     * 判断车架号是否超过一定长度
     */
    int VIN_LENGTH_FLAG = 17;

    /**
     * 数据库int类型字段缺省值
     */
    int DB_DEFAULT_INT = 0;

    /*
     * shopId数据有效性判断
     */ long SHOP_ID_FLAG = 1L;

    /*
     * managerId数据有效性判断
     */ long MANAGER_ID_FLAG = 1L;

    /*
     * rolesId数据有效性判断
     */ long ROLES_ID_FLAG = 1L;

    /*
     * 0标示
     */ long ZERO_FLAG = 0L;

    /**
     * 管理费标签
     */
    String GLF = "GLF";

    /**
     * 直营店等级
     */
    int SHOP_DIRECT_LEVEL = 9;

    /**
     * 合作热线
     */
    String JOIN_PHONE = "400-9937-288";

    /**
     * 合作短信模版
     */
    String REGISTER_SMS_ACTION_TPL = "legend_join_register";
    String APPOINT_CODE_TPL = "appoint_code";

    /**
     * APP预约单短信通知模版
     */
    String APP_APPOINT_SMS_TPL = "legend_app_appoint";

    /**
     * APP预约单取消短信通知模版
     */
    String APP_APPOINT_CANCEL_SMS_TPL = "legend_app_appoint_cancel";

    /**
     * 短信失效时间，单位为毫秒,默认为30分钟
     */
    Integer SMS_DEADLINE = 1000 * 60 * 30;

    /**
     * 合作手机号
     */
    String SESSION_JOIN_MOBILE = "SESSION_JOIN_MOBILE";

    /**
     * 合作店UUID
     */
    String SESSION_JOIN_UUID = "SESSION_JOIN_UUID";

    /**
     * sign的前面的字段
     */
    // String SIGN_PRE = "abcdef";
    String SIGN_PRE = "0d0304d5b";

    /**
     * sign的后面的字段
     */
    // String SIGN_POST = "1234567";
    String SIGN_POST = "89e9e1348bef7";


    /**
     * 工单锁定 标志
     */
    String ORDER_LOCK = "Y";


    /**
     * 营销短信模版
     */
    String LEGEND_MARKETING_SMS_TPL = "legend_marketing_sms";

    /**
     * 云修车主领取优惠券验证码短信模版
     */
    String LEGEND_COUPON_SMS_TPL = "legend_coupon_mobile_code";//内容:【淘汽云修】验证码为${code}，请输入验证码领取优惠券，{$shopName}。

    String LEGEND_BANK_BIND_CODE = "bank_bind_code";

    /**
     * 淘汽云修工单结算使用其他客户的优惠券和会员卡结算
     */
    String LEGEND_ORDER_SETTLEMENT_TPL = "legend_order_settlement";

    // 预约提醒天数，默认为3
    Integer APPOINT_NOTICE_DAY = 2;

    // 回访提醒天数，默认为2
    Integer FEEDBACK_NOTICE_DAY = 2;

    // 保险提醒天数，默认为30
    Integer INSURANCE_NOTICE_DAY = 15;

    // 年审提醒天数，默认为30
    Integer CAR_AUDIT_NOTICE_DAY = 15;

    // 保养提醒天数，默认为30
    Integer KEEPUP_NOTICE_DAY = 30;
    Integer KEEPUP_SECOND_NOTICE_DAY = 15;

    //生日提醒天数，默认1
    Integer BIRTHDAY_NOTICE_DAY = 1;

    //流失客户提醒天数，默认15天
    Integer LOST_CUSTOMER_NOTICE_DAY = 15;

    //提醒过期保存天数，默认15
    Integer LOST_SAVE_DAY = 15;

    /**
     * 2C-APP 推送约定的type 属性值
     */
    String APPOINT_CREATE = "1";// 预约单创建待确认

    String ORDER_CREATE = "2";// 工单创建

    String ORDER_FINISH = "3";// 工单完成

    String ORDER_SETTLE = "4";// 工单结算

    String ORDER_INVALID = "5";// 工单无效

    String APPOINT_CONFIRM = "6";// 预约单确认

    String APPOINT_C_CANCEL = "7";// C端预约单取消

    String APPOINT_B_CANCEL = "8";// B端预约单取消

    String APPOINT_WECHAT_CANCEL = "9";// 微信端预约单取消

    /**
     * 门店营销活动其地址被访问的前缀，统计访问量用的
     */
    String MARKETING_VISIT_COUNT_PREFIX = "VISIT_COUNT_KEY";

    /**
     * 常用车牌前缀
     */
    String COMM_CAR_BRAND_PREFIX = "COMM_CAR_BRAND_KEY";

    /**
     * create by jason 2015-09-28
     * 推送到车主app,门店活动消息type
     */
    String CZ_ACTIVITY_RELEASE = "1";//车主活动提交待审核
    String CZ_ACTIVITY_DEL = "2";//车主活动删除
    String CZ_ACTIVITY_OFFLINE = "3";//车主活动下线

    String CZ_NOTICE_RELEASE = "4";//车主公告提交待审核
    String CZ_NOTICE_DEL = "5";//车主公告删除
    String CZ_NOTICE_OFFLINE = "6";//车主公告下线

    /**
     * 公告通知 cookie存在时间
     */
    String PUBLIC_NOTICE_TIME = "PUBLIC_NOTICE_TIME";
    Integer NOTICE_CACHE_TIME = 7 * 24 * 60 * 60;

    /**
     * 余额 最小提现金额
     */
    BigDecimal LEAST_WITHDRAW_AMOUNT = BigDecimal.valueOf(15.00);
    /**
     * 提现到账时间提醒
     */
    String WITHDRAW_NOTICE = "每周进行打款";

    Long NOTE_APPOINT_CONF_TYPE = 2L;
    Long NOTE_VISIT_CONF_TYPE = 3L;
    Long NOTE_INSURANCE_CONF_TYPE = 4L;
    Long NOTE_AUDIT_CONF_TYPE = 5L;
    Long NOTE_KEEPUP_CONF_TYPE = 6L;
    Long NOTE_BIRTHDY_CONF_TYPE = 7L;
    Long NOTE_LOSTCUSTOMER_CONF_TYPE = 8L;
    Long NOTE_SIGNINFO_CONF_TYPE = 9L;


    /**
     * 导入控制excel单次读取的最大行数
     */
    Integer EXCEL_ROW_LIMIT = 1800;

    Integer EXCEL_EXPORT_LIMIT = 500;

    /**
     * 在蒲公英端创建客户 来源
     */
    String CUST_SOURCE = "LEGEND";

    /**
     * 寄售活动
     */
    String JSFW = "JSFW";

    /*
     * 销售专用车牌号
     */
    String SELL_DEFAULT_LICENSE = "销售专用车牌";

    /**
     * 洗车专用车牌
     */
    String CARWASH_DEFAULT_LICENSE = "洗车专用车牌";

    /**
     * 维修工人数
     */
    Integer MAX_WORKER_NUMBER = 8;

    /**
     * 分页查询每页最大数量
     */
    Integer MAX_PAGE_SIZE = 1000;

    /**
     * 分页查询每页中等数量
     */
    Integer MED_PAGE_SIZE = 100;

    /**
     * redis中门店上下班时间缓存KEY
     */
    String OPEN_TIME_KEY = "WORKTIMEVO_OPEN_TIME";
    String CLOSE_TIME_KEY = "WORKTIMEVO_CLOSE_TIME";
    String NOONBREAK_START_TIME_KEY = "WORKTIMEVO_NOONBREAK_START_TIME";
    String NOONBREAK_END_TIME_KEY = "WORKTIMEVO_NOONBREAK_END_TIME";


    /**
     * 档口版本
     */
    Integer SHOP_LEVEL_TQMALL_VERSION = 6;
    /**
     * 云修版本
     */
    Integer SHOP_LEVEL_YUNXIU_VERSION = 9;

    String SMS_SEND_LOCK_KEY = "SMS_SEND_LOCK_KEY";

    /**
     * 10分钟的毫秒数
     */
    long AVOID_LOGIN_TIME = 600000;

    // excel导出 密码提示
    String EXPORTPASSWORD_SMS_TPL = "legend_export_password";

    /**
     * 库存共享
     */
    String SESSION_WAREHOUSE_SHARE_ROLE = "SESSION_WAREHOUSE_SHARE_ROLE";
    /**
     * 门店开启的打印单据配置信息
     * user:lilige
     */
    String SHOP_OPEN_PRINT = "SHOP_OPEN_PRINT";

    /**
     * 打印新老版本
     * user:lilige
     */
    String SHOP_PRINT_VERSION = "SHOP_PRINT_VERSION";

    // 一个手机号多个门店账号的特殊code
    String MORE_USER_INFO_CODE = "20000";

    //门店销售 用户登陆短信模板
    String Legend_SELL_SHOP_LOGIN_SMS_TPL = "legend_sell_shop_login";

    //手机发送短信间隔时间 目前1分钟
    int SEND_MOBILE_CODE_TIME = 60 * 1000;

    //售卖门店有效期 3年
    int SELL_SHOP_EFFECTIVE_YEAR = 3;

    //手机登陆
    String SESSION_MOBILE_LOGIN= "SESSION_MOBILE_LOGIN";

    /**
     * 是否开通门店微信公众号 0未开通,1已开通
     */
    String IS_WECHAT_SHOP = "IS_WECHAT_SHOP";
    /**
     * 是否是样板店
     */
    String YBD = "YBD";

    /**
     * 用户操作字典表
     */
    String USER_OPERATE_DICT = "USER_OPERATE_DICT";
}
