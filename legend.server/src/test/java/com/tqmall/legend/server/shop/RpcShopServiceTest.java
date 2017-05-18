package com.tqmall.legend.server.shop;

import com.google.common.collect.Lists;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.shop.QryShopParam;
import com.tqmall.legend.object.result.shop.QryShopPageDTO;
import com.tqmall.legend.object.result.shop.ShopInfoDTO;
import com.tqmall.legend.service.shop.RpcShopService;
import com.tqmall.legend.server.BaseCaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/10/11.
 * 注：如果有事务操作，回滚，则需要添加注解：
 * transactionManagerLegend为mybatis-config.xml配置的名称
 * <tx:annotation-driven transaction-manager="transactionManagerLegend"/>
 * @Transactional("transactionManagerLegend")
 *
 * case上加@Rollback即可
 */
public class RpcShopServiceTest extends BaseCaseTest{

    @Autowired
    public RpcShopService rpcShopService;


    /**
     * 根据shopId获取门店信息
     * 异常返回
     */
    @Test
    public void findByShopIdNoParamTest() {
        Result<ShopInfoDTO> shopInfoDTOResult = rpcShopService.findByShopId(null);
        Assert.assertFalse(shopInfoDTOResult.isSuccess());
        Assert.assertEquals(shopInfoDTOResult.getMessage(), "店铺id必须传入.");
    }

    /**
     * 根据shopId获取门店信息
     * 正常返回
     */
    @Test
    public void findByShopIdTest() {
        Map<String, Object> shopQueryMap = getShopInfoMap();
        Long shopId = Long.valueOf(shopQueryMap.get("id").toString());
        Result<ShopInfoDTO> shopInfoDTOResult = rpcShopService.findByShopId(shopId);
        Assert.assertTrue(shopInfoDTOResult.isSuccess());
        ShopInfoDTO shopInfoDTO = shopInfoDTOResult.getData();
        Assert.assertNotNull(shopInfoDTO);
        Assert.assertEquals(shopInfoDTO.getShopId().toString(),shopQueryMap.get("id").toString());
        Assert.assertEquals(shopInfoDTO.getName(),shopQueryMap.get("name").toString());
        Assert.assertEquals(shopInfoDTO.getContact(),shopQueryMap.get("contact").toString());
        Assert.assertEquals(shopInfoDTO.getMobile(),shopQueryMap.get("mobile").toString());
        Assert.assertEquals(shopInfoDTO.getLevel().toString(),shopQueryMap.get("level").toString());
        Assert.assertEquals(shopInfoDTO.getUserGlobalId(),shopQueryMap.get("user_global_id").toString());
        Assert.assertEquals(shopInfoDTO.getShopStatus().toString(),shopQueryMap.get("shop_status").toString());
        Assert.assertEquals(shopInfoDTO.isTqmallVersion(),shopQueryMap.get("level").toString().equals("6"));
    }


    /**
     * 门店是否使用车间
     * 异常返回
     */
    @Test
    public void isUseWorkshopNoParamTest() {
        Result<Boolean> isUseWorkshopResult = rpcShopService.isUseWorkshop(null);
        Assert.assertFalse(isUseWorkshopResult.isSuccess());
        Assert.assertEquals(isUseWorkshopResult.getMessage(), "门店ID错误！");
    }

    /**
     * 门店是否使用车间
     * 正常返回
     */
    @Test
    public void isUseWorkshopTest() {
        Map<String, Object> shopQueryMap = getShopInfoMap();
        Long shopId = Long.valueOf(shopQueryMap.get("id").toString());
        Result<Boolean> isUseWorkshopResult = rpcShopService.isUseWorkshop(shopId);
        Assert.assertTrue(isUseWorkshopResult.isSuccess());
        String workshopStatus = shopQueryMap.get("workshop_status").toString();
        Assert.assertEquals(isUseWorkshopResult.getData(), workshopStatus.equals("1"));
    }

    @Test
    public void testGetShopPage(){
        QryShopParam qryShopParam = new QryShopParam();
        qryShopParam.setOffset(0);
        qryShopParam.setLimit(10);

        List<Long> citys = Lists.newArrayList();
        citys.add(383L);
        qryShopParam.setCityList(citys);

        Result<QryShopPageDTO> result = rpcShopService.getShopPage(qryShopParam);
        Assert.assertEquals(result.isSuccess(),"");
    }
}
