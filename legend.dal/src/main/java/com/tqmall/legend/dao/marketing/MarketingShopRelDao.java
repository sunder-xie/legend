package com.tqmall.legend.dao.marketing;


import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.marketing.MarketingShopRel;

@MyBatisRepository
public interface MarketingShopRelDao extends BaseDao<MarketingShopRel> {
    public  void updateByShopId(MarketingShopRel marketingShopRel);

}
