package com.tqmall.legend.web.marketing;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.marketing.activity.CzCouponNoticeService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.marketing.activity.CouponStatusEnum;
import com.tqmall.legend.entity.marketing.activity.CzCouponNotice;
import com.tqmall.legend.web.common.BaseController;
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

import java.util.*;

/**
 * Created by jason on 15/11/12.
 * 门店公告
 */
@Slf4j
@Controller
@RequestMapping("/shop/cz_app/notice")
public class CzNoticeController extends BaseController{

    @Autowired
    private CzCouponNoticeService czCouponNoticeService;

    /**
     * Created by jason on 15/11/12.
     * 门店公告list页
     *
     * @return "activity/notice_list"
     */
    @HttpRequestLog
    @RequestMapping("notice_list")
    public String index(Model model) {
        model.addAttribute("moduleUrl", "marketing");
        model.addAttribute("subModule", "promotion");

        return "activity/notice_list";
    }

    /**
     * Created by jason on 15/11/12.
     * 获取门店公告列表数据
     *
     * @param model
     * @return List<CzCouponNotice>
     */
    @RequestMapping("list/ng")
    @ResponseBody
    public Result getNoticeList(Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (userInfo == null) {
            return Result.wrapErrorResult("-1", "用户登录失效，请登录后再尝试");
        }

        Map map = new HashMap(2);
        map.put("shopId", userInfo.getShopId());
        List<String> sorts = new ArrayList<>();
        sorts.add("coupon_type");//按活动类型排序
        map.put("sorts", sorts);
        log.info("获取门店公告列表数据,参数={}", map);
        List<CzCouponNotice> result = czCouponNoticeService.select(map);

        model.addAttribute("moduleUrl", "marketing");
        return Result.wrapSuccessfulResult(warpCzCouponNotice(result));
    }

    /**
     * 处理公告数据
     * 为了前端好处理,不管什么情况默认给4条数据
     *
     * @param list
     * @return List<CzCouponNotice>
     */
    private List<CzCouponNotice> warpCzCouponNotice(List<CzCouponNotice> list) {

        if (!CollectionUtils.isEmpty(list)) {
            List<Integer> couponTypes = new ArrayList<>();
            for (CzCouponNotice notice : list) {
                couponTypes.add(notice.getCouponType());
            }
            if (list.size() < 4) {
                //优惠类型:1为满减,2立减,3满返,4赠
                //new 一个list中type在{1,2,3,4}中没有的 couponNotice对象
                for (int i = 1; i <= 4 ; i++) {
                    if (!couponTypes.contains(i)) {
                        CzCouponNotice couponNotice = new CzCouponNotice();
                        couponNotice.setCouponType(i);
                        list.add(couponNotice);
                    }
                }
            }
        } else {
            //默认4条数据
            //创建type为{1,2,3,4}的couponNotice对象
            for (int i = 1; i <= 4; i++) {
                CzCouponNotice couponNotice = new CzCouponNotice();
                couponNotice.setCouponType(i);
                list.add(couponNotice);
            }
        }
        //根据type,{1,2,3,4}排序
        Collections.sort(list);
        return list;

    }

    /**
     * Created by jason on 15/11/12.
     * 新增/更新门店公告数据
     *
     * @param couponNotice
     * @return true
     */
    @RequestMapping("add/ng")
    @ResponseBody
    public Result addNotice(@RequestBody CzCouponNotice couponNotice) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (userInfo == null) {
            return Result.wrapErrorResult("-1", "用户登录失效，请登录后再尝试");
        }
        //验证公告数据
        Result result = validateCouponNotice(couponNotice);
        if (!result.isSuccess()) {
            log.info("验证信息出错:{}", result.getErrorMsg());
            return Result.wrapErrorResult(result.getErrorMsg(), result.getErrorMsg());
        }
        Long shopId = userInfo.getShopId();

        couponNotice.setShopId(shopId);//设置门店信息
        if (null == couponNotice.getId()) {
            couponNotice.setCreator(userInfo.getUserId());//设置创建人
        }
        couponNotice.setCouponStatus(0);//状态改为草稿
        couponNotice.setModifier(userInfo.getUserId());//设置创建人
        couponNotice.setStartTime(DateUtil.convertStringToDateYMD(couponNotice.getStartTimeStr()));//设置公告开始时间
        String endTimeStr = couponNotice.getEndTimeStr();
        if (!StringUtils.isEmpty(endTimeStr)) {
            couponNotice.setEndTime(DateUtil.convertStringToDateYMD(endTimeStr));//设置公告结束时间
        } else {
            //如果不传值就存2099-12-31
            couponNotice.setEndTime(DateUtil.convertStringToDateYMD("2099-12-31"));//永久
        }
        try {
            log.info("新增/修改门店优惠公告对象={}", couponNotice);
            Result rst = czCouponNoticeService.saveOrUpdate(couponNotice);
            if (rst.isSuccess()) {
                return Result.wrapSuccessfulResult(true);
            } else {
                log.warn("保存或者更新门店信息失败:{}", rst.getErrorMsg());
                return Result.wrapErrorResult(rst.getCode(), rst.getErrorMsg());
            }
        } catch (Exception e) {
            log.error("保存或者更新门店公告信息异常:{}", e);
            return Result.wrapErrorResult("-999", "保存或者更新门店公告信息异常");
        }
    }


    /**
     * Created by jason on 15/11/12.
     * 发布门店公告优惠
     *
     * @param id
     * @return true
     */
    @RequestMapping("release_coupon/ng")
    @ResponseBody
    public Result releaseCoupon(@RequestParam(value = "id", required = true) Long id) {
        log.info("发布门店公告优惠参数={}", id);
        Result noticeResult = getCouponNotice(id);
        if (!noticeResult.isSuccess()) {
            return Result.wrapErrorResult(noticeResult.getCode(), noticeResult.getErrorMsg());
        }
        CzCouponNotice czCouponNotice = (CzCouponNotice) noticeResult.getData();
        czCouponNotice.setCouponStatus(1);//1提交待审核
        Result result = czCouponNoticeService.saveOrUpdate(czCouponNotice);
        log.info("发布门店公告优惠返回结果:{}", JSONUtil.object2Json(result));
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult("发布优惠公告成功,公告审核需1-2个工作日!");
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }

    /**
     * Created by jason on 15/11/12.
     * 下线门店公告优惠
     *
     * @param id
     * @return true
     */
    @RequestMapping("off_coupon/ng")
    @ResponseBody
    public Result offlineCoupon(@RequestParam(value = "id", required = true) Long id) {
        log.info("下线门店公告优惠参数={}", id);
        Result noticeResult = getCouponNotice(id);
        if (!noticeResult.isSuccess()) {
            return Result.wrapErrorResult(noticeResult.getCode(), noticeResult.getErrorMsg());
        }
        CzCouponNotice czCouponNotice = (CzCouponNotice) noticeResult.getData();

        czCouponNotice.setCouponStatus(0);//0下线为草稿
        Result result = czCouponNoticeService.saveOrUpdate(czCouponNotice);
        log.info("下线门店公告优惠返回结果:{}", JSONUtil.object2Json(result));
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult("下线优惠公告成功!");
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }

    /**
     * Created by jason on 15/11/12.
     * 删除门店公告优惠
     *
     * @param id
     * @return true
     */
    @RequestMapping("del_coupon/ng")
    @ResponseBody
    public Result delCoupon(@RequestParam(value = "id", required = true) Long id) {
        log.info("删除门店公告优惠参数={}", id);
        Result noticeResult = getCouponNotice(id);
        if (!noticeResult.isSuccess()) {
            return Result.wrapErrorResult(noticeResult.getCode(), noticeResult.getErrorMsg());
        }
        CzCouponNotice czCouponNotice = (CzCouponNotice) noticeResult.getData();

        czCouponNotice.setIsDeleted("Y");//删除
        Result result = czCouponNoticeService.saveOrUpdate(czCouponNotice);
        log.info("删除门店公告优惠返回结果:{}", JSONUtil.object2Json(result));
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult("删除优惠公告成功!");
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }


    /**
     * Created by jason on 15/11/12.
     * 验证门店公告数据有效性
     *
     * @param couponNotice
     * @return true
     */
    private Result validateCouponNotice(CzCouponNotice couponNotice) {
        if (null == couponNotice) {
            return Result.wrapErrorResult("-1", "门店公告对象不能为空!");
        }
        String couponName = couponNotice.getCouponName().trim();
        String couponDesc = couponNotice.getCouponDesc().trim();
        if (StringUtils.isEmpty(couponName)) {
            return Result.wrapErrorResult("-1", "优惠名称不能为空!");
        } else if (StringUtils.isEmpty(couponDesc)) {
            return Result.wrapErrorResult("-1", "优惠说明不能为空!");
        } else if (StringUtils.isEmpty(couponNotice.getStartTimeStr())) {
            return Result.wrapErrorResult("-1", "优惠开始时间不能为空!");
        }

        if (couponName.length() > 20) {
            return Result.wrapErrorResult("-1", "优惠名称不能超过20个字!");
        } else if (couponDesc.length() > 40) {
            return Result.wrapErrorResult("-1", "优惠说明不能超过40个字!");
        }
        couponNotice.setCouponName(couponName);
        couponNotice.setCouponDesc(couponDesc);
        return Result.wrapSuccessfulResult(true);
    }

    //根据公告Id获取公告优惠对象
    private Result getCouponNotice(Long Id) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (null == userInfo) {
            return Result.wrapErrorResult("-1", "用户登录失效，请登录后再尝试");
        }
        Map map = new HashMap();
        map.put("shopId", userInfo.getShopId());
        map.put("id", Id);
        //根据门店ID和shopId查找
        List<CzCouponNotice> couponNoticeList = czCouponNoticeService.select(map);
        if (CollectionUtils.isEmpty(couponNoticeList)) {
            log.warn("找不到公告优惠信息,参数={}", map);
            return Result.wrapErrorResult("-1", "找不到公告优惠信息");
        }
        CzCouponNotice czCouponNotice = couponNoticeList.get(0);
        czCouponNotice.setModifier(userInfo.getUserId());//修改人
        return Result.wrapSuccessfulResult(czCouponNotice);

    }

    /**
     * Created by jason on 15/11/12.
     * 根据shopId获取门店公告预览数据
     */
    @RequestMapping("preview/ng")
    @ResponseBody
    public Result getNoticeData() {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (userInfo == null) {
            return Result.wrapErrorResult("-1", "用户登录失效，请登录后再尝试");
        }
        Map map = new HashMap(2);
        map.put("shopId", userInfo.getShopId());
        List<String> sorts = new ArrayList<>();
        sorts.add("coupon_type");//按活动类型排序
        map.put("sorts", sorts);
        //门店优惠公告状态
        map.put("couponStatuss", CouponStatusEnum.getStatusExceptIndex(CouponStatusEnum.AUDIT_NOT_PASS.getIndex()));

        List<CzCouponNotice> result = czCouponNoticeService.select(map);

        return Result.wrapSuccessfulResult(result);
    }
}
