package com.tqmall.error;

import com.tqmall.zenith.errorcode.ErrorCode;

/**
 * Created by wanghui on 11/30/15.
 */
public class LegendErrorCode {
    /**
     * 客户模块-----------------
     */
    public static final ErrorCode CAR_LICENSE_PREFIX_EX =
            LegendErrorHelper.newLegendFatal().setDetailCode("0003").setMessageFormat("从legend中获取所有省份的简称列表失败").genErrorCode();
    public static final ErrorCode CAR_SERVICE_EMPTY_EX =
            LegendErrorHelper.newLegendFatal().setDetailCode("0004").setMessageFormat("获取数据失败").genErrorCode();
    public static final ErrorCode CAR_NULL_EX =
            LegendErrorHelper.newLegendFatal().setDetailCode("0005").setMessageFormat("车辆不存在").genErrorCode();
    public static final ErrorCode CAR_CHECK_EX =
            LegendErrorHelper.newLegendFatal().setDetailCode("0010").setMessageFormat("{}").genErrorCode();
    public static final ErrorCode CAR_TAG_DELETE_EX =
            LegendErrorHelper.newLegendFatal().setDetailCode("0011").setMessageFormat("客户标签删除失败").genErrorCode();
    public static final ErrorCode CAR_TAG_ADD_EX =
            LegendErrorHelper.newLegendFatal().setDetailCode("0012").setMessageFormat("客户标签添加失败").genErrorCode();
    public static final ErrorCode CAR_IMG_EX =
            LegendErrorHelper.newLegendFatal().setDetailCode("0013").setMessageFormat("暂无图片").genErrorCode();
    /**
     * 工单模块---------------
     */
    public static final ErrorCode ORDER_LIST_EX =
            LegendErrorHelper.newLegendFatal().setDetailCode("0006").setMessageFormat("分页偏移量={}从legend中获得工单列表失败").genErrorCode();
    public static final ErrorCode ORDER_NULL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0009").setMessageFormat("根据order_id:{}获取保险维修单审核状态数据为空").genErrorCode();
    public static final ErrorCode ORDER_NOT_FIND_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0010").setMessageFormat("工单不存在").genErrorCode();
    public static final ErrorCode ORDER_SERVICE_NULL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0011").setMessageFormat("工单对应的服务不存在").genErrorCode();
    public static final ErrorCode ORDER_COPY_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0012").setMessageFormat("工单转委托单对象转换异常").genErrorCode();
    public static final ErrorCode GET_CHANNEL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0013").setMessageFormat("获取渠道商列表失败").genErrorCode();
    public static final ErrorCode NO_SERVICE_CAN_PROXY_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0014").setMessageFormat("没有服务可以委托").genErrorCode();
    /**
     * 客户营销模块
     */
    public static final ErrorCode MARKETING_CUSTOMER_STAT_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0007").setMessageFormat("客户营销多纬度查询客户信息失败,入参:{}").genErrorCode();

    public static final ErrorCode MARKETING_CUSTOMER_SUMMARY_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0008").setMessageFormat("客户营销汇总信息查询失败,门店id:{}").genErrorCode();

    /**
     * dubbo服务模块
     */
    public static final ErrorCode DUBBO_SERVICE_SELECT_NULL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0020").setMessageFormat("dubbo服务：根据模板服务id获取淘汽服务信息为空或已经下架,入参:{}").genErrorCode();
    public static final ErrorCode DUBBO_CUSTOMER_CAR_NULL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0021").setMessageFormat("dubbo服务：门店客户车辆创建传递的参数为空,入参:{}").genErrorCode();
    public static final ErrorCode DUBBO_SOURCE_NULL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0022").setMessageFormat("dubbo服务：系统来源为空").genErrorCode();
    public static final ErrorCode DUBBO_PARAM_NULL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0023").setMessageFormat("dubbo服务：参数为空").genErrorCode();
    public static final ErrorCode DUBBO_PRC_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0024").setMessageFormat("dubbo服务：没有提供者").genErrorCode();

    /**
     * 云修金融finance
     * 简称FC
     */
    public static final ErrorCode PURCHASE_COUNT_NULL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0030").setMessageFormat("商品采购数量为空").genErrorCode();

    public static final ErrorCode GOODS_ID_NULL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0031").setMessageFormat("商品id为空").genErrorCode();

    public static final ErrorCode CLEAN_CART_FAIL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0033").setMessageFormat("清空购物车失败").genErrorCode();

    public static final ErrorCode CREATE_PURCHASE_PLAN_FAIL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0034").setMessageFormat("创建采购清单失败").genErrorCode();

    public static final ErrorCode ADD_GOODS_TO_CART_FAIL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0035").setMessageFormat("添加商品到购物车失败").genErrorCode();

    public static final ErrorCode GOODS_ACT_ID_NULL_FAIL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0036").setMessageFormat("商品活动id为空").genErrorCode();

    public static final ErrorCode GOODS_GROUP_ID_NULL_FAIL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0037").setMessageFormat("商品组织id为空").genErrorCode();

    public static final ErrorCode FC_UC_SHOP_ID_NULL_FAIL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0038").setMessageFormat("根据UC的shopId:{}获取不到门店信息").genErrorCode();

    public static final ErrorCode FC_NO_SUBMITTED_PURCHASE_FAIL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0039").setMessageFormat("该门店没有已提交的采购清单").genErrorCode();

    public static final ErrorCode FC_GET_CREDIT_FAIL_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0040").setMessageFormat("调用账务中心获取额度信息接口返回值不能为空").genErrorCode();

    public static final ErrorCode FC_AMOUNT_IS_NOT_IN_THE_MIN_AND_MAX_ERROR_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0041").setMessageFormat("清单总金额不在额度上下限内").genErrorCode();

    public static final ErrorCode FC_PURCHASE_HAS_CREATED_WARN_EX =
            LegendErrorHelper.newLegendWarn().setDetailCode("0042").setMessageFormat("本月采购清单已创建，请修改采购清单").genErrorCode();

    public static final ErrorCode FC_AMOUNT_HAS_USED_WARN_EX =
            LegendErrorHelper.newLegendWarn().setDetailCode("0043").setMessageFormat("您的额度已使用").genErrorCode();

    public static final ErrorCode FC_AMOUNT_HAS_FAILED_WARN_EX =
            LegendErrorHelper.newLegendWarn().setDetailCode("0044").setMessageFormat("您的额度已失效").genErrorCode();

    public static final ErrorCode FC_AMOUNT_IS_APPLYING_WARN_EX =
            LegendErrorHelper.newLegendWarn().setDetailCode("0045").setMessageFormat("您的额度在申请中").genErrorCode();

    public static final ErrorCode FC_AMOUNT_HAS_FROZEN_WARN_EX =
            LegendErrorHelper.newLegendWarn().setDetailCode("0046").setMessageFormat("您的额度已冻结").genErrorCode();

    public static final ErrorCode GOODS_INFO_NULL_ERROR_EX =
            LegendErrorHelper.newLegendError().setDetailCode("0047").setMessageFormat("获取商品详信息失败").genErrorCode();

    public static final ErrorCode GET_GOODS_FROM_CACHE_FAIL_EX =
            LegendErrorHelper.newLegendFatal().setDetailCode("0048").setMessageFormat("从缓存中获取采购商品信息失败").genErrorCode();

    //调用电商接口判断下采购清单时间接口失败
    public static final ErrorCode GET_PURCHASE_TIME_FAIL =
            LegendErrorHelper.newLegendError().setDetailCode("0049").setMessageFormat("采购清单时间异常").genErrorCode();

    public static final ErrorCode NOT_PURCHASE_TIME =
            LegendErrorHelper.newLegendWarn().setDetailCode("0050").setMessageFormat("未到采购时间").genErrorCode();

    public static final ErrorCode GET_PURCHASE_PLANS_FAIL =
            LegendErrorHelper.newLegendError().setDetailCode("0051").setMessageFormat("获取采购清单数据异常").genErrorCode();

    public static final ErrorCode PURCHASE_PLAN_CHANGE_ORDER =
            LegendErrorHelper.newLegendWarn().setDetailCode("0052").setMessageFormat("本月已有采购订单").genErrorCode();

    public static final ErrorCode FC_AMOUNT_IS_APPLYING_FAIL =
            LegendErrorHelper.newLegendWarn().setDetailCode("0053").setMessageFormat("您的额度申请失败").genErrorCode();

    public static final ErrorCode FC_AMOUNT_NOT_APPLYING =
            LegendErrorHelper.newLegendWarn().setDetailCode("0054").setMessageFormat("您还没有申请额度").genErrorCode();

    public static final ErrorCode PURCHASE_CITY_DIFFERENT =
            LegendErrorHelper.newLegendWarn().setDetailCode("0055").setMessageFormat("城市站不一致，请切换城市站，或删除采购清单，重新下单").genErrorCode();

    //调用判断清单城市站和当前城市站是否一致接口失败
    public static final ErrorCode PURCHASE_CITY_FAIl = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0056").setMessageFormat("修改采购清单异常！").genErrorCode();

    public static final ErrorCode RETURNS_TIME_ERROR = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0057").setMessageFormat("未到换货申请时间").genErrorCode();

    public static final ErrorCode RETURNS_APPLY_EXIST = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0058").setMessageFormat("本月已提交换货申请").genErrorCode();

    public static final ErrorCode RETURNS_PURCHASE_PALN_APPLY = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0059").setMessageFormat("需要提交采购清单").genErrorCode();

    //调用判断是否已有换货申请和是否为可换货时间接口失败
    public static final ErrorCode RETURNS_CAN_APPLY_FAIL = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0060").setMessageFormat("进入换货页面异常").genErrorCode();

    //调用判断是否下过采购清单接口失败
    public static final ErrorCode RETURNS_PURCHASE_PLAN_FAIL = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0061").setMessageFormat("进入换货页面失败").genErrorCode();

    public static final ErrorCode RETURNS_NO_VERIFYPARAM = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0062").setMessageFormat("退货商品信息为空").genErrorCode();

    public static final ErrorCode RETURNS_NO_SHOP = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0063").setMessageFormat("根据退货商品信息中的userGlobalId查不到相应门店").genErrorCode();

    //调用电商查询申请列表接口异常
    public static final ErrorCode RETURNS_GET_APPLY_NULL = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0064").setMessageFormat("获取换货申请单列表异常").genErrorCode();

    //调用电商查询申请列表接口失败
    public static final ErrorCode RETURNS_GET_APPLY_FAIL = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0065").setMessageFormat("获取换货申请单列表失败").genErrorCode();

    //电商查询申请列表接口dubbo服务未启动
    public static final ErrorCode RETURNS_GET_APPLY_DUBBO_FAIL = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0066").setMessageFormat("服务器错误").genErrorCode();

    public static final ErrorCode RETURNS_NO_GOODS_ID = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0067").setMessageFormat("退货商品信息没有填写商品id").genErrorCode();

    public static final ErrorCode RETURNS_NO_GOODS_NUM = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0068").setMessageFormat("退货商品信息没有填写退货数量").genErrorCode();

    public static final ErrorCode PURCHASE_PAYMENT_FAIl = LegendErrorHelper.newLegendWarn()
            .setDetailCode("0071").setMessageFormat("进入农行还款页面失败").genErrorCode();

    public static final ErrorCode PURCHASE_PAYMENT_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("0072").setMessageFormat("调用农行还款接口失败").genErrorCode();

    /**
     * 门店实力排行榜模块
     */
    public static final ErrorCode SHOP_RANKING_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("0060").setMessageFormat("{}月门店排名信息不存在").genErrorCode();
    public static final ErrorCode RANKING_SHOP_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("0061").setMessageFormat("店铺信息错误:{}").genErrorCode();
    public static final ErrorCode REPAIR_INTELLIGENCE_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("0062").setMessageFormat("维修资质不能为空").genErrorCode();
    public static final ErrorCode MAJOR_CAR_BRAND_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("0063").setMessageFormat("主修品牌不能为空").genErrorCode();
    public static final ErrorCode MANAGE_COUNT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("0064").setMessageFormat("员工数量错误:{}").genErrorCode();
    public static final ErrorCode STORE_AREA_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("0065").setMessageFormat("门店面积错误:{}").genErrorCode();
    public static final ErrorCode STATION_CNT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("0066").setMessageFormat("工位数量错误:{}").genErrorCode();
    public static final ErrorCode REG_TECH_CNT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("0067").setMessageFormat("技师注册数量错误:{}").genErrorCode();
    public static final ErrorCode COMPUTER_SHOP_AREA_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("0068").setMessageFormat("门店所属区域不能为空").genErrorCode();
    public static final ErrorCode COMPUTER_SHOP_BUSINESS_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("0069").setMessageFormat("门店主营业务不能为空").genErrorCode();
    public static final ErrorCode COMPUTER_MONTHLY_TURNOVER =
            LegendErrorHelper.newLegendError().setDetailCode("0070").setMessageFormat("月营业额错误:{}").genErrorCode();
    public static final ErrorCode SHOP_PAINT_CNT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("0071").setMessageFormat("烤漆房数量错误:{}").genErrorCode();
    public static final ErrorCode COMPUTER_ORDER_CNT =
            LegendErrorHelper.newLegendError().setDetailCode("0072").setMessageFormat("工单数量错误:{}").genErrorCode();
    public static final ErrorCode SHOP_RANK_SERVICE_ITEM_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("0073").setMessageFormat("员工产值错误:{}").genErrorCode();
    public static final ErrorCode COMPUTER_SHOP_NAME_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("0074").setMessageFormat("门店名称不能为空").genErrorCode();
    public static final ErrorCode RANKING_PARAM_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("0075").setMessageFormat("门店排行参数错误.month:{},shopId:{}").genErrorCode();
    public static final ErrorCode COMPUTER_RESULT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("0076").setMessageFormat("计算结果不存在").genErrorCode();
    public static final ErrorCode SHOP_INFO_CRM_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("0077").setMessageFormat("提交门店信息失败").genErrorCode();

    public static final ErrorCode SEARCH_ORDER_COUNT_EX = LegendErrorHelper.newLegendError().setDetailCode("0011").setMessageFormat("调用搜索获取工单数量失败").genErrorCode();
    /**
     * app接口模块
     */
    public static final ErrorCode APP_QUESTION_NOT_EXIST =
            LegendErrorHelper.newLegendError().setDetailCode("1001").setMessageFormat("问题不存在").genErrorCode();

    public static final ErrorCode APP_QUESTION_NOT_USER =
            LegendErrorHelper.newLegendError().setDetailCode("1002").setMessageFormat("问题不属于当前用户").genErrorCode();

    public static final ErrorCode APP_SHOP_ID_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1003").setMessageFormat("店铺信息错误. shopId:{}").genErrorCode();

    public static final ErrorCode APP_ACTIVITY_NOT_EXIST =
            LegendErrorHelper.newLegendError().setDetailCode("1004").setMessageFormat("活动不存在. activityID:{}").genErrorCode();

    public static final ErrorCode APP_ACTIVITY_ID_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1005").setMessageFormat("活动ID错误. activityID:{}").genErrorCode();

    public static final ErrorCode APP_USER_ID_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1006").setMessageFormat("用户ID错误. activityID:{}").genErrorCode();

    public static final ErrorCode APP_BOOK_ID_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("1007").setMessageFormat("资料ID不能为空").genErrorCode();

    public static final ErrorCode APP_ACTIVITY_INFO_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1008").setMessageFormat("获取活动信息失败").genErrorCode();

    public static final ErrorCode APP_ACTIVITY_INFO_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("1009").setMessageFormat("获取活动细则为空").genErrorCode();

    public static final ErrorCode APP_SUBSIDY_ACTIVITY_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1010").setMessageFormat("获取补贴包失败").genErrorCode();

    public static final ErrorCode APP_GET_SUBSIDY_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1011").setMessageFormat("领取补贴失败").genErrorCode();

    public static final ErrorCode APP_USER_BALANCE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1012").setMessageFormat("用户信息错误").genErrorCode();

    public static final ErrorCode APP_USER_ACCOUNT_BALANCE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1013").setMessageFormat("用户账户金额错误").genErrorCode();

    public static final ErrorCode APP_TEMPLATE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1014").setMessageFormat("{}").genErrorCode();

    public static final ErrorCode APP_USER_BALANCE_LOG_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1015").setMessageFormat("查询账户流水记录失败").genErrorCode();

    public static final ErrorCode ACCOUNT_IS_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("1016").setMessageFormat("登录名为空").genErrorCode();

    public static final ErrorCode SHOPMANAGER_IS_NOT_EXISITE =
            LegendErrorHelper.newLegendError().setDetailCode("1017").setMessageFormat("用户信息不存在").genErrorCode();

    public static final ErrorCode VALIDATE_CODE_OVERTIME =
            LegendErrorHelper.newLegendError().setDetailCode("1018").setMessageFormat("验证码超时，请点击“重新发送”").genErrorCode();

    public static final ErrorCode VALIDATE_FAIL =
            LegendErrorHelper.newLegendError().setDetailCode("1020").setMessageFormat("验证码校验失败").genErrorCode();

    public static final ErrorCode MOBILE_IS_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("1021").setMessageFormat("手机号为空").genErrorCode();

    public static final ErrorCode SEND_MESSAGE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1022").setMessageFormat("短信发送失败").genErrorCode();

    public static final ErrorCode GET_CODE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1023").setMessageFormat("请输入验证码").genErrorCode();

    public static final ErrorCode RESET_PASSWORD_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1024").setMessageFormat("重置密码失败").genErrorCode();

    public static final ErrorCode NEWPASSWORD_IS_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("1025").setMessageFormat("新密码不能为空").genErrorCode();

    public static final ErrorCode NEWPASSWORD_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1026").setMessageFormat("新密码必须为6-12位数字、字母组成").genErrorCode();

    public static final ErrorCode APP_PARAM_ERROR = LegendErrorHelper.newLegendError().setDetailCode("1031").setMessageFormat("传入参数错误").genErrorCode();
    public static final ErrorCode PASSWORD_DIFFERENCE =
            LegendErrorHelper.newLegendError().setDetailCode("1027").setMessageFormat("两次输入的密码不一样").genErrorCode();
    public static final ErrorCode BEAN_COPYPROPERTIES_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1028").setMessageFormat("内部转换错误").genErrorCode();
    public static final ErrorCode ADD_OR_UPDATE_CUSTOMER_CAR_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1029").setMessageFormat("更新车辆信息失败").genErrorCode();
    public static final ErrorCode TQ_CHECK_EXIST_RECORD_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1030").setMessageFormat("该车辆已存在检测记录").genErrorCode();
    public static final ErrorCode TQ_CHECK_ADD_RECORD_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("1031").setMessageFormat("新建检测记录失败").genErrorCode();

    public static final ErrorCode APP_ORDER_SEARCH_ERROR = LegendErrorHelper.newLegendError().setDetailCode("1018").setMessageFormat("工单搜索参数错误").genErrorCode();

    public static final ErrorCode APP_ORDER_LIST_ERROR = LegendErrorHelper.newLegendError().setDetailCode("1019").setMessageFormat("获取工单信息失败").genErrorCode();
    public static final ErrorCode APP_INTERNAL_TRANS_ERROR = LegendErrorHelper.newLegendError().setDetailCode("1016").setMessageFormat("内部转换错误").genErrorCode();
    /**
     * 满意度调查
     */
    public static final ErrorCode SATISFACTION_SHOP_NOT_EXIST =
            LegendErrorHelper.newLegendError().setDetailCode("2001").setMessageFormat("门店不存在.shopId:{}").genErrorCode();
    public static final ErrorCode SATISFACTION_PARAM_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2002").setMessageFormat("请求参数错误.type:{},shopId:{},code:{}").genErrorCode();
    public static final ErrorCode SATISFACTION_GET_INFO_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2003").setMessageFormat("获取门店信息失败 {}").genErrorCode();
    public static final ErrorCode RQ_CODE_CREATE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2004").setMessageFormat("生成二维码失败").genErrorCode();

    public static final ErrorCode APP_ORDER_ID_ERROR = LegendErrorHelper.newLegendError().setDetailCode("1032").setMessageFormat("工单ID错误").genErrorCode();

    public static final ErrorCode APP_CUSTOMER_CAR_ERROR = LegendErrorHelper.newLegendError().setDetailCode("1033").setMessageFormat("没有找到该车辆信息").genErrorCode();

    public static final ErrorCode APP_CUSTOMER_CAR_ID_ERROR = LegendErrorHelper.newLegendError().setDetailCode("1034").setMessageFormat("车辆id为空").genErrorCode();

    public static final ErrorCode APP_SETTLEMENT_ERROR = LegendErrorHelper.newLegendError().setDetailCode("1035").setMessageFormat("结算错误").genErrorCode();

    public static final ErrorCode APP_ORDERINFO_ERROR = LegendErrorHelper.newLegendError().setDetailCode("1036").setMessageFormat("获取工单详情失败").genErrorCode();

    public static final ErrorCode APP_SAVE_CUSTOMERFEEDBACK_ERROR = LegendErrorHelper.newLegendError().setDetailCode("1037").setMessageFormat("保存客户回访信息失败").genErrorCode();


    /**
     * 引流活动
     */
    public static final ErrorCode JOIN_ACTIVITY_ERROR = LegendErrorHelper.newLegendError().setDetailCode("2001").setMessageFormat("报名参加引流活动异常").genErrorCode();

    // [[工单结算
    public static final ErrorCode APP_ORDER_NOTEXSIT_ERROR = LegendErrorHelper.newLegendError().setDetailCode("3000").setMessageFormat("工单不存在").genErrorCode();

    public static final ErrorCode APP_ORDER_SETTLE_ERROR = LegendErrorHelper.newLegendError().setDetailCode("3001").setMessageFormat("结算工单失败,原因:{}").genErrorCode();

    // ]]

    // [[工单无效
    public static final ErrorCode APP_ORDER_INVALID_ERROR = LegendErrorHelper.newLegendError().setDetailCode("4001").setMessageFormat("工单无效失败,原因:{}").genErrorCode();

    // ]]

    public static final ErrorCode EXIT_ACTIVITY_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2002").setMessageFormat("退出引流活动异常").genErrorCode();
    public static final ErrorCode NO_BANK_BIND_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2003").setMessageFormat("没有绑定银行卡").genErrorCode();
    public static final ErrorCode BIND_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2004").setMessageFormat("银行卡绑定失败").genErrorCode();
    public static final ErrorCode CHECK_SETTLE_DB_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2005").setMessageFormat("引流活动确认对账数据库操作错误").genErrorCode();
    public static final ErrorCode PARAMS_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2006").setMessageFormat("引流活动结算对账单搜索参数未填").genErrorCode();
    public static final ErrorCode DATA_EMPTY_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2007").setMessageFormat("引流活动结算对账单搜索数据为空").genErrorCode();
    public static final ErrorCode MOBILE_NULL_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2008").setMessageFormat("手机号不能为空").genErrorCode();
    public static final ErrorCode MOBILE_PERMISSION_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2009").setMessageFormat("手机号不能为空").genErrorCode();
    public static final ErrorCode SMS_SEND_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2010").setMessageFormat("短信发送失败").genErrorCode();
    public static final ErrorCode SHOP_ID_ILLEGAL =
            LegendErrorHelper.newLegendError().setDetailCode("2011").setMessageFormat("门店信息不存在").genErrorCode();
    public static final ErrorCode ACTIVITY_NOT_EXIST =
            LegendErrorHelper.newLegendError().setDetailCode("2012").setMessageFormat("活动模板不存在或未发布").genErrorCode();
    public static final ErrorCode ACTIVITY_FAIL =
            LegendErrorHelper.newLegendError().setDetailCode("2013").setMessageFormat("活动已失效").genErrorCode();
    public static final ErrorCode SHOP_NOT_PART =
            LegendErrorHelper.newLegendError().setDetailCode("2014").setMessageFormat("门店未参加此活动").genErrorCode();
    public static final ErrorCode GET_SERVICES_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2015").setMessageFormat("根据活动模板ID获取套餐列表异常").genErrorCode();

    public static final ErrorCode SHOP_JOIN_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2016").setMessageFormat("该服务暂无门店参与").genErrorCode();
    public static final ErrorCode SAVE_APPOINT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2017").setMessageFormat("添加预约单异常").genErrorCode();
    public static final ErrorCode MOBILE_NO_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2018").setMessageFormat("手机号码有误").genErrorCode();
    public static final ErrorCode VALIDATE_CODE_EXPIRED =
            LegendErrorHelper.newLegendError().setDetailCode("2019").setMessageFormat("验证码已过期").genErrorCode();
    public static final ErrorCode VALIDATE_CODE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2020").setMessageFormat("验证码输入错误").genErrorCode();
    public static final ErrorCode APPOINT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("2021").setMessageFormat("预约单对象为空").genErrorCode();
    public static final ErrorCode APPOINTDATE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2022").setMessageFormat("预约时间错误").genErrorCode();
    public static final ErrorCode APPOINT_SHOPID_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2023").setMessageFormat("门店编号shopId错误").genErrorCode();
    public static final ErrorCode APPOINT_CHANNEL_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2024").setMessageFormat("预约渠道channel错误").genErrorCode();
    public static final ErrorCode APPOINT_SERVICEID_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2025").setMessageFormat("预约serviceId错误").genErrorCode();
    public static final ErrorCode SERVICE_FAIL =
            LegendErrorHelper.newLegendError().setDetailCode("2026").setMessageFormat("服务已失效").genErrorCode();
    public static final ErrorCode SERVICETPLID_NULL_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2027").setMessageFormat("模板服务id有误").genErrorCode();
    public static final ErrorCode PARAM_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2028").setMessageFormat("{}").genErrorCode();
    public static final ErrorCode INSERT_APPOINT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2029").setMessageFormat("预约单创建失败").genErrorCode();
    public static final ErrorCode APPOINT_CONFLICT =
            LegendErrorHelper.newLegendError().setDetailCode("2030").setMessageFormat("今天您已下过此服务的预约单").genErrorCode();
    public static final ErrorCode DATE_FORMAT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2031").setMessageFormat("时间转换异常").genErrorCode();
    public static final ErrorCode SHOP_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2032").setMessageFormat("shopId为空或没有此门店").genErrorCode();
    public static final ErrorCode VALIDATE_CODE_ERROR2 =
            LegendErrorHelper.newLegendError().setDetailCode("2032").setMessageFormat("验证码错误").genErrorCode();
    public static final ErrorCode GET_PARENT_ID_FAIL =
            LegendErrorHelper.newLegendError().setDetailCode("2033").setMessageFormat("寄售协议编号不存在").genErrorCode();
    public static final ErrorCode GET_AGREEMENTS_FAIL =
            LegendErrorHelper.newLegendError().setDetailCode("2034").setMessageFormat("寄售协议信息不存在").genErrorCode();
    public static final ErrorCode GET_QUOTA_INFO_FAIL =
            LegendErrorHelper.newLegendError().setDetailCode("2035").setMessageFormat("该额度对应的信息不存在").genErrorCode();
    public static final ErrorCode SET_QUOTA_INFO_FAIL =
            LegendErrorHelper.newLegendError().setDetailCode("2036").setMessageFormat("该额度对应的信息已存在,请勿重复操作").genErrorCode();
    public static final ErrorCode SYSTEM_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("2037").setMessageFormat("系统异常，请稍后再试").genErrorCode();


    /**
     * 导入模块
     */
    public static final ErrorCode EMPTY_FILE =
            LegendErrorHelper.newLegendError().setDetailCode("3001").setMessageFormat("文件为空").genErrorCode();
    public static final ErrorCode EMPTY_CONTENT =
            LegendErrorHelper.newLegendError().setDetailCode("3002").setMessageFormat("数据内容为空").genErrorCode();
    public static final ErrorCode EXCEL_TEMPLATE_CHANGE =
            LegendErrorHelper.newLegendError().setDetailCode("3003").setMessageFormat("模板有改动，请核对").genErrorCode();
    public static final ErrorCode FILE_NOT_EXIST =
            LegendErrorHelper.newLegendError().setDetailCode("3004").setMessageFormat("文件不存在").genErrorCode();
    public static final ErrorCode DB_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("3006").setMessageFormat("数据有误，请重新检查").genErrorCode();
    public static final ErrorCode IMPORT_DATE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("3007").setMessageFormat("温馨提示：现正处于使用系统高峰期，导入数据速度会有严重影响，建议您在晚上6点之后进行操作。").genErrorCode();
    public static final ErrorCode IMPORT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("3008").setMessageFormat("导入失败，请稍后再试").genErrorCode();

    /**
     * 服务模块
     */
    public static final ErrorCode NULL_SERVICE_NAME =
            LegendErrorHelper.newLegendError().setDetailCode("4001").setMessageFormat("服务名称为空").genErrorCode();
    public static final ErrorCode NULL_SHOP_ID =
            LegendErrorHelper.newLegendError().setDetailCode("4002").setMessageFormat("门店ID为空").genErrorCode();
    public static final ErrorCode NULL_SERVICE_STATUS =
            LegendErrorHelper.newLegendError().setDetailCode("4003").setMessageFormat("请选择车主服务推荐状态").genErrorCode();
    public static final ErrorCode NULL_SERVICE_CATE =
            LegendErrorHelper.newLegendError().setDetailCode("4004").setMessageFormat("请选择车主服务类别").genErrorCode();
    public static final ErrorCode MORE_THAN_THREE =
            LegendErrorHelper.newLegendError().setDetailCode("4005").setMessageFormat("该类别下车主服务超过3个,不能添加!").genErrorCode();
    public static final ErrorCode SERVICE_SN_EXSIT =
            LegendErrorHelper.newLegendError().setDetailCode("4006").setMessageFormat("服务编号已存在").genErrorCode();
    public static final ErrorCode SERVICE_NOT_EXSIT =
            LegendErrorHelper.newLegendError().setDetailCode("4007").setMessageFormat("服务不存在").genErrorCode();
    public static final ErrorCode SAME_SERVICE_EXIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("4008").setMessageFormat("不存在相同的服务").genErrorCode();

    /**
     * 权限模块
     */
    public static final ErrorCode VERSION_CHANGE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("5001").setMessageFormat("版本切换失败").genErrorCode();
    public static final ErrorCode PERMISSION_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("5002").setMessageFormat("您的服务时间已到期，续费请联系客服电话：400-9937-288").genErrorCode();

    /**
     * 共享中心
     */
    public static final ErrorCode SHARE_CHANNEL_LIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6001").setMessageFormat("获取渠道商列表失败").genErrorCode();
    public static final ErrorCode SHARE_CHANNEL_INFO_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6002").setMessageFormat("获取渠道商信息失败").genErrorCode();
    public static final ErrorCode SHARE_CHANNEL_SAVE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6003").setMessageFormat("保存渠道商信息失败").genErrorCode();
    public static final ErrorCode SHARE_CHANNEL_DELETE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6004").setMessageFormat("删除渠道商信息失败").genErrorCode();

    public static final ErrorCode ADD_PROXY_ORDER_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6005").setMessageFormat("添加委托单失败").genErrorCode();

    public static final ErrorCode UPDATE_PROXY_SERVICES_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6006").setMessageFormat("更新委托单服务失败").genErrorCode();
    public static final ErrorCode SHARE_PARTNER_PAGE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6005").setMessageFormat("获取股东列表失败").genErrorCode();
    public static final ErrorCode SHARE_PARTNER_SAVE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6006").setMessageFormat("保存股东信息失败").genErrorCode();
    public static final ErrorCode SHARE_PARTNER_QUIT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6007").setMessageFormat("退出股东失败").genErrorCode();
    public static final ErrorCode SHARE_PARTNER_ADD_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6008").setMessageFormat("添加股东失败").genErrorCode();

    public static final ErrorCode MODIFY_PRICE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6009").setMessageFormat("修改价格失败").genErrorCode();

    public static final ErrorCode ACCEPT_PROXY_ORDER_ERROR = LegendErrorHelper.newLegendError().setDetailCode("6009").setMessageFormat("接受委托单操作失败").genErrorCode();
    public static final ErrorCode BACK_CAR_ERROR = LegendErrorHelper.newLegendError().setDetailCode("6010").setMessageFormat("交车操作失败").genErrorCode();
    public static final ErrorCode CLEAR_PROXY_ORDER_ERROR = LegendErrorHelper.newLegendError().setDetailCode("6011").setMessageFormat("结清委托单操作失败").genErrorCode();
    public static final ErrorCode CANCEL_PROXY_ORDER_ERROR = LegendErrorHelper.newLegendError().setDetailCode("6012").setMessageFormat("取消委托单操作无效！").genErrorCode();
    public static final ErrorCode GET_PROXY_DETAIL_ERROR = LegendErrorHelper.newLegendError().setDetailCode("6013").setMessageFormat("获取委托单详情操作失败").genErrorCode();
    public static final ErrorCode GET_PROXY_LIST_ERROR = LegendErrorHelper.newLegendError().setDetailCode("6014").setMessageFormat("获取委托单列表操作失败").genErrorCode();
    public static final ErrorCode GET_NULL_PROXY_LIST = LegendErrorHelper.newLegendError().setDetailCode("6014").setMessageFormat("未获取到委托单列表！").genErrorCode();

    public static final ErrorCode PROXY_PARAM_NULL_ERROR = LegendErrorHelper.newLegendError().setDetailCode("6015").setMessageFormat("参数不能为空！").genErrorCode();
    public static final ErrorCode SHARE_SHOP_NOT_EXIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6009").setMessageFormat("门店不存在").genErrorCode();
    public static final ErrorCode SHARE_DUBBO_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6010").setMessageFormat("{}").genErrorCode();
    public static final ErrorCode SHARE_LOGIN_INFO_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6011").setMessageFormat("登录信息错误").genErrorCode();
    public static final ErrorCode SHARE_PARTNER_ADD_PARAM_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6012").setMessageFormat("添加股东不能为空").genErrorCode();
    public static final ErrorCode SHARE_CHANNEL_ID_NULL_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6013").setMessageFormat("渠道商id不能为空").genErrorCode();
    public static final ErrorCode SHARE_PARTNER_JOIN_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6014").setMessageFormat("加入股东失败").genErrorCode();
    public static final ErrorCode SHARE_PARTNER_INFO_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6015").setMessageFormat("获取股东信息失败").genErrorCode();
    public static final ErrorCode SETTLEMENT_PROXY_SUMMARY_TYPE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6015").setMessageFormat("判断委托方还是被委托失败").genErrorCode();
    public static final ErrorCode SETTLEMENT_PROXY_TYPE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6016").setMessageFormat("判断委托单输出类型失败").genErrorCode();
    public static final ErrorCode SETTLEMENT_PROXY_SHOP_LIST =
            LegendErrorHelper.newLegendError().setDetailCode("6017").setMessageFormat("获取门店id失败").genErrorCode();
    public static final ErrorCode SHARE_MOBILE_STYLE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6018").setMessageFormat("联系电话格式错误").genErrorCode();

    public static final ErrorCode SHARE_PARAM_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6018").setMessageFormat("参数错误，id={}").genErrorCode();
    public static final ErrorCode SHARE_PARTNER_PARAM_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6019").setMessageFormat("{}").genErrorCode();
    public static final ErrorCode SHARE_CHANNEL_PARAM_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6020").setMessageFormat("{}").genErrorCode();
    public static final ErrorCode SETTLEMENT_PROXY_INFO_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6021").setMessageFormat("获取委托对账单详情信息失败").genErrorCode();
    public static final ErrorCode SETTLEMENT_PROXY_LIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6022").setMessageFormat("获取委托对账单失败").genErrorCode();
    public static final ErrorCode SHOP_BE_PROXY_LIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6023").setMessageFormat("获取受托方门店失败").genErrorCode();
    public static final ErrorCode SETTLEMENT_PROXY_SUMMARY_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6024").setMessageFormat("获取委托对账单汇总失败").genErrorCode();



    public static final ErrorCode TEAM_ADD_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6025").setMessageFormat("添加班组信息失败！").genErrorCode();
    public static final ErrorCode TEAM_EDIT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6025").setMessageFormat("编辑班组信息失败！").genErrorCode();
    public static final ErrorCode TEAM_DELETE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6026").setMessageFormat("删除班组信息失败！").genErrorCode();
    public static final ErrorCode TEAM_GET_DETAIL_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6026").setMessageFormat("查询班组详情失败！").genErrorCode();
    public static final ErrorCode TEAM_GET_LIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6026").setMessageFormat("查询班组列表失败！").genErrorCode();
    public static final ErrorCode NO_AUTHORIZE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6026").setMessageFormat("用户没有权限！").genErrorCode();

    public static final ErrorCode PAINTLEVEL_EDIT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6027").setMessageFormat("编辑油漆级别信息失败！").genErrorCode();
    public static final ErrorCode PAINTLEVEL_LIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6027").setMessageFormat("查询油漆级别信息失败！").genErrorCode();
    public static final ErrorCode PAINTLEVEL_DELETE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6027").setMessageFormat("删除油漆级别信息失败！").genErrorCode();

    public static final ErrorCode PAINTSPECIES_EDIT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6028").setMessageFormat("编辑油漆种类信息失败！").genErrorCode();
    public static final ErrorCode PAINTSPECIES_LIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6028").setMessageFormat("查询油漆种类信息失败！").genErrorCode();
    public static final ErrorCode PAINTSPECIES_DELETE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6028").setMessageFormat("删除油漆种类信息失败！").genErrorCode();

    public static final ErrorCode PRODUCTIONLINE_EDIT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6029").setMessageFormat("编辑生产线信息失败！").genErrorCode();
    public static final ErrorCode PRODUCTIONLINE_LIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6029").setMessageFormat("查询生产线信息失败！").genErrorCode();
    public static final ErrorCode PRODUCTIONLINE_ADD_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6029").setMessageFormat("添加生产线信息失败！").genErrorCode();
    public static final ErrorCode PRODUCTIONLINE_DELETE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6029").setMessageFormat("删除生产线信息失败！").genErrorCode();
    public static final ErrorCode GET_MANAGER_EXT_INFO_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6025").setMessageFormat("获取员工扩展信息失败！").genErrorCode();

    public static final ErrorCode LINEPROCESSMANAGER_EDIT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6030").setMessageFormat("编辑生产线班组失败！").genErrorCode();
    public static final ErrorCode LINEPROCESSMANAGER_LIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6030").setMessageFormat("查询生产线班组失败！").genErrorCode();
    public static final ErrorCode LINEPROCESSMANAGER_DELETE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6030").setMessageFormat("删除生产线班组失败！").genErrorCode();
    public static final ErrorCode PROCESS_LIST_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6031").setMessageFormat("查询工序失败！").genErrorCode();

    public static final ErrorCode SEARCH_PLINE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6032").setMessageFormat("生产线对应工序为空，请编辑！").genErrorCode();

    public static final ErrorCode BOARD_PROCESS_LOAD =
            LegendErrorHelper.newLegendError().setDetailCode("6033").setMessageFormat("数据初始化加载失败！").genErrorCode();

    public static final ErrorCode APP_ORDERCOUNT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6034").setMessageFormat("获取店铺待办事项数量统计失败").genErrorCode();

    public static final ErrorCode APP_CUSTOMERORDERCOUNT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6034").setMessageFormat("获取客户待办事项数量统计失败").genErrorCode();
    public static final ErrorCode SHOP_SHARE_JOIN_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6035").setMessageFormat("您已加入委托，无需再次操作").genErrorCode();
    public static final ErrorCode SHOP_SHARE_JOIN_OUT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6036").setMessageFormat("您未加入委托，无需再次操作").genErrorCode();

    public static final ErrorCode QC_CODE_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("7000").setMessageFormat("二维码已过期").genErrorCode();

}
