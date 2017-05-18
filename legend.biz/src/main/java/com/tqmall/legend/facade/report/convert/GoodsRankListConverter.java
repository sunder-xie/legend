package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.legend.facade.report.vo.GoodsSaleRankVo;
import com.tqmall.search.dubbo.client.legend.warehouseout.LegendWarehouseOutDetailDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.Collection;
import java.util.List;

/**
 * Created by majian on 16/9/25.
 */
public class GoodsRankListConverter implements Converter<Collection<LegendWarehouseOutDetailDTO>, List<GoodsSaleRankVo>> {
    @Override
    public List<GoodsSaleRankVo> convert(Collection<LegendWarehouseOutDetailDTO> source) {
        List<GoodsSaleRankVo> destination = Lists.newArrayList();
        if (source != null) {
            for (LegendWarehouseOutDetailDTO sourceItem : source) {
                GoodsSaleRankVo destinationItem = new GoodsRankConverter().convert(sourceItem);
                destination.add(destinationItem);
            }
        }
        return destination;
    }
}
