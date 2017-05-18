package com.tqmall.legend.web.portal;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.sell.ShopSellService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.sell.SellOrder;
import com.tqmall.legend.enums.sell.ShopSellEnum;
import com.tqmall.legend.facade.sell.SellOrderPayFaced;
import com.tqmall.legend.facade.sms.SmsSendFacade;
import com.tqmall.legend.pojo.sell.SellOrderSaveReturnVO;
import com.tqmall.legend.pojo.sell.SellOrderSaveVO;
import com.tqmall.legend.pojo.sell.SellShopTypeVO;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feilong.li on 17/2/21.
 */
@Controller
@Slf4j
@RequestMapping("portal/shop/sell")
public class ShopSellController extends BaseController {

    @Autowired
    private SmsSendFacade smsSendFacade;

    @Autowired
    private SmsService smsService;

    @Autowired
    private ShopSellService shopSellService;

    @Autowired
    private SellOrderPayFaced sellOrderPayFaced;

    @Value("${legend.tqmallstall.certification.url}")
    private String tqmallStallCertificationUrl;

    /**
     * 手机号登陆
     *
     * @return
     */
    @RequestMapping("")
    public String login(Model model) {
        model.addAttribute("certificationUrl", tqmallStallCertificationUrl);
        return "yqx/page/sell/login";
    }

    /**
     * 选择购买版本
     *
     * @return
     */
    @RequestMapping("/select/version")
    public String selectVersion(Model model) {
        try {
            String mobile = shopSellService.getMobileFromRedis(request);
            model.addAttribute("mobile", mobile);
        } catch (BizException e) {
            log.info("[获取用户缓存的手机号] 获取缓存的用户信息失败, e:", e);
            model.addAttribute("info", "会话已过期，请重新登录");
            return "yqx/page/sell/login";
        } catch (Exception e) {
            log.error("[获取用户缓存的手机号] 获取缓存的用户信息异常, e:", e);
            model.addAttribute("error", "系统异常,请稍后重试!");
            return "common/error";
        }
        return "yqx/page/sell/select-version";
    }

    /**
     * 协议
     *
     * @return
     */
    @RequestMapping("/agreement")
    public String agreement() {

        return "yqx/page/sell/agreement";
    }

    /**
     * 版本详情页跳转
     *
     * @return
     */
    @RequestMapping("/version/detail")
    public String edition() {
        return "yqx/page/sell/version-detail";
    }

    /**
     * 发送手机验证码
     *
     * @return
     */
    @RequestMapping(value = "/send-mobile-core", method = RequestMethod.POST)
    @ResponseBody
    public Result sendMobileCode(@RequestParam(value = "mobile", required = false) final String mobile) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(StringUtils.isNotBlank(mobile), "请输入正确的手机号码");
                Assert.isTrue(StringUtil.isMobileNO(mobile), "请输入正确的手机号码");
            }

            @Override
            protected String process() throws BizException {
                if (shopSellService.checkMobileIsExistShop(mobile)) {
                    throw new BizException("该手机已开通云修系统,请直接登录系统使用!");
                }
                String sendCode = smsService.generateCode();
                log.info("[手机登陆发送短信] mobile:{}, sendCode:{}", mobile, sendCode);
                SmsBase smsBase = new SmsBase();
                smsBase.setAction(Constants.Legend_SELL_SHOP_LOGIN_SMS_TPL);
                smsBase.setMobile(mobile);
                Map<String, Object> smsMap = new HashMap<>();
                smsMap.put("code", sendCode);
                smsBase.setData(smsMap);
                boolean isSuccess = smsSendFacade.sendMobileCore(mobile, sendCode, smsBase, Constants.SEND_MOBILE_CODE_TIME);
                if (isSuccess) {
                    return "发送短信成功";
                }
                throw new BizException("发送短信失败");
            }
        }.execute();
    }


    /**
     * 创建购买订单
     *
     * @param sellOrderSaveVO
     *
     * @return
     */
    @RequestMapping(value = "/sell-order/save", method = RequestMethod.POST)
    @ResponseBody
    public Result<SellOrderSaveReturnVO> createSellOrder(@RequestBody final SellOrderSaveVO sellOrderSaveVO, final HttpServletRequest request) {
        return new ApiTemplate<SellOrderSaveReturnVO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                String mobile = shopSellService.getMobileFromRedis(request);
                if (StringUtils.isBlank(mobile)) {
                    throw new IllegalArgumentException("用户信息错误,请重新登录");
                }
                sellOrderSaveVO.setMobile(mobile);
                log.info("[云修系统在线售卖] 创建购买订单. mobile:{},shopLevel:{}", mobile, sellOrderSaveVO.getShopLevel());
                Assert.notNull(sellOrderSaveVO, "请选择购买版本");
                Assert.notNull(sellOrderSaveVO.getShopLevel(), "请选择购买版本");
                Assert.notNull(sellOrderSaveVO.getSellAmount(), "购买金额为空");
                Assert.isTrue(sellOrderSaveVO.getSellAmount().compareTo(BigDecimal.ZERO) > 0, "购买金额为负");
                Integer shopLevel = sellOrderSaveVO.getShopLevel();
                ShopSellEnum shopSellEnum = ShopSellEnum.getShopSellEnumByShopLevel(shopLevel);
                Assert.notNull(shopSellEnum, "购买版本信息错误");
            }

            @Override
            protected SellOrderSaveReturnVO process() throws BizException {
                SellOrder sellOrder = sellOrderPayFaced.sellOrderSave(sellOrderSaveVO);
                SellOrderSaveReturnVO sellOrderSaveReturnVO = new SellOrderSaveReturnVO();
                sellOrderSaveReturnVO.setSellOrderSn(sellOrder.getSellOrderSn());
                sellOrderSaveReturnVO.setSource(1);
                return sellOrderSaveReturnVO;
            }
        }.execute();
    }

    /**
     * 获取售卖版本信息(名称、金额、有效期)
     *
     * @param shopLevel
     *
     * @return
     */
    @RequestMapping(value = "/sell-shop-level/detail")
    @ResponseBody
    public Result<SellShopTypeVO> getShopSellByType(@RequestParam(value = "shopLevel", required = true) final Integer shopLevel,
                                                    @RequestParam(value = "mobile", required = false) final String mobileBak) {
        return new ApiTemplate<SellShopTypeVO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopLevel, "选择版本信息错误");
            }

            @Override
            protected SellShopTypeVO process() throws BizException {
                String mobile = shopSellService.getMobileFromRedis(request);
                log.info("[云修系统在线售卖] 获取选择版本详情,mobile:{}", mobile);
                if (StringUtils.isBlank(mobile)) {
                    throw new IllegalArgumentException("用户信息错误,请重新登录");
                }
                return sellOrderPayFaced.getSellShopTypeDetail(shopLevel, mobile);
            }
        }.execute();
    }

    /**
     * 获取售卖门店版本列表
     *
     * @return
     */
    @RequestMapping(value = "/sell-shop-level/list")
    @ResponseBody
    public Result<List<SellShopTypeVO>> getSellShopTypeVOList(@RequestParam(value = "mobile", required = false) final String mobileBak) {
        return new ApiTemplate<List<SellShopTypeVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<SellShopTypeVO> process() throws BizException {
                String mobile = shopSellService.getMobileFromRedis(request);
                log.info("[云修系统在线售卖] 获取选择售卖版本列表,mobile:{}", mobile);
                if (StringUtils.isBlank(mobile)) {
                    throw new IllegalArgumentException("用户信息错误,请重新登录");
                }
                return sellOrderPayFaced.getSellShopTypeList(mobile);
            }
        }.execute();
    }
}
