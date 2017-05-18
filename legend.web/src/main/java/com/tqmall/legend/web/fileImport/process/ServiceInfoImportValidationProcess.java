package com.tqmall.legend.web.fileImport.process;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.shop.CarLevelService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.fileImport.ImportFailedMessages;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.shop.CarLevel;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.web.fileImport.vo.ServiceInfoImportContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by twg on 16/12/10.
 */
@Slf4j
@Component
public class ServiceInfoImportValidationProcess implements FileImportProcess {
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private CarLevelService carLevelService;
    @Autowired
    private GoodsService goodsService;

    @Override
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is ServiceInfoImportValidationProcess");
        }
        ServiceInfoImportContext serviceInfoImportContext = (ServiceInfoImportContext) fileImportContext;
        Long shopId = serviceInfoImportContext.getShopId();
        Long userId = serviceInfoImportContext.getUserId();
        List<ShopServiceInfo> serviceInfoParams = serviceInfoImportContext.getExcelContents();
        List<String> faildMessages = serviceInfoImportContext.getFaildMessage();
        Map<Integer, List<String>> rowFaildMessages = serviceInfoImportContext.getRowFaildMessages();

        /*已存在的服务信息*/
        Map<String, ShopServiceInfo> serviceInfoMap = getServiceInfoMap(shopId, serviceInfoParams);
        /*已存在的服务类别信息*/
        Map<String, ShopServiceCate> serviceCateMap = getServiceInfoCateMap(shopId, serviceInfoParams);
        /*已存在的车辆级别信息*/
        Map<String, CarLevel> carLevelMap = getCarLevelMap(shopId, serviceInfoParams);

        int serviceInfoNum = shopServiceInfoService.getServiceInfoCount(shopId);

        /*存放需要批量新加的服务类别实例*/
        Map<String, ShopServiceCate> shopServiceCateMap = Maps.newHashMap();
        /*存放需要批量新加的车辆级别实例*/
        Map<String, CarLevel> carLevelHashMap = Maps.newHashMap();

        List<ShopServiceInfo> shopServiceInfoParams = Lists.newArrayList();

        Map<String, ShopServiceInfo> serviceInfoParamMap = Maps.newHashMap();
        for (int i = 0; i < serviceInfoParams.size(); i++) {
            ShopServiceInfo serviceInfoParam = serviceInfoParams.get(i);
            int rowNumber = serviceInfoParam.getRowNumber() + 1;
            String name = serviceInfoParam.getName();
            if (serviceInfoParamMap.containsKey(name) || serviceInfoMap.containsKey(name)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_REPEAT_MESSAGE, rowNumber, name, "服务名称");
                String faildMes = String.format(ImportFailedMessages.FAILED_REPEAT_MESSAGE, name, "服务名称");
                faildMessages.add(faildMessage);
                setRowFalidMessage(rowFaildMessages, rowNumber, faildMes);
                continue;
            }

            if (StringUtils.isBlank(serviceInfoParam.getServiceSn())) {
                String serviceSn = goodsService.generatGoodsSn(i, serviceInfoNum);
                serviceInfoParam.setServiceSn(serviceSn);
            }
            serviceInfoParam.setType(1);
            serviceInfoParam.setShopId(shopId);
            serviceInfoParam.setCreator(userId);
            serviceInfoParam.setTqmallStatus(2);
            serviceInfoParam.setMarketPrice(serviceInfoParam.getServicePrice());
            /*根据服务类别名，设置服务类别id*/
            setCategoryId(shopId, userId, serviceCateMap, shopServiceCateMap, serviceInfoParam);
            /*根据服务车辆级别名，设置车辆级别id*/
            setCarLevelId(shopId, userId, carLevelMap, carLevelHashMap, serviceInfoParam);
            shopServiceInfoParams.add(serviceInfoParam);

            serviceInfoParamMap.put(name, serviceInfoParam);
        }
        serviceInfoImportContext.setExcelContents(shopServiceInfoParams);
        serviceInfoImportContext.setCarLevelHashMap(carLevelHashMap);
        serviceInfoImportContext.setShopServiceCateMap(shopServiceCateMap);
    }

    private void setRowFalidMessage(Map<Integer, List<String>> rowFaildMessages, int rowNumber, String faildMessage) {
        if (rowFaildMessages.containsKey(rowNumber)) {
            List<String> faild = rowFaildMessages.get(rowNumber);
            faild.add(faildMessage);
        } else {
            List<String> failds = Lists.newArrayList();
            failds.add(faildMessage);
            rowFaildMessages.put(rowNumber, failds);
        }
    }

    private Map<String, ShopServiceInfo> getServiceInfoMap(Long shopId, List<ShopServiceInfo> serviceInfoParams) {
        Set<String> serviceInfoNames = Sets.newHashSet();
        for (ShopServiceInfo serviceInfoParam : serviceInfoParams) {
            serviceInfoNames.add(serviceInfoParam.getName());
        }
        if (CollectionUtils.isEmpty(serviceInfoNames)) {
            return Maps.newHashMap();
        }
        List<ShopServiceInfo> shopServiceInfos = shopServiceInfoService.findServiceInfoByNames(shopId, serviceInfoNames.toArray(new String[] { }));
        Map<String, ShopServiceInfo> serviceInfoMap = Maps.newHashMap();
        for (ShopServiceInfo shopServiceInfo : shopServiceInfos) {
            serviceInfoMap.put(shopServiceInfo.getName(), shopServiceInfo);
        }
        return serviceInfoMap;
    }

    private Map<String, ShopServiceCate> getServiceInfoCateMap(Long shopId, List<ShopServiceInfo> serviceInfoParams) {
        Set<String> catNames = Sets.newHashSet();
        for (ShopServiceInfo serviceInfoParam : serviceInfoParams) {
            catNames.add(serviceInfoParam.getCategoryName());
        }
        if (CollectionUtils.isEmpty(catNames)) {
            return Maps.newHashMap();
        }
        List<ShopServiceCate> shopServiceCates0 = shopServiceCateService.findServiceCatesByCatNames(0L, 2, catNames.toArray(new String[] { }));
        Map<String, ShopServiceCate> serviceCateMap = Maps.newHashMap();
        for (ShopServiceCate shopServiceCate : shopServiceCates0) {
            serviceCateMap.put(shopServiceCate.getName(), shopServiceCate);
        }

        List<ShopServiceCate> shopServiceCates = shopServiceCateService.findServiceCatesByCatNames(shopId, 0, catNames.toArray(new String[] { }));
        for (ShopServiceCate shopServiceCate : shopServiceCates) {
            if (!serviceCateMap.containsKey(shopServiceCate.getName())) {
                serviceCateMap.put(shopServiceCate.getName(), shopServiceCate);
            }
        }
        return serviceCateMap;
    }

    private Map<String, CarLevel> getCarLevelMap(Long shopId, List<ShopServiceInfo> serviceInfoParams) {
        Set<String> carLevelNames = Sets.newHashSet();
        for (ShopServiceInfo serviceInfoParam : serviceInfoParams) {
            if (StringUtils.isNotBlank(serviceInfoParam.getCarLevelName())) {
                carLevelNames.add(serviceInfoParam.getCarLevelName());
            }
        }
        if (CollectionUtils.isEmpty(carLevelNames)) {
            return Maps.newHashMap();
        }
        List<CarLevel> carLevels = carLevelService.findCarLevelsByNames(shopId, carLevelNames.toArray(new String[] { }));
        return Maps.uniqueIndex(carLevels, new Function<CarLevel, String>() {
            @Override
            public String apply(CarLevel input) {
                return input.getName();
            }
        });
    }

    private void setCategoryId(Long shopId, Long userId, Map<String, ShopServiceCate> serviceCateMap, Map<String, ShopServiceCate> shopServiceCateMap, ShopServiceInfo serviceInfoParam) {
        String catName = serviceInfoParam.getCategoryName();
        if (!serviceCateMap.containsKey(catName)) {
            ShopServiceCate shopServiceCate = new ShopServiceCate();
            shopServiceCate.setCreator(userId);
            shopServiceCate.setShopId(shopId);
            shopServiceCate.setName(catName);
            shopServiceCate.setCateTag(7);
            shopServiceCateMap.put(catName, shopServiceCate);
        } else {
            ShopServiceCate seviceCate = serviceCateMap.get(catName);
            serviceInfoParam.setCategoryId(seviceCate.getId());
            serviceInfoParam.setCateTag(seviceCate.getCateTag());
        }
    }

    private void setCarLevelId(Long shopId, Long userId, Map<String, CarLevel> carLevelMap, Map<String, CarLevel> carLevelHashMap, ShopServiceInfo serviceInfoParam) {
        String carLevelName = serviceInfoParam.getCarLevelName();
        if (StringUtils.isBlank(carLevelName)) {
            return;
        }
        if (!carLevelMap.containsKey(carLevelName)) {
            CarLevel carLevel = new CarLevel();
            carLevel.setCreator(userId);
            carLevel.setName(carLevelName);
            carLevel.setShopId(shopId);
            carLevelHashMap.put(carLevelName, carLevel);
        } else {
            CarLevel carLevel = carLevelMap.get(carLevelName);
            serviceInfoParam.setCarLevelId(carLevel.getId());
        }
    }

}
