package com.tqmall.legend.biz.service.shopnote.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.service.shopnote.InsuranceNoteService;
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
public class InsuranceNoteServiceImpl implements InsuranceNoteService {

    @Resource
    private NoteConfigureService noteConfigureService;
    @Resource
    private CustomerInfoService customerInfoService;
    @Resource
    private ShopNoteInfoService noteInfoService;

    @Override
    public void processInsuranceNoteInfo(Long shopId, Date currentTime) {
        // 获取保险提醒的配置信息
        NoteShopConfig insuranceConfig = findInsuranceConfig(shopId);

        // 左区间
        Date noteInsuranceTimeGt = DateUtil.getDateBy(currentTime, -insuranceConfig.getInvalidValue()); // 当前时间 - 过期天数
        // 右区间
        Date noteInsuranceTimeLt = DateUtil.getEndTime(DateUtil.getDateBy(currentTime, insuranceConfig.getFirstValue())); // 当前时间 + 提前提醒天数

        // 如果左区间大于右区间
        if (noteInsuranceTimeGt.after(noteInsuranceTimeLt)) {
            return;
        }

        // 得到需要提醒的保险信息(去除已经存在的)
        List<CustomerInfo> distinctInsuranceList = findDistinctInsuranceList(shopId, noteInsuranceTimeGt, noteInsuranceTimeLt);

        // 包装提醒实体
        List<NoteInfo> insertNoteInfoList = wrapInsuranceNoteInfoList(distinctInsuranceList, insuranceConfig.getInvalidValue(), currentTime);
        if (!CollectionUtils.isEmpty(insertNoteInfoList)) {
            // 批量保存
            noteInfoService.batchInsert(insertNoteInfoList);
        }
    }
    /**
     * 获取门店保险提醒配置
     *
     * @param shopId
     * @return
     */
    private NoteShopConfig findInsuranceConfig(Long shopId) {
        return noteConfigureService.getConfigure(shopId, Constants.NOTE_INSURANCE_CONF_TYPE);
    }

    /**
     * 根据保险到期时间区间获取保险记录
     *
     * @param shopId
     * @param noteInsuranceTimeGt
     * @param noteInsuranceTimeLt
     * @return
     */
    private List<CustomerInfo> findDistinctInsuranceList(Long shopId, Date noteInsuranceTimeGt, Date noteInsuranceTimeLt) {
        // 查询在提醒时间区间范围内的保险
        List<CustomerInfo> insuranceList = findInsuranceByInsuranceTime(shopId, noteInsuranceTimeGt, noteInsuranceTimeLt);

        // 得到车辆id集合
        List<Long> customerCarIds = Lists.transform(insuranceList, new Function<CustomerInfo, Long>() {
            @Override
            public Long apply(CustomerInfo input) {
                return input.getCustomerCarId();
            }
        });

        // 查询已经存在的保险提醒
        List<NoteInfo> noteInfoList = noteInfoService.getPeriodNoteInfoListByRelIdAndType(noteInsuranceTimeGt,noteInsuranceTimeLt,shopId, NoteType.INSURANCE_NOTE_TYPE,customerCarIds);
        // 去除已经存在的保险提醒
        return WrapCustomerInfo.distinctCustomerInfoByNoteInfo(noteInfoList, insuranceList, true);
    }

    /**
     * 根据保险到期时间区间获取保险记录
     *
     * @param shopId
     * @param noteInsuranceTimeGt
     * @param noteInsuranceTimeLt
     * @return
     */
    private List<CustomerInfo> findInsuranceByInsuranceTime(Long shopId, Date noteInsuranceTimeGt, Date noteInsuranceTimeLt) {
        Map insuranceParam = Maps.newHashMap();
        insuranceParam.put("shopId", shopId);
        insuranceParam.put("noteInsuranceTimeGt", noteInsuranceTimeGt);
        insuranceParam.put("noteInsuranceTimeLt", noteInsuranceTimeLt);
        return customerInfoService.selectCustomerInfoIsNote(insuranceParam);
    }

    /**
     * 包装需要新增的保险提醒实体List
     *
     * @param insuranceList
     * @param invalidValue
     * @return
     */
    private List<NoteInfo> wrapInsuranceNoteInfoList(List<CustomerInfo> insuranceList, Integer invalidValue, Date currentTime) {
        if (CollectionUtils.isEmpty(insuranceList)) {
            return Collections.emptyList();
        }

        List<NoteInfo> noteInfoList = Lists.newArrayList();
        for (CustomerInfo customerInfo : insuranceList) {
            // 保险到期时间
            Date insuranceTime = DateUtil.getStartTime(customerInfo.getNoteInsuranceTime());
            Date notePastTime = DateUtil.getDateBy(insuranceTime, invalidValue);
            if (notePastTime.compareTo(currentTime) > 0) {
                NoteInfo info = new NoteInfo();
                info.setShopId(customerInfo.getShopId());
                info.setRelId(customerInfo.getCustomerCarId());
                info.setCustomerId(customerInfo.getCustomerId());
                info.setCustomerCarId(customerInfo.getCustomerCarId());
                info.setNoteType(NoteType.INSURANCE_NOTE_TYPE);
                info.setNotePastTime(notePastTime);
                info.setCarLicense(customerInfo.getCarLicense());
                info.setCustomerName(customerInfo.getCustomerName());
                info.setMobile(customerInfo.getMobile());
                info.setContactName(customerInfo.getContactName());
                info.setContactMobile(customerInfo.getContactMobile());
                info.setNoteTime(insuranceTime);
                noteInfoList.add(info);

                // 打印log
                log.info(ShopNoteInfoLog.creatNoteLog(info.getShopId()));
            }
        }
        return noteInfoList;
    }
}
