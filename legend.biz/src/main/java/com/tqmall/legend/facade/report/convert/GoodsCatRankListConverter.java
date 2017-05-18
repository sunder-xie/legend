package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.legend.facade.report.vo.GoodsCatSaleRankVo;
import com.tqmall.search.dubbo.client.legend.warehouseout.LegendWarehouseOutDetailDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.Collection;
import java.util.List;

/**
 * Created by majian on 16/9/23.
 */
public class GoodsCatRankListConverter implements Converter<Collection<LegendWarehouseOutDetailDTO>, List<GoodsCatSaleRankVo>> {
    @Override
    public List<GoodsCatSaleRankVo> convert(Collection<LegendWarehouseOutDetailDTO> source) {
        List<GoodsCatSaleRankVo> result = Lists.newArrayList();
        if (source != null) {
            for (LegendWarehouseOutDetailDTO sourceItem : source) {
                GoodsCatSaleRankVo resultItem = new GoodsCatRankConverter().convert(sourceItem);
                result.add(resultItem);
            }
        }
        return result;
    }
}
