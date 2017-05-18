package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.entity.shop.NoteInfo;
import com.tqmall.legend.entity.shop.NoteInfoVO;
import com.tqmall.legend.pojo.shopnote.ShopNoteInfoVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by twg on 16/3/20.
 */
public interface ShopNoteInfoService {


    /**
     * 获取门店提醒信息列表
     * @param shopId
     * @param noteType
     * @return
     */
    List<NoteInfo> getNoteInfoList(Long shopId,Integer noteType);


    /**
     * 获取门店提醒消息总计
     * @param shopId
     * @param noteType
     * @return
     */
    Integer getNoteInfoCount(Long shopId, Integer noteType);

    /**
     * 获取客户列表
     * @return
     */
    Page<CustomerInfo> getCustomerInfo(Map<String,Object> params,Pageable pageable);


    Page<NoteInfo> getPage(Map<String,Object> params,Pageable pageable);

    /**
     * 获取提醒条数
     */
    Integer getShopNoteInfoCount(Map<String,Object> params);

    List<NoteInfo> select(Map<String,Object> params);

    int updateById(NoteInfo info);

    List<NoteInfo> selectLastNote(Map param);

    /**
     * 批量插入
     * @param t
     * @return
     */
    Integer batchInsert(List<NoteInfo> t);

    /**
     * 批量处理提醒
     * @param shopId 门店id
     * @param noteInfoIds 提醒id集合
     * @param noteWay 提醒方式
     * @param operator 操作人
     * @return
     */
    int batchHandleNoteInfo(Long shopId, List<Long> noteInfoIds, int noteWay, String operator);

    /**
     * 获取特定时间内的Note数据
     * @param shopId 店铺ID
     * @param noteType 通知类型
     * @param relIds 关联ID set
     * @return
     */
    List<NoteInfo> getPeriodNoteInfoListByRelIdAndType(Date noteTimeGt, Date noteTimeLt, Long shopId, int noteType, List<Long> relIds);

    /**
     * 删除未处理的门店信息
     *
     * @param shopId
     * @param noteTypeList
     * @return
     */
    Integer deleteUnHandleNoteInfoByType(Long shopId, List<Integer> noteTypeList);

    /**
     * 查询未处理的门店信息数量
     *
     * @param shopId
     * @return
     */
    NoteInfoVO countUnHandleNoteInfo(Long shopId);

    /**
     * 根据类型查询门店提醒
     *
     * @param shopId
     * @param noteType
     * @return
     */
    Page<ShopNoteInfoVO> getShopNoteInfo(Long shopId, Integer noteType, int page, int pageSize);

    /**
     * 查询门店工单回访提醒
     * @param shopId 门店id
     * @param customerCarId 车辆id
     * @param isVisit 是否已回访 (null:包括已回访和未回访; true:已回访; false:未回访)
     * @param page 页码
     * @param pageSize 每页数量
     * @return
     */
    Page<NoteInfo> getVisitNoteInfo(Long shopId, Long customerCarId, Boolean isVisit, int page, int pageSize);

    /**
     * 查询指定的工单回访提醒
     * @param shopId
     * @param orderId
     * @return
     */
    NoteInfo getVisitNoteInfoByOrderId(Long shopId, Long orderId);

    /**
     * 查询门店提醒
     * @param shopId
     * @param id
     * @return
     */
    NoteInfo getById(Long shopId, Long id);

    /**
     * 门店提醒过期
     */
    boolean expiredNoteInfo();

    /**
     * 批量删除提醒
     * @param noteInfoIds
     * @return
     */
    int deleteByIds(List<Long> noteInfoIds);

    /**
     * 查询CarLicenses中指定类型,未处理的提醒
     * @param shopId
     * @param noteType
     * @param carLicenses
     * @return
     */
    List<NoteInfo> listUnhandled(Long shopId, Integer noteType, Collection<String> carLicenses);


    void refreshNoteInfo(Long shopId, Integer noteType);
}
