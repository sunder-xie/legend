package com.tqmall.legend.web.setting;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.shop.ShopFacade;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 设置->功能配置->委托设置
 * Created by wushuai on 16/12/5.
 */
@Slf4j
@Controller
@RequestMapping("shop/conf/share-join")
public class ShopShareJoinController extends BaseController {

    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    private ShopFacade shopFacade;

    /**
     * 门店加入委托
     *
     * @return
     */
    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTINGS.getModuleUrl());
        model.addAttribute("subModuleUrl", "share-join");
        Long shopId = UserUtils.getShopIdForSession(request);
        model.addAttribute("isJoin", shopFunFacade.isJoin(shopId));
        return "yqx/page/setting/function/share-join";
    }

    /**
     * 加入、退出委托接口
     *
     * @param joinStatus 0:不加入，1加入
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> save(@RequestParam(value = "joinStatus") final Integer joinStatus) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(joinStatus, "joinStatus不能为空");
            }

            @Override
            protected String process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                log.info("[功能配置]门店设置委托体系状态,门店id:{}，操作人id:{},joinStatus:{}", userInfo.getShopId(), userInfo.getUserId(), joinStatus);
                shopFacade.setShopJoinStatus(joinStatus, userInfo);
                return "操作成功";
            }
        }.execute();
    }
}
