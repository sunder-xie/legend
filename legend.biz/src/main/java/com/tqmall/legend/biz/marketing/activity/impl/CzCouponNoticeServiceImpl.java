package com.tqmall.legend.biz.marketing.activity.impl;

import com.tqmall.common.Constants;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.jms.yunxiu.MessagePlatformCzApp;
import com.tqmall.legend.biz.marketing.activity.CzCouponNoticeService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.marketing.activity.CzCouponNoticeDao;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.entity.marketing.activity.CzCouponNotice;
import com.tqmall.legend.entity.shop.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/11/10.
 */
@Slf4j
@Service
public class CzCouponNoticeServiceImpl extends BaseServiceImpl implements CzCouponNoticeService {

    @Autowired
    private CzCouponNoticeDao czCouponNoticeDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private MessagePlatformCzApp czApp;


    public List<CzCouponNotice> getAll() {
        return super.getAll(czCouponNoticeDao);
    }

    public Page<CzCouponNotice> getPage(Pageable pageable, Map<String, Object> searchParams) {
        return super.getPage(czCouponNoticeDao, pageable, searchParams);
    }

    public CzCouponNotice getById(Long id) {
        return super.getById(czCouponNoticeDao, id);
    }

    public boolean deleteById(Long id) {
        return super.deleteById(czCouponNoticeDao, id);
    }


    @Override
    public List<CzCouponNotice> select(Map param) {
        return czCouponNoticeDao.select(param);
    }

    /**
     * Created by jason on 15/11/20.
     * 保存/更新门店优惠公告信息
     *
     * @param czCouponNotice
     * @return true
     */
    public Result saveOrUpdate(CzCouponNotice czCouponNotice) {
        try {
            czCouponNotice.setGmtModified(new Date());
            if (super.save(czCouponNoticeDao, czCouponNotice)) {

                return Result.wrapSuccessfulResult(true);
            } else {
                return Result.wrapErrorResult("-1", "保存或者更新门店优惠公告失败");
            }
        } catch (Exception e) {
            log.error("保存门店优惠公告异常:{}", e);
            return Result.wrapErrorResult("-999", "保存或者更新门店优惠公告异常");
        }
    }

    /**
     * Created by jason on 15/11/20.
     * 车主app端回写更新门店优惠公告信息
     *
     * @param czCouponNotice
     * @return czCouponNotice.getId()
     */
    public Result update(CzCouponNotice czCouponNotice) {
        try {
            czCouponNotice.setGmtModified(new Date());
            int cnt = czCouponNoticeDao.updateById(czCouponNotice);
            if (cnt > 0) {
                return Result.wrapSuccessfulResult(czCouponNotice.getId());
            } else {
                return Result.wrapErrorResult("-1", "调用车主app端回写更新门店优惠公告失败");
            }
        } catch (Exception e) {
            log.error("车主app端回写门店优惠公告异常:{}", e);
            return Result.wrapErrorResult("-999", "车主app端回写更新门店优惠公告异常");
        }
    }

    /**
     * Created by jason on 15/11/20.
     * 获取门店优惠公告总数量
     *
     * @param param
     * @return integer 数量
     */
    @Override
    public Integer selectCount(Map<String, Object> param) {
        return czCouponNoticeDao.selectCount(param);
    }

    /**
     * Created by jason on 15/11/20.
     * 推送消息到车主app端去审核
     */
    private void pushMsg(String type, CzCouponNotice czCouponNotice) {
        Long shopId = czCouponNotice.getShopId();
        Shop shop = shopDao.selectById(shopId);
        Map<String, Object> message = new HashMap<>();
        message.put("type", type);
        message.put("id", czCouponNotice.getId());
        message.put("userGlobalId", shop.getUserGlobalId());
        czApp.pushMsgToCzApp(message);
    }
}
