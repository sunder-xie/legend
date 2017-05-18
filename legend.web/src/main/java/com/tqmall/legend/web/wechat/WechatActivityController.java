package com.tqmall.legend.web.wechat;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.dandelion.wechat.client.dto.JoinActivityLimitDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActAppointmentDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActivityGroupBuyUserDetailDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.DiscountServiceDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameCouponLogDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameCouponStatisticsDTO;
import com.tqmall.dandelion.wechat.client.param.wechat.GameCouponUserLogParam;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.wechat.ShopWechatStatusEnum;
import com.tqmall.legend.enums.wechat.WechatActModuleTypeEnum;
import com.tqmall.legend.facade.activity.WechatActivityFacade;
import com.tqmall.legend.facade.activity.vo.ActivityAppointDataVo;
import com.tqmall.legend.facade.activity.vo.ActivityDetailVo;
import com.tqmall.legend.facade.activity.vo.ActivityGroupBuyShareVo;
import com.tqmall.legend.facade.activity.vo.BarginAppointDataVo;
import com.tqmall.legend.facade.activity.vo.BarginCouponDataVo;
import com.tqmall.legend.facade.activity.vo.GameActivityDetailVo;
import com.tqmall.legend.facade.activity.vo.SaveWechatActivityVo;
import com.tqmall.legend.facade.activity.vo.ShopActivityVo;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.facade.service.vo.SaveShopServiceInfoVo;
import com.tqmall.legend.facade.wechat.WechatFacade;
import com.tqmall.legend.facade.wechat.vo.ShopWechatVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门店微信活动管理(包括游戏活动)管理
 * Created by wushuai on 16/6/2.
 */
@RequestMapping("/shop/wechat")
@Controller
@Slf4j
public class WechatActivityController extends BaseController {

    @Autowired
    private WechatFacade wechatFacade;
    @Autowired
    private WechatActivityFacade wechatActivityFacade;
    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    ShopConfigureService shopConfigureService;

    /**
     * 活动列表页
     * @param model
     * @return
     */
    @HttpRequestLog
    @RequestMapping("/activity-list")
    public String activityListPage(Model model,@RequestParam(value = "actType",required = false) Integer actType) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());

        model.addAttribute("subModule", "wechat-spread");
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result<ShopWechatVo> wechatShopResult = wechatFacade.qryShopWechat(shopId);
        ShopWechatVo shopWechatVo = wechatShopResult.getData();
        int isWechatShop =0;//0未开通,1已开通
        if (shopWechatVo != null) {
            if(shopWechatVo.getShopStatus()!=null &&shopWechatVo.getShopStatus().equals(ShopWechatStatusEnum.REGISTERED.getValue())){
                isWechatShop=1;
                model.addAttribute("subModule", "wechat-activity");
            }
        }
        model.addAttribute("isWechatShop", isWechatShop);
        model.addAttribute("actType", actType);
        return "yqx/page/wechat/activity/activity-list";
    }

    /**
     * 活动详细页
     * @param model
     * @return
     */
    @RequestMapping("/activity-detail")
    public String activityDetailPage(Model model,@RequestParam(value = "actTplId") Long actTplId) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-spread");
        UserInfo userInfo = UserUtils.getUserInfo(request);
        model.addAttribute("isAdmin", userInfo.getUserIsAdmin());//userIsAdmin1表示是管理员
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result<ShopWechatVo> wechatShopResult = wechatFacade.qryShopWechat(shopId);
        ShopWechatVo shopWechatVo = wechatShopResult.getData();
        model.addAttribute("shopWechatVo", shopWechatVo);
        if (shopWechatVo != null) {
            if(shopWechatVo.getShopStatus()!=null &&shopWechatVo.getShopStatus().equals(ShopWechatStatusEnum.REGISTERED.getValue())){
                model.addAttribute("subModule", "wechat-activity");
            }
        }
        ActivityDetailVo activityDetailVo = wechatActivityFacade.getActivityDetail(shopId, actTplId);
        if(activityDetailVo==null){
            return "/common/403";
        }
        model.addAttribute("activityDetailVo",activityDetailVo);
        String wxPayConfVal = shopConfigureService.getShopConfigure(userInfo.getShopId(), ShopConfigureTypeEnum.PAYMENT.getCode(), "wechat");
        model.addAttribute("wxPayConfVal", wxPayConfVal);//是否开通微信支付 值为close,open
        return "yqx/page/wechat/activity/activity-detail";
    }

    /**
     * 游戏活动详细页
     * @param model
     * @return
     */
    @RequestMapping("/activity-game-detail")
    public String activityGameDetailPage(Model model,@RequestParam(value = "gameId") Long gameId) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-spread");
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result<ShopWechatVo> wechatShopResult = wechatFacade.qryShopWechat(shopId);
        ShopWechatVo shopWechatVo = wechatShopResult.getData();
        model.addAttribute("shopWechatVo", shopWechatVo);
        if (shopWechatVo != null) {
            if(shopWechatVo.getShopStatus()!=null &&shopWechatVo.getShopStatus().equals(ShopWechatStatusEnum.REGISTERED.getValue())){
                model.addAttribute("subModule", "wechat-activity");
            }
        }
        GameActivityDetailVo gameActivityDetailVo = null;
        try{
            gameActivityDetailVo = wechatFacade.gameDetailsByGameId(gameId,shopId);
        }catch (Exception e){
            log.error("查询微信游戏活动异常,gameId:"+gameId+",异常信息:{}",e);
        }
        if(gameActivityDetailVo==null){
            return "/common/403";
        }
        model.addAttribute("gameActivityDetailVo", gameActivityDetailVo);
        return "yqx/page/wechat/activity/activity-game-detail";
    }

    @RequestMapping(value = "/op/qry-activity-list")
    @ResponseBody
    public Result<Page<ShopActivityVo>> qryActivityList(@PageableDefault(page = 1, value = 10) Pageable pageable){
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
        int limit = pageable.getPageSize();
        searchParams.put("offset", offset);
        searchParams.put("limit", limit);
        searchParams.put("channel", 6);//6.微信活动
        //查询条件的开始结束时间要处于活动设置的时间区间
        if (searchParams.containsKey("startTime")) {
            searchParams.put("startTimeLt", searchParams.get("startTime") + " 23:59:59");
            searchParams.remove("startTime");
        }
        if (searchParams.containsKey("endTime")) {
            searchParams.put("endTimeGt", searchParams.get("endTime") + " 00:00:00");
            searchParams.remove("endTime");
        }
        searchParams.put("sorts", new String[]{"gmt_create desc"});
        int wechatActivityType = 1;
        if(searchParams.get("wechatActivityType")!=null){
            wechatActivityType = Integer.parseInt(searchParams.get("wechatActivityType").toString());
        }
        log.info("[微信公众号]查询微信活动,查询条件:{}", LogUtils.objectToString(searchParams));
        Page<ShopActivityVo> page = null;
        if(wechatActivityType==WechatActModuleTypeEnum.GAME.getValue()){
            page = wechatFacade.getActivityPage(searchParams);
        } else {
            page  = wechatActivityFacade.getActivityPage(searchParams);
        }
        log.info("[微信公众号]查询活动列表查询条件{}", LogUtils.objectToString(searchParams));
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 微信活动的预约统计信息
     * @param actId
     * @return
     */
    @RequestMapping(value = "/op/qry-activity-appoint-data")
    @ResponseBody
    public Result qryActivityAppointData(@RequestParam(value = "actId") Long actId){
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        Result<ActivityAppointDataVo> result = wechatActivityFacade.qryActivityAppointData(shopId, actId);
        log.info("[微信公众号]查询微信活动的预约统计信息:入参actId:{},success:{}", actId, result.isSuccess());
        return Result.wrapSuccessfulResult(result);
    }

    /**
     * 微信活动内服务预约的用户列表信息
     * @param pageable
     * @param actId
     * @param serviceId
     * @return
     */
    @RequestMapping(value = "/op/qry-act-appoint-list")
    @ResponseBody
    public Result qryActAppointList(@PageableDefault(page = 1, value = 10) Pageable pageable,
                                    @RequestParam(value = "actId") Long actId,
                                    @RequestParam(value = "serviceId") Long serviceId){
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
        int limit = pageable.getPageSize();
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<Page<ActAppointmentDTO>> result= wechatActivityFacade.qryActAppointList(shopId, actId, serviceId, offset, limit);
        log.info("[微信公众号]查询微信活动内服务预约的用户列表信息actId:{},serviceId:{},success:{}",actId,serviceId,result.isSuccess());
        return result;
    }


    /**
     * 保存服务实体
     * @return
     */
    @RequestMapping(value = "/op/save-shop-service-info", method = RequestMethod.POST)
    @ResponseBody
    public Result saveShopServiceInfo(@RequestBody SaveShopServiceInfoVo saveShopServiceInfoVo) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            ShopServiceInfo shopServiceInfo = shopServiceInfoFacade.save(saveShopServiceInfoVo, userInfo);
            log.info("[微信公众号-微信活动]-保存门店服务实体shopId:{},userId:{},saveShopServiceInfoVo:{},保存后的eserviceId:{}", userInfo.getShopId(), userInfo.getUserId(), LogUtils.objectToString(saveShopServiceInfoVo), shopServiceInfo.getId());
            return Result.wrapSuccessfulResult(shopServiceInfo);
        } catch (BizException e) {
            log.error("[微信公众号-微信活动]-保存门店服务实体出现业务异常shopId:{},userId:{},saveShopServiceInfoVo:{},异常信息", userInfo.getShopId(), userInfo.getUserId(), LogUtils.objectToString(saveShopServiceInfoVo), e);
            return Result.wrapErrorResult("-1", e.getMessage());
        } catch (Exception e) {
            log.error("[微信公众号-微信活动]-保存门店服务实体出现系统异常shopId:{},userId:{},saveShopServiceInfoVo:{},异常信息", userInfo.getShopId(), userInfo.getUserId(), LogUtils.objectToString(saveShopServiceInfoVo), e);
            return Result.wrapErrorResult("-1", "系统内部错误");
        }
    }

    /**
     * 保存微信活动实体
     * @return
     */
    @RequestMapping(value = "/op/save-shop-wechat-activity", method = RequestMethod.POST)
    @ResponseBody
    public Result saveShopWechatActivity(@RequestBody SaveWechatActivityVo saveWechatActivityVo) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Result result = wechatActivityFacade.save(saveWechatActivityVo, userInfo);
        log.info("[微信公众号-微信活动]保存门店活动shopId:{},userId:{},saveWechatActivityVo:{},success:{}", userInfo.getShopId(), userInfo.getUserId(), LogUtils.objectToString(saveWechatActivityVo), result.isSuccess());
        return result;
    }

    /**
     * 获取微信活动页面地址
     * @return
     */
    @RequestMapping(value = "/op/get-activity-url")
    @ResponseBody
    public Result getActivityPreviewUrl(@RequestParam(value = "actId") Long actId,
                                        @RequestParam(value = "isFormal",required = false,defaultValue = "0") Integer isFormal,
                                        @RequestParam(value = "wechatActivityType",required = false,defaultValue = "1") Integer wechatActivityType) {//1.预约活动，2.游戏活动

        Long shopId = UserUtils.getShopIdForSession(request);
        try {
            Result<String> result = null;
            if (wechatActivityType.intValue() == WechatActModuleTypeEnum.GAME.getValue()) {
                result = wechatFacade.getGameActUrl(shopId, actId, isFormal);
            } else if (wechatActivityType.intValue() == WechatActModuleTypeEnum.SERVICE.getValue()
                    ||wechatActivityType.intValue() == WechatActModuleTypeEnum.DISCOUNT.getValue()){
                result = wechatActivityFacade.getActivityPreviewUrl(shopId, actId, isFormal);
            } else {
                result = Result.wrapErrorResult("-1","为定义的活动类型");
            }
            log.info("[微信公众号-微信活动]获取活动url,shopId:{},actId:{},isFormal:{},success:{}", shopId, actId, isFormal, result.isSuccess());
            return result;
        } catch (Exception e){
            log.error("[微信公众号-微信活动]获取活动url出现异常,shopId:{},actId:{},isFormal:{},异常信息:", shopId, actId, isFormal, e);
            return Result.wrapErrorResult("-1","出现异常");
        }
    }

    /**
     * 保存微信活动实体
     * @return
     */
    @RequestMapping(value = "/op/save-wechat-game-activity", method = RequestMethod.POST)
    @ResponseBody
    public Result saveWechatGameActivity(@RequestBody GameActivityDetailVo gameActivityDetailVo) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try{
            Result result = wechatFacade.saveWechatGameActivity(gameActivityDetailVo,userInfo);
            log.info("[微信公众号-微信活动]保存门店活动shopId:{},saveWechatActivityVo:{},success:{}", userInfo.getShopId(), LogUtils.objectToString(gameActivityDetailVo), result.isSuccess());
            return result;
        }catch (BizException e){
            log.error("[微信公众号-微信活动]shopId:{},保存微信游戏活动出现异常,gameActivityDetailVo:{},异常信息:", userInfo.getShopId(), LogUtils.objectToString(gameActivityDetailVo), e);
            return Result.wrapErrorResult("-1",e.getMessage());
        }catch (Exception e){
            log.error("[微信公众号-微信活动]shopId:{}保存微信游戏活动出现异常,gameActivityDetailVo:{},异常信息:", userInfo.getShopId(), LogUtils.objectToString(gameActivityDetailVo), e);
            return Result.wrapErrorResult("-1","内部错误");
        }
    }

    /**
     * 微信游戏活动的领券信息
     * @param gameId
     * @return
     */
    @RequestMapping(value = "/op/qry-game-activity-coupon-list")
    @ResponseBody
    public Result qryGameActivityCouponListData(@RequestParam(value = "gameId") Long gameId){
        Long shopId = UserUtils.getShopIdForSession(request);
        try{
            GameCouponStatisticsDTO gameCouponStatisticsDTO = wechatFacade.gameCouponStatistic(gameId, shopId);
            return Result.wrapSuccessfulResult(gameCouponStatisticsDTO);
        } catch (Exception e){
            log.error("[微信公众号-微信活动]shopId:{},gameId:{},查询微信游戏活动的领券信息出现异常:", shopId, gameId, e);
            return Result.wrapErrorResult("-1","查询失败");
        }
    }

    /**
     * 微信游戏活动优惠券领取用户列表信息
     * @param pageable
     * @param gameId
     * @param couponInfoId
     * @return
     */
    @RequestMapping(value = "/op/qry-game-coupon-user-list")
    @ResponseBody
    public Result qryGameCouponUserList(@PageableDefault(page = 1, value = 10) Pageable pageable,
                                    @RequestParam(value = "gameId") Long gameId,
                                    @RequestParam(value = "couponInfoId") Long couponInfoId){
        Long shopId = UserUtils.getShopIdForSession(request);
        GameCouponUserLogParam gameCouponUserLogParam = new GameCouponUserLogParam();
        try{
            int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
            int limit = pageable.getPageSize();
            gameCouponUserLogParam.setOffset(offset);
            gameCouponUserLogParam.setLimit(limit);
            gameCouponUserLogParam.setGameId(gameId);
            gameCouponUserLogParam.setCouponInfoId(couponInfoId);
            Page<GameCouponLogDTO> page = wechatFacade.getGameCouponUserList(shopId, gameCouponUserLogParam);
            return Result.wrapSuccessfulResult(page);
        }catch (Exception e){
            log.error("[微信公众号-微信活动]查询微信游戏活动优惠券领取用户列表出现异常,shopId:{},参数:{},异常信息:", shopId, LogUtils.objectToString(gameCouponUserLogParam), e);
            return Result.wrapErrorResult("-1","查询失败");
        }
    }

    /**
     * 查询参加活动限制信息
     *
     * @param actTplId
     * @return
     */
    @RequestMapping(value = "/op/get-join-activity-limit")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<JoinActivityLimitDTO> getJoinActivityLimit(@RequestParam(value = "actTplId") final Long actTplId){
        return new ApiTemplate<JoinActivityLimitDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(actTplId,"模版Id不能为空");
            }
            @Override
            protected JoinActivityLimitDTO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                JoinActivityLimitDTO joinActivityLimitDTO = wechatActivityFacade.getJoinActivityLimit(actTplId,shopId);
                log.info("[微信公众号-微信活动]查询参加活动限制信息,actTplId:{},shopId:{}", actTplId, shopId);
                return joinActivityLimitDTO;
            }
        }.execute();
    }

    /**
     * 微信砍价活动的预约统计信息
     * @param actId
     * @return
     */
    @RequestMapping(value = "/op/qry-bargin-appoint-data")
    @ResponseBody
    public Result qryBarginAppointData(@RequestParam(value = "actId") Long actId){
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<BarginAppointDataVo> result = wechatActivityFacade.qryBarginAppointData(shopId, actId);
        log.info("[微信公众号-微信活动]查询微信砍价活动的预约统计信息:入参actId:{},success:{}", actId, result.isSuccess());
        return Result.wrapSuccessfulResult(result);
    }

    /**
     * 微信砍价活动内服务预约的用户列表信息
     * @param pageable
     * @param actId
     * @param serviceId
     * @return
     */
    @RequestMapping(value = "/op/qry-bargin-appoint-list")
    @ResponseBody
    public Result qryBarginAppointList(@PageableDefault(page = 1, value = 10) Pageable pageable,
                                    @RequestParam(value = "actId") Long actId,
                                    @RequestParam(value = "serviceId") Long serviceId){
        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
        int limit = pageable.getPageSize();
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<Page<DiscountServiceDTO>> result= wechatActivityFacade.qryBarginAppointList(shopId, actId, serviceId, offset, limit);
        log.info("[微信公众号-微信活动]查询微信砍价活动内服务预约的用户列表信息actId:{},serviceId:{},success:{}", actId, serviceId, result.isSuccess());
        return result;
    }

    /**
     *  查询可发布到砍价活动的门店服务
     * @param serviceId
     * @param serviceName
     * @return
     */
    @RequestMapping(value = "/op/qry-discount-service-list")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<ShopServiceInfo>> qryDiscountServiceList(@RequestParam(value = "serviceId",required = false) final Long serviceId,
                                                                                               @RequestParam(value = "serviceName",required = false) final String serviceName){
        return new ApiTemplate<List<ShopServiceInfo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }
            @Override
            protected List<ShopServiceInfo> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Map<String,Object> qryparam = new HashMap<>();
                qryparam.put("shopId",shopId);
                qryparam.put("id",serviceId);
                qryparam.put("serviceNameLike",serviceName);
                qryparam.put("appPublishStatus",1);//车主服务状态:1已发布
                qryparam.put("priceType",1);//1 正常价格数值显示 2 到店洽谈 3 免费
                qryparam.put("offset",0);
                qryparam.put("limit",50);
                List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(qryparam);
                shopServiceInfoService.setServiceSuitAmount(shopServiceInfoList);
                shopServiceInfoService.setSuitAmount2ServicePrice(shopServiceInfoList);
                log.info("[微信公众号-微信活动]查询可发布到砍价活动的门店服务qryparam:{}", LogUtils.objectToString(qryparam));
                return shopServiceInfoList;
            }
        }.execute();
    }

    /**
     *  查询服务是否在活动中
     * @param serviceId
     * @return
     */
    @RequestMapping(value = "/op/whether-service-in-activity")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> whetherServiceInActivity(@RequestParam(value = "serviceId") final Long serviceId){
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(serviceId,"服务id不能为空");
            }
            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                boolean inActivity = wechatActivityFacade.whetherServiceInActivity(shopId, serviceId);
                return inActivity;
            }
        }.execute();
    }


    /**
     * 微信拼团活动的拼团用户信息
     * @param actId
     * @return
     */
    @RequestMapping(value = "/op/qry-activity-groupbuy-share-data")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<ActivityGroupBuyShareVo> qryActivityGroupbugShareData(@RequestParam(value = "actId") final Long actId){
        return new ApiTemplate<ActivityGroupBuyShareVo>(){

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(actId,"活动id不能为空");
            }

            @Override
            protected ActivityGroupBuyShareVo process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                ActivityGroupBuyShareVo activityGroupBuyShareVo = wechatActivityFacade.qryActivityGroupbugShareData(shopId, actId);
                return activityGroupBuyShareVo;
            }
        }.execute();
    }

    /**
     * 微信拼团活动单个服务详细用户列表信息
     * @param pageable
     * @param actId
     * @param serviceId
     * @return
     */
    @RequestMapping(value = "/op/qry-act-groupbuy-user-detail-list")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Page<ActivityGroupBuyUserDetailDTO>> qryActGroupbuyUserDetailList(@PageableDefault(page = 1, value = 10) final Pageable pageable,
                                                                                                                  @RequestParam(value = "actId") final Long actId,
                                                                                                                  @RequestParam(value = "serviceId") final Long serviceId) {
        return new ApiTemplate<Page<ActivityGroupBuyUserDetailDTO>>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(actId,"活动id不能为空");
                Assert.notNull(serviceId,"服务id不能为空");
            }

            @Override
            protected Page<ActivityGroupBuyUserDetailDTO> process() throws BizException {
                int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
                int limit = pageable.getPageSize();
                Long shopId = UserUtils.getShopIdForSession(request);
                Page<ActivityGroupBuyUserDetailDTO> page = wechatActivityFacade.qryQroupBuyUserDetail(shopId, actId, serviceId, offset, limit);
                return page;
            }
        }.execute();
    }

    /**
     * 微信砍券活动的统计信息
     * @param actId
     * @return
     */
    @RequestMapping(value = "/op/qry-bargin-coupon-data")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<BarginCouponDataVo> qryBarginCouponData(@RequestParam(value = "actId") final Long actId){
        return new ApiTemplate<BarginCouponDataVo>(){

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(actId,"活动id不能为空");
            }

            @Override
            protected BarginCouponDataVo process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                BarginCouponDataVo barginCouponDataVo = wechatActivityFacade.qryBarginCouponData(shopId, actId);
                return barginCouponDataVo;
            }
        }.execute();
    }

    /**
     * 微信砍券活动内服务的用户列表信息
     * @param pageable
     * @param actId
     * @param couponTplId
     * @return
     */
    @RequestMapping(value = "/op/qry-bargin-coupon-users")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Page<DiscountServiceDTO>> qryBarginCouponUsers(@PageableDefault(page = 1, value = 10) final Pageable pageable,
                                       @RequestParam(value = "actId") final Long actId,
                                       @RequestParam(value = "couponTplId") final Long couponTplId){

        return new ApiTemplate<Page<DiscountServiceDTO>>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(actId,"活动id不能为空");
                Assert.notNull(couponTplId,"优惠券模版id不能为空");
            }

            @Override
            protected Page<DiscountServiceDTO> process() throws BizException {
                int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();//从0开始
                int limit = pageable.getPageSize();
                Long shopId = UserUtils.getShopIdForSession(request);
                Page<DiscountServiceDTO> page= wechatActivityFacade.qryBarginCouponUsers(shopId, actId, couponTplId, offset, limit);
                return page;
            }
        }.execute();
    }

}
