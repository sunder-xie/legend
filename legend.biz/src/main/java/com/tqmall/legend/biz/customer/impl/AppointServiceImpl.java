package com.tqmall.legend.biz.customer.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.dandelion.service.car.PgyCarService;
import com.tqmall.dandelion.service.user.PgyUserService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.customer.AppointServiceService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.jms.ddlwechat.MessagePlatformDdlWechat;
import com.tqmall.legend.biz.jms.yunxiu.MessagePlatformCApp;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ServiceGoodsSuiteService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.customer.AppointDao;
import com.tqmall.legend.dao.customer.AppointServiceDao;
import com.tqmall.legend.dao.customer.AppointVoDao;
import com.tqmall.legend.dao.customer.CustomerCarDao;
import com.tqmall.legend.dao.customer.CustomerContactDao;
import com.tqmall.legend.dao.customer.CustomerDao;
import com.tqmall.legend.dao.shop.ServiceGoodsSuiteDao;
import com.tqmall.legend.dao.shop.ServiceTemplateDao;
import com.tqmall.legend.dao.shop.ShopServiceInfoDao;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.AppointAppVo;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.ServiceGoodsSuite;
import com.tqmall.legend.entity.shop.ServiceTemplate;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.appoint.AppointChannelEnum;
import com.tqmall.legend.enums.appoint.AppointPayStatusEnum;
import com.tqmall.legend.object.enums.appoint.AppointStatusEnum;
import com.tqmall.legend.rpc.crm.CrmCustomerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2015-01-14 11:03
 */
@Service
public class AppointServiceImpl extends BaseServiceImpl implements AppointService {
    Logger logger = LoggerFactory.getLogger(AppointServiceImpl.class);

    @Autowired
    AppointDao appointDao;

    @Autowired
    AppointVoDao appointVoDao;

    @Autowired
    AppointServiceDao appointServiceDao;

    @Autowired
    private AppointServiceService appointServiceService;
    @Autowired
    CustomerCarService customerCarService;

    @Autowired
    ShopManagerService shopManagerService;

    @Autowired
    private ShopService shopService;
    @Autowired
    CustomerCarDao customerCarDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    CustomerContactDao customerContactDao;

    @Autowired
    ShopServiceInfoDao shopServiceInfoDao;

    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    ServiceGoodsSuiteDao serviceGoodsSuiteDao;
    @Autowired
    private ServiceGoodsSuiteService serviceGoodsSuiteService;
    @Autowired
    ServiceTemplateDao serviceTemplateDao;
    @Autowired
    private MessagePlatformCApp cAppMp;
    @Autowired
    private CacheComponent<Map<Long, Integer>> cacheComponent;
    @Autowired
    private PgyUserService pgyUserService;
    @Autowired
    private PgyCarService pgyCarService;
    @Autowired
    private MessagePlatformDdlWechat ddlwechatMp;
    @Autowired
    private SnFactory snFactory;
    @Resource
    private SmsService smsService;
    @Autowired
    private CrmCustomerService crmCustomerService;
    /**
     * create by jason 2015-07-20
     * 新增预约单和预约单服务关系
     */
    @Override
    public Result addAppointAndService(Appoint appoint, List<AppointServiceVo> list) {

        logger.info("新增预约单和服务的关系!AppointService List:{}", list);
        Long customerCarId = appoint.getCustomerCarId();
        if (null != customerCarId) {
            // 车辆信息
            Optional<CustomerCar> customerCarOptional = customerCarService.getCustomerCar(customerCarId);
            if (!customerCarOptional.isPresent()) {
                return Result.wrapErrorResult("", "车牌信息不存在，保存失败");
            }
            CustomerCar customerCar = customerCarOptional.get();
            appoint.setCarBrandName(customerCar.getCarBrand());
            appoint.setCarBrandId(customerCar.getCarBrandId());
            appoint.setCarSeriesName(customerCar.getCarSeries());
            appoint.setCarSeriesId(customerCar.getCarSeriesId());
            appoint.setCarAlias(customerCar.getByName());
        }

        Long registrantId = appoint.getRegistrantId();
        if(registrantId != 0){
            ShopManager shopManager = shopManagerService.selectById(registrantId);
            if (null == shopManager) {
                return Result.wrapErrorResult("", "登记人不存在");
            } else {
                appoint.setRegistrantName(shopManager.getName());
            }
        }


        //门店预约 status = 1 确认预约
        appoint.setStatus(1l);
        //渠道来源
        if(registrantId != 0){
            appoint.setChannel(0l);//0 门店web, 1 商家app, 2 车主app, 3 车主微信, 4 橙牛 5滴滴
        }else {
            appoint.setChannel(9l);//0 门店web, 1 商家app, 2 车主app, 3 车主微信, 4 橙牛 5滴滴 9云修系统客服
        }

        appointDao.insert(appoint);
        logger.info("创建预约单成功!appoint对象:{}", appoint);
        if (!CollectionUtils.isEmpty(list)) {
            for (AppointServiceVo as : list) {
                if (null != as.getServiceId() && 0l != as.getServiceId()) {
                    as.setAppointId(appoint.getId());
                    as.setAppointSn(appoint.getAppointSn());
                    as.setShopId(appoint.getShopId());
                    as.setCreator(appoint.getCreator());
                    Integer cnt = appointServiceDao.insert(as);
                    if (cnt <= 0) {
                        //创建预约单服务失败
                        return Result.wrapErrorResult("-1", "添加失败");
                    }
                }
            }
        }
        try {
            //开始推送消息到2C-APP MQ
            pushMsg(Constants.APPOINT_CONFIRM, appoint);
        } catch (Exception e) {
            logger.error("推送消息到2C-APP MQ异常!",e);
        }

        return Result.wrapSuccessfulResult(true);
    }

    public void pushMsg(String type, Appoint appoint) {
        if(appoint==null || appoint.getShopId()==null){
            return;
        }
        Long shopId = appoint.getShopId();
        Shop shop = shopService.selectById(shopId);
        if(shop==null){
            throw new BizException("预约单对应的门店不存在");
        }
        Map<String, String> message = new HashMap<>();
        message.put("type", type);
        message.put("appointId", String.valueOf(appoint.getId()));//预约ID
        message.put("appointTime", appoint.getAppointTimeFormat());//预约时间
        message.put("appointContent", appoint.getAppointContent());//预约内容
        message.put("shopName", shop.getName());//店铺名称
        message.put("shopAddress", shop.getAddress());//店铺地址
        message.put("shopTel", shop.getTel());//固话
        message.put("shopMobile", shop.getMobile());//手机号
        message.put("contactMobile", appoint.getMobile());//联系人手机号
        message.put("channel", appoint.getChannel().toString());//
        message.put("appointSn", appoint.getAppointSn());//预约单号
        if(appoint.getCustomerCarId()!=null){
            CustomerCar customerCar= customerCarService.selectById(appoint.getCustomerCarId());
            if(customerCar!=null){
                message.put("carLicense", customerCar.getLicense());//车牌号
            }
        }
        if (Constants.APPOINT_CREATE.equals(type)) {
            cAppMp.pushMsgToCApp(message);
        } else if (Constants.APPOINT_CONFIRM.equals(type)) {
            cAppMp.pushMsgToCApp(message);
            //wechatMp.pushMsgToWechat(message);
            ddlwechatMp.pushMsgToDdlWechat(message, shopId);
        } else if (Constants.APPOINT_B_CANCEL.equals(type)) {
            message.put("cancelReason", appoint.getCancelReason());//取消原因
            cAppMp.pushMsgToCApp(message);
            //wechatMp.pushMsgToWechat(message);
            ddlwechatMp.pushMsgToDdlWechat(message, shopId);
        } else if (Constants.APPOINT_C_CANCEL.equals(type)) {
            message.put("cancelReason", appoint.getCancelReason());//取消原因
            cAppMp.pushMsgToCApp(message);
            ddlwechatMp.pushMsgToDdlWechat(message, shopId);
        }
    }


    /**
     * create by jason 2015-07-20
     * 新增来自APP预约单和预约单服务关系
     */
    @Override
    public Result addAppointApp(AppointAppVo appoint) {
        logger.info("新增预约单和服务的关系! license:" + appoint.getLicense() + " mobile:" + appoint.getMobile() +
                " userGlobalId:" + appoint.getUserGlobalId() + " channel:" + appoint.getChannel());
        Long shopId = appoint.getShopId();
        if(shopId == null){
            Map map = new HashMap(2);
            String userGlobalId = appoint.getUserGlobalId();
            map.put("userGlobalId", userGlobalId);
            List<Shop> shopList = shopService.select(map);
            if (!CollectionUtils.isEmpty(shopList)) {
                shopId = shopList.get(0).getId();
                appoint.setShopId(shopId);
                //创建客户信息
                try {
                    if (!StringUtils.isEmpty(appoint.getLicense())) {
                        Result result = customerCarService.addOrUpdate(appoint);
                        if (result.isSuccess()) {
                            if (null != result.getData()) {
                                CustomerCar customerCar = (CustomerCar)result.getData();
                                appoint.setCustomerCarId(customerCar.getId());
                            }
                        } else {
                            logger.warn("创建客户信息失败:{}", result.getErrorMsg());
                            return Result.wrapErrorResult("", result.getErrorMsg());
                        }
                    }
                } catch (Exception e) {
                    logger.error("创建或修改客户信息异常!" + e);
                }

            } else {
                logger.warn("根据USER_GLOBAL_ID:{}找不到店铺", userGlobalId);
                return Result.wrapErrorResult("", " 根据USER_GLOBAL_ID找不到店铺!");
            }
        }
        //设置预约单号appointSn
        if (StringUtils.isBlank(appoint.getAppointSn())) {
            String appointSn = snFactory.generateAppointSn(shopId);
            appoint.setAppointSn(appointSn);
        }

        Long serviceId = appoint.getServiceId();
        ShopServiceInfo shopServiceInfo = null;
        //set 预约服务内容
        BigDecimal oriAppointAmount = new BigDecimal("0.00");//预约单原价,默认0.00
        if (null != serviceId && 0l != serviceId) {
            logger.info("预约单服务ID是:{}", serviceId);
            Long[] ids = new Long[1];
            ids[0] = serviceId;
            List<ShopServiceInfo> shopServiceInfos = shopServiceInfoDao.selectAllByIds(ids);
            if (CollectionUtils.isEmpty(shopServiceInfos)) {
                return Result.wrapErrorResult("", "找不到该服务");
            }
            shopServiceInfo = shopServiceInfos.get(0);
            if (null == shopServiceInfo) {
                return Result.wrapErrorResult("", "找不到该服务");
            }
            //TQFW CZFW下架用status判断
            String flags = shopServiceInfo.getFlags();
            Long status = shopServiceInfo.getStatus();
            if ("CZFW".equals(flags)) {
                if (null == status || status == -2l) {
                    return Result.wrapErrorResult("7004", "该车主服务已经下架!");
                }
            } else if ("TQFW".equals(flags)) {
                //Long status = shopServiceInfo.getStatus();
                if (null == status || status == -1l) {
                    return Result.wrapErrorResult("7004", "该淘汽服务已经下架!");
                }
            }
            String serviceNote = shopServiceInfo.getServiceNote();
            BigDecimal servicePrice = shopServiceInfo.getServicePrice()==null?new BigDecimal("0.00"):shopServiceInfo.getServicePrice();
            appoint.setAppointContent(serviceNote);//服务描述
            appoint.setTemplateId(shopServiceInfo.getParentId());//服务模板id
            if (shopServiceInfo.getSuiteNum() != null && shopServiceInfo.getSuiteNum() > 0) {
                //服务套餐
                ServiceGoodsSuite serviceGoodsSuite = serviceGoodsSuiteDao.selectByServiceId(serviceId);
                if (null != serviceGoodsSuite) {
                    oriAppointAmount = serviceGoodsSuite.getSuitePrice();
                }
            } else {
                oriAppointAmount = servicePrice;
            }
        }
        BigDecimal inputAppointAmount = appoint.getAppointAmount();
        BigDecimal totalDiscount = new BigDecimal("0.00");//优惠金额,默认0.00
        if (inputAppointAmount==null){
            appoint.setAppointAmount(oriAppointAmount);//服务价格
        } else {
            appoint.setAppointAmount(inputAppointAmount);
            totalDiscount = oriAppointAmount.subtract(inputAppointAmount,new MathContext(2,RoundingMode.HALF_UP));
        }
        Appoint newAppoint = new Appoint();
        BeanUtils.copyProperties(appoint, newAppoint);
        String byName = appoint.getByName();//车辆别名
        if (StringUtils.isNotEmpty(byName)) {
            newAppoint.setCarAlias(byName);
        }
        if(appoint.isNeedDownPay()){
            newAppoint.setPayStatus(0);//支付状态，0支付失败(或未支付)，1支付成功
        }
        appointDao.insert(newAppoint);
        Long appointId = newAppoint.getId();
        logger.info("创建预约单成功! 预约单对象:{}", newAppoint);
        appoint.setId(newAppoint.getId());
        //新增预约服务信息
        _addAppointService(appoint, shopServiceInfo ,totalDiscount);
        //customer_car表中预约统计信息修改
        try {
            updateCustomerCar(newAppoint);
        } catch (Exception e) {
            logger.info("更新客户车辆信息异常!" + e);
        }
        if (!appoint.isNeedDownPay()) {
            //不需要支付的预约单创建的时候就处理消息推送
            try {
                //商家app和易车过来的预约单不用推送消息
                if (newAppoint.getChannel().longValue() != AppointChannelEnum.WEB.getChannelId()
                        && newAppoint.getChannel().longValue() != AppointChannelEnum.YXCL.getChannelId()) {
                    pushMsg(Constants.APPOINT_CREATE, newAppoint);
                }
                if (newAppoint.getChannel()!=null && AppointChannelEnum.MACE.getChannelId()==newAppoint.getChannel().intValue()) {
                    //商家app创建的预约单状态为1.已确认,直接发送预约单确认消息
                    pushMsg(Constants.APPOINT_CONFIRM, newAppoint);
                }
            } catch (Exception e) {
                logger.error("推送消息到MQ异常!" + e);
            }
        }
        return Result.wrapSuccessfulResult(appointId);
    }

    private void _addAppointService(AppointAppVo appoint, ShopServiceInfo shopServiceInfo,BigDecimal totalDiscount) {
        if (shopServiceInfo==null){
            return;
        }
        Long appointId = appoint.getId();
        Long serviceId = shopServiceInfo.getId();
        Long shopId = appoint.getShopId();
        Long suiteNum = shopServiceInfo.getSuiteNum();
        List<AppointServiceVo> appointServiceVoList = new ArrayList<>();
        if (suiteNum == 0l || suiteNum == 1l) {//基本服务
            AppointServiceVo appointService = new AppointServiceVo();
            appointService.setAppointId(appointId);
            appointService.setAppointSn(appoint.getAppointSn());
            appointService.setServiceId(appoint.getServiceId());
            appointService.setTemplateId(appoint.getTemplateId());
            appointService.setServiceName(shopServiceInfo.getName());
            appointService.setServiceNote(shopServiceInfo.getServiceNote());
            appointService.setShopId(shopId);
            appointService.setDiscountAmount(totalDiscount);
            appointService.setServicePrice(shopServiceInfo.getServicePrice());
            appointServiceVoList.add(appointService);
        } else if (suiteNum == 2l) {//服务套餐
            ServiceGoodsSuite serviceGoodsSuite = serviceGoodsSuiteDao.selectByServiceId(serviceId);
            if (serviceGoodsSuite==null) {
                logger.error("预约套餐服务不存在,套餐服务id:{}",serviceId);
                throw new BizException("查询不到服务套餐");
            }
            String serviceInfo = serviceGoodsSuite.getServiceInfo();
            //套餐跟服务的关系
            List<ShopServiceInfo> shopServiceMapList = new Gson().fromJson(serviceInfo, new TypeToken<List<ShopServiceInfo>>() {
            }.getType());
            if (!CollectionUtils.isEmpty(shopServiceMapList)) {
                BigDecimal totalServicePrice = new BigDecimal("0.00");
                for (ShopServiceInfo info : shopServiceMapList) {
                    BigDecimal servicePrice = info.getServicePrice()==null?new BigDecimal("0.00"):info.getServicePrice();
                    totalServicePrice = totalServicePrice.add(servicePrice);
                }
                BigDecimal remainDiscount = totalDiscount;//总优惠的剩余值
                for (int i = 0; i < shopServiceMapList.size(); i++) {
                    BigDecimal serviceAmount = shopServiceMapList.get(i).getServiceAmount() == null ? new BigDecimal("0.00") : shopServiceMapList.get(i).getServiceAmount();
                    AppointServiceVo appointService = new AppointServiceVo();
                    appointService.setAppointId(appointId);
                    appointService.setAppointSn(appoint.getAppointSn());
                    appointService.setServiceName(shopServiceMapList.get(i).getName());
                    appointService.setServiceId(shopServiceMapList.get(i).getId());//服务ID
                    appointService.setParentServiceId(appoint.getServiceId());//父服务ID(服务套餐id)
                    appointService.setShopId(shopId);
                    appointService.setServiceNote(shopServiceMapList.get(i).getServiceNote());
                    appointService.setServicePrice(serviceAmount);
                    if (totalServicePrice.compareTo(BigDecimal.ZERO) <= 0) {
                        //服务原总价为0,优惠总金额(若有优惠此时一定为负值)全部设置到第一个服务优惠金额上
                        if(i==0){
                            appointService.setDiscountAmount(remainDiscount);
                            remainDiscount = remainDiscount.subtract(totalDiscount);
                        }
                    } else {
                        //按照原价价格加权平均折扣金额到每个服务上
                        if (i == (shopServiceMapList.size() - 1)) {
                            //最后一项直接取扣减金额的剩余部分
                            appointService.setDiscountAmount(remainDiscount.setScale(2,BigDecimal.ROUND_HALF_UP));
                        } else {
                            //原价价格加权平均折扣 = 总折扣*当前服务原价/所有服务总原价  ###四舍五入后保留两位小数
                            BigDecimal discount = totalDiscount.multiply(serviceAmount).divide(totalServicePrice, 2, BigDecimal.ROUND_HALF_UP);
                            remainDiscount = totalDiscount.subtract(discount);
                            appointService.setDiscountAmount(discount);
                        }
                    }
                    appointServiceVoList.add(appointService);
                }
            }
        }
        if (!CollectionUtils.isEmpty(appointServiceVoList)) {
            //优惠金额正值化处理(负优惠取绝对值加到服务原价上,优惠金额归0)
            _absDiscount(appointServiceVoList);
            appointServiceDao.batchInsert(appointServiceVoList);
        }
    }

    /**
     * 优惠金额正值化处理(负优惠取绝对值加到服务原价上,优惠金额归0)
     * @param appointServiceVoList
     */
    private void _absDiscount(Collection<AppointServiceVo> appointServiceVoList) {
        for (AppointServiceVo appointServiceVo : appointServiceVoList) {
            BigDecimal discount = appointServiceVo.getDiscountAmount() == null ? new BigDecimal("0.00") : appointServiceVo.getDiscountAmount();
            if(discount.compareTo(BigDecimal.ZERO)<0){
                BigDecimal servicePrice = appointServiceVo.getServicePrice() == null ? new BigDecimal("0.00") : appointServiceVo.getServicePrice();
                servicePrice = servicePrice.add(discount.abs()).setScale(2,BigDecimal.ROUND_HALF_UP);
                appointServiceVo.setServicePrice(servicePrice);
                appointServiceVo.setDiscountAmount(new BigDecimal("0.00"));
            }
        }
    }

    /**
     * create by jason 2015-07-20
     * 更新预约单和预约单服务关系
     */
    @Transactional
    @Override
    public Result updateAppointAndService(Appoint appoint, List<AppointServiceVo> list, UserInfo userInfo) {
        Long appointId = appoint.getId();
        Appoint oldAppoint = appointDao.selectById(appointId);
        if(oldAppoint==null){
            logger.error("预约单appointId{}不存在,不能编辑",appointId);
            return Result.wrapErrorResult("-1", "预约单不存在,不能编辑");
        }
        Long oldStatus = oldAppoint.getStatus();
        if(oldStatus!=AppointStatusEnum.TO_CONFIRM.getIndex().longValue() && oldStatus!=AppointStatusEnum.APPOINT_SUCCESS.getIndex().longValue()){
            String statusName = AppointStatusEnum.getNameByStatus(oldStatus.intValue());
            logger.error("预约单appointId{}状态为{},不能进行编辑",appointId,statusName);
            return Result.wrapErrorResult("-1", "预约单状态为"+statusName+",不能进行编辑");
        }
        Long registrantId = appoint.getRegistrantId();
        ShopManager shopManager = shopManagerService.selectById(registrantId);
        if (null == shopManager) {
            return Result.wrapErrorResult("", "登记人不存在");
        } else {
            appoint.setRegistrantName(shopManager.getName());
        }
        //若更新预约时间,预约时间不能小于当前时间
        if(appoint.getAppointTime().getTime()-System.currentTimeMillis()<=0){
            logger.info("appointSn:{}更新,预约时间{}小于当前时间,不能保存",appoint.getAppointSn(),appoint.getAppointTime());
            return Result.wrapErrorResult("-1", "预约时间小于当前时间,不能保存");
        }
        appoint.setGmtModified(new Date());//修改时间
        appoint.setModifier(userInfo.getUserId());
        Integer flag = appointDao.updateById(appoint);
        if (flag > 0) {

            Map map = new HashMap();
            map.put("shopId", userInfo.getShopId());
            map.put("appointId", appoint.getId());
            //当前数据库中得appoint service
            List<AppointServiceVo> oldAppointServoceList = appointServiceDao.select(map);

            Set<Long> newAppointServiceIds = new HashSet<>();
            /*Set<Long> oldAppointServiceIds = new HashSet<>();
            if(!CollectionUtils.isEmpty(oldAppointServoceList)){
                for(AppointServiceVo old: oldAppointServoceList){
                    oldAppointServiceIds.add(old.getId());
                }
            }*/

            if (!CollectionUtils.isEmpty(list)) {
                for (AppointServiceVo as : list) {
                    Long serviceId = as.getServiceId();
                    Long id = as.getId();
                    if (null == serviceId || 0l == serviceId) { //服务ID为空,说明这是无效数据
                        continue;
                    }
                    if (null == id) {//ID为空,服务ID不为空,这是新增的数据
                        //新增的
                        as.setAppointId(appoint.getId());
                        as.setAppointSn(oldAppoint.getAppointSn());
                        as.setShopId(userInfo.getShopId());
                        as.setCreator(userInfo.getUserId());
                        appointServiceDao.insert(as);

                    } else {//ID不为空,服务ID不为空,这是更新的数据
                        //更新的
                        as.setModifier(userInfo.getUserId());
                        as.setGmtModified(new Date());
                        appointServiceDao.updateById(as);
                        newAppointServiceIds.add(as.getId());
                    }
                }
            }

            if (!CollectionUtils.isEmpty(oldAppointServoceList)) {
                Set<Long> deleteIds = Sets.newHashSet();
                //原有的appointService不在新appointService中的需要删除
                for (AppointServiceVo oldService : oldAppointServoceList) {
                    if (newAppointServiceIds.isEmpty() || (!newAppointServiceIds.isEmpty() && !newAppointServiceIds.contains(oldService.getId()))) {
                        deleteIds.add(oldService.getId());
                    }
                }
                if(!CollectionUtils.isEmpty(deleteIds)){
                    Long[] deleteIdsArray = new Long[deleteIds.size()];
                    deleteIds.toArray(deleteIdsArray);//list转数组
                    appointServiceDao.deleteByIds(deleteIdsArray);
                }
            }
            return Result.wrapSuccessfulResult(true);
        } else {
            return Result.wrapErrorResult("-1", "更新失败");
        }
    }

    @Transactional
    @Override
    public Result update(Appoint appoint) {
        appointDao.updateById(appoint);
        return Result.wrapSuccessfulResult(appoint.getId());
    }

    @Override
    public Appoint selectById(Long id) {
        return appointDao.selectById(id);
    }

    /**
     * create by jason 2015-08-05
     * 确认预约单
     */
    public Result confirmAppoint(Map<String, Object> map) {
        logger.info("确认预约单参数:{}", map);
        try {
            List<Appoint> appointList = appointDao.select(map);
            if (!CollectionUtils.isEmpty(appointList)) {
                Appoint appoint = appointList.get(0);
                if (null != appoint) {
                    appoint.setStatus(1l);//确认预约单
                    appoint.setPreviewType(1l);//预约单已处理
                    appoint.setGmtModified(new Date());
                    Integer cnt = appointDao.updateById(appoint);
                    if (cnt > 0) {

                        try {
                            pushMsg(Constants.APPOINT_CONFIRM, appoint);
                        } catch (Exception e) {
                            logger.error("推送消息 MQ异常!" + e);
                        }
                        return Result.wrapSuccessfulResult("确认预约单成功");
                    } else {
                        logger.info("确认预约单失败!appoint:{}", appoint);
                        return Result.wrapErrorResult("-1", "确认预约单失败!");
                    }
                } else {
                    //根据预约单ID获得的appoint对象为空
                    return Result.wrapErrorResult("-1", "获得的appoint对象为空!");
                }
            } else {
                return Result.wrapErrorResult("-1", "获得的appoint对象为空!");
            }
        } catch (Exception e) {
            logger.error("确认预约单异常!" + e);
            return Result.wrapErrorResult("-1", "确认预约单异常!");
        }

    }

    @Override
    public List<Appoint> select(Map<String, Object> params) {
        return appointDao.select(params);
    }


    @Override
    public Integer selectCount(Map<String, Object> params) {
        return appointDao.selectCount(params);
    }


    @Override
    public Integer deleteById(Long id) {
        Integer result = appointDao.deleteById(id);
        if (result > 0) {
            try {
                Map map = new HashMap(1);
                map.put("appointId", id);
                List<AppointServiceVo> appointServiceVo = appointServiceDao.select(map);
                if (!CollectionUtils.isEmpty(appointServiceVo)) {
                    for (AppointServiceVo as : appointServiceVo) {
                        appointServiceDao.deleteById(as.getId());//删除预约单对应的服务
                    }
                }

            } catch (Exception e) {
                logger.error("删除预约单服务信息异常! " + e);

            }
        }

        return result;
    }


    @Override
    public Page<Appoint> getPage(Pageable pageable, Map<String, Object> searchParams) {
        Page<Appoint> page = super.getPage(appointDao, pageable, searchParams);
        return page;
    }


    /**
     * 柯昌强 2015-12-28
     * 新增来自橙牛的预约单和预约单服务
     */
    @Transactional
    @Override
    public Result addAppoint(AppointAppVo appoint){
        Long templateId = appoint.getTemplateId();//服务模版id
        ServiceTemplate serviceTemplate = serviceTemplateDao.selectById(templateId);//服务模版
        if (serviceTemplate == null) {
            logger.info("不存在服务模版id对应的服务模版!服务模版id:{}", appoint.getTemplateId());
            return Result.wrapErrorResult("", "不存在对应的该服务模版！");
        }
        if (serviceTemplate.getStatus() == -1) {
            logger.info("服务模版id对应的服务模版已下架!服务模版id:{}", appoint.getTemplateId());
            return Result.wrapErrorResult("", "服务模版已下架！");
        }
        if (!serviceTemplate.getFlags().equals("TQFW")) {
            logger.info("服务模版id对应的服务模版不是淘汽服务!服务模版id:{}", appoint.getTemplateId());
            return Result.wrapErrorResult("", "服务模版对应的不是淘汽服务！");
        }
        //创建预约单
        Appoint newAppoint = new Appoint();
        BeanUtils.copyProperties(appoint, newAppoint);
        //生成预约单编码
        String appointSn = snFactory.generateAppointSn(0l);//未生成门店，门店设为0
        newAppoint.setAppointSn(appointSn);
        newAppoint.setAppointAmount(serviceTemplate.getServicePrice());
        appointDao.insert(newAppoint);
        Long appointId = newAppoint.getId();//预约单id
        //创建预约单服务
        AppointServiceVo appointServiceVo = new AppointServiceVo();
        appointServiceVo.setAppointId(appointId);
        appointServiceVo.setAppointSn(appoint.getAppointSn());
        appointServiceVo.setServiceName(serviceTemplate.getName());
        appointServiceVo.setServiceNote(serviceTemplate.getServiceNote());
        appointServiceVo.setTemplateId(templateId);

        int num = appointServiceDao.insert(appointServiceVo);//插入预约单服务条数
        if (num <= 0) {
            logger.info("创建预约单服务失败! appointSn:{},templateId:{}", appoint.getAppointSn(), appointServiceVo.getTemplateId());
            return Result.wrapErrorResult("-1", "创建预约单服务失败!");
        }
        return Result.wrapSuccessfulResult(appointId);
    }

    /**
     * 推送消息至cube
     *
     * @param type
     * @param appoint
     * @return
     */
    @Override
    public Result pushMsgToCube(String type, Appoint appoint) {
        //TODO 此方法没有返回值，建议改造下，目前只能先返回true
        pushMsg(type,appoint);
        return Result.wrapSuccessfulResult("发送成功");
    }

    /**
     * 夏日活动预约限制检查
     * @param mobile
     * @param serviceId
     * @param shopId
     * @return 可以预约返回true
     */
    private boolean appointLimitCheck(String mobile, Long serviceId, Long shopId){
        //根据手机号查询预约单
        Map paras = Maps.newHashMap();
        paras.put("mobile",mobile);
        List<String> sorts = new ArrayList<>();
        sorts.add("gmtCreate desc");
        paras.put("sorts", sorts);
        paras.put("shopId", shopId);
        List<Appoint> appointList = appointDao.select(paras);
        if(CollectionUtils.isEmpty(appointList)||appointList.get(0)==null){
            return true;
        }
        Appoint appoint = appointList.get(0);
        int between = 0;
        try{
            between = DateUtil.daysBetween(appoint.getGmtCreate(),new Date());
        }catch (Exception e){
            logger.error("日期比较异常",e);
        }
        if(between!=0){
            return true;
        } else {
            //查询预约服务
            Map searchMap = Maps.newHashMap();
            searchMap.put("appointId", appoint.getId());
            searchMap.put("serviceId",serviceId);
            searchMap.put("shopId",shopId);
            List<AppointServiceVo> appointSvList = appointServiceDao.select(searchMap);
            if (CollectionUtils.isEmpty(appointSvList) || appointSvList.get(0) == null) {
                return true;
            }
            return false;
        }
    }


    //customer_car表中预约统计信息修改
    private void updateCustomerCar(Appoint newAppoint) {
        String license = newAppoint.getLicense();
        Long shopId = newAppoint.getShopId();
        if (StringUtil.isNotStringEmpty(license) && StringUtil.isNotStringEmpty(shopId + "")) {
            CustomerCar customerCar = customerCarService.selectByLicenseAndShopId(license, shopId);
            if (null != customerCar) {
                customerCar.setLatestAppoint(new Date());
                customerCar.setAppointCout(customerCar.getAppointCout() + 1);
                customerCar.setShopId(shopId);
                customerCar.setLicense(license);
                customerCarService.update(customerCar);
            }
        }
    }





    @Override
    public Result<Appoint> cancelAppoint(Long shopId,Long appointId,Long userId, String cancelReason, Long status) {
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId",shopId);
        searchMap.put("id",appointId);
        List<Appoint> appointList = appointDao.select(searchMap);
        if(CollectionUtils.isEmpty(appointList)){
            logger.error("取消预约,根据预约单ID和shopId获得不到预约单数据!id:{},shopId:{}", appointId,shopId);
            return Result.wrapErrorResult("-1", "根据预约单ID获取不到预约单信息");
        }
        Appoint appoint = appointList.get(0);

        Long oldStatus = appoint.getStatus();
        if(oldStatus!= AppointStatusEnum.TO_CONFIRM.getIndex().longValue()
                &&oldStatus!=AppointStatusEnum.APPOINT_SUCCESS.getIndex().longValue()){
            logger.info("取消预约单{}失败,原因是该预约单状态为:{}", appoint,AppointStatusEnum.getNameByStatus(oldStatus.intValue()));
            return Result.wrapErrorResult("-1", "取消预约单失败,原因是:"+AppointStatusEnum.getNameByStatus(oldStatus.intValue()));
        }
        appoint.setStatus(status);//取消预约单 3 车主取消, 4 门店取消 ,5 微信端取消
        appoint.setGmtModified(new Date());
        appoint.setCancelReason(cancelReason);//取消原因
        appoint.setPreviewType(1l);//预约单已处理
        appoint.setModifier(userId);
        Integer cnt = appointDao.updateById(appoint);
        if(cnt==null||cnt<1){
            logger.info("取消预约单失败!appoint:{}", appoint);
            return Result.wrapErrorResult("-1", "取消预约单失败!");
        }
        return Result.wrapSuccessfulResult(appoint);
    }


    @Transactional
    @Override
    public Result<Appoint> insertAppointAndService(Appoint appoint, List<AppointServiceVo> appointServicesVoList) {
        //入参非空检查
        if(appoint==null){
            logger.error("预约单不能为空");
            return Result.wrapErrorResult("-1", "预约单为空,创建预约单失败");
        }
        if(appoint.getChannel()==null){
            logger.error("预约单渠道不能为空,appoint:{}",LogUtils.objectToString(appoint));
            return Result.wrapErrorResult("-1", "预约单渠道为空,创建预约单失败");
        }
        AppointChannelEnum appointChannel = AppointChannelEnum.getAppointChannelEnum(appoint.getChannel().intValue());
        if(appointChannel==null){
            logger.error("预约单渠道未知,appoint:{}",LogUtils.objectToString(appoint));
            return Result.wrapErrorResult("-1", "预约单渠道未知,创建预约单失败");
        }
        if(appointChannel.isCheckAppointTime()){//是否需要检查预约时间
            if(appoint.getAppointTime().getTime()-System.currentTimeMillis()<0){
                logger.info("预约单新增appointSn:{},预约时间{}小于当前时间,不能保存",appoint.getAppointSn(),appoint.getAppointTime());
                return Result.wrapErrorResult("-1", "预约时间小于当前时间,不能保存");
            }
        }
        //.预约登记人处理
        Long registrantId = appoint.getRegistrantId();
        if (registrantId != null) {
            ShopManager shopManager = shopManagerService.selectByShopIdAndManagerId(appoint.getShopId(), registrantId);
            if (shopManager != null) {
                appoint.setRegistrantName(shopManager.getName());
            }
        }
        if(appointChannel.isNeedUpdateCustomer()){//是否需要更新或创建客户车辆信息
            //非商家app端和门店web端的需要更新或创建客户车辆信息
            AppointAppVo appointAppVo = new AppointAppVo();
            BeanUtils.copyProperties(appoint,appointAppVo);
            Result result = customerCarService.addOrUpdate(appointAppVo);
            if (result.isSuccess()&& null != result.getData()) {
                CustomerCar customerCar = (CustomerCar)result.getData();
                appoint.setCustomerCarId(customerCar.getId());
            } else {
                logger.error("创建客户信息失败:{}", result.getErrorMsg());
                return Result.wrapErrorResult("-1", "处理客户车辆信息失败");
            }
        }

        //计算优惠金额
        BigDecimal inputAppointAmount = appoint.getAppointAmount();//传入的预约单金额
        BigDecimal oriAppointAmount = appoint.getOriAppointAmount() == null ? new BigDecimal("0.00") : appoint.getOriAppointAmount();//根据服务计算出来的预约单金额,没有则默认为0.00
        BigDecimal totalDiscount = new BigDecimal("0.00");
        if(inputAppointAmount==null){
            appoint.setAppointAmount(oriAppointAmount);
        } else {
            totalDiscount = oriAppointAmount.subtract(inputAppointAmount);
        }

        //按服务价格加权平均优惠金额到每个服务上
        _weightedAverageDiscount(totalDiscount,appointServicesVoList);
        //新增预约单
        appointDao.insert(appoint);
        if (!CollectionUtils.isEmpty(appointServicesVoList)) {//有预约服务,新增预约服务
            for (AppointServiceVo as : appointServicesVoList) {
                if (null != as.getServiceId() && 0l != as.getServiceId()) {
                    as.setAppointId(appoint.getId());
                    as.setAppointSn(appoint.getAppointSn());
                }
            }
            Integer count = appointServiceService.batchInsert(appointServicesVoList);
            if(count==null||count<1){
                logger.error("预约服务添加失败!appointServicesVoList:{}", appointServicesVoList);
                return Result.wrapErrorResult("-1", "预约服务添加失败");
            }
        }
        //.更新customerCar预约信息
        String license = appoint.getLicense();
        Long shopId = appoint.getShopId();
        if(license!=null&&shopId!=null){
            int upCustomerCount = customerCarService.appointNumberAddOne(license,shopId);
            if(upCustomerCount<1){
                logger.error("shopId{},车牌号{}的客户车辆信息异常",shopId,license);
            }
        }
        if (appoint.getPayStatus() == null || AppointPayStatusEnum.SUCCESS.getPayStatus().equals(appoint.getPayStatus())) {
            //不需要支付或者支付成功的处理消息推送
            sendAppointMsg(appoint);
        }
        return Result.wrapSuccessfulResult(appoint);
    }

    public void sendAppointMsg(Appoint appoint) {
        Assert.notNull(appoint,"预约单appoint不能为空");
        Long shopId = appoint.getShopId();
        AppointChannelEnum appointChannel = AppointChannelEnum.getAppointChannelEnum(appoint.getChannel().intValue());
        if(appointChannel==null){
            throw new BizException("未知的预约单渠道");
        }
        //.是否推送消息
        if (appointChannel.isPushMsg()) {
            try {
                if (appointChannel.getChannelId().intValue() == AppointChannelEnum.WEB.getChannelId()
                        || appointChannel.getChannelId().intValue() == AppointChannelEnum.MACE.getChannelId()) {
                    // 门店web,商家app创建预约单直接为确认状态
                    pushMsg(Constants.APPOINT_CONFIRM, appoint);
                } else {
                    pushMsg(Constants.APPOINT_CREATE, appoint);
                }
            } catch (Exception e) {
                logger.error("推送MQ异常appoint:{},异常信息:", LogUtils.objectToString(appoint), e);
            }
        }
        //.是否发送短信给SA
        if (appointChannel.isSmsSA()) {
            try{
                sendMsgToSA(shopId, appoint.getAppointSn(), appoint.getMobile(), null, Constants.APP_APPOINT_SMS_TPL);
            } catch (Exception e){
                logger.error("[预约单]发送预约单信息给门店SA出现异常,appoint:{},异常信息:", LogUtils.objectToString(appoint), e);
            }
        }
    }

    /**
     * 按服务价格加权平均优惠金额到每个服务上
     * @param totalDiscount
     * @param appointServicesVoList
     */
    private void _weightedAverageDiscount(BigDecimal totalDiscount, List<AppointServiceVo> appointServicesVoList) {
        if (totalDiscount.compareTo(BigDecimal.ZERO) == 0
                || CollectionUtils.isEmpty(appointServicesVoList)) {
            return;
        }
        //.计算总服务价格
        BigDecimal totalServicePrice = new BigDecimal("0.00");
        for (AppointServiceVo appointServiceVo : appointServicesVoList) {
            BigDecimal servicePrice = appointServiceVo.getServicePrice()==null?new BigDecimal("0.00"):appointServiceVo.getServicePrice();
            totalServicePrice = totalServicePrice.add(servicePrice);
        }
        //.加权拆分总优惠
        if (totalServicePrice.compareTo(BigDecimal.ZERO) <= 0) {
            //服务总价为0,没发加权平均,默认把总优惠计入第一个服务
            appointServicesVoList.get(0).setDiscountAmount(totalDiscount);
        } else {
            BigDecimal remainDiscount = totalDiscount;//剩余待拆分的优惠金额
            for(int i=0;i<appointServicesVoList.size();i++){
                AppointServiceVo appointServicesVo = appointServicesVoList.get(i);
                //按照原价价格加权平均折扣金额到每个服务上
                if (i == (appointServicesVoList.size() - 1)) {
                    //最后一项直接取扣减金额的剩余部分
                    appointServicesVo.setDiscountAmount(remainDiscount.setScale(2,BigDecimal.ROUND_HALF_UP));
                } else {
                    //原价价格加权平均折扣 = 总折扣*当前服务原价/所有服务总原价  ###四舍五入后保留两位小数
                    BigDecimal servicePrice = appointServicesVo.getServicePrice()==null?new BigDecimal("0.00"):appointServicesVo.getServicePrice();
                    BigDecimal discount = totalDiscount.multiply(servicePrice).divide(totalServicePrice, 2, BigDecimal.ROUND_HALF_UP);
                    remainDiscount = totalDiscount.subtract(discount);
                    appointServicesVo.setDiscountAmount(discount);
                }
            }
        }
        //.优惠金额正值化处理(负值优惠取绝对值加到服务价格上,优惠归0)
        _absDiscount(appointServicesVoList);
    }

    @Override
    public Result<Appoint> insertAppointAndService(List<Long> serviceIds, Appoint appoint) {
        if(CollectionUtils.isEmpty(serviceIds)){
            return insertAppointAndService(appoint,null);
        }
        Map<String,Object> svInfoSearchMap = Maps.newHashMap();
        svInfoSearchMap.put("ids",serviceIds);
        svInfoSearchMap.put("status",0);//0,有效状态
        List<ShopServiceInfo> shopServiceInfos = shopServiceInfoService.select(svInfoSearchMap);
        if(CollectionUtils.isEmpty(shopServiceInfos)){
            logger.error("serviceIds:{},获取门店服务信息为空", LogUtils.objectToString(serviceIds));
            return Result.wrapErrorResult("-1", "获取门店服务信息为空,创建预约单失败");
        }
        shopServiceInfoService.setServiceSuitAmount(shopServiceInfos);
        Long[] serviceIdsArray = new Long[serviceIds.size()];
        serviceIds.toArray(serviceIdsArray);//list转数组
        List<ServiceGoodsSuite> serviceGoodsSuiteList = serviceGoodsSuiteService.selectByServiceIds(serviceIdsArray);
        Map<Long,ServiceGoodsSuite> serviceGoodsSuiteMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(serviceGoodsSuiteList)){
            for(ServiceGoodsSuite serviceGoodsSuite:serviceGoodsSuiteList){
                serviceGoodsSuiteMap.put(serviceGoodsSuite.getServiceId(),serviceGoodsSuite);
            }
        }
        List<AppointServiceVo> appointServiceVoList = new ArrayList<>();
        StringBuffer appointContent = new StringBuffer();//服务描述
        BigDecimal oriAppointAmount = new BigDecimal("0.00");//预约单原金额(由云修根据服务价格计算得出)
        for(ShopServiceInfo shopServiceInfo:shopServiceInfos){
            int suiteNum = shopServiceInfo.getSuiteNum()==null?0:shopServiceInfo.getSuiteNum().intValue();
            appointContent.append(shopServiceInfo.getServiceNote());
            if(suiteNum==0l||suiteNum==1l){
                AppointServiceVo appointServiceVo = new AppointServiceVo();
                appointServiceVo.setShopId(appoint.getShopId());
                appointServiceVo.setAppointSn(appoint.getAppointSn());
                appointServiceVo.setServiceId(shopServiceInfo.getId());
                appointServiceVo.setTemplateId(shopServiceInfo.getParentId());
                appointServiceVo.setServiceName(shopServiceInfo.getName());
                appointServiceVo.setServiceNote(shopServiceInfo.getServiceNote());
                BigDecimal servicePrice = shopServiceInfo.getServicePrice() == null ? new BigDecimal("0.00") : shopServiceInfo.getServicePrice();
                appointServiceVo.setServicePrice(servicePrice);
                BigDecimal serviceAmount = shopServiceInfo.getSuiteAmount() == null ? new BigDecimal("0.00") : shopServiceInfo.getSuiteAmount();
                if (suiteNum == 0) {
                    oriAppointAmount = oriAppointAmount.add(servicePrice);
                } else {
                    oriAppointAmount = oriAppointAmount.add(serviceAmount);
                }
                appointServiceVoList.add(appointServiceVo);
            } else if(suiteNum==2l){
                ServiceGoodsSuite serviceGoodsSuite = serviceGoodsSuiteMap.get(shopServiceInfo.getId());
                if(serviceGoodsSuite==null){
                    continue;
                }
                String serviceInfo = serviceGoodsSuite.getServiceInfo();
                //套餐跟服务的关系
                List<ShopServiceInfo> shopServiceMapList = new Gson().fromJson(serviceInfo, new TypeToken<List<ShopServiceInfo>>() {
                }.getType());
                if(CollectionUtils.isEmpty(shopServiceMapList)){
                    continue;
                }
                BigDecimal suitPrice = serviceGoodsSuite.getSuitePrice() == null ? new BigDecimal("0.00") : serviceGoodsSuite.getSuitePrice();
                oriAppointAmount = oriAppointAmount.add(suitPrice);
                for (ShopServiceInfo s : shopServiceMapList) {
                    AppointServiceVo appointServiceVo = new AppointServiceVo();
                    appointServiceVo.setAppointSn(appoint.getAppointSn());
                    appointServiceVo.setServiceName(s.getName());
                    appointServiceVo.setServiceId(s.getId());//服务ID
                    appointServiceVo.setParentServiceId(shopServiceInfo.getId());//父服务ID(服务套餐id)
                    appointServiceVo.setShopId(appoint.getShopId());
                    appointServiceVo.setServiceNote(s.getServiceNote());
                    //为了兼容serviceGoodsSuite中serviceInfo新老数据问题
                    BigDecimal price = null;
                    BigDecimal serviceAmount = s.getServiceAmount();//新数据的套餐服务价格
                    BigDecimal servicePrice = s.getServicePrice();//老数据的套餐服务价格
                    price = servicePrice;
                    if (serviceAmount != null) {
                        price = serviceAmount;
                    }
                    if (price == null) {
                        price = new BigDecimal("0.00");
                    }
                    appointServiceVo.setServicePrice(price);
                    appointServiceVoList.add(appointServiceVo);
                }
            }
        }
        if (StringUtils.isBlank(appoint.getAppointContent())) {
            appoint.setAppointContent(appointContent.toString());//预约内容
        }
        appoint.setOriAppointAmount(oriAppointAmount);
        if(CollectionUtils.isEmpty(appointServiceVoList)){
            logger.info("根据服务Id构造的预约服务为空");
            return Result.wrapErrorResult("-1", "构造的预约服务为空,创建预约单失败");
        }
        return insertAppointAndService(appoint,appointServiceVoList);
    }

    @Override
    public Integer updateById(Appoint appoint) {
        return appointDao.updateById(appoint);
    }

    /**
     * app端 获取客户预约单列表
     *
     * @param searchParams
     * @return
     * @since app3.0
     */
    @Override
    public List<Appoint> getAppointRecordApp(Map<String, Object> searchParams) {
        List<Appoint> appointList = appointDao.select(searchParams);
        return appointList;
    }

    @Override
    public boolean isExistAppointSn(Long shopId, String appointSn) {
        if(appointSn==null){
            return false;
        }
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId",shopId);
        searchMap.put("appointSn",appointSn);
        Integer count = selectCount(searchMap);
        if(count.intValue()>0){
            return true;
        }
        return false;
    }

    @Override
    public Result deleteAppoint(Long shopId, Long appointId,Long userId) {
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId",shopId);
        searchMap.put("id",appointId);
        List<Appoint> appointList = appointDao.select(searchMap);
        if(CollectionUtils.isEmpty(appointList)){
            logger.error("取消预约,根据预约单ID和shopId获得不到预约单数据!id:{},shopId:{}", appointId,shopId);
            return Result.wrapErrorResult("-1", "根据预约单ID获取不到预约单信息");
        }
        Appoint appoint = appointList.get(0);
        Long oldStatus = appoint.getStatus();
        if(oldStatus!=AppointStatusEnum.INVALID.getIndex().longValue()){//只有作废的预约单才能删除
            logger.info("删除预约单{}失败,原因是该预约单状态为:{}", appoint,AppointStatusEnum.getNameByStatus(oldStatus.intValue()));
            return Result.wrapErrorResult("-1", "删除预约单失败,原因是只有状态为"+AppointStatusEnum.INVALID.getName()+"的预约单才能删除");
        }
        appoint.setGmtModified(new Date());
        appoint.setModifier(userId);
        Integer cnt = appointDao.deleteById(appointId);
        if(cnt==null||cnt<1){
            logger.info("删除预约单失败!appoint:{}", appoint);
            return Result.wrapErrorResult("-1", "删除预约单失败");
        }
        return Result.wrapSuccessfulResult(appoint);
    }

    @Override
    public Result invalidAppoint(Long shopId, Long appointId,Long userId) {
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId",shopId);
        searchMap.put("id",appointId);
        List<Appoint> appointList = appointDao.select(searchMap);
        if(CollectionUtils.isEmpty(appointList)){
            logger.error("作废预约,根据预约单ID和shopId获得不到预约单数据!id:{},shopId:{}", appointId,shopId);
            return Result.wrapErrorResult("-1", "根据预约单ID获取不到预约单信息");
        }
        Appoint appoint = appointList.get(0);
        Long oldStatus = appoint.getStatus();
        if(oldStatus!=AppointStatusEnum.CHEZHU_CANCEL.getIndex().longValue()
                &&oldStatus!=AppointStatusEnum.SHOP_CANCEL.getIndex().longValue()
                &&oldStatus!=AppointStatusEnum.WECHAT_CANCEL.getIndex().longValue()){//只有取消的预约单才能作废
            logger.info("作废预约单{}失败,原因是该预约单状态为:{}", appoint,AppointStatusEnum.getNameByStatus(oldStatus.intValue()));
            return Result.wrapErrorResult("-1", "操作失败,原因是只有状态为取消的预约单才能置为无效");
        }
        appoint.setStatus(AppointStatusEnum.INVALID.getIndex().longValue());
        appoint.setGmtModified(new Date());
        appoint.setModifier(userId);
        appoint.setPreviewType(1l);//预约单已处理
        Integer cnt = appointDao.updateById(appoint);
        if(cnt==null||cnt<1){
            logger.info("作废预约单失败!appoint:{}", appoint);
            return Result.wrapErrorResult("-1", "操作失败");
        }
        return Result.wrapSuccessfulResult(appoint);
    }

    @Override
    public void sendMsgToSA(Long shopId, String appointSn, String appointMobile, String cancelReason, String type) {
        Assert.notNull(shopId, "shopId不能为空");
        Assert.notNull(appointSn, "预约单Sn不能为空");
        Assert.notNull(appointMobile, "预约单手机号不能为空");
        AppointAppVo appointAppVo = new AppointAppVo();
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        appointAppVo.setUserGlobalId("" + userGlobalId);
        appointAppVo.setAppointSn(appointSn);
        appointAppVo.setMobile(appointMobile);
        appointAppVo.setCancelReason(cancelReason);
        sendMsgToSA(appointAppVo, type);
    }

    /**
     * 获得CRM SA mobile,并发送通知短信
     * create by jason 2015-07-30
     * @param appointAppVo
     */
    @Override
    public void sendMsgToSA(AppointAppVo appointAppVo, String type) {
        String userGlobalId = appointAppVo.getUserGlobalId();
        //根据userGlobald获得SA mobile
        try {
            String saMobile = crmCustomerService.selectSaMobilePhone(Long.valueOf(userGlobalId));
            if (StringUtils.isBlank(saMobile)){
                logger.error("sa手机号为空,userGlobalId:{}",userGlobalId);
                return;
            }
            //设置发送短信配置
            SmsBase smsBase = new SmsBase();
            smsBase.setAction(type);//短信模板
            smsBase.setMobile(saMobile);//SA手机号
            Map<String, Object> smsMap = new HashMap<>();
            smsMap.put("appointSn", appointAppVo.getAppointSn());//预约单编码
            smsMap.put("mobile", appointAppVo.getMobile());//预约单联系人手机号
            if (Constants.APP_APPOINT_CANCEL_SMS_TPL.equals(type)) {
                smsMap.put("cancelReason", appointAppVo.getCancelReason());//取消原因
            }
            smsBase.setData(smsMap);
            smsService.sendMsg(smsBase, "推送店铺SA短信");
        } catch (BizException e) {
            logger.error(e.getMessage());
        }catch (Exception e){
            logger.error("[调用dubbo接口失败]:",e);
        }

    }
}
