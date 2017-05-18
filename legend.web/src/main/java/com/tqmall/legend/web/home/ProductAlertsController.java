package com.tqmall.legend.web.home;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.article.IProductAlertService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.article.ProductAlert;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 首页-云修产品广告列表
 */
@Controller
@RequestMapping("home")
public class ProductAlertsController extends BaseController {

    @Autowired
    private IProductAlertService productAlertService;

    /**
     * 产品咨询列表页面
     */
    @RequestMapping(value = "products/products-list", method = RequestMethod.GET)
    public String productAlertPage(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.PRODUCTS.getModuleUrl());
        return "/yqx/page/home/products/products-list";
    }


    /**
     * TODO redis
     * 获取产品咨询列表(排位号升序1....)
     *
     * @param pageable
     * @return json
     */
    @RequestMapping(value = "productalert/list", method = RequestMethod.GET)
    @ResponseBody
    public Result productAlertListJSON(
            @PageableDefault(page = 1, value = 10, sort = "item_sort", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        return Result.wrapSuccessfulResult(productAlertService.getPage(pageable, new HashMap<String, Object>()));
    }


    /**
     * 产品咨询详情页
     *
     * @param itemId 产品资讯ID
     */
    @RequestMapping(value = "products/products-detail", method = RequestMethod.GET)
    public String productAlertDetail(Model model, @RequestParam(value = "itemid", required = true) Long itemId) {
        Optional<ProductAlert> productalertOptional = productAlertService.get(itemId);
        if (productalertOptional.isPresent()) {
            model.addAttribute("product", productalertOptional.get());
        }
        model.addAttribute("moduleUrl", ModuleUrlEnum.PRODUCTS.getModuleUrl());
        return "/yqx/page/home/products/products-detail";
    }


    /**
     * TODO redis
     *
     * 首页云修产品咨询 TOP3
     *
     * @return json
     */
    @RequestMapping(value = "productalert/top", method = RequestMethod.GET)
    @ResponseBody
    public Result productAlertTopJSON(HttpServletRequest request) {
        return Result.wrapSuccessfulResult(productAlertService.getTop3());
    }

}
