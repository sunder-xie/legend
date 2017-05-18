package com.tqmall.legend.biz.account;

import com.tqmall.legend.entity.account.AccountComboServiceRel;

import java.util.Collection;
import java.util.List;

/**
 * Created by twg on 16/12/9.
 */
public interface AccountComboServiceRelService {
    void batchSave(List<AccountComboServiceRel> accountComboServiceRels);

    List<AccountComboServiceRel> listByComboIds(Long shopId, Collection<Long> comboIds);
}
