package com.tqmall.legend.biz.bo.dandelion;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by lixiao on 15/7/21.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class TaoqiCouponParam {

    private String couponCode;
    private String mobile;
    private String license;
    private Long workOrderId; //工单id
    private String settleTime;  //结算时间
    private Long shopId; //店铺id
    private String shopPhone;  //店铺电话
    private String saId;  //saID
    private String saName; //sa名称
    private String saPhone;  //sa电话
    private List<String> itemIds;

    @Override
    public String toString() {
        return "TaoqiCouponParam{" +
                "couponCode='" + couponCode + '\'' +
                ", mobile='" + mobile + '\'' +
                ", license='" + license + '\'' +
                ", workOrderId=" + workOrderId +
                ", settleTime='" + settleTime + '\'' +
                ", shopId=" + shopId +
                ", shopPhone='" + shopPhone + '\'' +
                ", saId='" + saId + '\'' +
                ", saName='" + saName + '\'' +
                ", saPhone='" + saPhone + '\'' +
                '}';
    }
}
