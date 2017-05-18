package com.tqmall.legend.server.common;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.service.common.RpcSnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by zsy on 16/11/21.
 */
@Service("rpcSnService")
public class RpcSnServiceImpl implements RpcSnService {
    @Autowired
    private SnFactory snFactory;

    @Override
    public Result<String> generateSn(final String type, final Long shopId) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(type, "类型不能为空");
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected String process() throws BizException {
                String typeUpperCase = type.toUpperCase();
                String sn;
                switch (typeUpperCase) {
                    case SnFactory.ORDER:
                        //工单
                        sn = snFactory.generateSn(SnFactory.ORDER_SN_INCREMENT, shopId, SnFactory.ORDER);
                        break;
                    case SnFactory.APPOINT:
                        //预约单
                        sn = snFactory.generateSn(SnFactory.APPOINT_SN_INCREMENT, shopId, SnFactory.APPOINT);
                        break;
                    case SnFactory.GOODS:
                        //配件
                        sn = snFactory.generateSn(SnFactory.GOODS_SN_INCREMENT, shopId, SnFactory.GOODS);
                        break;
                    case SnFactory.SERVICE:
                        //服务
                        sn = snFactory.generateSn(SnFactory.SERVICE_SN_INCREMENT, shopId, SnFactory.SERVICE);
                        break;
                    case SnFactory.FEE:
                        //附加费用
                        sn = snFactory.generateSn(SnFactory.FEE_SN_INCREMENT, shopId, SnFactory.FEE);
                        break;
                    case SnFactory.SUPPLIER:
                        //供应商
                        sn = snFactory.generateSn(SnFactory.SUPPLIER_SN_INCREMENT, shopId, SnFactory.SUPPLIER);
                        break;
                    default:
                        sn = "";
                        break;
                }
                return sn;
            }
        }.execute();
    }
}
