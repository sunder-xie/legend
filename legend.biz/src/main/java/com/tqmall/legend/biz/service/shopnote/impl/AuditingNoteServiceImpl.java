package com.tqmall.legend.biz.service.shopnote.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.service.shopnote.AuditingNoteService;
import com.tqmall.legend.biz.service.shopnote.adaptor.WrapCustomerInfo;
import com.tqmall.legend.biz.shop.CustomerInfoService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.common.NoteType;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.entity.shop.NoteInfo;
import com.tqmall.legend.entity.shop.NoteShopConfig;
import com.tqmall.legend.log.ShopNoteInfoLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by yuchengdu on 16/8/4.
 */
@Slf4j
@Service
public class AuditingNoteServiceImpl implements AuditingNoteService {
    @Resource
    private NoteConfigureService noteConfigureService;
    @Resource
    private CustomerInfoService customerInfoService;
    @Resource
    private ShopNoteInfoService noteInfoService;

    @Override
    public void processAuditingNoteInfo(Long shopId, Date currentTime) {
        // 获取年检提醒的配置信息
        NoteShopConfig auditingConfig = findAuditingConfig(shopId);

        // 左区间
        Date noteAuditingTimeGt = DateUtil.getDateBy(currentTime, -auditingConfig.getInvalidValue()); // 当前时间 - 过期天数
        // 右区间
        Date noteAuditingTimeLt = DateUtil.getEndTime(DateUtil.getDateBy(currentTime, auditingConfig.getFirstValue())); // 当前时间 + 提前提醒天数

        // 如果左区间大于右区间
        if (noteAuditingTimeGt.after(noteAuditingTimeLt)) {
            return;
        }

        // 得到需要提醒的年检信息(去除已经存在的)
        List<CustomerInfo> distinctAuditingList = findDistinctAuditingList(shopId, noteAuditingTimeGt, noteAuditingTimeLt);

        // 包装提醒实体
        List<NoteInfo> insertNoteInfoList = wrapAuditingNoteInfoList(distinctAuditingList, auditingConfig.getInvalidValue(), currentTime);
        if (!CollectionUtils.isEmpty(insertNoteInfoList)) {
            // 批量保存
            noteInfoService.batchInsert(insertNoteInfoList);
        }
    }
    /**
     * 获取门店年检提醒配置
     *
     * @param shopId
     * @return
     */
    private NoteShopConfig findAuditingConfig(Long shopId) {
        return noteConfigureService.getConfigure(shopId, Constants.NOTE_AUDIT_CONF_TYPE);
    }

    /**
     * 根据年检到期时间区间获取年检记录
     *
     * @param shopId
     * @param noteAuditingTimeGt
     * @param noteAuditingTimeLt
     * @return
     */
    private List<CustomerInfo> findDistinctAuditingList(Long shopId, Date noteAuditingTimeGt, Date noteAuditingTimeLt) {
        // 查询在提醒时间区间范围内的年检
        List<CustomerInfo> auditingList = findAuditingByAuditingTime(shopId, noteAuditingTimeGt, noteAuditingTimeLt);

        // 得到车辆id集合
        List<Long> customerCarIds = Lists.transform(auditingList, new Function<CustomerInfo, Long>() {
            @Override
            public Long apply(CustomerInfo input) {
                return input.getCustomerCarId();
            }
        });

        // 查询已经存在的年检提醒
        List<NoteInfo> noteInfoList = noteInfoService.getPeriodNoteInfoListByRelIdAndType(noteAuditingTimeGt,noteAuditingTimeLt,shopId, NoteType.AUDITING_NOTE_TYPE,customerCarIds);
        // 去除已经存在的年检提醒
        return WrapCustomerInfo.distinctCustomerInfoByNoteInfo(noteInfoList, auditingList, true);
    }

    /**
     * 根据年检到期时间区间获取年检记录
     *
     * @param shopId
     * @param noteAuditingTimeGt
     * @param noteAuditingTimeLt
     * @return
     */
    private List<CustomerInfo> findAuditingByAuditingTime(Long shopId, Date noteAuditingTimeGt, Date noteAuditingTimeLt) {
        Map auditingParam = Maps.newHashMap();
        auditingParam.put("shopId", shopId);
        auditingParam.put("noteAuditingTimeGt", noteAuditingTimeGt);
        auditingParam.put("noteAuditingTimeLt", noteAuditingTimeLt);
        return customerInfoService.selectCustomerInfoIsNote(auditingParam);
    }
    /**
     * 包装需要新增的年检提醒实体List
     *
     * @param auditingList
     * @param invalidValue
     * @return
     */
    private List<NoteInfo> wrapAuditingNoteInfoList(List<CustomerInfo> auditingList, Integer invalidValue, Date currentTime) {
        if (CollectionUtils.isEmpty(auditingList)) {
            return Collections.emptyList();
        }

        List<NoteInfo> noteInfoList = Lists.newArrayList();
        for (CustomerInfo customerInfo : auditingList) {
            // 年检到期时间
            Date auditingTime = DateUtil.getStartTime(customerInfo.getNoteAuditingTime());
            Date notePastTime = DateUtil.getDateBy(auditingTime, invalidValue);
            if (notePastTime.compareTo(currentTime) > 0) {
                NoteInfo info = new NoteInfo();
                info.setShopId(customerInfo.getShopId());
                info.setRelId(customerInfo.getCustomerCarId());
                info.setCustomerId(customerInfo.getCustomerId());
                info.setCustomerCarId(customerInfo.getCustomerCarId());
                info.setNoteType(NoteType.AUDITING_NOTE_TYPE);
                info.setNotePastTime(notePastTime);
                info.setCarLicense(customerInfo.getCarLicense());
                info.setCustomerName(customerInfo.getCustomerName());
                info.setMobile(customerInfo.getMobile());
                info.setContactName(customerInfo.getContactName());
                info.setContactMobile(customerInfo.getContactMobile());
                info.setNoteTime(auditingTime);
                noteInfoList.add(info);

                // 打印log
                log.info(ShopNoteInfoLog.creatNoteLog(info.getShopId()));
            }
        }
        return noteInfoList;
    }
}
