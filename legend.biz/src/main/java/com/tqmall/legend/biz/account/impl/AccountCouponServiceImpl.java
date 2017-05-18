package com.tqmall.legend.biz.account.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.dandelion.wechat.client.wechat.coupon.WeChatUserCouponLogService;
import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.account.*;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.dao.account.AccountCouponDao;
import com.tqmall.legend.dao.account.CouponInfoDao;
import com.tqmall.legend.dao.account.CouponSuiteDao;
import com.tqmall.legend.dao.account.SuiteCouponRelDao;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.account.vo.AccountCouponVo;
import com.tqmall.legend.entity.account.vo.CouponVo;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.enums.coupon.AccountCouponSourceEnum;
import com.tqmall.legend.facade.account.CouponFacadeService;
import com.tqmall.legend.facade.wechat.vo.WechatAccountCouponVo;
import com.tqmall.legend.log.MarketCardCouponLog;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

@Slf4j
@Service
public class AccountCouponServiceImpl extends BaseServiceImpl implements AccountCouponService {
    @Autowired
    private AccountCouponDao accountCouponDao;

    @Autowired
    private SuiteCouponRelDao suiteCouponRelDao;
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private CouponInfoDao couponInfoDao;
    @Autowired
    private CouponFacadeService couponFacadeService;
    @Autowired
    private AccountCouponFlowDetailService accountCouponFlowDetailService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private CouponSuiteDao couponSuiteDao;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private WeChatUserCouponLogService weChatUserCouponLogService;

    @Override
    public Integer getAccountCouponCountOfUnUsed(Long shopId, Long accountId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountId", accountId);
        param.put("usedStatus", 0);
        return accountCouponDao.selectCount(param);
    }

    @Override
    public boolean isExist(Long shopId, Long accountId) {
        return accountCouponDao.isExist(shopId, accountId) != null ? true : false;
    }

    @Override
    public AccountTradeFlow grant(AccountCouponVo accountCouponVo) {
        Long shopId = accountCouponVo.getShopId();
        /**
         * 处理套餐逻辑
         */
        Integer couponSource = 0;
        Long couponSuiteId = accountCouponVo.getCouponSuiteId();
        if (couponSuiteId != null) {
            /**
             * 如果套餐ID不为0,根据ID查询优惠券信息后存入
             */
            Map<String, Object> param = new HashMap<>();
            param.put("shopId", shopId);
            param.put("suiteId", couponSuiteId);
            List<SuiteCouponRel> couponRels = suiteCouponRelDao.select(param);
            if (CollectionUtils.isNotEmpty(couponRels)) {
                List<CouponVo> couponVos = new ArrayList<>();
                for (SuiteCouponRel couponRel : couponRels) {
                    CouponVo couponVo = new CouponVo();
                    couponVo.setId(couponRel.getCouponInfoId());
                    couponVo.setNum(couponRel.getCouponCount());
                    couponVos.add(couponVo);
                }
                accountCouponVo.setCouponVos(couponVos);
            }
            /**
             * 增加发放充值数
             */
            CouponSuite couponSuite = couponSuiteDao.selectById(couponSuiteId);
            Integer usedCount = couponSuite.getUsedCount();
            if(usedCount == null){
                usedCount = 0;
            }
            couponSuite.setUsedCount(usedCount+1);
            couponSuiteDao.updateById(couponSuite);
        } else {
            couponSource = 1;
        }
        return this._grantCoupon(accountCouponVo, couponSource);
    }

    @Override
    public AccountCoupon grant(AccountCouponVo accountCouponVo, AccountCouponSourceEnum couponSource) {
        Assert.notNull(couponSource,"领券来源不能为空");
        AccountTradeFlow accountTradeFlow = this._grantCoupon(accountCouponVo, couponSource.getValue());
        Long flowId = accountTradeFlow.getId();
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put("shopId",accountTradeFlow.getShopId());
        searchParams.put("flowId",flowId);
        List<AccountCoupon> accountCouponList = this.select(searchParams);
        if(CollectionUtils.isEmpty(accountCouponList)){
            log.error("[领取优惠券]根据flowId:{}查不到优惠券实例",flowId);
            throw new BizException("获取优惠券失败");
        }
        return accountCouponList.get(0);
    }

    /**
     * 领券:
     * 1.记账户交易流水;
     * 2.新增账户优惠券实例;
     * 3.记账户优惠券交易流水;
     * @param accountCouponVo
     * @param couponSource
     * @return
     */
    private AccountTradeFlow _grantCoupon(AccountCouponVo accountCouponVo, Integer couponSource) {
        Long accountId = accountCouponVo.getAccountId();
        if (accountId == null) {
            throw new BizException("无账户信息");
        }
        Long shopId = accountCouponVo.getShopId();
        Long creator = accountCouponVo.getCreator();
        ShopManager shopManager = null;
        if(creator!=null){
            shopManager = shopManagerService.selectById(creator);
        } else{
            creator =0L;// 表示系统操作
        }
        Long couponSuiteId = accountCouponVo.getCouponSuiteId();
        /**
         * 查询优惠券详情
         */
        Map<String, Object> param = new HashMap<>();
        List<CouponVo> couponVos = accountCouponVo.getCouponVos();
        List<Long> ids = new LinkedList<>();
        for (CouponVo couponVo : couponVos) {
            ids.add(couponVo.getId());
        }
        if (CollectionUtils.isEmpty(ids)) {
            throw new BizException("无优惠券");
        }
        param.put("ids", ids);
        List<CouponInfo> couponInfos = couponInfoDao.select(param);
        Map<Long, CouponInfo> couponInfoMap = new HashMap<>();
        for (CouponInfo couponInfo : couponInfos) {
            couponInfoMap.put(couponInfo.getId(), couponInfo);
        }
        AccountTradeFlow accountTradeFlow = accountTradeFlowService.recordTradeFlowForCouponCharge(accountCouponVo, shopId, creator, accountId);
        /**
         * 处理优惠券逻辑
         */
        if (CollectionUtils.isNotEmpty(couponVos)) {
            List<AccountCoupon> result = new ArrayList<>();
            for (CouponVo couponVo : couponVos) {
                Long id = couponVo.getId();
                CouponInfo couponInfo = couponInfoMap.get(id);
                if (Integer.valueOf(0).equals(couponInfo.getCustomizeTime())) {//非自定义时间
                    Integer effectivePeriodDays = couponInfo.getEffectivePeriodDays();
                    /**
                     * 处理过期时间逻辑
                     */

                    if (effectivePeriodDays != null) {
                        couponInfo.setEffectiveDate(new Date());
                        Calendar c = Calendar.getInstance();
                        c.setTime(couponInfo.getEffectiveDate());
                        c.add(c.DATE, effectivePeriodDays);
                        couponInfo.setExpireDate(c.getTime());
                    }
                }

                Integer num = couponVo.getNum();
                for (Integer i = 0; i < num; i++) {
                    /**
                     * 组装实体
                     */
                    AccountCoupon accountCoupon = new AccountCoupon();
                    accountCoupon.setAccountId(accountId);
                    accountCoupon.setShopId(shopId);
                    accountCoupon.setCreator(creator);
                    accountCoupon.setCouponSource(couponSource);
                    String couponCode = couponFacadeService.genCouponSN();
                    accountCoupon.setCouponCode(couponCode);
                    accountCoupon.setUsedStatus(0);
                    accountCoupon.setCouponInfoId(id);
                    accountCoupon.setCouponType(couponInfo.getCouponType());
                    accountCoupon.setCouponName(couponInfo.getCouponName());
                    accountCoupon.setEffectiveDate(couponInfo.getEffectiveDate());
                    accountCoupon.setExpireDate(couponInfo.getExpireDate());
                    accountCoupon.setSuiteId(couponSuiteId);
                    if(shopManager == null) {
                        accountCoupon.setOperatorName("");
                    } else {
                        accountCoupon.setOperatorName(shopManager.getName());
                    }
                    accountCoupon.setFlowId(accountTradeFlow.getId());
                    accountCoupon.setFlowSn(accountTradeFlow.getFlowSn());
                    accountCouponDao.insert(accountCoupon);
                    result.add(accountCoupon);
                    // 记录log
                    log.info(MarketCardCouponLog.grantCouponLog(shopId));
                }
            }
            for (AccountCoupon accountCoupon : result) {
                this.couponFacadeService.removeCachedSn(accountCoupon.getCouponCode());
            }
            /**
             * 插入流水
             */
            accountTradeFlowService.recordFlowForCouponCharge(accountCouponVo,accountTradeFlow, shopId, creator, result);
        }
        return accountTradeFlow;
    }


    @Override
    public List<AccountCoupon> getAccountCoupon(Long shopId, Long accountId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountId", accountId);
        return this._findAccountCoupon(param);
    }

    @Override
    public List<AccountCoupon> getAccountCouponListByGroup(Long shopId, Long accountId){
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountId", accountId);
        List<AccountCoupon> accountCouponList = this._findAccountCoupon(param);
        List<AccountCoupon> result = Lists.newArrayList();
        Map<String,AccountCoupon> accountCouponMap = Maps.newHashMap();
        for (AccountCoupon accountCoupon : accountCouponList) {
            Long flowId = accountCoupon.getFlowId();
            String couponName = accountCoupon.getCouponName();
            String key = couponName+flowId;
            if(!accountCouponMap.containsKey(key)){
                if(accountCoupon.getUsedStatus() == 0){
                    accountCoupon.setCouponNum(1);
                    accountCouponMap.put(key, accountCoupon);
                }
            }else {
                AccountCoupon account_coupon = accountCouponMap.get(key);
                if(account_coupon != null && accountCoupon.getUsedStatus() == 0){
                    account_coupon.setCouponCode(account_coupon.getCouponCode()+" "+accountCoupon.getCouponCode());
                    account_coupon.setCouponNum(account_coupon.getCouponNum()+1);
                }
            }
        }

        for (Map.Entry<String, AccountCoupon> accountCouponEntry : accountCouponMap.entrySet()) {
            result.add(accountCouponEntry.getValue());
        }
        return result;
    }

    @Override
    public List<AccountCoupon> findAvailableAccountCoupon(Long shopId, Long accountId) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountId", accountId);
        param.put("usedStatus", 0);//未使用
        Date date = new Date();
        param.put("gtExpireDate", date);//未过期
        param.put("ltEffectiveDate", date);//已生效
        return this._findAccountCoupon(param);
    }

    @Override
    public List<AccountCoupon> findAvailableAccountCouponByAccountIds(Long shopId, List<Long> accountIds) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountIds", accountIds);
        param.put("usedStatus", 0);//未使用
        Date date = new Date();
        param.put("gtExpireDate", date);//未过期
        param.put("ltEffectiveDate", date);//已生效
        return this._findAccountCoupon(param);
    }

    @Override
    public List<AccountCoupon> findAllAccountCoupon(Long shopId, Long accountId) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountId", accountId);
        param.put("usedStatus", 0);//未使用
        Date date = new Date();
        param.put("gtExpireDate", date);//未过期
        return this._findAccountCoupon(param);
    }

    @Override
    public List<AccountCoupon> findAllAccountCouponByAccountIds(Long shopId, List<Long> accountIds) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountIds", accountIds);
        param.put("usedStatus", 0);//未使用
        Date date = new Date();
        param.put("gtExpireDate", date);//未过期
        return this._findAccountCoupon(param);
    }

    private List<AccountCoupon> _findAccountCoupon(Map<String,Object> param) {
        Long shopId = (Long) param.get("shopId");

        List<AccountCoupon> accountCoupons = accountCouponDao.select(param);
        if (CollectionUtils.isEmpty(accountCoupons)) {
            return accountCoupons;
        }
        List<Long> ids = new LinkedList<>();
        List<Long> suiteIds = new LinkedList<>();
        for (AccountCoupon accountCoupon : accountCoupons) {
            ids.add(accountCoupon.getCouponInfoId());
            Long suiteId = accountCoupon.getSuiteId();
            if(suiteId != null) {
                suiteIds.add(suiteId);
            }
        }
        /**
         * 组装优惠券类型信息
         */
        if (!CollectionUtils.isEmpty(ids)) {
            Map<Long, CouponInfo> couponInfoMap = new HashMap<>();
            List<CouponInfo> couponInfoList = couponInfoService.findByIds(shopId, ids);
            couponInfoService.batchAttachCouponService(shopId,couponInfoList);
            for (CouponInfo info : couponInfoList) {
                couponInfoMap.put(info.getId(), info);
            }
            for (AccountCoupon accountCoupon : accountCoupons) {
                CouponInfo couponInfo = couponInfoMap.get(accountCoupon.getCouponInfoId());
                if (couponInfo == null) {
                    couponInfo = new CouponInfo();
                    log.error("查询优惠券错误,优惠券类型已被删除,shopId:{},accountCouponId:{}", accountCoupon.getShopId(), accountCoupon.getId());
                }
                accountCoupon.setCouponInfo(couponInfo);
            }
        }
        /**
         * 组装套餐信息
         */
        if (!CollectionUtils.isEmpty(suiteIds)) {
            Map<Long, CouponSuite> couponSuiteMap = new HashMap<>();
            Map<String,Object> suiteParam = new HashMap<>();
            suiteParam.put("ids", suiteIds);
            List<CouponSuite> couponSuites = couponSuiteDao.select(suiteParam);
            for (CouponSuite couponSuite : couponSuites) {
                couponSuiteMap.put(couponSuite.getId(),couponSuite);
            }
            for (AccountCoupon accountCoupon : accountCoupons) {
                Long suiteId = accountCoupon.getSuiteId();
                if(suiteId != null && suiteId != 0){
                    CouponSuite couponSuite = couponSuiteMap.get(suiteId);
                    if(couponSuite != null){
                        accountCoupon.setSuiteName(couponSuite.getSuiteName());
                    }
                }else {
                    accountCoupon.setSuiteName("赠送");//账户详情中优惠券来源
                }
            }
        }
        return accountCoupons;
    }

    @Override
    public List<AccountCoupon> findByCouponSn(Long shopId, String... couponSn) {
        if (couponSn == null || couponSn.length == 0) {
            return Lists.newArrayList();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        List<String> couponSns = new ArrayList<>();
        Collections.addAll(couponSns, couponSn);
        param.put("couponCodes", couponSns);
        return accountCouponDao.select(param);
    }

    @Override
    public void batchInsert(List<AccountCoupon> accountCouponList) {
        super.batchInsert(accountCouponDao,accountCouponList,1000);
    }

    @Override
    public Boolean consume(Long shopId, Long userId, List<Long> ids, Map<Long, Long> flowIdMap) {
        if (CollectionUtils.isNotEmpty(ids)) {
            Map<String, Object> param = new HashMap<>();
            param.put("ids", ids);
            param.put("shopId", shopId);
            List<String> couponCodes = new ArrayList<>();
            List<AccountCoupon> accountCoupons = accountCouponDao.select(param);
            if (Langs.isEmpty(accountCoupons)) {
                return false;
            }
            for (AccountCoupon accountCoupon : accountCoupons) {
                accountCoupon.setUsedStatus(1);
                accountCouponDao.updateById(accountCoupon);
                if(accountCoupon.getCouponSource()!=null &&
                        accountCoupon.getCouponSource().intValue()==AccountCouponSourceEnum.SHOP_WECHAT.getValue()){//通过门店微信公众号领取的优惠券使用时需要通知微信端
                    couponCodes.add(accountCoupon.getCouponCode());
                }
            }
            accountCouponFlowDetailService.recordDetailForConsume(shopId, userId, flowIdMap, accountCoupons);
            if(!CollectionUtils.isEmpty(couponCodes)){
                try {
                    Date useDate = new Date();
                    com.tqmall.core.common.entity.Result<String> result = weChatUserCouponLogService.updateBycouponCode(couponCodes,1,useDate);
                    log.info("[dubbo]通过门店微信公众号领取的优惠券使用时需要通知微信端,卡券代码:{},使用时间:{},success:{}",
                            LogUtils.objectToString(couponCodes),LogUtils.objectToString(useDate),result.isSuccess());
                } catch (Exception e){
                    log.error("[dubbo]通过门店微信公众号领取的优惠券(优惠券代码:"+LogUtils.objectToString(couponCodes)+")使用时需要通知微信端出现异常:{}",e);
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public AccountCoupon findById(Long id, Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("id", id);
        List<AccountCoupon> coupons = accountCouponDao.select(param);
        if (CollectionUtils.isNotEmpty(coupons)) {
            return coupons.get(0);
        }
        return null;
    }


    @Override
    public boolean update(AccountCoupon accountCoupon) {
        return accountCouponDao.updateById(accountCoupon) > 0 ? true : false;
    }

    @Override
    public void delete(Long shopId) {
        Map param = Maps.newHashMap();
        if (shopId != null) {
            param.put("shopId", shopId);
        }
        accountCouponDao.delete(param);
    }

    @Override
    public void reverseConsume(Long userId, Long shopId, Long reverseFlowId, AccountCouponFlowDetail item) {
        Long couponId = item.getCouponId();
        AccountCoupon accountCoupon = accountCouponDao.selectById(couponId);
        accountCoupon.setModifier(userId);
        accountCoupon.setGmtModified(new Date());
        accountCoupon.setUsedStatus(0);
        accountCouponDao.updateById(accountCoupon);
        accountCouponFlowDetailService.recordRevertDetailForConsume(userId,shopId,reverseFlowId,item);
        if(accountCoupon.getCouponSource()!=null &&
                accountCoupon.getCouponSource().intValue()==AccountCouponSourceEnum.SHOP_WECHAT.getValue()){//通过门店微信公众号领取的优惠券会退时需要通知微信端
            List<String> couponCodes = new ArrayList<>();
            try {
                Date useDate = new Date();
                couponCodes.add(accountCoupon.getCouponCode());
                com.tqmall.core.common.entity.Result<String> result = weChatUserCouponLogService.updateBycouponCode(couponCodes,0,useDate);
                log.info("[dubbo]通过门店微信公众号领取的优惠券会退时需要通知微信端,卡券代码:{},回退时间:{},success:{}",
                        LogUtils.objectToString(couponCodes),LogUtils.objectToString(useDate),result.isSuccess());
            } catch (Exception e){
                log.error("[dubbo]通过门店微信公众号领取的优惠券(优惠券代码:"+LogUtils.objectToString(couponCodes)+")回退时需要通知微信端出现异常:{}",e);
            }
        }
    }

    @Override
    public List<AccountCoupon> findByFlowId(Long shopId, Long flowId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("flowId", flowId);
        List<AccountCoupon> couponList = accountCouponDao.select(param);
        return couponList;
    }
    @Override
    public List<AccountCoupon> select(Map param){
        return accountCouponDao.select(param);
    }

    @Override
    public Integer deleteByIds(List<Long> ids) {
        return accountCouponDao.deleteByIds(ids.toArray());
    }

    @Override
    public List<String> selectExistsSn(String[] snList) {
        return this.accountCouponDao.selectExistsSn(snList);
    }

    @Override
    public List<WechatAccountCouponVo> qryAccountCoupon(Map<String, Object> searchParams) {
        List<AccountCoupon> accountCouponList = this._findAccountCoupon(searchParams);
        List<WechatAccountCouponVo> wechatAccountCouponVoList = new ArrayList<>();
        List<Long> accountIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(accountCouponList)){
            for(AccountCoupon accountCoupon:accountCouponList){
                accountIds.add(accountCoupon.getAccountId());
                WechatAccountCouponVo wechatAccountCouponVo = new WechatAccountCouponVo();
                BeanUtils.copyProperties(accountCoupon,wechatAccountCouponVo);
                wechatAccountCouponVoList.add(wechatAccountCouponVo);
            }
        }
        Map<Long,String> accountIdMobileMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(accountIds)){
            List<AccountInfo> accountInfoList = accountInfoService.getInfoByIds(accountIds);
            if(!CollectionUtils.isEmpty(accountInfoList)){
                for(AccountInfo accountInfo:accountInfoList){
                    accountIdMobileMap.put(accountInfo.getId(),accountInfo.getMobile());
                }
            }
        }
        if(!CollectionUtils.isEmpty(wechatAccountCouponVoList)){
            for(WechatAccountCouponVo wechatAccountCouponVo:wechatAccountCouponVoList){
                wechatAccountCouponVo.setMobile(accountIdMobileMap.get(wechatAccountCouponVo.getAccountId()));
            }
        }
        return wechatAccountCouponVoList;
    }

    @Override
    public Integer selectCount(Map param) {
        return accountCouponDao.selectCount(param);
    }

    @Override
    public void insert(AccountCoupon coupon) {
        accountCouponDao.insert(coupon);
    }

    @Override
    public Page<AccountCoupon> getAccountCouponByPage(Pageable pageable, Map<String, Object> params) {
        List<AccountCoupon> accountCoupons = accountCouponDao.getCountByGroupFlowIdAndCouponInfoId(params);

        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            List<String> sorts = Lists.newArrayList();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            params.put("sorts", sorts);
        }

        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1
                : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1
                : pageable.getPageSize(), pageable.getSort());
        params.put("offset", pageRequest.getOffset());
        params.put("limit", pageRequest.getPageSize());
        List<AccountCoupon> accountCouponList = accountCouponDao.getListByGroupFlowIdAndCouponInfoId(params);
        DefaultPage<AccountCoupon> page = new DefaultPage<>(accountCouponList, pageRequest, accountCoupons.size());
        return page;
    }

    @Override
    public boolean hasOwnedCouponByType(Long shopId, Long accountId, Long couponTypeId) {
        return this.accountCouponDao.countOwnedCouponByTypeId(shopId, accountId, couponTypeId).compareTo(Integer.valueOf(0)) > 0;
    }

    @Override
    public List<AccountCoupon> listFreeCoupon(Long shopID, Long accountId) {
        List<AccountCoupon> couponList = this.listCoupon(shopID, accountId);

        List<AccountCoupon> freeCouponList = Lists.newArrayList();
        for (AccountCoupon coupon : couponList) {

            if (isCouponFree(coupon)) {
                freeCouponList.add(coupon);
            }
        }
        return freeCouponList;
    }

    private boolean isCouponFree(AccountCoupon coupon) {
        Long suiteId = coupon.getSuiteId();
        return suiteId == null || suiteId == 0 || coupon.getFlowId() == 0;
    }

    private List<AccountCoupon> listCoupon(Long shopID, Long accountId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopID);
        param.put("accountId", accountId);
        return accountCouponDao.select(param);
    }

    @Override
    public List<AccountCoupon> listBundleCoupon(Long shopID, Long accountId) {
        List<AccountCoupon> couponList = this.listCoupon(shopID, accountId);

        List<AccountCoupon> bundleCouponList = Lists.newArrayList();
        for (AccountCoupon coupon : couponList) {
            if (!isCouponFree(coupon)) {
                bundleCouponList.add(coupon);
            }
        }
        return bundleCouponList;
    }

    @Override
    public List<AccountCoupon> findAccountCouponByFlowIds(Long shopId, List<Long> flowIds) {
        if (Langs.isEmpty(flowIds)) {
            return Collections.emptyList();
        }
        return accountCouponDao.findAccountCouponByFlowIds(shopId, flowIds);
    }

    /**
     * 批量查询账户下未过期的优惠券数量
     *
     * @param shopId
     * @param accountIds
     * @return
     */
    @Override
    public Map<Long, Long> getUnExpireCouponNum(Long shopId, Collection<Long> accountIds) {
        Assert.isTrue(shopId != null && shopId > 0);
        if (Langs.isEmpty(accountIds)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> unExpireCouponNumMap = Maps.newHashMap();
        List<CommonPair<Long, Long>> unExpireCouponNumList = accountCouponDao.getUnExpireCouponNum(shopId, accountIds);
        if (Langs.isNotEmpty(unExpireCouponNumList)) {
            for (CommonPair<Long, Long> pair : unExpireCouponNumList) {
                unExpireCouponNumMap.put(pair.getDataF(), pair.getDataS());
            }
        }
        return unExpireCouponNumMap;
    }

    /**
     * 批量查询账户下未过期的优惠券
     *
     * @param shopId
     * @param accountIds
     * @return
     */
    @Override
    public List<AccountCoupon> getUnExpireCouponList(Long shopId, Collection<Long> accountIds) {
        Assert.isTrue(shopId != null && shopId > 0);
        if (Langs.isEmpty(accountIds)) {
            return Collections.emptyList();
        }
        return accountCouponDao.getUnExpireCouponList(shopId, accountIds);
    }

    @Override
    public List<AccountCoupon> listByIds(Collection<Long> ids) {
        if (Langs.isEmpty(ids)) {
            return Collections.emptyList();
        }
        Long[] idArr = new Long[ids.size()];
        return accountCouponDao.selectByIds(ids.toArray(idArr));
    }


}
