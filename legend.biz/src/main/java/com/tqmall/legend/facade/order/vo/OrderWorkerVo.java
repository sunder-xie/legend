package com.tqmall.legend.facade.order.vo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.order.OrderCategoryEnum;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zsy on 16/4/14.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderWorkerVo implements Serializable {

    //工单id
    private Long id;
    //车牌
    private String carLicense;
    //工单编号
    private String orderSn;
    //服务顾问
    private String receiverName;
    //开单时间
    private Date createTime;
    private String createTimeStr;
    //维修工
    private String workerNames;
    //维修状态
    private String orderStatus;
    private String orderStatusName;
    //档口版门店状态
    private String tqmallOrderStatusName;
    //工单标签，1为普通工单，2为洗车工单，3为快修快保单，4为保险维修单，5销售单
    private Integer orderTag;
    private Integer orderTagName;
    //车型
    private String carInfo;

    private Integer payStatus;

    public String getCreateTimeStr(){
        return DateUtil.convertDateToYMDHHmm(createTime);
    }

    public String getOrderStatusName(){
        if(OrderStatusEnum.getorderStatusClientNameByKey(orderStatus) != null){
            return OrderStatusEnum.getorderStatusClientNameByKey(orderStatus);
        }else{
            return "";
        }
    }

    public String getOrderTagName(){
        if(orderTag != null && OrderCategoryEnum.getsNameByCode(orderTag) != null){
            return OrderCategoryEnum.getsNameByCode(orderTag);
        }else{
            return "";
        }
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
