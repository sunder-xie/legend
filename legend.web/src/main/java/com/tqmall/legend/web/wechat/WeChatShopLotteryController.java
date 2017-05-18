package com.tqmall.legend.web.wechat;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.dandelion.wechat.client.dto.wechat.lottery.LotteryStatisticDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.lottery.LotteryUserPageDTO;
import com.tqmall.insurance.domain.param.insurance.coupon.CouponTemplateForShopParam;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.insurance.domain.result.coupon.InsuranceCouponTemplateDTO;
import com.tqmall.insurance.service.insurance.coupon.RpcInsuranceCouponService;
import com.tqmall.legend.biz.account.CouponInfoService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.wechat.WeChatActivityLotteryFacade;
import com.tqmall.legend.facade.wechat.WechatFacade;
import com.tqmall.legend.facade.wechat.bo.ActivityLotteryBO;
import com.tqmall.legend.facade.wechat.bo.ActivityLotteryPrizeBO;
import com.tqmall.legend.facade.wechat.vo.ShopWechatVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.legend.web.vo.wechat.ActivityLotteryPrizeVO;
import com.tqmall.legend.web.vo.wechat.ActivityLotteryVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 微信公众号门店抽奖管理
 *
 * @author kang.zhao@tqmall.com
 * @version 1.0 2016/10/18
 */
@RequestMapping("/shop/wechat")
@Controller
@Slf4j
public class WeChatShopLotteryController extends BaseController {
    @Autowired
    private WechatFacade wechatFacade;
    @Autowired
    private WeChatActivityLotteryFacade weChatActivityLotteryFacade;
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private RpcInsuranceCouponService rpcInsuranceCouponService;
    @Autowired
    private ShopService shopService;
    /**
     * 抽奖活动管理页
     */
    @RequestMapping("/activity-lottery-management")
    public String activityLotteryManagementPage(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-lottery");
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result<ShopWechatVo> wechatShopResult = wechatFacade.qryShopWechat(shopId);
        ShopWechatVo shopWechatVo = wechatShopResult.getData();
        model.addAttribute("shopWechatVo", shopWechatVo);

        String lotteryPreviewUrl = weChatActivityLotteryFacade.getLotteryPreviewUrl(shopId);

        model.addAttribute("lotteryPreviewUrl", lotteryPreviewUrl);

        return "yqx/page/wechat/activity/wechat-activity-management";
    }

    @RequestMapping(value = "/lottery/get", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> getOpeningLotteryActivity() {
        Long legendShopId = UserUtils.getUserInfo(request).getShopId();
        Result<ShopWechatVo> wechatShopResult = wechatFacade.qryShopWechat(legendShopId);
        ShopWechatVo shopWechatVo = wechatShopResult.getData();

        if(shopWechatVo == null){
            return Result.wrapErrorResult(LegendError.PERMISSION_ERROR);
        }

        try{
            ActivityLotteryBO activityLotteryBO = weChatActivityLotteryFacade.getLotteryByShopId(shopWechatVo.getUcShopId());

            ActivityLotteryVO activityLotteryVO = null;

            if(null != activityLotteryBO) {
                activityLotteryVO = convertActivityLotteryBO2VO(activityLotteryBO);

                activityLotteryVO.setLotteryStartTimeStr(DateUtil.convertDateToYMD(activityLotteryVO.getLotteryStartTime()));
                activityLotteryVO.setLotteryEndTimeStr(DateUtil.convertDateToYMD(activityLotteryVO.getLotteryEndTime()));
            }

            return Result.wrapSuccessfulResult(activityLotteryVO);
        }catch(BizException e){
            log.error("门店获取抽奖活动错误,shopId= {" + shopWechatVo.getId() + "}" , e);
            return Result.wrapErrorResult(e.getCode(), e.getErrorMessage());
        }
    }

    @RequestMapping(value = "/lottery/save", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> saveLotteryActivity(@RequestBody ActivityLotteryVO activityLotteryVO) {
        String checkResult = checkActivityLottery(activityLotteryVO);

        if( checkResult != null ){
            return Result.wrapErrorResult("-10001", checkResult);
        }

        Long legendShopId = UserUtils.getUserInfo(request).getShopId();
        Result<ShopWechatVo> wechatShopResult = wechatFacade.qryShopWechat(legendShopId);
        ShopWechatVo shopWechatVo = wechatShopResult.getData();

        if(shopWechatVo == null){
            return Result.wrapErrorResult(LegendError.PERMISSION_ERROR);
        }

        if (StringUtils.isNotBlank(activityLotteryVO.getLotteryStartTimeStr())) {
            activityLotteryVO.setLotteryStartTime(DateUtil.convertStringToDate(activityLotteryVO.getLotteryStartTimeStr() + " 00:00:00"));
        }

        if (StringUtils.isNotBlank(activityLotteryVO.getLotteryEndTimeStr())) {
            activityLotteryVO.setLotteryEndTime(DateUtil.convertStringToDate(activityLotteryVO.getLotteryEndTimeStr() + " 23:59:59"));
        }

        ActivityLotteryBO activityLotteryBO = convertActivityLotteryVO2BO(activityLotteryVO);
        try{
            activityLotteryBO.setShopId(shopWechatVo.getUcShopId());
            weChatActivityLotteryFacade.saveLotteryActivity(activityLotteryBO);
        }catch(BizException e){
            log.error("门店保存抽奖活动错误,param= {" + ObjectUtils.objectToJSON(activityLotteryVO) + "}" , e);
            return Result.wrapErrorResult(e.getCode(), e.getErrorMessage());
        }

        activityLotteryVO = convertActivityLotteryBO2VO(activityLotteryBO);

        return Result.wrapSuccessfulResult(activityLotteryVO);
    }

    @RequestMapping(value = "/lottery/close", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> closeLotteryActivity() {
        Long legendShopId = UserUtils.getUserInfo(request).getShopId();
        Result<ShopWechatVo> wechatShopResult = wechatFacade.qryShopWechat(legendShopId);
        ShopWechatVo shopWechatVo = wechatShopResult.getData();

        if(shopWechatVo == null){
            return Result.wrapErrorResult(LegendError.PERMISSION_ERROR);
        }

        try{
            weChatActivityLotteryFacade.closeLotteryActivity(shopWechatVo.getUcShopId());
        }catch(BizException e){
            log.error("门店关闭抽奖活动错误,legendShopId = {" + legendShopId + "}" , e);
            return Result.wrapErrorResult(e.getCode(), e.getErrorMessage());
        }

        return Result.wrapSuccessfulResult(null);
    }

    private String checkActivityLottery(ActivityLotteryVO activityLotteryVO){
        return null;
    }

    private ActivityLotteryBO convertActivityLotteryVO2BO(ActivityLotteryVO activityLotteryVO){
        ActivityLotteryBO activityLotteryBO = new ActivityLotteryBO();

        BeanUtils.copyProperties(activityLotteryVO, activityLotteryBO);

        if(activityLotteryVO.getActivityLotteryPrizeVOList() != null){
            List<ActivityLotteryPrizeBO> activityLotteryPrizeBOList = new ArrayList<>();

            for(ActivityLotteryPrizeVO activityLotteryPrizeVO : activityLotteryVO.getActivityLotteryPrizeVOList()){
                ActivityLotteryPrizeBO activityLotteryPrizeBO = new ActivityLotteryPrizeBO();

                BeanUtils.copyProperties(activityLotteryPrizeVO, activityLotteryPrizeBO);

                activityLotteryPrizeBOList.add(activityLotteryPrizeBO);
            }

            activityLotteryBO.setActivityLotteryPrizeBOList(activityLotteryPrizeBOList);
        }

        return activityLotteryBO;
    }

    private ActivityLotteryVO convertActivityLotteryBO2VO(ActivityLotteryBO activityLotteryBO){
        ActivityLotteryVO activityLotteryVO = new ActivityLotteryVO();

        BeanUtils.copyProperties(activityLotteryBO, activityLotteryVO);

        if(activityLotteryBO.getActivityLotteryPrizeBOList() != null){
            List<ActivityLotteryPrizeVO> activityLotteryPrizeVOList = new ArrayList<>();

            for(ActivityLotteryPrizeBO activityLotteryPrizeBO : activityLotteryBO.getActivityLotteryPrizeBOList()){
                ActivityLotteryPrizeVO activityLotteryPrizeVO = new ActivityLotteryPrizeVO();

                BeanUtils.copyProperties(activityLotteryPrizeBO, activityLotteryPrizeVO);

                activityLotteryPrizeVOList.add(activityLotteryPrizeVO);
            }

            activityLotteryVO.setActivityLotteryPrizeVOList(activityLotteryPrizeVOList);
        }

        return activityLotteryVO;
    }

    /**
     *
     * @param serviceName
     * @param couponType
     * @param category 1 云修优惠券 2 保险优惠券
     * @return
     */
    @RequestMapping("/lottery/search")
    @ResponseBody
    public Result list(@RequestParam(value = "serviceName", required = false)String serviceName,
                                                    @RequestParam(value = "couponType", required = false)Integer couponType,@RequestParam(value = "category", required = false,defaultValue = "1")Integer category){
        if(category==null||category==1) {//云修优惠券
            Map<String, Object> param = ServletUtils.getParametersMapStartWith(request);
            if (serviceName != null) {
                param.put("nameLike", serviceName);
                param.put("couponStatus", 1);
            }
            if (couponType != null) {
                param.put("couponType", couponType);
                param.put("couponStatus", 1);
            }
            param.put("shopId", UserUtils.getShopIdForSession(request));
            List<CouponInfo> couponInfos = couponInfoService.selectWithCount(param);
            return Result.wrapSuccessfulResult(couponInfos);
        }else if(category==2){//保险优惠券
            CouponTemplateForShopParam couponTemplateForShopParam = new CouponTemplateForShopParam();
            couponTemplateForShopParam.setPageSize(1000);
            Long shopId = UserUtils.getShopIdForSession(request);
            Long userGlobalId = shopService.getUserGlobalId(shopId);
            couponTemplateForShopParam.setShopId(userGlobalId.intValue());

            com.tqmall.core.common.entity.Result<PageEntityDTO<InsuranceCouponTemplateDTO>> result = rpcInsuranceCouponService.queryCouponTemplateListByShopId(couponTemplateForShopParam);
            if (result!=null&&result.isSuccess()) {
                return Result.wrapSuccessfulResult(result.getData().getRecordList());
            }
        }
        return Result.wrapSuccessfulResult(null);
    }

    /**
     * 微信砍券活动的统计信息
     * @return
     */
    @RequestMapping(value = "/op/qry-lottery-statistic")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<LotteryStatisticDTO> qryLotteryStatisticList(){
        return new ApiTemplate<LotteryStatisticDTO>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                //Assert.notNull(actId, "活动id不能为空");
            }
            @Override
            protected LotteryStatisticDTO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                LotteryStatisticDTO lotteryStatisticDTO = weChatActivityLotteryFacade.qryLotteryStatisticList(shopId);
                return lotteryStatisticDTO;
            }
        }.execute();
    }

    /**
     * 微信抽奖活动内奖品的获奖用户列表信息
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/op/qry-lottery-users")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Page<LotteryUserPageDTO>> qryLotteryUsersByPrizeId(@PageableDefault(page = 1, value = 10) final Pageable pageable,
                                                                                               @RequestParam(value = "lotteryPrizeId") final Long lotteryPrizeId){

        return new ApiTemplate<Page<LotteryUserPageDTO>>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
//                Assert.notNull(actId,"活动id不能为空");
            }

            @Override
            protected Page<LotteryUserPageDTO> process() throws BizException {
                int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
                int limit = pageable.getPageSize();
               Long shopId = UserUtils.getShopIdForSession(request);
                Page<LotteryUserPageDTO> page= weChatActivityLotteryFacade.qryLotteryUsersByPrizeId(shopId,lotteryPrizeId, offset, limit);
                return page;
            }
        }.execute();
    }
}
