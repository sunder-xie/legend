package com.tqmall.legend.web.fileImport.process;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.shop.CarLevelService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.shop.CarLevel;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.web.fileImport.vo.ServiceInfoImportContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/12/10.
 */
@Slf4j
@Component
public class ServiceInfoImportProcess implements FileImportProcess {
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private CarLevelService carLevelService;

    @Override
    @Transactional
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is ServiceInfoImportProcess");
        }
        ServiceInfoImportContext serviceInfoImportContext = (ServiceInfoImportContext) fileImportContext;
        Long shopId = serviceInfoImportContext.getShopId();
        List<ShopServiceInfo> serviceInfos = serviceInfoImportContext.getExcelContents();
        Map<String, CarLevel> carLevelHashMap = serviceInfoImportContext.getCarLevelHashMap();
        Map<String, ShopServiceCate> shopServiceCateMap = serviceInfoImportContext.getShopServiceCateMap();

        if (!CollectionUtils.isEmpty(serviceInfos)) {
            /*批量保存服务类别，并获取对应的信息*/
            shopServiceCateMap = getShopServiceCateMap(shopId, shopServiceCateMap);
            /*批量保存车辆级别，并获取对应的信息*/
            carLevelHashMap = getCarLevelMap(shopId, carLevelHashMap);
            setCatIdAndCarLevelId(shopServiceCateMap, carLevelHashMap, serviceInfos);
            shopServiceInfoService.batchInsert(serviceInfos);
            serviceInfoImportContext.setSuccessNum(serviceInfos.size());
            cacheComponent.reload(CacheKeyConstant.SERVICE_CATEGORY);
        }
    }


    private Map<String, ShopServiceCate> getShopServiceCateMap(Long shopId, Map<String, ShopServiceCate> shopServiceCateMap) {
        if (!shopServiceCateMap.isEmpty()) {
            List<ShopServiceCate> shopServiceCates = Lists.newArrayList();
            List<String> shopServiceCateNames = Lists.newArrayList();
            for (Map.Entry<String, ShopServiceCate> serviceCateEntry : shopServiceCateMap.entrySet()) {
                shopServiceCates.add(serviceCateEntry.getValue());
                shopServiceCateNames.add(serviceCateEntry.getKey());
            }
            shopServiceCateService.batchSave(shopServiceCates);
            List<ShopServiceCate> serviceCates = shopServiceCateService.findServiceCatesByCatNames(shopId, 0, shopServiceCateNames.toArray(new String[] { }));
            shopServiceCateMap = Maps.uniqueIndex(serviceCates, new Function<ShopServiceCate, String>() {
                @Override
                public String apply(ShopServiceCate input) {
                    return input.getName();
                }
            });
        }
        return shopServiceCateMap;
    }

    private Map<String, CarLevel> getCarLevelMap(Long shopId, Map<String, CarLevel> carLevelHashMap) {
        if (!carLevelHashMap.isEmpty()) {
            List<CarLevel> carLevels = Lists.newArrayList();
            List<String> carLevelNames = Lists.newArrayList();
            for (Map.Entry<String, CarLevel> carLevelEntry : carLevelHashMap.entrySet()) {
                carLevels.add(carLevelEntry.getValue());
                carLevelNames.add(carLevelEntry.getKey());
            }
            carLevelService.batchSave(carLevels);
            List<CarLevel> carLevelList = carLevelService.findCarLevelsByNames(shopId, carLevelNames.toArray(new String[] { }));
            carLevelHashMap = Maps.uniqueIndex(carLevelList, new Function<CarLevel, String>() {
                @Override
                public String apply(CarLevel input) {
                    return input.getName();
                }
            });
        }
        return carLevelHashMap;
    }

    private void setCatIdAndCarLevelId(Map<String, ShopServiceCate> shopServiceCateMap, Map<String, CarLevel> carLevelHashMap, List<ShopServiceInfo> shopServiceInfoParams) {
        for (ShopServiceInfo shopServiceInfoParam : shopServiceInfoParams) {
            String catName = shopServiceInfoParam.getCategoryName();
            String carLeveName = shopServiceInfoParam.getCarLevelName();
            if (shopServiceInfoParam.getCategoryId() == null && shopServiceCateMap.containsKey(catName)) {
                shopServiceInfoParam.setCategoryId(shopServiceCateMap.get(catName).getId());
                shopServiceInfoParam.setCateTag(shopServiceCateMap.get(catName).getCateTag());
            }
            if (shopServiceInfoParam.getCarLevelId() == null && carLevelHashMap.containsKey(carLeveName)) {
                shopServiceInfoParam.setCarLevelId(carLevelHashMap.get(carLeveName).getId());
            }
        }
    }
}
