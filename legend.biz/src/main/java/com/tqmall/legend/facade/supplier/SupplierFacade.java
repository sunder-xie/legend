package com.tqmall.legend.facade.supplier;

import com.tqmall.common.UserInfo;
import com.tqmall.itemcenter.object.param.supplier.SupplierParam;
import com.tqmall.itemcenter.object.param.supplier.SupplierQuery;
import com.tqmall.itemcenter.object.result.supplier.SupplierDTO;
import com.tqmall.legend.biz.common.DefaultPage;

import java.util.List;

/**
 * Created by sven on 16/11/23.
 */
public interface SupplierFacade {
    /**
     * 获取供应商编号
     */

    String getSupplierSn(Long shopId);

    /**
     * 新增/更新供应商
     */

    Long saveOrUpdate(SupplierParam supplierParam);

    /**
     * 查询供应商详情
     *
     * @param shopId
     * @param id
     * @return
     */

    SupplierDTO selectDetail(Long shopId, Long id);


    /**
     * 获取门店所有供应商
     *
     * @param shopId
     * @return
     */
    List<SupplierDTO> selectByShopId(Long shopId);


    /**
     * 供应商查询
     *
     * @param supplierQuery
     * @return
     */
    DefaultPage<SupplierDTO> select(SupplierQuery supplierQuery);


    /**
     * 供应商删除
     *
     * @param userInfo
     * @param id
     */
    void delete(UserInfo userInfo, Long id);


    /**
     * 合并供应商
     *
     * @param requestId 被合并的供应商
     * @param resultId  合并后的供应商
     */
    void mergeSupplier(Long requestId, Long resultId, UserInfo userInfo);

    /**
     * 批量查询供应商
     *
     * @param shopId
     * @param supplierIds
     * @return
     */

    List<SupplierDTO> selectByShopIdAndIds(Long shopId, List<Long> supplierIds);
}
