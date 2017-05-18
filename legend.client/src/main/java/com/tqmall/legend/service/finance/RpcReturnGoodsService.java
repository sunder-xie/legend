package com.tqmall.legend.service.finance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.finance.VerifyParam;

import java.util.List;

/**
 * Created by jason on 16/4/5.
 * 退换货接口
 */
public interface RpcReturnGoodsService {

    /**
     * 新版：返利补贴检验接口
     *
     * @param paramList 换货商品信息
     */
    public Result<Boolean> newVerifySubsidyGoods(List<VerifyParam> paramList);

}
