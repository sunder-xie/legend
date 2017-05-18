package com.tqmall.legend.log;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 门店提醒日志打印
 * Created by xin on 16/9/10.
 */
public class ShopNoteInfoLog {

    // 创建提醒
    private static final String REMIND_STATUS_CJTX = "CJTX";
    // 处理提醒
    private static final String REMIND_STATUS_CLTX = "CLTX";

    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");

    /**
     * 创建提醒log
     * @return
     */
    public static String creatNoteLog(Long shop_id) {
        return generateNoteLog(shop_id, REMIND_STATUS_CJTX);
    }

    /**
     * 处理提醒log
     * @return
     */
    public static String handleNoteLog(Long shop_id) {
        return generateNoteLog(shop_id, REMIND_STATUS_CLTX);
    }

    private static String generateNoteLog(Long shop_id, String remind_status) {
        LocalDate localDate = new LocalDate();
        localDate.toString(fmt);
        StringBuilder sb = new StringBuilder();
        sb.append("[shop_id:").append(shop_id.toString()).append("]")
                .append("[remind_status:").append(remind_status).append("]")
                .append("[remind_date:").append(localDate.toString(fmt)).append("]");
        return sb.toString();
    }

}
