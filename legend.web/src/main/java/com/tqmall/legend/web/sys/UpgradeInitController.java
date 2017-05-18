package com.tqmall.legend.web.sys;

import com.tqmall.legend.biz.attendance.AppAttendanceService;
import com.tqmall.legend.biz.component.ReportConfig;
import com.tqmall.legend.biz.component.converter.DataNoteShopConfigConverter;
import com.tqmall.legend.biz.component.converter.DataShopConfigConverter;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.NoteShopConfig;
import com.tqmall.legend.entity.shop.ShopConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by mokala on 11/5/15.
 */
@Controller
@Slf4j
@RequestMapping("/upgrade")
public class UpgradeInitController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private AppAttendanceService appAttendanceService;


    @RequestMapping()
    public String index(Model model) {
        model.addAttribute("moduleUrl", "settings");
        return "shop-upgrade-index";
    }

    //===============================测试用=====================================//
    @RequestMapping("testSaveShopConfig/{shopId}/{confType}")
    @ResponseBody
    public Result testSaveShopConfig(@PathVariable("shopId") Long shopId, @PathVariable("confType") Integer confType, @RequestParam(value = "firstValue", required = false) Integer firstValue, @RequestParam(value = "secondValue", required = false) Integer secondValue, @RequestParam(value = "invalidValue", required = false) Integer invalidValue) {
        NoteShopConfig shopConfigure = new NoteShopConfig();
        shopConfigure.setShopId(shopId);
        shopConfigure.setConfType(confType);
        shopConfigure.setFirstValue(firstValue);
        shopConfigure.setSecondValue(secondValue);
        shopConfigure.setInvalidValue(invalidValue);

        return Result.wrapSuccessfulResult(shopConfigureService.saveOrUpdateShopConfigure(shopId, confType, new DataNoteShopConfigConverter<NoteShopConfig>(), shopConfigure));
    }

    @RequestMapping("getShopConfigure/{shopId}/{confType}/{confKey}")
    @ResponseBody
    public Result getShopConfigure(@PathVariable("shopId") Long shopId, @PathVariable("confType") Integer confType, @PathVariable("confKey") String confKey) {
        return Result.wrapSuccessfulResult(shopConfigureService.getShopConfigure(shopId, confType, confKey, String.class));
    }

    @RequestMapping("getShopConfigure/{shopId}/{confType}")
    @ResponseBody
    public Result getShopConfigureByConfType(@PathVariable("shopId") Long shopId, @PathVariable("confType") Integer confType) {
        return Result.wrapSuccessfulResult(shopConfigureService.getShopConfigure(shopId, confType, new DataShopConfigConverter<List<ShopConfigure>>()));
    }

    @RequestMapping("getShopConfigureByJson/{shopId}/{confType}/{confKey}")
    @ResponseBody
    public Result getShopConfigureByJson(@PathVariable("shopId") Long shopId, @PathVariable("confType") Integer confType, @PathVariable("confKey") String confKey) {
        return Result.wrapSuccessfulResult(shopConfigureService.getConfigureByJson(shopId, confType, confKey, ReportConfig.class));
    }

    @RequestMapping("getAppAttend/{shopId}")
    @ResponseBody
    public Result getShopConfigureByJson(@PathVariable("shopId") Long shopId) {
        return Result.wrapSuccessfulResult(appAttendanceService.getSigTime(shopId));
    }

}
