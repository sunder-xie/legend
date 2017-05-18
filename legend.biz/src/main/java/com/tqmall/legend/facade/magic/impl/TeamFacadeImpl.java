package com.tqmall.legend.facade.magic.impl;

import com.google.common.collect.Lists;
import com.tqmall.common.util.BdUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.magic.ShopManagerFacade;
import com.tqmall.legend.facade.magic.TeamFacade;
import com.tqmall.legend.facade.magic.vo.ShopManagerExtVO;
import com.tqmall.legend.facade.magic.vo.TeamVO;
import com.tqmall.magic.object.param.shopmanager.TeamParam;
import com.tqmall.magic.object.result.shopmanager.ShopManagerExtensionDTO;
import com.tqmall.magic.object.result.shopmanager.TeamDTO;
import com.tqmall.magic.service.shopmanger.RpcShopManagerExtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 班组
 * Created by shulin on 16/7/4.
 */
@Service
@Slf4j
public class TeamFacadeImpl implements TeamFacade {

    @Autowired
    private RpcShopManagerExtService rpcShopManagerExtService;

    @Autowired
    private ShopManagerFacade shopManagerFacade;

    @Override
    public Result<Long> addTeam(TeamVO teamVO) {
        if (teamVO == null || teamVO.getName() == null || "".equals(teamVO.getName())) {
            return Result.wrapErrorResult("", "班组名称不能为空！");
        }
        log.info("[班组] Start 调用DUBBO接口添加班组信息,name={},remark={}", teamVO.getName(), teamVO.getRemark());
        TeamDTO teamDTO = BdUtil.bo2do(teamVO, TeamDTO.class);
        Result<Long> result = null;
        try {
            result = rpcShopManagerExtService.createTeam(teamDTO);
            log.info("[班组] End 调用DUBBO接口添加班组返回结果,flag={}", result.isSuccess());
        } catch (Exception e) {
            log.error("[班组] 调用DUBBO接口添加班组信息失败！e={}", e);
            return Result.wrapErrorResult("", "添加班组失败！");
        }
        return result;
    }

    @Override
    public Result removeTeam(Long teamId) {
        log.info("[班组] Start 调用DUBBO接口删除班组信息，teamID={}", teamId);
        Result<Integer> result = null;
        try {
            result = rpcShopManagerExtService.removeTeam(teamId);
            log.info("[班组] End 调用DUBBO接口删除班组,flag={}", result.isSuccess());
        } catch (Exception e) {
            log.error("", "[班组] 调用DUBBO服务删除班组失败！e={}", e);
            return Result.wrapErrorResult("", "删除班组失败！");
        }
        return result;
    }

    @Override
    public Result modifyTeam(TeamVO teamVO) {
        log.info("[班组] Start 调用DUBBO接口修改班组信息，teamId={}", teamVO.getId());
        TeamDTO teamDTO = BdUtil.bo2do(teamVO, TeamDTO.class);
        Result<Integer> result = null;
        try {
            result = rpcShopManagerExtService.modifyTeam(teamDTO);
            log.info("[班组] End 调用DUBBO接口修改班组信息，flag={}", result.isSuccess());
        } catch (Exception e) {
            log.error("[班组] 修改班组信息失败！e={}", e);
            return Result.wrapErrorResult("", "修改班组信息失败！");
        }
        return result;
    }

    @Override
    public Result<List<TeamVO>> getTeamByShopId(Long shopId) {
        if (shopId == null) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        log.info("[班组] Start 通过ShopId获取班组列表shopId ={}", shopId);
        TeamParam teamParam = new TeamParam();
        teamParam.setShopId(shopId);
        try {
            Result<List<TeamDTO>> result = rpcShopManagerExtService.getTeamList(teamParam);
            List<TeamDTO> resultDTO = result.getData();
            log.info("[班组] 调用DUBBO接口获取班组列表 结束，flag={}", result.isSuccess());
            List<TeamVO> resultVO = new ArrayList<>();
            if (!CollectionUtils.isEmpty(resultDTO)) {
                log.info("[班组] 调用DUBBO接口获取被分组的员工列表 开始，shopId={}", shopId);
                Result<List<ShopManagerExtensionDTO>> resultTemp = rpcShopManagerExtService.getGroupedShopManagerExtensions(shopId);
                List<ShopManagerExtensionDTO> shopManagerExtensionDTOList = resultTemp.getData();
                log.info("[班组] 调用DUBBO接口获取被分组的员工列表 结束，flag={}", resultTemp.isSuccess());
                if (!CollectionUtils.isEmpty(shopManagerExtensionDTOList)) {
                    for (TeamDTO teamDTO : resultDTO) {
                        Long teamId = teamDTO.getId();
                        TeamVO teamVO = BdUtil.bo2do(teamDTO, TeamVO.class);
                        List<ShopManagerExtVO> shopManagerExtVOList = new ArrayList<>();
                        for (ShopManagerExtensionDTO shopManagerExtensionDTO : shopManagerExtensionDTOList) {
                            if (teamId == shopManagerExtensionDTO.getTeamId()) {
                                ShopManagerExtVO shopManagerExtVO = BdUtil.bo2do(shopManagerExtensionDTO, ShopManagerExtVO.class);
                                shopManagerExtVOList.add(shopManagerExtVO);
                            }
                        }
                        teamVO.setShopManagerExtVOList(shopManagerExtVOList);
                        resultVO.add(teamVO);
                    }
                } else {
                    for (TeamDTO teamDTO : resultDTO) {
                        TeamVO teamVO = BdUtil.bo2do(teamDTO, TeamVO.class);
                        resultVO.add(teamVO);
                    }
                }
            }
            return Result.wrapSuccessfulResult(resultVO);
        } catch (Exception e) {
            log.error("[班组] 调用Magic DUBBO接口获取班组列表信息失败！e={}", e);
            return Result.wrapErrorResult("", "获取班组列表失败！");
        }

    }

    @Override
    public Result<TeamVO> getTeamDetail(Long shopId, Long teamId) {
        if (shopId == null || teamId == null) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        TeamParam teamParam = new TeamParam();
        teamParam.setId(teamId);
        teamParam.setShopId(shopId);
        log.info("[班组] Start 调用DUBBO接口获取班组详情,shopId={},teamId={}", shopId, teamId);
        Result<List<TeamDTO>> result = null;
        List<TeamDTO> resultDTO = null;
        try {
            result = rpcShopManagerExtService.getTeamList(teamParam);
            log.info("[班组] End 调用DUBBO接口获取班组详情,flag={}", result.isSuccess());
            resultDTO = result.getData();
        } catch (Exception e) {
            log.error("[班组] 调用Magic DUBBO接口获取班组详情失败！e={}", e);
            return Result.wrapErrorResult("", "获取班组详情失败！");
        }
        TeamVO resultVO = new TeamVO();
        //如果不为空，那么必然仅有一条数据
        if (!CollectionUtils.isEmpty(resultDTO)) {
            BeanUtils.copyProperties(resultDTO.get(0), resultVO);
            List<ShopManagerExtVO> shopManagerExtVOs = shopManagerFacade.getShopManagerExtInfoByTeamId(resultVO.getShopId(), resultVO.getId()).getData();
            resultVO.setShopManagerExtVOList(shopManagerExtVOs);
        }
        return Result.wrapSuccessfulResult(resultVO);
    }

    @Override
    public Result<List<ShopManagerExtVO>> getManagerExtListByTeamId(Long shopId, Long teamId) {
        if (shopId == null || teamId == null) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        TeamParam teamParam = new TeamParam();
        teamParam.setId(teamId);
        teamParam.setShopId(shopId);
        log.info("[班组] Start 获取班组人员列表信息，teamId={},shopId={}", teamId, shopId);
        Result<List<TeamDTO>> result = null;
        List<TeamDTO> teamDTOList = null;
        try {
            result = rpcShopManagerExtService.getTeamList(teamParam);
            log.info("[班组] End 获取班组人员列表信息，flag={}", result.isSuccess());
            teamDTOList = result.getData();
        } catch (Exception e) {
            log.error("[班组] 获取班组人员列表信息失败！e={}", e);
            return Result.wrapErrorResult("", "获取班组人员列表失败！");
        }
        List<ShopManagerExtVO> shopManagerExtVOs = Lists.newArrayList();
        //如果不为空，那么必然仅有一条数据
        if (!CollectionUtils.isEmpty(teamDTOList)) {
            TeamDTO teamDTO = teamDTOList.get(0);
            shopManagerExtVOs = shopManagerFacade.getOnWorkShopManagerExtInfoByTeamId(teamDTO.getShopId(), teamDTO.getId()).getData();
        }
        return Result.wrapSuccessfulResult(shopManagerExtVOs);
    }
}
