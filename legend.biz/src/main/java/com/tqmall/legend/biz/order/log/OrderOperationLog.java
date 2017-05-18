package com.tqmall.legend.biz.order.log;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.shop.Shop;

/**
 * Created by zsy on 16/8/4.
 * 工单操作日志
 */
public class OrderOperationLog {

    /**
     * 组装工单操作日志,工单创建和无效
     *
     * @param orderInfo
     * @param msg
     * @return
     */
    public static String getOperationLog(OrderInfo orderInfo, StringBuffer msg) {
        StringBuffer orderLog = new StringBuffer();
        orderLog.append("[shop_id:");
        orderLog.append(orderInfo.getShopId());
        orderLog.append("]");
        orderLog.append("[order_state:");
        orderLog.append(orderInfo.getOrderStatus());
        orderLog.append("]");
        orderLog.append("[order_tag:");
        orderLog.append(orderInfo.getOrderTag());
        orderLog.append("]");
        orderLog.append("[order_date:");
        orderLog.append(DateUtil.convertDateToyMd(orderInfo.getCreateTime()));
        orderLog.append("]");
        orderLog.append("[msg:");
        orderLog.append(msg);
        orderLog.append("]");
        return orderLog.toString();
    }

    /**
     * 组装工单确认账单日志（指定配件的打印）
     *
     * @param orderInfo
     * @param customerCar
     * @param goods
     * @return
     */
    public static String getCarInfoLog(OrderInfo orderInfo, CustomerCar customerCar, Goods goods) {
        StringBuffer orderLog = new StringBuffer();
        String carBrand = customerCar.getCarBrand();
        String carSeries = customerCar.getCarSeries();
        orderLog.append("[confirm_date:");
        orderLog.append(DateUtil.convertDateToyMd(orderInfo.getConfirmTime()));
        orderLog.append("]");
        orderLog.append("[car_brand_id:");
        orderLog.append(customerCar.getCarBrandId());
        orderLog.append("]");
        orderLog.append("[car_series_id:");
        orderLog.append(customerCar.getCarSeriesId());
        orderLog.append("]");
        orderLog.append("[car_info:");
        orderLog.append(carBrand);
        orderLog.append(" ");
        orderLog.append(carSeries);
        orderLog.append("]");
        orderLog.append("[tqmall_goods_sn:");
        orderLog.append(goods.getTqmallGoodsSn());
        orderLog.append("]");
        return orderLog.toString();
    }

    /**
     * 工单打印日志记录
     * 例：[综合维修单打印][门店id:1][门店名称：某某汽修店]
     * @return
     */
    public static String getOrderPrintLog(String header, Shop shop) {
        StringBuffer orderLog = new StringBuffer();
        orderLog.append("[");
        orderLog.append(header);
        orderLog.append("]");
        orderLog.append("[门店id:");
        orderLog.append(shop.getId());
        orderLog.append("]");
        orderLog.append("[门店名称:");
        orderLog.append(shop.getName());
        orderLog.append("]");
        return orderLog.toString();
    }
}
