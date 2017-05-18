package com.tqmall.legend.dao.warehouseout;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.bi.entity.StatisticsWarehouseOut;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.statistics.param.WarehouseOutReportParam;
import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;

@MyBatisRepository
public interface WarehouseOutDetailDao extends BaseDao<WarehouseOutDetail> {

    /**
     * 物料数
     *
     * @param searchParams
     * @return
     */
    Integer selectGoodsCount(Map<String, Object> searchParams);

    int update(Map<String, Object> param);

    /**
     * 获取出库明细总计信息
     *
     * @param warehouseOutReportParam
     * @return
     */
    StatisticsWarehouseOut getTotalInfo(WarehouseOutReportParam warehouseOutReportParam);

    /**
     * 出库明细报表数量查询
     *
     * @param warehouseOutReportParam
     * @return
     */
    Integer selectReportCount(WarehouseOutReportParam warehouseOutReportParam);

    /**
     * 查询出库信息
     *
     * @param warehouseOutReportParam
     * @return
     */
    List<Long> selectReportInfo(WarehouseOutReportParam warehouseOutReportParam);

    List<CommonPair<Long, BigDecimal>> listOrderId2RealInventoryAmountPair(@Param("shopId") Long shopId, @Param("orderIds") List<Long> orderIds);
}
