package com.tqmall.legend.web.vo.report.order;

import com.tqmall.legend.web.vo.report.order.requestpara.OrderServicesReportRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuchengdu on 16/7/11.
 */
public class OrderServicesReportResult {
    private List<OrderServicesVO> orderServicesList;
    private BigDecimal discountTotal;
    private Long totalSize;
    /**
     * 总页数
     */
    private Integer totalPage;
    private OrderServicesReportRequest requestParam;
    public List<OrderServicesVO> getOrderServicesList() {
        return orderServicesList;
    }

    public void setOrderServicesList(List<OrderServicesVO> orderServicesList) {
        this.orderServicesList = orderServicesList;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public OrderServicesReportRequest getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(OrderServicesReportRequest requestParam) {
        this.requestParam = requestParam;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
