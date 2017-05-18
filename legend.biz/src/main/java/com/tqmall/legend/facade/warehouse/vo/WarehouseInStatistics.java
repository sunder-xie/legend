package com.tqmall.legend.facade.warehouse.vo;

import com.tqmall.legend.entity.warehousein.WarehouseIn;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sven on 16/10/19.
 */
@Getter
@Setter
public class WarehouseInStatistics {
    private List<WarehouseIn> warehouseInList;
    private BigDecimal totalAmount;
    private BigDecimal amountPayable;
    private BigDecimal amountPaid;
    private BigDecimal taxAmount; //税费
    private BigDecimal freightAmount; //运费
    private BigDecimal goodsAmount; //配件总价

    public WarehouseInStatistics() {
        this.totalAmount = BigDecimal.ZERO;
        this.amountPaid = BigDecimal.ZERO;
        this.amountPayable = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.freightAmount = BigDecimal.ZERO;
        this.goodsAmount = BigDecimal.ZERO;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (WarehouseIn warehouseIn : warehouseInList) {
            total = total.add(warehouseIn.getTotalAmount());
        }
        return total;
    }

    public BigDecimal getAmountPayable() {
        BigDecimal total = BigDecimal.ZERO;
        for (WarehouseIn warehouseIn : warehouseInList) {
            total = total.add(warehouseIn.getAmountPayable());
        }
        return total;
    }

    public BigDecimal getAmountPaid() {
        BigDecimal total = BigDecimal.ZERO;
        for (WarehouseIn warehouseIn : warehouseInList) {
            total = total.add(warehouseIn.getAmountPaid());
        }
        return total;
    }

    public BigDecimal getFreightAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (WarehouseIn warehouseIn : warehouseInList) {
            total = total.add(warehouseIn.getFreight());
        }
        return total;
    }

    public BigDecimal getTaxAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (WarehouseIn warehouseIn : warehouseInList) {
            total = total.add(warehouseIn.getTax());
        }
        return total;
    }

    public BigDecimal getGoodsAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (WarehouseIn warehouseIn : warehouseInList) {
            total = total.add(warehouseIn.getGoodsAmount());
        }
        return total;
    }
}
