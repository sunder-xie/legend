package com.tqmall.legend.facade.appoint.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.customer.AppointServiceService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ServiceGoodsSuiteService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.AppointAppVo;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.customer.AppointVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.ServiceGoodsSuite;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.appoint.AppointChannelEnum;
import com.tqmall.legend.object.enums.appoint.AppointStatusEnum;
import com.tqmall.legend.facade.appoint.AppointFacade;
import com.tqmall.legend.facade.appoint.vo.AppointDetailFacVo;
import com.tqmall.legend.facade.customer.CustomerCarFacade;
import com.tqmall.legend.facade.customer.vo.CarDetailVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lixiao on 16/4/8.
 */
@Slf4j
@Service
public class AppointFacadeImpl implements AppointFacade{
    @Autowired
    private AppointService appointService;
    @Autowired
    private AppointServiceService appointServiceService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private ServiceGoodsSuiteService serviceGoodsSuiteService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private CustomerCarFacade customerCarFacade;

    @Override
    public List<Appoint> getAppointInfoList(Map<String, Object> searchParams) {
        List<Appoint> appointList = new ArrayList<>();
        Long shopId = null;
        if (searchParams==null || searchParams.get("shopId")==null) {
            return appointList;
        } else {
            shopId = (Long) searchParams.get("shopId");
            if (searchParams.get("limit") == null) {
                searchParams.put("limit", 1000);//单次最多查询1000条数据
            } else {
                int limit = Integer.parseInt(searchParams.get("limit").toString());
                if (limit > 1000) {
                    searchParams.put("limit", 1000);//单次最多查询1000条数据
                }
            }
            if(searchParams.get("offset")==null){
                searchParams.put("offset",0);
            }
            appointList = appointService.select(searchParams);
        }
        if(CollectionUtils.isEmpty(appointList)){
            return appointList;
        }
        Set<Long> appointIds = new HashSet<>();
        Set<Long> customerCarIds = new HashSet<>();
        for (Appoint appoint : appointList) {
            appointIds.add(appoint.getId());
            customerCarIds.add(appoint.getCustomerCarId());
        }
        //查询车型
        Map<String,Object> carParams = Maps.newHashMap();
        carParams.put("shopId", shopId);
        carParams.put("ids", customerCarIds);
        List<CustomerCar> customerCarList = customerCarService.select(carParams);
        if(!CollectionUtils.isEmpty(customerCarList)){
            for (Appoint appoint : appointList) {
                for (CustomerCar customerCar : customerCarList) {
                    if(appoint.getCustomerCarId().longValue()==customerCar.getId().longValue()){
                        appoint.setCarInfo(customerCar.getCarInfo());//设置预约单车辆型号
                        break;
                    }
                }
            }
        }
        //查询预约服务
        Map<String,Object> appointServiceParams = Maps.newHashMap();
        appointServiceParams.put("shopId", shopId);
        appointServiceParams.put("appointIds", appointIds);
        List<AppointServiceVo> appointServiceVoList = appointServiceService.select(appointServiceParams);
        if(CollectionUtils.isEmpty(appointServiceVoList)){
            return appointList;
        }
        Set<Long> serviceIds = new HashSet<>();//服务Id的集合
        for (AppointServiceVo appointServiceVo : appointServiceVoList) {
            serviceIds.add(appointServiceVo.getServiceId());
        }
        //查询物料
        Map<String,Object> goodsSuiteParams = Maps.newHashMap();
        goodsSuiteParams.put("shopId", shopId);
        goodsSuiteParams.put("serviceIds", serviceIds);
        List<ServiceGoodsSuite> serviceGoodsSuiteList = serviceGoodsSuiteService.select(goodsSuiteParams);
        for (Appoint appoint : appointList) {
            StringBuffer serviceName = new StringBuffer();
            StringBuffer goodsName = new StringBuffer();
            for (AppointServiceVo appointServiceVo : appointServiceVoList) {
                if(appoint.getId().longValue()==appointServiceVo.getAppointId().longValue()){
                    serviceName.append(appointServiceVo.getServiceName()).append(";");
                    if (!CollectionUtils.isEmpty(serviceGoodsSuiteList)) {
                        for (ServiceGoodsSuite serviceGoodsSuite : serviceGoodsSuiteList) {
                            if(appointServiceVo.getServiceId().longValue()==serviceGoodsSuite.getServiceId().longValue()){
                                String goodsInfo = serviceGoodsSuite.getGoodsInfo();
                                if (!StringUtils.isEmpty(goodsInfo)) {
                                    List<Goods> goodsList = new Gson().fromJson(goodsInfo, new TypeToken<List<Goods>>() {}.getType());
                                    if (!CollectionUtils.isEmpty(goodsList)) {
                                        for (Goods goods : goodsList) {
                                            goodsName.append(goods.getName()).append(";");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(serviceName)) {
                appoint.setServiceName(serviceName.deleteCharAt(serviceName.length() - 1).toString());//设置预约单的项目名称
            }
            if (StringUtils.isNotEmpty(goodsName)) {
                appoint.setGoodName(goodsName.deleteCharAt(goodsName.length() - 1).toString());
            }
        }
        return appointList;
    }

    @Override
    public Result<Appoint> cancelAppoint(Long shopId, Long appointId,Long userId, String cancelReason, Long status) {
        Result<Appoint> result = appointService.cancelAppoint(shopId, appointId, userId, cancelReason, status);
        if(!result.isSuccess()){
            return result;
        }
        Appoint appoint = result.getData();
        try {
            if(status.longValue()==AppointStatusEnum.SHOP_CANCEL.getIndex().longValue()
                    ||status.longValue()==AppointStatusEnum.CHEZHU_CANCEL.getIndex().longValue()){
                String msgType = AppointStatusEnum.getPushMsgTypeByStatus(status.intValue());
                appointService.pushMsg(msgType,appoint);
            }
        } catch (Exception e) {
            log.error("门店取消推送消息到2C-APP MQ异常!" + e);
        }
        return Result.wrapSuccessfulResult(appoint);
    }

    @Override
    public Result<Appoint> cancelAppointByOuter(AppointAppVo appointAppVo) {
        Long appointId = appointAppVo.getId();
        String cancelReason = appointAppVo.getCancelReason();
        Long status = appointAppVo.getStatus();
        Result<Appoint> result = cancelAppoint(null, appointId, null, cancelReason, status);
        if(!result.isSuccess()){
            log.info("预约单{}取消预约单失败" ,appointAppVo, result.getErrorMsg());
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
        try{
            //获得SA电话,然后发送短信
            Appoint appoint = appointService.selectById(appointId);
            if (null != appoint) {
                appointAppVo.setAppointSn(appoint.getAppointSn());
                Long shopId = appoint.getShopId();
                Shop shop = shopService.selectById(shopId);
                if (null != shop) {
                    appointAppVo.setUserGlobalId(shop.getUserGlobalId());
                    //发送短信给SA
                    appointService.sendMsgToSA(appointAppVo, Constants.APP_APPOINT_CANCEL_SMS_TPL);
                }
            }
        } catch (Exception e){
            log.error("预约单{}取消成功,发短信给SA失败",appointAppVo);
        }
        log.info("取消预约单成功!" + result.getData());
        return Result.wrapSuccessfulResult(result.getData());
    }

    @Override
    public Result createAppointByShopWeb(Appoint appoint, List<AppointServiceVo> appointServicesList) {
        if(AppointChannelEnum.WEB.getChannelId().longValue()!=appoint.getChannel()){
            log.error("渠道编号为{}不能使用此方式新建预约单",appoint.getChannel());
            return Result.wrapErrorResult("-1","渠道编号错误");
        }
        Long registrantId = appoint.getRegistrantId();
        if(registrantId!=null&&registrantId != 0){
            ShopManager shopManager = shopManagerService.selectById(registrantId);
            if (null == shopManager) {
                return Result.wrapErrorResult("", "登记人不存在");
            }
            appoint.setRegistrantName(shopManager.getName());
        }
        Long customerCarId = appoint.getCustomerCarId();
        if (null != customerCarId) {
            // 车辆信息
            CustomerCar customerCar = customerCarService.selectById(customerCarId);
            if (customerCar==null) {
                return Result.wrapErrorResult("", "客户车辆信息不存在，保存失败");
            }
            appoint.setCarBrandName(customerCar.getCarBrand());
            appoint.setCarBrandId(customerCar.getCarBrandId());
            appoint.setCarSeriesName(customerCar.getCarSeries());
            appoint.setCarSeriesId(customerCar.getCarSeriesId());
            appoint.setCarAlias(customerCar.getByName());
        }
        /**
         * 预约单原金额设置为预约单输入金额,表示没有优惠
         * 仅在门店端开放,其他渠道预约单原金额oriAppointAmount都是需要根据服务计算得出的.
         */
        appoint.setOriAppointAmount(appoint.getAppointAmount());
        Result<Appoint> result = appointService.insertAppointAndService(appoint,appointServicesList);
        return result;
    }

    @Override
    public Result updateAppointAndService(Appoint appoint, List<AppointServiceVo> appointServicesList, UserInfo userInfo) {
        Appoint oldAppoint = appointService.selectById(appoint.getId());
        if(oldAppoint == null){
            return Result.wrapErrorResult("","预约单不存在");
        }
        Long oldStatus = oldAppoint.getStatus();
        Result result = appointService.updateAppointAndService(appoint, appointServicesList, userInfo);
        if(AppointStatusEnum.TO_CONFIRM.getIndex().intValue() == oldStatus.intValue()){
            //首次预约确认推送消息给app
            try {
                //开始推送消息到2C-APP MQ
                appointService.pushMsg(Constants.APPOINT_CONFIRM, appoint);
            } catch (Exception e) {
                log.error("推送消息到2C-APP MQ异常!" + e);
            }
        }
        return result;
    }

    @Override
    public AppointDetailFacVo getAppointDetail(Long appointId, Long shopId) {
        AppointDetailFacVo appointDetailFacVo = new AppointDetailFacVo();
        if(appointId==null||shopId==null){
            return appointDetailFacVo;
        }
        //预约单信息
        AppointVo appointVo = null;
        Map<String,Object> appointSearch = Maps.newHashMap();
        appointSearch.put("id",appointId);
        appointSearch.put("shopId",shopId);
        List<AppointVo> appointVoList = getAppointVoList(appointSearch);
        if(!CollectionUtils.isEmpty(appointVoList)){
            appointVo = appointVoList.get(0);
        }
        if(appointVo==null){
            return appointDetailFacVo;
        }
        appointDetailFacVo.setAppointVo(appointVo);

        //预约服务信息
        Map<String,Object> appointSvSearch = Maps.newHashMap();
        appointSvSearch.put("shopId", shopId);
        appointSvSearch.put("appointId", appointId);
        List<AppointServiceVo> appointServiceVoList =  appointServiceService.select(appointSvSearch);
        if(CollectionUtils.isEmpty(appointServiceVoList)){
            return appointDetailFacVo;
        }

        //获取服务类目
        Map paramMap = new HashMap();
        paramMap.put("shopId", shopId);
        List<ShopServiceCate> serviceCateList = shopServiceCateService.select(paramMap);
        Map<Long,ShopServiceCate> shopServiceCateMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(serviceCateList)){
            for(ShopServiceCate shopServiceCate:serviceCateList){
                shopServiceCateMap.put(shopServiceCate.getId(),shopServiceCate);
            }
        }
        //获取服务信息
        List<Long> serviceIds = new ArrayList<Long>();
        for (AppointServiceVo appointServiceVo : appointServiceVoList) {
            Long serviceId = appointServiceVo.getServiceId();
            if (null != serviceId && 0l != serviceId) {
                serviceIds.add(appointServiceVo.getServiceId());
            }
        }
        List<ShopServiceInfo> serviceInfoList =  shopServiceInfoService.selectAllByIds(serviceIds);
        Map<Long,ShopServiceInfo> shopServiceInfoMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(serviceInfoList)){
            for(ShopServiceInfo shopServiceInfo:serviceInfoList){
                shopServiceInfoMap.put(shopServiceInfo.getId(),shopServiceInfo);
            }
        }
        //获取物料信息
        Long[] serviceIdsArray = new Long[serviceIds.size()];
        serviceIds.toArray(serviceIdsArray);//list转数组
        List<ServiceGoodsSuite> serviceGoodsSuiteList =  serviceGoodsSuiteService.selectByServiceIds(serviceIdsArray);
        Map<Long,ServiceGoodsSuite> serviceGoodsSuiteMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(serviceGoodsSuiteList)){
            for(ServiceGoodsSuite serviceGoodsSuite:serviceGoodsSuiteList){
                serviceGoodsSuiteMap.put(serviceGoodsSuite.getServiceId(),serviceGoodsSuite);
            }
        }
        //向appointServiceVo中设置categoryName,servicePrice,goodsIdStr,并统计总的物料信息totalGoodsList
        List<Goods> totalGoodsList = new ArrayList<>();
        BigDecimal totalServiceAmount = new BigDecimal(0);
        BigDecimal totalGoodsAmount = new BigDecimal(0);
        for (AppointServiceVo appointServiceVo : appointServiceVoList) {
            Long serviceId = appointServiceVo.getServiceId();
            ShopServiceInfo shopServiceInfo = shopServiceInfoMap.get(serviceId);
            if(shopServiceInfo!=null){
                appointServiceVo.setShopServiceInfo(shopServiceInfo);
                appointServiceVo.setSuiteNum(shopServiceInfo.getSuiteNum());
                totalServiceAmount = totalServiceAmount.add(appointServiceVo.getServicePrice()).subtract(appointServiceVo.getDiscountAmount());
                Long categoryId = shopServiceInfo.getCategoryId();
                ShopServiceCate shopServiceCate = shopServiceCateMap.get(categoryId);
                if(shopServiceCate!=null){
                    appointServiceVo.setCategoryName(shopServiceCate.getName());
                }
            }
            ServiceGoodsSuite serviceGoodsSuite = serviceGoodsSuiteMap.get(serviceId);
            if(serviceGoodsSuite!=null){
                String goodsJsonStr =  serviceGoodsSuite.getGoodsInfo();
                if(StringUtils.isNotEmpty(goodsJsonStr)){
                    List<Goods> goodsList = null;
                    JSONArray jsonArray = JSONArray.fromObject(goodsJsonStr);
                    goodsList = (List<Goods>)JSONArray.toCollection(jsonArray, Goods.class);
                    if(!CollectionUtils.isEmpty(goodsList)){
                        StringBuffer goodsIdSb = new StringBuffer();
                        for(Goods goods:goodsList){
                            totalGoodsList.add(goods);
                            goodsIdSb.append(goods.getId()).append(",");
                            if(goods.getGoodsAmount()!=null){
                                totalGoodsAmount = totalGoodsAmount.add(goods.getGoodsAmount());
                            }
                        }
                        if (StringUtil.isNotStringEmpty(String.valueOf(goodsIdSb))) {
                            appointServiceVo.setGoodsIdStr((goodsIdSb.deleteCharAt(goodsIdSb.length() - 1)).toString());
                        }
                    }
                }
            }
        }
        //物料信息
        appointDetailFacVo.getAppointVo().setTotalServiceAmount(totalServiceAmount);
        appointDetailFacVo.getAppointVo().setTotalGoodsAmount(totalGoodsAmount);
        appointDetailFacVo.setGoodsList(totalGoodsList);
        appointDetailFacVo.setAppointServiceVoList(appointServiceVoList);
        return appointDetailFacVo;
    }

    @Override
    public Result<String> confirmAppoint(Long appointId, Long shopId, Long userId) {
        Assert.notNull(appointId,"预约单ID不能为空");
        Map map = new HashMap();
        map.put("shopId",shopId);
        map.put("id",appointId);
        List<Appoint> appointList = appointService.select(map);
        if(CollectionUtils.isEmpty(appointList)){
            log.error("-1", "获得的appoint对象为空!");
            return Result.wrapErrorResult("-1", "获得的appoint对象为空!");
        }
        Appoint appoint = appointList.get(0);
        if(AppointStatusEnum.TO_CONFIRM.getIndex().longValue()!=appoint.getStatus()){
            String statusName = AppointStatusEnum.getNameByStatus(appoint.getStatus().intValue());
            log.error("-1", "预约单状态为{},不能确认",statusName);
            return Result.wrapErrorResult("-1", "预约单状态为"+statusName+",不能确认");
        }
        appoint.setStatus(1l);//确认预约单
        appoint.setPreviewType(1l);//预约单已处理
        appoint.setGmtModified(new Date());
        appoint.setModifier(userId);
        int count = appointService.updateById(appoint);
        if(count<1){
            log.error("确认预约单失败!appoint:{}", appoint);
            return Result.wrapErrorResult("-1", "确认预约单失败!");
        }
        try {
            appointService.pushMsg(Constants.APPOINT_CONFIRM, appoint);
        } catch (Exception e) {
            log.error("推送消息 MQ异常!" + e);
        }
        return Result.wrapSuccessfulResult("确认预约单成功");
    }

    @Override
    public Page<AppointVo> getAppointVoPage(Pageable pageable, Map<String, Object> searchParams) {
        Page<Appoint> appointPage = appointService.getPage(pageable,searchParams);

        List<AppointVo> appointVoList = appointLsit2VoList(appointPage.getContent());

        int pageNo = (pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1;
        int pageSize = pageable.getPageSize() < 1 ? 1 : pageable.getPageSize();
        PageRequest pageRequest = new PageRequest(pageNo, pageSize, pageable.getSort());
        Page<AppointVo> appointVoPage = new DefaultPage<AppointVo>(appointVoList, pageRequest, appointPage.getTotalElements());
        return appointVoPage;
    }
    private List<AppointVo> appointLsit2VoList(List<Appoint> appointList) {
        List<AppointVo> appointVoList = new ArrayList<>();
        if(CollectionUtils.isEmpty(appointList)){
            return appointVoList;
        }
        List<Long> customerCarIds = new ArrayList<>();
        for (Appoint appoint : appointList) {
            customerCarIds.add(appoint.getCustomerCarId());
        }
        Long shopId = appointList.get(0).getShopId();
        List<CarDetailVo> carDetailVoList = customerCarFacade.getSimpleCarByIds(customerCarIds, shopId);
        Map<Long,CarDetailVo> carDetailVoMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(carDetailVoList)){
            for(CarDetailVo carDetailVo:carDetailVoList){
                if(carDetailVo!=null&&carDetailVo.getCustomerCar()!=null){
                    Long carId = carDetailVo.getCustomerCar().getId();
                    carDetailVoMap.put(carId,carDetailVo);
                }
            }
        }
        for (Appoint appoint : appointList) {
            AppointVo appointVo = new AppointVo();
            BeanUtils.copyProperties(appoint, appointVo);
            CarDetailVo carDetailVo = carDetailVoMap.get(appoint.getCustomerCarId());
            if(carDetailVo!=null){
                CustomerCar customerCar = carDetailVo.getCustomerCar();
                Customer customer = carDetailVo.getCustomer();
                if (customerCar != null) {
                    appointVo.setPrecheckCount(customerCar.getPrecheckCount());
                    appointVo.setLatestPrecheck(customerCar.getLatestPrecheck());
                    appointVo.setRepairCount(customerCar.getRepairCount());
                    appointVo.setLatestRepair(customerCar.getLatestRepair());
                    appointVo.setAppointCout(customerCar.getAppointCout());
                    appointVo.setLatestAppoint(customerCar.getLatestAppoint());
                    appointVo.setCarModel(customerCar.getCarModel());
                    appointVo.setImportInfo(customerCar.getImportInfo());
                    appointVo.setCarYear(customerCar.getCarYear());
                    appointVo.setCarPower(customerCar.getCarPower());
                }
                if(customer!=null){
                    appointVo.setContact(customer.getContact());
                    appointVo.setContactMobile(customer.getContactMobile());
                }
            }
            appointVoList.add(appointVo);
        }
        return appointVoList;
    }


    @Override
    public List<AppointVo> getAppointVoList(Map<String, Object> searchParams) {
        List<Appoint> appointList = appointService.select(searchParams);
        List<AppointVo> appointVoList = appointLsit2VoList(appointList);
        return appointVoList;
    }

    /**
     * 校验预约单的预定金是否有效
     * @param appointId
     * @param downPayment
     * @return
     */
    @Override
    public boolean checkDownPaymentIsValid(Long appointId, BigDecimal downPayment) {
        //查询委托单
        Appoint appoint = appointService.selectById(appointId);
        if(appoint == null){
            return false;
        }
        BigDecimal checkDownPayment = appoint.getDownPayment();
        if(downPayment.compareTo(checkDownPayment) != 0){
            return false;
        }
        return true;
    }

    @Override
    public Map<Long, Integer> getAppointCount(Long... serviceIds) {
        Map<Long, Integer> retMap = new HashMap<>();
        if (serviceIds == null || serviceIds.length < 1) {
            return retMap;
        }
        //.查询服务
        Map<String, Object> qryServiceInfoParam = new HashMap<>();
        qryServiceInfoParam.put("ids", serviceIds);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(qryServiceInfoParam);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return retMap;
        }
        Set<Long> unsuitServiceIds = new HashSet<>();//非套餐的服务id集合
        Set<Long> suitServiceIds = new HashSet<>();//是套餐的服务id集合
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            long suitNum = shopServiceInfo.getSuiteNum() == null ? 0l : shopServiceInfo.getSuiteNum();
            if (suitNum == 0 || suitNum == 1) {
                unsuitServiceIds.add(shopServiceInfo.getId());
            } else if (suitNum == 2) {
                suitServiceIds.add(shopServiceInfo.getId());
            }
        }
        //.查询服务的预约记录,并归并
        Map<Long, Set<Long>> servcieIdAppointIdsMap = new HashMap<>();
        List<AppointServiceVo> unsuitAppointServiceVoList = appointServiceService.selectByServiceIds(0, unsuitServiceIds.toArray(new Long[]{}));
        for (AppointServiceVo appointServiceVo : unsuitAppointServiceVoList) {
            Long serviceId = appointServiceVo.getServiceId();
            Set<Long> appointIds = servcieIdAppointIdsMap.get(serviceId);
            if (appointIds == null) {
                appointIds = new HashSet<>();
            }
            appointIds.add(appointServiceVo.getAppointId());
            servcieIdAppointIdsMap.put(serviceId, appointIds);
        }
        List<AppointServiceVo> suitAppointServiceVoList = appointServiceService.selectByServiceIds(2, unsuitServiceIds.toArray(new Long[]{}));
        for (AppointServiceVo appointServiceVo : suitAppointServiceVoList) {
            Long serviceId = appointServiceVo.getParentServiceId();
            Set<Long> appointIds = servcieIdAppointIdsMap.get(serviceId);
            if (appointIds == null) {
                appointIds = new HashSet<>();
            }
            appointIds.add(appointServiceVo.getAppointId());
            servcieIdAppointIdsMap.put(serviceId, appointIds);
        }
        //.组装结果
        for (Long serviceId : serviceIds) {
            Set<Long> appointIds = servcieIdAppointIdsMap.get(serviceId);
            if (appointIds == null) {
                retMap.put(serviceId, 0);
            } else {
                retMap.put(serviceId, appointIds.size());
            }
        }
        return retMap;
    }

    @Override
    public Appoint getAppointByOrderIdAndShopId(Long orderId, Long shopId) {
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("orderId",orderId);
        searchMap.put("shopId",shopId);
        List<Appoint> appointList = appointService.select(searchMap);
        if(CollectionUtils.isEmpty(appointList)){
            return null;
        }
        return appointList.get(0);
    }

    @Override
    public Appoint getAppointById(Long id) {
        Appoint appoint = appointService.selectById(id);
        return appoint;
    }
}
