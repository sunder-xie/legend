package com.tqmall.legend.web.marketing;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by mokala on 11/23/15.
 * 客户分析模块
 */
@RequestMapping("/marketing/customer")
@Controller
public class CustomerAnaylsisController {

    @RequestMapping
    public String index(Model model){
        return "marketing/customer/index";
    }
}
