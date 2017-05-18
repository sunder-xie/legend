package com.tqmall.legend.web.vo.report.order.adaptor;

import com.tqmall.common.Converter;
import org.springframework.beans.BeanUtils;

/**
 * Created by yuchengdu on 16/7/12.
 */
public class RpcOrderServicesVOConverter implements Converter {
    @Override
    public void apply(Object source, Object destination) {
        if (source==null ||destination==null){
            return;
        }
        BeanUtils.copyProperties(source,destination);
    }
}
