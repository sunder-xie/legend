package com.tqmall.legend.object.result.appoint;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsy on 16/3/26.
 * 预约服务对应门店服务详情对象
 */
@Data
public class AppointServiceDTO implements Serializable{
    private static final long serialVersionUID = 2056138189429706673L;

    private String name;//服务名称
    private String imgUrl;//服务图片
    private String serviceNote;//套餐内容
    private List<ShopServiceDTO> shopServiceDTOList;//服务
}
