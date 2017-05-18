package com.tqmall.yunxiu.web.pub;

import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.biz.marketing.activity.CzActivityService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.marketing.activity.CzActivity;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/11/11.
 * 门店活动,公告信息接口
 */
@Slf4j
@Controller
@RequestMapping("/pub/shop/activity")
public class AppActivityController extends BaseController{

    @Autowired
    private ShopService shopService;
    @Autowired
    private CzActivityService activityService;

    /**
     * create by jason 2015-11-11
     * 车主app端获取门店活动信息
     *
     * @param actId 活动ID
     * @param actStatus 活动状态
     * @param userGlobalId 门店编码
     */
    @RequestMapping(value = "get_act_info", method = RequestMethod.GET)
    @ResponseBody
    public Result getActInfo(@RequestParam(value = "actStatus", required = false) Long actStatus,
                             @RequestParam(value = "userGlobalId", required = true) String userGlobalId,
                             @RequestParam(value = "actId", required = true) Long actId) {
        //组装参数
        Map map = new HashMap<>();
        map.put("userGlobalId", userGlobalId);
        List<Shop> shopList = shopService.select(map);
        if (CollectionUtils.isEmpty(shopList)) {
            log.info("根据该门店编码找不到门店,userGlobalId={}", userGlobalId);
            return Result.wrapErrorResult("-1", "根据该门店编码找不到门店");
        }
        if (null != actStatus) {
            map.put("actStatus", actStatus);
        }
        Shop shop = shopList.get(0);
        map.put("id", actId);
        map.put("shopId", shop.getId());

        log.info("调用获取门店信息接口:参数={}", map);
        CzActivity result = activityService.wrapActivity(map);
        if (null != result) {
            result.setUserGlobalId(userGlobalId);
            result.setShopName(shop.getName());
            result.setAddress(shop.getAddress());
            result.setCityId(shop.getCity());
            log.info("调用获取门店信息接口返回result={}", result);
            return Result.wrapSuccessfulResult(result);
        } else {
            return Result.wrapErrorResult("-1", "根据参数返回为空!");
        }
    }

    /**
     * create by jason 2015-11-11
     * 车主app回写门店活动信息
     *
     * @param czActivity
     */
    @RequestMapping(value = "write_act_info", method = RequestMethod.POST)
    @ResponseBody
    public Result writeActInfo(@RequestBody CzActivity czActivity) {
        //参数验证
        Result paramResult = validateParam(czActivity);
        if (!paramResult.isSuccess()) {
            log.error("验证参数出错:{}", paramResult.getErrorMsg());
            return Result.wrapErrorResult(paramResult.getCode(), paramResult.getErrorMsg());
        }
        //find shop
        log.info("调用车主app回写门店信息接口:参数={}", czActivity);
        String userGlobalId = czActivity.getUserGlobalId();
        Map map = new HashMap<>();
        map.put("userGlobalId", userGlobalId);
        List<Shop> shopList = shopService.select(map);
        if (CollectionUtils.isEmpty(shopList)) {
            log.info("根据该门店编码找不到门店,userGlobalId={}", userGlobalId);
            return Result.wrapErrorResult("-1", "根据该门店编码找不到门店");
        }
        czActivity.setShopId(shopList.get(0).getId());
        Date endTime = czActivity.getEndTime();
        if (null == endTime) {
            //如果不传值就存2099-12-31
            czActivity.setEndTime(DateUtil.convertStringToDateYMD("2099-12-31"));//永久
        }
        Result result = activityService.update(czActivity, null);
        log.info("调用车主app回写门店信息接口返回数据={}", JSONUtil.object2Json(result));
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }
    
    /**
     * 验证门店活动数据有效性
     */
    private Result validateParam(CzActivity activity) {
        if (null == activity) {
            return Result.wrapErrorResult("-1", "门店活动数据对象不能为空");
        }
        String actName = activity.getActName();
        String actDesc = activity.getActDesc();
        Integer actStatus = activity.getActStatus();
        if (!StringUtils.isNumeric(activity.getId() + "")) {
            return Result.wrapErrorResult("-1", "活动ID不能为空!");
        } else if (!StringUtils.isNumeric(actStatus + "")) {
            return Result.wrapErrorResult("-1", "活动状态不能为空!");
        } else if (StringUtils.isEmpty(actName)) {
            return Result.wrapErrorResult("-1", "活动名称不能为空!");
        } else if (StringUtils.isEmpty(actDesc)) {
            return Result.wrapErrorResult("-1", "活动简介不能为空!");
        } else if (null == activity.getStartTime()) {
            return Result.wrapErrorResult("-1", "活动开始时间不能为空!");
        } else if (StringUtils.isEmpty(activity.getDetailDesc())) {
            return Result.wrapErrorResult("-1", "活动首页不能为空!");
        } else if (StringUtils.isEmpty(activity.getUserGlobalId())) {
            return Result.wrapErrorResult("-1", "门店编码不能为空!");
        } else if (CollectionUtils.isEmpty(activity.getActCateList())) {
            return Result.wrapErrorResult("-1", "活动类别不能为空!");
        } else if (actStatus == 3) {
            if (StringUtils.isEmpty(activity.getReason())) {
                return Result.wrapErrorResult("-1", "原因不能为空!");
            }
        } else if (actStatus == 4) {
            if (StringUtils.isEmpty(activity.getCodeImgUrl())) {
                return Result.wrapErrorResult("-1", "二维码不能为空!");
            }
        }

        if (actName.length() > 20) {
            return Result.wrapErrorResult("-1", "活动名称不能超过20个字!");
        } else if (actDesc.length() > 40) {
            return Result.wrapErrorResult("-1", "活动简介不能超过40个字!");

        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * create by jason 2015-11-24
     * 更新门店活动状态为活动中接口
     *
     * @param id 活动id
     */
    @RequestMapping(value = "release_act", method = RequestMethod.GET)
    @ResponseBody
    public Result releaseAct(@RequestParam(value = "id", required = true)Long id) {

        log.info("调用更新门店活动状态为活动中接口:参数={}", id);
        CzActivity czActivity = activityService.getById(id);
        if (null == czActivity) {
            return Result.wrapErrorResult("-1", "根据活动ID找不到活动!");
        }
        czActivity.setActStatus(4);//活动审核通过
        Result result = activityService.updateActStatus(czActivity);
        log.info("调用更新门店活动状态为活动中接口返回数据={}", JSONUtil.object2Json(result));
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }





}
