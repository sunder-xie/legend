package com.tqmall.legend.biz.shop.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.RpcCustomerInfoService;
import com.tqmall.cube.shop.result.CustomerInfoDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.service.shopnote.AppointNoteService;
import com.tqmall.legend.biz.service.shopnote.AuditingNoteService;
import com.tqmall.legend.biz.service.shopnote.BirthdayNoteService;
import com.tqmall.legend.biz.service.shopnote.InsuranceNoteService;
import com.tqmall.legend.biz.service.shopnote.LostCustomerNoteService;
import com.tqmall.legend.biz.service.shopnote.MaintainNoteService;
import com.tqmall.legend.biz.service.shopnote.NextVisitNoteService;
import com.tqmall.legend.biz.service.shopnote.VisitNoteService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.common.NoteType;
import com.tqmall.legend.dao.marketing.ng.CustomerInfoDao;
import com.tqmall.legend.dao.shop.NoteInfoDao;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.entity.shop.NoteInfo;
import com.tqmall.legend.entity.shop.NoteInfoVO;
import com.tqmall.legend.log.ShopNoteInfoLog;
import com.tqmall.legend.pojo.shopnote.ShopNoteInfoVO;
import com.tqmall.legend.pojo.shopnote.ShopNoteTypeNum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by twg on 16/3/20.
 */
@Service
@Slf4j
public class ShopNoteInfoServiceImpl implements ShopNoteInfoService {
    @Autowired
    private NoteInfoDao noteInfoDao;
    @Autowired
    private CustomerInfoDao customerInfoDao;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private RpcCustomerInfoService rpcCustomerInfoService;
    @Autowired
    private AppointNoteService appointNoteService;
    @Autowired
    private InsuranceNoteService insuranceNoteService;
    @Autowired
    private AuditingNoteService auditingNoteService;
    @Autowired
    private VisitNoteService visitNoteService;
    @Autowired
    private BirthdayNoteService birthdayNoteService;
    @Autowired
    private LostCustomerNoteService lostCustomerNoteService;
    @Autowired
    private MaintainNoteService maintainNoteService;
    @Autowired
    private NextVisitNoteService nextVisitNoteService;

    @Override
    public List<NoteInfo> getNoteInfoList(Long shopId, Integer noteType) {
        Map param = Maps.newConcurrentMap();
        param.put("shopId",shopId);
        param.put("noteType",noteType);
        param.put("noteFlag",0);
        List<NoteInfo> noteInfoList = noteInfoDao.select(param);
        return noteInfoList;
    }

    @Override
    public Integer getNoteInfoCount(Long shopId, Integer noteType){
        Map param = Maps.newConcurrentMap();
        param.put("shopId",shopId);
        param.put("noteType",noteType);
        param.put("noteFlag", 0);
        return noteInfoDao.selectCount(param);
    }

    @Override
    public Page<CustomerInfo> getCustomerInfo(Map<String, Object> params, Pageable pageable) {
        Integer totalSize = customerInfoDao.getCountOfCustomerInfo(params);
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        params.put("limit", pageRequest.getPageSize());
        params.put("offset", pageRequest.getOffset());

        List<CustomerInfo> data = customerInfoDao.getCustomerInfoList(params);
        DefaultPage<CustomerInfo> page = new DefaultPage<CustomerInfo>(data, pageRequest, totalSize);
        return page;
    }


    @Override
    public Page<NoteInfo> getPage(Map<String, Object> params, Pageable pageable) {
        if(pageable.getSort() != null){
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            params.put("sorts", sorts);
        }
        Integer totalSize = noteInfoDao.selectCount(params);
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        params.put("limit", pageRequest.getPageSize());
        params.put("offset", pageRequest.getOffset());

        List<NoteInfo> data = noteInfoDao.select(params);
        DefaultPage<NoteInfo> page = new DefaultPage<NoteInfo>(data, pageRequest, totalSize);
        return page;
    }

    @Override
    public Integer getShopNoteInfoCount(Map<String, Object> params) {
        return noteInfoDao.selectCount(params);
    }

    @Override
    public List<NoteInfo> select(Map<String, Object> params) {
        return noteInfoDao.select(params);
    }

    @Override
    public int updateById(NoteInfo info) {
        return noteInfoDao.updateById(info);
    }

    @Override
    public List<NoteInfo> selectLastNote(Map param){
        return  noteInfoDao.selectLastNote(param);
    }
    @Override
    public Integer batchInsert(List<NoteInfo> t){
        if (CollectionUtils.isEmpty(t)) {
            return 0;
        }
        return noteInfoDao.batchInsert(t);
    }

    /**
     * 批量处理提醒
     *
     * @param shopId       门店id
     * @param noteInfoIds  提醒id集合
     * @param noteWay      提醒方式
     * @param operator     操作人
     * @return
     */
    @Override
    public int batchHandleNoteInfo(Long shopId, List<Long> noteInfoIds, int noteWay, String operator) {
        if (!CollectionUtils.isEmpty(noteInfoIds)) {
            Long[] idArray = new Long[noteInfoIds.size()];
            List<NoteInfo> noteInfos = noteInfoDao.selectByIds(noteInfoIds.toArray(idArray));
            if (!CollectionUtils.isEmpty(noteInfos)) {
                for (NoteInfo noteInfo : noteInfos) {
                    if (noteInfo.getNoteType().equals(NoteType.VISIT_NOTE_TYPE)) {
                        Long orderId = noteInfo.getRelId();
                        if (orderId != null && orderId > 0) {
                            // 设置orderInfo已回访
                            orderInfoService.updateOrderVisit(shopId, orderId);
                        }
                    }
                    // 打印日志
                    log.info(ShopNoteInfoLog.handleNoteLog(shopId));
                }
            }
            return noteInfoDao.batchHandleNoteInfo(shopId, noteInfoIds, noteWay, operator);
        }
        return 0;
    }

    @Override
    public List<NoteInfo> getPeriodNoteInfoListByRelIdAndType(Date noteTimeGt, Date noteTimeLt, Long shopId, int noteType, List<Long> relIds) {
        if (noteTimeGt != null && noteTimeLt != null) {
            if (noteTimeGt.after(noteTimeLt)) {
                return Collections.emptyList();
            }
        }
        if (CollectionUtils.isEmpty(relIds)) {
            return Collections.emptyList();
        }
        Map notePara = Maps.newHashMap();
        notePara.put("NoteTimeGt", noteTimeGt);
        notePara.put("NoteTimeLt", noteTimeLt);
        notePara.put("relIds", relIds);
        notePara.put("shopId", shopId);
        notePara.put("noteType", noteType);
        //获取这一段时间以内已经产生的Note数据
        return noteInfoDao.selectLastNote(notePara);
    }

    /**
     * 删除未处理的门店信息
     *
     * @param shopId
     * @param noteTypeList
     * @return
     */
    @Override
    public Integer deleteUnHandleNoteInfoByType(Long shopId, List<Integer> noteTypeList) {
        if (CollectionUtils.isEmpty(noteTypeList)) {
            return 0;
        }
        return noteInfoDao.deleteUnHandleByType(shopId, noteTypeList);
    }

    /**
     * 查询未处理的门店信息数量
     *
     * @param shopId
     * @return
     */
    @Override
    public NoteInfoVO countUnHandleNoteInfo(Long shopId) {
        NoteInfoVO noteInfoVO = new NoteInfoVO();

        List<ShopNoteTypeNum> noteTypeNumList = noteInfoDao.getUnHandleNoteNumByType(shopId);
        if (!CollectionUtils.isEmpty(noteTypeNumList)) {
            Integer visitCount = 0;
            Integer nextVisitCount = 0;
            Integer firstMaintainCount = 0;
            Integer secondMaintainCount = 0;
            for (ShopNoteTypeNum noteTypeNum : noteTypeNumList) {
                Integer noteType = noteTypeNum.getNoteType();
                Integer num = noteTypeNum.getNum();
                if (noteType == null) {
                    continue;
                }
                switch (noteType) {
                    case NoteType.APPOINT_NOTE_TYPE:// 预约单提醒
                        noteInfoVO.setAppointCount(num);
                        break;
                    case NoteType.INSURANCE_NOTE_TYPE:// 保险提醒
                        noteInfoVO.setInsuranceCount(num);
                        break;
                    case NoteType.AUDITING_NOTE_TYPE:// 年检提醒
                        noteInfoVO.setAuditingCount(num);
                        break;
                    case NoteType.VISIT_NOTE_TYPE:// 回访提醒
                        visitCount = num;
                        break;
                    case NoteType.NEXT_VISIT_NOTE_TYPE:// 下次回访提醒
                        nextVisitCount = num;
                        break;
                    case NoteType.BIRTHDAY_NOTE_TYPE:// 生日提醒
                        noteInfoVO.setBirthdayCount(num);
                        break;
                    case NoteType.LOST_CUSTOMER_NOTE_TYPE:// 流失客户提醒
                        noteInfoVO.setLostCustomerCount(num);
                        break;
                    case NoteType.FIRST_MAINTAIN_NOTE_TYPE:// 首次保养提醒
                        firstMaintainCount = num;
                        break;
                    case NoteType.SECOND_MAINTAIN_NOTE_TYPE:// 保养二次提醒
                        secondMaintainCount = num;
                        break;
                }
            }
            noteInfoVO.setVisitCount(visitCount + nextVisitCount);
            noteInfoVO.setMaintainCount(firstMaintainCount + secondMaintainCount);
        }
        return noteInfoVO;
    }

    /**
     * 根据类型查询门店提醒
     *
     * @param shopId
     * @param noteType
     * @return
     */
    @Override
    public Page<ShopNoteInfoVO> getShopNoteInfo(Long shopId, Integer noteType, int page, int pageSize) {

        if (noteType == null) {
            return new DefaultPage(Collections.emptyList());
        }

        page = page < 1 ? 1 : page;
        pageSize = pageSize < 1 ? 10 : pageSize > 1000 ? 1000 : pageSize;

        int offset = (page - 1) * pageSize;
        int limit = pageSize;

        List<ShopNoteInfoVO> noteInfoVoList = Lists.newArrayList();
        List<Integer> noteTypeList = Lists.newArrayList(noteType);
        List<Integer> noteFlagList = Lists.newArrayList(0);
        List<String> sorts = Lists.newArrayList("note_time ASC", "id ASC");

        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("noteTypeList", noteTypeList);
        searchMap.put("noteFlagList", noteFlagList);
        searchMap.put("sorts", sorts);
        searchMap.put("offset", offset);
        searchMap.put("limit", limit);

        if (noteType.equals(NoteType.APPOINT_NOTE_TYPE)) { // 预约单提醒
            noteInfoVoList = noteInfoDao.findAppointNoteInfo(shopId, noteFlagList, offset, limit);
        } else {
            if (noteType.equals(NoteType.FIRST_MAINTAIN_NOTE_TYPE)) { // 保养提醒
                noteTypeList.add(NoteType.SECOND_MAINTAIN_NOTE_TYPE);
            } else if (noteType.equals(NoteType.VISIT_NOTE_TYPE)) { // 回访提醒
                noteTypeList.add(NoteType.NEXT_VISIT_NOTE_TYPE);
            }
            List<NoteInfo> noteInfoList = noteInfoDao.select(searchMap);
            if (!CollectionUtils.isEmpty(noteInfoList)) {
                for (NoteInfo noteInfo : noteInfoList) {
                    ShopNoteInfoVO shopNoteInfoVO = new ShopNoteInfoVO();
                    BeanUtils.copyProperties(noteInfo, shopNoteInfoVO);
                    noteInfoVoList.add(shopNoteInfoVO);
                }
            }
        }

        // 查询提醒总数
        int total = noteInfoDao.selectCount(searchMap);

        if (CollectionUtils.isEmpty(noteInfoVoList) || total == 0) {
            return new DefaultPage(Collections.emptyList());
        }

        Set<Long> customerCarIdSet = Sets.newHashSet();
        for (ShopNoteInfoVO noteInfoVO : noteInfoVoList) {
            Long customerCarId = noteInfoVO.getCustomerCarId();
            if (customerCarId != null && customerCarId > 0) {
                customerCarIdSet.add(customerCarId);
            }
        }

        Map<Long, CustomerInfoDTO> customerInfoMap = null;
        try {
            Result<Map<Long, CustomerInfoDTO>> customerInfoResult = rpcCustomerInfoService.findCustomerInfoByCarIds(shopId, customerCarIdSet);
            if (customerInfoResult != null) {
                if (customerInfoResult.isSuccess()) {
                    customerInfoMap = customerInfoResult.getData();
                } else {
                    log.error("调用cube的dubbo接口通过车辆id集合批量查询CustomerInfo, 错误信息: {}", customerInfoResult.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("调用cube的dubbo接口通过车辆id集合批量查询CustomerInfo, 错误信息: ", e);
        }

        if (customerInfoMap != null) {
            for (ShopNoteInfoVO noteInfoVO : noteInfoVoList) {
                Long customerCarId = noteInfoVO.getCustomerCarId();
                if (customerCarId != null && customerCarId > 0) {
                    CustomerInfoDTO customerInfoDTO = customerInfoMap.get(customerCarId);
                    if (customerInfoDTO != null) {
                        noteInfoVO.setCarModel(customerInfoDTO.getCarModel());
                        noteInfoVO.setMileage(customerInfoDTO.getMileage());
                        noteInfoVO.setLastNoteKeepupTime(customerInfoDTO.getLastNoteKeepupTime());
                        noteInfoVO.setTotalNumber(customerInfoDTO.getTotalNumber());
                        noteInfoVO.setLastPayTime(customerInfoDTO.getLastPayTime());
                    }
                }
            }
        }

        PageRequest pageRequest = new PageRequest(page - 1, pageSize);
        return new DefaultPage<>(noteInfoVoList, pageRequest, total);
    }

    /**
     * 查询门店工单回访提醒
     *
     * @param shopId        门店id
     * @param customerCarId 车辆id
     * @param isVisit       是否已回访 (null:包括已回访和未回访; true:已回访; false:未回访)
     * @param page          页码
     * @param pageSize      每页数量
     * @return
     */
    @Override
    public Page<NoteInfo> getVisitNoteInfo(Long shopId, Long customerCarId, Boolean isVisit, int page, int pageSize) {
        page = page < 1 ? 1 : page;
        pageSize = pageSize < 1 ? 10 : pageSize > 1000 ? 1000 : pageSize;

        int offset = (page - 1) * pageSize;
        int limit = pageSize;

        List<Integer> noteFlagList = Lists.newArrayList();
        if (isVisit == null) {
            noteFlagList.add(0);
            noteFlagList.add(1);
        } else if (isVisit) {
            noteFlagList.add(1);
        } else {
            noteFlagList.add(0);
        }
        List<Integer> noteTypeList = Lists.newArrayList(NoteType.VISIT_NOTE_TYPE);
        List<String> sorts = Lists.newArrayList("note_time ASC", "id ASC");

        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("customerCarId", customerCarId);
        searchMap.put("noteTypeList", noteTypeList);
        searchMap.put("noteFlagList", noteFlagList);
        searchMap.put("sorts", sorts);
        searchMap.put("offset", offset);
        searchMap.put("limit", limit);

        List<NoteInfo> noteInfoList = noteInfoDao.select(searchMap);

        // 查询提醒总数
        int total = noteInfoDao.selectCount(searchMap);

        if (CollectionUtils.isEmpty(noteInfoList) || total == 0) {
            return new DefaultPage(Collections.emptyList());
        }

        PageRequest pageRequest = new PageRequest(page - 1, pageSize);
        return new DefaultPage<>(noteInfoList, pageRequest, total);
    }


    /**
     * 查询指定的工单回访提醒
     * @param shopId
     * @param orderId
     * @return
     */
    @Override
    public NoteInfo getVisitNoteInfoByOrderId(Long shopId, Long orderId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("noteType",NoteType.VISIT_NOTE_TYPE);
        param.put("relId",orderId);
        param.put("noteFlag",0);
        List<NoteInfo> noteInfoList = noteInfoDao.select(param);
        if (!CollectionUtils.isEmpty(noteInfoList)) {
            return noteInfoList.get(0);
        }
        return null;
    }

    /**
     * 查询门店提醒
     *
     * @param shopId
     * @param id
     * @return
     */
    @Override
    public NoteInfo getById(Long shopId, Long id) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("id",id);
        List<NoteInfo> noteInfoList = noteInfoDao.select(param);
        if (!CollectionUtils.isEmpty(noteInfoList)) {
            return noteInfoList.get(0);
        }
        return null;
    }

    /**
     * 门店提醒过期
     * @return
     */
    @Override
    public boolean expiredNoteInfo() {
        Date endTime = DateUtil.getEndTime(DateUtil.addDate(new Date(), -1));
        Date startTime = DateUtil.getStartTime(DateUtil.addDate(endTime, -3));
        return noteInfoDao.expiredNoteInfo(startTime, endTime) > 0 ? true : false;
    }

    /**
     * 批量删除提醒
     *
     * @param noteInfoIds
     * @return
     */
    @Override
    public int deleteByIds(List<Long> noteInfoIds) {
        if (CollectionUtils.isEmpty(noteInfoIds)) {
            return 0;
        }
        Long[] ids = new Long[noteInfoIds.size()];
        return noteInfoDao.deleteByIds(noteInfoIds.toArray(ids));
    }

    @Override
    public List<NoteInfo> listUnhandled(Long shopId, Integer noteType, Collection<String> carLicenses) {
        Assert.notNull(shopId);
        Assert.notNull(noteType);
        if (CollectionUtils.isEmpty(carLicenses)) {
            return Lists.newArrayList();
        }
        List<Integer> noteTypeList = getNoteTypeList(noteType);
        return noteInfoDao.selectUnhandled(shopId, noteTypeList, carLicenses);
    }

    @Override
    public void refreshNoteInfo(final Long shopId, final Integer noteType) {
        new BizTemplate<Void>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0, "门店不存在");
                Assert.notNull(noteType, "noteType不能为空");
            }

            @Override
            protected Void process() throws BizException {
                // 删除未处理的提醒
                List<Integer> noteTypeList = getNoteTypeList(noteType);
                deleteUnHandleNoteInfoByType(shopId, noteTypeList);

                final Date currentTime = DateUtil.getStartTime();

                // 重新跑提醒任务
                switch (noteType) {
                    case NoteType.APPOINT_NOTE_TYPE : // 预约单提醒
                        appointNoteService.processAppointNoteInfo(shopId, currentTime);
                        break;
                    case NoteType.FIRST_MAINTAIN_NOTE_TYPE : // 保养提醒
                        maintainNoteService.processMaintainCustomerNoteInfo(shopId, currentTime);
                        break;
                    case NoteType.INSURANCE_NOTE_TYPE : // 保险提醒
                        insuranceNoteService.processInsuranceNoteInfo(shopId, currentTime);
                        break;
                    case NoteType.AUDITING_NOTE_TYPE : // 年检提醒
                        auditingNoteService.processAuditingNoteInfo(shopId, currentTime);
                        break;
                    case NoteType.VISIT_NOTE_TYPE : // 回访提醒
                        visitNoteService.processVisitNoteInfo(shopId, currentTime);
                        nextVisitNoteService.processNextVisitCustomerNoteInfo(shopId, currentTime);
                        break;
                    case NoteType.BIRTHDAY_NOTE_TYPE : // 生日提醒
                        birthdayNoteService.processBirthdayNoteInfo(shopId, currentTime);
                        break;
                    case NoteType.LOST_CUSTOMER_NOTE_TYPE : // 流失客户提醒
                        lostCustomerNoteService.processLostCustomerNoteInfo(shopId, currentTime);
                        break;
                    default :
                        log.error("未知的提醒类型, shopId: {}, noteType: {}", shopId, noteType);
                        throw new BizException("未知的提醒类型");
                }
                return null;
            }
        }.execute();

    }

    private List<Integer> getNoteTypeList(Integer noteType) {
        List<Integer> noteTypeList = Lists.newArrayList(noteType);
        if (noteType == NoteType.FIRST_MAINTAIN_NOTE_TYPE) { // 保养包括二次提醒
            noteTypeList.add(NoteType.SECOND_MAINTAIN_NOTE_TYPE);
        } else if (noteType == NoteType.VISIT_NOTE_TYPE) { // 回访包括下次回访提醒
            noteTypeList.add(NoteType.NEXT_VISIT_NOTE_TYPE);
        }
        return noteTypeList;
    }
}
