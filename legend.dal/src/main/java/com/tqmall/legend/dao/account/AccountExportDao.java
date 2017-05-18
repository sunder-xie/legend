package com.tqmall.legend.dao.account;

import java.util.List;

import com.tqmall.legend.dao.account.param.ComboExportPagedParam;
import com.tqmall.legend.dao.account.param.ComboExportParam;
import com.tqmall.legend.dao.account.param.MemberExportPagedParam;
import com.tqmall.legend.dao.account.param.MemberExportParam;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.ComboExportEntity;
import com.tqmall.legend.entity.account.ComboExportSummary;
import com.tqmall.legend.entity.account.MemberExportEntity;
import com.tqmall.legend.entity.account.MemberExportSummary;

/**
 * Created by majian on 17/1/5.
 */
@MyBatisRepository
public interface AccountExportDao {

    Integer countMember(MemberExportParam param);
    List<MemberExportEntity> pageMember(MemberExportPagedParam pagedParam);
    Integer countCombo(ComboExportParam param);
    List<ComboExportEntity> pageCombo(ComboExportPagedParam pagedParam);

    MemberExportSummary summarizeMember(MemberExportParam param);

    ComboExportSummary summarizeCombo(ComboExportParam param);
}
