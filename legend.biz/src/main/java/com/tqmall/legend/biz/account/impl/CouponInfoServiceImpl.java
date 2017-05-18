package com.tqmall.legend.biz.account.impl;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.account.CouponInfoService;
import com.tqmall.legend.biz.account.CouponServiceRelService;
import com.tqmall.legend.biz.account.bo.CouponCreateBo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.dao.account.AccountCouponDao;
import com.tqmall.legend.dao.account.CouponInfoDao;
import com.tqmall.legend.dao.account.CouponServiceRelDao;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.entity.account.CouponServiceRel;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.coupon.CouponInfoUseRangeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class CouponInfoServiceImpl extends BaseServiceImpl implements CouponInfoService {
    @Autowired
    private CouponInfoDao couponInfoDao;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private CouponServiceRelDao couponServiceRelDao;
    @Autowired
    private AccountCouponDao accountCouponDao;
    @Autowired
    private CouponServiceRelService couponServiceRelService;


    @Override
    public List<CouponInfo> select(Map param) {
        return couponInfoDao.select(param);
    }

    @Override
    public List<CouponInfo> findByIds(Long shopId, Long... ids) {
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        List idList = new ArrayList();
        Collections.addAll(idList, ids);
        param.put("ids", idList);
        return couponInfoDao.select(param);
    }

    @Override
    public List<CouponInfo> findByIds(Long shopId, Collection<Long> ids) {
        Map<String, Object> param = new HashMap<>();
        param.put("shopId",shopId);
        param.put("ids", ids);
        return couponInfoDao.select(param);
    }

    @Override
    public CouponInfo selectById(Long id, Long shopId) {
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("id", id);
        List<CouponInfo> couponInfos = couponInfoDao.select(param);
        if (CollectionUtils.isEmpty(couponInfos)) {
            return null;
        }
        CouponInfo couponInfo = couponInfos.get(0);
        List<CouponServiceRel> serviceRelList = couponServiceRelService.list(couponInfo.getId(), shopId);
        couponInfo.setCouponServiceList(serviceRelList);

        return couponInfo;
    }

    @Override
    public void updateBase(CouponInfo couponInfo) {
        String effectiveDateStr = couponInfo.getEffectiveDateStr();
        if (StringUtil.isNotStringEmpty(effectiveDateStr)) {
            Date effectiveDate = DateUtil.convertStringToDateYMD(effectiveDateStr);
            couponInfo.setEffectiveDate(effectiveDate);
        }
        String expireDateStr = couponInfo.getExpireDateStr();
        if (StringUtil.isNotStringEmpty(expireDateStr)) {
            Date expireDate = DateUtil.convertStringToDateYMD(expireDateStr);
            couponInfo.setExpireDate(expireDate);
        }

        couponInfoDao.updateById(couponInfo);
    }


    private void updateServiceRel(CouponCreateBo couponCreateBo) {
        Long shopId = couponCreateBo.getShopId();
        couponServiceRelService.deleteByCouponInfoId(shopId, couponCreateBo.getId());
        boolean isCustomizedRange = couponCreateBo.getUseRange() == 2;
        if (isCustomizedRange) {
            saveServiceRelList(couponCreateBo);
        }
    }


    @Override
    public List<CouponInfo> selectWithCount(Map<String, Object> param) {
        List<String> sorts = new ArrayList<>();
        sorts.add("gmt_modified DESC");
        param.put("sorts", sorts);
        List<CouponInfo> couponInfos = couponInfoDao.select(param);
        Long shopId = (Long) param.get("shopId");
        batchAttachCouponService(shopId,couponInfos);

        List<Long> ids = Lists.transform(couponInfos, new Function<CouponInfo, Long>() {
            @Override
            public Long apply(CouponInfo input) {
                return input.getId();
            }
        });

        /**
         * 计算已发放 已使用 已过期数
         */
        if (CollectionUtils.isNotEmpty(ids)) {
            param.put("infoIds", ids);
            List<AccountCoupon> accountCoupons = accountCouponDao.select(param);
            Map<Long, List<AccountCoupon>> tempMap = new HashMap<>();
            /**
             * 组装优惠券map
             */
            for (AccountCoupon accountCoupon : accountCoupons) {
                Long couponInfoId = accountCoupon.getCouponInfoId();
                List<AccountCoupon> accountCouponList = tempMap.get(couponInfoId);
                if (accountCouponList == null) {
                    accountCouponList = new ArrayList<>();
                    tempMap.put(couponInfoId, accountCouponList);
                }
                accountCouponList.add(accountCoupon);
            }
            /**
             * 计算数额
             */
            long l = System.currentTimeMillis();
            Date now = new Date(l);
            for (CouponInfo couponInfo : couponInfos) {
                Long id = couponInfo.getId();
                List<AccountCoupon> accountCouponList = tempMap.get(id);
                if (CollectionUtils.isEmpty(accountCouponList)) {
                    continue;
                }
                int usedCount = 0, grantCount = 0, expireCount = 0;
                for (AccountCoupon accountCoupon : accountCouponList) {
                    grantCount++;
                    if (accountCoupon.getUsedStatus() == 1) {
                        usedCount++;
                    } else if (accountCoupon.getExpireDate().before(now)) {
                        expireCount++;
                    }
                }
                couponInfo.setUsedCount(usedCount);
                couponInfo.setGrantCount(grantCount);
                couponInfo.setExpireCount(expireCount);
            }

        }
        /**
         * 拼接使用范围文本
         */
        for (CouponInfo couponInfo : couponInfos) {
            couponInfo.setUseRangeDescript(composeUseRangeDescript(couponInfo));
        }
        return couponInfos;
    }

    @Override
    public void batchAttachCouponService(Long shopId, List<CouponInfo> couponInfos) {
        List<Long> ids = Lists.transform(couponInfos, new Function<CouponInfo, Long>() {
            @Override
            public Long apply(CouponInfo input) {
                return input.getId();
            }
        });

        List<CouponServiceRel> couponServiceList = couponServiceRelService.list(shopId, ids);
        ImmutableMap<Long, CouponInfo> CouponInfoMap = Maps.uniqueIndex(couponInfos, new Function<CouponInfo, Long>() {
            @Override
            public Long apply(CouponInfo input) {
                return input.getId();
            }
        });

        for (CouponServiceRel item : couponServiceList) {
            CouponInfo couponInfo = CouponInfoMap.get(item.getCouponInfoId());
            if (couponInfo != null) {
                List<CouponServiceRel> couponServiceList1 = couponInfo.getCouponServiceList();
                if (couponServiceList1 == null) {
                    couponInfo.setCouponServiceList(Lists.<CouponServiceRel>newArrayList());
                }
                couponInfo.getCouponServiceList().add(item);
            }
        }
    }

    public String composeUseRangeDescript(CouponInfo couponInfo) {
        if (couponInfo.getUseRange().equals(0)) {
            return "全场通用";
        } else if (couponInfo.getUseRange().equals(1)) {
            return "全部服务";
        } else if (couponInfo.getUseRange().equals(2)) {
            List<CouponServiceRel> couponServiceList = couponInfo.getCouponServiceList();
            if (couponServiceList != null) {
                List<String> serviceNameList = Lists.transform(couponServiceList, new Function<CouponServiceRel, String>() {
                    @Override
                    public String apply(CouponServiceRel input) {
                        return input.getServiceName();
                    }
                });
                return Joiner.on(",").join(serviceNameList);
            }
            return "";
        }

        return "";
    }


    @Override
    public Integer selectCount(Map<String, Object> param) {
        return couponInfoDao.selectCount(param);
    }


    @Override
    public Map<String, CouponInfo> getMapOfCouponName(Long shopId) {
        Map<String, CouponInfo> result = Maps.newHashMap();

        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        List<CouponInfo> couponInfoList = couponInfoDao.select(param);
        if (CollectionUtils.isNotEmpty(couponInfoList)) {
            for (CouponInfo item : couponInfoList) {
                result.put(item.getCouponName(), item);
            }
        }
        return result;
    }

    @Override
    public Boolean isCouponNameRepeated(Long shopId, Long couponId, String name) {
        CouponInfo couponInfo = find(shopId, name);
        if (couponInfo == null) {
            return Boolean.FALSE;
        }
        if (couponInfo.getId().equals(couponId)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public CouponInfo find(Long shopId, String name) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("couponName", name);
        List<CouponInfo> couponInfos = couponInfoDao.select(param);
        if (CollectionUtils.isNotEmpty(couponInfos)) {
            return couponInfos.get(0);
        }
        return null;
    }

    @Override
    public void create(CouponCreateBo couponCreateBo) {
        //校验服务
        Long shopId = couponCreateBo.getShopId();
        if (isCouponNameRepeated(shopId,null,couponCreateBo.getCouponName())) {
            throw new BizException("优惠券名称重复");
        }

        //插主体
        CouponInfo couponInfo = convertFromBo2Entity(couponCreateBo);
        couponInfoDao.insert(couponInfo);

        //插服务
        boolean isCustomizedRange = couponCreateBo.getUseRange() == 2;
        if (isCustomizedRange) {
            couponCreateBo.setId(couponInfo.getId());
            saveServiceRelList(couponCreateBo);
        }

    }

    @Override
    public void update(CouponCreateBo couponUpdateBo) {
        List<Long> rawIds = couponUpdateBo.getServiceIds();
        if (rawIds != null) {
            checkInvalidService(couponUpdateBo, rawIds);
        }
        CouponInfo couponInfo = convertFromBo2Entity(couponUpdateBo);
        updateBase(couponInfo);
        updateServiceRel(couponUpdateBo);
    }

    private void checkInvalidService(CouponCreateBo couponUpdateBo, List<Long> rawIds) {Set<Long> invalidIds = shopServiceInfoService.listInvalidIds(couponUpdateBo.getShopId(), rawIds);
        if (CollectionUtils.isNotEmpty(invalidIds)) {
            Set<Integer> invalidIndexSet = Sets.newTreeSet();
            for (Long invalidId : invalidIds) {
                int index = rawIds.indexOf(invalidId);
                invalidIndexSet.add(index+1);
            }

            String message = "存在无效的服务,第"+invalidIndexSet.toString()+"行服务已失效";

            throw new BizException(message);
        }
    }

    @Override
    public void delete(Long shopId, Long couponInfoId) {
        couponServiceRelService.deleteByCouponInfoId(shopId,couponInfoId);
        deleteBase(shopId, couponInfoId);

    }

    private void deleteBase(Long shopId, Long couponInfoId) {Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("id", couponInfoId);
        couponInfoDao.delete(param);
    }

    private void checkShopServiceInfos(List<Long> serviceIds) {
            if (CollectionUtils.isEmpty(serviceIds)) {
                throw new BizException("请添加服务");
            }
    }

    private void saveServiceRelList(CouponCreateBo couponCreateBo) {
        checkShopServiceInfos(couponCreateBo.getServiceIds());
        List<CouponServiceRel> couponServices = composeShopServiceRelList(couponCreateBo);
        if (CollectionUtils.isNotEmpty(couponServices)) {
            couponServiceRelService.save(couponServices);
        }
    }

    private List<CouponServiceRel> composeShopServiceRelList(CouponCreateBo couponCreateBo) {
        List<ShopServiceInfo> shopServiceInfos = shopServiceInfoService.list(couponCreateBo.getShopId(), couponCreateBo.getServiceIds());
        List<CouponServiceRel> couponServices = Lists.newArrayList();
        for (ShopServiceInfo shopService : shopServiceInfos) {
            CouponServiceRel couponServiceRel = new CouponServiceRel();
            couponServiceRel.setShopId(couponCreateBo.getShopId());
            couponServiceRel.setCouponInfoId(couponCreateBo.getId());
            couponServiceRel.setCreator(couponCreateBo.getUserId());
            couponServiceRel.setModifier(couponCreateBo.getUserId());
            couponServiceRel.setServiceId(shopService.getId());
            couponServiceRel.setServiceName(shopService.getName());
            couponServices.add(couponServiceRel);
        }
        return couponServices;
    }

    private CouponInfo convertFromBo2Entity(CouponCreateBo couponCreateBo) {CouponInfo couponInfo = new CouponInfo();
        couponInfo.setId(couponCreateBo.getId());
        couponInfo.setShopId(couponCreateBo.getShopId());
        couponInfo.setCreator(couponCreateBo.getUserId());
        couponInfo.setModifier(couponCreateBo.getUserId());
        couponInfo.setCouponType(couponCreateBo.getCouponType());
        couponInfo.setCouponName(couponCreateBo.getCouponName());
        couponInfo.setDiscount(couponCreateBo.getDiscount());
        couponInfo.setUseRange(couponCreateBo.getUseRange());
        couponInfo.setCustomizeTime(couponCreateBo.isPeriodCustomized() ? 1 : 0);
        couponInfo.setEffectivePeriodDays(couponCreateBo.getEffectivePeriodDays());
        couponInfo.setEffectiveDate(couponCreateBo.getEffectiveDate());
        couponInfo.setExpireDate(couponCreateBo.getExpiredDate());
        couponInfo.setCompatibleWithCard(couponCreateBo.isCompatibleWithCard() ? 1 : 0);
        couponInfo.setCompatibleWithOtherAccount(couponCreateBo.isCompatibleWithOtherAccount() ? 1 : 0);
        couponInfo.setCompatibleWithOtherCoupon(couponCreateBo.isCompatibleWithOtherCoupon() ? 1 : 0);
        couponInfo.setSingleUse(couponCreateBo.isSingleUse() ? 1 : 0);
        couponInfo.setDiscountAmount(couponCreateBo.getDiscountAmount());
        couponInfo.setAmountLimit(couponCreateBo.getLimitAmount());
        couponInfo.setCouponStatus(CouponInfo.StateEnum.ENABLED.getValue());
        couponInfo.setRemark(couponCreateBo.getRemark());
        return couponInfo;
    }

    public void attachUseRange(List<CouponInfo> couponInfoList) {
        if (CollectionUtils.isEmpty(couponInfoList)) {
            return;
        }
        Long shopId = couponInfoList.get(0).getShopId();
        Set<Long> couponIds = new HashSet<>();
        for (CouponInfo couponInfo : couponInfoList) {
            int useRange = couponInfo.getUseRange() == null ? 0 : couponInfo.getUseRange();//使用范围,0:全场通用;1.只限服务工时;2.只限指定服务项目打折,默认为0
            if (useRange == CouponInfoUseRangeEnum.ZXZDFWXMDZ.getValue()) {
                couponIds.add(couponInfo.getId());
            }
        }
        if (CollectionUtils.isEmpty(couponIds)) {
            return;
        }
        List<CouponServiceRel> couponServiceRelList = couponServiceRelService.list(shopId, couponIds);
        if (CollectionUtils.isEmpty(couponServiceRelList)) {
            return;
        }
        Map<Long, List<CouponServiceRel>> serviceRelListMap = new HashMap<>();
        for (CouponServiceRel couponServiceRel : couponServiceRelList) {
            Long couponInfoId = couponServiceRel.getCouponInfoId();
            List<CouponServiceRel> serviceRelList = serviceRelListMap.get(couponInfoId);
            if (serviceRelList == null) {
                serviceRelList = new ArrayList<>();
            }
            serviceRelList.add(couponServiceRel);
            serviceRelListMap.put(couponInfoId, serviceRelList);
        }
        for (CouponInfo couponInfo : couponInfoList) {
            int useRange = couponInfo.getUseRange() == null ? 0 : couponInfo.getUseRange();//使用范围,0:全场通用;1.只限服务工时;2.只限指定服务项目打折,默认为0
            Long couponInfoId = couponInfo.getId();
            if (useRange == CouponInfoUseRangeEnum.ZXZDFWXMDZ.getValue()) {
                List<CouponServiceRel> serviceRelList = serviceRelListMap.get(couponInfoId);
                couponInfo.setCouponServiceList(serviceRelList);
            }
        }
    }

    @Override
    public List<CouponInfo> findCouponInfoByNames(Long shopId, List<String> names) {
        return couponInfoDao.findCouponInfoByNames(shopId, names);
    }
}
