package com.tqmall.legend.dao.marketing;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.marketing.MarketingSms;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface MarketingSmsDao extends BaseDao<MarketingSms> {

    List<MarketingSms> listByLogId(@Param("shopId") Long shopId, @Param("smsLogId") Long smsLogId, @Param("status") Integer status);
}
