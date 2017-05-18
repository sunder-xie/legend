package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.order.OrderServices;
import lombok.Data;

import java.util.List;

/**
 * Created by twg on 16/3/21.
 * 客情维护中的工单消费记录和回访记录
 */
@Data
public class OrderAndFeedbackInfoVO {
    private Long orderId;// 工单id
    private String orderSn;// 工单编号
    private String orderTime;// 工单创建时间
    private List<OrderServices> orderServicesList;// 工单服务项目
//    private List<OrderGoods> orderGoodsList;// 工单关联物料
}
