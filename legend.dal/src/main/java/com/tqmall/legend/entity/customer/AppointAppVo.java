package com.tqmall.legend.entity.customer;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jason on 15/7/29.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AppointAppVo extends BaseEntity {

    private String customerName;
    private String mobile;
    private Long appointDate;
    private String appointDateStr;
    private Date appointTime;
    private Long registrantId;
    private String registrantName;
    private String userGlobalId;
    private String refer;//数据来源：0：web, 1 : android,  2 :  IOS
    private Long serviceId;
    private String appointSn;
    private Long shopId;

    private String license;//车牌
    private String carBrandName;//车品牌
    private String carSeriesName;//车系列
    private String appointContent;//预约内容
    private Long customerCarId;//客户车辆ID

    private Long carBrandId;
    private Long carSeriesId;
    private String ver;//app版本号
    private String vin;//车架号
    private String byName;//车辆别名

    private String cancelReason;//取消原因
    private Long status;//预约单状态 0 待确认, 1 预约成功, 2 工单生成  3 车主取消, 4 门店取消 5 微信端取消
    private BigDecimal appointAmount;//预约单金额
    private Long channel;//0 门店web, 1 商家app, 2 车主app, 3 车主微信, 4 橙牛COMMENT ,5 滴滴,6 易迅车联,7 H5商家详情分享页面,8 斑马行车,9 云修系统客服,10夏日活动H5页面,11门店微信公众号
    private Long pushStatus;//下推状态 0 滴滴过来的预约单要有下推操作 1 另外途径过来 橙niu

    private String openid;//易车联下预约单带过来的
    private String echelianid;//易车联下用户ID
    private String appointTimeFormat;

    private String importInfo;//进出口

    private String customerAddress;//客户居住地址
    private Long templateId;//服务模版Id
    private String serviceNote;//服务备注
    private String comment;//预约单备注
    private String sign;//签名

    private String validateCode;//夏日活期手机短信验证码

    private boolean needDownPay=false;//是否需要在线支付预付定金
    private BigDecimal downPayment;//预付定金

    public String getAppointTimeFormat() {
        if(appointTimeFormat != null){
            return appointTimeFormat ;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (appointTime != null) {
            return df.format(appointTime);
        } else {
            return null;
        }
    }

}
