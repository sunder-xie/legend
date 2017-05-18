package com.tqmall.legend.biz.insurance;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.insurance.TianInsuranceCode;
import com.tqmall.legend.entity.shop.ShopServiceInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 天安核销码service
 * Created by lilige on 16/7/29.
 */
public interface TianInsuranceService {

    /**
     * 获取天安券号信息
     *
     * @return
     */
    List<TianInsuranceCode> select(Map<String,Object> searchParams);

    /**
     * 更新天安券号信息
     *
     * @param tianInsuranceCode
     * @return
     */
    boolean updateTianInsuranceCode(TianInsuranceCode tianInsuranceCode);

    /**
     * 根据车牌号获取对应的门店服务套餐
     * @param carLicense
     * @return
     */
    Result<ShopServiceInfo> getShopServiceInfoByLicense(String carLicense , Long shopId);

    Result<TianInsuranceCode> getCodeByLicense(String carLicense);

}
