package com.tqmall.legend.facade.customer.vo;

import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerContact;
import com.tqmall.legend.entity.customer.CustomerTag;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lilige on 16/4/7.
 * 客户详情页面展示对象
 */
@Data
public class CarDetailVo {
    /**
     * 车辆
     */
    CustomerCar customerCar;
    /**
     * 客户
     */
    Customer customer;
    /**
     * 联系人
     */
    CustomerContact customerContact;

    /**
     * 到店次数(包括有效+无效工单数)
     */
    private Integer totalOrderCount = Integer.valueOf("0");
    /**
     * 维修次数(有效工单数,消费次数)
     */
    private Integer validOrderCount = Integer.valueOf("0");
    /**
     * 挂帐工单数
     */
    private Integer suspendPaymentCount = Integer.valueOf("0");
    /**
     * 最近六个月消费金额
     */
    private BigDecimal recent6MonthAmount = BigDecimal.ZERO;
    /**
     * 挂帐金额
     */
    private BigDecimal suspendAmount = BigDecimal.ZERO;
    /**
     * 客户标签list
     */
    private List<CustomerTag> customerTagList;

}