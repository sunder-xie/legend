package com.tqmall.legend.web.portal;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 车主APP PC引导页面
 * Created by sky on 15/8/3.
 */
@Controller
@RequestMapping("portal/dandelion")
public class DandelionController extends PortalBaseController implements InitializingBean {

    @RequestMapping("download")
    public String download(Model model) {
        return "portal/dandelion/dandelion_download";
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
