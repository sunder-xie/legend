package com.tqmall.legend.biz.marketing.ng;



import com.tqmall.legend.entity.marketing.ng.NoteEffect;
import com.tqmall.legend.entity.marketing.ng.NoteEffectVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by wjc on 3/14/2016.
 */
public interface NoteEffectService {
    Integer count(Map<String,Object> params);

    BigDecimal effectAmount(Map<String,Object> params);

    Page<NoteEffect> selectList(Pageable pageable,Map<String,Object> params);

    List<NoteEffectVo> selectCountWithType(Map<String,Object> params);

    /**
     * 获取有效激活客户（去重）
     * @param param
     * @return
     */
    List<NoteEffect> selectActiveCustomer(Map param);

    /**
     * 总数
     * @param param
     * @return
     */
    Integer selectCount(Map param);

    List<NoteEffectVo> selectCountWithWay(Map<String, Object> params);
}
