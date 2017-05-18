package com.tqmall.legend.object.result.settlement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangDong.qu on 16/3/19.
 */
@Data
@EqualsAndHashCode
@ToString
public class CouponDTO implements Serializable{
    private static final long serialVersionUID = -5016168951517408210L;
    private Long id;
    private String isDeleted;
    private Date gmtCreate;
    private Long creator;
    private Date gmtModified;
    private Long modifier;
    private Long shopId;
    private String isUsed;
    private Long customerId;
    private Long customerCarId;
    private String carLicense;
    private String couponSn;
    private BigDecimal couponValue;
}
