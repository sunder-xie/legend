package com.tqmall.yunxiu.web.pub;

import com.tqmall.legend.biz.shop.*;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.pub.service.ServiceCateVo;
import com.tqmall.legend.entity.pub.service.ServiceDetailVo;
import com.tqmall.legend.entity.pub.service.ServiceInfoVo;
import com.tqmall.legend.entity.pub.service.ServiceTemplateInfoVo;
import com.tqmall.legend.entity.shop.*;
import com.tqmall.legend.web.common.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/7/16.
 */
@Controller
@RequestMapping("pub/shop/service")
public class ShopServiceListController extends BaseController {

    @Autowired
    ShopServiceInfoService shopServiceInfoService;

    @Autowired
    ServiceGoodsSuiteService serviceGoodsSuiteService;

    @Autowired
    ShopServiceCateService shopServiceCateService;

    @Autowired
    ShopService shopService;

    @Autowired
    ServiceTemplateCateRelService serviceTemplateCateRelService;
    @Autowired
    ShopServiceTagRelService shopServiceTagRelService;

    @Autowired
    ServiceTemplateService serviceTemplateService;


    Logger logger = LoggerFactory.getLogger(ShopServiceListController.class);


    /**
     * 2C-APP接口
     * 通过name获得车主服务一级类别信息
     * create by jason 2015-07-16
     */
    @RequestMapping(value = "category", method = RequestMethod.GET)
    @ResponseBody
    public Result getServiceCateList() {

        List<ServiceCateVo> serviceCateVoList  = shopServiceCateService.selectFirstCate();
        return Result.wrapSuccessfulResult(serviceCateVoList);
    }

    /**
     * 2C-APP接口
     * 获得车主服务套餐信息1.0版本
     * create by jason 2015-07-16
     * @param idList 是userGlobalId 的集合
     * @param typeList 服务类型
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Result getServiceSuiteList(@RequestParam(value = "idList[]", required = false) List<String> idList,
                                      @RequestParam(value = "type[]",required = false) List<String> typeList) {
        logger.info("获得车主服务套餐信息老接口:userGlobalId List参数:{},type参数:{}", idList, typeList);
        Result result = getServiceInfo(idList, typeList, 1);
        if (!result.isSuccess()) {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        } else {
            return Result.wrapSuccessfulResult(result.getData());
        }
    }

    /**
     * 2C-APP接口
     * 获得车主服务套餐信息新接口1.2版本
     * create by jason 2015-07-16
     * @param idList 是userGlobalId 的集合
     * @param typeList 服务类型
     */
    @RequestMapping(value = "list/new", method = RequestMethod.GET)
    @ResponseBody
    public Result getServiceSuiteListNew(@RequestParam(value = "idList[]", required = false) List<String> idList,
                                         @RequestParam(value = "type[]",required = false) List<String> typeList) {
        logger.info("获得车主服务套餐信息新接口:userGlobalId集合参数:{},type参数:{}", idList, typeList);
        Result result = getServiceInfo(idList, typeList, 2);
        if (!result.isSuccess()) {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        } else {
            return Result.wrapSuccessfulResult(result.getData());
        }
    }

    /**
     * create by jason 2015-08-25
     * app端商家详情接口
     *
     * @param serviceId 服务ID
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @ResponseBody
    public Result getServiceDetail(@RequestParam(value = "serviceId", required = true)Long serviceId ) {
        logger.info("进入服务详情方法!服务ID:{}", serviceId);
        ShopServiceInfo serviceInfo;
        try {
            ServiceDetailVo serviceDetailVo = new ServiceDetailVo();
            List<Long> list = new ArrayList<>();
            list.add(serviceId);
            List<ShopServiceInfo> serviceInfoList = shopServiceInfoService.selectAllByIds(list);
            if (!CollectionUtils.isEmpty(serviceInfoList)) {
                serviceInfo = serviceInfoList.get(0);
                if (null != serviceInfo) {

                    serviceDetailVo.setServiceId(serviceId);//服务ID
                    String flags = serviceInfo.getFlags();
                    Long status = serviceInfo.getStatus();
                    serviceDetailVo.setFlags(flags);
                    //status 0 正常 -1 TQFW下架 -2CZFW下架 云修系统
                    //车主端status = -1 表示下架不区分TQFW还是CZFW
                    if ("CZFW".equals(flags) && status == -2l) {
                        serviceDetailVo.setStatus(-1l);
                    } else {
                        serviceDetailVo.setStatus(status);
                    }
                    if (StringUtils.isBlank(serviceInfo.getImgUrl())) {
                        Integer appCateId = serviceInfo.getAppCateId();
                        if (appCateId != null) {
                            ShopServiceCate shopServiceCate = shopServiceCateService.selectById(appCateId.longValue());
                            if (shopServiceCate != null) {
                                serviceDetailVo.setImgUrl(shopServiceCate.getDefaultImgUrl());
                            }
                        }
                    } else {
                        serviceDetailVo.setImgUrl(serviceInfo.getImgUrl());
                    }
                    serviceDetailVo.setServiceName(serviceInfo.getName());//服务名称
                    serviceDetailVo.setServiceInfo(serviceInfo.getServiceInfo());//服务详情信息json
                    serviceDetailVo.setServiceNote(serviceInfo.getServiceNote());//服务描述
                    serviceDetailVo.setPriceType(serviceInfo.getPriceType());//价格类别 1 正常价格数值显示 2 到店洽谈 3 免费
                    serviceDetailVo.setThirdServiceInfo(serviceInfo.getThirdServiceInfo());//第三方（如橙牛）模板服务图片详细信息，只存图片url的数组json字符串
                    //服务价格
                    Long suiteNum = serviceInfo.getSuiteNum();
                    if (0l == suiteNum) {//服务不带配件
                        serviceDetailVo.setServicePrice(serviceInfo.getServicePrice());
                    } else {
                        ServiceGoodsSuite serviceGoodsSuite = serviceGoodsSuiteService.selectByServiceId(serviceId);
                        if (null != serviceGoodsSuite) {
                            serviceDetailVo.setServicePrice(serviceGoodsSuite.getSuitePrice());//服务和套餐价格
                        }
                    }
                    //市场价处理(不可第一服务价格)
                    BigDecimal servicePrice = serviceDetailVo.getServicePrice() == null ? new BigDecimal("0.00") : serviceDetailVo.getServicePrice();
                    BigDecimal marketPrice = serviceInfo.getMarketPrice() == null ? new BigDecimal("0.00") : serviceInfo.getMarketPrice();
                    serviceDetailVo.setMarketPrice(servicePrice.compareTo(marketPrice) > 0 ? servicePrice : marketPrice);

                    //组装标识 0品质标,1营销标
                    Map<Integer,List<ShopServiceTag>> tagMap = getServiceFlag(serviceInfo);
                    if (!CollectionUtils.isEmpty(tagMap)) {
                        serviceDetailVo.setQualityFlagList(tagMap.get(0));
                        serviceDetailVo.setMarketingFlagList(tagMap.get(1));
                    }
                    //userGlobalId
                    Long shopId = serviceInfo.getShopId();
                    Shop shop = shopService.selectById(shopId);
                    if (null != shop) {
                        serviceDetailVo.setUserGlobalId(shop.getUserGlobalId());
                    }
                } else {
                    logger.info("根据服务ID获得服务信息为空!服务ID:{}", serviceId);
                }
            }
            return Result.wrapSuccessfulResult(serviceDetailVo);
        } catch (Exception e) {
            logger.error("根据服务ID获得服务信息异常!服务ID:" + serviceId + " 出错信息:" + e);
            return Result.wrapErrorResult("-1", "根据服务ID获得服务信息异常");
        }

    }

    /**
     * 柯昌强 2015-11-28
     * 根据模板id取得服务模板（橙牛过来的订单）
     */
    @RequestMapping(value = "template_detail", method = RequestMethod.GET)
    @ResponseBody
    public Result getTemplateDetail(@RequestParam(value = "templateId", required = true)Long templateId ) {
        logger.info("进入服务详情方法!服务模板ID:{}", templateId);
        ServiceTemplate serviceTemplate = serviceTemplateService.selectById(templateId);//服务模板
        if (serviceTemplate == null) {
            logger.info("templateId没有对应的服务模板，服务模板id:{}", templateId);
            return Result.wrapErrorResult("", "templateId没有对应的服务模板");
        }
        //判断服务模板是否下架
        if (serviceTemplate.getStatus() == -1) {
            logger.info("templateId对应的服务模板已下架，服务模板id:{}", templateId);
            return Result.wrapErrorResult("", "templateId对应的服务模板已下架");
        }
        //判断是否为淘汽服务
        if (!serviceTemplate.getFlags().equals("TQFW")) {
            logger.info("templateId对应的服务模板不是淘汽服务，服务模板id:{}", templateId);
            return Result.wrapErrorResult("", "templateId对应的服务模板不是淘汽服务");
        }
        return Result.wrapSuccessfulResult(serviceTemplate);
    }

    /**
     * 组装服务,套餐价格
     * create by jason 2015-07-17
     *
     */
    private List<ShopServiceInfo> getSuitePrice(List<Long> suiteServiceIdsList, List<ShopServiceInfo> serviceList) {
        //获得服务和套餐价格
        if (!CollectionUtils.isEmpty(suiteServiceIdsList)) {
            Long[] serviceIds = suiteServiceIdsList
                    .toArray(new Long[suiteServiceIdsList.size()]);
            // 获取有套餐的服务数据相对应的套餐数据内容
            List<ServiceGoodsSuite> serviceGoodsSuiteList = serviceGoodsSuiteService.selectByServiceIds(serviceIds);
            if (!CollectionUtils.isEmpty(serviceGoodsSuiteList)) {
                // 套餐数据中的套餐金额存入map中
                HashMap<Long, BigDecimal> serviceGoodsSuiteMap = new HashMap<>();
                for (ServiceGoodsSuite serviceGoodsSuite : serviceGoodsSuiteList) {
                    serviceGoodsSuiteMap.put(serviceGoodsSuite.getServiceId(),
                            serviceGoodsSuite.getSuitePrice());
                }
                // 把套餐中的套餐金额，放入服务数据中
                for (ShopServiceInfo shopServiceInfo : serviceList) {
                    shopServiceInfo.setSuiteAmount(serviceGoodsSuiteMap.get(shopServiceInfo
                            .getId()));
                }
            }
        }
        return serviceList;
    }


    /**
     * 淘汽服务对应多个二级类目,取第一个类别
     *
     */
    private List<ShopServiceInfo> getServiceTQFWList(List<ShopServiceInfo> serviceTQFWList) {
        //淘汽服务对应多个二级类目ID
        if (!CollectionUtils.isEmpty(serviceTQFWList)) {
            for (ShopServiceInfo s: serviceTQFWList) {
                //淘汽服务对应的父ID
                Long parentId = s.getParentId();
                if (null != parentId && parentId > 0l) {
                    Map map = new HashMap();
                    map.put("templateId", parentId);
                    //取服务和类目对应关系表中获得数据 随机取一个类目ID
                    List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelService.select(map);

                    if (!CollectionUtils.isEmpty(serviceTemplateCateRelList)) {
                        //随机取一个类目ID
                        s.setAppCateId(serviceTemplateCateRelList.get(0).getCateId().intValue());
                    }
                }
            }
        }
        return serviceTQFWList;
    }

    /**
     * 淘汽服务对应多个二级类目,就需要多条淘汽服务数据1.2版本
     *
     */
    private List<ShopServiceInfo> getServiceTQFWListNew(List<ShopServiceInfo> serviceTQFWList) {

        //淘汽服务对应多个二级类目ID
        List<ShopServiceInfo> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(serviceTQFWList)) {

            for (ShopServiceInfo s: serviceTQFWList) {
                //淘汽服务对应的父ID
                Long parentId = s.getParentId();
                if (null != parentId && parentId > 0l) {
                    Map map = new HashMap();
                    map.put("templateId",parentId);
                    //去服务和类目对应关系表中获得数据 一个淘汽服务可能对应多个类目ID
                    List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelService.select(map);

                    if (!CollectionUtils.isEmpty(serviceTemplateCateRelList)) {

                        for (ServiceTemplateCateRel serviceTemplateCateRel : serviceTemplateCateRelList) {
                            ShopServiceInfo copyVo = new ShopServiceInfo();
                            //复制一个对象
                            BeanUtils.copyProperties(s, copyVo);
                            copyVo.setAppCateId(serviceTemplateCateRel.getCateId().intValue());

                            list.add(copyVo);
                        }
                    }
                }
            }
        }
        return list;

    }

    /**
     * 组装服务tag 0品质标,1营销标 sort asc 排序
     */
    private Map<Integer,List<ShopServiceTag>> getServiceFlag(ShopServiceInfo serviceInfo) {

        Long parentId = serviceInfo.getParentId();
        Map map = new HashMap();
        map.put("serviceId", parentId);
        Map<Integer,List<ShopServiceTag>> tagMap = null;
        try {
            tagMap = shopServiceTagRelService.getServiceFlag(map);
        } catch (Exception e) {
            logger.error("组装服务tag异常:{}",e);
        }
        return tagMap;

    }

    /**
     * 组装服务信息
     *
     * @param userGlobalIdList
     * @param typeList "CZFW" "TQFW"
     * @param flag 1 old接口 2 new接口
     *
     */
    private Result getServiceInfo(List<String> userGlobalIdList, List<String> typeList, int flag) {
        Map<Long, String> shopMap = getShopMap(userGlobalIdList);
        if (CollectionUtils.isEmpty(shopMap)) {
            logger.info("20020009 根据userGlobalId集合找不到门店信息,userGlobalId:{}", userGlobalIdList);
            return Result.wrapErrorResult("20020009", "根据userGlobalId集合找不到门店信息!");
        }
        //获得有效的服务信息
        List<ShopServiceInfo> shopServiceInfoList = getServiceInfoList(typeList, shopMap);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            //服务信息为空
            return Result.wrapSuccessfulResult(shopServiceInfoList);
        }
        List<ShopServiceInfo> shopServiceList = new ArrayList<>();
        List<ShopServiceInfo> shopServiceTQFWList = new ArrayList<>();
        List<Long> suiteServiceIdsList = new ArrayList<>();//suiteNum > 0 服务和服务套餐的ID
        for (ShopServiceInfo s : shopServiceInfoList) {
            String flags = s.getFlags().trim();
            if ("CZFW".equals(flags)) {
                //车主服务
                shopServiceList.add(s);
            } else if ("TQFW".equals(flags)) {
                //淘汽服务
                shopServiceTQFWList.add(s);
            }
            Long suiteNum = s.getSuiteNum();//0服务不加配件 1 服务加配件 2 套餐
            if (suiteNum != null && suiteNum > 0) {//服务和服务套餐
                suiteServiceIdsList.add(s.getId());
            }
        }
        //新组装的淘汽服务
        if (flag == 1) {
            //old
            shopServiceTQFWList = getServiceTQFWList(shopServiceTQFWList);
        } else if (flag == 2){
            //new
            shopServiceTQFWList = getServiceTQFWListNew(shopServiceTQFWList);
        }
        if (!CollectionUtils.isEmpty(shopServiceTQFWList)) {
            shopServiceList.addAll(shopServiceTQFWList);
        }
        //组装一级二级服务类别名称
        Map<Long,ShopServiceCate> serviceCateMap = shopServiceCateService.dealCateInfo();
        List<ShopServiceInfo> serviceList = new ArrayList<>();
        //车主服务获取服务类别
        for (ShopServiceInfo s : shopServiceList) {
            //服务的类目ID 在车主二级类目中
            Integer appCateId = s.getAppCateId();
            if (null != appCateId && -1 != appCateId) {
                Long cateId = Long.valueOf(appCateId);
                if (serviceCateMap.containsKey(cateId)) {
                    //把一级类目ID set到ShopServiceInfo中
                    s.setFirstCateId(serviceCateMap.get(cateId).getParentId());
                    //把一级类目名称 set到ShopServiceInfo中
                    s.setFirstCateName(serviceCateMap.get(cateId).getFirstCateName());
                    //把二级类目名称 set到ShopServiceInfo中
                    s.setAppCateName(serviceCateMap.get(cateId).getName());
                }
                serviceList.add(s);
            }
        }
        if (CollectionUtils.isEmpty(serviceList)) {
            //服务信息为空 都是垃圾数据
            return Result.wrapSuccessfulResult(shopServiceInfoList);
        }
        //组装服务和服务套餐的价格
        serviceList = getSuitePrice(suiteServiceIdsList, serviceList);
        //服务信息List
        List<ServiceInfoVo> serviceInfoList = new ArrayList<>();
        for (ShopServiceInfo s : serviceList) {
            ServiceInfoVo serviceInfo = new ServiceInfoVo();

            serviceInfo.setServiceId(s.getId());//服务ID
            serviceInfo.setFirstCateId(s.getFirstCateId());//一级类目ID
            serviceInfo.setFirstCateName(s.getFirstCateName());//一级类目名称
            serviceInfo.setSecondCateId(Long.valueOf(s.getAppCateId()));//二级类目ID
            serviceInfo.setSecondCateName(s.getAppCateName());//二级类目名称
            serviceInfo.setServiceSuiteName(s.getName());//服务名称
            serviceInfo.setPriceType(s.getPriceType());//价格类型
            BigDecimal suiteAmount = s.getSuiteAmount();
            if (null != suiteAmount && suiteAmount.compareTo(BigDecimal.ZERO) == 1) {
                serviceInfo.setServicePrice(s.getSuiteAmount());//服务套餐价格
            } else {
                serviceInfo.setServicePrice(s.getServicePrice());//服务价格
            }
            serviceInfo.setIsRecommend(s.getIsRecommend());//服务是否推荐
            serviceInfo.setServiceNote(s.getServiceNote());//服务内容
            serviceInfo.setUserGlobalId(shopMap.get(s.getShopId()));//user_global_id
            serviceInfo.setFlags(s.getFlags());
            serviceInfo.setImgUrl(s.getImgUrl());//服务图片
            serviceInfo.setSort(s.getSort());//排序字段
            serviceInfo.setStatus(s.getStatus());//状态

            //组装标识 0品质标,1营销标
            Map<Integer,List<ShopServiceTag>> tagMap = getServiceFlag(s);
            if (!CollectionUtils.isEmpty(tagMap)) {
                serviceInfo.setQualityFlagList(tagMap.get(0));
                serviceInfo.setMarketingFlagList(tagMap.get(1));
            }

            serviceInfoList.add(serviceInfo);
        }

        return Result.wrapSuccessfulResult(serviceInfoList);
    }

    //根据餐具获取有效的服务对象
    private List<ShopServiceInfo> getServiceInfoList(List<String> typeList, Map<Long, String> shopMap) {
        Map paramMap = new HashMap();//查询服务参数集合
        if (CollectionUtils.isEmpty(typeList)) {
            typeList = new ArrayList<>();
            typeList.add("CZFW");//车主服务
            typeList.add("TQFW");//淘汽服务
        }
        paramMap.put("flagsList", typeList);
        paramMap.put("shopIds", shopMap.keySet());
        paramMap.put("type", 1);//基本服务
        List<String> sorts = new ArrayList<>();
        sorts.add("sort asc");
        paramMap.put("sorts", sorts);
        //以下参数为了确保status = 0;
        int[] statuss = {0};
        paramMap.put("statuss", statuss);
        paramMap.put("status", statuss);
        //根据shopIds一次全部取出然后再剔除不是车主服务套餐
        logger.info("车主1.0版本获得服务对象接口参数:{}",  paramMap);
        return shopServiceInfoService.select(paramMap);
    }

    //获取shopId-userGlobalId Map集合
    private Map<Long, String> getShopMap(List<String> idList) {
        List<Shop> shopList;
        //idList[]为空的话 取所有的shop对象
        if (CollectionUtils.isEmpty(idList)) {
            shopList = shopService.select(null);
        } else {
            Map map = new HashMap(2);
            map.put("userGlobalIds", idList);
            shopList  = shopService.select(map);
        }
        Map<Long, String> map = new HashMap<>();//shopId-userGlobalId Map集合
        if (!CollectionUtils.isEmpty(shopList)) {
            for (Shop shop : shopList) {
                map.put(shop.getId(), shop.getUserGlobalId());//shopId-userGlobalId Map集合
            }
        }
        return map;
    }

    /**
     * 根据服务模版id和城市id取得用此服务模版且门店在对应城市的门店TemplateInfoVo(含服务id,门店userGlobalId)集合
     */
    @RequestMapping(value = "get_userGlobalIds", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result getShopIdsByTemplateId(@RequestParam(value = "templateId", required = true) Long templateId,
                                         @RequestParam(value = "cityId", required = true) Long cityId){
        logger.info("根据服务模版id:{}和城市id:{}取得用此服务模版且门店在对应城市的门店userGlobalId集合", templateId, cityId);
        //取得用服务模版id对应服务模版的门店id集合
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", templateId);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.select(map);//查询得到服务模版id对应的门店服务对象集合
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(null);
        }
        Map<Long, Long> serviceIdMap = new HashMap<>();//键值分别为门店id和服务id
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            Long shopId = shopServiceInfo.getShopId();
            serviceIdMap.put(shopId, shopServiceInfo.getId());
        }

        map.clear();
        map.put("shopIds", serviceIdMap.keySet());
        map.put("city", cityId);
        List<Shop> shopList = shopService.select(map);
        List<TemplateInfoVo> templateInfoVos = new ArrayList<>();//根据服务模版id和城市id取得用此服务模版且门店在对应城市的门店id集合
        if (CollectionUtils.isEmpty(shopList)) {
            return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(null);
        }
        for (Shop shop : shopList) {
            String userGlobalId = shop.getUserGlobalId();//门店userGlobalId
            if (!StringUtil.isStringEmpty(userGlobalId) && !"0".equals(userGlobalId)) {
                TemplateInfoVo templateInfoVo = new TemplateInfoVo();
                templateInfoVo.setUserGlobalId(userGlobalId);//设置templateInfoVo的门店userGlobalId
                templateInfoVo.setServiceId(serviceIdMap.get(shop.getId()));////设置templateInfoVo的服务id
                templateInfoVos.add(templateInfoVo);
            }
        }
        return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(templateInfoVos);
    }

    /**
     * 柯昌强 2016-1-26
     * 通过传入的templateIds，返回该淘汽服务的相关信息
     */
    @RequestMapping(value = "getTemplateInfo", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result getTemplateInfo(@RequestParam(value = "templateId[]", required = true) List<Long> templateIds) {
        //获得对应的服务模版集合
        Map map = new HashMap();
        map.put("ids", templateIds);
        List<ServiceTemplate> serviceTemplateList = serviceTemplateService.select(map);//获得服务模版
        if (CollectionUtils.isEmpty(serviceTemplateList)) {
            logger.info("对应的服务模版ID的集合：{}没有对应的服务模版", templateIds);
            return com.tqmall.core.common.entity.Result.wrapErrorResult("", "对应的服务模版ID的集合没有对应的服务模版");
        }

        //组装一级二级服务类别名称
        Map<Long,ShopServiceCate> serviceCateMap = shopServiceCateService.dealCateInfo();
        List<ServiceTemplateInfoVo> serviceTemplateInfoVos = new ArrayList<>();
        //组装服务模版信息
        for (ServiceTemplate serviceTemplate : serviceTemplateList) {
            Long templateId = serviceTemplate.getId();//服务模版id
            map.clear();
            map.put("serviceId", templateId);
            Map<Integer, List<ShopServiceTag>> tagMap = shopServiceTagRelService.getServiceFlag(map);//获得服务模版对应的服务标签
            ServiceTemplateInfoVo serviceTemplateInfoVo = new ServiceTemplateInfoVo();//服务模版信息
            BeanUtils.copyProperties(serviceTemplate, serviceTemplateInfoVo);
            if (!CollectionUtils.isEmpty(tagMap)) {
                serviceTemplateInfoVo.setQualityFlagList(tagMap.get(0));//设置服务模版的品质标
                serviceTemplateInfoVo.setMarketingFlagList(tagMap.get(1));//设置服务模版的营销标
            }
            map.clear();
            map.put("templateId", templateId);
            List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelService.select(map);//获得服务模版的类别关联对象
            if (!CollectionUtils.isEmpty(serviceTemplateCateRelList)) {
                ServiceTemplateCateRel serviceTemplateCateRel = serviceTemplateCateRelList.get(0);
                Long cateId = serviceTemplateCateRel.getCateId();//服务模版二级类目id
                ShopServiceCate shopServiceCate = serviceCateMap.get(cateId);
                if (shopServiceCate != null) {
                    serviceTemplateInfoVo.setFirstCateId(shopServiceCate.getParentId());//设置服务模版的一级类目id
                    serviceTemplateInfoVo.setFirstCateName(shopServiceCate.getFirstCateName());//设置服务模版的一级类目名称
                }
            }
            serviceTemplateInfoVos.add(serviceTemplateInfoVo);
        }
        return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(serviceTemplateInfoVos);
    }
}
