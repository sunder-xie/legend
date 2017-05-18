package com.tqmall.legend.web.home;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.finance.AvoidLoginService;
import com.tqmall.legend.entity.finance.LoginStallVo;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sven on 16/8/11.
 */
@RequestMapping("home/avoid")
@Controller
@Slf4j
public class AvoidLoginController extends BaseController {
    @Resource
    private AvoidLoginService avoidLoginService;

    /**
     * 免登录到汽配管家主页
     *
     * @param response
     */
    @HttpRequestLog
    @RequestMapping("epc/index")
    public void avoidLoginEpcIndex(HttpServletResponse response) {
        Long shopId = UserUtils.getShopIdForSession(request);
        String url = avoidLoginService.avoidLoginEpc(shopId, null);
        if (url != null) {
            try {
                response.sendRedirect(url);
            } catch (Exception e) {
                log.error("从云修免登陆到汽配失败,门店没有对应的登陆信息后，门店id:{},异常信息:{]", shopId, e);
            }
        }
    }

    /**
     * 免登录到电商主页
     *
     * @param response
     */
    @RequestMapping("tqmall/index")
    public void avoidLoginTqmallIndex(HttpServletResponse response) {
        Long shopId = UserUtils.getShopIdForSession(request);
        LoginStallVo loginStallVo = new LoginStallVo();
        loginStallVo.setOptType(LoginStallVo.OPT_YUNXIU_LOGIN);
        try {
            String url = avoidLoginService.avoidLoginStall(shopId, loginStallVo);
            if (url != null) {
                response.sendRedirect(url);
            }
        } catch (Exception e) {
            log.error("从云修免登陆到电商失败,门店没有对应的登陆信息后，门店id:{},异常信息:{]", shopId, e);
        }
    }

}
