package com.tqmall.legend.entity.order;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.settlement.Coupon;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderInfo extends BaseEntity {

    private Long shopId;
    private String shopName;
    private Long orderType;
    private Long parentId;
    private String orderSn;
    private Long customerId;
    private Long customerCarId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date expectedTime;
    private BigDecimal goodsAmount;
    private BigDecimal serviceAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String postscript;
    private String orderStatus;
    private String carLicense;
    private Long carBrandId;
    private Long carSeriesId;
    private Long carPowerId;
    private Long carYearId;
    private Long carModelsId;
    private String carBrand;
    private String carSeries;
    private String carPower;
    private String carYear;
    private String carModels;
    private String carCompany;
    private String importInfo;
    private String customerName;
    private String customerMobile;
    private String vin;
    private String engineNo;
    private Long receiver;
    private Long invoiceType;
    private BigDecimal discount;
    private String receiverName;
    private String operatorName;
    private BigDecimal orderAmount;
    private Integer payStatus;
    private Date finishTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date payTime;
    private Integer isVisit;
    // 创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
    private String createTimeStr;
    // 上一次开单日期
    private Date lastCreateTime;

    private Long orderCount;
    private Long goodsCount;
    private Long serviceCount;
    private String carAlias;
    private BigDecimal preDiscountRate;
    private BigDecimal preTaxAmount;
    private BigDecimal prePreferentiaAmount;
    private BigDecimal preCouponAmount;
    private BigDecimal preTotalAmount;
    private BigDecimal payAmount;

    private BigDecimal signAmount;
    private Long otherInsuranceCompanyId;
    private String otherInsuranceCompanyName;
    private BigDecimal goodsDiscount;
    private BigDecimal serviceDiscount;
    //附加总费用
    private BigDecimal feeAmount;
    //附加总优惠
    private BigDecimal feeDiscount;

    private String contactName;
    private String contactMobile;
    private Long insuranceCompanyId;
    private String insuranceCompanyName;
    private String mileage;
    private String carColor;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date buyTime;
    private String customerAddress;
    private String oilMeter;//油表
    private String isLock;/*是否锁定状态*/

    private String imgUrl;
    private Integer orderTag;
    private String orderTagName;
    private String ver;
    private Integer refer;
    private BigDecimal orderDiscountAmount;//工单优惠总金额
    private Date confirmTime;//工单确认时间

    private BigDecimal badAmount;   //坏账金额

    //委托单新增两个属性
    private Integer proxyType;//委托状态：0：无1：委托2：受委托
    private Long channelId;//渠道商id
    private String channelName;//渠道商名称
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date expectTime;//期望交车日期
    private BigDecimal downPayment;//预付定金
    private String expectTimeStr;//期望交车日期
    private String insuranceTimeStr;//保险到期日期


    /**
     * 是否显示会员卡打折输入框
     */
    private Boolean isShowDiscount;

    //管理费
    private BigDecimal manageFee;

    private String warehouseOutSn;

    private String payTimeYMD;
    private String finishTimeYMD;
    //预约单ID
    private Long appointId;


    /**
     * 服务项目
     */
    private List<OrderServices> serviceList;
    /**
     * 配件物料
     */
    private List<OrderGoods> goodsList;
    /**
     * 虚开物料
     */
    private List<OrderGoods> virtualGoodsList;
    /**
     * 附加费用
     */
    private List<OrderServices> otherList;
    private String gmtCreateStr;
    private String gmtModifiedStr;
    //其它添加字段
    private String gmtModifiedYMDHMS;
    private String expectedTimeYMD;
    private String buyTimeYMD;
    private String lastTime;
    private Integer orderStatusInt;
    //新规则的工单状态
    private String OrderStatusName;
    //档口店工单状态
    private String tqmallOrderStatusName;

    @Deprecated
    private String orderStatusClientName;
    private String orderTypeName;


    /*以下用于结算浮层显示数据 数据库没有这些字段*/
    //结算浮层展示历史结算 方式与金额,结算用
    // 上一次折扣比率
    private BigDecimal lastDiscountRate;
    // 上一次费用金额
    private BigDecimal lastTaxAmount;
    // 上一次优惠金额
    private BigDecimal lastPreferentiaAmount;
    //
    private BigDecimal memberPreAmount;
    // 上一次代金券使用总金额
    private BigDecimal lastCouponAmount;
    //淘汽优惠
    private BigDecimal taoqiCouponAmount;
    private String taoqiCouponSn;
    //第三方优惠
    private String otherCouponName;
    private BigDecimal otherCouponAmount;
    private String otherCouponSn;

    /*结算信息*/
    private BigDecimal realInventoryAmount;
    private BigDecimal payHistoryAmount;
    private Integer totalStar;
    /*结算备注*/
    private String settleComment;
    /*维修内容*/
    private String serviceContent;

    private List<Coupon> couponList;

    private String memberSn;
    /*已付金额 导出重构*/
    private BigDecimal payedAmount;

    // identity_card 身份证号
    private String identityCard;

    //物料出库标志
    private Integer goodsOutFlag = 0;
    //子单打印时间临时字段
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date printTime;

    // 客户单位
    private String company;

    //app 创建快修快保单用
    private Long carGearBoxId;
    private String carGearBox;
    // 客户信息
    private Customer customer;
    // 客户车辆信息
    CustomerCar customerCar;
    // 附加服务
    List<OrderServices> additionalServiceList;

    //车型
    private String carInfo;

    /**
     * 下次保养里程
     */
    private String upkeepMileage;

    public String getPayTimeYMD() {
        return DateUtil.convertDate(payTime);
    }

    public String getFinishTimeYMD() {
        return DateUtil.convertDate(finishTime);
    }

    public String getExpectTimeStr() {
        return DateUtil.convertDateToYMDHHmm(expectTime);
    }

    public String getGmtCreateStr() {
        if(createTime != null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return f.format(createTime);
        }else if (gmtCreate != null) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return f.format(gmtCreate);
        } else {
            return null;
        }
    }

    public String getCreateTimeStr() {
        if (createTimeStr != null) {
            return createTimeStr;
        }
        if (createTime != null) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return f.format(createTime);
        } else {
            return null;
        }
    }

    public String getGmtModifiedStr() {
        if (gmtModified != null) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return f.format(gmtModified);
        } else {
            return null;
        }
    }


    public String getGmtModifiedYMDHMS() {
        if (gmtModifiedYMDHMS != null) {
            return gmtModifiedYMDHMS;
        }
        if (gmtModified != null) {
            return DateUtil.convertDateToYMDHMS(gmtModified);
        } else {
            return null;
        }
    }

    public String getExpectedTimeYMD() {
        if (expectedTimeYMD != null) {
            return expectedTimeYMD;
        }
        if (expectedTime != null) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return f.format(expectedTime);
        } else {
            return null;
        }
    }

    public String getBuyTimeYMD() {
        if (buyTimeYMD != null) {
            return buyTimeYMD;
        }
        if (buyTime != null) {
            return DateUtil.convertDateToYMD(buyTime);
        } else {
            return null;
        }
    }

    public String getLastTime() {
        return DateUtil.convertDateToYMDHM(expectedTime);
    }

    public Integer getOrderStatusInt() {
        if (orderStatus != null) {
            return OrderStatusEnum.getorderStatusIntByKey(orderStatus);
        } else {
            return null;
        }
    }

    public String getOrderStatusClientName() {
        if (orderStatus != null) {
            return OrderStatusEnum.getorderStatusClientNameByKey(orderStatus);
        } else {
            return null;
        }

    }

    public String getOrderStatusName() {
        if (orderStatus != null && payStatus != null) {
            return OrderNewStatusEnum.getOrderStatusName(orderStatus, payStatus);
        } else {
            return null;
        }

    }

    public String getOrderTagName() {
        if (orderTag != null) {
            return OrderCategoryEnum.getsNameByCode(orderTag);
        }
        return null;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "Id=" + id + '\'' +
                ", shopId=" + shopId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", orderType=" + orderType +
                ", parentId=" + parentId +
                ", orderSn='" + orderSn + '\'' +
                ", customerId=" + customerId +
                ", customerCarId=" + customerCarId +
                ", expectedTime=" + expectedTime +
                ", goodsAmount=" + goodsAmount +
                ", serviceAmount=" + serviceAmount +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                ", postscript='" + postscript + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", carLicense='" + carLicense + '\'' +
                ", carBrandId=" + carBrandId +
                ", carSeriesId=" + carSeriesId +
                ", carPowerId=" + carPowerId +
                ", carYearId=" + carYearId +
                ", carModelsId=" + carModelsId +
                ", carBrand='" + carBrand + '\'' +
                ", carSeries='" + carSeries + '\'' +
                ", carPower='" + carPower + '\'' +
                ", carYear='" + carYear + '\'' +
                ", carModels='" + carModels + '\'' +
                ", carCompany='" + carCompany + '\'' +
                ", importInfo='" + importInfo + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerMobile='" + customerMobile + '\'' +
                ", vin='" + vin + '\'' +
                ", engineNo='" + engineNo + '\'' +
                ", receiver=" + receiver +
                ", invoiceType=" + invoiceType +
                ", discount=" + discount +
                ", receiverName='" + receiverName + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", orderAmount=" + orderAmount +
                ", payStatus=" + payStatus +
                ", finishTime=" + finishTime +
                ", payTime=" + payTime +
                ", isVisit=" + isVisit +
                "} ";
    }

    public String getLastCreateTimeStr() {
        if (lastCreateTime != null) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return f.format(lastCreateTime);
        } else {
            return null;
        }
    }
    public String getCarInfo() {
        StringBuffer sb = new StringBuffer();
        if (getCarBrand() != null) {
            sb.append(getCarBrand());
        }
        if (StringUtils.isNotBlank(getImportInfo())) {
            sb.append('(').append(getImportInfo()).append(')');
        }
        if (StringUtils.isNotBlank(getCarModels())) {
            sb.append(' ').append(getCarModels());
        } else if (StringUtils.isNotBlank(getCarSeries())) {
            sb.append(' ').append(getCarSeries());
        }

        return sb.toString();
    }

    public String getTqmallOrderStatusName() {
        if (orderStatus != null && payStatus != null) {
            //快修快保、销售单，待报价为待结算状态
            if(orderTag != null && ( OrderCategoryEnum.SPEEDILY.getCode() == orderTag
                    || OrderCategoryEnum.SELLGOODS.getCode() == orderTag)
                    && orderStatus.equals(OrderNewStatusEnum.DBJ.getOrderStatus())
                    ){
                return "待结算";
            }
            return OrderNewStatusEnum.getOrderStatusName(orderStatus, payStatus);
        } else {
            return null;
        }

    }
}

