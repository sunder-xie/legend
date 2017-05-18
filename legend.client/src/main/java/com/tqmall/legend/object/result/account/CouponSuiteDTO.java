package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by majian on 16/9/13.
 */
@Data
public class CouponSuiteDTO implements Serializable{
    private String suiteName;
    private List<WxCouponDTO> coupons;
}
