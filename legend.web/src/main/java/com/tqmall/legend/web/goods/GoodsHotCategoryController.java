package com.tqmall.legend.web.goods;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.goods.GoodsHotCategoryFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zsy on 16/5/10.
 * 配件热门类别
 */
@Slf4j
@Controller
@RequestMapping("shop/goods/hot/category")
public class GoodsHotCategoryController {

    @Autowired
    GoodsHotCategoryFacade goodsHotCategoryFacade;

    /**
     * 获取配件热门分类列表
     *
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Object goodsHotCategoryList(@RequestParam(value = "cateName", required = false) String cateName) {
        return Result.wrapSuccessfulResult(goodsHotCategoryFacade.selectByCatNameLike(cateName));
    }
}
