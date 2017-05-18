package com.tqmall.legend.web.buy;

/**
 * 淘汽采购-缺件配件
 */

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 缺货物料
 */
@Controller
@RequestMapping("shop/buy/short_goods")
public class ShortGoodsController extends BaseController{

    @RequestMapping("")
    public String index(Model model){
        model.addAttribute("moduleUrl", "buy");
        model.addAttribute("buyTab","short_goods");
        Long shopId = UserUtils.getShopIdForSession(request);
        return "yqx/page/buy/shortgoods/goods-list";
    }
}
