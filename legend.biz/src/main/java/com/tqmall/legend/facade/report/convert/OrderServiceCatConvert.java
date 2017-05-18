package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.DefaultResult;
import com.tqmall.cube.shop.result.order.OrderServiceCatInfoDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.report.vo.OrderServiceCatVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
public class OrderServiceCatConvert implements Converter<DefaultResult<OrderServiceCatInfoDTO>, Page<OrderServiceCatVo>> {

    @Override
    public Page<OrderServiceCatVo> convert(DefaultResult<OrderServiceCatInfoDTO> source) {
        List<OrderServiceCatVo> content = Lists.newArrayList();
        DefaultPage<OrderServiceCatVo> destination;
        if (source != null) {
            PageRequest pageRequest = new PageRequest(source.getPageNum(), source.getPageSize());
            for (OrderServiceCatInfoDTO item : source.getContent()) {
                OrderServiceCatVo vo = new OrderServiceCatVo();
                vo.setRank(item.getRank());
                vo.setSaleAmount(item.getSaleAmount());
                vo.setSaleNum(item.getSaleNum());
                vo.setServiceCatName(item.getServiceCatName());
                content.add(vo);
            }
            destination = new DefaultPage<>(content, pageRequest, source.getTotal());
        } else {
            destination = new DefaultPage<>(content);
        }
        return destination;
    }
}
