package com.tqmall.legend.web.finance;

import com.google.common.collect.Lists;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.base.BaseEnumBo;
import com.tqmall.legend.enums.activity.BankEnum;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zsy on 16/3/11.
 */
@Slf4j
@Controller
@RequestMapping("shop/finance/account")
public class FinanceAccountController extends BaseController {
    /**
     * 获取支持的银行卡列表
     * @return
     */
    @RequestMapping("get_bank_list")
    @ResponseBody
    public Result<List<BaseEnumBo>> getBankList() {
        List<BaseEnumBo> boList = Lists.newArrayList();
        for (BankEnum bankEnum : BankEnum.values()) {
            BaseEnumBo bo = new BaseEnumBo();
            bo.setCode(bankEnum.getCode());
            bo.setName(bankEnum.getMessage());
            boList.add(bo);
        }
        return Result.wrapSuccessfulResult(boList);
    }
}