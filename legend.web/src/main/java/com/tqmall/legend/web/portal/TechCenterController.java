package com.tqmall.legend.web.portal;

import com.tqmall.legend.biz.tech.TechProductService;
import com.tqmall.legend.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 技术中心
 * Created by lixiao on 15-4-13.
 */
@Controller
@RequestMapping("portal/tech_center")
public class TechCenterController {

    @Autowired
    TechProductService techProductService;

    @RequestMapping("")
    public String index(Model model) {
        //获取首页产品
        model.addAttribute("techProductList", techProductService.getTechProductForIndex());
        model.addAttribute("portalNav", "tech_center");
        return "portal/tech_center/index";
    }


    /**
     * 技术中心-推荐书籍
     *
     * @return
     */
    @RequestMapping(value = "recommendproducts", method = RequestMethod.GET)
    @ResponseBody
    public Result getRecommendProducts() {
        return Result.wrapSuccessfulResult(techProductService.getTechProductForIndex());
    }


}
