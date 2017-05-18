package com.tqmall.legend.dao.customer;

import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.entity.customer.LicenseCity;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/6.
 */
@MyBatisRepository
public interface LicenseCityDao extends BaseDao<LicenseCity> {
    /**
     * 通过省份简称获取车牌号前缀
     * @param paraMap 参数
     * @return 返回城市对应车牌号集合
     */
    List<LicenseCity> getLicenseByProvince(Map<String, Object> paraMap);

    /**
     * 获取省份简称
     * @return 返回城市对应车牌号集合
     */
    List<LicenseCity> getByProvince();
}
