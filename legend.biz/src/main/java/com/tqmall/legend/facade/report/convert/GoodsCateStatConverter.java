package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.DefaultResult;
import com.tqmall.cube.shop.result.warehouseanalysis.GoodsCateStatDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.report.vo.GoodsCateStatVO;
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
public class GoodsCateStatConverter implements Converter<DefaultResult<GoodsCateStatDTO>,Page<GoodsCateStatVO>> {

    @Override
    public Page<GoodsCateStatVO> convert(DefaultResult<GoodsCateStatDTO> source) {
        Pageable pageable = new PageRequest(source.getPageNum(), source.getPageSize());
        List<GoodsCateStatDTO> goodsCateStatDTOList = source.getContent();
        if (CollectionUtils.isEmpty(goodsCateStatDTOList)) {
            return new DefaultPage<>(Collections.<GoodsCateStatVO>emptyList(), pageable, source.getTotal());
        }

        List<GoodsCateStatVO> goodsCateStatVOList = Lists.newArrayList();
        for (GoodsCateStatDTO goodsCateStatDTO : goodsCateStatDTOList) {
            GoodsCateStatVO goodsCateStatVO = new GoodsCateStatVO();
            goodsCateStatVO.setRank(goodsCateStatDTO.getRank());
            goodsCateStatVO.setGoodsCateName(goodsCateStatDTO.getGoodsCateName());
            goodsCateStatVO.setGoodsCateWay(goodsCateStatDTO.getGoodsCateWay());
            goodsCateStatVO.setBeginGoodsAmount(goodsCateStatDTO.getBeginGoodsAmount());
            goodsCateStatVO.setEndGoodsAmount(goodsCateStatDTO.getEndGoodsAmount());
            goodsCateStatVO.setBorrowGoodsAmount(goodsCateStatDTO.getBorrowGoodsAmount());
            goodsCateStatVO.setLendGoodsAmount(goodsCateStatDTO.getLendGoodsAmount());
            goodsCateStatVO.setRotationRate(goodsCateStatDTO.getRotationRate());
            goodsCateStatVOList.add(goodsCateStatVO);
        }
        return new DefaultPage<>(goodsCateStatVOList, pageable, source.getTotal());
    }
}
