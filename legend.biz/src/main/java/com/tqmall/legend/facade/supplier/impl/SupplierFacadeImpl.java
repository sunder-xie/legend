package com.tqmall.legend.facade.supplier.impl;

import com.google.common.collect.Lists;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.itemcenter.object.param.BaseDelete;
import com.tqmall.itemcenter.object.param.supplier.SupplierParam;
import com.tqmall.itemcenter.object.param.supplier.SupplierQuery;
import com.tqmall.itemcenter.object.result.supplier.SupplierDTO;
import com.tqmall.itemcenter.service.supplier.RpcSupplierService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.supplier.SupplierFacade;
import com.tqmall.legend.facade.supplier.bo.SupplierBo;
import com.tqmall.legend.facade.warehouse.WarehouseInFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sven on 16/11/23.
 */
@Service
@Slf4j
public class SupplierFacadeImpl implements SupplierFacade {

    @Autowired
    private RpcSupplierService rpcSupplierService;
    @Autowired
    private WarehouseInFacade warehouseInFacade;

    @Override
    public String getSupplierSn(Long shopId) {
        log.info("【DUBBO】调用获取供应商编号接口,门店id:{}", shopId);
        Result<String> result = rpcSupplierService.getSupplierSn(Constants.CUST_SOURCE, shopId);
        if (!result.isSuccess()) {
            log.error("【DUBBO】调用获取供应商编号失败,错误原因", LogUtils.objectToString(result));
            throw new BizException("页面初始化失败");
        }
        return result.getData();
    }

    @Override
    public Long saveOrUpdate(SupplierParam supplierParam) {
        //更新入库单供应商
        updateSupplier(supplierParam);
        supplierParam.setSource(Constants.CUST_SOURCE);
        log.info("【DUBBO】调用保存或更新供应商接口,参数:{}", LogUtils.objectToString(supplierParam));
        Result<Long> result = rpcSupplierService.saveOrUpdateSupplier(supplierParam);
        if (!result.isSuccess()) {
            log.error("【DUBBO】调用保存或更新供应商接口失败,错误原因{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    @Override
    public SupplierDTO selectDetail(Long shopId, Long id) {
        if (shopId == null || id == null) {
            return null;
        }
        log.info("【DUBBO】调用获取供应商详情,门店id:{},供应商id:{}", shopId, id);
        Result<SupplierDTO> result = rpcSupplierService.selectDetail(Constants.CUST_SOURCE, shopId, id);
        if (!result.isSuccess()) {
            log.error("【DUBBO】调用获取供应商详情失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    @Override
    public List<SupplierDTO> selectByShopId(Long shopId) {
        if (shopId == null) {
            return Lists.newArrayList();
        }
        log.info("【DUBBO】调用根据门店ID获取供应商列表接口,门店id:{}", shopId);
        Result<List<SupplierDTO>> result = rpcSupplierService.selectByShopId(Constants.CUST_SOURCE, shopId);
        if (!result.isSuccess()) {
            log.error("【DUBBO】调用根据门店ID获取供应商列表接口失败,错误原因:{}", LogUtils.objectToString(result));
            return Lists.newArrayList();
        }
        return result.getData();
    }

    @Override
    public DefaultPage<SupplierDTO> select(SupplierQuery supplierQuery) {
        if (supplierQuery.getShopId() == null) {
            return null;
        }
        supplierQuery.setSource(Constants.CUST_SOURCE);
        DefaultPage<SupplierDTO> defaultPage = new DefaultPage<>(new ArrayList<SupplierDTO>());
        log.info("【DUBBO】调用查询供应商列表接口,查询参数:{}", LogUtils.objectToString(supplierQuery));
        PagingResult<SupplierDTO> result = rpcSupplierService.select(supplierQuery);
        if (!result.isSuccess()) {
            log.error("【DUBBO】调用查询供应商列表接口失败,错误原因:{}", LogUtils.objectToString(result));
            return defaultPage;
        }
        PageRequest pageRequest = new PageRequest(supplierQuery.getPage(), supplierQuery.getSize());
        defaultPage = new DefaultPage<>(result.getList(), pageRequest, result.getTotal());
        return defaultPage;
    }

    @Override
    public void delete(UserInfo userInfo, Long id) {
        if (id == null) {
            throw new BizException("供应商不存在");
        }
        Long shopId = userInfo.getShopId();
        boolean isExist = warehouseInFacade.isSupplierExist(shopId, id);
        if (isExist) {
            throw new BizException("存在该供应商的入库记录,无法删除");
        }
        BaseDelete baseDelete = new BaseDelete();
        baseDelete.setSource(Constants.CUST_SOURCE);
        baseDelete.setModifier(userInfo.getUserId());
        baseDelete.setId(id);
        baseDelete.setShopId(userInfo.getShopId());
        log.info("【DUBBO】调用供应商删除接口,参数:{}", LogUtils.objectToString(baseDelete));
        Result<Integer> result = rpcSupplierService.delete(baseDelete);
        if (!result.isSuccess()) {
            log.error("【DUBBO】调用供应商删除接口失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
    }

    /**
     * 1.判断结果供应商是否存在
     * 2.更新所有入库单供应商
     * 3.删除被合并的供应商
     *
     * @param requestId 被合并的供应商
     * @param resultId  结果供应商
     * @param userInfo
     */
    @Override
    public void mergeSupplier(Long requestId, Long resultId, UserInfo userInfo) {
        if (requestId == null || resultId == null) {
            throw new BizException("请选择要合并的供应商");
        }
        if (requestId.equals(resultId)) {
            throw new BizException("请选择不同的供应商合并");
        }
        Long shopId = userInfo.getShopId();
        SupplierDTO resultSupplier = selectDetail(shopId, resultId);
        if (resultSupplier == null) {
            throw new BizException("供应商不存在或已删除");
        }
        //更新入库单供应商
        SupplierBo supplierBo = new SupplierBo();
        supplierBo.setShopId(shopId);
        supplierBo.setReqSupplierId(requestId);
        supplierBo.setSupplierId(resultId);
        supplierBo.setSupplierName(resultSupplier.getSupplierName());
        supplierBo.setOperator(userInfo.getUserId());
        log.info("合并供应商:将{}合并为{}", requestId, resultId);
        warehouseInFacade.updateSupplier(supplierBo);
        delete(userInfo, requestId);
    }

    @Override
    public List<SupplierDTO> selectByShopIdAndIds(Long shopId, List<Long> supplierIds) {
        log.info("【DUBBO】调用供应商批量查询接口,参数:shopId:{},ids:{}", shopId, supplierIds.toString());
        Result<List<SupplierDTO>> result = rpcSupplierService.selectByShopIdAndIds(shopId, supplierIds);
        if (!result.isSuccess()) {
            log.error("【DUBBO】调用供应商批量查询接口失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    //更新入库单供应商
    private void updateSupplier(SupplierParam supplierParam) {
        if (supplierParam == null) {
            throw new BizException("供应商数据异常");
        }
        Long supplierId = supplierParam.getId();
        String supplierName = supplierParam.getSupplierName();
        if (supplierId == null || StringUtils.isBlank(supplierName)) {
            return;
        }
        SupplierBo supplierBo = new SupplierBo();
        supplierBo.setShopId(supplierParam.getShopId());
        supplierBo.setReqSupplierId(supplierParam.getId());
        supplierBo.setSupplierId(supplierParam.getId());
        supplierBo.setSupplierName(supplierParam.getSupplierName());
        supplierBo.setOperator(supplierParam.getModifier());
        warehouseInFacade.updateSupplier(supplierBo);
    }
}
