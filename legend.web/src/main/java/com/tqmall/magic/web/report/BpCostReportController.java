package com.tqmall.magic.web.report;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.magic.result.AccessiesCostDTO;
import com.tqmall.cube.magic.result.GoodCostDTO;
import com.tqmall.cube.magic.result.PaintCostDTO;
import com.tqmall.cube.magic.result.ProfitLostBalanceDTO;
import com.tqmall.cube.magic.service.RpcBpCostReportService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by shulin on 2017/1/18.
 */
@Controller
@RequestMapping("/bp/cost/")
@Slf4j
public class BpCostReportController extends BaseController {
    @Autowired
    private RpcBpCostReportService rpcBpCostReportService;

    /**
     * 油漆成本
     *
     * @param searchMonth
     * @return
     */
    @RequestMapping("/paintCost")
    @ResponseBody
    public Result<List<PaintCostDTO>> getPaintCostReport(String searchMonth) {
        if (StringUtil.isStringEmpty(searchMonth)) {
            return Result.wrapErrorResult("", "查询日期不能为空");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<List<PaintCostDTO>> result = null;
        try {
            result = rpcBpCostReportService.getPaintCostReport(shopId, searchMonth);
        } catch (Exception e) {
            log.error("[钣喷月报-油漆成本] 调用DUBBO接口失败！", e);
            return Result.wrapErrorResult("", "系统异常");
        }

        return result;
    }

    @RequestMapping("/accessiesCost")
    @ResponseBody
    public Result getAccessiesCostReport(String searchMonth) {
        if (StringUtil.isStringEmpty(searchMonth)) {
            return Result.wrapErrorResult("", "查询日期不能为空");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<List<AccessiesCostDTO>> result = null;
        try {
            result = rpcBpCostReportService.getAccessiesCostReport(shopId, searchMonth);
        } catch (Exception e) {
            log.error("[钣喷月报-辅料成本] 调用DUBBO接口失败！", e);
            return Result.wrapErrorResult("", "系统异常");
        }
        return result;
    }


    @RequestMapping("/nonGoodCost")
    @ResponseBody
    public Result getNonGoodCostReport(String searchMonth) {
        if (StringUtil.isStringEmpty(searchMonth)) {
            return Result.wrapErrorResult("", "查询日期不能为空");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<List<GoodCostDTO>> result = null;
        try {
            result = rpcBpCostReportService.getNonGoodCostReport(shopId, searchMonth);
        } catch (Exception e) {
            log.error("[钣喷月报-非物料成本] 调用DUBBO接口失败！", e);
            return Result.wrapErrorResult("", "系统异常");
        }
        return result;
    }

    /**
     * 盈亏平衡报表
     * @param searchMonth
     * @return
     */
    @RequestMapping("/getProfitLostBalance")
    @ResponseBody
    public Result<ProfitLostBalanceDTO> getProfitLostBalance(String searchMonth){
        if (StringUtil.isStringEmpty(searchMonth)) {
            return Result.wrapErrorResult("", "查询日期不能为空");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<ProfitLostBalanceDTO> result = null;
        try {
            result = rpcBpCostReportService.getProfitLostBalance(shopId, searchMonth);
        } catch (Exception e) {
            log.error("[钣喷月报-盈亏平衡] 调用DUBBO接口失败！", e);
            return Result.wrapErrorResult("", "系统异常");
        }
        return result;
    }

}
