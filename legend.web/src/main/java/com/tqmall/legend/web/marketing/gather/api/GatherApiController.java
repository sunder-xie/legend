package com.tqmall.legend.web.marketing.gather.api;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.facade.customer.vo.AllotUserVo;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zsy on 16/12/16.
 * 集客方案公共接口
 */
@Controller
@RequestMapping("marketing/gather/api")
public class GatherApiController extends BaseController {
    @Autowired
    private CustomerUserRelFacade customerUserRelFacade;

    /**
     * 获取员工列表
     *
     * @param choseUserIds 已选中的员工 , 有值，则过滤此员工
     * @param isAllot      是否需要过滤未分配的员工，false:不需要过滤 true:需要过滤，默认false
     * @return
     */
    @RequestMapping(value = "get-manager", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<AllotUserVo>> getManager(@RequestParam(value = "isAllot", defaultValue = "false", required = false) Boolean isAllot,
                             @RequestParam(value = "choseUserIds", required = false) List<Long> choseUserIds) {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<AllotUserVo> allotUserVoList = customerUserRelFacade.getAllotUserList(shopId, isAllot, choseUserIds);
        return Result.wrapSuccessfulResult(allotUserVoList);
    }
}
