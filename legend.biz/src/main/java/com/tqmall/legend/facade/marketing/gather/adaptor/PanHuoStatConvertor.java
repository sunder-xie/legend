package com.tqmall.legend.facade.marketing.gather.adaptor;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.marketing.gather.PanHuoStatDTO;
import com.tqmall.legend.facade.marketing.gather.vo.PanHuoStatVO;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2016/12/21.
 */
public class PanHuoStatConvertor {
    public static PanHuoStatVO convert(PanHuoStatDTO panHuoStatDTO) {
        if (panHuoStatDTO == null) {
            return null;
        }
        PanHuoStatVO panHuoStatVO = new PanHuoStatVO();
        panHuoStatVO.setOperateType(panHuoStatDTO.getOperateType());
        panHuoStatVO.setOperateTypeName(panHuoStatDTO.getOperateTypeName());
        panHuoStatVO.setOperateCustomerNum(panHuoStatDTO.getOperateCustomerNum());
        panHuoStatVO.setToStoreCustomerNum(panHuoStatDTO.getToStoreCustomerNum());
        panHuoStatVO.setIncome(panHuoStatDTO.getIncome());
        panHuoStatVO.setConversionRate(panHuoStatDTO.getConversionRate());
        return panHuoStatVO;
    }

    public static List<PanHuoStatVO> convertList(List<PanHuoStatDTO> panHuoStatDTOList) {
        if (CollectionUtils.isEmpty(panHuoStatDTOList)) {
            return Collections.emptyList();
        }
        List<PanHuoStatVO> panHuoStatVOList = Lists.newArrayListWithCapacity(panHuoStatDTOList.size());
        for (PanHuoStatDTO panHuoStatDTO : panHuoStatDTOList) {
            PanHuoStatVO panHuoStatVO = convert(panHuoStatDTO);
            if (panHuoStatVO != null) {
                panHuoStatVOList.add(panHuoStatVO);
            }
        }
        return panHuoStatVOList;
    }
}
