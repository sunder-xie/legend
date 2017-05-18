package com.tqmall.legend.log;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 营销短信日志
 * Created by xin on 16/9/13.
 */
public class MarketSmsLog {

    // 短信发送
    private static final String MSG_STATUS_DXFS = "DXFS";
    // 短信充值
    private static final String MSG_STATUS_DXCZ = "DXCZ";

    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");

    /**
     * 发送短信log
     * @return
     */
    public static String sendSmsLog(Long shop_id, int msg_num) {
        return generateSmsLog(shop_id, MSG_STATUS_DXFS, msg_num);
    }

    /**
     * 充值短信log
     * @return
     */
    public static String rechargeSmsLog(Long shop_id, int msg_num) {
        return generateSmsLog(shop_id, MSG_STATUS_DXCZ, msg_num);
    }

    private static String generateSmsLog(Long shop_id, String remind_status, int msg_num) {
        LocalDate localDate = new LocalDate();
        localDate.toString(fmt);
        StringBuilder sb = new StringBuilder();
        sb.append("[shop_id:").append(shop_id.toString()).append("]")
                .append("[msg_status:").append(remind_status).append("]")
                .append("[msg_date:").append(localDate.toString(fmt)).append("]")
                .append("[msg_num:").append(msg_num).append("]");
        return sb.toString();
    }
}
