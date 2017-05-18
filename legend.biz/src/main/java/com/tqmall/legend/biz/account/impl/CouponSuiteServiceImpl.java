package com.tqmall.legend.biz.account.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.CouponSuiteService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.account.CouponInfoDao;
import com.tqmall.legend.dao.account.CouponSuiteDao;
import com.tqmall.legend.dao.account.SuiteCouponRelDao;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.entity.account.CouponSuite;
import com.tqmall.legend.entity.account.SuiteCouponRel;
import com.tqmall.legend.entity.account.vo.CouponVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CouponSuiteServiceImpl extends BaseServiceImpl implements CouponSuiteService {
    @Autowired
    private CouponSuiteDao couponSuiteDao;
    @Autowired
    private CouponInfoDao couponInfoDao;
    @Autowired
    private SuiteCouponRelDao suiteCouponRelDao;

    @Override
    public void insert(CouponSuite couponSuite) {
        /**
         * 插入套餐记录
         */
        couponSuite.setSuiteStatus(1);
        couponSuiteDao.insert(couponSuite);
        List<CouponVo> couponVos = couponSuite.getCouponInfos();
        _insertRel(couponSuite, couponVos);
    }

    /**
     * 插入关联记录
     * @param couponSuite
     * @param couponVos
     */
    private void _insertRel(CouponSuite couponSuite, List<CouponVo> couponVos) {
        if (CollectionUtils.isNotEmpty(couponVos)) {
            /**
             * 查询优惠券套餐关联记录
             */
            List<Long> ids = new LinkedList<>();
            for (CouponVo couponVo : couponVos) {
                ids.add(couponVo.getId());
            }
            Map<String, Object> param = new HashMap<>();
            param.put("ids", ids);
            List<CouponInfo> couponInfos = couponInfoDao.select(param);
            /**
             * 组装优惠券Map
             */
            HashMap<Long, CouponInfo> tempMap = new HashMap<>();
            for (CouponInfo couponInfo : couponInfos) {
                tempMap.put(couponInfo.getId(),couponInfo);
            }
            /**
             * 插入优惠券套餐关联记录
             */
            List<SuiteCouponRel> results = new LinkedList<>();
            for (CouponVo couponVo : couponVos) {
                CouponInfo couponInfo = tempMap.get(couponVo.getId());
                if (couponInfo == null) {
                    log.error("获取优惠券信息出错,id:{}", couponVo.getId());
                    continue;
                }
                SuiteCouponRel suiteCouponRel = new SuiteCouponRel();
                suiteCouponRel.setCreator(couponSuite.getCreator());
                suiteCouponRel.setShopId(couponSuite.getShopId());
                suiteCouponRel.setSuiteId(couponSuite.getId());
                suiteCouponRel.setCouponCount(couponVo.getNum());
                suiteCouponRel.setCouponName(couponInfo.getCouponName());
                suiteCouponRel.setCouponType(couponInfo.getCouponType());
                suiteCouponRel.setCouponInfoId(couponInfo.getId());
                results.add(suiteCouponRel);
            }
            suiteCouponRelDao.batchInsert(results);
        }
    }

    @Override
    public List<CouponSuite> select(Map param) {
        return couponSuiteDao.select(param);
    }

    @Override
    public List<CouponSuite> selectDetail(Map param) {
        List<CouponSuite> couponSuites = couponSuiteDao.select(param);
        /**
         * 查询优惠券详情
         */
        List<SuiteCouponRel> suiteCouponRels = suiteCouponRelDao.select(param);
        Map<Long,List<SuiteCouponRel>> tempMap = new HashMap<>();
        for (SuiteCouponRel suiteCouponRel : suiteCouponRels) {
            Long suiteId = suiteCouponRel.getSuiteId();
            List<SuiteCouponRel> tempList = tempMap.get(suiteId);
            if(CollectionUtils.isEmpty(tempList)){
                tempList = new LinkedList<>();
                tempMap.put(suiteId,tempList);
            }
            tempList.add(suiteCouponRel);
        }
        for (CouponSuite couponSuite : couponSuites) {
            List<SuiteCouponRel> result = tempMap.get(couponSuite.getId());
            couponSuite.setSuiteCouponRels(result);
        }
        return couponSuites;
    }

    @Override
    public boolean update(CouponSuite couponSuite) {
        if(couponSuite == null){
            return false;
        }
        List<CouponVo> couponVos = couponSuite.getCouponInfos();
        if(CollectionUtils.isNotEmpty(couponVos)) {
            Map<String, Object> param = new HashMap<>();
            param.put("suiteId",couponSuite.getId());
            suiteCouponRelDao.delete(param);
            _insertRel(couponSuite, couponVos);
        }
        return couponSuiteDao.updateById(couponSuite) > 0 ? true : false;
    }

    @Override
    public CouponSuite getCouponSuiteById(Long id) {
        return couponSuiteDao.selectById(id);
    }

    @Override
    public List<SuiteCouponRel> getSuiteCouponRelList(Long shopId, Long suiteId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("suiteId",suiteId);
        return suiteCouponRelDao.select(param);
    }

    @Override
    public CouponSuite selectById(Long id, Long shopId) {
        Map<String, Object> param = new HashMap<>();
        param.put("id",id);
        param.put("shopId", shopId);
        List<CouponSuite> result = couponSuiteDao.select(param);
        if(CollectionUtils.isNotEmpty(result)) {
            return result.get(0);
        }
        return new CouponSuite();
    }

    @Override
    public List<SuiteCouponRel> getSuiteCouponRelListBySuiteIds(Long shopId, Long[] suiteId) {
        return suiteCouponRelDao.getSuiteCouponRelListBySuiteIds(shopId, suiteId);
    }

    @Override
    public void delete(Long shopId) {
        Map param = Maps.newHashMap();
        if(shopId != null){
            param.put("shopId",shopId);
        }
        couponSuiteDao.delete(param);
        suiteCouponRelDao.delete(param);
    }

    @Override
    public Integer selectCount(Map<String, Object> param) {
        return couponSuiteDao.selectCount(param);
    }

    @Override
    public void batchInsert(List<CouponSuite> couponSuiteList) {
        couponSuiteDao.batchInsert(couponSuiteList);
    }

    @Override
    public void updateUsedCount(Long shopId, Long couponSuiteId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("id",couponSuiteId);
        List<CouponSuite> suiteList = couponSuiteDao.select(param);
        if (CollectionUtils.isNotEmpty(suiteList)) {
            CouponSuite suite = suiteList.get(0);
            Integer usedCount = suite.getUsedCount();
            suite.setUsedCount(usedCount - 1);
            couponSuiteDao.updateById(suite);
        }
    }

    @Override
    public List<CouponSuite> listSuite(Long shopID, Collection<Long> suiteInfoIds) {
        return couponSuiteDao.selectByIdss(shopID, suiteInfoIds);

    }
}
