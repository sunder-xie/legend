package com.tqmall.legend.web.setting.vo;

import com.tqmall.legend.entity.balance.FinanceAccount;
import lombok.Data;

/**
 * Created by lilige on 17/1/16.
 */
@Data
public class FinanceAccountVo {
    private FinanceAccount financeAccount;
    private Boolean isShop;//true - 门店银行卡信息 ; false - 个人银行卡信息
    private String password;//编辑个人银行卡时需要输入app密码
}
