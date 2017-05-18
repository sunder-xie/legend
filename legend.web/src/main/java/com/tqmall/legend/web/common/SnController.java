package com.tqmall.legend.web.common;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.service.common.RpcSnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zwb on 14/12/16.
 */
@Controller
@RequestMapping(value = "/shop/sn")
public class SnController extends BaseController {
    @Autowired
    private RpcSnService rpcSnService;

    @RequestMapping("/generate")
    @ResponseBody
    public Result generateSn(@RequestParam(value = "type", required = true) String type) {
        Long shopId = UserUtils.getShopIdForSession(request);
        com.tqmall.core.common.entity.Result<String> result = rpcSnService.generateSn(type, shopId);
        if (result.isSuccess()) {
            String sn = result.getData();
            return Result.wrapSuccessfulResult(sn);
        }
        return Result.wrapErrorResult(result.getCode(), result.getMessage());
    }
}
