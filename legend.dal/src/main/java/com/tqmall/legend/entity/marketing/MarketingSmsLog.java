package com.tqmall.legend.entity.marketing;


import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class MarketingSmsLog extends BaseEntity {

    private Long shopId;
    private String operator;
    private Long smsNum;
    private Integer type;
    private String template;

    private String gmtCreateStr;
    private Integer position;

    public String getGmtCreateStr(){
        return DateUtil.convertDateToYMDHMS(gmtCreate);
    }

}