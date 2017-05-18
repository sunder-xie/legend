package com.tqmall.legend.web.home;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.shop.ShopFacade;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zsy on 16/8/13.
 * 档口门店同意协议
 */
@Controller
@RequestMapping("shop/tqmall-agreement")
public class AgreementController extends BaseController{
    @Autowired
    private ShopFacade shopFacade;
    @Autowired
    private ShopService shopService;
    /**
     * 同意协议页面
     * @return
     */
    @RequestMapping
    public String agreement(Model model){
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        if(shop == null){
            return "login_shop";
        }
        Integer shopStatus = shop.getShopStatus();
        Integer shopLevel = shop.getLevel();
        model.addAttribute("shopStatus",shopStatus);
        model.addAttribute("shopLevel",shopLevel);
        return "yqx/page/home/tqmall-agreement";
    }

    /**
     * 同意协议接口
     * @return
     */
    @RequestMapping(value = "agree", method = RequestMethod.GET)
    @ResponseBody
    public Result agree() {
        Long shopId = UserUtils.getShopIdForSession(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Result<Boolean> agreeResult = shopFacade.agree(shopId, userInfo);
        if(agreeResult.isSuccess()){
            return Result.wrapSuccessfulResult(true);
        }else{
            return Result.wrapErrorResult(agreeResult.getCode(),agreeResult.getErrorMsg());
        }
    }
}
