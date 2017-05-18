package com.tqmall.legend.biz.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tqmall.legend.biz.account.param.ComboExportArgs;
import com.tqmall.legend.biz.account.param.MemberExportArgs;
import com.tqmall.legend.entity.account.ComboExportEntity;
import com.tqmall.legend.entity.account.ComboExportSummary;
import com.tqmall.legend.entity.account.MemberExportEntity;
import com.tqmall.legend.entity.account.MemberExportSummary;

/**
 * Created by majian on 17/1/5.
 */
public interface AccountExportService {
    Page<MemberExportEntity> pageMemeber(Pageable pageRequest, MemberExportArgs param);
    Page<ComboExportEntity> pageCombo(Pageable pageRequest, ComboExportArgs param);

    MemberExportSummary summarizeMember(MemberExportArgs args);
    ComboExportSummary summarizeCombo(ComboExportArgs args);
}
