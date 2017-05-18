package com.tqmall.legend.web.buy.vo;

import com.tqmall.legend.common.Result;
import com.tqmall.tqmallstall.domain.result.Legend.LegendOrderGoodsDTO;

/**
 * @author wjc
 *
 *         2015年9月9日下午2:38:31
 */
public class LegendOrderGoodsDTOVO extends LegendOrderGoodsDTO {
    private Result GoodsResult;

    public Result getGoodsResult() {
        return GoodsResult;
    }

    public void setGoodsResult(Result goodsResult) {
        GoodsResult = goodsResult;
    }
}
