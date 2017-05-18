package com.tqmall.legend.facade.customer.vo;

import com.tqmall.search.dubbo.client.legend.cubecustomerinfo.result.CubeCustomerInfoDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 16/12/19.
 * 客户归属对象
 */
@Getter
@Setter
public class CubeCustomerInfoVo extends CubeCustomerInfoDTO {
    private String userName;
}
