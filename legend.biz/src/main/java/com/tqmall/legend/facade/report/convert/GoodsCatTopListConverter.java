package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.legend.facade.report.vo.GoodsCatSaleTopVo;
import com.tqmall.search.dubbo.client.legend.warehouseout.LegendWarehouseOutDetailDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by majian on 16/9/23.
 */
public class GoodsCatTopListConverter implements Converter<List<LegendWarehouseOutDetailDTO>, List<GoodsCatSaleTopVo>> {
    @Override
    public List<GoodsCatSaleTopVo> convert(List<LegendWarehouseOutDetailDTO> source) {
        List<GoodsCatSaleTopVo> result = Lists.newArrayList();
        if (source != null) {
            for (LegendWarehouseOutDetailDTO sourceItem : source) {
                GoodsCatSaleTopVo resultItem = new GoodsCatTopConverter().convert(sourceItem);
                result.add(resultItem);
            }
        }
        return result;
    }
}
