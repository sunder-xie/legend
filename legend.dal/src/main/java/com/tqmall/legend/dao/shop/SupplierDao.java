package com.tqmall.legend.dao.shop;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.shop.Supplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface SupplierDao extends BaseDao<Supplier> {
    /**
     * 通过供应商编号，获取供应商列表
     * @param shopId
     * @param supplierSn
     * @return
     */
    List<Supplier> findBySupplierSns(@Param("shopId")Long shopId,@Param("supplierSns")List<String> supplierSn);

}
