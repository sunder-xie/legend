package com.tqmall.legend.web.vo.home;

import lombok.Data;

/**
 * Created by wushuai on 16/5/10.
 */
@Data
public class RemindCountVo {
    private Integer appointNumber;//预约单提醒数
    private Integer orderNumber;//工单提醒数
    private Integer maintainNumber;//保养到期提醒数
    private Integer insuranceNumber;//保险到期提醒数
    private Integer auditingNumber;//年检到期提醒数
    private Integer stockwarningNumber; //仓库预警总数量
    private String appointNumberStr;//预约单提醒数显示样式
    private String orderNumberStr;//工单提醒数显示样式
    private String maintainNumberStr;//保养到期提醒数显示样式
    private String insuranceNumberStr;//保险到期提醒数显示样式
    private String auditingNumberStr;//年检到期提醒数显示样式
    private String stockwarningNumberStr; //仓库预警总数量

    public String getStockwarningNumberStr() {
        return formatNum99(stockwarningNumber);
    }
    public String getAppointNumberStr() {
        return formatNum99(appointNumber);
    }

    public String getOrderNumberStr() {
        return formatNum99(orderNumber);

    }

    public String getMaintainNumberStr() {
        return formatNum99(maintainNumber);

    }

    public String getInsuranceNumberStr() {
        return formatNum99(insuranceNumber);

    }

    public String getAuditingNumberStr() {
        return formatNum99(auditingNumber);
    }

    /**
     * 大于99的数字转为99+
     * @param i
     * @return
     */
    static String formatNum99(Integer i){
        if(i==null){
            return "";
        }
        if (i > 99) {
            return "99+";
        }
        return Integer.toString(i);
    }
}
