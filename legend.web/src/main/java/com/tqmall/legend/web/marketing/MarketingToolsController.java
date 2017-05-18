package com.tqmall.legend.web.marketing;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by mokala on 11/23/15.
 */
@RequestMapping("marketing/tools")
@Controller
public class MarketingToolsController {

    @RequestMapping
    public String index(){
        return "marketing/tools/index";
    }

}
