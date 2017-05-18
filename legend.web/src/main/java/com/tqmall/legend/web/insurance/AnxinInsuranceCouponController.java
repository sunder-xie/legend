package com.tqmall.legend.web.insurance;

import com.tqmall.common.exception.BizException;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.insurance.domain.param.insurance.coupon.LegendCouponParam;
import com.tqmall.insurance.domain.result.coupon.ShopCouponDetailDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.insurance.AnxinInsuranceCouponFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceCouponUserVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 2016/12/7.
 */
@Controller
@Slf4j
@RequestMapping("insurance/anxin/coupon")
public class AnxinInsuranceCouponController extends BaseController {
    @Resource
    private AnxinInsuranceCouponFacade anxinInsuranceCouponFacade;

    /**
     * 获取优惠券列表
     *
     * @param mobile
     * @param amount
     * @return
     */
    @RequestMapping(value = "coupon-list", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<InsuranceCouponUserVo>> getCouponList(@RequestParam("mobile") String mobile, @RequestParam("amount") BigDecimal amount) {
        Integer ucShopId = getUcShopId();
        List<InsuranceCouponUserVo> insuranceCouponUserDTOList = anxinInsuranceCouponFacade.getCouponList(ucShopId, mobile, amount);
        return Result.wrapSuccessfulResult(insuranceCouponUserDTOList);
    }

    /**
     * 优惠券校验
     *
     * @param couponId
     * @param mode
     * @param amount
     * @return
     */
    @RequestMapping("coupon-check")
    @ResponseBody
    public Result<Boolean> checkCoupon(@RequestParam("couponId") Integer couponId,
                                       @RequestParam("mode") Integer mode,
                                       @RequestParam("amount") BigDecimal amount) {
        Integer ucShopId = getUcShopId();
        anxinInsuranceCouponFacade.checkcoupon(ucShopId, couponId, mode, amount);
        return Result.wrapSuccessfulResult(true);
    }

    @RequestMapping("coupon-thaw")
    @ResponseBody
    public Result<Boolean> thawCoupon(@RequestParam("couponId") Integer couponId) {
        Integer ucShopId = getUcShopId();
        anxinInsuranceCouponFacade.thawCoupon(ucShopId, couponId);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 优惠券使用
     *
     * @param couponId 优惠券ID
     * @param orderSn  淘汽订单号
     * @return
     */
    @RequestMapping("coupon-use")
    @ResponseBody
    public Result<Boolean> useCoupon(@RequestParam("couponId") Integer couponId,
                                     @RequestParam("orderSn") String orderSn,
                                     @RequestParam("mode") Integer mode) {
        Integer ucShopId = getUcShopId();
        anxinInsuranceCouponFacade.useCoupon(ucShopId, couponId, orderSn, mode);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 跳转到优惠券补贴
     *
     * @param model
     * @return
     */
    @RequestMapping("coupon-settle")
    public String couponSettle(Model model) {
        Integer ucShopId = getUcShopId();
        ShopCouponDetailDTO shopCouponDetail = anxinInsuranceCouponFacade.selectCouponStatistics(ucShopId);
        model.addAttribute("shopCouponDetail", shopCouponDetail);
        return "yqx/page/ax_insurance/coupon/coupon-settle";
    }

    /**
     * 优惠券核销数据初始化
     *
     * @param pageable
     * @return
     */
    @RequestMapping("coupon-settle/data")
    @ResponseBody
    public Result<DefaultPage<InsuranceCouponUserVo>> couponSettleData(@PageableDefault(page = 1, value = 12,
            direction = Sort.Direction.DESC) Pageable pageable) {
        Integer ucShopId = getUcShopId();
        Map<String, Object> searchMap = ServletUtils.getParametersMapStartWith(request);
        LegendCouponParam param = new LegendCouponParam();
        if (searchMap.containsKey("settleStatus")) {
            param.setSettleStatus(Integer.parseInt(searchMap.get("settleStatus").toString()));
        }
        if (searchMap.containsKey("currentMonth")) {
            param.setCurrentMonth(true);
        }
        if (searchMap.containsKey("mobile")) {
            param.setMobile(searchMap.get("mobile").toString());
        }
        param.setShopId(ucShopId);
        DefaultPage<InsuranceCouponUserVo> page = anxinInsuranceCouponFacade.couponSettleList(param, pageable);
        return Result.wrapSuccessfulResult(page);
    }

    private Integer getUcShopId() {
        String userGlobalId = UserUtils.getUserGlobalIdForSession(request);
        if (StringUtils.isBlank(userGlobalId) || "0".equals(userGlobalId)) {
            throw new BizException("门店信息有误");
        }
        return Long.valueOf(userGlobalId).intValue();

    }
}
