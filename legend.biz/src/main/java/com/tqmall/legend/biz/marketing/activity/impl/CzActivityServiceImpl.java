package com.tqmall.legend.biz.marketing.activity.impl;

import com.tqmall.common.Constants;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.jms.yunxiu.MessagePlatformCzApp;
import com.tqmall.legend.biz.marketing.activity.CzActivityService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.marketing.activity.CzActCateRelDao;
import com.tqmall.legend.dao.marketing.activity.CzActivityDao;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.entity.marketing.activity.CouponStatusEnum;
import com.tqmall.legend.entity.marketing.activity.CzActCateRel;
import com.tqmall.legend.entity.marketing.activity.CzActivity;
import com.tqmall.legend.entity.pub.service.ServiceCateVo;
import com.tqmall.legend.entity.shop.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jason on 15/11/10.
 */
@Slf4j
@Service
public class CzActivityServiceImpl extends BaseServiceImpl implements CzActivityService {

    @Autowired
    private CzActivityDao czActivityDao;
    @Autowired
    private CzActCateRelDao czActCateRelDao;
    @Autowired
    private ShopServiceCateService serviceCateService;
    @Autowired
    private ShopDao shopDao;

    @Autowired
    private MessagePlatformCzApp czApp;


    @Override
    public List<CzActivity> select(Map param) {
        return czActivityDao.select(param);
    }

    public List<CzActivity> getAll() {
        return super.getAll(czActivityDao);
    }

    public Page<CzActivity> getPage(Pageable pageable, Map<String, Object> searchParams) {
        return super.getPage(czActivityDao, pageable, searchParams);
    }

    public CzActivity getById(Long id) {
        return super.getById(czActivityDao, id);
    }

    public boolean deleteById(Long id) {
        return super.deleteById(czActivityDao, id);
    }

    public int deleteByIds(Long[] ids) {
        return super.deleteByIds(czActivityDao, ids);
    }

    /**
     * create by jason 2015-11-11
     * 保存门店活动信息
     *
     * @param czActivity
     * @return czActivity.getId()
     */
    public Result save(CzActivity czActivity) {

        try {
            Integer cnt  = czActivityDao.insert(czActivity);
            if (cnt > 0) {

                if (!insertActCate(czActivity)) {
                    return Result.wrapErrorResult("-1", "活动类别不能为空!");
                }
                return Result.wrapSuccessfulResult(czActivity.getId());
            } else {
                return Result.wrapErrorResult("-1", "保存活动信息失败!");
            }
        } catch (Exception e) {
            log.error("保存活动信息异常:{},", e);
            return Result.wrapErrorResult("-999", "保存活动信息异常!");
        }

    }


    /**
     * 保存活动类别信息
     *
     * @param czActivity
     * @return boolean
     */
    private boolean insertActCate(CzActivity czActivity) {
        //保存活动和类别的对应关系
        List<CzActCateRel> actCateList = czActivity.getActCateList();
        if (CollectionUtils.isEmpty(actCateList)) {
            //活动类别为空
            return false;
        }
        //获取车主服务一级类别
        Map<Long, ServiceCateVo> firstCateMap = serviceCateService.warpFirstCateInfo();
        for (CzActCateRel actCateRel : actCateList) {
            Long cateId = actCateRel.getCateId();//类别ID
            ServiceCateVo cateVo = firstCateMap.get(cateId);
            if (null != cateVo) {
                actCateRel.setActId(czActivity.getId());//活动ID
                actCateRel.setCateName(cateVo.getName());//类别名称
                actCateRel.setCreator(czActivity.getCreator());//创建人
                czActCateRelDao.insert(actCateRel);
            }

        }
        return true;
    }

    /**
     * 推送消息到车主app端去审核
     */
    private void pushMsg(String type, CzActivity czActivity) {
        Long shopId = czActivity.getShopId();
        Shop shop = shopDao.selectById(shopId);
        Map<String, Object> message = new HashMap<>();
        message.put("type", type);
        message.put("id", czActivity.getId());
        message.put("userGlobalId", shop.getUserGlobalId());
        czApp.pushMsgToCzApp(message);
    }


    /**
     * create by jason 2015-11-20
     * 组装门店信息
     *
     * @param param
     * @return CzActivity
     */
    public CzActivity wrapActivity(Map param) {

        CzActivity czActivity = null;
        try {
            //根据param查询
            List<CzActivity> actList = czActivityDao.select(param);
            if (!CollectionUtils.isEmpty(actList)) {
                czActivity = actList.get(0);
                Long actId = Long.valueOf(param.get("id").toString());
                param.clear();
                param.put("actId", actId);
                List<CzActCateRel> actCateList = czActCateRelDao.select(param);
                czActivity.setActCateList(actCateList);
            }
        } catch (Exception e) {
            log.error("车主app获取门店活动信息出错:{}", e);
            return null;
        }

        return czActivity;
    }



    /**
     * create by jason 2015-11-11
     * 更新门店活动状态
     *
     * @param czActivity
     * @return czActivity.getId()
     */
    public Result updateActStatus(CzActivity czActivity) {
        try {
            czActivity.setGmtModified(new Date());
            int cnt = czActivityDao.updateById(czActivity);
            if (cnt > 0) {
                if (czActivity.getActStatus() == 0) {
                    //推送消息到车主端 活动下线
                    try {
                        pushMsg(Constants.CZ_ACTIVITY_OFFLINE, czActivity);
                    } catch (Exception e) {
                        log.error("门店活动下线推送消息异常:{}", e);
                    }
                } else if (czActivity.getActStatus() == 1) {
                    //推送消息到车主端 活动发布去审核
                    try {
                        pushMsg(Constants.CZ_ACTIVITY_RELEASE, czActivity);
                    } catch (Exception e) {
                        log.error("门店活动发布推送消息异常:{}", e);
                    }
                }
            }
            return Result.wrapSuccessfulResult(czActivity.getId());

        } catch (Exception e) {
            log.error("更新门店活动信息状态异常:{},", e);
            return Result.wrapErrorResult("-999", "更新门店活动信息状态异常!");
        }

    }

    /**
     * create by jason 2015-11-11
     * 更新门店活动信息
     *
     * @param czActivity
     * @return czActivity.getId()
     */
    public Result update(CzActivity czActivity, Integer flag) {
        try {
            czActivity.setGmtModified(new Date());
            Integer cnt  = czActivityDao.updateById(czActivity);
            if (cnt > 0) {
                //如果状态是提交待审核说明是点编辑页的发布按钮过来的要push消息
                if (null != flag && flag.equals(CouponStatusEnum.SUBMIT.getIndex())) {
                    pushMsg(Constants.CZ_ACTIVITY_RELEASE, czActivity);
                }
                if (!updateActCate(czActivity)) {
                    return Result.wrapErrorResult("-1", "更新活动类别信息出错!");
                }
                return Result.wrapSuccessfulResult(czActivity.getId());
            } else {
                return Result.wrapErrorResult("-1", "更新活动信息失败!");
            }
        } catch (Exception e) {
            log.error("更新活动信息异常:{},", e);
            return Result.wrapErrorResult("-999", "更新活动信息异常!");
        }

    }

    /**
     * 更新活动类别信息
     *
     * @param czActivity
     * @return boolean
     */
    private boolean updateActCate(CzActivity czActivity) {
        try {
            //活动和类别的对应关系
            List<CzActCateRel> actCateList = czActivity.getActCateList();
            //页面上过来的活动类别组装成ID List
            if (!CollectionUtils.isEmpty(actCateList)) {
                List<Long> actCateRelIds = new ArrayList<>();
                for (CzActCateRel czActCateRel : actCateList) {
                    actCateRelIds.add(czActCateRel.getCateId());
                }
                //old活动和类别的对应关系
                Long actId = czActivity.getId();
                Map map = new HashMap(2);
                map.put("actId", actId);
                List<CzActCateRel> oldActCateList = czActCateRelDao.select(map);
                //DB中原来的活动类别组装成 ID List
                if (!CollectionUtils.isEmpty(oldActCateList)) {
                    List<Long> oldActCateRelIds = new ArrayList<>();
                    for (CzActCateRel czActCateRel : oldActCateList) {
                        oldActCateRelIds.add(czActCateRel.getCateId());
                    }
                    //获取车主服务一级类别
                    Map<Long, ServiceCateVo> firstCateMap = serviceCateService.warpFirstCateInfo();
                    //新增类别
                    for (CzActCateRel actCate : actCateList) {
                        //老的类别没有包含新来的类别要新增该类别
                        if (!oldActCateRelIds.contains(actCate.getCateId())) {
                            //说明新增了类别
                            Long cateId = actCate.getCateId();
                            ServiceCateVo serviceCateVo = firstCateMap.get(cateId);
                            if (null != serviceCateVo) {
                                actCate.setActId(actId);//活动ID
                                actCate.setCateName(serviceCateVo.getName());//类别名称
                                czActCateRelDao.insert(actCate);
                            }
                        }
                    }
                    //删除类别
                    for (CzActCateRel actCate : oldActCateList) {
                        //新来的类别没有包含老的类别要删除该类别
                        if (!actCateRelIds.contains(actCate.getCateId())) {
                            //说明删除了该类别
                            czActCateRelDao.deleteById(actCate.getId());
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("车主app更新活动类别出错:{}", e);
            return false;
        }
        return true;
    }


    /**
     * create by jason 2015-11-12
     * 删除门店活动信息
     *
     * @param czActivity
     * @return true
     */
    public Result deleteAct(CzActivity czActivity) {

        try {
            Long actId = czActivity.getId();
            int cnt = czActivityDao.deleteById(actId);
            if (cnt > 0) {
                try {
                    //推送消息到车主端通知活动删除
                    pushMsg(Constants.CZ_ACTIVITY_DEL, czActivity);
                } catch (Exception e) {
                    log.error("门店活动删除推送消息出错:{}", e);
                }
                //删除门店活动对应的类别信息
                Map map = new HashMap(2);
                map.put("actId", actId);
                List<CzActCateRel> actCateRelList = czActCateRelDao.select(map);
                if (!CollectionUtils.isEmpty(actCateRelList)) {
                    for (CzActCateRel actCateRel : actCateRelList) {
                        czActCateRelDao.deleteById(actCateRel.getId());
                    }
                }
            } else {
                log.info("删除门店活动信息失败,参数:actId={}, shopId={}", actId, czActivity.getShopId());
                return Result.wrapErrorResult("-1", "删除门店活动信息失败");
            }
        } catch (Exception e) {
            log.error("删除门店活动信息异常:{}", e);
            return Result.wrapErrorResult("-999", "删除门店活动信息异常");
        }

        return Result.wrapSuccessfulResult(true);
    }


}
