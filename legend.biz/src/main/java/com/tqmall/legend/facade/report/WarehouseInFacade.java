package com.tqmall.legend.facade.report;


import com.tqmall.legend.bi.entity.StatisticsWarehouseIn;
import com.tqmall.legend.entity.statistics.SimplePage;
import com.tqmall.legend.entity.statistics.param.WarehouseInReportParam;
import com.tqmall.legend.pojo.warehouse.WarehouseInTotalVO;

import java.util.List;

/**
 * Created by tanghao on 16/9/9.
 */
public interface WarehouseInFacade {

    //获取出库明细报表分页信息
    SimplePage<StatisticsWarehouseIn> getPage(WarehouseInReportParam warehouseInReportParam);

    //获取出库总计信息
    WarehouseInTotalVO getTotalInfo(WarehouseInReportParam warehouseInReportParam);

    //获取出库明细报表总条数
    Integer getWarehousInSize(WarehouseInReportParam warehouseInReportParam);

    //获取出库明细报表excel
    List<StatisticsWarehouseIn> getWarehouseExcelList(WarehouseInReportParam warehouseInReportParam);
}
