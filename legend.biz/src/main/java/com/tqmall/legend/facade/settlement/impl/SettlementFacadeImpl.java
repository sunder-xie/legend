package com.tqmall.legend.facade.settlement.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.UpperNumbers;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopDTO;
import com.tqmall.dandelion.wechat.client.wechat.shop.WeChatShopService;
import com.tqmall.legend.biz.account.AccountComboFlowDetailService;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.AccountTradeFlowService;
import com.tqmall.legend.biz.account.ComboServiceRelService;
import com.tqmall.legend.biz.bo.dandelion.TaoqiBaseCouponParam;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.OrderTypeService;
import com.tqmall.legend.biz.order.log.OrderOperationLog;
import com.tqmall.legend.biz.order.vo.OrderServicesVo;
import com.tqmall.legend.biz.remote.dandelion.RemoteCouponHttp;
import com.tqmall.legend.biz.setting.vo.ShopPrintConfigVO;
import com.tqmall.legend.biz.settlement.vo.DebitBillAndFlowVo;
import com.tqmall.legend.biz.settlement.vo.DebitBillVo;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.biz.util.ShopPrintConfigUtil;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.AccountComboFlowDetail;
import com.tqmall.legend.entity.account.AccountComboServiceRel;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServiceTypeEnum;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.order.OrderType;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.shop.ShopConfigureVO;
import com.tqmall.legend.enums.order.OrderTradeTypeEnum;
import com.tqmall.legend.facade.order.WrapperOrderFacade;
import com.tqmall.legend.facade.settlement.DebitFacade;
import com.tqmall.legend.facade.settlement.SettlementFacade;
import com.tqmall.legend.facade.settlement.bo.SettlementSmsBO;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/6/18.
 */
@Service
@Slf4j
public class SettlementFacadeImpl implements SettlementFacade {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private RemoteCouponHttp remoteCouponHttp;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WrapperOrderFacade wrapperOrderFacade;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private DebitFacade debitFacade;
    @Autowired
    private ComboServiceRelService comboServiceRelService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private WeChatShopService weChatShopService;
    @Autowired
    private AccountTradeFlowService tradeFlowService;
    @Autowired
    private AccountComboFlowDetailService comboFlowDetailService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private JedisClient jedisClient;

    /**
     * 获取淘汽优惠券金额
     *
     * @param orderId
     * @param taoqiCouponSn
     * @return
     */
    @Override
    public Result couponCheck(Long orderId, String taoqiCouponSn) {
        if (StringUtils.isBlank(taoqiCouponSn)) {
            return Result.wrapErrorResult("", "请输入淘汽优惠券");
        }

        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            return Result.wrapErrorResult("", "工单不存在");
        }

        OrderInfo orderInfo = orderInfoOptional.get();
        if (StringUtils.isBlank(orderInfo.getContactMobile())) {
            return Result.wrapErrorResult("", "工单未填写联系人电话");
        }
        TaoqiBaseCouponParam baseCouponParam = new TaoqiBaseCouponParam();
        baseCouponParam.setCouponCode(taoqiCouponSn);
        baseCouponParam.setLicense(orderInfo.getCarLicense());
        baseCouponParam.setMobile(orderInfo.getContactMobile());
        //根据订单编号获取该工单包含的淘汽一级分类
        List<String> itemIds = orderServicesService.getTaoqiFirstCateIds(orderId);
        if (CollectionUtils.isEmpty(itemIds)) {
            itemIds = new ArrayList<>();
            itemIds.add("0");
            //            log.error("工单号:" + orderId + "该工单服务项目不在淘汽服务类别范围");
            //            return Result.wrapErrorResult("", "该工单服务项目不在淘汽服务类别范围");
        }
        baseCouponParam.setItemIds(itemIds);
        Map resultMap = remoteCouponHttp.couponCheck(baseCouponParam);
        if (resultMap == null) {
            return Result.wrapErrorResult("", "当前   优惠券无效");
        } else {
            String resultMapStr = new Gson().toJson(resultMap);
            Result result = new Gson().fromJson(resultMapStr, new TypeToken<Result>() {
            }.getType());
            return result;
        }
    }

    @Override
    public boolean settlePrint(Model model, Long shopId, Long orderId, String printType, ShopPrintConfigVO shopPrintConfigVO) {
        Shop shop = shopService.selectById(shopId);
        model.addAttribute("shop", shop);
        String printLog = OrderOperationLog.getOrderPrintLog(printType, shop);
        log.info(printLog);
        // 获取门店配置
        Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.SETTLEPRINT, shopId);
        String shopConfigureContent;
        if (shopConfigureOptional.isPresent()) {
            shopConfigureContent = shopConfigureOptional.get().getConfValue();
        } else {
            shopConfigureContent = "1、本公司的检验不承担用户提供配件的维修和已建议的维修故障而拒修的质量责任；<br />" +
                    "2、本维修工单内价格供参考，服务费用以实际结算为准。";
        }
        ShopConfigureVO configureVO = new ShopConfigureVO();
        configureVO.setSettleComment(shopConfigureContent);
        model.addAttribute("conf", configureVO);

        // 工单
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            return false;
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        //维修类别
        Long orderTypeId = orderInfo.getOrderType();
        if (orderTypeId > 0) {
            OrderType orderType = orderTypeService.selectById(orderTypeId);
            orderInfo.setOrderTypeName(orderType.getName());
        }

        // 获取车辆信息
        Optional<CustomerCar> customerCarOptional = customerCarService.getCustomerCar(orderInfo.getCustomerCarId());
        if (customerCarOptional.isPresent()) {
            CustomerCar customerCar = customerCarOptional.get();
            Customer customer = customerService.selectById(customerCar.getCustomerId());
            orderInfo = wrapperOrderFacade.wrapperOrderInfo(orderInfo, customerCar, customer);
        }
        // 工单信息
        model.addAttribute("orderInfo", orderInfo);

        // 工单物料
        Optional<List<OrderGoods>> orderGoodListOptional = orderGoodsService.getOrderGoodList(orderId, shopId);
        if (orderGoodListOptional.isPresent()) {
            model.addAttribute("orderGoodsList", orderGoodListOptional.get());
        }

        // 工单服务
        List<OrderServices> orderServicesList = orderServicesService.queryOrderServiceList(orderId, shopId);
        // 基本服务
        List<OrderServices> basicOrderServiceList = Lists.newArrayList();
        // 附加服务
        List<OrderServices> additionalOrderServiceList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                int serviceType = orderServices.getType();
                // 基本服务
                if (serviceType == OrderServiceTypeEnum.BASIC.getCode()) {
                    basicOrderServiceList.add(orderServices);
                }
                // 附加服务
                if (serviceType == OrderServiceTypeEnum.ANCILLARY.getCode()) {
                    additionalOrderServiceList.add(orderServices);
                }
            }
        }

        // 关联维修工名称
        List<OrderServicesVo> orderServicesVos = wrapperOrderFacade.orderServiceListReferWorderName(basicOrderServiceList);
        // 基本服务
        model.addAttribute("orderServicesList1", orderServicesVos);
        // 附加服务
        model.addAttribute("orderServicesList2", additionalOrderServiceList);

        model.addAttribute("orderAmountUpper", UpperNumbers.toChinese(orderInfo.getOrderAmount().toString()));

        // 查询收款和流水
        DebitBillAndFlowVo debitBillAndFlow = debitFacade.findDebitBillAndFlow(shopId, orderId);
        if (debitBillAndFlow != null) {
            DebitBillVo debitBillVo = debitBillAndFlow.getDebitBillVo();
            if (debitBillVo != null) {
                // 应收
                model.addAttribute("receivableAmountUpper", UpperNumbers.toChinese(debitBillVo.getReceivableAmount().toPlainString()));
                // 实收
                model.addAttribute("paidAmountUpper", UpperNumbers.toChinese(debitBillVo.getPaidAmount().toString()));
            }
            model.addAttribute("debitBill", debitBillVo);
            model.addAttribute("debitBillFlowList", debitBillAndFlow.getDebitBillFlowVoList());
        }

        //新版本打印
        if (null != shopPrintConfigVO && null != shopPrintConfigVO.getConfigFieldVO()) {
            ShopPrintConfigUtil.wrapperField(shopPrintConfigVO.getConfigFieldVO(), orderInfo);
            model.addAttribute("printConfigVO", shopPrintConfigVO);
        }
        //添加会员卡和计次卡信息
        List<AccountTradeFlow> tradeFlows = tradeFlowService.findComboDetail(shopId,orderId);
        //获取计次卡中对应消费的服务剩余信息
        if (!CollectionUtils.isEmpty(tradeFlows)) {
            List<AccountComboFlowDetail> comboFlowDetails = new ArrayList<>();
            for (AccountTradeFlow tradeFlow : tradeFlows) {
                //计次卡
                if (tradeFlow.getTradeType() == OrderTradeTypeEnum.METERCARD.getCode() || tradeFlow.getTradeType() == OrderTradeTypeEnum.OTHER.getCode()){
                    //根据tradeID 获取serviceID和 comboID
                    Map map = Maps.newHashMap();
                    map.put("accountTradeFlowId",tradeFlow.getId());
                    comboFlowDetails = comboFlowDetailService.getAccountComboFlowDetails(map);
                    //工单中使用的计次卡详情
                    model.addAttribute("comboFlowDetails",comboFlowDetails);
                    break;
                }
            }
            if (!CollectionUtils.isEmpty(comboFlowDetails)){
                List<AccountComboServiceRel> comboServiceList = new ArrayList<>();
                for (AccountComboFlowDetail comboFlowDetail : comboFlowDetails){
                   AccountComboServiceRel serviceRel = comboServiceRelService.findById(shopId,comboFlowDetail.getServiceId());
                    if (null != serviceRel){
                        comboServiceList.add(serviceRel);
                    }
                }
                if (!CollectionUtils.isEmpty(comboServiceList)) {
                    model.addAttribute("comboServiceList",comboServiceList);
                }
            }
        }

        //会员卡余额
        Long customerId = orderInfo.getCustomerId();
        if (null != customerId && customerId > 0){
            AccountInfo accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
            if (null != accountInfo) {
                List<MemberCard> memberCards = accountInfo.getMemberCards();
                if (Langs.isNotEmpty(memberCards)) {
                    model.addAttribute("memberCards", memberCards);
                }
            }
        }
        //二维码
        try {
            String userGidStr = shop.getUserGlobalId();
            if (StringUtils.isNotBlank(userGidStr)) {
                Long userGid = Long.valueOf(userGidStr);
                com.tqmall.core.common.entity.Result<ShopDTO> result = weChatShopService.selectShopByUcShopId(userGid);
                if (result.isSuccess()) {
                    String code = result.getData().getShopQrcode();
                    if (StringUtils.isNotBlank(code)) {
                        model.addAttribute("code", code);
                    }
                }
            }
        } catch (Exception e) {
            log.error("[调用微信端接口获取门店二维码失败]shopId :{}",shopId,e);
        }
        return true;
    }

    @Override
    public boolean sendCode(final SettlementSmsBO settlementSmsBO, final Long shopId) {
        return new BizTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(settlementSmsBO,"参数为空");
                Assert.notNull(settlementSmsBO.getLicense(),"车牌为空");
                boolean checkMobile = StringUtil.isMobileNO(settlementSmsBO.getMobile());
                Assert.isTrue(checkMobile, "手机号为空或格式有误");
                Assert.notNull(shopId,"shopId为空");
            }

            @Override
            protected Boolean process() throws BizException {
                //验证手机号是否在门店中存在
                String mobile = settlementSmsBO.getMobile();
                boolean isExistMobile = customerService.isExistMobile(shopId,mobile);
                if(!isExistMobile){
                    throw new BizException("门店中不存在此手机号码的客户,请检查");
                }
                Shop shop = shopService.selectById(shopId);
                Map<String, Object> smsMap = Maps.newHashMap();
                String sendCode = smsService.generateCode();
                String license = settlementSmsBO.getLicense();
                smsMap.put("code", sendCode);
                smsMap.put("license", license);
                smsMap.put("shopName", shop.getName());
                String tel = shop.getTel();
                if (StringUtils.isBlank(tel)) {
                    tel = shop.getMobile();
                }
                smsMap.put("tel", tel);
                SmsBase smsBase = new SmsBase(mobile, Constants.LEGEND_ORDER_SETTLEMENT_TPL, smsMap);
                Boolean isSuccess = smsService.sendMsg(smsBase, "使用其他客户的优惠券、会员卡，发送短信验证码");
                if (isSuccess) {
                    //放入缓存,key: legend_order_settlement_shopId_mobile (10分钟失效)
                    String key = getRedisKey(shopId, mobile);
                    jedisClient.set(key, CacheConstants.MOBILE_CODE_KEY_EXP_TIME, sendCode);
                    return true;
                } else {
                    throw new BizException("发送短信失败");
                }
            }
        }.execute();
    }

    /**
     * 校验验证码是否失效
     *
     * @param mobile
     * @param code
     * @param shopId
     * @return
     */
    @Override
    public boolean checkCode(final String mobile, final String code, final Long shopId) {
        return new BizTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(mobile, "手机号为空");
                Assert.isTrue(StringUtil.isMobileNO(mobile), "手机号格式有误");
                Assert.notNull(code, "验证码为空");
                Assert.notNull(shopId, "shopId为空");
            }

            @Override
            protected Boolean process() throws BizException {
                String key = getRedisKey(shopId, mobile);
                String checkCode = jedisClient.get(key, String.class);
                if(StringUtils.isBlank(checkCode)){
                    throw new BizException("验证码已失效");
                }
                if(!checkCode.equals(code)){
                    throw new BizException("验证码不正确，请重新输入！");
                }
                //验证通过后需要清空
                jedisClient.delete(key);
                return true;
            }
        }.execute();
    }

    /**
     * 获取验证码
     * @param shopId
     * @param mobile
     * @return
     */
    private String getRedisKey(Long shopId, String mobile) {
        StringBuilder key = new StringBuilder();
        key.append(Constants.LEGEND_ORDER_SETTLEMENT_TPL);
        key.append("_");
        key.append(shopId);
        key.append("_");
        key.append(mobile);
        return key.toString();
    }
}
