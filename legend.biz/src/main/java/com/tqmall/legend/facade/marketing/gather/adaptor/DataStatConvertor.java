package com.tqmall.legend.facade.marketing.gather.adaptor;

import com.tqmall.cube.shop.result.marketing.gather.DataStatDTO;
import com.tqmall.legend.facade.marketing.gather.vo.DataStatVO;

/**
 * Created by xin on 2016/12/24.
 */
public class DataStatConvertor {
    public static DataStatVO convert(DataStatDTO dataStatDTO) {
        return new DataStatVO(dataStatDTO.getValue(), dataStatDTO.getPercentage());
    }
}
