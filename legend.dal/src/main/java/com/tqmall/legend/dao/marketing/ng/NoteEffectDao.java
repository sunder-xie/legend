package com.tqmall.legend.dao.marketing.ng;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.marketing.ng.NoteEffect;
import com.tqmall.legend.entity.marketing.ng.NoteEffectVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface NoteEffectDao extends BaseDao<NoteEffect> {

    /**
     * 查询客情提醒基本信息总数
     *
     * @param param
     * @return
     */
    Integer selectNoteInfoCount(Map<String, Object> param);

    /**
     * 查询增收
     * @param params
     * @return
     */
    BigDecimal effectAmount(Map<String,Object> params);

    /**
     * 根据提醒类型查询
     * @param params
     * @return
     */
    List<NoteEffectVo> selectCountWithType(Map<String,Object> params);

    /**
     * 根据提醒方式查询
     * @param params
     * @return
     */
    List<NoteEffectVo> selectCountWithWay(Map<String,Object> params);
    /**
     * 获取有效激活客户（去重）
     * @param param
     * @return
     */
    List<NoteEffect> selectActiveCustomer(Map param);
}
