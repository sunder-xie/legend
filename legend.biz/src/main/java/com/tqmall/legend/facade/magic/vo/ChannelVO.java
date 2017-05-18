package com.tqmall.legend.facade.magic.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by macx on 16/5/12.
 */
@Data
public class ChannelVO {
    private Long id;
    private Date gmtCreate;//创建时间
    private Long creator;//创建人id
    private Long shopId;//门店id
    private String channelName;//渠道商名称
    private String channelType;//渠道商类别
    private BigDecimal settlementRate;//结算比例
    private Integer channelStatus;//'1:有效
    private String address;//联系地址
    private String mobile;//联系电话
    private String note;//备注
    private String contactName;//联系人
}
