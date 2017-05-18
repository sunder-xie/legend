package com.tqmall.legend.web.shop;

import com.tqmall.common.UserInfo;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.facade.shop.GrayReleaseFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 灰度发布
 * Created by sven on 2017/1/16.
 */
@Controller
@Slf4j
@RequestMapping("shop/gray/release")
public class GrayReleaseController extends BaseController {
    @Autowired
    private GrayReleaseFacade grayReleaseFacade;

    @RequestMapping("switch-version")
    @ResponseBody
    public Result<Boolean> switchVersion(@RequestParam("version") String version,
                                         @RequestParam("moduleKey") String moduleKey) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        grayReleaseFacade.switchVersion(version, moduleKey, userInfo);
        return Result.wrapSuccessfulResult(true);
    }
}
