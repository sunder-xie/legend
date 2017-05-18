package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.DefaultResult;
import com.tqmall.cube.shop.result.SeriesRankDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.report.vo.SeriesRankVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by tanghao on 16/9/6.
 */
public class SeriesRankConverter implements Converter<DefaultResult<SeriesRankDTO>,Page<SeriesRankVO>> {

    @Override
    public Page<SeriesRankVO> convert(DefaultResult<SeriesRankDTO> source) {
        List<SeriesRankDTO> seriesRankDTOs = source.getContent();
        Page<SeriesRankVO> destination = null;
        List<SeriesRankVO> seriesRankVOs = Lists.newArrayListWithCapacity(null != seriesRankDTOs?seriesRankDTOs.size():0);
        if(null != seriesRankDTOs){
            for(SeriesRankDTO seriesRankDTO : seriesRankDTOs){
                SeriesRankVO seriesRankVO = new SeriesRankVO();
                seriesRankVO.setCarBrand(seriesRankDTO.getCarBrand());
                seriesRankVO.setCarSeries(seriesRankDTO.getCarSeries());
                seriesRankVO.setCost(seriesRankDTO.getCost());
                seriesRankVO.setIncome(seriesRankDTO.getIncome());
                seriesRankVO.setProfit(seriesRankDTO.getProfit());
                seriesRankVO.setProfitRate(seriesRankDTO.getProfitRate());
                seriesRankVO.setRank(seriesRankDTO.getRank());
                seriesRankVO.setReceptionNumber(seriesRankDTO.getReceptionNumber());
                seriesRankVOs.add(seriesRankVO);
            }
            Pageable pageable = new PageRequest(source.getPageNum(), source.getPageSize());
            destination = new DefaultPage<SeriesRankVO>(seriesRankVOs,pageable,source.getTotal());
        }
        return destination;
    }
}
