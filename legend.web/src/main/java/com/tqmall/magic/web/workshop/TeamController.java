package com.tqmall.magic.web.workshop;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.facade.magic.ShopManagerFacade;
import com.tqmall.legend.facade.magic.TeamFacade;
import com.tqmall.legend.facade.magic.vo.ShopManagerExtVO;
import com.tqmall.legend.facade.magic.vo.TeamVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 班组
 * Created by shulin on 16/7/1.
 */
@Controller
@Slf4j
@RequestMapping("workshop/team")
public class TeamController {
    @Autowired
    private TeamFacade teamFacade;
    @Autowired
    private ShopManagerFacade shopManagerFacade;

    @RequestMapping("/addpage")
    public String toAddTeamPage() {
        return "yqx/page/magic/workshop/team_add";
    }


    @RequestMapping("/listpage")
    public String toTeamListPage(Model model) {
        model.addAttribute("leftNav", "team");
        return "yqx/page/magic/workshop/team_list";
    }

    @RequestMapping("/editpage")
    public String toEditPage() {
        return "yqx/page/magic/workshop/team_edit";
    }

    /**
     * 添加班组信息，可能存在同时添加员工信息的情况
     *
     * @param teamVO
     * @param request
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addTeam(@RequestBody TeamVO teamVO, HttpServletRequest request) {
        if (teamVO.getName() == null || "".equals(teamVO.getName())) {
            return Result.wrapErrorResult("", "班组名称不能为空！");
        }
        try {
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long currentUser = userInfo.getUserId();
            Result<Long> result = teamFacade.addTeam(teamVO);
            Long teamId = 0L;
            if (result.getData() != null) {
                teamId = result.getData();
            } else {
                log.error("班组信息插入失败！");
                return Result.wrapErrorResult(LegendErrorCode.TEAM_ADD_ERROR.getCode(), LegendErrorCode.TEAM_ADD_ERROR.getErrorMessage());
            }
            List<ShopManagerExtVO> shopManagerExtVOList = teamVO.getShopManagerExtVOList();
            if (!CollectionUtils.isEmpty(shopManagerExtVOList)) {
                for (ShopManagerExtVO shopManagerExtVO : shopManagerExtVOList) {
                    shopManagerExtVO.setTeamId(teamId);
                    shopManagerExtVO.setTeamName(teamVO.getName());
                    shopManagerExtVO.setModifier(currentUser);
                    shopManagerExtVO.setGmtModified(new Date());
                }
                log.info("开始插入班组员工信息");
                com.tqmall.legend.common.Result result1 = shopManagerFacade.modifyShopManagerExtList(shopManagerExtVOList);
            }

            return Result.wrapSuccessfulResult("");
        } catch (Exception e) {
            log.error("[添加班组信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.TEAM_ADD_ERROR.getCode(), LegendErrorCode.TEAM_ADD_ERROR.getErrorMessage());
        }
    }


    @RequestMapping("/ungrouped")
    @ResponseBody
    public Result<TeamVO> getUnGroupedShopManagers(HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        TeamVO teamVO = new TeamVO();
        teamVO.setShopId(shopId);
        List<ShopManagerExtVO> shopManagerExtVOList = shopManagerFacade.getUngroupedShopManagerExt(shopId).getData();
        teamVO.setShopManagerExtVOList(shopManagerExtVOList);
        return Result.wrapSuccessfulResult(teamVO);
    }

    /**
     * 通过班组ID删除班组信息
     *
     * @param teamId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result removeTeam(Long shopId, Long teamId, HttpServletRequest request) {
        if(teamId==null){
            return Result.wrapErrorResult("","参数不能为空！");
        }
        try {
            Integer result = teamFacade.removeTeam(teamId).getData();
            if (result > 0) {
                return Result.wrapSuccessfulResult("");
            } else {
                return Result.wrapErrorResult(LegendErrorCode.TEAM_DELETE_ERROR.getCode(), LegendErrorCode.TEAM_DELETE_ERROR.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[删除班组信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.TEAM_DELETE_ERROR.getCode(), LegendErrorCode.TEAM_DELETE_ERROR.getErrorMessage());
        }

    }

    /**
     * 查询一家门店的所有班组信息
     *
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result<List<TeamVO>> getTeamListByShopId(HttpServletRequest request) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        try {
            Result<List<TeamVO>> getTeamByShopId = teamFacade.getTeamByShopId(shopId);
            return getTeamByShopId;
        } catch (Exception e) {
            log.error("[显示班组列表] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.TEAM_GET_LIST_ERROR.getCode(), LegendErrorCode.TEAM_GET_LIST_ERROR.getErrorMessage());

        }
    }

    /**
     * 显示门店班组信息，包含具体成员信息
     *
     * @param teamId
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public Result<TeamVO> getTeamDetailList(Long teamId, HttpServletRequest request) {
        if(teamId==null){
            return Result.wrapErrorResult("","参数不能为空！");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        try {
            TeamVO teamVO = teamFacade.getTeamDetail(shopId, teamId).getData();
            teamVO.setShopId(shopId);
            return Result.wrapSuccessfulResult(teamVO);
        } catch (Exception e) {
            log.error("[显示班组详情] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.TEAM_GET_DETAIL_ERROR.getCode(), LegendErrorCode.TEAM_GET_DETAIL_ERROR.getErrorMessage());
        }
    }

    @RequestMapping("/managerByTeamId")
    @ResponseBody
    public Result<List<ShopManagerExtVO>> getManagerListByTeamId(Long teamId, HttpServletRequest request) {
        if(teamId==null){
            return Result.wrapErrorResult("","参数不能为空！");
        }
        try {
            Long shopId = UserUtils.getShopIdForSession(request);
            List<ShopManagerExtVO> shopManagerExtVOList = teamFacade.getManagerExtListByTeamId(shopId, teamId).getData();
            return Result.wrapSuccessfulResult(shopManagerExtVOList);
        } catch (Exception e) {
            log.error("[显示班组详情] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.TEAM_GET_DETAIL_ERROR.getCode(), LegendErrorCode.TEAM_GET_DETAIL_ERROR.getErrorMessage());
        }
    }


    /**
     * 班组编辑接口
     *
     * @param teamVO
     * @param request
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Result editTeamInfo(@RequestBody TeamVO teamVO, HttpServletRequest request) {
        if("".equals(teamVO.getName())){
            return Result.wrapErrorResult("","班组名称不能为空！");
        }
        log.info("接收到的参数是：teamVO={}", JSONUtil.object2Json(teamVO));
        Long shopId = UserUtils.getShopIdForSession(request);
        teamVO.setShopId(shopId);
        try {
            Integer result = teamFacade.modifyTeam(teamVO).getData();

            shopManagerFacade.modifyShopManagerExtList(teamVO.getShopManagerExtVOList());

            if (result > 0) {
                return Result.wrapSuccessfulResult("");
            } else {
                return Result.wrapErrorResult(LegendErrorCode.TEAM_EDIT_ERROR.getCode(), LegendErrorCode.TEAM_EDIT_ERROR.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[编辑班组信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.TEAM_EDIT_ERROR.getCode(), LegendErrorCode.TEAM_EDIT_ERROR.getErrorMessage());
        }
    }



}
