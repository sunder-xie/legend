package com.tqmall.legend.facade.wechat.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.HttpUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.dandelion.wechat.client.dto.wechat.ArticleDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ArticlePageDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ArticleParams;
import com.tqmall.dandelion.wechat.client.dto.wechat.AssessmentDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.AssessmentPageDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ButtonDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.CouponDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.CouponHistoryDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.CouponShareDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.CouponStatisticsDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.DataDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameCouponDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameCouponLogDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameCouponLogPageDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameCouponStatisticDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameCouponStatisticsDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameDetailsDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameStatisticDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameStatisticsDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.MenuDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.PagingParams;
import com.tqmall.dandelion.wechat.client.dto.wechat.PayFlowDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.PayTplDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.QrcodePageDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.QrcodeParams;
import com.tqmall.dandelion.wechat.client.dto.wechat.RescueDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.RescuePageDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopQrcodeRelDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.TemplateReplyDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.TemplateReplyPageDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.WechatWifiDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.WechatWifiPageDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.cardCoupon.CouponConfigDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.cardCoupon.CouponMapDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.cardCoupon.MembershipCardDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.cardCoupon.MembershipCardMapDTO;
import com.tqmall.dandelion.wechat.client.param.wechat.AssessmentParam;
import com.tqmall.dandelion.wechat.client.param.wechat.AuthorCheckParam;
import com.tqmall.dandelion.wechat.client.param.wechat.AuthorUrlParam;
import com.tqmall.dandelion.wechat.client.param.wechat.CouponStatisticsParam;
import com.tqmall.dandelion.wechat.client.param.wechat.GameCouponUserLogParam;
import com.tqmall.dandelion.wechat.client.param.wechat.GameSelectParam;
import com.tqmall.dandelion.wechat.client.param.wechat.RescueParam;
import com.tqmall.dandelion.wechat.client.param.wechat.ShareStatisticsParam;
import com.tqmall.dandelion.wechat.client.param.wechat.TemplateReplyParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.QueryCardParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.QueryCouponParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.SaveCardParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.SaveCouponParam;
import com.tqmall.dandelion.wechat.client.wechat.article.WeChatArticleService;
import com.tqmall.dandelion.wechat.client.wechat.assessment.WeChatAssessmentService;
import com.tqmall.dandelion.wechat.client.wechat.coupon.WeChatCouponConfigService;
import com.tqmall.dandelion.wechat.client.wechat.coupon.WeChatCouponHistoryService;
import com.tqmall.dandelion.wechat.client.wechat.coupon.WeChatCouponService;
import com.tqmall.dandelion.wechat.client.wechat.coupon.WeChatUserCouponLogService;
import com.tqmall.dandelion.wechat.client.wechat.data.DataService;
import com.tqmall.dandelion.wechat.client.wechat.game.WechatGameService;
import com.tqmall.dandelion.wechat.client.wechat.menu.ShopMenuService;
import com.tqmall.dandelion.wechat.client.wechat.pay.WeChatPayService;
import com.tqmall.dandelion.wechat.client.wechat.qrcode.WeChatQrcodeService;
import com.tqmall.dandelion.wechat.client.wechat.rescue.WeChatRescueService;
import com.tqmall.dandelion.wechat.client.wechat.shop.WeChatShopService;
import com.tqmall.dandelion.wechat.client.wechat.templatereply.TemplateReplyService;
import com.tqmall.dandelion.wechat.client.wechat.vipCard.WeChatMembershipCardConfigService;
import com.tqmall.dandelion.wechat.client.wechat.wifi.WifiService;
import com.tqmall.holy.provider.entity.customer.CustomerJoinAuditDTO;
import com.tqmall.holy.provider.service.crm.RpcCustomerCommonService;
import com.tqmall.legend.biz.account.AccountCouponService;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.CouponInfoService;
import com.tqmall.legend.biz.account.MemberCardInfoService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.pay.OnlinePayService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.support.common.CRMResult;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.ComboInfo;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.shop.CustomerJoinAudit;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.coupon.CouponInfoUseRangeEnum;
import com.tqmall.legend.enums.onlinepay.BusTypeEnum;
import com.tqmall.legend.enums.shop.ShopLevelEnum;
import com.tqmall.legend.enums.wechat.ShopWechatStatusEnum;
import com.tqmall.legend.enums.wechat.WechatActModuleTypeEnum;
import com.tqmall.legend.facade.account.AccountFacadeService;
import com.tqmall.legend.facade.activity.vo.GameActivityDetailVo;
import com.tqmall.legend.facade.activity.vo.GameCouponStatisticsVo;
import com.tqmall.legend.facade.activity.vo.ShopActivityVo;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.facade.service.vo.SaveShopServiceInfoVo;
import com.tqmall.legend.facade.wechat.WechatFacade;
import com.tqmall.legend.facade.wechat.vo.AssessmentVo;
import com.tqmall.legend.facade.wechat.vo.RescueVo;
import com.tqmall.legend.facade.wechat.vo.ShopWechatVo;
import com.tqmall.legend.facade.wechat.vo.WechatAccountCouponVo;
import com.tqmall.legend.facade.wechat.vo.WechatArticleVo;
import com.tqmall.legend.facade.wechat.vo.WechatCfgCouponVo;
import com.tqmall.legend.facade.wechat.vo.WechatFavormallCardVo;
import com.tqmall.legend.facade.wechat.vo.WechatFavormallCouponVo;
import com.tqmall.legend.facade.wechat.vo.WechatMenuVo;
import com.tqmall.legend.object.param.onlinepay.CallbackParam;
import com.tqmall.legend.rpc.crm.CrmCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wushuai on 16/6/6.
 */
@Slf4j
@Service
public class WechatFacadeImpl implements WechatFacade {

    @Autowired
    private WeChatShopService weChatShopService;
    @Autowired
    private WeChatPayService weChatPayService;
    @Autowired
    private WeChatArticleService weChatArticleService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OnlinePayService onlinePayService;
    @Autowired
    private SnFactory snFactory;
    @Autowired
    private RpcCustomerCommonService rpcCustomerCommonService;
    @Autowired
    private TemplateReplyService templateReplyService;
    @Autowired
    private WifiService wifiService;
    @Autowired
    private WeChatQrcodeService weChatQrcodeService;
    @Autowired
    private DataService dataService;
    @Autowired
    private ShopMenuService shopMenuService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private WeChatCouponConfigService weChatCouponConfigService;
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private WeChatCouponHistoryService weChatCouponHistoryService;
    @Autowired
    private WeChatUserCouponLogService weChatUserCouponLogService;
    @Autowired
    private WechatGameService wechatGameService;
    @Autowired
    private AccountFacadeService accountFacadeService;
    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;
    @Autowired
    private WeChatAssessmentService weChatAssessmentService;
    @Autowired
    private WeChatRescueService weChatRescueService;
    @Autowired
    private WeChatMembershipCardConfigService weChatMembershipCardConfigService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;
    @Autowired
    private WeChatCouponService weChatCouponService;
    @Autowired
    private CrmCustomerService crmCustomerService;
    @Override
    public CustomerJoinAuditDTO getShopInfoFromCrm(Long shopId,Integer shopLevel) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return null;
        }
        com.tqmall.core.common.entity.Result<CustomerJoinAuditDTO> customerJoinAuditDTOResult = null;
        if(shopLevel!=null && ShopLevelEnum.YUNXIU.getValue().intValue()==shopLevel){
            customerJoinAuditDTOResult = rpcCustomerCommonService.getAuditDTOByCustomerId(userGlobalId);
        } else if(shopLevel!=null && ShopLevelEnum.TQMALL.getValue().intValue()==shopLevel){
            customerJoinAuditDTOResult = rpcCustomerCommonService.getWechartCustomerDTOByCustomerId(userGlobalId);
        }
        log.info("[微信公众号-dubbo]调crm接口查询门店注册资料{}", LogUtils.funToString(userGlobalId, customerJoinAuditDTOResult));
        if(customerJoinAuditDTOResult==null){
            return null;
        }
        return customerJoinAuditDTOResult.getData();
    }

    @Override
    public Result initShopWechatInfo(Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        //先查询门店资料是否已经存在
        Result<ShopWechatVo> shopWechatVoResult = qryShopWechat(shopId);
        if (shopWechatVoResult.isSuccess() && shopWechatVoResult.getData() != null) {
            log.info("[微信公众号-dubbo]shopIdd:{},dl-wechat端门店信息已存在,直接返回成功",shopId);
            return Result.wrapSuccessfulResult("成功");
        }
        Shop shop = shopService.selectById(shopId);
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setUcShopId(userGlobalId);
        shopDTO.setShopName(shop.getName());
        Integer shopLevel = shop.getLevel();
        if(shopLevel!=null && ShopLevelEnum.TQMALL.getValue().intValue()==shopLevel){
            //档口版直接到待授权状态
            shopDTO.setShopStatus(ShopWechatStatusEnum.TO_GRANT.getValue());
        } else{
            shopDTO.setShopStatus(ShopWechatStatusEnum.SUBMITTED.getValue());
        }
        shopDTO.setShopMobile(shop.getMobile());
        shopDTO.setUserName(shop.getContact());
        shopDTO.setShopProvince(shop.getProvince());
        shopDTO.setShopCity(shop.getCity());
        shopDTO.setShopDistrict(shop.getDistrict());
        shopDTO.setShopStreet(shop.getStreet());
        shopDTO.setShopAddress(shop.getAddress());
        shopDTO.setShopLevel(shop.getLevel());
        shopDTO.setSigningTime(new Date());//签约时间默认当前
        shopDTO.setLegendShopStatus(shop.getShopStatus());
        log.info("wechat新增shop,uc_shop_id:{},shop_status:{}",userGlobalId,shop.getShopStatus());
        try {
            CustomerJoinAuditDTO customerJoinAuditDTO = getShopInfoFromCrm(shopId,shopLevel);
            if (customerJoinAuditDTO != null) {
                shopDTO.setPerWechatid(customerJoinAuditDTO.getWeixin());//个人微信号
                shopDTO.setShopEmail(customerJoinAuditDTO.getEmail());//邮箱
                shopDTO.setShopTelephone(customerJoinAuditDTO.getMobilephone());//座机号码
                shopDTO.setOwnerIdcard(customerJoinAuditDTO.getLegalPersonCardNo());//身份证号码
                shopDTO.setCompanyName(customerJoinAuditDTO.getBusinessName());//个体工商户名称/企业全称
                shopDTO.setChargePerson(customerJoinAuditDTO.getBusinessLegalPerson());//经营者姓名/法定代表人
                shopDTO.setBizLicense(customerJoinAuditDTO.getBusinessLicense());//工商执照注册号
                shopDTO.setOrgCode(customerJoinAuditDTO.getOrgCode());//组织机构代码
                if (!CollectionUtils.isEmpty(customerJoinAuditDTO.getBusinessUrl())) {
                    shopDTO.setBizLicenseImg(customerJoinAuditDTO.getBusinessUrl().get(0));//营业执照url,暂定只取第一张
                }
                if (!CollectionUtils.isEmpty(customerJoinAuditDTO.getOrgUrl())) {
                    shopDTO.setOrgCodeImg(customerJoinAuditDTO.getOrgUrl().get(0));//组织机构url,暂定只取第一张
                }
                shopDTO.setExpirationTime(customerJoinAuditDTO.getOpenAccountExpiredTime());
            }
        } catch (Exception e) {
            log.error("[微信公众号-dubbo]调用crm接口获取门店资料出现异常:{}", e);
        }

        com.tqmall.core.common.entity.Result<String> addShopResult = weChatShopService.addShop(shopDTO);
        log.info("[微信公众号-dubbo]门店微信资料初始化入参:{},success:{}", LogUtils.objectToString(shopDTO),addShopResult.isSuccess());
        if (!addShopResult.isSuccess()) {
            return Result.wrapErrorResult("", "门店微信资料初始化失败");
        } else {
            return Result.wrapSuccessfulResult(addShopResult.getData());
        }
    }

    @Override
    public Result<ShopWechatVo> qryShopWechat(Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,查询失败");
        }
        com.tqmall.core.common.entity.Result<ShopDTO> result = weChatShopService.selectShopByUcShopId(userGlobalId);
        log.info("[微信公众号-dubbo]查询门店微信资料入参:{},success:{}", userGlobalId,result.isSuccess());
        if (result.isSuccess()) {
            if (result.getData() == null) {
                return Result.wrapSuccessfulResult(null);
            } else {
                ShopWechatVo shopWechatVo = new ShopWechatVo();
                BeanUtils.copyProperties(result.getData(), shopWechatVo);
                return Result.wrapSuccessfulResult(shopWechatVo);
            }
        } else {
            return Result.wrapErrorResult("", result.getMessage());
        }
    }

    @Override
    public Result<String> regPayByZhifubao(Long payTplId, UserInfo userInfo, String backUrl) {
        Long shopId = userInfo.getShopId();
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }


        Result initResult = initShopWechatInfo(shopId);
        if (!initResult.isSuccess()) {
            return initResult;
        }
        //根据payTplId获取支付模板信息
        PagingParams payTplParams = new PagingParams();
        payTplParams.setLimit(1L);
        payTplParams.setOffset(0L);
        payTplParams.setTplId(payTplId);
        Result<List<PayTplDTO>> payTplDTOListResult = qryPayTpl(payTplParams);
        log.info("[微信公众号-dubbo]查询支付模板{}", LogUtils.funToString(payTplParams, payTplDTOListResult));
        if (!payTplDTOListResult.isSuccess() || CollectionUtils.isEmpty(payTplDTOListResult.getData())) {
            return Result.wrapErrorResult("", "查询不到支付模板信息,支付失败");
        }
        PayTplDTO payTplDTO = payTplDTOListResult.getData().get(0);
        BigDecimal payFee = payTplDTO.getChargeMoney();
        String subjectName = payTplDTO.getTplName();//商品名称
        String orderSn = snFactory.generatePayLogSn(shopId);
        //得到支付宝form表单
        Result<String> formResult = onlinePayService.payByZhifubaoCommon(orderSn, payFee, backUrl, subjectName, BusTypeEnum.WECHAT.getBusType());
        if (!formResult.isSuccess()) {
            return formResult;
        }
        //插支付流水
        Result addPayFlowResult = addPayFlow(orderSn, userGlobalId, payTplId, "1", null);
        if (!addPayFlowResult.isSuccess()) {
            return addPayFlowResult;
        }
        return formResult;
    }

    @Override
    public Result saveWechatInfo(ShopWechatVo shopWechatVo,Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        if (shopWechatVo == null || shopWechatVo.getId() == null) {
            log.error("[微信公众号-dubbo]更新门店微信资料失败的入场有误,入参{}", ObjectUtils.objectToJSON(shopWechatVo));
        }
        ShopDTO shopDTO = new ShopDTO();
        BeanUtils.copyProperties(shopWechatVo, shopDTO);
        shopDTO.setUcShopId(userGlobalId);
        shopDTO.setLegendShopStatus(shopDTO.getShopStatus());

        try {
            CustomerJoinAudit customerJoinAudit = crmCustomerService.showShopInformation(userGlobalId);
            String saMobilephone = customerJoinAudit.getSaMobilephone();
            if(StringUtils.isNotEmpty(saMobilephone)){
                shopDTO.setSaTelephone(saMobilephone);
            }else{
                shopDTO.setSaTelephone(shopDTO.getShopTelephone());
            }
        } catch (BizException e) {
            log.error(e.getErrorMessage(),e);
        } catch (Exception e) {
            log.error("[调用crm-dubbo接口失败]:",e);
        }
        com.tqmall.core.common.entity.Result<String> result = weChatShopService.updateShop(shopDTO);
        log.info("[微信公众号-dubbo]更新门店微信资料入参:{},success:{}", LogUtils.objectToString(shopDTO),result.isSuccess());
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult("", result.getMessage());
        }
    }

    @Override
    public Result regPayByOffline(Long payTplId, String payVoucher, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        Result initResult = initShopWechatInfo(shopId);
        if (!initResult.isSuccess()) {
            return initResult;
        }
        String orderSn = snFactory.generatePayLogSn(shopId);
        Result addPayFlowResult = addPayFlow(orderSn, userGlobalId, payTplId, "2", payVoucher);
        return addPayFlowResult;
    }

    @Override
    public Result addPayFlow(String orderSn, Long userGlobalId, Long payTplId, String payWay, String payVoucher) {
        //根据payTplId获取支付模板信息
        PagingParams payTplParams = new PagingParams();
        payTplParams.setLimit(1L);
        payTplParams.setOffset(0L);
        payTplParams.setTplId(payTplId);
        Result<List<PayTplDTO>> payTplDTOListResult = qryPayTpl(payTplParams);
        log.info("[微信公众号-dubbo]查询支付模板入参:{},success:{}",LogUtils.objectToString(payTplParams),payTplDTOListResult.isSuccess());
        if (!payTplDTOListResult.isSuccess() || CollectionUtils.isEmpty(payTplDTOListResult.getData())) {
            return Result.wrapErrorResult("", "查询不到支付模板信息,支付失败");
        }
        PayTplDTO payTplDTO = payTplDTOListResult.getData().get(0);
        PayFlowDTO payFlowDTO = new PayFlowDTO();
        payFlowDTO.setOrderSn(orderSn);
        payFlowDTO.setPayFee(payTplDTO.getChargeMoney());
        payFlowDTO.setPayInfo(payTplDTO.getAdpatType().toString());
        payFlowDTO.setPayWay(payWay);//1.支付宝支付2线下支付
        payFlowDTO.setUcShopId(userGlobalId);
        payFlowDTO.setPayStatus(0);//0 未支付,1 支付成功, 2 支付失败
        if ("2".equals(payWay)) {//2.线下支付,设置支付凭证
            payFlowDTO.setPayVoucher(payVoucher);
        }
        com.tqmall.core.common.entity.Result<String> addPayFlowResult = weChatPayService.addPayFlow(payFlowDTO);
        log.info("[微信公众号-dubbo]支付流水记录入参:{},success:{}", LogUtils.objectToString(payFlowDTO),addPayFlowResult.isSuccess());
        if (!addPayFlowResult.isSuccess()) {
            return Result.wrapErrorResult("", "操作失败");
        } else {
            return Result.wrapSuccessfulResult("成功");
        }
    }

    @Override
    public Result<List<PayTplDTO>> qryPayTpl(PagingParams payTplParams) {
        com.tqmall.core.common.entity.Result<List<PayTplDTO>> payTplDTOListResult = weChatPayService.queryPayTpl(payTplParams);
        log.info("[微信公众号-dubbo]查询支付模板入参:{},success:{}", LogUtils.objectToString(payTplParams),payTplDTOListResult.isSuccess());
        if (!payTplDTOListResult.isSuccess()) {
            return Result.wrapErrorResult("", "支付模板信息失败");
        } else {
            return Result.wrapSuccessfulResult(payTplDTOListResult.getData());
        }
    }

    @Override
    public com.tqmall.core.common.entity.Result<String> callBackPayFlow(CallbackParam param) {
        PayFlowDTO payFlowDTO = new PayFlowDTO();
        payFlowDTO.setOrderSn(param.getOrderSn());
        payFlowDTO.setPaySn(param.getPayNo());
        payFlowDTO.setBackTime(new Date());
        payFlowDTO.setPayFee(param.getPayFee());
        if (param.getPaySuccess()) {
            payFlowDTO.setPayStatus(1);//1.支付成功
        } else {
            payFlowDTO.setPayStatus(2);//2.支付失败
        }
        com.tqmall.core.common.entity.Result<String> callBackPayFlowResult = weChatPayService.callBackPayFlow(payFlowDTO);
        log.error("[微信公众号-dubbo]在线支付结果回写入参:{},success:{}", LogUtils.objectToString(payFlowDTO), callBackPayFlowResult.isSuccess());
        if (callBackPayFlowResult.isSuccess()) {
            return com.tqmall.core.common.entity.Result.wrapSuccessfulResult("回调成功");
        } else {
            return com.tqmall.core.common.entity.Result.wrapErrorResult("", "回调失败");
        }
    }

    @Override
    public Result<PayFlowDTO> qryRegPayInfo(Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        com.tqmall.core.common.entity.Result<PayFlowDTO> payFlowResult = weChatPayService.queryPayDatailsByShopId(userGlobalId);
        log.info("[微信公众号-dubbo]查询门店支付信息入参:{},success:{}", LogUtils.objectToString(userGlobalId), payFlowResult.isSuccess());
        if (payFlowResult.isSuccess()) {
            return Result.wrapSuccessfulResult(payFlowResult.getData());
        } else {
            return Result.wrapErrorResult("", "查询门店支付信息失败");
        }
    }

    @Override
    public Result<Page<WechatArticleVo>> queryArticlesPage(ArticleParams articleParams, Long shopId) {
        if (articleParams == null || articleParams.getLimit() == null || articleParams.getLimit() < 1
                || articleParams.getOffset() == null || articleParams.getOffset() < 0) {
            log.error("[微信公众号-dubbo]查询文章列表查询参数有误{}", articleParams);
            return Result.wrapErrorResult("", "表查询参数有误");
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        articleParams.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<ArticlePageDTO> articlePageResult = weChatArticleService.queryArticles(articleParams);
        log.info("[微信公众号-dubbo]查询文章列表入参:{},success:{}", LogUtils.objectToString(articleParams),articlePageResult.isSuccess());
        if (articlePageResult.isSuccess()) {
            ArticlePageDTO articlePageDTO = articlePageResult.getData();
            Page<WechatArticleVo> page = null;
            if (articlePageDTO != null) {
                Long pageNum = articleParams.getOffset() / articleParams.getLimit();
                PageRequest pageRequest = new PageRequest(pageNum.intValue(), articleParams.getLimit().intValue());
                long totalSize = articlePageDTO.getTotalNum();
                List<ArticleDTO> articleDTOList = articlePageDTO.getArticleDTOList();
                List<WechatArticleVo> wechatArticleVoList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(articleDTOList)) {
                    for (ArticleDTO articleDTO : articleDTOList) {
                        WechatArticleVo wechatArticleVo = new WechatArticleVo();
                        BeanUtils.copyProperties(articleDTO, wechatArticleVo);
                        wechatArticleVoList.add(wechatArticleVo);
                    }
                }
                page = new DefaultPage(wechatArticleVoList, pageRequest, totalSize);
            }
            return Result.wrapSuccessfulResult(page);
        } else {
            return Result.wrapErrorResult("", articlePageResult.getMessage());
        }
    }

    @Override
    public Result<Integer> canSendArticleCount(Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        ArticleParams articleParams = new ArticleParams();
        articleParams.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<Integer> canSenountResult = weChatArticleService.canSendArticleNum(articleParams);
        log.info("[微信公众号-dubbo]查询本月可发送文章数入参:{},success:{}", LogUtils.objectToString(articleParams),canSenountResult.isSuccess());
        if (canSenountResult.isSuccess()) {
            return Result.wrapSuccessfulResult(canSenountResult.getData());
        } else {
            return Result.wrapErrorResult("", canSenountResult.getMessage());
        }
    }

    @Override
    public Result<String> sendArticle(Long shopAricleRelId, Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        ArticleParams articleParams = new ArticleParams();
        articleParams.setShopAricleRelId(shopAricleRelId);
        articleParams.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<String> sendResult = weChatArticleService.sendArticle(articleParams);
        log.info("[微信公众号-dubbo]发送文章入参:{},success:{}", LogUtils.objectToString(articleParams), sendResult.isSuccess());
        if (sendResult.isSuccess()) {
            return Result.wrapSuccessfulResult("成功");
        } else {
            return Result.wrapErrorResult("", "发送失败["+sendResult.getCode()+"]");
        }
    }

    @Override
    public Result<Integer> saveReplyMsg(TemplateReplyDTO templateReplyDTO, Long shopId) {
        if (templateReplyDTO == null) {
            log.error("[微信公众号-dubbo]入参为空,保存自动回复消息失败");
            return Result.wrapErrorResult("", "保存自动回复消息失败");
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        templateReplyDTO.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<Integer> saveResult = null;
        if (templateReplyDTO.getId() != null) {
            //更新
            saveResult = templateReplyService.update(templateReplyDTO);
        } else {
            //新增
            saveResult = templateReplyService.add(templateReplyDTO);
        }
        log.info("[微信公众号-dubbo]保存自动回复消息入参{},success:{}", LogUtils.objectToString(templateReplyDTO),saveResult.isSuccess());
        if (saveResult.isSuccess()) {
            return Result.wrapSuccessfulResult(saveResult.getData());
        } else {
            return Result.wrapErrorResult("", saveResult.getMessage());
        }
    }

    @Override
    public Result<WechatWifiDTO> getWifiByShopId(Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        com.tqmall.core.common.entity.Result<WechatWifiPageDTO> wechatWifiDTOResult = wifiService.getWifiByShopId(userGlobalId, 0L, 1L);
        log.info("[微信公众号-dubbo]查询门店wifi,入参:{},success:{}", LogUtils.objectToString(userGlobalId),wechatWifiDTOResult.isSuccess());
        if (wechatWifiDTOResult.isSuccess()) {
            if (wechatWifiDTOResult.getData() != null && !CollectionUtils.isEmpty(wechatWifiDTOResult.getData().getWechatWifiDTOList())) {
                return Result.wrapSuccessfulResult(wechatWifiDTOResult.getData().getWechatWifiDTOList().get(0));
            } else {
                return Result.wrapSuccessfulResult(null);
            }
        } else {
            return Result.wrapErrorResult("", wechatWifiDTOResult.getMessage());
        }
    }

    @Override
    public Result<Integer> saveWifi(WechatWifiDTO wechatWifiDTO, Long shopId) {
        if (wechatWifiDTO == null) {
            log.error("[微信公众号-dubbo]入参为空,保存门店wifi失败");
            return Result.wrapErrorResult("", "保存wifi失败");
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        wechatWifiDTO.setShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<Integer> result = null;
        if (wechatWifiDTO.getId() != null) {
            //更新
            result = wifiService.updateWifi(wechatWifiDTO);
        } else {
            //新增
            result = wifiService.addWifi(wechatWifiDTO);
        }
        log.info("[微信公众号-dubbo]保存门店wifi{},入参:{},success:{}", LogUtils.objectToString(wechatWifiDTO),result.isSuccess());
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult("", result.getMessage());
        }
    }

    @Override
    public Result<Page<TemplateReplyDTO>> qryReplyMsgPage(TemplateReplyParam templateReplyParam, Long shopId, Long offset, Long limit) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        templateReplyParam.setShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<TemplateReplyPageDTO> templateReplyPageDTOResult = templateReplyService.getsByShopIdAndReplyStatusAndkeyword(templateReplyParam, offset, limit);
        log.info("[微信公众号-dubbo]查询字段回复消息列表,入参:{},success:{}", LogUtils.objectToString(templateReplyParam),templateReplyPageDTOResult.isSuccess());
        if (templateReplyPageDTOResult.isSuccess()) {
            Long pageNum = offset / limit;
            PageRequest pageRequest = new PageRequest(pageNum.intValue(), limit.intValue());
            Page<TemplateReplyDTO> page = new DefaultPage(new ArrayList(), pageRequest, 0);//初始化数据
            TemplateReplyPageDTO templateReplyPageDTO = templateReplyPageDTOResult.getData();
            if (templateReplyPageDTO != null && !CollectionUtils.isEmpty(templateReplyPageDTO.getTemplateReplyDTOList())) {
                int totalSize = templateReplyPageDTO.getTotalNum();
                page = new DefaultPage(templateReplyPageDTO.getTemplateReplyDTOList(), pageRequest, totalSize);
            }
            return Result.wrapSuccessfulResult(page);
        } else {
            return Result.wrapErrorResult("", templateReplyPageDTOResult.getMessage());
        }
    }

    @Override
    public Result<Page<ShopQrcodeRelDTO>> qryQrcodePage(QrcodeParams qrcodeParams, Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        qrcodeParams.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<QrcodePageDTO> qrcodePageDTOResult = weChatQrcodeService.queryQrcode(qrcodeParams);
        log.info("[微信公众号-dubbo]查询微信二维码入参:{},success:{}", LogUtils.objectToString(qrcodeParams),qrcodePageDTOResult.isSuccess());
        if (qrcodePageDTOResult.isSuccess()) {
            Long pageNum = qrcodeParams.getOffset() / qrcodeParams.getLimit();
            PageRequest pageRequest = new PageRequest(pageNum.intValue(), qrcodeParams.getLimit().intValue());
            Page<ShopQrcodeRelDTO> page = new DefaultPage(new ArrayList(), pageRequest, 0);//初始化数据;
            QrcodePageDTO qrcodePageDTO = qrcodePageDTOResult.getData();
            if (qrcodePageDTO != null && !CollectionUtils.isEmpty(qrcodePageDTO.getQrcodeDTOList())) {
                int totalSize = qrcodePageDTO.getTotalNum();
                page = new DefaultPage(qrcodePageDTO.getQrcodeDTOList(), pageRequest, totalSize);
            }
            return Result.wrapSuccessfulResult(page);
        } else {
            return Result.wrapErrorResult("", qrcodePageDTOResult.getMessage());
        }
    }

    @Override
    public WechatMenuVo qryWechatMenu(Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return null;
        }
        WechatMenuVo wechatMenuVo = new WechatMenuVo();
        List<ButtonDTO> customMenu = null;//当前有效菜单(实例菜单)
        List<ButtonDTO> defaultMenu = null;//管理后台默认菜单
        List<ButtonDTO> customNoUseMenu = null;//用户自定义未使用菜单
        //1.查询菜单实例
        com.tqmall.core.common.entity.Result<MenuDTO> customMenuResult = shopMenuService.getByShopId(userGlobalId);
        log.info("[微信公众号-dubbo]查询微信菜单实例shopId:{},success:{}", shopId,customMenuResult.isSuccess());
        if (customMenuResult.isSuccess()) {
            if(customMenuResult.getData()!=null){
                customMenu = customMenuResult.getData().getButton();
            }
        }
        //2.查询默认菜单配置
        com.tqmall.core.common.entity.Result<List<ButtonDTO>> defaultMenuResult = shopMenuService.getDefaultMenu(userGlobalId);
        log.info("[微信公众号-dubbo]查询微信默认菜单shopId:{},success:{}", shopId,defaultMenuResult.isSuccess());
        if(defaultMenuResult.isSuccess()){
            defaultMenu = defaultMenuResult.getData();
        }
        //3.查询自定义未使用菜单
        com.tqmall.core.common.entity.Result<List<ButtonDTO>> customNoUseMenuResult = shopMenuService.getNoUseCustomMenu(userGlobalId);
        log.info("[微信公众号-dubbo]查询自定义未使用微信菜单shopId:{},success:{}", shopId,customNoUseMenuResult.isSuccess());
        if(customNoUseMenuResult.isSuccess()){
            customNoUseMenu = customNoUseMenuResult.getData();
        }
        //判断默认菜单是否已被选中
        if(!CollectionUtils.isEmpty(defaultMenu)&& !CollectionUtils.isEmpty(customMenu)){
            for(ButtonDTO defaultBtn :defaultMenu){
                defaultBtn.setMenuType(0);//0.表示系统默认菜单
                for(ButtonDTO customBtn:customMenu){
                    if(defaultBtn.getKKey()!=null && defaultBtn.getKKey().equals(customBtn.getKKey())
                            && defaultBtn.getMenuUrl()!=null && defaultBtn.getMenuUrl().equals(customBtn.getMenuUrl())){
                        defaultBtn.setDefaultMenuIsUsed(1);//已使用
                    }
                }
            }
        }
        wechatMenuVo.setCustomMenu(customMenu);
        wechatMenuVo.setDefaultMenu(defaultMenu);
        wechatMenuVo.setCustomNoUseMenu(customNoUseMenu);
        return wechatMenuVo;
    }

    @Override
    public Result<MenuDTO> saveWechatMenu(MenuDTO menuDTO,Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return null;
        }
        menuDTO.setUcShopId(userGlobalId);
        List<ButtonDTO> buttonList = menuDTO.getButton();
        if(!CollectionUtils.isEmpty(buttonList)){
            //按照wIndex生序排序
            Collections.sort(buttonList, new Comparator<ButtonDTO>() {
                @Override
                public int compare(ButtonDTO o1, ButtonDTO o2) {
                    if(o1.getWIndex() !=null ||o2.getWIndex()!=null){
                        return o1.getWIndex().compareTo(o2.getWIndex());
                    }
                    return 0;
                }
            });
            long i1 = 11;
            long i2 = 21;
            long i3 = 31;
            for (ButtonDTO buttonDTO : buttonList) {
                Long wIndex = buttonDTO.getWIndex();
                if(wIndex!=null){
                    if(wIndex>10 &&wIndex<20){
                        buttonDTO.setWIndex(i1++);
                    } else if(wIndex>20 && wIndex<30){
                        buttonDTO.setWIndex(i2++);
                    } else if(wIndex>30 && wIndex<40){
                        buttonDTO.setWIndex(i3++);
                    }
                }
            }
        }
        com.tqmall.core.common.entity.Result<MenuDTO> menuDTOResult = shopMenuService.update(menuDTO);
        log.info("[微信公众号-dubbo]保存微信菜单设置入参:{},success:{}", LogUtils.objectToString(menuDTO),menuDTOResult.isSuccess());
        if (menuDTOResult.isSuccess()) {
            return Result.wrapSuccessfulResult(menuDTOResult.getData());
        } else {
            return Result.wrapErrorResult("", menuDTOResult.getMessage());
        }
    }

    @Override
    public DataDTO qryHomeData(Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return null;
        }
        com.tqmall.core.common.entity.Result<DataDTO> dataDTOResult = dataService.queryData(userGlobalId);
        log.info("[微信公众号-dubbo]查询微信首页数据shopId:{},success:{}", shopId,dataDTOResult.isSuccess());
        if (dataDTOResult.isSuccess()) {
            return dataDTOResult.getData();
        } else {
            return null;
        }
    }

    public Result<Integer> deleteReplyMsg(Long msgId) {
        com.tqmall.core.common.entity.Result<Integer> delResult = templateReplyService.delete(msgId);
        log.info("[微信公众号-dubbo]删除自动回复消息msgId:{},success:{}", msgId,delResult.isSuccess());
        if (delResult.isSuccess()) {
            return Result.wrapSuccessfulResult(delResult.getData());
        } else {
            return Result.wrapErrorResult("",delResult.getMessage());
        }
    }

    @Override
    public Result<Page<WechatAccountCouponVo>> qryAccountCouponList(Map<String, Object> searchParams, Pageable pageable) {
        Long shopId = Long.valueOf(searchParams.get("shopId").toString());
        if(shopId==null){
            return Result.wrapErrorResult("-1","门店信息缺失,不能查询");
        }
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
        int limit = pageable.getPageSize();
        int pageNum = offset /limit;//从0开始
        PageRequest pageRequest = new PageRequest(pageNum, limit);
        Page<WechatAccountCouponVo> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        Long accountId = null;
        if(searchParams.get("mobile")!=null){
            String mobile = searchParams.get("mobile").toString();
            List<Customer> customerList = customerService.getCustomerByMobile(mobile,shopId);
            if(CollectionUtils.isEmpty(customerList)){
                return Result.wrapSuccessfulResult(page);
            }
            Customer customer = customerList.get(0);
            AccountInfo accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId,customer.getId());
            if(accountInfo==null || accountInfo.getId()==null){
                return Result.wrapSuccessfulResult(page);
            }
            accountId = accountInfo.getId();
        }
        if(accountId!=null){
            searchParams.put("accountId",accountId);
        }
        int totalCount = accountCouponService.selectCount(searchParams);
        searchParams.put("offset",offset);
        searchParams.put("limit", limit);
        List<WechatAccountCouponVo> accountCouponList = accountCouponService.qryAccountCoupon(searchParams);
        if(CollectionUtils.isEmpty(accountCouponList)){
            return Result.wrapSuccessfulResult(page);
        }
        Set<String> mobiles = new HashSet<>();
        for (WechatAccountCouponVo wechatAccountCouponVo : accountCouponList) {
            if (StringUtils.isNotEmpty(wechatAccountCouponVo.getMobile())){
                mobiles.add(wechatAccountCouponVo.getMobile());
            }
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("-1","却少门店信息");
        }
        com.tqmall.core.common.entity.Result<Map<String, CouponShareDTO>> ddlresult = null;
        if(userGlobalId!=null &&!CollectionUtils.isEmpty(mobiles)){
            ShareStatisticsParam shareStatisticsParam = new ShareStatisticsParam();
            shareStatisticsParam.setShopId(userGlobalId);
            shareStatisticsParam.setMobiles(new ArrayList(mobiles));
            ddlresult = weChatUserCouponLogService.getShareData(shareStatisticsParam);
            log.info("[consumer-ddlwechat-dubbo]查询用户分享次数,分享注册用户数:入参{},success:{}",LogUtils.objectToString(shareStatisticsParam),ddlresult.isSuccess());
        }
        Map<String, CouponShareDTO> retMap = null;
        if(ddlresult!=null){
            retMap = ddlresult.getData();
        }
        if(retMap!=null){
            for (WechatAccountCouponVo wechatAccountCouponVo : accountCouponList) {
                String mobile = wechatAccountCouponVo.getMobile();
                CouponShareDTO couponShareDTO = retMap.get(mobile);
                if(couponShareDTO!=null){
                    wechatAccountCouponVo.setShareCount(couponShareDTO.getShareCount());
                    wechatAccountCouponVo.setRegisterCount(couponShareDTO.getRegisterCount());
                }
            }
        }
        page = new DefaultPage(accountCouponList, pageRequest, totalCount);
        return Result.wrapSuccessfulResult(page);
    }

    @Override
    public Result<WechatCfgCouponVo> qryWechatCouponCfg(Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("-1","门店信息欠缺,操作失败,请联系客服人员");
        }
        com.tqmall.core.common.entity.Result<CouponDTO> result = weChatCouponConfigService.selectCouponConfigByShopId(userGlobalId);
        log.info("[微信公众号-dubbo]查询关注送券配置shopId:{},success:{}",shopId,result.isSuccess());
        if(!result.isSuccess()){
            return Result.wrapErrorResult("-1",result.getMessage());
        }
        CouponDTO couponDTO = result.getData();
        if(couponDTO==null){
            return Result.wrapSuccessfulResult(null);
        }
        WechatCfgCouponVo wechatCfgCouponVo = new WechatCfgCouponVo();
        BeanUtils.copyProperties(couponDTO, wechatCfgCouponVo);
        return Result.wrapSuccessfulResult(wechatCfgCouponVo);
    }

    @Override
    public Result<List<WechatCfgCouponVo>> qryWechatCouponHisCfg(Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("-1","门店信息欠缺,操作失败,请联系客服人员");
        }
        com.tqmall.core.common.entity.Result<List<CouponHistoryDTO>> result = weChatCouponHistoryService.selectByShopId(userGlobalId);
        log.info("[微信公众号-dubbo]查询关注送券历史配置shopId:{},success:{}",shopId,result.isSuccess());
        if(!result.isSuccess()){
            return Result.wrapErrorResult("-1",result.getMessage());
        }
        List<WechatCfgCouponVo>  wechatCfgCouponVoList = new ArrayList<>();
        List<CouponHistoryDTO> couponHistoryDTOList = result.getData();
        if(!CollectionUtils.isEmpty(couponHistoryDTOList)){
            for(CouponHistoryDTO couponHistoryDTO:couponHistoryDTOList){
                WechatCfgCouponVo wechatCfgCouponVo = new WechatCfgCouponVo();
                BeanUtils.copyProperties(couponHistoryDTO,wechatCfgCouponVo);
                wechatCfgCouponVoList.add(wechatCfgCouponVo);
            }
        }
        return Result.wrapSuccessfulResult(wechatCfgCouponVoList);
    }

    @Override
    public Result<String> saveWechatCouponCfg(WechatCfgCouponVo wechatCouponVo,Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("-1","门店信息欠缺,操作失败,请联系客服人员");
        }
        if(wechatCouponVo==null){
            log.error("[微信公众号-dubbo]保存门店微信公众号配置入参为空,操作失败");
            return Result.wrapErrorResult("-1","参数为空,操作失败");
        }

        if(wechatCouponVo.getCouponTypeId()==null){
            log.error("[微信公众号-dubbo]保存门店微信公众号配置入参卡券类型id为空,操作失败");
            return Result.wrapErrorResult("-1","参数错误,操作失败");
        }
        Long couponTypeId = wechatCouponVo.getCouponTypeId();
        CouponInfo couponInfo = couponInfoService.selectById(couponTypeId,shopId);
        if(couponInfo==null){
            log.error("[微信公众号-dubbo]保存门店微信公众号配置入参卡券类型id查询不到卡券信息,操作失败");
            return Result.wrapErrorResult("-1","查询不到卡券信息,操作失败");
        }
        CouponDTO couponDTO = new CouponDTO();
        BeanUtils.copyProperties(wechatCouponVo,couponDTO);
        couponDTO.setShopId(userGlobalId);
        //设置卡券类型信息
        couponDTO.setCouponTypeId(couponInfo.getId());
        couponDTO.setCouponTypeName(couponInfo.getCouponName());
        couponDTO.setCouponTypeMoney(couponInfo.getDiscountAmount());
        StringBuffer couponTypeDescription = new StringBuffer();
        if(couponInfo.getUseRange()!=null){
            String useRangeName = CouponInfoUseRangeEnum.getNameByValue(couponInfo.getUseRange());
            if(StringUtils.isNotBlank(useRangeName)){
                couponTypeDescription.append(useRangeName+";");
            }
        }
        if(couponInfo.getAmountLimit()!=null &&couponInfo.getAmountLimit().compareTo(new BigDecimal("0"))>0){
            couponTypeDescription.append("满"+couponInfo.getAmountLimit()+"元使用;");
        }
        if(couponInfo.getSingleUse()!=null &&couponInfo.getSingleUse()==1){
            couponTypeDescription.append("不可同时使用多张优惠券;");
        } else{
            couponTypeDescription.append("可同时使用多张优惠券;");
        }
        if(couponInfo.getCompatibleWithCard()!=null &&couponInfo.getCompatibleWithCard()==0){
            couponTypeDescription.append("不允许与会员卡共同使用;");
        } else {
            couponTypeDescription.append("允许与会员卡共同使用;");
        }
        couponDTO.setCouponTypeDescription(couponTypeDescription.toString());
        com.tqmall.core.common.entity.Result<String> result = weChatCouponConfigService.save(couponDTO);
        log.info("[微信公众号-dubbo]保存门店微信公众号配置入参:{},success:{}",LogUtils.objectToString(couponDTO),result.isSuccess());
        if(!result.isSuccess()){
            return Result.wrapErrorResult("-1",result.getMessage());
        }
        return Result.wrapSuccessfulResult("操作成功");
    }

    @Override
    public Result<CouponStatisticsDTO> qryCouponStatis(CouponStatisticsParam couponStatisticsParam, Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("-1","门店信息欠缺,操作失败,请联系客服人员");
        }
        couponStatisticsParam.setShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<CouponStatisticsDTO> result = weChatUserCouponLogService.couponStatistics(couponStatisticsParam);
        log.info("[微信公众号-dubbo]查询关注送券统计信息入参:{},success:{}",LogUtils.objectToString(couponStatisticsParam),result.isSuccess());
        if(!result.isSuccess()){
            return Result.wrapErrorResult("-1",result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }

    @Override
    public Result getGrantUrl(Long shopId,String redirectUrl) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("-1","门店信息欠缺,操作失败,请联系客服人员");
        }
        AuthorUrlParam authorUrlParam = new AuthorUrlParam();
        authorUrlParam.setShopId(userGlobalId);
        authorUrlParam.setRedirectUrl(redirectUrl);
        com.tqmall.core.common.entity.Result<String> result = weChatShopService.getAuthorUrl(authorUrlParam);
        log.info("[微信公众号-dubbo]获取微信官方授权url入参:{},success:{}", LogUtils.objectToString(authorUrlParam), result.isSuccess());
        if(!result.isSuccess() || StringUtils.isBlank(result.getData())){
            return Result.wrapErrorResult("-1",result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }

    @Override
    public Result checkAuthor(Long shopId, String authCode) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("-1","门店信息欠缺,操作失败,请联系客服人员");
        }
        AuthorCheckParam authorCheckParam = new AuthorCheckParam();
        authorCheckParam.setShopId(userGlobalId);
        authorCheckParam.setAuthCode(authCode);
        com.tqmall.core.common.entity.Result<String> result = weChatShopService.checkAuthor(authorCheckParam);
        log.info("[微信公众号-dubbo]授权同步通知,授权检查入参:{},success:{}",LogUtils.objectToString(authorCheckParam),result.isSuccess());
        if (!result.isSuccess()){
            return Result.wrapErrorResult("-1",result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }

    @Override
    public Result<String> delCustomMenu(Long btnId) {
        com.tqmall.core.common.entity.Result<Boolean> result = shopMenuService.deleteCustomMenu(btnId);
        log.info("[微信公众号-dubbo]删除自定义菜单:参数btnId:{},success状态:{}",btnId,result.isSuccess());
        if(result.isSuccess()){
            return Result.wrapSuccessfulResult("操作成功");
        } else{
            return Result.wrapErrorResult("-1",result.getMessage());
        }
    }

    @Override
    public Result<ButtonDTO> saveCustomMenu(ButtonDTO buttonDTO,Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("-1","门店信息欠缺,操作失败,请联系客服人员");
        }
        buttonDTO.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<ButtonDTO> result = shopMenuService.saveCustomMenu(buttonDTO);
        log.info("[微信公众号-dubbo]保存自定义菜单入参:{},success:{}",LogUtils.objectToString(buttonDTO),result.isSuccess());
        if(result.isSuccess()){
            return Result.wrapSuccessfulResult(result.getData());
        } else{
            return Result.wrapErrorResult("-1",result.getMessage());
        }
    }


    @Override
    public Result<Page<AssessmentVo>> queryAssessmentsPage(AssessmentParam assessmentParam, Long shopId) {
        if (assessmentParam == null || assessmentParam.getLimit() == null || assessmentParam.getLimit() < 1
                || assessmentParam.getOffset() == null || assessmentParam.getOffset() < 0) {
            log.error("[微信公众号-dubbo]查询定损列表查询参数有误{}", assessmentParam);
            return Result.wrapErrorResult("", "表查询参数有误");
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        assessmentParam.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<AssessmentPageDTO> assessmentPageDTOResult = weChatAssessmentService.queryAssessment(assessmentParam);
        log.info("[微信公众号-dubbo]查询定损入参:{},success:{}", LogUtils.objectToString(assessmentParam),assessmentPageDTOResult.isSuccess());
        if (assessmentPageDTOResult.isSuccess()) {
            Long pageNum = assessmentParam.getOffset() / assessmentParam.getLimit();
            PageRequest pageRequest = new PageRequest(pageNum.intValue(), assessmentParam.getLimit().intValue());
            Page<AssessmentVo> page = new DefaultPage(new ArrayList(), pageRequest, 0);//初始化数据;
            AssessmentPageDTO assessmentPageDTO = assessmentPageDTOResult.getData();
            if(assessmentPageDTO==null){
                return Result.wrapSuccessfulResult(page);
            }
            List<AssessmentDTO> assessmentDTOList = assessmentPageDTO.getAssessmentDTOList();
            List<AssessmentVo> assessmentVos = new ArrayList<>();
            if (!CollectionUtils.isEmpty(assessmentDTOList)) {
                for (AssessmentDTO assessmentDTO : assessmentDTOList) {
                    AssessmentVo assessmentVo = new AssessmentVo();
                    BeanUtils.copyProperties(assessmentDTO, assessmentVo);
                    assessmentVos.add(assessmentVo);
                }
            }
                int totalSize = assessmentPageDTO.getTotalNum();
                page = new DefaultPage(assessmentVos, pageRequest, totalSize);
            return Result.wrapSuccessfulResult(page);
        } else {
            return Result.wrapErrorResult("", assessmentPageDTOResult.getMessage());
        }
    }

    @Override
    public Result<Page<RescueVo>> queryRescuesPage(RescueParam rescueParam, Long shopId) {
        if (rescueParam == null || rescueParam.getLimit() == null || rescueParam.getLimit() < 1
                || rescueParam.getOffset() == null || rescueParam.getOffset() < 0) {
            log.error("[微信公众号-dubbo]查询救援列表查询参数有误{}", rescueParam);
            return Result.wrapErrorResult("", "表查询参数有误");
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[微信公众号-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,操作失败,请联系客服人员");
        }
        rescueParam.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<RescuePageDTO> rescuePageDTOResult = weChatRescueService.queryRescue(rescueParam);
        log.info("[微信公众号-dubbo]查询救援列表入参:{},success:{}", LogUtils.objectToString(rescueParam),rescuePageDTOResult.isSuccess());
        if (rescuePageDTOResult.isSuccess()) {
            Long pageNum = rescueParam.getOffset() / rescueParam.getLimit();
            PageRequest pageRequest = new PageRequest(pageNum.intValue(), rescueParam.getLimit().intValue());
            Page<RescueVo> page = new DefaultPage(new ArrayList(), pageRequest, 0);//初始化数据;
            RescuePageDTO rescuePageDTO = rescuePageDTOResult.getData();
            if(rescuePageDTO==null){
                return Result.wrapSuccessfulResult(page);
            }
            List<RescueDTO> rescueDTOs = rescuePageDTO.getRescueDTOList();
            List<RescueVo> rescueVos = new ArrayList<>();
            if (!CollectionUtils.isEmpty(rescueDTOs)) {
                for (RescueDTO rescueDTO : rescueDTOs) {
                    RescueVo rescueVo = new RescueVo();
                    BeanUtils.copyProperties(rescueDTO, rescueVo);
                    rescueVos.add(rescueVo);
                }
            }
                int totalSize = rescuePageDTO.getTotalNum();
                page = new DefaultPage(rescueVos, pageRequest, totalSize);
            return Result.wrapSuccessfulResult(page);
        } else {
            return Result.wrapErrorResult("", rescuePageDTOResult.getMessage());
        }
    }

    @Override
    public Result<String> updateAssessmentStatus(Long id, Integer processStatus) {
        com.tqmall.core.common.entity.Result<Boolean> result =  weChatAssessmentService.updateAssessment(id, processStatus);
        log.info("[微信公众号-dubbo]修改定损状态:参数id:{},processStatus{},success状态:{}",id,processStatus,result.isSuccess());
        if(result.isSuccess()){
            return Result.wrapSuccessfulResult("操作成功");
        } else{
            return Result.wrapErrorResult("-1",result.getMessage());
        }
    }

    @Override
    public Result<String> updateRescuesStatus(Long id, Integer processStatus) {
        com.tqmall.core.common.entity.Result<Boolean> result =  weChatRescueService.updateRescueStatus(id, processStatus);
        log.info("[微信公众号-dubbo]修改救援状态:参数id:{},processStatus{},success状态:{}",id,processStatus,result.isSuccess());
        if(result.isSuccess()){
            return Result.wrapSuccessfulResult("操作成功");
        } else{
            return Result.wrapErrorResult("-1",result.getMessage());
        }
    }

    @Override
    public Page<ShopActivityVo> getActivityPage(Map<String, Object> param) throws BizException{
        Assert.notNull(param,"查询条件不能为空");
        Long shopId = Long.valueOf(param.get("shopId").toString());
        Assert.notNull(shopId,"查询条件中的shopid不能为空");
        Integer offset = 0;
        Integer limit = 10;
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consume-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            throw new BizException("门店信息缺失");
        }
        if (param.get("offset")!=null) {
            offset = Integer.parseInt(param.get("offset").toString());
        }
        if (param.get("limit")!=null) {
            limit = Integer.parseInt(param.get("limit").toString());
        }
        Integer pageNum = offset /limit;//从0开始
        PageRequest pageRequest = new PageRequest(pageNum, limit);
        Page<ShopActivityVo> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        Integer joinStatus = 1;//1.全部,2.已参加,3.未参加
        if (param.get("joinStatus")!=null) {
            joinStatus = Integer.parseInt(param.get("joinStatus").toString());
        }
        GameSelectParam gameSelectParam = new GameSelectParam();
        gameSelectParam.setLimit(limit);
        gameSelectParam.setOffset(offset);
        gameSelectParam.setUcShopId(userGlobalId);
        if(joinStatus==2){
            //查询已参加的
            gameSelectParam.setStatus(1);
        } else if(joinStatus==3){
            //查询未参加的
            gameSelectParam.setStatus(0);
        }
        if(param.get("actNameLike")!=null){
            gameSelectParam.setGameName(param.get("actNameLike").toString());
        }
        if(param.get("startTimeLt")!=null){
            gameSelectParam.setStartTime(DateUtil.convertStringToDate(param.get("startTimeLt").toString()));
        }
        if(param.get("endTimeGt")!=null){
            gameSelectParam.setEndTime(DateUtil.convertStringToDate(param.get("endTimeGt").toString()));
        }
        com.tqmall.core.common.entity.Result<GameStatisticsDTO> result = wechatGameService.gameStatisticsByActIdAndShopId(gameSelectParam);
        log.info("[consume-ddlwechat-dubbo]查询游戏活动列表,入参:{},success:{}",LogUtils.objectToString(gameSelectParam),result.isSuccess());
        if(!result.isSuccess()){
            throw new BizException(result.getMessage());
        }
        if(result.getData()==null ||CollectionUtils.isEmpty(result.getData().getGameStatisticDTOList())){
            return page;
        }
        List<ShopActivityVo> shopActivityVoList = new ArrayList<>();
        List<GameStatisticDTO> gameStatisticDTOList = result.getData().getGameStatisticDTOList();
        for (GameStatisticDTO gameStatisticDTO : gameStatisticDTOList) {
            ShopActivityVo shopActivityVo = new ShopActivityVo();
            if(gameStatisticDTO.getGameStatus()==null){
                shopActivityVo.setIsJoin(0);
            } else{
                if(gameStatisticDTO.getGameStatus().intValue()!=ShopActivityVo.WechatShopActGameStatusEnum.NOT_PART.getValue()
                        &&gameStatisticDTO.getGameStatus().intValue()!=ShopActivityVo.WechatShopActGameStatusEnum.OUTLINE.getValue()){
                    shopActivityVo.setIsJoin(1);
                } else{
                    shopActivityVo.setIsJoin(0);
                }
            }
            shopActivityVo.setVisitCount(gameStatisticDTO.getPv());
            shopActivityVo.setPartCount(gameStatisticDTO.getUv());
            shopActivityVo.setStartTime(gameStatisticDTO.getStartTime());
            shopActivityVo.setEndTime(gameStatisticDTO.getEndTime());
            shopActivityVo.setId(gameStatisticDTO.getId());
            shopActivityVo.setActTplId(gameStatisticDTO.getGameTemplateId());
            shopActivityVo.setActName(gameStatisticDTO.getGameName());
            shopActivityVo.setGmtCreate(gameStatisticDTO.getJoinTime());
            shopActivityVo.setWechatActivityType(WechatActModuleTypeEnum.GAME.getValue());
            shopActivityVo.setActGameStatus(gameStatisticDTO.getGameStatus());
            shopActivityVoList.add(shopActivityVo);
        }
        int count = result.getData().getGameNum();
        page = new DefaultPage(shopActivityVoList, pageRequest, count);
        return page;
    }

    @Override
    public GameActivityDetailVo gameDetailsByGameId(Long gameId,Long shopId) throws BizException {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consume-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            throw new BizException("门店信息缺失");
        }
        com.tqmall.core.common.entity.Result<GameDetailsDTO> result = wechatGameService.gameDetailsByGameId(gameId,userGlobalId);
        log.info("[consume-ddlwechat-dubbo]查询游戏详情,入参:{},success:{}",gameId,result.isSuccess());
        if(!result.isSuccess()){
            throw new BizException(result.getMessage());
        }
        GameDetailsDTO gameDetailsDTO = result.getData();
        if(gameDetailsDTO==null){
            return null;
        }
        GameActivityDetailVo gameActivityDetailVo = new GameActivityDetailVo();
        BeanUtils.copyProperties(gameDetailsDTO,gameActivityDetailVo);
        //.设置优惠券名称
        List<GameCouponDTO> gameCouponDTOList = gameActivityDetailVo.getGameCouponDTOs();
        Set<Long> couponInfoIds = new HashSet<>();
        if(!CollectionUtils.isEmpty(gameCouponDTOList)){
            for (GameCouponDTO gameCouponDTO : gameCouponDTOList) {
                couponInfoIds.add(gameCouponDTO.getCouponInfoId());
            }
        }
        Map<Long,CouponInfo> couponInfoMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(couponInfoIds)){
            List<CouponInfo> couponInfoList = couponInfoService.findByIds(shopId,couponInfoIds.toArray(new Long[]{}));
            if(!CollectionUtils.isEmpty(couponInfoList)){
                for (CouponInfo couponInfo : couponInfoList) {
                    couponInfoMap.put(couponInfo.getId(),couponInfo);
                }
            }
        }
        if(!CollectionUtils.isEmpty(gameCouponDTOList)){
            for (GameCouponDTO gameCouponDTO : gameCouponDTOList) {
                CouponInfo couponInfo = couponInfoMap.get(gameCouponDTO.getCouponInfoId());
                if(couponInfo!=null){
                    gameCouponDTO.setCouponName(couponInfo.getCouponName());
                }
            }
        }
        return gameActivityDetailVo;
    }

    @Override
    @Transactional
    public Result<GameActivityDetailVo> saveWechatGameActivity(GameActivityDetailVo gameActivityDetailVo, UserInfo userInfo) throws BizException {
        Assert.notNull(gameActivityDetailVo, "微信活动信息不能为空");
        Assert.notNull(userInfo, "操作员信息不能为空");
        Assert.notNull(userInfo.getShopId(), "门店id不能为空");
        Assert.notNull(userInfo.getUserId(), "操作员id不能为空");
        Long userGlobalId = shopService.getUserGlobalId(userInfo.getShopId());
        if (userGlobalId == null) {
            log.error("[consume-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", userInfo.getShopId());
            throw new BizException("门店信息缺失");
        }
        gameActivityDetailVo.setUcShopId(userGlobalId);
        if (gameActivityDetailVo.getGameStatus() != null && gameActivityDetailVo.getGameStatus() == 3
                && gameActivityDetailVo.getComboStatus() != null && gameActivityDetailVo.getComboStatus() == 1) {
            //开通游戏活动对应的计次卡
            Long comboInfoId = gameActivityDetailVo.getComboInfoId();
            ComboInfo comboInfo = accountFacadeService.getComboInfo(userInfo.getShopId(), comboInfoId);
            if (comboInfoId == null || comboInfoId == 0 || comboInfo == null) {
                //需要创建计次卡
                Long serviceTplId = gameActivityDetailVo.getServiceTmplId();
                String comboName = gameActivityDetailVo.getComboName();
                Long effectivePeriodDays = gameActivityDetailVo.getComboEffectivePeriodDays();
                comboInfoId = _createShopServiceAndCombo(serviceTplId, comboName, effectivePeriodDays, userInfo);
                if (comboInfoId == null) {
                    log.error("创建计次卡失败,serviceTplId:{},comboName:{},effectivePeriodDays,userInfo:{}",serviceTplId, comboName, effectivePeriodDays, LogUtils.objectToString(userInfo));
                    throw new BizException("创建计次卡失败");
                }
                gameActivityDetailVo.setComboInfoId(comboInfoId);
            }
        }
        GameDetailsDTO gameDetailsDTO = new GameDetailsDTO();
        BeanUtils.copyProperties(gameActivityDetailVo, gameDetailsDTO);
        com.tqmall.core.common.entity.Result<Boolean> result = wechatGameService.saveGame(gameDetailsDTO);
        log.info("[consume-ddlwechat-dubbo]保存微信游戏活动入参:{},success:{}", gameActivityDetailVo, result.isSuccess());
        if (!result.isSuccess()) {
            if ("-5".equals(result.getCode())) {
                //-5表示自动配置菜单失败,但服务发布是成功的
                gameActivityDetailVo.setAutoMenuSuccess(0);
                return Result.wrapSuccessfulResult(gameActivityDetailVo);
            }
            return Result.wrapErrorResult(result.getCode(), result.getMessage());
        }
        return Result.wrapSuccessfulResult(gameActivityDetailVo);
    }

    /**
     * 创建微信游戏活动使用的服务实例或计次卡
     *
     * @param serviceTplId
     * @param comboName
     * @param effectivePeriodDays
     * @param userInfo
     * @return 计次卡类型id
     */
    private Long _createShopServiceAndCombo(Long serviceTplId, String comboName, Long effectivePeriodDays, UserInfo userInfo) throws BizException {
        Assert.notNull(serviceTplId, "服务模版id不能为空");
        Assert.hasText(comboName, "计次卡名称不能为空");
        Assert.notNull(effectivePeriodDays, "计次卡有效期不能为空");
        Assert.notNull(userInfo, "操作员信息不能为空");
        Assert.notNull(userInfo.getShopId(), "门店id不能为空");
        Assert.notNull(userInfo.getUserId(), "操作员id不能为空");
        //.创建服务实例
        SaveShopServiceInfoVo saveShopServiceInfoVo = new SaveShopServiceInfoVo();
        saveShopServiceInfoVo.setServiceTplId(serviceTplId);
        saveShopServiceInfoVo.setServicePrice(new BigDecimal("0.00"));
        ShopServiceInfo shopServiceInfo = shopServiceInfoFacade.save(saveShopServiceInfoVo, userInfo);
        if (shopServiceInfo == null || shopServiceInfo.getId() == null) {
            log.error("根据服务模版生成门店服务失败,shopid:{},shopServiceInfo:{}",userInfo.getShopId(),LogUtils.objectToString(shopServiceInfo));
            throw new BizException("创建服务失败");
        }
        //创建计次卡
        Long shopId = userInfo.getShopId();
        Long operatorId = userInfo.getUserId();
        BigDecimal salePrice = new BigDecimal("0.00");
        Long shopServiceInfoId = shopServiceInfo.getId();
        Long comboInfoId = accountFacadeService.createSimpleComboType(shopId, operatorId, comboName, effectivePeriodDays, salePrice, null, shopServiceInfoId, 1);
        return comboInfoId;
    }

    @Override
    public GameCouponStatisticsVo gameCouponStatistic(Long gameId, Long shopId) throws BizException{
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consume-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            throw new BizException("门店信息缺失");
        }
        com.tqmall.core.common.entity.Result<GameCouponStatisticsDTO> result = wechatGameService.gameCouponStatistic(gameId,userGlobalId);
        log.info("[consume-ddlwechat-dubbo]查询游戏活动的优惠券领取信息,gameId:{},userGlobalId:{},success:{}",gameId,userGlobalId,result.isSuccess());
        if(!result.isSuccess()){
            throw new BizException(result.getMessage());
        }
        GameCouponStatisticsDTO gameCouponStatisticsDTO = result.getData();
        if(gameCouponStatisticsDTO==null){
            return null;
        }
        GameCouponStatisticsVo gameCouponStatisticsVo = new GameCouponStatisticsVo();
        BeanUtils.copyProperties(gameCouponStatisticsDTO,gameCouponStatisticsVo);
        GameDTO gameDTO = gameCouponStatisticsDTO.getGameDTO();
        if(gameDTO!=null &&gameDTO.getStartTime()!=null){
            Date endTime = new Date();
            if(gameDTO.getEndTime()!=null && gameDTO.getEndTime().before(endTime)){
                endTime = gameDTO.getEndTime();
            }
            int durationDay = DateUtil.getSpaceByCompareTwoDate(gameDTO.getStartTime(),endTime);
            if(durationDay>0){
                gameCouponStatisticsVo.setDurationDay(durationDay);
            } else {
                gameCouponStatisticsVo.setDurationDay(0);
            }
        }
        //.设置优惠券名称
        List<GameCouponStatisticDTO> gameCouponStatisticDTOList = gameCouponStatisticsVo.getGameCouponStatisticDTOList();
        Set<Long> couponInfoIds = new HashSet<>();
        if(!CollectionUtils.isEmpty(gameCouponStatisticDTOList)){
            for (GameCouponStatisticDTO gameCouponStatisticDTO : gameCouponStatisticDTOList) {
                couponInfoIds.add(gameCouponStatisticDTO.getCouponInfoId());
            }
        }
        Map<Long,CouponInfo> couponInfoMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(couponInfoIds)){
            List<CouponInfo> couponInfoList = couponInfoService.findByIds(shopId,couponInfoIds.toArray(new Long[]{}));
            if(!CollectionUtils.isEmpty(couponInfoList)){
                for (CouponInfo couponInfo : couponInfoList) {
                    couponInfoMap.put(couponInfo.getId(),couponInfo);
                }
            }
        }
        if(!CollectionUtils.isEmpty(gameCouponStatisticDTOList)){
            for (GameCouponStatisticDTO gameCouponStatisticDTO : gameCouponStatisticDTOList) {
                CouponInfo couponInfo = couponInfoMap.get(gameCouponStatisticDTO.getCouponInfoId());
                if(couponInfo!=null){
                    gameCouponStatisticDTO.setCouponName(couponInfo.getCouponName());
                }
            }
        }
        return gameCouponStatisticsVo;
    }

    @Override
    public Page<GameCouponLogDTO> getGameCouponUserList(Long shopId, GameCouponUserLogParam gameCouponUserLogParam) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consume-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            throw new BizException("门店信息缺失");
        }
        if(gameCouponUserLogParam==null){
            log.error("[consume-ddlwechat-dubbo]shopId:{},查询微信游戏活动优惠券领取用户列表信息失败,入参为空", shopId);
            throw new BizException("参数为空");
        }
        Integer offset = gameCouponUserLogParam.getOffset();
        Integer limit = gameCouponUserLogParam.getLimit();
        if(offset==null ||offset<0 ||limit==null ||limit<1){
            log.error("[consume-ddlwechat-dubbo]shopId:{},查询微信游戏活动优惠券领取用户列表信息失败,分页信息错误,入参:{}", shopId,LogUtils.objectToString(gameCouponUserLogParam));
            throw new BizException("分页参数错误");
        }
        gameCouponUserLogParam.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<GameCouponLogPageDTO> result = wechatGameService.getCouponUserLog(gameCouponUserLogParam);
        log.info("[consume-ddlwechat-dubbo]查询微信游戏活动优惠券领取用户列表信息,入参:{},success:{}",LogUtils.objectToString(gameCouponUserLogParam),result.isSuccess());
        Integer pageNum = offset /limit;//从0开始
        PageRequest pageRequest = new PageRequest(pageNum, limit);
        Page<GameCouponLogDTO> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        GameCouponLogPageDTO gameCouponLogPageDTO = result.getData();
        if(gameCouponLogPageDTO!=null){
            int count = gameCouponLogPageDTO.getTotalNum();
            List<GameCouponLogDTO> content = gameCouponLogPageDTO.getGameCouponLogDTOs();
            page = new DefaultPage(content, pageRequest, count);
        }
        return page;
    }

    @Override
    public Result<String> getGameActUrl(Long shopId, Long actId, Integer isFormal) {
        if(shopId==null||actId==null||isFormal==null){
            log.error("[consume-ddlwechat-dubbo]获取微信游戏活动url参数错误,shopId:{},actId:{},isFormal:{}", shopId, actId, isFormal);
            throw new BizException("参数错误");
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consume-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            throw new BizException("门店信息缺失");
        }
        com.tqmall.core.common.entity.Result<String> result = wechatGameService.getUrl(actId,isFormal.toString());
        log.error("[consume-ddlwechat-dubbo]获取微信游戏活动url,shopId:{},actId:{},isFormal:{},success:{}", shopId, actId, isFormal,result.isSuccess());
        if (!result.isSuccess()) {
            return Result.wrapErrorResult("-1",result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }

    @Override
    public Page<WechatFavormallCardVo> qryFavormallCardList(Long shopId, QueryCardParam queryCardParam) throws IllegalArgumentException, BizException {
        Assert.notNull(shopId, "shopId不能为空");
        Assert.notNull(queryCardParam, "查询条件不能为空");
        Assert.notNull(queryCardParam.getLimit(), "分页页长不能为空");
        Assert.notNull(queryCardParam.getOffset(), "分页起始值不能为空");
        int limit = queryCardParam.getLimit();
        int offset = queryCardParam.getOffset();
        PageRequest pageRequest = new PageRequest(offset / limit, limit);
        Page<WechatFavormallCardVo> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consume-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            throw new BizException("门店信息缺失");
        }
        queryCardParam.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<MembershipCardMapDTO> result = weChatMembershipCardConfigService.queryVipCard(queryCardParam);
        log.info("[consume-ddlwechat-dubbo]查询微信端卡券商城中会员卡列表,入参:{},success:{}", LogUtils.objectToString(queryCardParam), result.isSuccess());
        if (!result.isSuccess()) {
            BizException e = new BizException(result.getMessage());
            e.setCode(result.getCode());
            log.error("[consume-ddlwechat-dubbo]查询微信端卡券商城中会员卡列表失败,queryCardParam:{},resultCode:{},message:{}", LogUtils.objectToString(queryCardParam), result.getCode(), result.getMessage());
            throw e;
        }
        MembershipCardMapDTO membershipCardMapDTO = result.getData();
        if (membershipCardMapDTO == null || CollectionUtils.isEmpty(membershipCardMapDTO.getMembershipCardDTOList())
                || membershipCardMapDTO.getTotalSize() == null) {
            return page;
        }

        //.填充数据
        List<MembershipCardDTO> membershipCardDTOList = membershipCardMapDTO.getMembershipCardDTOList();
        Set<Long> memberCardInfoIds = new HashSet<>();
        for (MembershipCardDTO membershipCardDTO : membershipCardDTOList) {
            memberCardInfoIds.add(membershipCardDTO.getCardTypeId());
        }
        List<MemberCardInfo> memberCardInfoLists = new ArrayList<>();
        if (!CollectionUtils.isEmpty(memberCardInfoIds)) {
            memberCardInfoLists = memberCardInfoService.selectInfoByIds(shopId, new ArrayList<Long>(memberCardInfoIds));
        }
        if (CollectionUtils.isEmpty(memberCardInfoLists)) {
            return page;
        }
        memberCardInfoService.attachDiscount(memberCardInfoLists);
        ImmutableMap<Long, MemberCardInfo> memberCardInfoMap = Maps.uniqueIndex(memberCardInfoLists, new Function<MemberCardInfo, Long>() {
            @Override
            public Long apply(MemberCardInfo memberCardInfo) {
                return memberCardInfo.getId();
            }
        });
        List<WechatFavormallCardVo> wechatFavormallCouponVoList = Lists.newArrayList();
        for (MembershipCardDTO membershipCardDTO : membershipCardDTOList) {
            WechatFavormallCardVo wechatFavormallCardVo = new WechatFavormallCardVo();
            MemberCardInfo memberCardInfo = memberCardInfoMap.get(membershipCardDTO.getCardTypeId());
            if (memberCardInfo != null) {
                BeanUtils.copyProperties(memberCardInfo, wechatFavormallCardVo);
                wechatFavormallCardVo.setDiscountDescript(memberCardInfo.getDiscountDescription());
            }
            BeanUtils.copyProperties(membershipCardDTO, wechatFavormallCardVo);
            wechatFavormallCouponVoList.add(wechatFavormallCardVo);
        }
        page = new DefaultPage(wechatFavormallCouponVoList, pageRequest, membershipCardMapDTO.getTotalSize());//默认没有数据的page
        return page;
    }

    @Override
    public boolean saveFavormallCard(Long shopId, SaveCardParam saveCardParam) throws IllegalArgumentException, BizException {
        Assert.notNull(shopId, "shopId不能为空");
        Assert.notNull(saveCardParam, "会员卡参数不能为空");
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consume-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            throw new BizException("门店信息缺失");
        }
        SaveCardParam saveCardParamCp = new SaveCardParam();
        BeanUtils.copyProperties(saveCardParam, saveCardParamCp);
        saveCardParamCp.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<String> result = weChatMembershipCardConfigService.saveVipCard(saveCardParamCp);
        log.info("[consume-ddlwechat-dubbo]保存微信端卡券商城中会员卡设置,saveCardParam:{},success:{}", LogUtils.objectToString(saveCardParamCp), result.isSuccess());
        if (!result.isSuccess()) {
            BizException e = new BizException(result.getMessage());
            e.setCode(result.getCode());
            log.error("[consume-ddlwechat-dubbo]保存微信端卡券商城中会员卡设置失败,saveCardParam:{},resultCode:{},message:{}", LogUtils.objectToString(saveCardParamCp), result.getCode(), result.getMessage());
            throw e;
        }
        return true;
    }

    @Override
    public Page<WechatFavormallCouponVo> qryFavormallCouponList(Long shopId, QueryCouponParam queryCouponParam) {
        Assert.notNull(shopId, "shopId不能为空");
        Assert.notNull(queryCouponParam, "查询条件不能为空");
        Assert.notNull(queryCouponParam.getLimit(), "分页页长不能为空");
        Assert.notNull(queryCouponParam.getOffset(), "分页起始值不能为空");
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consume-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            throw new BizException("门店信息缺失");
        }
        queryCouponParam.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<CouponMapDTO> result = weChatCouponService.queryCoupon(queryCouponParam);
        log.info("[consume-ddlwechat-dubbo]查询微信端卡券商城中优惠券列表,入参:{},success:{}", LogUtils.objectToString(queryCouponParam), result.isSuccess());
        if (!result.isSuccess()) {
            log.error("[consume-ddlwechat-dubbo]查询微信端卡券商城中优惠券列表失败,queryCardParam:{},resultCode:{},message:{}", LogUtils.objectToString(queryCouponParam), result.getCode(), result.getMessage());
            throw new BizException(result.getMessage());
        }
        int limit = queryCouponParam.getLimit();
        int offset = queryCouponParam.getOffset();
        PageRequest pageRequest = new PageRequest(offset / limit, limit);
        Page<WechatFavormallCouponVo> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        CouponMapDTO couponMapDTO = result.getData();
        if (couponMapDTO == null) {
            return page;
        }
        List<CouponConfigDTO> couponConfigDTOList = couponMapDTO.getCouponVOList();
        if (CollectionUtils.isEmpty(couponConfigDTOList) || couponMapDTO.getTotalSize() == null) {
            return page;
        }
        //.填充数据
        Set<Long> couponInfoIds = Sets.newHashSet();

        for (CouponConfigDTO couponConfigDTO : couponConfigDTOList) {
            couponInfoIds.add(couponConfigDTO.getCouponTypeId());
        }
        List<CouponInfo> couponInfoList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(couponInfoIds)) {
            couponInfoList = couponInfoService.findByIds(shopId, couponInfoIds.toArray(new Long[]{}));
        }
        if (CollectionUtils.isEmpty(couponInfoList)) {
            return page;
        }
        couponInfoService.attachUseRange(couponInfoList);
        ImmutableMap<Long, CouponInfo> couponInfoMap = Maps.uniqueIndex(couponInfoList, new Function<CouponInfo, Long>() {
            @Override
            public Long apply(CouponInfo couponInfo) {
                return couponInfo.getId();
            }
        });
        List<WechatFavormallCouponVo> wechatFavormallCouponVoList = Lists.newArrayList();
        for (CouponConfigDTO couponConfigDTO : couponConfigDTOList) {
            WechatFavormallCouponVo wechatFavormallCouponVo = new WechatFavormallCouponVo();
            CouponInfo couponInfo = couponInfoMap.get(couponConfigDTO.getCouponTypeId());
            if (couponInfo != null) {
                BeanUtils.copyProperties(couponInfo, wechatFavormallCouponVo);
                wechatFavormallCouponVo.setUseRangeDescript(couponInfo.getUseRangeDescript());
            }
            BeanUtils.copyProperties(couponConfigDTO, wechatFavormallCouponVo);
            wechatFavormallCouponVoList.add(wechatFavormallCouponVo);
        }
        page = new DefaultPage(wechatFavormallCouponVoList, pageRequest, couponMapDTO.getTotalSize());
        return page;
    }

    @Override
    public boolean saveFavormallCoupon(Long shopId, SaveCouponParam saveCouponParam) {
        Assert.notNull(shopId, "shopId不能为空");
        Assert.notNull(saveCouponParam, "优惠券参数不能为空");
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consume-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            throw new BizException("门店信息缺失");
        }
        SaveCouponParam saveCouponParamCp = new SaveCouponParam();
        BeanUtils.copyProperties(saveCouponParam, saveCouponParamCp);
        saveCouponParamCp.setUcShopId(userGlobalId);
        com.tqmall.core.common.entity.Result<String> result = weChatCouponService.saveCoupon(saveCouponParamCp);
        log.info("[consume-ddlwechat-dubbo]保存微信端卡券商城中优惠券设置,saveCouponParam:{},success:{}", LogUtils.objectToString(saveCouponParamCp), result.isSuccess());
        if (!result.isSuccess()) {
            log.error("[consume-ddlwechat-dubbo]保存微信端卡券商城中会员卡设置失败,saveCouponParam:{},resultCode:{},message:{}", LogUtils.objectToString(saveCouponParamCp), result.getCode(), result.getMessage());
            throw new BizException(result.getMessage());
        }
        return true;
    }

    @Override
    public boolean isExistFavormallConfig(Integer configType, Long configId) {
        Assert.notNull(configType, "configType不能为空");
        Assert.notNull(configId, "configId不能为空");
        if (configType == 1) {
            com.tqmall.core.common.entity.Result<Boolean> result = weChatMembershipCardConfigService.checkCardIsSetted(configId);
            log.info("[consume-ddlwechat-dubbo]检查卡券商城中会员卡是否已经设置,configId:{},isExist:{}", configId, result.getData());
            if (result.isSuccess()) {
                return result.getData();
            }
        } else if (configType == 2) {
            com.tqmall.core.common.entity.Result<Boolean> result = weChatCouponService.checkCouponIsSetted(configId);
            log.info("[consume-ddlwechat-dubbo]检查卡券商城中优惠券是否已经设置,configId:{},isExist:{}", configId, result.getData());
            if (result.isSuccess()) {
                return result.getData();
            }
        }
        return false;
    }
}
