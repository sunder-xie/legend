package com.tqmall.legend.biz.service.shopnote.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.service.shopnote.VisitNoteService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.common.NoteType;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderStatusEnum;
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
public class VisitNoteServiceImpl implements VisitNoteService {

    @Resource
    private NoteConfigureService noteConfigureService;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private ShopNoteInfoService noteInfoService;

    @Override
    public void processVisitNoteInfo(Long shopId, Date currentTime) {
        // 获取回访提醒的配置信息
        NoteShopConfig visitConfig = findVisitConfig(shopId);

        // 左区间
        Date noteVisitTimeGt = DateUtil.getDateBy(currentTime, -(visitConfig.getFirstValue() + visitConfig.getInvalidValue())); // 当前时间 - 工单完成后天数 - 过期天数
        // 右区间
        Date noteVisitTimeLt = DateUtil.getEndTime(DateUtil.getDateBy(currentTime, -visitConfig.getFirstValue())); // 当前时间 - 工单完成后天数

        // 如果左区间大于右区间
        if (noteVisitTimeGt.after(noteVisitTimeLt)) {
            return;
        }

        // 得到需要提醒的回访信息(去除已经存在的)
        List<OrderInfo> distinctVisitList = findDistinctVisitList(shopId, noteVisitTimeGt, noteVisitTimeLt, visitConfig.getFirstValue());

        // 包装提醒实体
        List<NoteInfo> insertNoteInfoList = wrapVisitNoteInfoList(distinctVisitList, visitConfig, currentTime);
        if (!CollectionUtils.isEmpty(insertNoteInfoList)) {
            // 批量保存
            noteInfoService.batchInsert(insertNoteInfoList);
        }
    }
    /**
     * 获取门店回访提醒配置
     *
     * @param shopId
     * @return
     */
    private NoteShopConfig findVisitConfig(Long shopId) {
        return noteConfigureService.getConfigure(shopId, Constants.NOTE_VISIT_CONF_TYPE);
    }

    /**
     * 根据回访到期时间区间获取回访记录
     *
     * @param shopId
     * @param noteVisitTimeGt
     * @param noteVisitTimeLt
     * @return
     */
    private List<OrderInfo> findDistinctVisitList(Long shopId, Date noteVisitTimeGt, Date noteVisitTimeLt, Integer firstValue) {
        // 查询在提醒时间区间范围内的回访工单(根据确认账单时间)
        List<OrderInfo> visitOrderList = orderInfoService.findUnVisitList(shopId, noteVisitTimeGt, noteVisitTimeLt);
        if (CollectionUtils.isEmpty(visitOrderList)) {
            return Collections.emptyList();
        }

        // 得到工单id集合
        List<Long> orderIds = Lists.transform(visitOrderList, new Function<OrderInfo, Long>() {
            @Override
            public Long apply(OrderInfo input) {
                return input.getId();
            }
        });

        // 查询已经存在的回访提醒
//        Date noteTimeGt = DateUtil.getDateBy(noteVisitTimeGt, firstValue);
//        Date noteTimeLt = DateUtil.getDateBy(noteVisitTimeLt, firstValue);
        List<NoteInfo> noteInfoList = noteInfoService.getPeriodNoteInfoListByRelIdAndType(null,null,shopId, NoteType.VISIT_NOTE_TYPE,orderIds);
        // 去除已经存在的回访提醒
        List<OrderInfo> orderInfos = distinctVisitByNoteInfo(visitOrderList, noteInfoList);

        return deleteExistNoteInfo(shopId, orderInfos, firstValue);
    }

    /**
     * 去除已经存在的预约单提醒
     *
     * @param visitOrderList
     * @param noteInfoList
     * @return
     */
    private List<OrderInfo> distinctVisitByNoteInfo(List<OrderInfo> visitOrderList, List<NoteInfo> noteInfoList) {
        if (!CollectionUtils.isEmpty(noteInfoList)) {
            final List<Long> existOrderIds = Lists.transform(noteInfoList, new Function<NoteInfo, Long>() {
                @Override
                public Long apply(NoteInfo input) {
                    return input.getRelId();
                }
            });
            return Lists.newArrayList(Collections2.filter(visitOrderList, new Predicate<OrderInfo>() {
                @Override
                public boolean apply(OrderInfo input) {
                    return !existOrderIds.contains(input.getId());
                }
            }));
        }
        return visitOrderList;
    }

    /**
     * 删除同一辆车的工单回访提醒和下次回访提醒
     * @param shopId
     * @param visitOrderList
     * @param firstValue
     * @return
     */
    private List<OrderInfo> deleteExistNoteInfo(Long shopId, List<OrderInfo> visitOrderList, Integer firstValue) {
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("shopId", shopId);
        List<Integer> noteTypeList = Lists.newArrayList(NoteType.VISIT_NOTE_TYPE, NoteType.NEXT_VISIT_NOTE_TYPE);
        searchParam.put("noteTypeList", noteTypeList);
        searchParam.put("noteFlag", 0);
        List<NoteInfo> noteInfoList = noteInfoService.select(searchParam);
        if (!CollectionUtils.isEmpty(noteInfoList)) {
            List<Long> deleteNoteInfoIds = Lists.newArrayList();
            ListIterator<OrderInfo> iterator = visitOrderList.listIterator();
            while (iterator.hasNext()) {
                OrderInfo orderInfo = iterator.next();
                for (NoteInfo noteInfo : noteInfoList) {
                    if (orderInfo.getCustomerCarId() != null && orderInfo.getCustomerCarId().equals(noteInfo.getCustomerCarId())) {
                        Date confirmTime = DateUtil.getStartTime(orderInfo.getConfirmTime());
                        Date noteTime = DateUtil.getDateBy(confirmTime, firstValue);
                        if (noteTime.compareTo(noteInfo.getNoteTime()) > 0) {
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
        return visitOrderList;
    }


    /**
     * 包装需要新增的预约单提醒实体List
     *
     * @param orderInfoList
     * @param visitConfig
     * @return
     */
    private List<NoteInfo> wrapVisitNoteInfoList(List<OrderInfo> orderInfoList, NoteShopConfig visitConfig, Date currentTime) {
        if (CollectionUtils.isEmpty(orderInfoList)) {
            return Collections.emptyList();
        }
        List<NoteInfo> noteInfoList = Lists.newArrayList();
        for (OrderInfo orderInfo : orderInfoList) {
            // 确认账单时间
            Date confirmTime = DateUtil.getStartTime(orderInfo.getConfirmTime());
            Date noteTime = DateUtil.getDateBy(confirmTime, visitConfig.getFirstValue());
            Date notePastTime = DateUtil.getDateBy(noteTime, visitConfig.getInvalidValue());
            if (notePastTime.compareTo(currentTime) > 0) {
                NoteInfo info = new NoteInfo();
                info.setShopId(orderInfo.getShopId());
                info.setRelId(orderInfo.getId());
                info.setRelType(1);
                info.setCustomerId(orderInfo.getCustomerId());
                info.setCustomerCarId(orderInfo.getCustomerCarId());
                info.setNoteType(NoteType.VISIT_NOTE_TYPE);
                info.setNotePastTime(notePastTime);
                info.setCarLicense(orderInfo.getCarLicense());
                info.setCustomerName(orderInfo.getCustomerName());
                info.setMobile(orderInfo.getCustomerMobile());
                info.setContactName(orderInfo.getContactName());
                info.setContactMobile(orderInfo.getContactMobile());
                info.setNoteTime(noteTime);
                noteInfoList.add(info);

                // 打印log
                log.info(ShopNoteInfoLog.creatNoteLog(info.getShopId()));
            }
        }
        return noteInfoList;
    }
}
