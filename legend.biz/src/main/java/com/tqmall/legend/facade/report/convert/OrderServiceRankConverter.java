package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.DefaultResult;
import com.tqmall.cube.shop.result.order.OrderServiceCatInfoDTO;
import com.tqmall.cube.shop.result.order.OrderServiceDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.report.vo.OrderServiceVo;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by majian on 16/9/6.
 */
public class OrderServiceRankConverter implements Converter<DefaultResult<OrderServiceDTO>,Page<OrderServiceVo>>{
    @Override
    public Page<OrderServiceVo> convert(DefaultResult<OrderServiceDTO> source) {
        List<OrderServiceVo> content = Lists.newArrayList();
        Page<OrderServiceVo> destination;

        if (source != null) {
            Pageable pageable = new PageRequest(source.getPageNum(), source.getPageSize());
            for (OrderServiceDTO dto : source.getContent()) {
                OrderServiceVo vo = new OrderServiceVo();
                BeanUtils.copyProperties(dto,vo);
                content.add(vo);
            }
            destination = new DefaultPage<OrderServiceVo>(content,pageable,source.getTotal());
        } else {
            destination = new DefaultPage<OrderServiceVo>(content);
        }
        return destination;
    }
}
