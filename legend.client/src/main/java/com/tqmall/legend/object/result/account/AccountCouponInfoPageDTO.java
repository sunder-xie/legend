package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wushuai on 2016/07/12.
 */
@Data
public class AccountCouponInfoPageDTO implements Serializable{
    private static final long serialVersionUID = 3509465527583307311L;
    private List<AccountCouponInfoDTO> content = new ArrayList();
    private Long count =0L;//总数
}
