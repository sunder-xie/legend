package com.tqmall.legend.biz.marketing;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.entity.marketing.MarketingCase;
import com.tqmall.legend.entity.marketing.MarketingCaseService;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/7.
 */
public interface MarketingActivityService {

    /**
     * 获取门店营销活动列表
     * @param param 入参
     * @return 返回门店营销活动列表
     */
    public List<MarketingCase> select(Map param);

    /**
     * 保存门店营销活动实例
     * @param marketingCase 门店营销活动实例
     * @return 返回门店营销活动实例id
     */
    public Integer save(MarketingCase marketingCase,UserInfo userInfo);

    /**
     * 更新门店营销活动实例
     * @param marketingCase 门店营销活动实例
     * @return 返回门店营销活动实例id
     */
    public Integer update(MarketingCase marketingCase,UserInfo userInfo);

    /**
     * 门店营销活动服务套餐列表
     * @param param 入参
     * @return 返回门店营销活动服务套餐列表
     */
    public List<MarketingCaseService> selectCaseService(Map param);

    /**
     * 批量插入
     * @param marketingCases
     * @return
     */
    Integer batchInsert(List<MarketingCase> marketingCases);

    /**
     * 通过ID更新
     *
     * @param marketingCase
     * @return
     */
    int updateById(MarketingCase marketingCase);




}
