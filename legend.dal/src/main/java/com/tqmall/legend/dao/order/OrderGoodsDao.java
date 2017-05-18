package com.tqmall.legend.dao.order;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.order.OrderGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface OrderGoodsDao extends BaseDao<OrderGoods> {

    /**
     * 根据客户ID
     * 分页查询选购过的物料列表
     *
     * @return
     */
    List<OrderGoods> getHistoryGoodList(Map<String, Object> searchParams);

    /**
     * 根据客户ID
     * 查询选购过的物料总数
     *
     * @return
     */
    Integer countHistoryGoodList(@Param("orderIdList") List<Long> orderIdList);


    List<OrderGoods> selectByOrderIds(@Param("orderIds")Long[] orderIds);

}
