package com.tqmall.legend.server.shop;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.entity.shop.Supplier;
import com.tqmall.legend.object.result.supplier.SupplierDTO;
import com.tqmall.legend.server.BaseCaseTest;
import com.tqmall.legend.service.supplier.RpcSupplierService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by sven on 2017/2/17.
 */
public class RpcSupplierServiceTest extends BaseCaseTest {
    @Autowired
    private RpcSupplierService rpcSupplierService;

    @Test
    public void selectByShopId() {
        Result<List<SupplierDTO>> re = rpcSupplierService.getSupplierList(1L, "测试");
        Assert.notEmpty(re.getData());
    }
}
