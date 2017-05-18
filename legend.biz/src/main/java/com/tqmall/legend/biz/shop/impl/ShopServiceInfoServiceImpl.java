package com.tqmall.legend.biz.shop.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.HttpUtil;
import com.tqmall.dandelion.model.result.coupon.CouponDTO;
import com.tqmall.dandelion.service.coupon.PgyCouponService;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.activity.IShopActivityService;
import com.tqmall.legend.biz.activity.IShopActivityServiceRelService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.shop.CarLevelService;
import com.tqmall.legend.biz.shop.ServiceGoodsSuiteService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.customer.AppointServiceDao;
import com.tqmall.legend.dao.order.OrderServicesDao;
import com.tqmall.legend.dao.shop.ServiceGoodsSuiteDao;
import com.tqmall.legend.dao.shop.ServiceTemplateCateRelDao;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.dao.shop.ShopServiceCateDao;
import com.tqmall.legend.dao.shop.ShopServiceInfoDao;
import com.tqmall.legend.entity.activity.ShopActivity;
import com.tqmall.legend.entity.activity.ShopActivityServiceListVO;
import com.tqmall.legend.entity.activity.ShopActivityServiceRel;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.shop.PackageVo;
import com.tqmall.legend.entity.shop.ServiceGoodsSuite;
import com.tqmall.legend.entity.shop.ServiceTemplateCateRel;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.entity.shop.ShopServiceTypeEnum;
import com.tqmall.legend.enums.serviceInfo.ServiceInfoAppPublishStatusEnum;
import com.tqmall.legend.enums.serviceInfo.ServiceInfoFlagsEnum;
import com.tqmall.legend.enums.serviceInfo.ServiceInfoStatusEnum;
import com.tqmall.legend.enums.serviceInfo.ServiceInfoTypeEnum;
import com.tqmall.zenith.errorcode.support.SourceKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zwb on 14/10/29.
 */
@Service
public class ShopServiceInfoServiceImpl extends BaseServiceImpl implements ShopServiceInfoService {

    @Autowired
    ShopServiceInfoDao shopServiceInfoDao;

    @Autowired
    ServiceGoodsSuiteDao serviceGoodsSuiteDao;

    @Autowired
    ShopServiceCateDao shopServiceCateDao;

    @Autowired
    ShopDao shopDao;

    @Autowired
    OrderServicesDao orderServicesDao;

    @Autowired
    AppointServiceDao appointServiceDao;

    Logger logger = LoggerFactory.getLogger(ShopServiceInfoServiceImpl.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CarLevelService carLevelService;

    @Autowired
    private ServiceGoodsSuiteService serviceGoodsSuiteService;

    @Autowired
    private ShopServiceCateService shopServiceCateService;

    @Autowired
    private ServiceTemplateCateRelDao serviceTemplateCateRelDao;


    @Value("${i.search.url}")
    private String iSearchUrl;

    @Autowired
    IShopActivityService shopActivityService;
    @Autowired
    ShopService shopService;
    @Autowired
    PgyCouponService pgyCouponService;
    @Autowired
    IShopActivityServiceRelService shopActivityServiceRelService;

    @Override
    public List<ShopServiceInfo> select(Map<String, Object> searchMap) {
        return shopServiceInfoDao.select(searchMap);
    }

    @Override
    public List<ShopServiceInfo> selectAllStatus(Map<String, Object> searchMap) {
        return shopServiceInfoDao.selectAllStatus(searchMap);
    }

    @Override
    public Integer selectCount(Map<String, Object> searchMap) {
        return shopServiceInfoDao.selectCount(searchMap);
    }

    @Override
    public Result<ShopServiceInfo> saveShopServiceInfo(@NotNull ShopServiceInfo shopServiceInfo, @NotNull Long shopId, @NotNull Long userId) {
        try {
            setServiceInfoTypeAndUnit(shopServiceInfo, shopId, userId);
            getCarLevelId(shopServiceInfo, shopId, userId);
            isExistCateName(shopServiceInfo);
            shopServiceInfoDao.insert(shopServiceInfo);
            return Result.wrapSuccessfulResult(shopServiceInfo);
        } catch (Exception e) {
            logger.error("通过shopId= " + shopId + ",serviceSn=" + shopServiceInfo.getServiceSn() + "，添加服务信息异常，异常信息：{}", e);
        }
        return Result.wrapErrorResult("", "添加服务信息失败");
    }

    private void setServiceInfoTypeAndUnit(ShopServiceInfo shopServiceInfo, Long shopId, Long userId) {
        shopServiceInfo.setShopId(shopId);
        shopServiceInfo.setCreator(userId);
        shopServiceInfo.setModifier(userId);
        if (shopServiceInfo.getType() == null) {
            shopServiceInfo.setType(ServiceInfoTypeEnum.GENERAL_SERVICE.getCode());// 服务类型：1:常规服务（包括大套餐用suite_num=2区分） 2:其它费用服务
        }
        if (StringUtils.isBlank(shopServiceInfo.getServiceUnit())) {
            shopServiceInfo.setServiceUnit("工时");
        }
    }

    //通过门店id和车辆级别名，获取id
    private void getCarLevelId(ShopServiceInfo shopServiceInfo, Long shopId, Long userId) {
        if (StringUtils.isNotBlank(shopServiceInfo.getCarLevelName())) {
            Long carLevelId = this.carLevelService.getCarLevelId(shopId, userId, shopServiceInfo.getCarLevelName());
            shopServiceInfo.setCarLevelId(carLevelId);
        }
    }

    @Override
    public boolean isExistByServiceSnAndShopId(@NotNull Long shopId, @NotNull String serviceSn) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("serviceSn", serviceSn);
        Integer count = shopServiceInfoDao.selectCount(param);
        if (count == null || count == 0) {
            return false;
        }
        return true;
    }

    @Override
    public Result add(ShopServiceInfo shopServiceInfo, UserInfo userInfo) {
        if (StringUtils.isBlank(shopServiceInfo.getName())) {
            return Result.wrapErrorResult(LegendErrorCode.NULL_SERVICE_NAME.getCode(), LegendErrorCode.NULL_SERVICE_NAME.getErrorMessage());
        }
        if (shopServiceInfo.getIsShow() != null && (shopServiceInfo.getIsRecommend() == null || shopServiceInfo.getIsRecommend() == -1)) {
            return Result.wrapErrorResult(LegendErrorCode.NULL_SERVICE_STATUS.getCode(), LegendErrorCode.NULL_SERVICE_STATUS.getErrorMessage());
        }
        if (shopServiceInfo.getIsShow() != null && (shopServiceInfo.getAppCateId() == null || shopServiceInfo.getAppCateId() <= 0 || StringUtils.isBlank(shopServiceInfo.getAppCateName()))) {
            return Result.wrapErrorResult(LegendErrorCode.NULL_SERVICE_CATE.getCode(), LegendErrorCode.NULL_SERVICE_CATE.getErrorMessage());
        }
        if (StringUtils.isNotBlank(shopServiceInfo.getServiceSn()) && this.isExistByServiceSnAndShopId(userInfo.getShopId(), shopServiceInfo.getServiceSn())) {
            return Result.wrapErrorResult(LegendErrorCode.SERVICE_SN_EXSIT.getCode(), LegendErrorCode.SERVICE_SN_EXSIT.getErrorMessage());
        }
        this.checkCarOwnerServiceInfo(shopServiceInfo);
        return this.saveShopServiceInfo(shopServiceInfo, userInfo.getShopId(), userInfo.getUserId());
    }

    /**
     * 判断车主服务显示时（是否有推荐的，有则，修改为不推荐），否则app_cate_id和is_recommend设置为不选择
     *
     * @param shopServiceInfo
     * @return
     */
    private ShopServiceInfo checkCarOwnerServiceInfo(ShopServiceInfo shopServiceInfo) {
        if (shopServiceInfo.getIsShow() != null && shopServiceInfo.getAppCateId() != null && shopServiceInfo.getIsRecommend() != null) {
            shopServiceInfo.setFlags(ServiceInfoFlagsEnum.CZFW.getCode());// flags设置CZFW=车主服务
            shopServiceInfo.setStatus(Long.valueOf(ServiceInfoStatusEnum.VALID.getCode()));
            shopServiceInfo.setAppPublishStatus(ServiceInfoAppPublishStatusEnum.PUBLISHED.getCode());
        } else {
            shopServiceInfo.setAppCateId(-1);
            shopServiceInfo.setIsRecommend(-1);
            shopServiceInfo.setAppPublishStatus(ServiceInfoAppPublishStatusEnum.NOT_PUBLISH.getCode());
        }
        return shopServiceInfo;
    }


    // TODO 待重构：合并add方法，新增成功后：直接返回新增后实体
    @Override
    public Result addInOrder(ShopServiceInfo shopServiceInfo, UserInfo userInfo) {
        return this.saveShopServiceInfo(shopServiceInfo, userInfo.getShopId(), userInfo.getUserId());
    }

    /**
     * create by jason 2015-09-24
     * 如果cateName存在,返回改对象.如果不存在,新建一个类别
     *
     * @param shopServiceInfo
     */
    private void isExistCateName(ShopServiceInfo shopServiceInfo) {
        String categoryName = shopServiceInfo.getCategoryName();
        if (StringUtils.isNotEmpty(categoryName)) {
            ShopServiceCate shopServiceCate = new ShopServiceCate();
            shopServiceCate.setName(categoryName);
            shopServiceCate.setShopId(shopServiceInfo.getShopId());
            shopServiceCate.setCreator(shopServiceInfo.getModifier());
            Result shopServiceCateResult = shopServiceCateService.addWithoutRepeat(shopServiceCate);
            shopServiceInfo.setCategoryId(0L);
            if (shopServiceCateResult.isSuccess()) {
                shopServiceCate = (ShopServiceCate) shopServiceCateResult.getData();
                shopServiceInfo.setCategoryId(shopServiceCate.getId());
                shopServiceInfo.setCateTag(shopServiceCate.getCateTag());//回填服务tag
            }
        }
    }

    @Override
    public Result update(ShopServiceInfo shopServiceInfo, UserInfo userInfo) {
        if (StringUtils.isBlank(shopServiceInfo.getName())) {
            return Result.wrapErrorResult(LegendErrorCode.NULL_SERVICE_NAME.getCode(), LegendErrorCode.NULL_SERVICE_NAME.getErrorMessage());
        }
        if (shopServiceInfo.getIsShow() != null && (shopServiceInfo.getIsRecommend() == null || shopServiceInfo.getIsRecommend() == -1)) {
            return Result.wrapErrorResult(LegendErrorCode.NULL_SERVICE_STATUS.getCode(), LegendErrorCode.NULL_SERVICE_STATUS.getErrorMessage());
        }
        if (shopServiceInfo.getIsShow() != null && (shopServiceInfo.getAppCateId() == null || shopServiceInfo.getAppCateId() <= 0 || StringUtils.isBlank(shopServiceInfo.getAppCateName()))) {
            return Result.wrapErrorResult(LegendErrorCode.NULL_SERVICE_CATE.getCode(), LegendErrorCode.NULL_SERVICE_CATE.getErrorMessage());
        }
        Long serviceId = shopServiceInfo.getId();
        try {
            ShopServiceInfo oldShopServiceInfo = selectById(serviceId);
            if (null == oldShopServiceInfo) {
                return Result.wrapErrorResult(LegendErrorCode.SERVICE_NOT_EXSIT.getCode(), LegendErrorCode.SERVICE_NOT_EXSIT.getErrorMessage());
            }
            shopServiceInfo.setType(oldShopServiceInfo.getType());
            setServiceInfoTypeAndUnit(shopServiceInfo, userInfo.getShopId(), userInfo.getUserId());
            getCarLevelId(shopServiceInfo, userInfo.getShopId(), userInfo.getUserId());
            // 车主服务套餐
            Integer isShow = shopServiceInfo.getIsShow();
            if (null == isShow) {// update为不开放车主服务
                compareOldAndNewServiceByFlags(shopServiceInfo, oldShopServiceInfo);
            } else {
                this.checkCarOwnerServiceInfo(shopServiceInfo);
            }
            isExistCateName(shopServiceInfo);
            shopServiceInfoDao.updateById(shopServiceInfo);
            return Result.wrapSuccessfulResult("服务资料添加成功");
        } catch (Exception e) {
            logger.error("根据服务资料id=" + serviceId + "，更新服务信息异常，异常信息：{}", e);
        }
        return Result.wrapErrorResult("", "更新服务失败");
    }

    //比较新旧服务标签，如果是车主服务，flags置为空字符串,appPublishStatus置为0未发布
    private void compareOldAndNewServiceByFlags(ShopServiceInfo shopServiceInfo, ShopServiceInfo oldShopServiceInfo) {
        shopServiceInfo.setAppPublishStatus(ServiceInfoAppPublishStatusEnum.NOT_PUBLISH.getCode());//车主服务下架
        if (ServiceInfoFlagsEnum.CZFW.getCode().equals(oldShopServiceInfo.getFlags())) {
            shopServiceInfo.setFlags("");
        }
    }

    /**
     * @param servicesIds
     * @return
     */
    @Override
    public List<ShopServiceInfo> selectByIds(Long[] servicesIds) {
        return shopServiceInfoDao.selectByIds(servicesIds);
    }

    @Override
    public ShopServiceInfo selectById(Long servicesId) {
        return shopServiceInfoDao.selectById(servicesId);
    }

    /**
     * @param servicesId
     * @param shopId
     * @return
     */
    @Override
    public ShopServiceInfo selectById(Long servicesId, Long shopId) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", servicesId);
        hashMap.put("shopId", shopId);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoDao.select(hashMap);

        if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
            return shopServiceInfoList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<ShopServiceInfo> queryShopServiceList(Long shopId, ShopServiceTypeEnum shopServiceTypeEnum, String flags) {
        Map<String, Object> paramsMap = new HashMap<String, Object>(3);
        paramsMap.put("type", shopServiceTypeEnum.getCode());
        paramsMap.put("shopId", shopId);
        paramsMap.put("flags", flags);
        return this.select(paramsMap);
    }

    /**
     * create by zsy 2015-09-15
     * 门店初始化，添加标准化服务
     *
     * @param shopServiceInfoList
     * @return
     */
    @Override
    @Transactional
    public Integer initNormalShopServiceInfo(List<ShopServiceInfo> shopServiceInfoList) {
        Integer totalSize = super.batchInsert(shopServiceInfoDao, shopServiceInfoList, null);
        return totalSize;
    }

    @Override
    public List<ShopServiceInfo> selectByIds(List<Long> servicesIds) {
        return super.selectByIds(shopServiceInfoDao, servicesIds);
    }

    @Override
    public List<ShopServiceInfo> selectAllByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        int size = ids.size();
        Long[] idsArr = ids.toArray(new Long[size]);
        return shopServiceInfoDao.selectAllByIds(idsArr);
    }

    @Override
    public ShopServiceInfo selectByIdAndShopId(Long id, Long shopId) {
        return shopServiceInfoDao.selectByIdAndShopId(id, shopId);
    }


    /**
     * create by jason 2015-11-02
     * 然后还要组装服务类别和价格
     */
    @Override
    public List<ShopServiceInfo> wrapServiceCateAndSuitePrice(List<Long> ids) {
        int size = ids.size();
        Long[] idsArr = ids.toArray(new Long[size]);

        List<ShopServiceInfo> shopServiceList = shopServiceInfoDao.selectAllByIds(idsArr);
        if (CollectionUtils.isEmpty(shopServiceList)) {
            //根据appointId找不到预约单服务信息
            return shopServiceList;
        }
        //车主服务获取服务类别
        Map<Long, ShopServiceCate> serviceCateMap = shopServiceCateService.dealCateInfo();
        //组装服务类别和套餐价格
        wrapCateAndPrice(shopServiceList, serviceCateMap);
        return shopServiceList;
    }

    //组装服务类别和套餐价格
    private void wrapCateAndPrice(List<ShopServiceInfo> shopServiceList, Map<Long, ShopServiceCate> serviceCateMap) {
        //suiteNum > 0 服务和服务套餐的ID
        List<Long> suiteServiceIdsList = new ArrayList<>();
        for (ShopServiceInfo s : shopServiceList) {
            //处理非淘汽服务类目ID
            //服务的类目ID 在车主二级类目中
            Integer appCateId = s.getAppCateId();
            String flags = s.getFlags();
            if (null != appCateId && -1 != appCateId && !"TQFW".equals(flags)) {
                Long cateId = Long.valueOf(appCateId);
                setCateIdAndName(serviceCateMap, s, cateId);//set一级服务类别Id和名称,二级服务名称
            }
            //淘汽服务类别ID处理
            if ("TQFW".equals(s.getFlags())) {
                //淘汽服务对应的父ID
                Long parentId = s.getParentId();
                if (null != parentId && parentId > 0l) {
                    Map map = new HashMap();
                    map.put("templateId", parentId);
                    //去服务和类目对应关系表中获得数据 一个淘汽服务可能对应多个类目ID 随机取一个
                    List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelDao.select(map);

                    if (!CollectionUtils.isEmpty(serviceTemplateCateRelList)) {
                        Long cateId = serviceTemplateCateRelList.get(0).getCateId();
                        if (null != cateId && cateId > 0l) {
                            s.setAppCateId(cateId.intValue());
                            setCateIdAndName(serviceCateMap, s, cateId);//set一级服务类别Id和名称,二级服务名称
                        }
                    }
                }
            }
            Long suiteNum = s.getSuiteNum();//0服务不加配件 1 服务加配件 2 套餐
            if (suiteNum != null && suiteNum > 0l) {//服务和服务套餐
                suiteServiceIdsList.add(s.getId());
            }
        }
        //组装服务和服务套餐的价格
        getSuitePrice(suiteServiceIdsList, shopServiceList);
    }

    //set一级服务类别Id和名称,二级服务名称
    private void setCateIdAndName(Map<Long, ShopServiceCate> serviceCateMap, ShopServiceInfo s, Long cateId) {
        if (serviceCateMap.containsKey(cateId)) {
            ShopServiceCate serviceCate = serviceCateMap.get(cateId);
            //把一级类目ID set到ShopServiceInfo中
            s.setFirstCateId(serviceCate.getParentId());
            //把一级类目名称 set到ShopServiceInfo中
            s.setFirstCateName(serviceCate.getFirstCateName());
            //把二级类目名称 set到ShopServiceInfo中
            s.setAppCateName(serviceCate.getName());
        }
    }

    /**
     * 组装服务,套餐价格
     * create by jason 2015-07-17
     */
    private void getSuitePrice(List<Long> suiteServiceIdsList, List<ShopServiceInfo> serviceList) {
        //获得服务和套餐价格
        if (!CollectionUtils.isEmpty(suiteServiceIdsList)) {
            Long[] serviceIds = suiteServiceIdsList.toArray(new Long[suiteServiceIdsList.size()]);
            // 获取有套餐的服务数据相对应的套餐数据内容
            List<ServiceGoodsSuite> serviceGoodsSuiteList = serviceGoodsSuiteService.selectByServiceIds(serviceIds);
            if (serviceGoodsSuiteList != null) {
                // 套餐数据中的套餐金额存入map中
                HashMap<Long, BigDecimal> serviceGoodsSuiteMap = new HashMap<>();
                for (ServiceGoodsSuite serviceGoodsSuite : serviceGoodsSuiteList) {
                    serviceGoodsSuiteMap.put(serviceGoodsSuite.getServiceId(), serviceGoodsSuite.getSuitePrice());
                }
                // 把套餐中的套餐金额，放入服务数据中
                for (ShopServiceInfo shopServiceInfo : serviceList) {
                    shopServiceInfo.setSuiteAmount(serviceGoodsSuiteMap.get(shopServiceInfo.getId()));
                }
            }
        }
    }

    @Override
    @Transactional
    public Integer update(ShopServiceInfo shopServiceInfo) {
        return shopServiceInfoDao.updateById(shopServiceInfo);
    }


    /**
     * TODO refactor code
     * <p/>
     * 获取标准洗车服务列表
     *
     * @param shopId
     * @return
     */
    public List<ShopServiceInfo> getBZCarWashList(Long shopId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("shopId", shopId);
        searchMap.put("cateTag", 2);
        searchMap.put("searchBZFW", 0);

        List<String> sorts = new ArrayList<>();
        sorts.add(" service_price asc ");
        searchMap.put("sorts", sorts);

        searchMap.put("offset", 0);
        searchMap.put("limit", 3);

        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoDao.select(searchMap);
        return shopServiceInfoList;
    }

    @Override
    @Transactional
    public Result saveWashCarServiceList(List<ShopServiceInfo> shopServiceInfoList, Long shopId) {
        for (ShopServiceInfo temp : shopServiceInfoList) {
            Map<String, Object> param = new HashMap<>();
            param.put("shopId", shopId);
            param.put("id", temp.getId());
            List<ShopServiceInfo> shopServiceInfoListTemp = shopServiceInfoDao.select(param);
            if (CollectionUtils.isEmpty(shopServiceInfoListTemp)) {
                return Result.wrapErrorResult("10004", "服务不存在");
            }
            ShopServiceInfo shopServiceInfo = shopServiceInfoListTemp.get(0);
            shopServiceInfo.setServicePrice(temp.getServicePrice());
            shopServiceInfo.setServiceAmount(temp.getServiceAmount());
            shopServiceInfoDao.updateById(shopServiceInfo);
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 通用搜索
     *
     * @param shopId
     * @param serviceSn
     * @param serviceName
     * @param type
     * @param suiteNumLT
     * @param size
     * @return
     */
    public Result search(Long shopId, String serviceSn, String serviceName, String type, String suiteNumLT, Integer size) {

        StringBuffer requestParam = new StringBuffer("1=1");
        if (shopId != null && shopId != 0) {
            requestParam.append("&shopId=");
            requestParam.append(shopId);
        }
        if (size != null && size != 0) {
            requestParam.append("&size=");
            requestParam.append(size);
        }

        if (StringUtils.isNotEmpty(serviceSn)) {
            requestParam.append("&serviceSn=");
            requestParam.append(serviceSn);
        }

        if (StringUtils.isNotEmpty(serviceName)) {
            try {
                String serviceNameTran = URLEncoder.encode(serviceName, "utf-8");
                requestParam.append("&serviceName=");
                requestParam.append(serviceNameTran);
            } catch (UnsupportedEncodingException e) {
                logger.warn(e.getMessage());
            }
        }

        if (StringUtils.isNotEmpty(type)) {
            requestParam.append("&type=");
            requestParam.append(type);
        }

        if (StringUtils.isNotEmpty(suiteNumLT)) {
            requestParam.append("&suiteNumLT=");
            requestParam.append(suiteNumLT);
        }
        requestParam.append("&status=0,-2");//0正常-1TQFW下架-2 CZFW下架
        // iSearchUrl
        String requestUrl = iSearchUrl + "elasticsearch/cloudRepair/legendShopServer/shopServer";
        logger.info("requestUrl:{}", requestUrl);
        logger.info("requestParam:{}", requestParam.toString());

        // 相应实体

        try {
            Result respResult = new Result();
            String result = HttpUtil.sendGet(requestUrl, requestParam.toString(),1000,60000);
            JSONObject reqSearchResult = JSON.parseObject(result);
            JSONObject reqSearchResponse = reqSearchResult.getJSONObject("response");
            JSONArray reqSearchList = reqSearchResponse.getJSONArray("list");
            respResult.setSuccess(true);
            respResult.setData(reqSearchList);
            return respResult;
        } catch (Exception e) {
            logger.error("调用搜索服务，查询服务项目失败！", e);
            throw new BizException("查询服务项目失败，请稍后再试");
        }
    }

    @Override
    public Result getPackageByServiceId(Long shopId, String serviceParam) {
        //解析出多个服务或套餐
        String[] temp = serviceParam.split(",");
        if (null != temp && 0 < temp.length) {
            Long serviceId = 0l;
            int count = 0;
            PackageVo packageVo = new PackageVo();
            List<ShopServiceInfo> shopServiceInfoList = new ArrayList<>();
            List<Goods> goodsList = new ArrayList<>();
            //解析出对于的服务ID和服务个数
            for (String serviceTemp : temp) {
                serviceId = Long.parseLong(serviceTemp.split("n")[0]);
                count = Integer.parseInt(serviceTemp.split("n")[1]);
                ShopServiceInfo shopServiceInfo = this.selectById(serviceId, shopId);

                if (shopServiceInfo != null) {
                    ServiceGoodsSuite serviceGoodsSuite = serviceGoodsSuiteService.selectByServiceId(serviceId, shopId);
                    if (serviceGoodsSuite == null) {
                        return Result.wrapErrorResult("", "不是服务套餐");
                    }
                    String goodsInfo = serviceGoodsSuite.getGoodsInfo();
                    String serviceInfo = serviceGoodsSuite.getServiceInfo();

                    List<Goods> goodsMapList = new Gson().fromJson(goodsInfo, new TypeToken<List<Goods>>() {
                    }.getType());

                    // 存放goodsId
                    StringBuffer goodsIdSb = new StringBuffer();
                    if (!CollectionUtils.isEmpty(goodsMapList)) {
                        for (int i = 0; i < goodsMapList.size(); i++) {
                            Long goodsId = goodsMapList.get(i).getId();
                            goodsIdSb.append(goodsId).append(",");
                            Optional<Goods> goodsOptional = goodsService.selectById(goodsId, shopId);
                            if (goodsOptional.isPresent()) {
                                Goods goods = goodsOptional.get();
                                goods.setGoodsNum(goodsMapList.get(i).getGoodsNum());
                                goods.setGoodsNum(goods.getGoodsNum() * count);
                                goodsList.add(goods);
                            }

                        }
                        packageVo.setGoodsList(goodsList);
                    }

                    if (shopServiceInfo.getType() == 1 && shopServiceInfo.getSuiteNum() == 1) {

                        ShopServiceCate shopServiceCate = shopServiceCateService.selectById(shopServiceInfo.getCategoryId());
                        if (shopServiceCate != null) {
                            shopServiceInfo.setServiceCatName(shopServiceCate.getName());
                        }
                        shopServiceInfo.setServiceNum(Long.parseLong(count + ""));
                        // 获得服务对应goodsId
                        if (StringUtil.isNotStringEmpty(goodsIdSb.toString())) {
                            shopServiceInfo.setGoodsIdStr((goodsIdSb.deleteCharAt(goodsIdSb.length() - 1)).toString());// 截掉最后一位逗号
                        }
                        shopServiceInfoList.add(shopServiceInfo);

                    } else if (shopServiceInfo.getType() == 1 && shopServiceInfo.getSuiteNum() == 2) {
                        List<ShopServiceInfo> shopServiceMapList = new Gson().fromJson(serviceInfo, new TypeToken<List<ShopServiceInfo>>() {
                                }.getType());
                        if (!CollectionUtils.isEmpty(shopServiceMapList)) {
                            for (int j = 0; j < shopServiceMapList.size(); j++) {
                                Long shopServiceId = shopServiceMapList.get(j).getId();
                                ShopServiceInfo shopServiceInfo1 = this.selectById(shopServiceId, shopId);
                                if (shopServiceInfo1 != null) {

                                    // 获得服务对应的goodsId 为了前端删除服务的时候联动删除配件
                                    ServiceGoodsSuite serviceGoodsSuite1 = serviceGoodsSuiteService.selectByServiceId(shopServiceId, shopId);
                                    if (null != serviceGoodsSuite1) {
                                        String goodsInfo1 = serviceGoodsSuite1.getGoodsInfo();
                                        List<Goods> goodsMapList1 = new Gson().fromJson(goodsInfo1, new TypeToken<List<Goods>>() {
                                                }.getType());

                                        if (!CollectionUtils.isEmpty(goodsMapList1)) {
                                            StringBuffer goodsIdSb1 = new StringBuffer();
                                            for (int i = 0; i < goodsMapList1.size(); i++) {
                                                Long goodsId1 = goodsMapList1.get(i).getId();
                                                goodsIdSb1.append(goodsId1).append(",");
                                            }
                                            if (StringUtil.isNotStringEmpty(goodsIdSb1.toString())) {
                                                shopServiceInfo1.setGoodsIdStr((goodsIdSb1.deleteCharAt(goodsIdSb1.length() - 1)).toString());
                                            }
                                        }
                                    }
                                    if (shopServiceInfo1.getCategoryId() != null) {
                                        ShopServiceCate shopServiceCate1 = shopServiceCateService.selectById(shopServiceInfo1.getCategoryId());
                                        if (shopServiceCate1 != null) {
                                            shopServiceInfo1.setServiceCatName(shopServiceCate1.getName());
                                        }
                                    }
                                }
                                shopServiceInfo1.setServiceNum(shopServiceMapList.get(j).getServiceNum());
                                shopServiceInfo1.setServiceNum(shopServiceInfo1.getServiceNum() * count);
                                shopServiceInfoList.add(shopServiceInfo1);
                            }
                        }
                    }
                    packageVo.setShopServiceInfoList(shopServiceInfoList);
                }
            }
            return Result.wrapSuccessfulResult(packageVo);
        } else {
            return Result.wrapErrorResult("", "服务不存在");
        }
    }


    @Override
    public List<ShopServiceInfo> getServiceByFlag(String flag, Long shopId) {

        HashMap<String, Object> paramMap = new HashMap<String, Object>(2);
        paramMap.put("flags", flag);
        paramMap.put("shopId", shopId);
        List<ShopServiceInfo> shopServiceInfoList = null;
        try {
            shopServiceInfoList = shopServiceInfoDao.select(paramMap);
        } catch (Exception e) {
            logger.error("[DB]query shopService failure!,exception:{}", e.toString());
            shopServiceInfoList = new ArrayList<ShopServiceInfo>();
        }
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            shopServiceInfoList = new ArrayList<ShopServiceInfo>();
        }

        return shopServiceInfoList;
    }

    @Override
    public Optional<ShopServiceInfo> get(Long serviceId) {
        // get shopService
        ShopServiceInfo serviceInfo = null;
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>(2);
            paramMap.put("id", serviceId);
            paramMap.put("status", "ALL");
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoDao.select(paramMap);
            if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                serviceInfo = shopServiceInfoList.get(0);
            }
        } catch (Exception e) {
            logger.error("[DB] query shopService failure!,exception:{}", e.toString());
            return Optional.absent();
        }
        if (serviceInfo != null) {
            Long categoryId = serviceInfo.getCategoryId();
            ShopServiceCate category = shopServiceCateDao.selectById(categoryId);
            if (category != null) {
                serviceInfo.setCategoryName(category.getName());
            }
        }

        return Optional.fromNullable(serviceInfo);
    }

    @Override
    public ShopActivityServiceListVO getShopActivityServiceList(@NotNull Long shopId, @NotNull Long activityTemplateId) throws BusinessCheckedException {

        ShopActivityServiceListVO activityServiceListVO = new ShopActivityServiceListVO();
        // 1.根据活动模板,获取门店活动
        Optional<ShopActivity> shopActivityOptional = shopActivityService.get(shopId, activityTemplateId);
        if (!shopActivityOptional.isPresent()) {
            logger.error("获取门店活动的有效服务失败,该门店活动不存在,门店ID:{} 活动模板ID:{}", shopId, activityTemplateId);
            throw new BusinessCheckedException("", "活动不存在");
        }
        ShopActivity activity = shopActivityOptional.get();
        Long activityId = activity.getId();

        // 2.获取活动下有效服务套餐
        List<ShopServiceInfo> servcieList = this.getActivityServiceList(activityId);

        activityServiceListVO.setActivity(activity);
        activityServiceListVO.setServicelist(servcieList);

        return activityServiceListVO;
    }

    @Override
    public List<ShopServiceInfo> getActivityServiceList(Long activityId) {
        List<ShopServiceInfo> serviceInfoList = null;
        try {
            serviceInfoList = shopServiceInfoDao.queryActivityServiceList(activityId);
        } catch (Exception e) {
            logger.error("[DB]查询shopService异常,异常信息:{}", e);
            serviceInfoList = new ArrayList<ShopServiceInfo>();
        }

        return serviceInfoList;

    }

    @Override
    public Optional<ShopServiceInfo> getCouponActivityService(String couponCode, Long shopId) {

        ShopServiceInfo serviceInfo = null;

        // 1.dubbo 根据优惠券获取模板ID
        com.tqmall.zenith.errorcode.support.Result<CouponDTO> couponDTOResult = null;
        logger.info("[DUBBO]根据优惠券码获取优惠券,优惠券码:{}", couponCode);
        try {
            couponDTOResult = pgyCouponService.getCouponByCode(SourceKey.LEGEND.getKey(), couponCode);
        } catch (Exception e) {
            logger.error("[DUBBO]根据优惠券码获取优惠券异常,异常信息:{}", e);
            return Optional.fromNullable(serviceInfo);
        }

        if (!couponDTOResult.isSuccess()) {
            logger.error("[DUBBO]根据优惠券码获取优惠券失败");
            return Optional.fromNullable(serviceInfo);
        }
        CouponDTO couponDTO = couponDTOResult.getData();

        if (couponDTO == null) {
            logger.error("[DUBBO]根据优惠券码获取优惠券失败");
            return Optional.fromNullable(serviceInfo);
        }
        Integer templateId = couponDTO.getTemplateId();

        // 获取门店服务
        List<ShopServiceInfo> servcieList = this.getServicelist(templateId, shopId);
        if (!CollectionUtils.isEmpty(servcieList)) {
            serviceInfo = servcieList.get(0);

            // 查询对应的活动ID
            Long serviceId = serviceInfo.getId();
            Optional<ShopActivityServiceRel> shopActivityServiceRelOptional = shopActivityServiceRelService.getActivityId(serviceId);
            if (shopActivityServiceRelOptional.isPresent()) {
                ShopActivityServiceRel shopActivityServiceRel = shopActivityServiceRelOptional.get();
                serviceInfo.setShopActivityId(shopActivityServiceRel.getShopActId());
            } else {
                serviceInfo.setShopActivityId(0l);
            }
        }

        return Optional.fromNullable(serviceInfo);
    }


    @Override
    public List<ShopServiceInfo> getServicelist(Integer templateId, Long shopId) {
        Map<String, Object> paramMap = new HashMap<String, Object>(3);
        paramMap.put("parentId", templateId);
        paramMap.put("shopId", shopId);
        // 0 有效服务
        paramMap.put("status", "0");

        return shopServiceInfoDao.select(paramMap);
    }

    @Override
    @Transactional
    public Integer batchInsert(List<ShopServiceInfo> shopServiceInfoList) {
        return super.batchInsert(shopServiceInfoDao, shopServiceInfoList, 300);
    }

    @Override
    public List<ShopServiceInfo> list(Long shopId, Collection<Long> ids) {
        List<ShopServiceInfo> shopServiceInfos = new ArrayList<>();
        if (CollectionUtils.isEmpty(ids)) {
            return shopServiceInfos;
        }
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        shopServiceInfos = shopServiceInfoDao.selectByIdss(shopId, ids);
        return shopServiceInfos;
    }

    @Override
    public Set<Long> listInvalidIds(final Long shopId, final Collection<Long> ids) {

        return new BizTemplate<Set<Long>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "shopId can not be null");
                Assert.notNull(ids, "ids can not be null");
            }

            @Override
            protected Set<Long> process() throws BizException {
                Set<Long> invalidIdSet = Sets.newHashSet(ids);
                if (CollectionUtils.isEmpty(ids)) {
                    return invalidIdSet;
                }
                List<ShopServiceInfo> validList = list(shopId, ids);
                for (ShopServiceInfo shopServiceInfo : validList) {
                    Long id = shopServiceInfo.getId();
                    if (invalidIdSet.contains(id)) {
                        invalidIdSet.remove(id);
                    }
                }
                return invalidIdSet;
            }
        }.execute();
    }

    @Override
    public ShopServiceInfo getByTplId(Long shopId, Long serviceTplId, Integer status) throws IllegalArgumentException {
        Assert.notNull(shopId, "shopId不能为空");
        Assert.notNull(serviceTplId, "服务模版id不能为空");
        Map<String, Object> param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("parentId", serviceTplId);
        param.put("status", status);
        param.put("sorts", new String[] { "gmt_create asc" });
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoDao.select(param);
        if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
            return shopServiceInfoList.get(0);
        }
        return null;
    }

    @Override
    public int selectCountAllStatus(Map<String, Object> qryServiceInfoParam) {
        if (qryServiceInfoParam == null || qryServiceInfoParam.isEmpty()) {
            return 0;
        }
        return shopServiceInfoDao.selectCountAllStatus(qryServiceInfoParam);
    }

    @Override
    public int updateById(ShopServiceInfo shopServiceInfo, UserInfo userInfo) {
        Assert.notNull(shopServiceInfo, "要保存的服务不能为空");
        Assert.notNull(userInfo, "操作员信息不能为空");
        shopServiceInfo.setModifier(userInfo.getUserId());
        return shopServiceInfoDao.updateById(shopServiceInfo);
    }

    @Override
    public List<ShopServiceInfo> selectByCatIds(Collection<Long> catIds) {
        if (CollectionUtils.isEmpty(catIds)) {
            return Lists.newArrayList();
        }
        return shopServiceInfoDao.selectByCatIds(catIds);
    }

    @Override
    public void setServiceSuitAmount(Collection<ShopServiceInfo> shopServiceInfoList) {
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return;
        }
        Set<Long> suitIds = new HashSet<>();
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            if (shopServiceInfo.getSuiteNum() != null && shopServiceInfo.getSuiteNum().longValue() > 0) {
                suitIds.add(shopServiceInfo.getId());
            }
        }
        List<ServiceGoodsSuite> serviceGoodsSuiteList = serviceGoodsSuiteService.selectByServiceIds(suitIds.toArray(new Long[] { }));
        Map<Long, BigDecimal> suitPriceMap = new HashMap<>();//key:servcieId,value:suitePrice
        if (!CollectionUtils.isEmpty(serviceGoodsSuiteList)) {
            for (ServiceGoodsSuite serviceGoodsSuite : serviceGoodsSuiteList) {
                suitPriceMap.put(serviceGoodsSuite.getServiceId(), serviceGoodsSuite.getSuitePrice());
            }
        }
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            if (shopServiceInfo.getSuiteNum() != null && shopServiceInfo.getSuiteNum().longValue() > 0) {
                shopServiceInfo.setSuiteAmount(suitPriceMap.get(shopServiceInfo.getId()));
            }
        }
    }

    public void setSuitAmount2ServicePrice(Collection<ShopServiceInfo> shopServiceInfoList) {
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return;
        }
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            if (shopServiceInfo.getSuiteAmount() != null) {
                shopServiceInfo.setServicePrice(shopServiceInfo.getSuiteAmount());
            }
        }
    }

    @Override
    public List<ShopServiceInfo> findServiceInfoByNames(Long shopId, String... names) {
        return shopServiceInfoDao.findServiceInfoByNames(shopId, names);
    }

    @Override
    public int getServiceInfoCount(Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        return shopServiceInfoDao.selectCount(param);
    }
}
