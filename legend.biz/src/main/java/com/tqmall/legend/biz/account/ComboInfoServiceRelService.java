package com.tqmall.legend.biz.account;

import com.tqmall.legend.entity.account.ComboInfoServiceRel;

import java.util.List;

/**
 * Created by majian on 16/6/20.
 */
public interface ComboInfoServiceRelService {
    /**
     * 查找combInfoId对应的service
     *
     * @param shopId
     * @param comboInfoId
     * @return
     */
    List<ComboInfoServiceRel> findByComboInfoId(Long shopId, Long comboInfoId);

    List<ComboInfoServiceRel> findByComboInfoIds(Long shopId, List<Long> comboInfoIds);

    /**
     * 通过服务名，获取计次卡类型服务项目信息
     * @param shopId
     * @param serviceNames
     * @return
     */
    List<ComboInfoServiceRel> findByServiceNames(Long shopId,List<String> serviceNames);

    List<ComboInfoServiceRel> findByServiceId(long serviceId);
}
