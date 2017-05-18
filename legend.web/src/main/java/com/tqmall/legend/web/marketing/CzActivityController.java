package com.tqmall.legend.web.marketing;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.marketing.activity.CzActivityService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.marketing.activity.CzActivity;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/11/10.
 *
 * 门店活动 车主app端活动
 */
@Slf4j
@Controller
@RequestMapping("shop/cz_app/activity")
public class CzActivityController extends BaseController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CzActivityService activityService;

    @Autowired
    private ShopServiceCateService shopServiceCateService;

    /**
     * create by jason 2015-11-10
     * 办单店活动list表页
     *
     * @return "activity/act_list"
     */
    @HttpRequestLog
    @RequestMapping("act_list")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("activity/act_list");
        mv.addObject("moduleUrl", "marketing");
        mv.addObject("subModule", "promotion");
        return mv;
    }

    /**
     * create by jason 2015-11-10
     * 获取门店活动列表数据
     *
     * @return List<CzActivity>
     */
    @RequestMapping("list/ng")
    @ResponseBody
    public Result getActList(Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (userInfo == null) {
            return Result.wrapErrorResult("-1", "用户登录失效，请登录后再尝试");
        }

        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (null == shop) {
            log.warn("该门店不存在,shopId={}", shopId);
            return Result.wrapErrorResult("-1", "该门店不存在");
        }

        Map map = new HashMap(2);
        map.put("shopId", shopId);
        List<CzActivity> result = activityService.select(map);
        if (CollectionUtils.isEmpty(result)) {
            return Result.wrapSuccessfulResult(result);
        } else{
            for (CzActivity activity : result) {
                activity.setAddress(shop.getAddress());//门店地址
                activity.setShopName(shop.getName());//门店名称
            }
        }

        model.addAttribute("moduleUrl", "marketing");
        return Result.wrapSuccessfulResult(result);
    }


    /**
     * 新增门店活动页
     *
     * @return "activity/add_act"
     */
    @RequestMapping("add_act")
    public ModelAndView addIndex(){
        ModelAndView mv = new ModelAndView("activity/add_act");
        mv.addObject("moduleUrl", "marketing");
        mv.addObject("subModule", "center-act_list");
        //门店活动类别
        mv.addObject("activityTypeList", shopServiceCateService.selectFirstCate());
        return mv;
    }

    /**
     * 创建/保存门店活动数据
     *
     * @param activity
     * @return 活动ID
     */
    @RequestMapping("add/ng")
    @ResponseBody
    public Result addAct(@RequestBody CzActivity activity) {
        //验证和设置一些基础信息
        Result result = setBaseInfo(activity, 1);
        if (!result.isSuccess()) {
            log.info("验证信息出错={}", result.getErrorMsg());
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }

        try {
            //保存
            Result rst = activityService.save(activity);
            if (rst.isSuccess()) {
                return Result.wrapSuccessfulResult(rst.getData());
            } else {
                log.warn("保存门店信息失败:{}", rst.getErrorMsg());
                return Result.wrapErrorResult(rst.getCode(), rst.getErrorMsg());
            }
        } catch (Exception e) {
            log.error("保存门店活动信息异常:{}", e);
            return Result.wrapErrorResult("-999", "保存门店活动信息异常");
        }
    }

    /**
     * 编辑门店活动页
     *
     * @param actId 门店活动ID
     * @return "activity/edit_act"
     */
    @RequestMapping("edit_act")
    public ModelAndView editAct(@RequestParam(value="actId", required=true) Long actId, HttpServletRequest request){

        ModelAndView mv = new ModelAndView("activity/edit_act");
        Long shopId = UserUtils.getShopIdForSession(request);
        Map map = new HashMap();
        map.put("shopId", shopId);
        map.put("id", actId);
        //组装门店信息
        log.info("组装门店信息参数={}", map);
        CzActivity activity = activityService.wrapActivity(map);
        if (null != activity) {
            //前端时间'永久'判断
            activity.setEndTimeLong(activity.getEndTime().getTime());
        }
        //门店活动类别
        mv.addObject("activityTypeList", shopServiceCateService.selectFirstCate());
        mv.addObject("activity", activity);
        mv.addObject("moduleUrl", "marketing");
        mv.addObject("subModule", "promotion");

        return mv;
    }

    /**
     * 编辑门店活动数据
     *
     * @param activity
     * @return true
     */
    @RequestMapping("edit/ng")
    @ResponseBody
    public Result editAct(@RequestBody CzActivity activity) {
        //验证和设置一些基础信息
        Result result = setBaseInfo(activity, 0);
        if (!result.isSuccess()) {
            log.info("验证信息出错={}", result.getErrorMsg());
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }

        if (activity.getActStatus() == 3) {
            //审核不通过的活动,经过编辑后,转为草稿
            activity.setActStatus(0);
        }
        try {
            //更新
            Result rst = activityService.update(activity, null);
            if (rst.isSuccess()) {
                return Result.wrapSuccessfulResult(true);
            } else {
                log.warn("更新门店信息失败:{}", rst.getErrorMsg());
                return Result.wrapErrorResult(rst.getCode(), rst.getErrorMsg());
            }
        } catch (Exception e) {
            log.error("更新门店活动信息异常:{}", e);
            return Result.wrapErrorResult("-999", "更新门店活动信息异常");
        }
    }

    /**
     * 编辑页发布活动
     *
     * @param activity
     * @return true
     */
    @RequestMapping("open/ng")
    @ResponseBody
    public Result openAct(@RequestBody CzActivity activity) {

        //验证和设置一些基础信息
        Result result = setBaseInfo(activity, 0);
        if (!result.isSuccess()) {
            log.info("验证信息={}", result.getErrorMsg());
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
        activity.setActStatus(1);//提交审核
        try {
            //更新
            Result rst = activityService.update(activity, 1);
            if (rst.isSuccess()) {
                return Result.wrapSuccessfulResult(true);
            } else {
                log.warn("发布门店信息失败:{}", rst.getErrorMsg());
                return Result.wrapErrorResult(rst.getCode(), rst.getErrorMsg());
            }
        } catch (Exception e) {
            log.error("发布门店活动信息异常:{}", e);
            return Result.wrapErrorResult("-999", "发布门店活动信息异常");
        }
    }

    /**
     * 列表页发布门店活动
     *
     * @param actId
     * @return true
     */
    @RequestMapping("release_act/ng")
    @ResponseBody
    public Result releaseAct(@RequestParam(value = "actId", required = true) Long actId) {
        log.info("发布门店活动参数={}", actId);

        Result actResult = getCzActivity(actId);
        if (!actResult.isSuccess()) {
            return Result.wrapErrorResult(actResult.getCode(), actResult.getErrorMsg());
        }
        CzActivity czActivity = (CzActivity) actResult.getData();
        editAct(czActivity);
        czActivity.setActStatus(1);//1提交待审核
        Result result = activityService.updateActStatus(czActivity);
        log.info("发布门店活动返回信息={}", JSONUtil.object2Json(result));
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult("发布活动成功，活动审核需1-2个工作日!");
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }

    /**
     * 删除门店活动
     *
     * @param actId
     * @return true
     */
    @RequestMapping("del_act/ng")
    @ResponseBody
    public Result delAct(@RequestParam(value = "actId", required = true) Long actId) {
        log.info("删除门店活动参数={}", actId);
        Result actResult = getCzActivity(actId);
        if (!actResult.isSuccess()) {
            return Result.wrapErrorResult(actResult.getCode(), actResult.getErrorMsg());
        }
        CzActivity czActivity = (CzActivity) actResult.getData();
        //根据门店活动ID和shopId删除
        Result result = activityService.deleteAct(czActivity);
        log.info("删除门店活动返回信息={}", JSONUtil.object2Json(result));
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult("删除活动成功!");
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }

    /**
     * 下线门店活动
     *
     * @param actId
     * @return true
     */
    @RequestMapping("off_act/ng")
    @ResponseBody
    public Result offlineAct(@RequestParam(value = "actId", required = true) Long actId) {
        log.info("下线门店活动参数={}", actId);
        Result actResult = getCzActivity(actId);
        if (!actResult.isSuccess()) {
            return Result.wrapErrorResult(actResult.getCode(), actResult.getErrorMsg());
        }
        CzActivity czActivity = (CzActivity) actResult.getData();
        czActivity.setActStatus(0);//0下线转为草稿
        Result result = activityService.updateActStatus(czActivity);
        log.info("下线门店活动返回信息={}", JSONUtil.object2Json(result));
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult("下线活动成功!");
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }

    /**
     * 验证门店活动数据有效性
     *
     * @param activity
     * @return true
     */
    private Result validateActInfo(CzActivity activity) {
        if (null == activity) {
            return Result.wrapErrorResult("-1", "门店数据对象不能为空!");
        }
        String actName = activity.getActName().trim();
        String actDesc = activity.getActDesc().trim();
        if (StringUtils.isEmpty(actName)) {
            return Result.wrapErrorResult("-1", "活动名称不能为空!");
        } else if (StringUtils.isEmpty(actDesc)) {
            return Result.wrapErrorResult("-1", "活动简介不能为空!");
        } else if (StringUtils.isEmpty(activity.getStartTimeStr())) {
            return Result.wrapErrorResult("-1", "活动开始时间不能为空!");
        } else if (CollectionUtils.isEmpty(activity.getActCateList())) {
            return Result.wrapErrorResult("-1", "活动类别不能为空!");
        } else if (StringUtils.isEmpty(activity.getDetailDesc())) {
            return Result.wrapErrorResult("-1", "活动首页不能为空!");
        }

        if (actName.length() > 20) {
            return Result.wrapErrorResult("-1", "活动名称不能超过20个字!");
        } else if (actDesc.length() > 40) {
            return Result.wrapErrorResult("-1", "活动简介不能超过40个字!");

        }
        activity.setActName(actName);
        activity.setActDesc(actDesc);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 根据actId获取活动对象
     *
     * @param actId
     * @return czActivity
     */
    private Result getCzActivity(Long actId) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (null == userInfo) {
            log.info("用户登录失效,userInfo=null");
            return Result.wrapErrorResult("-1", "用户登录失效, 请登录后再尝试");
        }
        Map map = new HashMap();
        map.put("shopId", userInfo.getShopId());
        map.put("id", actId);
        //根据门店活动ID和shopId查找
        List<CzActivity> actList = activityService.select(map);
        if (CollectionUtils.isEmpty(actList)) {
            log.warn("找不到活动信息,参数={}", map);
            return Result.wrapErrorResult("-1", "找不到活动信息");
        }
        CzActivity czActivity = actList.get(0);
        czActivity.setModifier(userInfo.getUserId());//修改人
        return Result.wrapSuccessfulResult(czActivity);

    }


    /**
     * 门店活动预览效果接口数据
     *
     * @param id
     * @return activity
     */
    @RequestMapping("preview/ng")
    @ResponseBody
    public Result getPreviewData(@RequestParam(value = "id", required = true)Long id) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (userInfo == null) {
            return Result.wrapErrorResult("-1", "用户登录失效，请登录后再尝试");
        }

        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (null == shop) {
            log.warn("该门店不存在,shopId={}", shopId);
            return Result.wrapErrorResult("-1","该门店不存在");
        }

        Map map = new HashMap(2);
        map.put("shopId", shopId);
        map.put("id", id);
        log.info("根据shopId和actId获取预览数据,参数:{}", map);
        List<CzActivity> result = activityService.select(map);
        if (CollectionUtils.isEmpty(result)) {
            log.info("根据shopId和actId获取预览数据为空,{}", map);
            return Result.wrapErrorResult("-1", "获取预览数据为空,请刷新重试!");
        } else{
            CzActivity activity = result.get(0);
            activity.setAddress(shop.getAddress());//门店地址
            activity.setShopName(shop.getName());//门店名称

            return Result.wrapSuccessfulResult(activity);
        }

    }

    /**
     * 验证一些基础信息
     *
     * @param activity
     * @return activity
     */
    private Result setBaseInfo(CzActivity activity, Integer flag) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (null == userInfo) {
            return Result.wrapErrorResult("-1", "用户登录失效，请登录后再尝试");
        }
        //验证门店信息
        Result result = validateActInfo(activity);
        if (!result.isSuccess()) {
            log.info("验证门店信息出错:{}", result.getErrorMsg());
            return Result.wrapErrorResult(result.getErrorMsg(), result.getErrorMsg());
        }
        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (null == shop) {
            log.warn("该门店不存在,shopId={}", shopId);
            return Result.wrapErrorResult("-1","该门店不存在");
        }

        //说明新创建的
        if (flag == 1) {
            activity.setCreator(userInfo.getUserId());
        }
        activity.setShopId(shopId);
        activity.setModifier(userInfo.getUserId());//设置修改人
        activity.setStartTime(DateUtil.getStartTime(activity.getStartTime()));//设置活动开始时间
        Date endTime = activity.getEndTime();
        if (null != endTime) {
            activity.setEndTime(DateUtil.getEndTime(endTime));//设置活动结束时间
        } else {
            //如果不传值就存2099-12-31
            activity.setEndTime(DateUtil.convertStringToDateYMD("2099-12-31"));//永久
        }
        return Result.wrapSuccessfulResult(activity);
    }
}
