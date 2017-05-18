package com.tqmall.legend.facade.report.convert;

import com.tqmall.cube.shop.result.VisitantStatisticDTO;
import com.tqmall.legend.facade.report.vo.VistantStatisticVo;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by majian on 16/9/1.
 */
public class VistantConverter implements Converter<VisitantStatisticDTO,VistantStatisticVo> {
    @Override
    public VistantStatisticVo convert(VisitantStatisticDTO source) {
        VistantStatisticVo destination = new VistantStatisticVo();
        BeanUtils.copyProperties(source,destination);
        return destination;
    }
}
