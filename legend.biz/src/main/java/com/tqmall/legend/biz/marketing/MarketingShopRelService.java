package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.MarketingShopRel;

import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 15/6/10.
 */
public interface MarketingShopRelService{

    /**
     * 根据shopId查找MarketingSmsShopRel对象
     * @param params
     * @return
     */
    public MarketingShopRel selectOne(Map<String,Object> params);
    
    public void updateById(MarketingShopRel marketingShopRel);

    public  void updateByShopId(MarketingShopRel marketingShopRel);
    
    public List<MarketingShopRel> select(Map<String,Object> params);

    boolean insert(MarketingShopRel marketingShopRel);

    /**
     * 根据shopId查找MarketingSmsShopRel对象
     * @param shopID
     * @return
     */
    MarketingShopRel selectOneById(Long shopID);

    void updateRemain(Long shopId, Integer usedCount);
}
