package com.tqmall.legend.web.setting;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopDTO;
import com.tqmall.dandelion.wechat.client.wechat.shop.WeChatShopService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
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
 * 设置->功能配置->微信评论设置
 * Created by pituo on 17/02/09.
 */
@Slf4j
@Controller
@RequestMapping("shop/conf/wechat-evaluation")
public class ShopWechatEvalutionController extends BaseController{

    @Autowired
    private WeChatShopService weChatShopService;
    @Autowired
    private ShopService shopService;

    /**
     * 消息推送设置页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTINGS.getModuleUrl());
        model.addAttribute("subModuleUrl", "msg-push");
        Long shopId = UserUtils.getShopIdForSession(request);
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        Result<ShopDTO> result=weChatShopService.selectShopByUcShopId(userGlobalId);
        if(result.isSuccess()&&result.getData()!=null) {
            Integer evaluationSwitch = result.getData().getEvaluationSwitch();
            model.addAttribute("evaluationSwitch", evaluationSwitch!=0);
        }else{
            model.addAttribute("evaluationSwitch", false);
        }
        return "yqx/page/setting/function/wechat_evaluation";
    }

    /**
     * 保存消息推送设置
     *
     * @param evaluationSwitch
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> save(@RequestParam(value = "evaluationSwitch") final Integer evaluationSwitch) {
        return new ApiTemplate<Boolean>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(evaluationSwitch,"evaluationSwitch不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Long userGlobalId = shopService.getUserGlobalId(shopId);
                //Result<ShopDTO> result=weChatShopService.selectShopByUcShopId(userGlobalId);
                log.info("[功能配置]微信评论设置,门店id:{}，confValue:{}", shopId, evaluationSwitch);
                ShopDTO shopDTO=new ShopDTO();
                shopDTO.setUcShopId(userGlobalId);
                shopDTO.setEvaluationSwitch(evaluationSwitch);
                Result<String> result1= weChatShopService.updateShopForMega(shopDTO);
                return result1.isSuccess();
            }
        }.execute();
    }
}
