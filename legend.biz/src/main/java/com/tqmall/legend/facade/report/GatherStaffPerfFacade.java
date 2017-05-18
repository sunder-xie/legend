package com.tqmall.legend.facade.report;

import com.tqmall.cube.shop.param.report.RepairPrefParam;
import com.tqmall.cube.shop.result.shop.*;
import com.tqmall.legend.facade.report.bo.GatherConfigParam;
import com.tqmall.legend.facade.report.vo.EmpPrefConfig;

import java.util.List;

/**
 * Created by tanghao on 16/12/16.
 */
public interface GatherStaffPerfFacade {

    /**
     * 检测配置信息
     * @param shopId
     * @return
     */
    boolean checkConfig(Long shopId,String month);

    /**
     * 提交配置信息
     * @param shopId
     * @param gatherConfigParam
     * @return
     */
    boolean submitPerformanceConfig(Long shopId,GatherConfigParam gatherConfigParam);

    /**
     * 根据月份获取门店所有配置信息
     * @param shopId
     * @param month yyyy-MM
     * @return
     */
    List<EmpPrefConfig> getAllPerformanceConfigByMonth(Long shopId, String month);

    /**
     * 根据门店id获取加点绩效
     * @param shopId
     * @return
     */
    List<EmpPrefConfig> getAddPointConfig(Long shopId);

    /**
     * 获取样板店老板业绩汇总
     * @param shopId
     * @param month
     * @return
     */
    List<GatherBossPerformanceInfoVO> queryBossCollect(Long shopId,String month);

    /**
     * 获取样板店员工业绩汇总
     * @param shopId
     * @param month
     * @param userId
     * @return
     */
    GatherEmpPerformanceInfoVO queryUserCollect(Long shopId, String month, Long userId);

    /**
     * 获取门店加点绩效统计列表
     * @param shopId
     * @param month
     * @return
     */
    List<GatherAddPointPrefVO> queryAddPointInfo(Long shopId, String month);

    /**
     * 获取门店加点绩效统详情统计图接口
     * @param shopId
     * @param month
     * @return
     */
    GatherAddPointPrefVO queryAddPointDetail(Long shopId, String month,Long userId);

    /**
     * 查询销售之星列表
     * @param shopId
     * @param month
     * @return
     */
    List<GatherPrefSaleStarVO> querySaleStarList(Long shopId,String month,Long userId);

    /**
     * 查询拉新客户列表
     * @param param
     * @return
     */
    SimplePage<GatherPrefNewCustomerPrefVo> queryNewCustomerList(RepairPrefParam param);


    /**
     * 查询业绩归属列表
     * @param param
     * @return
     */
    SimplePage<GatherBusinessBeloneVO> queryBusinessBeloneList(RepairPrefParam param);

}
