package com.tqmall.legend.dao.order;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.order.OrderType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zsy on 2015/1/20.
 */
@MyBatisRepository
public interface OrderTypeDao extends BaseDao<OrderType> {

    /**
     * 根据ids批量更新状态
     *
     * @param showStatus
     * @param ids
     * @param modifier 修改人
     * @return
     */
    int updateShowStatusByIds(@Param("showStatus") Integer showStatus, @Param("ids") List<Long> ids, @Param("modifier") Long modifier);

}
