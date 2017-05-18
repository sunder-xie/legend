package com.tqmall.legend.facade.marketing.gather;

import com.tqmall.legend.facade.marketing.gather.vo.GatherEffectDetailVO;
import com.tqmall.legend.facade.marketing.gather.vo.GatherOperateStatVO;
import com.tqmall.legend.facade.marketing.gather.vo.PerformanceStatVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by xin on 2016/12/22.
 */
public interface GatherDetailFacade {

    Page<GatherEffectDetailVO> getGatherEffectDetailPage(Long shopId, Long userId, String dateStr, Pageable pageable);

    GatherOperateStatVO getGatherOperateStat(Long shopId, Long userId, String dateStr);

    PerformanceStatVO getPerformanceStat(Long shopId, Long userId, String dateStr);

    List<GatherEffectDetailVO> getGatherDetailList(Long shopId, Long userId, String dateStr, int pageNum, int pageSize);
}
