package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.DefaultResult;
import com.tqmall.cube.shop.result.warehouseanalysis.GoodsStatDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.report.vo.GoodsStatVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2016/11/8.
 */
public class GoodsStatConverter implements Converter<DefaultResult<GoodsStatDTO>, Page<GoodsStatVO>> {

    @Override
    public Page<GoodsStatVO> convert(DefaultResult<GoodsStatDTO> source) {
        Pageable pageable = new PageRequest(source.getPageNum(), source.getPageSize());
        List<GoodsStatDTO> goodsStatDTOList = source.getContent();
        if (CollectionUtils.isEmpty(goodsStatDTOList)) {
            return new DefaultPage<>(Collections.<GoodsStatVO>emptyList(), pageable, source.getTotal());
        }
        List<GoodsStatVO> goodsStatVOList = Lists.newArrayList();
        for (GoodsStatDTO goodsStatDTO : goodsStatDTOList) {
            GoodsStatVO goodsStatVO = new GoodsStatVO();
            goodsStatVO.setRank(goodsStatDTO.getRank());
            goodsStatVO.setGoodsId(goodsStatDTO.getGoodsId());
            goodsStatVO.setGoodsName(goodsStatDTO.getGoodsName());
            goodsStatVO.setMeasureUnit(goodsStatDTO.getMeasureUnit());
            goodsStatVO.setBeginGoodsNumber(goodsStatDTO.getBeginGoodsNumber());
            goodsStatVO.setBeginGoodsAmount(goodsStatDTO.getBeginGoodsAmount());
            goodsStatVO.setEndGoodsNumber(goodsStatDTO.getEndGoodsNumber());
            goodsStatVO.setEndGoodsAmount(goodsStatDTO.getEndGoodsAmount());
            goodsStatVO.setBorrowGoodsNumber(goodsStatDTO.getBorrowGoodsNumber());
            goodsStatVO.setBorrowGoodsAmount(goodsStatDTO.getBorrowGoodsAmount());
            goodsStatVO.setLendGoodsNumber(goodsStatDTO.getLendGoodsNumber());
            goodsStatVO.setLendGoodsAmount(goodsStatDTO.getLendGoodsAmount());
            goodsStatVO.setAverageNumber(goodsStatDTO.getAverageNumber());
            goodsStatVO.setProfitGoodsNumber(goodsStatDTO.getProfitGoodsNumber());
            goodsStatVO.setProfitGoodsAmount(goodsStatDTO.getProfitGoodsAmount());
            goodsStatVO.setSuggestGoodsNumber(goodsStatDTO.getSuggestGoodsNumber());
            goodsStatVO.setRotationRate(goodsStatDTO.getRotationRate());
            goodsStatVOList.add(goodsStatVO);
        }
        return new DefaultPage<>(goodsStatVOList, pageable, source.getTotal());
    }
}
