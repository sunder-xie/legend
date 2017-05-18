package com.tqmall.legend.web.vo.report.order;

import com.tqmall.legend.web.vo.report.order.requestpara.OrderGoodsReportRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuchengdu on 16/7/11.
 */
public class OrderGoodsReportResult {
    private List<OrderGoodsVO> orderGoodsList;
    /**
     * 配件数量总计
     */
    private BigDecimal goodsNumberTotal;
    /**
     * 成本总计
     */
    private BigDecimal inventoryTotal;
    private Long totalSize;
    private OrderGoodsReportRequest requestParam;
    /**
     * 总页数
     */
    private Integer totalPage;

    public List<OrderGoodsVO> getOrderGoodsList() {
        return orderGoodsList;
    }

    public void setOrderGoodsList(List<OrderGoodsVO> orderGoodsList) {
        this.orderGoodsList = orderGoodsList;
    }

    public BigDecimal getGoodsNumberTotal() {
        return goodsNumberTotal;
    }

    public void setGoodsNumberTotal(BigDecimal goodsNumberTotal) {
        this.goodsNumberTotal = goodsNumberTotal;
    }

    public BigDecimal getInventoryTotal() {
        return inventoryTotal;
    }

    public void setInventoryTotal(BigDecimal inventoryTotal) {
        this.inventoryTotal = inventoryTotal;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public OrderGoodsReportRequest getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(OrderGoodsReportRequest requestParam) {
        this.requestParam = requestParam;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
