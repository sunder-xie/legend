package com.tqmall.legend.entity.account;

import com.tqmall.legend.entity.account.vo.CouponVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class CouponSuite extends BaseEntity {

    private Long shopId;//门店id
    private String suiteName;//充值包名
    private BigDecimal amount;//金额
    private BigDecimal totalAmount;//总价值
    private BigDecimal salePrice;//售价
    private Integer suiteStatus;//状态1上架2下架
    private Integer usedCount;//已被充值数
    private List<CouponVo> couponInfos;
    private List<SuiteCouponRel> suiteCouponRels;
    private String gmtCreateStr;

    public String getGmtCreateStr() {
        if (gmtCreateStr != null) {
            return gmtCreateStr;
        }
        if (gmtCreate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(gmtCreate);
        }
        return null;
    }

}

