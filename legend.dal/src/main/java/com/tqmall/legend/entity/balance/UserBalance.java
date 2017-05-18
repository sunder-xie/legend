package com.tqmall.legend.entity.balance;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserBalance extends BaseEntity {

    private Long shopId;//店铺id
    private Long userId;//用户id
    private BigDecimal balance;//账户余额

}


