package com.tqmall.legend.dao.marketing.ng;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface CustomerInfoDao extends BaseDao<CustomerInfo> {
    Integer selectWithTypeCount(Map<String, Object> param);


    List<CustomerInfo> selectWithType(Map<String, Object> param);

    /**
     * 获取在提醒范围内的客户信息
     */
    List<CustomerInfo> selectCustomerInfoIsNote(Map param);

    List<CustomerInfo> selectAccurate(Map<String, Object> param);

    Integer selectAccurateCount(Map<String, Object> param);

    /**
     * 客户信息数
     * @param param
     * @return
     */
    Integer getCountOfCustomerInfo(Map param);

    /**
     * 客户列表
     * @param param
     * @return
     */
    List<CustomerInfo> getCustomerInfoList(Map param);

    List<CustomerInfo> getCustomerInfoListFilterTimeIsNull(Map param);

    Integer selectCountByCustomerTag(Map<String, Object> params);

    List<CustomerInfo> selectByCustomerTag(Map<String, Object> params);

    Integer selectCountByCustomerCompany(Map<String, Object> params);

    List<CustomerInfo> selectByCustomerCompany(Map<String, Object> params);

    List<CustomerInfo> listByCarIds(@Param("shopId") Long shopId, @Param("carIds") Collection<Long> carIds);

    List<CustomerInfo> selectWithCardInfo(Map param);
    List<CustomerInfo> selectAccurateWithCardInfo(Map param);

    Integer selectCount2(Map param);
}
