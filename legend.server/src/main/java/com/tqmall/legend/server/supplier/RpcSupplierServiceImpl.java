package com.tqmall.legend.server.supplier;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.shop.SupplierService;
import com.tqmall.legend.entity.shop.Supplier;
import com.tqmall.legend.enums.warehouse.PayMethodEnum;
import com.tqmall.legend.object.result.supplier.SupplierDTO;
import com.tqmall.legend.service.supplier.RpcSupplierService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/11/22.
 */
@Service("rpcSupplierService")
public class RpcSupplierServiceImpl implements RpcSupplierService {
    @Autowired
    private SupplierService supplierService;

    /**
     * 根据门店id获取供应商列表
     *
     * @param shopId
     * @param nameLike 供应商名称模糊查询，可不传
     * @return
     */
    @Override
    public Result<List<SupplierDTO>> getSupplierList(final Long shopId, final String nameLike) {
        return new ApiTemplate<List<SupplierDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected List<SupplierDTO> process() throws BizException {
                List<SupplierDTO> supplierDTOList = Lists.newArrayList();
                Map<String, Object> searchMap = Maps.newHashMap();
                searchMap.put("shopId", shopId);
                if (StringUtils.isNotBlank(nameLike)) {
                    searchMap.put("keyword", nameLike);
                }
                List<Supplier> supplierList = supplierService.select(searchMap);
                if (CollectionUtils.isEmpty(supplierList)) {
                    return supplierDTOList;
                }
                for (Supplier supplier : supplierList) {
                    SupplierDTO supplierDTO = new SupplierDTO();
                    BeanUtils.copyProperties(supplier, supplierDTO);
                    //设置付款方式中文名称
                    String payMode = PayMethodEnum.getMessageByCode(supplier.getPayMethod());
                    if(StringUtils.isNotBlank(payMode)){
                        supplierDTO.setPayMode(payMode);
                    }
                    supplierDTOList.add(supplierDTO);
                }
                return supplierDTOList;
            }
        }.execute();
    }
}
