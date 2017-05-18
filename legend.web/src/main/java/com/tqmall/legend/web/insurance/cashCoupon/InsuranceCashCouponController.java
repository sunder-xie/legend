package com.tqmall.legend.web.insurance.cashCoupon;

import com.google.common.collect.Lists;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.SearchCashCouponParam;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.SearchFormForCashCouponParam;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.UseCashCouponParam;
import com.tqmall.insurance.domain.result.InsuranceCashCouponFormDTO;
import com.tqmall.insurance.domain.result.cashcoupon.CashCouponDetailDTO;
import com.tqmall.insurance.domain.result.cashcoupon.ShopCashCouponDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.insurance.InsuranceCashCouponFacade;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.mana.client.beans.cashcoupon.CreateRuleConfigResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zhouheng on 17/3/11.
 */
@Slf4j
@Controller
@RequestMapping("insurance/anxin/cashCoupon")
public class InsuranceCashCouponController extends BaseController {

    @Autowired
    private InsuranceCashCouponFacade insuranceCashCouponFacade;

    @Autowired
    private InsuranceFormFacade insuranceFormFacade;

    @Autowired
    private ShopService shopService;


    /**
     * 现金券规则说明页面
     *
     * @return
     */
    @RequestMapping(value = "cashCouponIntroduction")
    public String cashCouponIntroduction(Model model) {
        String howGetCashCoupon = insuranceCashCouponFacade.getCouponDesc("HowGetCashCoupon");
        String whatUseCashCoupon = insuranceCashCouponFacade.getCouponDesc("WhatUseCashCoupon");
        String kindInsuranceFormCanUse = insuranceCashCouponFacade.getCouponDesc("KindInsuranceFormCanUse");
        String cahCouponElseAdvantage = insuranceCashCouponFacade.getCouponDesc("CahCouponElseAdvantage");
        String cashCouponValidityDate = insuranceCashCouponFacade.getCouponDesc("CashCouponValidityDate");
        model.addAttribute("HowGetCashCoupon", howGetCashCoupon);
        model.addAttribute("WhatUseCashCoupon", whatUseCashCoupon);
        model.addAttribute("KindInsuranceFormCanUse", kindInsuranceFormCanUse);
        model.addAttribute("CahCouponElseAdvantage", cahCouponElseAdvantage);
        model.addAttribute("CashCouponValidityDate", cashCouponValidityDate);
        return "yqx/page/ax_insurance/cashCoupon/cash-rule-explain";
    }

    /**
     * 查询现金券信息页面
     *
     * @return
     */
    @RequestMapping(value = "cashCouponList")
    public String cashCouponList() {

        return "yqx/page/ax_insurance/cashCoupon/cash-coupon-list";

    }


    /**
     * 返利计算跳转页面
     *
     * @return
     */
    @RequestMapping(value = "cashRebateCount")
    public String cashRebateCount(Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        CreateRuleConfigResultDTO cashCouponSettleRule = insuranceCashCouponFacade.getCashCouponSettleRule(shop.getCity().toString());
        List<InsuranceCashCouponFormDTO> list = Lists.newArrayList();
        Result<List<InsuranceCashCouponFormDTO>> result = getInsuranceCashCouponFormDTOS(ucShopId);
        if(result.isSuccess()){
            list = result.getData();
        }
        model.addAttribute("list", list);
        model.addAttribute("cashCouponSettleRule", cashCouponSettleRule);
        return "yqx/page/ax_insurance/cashCoupon/cash-rebate-count";

    }


    /**
     * @return
     */
    @RequestMapping(value = "cashIntroductionHome")
    public String cashIntroductionHome() {
        return "yqx/page/ax_insurance/cashCoupon/cash-introduction-home";

    }

    /**
     * 获取门店现金券信息
     *
     * @return
     */
    @RequestMapping("getShopCashCouponInfo")
    @ResponseBody
    public Result<ShopCashCouponDTO> getShopCashCouponInfo() {
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        Integer provinceId = Integer.valueOf(shop.getProvince().toString());
        Integer companyId = null;
        if (provinceId.compareTo(6) == 0 || provinceId.compareTo(2) == 0) {
            companyId = 1;
        }
        if (provinceId.compareTo(24) == 0) {
            companyId = 2;
        }
        SearchFormForCashCouponParam param = new SearchFormForCashCouponParam();
        param.setAgentId(Integer.valueOf(ucShopId.toString()));
        param.setCompanyId(companyId);
        param.setProvince(shop.getProvinceName());
        param.setCityCode(shop.getCity().toString());
        ShopCashCouponDTO cashCouponDTO = insuranceCashCouponFacade.getShopCashCouponInfo(param);
        return Result.wrapSuccessfulResult(cashCouponDTO);
    }

    /**
     * 判断城市是否开通现金券
     *
     * @return
     */
    @RequestMapping("judgeProvince")
    @ResponseBody
    public Result<Boolean> judgeProvince() {
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        boolean flag = insuranceCashCouponFacade.judgeIsOpenCashCoupon(shop.getCity().toString());
        return Result.wrapSuccessfulResult(flag);
    }


    /**
     * 确认用券
     */
    @RequestMapping("useCashCoupon")
    @ResponseBody
    public com.tqmall.core.common.entity.Result useCashCoupon(UseCashCouponParam uesCashCouponParam) {

        String ucShopId = UserUtils.getUserGlobalIdForSession(request);

        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        Integer provinceId = Integer.valueOf(shop.getProvince().toString());
        Integer companyId = null;
        if (provinceId.compareTo(6) == 0 || provinceId.compareTo(2) == 0) {
            companyId = 1;
        }
        if (provinceId.compareTo(24) == 0) {
            companyId = 2;
        }
        uesCashCouponParam.setShopId(Integer.valueOf(ucShopId));
        uesCashCouponParam.setCompanyId(companyId);
        uesCashCouponParam.setProvince(shop.getProvinceName());
        uesCashCouponParam.setCityCode(Integer.valueOf(shop.getCity().toString()));

        return insuranceCashCouponFacade.useCashCoupon(uesCashCouponParam);

    }

    /**
     * 分页查询门店现金券信息
     *
     * @return
     */
    @RequestMapping("queryCashCouponPage")
    @ResponseBody
    public Result queryCashCouponPage(HttpServletRequest request, @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        String shopId = UserUtils.getUserGlobalIdForSession(request);
        if (StringUtils.isBlank(shopId)) {
            log.error("获取保单门店id取得失败：shopId为空");
            return Result.wrapErrorResult("", "系统错误");
        }
        String cashCouponStatus = request.getParameter("cashCouponStatus");
        String searchDayType = request.getParameter("searchDayType");
        SearchCashCouponParam couponParam = new SearchCashCouponParam();
        if (shopId != null && !shopId.equals("")) {
            couponParam.setShopId(Integer.valueOf(shopId));
        }
        if (cashCouponStatus != null && !cashCouponStatus.equals("")) {
            couponParam.setCashCouponStatus(Integer.valueOf(cashCouponStatus));
        }
        if (searchDayType != null && !searchDayType.equals("")) {
            couponParam.setSearchDayType(Integer.valueOf(searchDayType));
        }
        couponParam.setPageNum(pageable.getPageNumber());
        couponParam.setPageSize(pageable.getPageSize());
        PagingResult<CashCouponDetailDTO> pagingResult = insuranceCashCouponFacade.queryCashCouponPage(couponParam);
        DefaultPage<CashCouponDetailDTO> page = new DefaultPage(pagingResult.getList(), pageable, pagingResult.getTotal());
        return Result.wrapSuccessfulResult(page);

    }

    @RequestMapping("getCashCouponFormByShopId")
    @ResponseBody
    public Result<List<InsuranceCashCouponFormDTO>> getCashCouponFormByShopId() {
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        if (StringUtils.isBlank(ucShopId)) {
            log.error("获取保单门店id取得失败：shopId为空");
            return Result.wrapErrorResult("", "系统错误");
        }
        Result<List<InsuranceCashCouponFormDTO>> result = getInsuranceCashCouponFormDTOS(ucShopId);

        return result;
    }

    private Result<List<InsuranceCashCouponFormDTO>>getInsuranceCashCouponFormDTOS(String ucShopId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        Integer provinceId = Integer.valueOf(shop.getProvince().toString());
        Integer companyId = null;
        if (provinceId.compareTo(6) == 0 || provinceId.compareTo(2) == 0) {
            companyId = 1;
        }
        if (provinceId.compareTo(24) == 0) {
            companyId = 2;
        }
        SearchFormForCashCouponParam param = new SearchFormForCashCouponParam();
        param.setAgentId(Integer.valueOf(ucShopId.toString()));
        param.setCompanyId(companyId);
        param.setProvince(shop.getProvinceName());
        param.setCityCode(shop.getCity().toString());
        return transformResult(insuranceFormFacade.getInsuranceFormForCashCoupon(param));
    }

    //result转换
    private Result transformResult(com.tqmall.legend.common.Result resultTemp) {
        Result needResult = new Result();
        needResult.setCode(resultTemp.getCode());
        needResult.setData(resultTemp.getData());
        needResult.setErrorMsg(resultTemp.getErrorMsg());
        needResult.setSuccess(resultTemp.isSuccess());
        return needResult;
    }

}
