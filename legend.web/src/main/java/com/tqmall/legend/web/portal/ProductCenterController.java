package com.tqmall.legend.web.portal;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 产品中心
 * Created by nawks on 3/12/15.
 */
@Controller
@RequestMapping("portal/product")
public class ProductCenterController extends PortalBaseController implements InitializingBean{
    @RequestMapping
    public String index(Model model){
        model.addAttribute("portalNav","product");
        return "portal/product/index";
    }

    @RequestMapping("info")
    public String info(Model model,@RequestParam(value="type",required = false)String type){
        model.addAttribute("portalNav", "product");
        if(type != null && (type.equals("join") || type.equals("direct")) ) {
            model.addAttribute("productType", type);
        }
        return "portal/product/info";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setPageName("product");
    }
}
