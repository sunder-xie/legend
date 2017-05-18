package com.tqmall.legend.biz.service.shopnote.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.customer.CustomerFeedbackService;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.service.shopnote.NextVisitNoteService;
import com.tqmall.legend.biz.service.shopnote.adaptor.WarpCustomerFeedback;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.common.NoteType;
import com.tqmall.legend.entity.customer.CustomerFeedback;
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
public class NextVisitNoteServiceImpl implements NextVisitNoteService {
    @Resource
    private NoteConfigureService noteConfigureService;

    @Resource
    private ShopNoteInfoService noteInfoService;

    @Resource
    private CustomerFeedbackService customerFeedbackService;

    @Override
    public void processNextVisitCustomerNoteInfo(Long shopId, Date current_time) {
        //获取店铺回访的配置信息
        NoteShopConfig nextVisitConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_VISIT_CONF_TYPE);
        Date noteTimeGt = DateUtil.getDateBy(current_time, -nextVisitConfig.getInvalidValue());
        Date noteTimeLt = DateUtil.getEndTime(DateUtil.getDateBy(current_time, 3));

        // 如果左区间大于右区间
        if (noteTimeGt.after(noteTimeLt)) {
            return;
        }

        List<CustomerFeedback> customerFeedbackList = customerFeedbackService.selectLastFeedback(shopId, noteTimeGt, noteTimeLt);
        if (CollectionUtils.isEmpty(customerFeedbackList)) {
            return;
        }

        List<CustomerFeedback> waitingCreateNoteCustomerFeedBack;//等待创建提醒的数据源
        List<Long> customerFeedbackIds = Lists.transform(customerFeedbackList, new Function<CustomerFeedback, Long>() {
            @Override
            public Long apply(CustomerFeedback input) {
                return input.getId();
            }
        });

        //获取这一段时间以内已经产生的Note数据
        List<NoteInfo> noteInfoList = noteInfoService.getPeriodNoteInfoListByRelIdAndType(noteTimeGt,noteTimeLt,shopId,NoteType.NEXT_VISIT_NOTE_TYPE,customerFeedbackIds);
        waitingCreateNoteCustomerFeedBack = WarpCustomerFeedback.distinctCustomerFeedbackByNoteInfo(noteInfoList, customerFeedbackList);
        waitingCreateNoteCustomerFeedBack = deleteExistNoteInfo(shopId, waitingCreateNoteCustomerFeedBack);
        //所有等待创建的通知消息
        List<NoteInfo> waitingCreateNoteList = warpCustomerFeedbackToNoteInfo(waitingCreateNoteCustomerFeedBack, current_time, nextVisitConfig);
        if (!CollectionUtils.isEmpty(waitingCreateNoteList)) {
            noteInfoService.batchInsert(waitingCreateNoteList);
        }
    }

    /**
     * 删除同一辆车的工单回访提醒和下次回访提醒
     * @param shopId
     * @param customerFeedbackList
     * @return
     */
    private List<CustomerFeedback> deleteExistNoteInfo(Long shopId, List<CustomerFeedback> customerFeedbackList) {
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("shopId", shopId);
        searchParam.put("noteTypeList", Lists.newArrayList(NoteType.VISIT_NOTE_TYPE, NoteType.NEXT_VISIT_NOTE_TYPE));
        searchParam.put("noteFlag", 0);
        List<NoteInfo> noteInfoList = noteInfoService.select(searchParam);
        if (!CollectionUtils.isEmpty(noteInfoList)) {
            List<Long> deleteNoteInfoIds = Lists.newArrayList();
            ListIterator<CustomerFeedback> iterator = customerFeedbackList.listIterator();
            while (iterator.hasNext()) {
                CustomerFeedback customerFeedback = iterator.next();
                for (NoteInfo noteInfo : noteInfoList) {
                    if (customerFeedback.getCustomerCarId() != null && customerFeedback.getCustomerCarId().equals(noteInfo.getCustomerCarId())) {
                        Date visitTime = DateUtil.getStartTime(customerFeedback.getNextVisitTime());
                        if (visitTime.compareTo(noteInfo.getNoteTime()) > 0) {
                            deleteNoteInfoIds.add(noteInfo.getId());
                        } else {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            noteInfoService.deleteByIds(deleteNoteInfoIds);
        }
        return customerFeedbackList;
    }

    /**
     * @param waitingCreateNoteCustomerFeedBack
     * @param current_time
     * @param shopConfig
     * @return
     */
    private List<NoteInfo> warpCustomerFeedbackToNoteInfo(List<CustomerFeedback> waitingCreateNoteCustomerFeedBack, Date current_time, NoteShopConfig shopConfig) {
        if (CollectionUtils.isEmpty(waitingCreateNoteCustomerFeedBack)) {
            return Collections.emptyList();
        }
        List<NoteInfo> noteInfoList = Lists.newArrayList();
        for (CustomerFeedback customerFeedback : waitingCreateNoteCustomerFeedBack) {
            Date visitTime = DateUtil.getStartTime(customerFeedback.getNextVisitTime());
            Date note_past_time = DateUtil.getDateBy(visitTime, shopConfig.getInvalidValue());
            if (note_past_time.compareTo(current_time) > 0) {
                NoteInfo info = new NoteInfo();
                info.setShopId(customerFeedback.getShopId());
                info.setCustomerCarId(customerFeedback.getCustomerCarId());
                info.setCustomerId(customerFeedback.getCustomerId());
                info.setCarLicense(customerFeedback.getCarLicense());
                info.setCustomerName(customerFeedback.getCustomerName());
                info.setMobile(customerFeedback.getMobile());
                info.setContactName(customerFeedback.getCustomerName());
                info.setContactMobile(customerFeedback.getMobile());
                info.setNoteType(NoteType.NEXT_VISIT_NOTE_TYPE);
                info.setNotePastTime(note_past_time);
                info.setNoteTime(visitTime);
                info.setRelId(customerFeedback.getId());
                noteInfoList.add(info);

                // 打印log
                log.info(ShopNoteInfoLog.creatNoteLog(info.getShopId()));
            }

        }
        return noteInfoList;
    }
}
