package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by majian on 16/9/9.
 * 计次卡详情(微信)
 */
@Data
public class ComboDetailDTO implements Serializable {
    private String name;//计次卡名称
    private Date expireDate;//过期时间
    private Long typeId;//计次卡类型id
    private List<ComboServiceDTO> serviceList;//计次卡服务列表
}
