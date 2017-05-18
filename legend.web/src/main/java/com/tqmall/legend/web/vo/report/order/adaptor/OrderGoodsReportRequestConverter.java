package com.tqmall.legend.web.vo.report.order.adaptor;

import com.tqmall.common.Converter;
import com.tqmall.common.util.DateUtil;
import com.tqmall.cube.shop.param.order.OrderGoodsReportParam;
import com.tqmall.legend.web.vo.report.order.requestpara.OrderGoodsReportRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by yuchengdu on 16/7/11.
 */
public class OrderGoodsReportRequestConverter implements Converter<OrderGoodsReportRequest, OrderGoodsReportParam>  {
    @Override
    public void apply(OrderGoodsReportRequest source, OrderGoodsReportParam destination)  {

        if (source == null|| destination== null) {
            return;
        }
        BeanUtils.copyProperties(source,destination);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(!StringUtils.isEmpty(source.getOrderCreateStartDate())) {
                destination.setOrderCreateStartDate(sdf.parse(source.getOrderCreateStartDate()));
            }
            if (!StringUtils.isEmpty(source.getOrderCreateEndDate())) {
                destination.setOrderCreateEndDate(
                        DateUtil.getEndTime(sdf.parse(source.getOrderCreateEndDate()))
                );
            }
            if(!StringUtils.isEmpty(source.getOrderSettleStartDate())) {
                destination.setOrderSettleStartDate(sdf.parse(source.getOrderSettleStartDate()));
            }
            if (!StringUtils.isEmpty(source.getOrderSettleEndDate())) {
                destination.setOrderSettleEndDate(DateUtil.getEndTime(sdf.parse(source.getOrderSettleEndDate())));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
