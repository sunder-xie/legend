package com.tqmall.legend.facade.report.convert;

import com.tqmall.legend.facade.report.vo.GoodsSaleRankVo;
import com.tqmall.search.dubbo.client.legend.warehouseout.LegendWarehouseOutDetailDTO;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * Created by majian on 16/9/25.
 */
public class GoodsRankConverter implements Converter<LegendWarehouseOutDetailDTO, GoodsSaleRankVo> {
    @Override
    public GoodsSaleRankVo convert(LegendWarehouseOutDetailDTO source) {
        GoodsSaleRankVo result = new GoodsSaleRankVo();
        result.setGoodsId(source.getGoodsId().longValue());
        result.setGoodsName(source.getGoodsName());
        result.setSaleCount(BigDecimal.valueOf(source.getGoodsCount()));
        result.setCost(BigDecimal.valueOf(source.getCost()));
        result.setIncome(BigDecimal.valueOf(source.getIncome()));
        result.setUnit(source.getMeasureUnit());
        return result;
    }
}
