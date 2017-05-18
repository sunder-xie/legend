package com.tqmall.legend.dao.warehousein;

import com.tqmall.legend.bi.entity.StatisticsWarehouseIn;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.statistics.param.WarehouseInReportParam;
import com.tqmall.legend.entity.warehousein.WarehouseInDetail;
import com.tqmall.legend.pojo.warehouse.WarehouseInTotalVO;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface WarehouseInDetailDao extends BaseDao<WarehouseInDetail> {
    int update(Map<String, Object> params);

    /**
     * 查询物料数
     *
     * @param searchParams
     * @return
     */
    Integer selectGoodsCount(Map<String, Object> searchParams);

    /**
     * 获取入库总成本金额
     *
     * @param warehouseInReportParam
     * @return
     */
    WarehouseInTotalVO getTotalInfo(WarehouseInReportParam warehouseInReportParam);

    /**
     * 获取入库总金额、税费、运费
     * @param warehouseInReportParam
     * @return
     */
    WarehouseInTotalVO getTotalAmount(WarehouseInReportParam warehouseInReportParam);

    /**
     * 出库明细报表数量查询
     *
     * @param warehouseInReportParam
     * @return
     */
    Integer selectReportCount(WarehouseInReportParam warehouseInReportParam);

    /**
     * 查询出库信息
     *
     * @param warehouseInReportParam
     * @return
     */
    List<StatisticsWarehouseIn> selectReportInfo(WarehouseInReportParam warehouseInReportParam);
}
