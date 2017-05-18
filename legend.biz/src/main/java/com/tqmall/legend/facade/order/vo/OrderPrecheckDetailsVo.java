package com.tqmall.legend.facade.order.vo;

import com.tqmall.legend.entity.order.OrderPrecheckDetails;
import com.tqmall.legend.entity.precheck.PrecheckValue;
import lombok.Data;

import java.util.List;

/**
 * Created by zsy on 16/7/10.
 */
@Data
public class OrderPrecheckDetailsVo {
    //工单预检信息选项
    private List<PrecheckValue> precheckValueList;
    //工单预检详细信息
    private List<OrderPrecheckDetails> orderPrecheckDetailsList;
}
