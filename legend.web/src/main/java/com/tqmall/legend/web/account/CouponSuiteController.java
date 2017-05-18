package com.tqmall.legend.web.account;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.account.AccountCouponService;
import com.tqmall.legend.biz.account.AccountSuiteRelService;
import com.tqmall.legend.biz.account.CouponSuiteService;
import com.tqmall.legend.biz.account.SuiteCouponRelService;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.account.vo.AccountCouponVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghui on 6/2/16.
 */
@RequestMapping("account/coupon/suite")
@Controller
@Slf4j
public class CouponSuiteController extends BaseController {
    @Autowired
    private CouponSuiteService couponSuiteService;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private SuiteCouponRelService suiteCouponRelService;
    @Autowired
    private AccountSuiteRelService accountSuiteRelService;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(Model model,@RequestParam(value = "id", required = false)Long id) {
        if(id != null && id != 0l){
            Map param = new HashMap();
            param.put("id",id);
            param.put("shopId", UserUtils.getShopIdForSession(request));
            List<CouponSuite> couponSuites = couponSuiteService.select(param);
            if(CollectionUtils.isNotEmpty(couponSuites)){
                CouponSuite couponSuite = couponSuites.get(0);
                model.addAttribute("couponSuite", couponSuite);
                param.remove("id");
                param.put("suiteId", couponSuite.getId());
                List<SuiteCouponRel> couponRels = suiteCouponRelService.select(param);
                model.addAttribute("basicOrderService",couponRels);
            }
        }
        model.addAttribute("moduleUrl","customer");
        model.addAttribute("subModule","account-setting");
        return "yqx/page/account/coupon/suite_create";
    }


    @HttpRequestLog
    @RequestMapping("list")
    @ResponseBody
    public Result list(){
        Map<String, Object> param = ServletUtils.getParametersMapStartWith(request);
        param.put("shopId", UserUtils.getShopIdForSession(request));
        List<CouponSuite> couponSuites = couponSuiteService.selectDetail(param);
        return Result.wrapSuccessfulResult(couponSuites);
    }

    @RequestMapping(value = "suiteList",method = RequestMethod.GET)
    @ResponseBody
    public Result suiteList(){
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", UserUtils.getShopIdForSession(request));
        param.put("suiteStatus",1);
        List<CouponSuite> couponSuites = couponSuiteService.select(param);
        return Result.wrapSuccessfulResult(couponSuites);
    }

    @RequestMapping("flow/list")
    @ResponseBody
    public Result flowList(@PageableDefault(page = 1, value = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> param = ServletUtils.getParametersMapStartWith(request);
        param.put("shopId", UserUtils.getShopIdForSession(request));
        param.put("couponSource", AccountSuite.SourceEnum.CHARGE.getCode());
        Page<AccountSuite> page = accountSuiteRelService.getPage(pageable, param);
        return Result.wrapSuccessfulResult(page);
    }

    @RequestMapping("get")
    @ResponseBody
    public Result get(@RequestParam(value = "id", required = false)Long id){
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", UserUtils.getShopIdForSession(request));
        param.put("suiteId", id);
        List<SuiteCouponRel> result = suiteCouponRelService.select(param);
        return Result.wrapSuccessfulResult(result);
    }

    @RequestMapping("create/insert")
    @ResponseBody
    public Result insert(@RequestBody CouponSuite couponSuite) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Map<String,Object> param = new HashMap();
        param.put("suiteName",couponSuite.getSuiteName());
        Long shopId = userInfo.getShopId();
        param.put("shopId", shopId);
        List<CouponSuite> couponSuites = couponSuiteService.select(param);
        if(CollectionUtils.isNotEmpty(couponSuites)){
            return Result.wrapErrorResult("","套餐名称不能重复");
        }
        couponSuite.setShopId(shopId);
        couponSuite.setCreator(userInfo.getUserId());
        couponSuiteService.insert(couponSuite);
        return Result.wrapSuccessfulResult("插入成功");
    }

    @RequestMapping("create/update")
    @ResponseBody
    public Result update(@RequestBody CouponSuite couponSuite) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        couponSuite.setShopId(userInfo.getShopId());
        couponSuite.setModifier(userInfo.getUserId());
        couponSuiteService.update(couponSuite);
        return Result.wrapSuccessfulResult("插入成功");
    }


    @RequestMapping("grant/insert")
    @ResponseBody
    public Result grant(@RequestBody AccountCouponVo accountCouponVo){
        UserInfo userInfo = UserUtils.getUserInfo(request);
        accountCouponVo.setShopId(userInfo.getShopId());
        Long userId = userInfo.getUserId();
        accountCouponVo.setCreator(userId);
        AccountTradeFlow flow = accountCouponService.grant(accountCouponVo);
        return Result.wrapSuccessfulResult(flow);
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(@RequestParam("id")Long id){
        Long shopId = UserUtils.getShopIdForSession(request);
        if(id == null){
            return Result.wrapErrorResult("","ID不能为空");
        }
        CouponSuite couponSuite = couponSuiteService.selectById(id, shopId);
        if(couponSuite.getId() == null){
            return Result.wrapErrorResult("","查询不到此ID结果");
        }
        couponSuite.setIsDeleted("Y");
        couponSuiteService.update(couponSuite);
        return Result.wrapSuccessfulResult("删除成功");
    }

    @RequestMapping("status/update")
    @ResponseBody
    public Result updateStatus(@RequestParam("id")Long id){
        Long shopId = UserUtils.getShopIdForSession(request);
        if(id == null){
            return Result.wrapErrorResult("","ID不能为空");
        }
        CouponSuite couponSuite = couponSuiteService.selectById(id, shopId);
        Integer status = couponSuite.getSuiteStatus();
        String result;
        if(status.intValue() == 1){
            couponSuite.setSuiteStatus(2);
            result = "停用成功";
        } else {
            couponSuite.setSuiteStatus(1);
            result = "启用成功";
        }
        couponSuiteService.update(couponSuite);
        return Result.wrapSuccessfulResult(result);
    }
}
