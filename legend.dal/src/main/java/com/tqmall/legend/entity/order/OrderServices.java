package com.tqmall.legend.entity.order;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderServices extends BaseEntity {

    private Long shopId;
    private Long orderId;
    private Long serviceId;
    private BigDecimal soldPrice;
    private BigDecimal serviceHour;
    private String orderSn;
    private BigDecimal servicePrice;
    private BigDecimal serviceAmount;
    private BigDecimal discount;
    private Integer type;
    private BigDecimal soldAmount;
    private String serviceName;
    private String serviceSn;
    private Long workerId;
    private String workerName;
    private Long serviceCatId;
    private String serviceCatName;
    private String flags;
    private String serviceNote;
    // 服务父ID，用于子服务指向服务套餐
    private Long parentServiceId;
    private Integer priceType;          //服务价格类型 1 正常价格数值显示 2 到店洽谈 3 免费

    private String flagsLike;

    //管理费比率
    private BigDecimal manageRate;

    private List<OrderGoods> orderGoodsList;

    // 维修工人列表，以逗号，隔开
    private String workerIds;

    @Override
    public String toString() {
        return "OrderServices{" +
                "shopId=" + shopId +
                ", orderId=" + orderId +
                ", serviceId=" + serviceId +
                ", soldPrice=" + soldPrice +
                ", serviceHour=" + serviceHour +
                ", orderSn='" + orderSn + '\'' +
                ", servicePrice=" + servicePrice +
                ", serviceAmount=" + serviceAmount +
                ", discount=" + discount +
                ", type=" + type +
                ", soldAmount=" + soldAmount +
                ", serviceName='" + serviceName + '\'' +
                ", serviceSn='" + serviceSn + '\'' +
                ", workerId=" + workerId +
                ", workerName='" + workerName + '\'' +
                ", serviceCatId=" + serviceCatId +
                ", serviceCatName='" + serviceCatName + '\'' +
                ", flags='" + flags + '\'' +
                ", serviceNote='" + serviceNote + '\'' +
                ", parentServiceId=" + parentServiceId +
                ", flagsLike='" + flagsLike + '\'' +
                ", manageRate=" + manageRate +
                ", orderGoodsList=" + orderGoodsList +
                ", workerIds='" + workerIds + '\'' +
                "} " + super.toString();
    }
}

