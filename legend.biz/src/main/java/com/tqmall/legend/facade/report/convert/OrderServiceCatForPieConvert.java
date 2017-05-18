package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.order.OrderServiceCatInfoDTO;
import com.tqmall.legend.facade.report.vo.OrderServiceCatForPie;
import com.tqmall.wheel.lang.Objects;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
public class OrderServiceCatForPieConvert implements Converter<List<OrderServiceCatInfoDTO>, List<OrderServiceCatForPie>> {


    @Override
    public List<OrderServiceCatForPie> convert(List<OrderServiceCatInfoDTO> source) {
        List<OrderServiceCatForPie> orderServiceCatForPieList = Lists.newArrayListWithCapacity(Objects.isNull(source)?0:source.size());
        if (Objects.isNotNull(source)) {
            for (OrderServiceCatInfoDTO orderServiceCatInfoDTO : source) {
                OrderServiceCatForPie orderServiceCatForPie = new OrderServiceCatForPie();
                orderServiceCatForPie.setServiceCatId(orderServiceCatInfoDTO.getServiceCatId());
                orderServiceCatForPie.setServiceCatName(orderServiceCatInfoDTO.getServiceCatName());
                orderServiceCatForPie.setSaleNum(orderServiceCatInfoDTO.getSaleNum());
                orderServiceCatForPie.setSaleAmount(orderServiceCatInfoDTO.getSaleAmount());
                orderServiceCatForPieList.add(orderServiceCatForPie);
            }
        }
        return orderServiceCatForPieList;
    }
}
