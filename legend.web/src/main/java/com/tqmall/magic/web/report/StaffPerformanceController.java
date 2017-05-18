package com.tqmall.magic.web.report;

import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.magic.result.MechanicPerformanceDTO;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.magic.result.ProcessTimePerformanceDTO;
import com.tqmall.cube.magic.result.ServiceSaPerformanceVO;
import com.tqmall.cube.magic.service.RpcStaffPerformanceService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by dingbao on 16/12/13.
 */
@Controller
@RequestMapping("staffPerformance")
@Slf4j
public class StaffPerformanceController extends BaseController {

    @Autowired
    private RpcStaffPerformanceService rpcStaffPerformanceService;

    /**
     * 获取服务顾问绩效信息
     * @param month
     * @return
     */
    @RequestMapping("/serviceSaPerformance")
    @ResponseBody
    public Result<ServiceSaPerformanceVO> getServiceSaPerformanceInfo(HttpServletRequest request, String month) {
        log.info("获取服务顾问绩效信息.参数：searchTime={}",month);
        Long shopId = UserUtils.getShopIdForSession(request);
        if (StringUtil.isStringEmpty(month)){
            log.error("参数不能为空。searchTime={}",month);
            return Result.wrapErrorResult("","参数不能为空");
        }
        if (shopId == null || shopId < 1){
            log.error("获取门店id错误。shopId={}",shopId);
            return Result.wrapErrorResult("","获取门店id错误");
        }
        Result<ServiceSaPerformanceVO> result = null;
        try {
            result = rpcStaffPerformanceService.getServiceSaPerformanceInfo(shopId, month);
        } catch (Exception e) {
            log.error("调用远程获取服务顾问绩效信息失败", e);
            return Result.wrapErrorResult("","调用远程获取服务顾问绩效信息失败");
        }
        return result;
    }

    /**
     * 获取技工工作绩效信息
     * @param month
     * @return
     */
    @RequestMapping("/mechanicPerformance")
    @ResponseBody
    public Result<MechanicPerformanceDTO> getMechanicPerformanceInfo(HttpServletRequest request, String month){
        log.info("获取技工工作绩效信息.参数：searchTime={}",month);
        Long shopId = UserUtils.getShopIdForSession(request);
        if (StringUtil.isStringEmpty(month)){
            log.error("参数不能为空。searchTime={}",month);
            return Result.wrapErrorResult("","参数不能为空");
        }
        if (shopId == null || shopId < 1){
            log.error("获取门店id错误。shopId={}",shopId);
            return Result.wrapErrorResult("","获取门店id错误");
        }
        Result<MechanicPerformanceDTO> result = null;
        try {
            result = rpcStaffPerformanceService.getMechanicPerformanceInfo(shopId, month);
        } catch (Exception e) {
            log.error("调用远程获取技工工作绩效信息失败",e);
            return Result.wrapErrorResult("","调用远程获取技工工作绩效信息失败");
        }
        return result;
    }

    /**
     * 获取钣喷员工绩效-工序时间绩效
     *
     * @param month
     * @return
     */
    @RequestMapping("/processTimePerformance")
    @ResponseBody
    public Result<List<ProcessTimePerformanceDTO>> getProcessTimePerformance(String month) {
        if (StringUtil.isStringEmpty(month)) {
            return Result.wrapErrorResult("", "参数不能为空");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<List<ProcessTimePerformanceDTO>> result = null;
        try {
            log.info("[钣喷-工序时间绩效]  start，shopId={},currentDate={}", shopId, month);
            result = rpcStaffPerformanceService.getProcessTimePerformance(shopId, month);
            log.info("[钣喷-工序时间绩效]  end，result={},Msg={}", result.isSuccess(), result.getMessage());
        } catch (Exception e) {
            log.error("[钣喷-工序时间绩效] 获取失败！shopId=" + shopId + ",currentDate=" + month, e);
            return Result.wrapErrorResult("", "时间工序绩效获取失败");
        }
        return result;
    }


}
