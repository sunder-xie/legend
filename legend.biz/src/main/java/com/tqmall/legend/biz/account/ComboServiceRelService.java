package com.tqmall.legend.biz.account;

import com.google.common.base.Optional;
import com.tqmall.legend.entity.account.AccountComboServiceRel;

import java.util.Collection;
import java.util.List;

/**
 * Created by majian on 16/6/17.
 */
public interface ComboServiceRelService {
    /**
     * 获取combo实例关联的service
     *
     * @param shopId
     * @param comboServiceRelId
     * @return
     */
    AccountComboServiceRel findById(Long shopId, Long comboServiceRelId);
    List<AccountComboServiceRel> findByComboId(Long shopId, Long comboId);
    List<AccountComboServiceRel> listByComboIds(Long shopId, Collection<Long> comboIds);

    //获取计次卡内的服务信息
    Optional<AccountComboServiceRel> findByService(Long serviceId, Long comboId, Long shopId);

    List<AccountComboServiceRel> listByIds(Collection<Long> ids);

}
