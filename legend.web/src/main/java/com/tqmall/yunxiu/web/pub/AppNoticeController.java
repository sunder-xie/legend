package com.tqmall.yunxiu.web.pub;

import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.biz.marketing.activity.CzCouponNoticeService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.marketing.activity.CouponStatusEnum;
import com.tqmall.legend.entity.marketing.activity.CzCouponNotice;
import com.tqmall.legend.entity.pub.activity.CouponNoticeVo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by jason on 15/11/12.
 * 车主门店优惠公告接口
 */
@Slf4j
@Controller
@RequestMapping("/pub/shop/notice")
public class AppNoticeController extends BaseController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private CzCouponNoticeService czCouponNoticeService;

    /**
     * create by jason 2015-11-11
     * 车主app端获取门店公告信息
     *
     * @param id 公告ID
     * @param userGlobalId 门店编码
     * @param status 状态
     */
    @RequestMapping(value = "get_notice_info", method = RequestMethod.GET)
    @ResponseBody
    public Result getNoticeInfo(@RequestParam(value = "userGlobalId", required = false) String userGlobalId,
                                @RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "status", required = false) Long status) {
        log.info("车主app端获取门店公告信息:userGlobalId={}, id={}, status={}", userGlobalId, id, status);
        //组装参数
        Map map = new HashMap<>();
        Shop shop = null;
        if (!StringUtils.isEmpty(userGlobalId)) {
            map.put("userGlobalId", userGlobalId);
            List<Shop> shopList = shopService.select(map);
            if (CollectionUtils.isEmpty(shopList)) {
                log.info("根据该门店编码找不到门店,userGlobalId={}", userGlobalId);
                return Result.wrapErrorResult("-1", "根据该门店编码找不到门店");
            }

            shop = shopList.get(0);
            map.put("shopId", shop.getId());
        }
        if (null != id) {
            map.put("id", id);
        }
        //优惠公告状态
        if (null != status) {
            map.put("couponStatus", status);
        }

        log.info("调用获取门店优惠公告信息接口:参数={}", map);
        List<CzCouponNotice> resultList = czCouponNoticeService.select(map);
        if (!CollectionUtils.isEmpty(resultList)) {
            for (CzCouponNotice czCouponNotice : resultList) {
                Long shopId = czCouponNotice.getShopId();
                if (StringUtils.isEmpty(userGlobalId)) {
                    shop = shopService.selectById(shopId);
                }
                czCouponNotice.setUserGlobalId(userGlobalId);
                czCouponNotice.setShopName(shop.getName());
                czCouponNotice.setAddress(shop.getAddress());
            }
            log.info("调用获取门店优惠公告信息接口返回数据={}", resultList);
            return Result.wrapSuccessfulResult(resultList);
        } else {
            log.info("调用获取门店优惠公告信息接口返回数据={}", resultList);
            return Result.wrapErrorResult("-1", "返回数据为空!");
        }

    }

    /**
     * create by jason 2015-11-23
     * 车主app端根据status和user_global_ids获取门店公告信息列表接口
     *
     * @param userGlobalIds 门店编码List
     * @param status 活动状态:0草稿 1提交待审核,2审核通过待发布,3审核未通过,4已发布
     * @param offset 偏移量
     * @param limit
     * @return resultList
     */
    @RequestMapping(value = "get_notice_list", method = RequestMethod.GET)
    @ResponseBody
    public Result getNoticeList(@RequestParam(value = "userGlobalId[]", required = false) List<String>
                                            userGlobalIds,
                                @RequestParam(value = "status", required = false) Long status,
                                @RequestParam(value = "offset", required = true) Long offset,
                                @RequestParam(value = "limit", required = false) Long limit,
                                @RequestParam(value = "isExpired", required = false) Integer isExpired ) {
        log.info("车主app端根据status和userGlobalId获取门店公告信息列表参数userGlobalIds={}, status={}", userGlobalIds, status);
        //组装参数
        Map map = new HashMap<>();
        Map<Long, Shop> shopMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(userGlobalIds)) {
            map.put("userGlobalIds", userGlobalIds);
            List<Shop> shopList = shopService.select(map);
            if (CollectionUtils.isEmpty(shopList)) {
                log.info("根据该门店编码找不到门店,userGlobalIds={}", userGlobalIds);
                return Result.wrapErrorResult("-1", "根据门店编码找不到门店");
            }
            //组装shopId list
            List<Long> shopIds = new ArrayList<>();
            for (Shop shop : shopList) {
                shopIds.add(shop.getId());
                shopMap.put(shop.getId(), shop);
            }
            map.put("shopIds", shopIds);
        }
        //优惠公告状态
        if (null != status) {
            map.put("couponStatus", status);
        } else {
            //获取所有status除了草稿状态
            map.put("couponStatuss", CouponStatusEnum.getStatusExceptIndex(CouponStatusEnum.DRAFT.getIndex()));
        }
        if (null == offset || offset < 0l) {
            offset = 0l;
        }
        if (null == limit || limit <= 0l) {
            limit = 10l;
        }
        //增加对应的条件：isExpired(不是必传) 1过期 2未过期 不传就返回全部
        Date nowDate = DateUtil.currentDate();//获得现在时间
        if (isExpired != null) {
            if (isExpired == 2) {
                map.put("usable", nowDate);
            } else if (isExpired == 1) {
                map.put("unusable", nowDate);
            }
        }
        Integer totalCount = czCouponNoticeService.selectCount(map);
        map.put("limit", limit);
        map.put("offset", offset);

        log.info("车主app端根据status和user_global_ids获取门店公告信息列表:参数={}", map);
        List<CzCouponNotice> resultList = czCouponNoticeService.select(map);
        if (!CollectionUtils.isEmpty(resultList)) {
            CouponNoticeVo noticeVo = new CouponNoticeVo();
            noticeVo.setTotalCount(totalCount);//总数量

            for (CzCouponNotice czCouponNotice : resultList) {
                Long shopId = czCouponNotice.getShopId();
                Shop shop;
                if (!CollectionUtils.isEmpty(shopMap)) {
                    shop = shopMap.get(shopId);
                } else {
                    shop = shopService.selectById(shopId);
                }
                if(shop !=null) {
                    czCouponNotice.setUserGlobalId(shop.getUserGlobalId());
                    czCouponNotice.setShopName(shop.getName());
                    czCouponNotice.setAddress(shop.getAddress());
                }
            }

            noticeVo.setList(resultList);
            log.info("车主app端根据status和user_global_ids获取门店公告信息列表接口返回数据={}", resultList);
            return Result.wrapSuccessfulResult(noticeVo);
        } else {
            log.info("车主app端根据status和user_global_ids获取门店公告信息列表接口返回数据={}", resultList);
            return Result.wrapErrorResult("-1", "返回数据为空!");
        }


    }

    /**
     * create by jason 2015-11-11
     * 车主app回写门店优惠公告信息
     *
     * @param czCouponNotice
     */
    @RequestMapping(value = "write_notice_info", method = RequestMethod.POST)
    @ResponseBody
    public Result writeNoticeInfo(@RequestBody CzCouponNotice czCouponNotice) {
        log.info("调用车主app回写门店公告信息接口:参数={}", czCouponNotice);
        String startTimeStr = czCouponNotice.getStartTimeStr();
        if (StringUtils.isEmpty(startTimeStr)) {
            return Result.wrapErrorResult("-1", "优惠公告开始时间不能为空");
        }
        czCouponNotice.setStartTime(DateUtil.convertStringToDate(startTimeStr));
        //参数验证
        Result paramResult = validateCouponNotice(czCouponNotice);
        if (!paramResult.isSuccess()) {
            log.error("验证参数出错:{}", paramResult.getErrorMsg());
            return Result.wrapErrorResult(paramResult.getCode(), paramResult.getErrorMsg());
        }
        //find shop
        String userGlobalId = czCouponNotice.getUserGlobalId();
        Map map = new HashMap<>();
        map.put("userGlobalId", userGlobalId);
        List<Shop> shopList = shopService.select(map);
        if (CollectionUtils.isEmpty(shopList)) {
            log.info("根据该门店编码找不到门店,userGlobalId={}", userGlobalId);
            return Result.wrapErrorResult("-1", "根据该门店编码找不到门店");
        }
        czCouponNotice.setShopId(shopList.get(0).getId());
        String endTimeStr = czCouponNotice.getEndTimeStr();
        if (StringUtils.isEmpty(endTimeStr)) {
            //如果不传值就存2099-12-31
            czCouponNotice.setEndTime(DateUtil.convertStringToDateYMD("2099-12-31"));//永久
        } else {
            czCouponNotice.setEndTime(DateUtil.convertStringToDate(endTimeStr));
        }

        Result result = czCouponNoticeService.update(czCouponNotice);
        log.info("调用车主app回写门店公告信息接口返回数据result={}", JSONUtil.object2Json(result));
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }

    /**
     * 验证门店公告数据有效性
     */
    private Result validateCouponNotice(CzCouponNotice couponNotice) {
        if (null == couponNotice) {
            return Result.wrapErrorResult("-1", "门店公告对象不能为空!");
        }
        String couponName = couponNotice.getCouponName();
        String couponDesc = couponNotice.getCouponDesc();
        Integer couponStatus = couponNotice.getCouponStatus();
        if (!StringUtils.isNumeric(couponNotice.getId() + "")) {
            return Result.wrapErrorResult("-1", "优惠公告ID不能为空!");
        } else if (StringUtils.isEmpty(couponNotice.getUserGlobalId())) {
            return Result.wrapErrorResult("-1", "门店编码不能为空!");
        } else if (!StringUtils.isNumeric(couponStatus + "")) {
            return Result.wrapErrorResult("-1", "优惠公告状态不能为空!");
        } else if (StringUtils.isEmpty(couponName)) {
            return Result.wrapErrorResult("-1", "优惠名称不能为空!");
        } else if (StringUtils.isEmpty(couponDesc)) {
            return Result.wrapErrorResult("-1", "优惠说明不能为空!");
        } else if (null == couponNotice.getStartTime()) {
            return Result.wrapErrorResult("-1", "优惠开始时间不能为空!");
        } else if (!StringUtils.isNumeric(couponNotice.getCouponType() + "")) {
            return Result.wrapErrorResult("-1", "优惠类型不能为空!");
        } else if (couponStatus == 3) {
            if (StringUtils.isEmpty(couponNotice.getReason())) {
                return Result.wrapErrorResult("-1", "原因不能为空!");
            }
        } else if (couponStatus == 4) {
            if (StringUtils.isEmpty(couponNotice.getCodeImgUrl())) {
                return Result.wrapErrorResult("-1", "二维码不能为空!");
            }
        }

        if (couponName.length() > 20) {
            return Result.wrapErrorResult("-1", "优惠名称不能超过20个字!");
        } else if (couponDesc.length() > 40) {
            return Result.wrapErrorResult("-1", "优惠说明不能超过40个字!");
        }

        return Result.wrapSuccessfulResult(true);
    }

    /**
     * create by jason 2015-11-24
     * 更新门店公告状态为活动中接口
     *
     * @param id 活动id
     */
    @RequestMapping(value = "release_notice", method = RequestMethod.GET)
    @ResponseBody
    public Result releaseNotice(@RequestParam(value = "id", required = true)Long id) {

        //find shop
        log.info("调用更新门店公告状态为活动中接口:参数={}", id);
        CzCouponNotice czCouponNotice = czCouponNoticeService.getById(id);
        if (null == czCouponNotice) {
            return Result.wrapErrorResult("-1", "根据公告ID找不到公告!");
        }
        czCouponNotice.setCouponStatus(4);//公告审核通过
        Result result = czCouponNoticeService.update(czCouponNotice);
        log.info("调调用更新门店公告状态为活动中接口返回数据={}", JSONUtil.object2Json(result));
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }
}
