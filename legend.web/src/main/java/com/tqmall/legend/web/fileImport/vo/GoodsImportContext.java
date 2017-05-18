package com.tqmall.legend.web.fileImport.vo;

import com.google.common.collect.Maps;
import com.tqmall.itemcenter.object.result.goods.GoodsBrandDTO;
import com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.entity.goods.Goods;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by twg on 16/12/10.
 */
@Getter
@Setter
public class GoodsImportContext extends CommonFileImportContext<Goods> {
    /*需要批量新加的配件分类信息*/
    private Map<String, GoodsCategoryDTO> goodsCategoryHashMap = Maps.newHashMap();
    /*需要批量新增的配件品牌信息*/
    private Map<String, GoodsBrandDTO> goodsBrandMap = Maps.newHashMap();
}
