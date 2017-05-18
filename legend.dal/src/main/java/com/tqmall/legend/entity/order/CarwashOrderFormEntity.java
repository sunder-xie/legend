package com.tqmall.legend.entity.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 洗车单表单，提交实体
 * <p/>
 * Created by dongc on 15/5/29.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CarwashOrderFormEntity implements Serializable {


    private static final long serialVersionUID = 5732524670325610295L;
    // 洗车金额服务
    OrderServices carwashService;
    // 客户ID
    private Long customerCarId;
    // 客户单位
    private String company;
    // 车牌号
    private String carLicense;
    // 洗车工ID
    private Long workerId;
    // 洗车工名称
    private String workerName;
    // 服务顾问ID
    private Long receiver;
    // 服务顾问名称
    private String receiverName;
    // 备注
    private String postscript;
    // 洗车金额服务
    private String orderServiceJson;
    // 结算方式
    private String paymentJson;
    // 使用会员卡余额结算信息
    private String cardMemberJson;
    // 会员优惠券列表
    private String couponDetailJson;
    /**
     *  使用会员卡优惠信息
     *  注：如果选中会员卡，则需要传 memberCardId、cardNumber、cardDiscountReason、discountAmount
     *                   否则只需传discountAmount
     */
    private Long memberCardId;//会员卡id
    private String cardNumber;//会员卡编号
    private Long accountId;//账户id
    private String cardDiscountReason;//会员卡优惠原因
    private BigDecimal discountAmount;//优惠金额

    // 是否挂账{0:全款；1:挂账}
    private int isSign;
    //--以下字段用于APP
    //门店ID
    private Long shopId;
    //用户ID
    private Long userId;
    //用户名称
    private String userName;
    //版本
    private String ver;
    //来源
    private Integer refer;
    //车牌图片
    private String imgUrl;

    // 维修工人列表，以逗号，隔开
    private String workerIds;
    // 工单开单时间
    private String createTimeStr;
    //预约单id
    private Long appointId;
    //预付定金
    private BigDecimal downPayment;
}
