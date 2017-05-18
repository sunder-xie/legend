package com.tqmall.legend.log;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 营销卡券日志
 * Created by xin on 16/9/13.
 */
public class MarketCardCouponLog {
    // 会员卡
    private static final String CARD_TYPE_CARD = "CARD";
    // 计次卡
    private static final String CARD_TYPE_COMBO = "COMBO";
    // 优惠券
    private static final String CARD_TYPE_COUPON = "COUPON";

    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");

    /**
     * 发会员卡log
     * @return
     */
    public static String grantCardLog(Long shop_id) {
        return generateLog(shop_id, CARD_TYPE_CARD);
    }

    /**
     * 发计次卡log
     * @return
     */
    public static String grantComboLog(Long shop_id) {
        return generateLog(shop_id, CARD_TYPE_COMBO);
    }

    /**
     * 发优惠券log
     * @param shop_id
     * @return
     */
    public static String grantCouponLog(Long shop_id) {
        return generateLog(shop_id, CARD_TYPE_COUPON);
    }

    private static String generateLog(Long shop_id, String remind_status) {
        LocalDate localDate = new LocalDate();
        localDate.toString(fmt);
        StringBuilder sb = new StringBuilder();
        sb.append("[shop_id:").append(shop_id.toString()).append("]")
                .append("[card_type:").append(remind_status).append("]")
                .append("[delivery_date:").append(localDate.toString(fmt)).append("]");
        return sb.toString();
    }
}
