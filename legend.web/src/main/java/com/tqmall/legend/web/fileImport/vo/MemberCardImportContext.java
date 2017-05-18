package com.tqmall.legend.web.fileImport.vo;

import com.google.common.collect.Lists;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.entity.account.MemberCard;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by twg on 16/12/7.
 */
@Setter
@Getter
public class MemberCardImportContext extends CommonFileImportContext<MemberCard> {
    private List<MemberCardTypeInfo> memberCardTypeInfos = Lists.newArrayList();
}




