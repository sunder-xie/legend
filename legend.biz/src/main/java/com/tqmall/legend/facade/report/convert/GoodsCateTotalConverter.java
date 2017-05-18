package com.tqmall.legend.facade.report.convert;

import com.tqmall.cube.shop.result.warehouseanalysis.GoodsCateTotalDTO;
import com.tqmall.legend.facade.report.vo.GoodsCateTotalVO;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by xin on 2016/11/8.
 */
public class GoodsCateTotalConverter implements Converter<GoodsCateTotalDTO,GoodsCateTotalVO> {

    @Override
    public GoodsCateTotalVO convert(GoodsCateTotalDTO goodsCateTotalDTO) {
        GoodsCateTotalVO goodsCateTotalVO = new GoodsCateTotalVO();
        if (goodsCateTotalDTO == null) {
            return goodsCateTotalVO;
        }
        goodsCateTotalVO.setTotalBeginGoodsAmount(goodsCateTotalDTO.getTotalBeginGoodsAmount());
        goodsCateTotalVO.setTotalEndGoodsAmount(goodsCateTotalDTO.getTotalEndGoodsAmount());
        goodsCateTotalVO.setTotalBorrowGoodsAmount(goodsCateTotalDTO.getTotalBorrowGoodsAmount());
        goodsCateTotalVO.setTotalLendGoodsAmount(goodsCateTotalDTO.getTotalLendGoodsAmount());
        goodsCateTotalVO.setRotationRate(goodsCateTotalDTO.getRotationRate());
        return goodsCateTotalVO;
    }
}
