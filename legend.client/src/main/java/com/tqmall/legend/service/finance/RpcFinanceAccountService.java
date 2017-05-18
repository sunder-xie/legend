package com.tqmall.legend.service.finance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.finance.FinanceAccountDTO;

import java.util.List;

/**
 * Created by zsy on 16/9/17.
 */
public interface RpcFinanceAccountService {

    /**
     * 根据ucShopId获取门店的对账银行卡信息
     *
     * @return
     */
    Result<FinanceAccountDTO> getShopFinanceAccount(String source, Integer ucShopId);

    /**
     * 批量查询银行卡
     *
     * @param source
     * @param ucShopIdList
     * @return
     */
    Result<List<FinanceAccountDTO>> selectShopFinanceAccountList(String source, List<Integer> ucShopIdList);
}
