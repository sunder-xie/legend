package com.tqmall.legend.web.sys;

import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.web.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Mokala on 7/3/15.
 */
@Controller
@RequestMapping("/sys/redis")
@Slf4j
public class RedisCacheController {

    @Autowired
    private CacheComponent cacheComponent;


    @RequestMapping("reload")
    @ResponseBody
    public Result reload(@RequestParam(value="code",required = false) String bizCode){
        Result result = Result.wrapSuccessfulResult(null);
        try {
            if(bizCode != null){
                this.cacheComponent.reload(bizCode);
            } else {
                this.cacheComponent.reloadAll();
            }
        } catch (Exception e) {
            log.error("reload redis cache Fail!!",e);
            result =  Result.wrapErrorResult("0","reload redis cache Fail!");
        }
        return result;
    }
}
