package com.tqmall.legend.web.marketing.gather;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by tanghao on 16/12/16.
 * 设置奖励规则页面
 */
@Controller
@RequestMapping("/marketing/gather/rule")
public class GatherPerfConfigController {
    @RequestMapping
    public String index(Model model) {
        model.addAttribute("subModule","gather-rule");
        return "yqx/page/marketing/gather/gather_rule";
    }

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "marketing";
    }
}
