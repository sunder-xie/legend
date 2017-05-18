package com.tqmall.legend.web.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.maxsn.MaxSnService;
import com.tqmall.legend.common.LegendError;

/**
 * Created by guangxue on 14/11/4.
 */
@Controller
@RequestMapping(value = "/user/sn")
public class MaxSnController extends BaseController{
    @Autowired
    private MaxSnService maxSnService;

    @RequestMapping("/gen")
    @ResponseBody
    public Result generateSn(@RequestParam("snType") String snType) {
        Long shopId = (Long)UserUtils.getShopIdForSession(request);
        if (null == shopId)
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        return Result.wrapSuccessfulResult(maxSnService.getMaxSn(shopId, snType));
    }
}
