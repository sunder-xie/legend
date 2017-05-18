package com.tqmall.legend.biz.service.shopnote.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.service.shopnote.LostCustomerNoteService;
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
public class LostCustomerNoteServiceImpl implements LostCustomerNoteService {

    @Resource
    private NoteConfigureService noteConfigureService;

    @Resource
    private CustomerInfoService customerInfoService;

    @Resource
    private ShopNoteInfoService noteInfoService;

    @Override
    public void processLostCustomerNoteInfo(Long shopId, Date current_time) {
        //获取流失客户的配置信息
        NoteShopConfig LostCustomerConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_LOSTCUSTOMER_CONF_TYPE);

        Date noteTimeGt = DateUtil.getDateBy(current_time, -LostCustomerConfig.getInvalidValue());
        noteTimeGt = DateUtil.addMonth(noteTimeGt, -6);
        Date noteTimeLt = DateUtil.getEndTime(DateUtil.getDateBy(current_time, LostCustomerConfig.getFirstValue()));
        noteTimeLt = DateUtil.addMonth(noteTimeLt, -6);

        // 如果左区间大于右区间
        if (noteTimeGt.after(noteTimeLt)) {
            return;
        }

        //获取需要产生通知信息的LostCustomer数据源
        List<CustomerInfo> lostCustomerInfo = getLostCustomerInfo(noteTimeGt,noteTimeLt,shopId);
        if (CollectionUtils.isEmpty(lostCustomerInfo)) {
            return;
        }
        //以customerId为维度剔除lostCustomerInfo列表中的重复项,如果有重复的以最近创建的为准
        List<CustomerInfo> distinctCustomerInfo = WrapCustomerInfo.distinctCustomerInfoByCustomerId(lostCustomerInfo);
        List<CustomerInfo> waitingCreateNoteCustomer;//等待创建提醒的数据源
        List<Long> customerIds = Lists.transform(distinctCustomerInfo, new Function<CustomerInfo, Long>() {
            @Override
            public Long apply(CustomerInfo input) {
                return input.getCustomerId();
            }
        });

        //获取这一段时间以内已经产生的Note数据
        List<NoteInfo> noteInfoList = noteInfoService.getPeriodNoteInfoListByRelIdAndType(noteTimeGt,noteTimeLt,shopId,NoteType.LOST_CUSTOMER_NOTE_TYPE,customerIds);
        waitingCreateNoteCustomer = WrapCustomerInfo.distinctCustomerInfoByNoteInfo(noteInfoList, distinctCustomerInfo, false);
        //所有等待创建的通知消息
        List<NoteInfo> waitingCreateNoteList = warpLostCustomerInfoToNoteInfo(waitingCreateNoteCustomer, current_time, LostCustomerConfig);
        if (!CollectionUtils.isEmpty(waitingCreateNoteList)) {
            noteInfoService.batchInsert(waitingCreateNoteList);
        }
    }

    /**
     * 获取需要产生通知信息的LostCustomer数据源
     * @param noteTimeGt 时间轴的左端点
     * @param noteTimeLt 时间轴的右端点
     * @param shopId 店铺ID
     * @return
     */
    private List<CustomerInfo> getLostCustomerInfo(Date noteTimeGt, Date noteTimeLt, Long shopId) {
        Map LostCustomerPara = Maps.newHashMap();
        LostCustomerPara.put("lostCustomerTimeGt", noteTimeGt);
        LostCustomerPara.put("lostCustomerTimeLt", noteTimeLt);
        LostCustomerPara.put("shopId", shopId);
        List<CustomerInfo> lostCustomerInfo = customerInfoService.selectCustomerInfoIsNote(LostCustomerPara);
        return lostCustomerInfo;
    }

    private List<NoteInfo> warpLostCustomerInfoToNoteInfo(List<CustomerInfo> waitingCreateNoteCustomer, Date current_time, NoteShopConfig shopConfig) {
        if (CollectionUtils.isEmpty(waitingCreateNoteCustomer)) {
            return Collections.emptyList();
        }
        List<NoteInfo> noteInfoList = Lists.newArrayList();
        for (CustomerInfo customerInfo : waitingCreateNoteCustomer) {
            Date lastPayTime = DateUtil.getStartTime(customerInfo.getLastPayTime());
            Date note_past_time = DateUtil.getDateBy(lastPayTime, 180 + shopConfig.getInvalidValue());
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
                info.setNoteType(NoteType.LOST_CUSTOMER_NOTE_TYPE);
                info.setNotePastTime(note_past_time);
                info.setNoteTime(lastPayTime);
                info.setRelId(customerInfo.getCustomerId());
                noteInfoList.add(info);

                // 打印log
                log.info(ShopNoteInfoLog.creatNoteLog(info.getShopId()));
            }

        }
        return noteInfoList;
    }
}
