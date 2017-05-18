package com.tqmall.legend.dao.question;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.question.ExpertBonusRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ExpertBonusRecordDao extends BaseDao<ExpertBonusRecord> {
    public BigDecimal selectSumAmount(Map<String,Object> param);

    public int batchUpdateToWithdraw(Map<String,Object> param);
}
