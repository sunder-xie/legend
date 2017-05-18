package com.tqmall.legend.web.common;

import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.tqmallstall.domain.result.RegionDTO;
import com.tqmall.tqmallstall.service.common.AppRegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping(value = "/dict")
public class DictController {
    private static final Logger logger = LoggerFactory.getLogger(DictController.class);

    @Autowired
    private AppRegionService appRegionService;

	@RequestMapping(value = "getSubRegionByPid", method = RequestMethod.GET)
	@ResponseBody
	public Result getSubRegionByPid(int p_id) throws Exception {
		Result<List<RegionDTO>> result = appRegionService.subRegion(p_id);
        if(result != null && result.isSuccess() && result.getData() != null){
            return Result.wrapSuccessfulResult(result.getData());
        }else {
            logger.error("获取城市站信息失败,{}", LogUtils.funToString(p_id,result));
        }
        return result;
	}

	@RequestMapping(value = "getRegionListByIds", method = RequestMethod.GET)
	@ResponseBody
	public Result getRegionListByIds(String ids) throws Exception {
		Result<List<RegionDTO>> result = appRegionService.getRegionListByIds(ids);
        if(result != null && result.isSuccess() && result.getData() != null){
            return Result.wrapSuccessfulResult(result.getData());
        }else {
            logger.error("获取城市站信息失败,{}",LogUtils.funToString(ids,result));
        }
		return result;
	}

}
