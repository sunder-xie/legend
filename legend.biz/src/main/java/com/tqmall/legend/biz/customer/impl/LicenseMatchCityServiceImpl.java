package com.tqmall.legend.biz.customer.impl;

import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.customer.LicenseMatchCityService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.dao.customer.LicenseCityDao;
import com.tqmall.legend.entity.customer.LicenseCity;
import com.tqmall.legend.entity.shop.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/6.
 */
@Service
public class LicenseMatchCityServiceImpl implements LicenseMatchCityService {

    @Autowired
    private LicenseCityDao licenseCityDao;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CacheComponent<List<LicenseCity>> cacheComponent;

    @Override
    public List<LicenseCity> select(Map<String, Object> paraMap) {
        return licenseCityDao.select(paraMap);
    }

    @Override
    public List<LicenseCity> getLicenseByProvince(Map<String, Object> paraMap) {
        return licenseCityDao.getLicenseByProvince(paraMap);
    }

    @Override
    public List<LicenseCity> getByProvince() {
        return licenseCityDao.getByProvince();
    }

    /**
     * 根据门店id获取门店，再根据门店city_id获取车牌号前缀
     * 如383是杭州，返回浙A
     *
     * @param shopId
     * @return
     */
    @Override
    public String getLicenseCityByShopId(Long shopId) {
        Shop shop = shopService.selectById(shopId);
        String license = "";
        if(shop != null){
            Long cityId = shop.getCity();
            List<LicenseCity> licenseCityList = cacheComponent.getCache(CacheKeyConstant.LICENSE_CITY);
            if(!CollectionUtils.isEmpty(licenseCityList)){
                for(LicenseCity licenseCity : licenseCityList){
                    Long cacheCityId = Long.parseLong(licenseCity.getCityId());
                    if(Long.compare(cityId,cacheCityId) == 0){
                        license = licenseCity.getLicense();
                    }
                }
            }
        }
        return license;
    }
}
