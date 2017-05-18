package com.tqmall.legend.facade.magic.vo;

import com.tqmall.common.util.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by macx on 16/5/13.
 */
@Data
public class ShopPartnerVO {
    private Long id;
    private Date gmtCreate;//创建时间
    private Long creator;//创建人id
    private Long shopId;//共享中心店id
    private Long partnerId;//股东店id
    private String name;//股东店名称
    private String contactName;//股东店联系人
    private String address;//股东店地址
    private String mobile;//股东店联系电话
    private BigDecimal rate;//结算比例
    private Integer partnerStatus;//'股东状态：1：加入 2:退出
    private String reason;//退股原因
    private Date exitTime;//退出时间
    private String note;//备注

    private String gmtCreateStr;//创建时间格式化
    private String exitTimeStr;//退出时间格式化

    public String getGmtCreateStr(){
        return DateUtil.convertDateToYMDHMS(this.getGmtCreate());
    }

    public String getExitTimeStr(){
        return DateUtil.convertDateToYMDHMS(this.getExitTime());
    }

}
