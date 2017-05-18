package com.tqmall.legend.facade.customer;

import com.tqmall.legend.facade.customer.bo.AllotBo;
import com.tqmall.legend.facade.customer.bo.CustomAllotBo;
import com.tqmall.legend.facade.customer.vo.AllotResultVo;
import com.tqmall.legend.facade.customer.vo.AllotUserVo;

import java.util.List;
import java.util.Map;

/**
 * 客户员工归属服务
 * Created by zsy on 16/12/15.
 */
public interface CustomerUserRelFacade {

    /**
     * 该用户是否有分配数据
     *
     * @param shopId
     * @return
     */
    boolean isAllot(Long shopId, Long userId);

    /**
     * 员工客户分配总数
     *
     * @param shopId
     * @param userId
     * @return
     */
    Integer userAllotCount(Long shopId, Long userId);

    /**
     * 单个、多个客户调整接口
     *
     * @param shopId        门店id
     * @param newUserId     员工id
     * @param customerCarIdList 车辆id集合
     * @return 分配成功的车辆数
     */
    Integer allotCustomerCars(Long shopId, Long newUserId, List<Long> customerCarIdList, Long operatorId);

    /**
     * 解绑车辆
     *
     * @param shopId 门店id
     * @param userId 预解绑的员工id
     * @return 解绑成功的车辆数
     */
    Integer unAllotByUserId(Long shopId, Long userId, Long operatorId);

    /**
     * 解绑车辆
     *
     * @param shopId        门店id
     * @param customerCarId 车辆id集合
     * @return 成功解绑的车辆数
     */
    Integer unAllotCustomerCarIds(Long shopId, Long operatorId, Long... customerCarId);

    /**
     * 车辆归属迁移
     *
     * @param shopId    门店id
     * @param oldUserId 原员工id
     * @param newUserId 目标员工id
     * @return 成功迁移的车辆数
     */
    Integer transformAllotCustomerCars(Long shopId, Long oldUserId, Long newUserId, Long operatorId);


    /**
     * 获取归属员工列表 （店长+老板+服务顾问）
     *
     * @param shopId       门店id
     * @return
     */
    List<AllotUserVo> getAllotUserList(Long shopId);

    /**
     * 获取归属员工列表
     *
     * @param shopId       门店id
     * @param isAllot      是否只查询分配过的员工，false:否 true:是，默认false
     * @return
     */
    List<AllotUserVo> getAllotUserList(Long shopId, Boolean isAllot);

    /**
     * 获取归属员工列表
     *
     * @param shopId       门店id
     * @param isAllot      是否只查询分配过的员工，false:否 true:是，默认false
     * @param choseUserIds 已选中的员工 , 有值，则过滤此员工
     * @return
     */
    List<AllotUserVo> getAllotUserList(Long shopId, Boolean isAllot, List<Long> choseUserIds);

    /**
     * 根据车辆id获取分配的员工信息
     * @param carId
     * @return
     */
    AllotUserVo getAllotUserByCarId(Long carId);

    /**
     * 根据门店id,车辆ids获取分配的员工信息
     * @param shopId
     * @param carIds
     * @return  key为carId，value为服务顾问对象
     */
    Map<Long, AllotUserVo> getAllotUserMapByShopIdAndCarIds(Long shopId, List<Long> carIds);

    /**
     * 标准化分配：一键分配
     * @param allotBo
     * @return
     */
    List<AllotResultVo> allot(AllotBo allotBo);

    /**
     * 自定义分配：平均分配
     * @param customAllotBo
     * @return
     */
    List<AllotResultVo> customAllot(CustomAllotBo customAllotBo);
}
