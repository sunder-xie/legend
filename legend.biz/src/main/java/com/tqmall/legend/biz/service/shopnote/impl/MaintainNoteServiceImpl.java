package com.tqmall.legend.biz.service.shopnote.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.service.shopnote.MaintainNoteService;
import com.tqmall.legend.biz.service.shopnote.adaptor.WrapCustomerInfo;
import com.tqmall.legend.biz.shop.CustomerInfoService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.common.NoteType;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.entity.shop.NoteInfo;
import com.tqmall.legend.entity.shop.NoteShopConfig;
import com.tqmall.legend.log.ShopNoteInfoLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by yuchengdu on 16/8/4.
 */
@Slf4j
@Service
public class MaintainNoteServiceImpl implements MaintainNoteService {
    @Resource
    private NoteConfigureService noteConfigureService;

    @Resource
    private CustomerInfoService customerInfoService;

    @Resource
    private ShopNoteInfoService noteInfoService;

    @Override
    public void processMaintainCustomerNoteInfo(Long shopId, Date current_time) {
        //获取保养提醒的配置信息
        NoteShopConfig maintainConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_KEEPUP_CONF_TYPE);
        List<CustomerInfo> firstWaitingCreateNoteCustomer = waitingCreateNoteCustomerList(shopId, NoteType.FIRST_MAINTAIN_NOTE_TYPE, current_time, maintainConfig);
        List<CustomerInfo> secondWaitingCreateNoteCustomer = waitingCreateNoteCustomerList(shopId, NoteType.SECOND_MAINTAIN_NOTE_TYPE, current_time, maintainConfig);
        //所有等待创建的通知消息
        List<NoteInfo> firstWaitingCreateNoteList = warpMaintainCustomerInfoToNoteInfo(firstWaitingCreateNoteCustomer, current_time, maintainConfig, NoteType.FIRST_MAINTAIN_NOTE_TYPE);
        if (!CollectionUtils.isEmpty(firstWaitingCreateNoteList)) {
            noteInfoService.batchInsert(firstWaitingCreateNoteList);
        }
        List<NoteInfo> secondWaitingCreateNoteList = warpMaintainCustomerInfoToNoteInfo(secondWaitingCreateNoteCustomer, current_time, maintainConfig, NoteType.SECOND_MAINTAIN_NOTE_TYPE);
        if (!CollectionUtils.isEmpty(secondWaitingCreateNoteList)) {
            noteInfoService.batchInsert(secondWaitingCreateNoteList);
        }
    }

    /**
     * 封装等待插入提醒消息的数据源
     *
     * @param shopId         店铺ID
     * @param noteType       提醒类型（1：首次保养提醒，8：二次保养提醒）
     * @param current_time   调度执行所在天的开始时间  eg：2016-07-30 00：00：00
     * @param maintainConfig 店铺设置的保养配置数据
     * @return
     */
    private List<CustomerInfo> waitingCreateNoteCustomerList(Long shopId, int noteType, Date current_time, NoteShopConfig maintainConfig) {
        Date noteTimeGt = DateUtil.getDateBy(current_time, -maintainConfig.getInvalidValue());
        Date noteTimeLt;
        if (noteType == NoteType.FIRST_MAINTAIN_NOTE_TYPE) {
            noteTimeLt = DateUtil.getEndTime(DateUtil.getDateBy(current_time, maintainConfig.getFirstValue()));
        } else {
            noteTimeLt = DateUtil.getEndTime(DateUtil.getDateBy(current_time, maintainConfig.getSecondValue()));
        }

        // 如果左区间大于右区间
        if (noteTimeGt.after(noteTimeLt)) {
            return Collections.emptyList();
        }

        Map maintainPara = Maps.newHashMap();
        maintainPara.put("noteKeepupTimeGt", noteTimeGt);
        maintainPara.put("noteKeepupTimeLt", noteTimeLt);
        maintainPara.put("shopId", shopId);
        List<CustomerInfo> maintainCustomerInfo = customerInfoService.selectCustomerInfoIsNote(maintainPara);
        if (CollectionUtils.isEmpty(maintainCustomerInfo)) {
            return Collections.emptyList();
        }
        List<CustomerInfo> waitingCreateNoteCustomer;//等待创建提醒的数据源
        List<Long> customerCarIds = Lists.transform(maintainCustomerInfo, new Function<CustomerInfo, Long>() {
            @Override
            public Long apply(CustomerInfo input) {
                return input.getCustomerCarId();
            }
        });

        //获取这一段时间以内已经产生的Note数据
        List<NoteInfo> noteInfoList = noteInfoService.getPeriodNoteInfoListByRelIdAndType(noteTimeGt,noteTimeLt,shopId, noteType, customerCarIds);//noteInfoService.selectLastNote(notePara);
        waitingCreateNoteCustomer = WrapCustomerInfo.distinctCustomerInfoByNoteInfo(noteInfoList, maintainCustomerInfo, true);
        //所有等待创建的通知消息
        return waitingCreateNoteCustomer;
    }

    /**
     * @param waitingCreateNoteCustomer 等待创建消息的数据源
     * @param current_time              当前时间所在天的开始时间  eg:2016-07-30 00：00：00
     * @param shopConfig                店铺提醒配置
     * @param noteType                  通知类型（1：首次提醒，8：二次提醒）
     * @return
     */
    private List<NoteInfo> warpMaintainCustomerInfoToNoteInfo(List<CustomerInfo> waitingCreateNoteCustomer, Date current_time, NoteShopConfig shopConfig, int noteType) {
        if (CollectionUtils.isEmpty(waitingCreateNoteCustomer)) {
            return Collections.emptyList();
        }
        //如果是二次提醒需要提出又首次保养提醒且未处理的数据（结合过期Job 仅需要考虑未处理数据）
        if (NoteType.SECOND_MAINTAIN_NOTE_TYPE == noteType) {
            List<Long> customerCarIdList = Lists.transform(waitingCreateNoteCustomer, new Function<CustomerInfo, Long>() {
                @Override
                public Long apply(CustomerInfo input) {
                    return input.getCustomerCarId();
                }
            });
            Map<String, Object> paraMap = Maps.newHashMap();
            paraMap.put("noteType", NoteType.FIRST_MAINTAIN_NOTE_TYPE);
            paraMap.put("customerCarIdList", customerCarIdList);
            paraMap.put("noteFlag", 0);
            List<NoteInfo> firstNoteList = noteInfoService.select(paraMap);
            if (CollectionUtils.isNotEmpty(firstNoteList)) {
                final List<Long> firstMaintaineCustomerCarIds = Lists.transform(firstNoteList, new Function<NoteInfo, Long>() {
                    @Override
                    public Long apply(NoteInfo input) {
                        return input.getCustomerCarId();
                    }
                });
                //originNoteInfoList按照车为维度移除已经产生过首次提醒信息且未处理的数据
                waitingCreateNoteCustomer = Lists.newArrayList(Collections2.filter(waitingCreateNoteCustomer, new Predicate<CustomerInfo>() {
                    @Override
                    public boolean apply(CustomerInfo input) {
                        return !firstMaintaineCustomerCarIds.contains(input.getCustomerCarId());
                    }
                }));
            }
        }

        List<NoteInfo> originNoteInfoList = Lists.newArrayList();
        for (CustomerInfo customerInfo : waitingCreateNoteCustomer) {
            //提醒时间
            Date maintainTime = DateUtil.getStartTime(customerInfo.getNoteKeepupTime());
            //提醒的过期时间
            Date note_past_time = DateUtil.getDateBy(maintainTime, shopConfig.getInvalidValue());
            if (note_past_time.compareTo(current_time) > 0) {
                NoteInfo info = new NoteInfo();
                info.setShopId(customerInfo.getShopId());
                info.setCustomerCarId(customerInfo.getCustomerCarId());
                info.setCustomerId(customerInfo.getCustomerId());
                info.setCarLicense(customerInfo.getCarLicense());
                info.setCustomerName(customerInfo.getCustomerName());
                info.setMobile(customerInfo.getMobile());
                info.setContactName(customerInfo.getContactName());
                info.setContactMobile(customerInfo.getContactMobile());
                info.setNoteType(noteType);
                info.setNotePastTime(note_past_time);
                info.setNoteTime(maintainTime);
                info.setRelId(customerInfo.getCustomerCarId());
                originNoteInfoList.add(info);

                // 打印log
                log.info(ShopNoteInfoLog.creatNoteLog(info.getShopId()));
            }
        }
        return originNoteInfoList;
    }
}
