package com.tqmall.legend.pojo.warehouse;

import java.math.BigDecimal;

/**
 * Created by xin on 2017/4/18.
 */
public class WarehouseInTotalVO {
    private Long totalCount;                        //总数量
    private BigDecimal totalPurchase;               //总成本金额
    private Integer totalSize;                         //总条数

    private BigDecimal totalAmount;//入库总金额
    private BigDecimal totalTax;//税费
    private BigDecimal totalFreight;//运费

    public Long getTotalCount() {
        if (totalCount == null) {
            return 0L;
        }
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalPurchase() {
        if (totalPurchase == null) {
            return BigDecimal.ZERO;
        }
        return totalPurchase.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setTotalPurchase(BigDecimal totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public Integer getTotalSize() {
        if (totalSize == null) {
            return 0;
        }
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public BigDecimal getTotalAmount() {
        if (totalAmount == null) {
            return BigDecimal.ZERO;
        }
        return totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalTax() {
        if (totalTax == null) {
            return BigDecimal.ZERO;
        }
        return totalTax.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotalFreight() {
        if (totalFreight == null) {
            return BigDecimal.ZERO;
        }
        return totalFreight.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setTotalFreight(BigDecimal totalFreight) {
        this.totalFreight = totalFreight;
    }
}
