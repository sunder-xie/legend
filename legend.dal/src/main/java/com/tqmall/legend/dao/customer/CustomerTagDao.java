package com.tqmall.legend.dao.customer;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.customer.CustomerTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zsy on 16/4/12.
 */
@MyBatisRepository
public interface CustomerTagDao extends BaseDao<CustomerTag> {

    List<String> selectTag(@Param("shopId") Long shopId);

    List<String> listCustomizeTag(@Param("shopId") Long shopId);
}
