package com.tqmall.error;

import com.tqmall.zenith.errorcode.ErrorCode;

/**
 * Created by zwb on 16/9/7.
 */
public class LegendInsuranceErrorCode {
    public static final ErrorCode ORDER_REPEAT_SUBMIT_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("9000000").setMessageFormat("创建订单重复提交").genErrorCode();
    public static final ErrorCode TBD_LIST_SEARCH_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("9000001").setMessageFormat("查询投保单列表异常").genErrorCode();
    public static final ErrorCode NO_EFFECTIVE_REWARD_SEARCH_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("9000002").setMessageFormat("查询待生效奖励金异常").genErrorCode();
    public static final ErrorCode EFFECTIVE_REWARD_SEARCH_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("9000003").setMessageFormat("查询奖励金异常").genErrorCode();
    public static final ErrorCode PROVINCE_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000004").setMessageFormat("投保所在省不能为空").genErrorCode();
    public static final ErrorCode CITY_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000005").setMessageFormat("投保所在城市不能为空").genErrorCode();
    public static final ErrorCode PROVINCE_CODE_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000006").setMessageFormat("投保所在省不能为空").genErrorCode();
    public static final ErrorCode CITY_CODE_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000007").setMessageFormat("投保所在城市不能为空").genErrorCode();
    public static final ErrorCode IS_GET_VEHICLE =
            LegendErrorHelper.newLegendError().setDetailCode("9000008").setMessageFormat("是否取得车牌不能为空").genErrorCode();
    public static final ErrorCode NEW_CAR_INVOICE =
            LegendErrorHelper.newLegendError().setDetailCode("9000009").setMessageFormat("新车购置发票号不能为空").genErrorCode();
    public static final ErrorCode INVOICE_DATE_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000010").setMessageFormat("发票开具日期不能为空").genErrorCode();
    public static final ErrorCode CARFRAME_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000011").setMessageFormat("车架号不能为空").genErrorCode();
    public static final ErrorCode CAR_REGISTER_DATE =
            LegendErrorHelper.newLegendError().setDetailCode("9000012").setMessageFormat("车辆登记日期不能为空").genErrorCode();
    public static final ErrorCode CAR_OWNER_NAME_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000013").setMessageFormat("车主名称名称不能为空").genErrorCode();
    public static final ErrorCode OWNER_CERTIFICATE_TYPE_NOT_NULL=
            LegendErrorHelper.newLegendError().setDetailCode("9000014").setMessageFormat("车主证件类型不能为空").genErrorCode();
    public static final ErrorCode OWNER_CERTIFICATE_CODE_NOT_NULL=
            LegendErrorHelper.newLegendError().setDetailCode("9000015").setMessageFormat("车主证件编码不能为空").genErrorCode();
    public static final ErrorCode OWNER_PHONE_NUMBER_NOT_NULL=
            LegendErrorHelper.newLegendError().setDetailCode("9000016").setMessageFormat("车主手机号不能为空").genErrorCode();
    public static final ErrorCode INSURANCE_EFFECTIVE_DATE=
            LegendErrorHelper.newLegendError().setDetailCode("9000017").setMessageFormat("生效时间不能为空").genErrorCode();
    public static final ErrorCode ITEM_MAJOR_KEY =
            LegendErrorHelper.newLegendError().setDetailCode("9000018").setMessageFormat("险别项目主键不能为空").genErrorCode();
    public static final ErrorCode TOKEN_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000019").setMessageFormat("参数不能为空").genErrorCode();
    public static final ErrorCode SHOP_ID_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000020").setMessageFormat("门店ID不能为空").genErrorCode();
    public static final ErrorCode INSURANCE_COMPANY_ID_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000021").setMessageFormat("保险公司ID不能为空").genErrorCode();
    public static final ErrorCode AGENT_NAME_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000022").setMessageFormat("保险代理人不能为空").genErrorCode();
    public static final ErrorCode INSURE_PEOPLE_NAME_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000023").setMessageFormat("投保人名称不能为空").genErrorCode();
    public static final ErrorCode INSURE_PEOPLE_CERTIFICATE_TYPE_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000024").setMessageFormat("投保人证件类型名称不能为空").genErrorCode();
    public static final ErrorCode INSURE_PEOPLE_CERTIFICATE_CODE_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000025").setMessageFormat("投保人证件编码不能为空").genErrorCode();
    public static final ErrorCode INSURE_PEOPLE_PHONE_NUMBER_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000026").setMessageFormat("投保人手机号不能为空").genErrorCode();
    public static final ErrorCode BEI_INSURE_PEOPLE_NAME_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000027").setMessageFormat("被保人名称不能为空").genErrorCode();
    public static final ErrorCode BEI_INSURE_PEOPLE_CERTIFICATE_TYPE_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000028").setMessageFormat("被保人证件类型不能为空").genErrorCode();
    public static final ErrorCode BEI_INSURE_PEOPLE_CERTIFICATE_CODE_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000029").setMessageFormat("被保人证件编码不能为空").genErrorCode();
    public static final ErrorCode BEI_PHONE_NUMBER_NOT_NULL=
            LegendErrorHelper.newLegendError().setDetailCode("9000030").setMessageFormat("被保人手机号").genErrorCode();
    public static final ErrorCode RECEIVER_PEOPLE_NAME=
            LegendErrorHelper.newLegendError().setDetailCode("9000031").setMessageFormat("收保单人姓名不能为空").genErrorCode();
    public static final ErrorCode RECEIVER_PEOPLE_NAME_PHONE_NUMBER_NOT_NULL =
            LegendErrorHelper.newLegendError().setDetailCode("9000032").setMessageFormat("收保单人手机号不能为空").genErrorCode();
    public static final ErrorCode RECEIVER_PROVINCE_CODE_NOT_NULL=
            LegendErrorHelper.newLegendError().setDetailCode("9000033").setMessageFormat("收保单人所在省不能为空").genErrorCode();
    public static final ErrorCode RECEIVER_CITY_CODE_NOT_NULL=
            LegendErrorHelper.newLegendError().setDetailCode("9000034").setMessageFormat("收保单人所在市不能为空").genErrorCode();
    public static final ErrorCode RECEIVER_REGION_CODE_NOT_NULL=
            LegendErrorHelper.newLegendError().setDetailCode("9000035").setMessageFormat("收保单人所在地区不能为空").genErrorCode();
    public static final ErrorCode RECEIVER_ADDRESS_NOT_NULL=
            LegendErrorHelper.newLegendError().setDetailCode("9000036").setMessageFormat("收保单人所在详细地址不能为空").genErrorCode();
    public static final ErrorCode INSURE_MODE_NOT_NULL=
            LegendErrorHelper.newLegendError().setDetailCode("9000037").setMessageFormat("投保模式不能为空").genErrorCode();
    public static final ErrorCode CREDENTAIL_TYPE_IDCARD_LENGHT=
            LegendErrorHelper.newLegendError().setDetailCode("9000038").setMessageFormat("请正确输入18位的身份证号").genErrorCode();

}

