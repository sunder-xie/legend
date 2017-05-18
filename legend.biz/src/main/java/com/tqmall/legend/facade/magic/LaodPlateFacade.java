package com.tqmall.legend.facade.magic;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.magic.vo.LoadVO;
import com.tqmall.legend.facade.magic.vo.PlateVO;
import com.tqmall.legend.facade.magic.vo.ProductionLineVO;

import java.util.List;

/**
 * Created by yanxinyin on 16/7/22.
 */
public interface LaodPlateFacade {
    public Result<LoadVO> getMessage(Long shopId) ;

    public PlateVO getPlateVO(PlateVO plateVO, Long shopId, Long lineId, Long type );

    List<ProductionLineVO> getProductionLine(Long shopId);

    }
