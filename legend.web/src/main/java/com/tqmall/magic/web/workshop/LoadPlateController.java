package com.tqmall.magic.web.workshop;

import com.tqmall.common.UserInfo;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.enums.ProductionLineTypeEnum;
import com.tqmall.legend.facade.magic.LaodPlateFacade;
import com.tqmall.legend.facade.magic.WorkTimeFacade;
import com.tqmall.legend.facade.magic.vo.LoadVO;
import com.tqmall.legend.facade.magic.vo.PlateVO;
import com.tqmall.legend.facade.magic.vo.ProductionLineVO;
import com.tqmall.legend.facade.magic.vo.WorkTimeVo;
import com.tqmall.magic.object.result.workshop.ProductionLineDTO;
import com.tqmall.magic.service.workshop.RpcProductionLineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanxinyin on 16/7/8.
 */
@Controller
@Slf4j
@RequestMapping("workshop/loadplate")
public class LoadPlateController {

    @Autowired
    public LaodPlateFacade laodPlateFacade;

    @Autowired
    private RpcProductionLineService rpcProductionLineService;

    @Autowired
    private WorkTimeFacade workTimeFacade;

    @RequestMapping("/worktime")
    @ResponseBody
    public Result<WorkTimeVo> getWorkTime(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        WorkTimeVo workTimeVo = workTimeFacade.initWorkTime(shopId);
        return Result.wrapSuccessfulResult(workTimeVo);
    }


    @RequestMapping("/loadplate-show")
    public String toLoadPlatePage(HttpServletRequest request, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        //getMessage(shopId, request);
        List<ProductionLineVO> productionLines = null;
        List<ProductionLineVO> productionLineVOList = new ArrayList<>();
        try {
            productionLines = laodPlateFacade.getProductionLine(shopId);
        } catch (Exception e) {
            log.error("[负载看板] 获取生产线信息失败！e={}", e);
        }
        // add:钣喷四期增加小钣金事故线。入口先关闭
        if (!CollectionUtils.isEmpty(productionLines)) {
            for (ProductionLineVO productionLineVO : productionLines) {
                if (productionLineVO.getType() != null && !productionLineVO.getType().equals(ProductionLineTypeEnum.XSGX.getType())) {
                    productionLineVOList.add(productionLineVO);
                }
            }
        }
        model.addAttribute("productionLines", productionLineVOList);
        return "yqx/page/magic/workshop/loadplate";
    }

    @RequestMapping("/loadplate-load")
    @ResponseBody
    public Result<LoadVO> getMessage(Long shopId, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        shopId = userInfo.getShopId();
        Result<LoadVO> result = laodPlateFacade.getMessage(shopId);
        return Result.wrapSuccessfulResult(result.getData());
    }

    @RequestMapping("/loadplate-line")
    @ResponseBody
    public Result<PlateVO> getLoadplateLine(Long lineId, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        PlateVO plateVO = new PlateVO();
        Result<ProductionLineDTO> p = null;
        try {
            p = rpcProductionLineService.selectProductionLine(lineId);
        } catch (Exception e) {
            log.error("查询生产线失败", e);
            return Result.wrapErrorResult("", "查询生产线失败");
        }
        if (p.isSuccess() && null != p.getData()) {
            ProductionLineDTO productionLineDTO = p.getData();
            try {
                laodPlateFacade.getPlateVO(plateVO, shopId, lineId, Long.valueOf(productionLineDTO.getType().toString()));
            } catch (Exception e) {
                log.error("[负载看板] 加载生产线模板失败！e={}", e);
                return Result.wrapErrorResult("", "加载模板生产线失败！");
            }
            plateVO.setType(Long.valueOf(productionLineDTO.getType()));
            plateVO.setLineName(productionLineDTO.getName());
            if (plateVO.getLastTime() != null) {
                plateVO.setLastTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(plateVO.getLastTime()));
            }
            if (plateVO.getPlanTime() != null) {
                plateVO.setPlanTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(plateVO.getPlanTime()));
            }
        }

        return Result.wrapSuccessfulResult(plateVO);
    }

}