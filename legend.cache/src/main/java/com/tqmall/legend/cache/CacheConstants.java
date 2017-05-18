package com.tqmall.legend.cache;

/**
 * 缓存常量
 * Created by lixiao on 15/12/8.
 */
public interface CacheConstants {

    //门店key失效时间,默认7天，解决门店反馈操作一段时间自动退出问题
    public static final Integer SHOP_KEY_EXP_TIME = 604800;
    //登录验证码key失效时间，默认10分钟
    public static final Integer LOGIN_VC_KEY_EXP_TIME = 600;
    //招商手机key失效时间，默认30分钟
    public static final Integer INVEST_MOBILE_KEY_EXP_TIME = 1800;
    //业务字典key失效时间,默认12小时
    public static final Integer BIZ_DIR_KEY_EXP_TIME = 43200;
    //淘汽购物车key失效时间，默认7天
    public static final Integer TQ_CART_KEY_EXP_TIME = 604800;
    //抽奖key失效时间，默认30s
    public static final Integer LOTTERY_KEY_EXP_TIME = 30;
    //业务编号key失效时间，默认30s
    public static final Integer BIZ_SN_KEY_EXP_TIME = 30;
    //营销活动在线转发key失效时间
    public static final Integer MARKETING_RELAY_KEY_EXP_TIME = 18000;
    //预约单数量key失效时间，默认2小时
    public static final Integer APPOINT_NUM_KEY_EXP_TIME = 7200;
    //常用车型key失效时间，默认7天
    public static final Integer COMM_CART_MODEL_KEY_EXP_TIME = 604800;
    //门店活动模板二维码失效时间，默认7天
    public static final Integer MARKETING_TEMPLATE_URL_KEY_EXP_TIME = 604800;
    //门店营销活动其地址被访问计数的失效时间，默认24小时
    public static final Integer VISIT_COUNT_KEY_EXP_TIME = 86400;
    //淘汽金融购物车key失效时间，默认24小时
    public static final Integer TQ_FINANCE_CART_KEY_EXP_TIME = 86400;
    //预检单信息失效时间，默认7天
    public static final Integer PRECHECK_KEY_EXP_TIME = 604800;
    //免登陆用户失效时间，默认12小时
    public static final Integer AVOID_LOGIN_KEY_EXP_TIME = 43200;
    //安全登陆cookie/缓存失效时间，默认1分钟
    public static final Integer SECURE_LOGIN_KEY_EXP_TIME = 60;
    //安全二维码扫码登陆失效时间
    public static final Integer SECURE_QRCODE_LOGIN_KEY_EXP_TIME = 86400;
    //通用手机短信验证码key失效时间，默认10分钟
    public static final Integer MOBILE_CODE_KEY_EXP_TIME = 600;
    //售卖门店手机登陆cookie/缓存失效时间, 默认45分钟
    public static final Integer SELL_MOBILE_LOGIN_KEY_EXP_TIME = 2700;
    //用户操作行为字典数据失效时间，30天
    public static final Integer USER_OPERATE_DICT_KEY_EXP_TIME = 2592000;


}
