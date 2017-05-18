package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.order.SprayOrderServiceCateDTO;
import com.tqmall.legend.facade.report.vo.SprayOrderServiceCateVO;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2016/12/13.
 */
public class SprayOrderServiceCateConvert {

    public static SprayOrderServiceCateVO convertVO(SprayOrderServiceCateDTO dto) {
        if (dto == null) {
            return null;
        }
        SprayOrderServiceCateVO vo = new SprayOrderServiceCateVO();
        vo.setRank(dto.getRank());
        vo.setServiceCatName(dto.getServiceCatName());
        vo.setServiceCatId(dto.getServiceCatId());
        vo.setSaleNum(dto.getSaleNum());
        vo.setSaleAmount(dto.getSaleAmount());
        vo.setSurfaceNum(dto.getSurfaceNum());
        return vo;
    }

    public static List<SprayOrderServiceCateVO> convertVOList(List<SprayOrderServiceCateDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Collections.emptyList();
        }
        List<SprayOrderServiceCateVO> voList = Lists.newArrayListWithCapacity(dtoList.size());
        for (SprayOrderServiceCateDTO dto : dtoList) {
            SprayOrderServiceCateVO vo = convertVO(dto);
            if (vo != null) {
                voList.add(vo);
            }
        }
        return voList;
    }
}
