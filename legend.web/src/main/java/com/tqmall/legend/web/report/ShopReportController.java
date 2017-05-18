package com.tqmall.legend.web.report;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.component.ReportConfig;
import com.tqmall.legend.biz.component.converter.DataShopConfigConverter;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/report")
@Controller
public class ShopReportController extends BaseController {
    @Autowired
    private ShopConfigureService shopConfigureService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping
    public String index(Model model){
        Long shopId = UserUtils.getShopIdForSession(request);
        model.addAttribute("shopId",shopId);
        return "report/index";
    }

    @RequestMapping("/detail")
    public String detail(Model model) {
        return "report/detail";
    }

    @RequestMapping("/amount")
    public String amount(Model model) {
        return "report/amount";
    }

    @RequestMapping("/statistics/detail")
    public String statisticsDetail(Model model) {
        return "report/statistics-detail";
    }

    @RequestMapping("/type/analysis")
    public String typeAnalysis(Model model) {
        return "report/type-analysis";
    }

    @RequestMapping("/layout-demo")
    public String layoutDemo(Model model) { return "report/layout-demo"; }

    @RequestMapping("/get_config/{confKey}")
    @ResponseBody
    public Result getShopConfigure(@PathVariable("confKey") String confKey, HttpServletRequest request){
        Long shopId = UserUtils.getShopIdForSession(request);
        return Result.wrapSuccessfulResult(shopConfigureService.getConfigureByJson(shopId,ShopConfigureTypeEnum.REPORTDISPLAY.getCode(),confKey, ReportConfig.class));
    }

    /**
     * 根据ConfKey储存报表显示列名的配置
     * @param confKey 设置的key 需要有一条shopId为0的初始数据
     * @param request
     * @return
     */
    @RequestMapping("/save_config/{confKey}")
    @ResponseBody
    public Result saveShopConfigure(@PathVariable("confKey") String confKey,String confValue, HttpServletRequest request){
        Long shopId = UserUtils.getShopIdForSession(request);
        ShopConfigure shopConfigure = new ShopConfigure();
        shopConfigure.setShopId(shopId);
        shopConfigure.setConfType(new Long(ShopConfigureTypeEnum.REPORTDISPLAY.getCode()));
        shopConfigure.setConfKey(confKey);
        shopConfigure.setConfValue(confValue);
        boolean flag = shopConfigureService.saveOrUpdateShopConfigure(shopId, ShopConfigureTypeEnum.REPORTDISPLAY.getCode(),confKey, new DataShopConfigConverter<ShopConfigure>(), shopConfigure);
        if(flag){
            return Result.wrapSuccessfulResult("更新成功");
        }else{
            return Result.wrapErrorResult("","文字更新失败");
        }
    }

}
