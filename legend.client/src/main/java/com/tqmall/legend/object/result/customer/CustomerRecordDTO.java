package com.tqmall.legend.object.result.customer;

import com.tqmall.legend.object.result.order.OrderInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by lifeilong on 2016/3/18.
 * 客户档案
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerRecordDTO implements Serializable {
    private static final long serialVersionUID = 3911077142124639739L;

    private Long customerCarId;//车辆id
    private String license;//车牌号
    private String carInfo; //车辆型号信息 (carBrandName+(importInfo)+ carModel+carSeriesName)

    private Integer totalNum; //总记录数

    //预约单
    private List<CustomerAppointDTO> customerAppointDTOList;

    //预检单
    private List<CustomerPrechecksDTO> customerPrechecksDTOList;

    //维修工单、回访记录共用
    private List<OrderInfoDTO> orderInfoDTOList;

}
