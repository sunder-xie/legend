package com.tqmall.legend.biz.sell;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.base.BizJunitBase;
import com.tqmall.legend.entity.sell.SellOrder;
import com.tqmall.legend.enums.sell.ShopSellEnum;
import com.tqmall.legend.enums.shop.ShopLevelEnum;
import com.tqmall.legend.facade.sell.SellOrderPayFaced;
import com.tqmall.legend.pojo.sell.SellOrderSaveVO;
import com.tqmall.legend.pojo.sell.SellShopTypeVO;
import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xiangdong.qu on 17/2/28 15:32.
 */
@Slf4j
public class SellOrderPayFacedTest extends BizJunitBase {

    @Resource
    private SellOrderPayFaced sellOrderPayFaced;

    /**
     * 保存订单测试 当前电话号码门店已存在
     */
    @Test
    public void sellOrderSaveTest() {
        SellOrderSaveVO sellOrderSaveVO = new SellOrderSaveVO();
        String mobile = "15158116453";
        sellOrderSaveVO.setMobile(mobile);
        sellOrderSaveVO.setSellAmount(ShopSellEnum.BASE.getPrice());
        sellOrderSaveVO.setShopLevel(ShopLevelEnum.BASE.getValue());
        try {
            SellOrder sellOrder = sellOrderPayFaced.sellOrderSave(sellOrderSaveVO);
            Assert.assertTrue(null == sellOrder);
        } catch (IllegalArgumentException ill) {

        } catch (BizException biz) {
            Assert.assertTrue(StringUtils.equals(biz.getErrorMessage(), "当前手机号已开通门店:" + mobile));
        } catch (Exception e) {

        }
    }

    /**
     * 保存订单测试
     */
    @Test
    @Rollback
    public void sellOrderSaveTestOne() {
        SellOrderSaveVO sellOrderSaveVO = new SellOrderSaveVO();
        String mobile = "15158116453";
        sellOrderSaveVO.setMobile(mobile);
        sellOrderSaveVO.setSellAmount(ShopSellEnum.BASE.getPrice());
        sellOrderSaveVO.setShopLevel(ShopLevelEnum.BASE.getValue());
        try {
            SellOrder sellOrder = sellOrderPayFaced.sellOrderSave(sellOrderSaveVO);
            Assert.assertTrue(null != sellOrder);
            Assert.assertTrue(sellOrder.getBuyMobile().equals(mobile));
        } catch (IllegalArgumentException ill) {

        } catch (BizException biz) {

        } catch (Exception e) {

        }
    }
    /**
     * 售卖版本的原价和折扣后价格比率 0-1 验证; 是否展示折扣后金额验证
     */
    @Test
    public void getSellShopTypeListTest_01() {
        String mobile = "13255711271";
        List<SellShopTypeVO> sellShopTypeVOList = sellOrderPayFaced.getSellShopTypeList(mobile);
        Assert.assertNotNull(sellShopTypeVOList);
        for (SellShopTypeVO sellShopTypeVO : sellShopTypeVOList) {
            BigDecimal originalPrice = sellShopTypeVO.getOriginalPrice();
            BigDecimal price = sellShopTypeVO.getPrice();
            BigDecimal rate = price.divide(originalPrice,BigDecimal.ROUND_UP);
            log.info("originalPrice:{}, price:{},rate:{}", originalPrice, price, rate);
            Assert.assertTrue(rate.compareTo(BigDecimal.ZERO) >= 0);
            Assert.assertTrue(rate.compareTo(BigDecimal.ONE)<= 0);
            if (rate.compareTo(BigDecimal.ONE) !=0 ) {
                Assert.assertTrue(sellShopTypeVO.getIsShowDiscountPrice());
            }
        }
    }

    /**
     * 选择购买版本详情校验
     */
    @Test
    public void getSellShopTypeDetailTest_01 () {
        String mobile = "13255711271";
        Integer shopLevel = ShopSellEnum.BASE.getShopLevel();
        SellShopTypeVO sellShopTypeVO = sellOrderPayFaced.getSellShopTypeDetail(shopLevel, mobile);
        Assert.assertNotNull(sellShopTypeVO);
        BigDecimal originalPrice = sellShopTypeVO.getOriginalPrice();
        BigDecimal price = sellShopTypeVO.getPrice();
        BigDecimal rate = price.divide(originalPrice,BigDecimal.ROUND_UP);
        log.info("originalPrice:{}, price:{},rate:{}", originalPrice, price, rate);
        Assert.assertTrue(rate.compareTo(BigDecimal.ZERO) >= 0);
        Assert.assertTrue(rate.compareTo(BigDecimal.ONE)<= 0);
        Assert.assertTrue(sellShopTypeVO.getOriginalPrice().compareTo(ShopSellEnum.BASE.getPrice()) == 0);

    }
}
