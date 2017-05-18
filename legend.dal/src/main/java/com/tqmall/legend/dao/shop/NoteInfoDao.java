package com.tqmall.legend.dao.shop;

import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.shop.NoteInfo;
import com.tqmall.legend.pojo.shopnote.ShopNoteInfoVO;
import com.tqmall.legend.pojo.shopnote.ShopNoteTypeNum;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface NoteInfoDao extends BaseDao<NoteInfo> {
    /**
     * 获取最近的提醒信息
     */
    List<NoteInfo> selectLastNote(Map param);

    List<CommonPair<Integer,Integer>> selectCountWithType(Map<String, Object> params);

    List<CommonPair<Integer,Integer>> selectCountWithWay(Map<String, Object> params);

    int expiredNoteInfo(@Param("startPastTime") Date startPastTime, @Param("endPastTime") Date endPastTime);

    /**
     *
     * @param shopId       门店id
     * @param noteInfoIds  提醒id集合
     * @param noteWay      提醒方式
     * @param operator     操作人
     * @return
     */
    int batchHandleNoteInfo(@Param("shopId") Long shopId, @Param("noteInfoIds") List<Long> noteInfoIds, @Param("noteWay") int noteWay, @Param("operator") String operator);

    /**
     * 删除未处理的提醒
     *
     * @param shopId
     * @param noteTypeList
     * @return
     */
    int deleteUnHandleByType(@Param("shopId") Long shopId, @Param("noteTypeList") List<Integer> noteTypeList);

    /**
     * 查询预约单提醒
     *
     * @param shopId
     * @param noteFlagList
     * @param offset
     * @param limit
     * @return
     */
    List<ShopNoteInfoVO> findAppointNoteInfo(@Param("shopId") Long shopId, @Param("noteFlagList") List<Integer> noteFlagList, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 查询未处理的提醒数量(按提醒类型划分)
     * @param shopId
     * @return
     */
    List<ShopNoteTypeNum> getUnHandleNoteNumByType(@Param("shopId") Long shopId);

    /**
     * 批量查询未处理数据, 指定提醒类型, 指定车牌
     * @param shopId
     * @param noteType
     * @param carLicenses
     * @return
     */
    List<NoteInfo> selectUnhandled(@Param("shopId") Long shopId,
                               @Param("noteTypes") List<Integer> noteType,
                               @Param("carLicenses") Collection<String> carLicenses);
}
