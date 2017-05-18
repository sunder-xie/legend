package com.tqmall.legend.entity.order;

import com.tqmall.legend.entity.customer.CustomerCar;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 创建工单，提交实体
 * <p/>
 * Created by dongc on 15/5/29.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderFormEntityBo implements Serializable {

    private static final long serialVersionUID = -6815844940261536888L;

    //  表单实体
    private OrderInfo orderInfo;
    // 订购服务LIST
    private List<OrderServices> orderServicesList;
    // 订购物料LIST
    private List<OrderGoods> orderGoodsList;
    // 订购服务JSON
    private String orderServiceJson;
    // 订购物料JSON
    private String orderGoodJson;
    // 虚拟子单ID
    private Long virtualOrderId;
    // 客户信息
    private CustomerCar customerCar;
    // 工单外观检查预检信息JSON
    private String orderPrecheckDetailsJson;
    // 预检单id
    private Long precheckId;

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

    //以下字段用户共享中心
    private Long proxyId;//委托单id

    @Override
    public String toString() {
        return "OrderFormEntityBo{" +
                "orderInfo=" + orderInfo.toString() +
                ", orderServiceJson='" + orderServiceJson + '\'' +
                ", orderGoodJson='" + orderGoodJson + '\'' +
                '}';
    }
}
