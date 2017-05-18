package com.tqmall.legend.facade.magic;

import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.magic.vo.PaintUseRecordVo;
import com.tqmall.magic.object.param.paint.PaintUseRecordParam;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 业务场景：油漆使用记录相关业务
 * Created by shulin on 16/11/15.
 */
public interface PaintUseRecordFacade {

    /**
     * 获取使用记录，包含明细
     *
     * @param shopId
     * @param useRecordId
     * @return
     */
    PaintUseRecordVo getPaintUseRecord(Long shopId, Long useRecordId);

    /**
     * 获取油漆使用记录列表（分页）
     *
     * @param param
     * @return
     */
    DefaultPage<PaintUseRecordVo> getPaintUseRecordListByPage(PaintUseRecordParam param,Pageable pageable);


    /**
     * 新增油漆使用记录
     *
     * @param paintUseRecordVo
     * @return
     */
    Long addPaintUseRecord(PaintUseRecordVo paintUseRecordVo,Long shopI);

}
