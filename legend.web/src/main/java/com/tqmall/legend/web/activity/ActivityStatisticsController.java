package com.tqmall.legend.web.activity;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zsy on 16/3/2.
 * 活动业绩统计
 *
 */
@Slf4j
@Controller
@RequestMapping("shop/activity/achievement")
public class ActivityStatisticsController extends BaseController{

    /**
     * 引流活动：活动业绩页面
     *
     * @return
     */
    @RequestMapping
    public String index(Model model){
        model.addAttribute("moduleUrl", ModuleUrlEnum.ACTIVITY.getModuleUrl());
        model.addAttribute("actTab", "achievement");
        return "activity/act_charts";
    }
}
