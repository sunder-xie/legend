package com.tqmall.magic.web.workshop;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.facade.magic.vo.LineProcessManagerVO;
import com.tqmall.legend.facade.magic.vo.PLineProcessVO;
import com.tqmall.legend.facade.magic.vo.PLineProcessPackVO;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.magic.object.param.workshop.LineProcessManagerParam;
import com.tqmall.magic.object.result.workshop.LineProcessManagerDTO;
import com.tqmall.magic.object.result.workshop.PLineProcessRelDTO;
import com.tqmall.magic.service.workshop.RpcLineProcessManagerService;
import com.tqmall.magic.service.workshop.RpcPLineProcessRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yanxinyin on 16/7/8.
 */
@Controller
@Slf4j
@RequestMapping("workshop/LineProcessManager")
public class LineProcessManagerController extends BaseController {

    @Autowired
    private RpcLineProcessManagerService rpcLineProcessManagerService;
    @Autowired
    private RpcPLineProcessRelService rpcPLineProcessRelService;

    @RequestMapping("/LineProcessManager-list")
    public String toLineProcessManagerList() {
        return "yqx/page/magic/workshop/LineProcessManager-list";
    }

    @RequestMapping("/LineProcessManager-edit")
    public String toLineProcessManagerAdd() {
        return "yqx/page/magic/workshop/LineProcessManager-edit";
    }

    /**
     * 批量插入更新
     *
     * @param managerListStr
     * @param request
     * @return
     */
    @RequestMapping(value = "/batchSaveOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Result saveUpdateLineProcessManager(String managerListStr, HttpServletRequest request) {
        try {
            /*WorkTimeVo workTimeVo = new WorkTimeVo();
            Date now = DateUtil.currentDate();
            if (now.after(workTimeVo.getOpenDate()) || now.before(workTimeVo.getCloseDate())) {
                return Result.wrapErrorResult("", "已开工，无法排班");
            }*/
            if (managerListStr == null || managerListStr.equals("")) {
                return Result.wrapErrorResult("", "请选择班组和员工！");
            }
            Long shopId = UserUtils.getShopIdForSession(request);
            List<LineProcessManagerVO> lineProcessManagerVOList = JSONUtil.jsonStr2List(managerListStr, LineProcessManagerVO.class);
            //确保数据的完整性
            if (!CollectionUtils.isEmpty(lineProcessManagerVOList)) {
                for (LineProcessManagerVO lineProcessManagerVO : lineProcessManagerVOList) {
                    if (lineProcessManagerVO.getLineProcessId() == null || lineProcessManagerVO.getTeamId() == null) {
                        return Result.wrapErrorResult("", "请选择班组和员工！");
                    }
                }
            }
            List<LineProcessManagerParam> lineProcessManagerParamList = Lists.newArrayList();
            for (LineProcessManagerVO lineProcessManagerVO : lineProcessManagerVOList) {
                LineProcessManagerParam lineProcessManagerParam = new LineProcessManagerParam();
                BeanUtils.copyProperties(lineProcessManagerVO, lineProcessManagerParam);
                lineProcessManagerParamList.add(lineProcessManagerParam);
            }
            log.info("[共享中心] DUBBO接口rpcLineProcessManagerService.saveOrUpdateList 批量更新保存生产线班组");
            Result<Integer> result = new Result<>();
            try {
                result = rpcLineProcessManagerService.saveOrUpdateList(shopId, lineProcessManagerParamList);
            } catch (Exception e) {
                log.error("保存或更新每日生产线人员失败", e);
                return Result.wrapErrorResult("", "保存或更新每日生产线人员失败");
            }
            log.info("[共享中心] DUBBO接口rpcLineProcessManagerService.saveOrUpdateList 批量更新保存生产线班组");
            if (!result.isSuccess()) {
                return Result.wrapErrorResult(result.getCode(), result.getMessage());
            }
            return Result.wrapSuccessfulResult("");
        } catch (Exception e) {
            log.error("[批量更新保存生产线班组信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.LINEPROCESSMANAGER_EDIT_ERROR.getCode(), LegendErrorCode.LINEPROCESSMANAGER_EDIT_ERROR.getErrorMessage());
        }
    }

    /**
     * 删除
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    @ResponseBody
    public Result deleteLineProcessManager(Long id, HttpServletRequest request) {
        try {
            log.info("[共享中心] DUBBO接口rpcLineProcessManagerService.deletePaintSpecies删除生产线班组,参数:{}", id);
            try {
                rpcLineProcessManagerService.deleteLineProcessManager(id);
            } catch (Exception e) {
                log.error("删除生产线班组失败", e);
                return Result.wrapErrorResult("", "删除生产线班组失败");
            }
            log.info("[共享中心] DUBBO接口rpcLineProcessManagerService.deletePaintSpecies删除生产线班组,返回:{}");
            return Result.wrapSuccessfulResult("");
        } catch (Exception e) {
            log.error("[修改生产线班组信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.LINEPROCESSMANAGER_DELETE_ERROR.getCode(), LegendErrorCode.LINEPROCESSMANAGER_DELETE_ERROR.getErrorMessage());
        }
    }

    /**
     * 根据生产线工序id查询生产线人员
     *
     * @param lineId
     * @return
     */
    @RequestMapping(value = "/managerList", method = RequestMethod.GET)
    @ResponseBody
    public Result<PLineProcessPackVO> addLineProcessManager(Long lineId) {
        try {
            PLineProcessPackVO result = new PLineProcessPackVO();
            List<PLineProcessVO> pLineProcessVOList;
            Long shopId = UserUtils.getShopIdForSession(request);
            Result<List<PLineProcessRelDTO>> pLineProcessRelDTOResult = null;
            try {
                pLineProcessRelDTOResult = rpcPLineProcessRelService.getPLineProcessRelList(shopId, lineId);
            } catch (Exception e) {
                log.error("根据生产线工序id查询生产线人员失败", e);
                return Result.wrapErrorResult("", "根据生产线工序id查询生产线人员失败");
            }
            List<PLineProcessRelDTO> pLineProcessRelDTOList = pLineProcessRelDTOResult.getData();
            if (!pLineProcessRelDTOResult.isSuccess() || pLineProcessRelDTOList.size() == 0) {
                return Result.wrapErrorResult(LegendErrorCode.SEARCH_PLINE_ERROR.getCode(), LegendErrorCode.SEARCH_PLINE_ERROR.getErrorMessage());
            }
            pLineProcessVOList = BdUtil.bo2do4List(pLineProcessRelDTOList, PLineProcessVO.class);
            List<Long> lineProcessIdList = Lists.transform(pLineProcessRelDTOList, new Function<PLineProcessRelDTO, Long>() {
                @Override
                public Long apply(PLineProcessRelDTO pLineProcessRelDTO) {
                    return pLineProcessRelDTO.getId();
                }
            });
            log.info("[共享中心] DUBBO接口rpcLineProcessManagerService.selectListByLineProcessIdLs查询生产线班组");
            Result<List<LineProcessManagerDTO>> lineProcessManagerDTOResult = null;
            try {
                lineProcessManagerDTOResult = rpcLineProcessManagerService.selectListByLineProcessIdLs(lineProcessIdList, shopId);
            } catch (Exception e) {
                log.error("查询生产线班组失败", e);
                return Result.wrapErrorResult("", "查询生产线班组失败");
            }
            log.info("[共享中心] DUBBO接口rpcLineProcessManagerService.selectListByLineProcessIdLs查询生产线班组");
            if (lineProcessManagerDTOResult.isSuccess() && null != lineProcessManagerDTOResult.getData()) {
                for (PLineProcessVO pLineProcessVO : pLineProcessVOList) {
                    List<LineProcessManagerVO> lineProcessManagerVOList = Lists.newArrayList();
                    for (LineProcessManagerDTO lineProcessManagerDTO : lineProcessManagerDTOResult.getData()) {
                        if (pLineProcessVO.getId().equals(lineProcessManagerDTO.getLineProcessId())) {
                            LineProcessManagerVO lineProcessManagerVO = new LineProcessManagerVO();
                            BeanUtils.copyProperties(lineProcessManagerDTO, lineProcessManagerVO);
                            lineProcessManagerVOList.add(lineProcessManagerVO);
                        }
                    }
                    pLineProcessVO.setLineProcessManagerVOList(lineProcessManagerVOList);
                }

            }
            result.setPLineProcessVOList(pLineProcessVOList);
            result.setLineName(pLineProcessRelDTOList.get(0).getLineName());
            result.setLineId(pLineProcessRelDTOList.get(0).getLineId());
            return Result.wrapSuccessfulResult(result);
        } catch (Exception e) {
            log.error("[查询生产线班组信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.LINEPROCESSMANAGER_LIST_ERROR.getCode(), LegendErrorCode.LINEPROCESSMANAGER_LIST_ERROR.getErrorMessage());
        }
    }

}
