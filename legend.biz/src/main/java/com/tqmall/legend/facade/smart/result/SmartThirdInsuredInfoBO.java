package com.tqmall.legend.facade.smart.result;

import com.tqmall.legend.facade.smart.annotation.SmartBiHuAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jinju.zeng on 2016/12/27.
 */
@Getter
@Setter
public class SmartThirdInsuredInfoBO {
    //主键ID
    private Integer id;

    //创建时间
    private Date gmtCreate;

    //创建人ID
    private Integer creator;

    //更新时间
    private Date gmtModified;

    //更新人ID
    private Integer modifier;

    //是否删除,Y删除,N未删除
    private String isDeleted;

    //第三方平台支持的城市Code
    private Integer thirdCityCode;

    //第三方平台code，smart_third_company 表的唯一
    private String thirdCompanyCode;

    //车牌号
    private String licenseNo;

    //是否成功,0-失败 1-成功
    private Boolean isSuccess;

    //淘汽标准是否成功
    private Boolean tqmallIsSuccess;

    //商业险到期时间，从这个时间到一年之间，数据均可被再此使用,超过后，不使用+归档
    private Date businessExpireDate;

    //使用有效期终止日期，smart_third_insurance_info.business_expire_date + 1年
    private Date validityEndTime;

    //下年的商业险起保日期
    private Date nextBusinessStartDate;

    //下年的交强起保日期
    private Date nextForceStartDate;

    //车主姓名
    private String licenseOwner;

    //证件号码(车主本人)
    private String credentislasNum;

    //证件类型:1.身份证,2.组织机构代码证,3.护照,4.军官证,5.港澳回乡证或台胞证
    private Integer idType;

    //投保人姓名
    private String postedName;

    //投保人证件号
    private String holderIdCard;

    //证件类型:1.身份证,2.组织机构代码证,3.护照,4.军官证,5.港澳回乡证或台胞证
    private Integer holderIdType;

    //投保人联系方式
    private String holderMobile;

    //被保险人姓名
    private String insuredName;

    //被保人证件号
    private String insuredIdCard;

    //证件类型:1.身份证,2.组织机构代码证,3.护照,4.军官证,5.港澳回乡证或台胞证
    private Integer insuredIdType;

    //被保人手机号
    private String insuredMobile;

    //发动机号
    private String engineNo;

    //品牌型号
    private String modleName;

    //车辆识别代码
    private String carVin;

    //车辆注册日期
    private Date registerDate;

    //座位数
    private Integer seatCount;

    //车损保额
    @SmartBiHuAnnotation(name = "033001")
    private BigDecimal cheSunFee;

    //第三方责任险保额
    @SmartBiHuAnnotation(name = "033002")
    private BigDecimal sanZheFee;

    //全车盗抢保险保额
    @SmartBiHuAnnotation(name = "033005")
    private BigDecimal daoQiangFee;

    //车上人员责任险(司机)保额
    @SmartBiHuAnnotation(name = "033003")
    private BigDecimal siJiFee;

    //车上人员责任险(乘客)保额
    @SmartBiHuAnnotation(name = "033004")
    private BigDecimal chengKeFee;

    //玻璃单独破碎险保额
    @SmartBiHuAnnotation(name = "033006",isDeductible = false)
    private Integer boLiFee;

    //车身划痕损失险保额
    @SmartBiHuAnnotation(name = "033014")
    private BigDecimal huaHenFee;

    //涉水行驶损失险保额
    @SmartBiHuAnnotation(name = "033015")
    private BigDecimal sheShuiFee;

    //自燃损失险保额
    @SmartBiHuAnnotation(name = "033007")
    private BigDecimal ziRanFee;

    //精神损失险保额
    @SmartBiHuAnnotation(name = "033011")
    private BigDecimal jingShenFee;

    //不计免赔险（车损) 0:不计免赔，1.计免赔
    private Boolean mianCheSun;

    //不计免赔险（三者）0:不计免赔，1.计免赔
    private Boolean mianSanZhe;

    //不计免赔险（盗抢) 0:不计免赔，1.计免赔
    private Boolean mianDaoQiang;

    //不计免乘客保额 0:不计免赔，1.计免赔
    private Boolean mianChengKe;

    //不计免司机保额 0:不计免赔，1.计免赔
    private Boolean mianSiJi;

    //不计免划痕保额 0:不计免赔，1.计免赔
    private Boolean mianHuaHen;

    //不计免涉水保额 0:不计免赔，1.计免赔
    private Boolean mianSheShui;

    //不计免自燃保额 0:不计免赔，1.计免赔
    private Boolean mianZiRan;

    //不计免精神损失保额 0:不计免赔，1.计免赔
    private Boolean mianJingShen;
}
