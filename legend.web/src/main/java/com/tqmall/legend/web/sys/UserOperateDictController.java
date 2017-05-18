package com.tqmall.legend.web.sys;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.facade.sys.UserOperateDictFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zsy on 07/5/10.
 * 用户行为分析，统计点击次数接口
 */
@Slf4j
@RequestMapping("shop/user/operate")
@Controller
public class UserOperateDictController {
    @Autowired
    private UserOperateDictFacade userOperateDictFacade;

    @HttpRequestLog
    @RequestMapping("count")
    @ResponseBody
    public Result<Boolean> reload(@RequestParam(value = "refer", required = true) String refer) {
        log.info("调用通用用户统计接口，传值refer为：{}", refer);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 删除缓存
     * @return
     */
    @RequestMapping("delKey")
    @ResponseBody
    public Result<Boolean> delKey() {
        userOperateDictFacade.delKey();
        return Result.wrapSuccessfulResult(true);
    }
}
