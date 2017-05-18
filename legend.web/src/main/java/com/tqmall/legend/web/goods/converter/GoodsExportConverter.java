package com.tqmall.legend.web.goods.converter;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import com.tqmall.legend.web.goods.vo.GoodsExportVo;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2017/2/7.
 */
public class GoodsExportConverter {

    public static GoodsExportVo convert(SearchGoodsVo searchGoodsVo) {
        if (searchGoodsVo == null) {
            return null;
        }
        GoodsExportVo goodsExportVo = new GoodsExportVo();
        goodsExportVo.setGoodsSn(searchGoodsVo.getGoodsSn());
        goodsExportVo.setName(searchGoodsVo.getName());
        goodsExportVo.setFormat(searchGoodsVo.getFormat());
        goodsExportVo.setCarInfoStr(searchGoodsVo.getCarInfoStr());
        Double stock = searchGoodsVo.getStock();
        if (stock == null) {
            stock = 0.0;
        }
        goodsExportVo.setStock(stock);
        goodsExportVo.setPrice(searchGoodsVo.getPrice());
        Double inventoryPrice = searchGoodsVo.getInventoryPrice();
        if (inventoryPrice == null) {
            inventoryPrice = 0.0;
        }
        goodsExportVo.setInventoryPrice(inventoryPrice);
        goodsExportVo.setLastInPrice(searchGoodsVo.getLastInPrice());
        goodsExportVo.setLastInTimeStr(searchGoodsVo.getLastInTimeStr());
        goodsExportVo.setDepot(searchGoodsVo.getDepot());
        goodsExportVo.setGoodsBrand(searchGoodsVo.getGoodsBrand());
        goodsExportVo.setGoodsCat(searchGoodsVo.getGoodsCat());
        goodsExportVo.setMeasureUnit(searchGoodsVo.getMeasureUnit());
        return goodsExportVo;
    }

    public static List<GoodsExportVo> convertList(List<SearchGoodsVo> searchGoodsVoList) {
        if (CollectionUtils.isEmpty(searchGoodsVoList)) {
            return Collections.emptyList();
        }
        return Lists.transform(searchGoodsVoList, new Function<SearchGoodsVo, GoodsExportVo>() {
            @Override
            public GoodsExportVo apply(SearchGoodsVo searchGoodsVo) {
                return convert(searchGoodsVo);
            }
        });
    }
}
