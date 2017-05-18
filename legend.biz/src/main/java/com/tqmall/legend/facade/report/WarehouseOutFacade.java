package com.tqmall.legend.facade.report;

import com.tqmall.legend.bi.entity.StatisticsWarehouseOut;
import com.tqmall.legend.entity.statistics.SimplePage;
import com.tqmall.legend.entity.statistics.param.WarehouseOutReportParam;

import java.util.List;

/**
 * Created by tanghao on 16/9/8.
 */
public interface WarehouseOutFacade {

    //获取出库明细报表分页信息
    SimplePage<StatisticsWarehouseOut> getPage(WarehouseOutReportParam warehouseOutReportParam);

    //获取出库总计信息
    StatisticsWarehouseOut getTotalInfo(WarehouseOutReportParam warehouseOutReportParam);

    //获取出库明细报表总条数
    Integer getWarehousOutSize(WarehouseOutReportParam warehouseOutReportParam);

    //获取出库明细报表excel
    List<StatisticsWarehouseOut> getWarehouseExcelList(WarehouseOutReportParam warehouseOutReportParam);
}
