package com.tqmall.legend.bi.dao.cube;

import com.tqmall.legend.bi.entity.cube.CustomerInfo;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
/**
 * Created by zsy on 17/3/20.
 */
@MyBatisRepository("biCustomerInfo")
public interface CustomerInfoDao extends BaseDao<CustomerInfo> {

}
