package com.tqmall.legend.facade.report;

import java.math.BigDecimal;
import java.util.List;

import com.tqmall.common.UserInfo;
import com.tqmall.cube.shop.param.report.RepairPrefParam;
import com.tqmall.cube.shop.result.shop.*;
import com.tqmall.legend.facade.report.vo.EmpPrefConfig;

/**
 * Created by tanghao on 16/10/12.
 */
public interface StaffPerfFacade {

    /**
     * 检测配置信息
     * @param shopId
     * @return
     */
    boolean checkConfig(Long shopId);

    /**
     * 获取门店维修提成配置信息
     * @param shopId
     * @return
     */
    List<EmpPrefConfig> getRepairPerformanceConfig(Long shopId);

    /**
     * 获取门店销售提成配置信息
     * @param shopId
     * @return
     */
    List<EmpPrefConfig> getSalePerformanceConfig(Long shopId);

    /**
     * 获取门店销售提成配置信息
     * @param shopId
     * @return
     */
    BigDecimal getSaPerformanceConfig(Long shopId);

    /**
     * 根据月份获取门店所有配置信息
     * @param shopId
     * @param month yyyy-MM
     * @return
     */
    List<EmpPrefConfig> getAllPerformanceConfigByMonth(Long shopId,String month);

    /**
     * 提交配置信息
     * @param shopId
     * @param employeePerformanceConfigVOs
     * @return
     */
    boolean submitPerformanceConfig(Long shopId,List<EmployeePerformanceConfigVO> employeePerformanceConfigVOs);

    /**
     * 门店总绩效列表
     * @param shopId
     * @param dateStr
     * @return
     */
    List<EmpPerformanceInfoVO> queryTotalPercentageInfo(Long shopId,String dateStr);

    /**
     * 查询门店维修提成(员工维度)
     * @param shopId
     * @param dateStr
     * @return
     */
    List<EmpRepairInfoVO> queryTotalRepairPref(Long shopId,String dateStr);

    /**
     * 查询门店的维修提成(服务维度)
     * @param repairPrefParam
     * @return
     */
    SimplePage<EmpRepairInfoVO> queryRepairInfoGroupByService(RepairPrefParam repairPrefParam);

    /**
     * 查询门店维修提成(工单维度)
     * @param repairPrefParam
     * @return
     */
    SimplePage<EmpRepairInfoVO> queryRepairInfoGroupByOrder(RepairPrefParam repairPrefParam);

    /**
     * 查询门店销售提成(员工维度)
     * @param shopId
     * @param dateStr
     * @return
     */
    List<EmpSaleInfoVO> queryTotalSaleInfo(Long shopId,String dateStr);

    /**
     * 查询门店销售提成(物料维度)
     * @param shopId
     * @param dateStr
     * @param empId
     * @return
     */
    List<EmpSaleInfoVO> querySaleInfoGroupByService(Long shopId,String dateStr,Long empId);

    /**
     * 查询门店销售提成(工单维度)
     * @param repairPrefParam
     * @return
     */
    SimplePage<EmpSaleInfoVO> querySaleInfoGroupByOrder(RepairPrefParam repairPrefParam);

    /**
     * 查询门店销售提成(工单维度)
     * @param repairPrefParam
     * @return
     */
    SimplePage<EmpSAInfoVO> querySAInfoGroupByOrder(RepairPrefParam repairPrefParam);

    /**
     * 查询门店销售提成(员工维度)
     * @param shopId
     * @param dateStr
     * @return
     */
    List<EmpSAInfoVO> querySATotalInfo(Long shopId,String dateStr);



}
