package com.tqmall.legend.biz.service.shopnote.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.service.shopnote.BirthdayNoteService;
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
public class BirthdayNoteServiceImpl implements BirthdayNoteService {
    @Resource
    private NoteConfigureService noteConfigureService;

    @Resource
    private CustomerInfoService customerInfoService;

    @Resource
    private ShopNoteInfoService noteInfoService;

    @Override
    public void processBirthdayNoteInfo(Long shopId, Date current_time) {
        //获取生日通知的配置信息
        NoteShopConfig birthdayConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_BIRTHDY_CONF_TYPE);
        //生日提醒无需关注过期多少天失效 超过生日日期即失效 所以只需要check firstValue 所以时间轴为cur<AP<cur+firstValue
        Date NoteTimeGt = DateUtil.getDateBy(current_time, -1);
        Date NoteTimeLt = DateUtil.getEndTime(DateUtil.getDateBy(current_time, birthdayConfig.getFirstValue()));

        // 如果左区间大于右区间
        if (NoteTimeGt.after(NoteTimeLt)) {
            return;
        }

        List<CustomerInfo> birthdayCustomerInfo = getBirthdayCustomerInfo(NoteTimeGt,NoteTimeLt,shopId);
        if (CollectionUtils.isEmpty(birthdayCustomerInfo)) {
            return;
        }
        //以customerId为维度剔除birthdayCustomerInfo列表中的重复项如果有重复的以最近创建的为准
        List<CustomerInfo> distinctCustomerInfo = WrapCustomerInfo.distinctCustomerInfoByCustomerId(birthdayCustomerInfo);
        List<Long> customerIds = Lists.transform(distinctCustomerInfo, new Function<CustomerInfo, Long>() {
            @Override
            public Long apply(CustomerInfo input) {
                return input.getCustomerId();
            }
        });

        //获取对应时间段类以及产生的通知数据
        List<NoteInfo> noteInfoList=noteInfoService.getPeriodNoteInfoListByRelIdAndType(NoteTimeGt,NoteTimeLt,shopId,NoteType.BIRTHDAY_NOTE_TYPE,customerIds);
        //剔除CustomerInfoList中已经产生了消息提醒的数据
        List<CustomerInfo>  waitingCreateNoteCustomer = WrapCustomerInfo.distinctCustomerInfoByNoteInfo(noteInfoList, distinctCustomerInfo, false);
        //封装所有等待创建的生日通知的消息
        List<NoteInfo> waitingCreateNoteList = warpCustomerBirthdayInfoToNoteInfo(waitingCreateNoteCustomer, current_time);
        if (!CollectionUtils.isEmpty(waitingCreateNoteList)) {
            noteInfoService.batchInsert(waitingCreateNoteList);
        }
    }
    /**
     * 获取需要产生生日NoteInfo信息的customer数据源
     * @param NoteTimeGt 时间轴的左端点
     * @param NoteTimeLt 时间轴的右端点
     * @param shopId 店铺ID
     * @return
     */
    private List<CustomerInfo> getBirthdayCustomerInfo(Date NoteTimeGt, Date NoteTimeLt, Long shopId) {
        Map customerBirthdayPara = Maps.newHashMap();
        customerBirthdayPara.put("birthdayGt", NoteTimeGt);
        customerBirthdayPara.put("birthdayLt", NoteTimeLt);
        customerBirthdayPara.put("shopId", shopId);
        List<CustomerInfo> birthdayCustomerInfo = customerInfoService.selectCustomerInfoIsNote(customerBirthdayPara);
        return birthdayCustomerInfo;
    }

    /**
     * @param waitingCreateNoteCustomer 待创建通知的客户信息
     * @return
     */
    private List<NoteInfo> warpCustomerBirthdayInfoToNoteInfo(List<CustomerInfo> waitingCreateNoteCustomer, Date currentTime) {
        if (CollectionUtils.isEmpty(waitingCreateNoteCustomer)) {
            return Collections.emptyList();
        }
        List<NoteInfo> noteInfoList = Lists.newArrayList();
        for (CustomerInfo customerInfo : waitingCreateNoteCustomer) {
            Date birthday = DateUtil.getStartTime(customerInfo.getBirthday());
            Date notePastTime = DateUtil.getDateBy(birthday, 1);
            if (notePastTime.compareTo(currentTime) > 0) {
                NoteInfo info = new NoteInfo();
                info.setShopId(customerInfo.getShopId());
                info.setCustomerCarId(customerInfo.getCustomerCarId());
                info.setCustomerId(customerInfo.getCustomerId());
                info.setCarLicense(customerInfo.getCarLicense());
                info.setCustomerName(customerInfo.getCustomerName());
                info.setMobile(customerInfo.getMobile());
                info.setContactName(customerInfo.getContactName());
                info.setContactMobile(customerInfo.getContactMobile());
                info.setNoteType(NoteType.BIRTHDAY_NOTE_TYPE);
                info.setNotePastTime(notePastTime);
                info.setNoteTime(birthday);
                info.setRelId(customerInfo.getCustomerId());
                noteInfoList.add(info);

                // 打印log
                log.info(ShopNoteInfoLog.creatNoteLog(info.getShopId()));
            }
        }
        return noteInfoList;
    }
}
