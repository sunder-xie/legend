package com.tqmall.legend.entity.sell;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangdong.qu on 17/2/22 20:52.
 */
@Data
public class SellOrder extends BaseEntity {
    private String sellOrderSn;//售卖订单号
    private String buyMobile;//购买手机号码
    private Integer shopLevel;//购买版本
    private String shopLevelName;//购买版本名称
    private BigDecimal sellAmount;//售卖金额(以元为单位)
    private Integer payStatus;//支付状态0:待支付1:支付失败2:支付成功
    private Integer handleStatus;//该条消息处理状态,针对支付成功的消息(0:待开通1:开通成功:开通失败)
    private BigDecimal costAmount;//原价
    private BigDecimal discount;//折扣0-1
    private Long userGlobalId;//crm用户id
    private Long salesmanId;//直销Id
    private String salesmanName;//直销名称
    private String salesmanProvince;//直销所属省份
    private Long salesmanProvinceId;//直销所属省份id
    private String shopName;//店铺名称
    private Long shopCityId;//店铺城市id
    private Long shopProvinceId;//店铺省份id
    private String shopCity;//店铺城市
    private String shopProvince;//店铺省份
    private Date shopOpenTime;//店铺开通时间
    private Date shopEndTime;//店铺到期时间
}
