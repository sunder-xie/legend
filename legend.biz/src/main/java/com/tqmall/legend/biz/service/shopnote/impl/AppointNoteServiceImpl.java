package com.tqmall.legend.biz.service.shopnote.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.service.shopnote.AppointNoteService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.common.NoteType;
import com.tqmall.legend.entity.customer.Appoint;
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
public class AppointNoteServiceImpl implements AppointNoteService {

    @Resource
    private NoteConfigureService noteConfigureService;
    @Resource
    private AppointService appointService;
    @Resource
    private ShopNoteInfoService noteInfoService;

    @Override
    public void processAppointNoteInfo(Long shopId, Date currentTime) {
        // 获取预约单提醒的配置信息
        NoteShopConfig appointConfig = findAppointConfig(shopId);

        // 左区间
        Date appointTimeGt = DateUtil.getDateBy(currentTime, -appointConfig.getInvalidValue()); // 当前时间 - 过期天数
        // 右区间
        Date appointTimeLt = DateUtil.getEndTime(DateUtil.getDateBy(currentTime, appointConfig.getFirstValue())); // 当前时间 + 提前提醒天数

        // 如果左区间大于右区间
        if (appointTimeGt.after(appointTimeLt)) {
            return;
        }

        // 得到需要提醒的预约单(去除已经存在的)
        List<Appoint> distinctAppointList = findDistinctAppointList(shopId, appointTimeGt, appointTimeLt);

        // 包装提醒实体
        List<NoteInfo> insertNoteInfoList = wrapAppointNoteInfoList(distinctAppointList, appointConfig.getInvalidValue(), currentTime);
        if (!CollectionUtils.isEmpty(insertNoteInfoList)) {
            // 批量保存
            noteInfoService.batchInsert(insertNoteInfoList);
        }
    }

    /**
     * 获取门店预约单提醒配置
     *
     * @param shopId
     * @return
     */
    private NoteShopConfig findAppointConfig(Long shopId) {
        return noteConfigureService.getConfigure(shopId, Constants.NOTE_APPOINT_CONF_TYPE);
    }

    /**
     * 查询需要提醒的预约单
     *
     * @return
     */
    private List<Appoint> findDistinctAppointList(Long shopId, Date appointTimeGt, Date appointTimeLt) {
        // 查询在提醒时间区间范围内的预约单
        List<Appoint> appointList = findAppointByAppointTime(shopId, appointTimeGt, appointTimeLt);
        if (CollectionUtils.isEmpty(appointList)) {
            return Collections.emptyList();
        }

        // 得到预约单id集合
        List<Long> appointIds = Lists.transform(appointList, new Function<Appoint, Long>() {
            @Override
            public Long apply(Appoint input) {
                return input.getId();
            }
        });

        // 查询已经存在的预约单提醒
        List<NoteInfo> noteInfoList = noteInfoService.getPeriodNoteInfoListByRelIdAndType(appointTimeGt, appointTimeLt, shopId, NoteType.APPOINT_NOTE_TYPE,appointIds);

        // 去除已经存在的预约单提醒
        appointList = distinctAppointByNoteInfo(appointList, noteInfoList);

        return deleteExistNoteInfo(shopId, appointList);
    }

    /**
     * 根据预约时间区间获取预约记录
     *
     * @param shopId
     * @param appointTimeGt
     * @param appointTimeLt
     * @return
     */
    private List<Appoint> findAppointByAppointTime(Long shopId, Date appointTimeGt, Date appointTimeLt) {
        Map appointParam = Maps.newHashMap();
        appointParam.put("shopId", shopId);
        appointParam.put("appointTimeGt", appointTimeGt);
        appointParam.put("appointTimeLt", appointTimeLt);
        return appointService.select(appointParam);
    }
    /**
     * 去除已经存在的预约单提醒
     *
     * @param appointList
     * @param noteInfoList
     * @return
     */
    private List<Appoint> distinctAppointByNoteInfo(List<Appoint> appointList, List<NoteInfo> noteInfoList) {
        Multimap<Long, Appoint> sourceAppointMapList = Multimaps.index(appointList, new Function<Appoint, Long>() {
            @Override
            public Long apply(Appoint input) {
                return input.getCustomerCarId();
            }
        });
        List<Appoint> destAppointList = Lists.newArrayList();
        for (Long mapKey : sourceAppointMapList.keySet()) {
            Collection<Appoint> AppointCollection = sourceAppointMapList.get(mapKey);
            Appoint lastOrderInfo = Collections.max(AppointCollection, new Comparator<Appoint>() {
                @Override
                public int compare(Appoint o1, Appoint o2) {
                    return o1.getAppointTime().compareTo(o2.getAppointTime());
                }
            });
            destAppointList.add(lastOrderInfo);
        }

        if (!CollectionUtils.isEmpty(noteInfoList)) {
            final List<Long> existAppointIds = Lists.transform(noteInfoList, new Function<NoteInfo, Long>() {
                @Override
                public Long apply(NoteInfo input) {
                    return input.getRelId();
                }
            });
            return Lists.newArrayList(Collections2.filter(destAppointList, new Predicate<Appoint>() {
                @Override
                public boolean apply(Appoint input) {
                    return !existAppointIds.contains(input.getId());
                }
            }));
        }
        return destAppointList;
    }

    /**
     * 删除同一辆车的工单回访提醒和下次回访提醒
     * @param shopId
     * @param appointList
     * @return
     */
    private List<Appoint> deleteExistNoteInfo(Long shopId, List<Appoint> appointList) {
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("shopId", shopId);
        searchParam.put("noteType", NoteType.APPOINT_NOTE_TYPE);
        searchParam.put("noteFlag", 0);
        List<NoteInfo> noteInfoList = noteInfoService.select(searchParam);
        if (!CollectionUtils.isEmpty(noteInfoList)) {
            List<Long> deleteNoteInfoIds = Lists.newArrayList();
            ListIterator<Appoint> iterator = appointList.listIterator();
            while (iterator.hasNext()) {
                Appoint appoint = iterator.next();
                for (NoteInfo noteInfo : noteInfoList) {
                    if (appoint.getCustomerCarId() != null && appoint.getCustomerCarId().equals(noteInfo.getCustomerCarId())) {
                        Date appointTime = DateUtil.getStartTime(appoint.getAppointTime());
                        if (appointTime.compareTo(noteInfo.getNoteTime()) > 0) {
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
        return appointList;
    }

    /**
     * 包装需要新增的预约单提醒实体List
     *
     * @param appointList
     * @param invalidValue
     * @return
     */
    private List<NoteInfo> wrapAppointNoteInfoList(List<Appoint> appointList, Integer invalidValue, Date currrentTime) {
        if (CollectionUtils.isEmpty(appointList)) {
            return Collections.emptyList();
        }

        List<NoteInfo> noteInfoList = Lists.newArrayList();
        for (Appoint appoint : appointList) {
            // 预约时间
            Date appointTime = DateUtil.getStartTime(appoint.getAppointTime());
            Date notePastTime = DateUtil.getDateBy(appointTime, invalidValue);
            if (notePastTime.compareTo(currrentTime) > 0) {
                NoteInfo info = new NoteInfo();
                info.setShopId(appoint.getShopId());
                info.setRelId(appoint.getId());
                info.setRelType(0);
                info.setCustomerCarId(appoint.getCustomerCarId());
                info.setNoteType(NoteType.APPOINT_NOTE_TYPE);
                info.setNotePastTime(notePastTime);
                info.setCarLicense(appoint.getLicense());
                info.setCustomerName(appoint.getCustomerName());
                info.setMobile(appoint.getMobile());
                info.setNoteTime(appointTime);
                noteInfoList.add(info);

                // 打印log
                log.info(ShopNoteInfoLog.creatNoteLog(info.getShopId()));
            }
        }
        return noteInfoList;
    }
}
