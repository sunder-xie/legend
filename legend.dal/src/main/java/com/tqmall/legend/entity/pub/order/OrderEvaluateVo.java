package com.tqmall.legend.entity.pub.order;

import com.tqmall.legend.entity.pub.service.ServiceCateVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by jason on 15/9/18.
 * 7天之内工单对象
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderEvaluateVo {

    private Long orderId;
    private Integer orderTag;
    private String userGlobalId;
    private String license;
    private List<ServiceCateVo> list;

}
