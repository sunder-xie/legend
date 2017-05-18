package com.tqmall.legend.web.portal;

import com.tqmall.common.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * 招商合作 Created by nawks on 3/12/15.
 */
@Controller
@RequestMapping("portal/invest")
public class InvestController extends PortalBaseController {

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("portalNav", "invest");
        model.addAttribute("join_phone", Constants.JOIN_PHONE);
        return "portal/invest/index";
    }
}
