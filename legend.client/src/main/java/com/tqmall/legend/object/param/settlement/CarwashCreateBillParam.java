package com.tqmall.legend.object.param.settlement;

import com.tqmall.legend.object.param.BaseRpcParam;
import com.tqmall.legend.object.param.account.DiscountSelectedComboParam;
import com.tqmall.legend.object.param.account.DiscountSelectedCouponParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 洗车单首次账单确认AND收款页面 参数实体
 */
@Data
public class CarwashCreateBillParam extends BaseRpcParam {

    private static final long serialVersionUID = -2392645546715917129L;

    // 洗车服务ID(自定义洗车服务 传入0l)
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
    // 会员卡余额支付(关联会员卡号)
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
    // 已选择的计次卡
    private List<DiscountSelectedComboParam> discountSelectedComboParamList;
    // 选择的现金券和通用券
    private List<DiscountSelectedCouponParam> discountSelectedCouponParamList;
    /**
     * 使用其他车主的会员卡、优惠券，需要传手机号和验证码
     */
    private String guestMobile;//其他车主的手机号

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
    // 开单时间
    private String createTimeStr;
    // 是否挂账{0:全款；1:挂账}
    private int isSign;

    @Override
    public String toString() {
        return "CarwashCreateBillParam{" +
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
                ", discountSelectedComboParamList=" + discountSelectedComboParamList +
                ", discountSelectedCouponParamList=" + discountSelectedCouponParamList +
                ", shopId=" + shopId +
                ", userId=" + userId +
                ", ver='" + ver + '\'' +
                ", refer=" + refer +
                ", imgUrl='" + imgUrl + '\'' +
                ", workerIds='" + workerIds + '\'' +
                ", createTimeStr='" + createTimeStr + '\'' +
                ", isSign=" + isSign +
                "} " + super.toString();
    }
}
