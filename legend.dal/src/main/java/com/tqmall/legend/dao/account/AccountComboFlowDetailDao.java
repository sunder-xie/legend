package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.AccountComboFlowDetail;
import com.tqmall.legend.entity.account.vo.RechargeComboFlowBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by majian on 16/6/2.
 */
@MyBatisRepository
public interface AccountComboFlowDetailDao extends BaseDao<AccountComboFlowDetail> {

    List<AccountComboFlowDetail> selectByFlowIds(List<Long> list);

}
