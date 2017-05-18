package com.tqmall.legend.web.test;

import com.tqmall.legend.web.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ch on 16/3/21.
 */
@Controller
@RequestMapping("yqx/page/test")
public class Test extends BaseController {
    @RequestMapping("layout-test")
    public String layoutTest(){
        return "yqx/page/test/layout-test";
    }
}
