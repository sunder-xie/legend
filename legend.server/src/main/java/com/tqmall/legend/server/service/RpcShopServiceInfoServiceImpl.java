package com.tqmall.legend.server.service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.provider.popularsort.RpcPopularSortService;
import com.tqmall.cube.shop.result.popularsort.PopularDataDTO;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.vo.OrderServicesVo;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.shop.ServiceGoodsSuiteService;
import com.tqmall.legend.biz.shop.ServiceTemplateCateRelService;
import com.tqmall.legend.biz.shop.ServiceTemplateService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoRelService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.shop.bo.ServiceStatisBo;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.SuiteGoods;
import com.tqmall.legend.entity.pub.service.ServiceCateVo;
import com.tqmall.legend.entity.shop.*;
import com.tqmall.legend.enums.service.ServiceCateTagEnum;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.object.param.service.*;
import com.tqmall.legend.object.result.goods.SearchPackageGoodsDTO;
import com.tqmall.legend.object.result.region.RegionDTO;
import com.tqmall.legend.object.result.service.*;
import com.tqmall.legend.object.result.shop.ShopDTO;
import com.tqmall.legend.object.result.shop.ShopPageDTO;
import com.tqmall.legend.service.service.RpcShopServiceInfoService;
import com.tqmall.tqmallstall.domain.result.CityListDTO;
import com.tqmall.tqmallstall.service.common.AppRegionService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zsy on 15/12/15.
 */
@Service("rpcShopServiceInfoService")
public class RpcShopServiceInfoServiceImpl implements RpcShopServiceInfoService {
    private static final Logger log = LoggerFactory.getLogger(RpcShopServiceInfoServiceImpl.class);
    @Autowired
    private ShopService shopService;//门店service
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;//门店参加的淘汽服务实例service
    @Autowired
    private ShopServiceInfoRelService shopServiceInfoRelService;//模板淘汽服务城市站关联service
    @Autowired
    private ServiceTemplateService serviceTemplateService;//模板淘汽服务service
    @Autowired
    private AppRegionService appRegionService;//iserver Dubbo接口
    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private ServiceGoodsSuiteService serviceGoodsSuiteService;
    @Autowired
    private ServiceTemplateCateRelService serviceTemplateCateRelService;
    @Autowired
    private RpcPopularSortService rpcPopularSortService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private IOrderService orderService;

    /**
     * 根据服务id获取城市站
     *
     * @param param 注：需要传来源和服务id
     * @return
     */
    @Override
    public Result<ShopServiceInfoDTO> getRegionById(ShopServiceInfoParam param) {
        Result returnResult;
        try {
            log.info("【dubbo】RpcShopServiceInfoServiceImpl.getRegionById：根据服务id获取城市站,传参对象{},来源{}", param,param.getSource());
            ShopServiceInfoDTO shopServiceInfoDTO = new ShopServiceInfoDTO();
            Result result = getShopServiceInfoDTOResult(param);
            if (!result.isSuccess()) {
                log.info("【dubbo】RpcShopServiceInfoServiceImpl.getRegionById：数据有误，返回值{}", LogUtils.objectToString(result));
                return result;
            }else{
                BeanUtils.copyProperties(shopServiceInfoDTO,result.getData());
            }
            Long id = param.getServiceId();
            //根据id获取淘汽服务开放的城市站
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("templateId", id);
            List<ShopServiceInfoRel> shopServiceInfoRelList = shopServiceInfoRelService.select(searchMap);
            if (CollectionUtils.isEmpty(shopServiceInfoRelList)) {
                return Result.wrapErrorResult(LegendErrorCode.DUBBO_SERVICE_SELECT_NULL_EX.getCode(), "服务没有对应的城市站");
            }
            Map<Long, ShopServiceInfoRel> cityMap = new HashMap<>();
            for (ShopServiceInfoRel shopServiceInfoRel : shopServiceInfoRelList) {
                Long cityId = shopServiceInfoRel.getCity();
                cityMap.put(cityId, shopServiceInfoRel);
            }
            //查询所有正式门店
            searchMap.clear();
            searchMap.put("shopStatus", 1);
            List<Shop> shopList = shopService.select(searchMap);
            //组装数据
            //1、此城市一共多少门店
            Map<Long, Shop> shopMap = new HashMap<>();
            Map<Long, Integer> shopNumMap = new HashMap<>();
            Integer shopNum;
            for (Shop shop : shopList) {
                Long cityId = shop.getCity();
                if (shopNumMap.containsKey(cityId)) {
                    shopNum = shopNumMap.get(cityId);
                    shopNum++;
                } else {
                    shopNum = 1;
                }
                shopNumMap.put(cityId, shopNum);
                Long shopId = shop.getId();
                shopMap.put(shopId, shop);
            }
            //根据id获取参加此模板服务的门店服务
            searchMap.clear();
            searchMap.put("parentId", id);
            searchMap.put("status", 0);
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.select(searchMap);
            //2、此城市一共多少门店参加此服务
            Map<Long, Integer> shopJoinNumMap = new HashMap<>();
            Integer shopJoinNum;
            for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                Long shopId = shopServiceInfo.getShopId();
                if (shopMap.containsKey(shopId)) {
                    Shop shop = shopMap.get(shopId);
                    Long cityId = shop.getCity();
                    if (shopJoinNumMap.containsKey(cityId)) {
                        shopJoinNum = shopJoinNumMap.get(cityId);
                        shopJoinNum++;
                    } else {
                        shopJoinNum = 1;
                    }
                    shopJoinNumMap.put(cityId, shopJoinNum);
                }
            }
            Map<String, RegionDTO> regionDTOMap = getProvinceAndCityDTOMap(shopNumMap, shopJoinNumMap);
            shopServiceInfoDTO.setRegionDTOMap(regionDTOMap);
            returnResult = Result.wrapSuccessfulResult(shopServiceInfoDTO);
            log.info("【dubbo】RpcShopServiceInfoServiceImpl.getRegionById，返回数据{}", LogUtils.objectToString(returnResult));
            return returnResult;
        } catch (Exception e) {
            log.error("【dubbo】RpcShopServiceInfoServiceImpl.getRegionById，出现异常{}", e);
        }
        returnResult = Result.wrapErrorResult("", "系统异常，请稍后再试");
        log.info("【dubbo】RpcShopServiceInfoServiceImpl.getRegionById，出现异常,返回{}", returnResult);
        return returnResult;
    }

    /**
     * 根据serviceId、cityId、status获取门店
     * status不设置默认为0
     *
     * @param param
     * @return
     */
    @Override
    public Result<ShopPageDTO> getShopPage(ShopServiceInfoParam param) {
        Result returnResult;
        try {
            log.info("【dubbo】RpcShopServiceInfoServiceImpl.getShopPage：获取淘汽服务门店详情,传参对象{},来源{}", param,param.getSource());
            //校验数据
            Result result = getShopServiceInfoDTOResult(param);
            if (!result.isSuccess()) {
                log.info("【dubbo】RpcShopServiceInfoServiceImpl.getShopPage：数据有误，返回值{}", LogUtils.objectToString(result));
                return result;
            }
            Long cityId = param.getCityId();
            if (cityId == null) {
                cityId = 383L;//默认为杭州
            }
            Integer status = param.getStatus();
            if (status == null) {
                status = 0;//默认为参加淘汽服务
            }
            Map<String, Object> searchMap = new HashMap<>();
            //根据服务id查询参加的门店
            Long serviceId = param.getServiceId();
            searchMap.put("parentId", serviceId);
            searchMap.put("status", 0);
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.select(searchMap);
            Map<Long, ShopServiceInfo> shopServiceInfoMap = new HashMap<>();
            for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                Long shopId = shopServiceInfo.getShopId();
                shopServiceInfoMap.put(shopId, shopServiceInfo);
            }
            //查询此城市的所有正式门店
            searchMap.clear();
            searchMap.put("shopStatus", 1);
            searchMap.put("city", cityId);
            List<Shop> shopList = shopService.select(searchMap);
            List<Shop> jionList = new ArrayList<Shop>();
            List<Shop> notJoinList = new ArrayList<Shop>();
            for (Shop shop : shopList) {
                Long id = shop.getId();
                if (shopServiceInfoMap.containsKey(id)) {
                    //参加门店的id
                    jionList.add(shop);
                } else {
                    notJoinList.add(shop);
                }
            }
            Integer shopJoinNum = jionList.size();
            Integer shopNotJoinNum = notJoinList.size();
            ShopPageDTO shopPageDTO = new ShopPageDTO();
            shopPageDTO.setShopJoinNum(shopJoinNum);//参加门店数
            shopPageDTO.setShopNotJoinNum(shopNotJoinNum);//未参加门店数
            //datalist default = jionlist
            List<Shop> dataList = jionList;
            if (status == 1 ){
                dataList = notJoinList;
            }
            //设置返回值
            if (!CollectionUtils.isEmpty(dataList)) {
                List<ShopDTO> shopDTOList = new ArrayList<>();
                for (Shop shop : dataList) {
                    ShopDTO shopDTO = new ShopDTO();
                    BeanUtils.copyProperties(shopDTO, shop);
                    shopDTO.setStatus(status);
                    shopDTOList.add(shopDTO);
                }
                shopPageDTO.setShopList(shopDTOList);
            }
            returnResult = Result.wrapSuccessfulResult(shopPageDTO);
            log.info("【dubbo】RpcShopServiceInfoServiceImpl.getShopPage，返回数据{}", LogUtils.objectToString(returnResult));
            return returnResult;
        } catch (Exception e) {
            log.error("【dubbo】RpcShopServiceInfoServiceImpl.getShopPage，出现异常,返回{}", e);
        }
        returnResult = Result.wrapErrorResult("", "系统异常，请稍后再试");
        log.info("【dubbo】RpcShopServiceInfoServiceImpl.getShopPage，出现异常,返回{}", returnResult);
        return returnResult;
    }

    /**
     * 校验数据，并给返回结果赋值
     *
     * @param param
     * @return
     */
    private Result getShopServiceInfoDTOResult(ShopServiceInfoParam param) {
        //判断来源是否为空
        String source = param.getSource();
        if (StringUtils.isBlank(source)) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SOURCE_NULL_EX.getCode(), "系统来源为空");
        }
        Long id = param.getServiceId();
        //根据id获取淘汽服务
        ServiceTemplate serviceTemplate = serviceTemplateService.selectById(id);
        if (serviceTemplate == null || !"TQFW".equals(serviceTemplate.getFlags())) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SERVICE_SELECT_NULL_EX.getCode(), "服务不存在");
        }
        Integer status = serviceTemplate.getStatus();
        if (status == null || status == -1) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SERVICE_SELECT_NULL_EX.getCode(), "服务已下架");
        }
        return Result.wrapSuccessfulResult(serviceTemplate);
    }

    /**
     * 获取省份
     *
     * @return
     */
    private Map<String, RegionDTO> getProvinceAndCityDTOMap(Map<Long, Integer> shopNumMap, Map<Long, Integer> shopJoinNumMap) {
        Map<String, RegionDTO> provinceTempMap = new HashMap<>();
        try {
            Result<List<com.tqmall.tqmallstall.domain.result.RegionDTO>> provinceResult = appRegionService.subRegion(1);
            Result<CityListDTO> cityResult = appRegionService.citySite();
            if (provinceResult.isSuccess() && cityResult.isSuccess()) {
                Map<Integer, List<RegionDTO>> cityTempMap = new HashMap<>();
                List<com.tqmall.tqmallstall.domain.result.RegionDTO> provinceRegionDTOList = provinceResult.getData();
                log.info("【dubbo】：获取省接口：appRegionService.subRegion(1),结果为：" + provinceRegionDTOList);
                CityListDTO cityRegionDTOList = cityResult.getData();
                log.info("【dubbo】：获取城市站接口：appRegionService.citySite(),结果为：" + cityRegionDTOList);
                for (com.tqmall.tqmallstall.domain.result.RegionDTO regionDTO : cityRegionDTOList.getCities()) {
                    RegionDTO regionDTOTemp = new RegionDTO();
                    BeanUtils.copyProperties(regionDTOTemp, regionDTO);
                    Integer parentId = regionDTOTemp.getParentId();
                    Long id = regionDTOTemp.getId().longValue();
                    if (shopNumMap.containsKey(id)) {
                        //此城市存放门店总数，判断是否有参加活动的门店
                        Integer shopNum = shopNumMap.get(id);
                        regionDTOTemp.setShopNum(shopNum);
                        if (shopJoinNumMap.containsKey(id)) {
                            //参加门店数
                            Integer shopJoinNum = shopJoinNumMap.get(id);
                            regionDTOTemp.setShopJoinNum(shopJoinNum);
                        }
                    }
                    if (cityTempMap.containsKey(parentId)) {
                        List<RegionDTO> tempList = cityTempMap.get(parentId);
                        tempList.add(regionDTOTemp);
                    } else {
                        List<RegionDTO> tempList = new ArrayList<>();
                        tempList.add(regionDTOTemp);
                        cityTempMap.put(regionDTOTemp.getParentId(), tempList);
                    }
                }
                //组装数据，并返回数据
                for (com.tqmall.tqmallstall.domain.result.RegionDTO regionDTO : provinceRegionDTOList) {
                    RegionDTO regionDTOTemp = new RegionDTO();
                    BeanUtils.copyProperties(regionDTOTemp, regionDTO);
                    Integer id = regionDTOTemp.getId();
                    if (cityTempMap.containsKey(id)) {
                        List<RegionDTO> cityTempList = cityTempMap.get(id);
                        Integer shopNum = 0;
                        Integer shopJoinNum = 0;
                        //计算省对应的门店总数和参加门店数
                        for (RegionDTO temp : cityTempList) {
                            shopNum += temp.getShopNum();
                            shopJoinNum += temp.getShopJoinNum();
                        }
                        regionDTOTemp.setShopNum(shopNum);
                        regionDTOTemp.setShopJoinNum(shopJoinNum);
                        regionDTOTemp.setCityList(cityTempList);
                        provinceTempMap.put(regionDTOTemp.getRegionName(), regionDTOTemp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("【dubbo】：获取城市站出现异常:" + e.getMessage());
        }
        return provinceTempMap;
    }

    @Override
    public Result<ShopServiceInfoPageDTO> getServiceInfoPage(ShopServiceInfoParam param) {
        if(param==null){
            log.error("[dubbo]查询门店服务实例列表失败,参数为空");
            return Result.wrapErrorResult("-1","参数为空");
        }
        //返回值初始化
        ShopServiceInfoPageDTO shopServiceInfoPageDTO = new ShopServiceInfoPageDTO();
        List<ShopServiceInfoDTO> content = new ArrayList<>();
        int offset = 0;
        int limit = 10;
        if(param.getOffset()!=null){
            offset = param.getOffset();
        }
        if(param.getLimit()!=null){
            limit = param.getLimit();
        }
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("offset",offset);
        searchMap.put("limit",limit);
        Long userGlobalId = param.getUserGlobalId();
        if(userGlobalId!=null){
            Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
            if (shop==null || shop.getId()==null){
                Result.wrapSuccessfulResult(shopServiceInfoPageDTO);
            }
            searchMap.put("shopId",shop.getId());
        }
        Long serviceId = param.getServiceId();
        if(serviceId!=null){
            searchMap.put("id",serviceId);
        }
        List<Long> serviceIds = param.getServiceIds();
        if(!CollectionUtils.isEmpty(serviceIds)){
            searchMap.put("ids",serviceIds);
        }
        List<String> sorts = param.getSorts();
        if(!CollectionUtils.isEmpty(sorts)){
            searchMap.put("sorts",sorts);
        }
        Integer total = shopServiceInfoService.selectCount(searchMap);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.select(searchMap);
        if(!CollectionUtils.isEmpty(shopServiceInfoList)){
            //设置服务套餐价格suitAmount
            shopServiceInfoService.setServiceSuitAmount(shopServiceInfoList);
            for(ShopServiceInfo shopServiceInfo:shopServiceInfoList){
                ShopServiceInfoDTO shopServiceInfoDTO = new ShopServiceInfoDTO();
                org.springframework.beans.BeanUtils.copyProperties(shopServiceInfo,shopServiceInfoDTO);
                //售价和市场价处理
                BigDecimal appServicePrice;
                if(shopServiceInfo.getSuiteNum()!=null && shopServiceInfo.getSuiteNum()>0){
                    appServicePrice = shopServiceInfo.getSuiteAmount() == null ? new BigDecimal("0.00") : shopServiceInfo.getSuiteAmount();
                } else {
                    appServicePrice = shopServiceInfo.getServicePrice() == null ? new BigDecimal("0.00") : shopServiceInfo.getServicePrice();
                }
                shopServiceInfoDTO.setServicePrice(appServicePrice);
                BigDecimal marketPrice = shopServiceInfo.getMarketPrice() == null ? new BigDecimal("0.00") : shopServiceInfo.getMarketPrice();
                shopServiceInfoDTO.setMarketPrice(appServicePrice.compareTo(marketPrice) > 0 ? appServicePrice : marketPrice);
                content.add(shopServiceInfoDTO);
            }
        }
        shopServiceInfoPageDTO.setContent(content);
        shopServiceInfoPageDTO.setTotal(total);
        return Result.wrapSuccessfulResult(shopServiceInfoPageDTO);
    }

    @Override
    public Result<ServiceTemplatePageDTO> getServiceTemplatePage(ServiceTemplateParam param) {
        if(param==null){
            log.error("[dubbo]查询服务模版列表失败,参数为空");
            return Result.wrapErrorResult("-1","参数为空");
        }
        ServiceTemplatePageDTO serviceTemplatePageDTO = new ServiceTemplatePageDTO();
        List<ServiceTemplateDTO> content = new ArrayList<>();
        int offset = 0;
        int limit = 10;
        if(param.getOffset()!=null){
            offset = param.getOffset();
        }
        if(param.getLimit()!=null){
            limit = param.getLimit();
        }
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("offset",offset);
        searchMap.put("limit",limit);
        if(StringUtils.isNotEmpty(param.getServiceName())) {
            searchMap.put("nameLike",param.getServiceName());
        }
        if(param.getStatus()!=null){
            searchMap.put("status",param.getStatus());
        }
        List<String> sorts = param.getSorts();
        if(!CollectionUtils.isEmpty(sorts)){
            searchMap.put("sorts",sorts);
        }
        if (!CollectionUtils.isEmpty(param.getServiceTplIds())) {
            searchMap.put("ids", param.getServiceTplIds());
        }
        Integer total = serviceTemplateService.selectCount(searchMap);
        List<ServiceTemplate> serviceTemplateList = serviceTemplateService.select(searchMap);
        if(!CollectionUtils.isEmpty(serviceTemplateList)){
            for(ServiceTemplate serviceTemplate:serviceTemplateList){
                ServiceTemplateDTO serviceTemplateDTO = new ServiceTemplateDTO();
                org.springframework.beans.BeanUtils.copyProperties(serviceTemplate, serviceTemplateDTO);
                content.add(serviceTemplateDTO);
            }
        }
        serviceTemplatePageDTO.setTotal(total);
        serviceTemplatePageDTO.setContent(content);
        return Result.wrapSuccessfulResult(serviceTemplatePageDTO);
    }

    /**
     * 初始化钣喷服务
     *
     * @param shopId 门店id
     * @param userId 后台用户id
     * @return
     */
    @Override
    public Result<Boolean> initBpService(Long shopId, Long userId) {
        Shop shop = shopService.selectById(shopId);
        if(shop == null){
            return Result.wrapErrorResult(LegendErrorCode.SHOP_ERROR.getCode(),"门店不存在");
        }
        try {
            log.info("【dubbo:初始化钣喷服务】门店id：{}，userId：{}", shopId, userId);
            shopServiceInfoFacade.initBpService(shopId,userId);
            //刷新缓存
            cacheComponent.reload(CacheKeyConstant.SERVICE_CATEGORY);
            return Result.wrapSuccessfulResult(true);
        } catch (Exception e) {
            log.error("【dubbo:初始化钣喷服务】门店id：{}，userId：{},出现异常",shopId,userId,e);
            return Result.wrapSuccessfulResult(false);
        }
    }

    @Override
    public Result<List<AppServiceGroupDTO>> getAppServiceGroup(Long userGlobalId) {
        Assert.notNull(userGlobalId, "门店编号不能为空");
        Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
        if (shop == null) {
            log.error("[dubbo:查询可展示给车主的服务]userGlobalId{}对应的门店不存在,查询失败", userGlobalId);
            return Result.wrapErrorResult("-1", "门店不存在");
        }
        Long shopId = shop.getId();
        //.获取车主服务一,二级服务类别
        Map<String, Object> qryCateParam = new HashMap<>();
        qryCateParam.put("cateType", 1);//类别类型，0门店服务，1淘汽、车主服务，2，标准服务类别
        qryCateParam.put("sorts", new String[]{"parent_id asc", "cate_sort desc"});
        List<ShopServiceCate> shopServiceCateList = shopServiceCateService.selectNoCache(qryCateParam);
        if (CollectionUtils.isEmpty(shopServiceCateList)) {
            return Result.wrapSuccessfulResult((List<AppServiceGroupDTO>) (new ArrayList<AppServiceGroupDTO>()));
        }
        List<ShopServiceCate> parentShopServiceCateList = new ArrayList<>();//一级车主服务类别
        List<ShopServiceCate> childShopServiceCateList = new ArrayList<>();//二级车主服务类别
        for (ShopServiceCate shopServiceCate : shopServiceCateList) {
            if (shopServiceCate.getParentId() == 0) {
                parentShopServiceCateList.add(shopServiceCate);
            } else {
                childShopServiceCateList.add(shopServiceCate);
            }
        }
        ImmutableMap<Long, ShopServiceCate> parentShopServiceCateMap = Maps.uniqueIndex(parentShopServiceCateList, new Function<ShopServiceCate, Long>() {
            @Override
            public Long apply(ShopServiceCate shopServiceCate) {
                return shopServiceCate.getId();
            }
        });
        ImmutableMap<Long, ShopServiceCate> childShopServiceCateMap = Maps.uniqueIndex(childShopServiceCateList, new Function<ShopServiceCate, Long>() {
            @Override
            public Long apply(ShopServiceCate shopServiceCate) {
                return shopServiceCate.getId();
            }
        });
        List<AppServiceGroupDTO> appServiceGroupDTOList = new ArrayList<>();
        AppServiceGroupDTO recommendServcieGroup = new AppServiceGroupDTO();//推荐的车主服务,就是所谓"推荐"那一类别的服务
        recommendServcieGroup.setCateId(0L);
        recommendServcieGroup.setCateName("推荐");
        recommendServcieGroup.setCateSort(Integer.MAX_VALUE);//由于排序是按cateSort降序的,此处设置Integer.MAX_VALUE表示置顶
        appServiceGroupDTOList.add(recommendServcieGroup);
        for (ShopServiceCate shopServiceCate : parentShopServiceCateList) {
            AppServiceGroupDTO appServiceGroupDTO = new AppServiceGroupDTO();
            appServiceGroupDTO.setCateId(shopServiceCate.getId());
            appServiceGroupDTO.setCateName(shopServiceCate.getName());
            org.springframework.beans.BeanUtils.copyProperties(shopServiceCate, appServiceGroupDTO);
            appServiceGroupDTOList.add(appServiceGroupDTO);
        }
        //.查询可以在车主端展示的服务
        Map<String, Object> qryServiceInfoParam = new HashMap<>();
        qryServiceInfoParam.put("shopId", shopId);
        qryServiceInfoParam.put("status", 0);//状态 0有效 -1 TQFW下架 -2 CZFW下架
        qryServiceInfoParam.put("appPublishStatus", 1);//查询需要在车主端展示的服务
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(qryServiceInfoParam);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return Result.wrapSuccessfulResult(appServiceGroupDTOList);
        }
        Set<Long> serviceTplIds = new HashSet<>();
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            if (ServiceFlagsEnum.TQFW.getFlags().equals(shopServiceInfo.getFlags())
                    || ServiceFlagsEnum.MDFW.getFlags().equals(shopServiceInfo.getFlags())) {
                serviceTplIds.add(shopServiceInfo.getParentId());
            }
        }
        //.套餐服务价格设置
        shopServiceInfoService.setServiceSuitAmount(shopServiceInfoList);
        //.服务模版类型关系查询
        Map<Long, Set<Long>> childCateIdsMap = new HashMap<>();//key:serviceTemplateId,value:childcateIds
        if (!CollectionUtils.isEmpty(serviceTplIds)) {
            Map<String, Object> qryCateRelParam = new HashMap<>();
            qryCateRelParam.put("templateIds", serviceTplIds);
            List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelService.select(qryCateRelParam);
            if (!CollectionUtils.isEmpty(serviceTemplateCateRelList)) {
                for (ServiceTemplateCateRel serviceTemplateCateRel : serviceTemplateCateRelList) {
                    Set<Long> childCateIds = childCateIdsMap.get(serviceTemplateCateRel.getTemplateId());
                    if (childCateIds == null) {
                        childCateIds = new HashSet<>();
                    }
                    childCateIds.add(serviceTemplateCateRel.getCateId());
                    childCateIdsMap.put(serviceTemplateCateRel.getTemplateId(), childCateIds);
                }
            }
        }
        //.设置默认图标,服务归类
        Map<Long, List<ShopServiceInfo>> shopServiceListMap = _reduceAppServices(parentShopServiceCateMap, childShopServiceCateMap, shopServiceInfoList, childCateIdsMap);
        //.内部排序,组装结果
        _sortAndWrapAppServcieGroup(appServiceGroupDTOList, shopServiceListMap);
        return Result.wrapSuccessfulResult(appServiceGroupDTOList);
    }

    @Override
    public Result<List<ServiceStatisDTO>> statisServiceUsage() {
        return new ApiTemplate<List<ServiceStatisDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<ServiceStatisDTO> process() throws BizException {
                List<ServiceStatisBo> serviceStatisBoList = shopServiceInfoFacade.getServiceStatisBase(null, null);
                List<ServiceStatisBo> serviceStatisBoList1 = shopServiceInfoFacade.attachPointUsages(serviceStatisBoList);
                List<ServiceStatisBo> serviceStatisBoList2 = shopServiceInfoFacade.attachOrderUsages(serviceStatisBoList1);
                List<ServiceStatisBo> serviceStatisBoList3 = shopServiceInfoFacade.attachOrderConfrimUsages(serviceStatisBoList2);
                Map<Long, ServiceStatisDTO> statisDTOMap = Maps.newHashMap();
                Map<Long, Set<Long>> catIdShopIdMap = Maps.newHashMap();
                for (ServiceStatisBo serviceStatisBo : serviceStatisBoList3) {
                    Long topCatId = serviceStatisBo.getTopCatId();
                    if (statisDTOMap.containsKey(topCatId)) {
                        ServiceStatisDTO statisDTO = statisDTOMap.get(topCatId);
                        statisDTO.addAppointUsages(serviceStatisBo.getUsageInPoints());
                        statisDTO.addPublishCount(1);
                        statisDTO.addOrderUsages(serviceStatisBo.getUsageInOrders());
                        statisDTO.addConfirmedOrderUsages(serviceStatisBo.getUsageInConfirmOrders());
                    } else {
                        ServiceStatisDTO statisDTO = new ServiceStatisDTO();
                        statisDTO.setTopCatId(topCatId);
                        statisDTO.setAppointUsages(serviceStatisBo.getUsageInPoints());
                        statisDTO.setPublishCount(1);
                        statisDTO.setOrderUsages(serviceStatisBo.getUsageInOrders());
                        statisDTO.setConfirmedOrderUsage(serviceStatisBo.getUsageInConfirmOrders());
                        statisDTOMap.put(topCatId,statisDTO);
                    }

                    //统计门店数
                    if (catIdShopIdMap.containsKey(topCatId)) {
                        if (catIdShopIdMap.get(topCatId) == null) {
                            catIdShopIdMap.put(topCatId, Sets.<Long>newHashSet());
                        }
                        Set<Long> shopIdSet = catIdShopIdMap.get(topCatId);
                        shopIdSet.add(serviceStatisBo.getShopId());
                    } else {
                        Set<Long> shopIdSet = Sets.newHashSet();
                        shopIdSet.add(serviceStatisBo.getShopId());
                        catIdShopIdMap.put(topCatId, shopIdSet);
                    }

                }

                //补全每类的发布门店数
                Set<Map.Entry<Long, ServiceStatisDTO>> entrySet = statisDTOMap.entrySet();
                for (Iterator<Map.Entry<Long, ServiceStatisDTO>> iterator = entrySet.iterator(); iterator.hasNext();) {
                    Map.Entry<Long, ServiceStatisDTO> next = iterator.next();
                    Set<Long> shopIdSet = catIdShopIdMap.get(next.getKey());
                    next.getValue().setPublisherCount(shopIdSet == null ? 0 : shopIdSet.size());
                }
                Collection<ServiceStatisDTO> resultData = statisDTOMap.values();

                // addCatName
                List<ServiceCateVo> serviceFirstCateVoList = shopServiceCateService.selectFirstCate();
                ImmutableMap<Long, ServiceCateVo> serviceFirstCateMap = Maps.uniqueIndex(serviceFirstCateVoList, new Function<ServiceCateVo, Long>() {
                    @Override
                    public Long apply(ServiceCateVo input) {
                        return input.getId();
                    }
                });
                for (ServiceStatisDTO item : resultData) {
                    Long topCatId = item.getTopCatId();
                    ServiceCateVo serviceCateVo = serviceFirstCateMap.get(topCatId);
                    String name = serviceCateVo == null ? null : serviceCateVo.getName();
                    item.setCatName(name);
                }
                List<ServiceStatisDTO> resultList = Lists.newLinkedList(resultData);

                return resultList;
            }
        }.execute();

    }

    @Override
    public Result<ServiceCatStatisDTO> statisServiceUsageByCat(final Long catId, final String shopName, final Integer pageIndex, final Integer pageSize) {

        return new ApiTemplate<ServiceCatStatisDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(catId, "catId can not be null");
                checkPageParam(pageIndex, pageSize);
            }

            @Override
            protected ServiceCatStatisDTO process() throws BizException {
                List<ServiceStatisBo> serviceStatisBoList = shopServiceInfoFacade.getServiceStatisBase(null, catId);
                Map<Long, Shop> shopMap = null;
                if (shopName != null) {
                    List<ServiceStatisBo> serviceStatisBoListTem = Lists.newArrayList();
                    List<Shop> shopList = shopService.matchName(shopName);

                    shopMap = Maps.uniqueIndex(shopList, new Function<Shop, Long>() {
                        @Override
                        public Long apply(Shop input) {
                            return input.getId();
                        }
                    });
                    for (ServiceStatisBo serviceStatisBo : serviceStatisBoList) {
                        if (shopMap.containsKey(serviceStatisBo.getShopId())) {
                            serviceStatisBoListTem.add(serviceStatisBo);
                        }
                    }
                    serviceStatisBoList = serviceStatisBoListTem;
                } else {
                    shopMap = shopService.getAllShopMap();
                }
                List<ServiceStatisBo> serviceStatisBoList1 = shopServiceInfoFacade.attachPointUsages(serviceStatisBoList);
                List<ServiceStatisBo> serviceStatisBoList2 = shopServiceInfoFacade.attachOrderUsages(serviceStatisBoList1);
                List<ServiceStatisBo> serviceStatisBoList3 = shopServiceInfoFacade.attachOrderConfrimUsages(serviceStatisBoList2);

                Map<Long, ServiceCatStatisVO> statisDTOMap = Maps.newHashMap();
                for (ServiceStatisBo serviceStatisBo : serviceStatisBoList3) {
                    Long shopId = serviceStatisBo.getShopId();
                    if (statisDTOMap.containsKey(shopId)) {
                        ServiceCatStatisVO serviceCatStatisVO = statisDTOMap.get(shopId);
                        serviceCatStatisVO.addPublishCount(1);
                        serviceCatStatisVO.addAppointUsages(serviceStatisBo.getUsageInPoints());
                        serviceCatStatisVO.addOrderUsages(serviceStatisBo.getUsageInOrders());
                        serviceCatStatisVO.addConfirmedOrderUsages(serviceStatisBo.getUsageInConfirmOrders());
                    } else {
                        Shop shop = shopMap.get(shopId);
                        ServiceCatStatisVO serviceCatStatisVO = new ServiceCatStatisVO();
                        serviceCatStatisVO.setShopId(shopId);
                        serviceCatStatisVO.setShopName(shop.getName());
                        serviceCatStatisVO.setPublishCount(1);
                        serviceCatStatisVO.setAppointUsages(serviceStatisBo.getUsageInPoints());
                        serviceCatStatisVO.setOrderUsages(serviceStatisBo.getUsageInOrders());
                        serviceCatStatisVO.setConfirmedOrderUsage(serviceStatisBo.getUsageInConfirmOrders());
                        statisDTOMap.put(shopId, serviceCatStatisVO);
                    }
                }

                ArrayList<ServiceCatStatisVO> resultList = Lists.newArrayList(statisDTOMap.values());
                for (ServiceCatStatisVO item : resultList) {
                    Long shopId = item.getShopId();
                    Shop shop = shopMap.get(shopId);
                    item.setShopId(shop == null ? 0 : Long.valueOf(shop.getUserGlobalId()));
                }

                //paging
                Collections.sort(resultList, new Comparator<ServiceCatStatisVO>() {
                    @Override
                    public int compare(ServiceCatStatisVO o1, ServiceCatStatisVO o2) {
                        return o1.getShopId().compareTo(o2.getShopId());
                    }
                });
                int size = resultList.size();
                int begin = (pageIndex-1) * pageSize;
                if (begin > size) {
                    throw new BizException("no data on page=" + pageIndex);
                }
                int end = begin + pageSize;
                end = end > size ? size : end;
                List<ServiceCatStatisVO> pageList = resultList.subList(begin, end);

                return new ServiceCatStatisDTO(pageList, size);
            }
        }.execute();
    }

    @Override
    public Result<ServiceShopStatisDTO> statisServiceUsageByShop(final Long ucShopId, final Long catId, final Integer pageIndex, final Integer pageSize) {
        return new ApiTemplate<ServiceShopStatisDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "ucShopId can not be null");
                Assert.notNull(catId, "catId can not be null");
                checkPageParam(pageIndex, pageSize);
            }

            @Override
            protected ServiceShopStatisDTO process() throws BizException {
                Shop shop = shopService.getShopByUserGlobalId(ucShopId);
                if (shop == null) {
                    throw  new BizException("the shop of ucShopId = " + ucShopId + "is null");
                }
                List<ServiceStatisBo> serviceStatisBase = shopServiceInfoFacade.getServiceStatisBase(shop.getId(), catId);
                List<ServiceStatisBo> serviceStatisBoList = shopServiceInfoFacade.attachPointUsages(serviceStatisBase);
                List<ServiceStatisBo> serviceStatisBoList1 = shopServiceInfoFacade.attachOrderUsages(serviceStatisBoList);
                List<ServiceStatisBo> serviceStatisBoList2 = shopServiceInfoFacade.attachOrderConfrimUsages(serviceStatisBoList1);

                List<ServiceShopStatisVO> statisShopVOList = Lists.newArrayList();
                for (ServiceStatisBo serviceStatisBo : serviceStatisBoList2) {
                    ServiceShopStatisVO serviceShopStatisVO = new ServiceShopStatisVO();
                    serviceShopStatisVO.setServiceId(serviceStatisBo.getServiceId());
                    serviceShopStatisVO.setServiceName(serviceStatisBo.getServiceName());
                    serviceShopStatisVO.setAppointUsages(serviceStatisBo.getUsageInPoints());
                    serviceShopStatisVO.setOrderUsages(serviceStatisBo.getUsageInOrders());
                    serviceShopStatisVO.setConfirmedOrderUsage(serviceStatisBo.getUsageInOrders());
                    statisShopVOList.add(serviceShopStatisVO);
                }

                //paging
                Collections.sort(statisShopVOList, new Comparator<ServiceShopStatisVO>() {
                    @Override
                    public int compare(ServiceShopStatisVO o1, ServiceShopStatisVO o2) {
                        return o1.getServiceId().compareTo(o2.getServiceId());
                    }
                });
                int size = statisShopVOList.size();
                int begin = (pageIndex-1) * pageSize;
                if (begin > size) {
                    throw new BizException("no data on page=" + pageIndex);
                }
                int end = begin + pageSize;
                end = end > size ? size : end;
                List<ServiceShopStatisVO> pageList = statisShopVOList.subList(begin, end);
                return new ServiceShopStatisDTO(size, pageList);
            }
        }.execute();
    }

    /**
     * 获取更多洗车服务
     *
     * @return
     */
    @Override
    public Result<List<ShopServiceInfoDTO>> gerMoreCarWashService(final Long shopId) {
        return new ApiTemplate<List<ShopServiceInfoDTO>>(){

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "shopId为空");
            }

            @Override
            protected List<ShopServiceInfoDTO> process() throws BizException {
                List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoFacade.getShopServiceInfoByCateTag(shopId, ServiceCateTagEnum.XC.getCode(), null);
                List<ShopServiceInfoDTO> shopServiceInfoDTOList = Lists.newArrayList();
                for(ShopServiceInfo shopServiceInfo : shopServiceInfoList){
                    ShopServiceInfoDTO shopServiceInfoDTO = new ShopServiceInfoDTO();
                    org.springframework.beans.BeanUtils.copyProperties(shopServiceInfo, shopServiceInfoDTO);
                    shopServiceInfoDTOList.add(shopServiceInfoDTO);
                }
                return shopServiceInfoDTOList;
            }
        }.execute();
    }

    /**
     * APP/PC自定义添加服务资料接口
     *
     * @param serviceInfoParam
     * @return
     */
    @Override
    public Result<ShopServiceInfoDTO> insertCustomShopServiceInfo(final ServiceInfoParam serviceInfoParam) {
        return new ApiTemplate<ShopServiceInfoDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                checkServiceParams(serviceInfoParam);
            }

            @Override
            protected ShopServiceInfoDTO process() throws BizException {
                ShopServiceInfo shopServiceInfo = new ShopServiceInfo();
                shopServiceInfo.setParentId(0L);
                shopServiceInfo.setSuiteNum(0L);
                shopServiceInfo.setSuiteGoodsNum(0L);
                shopServiceInfo.setTqmallServiceItemId(0L);
                shopServiceInfo.setType(1);
                org.springframework.beans.BeanUtils.copyProperties(serviceInfoParam, shopServiceInfo);
                com.tqmall.legend.common.Result<ShopServiceInfo> insertResult = shopServiceInfoService.saveShopServiceInfo(shopServiceInfo, serviceInfoParam.getShopId(), serviceInfoParam.getCreator());
                if(insertResult.isSuccess()){
                    ShopServiceInfoDTO shopServiceInfoDTO = new ShopServiceInfoDTO();
                    ShopServiceInfo insertServiceInfo = insertResult.getData();
                    org.springframework.beans.BeanUtils.copyProperties(insertServiceInfo,shopServiceInfoDTO);
                    shopServiceInfoDTO.setCategoryName(serviceInfoParam.getCategoryName());
                    shopServiceInfoDTO.setCateId(insertServiceInfo.getCategoryId());
                    return shopServiceInfoDTO;
                }
                throw new BizException("添加服务失败");
            }
        }.execute();
    }

    private void checkServiceParams(ServiceInfoParam serviceInfoParam) {
        Assert.notNull(serviceInfoParam, "服务信息不能为空");
        String name = serviceInfoParam.getName();
        String categoryName = serviceInfoParam.getCategoryName();
        String serviceSn = serviceInfoParam.getServiceSn();
        String carLevelName = serviceInfoParam.getCarLevelName();
        BigDecimal servicePrice = serviceInfoParam.getServicePrice();
        Long creator = serviceInfoParam.getCreator();
        Long shopId = serviceInfoParam.getShopId();
        Assert.notNull(name,"服务名称不能为空");
        Assert.notNull(categoryName,"服务类别不能为空");
        Assert.notNull(serviceSn,"服务编号不能为空");
        Assert.notNull(servicePrice,"服务价格不能为空");
        Assert.notNull(creator,"用户id不能为空");
        Assert.notNull(shopId,"门店id不能为空");
        boolean checkBool = shopServiceInfoFacade.checkWithShopId(shopId, "name", name);
        if(checkBool == false){
            throw new IllegalArgumentException("服务名称已存在");
        }
    }

    @Override
    public Result<Boolean> insertShopServiceInfo(final ServiceGoodsParam serviceGoodsParam) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                ServiceInfoParam serviceInfoParam = serviceGoodsParam.getServiceInfoParam();
                checkServiceParams(serviceInfoParam);
                List<ServiceGoodsSuiteParam> serviceGoodsSuiteParams = serviceGoodsParam.getGoodsSuiteParamList();
                if(!CollectionUtils.isEmpty(serviceGoodsSuiteParams)){
                    for(ServiceGoodsSuiteParam serviceGoodsSuiteParam : serviceGoodsSuiteParams){
                        Long id = serviceGoodsSuiteParam.getGoodsId();
                        Integer goodsNumber = serviceGoodsSuiteParam.getGoodsNumber();
                        Assert.notNull(id, "配件id不能为空");
                        Assert.notNull(goodsNumber, "配件数据不能为空");
                    }
                }
            }

            @Override
            protected Boolean process() throws BizException {
                List<ServiceGoodsSuiteParam> serviceGoodsSuiteParams = serviceGoodsParam.getGoodsSuiteParamList();
                Map<Long,Goods> goodsMap = Maps.newHashMap();
                Integer goodsNumber = 0;
                if(!CollectionUtils.isEmpty(serviceGoodsSuiteParams)){
                    List<Long> ids = Lists.newArrayList();
                    for(ServiceGoodsSuiteParam serviceGoodsSuiteParam : serviceGoodsSuiteParams){
                        Long id = serviceGoodsSuiteParam.getGoodsId();
                        goodsNumber += serviceGoodsSuiteParam.getGoodsNumber();
                        ids.add(id);
                    }
                    List<Goods> goodsList = goodsService.selectByIds(ids.toArray(new Long[ids.size()]));
                    if(CollectionUtils.isEmpty(goodsList)){
                        throw new BizException("添加的配件不存在");
                    }
                    for(Goods goods : goodsList){
                        Long id = goods.getId();
                        goodsMap.put(id,goods);
                    }
                    for(ServiceGoodsSuiteParam serviceGoodsSuiteParam : serviceGoodsSuiteParams){
                        Long id = serviceGoodsSuiteParam.getGoodsId();
                        if(!goodsMap.containsKey(id)){
                            Goods goods = goodsMap.get(id);
                            String goodsName = goods.getName();
                            StringBuffer error = new StringBuffer();
                            error.append("配件:");
                            error.append(goodsName);
                            error.append("不存在");
                            throw new BizException(error.toString());
                        }
                    }
                }
                ShopServiceInfo shopServiceInfo = new ShopServiceInfo();
                ServiceInfoParam serviceInfoParam = serviceGoodsParam.getServiceInfoParam();
                org.springframework.beans.BeanUtils.copyProperties(serviceInfoParam, shopServiceInfo);
                if(!CollectionUtils.isEmpty(serviceGoodsSuiteParams)){
                    shopServiceInfo.setSuiteNum(1l);
                    shopServiceInfo.setSuiteGoodsNum(goodsNumber.longValue());
                }
                UserInfo userInfo = new UserInfo();
                Long shopId = serviceInfoParam.getShopId();
                Long userId = serviceInfoParam.getCreator();
                userInfo.setShopId(shopId);
                userInfo.setUserId(userId);
                com.tqmall.legend.common.Result<ShopServiceInfo> result = shopServiceInfoService.add(shopServiceInfo, userInfo);
                if(!result.isSuccess()){
                    throw new BizException("服务添加失败："+result.getErrorMsg());
                }
                if(CollectionUtils.isEmpty(serviceGoodsSuiteParams)){
                    return true;
                }
                Long serviceId = result.getData().getId();
                ServiceGoodsSuite serviceGoodsSuite = new ServiceGoodsSuite();
                serviceGoodsSuite.setSuiteName(serviceInfoParam.getName());
                serviceGoodsSuite.setServiceId(serviceId);
                serviceGoodsSuite.setShopId(shopId);
                serviceGoodsSuite.setCreator(userId);
                serviceGoodsSuite.setGoodsNumber(goodsNumber.longValue());
                StringBuffer serviceInfo = new StringBuffer();
                serviceInfo.append("{");
                serviceInfo.append("\"servicePrice\"");
                serviceInfo.append(":");
                serviceInfo.append(serviceInfoParam.getServicePrice());
                serviceInfo.append("}");
                serviceGoodsSuite.setServiceInfo(serviceInfo.toString());
                BigDecimal suitePrice = BigDecimal.ZERO;
                List<SuiteGoods> suiteGoodsList = Lists.newArrayList();
                for(ServiceGoodsSuiteParam serviceGoodsSuiteParam : serviceGoodsSuiteParams){
                    Long id = serviceGoodsSuiteParam.getGoodsId();
                    Goods goods = goodsMap.get(id);
                    BigDecimal price = goods.getPrice();
                    Integer thisGoodsNumber = serviceGoodsSuiteParam.getGoodsNumber();
                    BigDecimal totalPrice = price.multiply(new BigDecimal(thisGoodsNumber));
                    suitePrice = suitePrice.add(totalPrice);
                    //与PC前端保持一致
                    SuiteGoods suiteGoods = new SuiteGoods();
                    suiteGoods.setId(goods.getId());
                    suiteGoods.setGoodsSn(goods.getGoodsSn());
                    suiteGoods.setName(goods.getName());
                    suiteGoods.setFormat(goods.getFormat());
                    suiteGoods.setPrice(price);
                    suiteGoods.setGoodsSoldPrice(price);
                    suiteGoods.setGoodsNum(thisGoodsNumber.longValue());
                    suiteGoods.setGoodsAmount(totalPrice);
                    suiteGoods.setMeasureUnit(goods.getMeasureUnit());
                    suiteGoodsList.add(suiteGoods);
                }
                suitePrice = suitePrice.add(serviceInfoParam.getServicePrice());
                serviceGoodsSuite.setSuitePrice(suitePrice);
                serviceGoodsSuite.setGoodsInfo(JSONUtil.object2Json(suiteGoodsList));
                serviceGoodsSuiteService.add(serviceGoodsSuite);
                return true;
            }
        }.execute();
    }

    @Override
    public Result<List<ShopServiceInfoDTO>> getHotService(final Long shopId) {
        return new ApiTemplate<List<ShopServiceInfoDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected List<ShopServiceInfoDTO> process() throws BizException {
                List<ShopServiceInfoDTO> hotServiceList = Lists.newArrayList();
                //调用cube获取热门服务数据（最多20项）
                Result<List<PopularDataDTO>> orderServiceListResult = null;
                try {
                    orderServiceListResult = rpcPopularSortService.getOrderServiceList(shopId);
                } catch (Exception e) {
                    log.error("调用cube获取常用服务接口异常",e);
                    return hotServiceList;
                }
                if(!orderServiceListResult.isSuccess() || CollectionUtils.isEmpty(orderServiceListResult.getData())){
                    return hotServiceList;
                }
                List<PopularDataDTO> popularOrderServiceDTOList = orderServiceListResult.getData();
                List<Long> serviceIdsList = Lists.newArrayList();
                List<Long> categoryIdsList = Lists.newArrayList();
                for(PopularDataDTO popularDataDTO : popularOrderServiceDTOList){
                    Long id = popularDataDTO.getId();
                    serviceIdsList.add(id);
                }
                List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoFacade.selectServiceByIds(serviceIdsList);
                if(CollectionUtils.isEmpty(shopServiceInfoList)){
                    return hotServiceList;
                }
                Map<Long, ShopServiceInfo> shopServiceInfoMap = Maps.newHashMap();
                for(ShopServiceInfo shopServiceInfo : shopServiceInfoList){
                    Long categoryId = shopServiceInfo.getCategoryId();
                    categoryIdsList.add(categoryId);
                    Long id = shopServiceInfo.getId();
                    shopServiceInfoMap.put(id, shopServiceInfo);
                }
                List<ShopServiceCate> shopServiceCateList = shopServiceCateService.selectByIds(categoryIdsList);
                Map<Long,String> cateNameMap = Maps.newHashMap();
                for(ShopServiceCate shopServiceCate : shopServiceCateList){
                    cateNameMap.put(shopServiceCate.getId(),shopServiceCate.getName());
                }
                for(PopularDataDTO popularDataDTO : popularOrderServiceDTOList){
                    Long id = popularDataDTO.getId();
                    if(!shopServiceInfoMap.containsKey(id)){
                        continue;
                    }
                    ShopServiceInfo shopServiceInfo = shopServiceInfoMap.get(id);
                    ShopServiceInfoDTO shopServiceInfoDTO = new ShopServiceInfoDTO();
                    shopServiceInfoDTO.setId(shopServiceInfo.getId());
                    shopServiceInfoDTO.setName(shopServiceInfo.getName());
                    shopServiceInfoDTO.setServicePrice(shopServiceInfo.getServicePrice());
                    shopServiceInfoDTO.setServiceNote(shopServiceInfo.getServiceNote());
                    shopServiceInfoDTO.setType(shopServiceInfo.getType());
                    shopServiceInfoDTO.setSuiteNum(shopServiceInfo.getSuiteNum());
                    shopServiceInfoDTO.setServiceUnit(shopServiceInfo.getServiceUnit());
                    Long categoryId = shopServiceInfo.getCategoryId();
                    shopServiceInfoDTO.setCategoryId(shopServiceInfo.getCategoryId());
                    if(cateNameMap.containsKey(categoryId)){
                        shopServiceInfoDTO.setCategoryName(cateNameMap.get(categoryId));
                    }
                    shopServiceInfoDTO.setServiceSn(shopServiceInfo.getServiceSn());
                    hotServiceList.add(shopServiceInfoDTO);
                }
                return hotServiceList;
            }
        }.execute();
    }

    @Override
    public Result<ShopWashCarServiceDTO> getBZCarWashService(final Long shopId) {
        return new ApiTemplate<ShopWashCarServiceDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId,"门店id不能为空");
            }

            @Override
            protected ShopWashCarServiceDTO process() throws BizException {
                List<ShopServiceInfo> shopServiceInfos = shopServiceInfoService.getBZCarWashList(shopId);
                OrderServicesVo orderServices = orderService.getLastCarwash(shopId);
                List<ShopServiceInfoDTO> shopServiceInfoDTOS = BdUtil.bo2do4List(shopServiceInfos, ShopServiceInfoDTO.class);
                ShopWashCarServiceDTO washCarServiceDTO = new ShopWashCarServiceDTO();
                if (orderServices == null) {
                    washCarServiceDTO.setAddPrice(true);
                }
                washCarServiceDTO.setShopServiceInfoDTOS(shopServiceInfoDTOS);
                return washCarServiceDTO;
            }
        }.execute();
    }

    @Override
    public Result<List<ShopServiceInfoExtDTO>> getShopServiceFromSearch(final ServiceSearchParam serviceSearchParam) {
        return new ApiTemplate<List<ShopServiceInfoExtDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(serviceSearchParam, "搜索实体不能为空");
                Assert.notNull(serviceSearchParam.getShopId(), "门店ID不能为空");
            }

            @Override
            protected List<ShopServiceInfoExtDTO> process() throws BizException {
                com.tqmall.legend.common.Result result = shopServiceInfoService.search(serviceSearchParam.getShopId(), serviceSearchParam.getServiceSn()
                        , serviceSearchParam.getServiceName(), serviceSearchParam.getType(), serviceSearchParam.getSuiteNumLT()
                        , serviceSearchParam.getSize());
                if (!result.isSuccess()) {
                    throw new BizException(result.getErrorMsg());
                }
                Object object = result.getData();
                //List<ShopServiceInfoDTO> shopServiceInfoDTOS = BdUtil.bo2do4List((Collection<Object>) obj, ShopServiceInfoDTO.class);
                List<ShopServiceInfoExtDTO> shopServiceInfoDTOS = new ArrayList<>();
                Gson gson = new Gson();
                for (Object obj : (Collection<Object>) object) {
                    String objStr = gson.toJson(obj);
                    ShopServiceInfoExtDTO shopServiceInfoExtDTO = gson.fromJson(objStr, ShopServiceInfoExtDTO.class);
                    shopServiceInfoDTOS.add(shopServiceInfoExtDTO);
                }
                return shopServiceInfoDTOS;
            }
        }.execute();
    }

    @Override
    public Result<ServicePackageDTO> getPackageByServiceId(final Long shopId, final String serviceParam) {
        return new ApiTemplate<ServicePackageDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店ID不能为空");
                Assert.hasText(serviceParam,"选择服务不能为空");

            }

            @Override
            protected ServicePackageDTO process() throws BizException {
                com.tqmall.legend.common.Result result = shopServiceInfoService.getPackageByServiceId(shopId, serviceParam);
                if (!result.isSuccess()) {
                    throw new BizException(result.getErrorMsg());
                }
                PackageVo packageVo = (PackageVo) result.getData();
                ShopServiceInfo shopServiceInfo = packageVo.getShopServiceInfo();
                List<ShopServiceInfo> shopServiceInfos = packageVo.getShopServiceInfoList();
                List<Goods> goodss = packageVo.getGoodsList();
                ShopServiceInfoDTO shopServiceInfoDTO = BdUtil.bo2do(shopServiceInfo, ShopServiceInfoDTO.class);
                List<ShopServiceInfoExtDTO> shopServiceInfoExtDTOS = BdUtil.bo2do4List(shopServiceInfos, ShopServiceInfoExtDTO.class);
                List<SearchPackageGoodsDTO> searchPackageGoodsDTOS = BdUtil.bo2do4List(goodss, SearchPackageGoodsDTO.class);
                ServicePackageDTO servicePackageDTO = new ServicePackageDTO();
                servicePackageDTO.setShopServiceInfoDTO(shopServiceInfoDTO);
                servicePackageDTO.setShopServiceInfoExtDTOS(shopServiceInfoExtDTOS);
                servicePackageDTO.setSearchPackageGoodsDTOS(searchPackageGoodsDTOS);
                return servicePackageDTO;
            }
        }.execute();
    }

    private void checkPageParam(Integer pageIndex, Integer pageSize) {
        Assert.notNull(pageIndex, "pageIndex can't be null");
        Assert.isTrue(pageIndex > 0, "pageIndex must greater than 0");
        Assert.notNull(pageSize, "pageSize can't be null");
        Assert.isTrue(pageSize != 0, "pageSize can't equals to 0");
    }

    /**
     * 服务归类
     * @param parentShopServiceCateMap
     * @param childShopServiceCateMap
     * @param shopServiceInfoList
     * @param childCateIdsMap
     * @return
     */
    private Map<Long, List<ShopServiceInfo>> _reduceAppServices(Map<Long, ShopServiceCate> parentShopServiceCateMap, Map<Long, ShopServiceCate> childShopServiceCateMap, List<ShopServiceInfo> shopServiceInfoList, Map<Long, Set<Long>> childCateIdsMap) {
        Map<Long, List<ShopServiceInfo>> shopServiceListMap = new HashMap<>();//key:parentShopServiceCateId
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            if (ServiceFlagsEnum.TQFW.getFlags().equals(shopServiceInfo.getFlags())
                    || ServiceFlagsEnum.MDFW.getFlags().equals(shopServiceInfo.getFlags())) {
                Set<Long> childCateIds = childCateIdsMap.get(shopServiceInfo.getParentId());
                if (!CollectionUtils.isEmpty(childCateIds)) {
                    for (Long childCateId : childCateIds) {
                        ShopServiceCate childShopServiceCate = childShopServiceCateMap.get(childCateId);
                        if (childShopServiceCate != null) {
                            Long parentCateId = childShopServiceCate.getParentId();
                            ShopServiceCate parentShopServiceCate = parentShopServiceCateMap.get(parentCateId);
                            if (parentShopServiceCate != null) {
                                if (StringUtils.isBlank(shopServiceInfo.getImgUrl())) {
                                    shopServiceInfo.setImgUrl(parentShopServiceCate.getDefaultImgUrl());
                                }
                                List<ShopServiceInfo> shopServiceInfoList2 = shopServiceListMap.get(parentCateId);
                                if (shopServiceInfoList2 == null) {
                                    shopServiceInfoList2 = new ArrayList<>();
                                }
                                shopServiceInfoList2.add(shopServiceInfo);
                                shopServiceListMap.put(parentCateId, shopServiceInfoList2);
                            }
                        }
                    }
                }
            } else if (ServiceFlagsEnum.CZFW.getFlags().equals(shopServiceInfo.getFlags())) {
                Long childCateId = shopServiceInfo.getAppCateId() == null ? null : shopServiceInfo.getAppCateId().longValue();
                ShopServiceCate childShopServiceCate = childShopServiceCateMap.get(childCateId);
                if (childShopServiceCate != null) {
                    Long parentCateId = childShopServiceCate.getParentId();
                    ShopServiceCate parentShopServiceCate = parentShopServiceCateMap.get(parentCateId);
                    if (parentShopServiceCate != null) {
                        if (StringUtils.isBlank(shopServiceInfo.getImgUrl())) {
                            shopServiceInfo.setImgUrl(parentShopServiceCate.getDefaultImgUrl());
                        }
                        List<ShopServiceInfo> shopServiceInfoList2 = shopServiceListMap.get(parentCateId);
                        if (shopServiceInfoList2 == null) {
                            shopServiceInfoList2 = new ArrayList<>();
                        }
                        shopServiceInfoList2.add(shopServiceInfo);
                        shopServiceListMap.put(parentCateId, shopServiceInfoList2);
                    }
                }
            }
            if (shopServiceInfo.getIsRecommend() != null && shopServiceInfo.getIsRecommend() == 1) {
                List<ShopServiceInfo> shopServiceInfoList2 = shopServiceListMap.get(0L);//0L表示推荐的服务即"推荐"这一类服务
                if (shopServiceInfoList2 == null) {
                    shopServiceInfoList2 = new ArrayList<>();
                }
                shopServiceInfoList2.add(shopServiceInfo);
                shopServiceListMap.put(0L, shopServiceInfoList2);
            }
        }
        return shopServiceListMap;
    }

    /**
     * 内部排序,组装结果
     *
     * @param appServiceGroupDTOList
     * @param shopServiceListMap
     */
    private void _sortAndWrapAppServcieGroup(List<AppServiceGroupDTO> appServiceGroupDTOList, Map<Long, List<ShopServiceInfo>> shopServiceListMap) {
        for (AppServiceGroupDTO appServiceGroupDTO : appServiceGroupDTOList) {
            List<ShopServiceInfo> serviceInfoList = shopServiceListMap.get(appServiceGroupDTO.getCateId());
            if (!CollectionUtils.isEmpty(serviceInfoList)) {
                //根据sort升序排序
                Collections.sort(serviceInfoList, new Comparator<ShopServiceInfo>() {
                    @Override
                    public int compare(ShopServiceInfo o1, ShopServiceInfo o2) {
                        long i1 = o1.getSort() == null ? Long.MAX_VALUE : o1.getSort();
                        long i2 = o2.getSort() == null ? Long.MAX_VALUE : o2.getSort();
                        if (i1 > i2) {
                            return 1;
                        } else if (i1 == i2) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                });
                //根据isRecommend降序排序
                Collections.sort(serviceInfoList, new Comparator<ShopServiceInfo>() {
                    @Override
                    public int compare(ShopServiceInfo o1, ShopServiceInfo o2) {
                        int i1 = o1.getIsRecommend() == null ? 0 : o1.getIsRecommend();
                        int i2 = o2.getIsRecommend() == null ? 0 : o2.getIsRecommend();
                        return i2 - i1;
                    }
                });
                //"推荐"这一类需要根据修改时间降序排序
                if (appServiceGroupDTO.getCateId() == 0) {
                    //根据isRecommend降序排序
                    Collections.sort(serviceInfoList, new Comparator<ShopServiceInfo>() {
                        @Override
                        public int compare(ShopServiceInfo o1, ShopServiceInfo o2) {
                            long i1 = o1.getGmtModified() == null ? 0l : o1.getGmtModified().getTime();
                            long i2 = o2.getGmtModified() == null ? 0l : o2.getGmtModified().getTime();
                            if (i1 > i2) {
                                return -1;
                            } else if (i1 == i2) {
                                return 0;
                            } else {
                                return 1;
                            }
                        }
                    });
                }
                List<AppServiceInfoDTO> serviceInfoDTOs = new ArrayList<>();
                for (ShopServiceInfo shopServiceInfo : serviceInfoList) {
                    AppServiceInfoDTO appServiceInfoDTO = new AppServiceInfoDTO();
                    org.springframework.beans.BeanUtils.copyProperties(shopServiceInfo, appServiceInfoDTO);
                    appServiceInfoDTO.setServiceId(shopServiceInfo.getId());
                    appServiceInfoDTO.setServiceName(shopServiceInfo.getName());
                    appServiceInfoDTO.setUrl(shopServiceInfo.getImgUrl());
                    appServiceInfoDTO.setPriceType(shopServiceInfo.getPriceType() == null ? 1 : shopServiceInfo.getPriceType().intValue());
                    BigDecimal appServicePrice;//车主端使用的服务售价,普通服务即为门店服务实例售价,套餐则为套餐价格
                    if(shopServiceInfo.getSuiteNum()!=null && shopServiceInfo.getSuiteNum()>0){
                        appServicePrice = shopServiceInfo.getSuiteAmount() == null ? new BigDecimal("0.00") : shopServiceInfo.getSuiteAmount();
                    } else {
                        appServicePrice = shopServiceInfo.getServicePrice() == null ? new BigDecimal("0.00") : shopServiceInfo.getServicePrice();
                    }
                    appServiceInfoDTO.setServicePrice(appServicePrice);
                    BigDecimal marketPrice = shopServiceInfo.getMarketPrice() == null ? new BigDecimal("0.00") : shopServiceInfo.getMarketPrice();
                    appServiceInfoDTO.setMarketPrice(appServicePrice.compareTo(marketPrice) > 0 ? appServicePrice : marketPrice);
                    serviceInfoDTOs.add(appServiceInfoDTO);
                }
                appServiceGroupDTO.setServiceInfoDTOs(serviceInfoDTOs);
            }
        }
    }
}
