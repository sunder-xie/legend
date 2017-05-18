package com.tqmall.manage.web.sys;

import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.sys.RedisCacheController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zsy on 15/11/25.
 */
@Slf4j
@Controller
@RequestMapping("manage/sys/redis")
public class SysRedisController {

    @Autowired
    private RedisCacheController redisCacheController;

    /**
     * 后台刷新缓存接口
     *
     * @param cacheKey
     * @return
     */
    @RequestMapping(value = "reload",method = RequestMethod.GET)
    @ResponseBody
    public Result reload(@RequestParam(value = "code",required = false)String cacheKey){
        Result result = redisCacheController.reload(cacheKey);
        log.info("【管理后台刷新缓存】接口：{}{}，具体信息：{}","manage/sys/redis/reload?code=", cacheKey,LogUtils.funToString(cacheKey,result));
        return result;
    }

}
