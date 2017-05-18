package com.tqmall.legend.web.wechat;

import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.common.util.WebUtils;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.dandelion.wechat.client.dto.wechat.*;
import com.tqmall.dandelion.wechat.client.param.wechat.AssessmentParam;
import com.tqmall.dandelion.wechat.client.param.wechat.CouponStatisticsParam;
import com.tqmall.dandelion.wechat.client.param.wechat.RescueParam;
import com.tqmall.dandelion.wechat.client.param.wechat.TemplateReplyParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.QueryCardParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.QueryCouponParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.SaveCardParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.SaveCouponParam;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.marketing.MarketingShopRelService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.entity.marketing.MarketingShopRel;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.coupon.AccountCouponSourceEnum;
import com.tqmall.legend.enums.wechat.AssessmentStatusEnum;
import com.tqmall.legend.enums.wechat.RescueStatusEnum;
import com.tqmall.legend.enums.wechat.ShopWechatStatusEnum;
import com.tqmall.legend.facade.account.AccountFacadeService;
import com.tqmall.legend.facade.wechat.WechatFacade;
import com.tqmall.legend.facade.wechat.vo.AssessmentVo;
import com.tqmall.legend.facade.wechat.vo.ShopWechatVo;
import com.tqmall.legend.facade.wechat.vo.TemplateReplyVo;
import com.tqmall.legend.facade.wechat.vo.WechatArticleVo;
import com.tqmall.legend.facade.wechat.vo.WechatCfgCouponVo;
import com.tqmall.legend.facade.wechat.vo.WechatFavormallCardVo;
import com.tqmall.legend.facade.wechat.vo.WechatFavormallCouponVo;
import com.tqmall.legend.facade.wechat.vo.WechatMenuVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 门店微信管理
 * Created by wushuai on 16/6/2.
 */
@RequestMapping("/shop/wechat")
@Controller
@Slf4j
public class ShopWechatController extends BaseController {

    @Autowired
    private WechatFacade wechatFacade;
    @Autowired
    private MarketingShopRelService marketingShopRelService;
    @Autowired
    private AccountFacadeService accountFacadeService;

    /**
     * 微信公众号主页,根据门店的微信号状态跳转到对于页面上
     *
     * @param model
     * @return
     */
    @HttpRequestLog
    @RequestMapping("")
    public String index(Model model,@RequestParam(value = "isRegister",required = false) String isRegister) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result<ShopWechatVo> wechatShopResult = wechatFacade.qryShopWechat(shopId);
        if (wechatShopResult.getData() == null) {
            //需要申请开通,首先进入开通宣传页
            return "yqx/page/wechat/reg-apply";
        }
        ShopWechatVo shopWechatVo = wechatShopResult.getData();
        model.addAttribute("shopWechatVo", wechatShopResult.getData());
        Integer shopStatus = shopWechatVo.getShopStatus();
        if (ShopWechatStatusEnum.EXPIRED.getValue().equals(shopStatus)) {
            //4.已过期
            return "yqx/page/wechat/reg-apply";
        } else if (ShopWechatStatusEnum.REGISTERED.getValue().equals(shopStatus)) {
            //3.已注册
            if ("1".equals(isRegister)){
                //处于注册流程中,需要先进入注册成功提示页
                log.info("[微信公众号]开通成功,首次进入");
            }
            model.addAttribute("subModule", "wechat-index");
            return "redirect:/shop/wechat/wechat-index";
        } else if (ShopWechatStatusEnum.SUBMITTED.getValue().equals(shopStatus)) {
            //2.已提交(受理中)
            return "yqx/page/wechat/apply/wx-check";
        } else if (ShopWechatStatusEnum.PAID.getValue().equals(shopStatus)) {
            //1.已支付
            //门店支付信息查询
            Result<PayFlowDTO> payFlowDTOResult = wechatFacade.qryRegPayInfo(shopId);
            if (payFlowDTOResult.isSuccess() && payFlowDTOResult.getData() != null) {
                model.addAttribute("payFlow", payFlowDTOResult.getData());
            }
            return "yqx/page/wechat/reg-edit";
        } else if (ShopWechatStatusEnum.UNPAID.getValue().equals(shopStatus)) {
            //0.待支付或者线下支付待审核
            Result<PayFlowDTO> payFlowDTOResult = wechatFacade.qryRegPayInfo(shopId);
            if (payFlowDTOResult.isSuccess() && payFlowDTOResult.getData() != null
                    && "2".equals(payFlowDTOResult.getData().getPayWay())) {
                //payWay=2线下支付待审核
                model.addAttribute("payFlow", payFlowDTOResult.getData());
                return "yqx/page/wechat/reg-edit";
            }
            PagingParams pagingParams = new PagingParams();
            pagingParams.setLimit(10L);
            pagingParams.setOffset(0L);
            Result<List<PayTplDTO>> result = wechatFacade.qryPayTpl(pagingParams);
            if (result.isSuccess() && result.getData() != null) {
                model.addAttribute("payTplList", result.getData());
            }
            return "yqx/page/wechat/reg-pay";
        } else if (ShopWechatStatusEnum.TO_GRANT.getValue().equals(shopStatus)){
            return "yqx/page/wechat/apply/wx-check";
        } else if (ShopWechatStatusEnum.GRANTLOCK.getValue().equals(shopStatus)
                || ShopWechatStatusEnum.DATA_INIT.getValue().equals(shopStatus)){
            return "yqx/page/wechat/apply/wx-grant";
        } else {
            //未定义的状态
            log.error("[微信公众号]shopId:{},未定义的门店状态{}",shopId,shopWechatVo.getShopStatus());
            return "/common/error";
        }
    }

    /**
     * 申请开通页
     * @param model
     * @return
     */
    @RequestMapping("/wx-apply")
    public String regApply(Model model) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result<ShopWechatVo> wechatShopResult = wechatFacade.qryShopWechat(shopId);
        if (wechatShopResult.getData() != null) {
            return "redirect:/shop/wechat";
        }
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat");
        return "yqx/page/wechat/apply/wx-apply";
    }

    /**
     * 注册-支付页
     * @param model
     * @return
     */
    @RequestMapping("/reg-pay")
    public String regPay(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        PagingParams pagingParams = new PagingParams();
        pagingParams.setLimit(10L);
        pagingParams.setOffset(0L);
        Result<List<PayTplDTO>> result = wechatFacade.qryPayTpl(pagingParams);
        if (result.isSuccess() && result.getData() != null) {
            model.addAttribute("payTplList", result.getData());
        }
        return "yqx/page/wechat/reg-pay";
    }

    /**
     * 微信主页(注册成功的门店才有)
     *
     * @param model
     * @return
     */
    @RequestMapping("/wechat-index")
    public String wechatIndex(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-index");
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        DataDTO dataDTO = wechatFacade.qryHomeData(shopId);
        if (dataDTO != null) {
            model.addAttribute("dataDTO", dataDTO);
        }
        return "yqx/page/wechat/wechat-index";
    }

    /**
     * 信息维护页(注册成功的门店才有)
     *
     * @param model
     * @return
     */
    @RequestMapping("/wechat-info")
    public String info(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-info");
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result wechatShopResult = wechatFacade.qryShopWechat(shopId);
        if (wechatShopResult.isSuccess()) {
            model.addAttribute("shopWechatVo", wechatShopResult.getData());
        }
        return "yqx/page/wechat/wechat-info";
    }

    /**
     * 文章管理(注册成功的门店才有)
     *
     * @param model
     * @return
     */
    @RequestMapping("/article-list")
    public String articleList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "article-list");
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result<Integer> canSendArticleCount = wechatFacade.canSendArticleCount(shopId);
        if (canSendArticleCount.isSuccess()) {
            model.addAttribute("canSendArticleCount", canSendArticleCount.getData());
        }
        return "yqx/page/wechat/article-list";
    }

    /**
     * 预览发送文章页面(注册成功的门店才有)
     *
     * @param model
     * @returniew
     */
    @RequestMapping("/article-view")
    public String articleView(Model model, @RequestParam(value = "shopAricleRelId", required = true) Long shopAricleRelId) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "article-list");
        if (shopAricleRelId != null) {
            Long shopId = UserUtils.getUserInfo(request).getShopId();
            ArticleParams articleParams = new ArticleParams();
            articleParams.setShopAricleRelId(shopAricleRelId);
            articleParams.setOffset(0L);
            articleParams.setLimit(1L);
            Result<Page<WechatArticleVo>> pageResult = wechatFacade.queryArticlesPage(articleParams, shopId);
            if (pageResult.isSuccess() && pageResult.getData() != null && !CollectionUtils.isEmpty(pageResult.getData().getContent())) {
                WechatArticleVo wechatArticleVo = pageResult.getData().getContent().get(0);
                model.addAttribute("wechatArticleVo", wechatArticleVo);
            }
        }
        return "yqx/page/wechat/article-view";
    }

    /**
     * 自动回复消息(注册成功的门店才有)
     *
     * @param model
     * @return
     */
    @RequestMapping("/msg-list")
    public String msgList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "msg-list");
        return "yqx/page/wechat/msg-list";
    }

    /**
     * 自动回复消息(注册成功的门店才有)
     *
     * @param model
     * @return
     */
    @RequestMapping("/msg-edit")
    public String msgEdit(Model model, @RequestParam(value = "msgId", required = false) Long msgId) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "msg-list");
        if (msgId != null) {
            Long shopId = UserUtils.getUserInfo(request).getShopId();
            TemplateReplyParam templateReplyParam = new TemplateReplyParam();
            templateReplyParam.setId(msgId);
            Result<Page<TemplateReplyDTO>> pageResult = wechatFacade.qryReplyMsgPage(templateReplyParam, shopId, 0L, 1l);
            if (pageResult.isSuccess() && pageResult.getData() != null
                    && !CollectionUtils.isEmpty(pageResult.getData().getContent())) {
                TemplateReplyDTO templateReplyDTO = pageResult.getData().getContent().get(0);
                TemplateReplyVo templateReplyVo = new TemplateReplyVo();
                if(templateReplyDTO!=null){
                    BeanUtils.copyProperties(templateReplyDTO,templateReplyVo);
                }
                model.addAttribute("templateReply", templateReplyVo);
            }
        }
        return "yqx/page/wechat/msg-edit";
    }

    /**
     * 设置WIFI(注册成功的门店才有)
     *
     * @param model
     * @return
     */
    @RequestMapping("/wifi-manage")
    public String wifiManage(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wifi-manage");
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result<WechatWifiDTO> wechatWifiDTOResult = wechatFacade.getWifiByShopId(shopId);
        if (wechatWifiDTOResult.isSuccess() && wechatWifiDTOResult.getData() != null) {
            model.addAttribute("wechatWifi", wechatWifiDTOResult.getData());
        }
        return "yqx/page/wechat/wifi-manage";
    }

    /**
     * 二维码列表(注册成功的门店才有)
     *
     * @param model
     * @return
     */
    @RequestMapping("/qrcode-list")
    public String qrcodeList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "qrcode-list");
        return "yqx/page/wechat/qrcode-list";
    }

    /**
     * 微信菜单配置(注册成功的门店才有)
     *
     * @param model
     * @return
     */
    @RequestMapping("/wechat-menu")
    public String wechatMenu(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-menu");
        return "yqx/page/wechat/wechat-menu";
    }

    /**
     * 卡券商城列表页
     * @param model
     * @return
     */
    @RequestMapping("/favormall-list")
    public String favormallList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-favormall");
        return "yqx/page/wechat/favormall/favormall-list";
    }

    /**
     * 卡券商城-编辑会员卡页
     * @param model
     * @return
     */
    @RequestMapping("/favormall-card-edit")
    public String favormallCardEdit(Model model, @RequestParam(value = "cardCfgId", required = false) final Long cardCfgId) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-favormall");
        if (cardCfgId != null) {
            Long shopId = UserUtils.getUserInfo(request).getShopId();
            QueryCardParam queryCardParam = new QueryCardParam();
            queryCardParam.setLimit(1);
            queryCardParam.setOffset(0);
            queryCardParam.setCardConfigId(cardCfgId);
            Page<WechatFavormallCardVo> page = wechatFacade.qryFavormallCardList(shopId, queryCardParam);
            log.info("[微信公众号]查询卡券商城会员卡列表,shopid:{},queryCardParam:{}", shopId, LogUtils.objectToString(queryCardParam));
            if (page!=null &&!CollectionUtils.isEmpty(page.getContent())) {
                model.addAttribute("wechatFavormallCardVo", page.getContent().get(0));
            }
        }
        return "yqx/page/wechat/favormall/favormall-card-edit";
    }

    /**
     * 卡券商城-编辑优惠券页
     * @param model
     * @return
     */
    @RequestMapping("/favormall-coupon-edit")
    public String favormallCouponEdit(Model model, @RequestParam(value = "couponCfgId", required = false) final Long couponCfgId) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-favormall");
        if(couponCfgId!=null){
            Long shopId = UserUtils.getUserInfo(request).getShopId();
            QueryCouponParam queryCouponParam = new QueryCouponParam();
            queryCouponParam.setLimit(1);
            queryCouponParam.setOffset(0);
            queryCouponParam.setCouponConfigId(couponCfgId);
            Page<WechatFavormallCouponVo> page = wechatFacade.qryFavormallCouponList(shopId,queryCouponParam);
            log.info("[微信公众号]查询卡券商城优惠全列表,shopid:{},queryCardParam:{}", shopId, LogUtils.objectToString(queryCouponParam));
            if(page!=null &&!CollectionUtils.isEmpty(page.getContent())){
                model.addAttribute("wechatFavormallCouponVo",page.getContent().get(0));
            }
        }
        return "yqx/page/wechat/favormall/favormall-coupon-edit";
    }

    /**
     * 卡券商城列表页
     * @param model
     * @return
     */
    @RequestMapping("/appService-list")
    public String appServiceList(Model model) {
        //TODO
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-favormall");
        return "yqx/page/wechat/favormall/favormall-list";
    }

    /**
     * 支付成功后的前台页面跳转
     *
     * @param model
     * @return
     */
    @RequestMapping("/online-pay/show")
    public String returnShow(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        return "redirect:/shop/wechat";
    }

    /**
     * 支付成功后的前台页面跳转
     *
     * @param model
     * @return
     */
    @RequestMapping("/pay/pay-error")
    public String payError(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        return "yqx/page/wechat/pay-error";
    }

    /**
     * 申请开通微信公众号支付费用
     *
     * @return
     */
    @RequestMapping(value = "/op/toPay", method = RequestMethod.POST)
    public String toPay(@RequestParam(value = "payType") Long payType,
                        @RequestParam(value = "payTplId") Long payTplId,
                        @RequestParam(value = "payVoucher", required = false) String payVoucher, Model model) {
        if (payTplId == null || payType == null) {
            model.addAttribute("errMsg", "支付参数为空");
            return "yqx/page/wechat/pay-error";
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (payType == 1l) {//支付宝
            Result<String> result = wechatFacade.regPayByZhifubao(payTplId, userInfo, WebUtils.getHostUrl(request));
            if (result.isSuccess()) {
                model.addAttribute("payHtml", result.getData());
                return "onlinepay/zhifubao";
            } else {
                model.addAttribute("errMsg", "获取在线支付信息失败.");
                return "yqx/page/wechat/pay-error";
            }
        } else if (payType == 2l) {//线下付款
            Result result = wechatFacade.regPayByOffline(payTplId, payVoucher, userInfo);
            if (result.isSuccess()) {
                return "redirect:/shop/wechat";
            } else {
                model.addAttribute("errMsg", result.getErrorMsg());
                return "yqx/page/wechat/pay-error";
            }
        } else {
            model.addAttribute("errMsg", "未知的支付类型");
            return "yqx/page/wechat/pay-error";
        }
    }

    /**
     * 新流程:申请使用微信公众号功能
     * @return
     */
    @RequestMapping(value = "/op/apply-use", method = RequestMethod.POST)
    @ResponseBody
    public Result applyUse() {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        log.info("[微信公众号]门店申请使用微信公众号功能,shopId{}", shopId);
        return wechatFacade.initShopWechatInfo(shopId);
    }

    /**
     * 新流程:申请使用微信公众号功能
     * @return
     */
    @RequestMapping(value = "/op/get-grant-url")
    @ResponseBody
    public Result getGrantUrl() {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        String redirectUrl = WebUtils.getHostUrl(request)+"/shop/wechat/op/receive-grant-back";
        log.info("[微信公众号]获取授权url,shopId{}", shopId);
        return wechatFacade.getGrantUrl(shopId, redirectUrl);
    }

    /**
     * 授权成功同步通知
     * @return
     */
    @RequestMapping(value = "/op/receive-grant-back")
    public String receiveGrantBack(@RequestParam(value = "auth_code") String authCode) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result result = wechatFacade.checkAuthor(shopId, authCode);
        log.info("[微信公众号]授权同步通知,shopId:{},authCode:{},success:{}", shopId, authCode, result.isSuccess());
        return "redirect:/shop/wechat?isRegister=1";
    }

    /**
     * 微信资料提交
     *
     * @return
     */
    @RequestMapping(value = "/op/save-wechat-info", method = RequestMethod.POST)
    @ResponseBody
    public Result saveWechatInfo(@RequestBody ShopWechatVo shopWechatVo) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        if (shopWechatVo.getId() == null) {
            return Result.wrapErrorResult("", "参数异常:Id不能为空");
        }
        //格式校验
        String mobile = shopWechatVo.getShopMobile();//手机号
        if (StringUtils.isNotEmpty(mobile)) {
            if (!StringUtil.isMobileNO(mobile)) {
                return Result.wrapErrorResult("", "手机号码格式不正确");
            }
        }
        String idCard = shopWechatVo.getOwnerIdcard();
        if (StringUtils.isNotEmpty(idCard)) {
            if (!StringUtil.isIdCard(idCard)) {
                return Result.wrapErrorResult("", "身份证号码格式不正确");
            }
        }
        if (shopWechatVo.getOpSubmitType() == 1) {
            //opSubmitType=1表示申请资料提交
            shopWechatVo.setShopStatus(ShopWechatStatusEnum.SUBMITTED.getValue());//2.已提交
        } else {
            //不可更新的字段
            shopWechatVo.setShopStatus(null);
            shopWechatVo.setPayVoucher(null);
        }
        //去除一些门店端不能编辑的字段
        shopWechatVo.setOnlineTime(null);
        shopWechatVo.setSigningTime(null);
        shopWechatVo.setExpirationTime(null);
        shopWechatVo.setSubmitTime(null);
        shopWechatVo.setOnlinePaytime(null);
        shopWechatVo.setOfflinePaytime(null);
        Result result = wechatFacade.saveWechatInfo(shopWechatVo, shopId);
        log.info("[微信公众号]保存门店微信资料入参shopId:{},success:{}", shopId, result.isSuccess());
        return result;
    }

    /**
     * 查询门店微信号注册信息
     *
     * @return
     */
    @RequestMapping(value = "/op/qry-wechat-info")
    @ResponseBody
    public Result qryWechatInfo() {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        return wechatFacade.qryShopWechat(shopId);
    }

    /**
     * 查询文章列表
     *
     * @return
     */
    @RequestMapping(value = "/op/qry-article-list")
    @ResponseBody
    public Result qryArticleList(@PageableDefault(page = 1, value = 10) Pageable pageable) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        ArticleParams articleParams = new ArticleParams();
        articleParams.setLimit(Long.valueOf(pageable.getPageSize()));
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
        articleParams.setOffset(Long.valueOf(offset));
        if (searchParams.get("sendStatus") != null) {
            articleParams.setSendStatus(Integer.parseInt(searchParams.get("sendStatus").toString()));
        }
        if (searchParams.get("keyword") != null) {
            articleParams.setKeyword(searchParams.get("keyword").toString());
        }
        return wechatFacade.queryArticlesPage(articleParams, shopId);
    }

    /**
     * 发送文章
     *
     * @return
     */
    @RequestMapping(value = "/op/send-article", method = RequestMethod.POST)
    @ResponseBody
    public Result sendArticle(@RequestParam(value = "shopAricleRelId", required = true) Long shopAricleRelId) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        if (shopAricleRelId == null) {
            log.error("shopId:{}发送文章的shopAricleRelId为空,发送失败", shopId);
            return Result.wrapErrorResult("", "参数错误");
        }
        log.info("[微信公众号]发送文章,shopId:{},shopAricleRelId:{}", shopId, shopAricleRelId);
        return wechatFacade.sendArticle(shopAricleRelId, shopId);

    }

    /**
     * 保存自动回复消息
     *
     * @return
     */
    @RequestMapping(value = "/op/save-replymsg", method = RequestMethod.POST)
    @ResponseBody
    public Result saveReplyMsg(@RequestBody TemplateReplyVo templateReplyVo) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        if(templateReplyVo==null){
            log.error("[微信公众号]shopId{}保存自动回复消息失败:参数templateReplyVo为空",shopId);
            return Result.wrapErrorResult("-1","参数为空");
        }
        TemplateReplyDTO templateReplyDTO = new TemplateReplyDTO();
        BeanUtils.copyProperties(templateReplyVo, templateReplyDTO);
        String[] replyKeywords = templateReplyVo.getReplyKeywords();
        if(replyKeywords!= null){
            StringBuffer keywordSbf = new StringBuffer();
            for (int i=0;i<replyKeywords.length;i++){
                if(i==replyKeywords.length-1){
                    keywordSbf.append(replyKeywords[i]);
                } else{
                    keywordSbf.append(replyKeywords[i]+",");
                }
            }
            templateReplyDTO.setReplyKeyword(keywordSbf.toString());
        }
        log.info("[微信公众号]保存自动回复消息{}", ObjectUtils.objectToJSON(templateReplyDTO));
        return wechatFacade.saveReplyMsg(templateReplyDTO, shopId);
    }

    /**
     * 保存wifi
     *
     * @return
     */
    @RequestMapping(value = "/op/save-wifi", method = RequestMethod.POST)
    @ResponseBody
    public Result saveWifi(@RequestBody WechatWifiDTO wechatWifiDTO) {
        if (StringUtils.isBlank(wechatWifiDTO.getWifiName()) || StringUtils.isBlank(wechatWifiDTO.getWifiPwd())) {
            log.error("[微信公众号]保存wifi失败,因为wifi账号或密码为空{}", LogUtils.objectToString(wechatWifiDTO));
            return Result.wrapErrorResult("", "WiFi账号或密码为空,保存失败");
        }
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        log.info("[微信公众号]保存wifi{}", LogUtils.objectToString(wechatWifiDTO));
        return wechatFacade.saveWifi(wechatWifiDTO, shopId);
    }

    /**
     * 查询自动回复消息
     *
     * @return
     */
    @RequestMapping(value = "/op/qry-replymsg-list")
    @ResponseBody
    public Result qryReplyMsgList(@PageableDefault(page = 1, value = 10) Pageable pageable) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
        int limit = pageable.getPageSize();
        TemplateReplyParam templateReplyParam = new TemplateReplyParam();
        if (searchParams.get("keyword") != null) {
            templateReplyParam.setKeyword(searchParams.get("keyword").toString());
        }
        if (searchParams.get("replyStatus") != null) {
            templateReplyParam.setReplyStatus(Integer.parseInt(searchParams.get("replyStatus").toString()));
        }
        return wechatFacade.qryReplyMsgPage(templateReplyParam, shopId, Long.valueOf(offset), Long.valueOf(limit));
    }

    /**
     * 查询二维码
     *
     * @return
     */
    @RequestMapping(value = "/op/qry-qrcode-list")
    @ResponseBody
    public Result qryQrcodeList(@PageableDefault(page = 1, value = 10) Pageable pageable) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
        int limit = pageable.getPageSize();
        QrcodeParams qrcodeParams = new QrcodeParams();
        qrcodeParams.setLimit(Long.valueOf(limit));
        qrcodeParams.setOffset(Long.valueOf(offset));
        return wechatFacade.qryQrcodePage(qrcodeParams, shopId);
    }

    /**
     * 保存微信菜单配置
     *
     * @return
     */
    @RequestMapping(value = "/op/save-menu", method = RequestMethod.POST)
    @ResponseBody
    public Result saveMenu(@RequestBody MenuDTO menuDTO) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        log.info("[微信公众号]保存微信菜单设置shopId:{},menuDTO:{}", shopId, LogUtils.objectToString(menuDTO));
        return wechatFacade.saveWechatMenu(menuDTO, shopId);
    }

    /**
     * 保存自定义菜单
     *
     * @return
     */
    @RequestMapping(value = "/op/save-custom-menu", method = RequestMethod.POST)
    @ResponseBody
    public Result saveCustomMenu(@RequestBody ButtonDTO buttonDTO) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        log.info("[微信公众号]保存自定义微信菜单shopId:{},buttonDTO:{}", shopId, LogUtils.objectToString(buttonDTO));
        return wechatFacade.saveCustomMenu(buttonDTO, shopId);
    }

    /**
     * 删除自定义菜单
     *
     * @return
     */
    @RequestMapping(value = "/op/del-custom-btn", method = RequestMethod.POST)
    @ResponseBody
    public Result delCustomMenu(@RequestParam(value = "btnId", required = true) Long btnId) {
        if(btnId==null){
            return Result.wrapErrorResult("-1","参数为空");
        }
        log.info("[微信公众号]删除自定义微信菜单:btnId{}", btnId);
        return wechatFacade.delCustomMenu(btnId);
    }

    /**
     * 查询微信菜单
     * @return
     */
    @RequestMapping(value = "/op/qry-menu", method = RequestMethod.GET)
    @ResponseBody
    public Result qryMenu() {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        WechatMenuVo wechatMenuVo = wechatFacade.qryWechatMenu(shopId);
        if (wechatMenuVo == null) {
            log.error("[微信公众号]shopId{}查询菜单配置失败",shopId);
            return Result.wrapErrorResult("", "查询菜单配置失败");
        } else {
            return Result.wrapSuccessfulResult(wechatMenuVo);
        }
    }

    /**
     * 删除自动回复
     *
     * @return
     */
    @RequestMapping(value = "/op/delete-replymsg", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteReplymsg(@RequestParam(value = "msgId", required = false) Long msgId) {
        if (msgId == null) {
            log.error("[微信公众号]参数为空,删除自动回复消息失败");
            return Result.wrapErrorResult("", "参数为空,操作失败");
        }
        log.info("[微信公众号]删除自动回复消息,msgId:{}", msgId);
        return wechatFacade.deleteReplyMsg(msgId);
    }

    /**
     * 微信优惠券设置
     *
     * @param model
     * @return
     */
    @RequestMapping("/wechat-coupon")
    public String coupon(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-coupon");
        return "yqx/page/wechat/wechat-coupon";
    }

    /**
     * 微信优惠券列表页
     *
     * @param model
     * @return
     */
    @RequestMapping("/wechat-coupon-list")
    public String couponList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-coupon");
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        model.addAttribute("searchParams", searchParams);
        return "yqx/page/wechat/wechat-coupon-list";
    }

    /**
     * 查询门店微信公众号配置
     * @return
     */
    @RequestMapping(value = "/op/qry-coupon-setting")
    @ResponseBody
    public Result qryCouponSetting() {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        return wechatFacade.qryWechatCouponCfg(shopId);
    }

    /**
     * 查询门店微信公众号历史配置
     * @return
     */
    @RequestMapping(value = "/op/qry-coupon-his-setting")
    @ResponseBody
    public Result qryCouponHisSetting() {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        return wechatFacade.qryWechatCouponHisCfg(shopId);
    }

    @RequestMapping(value = "/op/save-coupon-setting", method = RequestMethod.POST)
    @ResponseBody
    public Result saveCouponSetting(@RequestBody WechatCfgCouponVo wechatCouponVo) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        return wechatFacade.saveWechatCouponCfg(wechatCouponVo, shopId);
    }

    @RequestMapping(value = "/op/qry-coupon-statis")
    @ResponseBody
    public Result qryCouponStatis() {
        CouponStatisticsParam couponStatisticsParam = new CouponStatisticsParam();
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        if (searchParams.containsKey("startTime")) {
            String startTimeStr =  searchParams.get("startTime") + " 00:00:00";
            couponStatisticsParam.setStartTime(DateUtil.convertStringToDate(startTimeStr));
        }
        if (searchParams.containsKey("endTime")) {
            String endTimeStr =  searchParams.get("endTime") + " 23:59:59";
            couponStatisticsParam.setEndTime(DateUtil.convertStringToDate(endTimeStr));
        }
        if(searchParams.get("couponTypeId")!=null){
            couponStatisticsParam.setCouponTypeId(Long.parseLong(searchParams.get("couponTypeId").toString()));
        }
        return wechatFacade.qryCouponStatis(couponStatisticsParam, shopId);
    }

    @RequestMapping(value = "/op/qry-acount-coupon-list")
    @ResponseBody
    public Result qryAccountCouponList(@PageableDefault(page = 1, value = 10) Pageable pageable){
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        if (searchParams.containsKey("startTime")) {
            searchParams.put("gmtModifiedGt", searchParams.get("startTime") + " 00:00:00");
            searchParams.remove("startTime");
        }
        if (searchParams.containsKey("endTime")) {
            searchParams.put("gmtModifiedLt", searchParams.get("endTime") + " 23:59:59");
            searchParams.remove("endTime");
        }
        //只查询是通过门店微信公众号领取的
        searchParams.put("couponSource", AccountCouponSourceEnum.SHOP_WECHAT.getValue());
        return wechatFacade.qryAccountCouponList(searchParams, pageable);
    }

    /**
     * 关注送券,群发短信
     * @param model
     * @return
     */
    @RequestMapping("/sms-info")
    public String smsIndex(Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-coupon");
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        MarketingShopRel marketingShopRel = marketingShopRelService.selectOne(param);
        if(null!=marketingShopRel){
            model.addAttribute("smsNum", marketingShopRel.getSmsNum());
        }else{
            model.addAttribute("smsNum", 0);
        }
        return "yqx/page/wechat/wechat-sms";
    }

    /**
     * 救援定损页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/rescue-assessment-list")
    public String rescueList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-rescue-assessment");
        return "yqx/page/wechat/rescue-assessment-list";
    }
    /**
     * 救援申请
     *
     * @return
     */
    @RequestMapping(value = "/op/rescue-apply-list")
    @ResponseBody
    public Result rescueApplyList(@PageableDefault(page = 1, value = 10) Pageable pageable) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        RescueParam rescueParam = new RescueParam();
        rescueParam.setLimit(Long.valueOf(pageable.getPageSize()));
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
        rescueParam.setOffset(Long.valueOf(offset));
        if (searchParams.get("processStatus") != null) {
            rescueParam.setProcessStatus(Integer.parseInt(searchParams.get("processStatus").toString()));
        }
        if (searchParams.get("userMobile") != null) {
            rescueParam.setUserMobile(searchParams.get("userMobile").toString());
        }
        log.info("[微信公众号]救援申请列表页查询searchParams:{}", LogUtils.objectToString(searchParams));
        return wechatFacade.queryRescuesPage(rescueParam, shopId);
    }

    /**
     * 确认救援
     * @param id
     * @return
     */
    @RequestMapping(value = "/op/confirm-rescue", method = RequestMethod.POST)
    @ResponseBody
    public Result confirmRescue(@RequestParam(value = "id", required = true)Long id) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        log.info("[微信公众号]救援申请确认shopId{},rescueId{}", shopId, id);
        Integer status = RescueStatusEnum.CONFIRM.getValue();
        return wechatFacade.updateRescuesStatus(id, status);
    }
    /**
     * 取消救援
     * @param id
     * @return
     */
    @RequestMapping(value = "/op/cancel-rescue",method = RequestMethod.POST)
    @ResponseBody
    public Result cancelRescue(@RequestParam(value = "id", required = true)Long id) {
        log.info("[微信公众号]门店取消救援:id{}", id);
        Integer status = RescueStatusEnum.CANCEL.getValue();//门店取消救援信息
        return wechatFacade.updateRescuesStatus(id, status);
    }



    /**
     * 定损申请
     *
     * @return
     */
    @RequestMapping(value = "/op/assessment-apply-list")
    @ResponseBody
    public Result assessmentApplyList(@PageableDefault(page = 1, value = 10) Pageable pageable) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        AssessmentParam assessmentParam = new AssessmentParam();
        assessmentParam.setLimit(Long.valueOf(pageable.getPageSize()));
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
        assessmentParam.setOffset(Long.valueOf(offset));
        if (searchParams.get("processStatus") != null) {
            assessmentParam.setProcessStatus(Integer.parseInt(searchParams.get("processStatus").toString()));
        }
        if (searchParams.get("userMobile") != null) {
            assessmentParam.setUserMobile(searchParams.get("userMobile").toString());
        }
        Result<Page<AssessmentVo>> result = wechatFacade.queryAssessmentsPage(assessmentParam, shopId);
        log.info("[微信公众号]定损申请列表页查询,assessmentParam:{},shopId:{},success:{}", LogUtils.objectToString(assessmentParam), shopId, result.isSuccess());
        return result;
    }
    /**
     * 确认定损
     * @param assessmentId
     * @return
     */
    @RequestMapping(value = "/op/confirm-assessment", method = RequestMethod.POST)
    @ResponseBody
    public Result confirmAssessment(@RequestParam(value = "assessmentId", required = true)Long assessmentId) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        log.info("[微信公众号]救援申请确认shopId{},assessmentId{}", shopId, assessmentId);
        Integer status = AssessmentStatusEnum.CONFIRM.getValue();
        return wechatFacade.updateAssessmentStatus(assessmentId, status);
    }
    /**
     * 取消定损
     * @param assessmentId
     * @return
     */
    @RequestMapping(value = "/op/cancel-assessment",method = RequestMethod.POST)
    @ResponseBody
    public Result cancelAssessment(@RequestParam(value = "assessmentId", required = true)Long assessmentId) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Integer status = RescueStatusEnum.CANCEL.getValue();//门店取消预约单
        log.info("[微信公众号]救援申请确认shopId{},assessmentId{}", shopId, assessmentId);
        return wechatFacade.updateAssessmentStatus(assessmentId, status);
    }

    /**
     * 查询可在微信端发放的会员卡列表
     *
     * @return
     */
    @RequestMapping(value = "/op/qry-free-memberCard-list")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<MemberCardInfo>> qryFreeMemberCardList() {
        final Long shopId = UserUtils.getUserInfo(request).getShopId();
        return new ApiTemplate<List<MemberCardInfo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "shopId不能为空");
            }

            @Override
            protected List<MemberCardInfo> process() throws BizException {
                List<MemberCardInfo> memberCardInfoList = accountFacadeService.listFreeMemberCardInfo(shopId);
                log.info("[微信公众号]查询可在可在微信端发放的会员卡列表,shopId:{}", shopId);
                return memberCardInfoList;
            }
        }.execute();
    }

    /**
     * 查询卡券商城会员卡列表
     *
     * @return
     */
    @RequestMapping(value = "/op/qry-favormall-card-list")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Page<WechatFavormallCardVo>> qryFavormallCardList(@PageableDefault(page = 1, value = 10) final Pageable pageable) {
        final Long shopId = UserUtils.getUserInfo(request).getShopId();
        final Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        return new ApiTemplate<Page<WechatFavormallCardVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "shopId不能为空");
                Assert.notNull(searchParams, "查询查询条件不能为空");
            }

            @Override
            protected Page<WechatFavormallCardVo> process() throws BizException {
                QueryCardParam queryCardParam = new QueryCardParam();
                int limit = pageable.getPageSize();
                int offset = (pageable.getPageNumber() - 1) * limit;//从0开始
                queryCardParam.setLimit(limit);
                queryCardParam.setOffset(offset);
                if (searchParams.get("givingStatus") != null) {
                    queryCardParam.setGivingStatus(Integer.parseInt(searchParams.get("givingStatus").toString()));
                }
                Page<WechatFavormallCardVo> page = wechatFacade.qryFavormallCardList(shopId, queryCardParam);
                log.info("[微信公众号]查询卡券商城会员卡列表,shopid:{},queryCardParam:{}", shopId, LogUtils.objectToString(queryCardParam));
                return page;
            }
        }.execute();
    }

    /**
     * 保存卡券商城会员卡设置
     *
     * @return
     */
    @RequestMapping(value = "/op/save-favormall-card", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> saveFavormallCard(@RequestBody final SaveCardParam saveCardParam) {
        final Long shopId = UserUtils.getUserInfo(request).getShopId();
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "shopId不能为空");
                Assert.notNull(saveCardParam, "会员卡参数不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                boolean isSuccess = wechatFacade.saveFavormallCard(shopId, saveCardParam);
                log.info("[微信公众号]保存卡券商城会员卡设置,shopid:{},saveCardParam:{},success:{}", shopId, LogUtils.objectToString(saveCardParam), isSuccess);
                return isSuccess;
            }
        }.execute();
    }

    /**
     * 查询卡券商城优惠券列表
     *
     * @return
     */
    @RequestMapping(value = "/op/qry-favormall-coupon-list")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Page<WechatFavormallCouponVo>> qryFavormallCouponList(@PageableDefault(page = 1, value = 10) final Pageable pageable) {
        final Long shopId = UserUtils.getUserInfo(request).getShopId();
        final Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        return new ApiTemplate<Page<WechatFavormallCouponVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "shopId不能为空");
                Assert.notNull(searchParams, "查询查询条件不能为空");
            }

            @Override
            protected Page<WechatFavormallCouponVo> process() throws BizException {
                QueryCouponParam queryCouponParam = new QueryCouponParam();
                int limit = pageable.getPageSize();
                int offset = (pageable.getPageNumber() - 1) * limit;//从0开始
                queryCouponParam.setLimit(limit);
                queryCouponParam.setOffset(offset);
                if (searchParams.get("givingStatus") != null) {
                    queryCouponParam.setGivingStatus(Integer.parseInt(searchParams.get("givingStatus").toString()));
                }
                Page<WechatFavormallCouponVo> page = wechatFacade.qryFavormallCouponList(shopId, queryCouponParam);
                log.info("[微信公众号]查询卡券商城优惠券列表,shopid:{},queryCardParam:{}", shopId, LogUtils.objectToString(queryCouponParam));
                return page;
            }
        }.execute();
    }

    /**
     * 保存卡券商城优惠券设置
     *
     * @return
     */
    @RequestMapping(value = "/op/save-favormall-coupon", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> saveFavormallCoupon(@RequestBody final SaveCouponParam saveCouponParam) {
        final Long shopId = UserUtils.getUserInfo(request).getShopId();
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "shopId不能为空");
                Assert.notNull(saveCouponParam, "优惠券参数不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                boolean isSuccess = wechatFacade.saveFavormallCoupon(shopId, saveCouponParam);
                log.info("[微信公众号]保存卡券商城优惠券设置,shopid:{},saveCouponParam:{},success:{}", shopId, LogUtils.objectToString(saveCouponParam), isSuccess);
                return isSuccess;
            }
        }.execute();
    }

    /**
     * 检查卡券商城中是否已经配置过此会员卡,优惠券
     *
     * @return
     */
    @RequestMapping(value = "/op/isExist-favormall-config")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> isExistFavormallConfig(@RequestParam(value = "configType", required = true) final Integer configType,
                                                                                @RequestParam(value = "configId", required = true) final Long configId) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(configType, "configType不能为空");
                Assert.notNull(configId, "configId不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                boolean isExist = false;
                isExist = wechatFacade.isExistFavormallConfig(configType, configId);
                log.info("[微信公众号]检查卡券商城中是否已经配置过此会员卡,优惠券,configType:{},configId:{}", configType, configId);
                return isExist;
            }
        }.execute();
    }

}
