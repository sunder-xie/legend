package com.tqmall.legend.web.setting;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 设置->功能配置->标准化服务
 * Created by wushuai on 16/12/5.
 */
@Slf4j
@Controller
@RequestMapping("shop/conf/normal-service")
public class ShopNormalServiceController extends BaseController {

    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;

    /**
     * 标准化服务设置主页
     *
     * @return
     */
    @RequestMapping("")
    public String index(Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<ShopServiceCate> returnList = shopServiceCateService.getNormalService(shopId);
        model.addAttribute("shopServiceCateList", returnList);
        return "yqx/page/setting/function/normal-service";
    }

    /**
     * 添加初始化标准服务
     *
     * @return
     */
    @Deprecated
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> save(@RequestBody final List<ShopServiceInfo> shopServiceInfoList) {
        return new ApiTemplate<Boolean>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected Boolean process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                log.info("[功能配置]保准化服务,门店id:{}，操作人id:{}", userInfo.getShopId(), userInfo.getUserId());
                shopServiceInfoFacade.updateNormalService(shopServiceInfoList,userInfo);
                return true;
            }
        }.execute();

    }
}
