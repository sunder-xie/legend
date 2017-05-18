package com.tqmall.legend.web.vo.report.order.adaptor;

import com.tqmall.common.Converter;
import com.tqmall.cube.shop.result.OrderGoodsReportDTO;
import com.tqmall.cube.shop.result.RpcOrderGoodsVO;
import com.tqmall.legend.web.vo.report.order.OrderGoodsVO;
import org.springframework.beans.BeanUtils;

/**
 * Created by yuchengdu on 16/7/12.
 */
public class RpcOrderGoodsVOConverter implements Converter<RpcOrderGoodsVO,OrderGoodsVO> {
    @Override
    public void apply(RpcOrderGoodsVO source, OrderGoodsVO destination) {
        if (source==null||destination==null){
            return;
        }
        BeanUtils.copyProperties(source,destination);
    }
}
