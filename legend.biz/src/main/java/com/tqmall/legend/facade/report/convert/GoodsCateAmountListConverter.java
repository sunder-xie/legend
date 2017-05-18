package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.warehouseanalysis.GoodsCateAmountDTO;
import com.tqmall.legend.facade.report.vo.GoodsCateAmountVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2016/11/8.
 */
public class GoodsCateAmountListConverter implements Converter<List<GoodsCateAmountDTO>, List<GoodsCateAmountVO>> {

    @Override
    public List<GoodsCateAmountVO> convert(List<GoodsCateAmountDTO> goodsCateAmountDTOs) {
        if (CollectionUtils.isEmpty(goodsCateAmountDTOs)) {
            return Collections.emptyList();
        }
        List<GoodsCateAmountVO> goodsCateAmountVOList = Lists.newArrayListWithCapacity(goodsCateAmountDTOs.size());
        for (GoodsCateAmountDTO goodsCateAmountDTO : goodsCateAmountDTOs) {
            GoodsCateAmountVO goodsCateAmountVO = new GoodsCateAmountVO();
            goodsCateAmountVO.setGoodsCateName(goodsCateAmountDTO.getGoodsCateName());
            goodsCateAmountVO.setBeginGoodsAmount(goodsCateAmountDTO.getBeginGoodsAmount());
            goodsCateAmountVO.setEndGoodsAmount(goodsCateAmountDTO.getEndGoodsAmount());
            goodsCateAmountVOList.add(goodsCateAmountVO);
        }
        return goodsCateAmountVOList;
    }
}
