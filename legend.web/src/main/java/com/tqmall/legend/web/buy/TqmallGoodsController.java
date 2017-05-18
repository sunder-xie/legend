package com.tqmall.legend.web.buy;

import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Mokala on 7/17/15.
 * 淘汽商品采购
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("shop/buy/tqmall_goods")
public class TqmallGoodsController extends BaseController {

    @HttpRequestLog
    @RequestMapping("index")
    public String index() {
        return "redirect:/shop/buy";
    }




}
