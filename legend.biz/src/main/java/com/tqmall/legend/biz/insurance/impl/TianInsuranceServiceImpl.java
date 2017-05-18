package com.tqmall.legend.biz.insurance.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.insurance.TianInsuranceService;
import com.tqmall.legend.biz.shop.ServiceTemplateService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.insurance.TianInsuranceCodeDao;
import com.tqmall.legend.entity.insurance.TianInsuranceCode;
import com.tqmall.legend.entity.shop.ServiceTemplate;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.insurance.TianServiceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lilige on 16/7/29.
 */
@Slf4j
@Service
public class TianInsuranceServiceImpl extends BaseServiceImpl implements TianInsuranceService {
    @Autowired
    private TianInsuranceCodeDao tianInsuranceCodeDao;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ServiceTemplateService serviceTemplateService;
    @Autowired
    private ShopServiceCateService serviceCateService;

    /**
     * 获取天安券号信息
     *
     * @param searchParams
     * @return
     */
    @Override
    public List<TianInsuranceCode> select(Map<String, Object> searchParams) {
        List<TianInsuranceCode> tianInsuranceCodes = tianInsuranceCodeDao.select(searchParams);
        return tianInsuranceCodes;
    }

    @Override
    public boolean updateTianInsuranceCode(TianInsuranceCode tianInsuranceCode) {
        return tianInsuranceCodeDao.updateById(tianInsuranceCode) > 0 ? true : false;
    }

    /**
     * 根据车牌号获取对应的门店服务套餐
     *
     * @param carLicense
     * @return
     */
    @Override
    public Result<ShopServiceInfo> getShopServiceInfoByLicense(String carLicense,Long shopId) {
        Result<TianInsuranceCode> codeResult = this.getCodeByLicense(carLicense);
        if (!codeResult.isSuccess()){
            return Result.wrapErrorResult("",codeResult.getErrorMsg());
        }
        TianInsuranceCode code = codeResult.getData();
        String serviceItem = code.getServiceItem();
        Long serviceId = TianServiceEnum.getIdByName(serviceItem);
        if (null == serviceId){
            return Result.wrapErrorResult("","系统无法查询对应车牌天安赠送服务信息");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("parentId",serviceId);
        map.put("shopId",shopId);
        List<ShopServiceInfo> shopServiceInfos = shopServiceInfoService.select(map);
        if (CollectionUtils.isEmpty(shopServiceInfos)){
            return Result.wrapErrorResult("","系统无法查询对应车牌天安赠送服务信息");
        }
        return Result.wrapSuccessfulResult(shopServiceInfos.get(0));
    }

    @Override
    public Result<TianInsuranceCode> getCodeByLicense(String carLicense) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("carLicense",carLicense);
        params.put("nowTime",new Date());
        params.put("insuredStatus", 1);
        List<TianInsuranceCode> codes = tianInsuranceCodeDao.select(params);
        if (CollectionUtils.isEmpty(codes)){
            return Result.wrapErrorResult("","系统无法查询对应车牌天安赠送服务信息");
        }
        TianInsuranceCode code = codes.get(0);
        //设置冗余字段
        String service = code.getServiceItem();
        Long serviceTemplateId = TianServiceEnum.getIdByName(service);
        ServiceTemplate template = serviceTemplateService.selectById(serviceTemplateId);
        if (null == template){
            log.error("","未找到对应的服务模版,模版id:{}",serviceTemplateId);
            return Result.wrapErrorResult("","系统无法查询对应车牌天安赠送服务信息");
        }
        code.setServiceName(template.getName());
        code.setSettlePrice(template.getSettlePrice());
        Long cateId = template.getCateId();
        ShopServiceCate serviceCate = serviceCateService.selectById(cateId);
        if (null == serviceCate){
            return Result.wrapSuccessfulResult(code);
        }
        code.setServiceCate(serviceCate.getName());
        return Result.wrapSuccessfulResult(code);
    }

}
