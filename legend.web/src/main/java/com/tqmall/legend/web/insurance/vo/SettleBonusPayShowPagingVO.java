package com.tqmall.legend.web.insurance.vo;

import com.tqmall.core.common.entity.PagingResult;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zxg on 17/2/8.
 * 18:53
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Data
public class SettleBonusPayShowPagingVO {
    private PagingResult<SettleBonusPayShowVO> payShowVOPagingResult;

    //支出或返还的总金额
    private BigDecimal totalAmount;
}
