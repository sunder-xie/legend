package com.tqmall.yunxiu.web.pub;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.customer.AppointServiceService;
import com.tqmall.legend.biz.marketing.activity.CzCouponNoticeService;
import com.tqmall.legend.biz.shop.ServiceGoodsSuiteService;
import com.tqmall.legend.biz.shop.ServiceTemplateCateRelService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.AppointAppVo;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.marketing.activity.CzCouponNotice;
import com.tqmall.legend.entity.pub.order.AppointDetailVo;
import com.tqmall.legend.entity.pub.order.OrderAppointVo;
import com.tqmall.legend.entity.pub.service.ServiceInfoVo;
import com.tqmall.legend.entity.shop.ServiceGoodsSuite;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.object.enums.appoint.AppointStatusEnum;
import com.tqmall.legend.facade.appoint.AppointFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by jason on  2015-07-20.
 * 预约单接口
 */
@Controller
@RequestMapping(value = "/pub/appoint")
public class PubAppointController extends BaseController {
    Logger logger = LoggerFactory.getLogger(PubAppointController.class);

    @Autowired
    private AppointService appointService;

    @Autowired
    private AppointFacade appointFacade;

    @Autowired
    private AppointServiceService appointServiceService;

    @Autowired
    private ShopService shopService;

    @Autowired
    ShopServiceCateService shopServiceCateService;

    @Autowired
    private ShopServiceInfoService shopServiceInfoService;

    @Autowired
    ServiceGoodsSuiteService serviceGoodsSuiteService;

    @Autowired
    ServiceTemplateCateRelService serviceTemplateCateRelService;

    @Autowired
    private CzCouponNoticeService czCouponNoticeService;


    /**
     * create by jason 2015-07-20
     * 获得所有预约单接口
     *
     * @param mobile
     * @param channel 渠道来源 0 门店web, 1 商家app, 2 车主app, 3 车主微信, 4 橙牛
     *
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result getAllAppoint(@RequestParam(value = "mobile", required = true) String mobile,
                                @RequestParam(value = "userGlobalId", required = false) String userGlobalId,
                                @RequestParam(value = "channel", required = false) Long channel,
                                @RequestParam(value = "offset", required = false ) Long  offset,
                                @RequestParam(value = "limit", required = false ) Long  limit) {
        //预约单
        mobile = mobile.trim();
        if(!StringUtil.isMobileNO(mobile)){
            logger.info("手机号{}不正确,直接返回",mobile);
            return Result.wrapErrorResult("", "手机号不正确");
        }
        Map map = new HashMap();
        map.put("mobile", mobile);
        if(StringUtils.isNotBlank(userGlobalId)){
            Map<String,Object> shopParam = Maps.newHashMap();
            shopParam.put("userGlobalId",userGlobalId);
            List<Shop> shopList = shopService.select(shopParam);
            if(!CollectionUtils.isEmpty(shopList)){
                Shop shop = shopList.get(0);
                map.put("shopId",shop.getId());
            }  else{
                logger.info("根据userGlobalId:{}查询不到门店信息,预约单查询失败",userGlobalId);
                return Result.wrapErrorResult("", "获取不到门店信息");
            }
        }
        if (null != channel) {
            map.put("channel", channel);
        }
        if (null == offset || 0l > offset) {
            offset = 0l;
        }
        if (null == limit || 0l > limit) {
            limit = 5l;
        }
        map.put("limit", limit);
        map.put("offset", offset);
        List<String> sorts = new ArrayList<>();
        sorts.add("gmt_modified desc");
        map.put("sorts", sorts);

        logger.info("获取所有预约单接口参数!参数map:{},", map);
        List<Appoint> appointList = appointService.select(map);
        List<OrderAppointVo> orderAppointList = warpAppointList(appointList);

        if (CollectionUtils.isEmpty(orderAppointList)) {
            return Result.wrapErrorResult("", "获取所有预约单数据为空");
        } else {
            logger.info("获取所有预约单数据成功,mobile:{},channel:{}",mobile,channel);
            return Result.wrapSuccessfulResult(orderAppointList);
        }
    }

    //组装预约单LIST
    private List<OrderAppointVo> warpAppointList(List<Appoint> appointList) {
        List<OrderAppointVo> orderAppointList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appointList)) {
            for (Appoint appoint : appointList) {
                OrderAppointVo orderAppointVo = new OrderAppointVo();
                Long shopId = appoint.getShopId();
                //店铺
                Shop shop = shopService.selectById(shopId);
                Long appointId = appoint.getId();
                if (null != appointId) {
                    //根据预约单ID 获得服务
                    List<ServiceInfoVo> list = dealAppointServiceList(appointId, null);
                    orderAppointVo.setAppointServiceList(list);
                }
                orderAppointVo.setAppointId(appoint.getId());//预约单ID
                orderAppointVo.setAppointSn(appoint.getAppointSn());//预约单编号
                if (null != shop) {
                    orderAppointVo.setShopId(shopId);
                    orderAppointVo.setShopName(shop.getName());//店铺名称
                    orderAppointVo.setShopAddress(shop.getAddress());//店铺地址
                    orderAppointVo.setMobile(appoint.getMobile());
                    orderAppointVo.setUserGlobalId(shop.getUserGlobalId());
                }
                orderAppointVo.setAppointStatus(appoint.getStatus());//订单状态
                orderAppointVo.setAppointTime(appoint.getAppointTime());//预约时间
                orderAppointVo.setGmtCreate(appoint.getGmtCreate());//创建时间
                orderAppointVo.setAppointContent(appoint.getAppointContent());//预约内容
                orderAppointVo.setAppointAmount(appoint.getAppointAmount());//预约单金额
                orderAppointVo.setDownPayment(appoint.getDownPayment());//预约定金
                orderAppointVo.setPayStatus(appoint.getPayStatus());//支付状态
                orderAppointList.add(orderAppointVo);
            }
        }

        return orderAppointList;
    }


    /**
     * create by jason 2015-07-20
     * 获得预约单详情接口
     *
     * @param mobile
     * @param appointId
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Result getAppointDetail(@RequestParam(value = "mobile", required = false) String mobile,
                                   @RequestParam(value = "appointId", required = true) Long appointId) {
        logger.info("进入获取预约单详情接口!mobile:{},appointId:{}", mobile, appointId);
        Appoint appoint = appointService.selectById(appointId);
        if(appoint==null){
            logger.info("获得预约单为空,手机号是:{},预约单ID是:{}", mobile, appointId);
            return Result.wrapErrorResult("", "获得预约单为空");
        }
        if (StringUtils.isNotBlank(mobile)) {
            mobile = mobile.trim();
            if(!StringUtils.equals(appoint.getMobile(), mobile)){
                logger.info("获得预约单详情手机号不匹配,预约单手机号是:{},预约单ID是:{},入參手机号是:{}", appoint.getMobile(), appointId,mobile);
                return Result.wrapErrorResult("", "获得预约单详情失败");
            }
        }
        AppointDetailVo appointDetailVo = new AppointDetailVo();
        //店铺
        Long shopId = appoint.getShopId();
        Shop shop = shopService.selectById(shopId);

        appointDetailVo.setAppointId(appoint.getId());//预约单ID
        Long appointOrderId = appoint.getOrderId();
        Long orderId = (appointOrderId == null) ? Long.valueOf(0l) : appointOrderId;
        appointDetailVo.setOrderId(orderId);//工单ID
        appointDetailVo.setAppointSn(appoint.getAppointSn());//预约单编号
        appointDetailVo.setChannel(appoint.getChannel());
        if (null != shop) {
            appointDetailVo.setShopName(shop.getName());//店铺名称
            appointDetailVo.setUserGlobalId(shop.getUserGlobalId());
            appointDetailVo.setShopAddress(shop.getAddress());//店铺地址
            appointDetailVo.setTel(shop.getTel());//店铺固话
            appointDetailVo.setMobile(shop.getMobile());//店铺手机号

            //set门店优惠公告
            Map map = new HashMap();
            map.put("shopId", shopId);
            map.put("couponStatus", 4);//已发布
            List<CzCouponNotice> noticeList = czCouponNoticeService.select(map);
            appointDetailVo.setCouponNoticeList(noticeList);
        }
        appointDetailVo.setAppointStatus(appoint.getStatus());//订单状态
        appointDetailVo.setAppointTime(appoint.getAppointTime());//预约时间
        appointDetailVo.setGmtCreate(appoint.getGmtCreate());//预约创建时间
        appointDetailVo.setAppointContent(appoint.getAppointContent());//预约内容
        appointDetailVo.setCarLicense(appoint.getLicense());//预约车牌
        appointDetailVo.setCancelReason(appoint.getCancelReason());//取消原因
        String carBrandName = appoint.getCarBrandName();
        String carSeriesName = appoint.getCarSeriesName();
        if (null != carBrandName) {
            StringBuffer carModel = new StringBuffer(carBrandName);
            if (null != carSeriesName) {
                carModel.append(" ").append(carSeriesName);
            }
            appointDetailVo.setCarModel(carModel.toString());//预约车型
        }
        appointDetailVo.setContactName(appoint.getCustomerName());//联系人
        appointDetailVo.setContactMobile(appoint.getMobile());//联系人电话
        appointDetailVo.setAppointAmount(appoint.getAppointAmount());//预约单金额
        appointDetailVo.setDownPayment(appoint.getDownPayment());//预付定金
        appointDetailVo.setPayStatus(appoint.getPayStatus());//支付状态
        //根据预约单ID 获得预约服务列表,并设置预约单详情服务套餐名称和图片
        List<ServiceInfoVo> list = dealAppointServiceList(appointId,appointDetailVo);
        appointDetailVo.setAppointServiceList(list);
        return Result.wrapSuccessfulResult(appointDetailVo);
    }

    /**
     * create by jason 2015-07-20
     * 从APP端获取预约单信息,insert我们数据库中,然后返回appointId
     *
     * @param appointAppVo
     */
    @Deprecated
    @RequestMapping(value = "/insertAppAppoint", method = RequestMethod.POST)
    @ResponseBody
    public Result insertAppAppoint(@RequestBody AppointAppVo appointAppVo) {
        logger.info("移动端创建预约单方法!入参对象:{}", appointAppVo);
        //后台检验
        if (null == appointAppVo) {
            return Result.wrapErrorResult("40058", "预约单对象不能为空!");
        }
        Long channel = appointAppVo.getChannel();
        //易车联传过来得时间是字符串
        if (null != channel && channel == 6l) {
            String appointDateStr = appointAppVo.getAppointDateStr();
            try {
                if (StringUtil.isStringEmpty(appointDateStr)) {
                    logger.info("appointDateStr不正确,值为:{}", appointDateStr);
                    return Result.wrapErrorResult("40058", "请传入正确的参数");
                } else {
                    appointAppVo.setAppointDate(DateUtil.convertStringToDate(appointDateStr, "yyyy-MM-dd HH:mm:ss")
                            .getTime());
                }
            } catch (Exception e) {
                logger.error("预约时间格式转化出错:{}", appointDateStr);
                return Result.wrapErrorResult("40058", "请传入正确的参数");
            }

        }
        String userGlobalId = appointAppVo.getUserGlobalId();
        Long appointDate = appointAppVo.getAppointDate();
        Long carBrandId = appointAppVo.getCarBrandId();
        String carBrandName = appointAppVo.getCarBrandName();
        Long carSeriesId = appointAppVo.getCarSeriesId();
        String carSeriesName = appointAppVo.getCarSeriesName();
        String license = appointAppVo.getLicense();
        String mobile = appointAppVo.getMobile();
        String customerName = appointAppVo.getCustomerName();
        String refer = appointAppVo.getRefer();
        if (StringUtil.isStringEmpty(customerName)) {
            logger.info("customerName不正确,值为:{}", customerName);
            return Result.wrapErrorResult("40058", "请传入正确的参数");
        }
        if (!StringUtil.isMobileNO(mobile)) {
            logger.info("mobile不正确,值为:{}", mobile);
            return Result.wrapErrorResult("40058", "请传入正确的手机号");
        }
        if (appointDate == null || 0l == appointDate) {
            logger.info("appointDate不正确,值为:{}", appointDate);
            return Result.wrapErrorResult("40058", "请传入正确的预约时间");
        }
        if (StringUtil.isStringEmpty(userGlobalId)) {
            logger.info("userGlobalId不正确,值为:{}", userGlobalId);
            return Result.wrapErrorResult("40058", "请传入正确的参数");
        }
        if (StringUtil.isStringEmpty(refer)) {
            logger.info("refer不正确,值为:{}", refer);
            return Result.wrapErrorResult("40058", "请传入正确的参数");
        }
        if (channel == null) {
            logger.info("channel不正确,值为:null");
            return Result.wrapErrorResult("40058", "请传入正确的渠道编码");
        }
        if (channel == 6l){
            //车易联过来的预约单
            String openid = appointAppVo.getOpenid();
            if (StringUtils.isEmpty(openid)) {
                logger.info("openid不正确,值为:{}", openid);
                return Result.wrapErrorResult("40058", "请传入正确的参数");
            }
            appointAppVo.setEchelianid(openid);//易车联用户ID
            appointAppVo.setPushStatus(0l);//先推到云修营运那边确认

        } else {
            if (!StringUtil.isCarLicense(license)) {
                license = license.toUpperCase();//字母转大写
                logger.info("license不正确,值为:{}", license);
                return Result.wrapErrorResult("40058", "请传入正确的车牌号");
            } else if (null == carBrandId || 0l == carBrandId) {
                logger.info("carBrandId不正确,值为:{}", carBrandId);
                return Result.wrapErrorResult("40058", "请传入正确的参数");
            } else if (null == carSeriesId || 0l == carSeriesId) {
                logger.info("carSeriesId不正确,值为:{}", carSeriesId);
                return Result.wrapErrorResult("40058", "请传入正确的参数");
            } else if (StringUtil.isStringEmpty(carBrandName)) {
                logger.info("carBrandName不正确,值为:{}", carBrandName);
                return Result.wrapErrorResult("40058", "请传入正确的参数");
            } else if (StringUtil.isStringEmpty(carSeriesName)) {
                logger.info("carSeriesName不正确,值为:{}", carSeriesName);
                return Result.wrapErrorResult("40058", "请传入正确的参数");
            }
            if(channel == 1) {
                //商家APP端创建预约单状态直接为1.已确认
                appointAppVo.setStatus(AppointStatusEnum.APPOINT_SUCCESS.getIndex().longValue());
            }
        }

        // 时间字符串转Date
        Date appointTime = new Date(appointDate);
        appointAppVo.setAppointTime(appointTime);

        com.tqmall.legend.common.Result result = null;
        try {
            result = appointService.addAppointApp(appointAppVo);

            if (null != result) {
                if (result.isSuccess()) {
                    logger.info("保存预约单成功!channel:{},appointTime:{},license:{},userGlobalId:{},mobile:{}",channel,
                            appointTime,license,userGlobalId,mobile);
                    //channel == 6是易车过来的预约单,预约单还没有确认,不用通知SA
                    //channel == 1是商家APP过来的预约单,不用通知SA
                    if (appointAppVo.getChannel() != 6l
                            && appointAppVo.getChannel() != 1l) {
                        //获得SA电话,然后发送短信
                        appointService.sendMsgToSA(appointAppVo,Constants.APP_APPOINT_SMS_TPL);
                    }

                    return Result.wrapSuccessfulResult(result.getData());
                } else {
                    logger.error("保存预约单失败:{}", result.getErrorMsg());
                    return Result.wrapErrorResult("10001", result.getErrorMsg());
                }
            } else {
                logger.error("保存预约单异常!result返回为null");
                return Result.wrapErrorResult("10001","系统内部错误，请稍后再试！");
            }
        } catch (Exception e) {
            logger.error("保存预约单异常!" + e);
            return Result.wrapErrorResult("10001","系统内部错误，请稍后再试！");
        }
    }

    /**
     * 柯昌强 2015-12-28
     * 插入预约单 这些预约单没有userGlobalId
     */
    @Deprecated
    @RequestMapping(value = "insert_appoint", method = RequestMethod.POST)
    @ResponseBody
    public Result insertAppoint(@RequestBody AppointAppVo appointAppVo) {
        logger.info("移动端创建橙牛过来的预约单方法!传入参数对象:{}", appointAppVo);
        //后台检验
        if (null == appointAppVo) {
            return Result.wrapErrorResult("-1", "预约单对象不能为空!");
        }
        String customerName = appointAppVo.getCustomerName();
        String mobile = appointAppVo.getMobile();
        String appointDateStr = appointAppVo.getAppointDateStr();
        String refer = appointAppVo.getRefer();
        Long channel = appointAppVo.getChannel();
        Long carSeriesId = appointAppVo.getCarSeriesId();
        String carSeriesName = appointAppVo.getCarSeriesName();
        Long carBrandId = appointAppVo.getCarBrandId();
        String carBrandName = appointAppVo.getCarBrandName();
        String customerAddress = appointAppVo.getCustomerAddress();
        Long templateId = appointAppVo.getTemplateId();
        Pattern pattern = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        if (StringUtil.isStringEmpty(customerName)) {
            logger.info("customerName为空,值为:{}", customerName);
            return Result.wrapErrorResult("－1", "客户名为空！");
        } else if (StringUtil.isStringEmpty(mobile) || !pattern.matcher(mobile).find()) {
            logger.info("mobile为空,值为:{}", mobile);
            return Result.wrapErrorResult("－1", "客户手机号为空或错误！");
        } else if (StringUtil.isStringEmpty(appointDateStr)) {
            logger.info("appointDateStr为空,值为:{}", appointDateStr);
            return Result.wrapErrorResult("－1", "预约时间为空！");
        } else if (StringUtil.isStringEmpty(refer)) {
            logger.info("refer为空,值为:{}", refer);
            return Result.wrapErrorResult("－1", "预约单过来的途径为空！");
        } else if (channel == null) {
            logger.info("channel为空,值为:null");
            return Result.wrapErrorResult("－1", "预约单来源为空！");
        } else if (carSeriesId == null || carSeriesId == 0l) {
            logger.info("车系列id为空,值为:null");
            return Result.wrapErrorResult("－1", "车系列id为空！");
        } else if (StringUtil.isStringEmpty(carSeriesName)) {
            logger.info("车系列名称为空,值为:null");
            return Result.wrapErrorResult("－1", "车系列名称为空！");
        } else if (carBrandId == null || carBrandId == 0l) {
            logger.info("车品牌id为空,值为:null");
            return Result.wrapErrorResult("－1", "车品牌id为空！");
        } else if (StringUtil.isStringEmpty(carBrandName)) {
            logger.info("车品牌名称为空,值为:null");
            return Result.wrapErrorResult("－1", "车品牌名称为空！");
        } else if (StringUtil.isStringEmpty(customerAddress)) {
            logger.info("客户住址,值为:null");
            return Result.wrapErrorResult("－1", "客户住址为空！");
        } else if (templateId == null || templateId == 0l) {
            logger.info("服务模板id为空,值为:null");
            return Result.wrapErrorResult("－1", "服务模板id为空！");
        }
        appointAppVo.setPushStatus(0l);//先推到云修营运那边确认
        appointAppVo.setAppointTime(DateUtil.convertStringToDate(appointDateStr));
        com.tqmall.legend.common.Result result = appointService.addAppoint(appointAppVo);
        if (!result.isSuccess()) {
            logger.info("保存预约单失败!传入参数对象:{}", appointAppVo);
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }

    /**
     * create by jason 2015-08-05
     * 从APP端取消预约单信息
     *
     */
    @Deprecated
    @RequestMapping(value = "/cancelAppAppoint",method = RequestMethod.POST)
    @ResponseBody
    public Result cancelAppAppoint(@RequestBody AppointAppVo appointAppVo) {
        logger.info("进入APP端取消预约单方法!appointAppVo对象:{}",appointAppVo);
        //后台检验
        Long appointId = appointAppVo.getId();
        //String userGlobalId = appointAppVo.getUserGlobalId();
        String cancelReason = appointAppVo.getCancelReason();
        Long channel = appointAppVo.getChannel();
        String mobile = appointAppVo.getMobile();
        if (null == appointId || 0l > appointId) {
            return Result.wrapErrorResult("-1", "预约单ID不能为空");
        } else if (StringUtil.isStringEmpty(cancelReason)) {
            return Result.wrapErrorResult("-1", "取消原因不能为空");
        } else if (StringUtil.isStringEmpty(mobile)) {
            return Result.wrapErrorResult("-1", "联系电话不能为空");
        }

        com.tqmall.legend.common.Result result = null;
        try {
            Long status = 3l;//默认车主app端取消
            if (null != channel) { //车主微信
                if (channel == 3l) {
                    status = 5l;// 微信端取消
                } else if (channel == 1l) {//商家app
                    status = 4l;// 商家app端取消
                }
            }
            appointAppVo.setStatus(status);
            result = appointFacade.cancelAppointByOuter(appointAppVo);
            if (result.isSuccess()) {
                logger.info("取消预约单成功!" + result.getData());
                return Result.wrapSuccessfulResult(appointId);
            } else {
                logger.info("取消预约单失败" + result.getErrorMsg());
                return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
            }
        } catch (Exception e) {
            logger.error("取消预约单异常" + e);
            return Result.wrapErrorResult("-1","取消预约单异常!");
        }
    }

    /**
     * create by jason 2015-08-05
     * 从APP端删除预约单信息
     *
     */
    @RequestMapping(value = "/deleteAppAppoint")
    @ResponseBody
    public Result deleteAppAppoint(Long appointId) {
        logger.info("进入APP端删除预约单方法!appointId:{}",appointId);
        //后台检验
        if (null == appointId || 0l > appointId) {
            return Result.wrapErrorResult("-1", "预约单ID不能为空");
        }
        try {
            Integer cnt = appointService.deleteById(appointId);
            if (cnt > 0) {
                return Result.wrapSuccessfulResult(appointId);
            } else {
                return Result.wrapErrorResult("-1","删除预约单失败!");
            }
        } catch (Exception e) {
            logger.error("删除预约单异常" + e);
            return Result.wrapErrorResult("-1","删除预约单异常!");
        }
    }


    /**
     * create by jason 2015-08-17
     * 获得预约单数量接口
     *
     * @param mobile
     * @param channel 渠道来源 0 门店web, 1 商家app, 2 车主app, 3 车主微信, 4 橙牛
     *
     */
    @RequestMapping(value = "/getAppointCnt", method = RequestMethod.GET)
    @ResponseBody
    public Result getAppointCnt(@RequestParam(value = "mobile", required = true) String mobile,
                                @RequestParam(value = "channel", required = false) Long channel) {
        //预约单
        if (StringUtil.isStringEmpty(mobile) || "null".equals(mobile)) {
            return Result.wrapErrorResult("", "手机号不正确!");
        }

        mobile = mobile.trim();
        Map map = new HashMap(2);
        map.put("mobile", mobile);
        if (null != channel) {
            map.put("channel",channel);
        }
        logger.info("进入获得预约单数量接口!参数:{}",map);
        try {
            List<Appoint> appointList = appointService.select(map);
            Integer appointCnt = appointList.size();
            logger.info("获取预约单数量成功,appointCnt:{}",appointCnt);
            return Result.wrapSuccessfulResult(appointCnt);

        } catch (Exception e) {
            logger.info("获取预约单数量异常" + e);
            return Result.wrapErrorResult("-1","获取预约单数量异常");
        }

    }


    /**
     * 根据预约单ID获得服务
     * appointDetailVo 不为空时设置其中的服务套餐名serviceSuitName,服务套餐小图字段serviceSuitImgUrl
     *
     */
    private List<ServiceInfoVo> dealAppointServiceList(Long appointId, AppointDetailVo appointDetailVo) {
        logger.info("获得预约单服务 appointId:{}", appointId);
        Map searchMap = new HashMap();
        searchMap.put("appointId", appointId);
        List<AppointServiceVo> list = appointServiceService.select(searchMap);
        List<ServiceInfoVo> serviceInfoVoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            //根据appointId找不到预约单信息
            return serviceInfoVoList;
        }
        //设置预约单详情服务套餐名serviceSuitName,服务套餐小图字段serviceSuitImgUrl
        if(appointDetailVo!=null){
            Set<Long> serviceSuitIds = new HashSet<>();
            BigDecimal totalDiscount = new BigDecimal("0.00");
            for (AppointServiceVo appointServiceVo : list) {
                if(appointServiceVo.getParentServiceId()!=null && appointServiceVo.getParentServiceId()>0){
                    serviceSuitIds.add(appointServiceVo.getParentServiceId());
                }
                if(appointServiceVo.getDiscountAmount()!=null){
                    totalDiscount = totalDiscount.add(appointServiceVo.getDiscountAmount());
                }
            }
            appointDetailVo.setTotalDiscount(totalDiscount);
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllByIds(serviceSuitIds);
            if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                appointDetailVo.setServiceSuitImgUrl(shopServiceInfoList.get(0).getImgUrl());
                List<String> suitNames = Lists.transform(shopServiceInfoList, new Function<ShopServiceInfo, String>() {
                    @Override
                    public String apply(ShopServiceInfo shopServiceInfo) {
                        return shopServiceInfo.getName();
                    }
                });
                appointDetailVo.setServiceSuitName(Joiner.on(",").join(suitNames));

            }
        }
        List<Long> serviceIdList = getServiceIdList(list);

        List<ShopServiceInfo> shopServiceList = shopServiceInfoService.wrapServiceCateAndSuitePrice(serviceIdList);

        if (CollectionUtils.isEmpty(shopServiceList)) {
            //shopServiceList为空
            return serviceInfoVoList;
        }
        for (ShopServiceInfo s : shopServiceList) {
            ServiceInfoVo serviceInfo = new ServiceInfoVo();

            serviceInfo.setServiceId(s.getId());//服务ID
            serviceInfo.setFirstCateId(s.getFirstCateId());//一级类目ID
            serviceInfo.setFirstCateName(s.getFirstCateName());//一级类目名称
            String flags = s.getFlags();
            Long status = s.getStatus();
            //status 0 正常 -1 TQFW下架 -2CZFW下架 云修系统
            //车主端status = -1 表示下架不区分TQFW还是CZFW
            if ("CZFW".equals(flags) && status == -2l) {
                serviceInfo.setStatus(-1l);
            } else {
                serviceInfo.setStatus(status);
            }
            Integer appCateId = s.getAppCateId();
            if (null != appCateId && appCateId > 0) {
                serviceInfo.setSecondCateId(Long.valueOf(appCateId));//二级类目ID
            }
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
            serviceInfo.setImgUrl(s.getImgUrl());
            Long shopId = s.getShopId();
            if (null != shopId) {
                Shop shop = shopService.selectById(shopId);
                if (null != shop) {
                    serviceInfo.setUserGlobalId(shop.getUserGlobalId());//user_global_id
                }
            }
            serviceInfo.setFlags(flags);
            serviceInfo.setSort(s.getSort());//排序字段

            serviceInfoVoList.add(serviceInfo);
        }
        return serviceInfoVoList;
    }

    //获得服务ID 如果是套餐的获得主服务ID
    private List<Long> getServiceIdList(List<AppointServiceVo> list) {
        //根据预约服务获取服务,然后获得一级服务类目ID
        List<Long> serviceIdList = new ArrayList<>();
        for (AppointServiceVo as : list) {
            //CZFW套餐里面嵌套 CZFW服务需要找parentServiceId
            Long parentServiceId = as.getParentServiceId();
            if (parentServiceId > 0l) {
                if (!serviceIdList.contains(parentServiceId)) {
                    serviceIdList.add(parentServiceId);
                }
            } else {
                serviceIdList.add(as.getServiceId());
            }
        }
        return serviceIdList;
    }


    /**
     * 组装服务,套餐价格
     * create by jason 2015-07-17
     *
     */
    private List<ShopServiceInfo> getSuitePrice(List<Long> suiteServiceIdsList,List<ShopServiceInfo> serviceList) {
        //获得服务和套餐价格
        if (!CollectionUtils.isEmpty(suiteServiceIdsList)) {
            Long[] serviceIds = suiteServiceIdsList
                    .toArray(new Long[suiteServiceIdsList.size()]);
            // 获取有套餐的服务数据相对应的套餐数据内容
            List<ServiceGoodsSuite> serviceGoodsSuiteList = serviceGoodsSuiteService.selectByServiceIds(serviceIds);
            if (serviceGoodsSuiteList != null) {
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



}
