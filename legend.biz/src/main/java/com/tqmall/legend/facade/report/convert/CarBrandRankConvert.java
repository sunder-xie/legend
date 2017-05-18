package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.BrandRankDTO;
import com.tqmall.legend.facade.report.vo.CarBrandRankVo;
import com.tqmall.wheel.lang.Objects;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
public class CarBrandRankConvert implements Converter<List<BrandRankDTO>, List<CarBrandRankVo>> {

    @Override
    public List<CarBrandRankVo> convert(List<BrandRankDTO> source) {
        List<CarBrandRankVo> carBrandRankVos = Lists.newArrayListWithCapacity(Objects.isNull(source) ? 0 : source.size());
        if (Objects.isNotNull(source)) {
            for (BrandRankDTO brandRankDTO : source) {
                CarBrandRankVo carBrandRankVo = new CarBrandRankVo();
                carBrandRankVo.setCarBrand(brandRankDTO.getCarBrand());
                carBrandRankVo.setRank(brandRankDTO.getRank());
                carBrandRankVo.setReceptionNumber(brandRankDTO.getReceptionNumber());
                carBrandRankVos.add(carBrandRankVo);
            }
        }
        return carBrandRankVos;
    }
}
