package com.tqmall.legend.biz.account.impl;

import java.util.*;

import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.account.ComboInfoService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.dao.account.AccountComboDao;
import com.tqmall.legend.dao.account.ComboInfoDao;
import com.tqmall.legend.dao.account.ComboInfoServiceRelDao;
import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.ComboInfo;
import com.tqmall.legend.entity.account.ComboInfoServiceRel;
import com.tqmall.legend.entity.shop.ShopServiceInfo;

/**
 * Created by majian on 16/6/17.
 */
@Service
@Slf4j
public class ComboInfoServiceImpl implements ComboInfoService {

    @Autowired
    private ComboInfoDao comboInfoDao;
    @Autowired
    private ComboInfoServiceRelDao comboInfoServiceRelDao;
    @Autowired
    private AccountComboDao accountComboDao;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;

    @Override
    public Long addComboInfo(ComboInfo comboInfo, Long shopId, Long userId) throws BizException {

        if (StringUtil.isStringEmpty(comboInfo.getComboName())) {
            throw new BizException("计次卡名字不能为空");
        }

        if (checkRepetition(comboInfo, shopId)) {
            throw new BizException("新增计次卡类型失败,该计次卡名字已存在!");
        }
        if(null == comboInfo.getCustomizeTime()){
            throw new BizException("是否自定义时间不能为空");
        }else{
            if(ComboInfo.NON_CUSTOM_TIME == comboInfo.getCustomizeTime()){
                Long effectivePeriodDays = comboInfo.getEffectivePeriodDays();
                if (effectivePeriodDays == null || effectivePeriodDays.compareTo(0l) == 0) {
                    throw new BizException("有效期不能为空或为0");
                }
            }else if(ComboInfo.CUSTOMIZED_TIME == comboInfo.getCustomizeTime()){
                Optional<String> effectiveDateStr = Optional.fromNullable(comboInfo.getEffectiveDateStr());
                Optional<String> expireDateStr = Optional.fromNullable(comboInfo.getExpireDateStr());
                if(!effectiveDateStr.isPresent()||!expireDateStr.isPresent()){
                    throw new BizException("自定义失效时间或生效时间不能为空");
                }else {
                    Date effectiveDate = DateUtil.convertStringToDate(DateUtil.convertToBeginStr(effectiveDateStr.get()));
                    Date expireDate = DateUtil.convertStringToDate(DateUtil.convertToEndStr(expireDateStr.get()));
                    comboInfo.setEffectiveDate(effectiveDate);
                    comboInfo.setExpireDate(expireDate);
                }
            }
        }


        List<ComboInfoServiceRel> content = comboInfo.getContent();

        if (content == null) {
            throw new BizException("请加入服务项目");
        }

        for (ComboInfoServiceRel item : content) {
            Integer count = item.getServiceCount();
            if (count == null || count == 0 ) {
                throw new BizException("请填写服务项目数量");
            }
        }

        List<Long> serviceIds = Lists.transform(content, new Function<ComboInfoServiceRel, Long>() {
            @Override
            public Long apply(ComboInfoServiceRel input) {
                return input.getServiceId();
            }
        });
        List<ShopServiceInfo> serviceList = shopServiceInfoService.list(shopId, serviceIds);
        if (CollectionUtils.isEmpty(serviceList)) {
            throw new BizException("请选择服务");
        }
        Map<Long,String> serviceMap = Maps.newHashMap();
        for (ShopServiceInfo item : serviceList) {
            serviceMap.put(item.getId(),item.getName());
        }

        for (ComboInfoServiceRel item : content) {
            Long serviceId = item.getServiceId();
            String serviceName = serviceMap.get(serviceId);
            item.setServiceName(serviceName);
        }


        /**
         * 1.先插入计次卡基本信息
         */
        comboInfo.setCreator(userId);
        comboInfo.setModifier(userId);
        comboInfo.setShopId(shopId);
        comboInfo.setComboStatus(ComboInfo.ENABLED);
        comboInfoDao.insert(comboInfo);

        /**
         * 2.插入计次卡关联服务信息
         */
        Long comboInfoId = comboInfo.getId();
        List<ComboInfoServiceRel> comboInfoContent = content;
        if (!CollectionUtils.isEmpty(comboInfoContent)) {
            for (ComboInfoServiceRel item : comboInfoContent) {
                item.setCreator(userId);
                item.setModifier(userId);
                item.setShopId(shopId);
                item.setComboInfoId(comboInfoId);
            }
        }
        comboInfoServiceRelDao.batchInsert(comboInfoContent);

        return comboInfoId;

    }

    private boolean checkRepetition(ComboInfo comboInfo, Long shopId) {
        List<ComboInfo> comboInfoList = this.listComboInfo(shopId);
        if (!CollectionUtils.isEmpty(comboInfoList)) {
            for (ComboInfo item : comboInfoList) {
                boolean isIdEqual = false;
                if ((comboInfo.getId() != null) && (item.getId().compareTo(comboInfo.getId()) == 0)) {
                    isIdEqual = true;
                }
                boolean isNameEqual = item.getComboName().equals(comboInfo.getComboName());
                if (!isIdEqual && isNameEqual) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void updateComboInfo(ComboInfo comboInfo, Long shopId, Long userId) {
        if (this.isComboINfoGranted(comboInfo.getId())) {
            throw  new BizException("该计次卡已发放,不能修改");
        }
        if (checkRepetition(comboInfo, shopId)) {
            throw  new BizException("编辑计次卡类型失败,该计次卡名字已存在!");
        }
        if(null == comboInfo.getCustomizeTime()){
            throw new BizException("是否自定义时间不能为空");
        }else{
            if(ComboInfo.NON_CUSTOM_TIME == comboInfo.getCustomizeTime()){
                Long effectivePeriodDays = comboInfo.getEffectivePeriodDays();
                if (effectivePeriodDays == null || effectivePeriodDays.compareTo(0l) == 0) {
                    throw new BizException("有效期不能为空或为0");
                }
            }else if(ComboInfo.CUSTOMIZED_TIME == comboInfo.getCustomizeTime()){
                Optional<String> effectiveDateStr = Optional.fromNullable(comboInfo.getEffectiveDateStr());
                Optional<String> expireDateStr = Optional.fromNullable(comboInfo.getExpireDateStr());
                if(!effectiveDateStr.isPresent()||!expireDateStr.isPresent()){
                    throw new BizException("自定义失效时间或生效时间不能为空");
                }else {
                    Date effectiveDate = DateUtil.convertStringToDate(DateUtil.convertToBeginStr(effectiveDateStr.get()));
                    Date expireDate = DateUtil.convertStringToDate(DateUtil.convertToEndStr(expireDateStr.get()));
                    comboInfo.setEffectiveDate(effectiveDate);
                    comboInfo.setExpireDate(expireDate);
                }
            }
        }

        comboInfo.setModifier(userId);
        comboInfoDao.updateById(comboInfo);

        Map deleteParam = Maps.newHashMap();
        deleteParam.put("comboInfoId", comboInfo.getId());
        comboInfoServiceRelDao.delete(deleteParam);
        List<ComboInfoServiceRel> comboInfoContent = comboInfo.getContent();
        if (!CollectionUtils.isEmpty(comboInfoContent)) {
            for (ComboInfoServiceRel item : comboInfoContent) {
                item.setCreator(userId);
                item.setModifier(userId);
                item.setShopId(shopId);
                item.setComboInfoId(comboInfo.getId());
            }
        }
        comboInfoServiceRelDao.batchInsert(comboInfoContent);
    }

    @Override
    public ComboInfo getComboInfo(Long id, Long shopId) {
        Map param = Maps.newHashMap();
        param.put("id", id);
        param.put("shopId", shopId);
        List<ComboInfo> comboInfoList = comboInfoDao.select(param);
        ComboInfo comboInfo = null;
        if (!CollectionUtils.isEmpty(comboInfoList)) {
            comboInfo = comboInfoList.get(0);
        }

        List<ComboInfoServiceRel> content = comboInfoServiceRelDao.selectByComboInfoId(id);
        if (comboInfo != null) {
            comboInfo.setContent(content);
        }
        return comboInfo;
    }


    @Override
    public List<ComboInfo> listComboInfo(Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("sorts", new String[]{"gmt_modified desc", "gmt_create desc"});

        List<ComboInfo> comboInfoList = comboInfoDao.select(param);
        if (!CollectionUtils.isEmpty(comboInfoList)) {
            for (ComboInfo comboInfo : comboInfoList) {
                HashMap<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("shopId", comboInfo.getShopId());
                paramMap.put("comboInfoId", comboInfo.getId());
            }
        }

        /**
         * 内存中组装会员卡信息
         */
        List<CommonPair<Long, Integer>> usedCardTypeList = accountComboDao.findUsedCardType(shopId);
        Map<Long, CommonPair<Long, Integer>> usedCardTypeMap = Maps.uniqueIndex(usedCardTypeList, new Function<CommonPair<Long, Integer>, Long>() {
            @Override
            public Long apply(CommonPair<Long, Integer> input) {
                return input.getDataF();
            }
        });
        /**
         * 内存中组装关联服务、关联物料信息
         */
        for (ComboInfo comboInfo : comboInfoList) {
            if (usedCardTypeMap.containsKey(comboInfo.getId())) {
                comboInfo.setGrantedCount(usedCardTypeMap.get(comboInfo.getId()).getDataS());
            } else {
                comboInfo.setGrantedCount(0);
            }
        }


        setContentOfComboInfoList(comboInfoList);
        return comboInfoList;
    }

    @Override
    public List<ComboInfo> listEnabledComboInfo(Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("comboStatus",1);
        param.put("sorts", new String[]{"gmt_modified desc", "gmt_create desc"});

        List<ComboInfo> comboInfoList = comboInfoDao.select(param);

        setContentOfComboInfoList(comboInfoList);
        return comboInfoList;
    }


    @Override
    public void enableComboInfo(Long id, UserInfo userInfo) {
        ComboInfo comboInfo = findById(userInfo.getShopId(), id);
        if (comboInfo.getComboStatus().equals(ComboInfo.DISABLED)) {
            comboInfo.setModifier(userInfo.getUserId());
            comboInfo.setComboStatus(ComboInfo.ENABLED);
            comboInfoDao.updateById(comboInfo);
        } else {
            throw new BizException("改计次卡类型已启用,不能再次启用");
        }
    }

    @Override
    public void disableComboInfo(Long id, UserInfo userInfo) {
        ComboInfo comboInfo = findById(userInfo.getShopId(), id);

        if (comboInfo.getComboStatus().equals(ComboInfo.ENABLED)) {
            comboInfo.setModifier(userInfo.getUserId());
            comboInfo.setComboStatus(ComboInfo.DISABLED);
            comboInfoDao.updateById(comboInfo);
        } else {
            throw new BizException("改计次卡类型已停用,不能再次停用用");
        }
    }

    @Override
    public void deleteComboInfo(Long id, UserInfo userInfo) {

        comboInfoDao.deleteById(id);
        Map param = Maps.newHashMap();
        param.put("shopId",userInfo.getShopId());
        param.put("comboInfoId", id);
        comboInfoServiceRelDao.delete(param);

    }

    @Override
    public List<ComboInfo> findComboInfoByIds(Long shopId, Long... comboIds) {

        List<ComboInfo> comboInfoList = comboInfoDao.selectByIds(comboIds);
        setContentOfComboInfoList(comboInfoList);
        return comboInfoList;
    }

    @Override
    public Integer selectCount(Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        return comboInfoDao.selectCount(param);
    }

    @Override
    public ComboInfo findById(Long shopId, Long id) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("id", id);
        List<ComboInfo> comboInfoList = comboInfoDao.select(param);
        if (CollectionUtils.isEmpty(comboInfoList)) {
            throw new BizException("找不到改计次卡类型,shopId="+shopId+",id="+id);
        } else if (comboInfoList.size() > 1){
            throw new BizException("找到多个计次卡类型,shopId="+shopId+",id="+id);
        }
        return comboInfoList.get(0);
    }

    @Override
    public boolean isComboINfoGranted(Long id) {
        Map param = Maps.newHashMap();
        param.put("comboInfoId", id);
        List<AccountCombo> comboList = accountComboDao.select(param);
        int count = 0;
        if (!CollectionUtils.isEmpty(comboList)) {
            count = comboList.size();
        }
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Map<String, ComboInfoServiceRel>> getComboInfoMap(Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        List<ComboInfo> comboInfoList = comboInfoDao.select(param);
        List<ComboInfoServiceRel> serviceRelList = comboInfoServiceRelDao.select(param);

        Map<String, Map<String, ComboInfoServiceRel>> result = Maps.newHashMap();
        if (CollectionUtils.isEmpty(comboInfoList) || CollectionUtils.isEmpty(serviceRelList)) {
            return result;
        }
        Map<Long, String> comboInfoMap = Maps.newHashMap();
        for (ComboInfo item : comboInfoList) {
            comboInfoMap.put(item.getId(), item.getComboName());
        }

        for (ComboInfoServiceRel item :serviceRelList) {
            Long comboInfoId = item.getComboInfoId();
            String comboName = comboInfoMap.get(comboInfoId);
            if (result.containsKey(comboName)) {
                result.get(comboName).put(item.getServiceName(),item);
            } else {
                Map<String, ComboInfoServiceRel> serviceRelMap = Maps.newHashMap();
                serviceRelMap.put(item.getServiceName(),item);
                result.put(comboName,serviceRelMap);
            }
        }


        return result;
    }

    private void setContentOfComboInfoList(List<ComboInfo> comboInfoList) {
        if (!CollectionUtils.isEmpty(comboInfoList)) {
            List<Long> comboInfoIds = Lists.newArrayList();
            Map<Long,List<ComboInfoServiceRel>> comboInfoServiceRelMap = Maps.newHashMap();
            for (ComboInfo comboInfo : comboInfoList) {
                comboInfoIds.add(comboInfo.getId());
                List<ComboInfoServiceRel> comboInfoServiceRels = Lists.newArrayList();
                comboInfoServiceRelMap.put(comboInfo.getId(),comboInfoServiceRels);
            }
            List<ComboInfoServiceRel> comboInfoServiceRels = comboInfoServiceRelDao.selectByComboInfoIds(comboInfoIds.toArray(new Long[comboInfoIds.size()]));
            for(ComboInfoServiceRel comboInfoServiceRel : comboInfoServiceRels){
                List<ComboInfoServiceRel> comboInfoServiceRel1 = comboInfoServiceRelMap.get(comboInfoServiceRel.getComboInfoId());
                comboInfoServiceRel1.add(comboInfoServiceRel);
            }
            for (ComboInfo comboInfo : comboInfoList){
                comboInfo.setContent(comboInfoServiceRelMap.get(comboInfo.getId()));
            }
        }
    }


    @Override
    public List<ComboInfo> findComboInfoByNames(Long shopId, List<String> comboNames) {
        return comboInfoDao.findComboInfoByNames(shopId,comboNames);
    }

    @Override
    public List<ComboInfo> list(Long shopId) {
        Assert.notNull(shopId);
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        return comboInfoDao.select(param);
    }

    @Override
    public List<ComboInfo> listByIds(List<Long> ids) {
        if (Langs.isEmpty(ids)) {
            return Collections.emptyList();
        }
        Long[] idArr = new Long[ids.size()];
        return comboInfoDao.selectByIds(ids.toArray(idArr));
    }
}
