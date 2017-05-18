package com.tqmall.tqmallstall;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.cube.shop.RpcGoodsPurchaseSaleStatistics;
import com.tqmall.cube.shop.param.report.goods.sale.GoodsCatTopNQueryParam;
import com.tqmall.cube.shop.param.report.goods.sale.GoodsQueryParam;
import com.tqmall.cube.shop.result.report.goods.GoodsPurchaseSaleCategoryTopDTO;
import com.tqmall.cube.shop.result.report.goods.GoodsPurchaseSaleDTO;
import com.tqmall.wheel.support.data.Page;
import com.tqmall.wheel.support.rpc.result.RpcResult;


/**
 * Created by wanghui on 12/8/15.
 */
public class DubboTest {
    public static void main(String[] args) {
        ApplicationConfig application = new ApplicationConfig();
        application.setName("legend");

        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://115.29.220.170:2182");

        ReferenceConfig<RpcGoodsPurchaseSaleStatistics> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(RpcGoodsPurchaseSaleStatistics.class);
        reference.setVersion("1.0.0.mj.dev");
//        GoodsCatQueryParam param = new GoodsCatQueryParam();
//        param.setMonth("2016-09");
//        param.setShopId(1l);
//        param.setPageNum(1);
//        param.setPageSize(10);
//        com.tqmall.wheel.support.rpc.result.RpcResult<com.tqmall.wheel.support.data.Page<GoodsPurchaseSaleCategoryDTO>> pageRpcResult = reference.get().statisticByGoodsCat(param);
        GoodsQueryParam param = new GoodsQueryParam();
        param.setShopId(1l);
        param.setPageNum(1);
        param.setPageSize(10);
        param.setMonth("2016-09");
        RpcResult<Page<GoodsPurchaseSaleDTO>> pageRpcResult = reference.get().statisticByGoods(param);

        GoodsCatTopNQueryParam goodsCatTopNQueryParam = new GoodsCatTopNQueryParam();
        goodsCatTopNQueryParam.setTopN(5);
        goodsCatTopNQueryParam.setShopId(1l);
        goodsCatTopNQueryParam.setMonth("2016-09");
        RpcResult<GoodsPurchaseSaleCategoryTopDTO> rpcResult = reference.get().statisticByGoodsCatTopN(goodsCatTopNQueryParam);
        System.out.println(ObjectUtils.objectToJSON(pageRpcResult));
    }
}
