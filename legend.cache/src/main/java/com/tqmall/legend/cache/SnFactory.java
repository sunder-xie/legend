package com.tqmall.legend.cache;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by lixiao on 16/6/1.
 */
@Component
public class SnFactory {
    //key
    public static final String ORDER_SN_INCREMENT = "order_sn_increment";
    public static final String PAY_LOG_SN_INCREMENT = "pay_log_sn_increment";//没有前缀
    public static final String APPOINT_SN_INCREMENT = "appoint_sn_increment";
    public static final String GOODS_SN_INCREMENT = "goods_sn_increment";
    public static final String SERVICE_SN_INCREMENT = "service_sn_increment";
    public static final String FEE_SN_INCREMENT = "fee_sn_increment";
    public static final String SUPPLIER_SN_INCREMENT = "supplier_sn_increment";
    public static final String PAINT_USE_RECORD_SN_INCREMENT = "paint_use_record_sn_increment";
    public static final String SELL_ORDER_SN_INCREMENT = "sell_order_sn_increment";

    //前缀
    public static final String ORDER = "C";
    public static final String APPOINT = "YY";
    public static final String GOODS = "PJ";
    public static final String SERVICE = "FW";
    public static final String FEE = "FY";
    public static final String SUPPLIER = "GY";
    public static final String PAINT_USE_RECORD = "YQ";
    public static final String SELL_ORDER = "YXSM";


    @Autowired
    private SnCache snCache;

    /**
     * 获取工单编号
     *
     * @return
     */
    public String generateOrderSn(Long shopId) {
        return snCache.getNextOrderSnIncrement(ORDER_SN_INCREMENT, shopId, ORDER);
    }

    /**
     * 获取支付流水号
     *
     * @return
     */
    public String generatePayLogSn(Long shopId) {
        return snCache.getNextOrderSnIncrement(PAY_LOG_SN_INCREMENT, shopId, "");
    }

    /**
     * 获取预约单编号
     *
     * @return
     */
    public String generateAppointSn(Long shopId) {
        return snCache.getNextOrderSnIncrement(APPOINT_SN_INCREMENT, shopId, APPOINT);
    }

    /**
     * 获取云修系统销售订单单号
     *
     * @return
     */
    public String generateSellOrderSn(Long shopId) {
        Long randomNum = RandomUtils.nextLong(1l, 999l) + shopId;
        return snCache.getNextOrderSnIncrement(SELL_ORDER_SN_INCREMENT, randomNum, SELL_ORDER);
    }

    /**
     * 公共生成编号
     *
     * @param key
     * @param shopId
     * @param prefix
     *
     * @return
     */
    public String generateSn(String key, Long shopId, String prefix) {
        return snCache.getNextOrderSnIncrement(key, shopId, prefix);
    }
}
