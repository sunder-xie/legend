package com.tqmall.legend.web.insurance.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zxg on 17/2/8.
 * 16:41
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Data
public class SettleBonusPayShowVO {

    /**创建时间**/
    private java.util.Date gmtCreate;

    /**操作金额--支出或返回的金额**/
    private BigDecimal amount;

    //原因，根据action获得
    private String reason;

    // 订单号
    private String outOrderSn;

    //当前条件下的总金额
    private BigDecimal totalAmount;

}
