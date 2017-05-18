package com.tqmall.legend.facade.magic;

import com.tqmall.legend.facade.magic.vo.WorkTimeVo;

/**
 *
 * Created by shulin on 16/7/27.
 */
public interface WorkTimeFacade {
    /**
     * 初始化工作时间对象
     *
     * @param shopId
     * @return
     */
    public WorkTimeVo initWorkTime(Long shopId);
}
