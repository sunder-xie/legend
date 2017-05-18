package com.tqmall.legend.facade.marketing.gather;

import com.tqmall.legend.facade.marketing.gather.vo.LaXinStatVO;
import com.tqmall.legend.facade.marketing.gather.vo.PanHuoStatVO;

import java.util.List;

/**
 * Created by xin on 2016/12/21.
 */
public interface GatherEffectFacade {

    /**
     * 统计盘活效果
     * @param shopId
     * @param userId
     * @return
     */
    List<PanHuoStatVO> getPanHuoStat(Long shopId, Long userId, String dateStr);

    /**
     * 统计拉新效果
     * @param shopId
     * @param userId
     * @return
     */
    LaXinStatVO getLaXinStat(Long shopId, Long userId, String dateStr);
}
