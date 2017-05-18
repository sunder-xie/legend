package com.tqmall.legend.web.reception;

import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 接车维修首页
 * Created by lixiao on 16/4/7.
 */
@Slf4j
@Controller
@RequestMapping("shop/reception")
public class ReceptionController {

    @HttpRequestLog
    @RequestMapping("")
    public String home(Model model){
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        return "yqx/page/reception";
    }
}
