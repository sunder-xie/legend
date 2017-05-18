package com.tqmall.legend.facade.report.convert;

import com.tqmall.legend.facade.report.vo.GoodsCatSaleRankVo;
import com.tqmall.search.dubbo.client.legend.warehouseout.LegendWarehouseOutDetailDTO;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * Created by majian on 16/9/23.
 */
public class GoodsCatRankConverter implements Converter<LegendWarehouseOutDetailDTO, GoodsCatSaleRankVo> {
    @Override
    public GoodsCatSaleRankVo convert(LegendWarehouseOutDetailDTO source) {
        GoodsCatSaleRankVo result = new GoodsCatSaleRankVo();
        result.setCatId(source.getCatId().longValue());
        result.setCatName(source.getCatName());
        result.setCost(BigDecimal.valueOf(source.getCost()));
        result.setIncome(BigDecimal.valueOf(source.getIncome()));
        result.setSaleCount(BigDecimal.valueOf(source.getGoodsCount()));
        return result;
    }
}
