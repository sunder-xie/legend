package com.tqmall.legend.web.shop;

import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.tqmallstall.domain.result.RegionDTO;
import com.tqmall.tqmallstall.service.common.AppRegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by lixiao on 14-11-14.
 */
@Controller
@RequestMapping("index/region")
public class RegionController {
    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private AppRegionService appRegionService;

    @RequestMapping("sub")
    @ResponseBody
    public Result subRegion(Integer pid) {
        Result<List<RegionDTO>> result = appRegionService.subRegion(pid);
        if(result != null && result.isSuccess() && result.getData() != null){
            return Result.wrapSuccessfulResult(result.getData());
        }else {
            logger.error("获取城市站信息失败,{}", LogUtils.funToString(pid,result));
        }
        return result;
    }

    @RequestMapping("getRegionListByIds")
    @ResponseBody
    public Result<List<RegionDTO>> getRegionListByIds(String ids) {
        Result<List<RegionDTO>> result = appRegionService.getRegionListByIds(ids);
        if(result != null && result.isSuccess() && result.getData() != null){
            return result;
        }else {
            logger.error("获取城市站信息失败,{}",LogUtils.funToString(ids,result));
        }
        return result;
    }


}
