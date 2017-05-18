package com.tqmall.legend.biz.customer;

import com.tqmall.legend.entity.customer.LicenseCity;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/6.
 */
public interface LicenseMatchCityService {
    /**
     * 通过参数获取车牌号前缀集合
     * @param paraMap 参数
     * @return 返回城市对应车牌号集合
     */
    List<LicenseCity> select(Map<String, Object> paraMap);

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

    /**
     * 根据门店id获取车牌号前缀
     * @param shopId
     * @return
     */
    public String getLicenseCityByShopId(Long shopId);
}
