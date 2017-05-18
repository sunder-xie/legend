package com.tqmall.legend.web.fileImport.vo;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.ComboInfoServiceRel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by twg on 16/12/7.
 */
@Getter
@Setter
public class AccountComboImportContext extends CommonFileImportContext<AccountCombo> {
    /*计次卡类型服务项目*/
    private Multimap<Long, ComboInfoServiceRel> comboInfoServiceRelMap = HashMultimap.create();

}
