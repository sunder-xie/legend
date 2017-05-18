package com.tqmall.legend.biz.account;

import java.util.List;
import java.util.Map;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.entity.account.ComboInfo;
import com.tqmall.legend.entity.account.ComboInfoServiceRel;

/**
 * Created by majian on 16/6/17.
 */
public interface ComboInfoService {
    /**
     * 新增计次卡类型
     * comboInfo != null
     * @param comboInfo
     * @param shopId
     * @param userId @return
     */
    Long addComboInfo(ComboInfo comboInfo, Long shopId, Long userId) throws BizException;

    /**
     * 删除ComboInfo
     *
     * @param id
     * @param userInfo
     * @return
     */
    void deleteComboInfo(Long id, UserInfo userInfo);

    /**
     * 修改计次卡类型内容
     * comboInfo != null
     *
     * @param comboInfo
     * @param shopId
     *@param userId @return
     */
    void updateComboInfo(ComboInfo comboInfo, Long shopId, Long userId);

    /**
     * 停用ComboInfo
     *
     * @param id
     * @param userInfo
     * @return
     */
    void disableComboInfo(Long id, UserInfo userInfo);

    /**
     * 启用ComboInfo
     *
     * @param id
     * @param userInfo
     * @return
     */
    void enableComboInfo(Long id, UserInfo userInfo);

    /**
     * 显示门店的计次卡类型列表
     *
     * @param shopId
     * @return
     */
    List<ComboInfo> listComboInfo(Long shopId);

    /**
     * 显示门店的可用计次卡类型列表
     *
     * @param shopId
     * @return
     */
    List<ComboInfo> listEnabledComboInfo(Long shopId);

    /**
     * 获取comboInfo包括serviceRelList
     *
     * @param id
     * @return
     */
    ComboInfo getComboInfo(Long id, Long shopId);

    /**
     * 批量获取计次卡类型
     *
     * @param shopId
     * @param comboIds
     * @return
     */
    List<ComboInfo> findComboInfoByIds(Long shopId, Long... comboIds);

    /**
     * 获取门店的计次卡类型数量
     *
     * @param shopId
     * @return
     */
    Integer selectCount(Long shopId);

    /**
     *
     * @param shopId
     * @param id
     * @return
     */
    ComboInfo findById(Long shopId, Long id);

    boolean isComboINfoGranted(Long id);

    Map<String,Map<String,ComboInfoServiceRel>> getComboInfoMap(Long shopId);

    /**
     * 通过计次卡名，获取计次卡类型信息
     * @param shopId
     * @param comboNames
     * @return
     */
    List<ComboInfo> findComboInfoByNames(Long shopId,List<String> comboNames);

    List<ComboInfo> list(Long shopId);

    List<ComboInfo> listByIds(List<Long> ids);
}
