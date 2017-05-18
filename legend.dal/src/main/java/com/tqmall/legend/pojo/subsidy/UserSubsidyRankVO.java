package com.tqmall.legend.pojo.subsidy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by xiangDong.qu on 16/2/29.
 */
@Data
@EqualsAndHashCode
public class UserSubsidyRankVO {
    private String userName;
    private BigDecimal amount;
}
