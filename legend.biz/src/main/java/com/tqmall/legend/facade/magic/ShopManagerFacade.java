package com.tqmall.legend.facade.magic;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.magic.vo.ShopManagerExtVO;
import com.tqmall.legend.facade.magic.vo.WorkOrderProcessRelVo;

import java.util.List;

/**
 * 钣喷中心-技师扩展信息表
 * Created by shulin on 16/7/1.
 */
public interface ShopManagerFacade {
    /**
     * 更新员工扩展表数据
     *
     * @param shopManagerExtVO
     * @return
     */
    public Result modifyShopManagerExt(ShopManagerExtVO shopManagerExtVO, Long modifier);

    /**
     * 新增扩展信息
     *
     * @param shopManagerExtVO
     * @param creator
     * @return
     */
    public Result addShopManagerExtInfo(ShopManagerExtVO shopManagerExtVO, Long creator);


    /**
     * 批量更新员工扩展表数据，用于创建班组时选定员工信息
     *
     * @param shopManagerExtVOList
     * @return
     */
    public Result modifyShopManagerExtList(List<ShopManagerExtVO> shopManagerExtVOList);

    /**
     * 通过店铺ID和员工ID查找员工扩展信息详情
     *
     * @param shopId
     * @param managerId
     * @return
     */
    public Result<ShopManagerExtVO> getShopManagerExtInfo(Long shopId, Long managerId);


    /**
     * 获取未被分组的员工信息
     *
     * @param shopId
     * @return
     */
    public Result<List<ShopManagerExtVO>> getUngroupedShopManagerExt(Long shopId);


    /**
     * 修改员工（0-空闲，1-忙碌）状态
     *
     * @param managerId
     * @param status
     * @return
     */
    public Result<Boolean> modifyShopManagerExtStatus(Long shopId, Long managerId, Long currentUser, Integer status, WorkOrderProcessRelVo currentProcess);

    /**
     * 通过店铺ID和员工编号来查询扩展信息
     *
     * @param shopId
     * @param carNum
     * @return
     */
    public Result<ShopManagerExtVO> getShopManagerExtByCarNum(Long shopId, String carNum);


    /**
     * 通过店铺ID和班组ID 查询某个班组内的成员列表
     *
     * @param shopId
     * @param teamId
     * @return
     */
    public Result<List<ShopManagerExtVO>> getShopManagerExtInfoByTeamId(Long shopId, Long teamId);

    /**
     * 通过店铺ID和班组ID 查询某个班组内的在岗成员列表
     *
     * @param shopId
     * @param teamId
     * @return
     */
    public Result<List<ShopManagerExtVO>> getOnWorkShopManagerExtInfoByTeamId(Long shopId, Long teamId);


    /**
     * 通过店铺ID和员工ID删除员工扩展信息详情
     *
     * @param shopId
     * @param managerId
     * @return
     */
    public Result<Integer> removeShopMnagerExt(Long shopId, Long managerId, Long modifier);


}
