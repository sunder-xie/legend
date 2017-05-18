package com.tqmall.legend.facade.report.bo;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author 辉辉大侠
 * @Date 2017-04-13 6:03 PM
 * @Motto 一生伏首拜阳明
 */
@Excel
@Data
public class GrossProfitsBo {
    /**
     * 工单id
     */
    private Long orderId;
    /**
     * 工单sn
     */
    @ExcelCol(value = 0, title = "单号", width = 18)
    private String orderSn;
    /**
     * 车牌
     */
    @ExcelCol(value = 1, title = "车牌")
    private String carLicense;
    /**
     * 客户姓名
     */
    @ExcelCol(value = 2, title = "客户")
    private String customerName;
    /**
     * 账单确认时间
     */
    @ExcelCol(value = 3, title = "确认账单时间", width = 18)
    private Date confirmTime;
    /**
     * 配件收入
     */
    @ExcelCol(value = 4, title = "物料销售收入")
    private BigDecimal goodsFinalAmount;

    public BigDecimal getGoodsFinalAmount() {
        return goodsFinalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 服务收入
     */
    @ExcelCol(value = 5, title = "工时收入")
    private BigDecimal serviceFinalAmount;

    public BigDecimal getServiceFinalAmount() {
        return serviceFinalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 附加费用收入
     */
    @ExcelCol(value = 6, title = "其他收入")
    private BigDecimal feeFinalAmount;

    public BigDecimal getFeeFinalAmount() {
        return feeFinalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 总收入
     */
    private BigDecimal totalFinalAmount;

    public BigDecimal getTotalFinalAmount() {
        return totalFinalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 折扣总金额
     */
    @ExcelCol(value = 9, title = "优惠")
    private BigDecimal discountAmount;

    public BigDecimal getDiscountAmount() {
        return discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 配件成本
     */
    @ExcelCol(value = 8, title = "物料成本")
    private BigDecimal inventoryFinalAmount;

    public BigDecimal getInventoryFinalAmount() {
        return inventoryFinalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @ExcelCol(value = 7, title = "收入合计")
    public BigDecimal getOrderAmount() {
        return getGoodsFinalAmount().add(getServiceFinalAmount()).add(getFeeFinalAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @ExcelCol(value = 10, title = "毛利")
    public BigDecimal getGrossProfits() {
        return totalFinalAmount.subtract(inventoryFinalAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    public BigDecimal getGrossProfitsRate() {
        if (BigDecimal.ZERO.compareTo(totalFinalAmount) == 0) {
            return new BigDecimal(0);
        } else {
            BigDecimal data = getGrossProfits().divide(totalFinalAmount, 2, BigDecimal.ROUND_HALF_UP);
            data = data.setScale(2, BigDecimal.ROUND_HALF_UP);
            return data.multiply(BigDecimal.valueOf(100));
        }
    }

    @ExcelCol(value = 11, title = "毛利率")
    public String getGrossProfitsRateStr() {
        return getGrossProfitsRate() + "%";
    }

    public String getConfirmTimeStr() {
        return DateFormatUtils.toYMDHM(getConfirmTime());
    }
}
