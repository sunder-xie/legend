package com.tqmall.legend.web.portal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 招聘
 * Created by zsy on 2015/3/20.
 */
@Controller
@RequestMapping("portal/insurance")
public class InsuranceController {

    @RequestMapping("tqmall-insurance")
    public String index(Model model) {
        model.addAttribute("portalNav", "insurance");
        return "portal/insurance/tqmall-insurance";
    }
}
