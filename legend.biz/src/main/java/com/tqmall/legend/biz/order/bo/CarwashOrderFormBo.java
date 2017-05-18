package com.tqmall.legend.biz.order.bo;

import com.tqmall.legend.object.param.settlement.PayChannel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 洗车单表单，提交实体
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CarwashOrderFormBo implements Serializable {


    private static final long serialVersionUID = -1174486715133262455L;
    
    // 洗车服务ID(自定义洗车服务 传入null)
    private Long orderServiceId;
    // 洗车服务价格
    private BigDecimal servicePrice;
    // 客户ID
    private Long customerCarId;
    // 客户单位
    private String company;
    // 车牌号
    private String carLicense;
    // 服务顾问ID
    private Long receiver;
    // 服务顾问名称
    private String receiverName;
    // 备注
    private String postscript;
    // 现金\及其他方式 结算
    private PayChannel payChannel;
    // 会员卡余额支付
    // 会员卡ID
    private Long cardMemberId;
    /**
     * 是否使用会员卡优惠
     */
    private boolean useMemberCard;
    // 使用优惠的会员卡id
    private Long useMemberCardId;
    // 优惠金额
    private BigDecimal discountAmount;
    // 会员卡优惠原因
    private String cardDiscountReason;

    // 会员优惠券列表
    private List<OrderDiscountFlowBo> orderDiscountFlowParamList;
    //门店ID
    private Long shopId;
    //用户ID
    private Long userId;
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
    // 是否挂账{0:全款；1:挂账}
    private int isSign;

    @Override
    public String toString() {
        return "CarwashOrderFormBo{" +
                "orderServiceId=" + orderServiceId +
                ", servicePrice=" + servicePrice +
                ", customerCarId=" + customerCarId +
                ", company='" + company + '\'' +
                ", carLicense='" + carLicense + '\'' +
                ", receiver=" + receiver +
                ", receiverName='" + receiverName + '\'' +
                ", postscript='" + postscript + '\'' +
                ", payChannel=" + payChannel +
                ", cardMemberId=" + cardMemberId +
                ", orderDiscountFlowParamList=" + orderDiscountFlowParamList +
                ", isSign=" + isSign +
                ", shopId=" + shopId +
                ", userId=" + userId +
                ", ver='" + ver + '\'' +
                ", refer=" + refer +
                ", imgUrl='" + imgUrl + '\'' +
                ", workerIds='" + workerIds + '\'' +
                ", createTimeStr='" + createTimeStr + '\'' +
                "} " + super.toString();
    }
}
