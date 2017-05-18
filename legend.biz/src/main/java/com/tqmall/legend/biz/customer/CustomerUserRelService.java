package com.tqmall.legend.biz.customer;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.entity.customer.CustomerUserRel;
import com.tqmall.legend.facade.customer.bo.UnbindingBo;

import java.util.List;
import java.util.Map;

/**
 * 客户员工归属服务
 * Created by lixiao on 16/12/15.
 */
public interface CustomerUserRelService {

    /**
     * 根据员工获取关系
     *
     * @param userId
     * @return
     */
    List<CustomerUserRel> selectByUserId(Long userId);

    /**
     * 根据员工ids获取关系
     * @param userIds
     * @return
     */
    List<CustomerUserRel> selectByUserIds(List<Long> userIds);

    /**
     * 根据门店id和车辆id获取客户关系
     * @param shopId
     * @param customerCarIds
     * @return
     */
    List<CustomerUserRel> selectByShopIdAndCustomerCarIds(Long shopId,List<Long> customerCarIds);

    /**
     * 根据车辆获取关系
     *
     * @param customerCarId
     * @return
     */
    CustomerUserRel selectByCustomerCarId(Long customerCarId);


    /**
     * 根据客户门店id获取分配客户数
     *
     * @param shopId
     * @return
     */
    Integer selectCount(Long shopId);

    /**
     * 根据客户id和门店id获取分配客户数
     *
     * @param shopId
     * @param userId
     * @return
     */
    Integer selectCount(Long shopId, Long userId);

    /**
     * 批量插入
     *
     * @param customerUserRelList
     * @return
     */
    Integer batchInsert(List<CustomerUserRel> customerUserRelList);

    /**
     * 解绑客户
     *
     * @param unbindingBo
     * @return
     */
    Integer batchDelete(UnbindingBo unbindingBo);

    /**
     * 获取已分配的员工ids
     * @param shopId
     * @return
     */
    List<Long> selectAllotUserIds(Long shopId);

    /**
     *  插入客户用户归属
     * @param customerUserRel
     * @return
     */
    int insert(CustomerUserRel customerUserRel);

    /**
     * 根据用户ids，查询分配此用户的客户数
     *
     * @param userIds
     * @return
     */
    Map<Long, Integer> selectAllotUserNumMapByUserIds(Long shopId, Long[] userIds);
}
