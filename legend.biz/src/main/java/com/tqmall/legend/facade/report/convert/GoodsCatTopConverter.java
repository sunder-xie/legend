package com.tqmall.legend.facade.report.convert;

import com.tqmall.legend.facade.report.vo.GoodsCatSaleTopVo;
import com.tqmall.search.dubbo.client.legend.warehouseout.LegendWarehouseOutDetailDTO;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * Created by majian on 16/9/23.
 */
public class GoodsCatTopConverter implements Converter<LegendWarehouseOutDetailDTO, GoodsCatSaleTopVo> {
    @Override
    public GoodsCatSaleTopVo convert(LegendWarehouseOutDetailDTO source) {
        GoodsCatSaleTopVo result = new GoodsCatSaleTopVo();
        result.setCatName(source.getCatName());
        result.setSaleCount(BigDecimal.valueOf(source.getGoodsCount()));
        result.setSaleAmount(BigDecimal.valueOf(source.getIncome()));
        return result;
    }
}
