package com.tqmall.legend.facade.magic.impl;

import com.tqmall.common.util.BdUtil;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.magic.ShopManagerFacade;
import com.tqmall.legend.facade.magic.vo.ShopManagerExtVO;
import com.tqmall.legend.facade.magic.vo.WorkOrderProcessRelVo;
import com.tqmall.magic.object.result.shopmanager.ShopManagerExtensionDTO;
import com.tqmall.magic.object.result.workshop.WorkOrderProcessRelDTO;
import com.tqmall.magic.service.shopmanger.RpcShopManagerExtService;
import com.tqmall.magic.service.workshop.RpcWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shulin on 16/7/1.
 */
@Service
@Slf4j
public class ShopManagerFacadeImpl implements ShopManagerFacade {
    private static final String DUBBO = "[调用Magic DUBBO接口]";

    @Autowired
    private RpcShopManagerExtService rpcShopManagerExtService;
    @Autowired
    private RpcWorkOrderService rpcWorkOrderService;

    @Override
    public Result modifyShopManagerExt(ShopManagerExtVO shopManagerExtVO, Long modifier) {
        log.info("{} 更新员工扩展表，参数shopManagerExtId={}，modifier={}", DUBBO, shopManagerExtVO.getId(), modifier);
        ShopManagerExtensionDTO shopManagerExtensionDTO = BdUtil.bo2do(shopManagerExtVO, ShopManagerExtensionDTO.class);
        shopManagerExtensionDTO.setModifier(modifier);
        com.tqmall.core.common.entity.Result<Integer> result = null;
        try {
            result = rpcShopManagerExtService.modifyShopManagerExtension(shopManagerExtensionDTO);
            log.info("{} 修改员工扩展信息，flag={}", DUBBO, result.isSuccess());
        } catch (Exception e) {
            log.error("调用Magic dubbo接口修改门店员工扩展信息失败！，e={}", e);
            return Result.wrapErrorResult("", "修改门店员工扩展信息失败！");
        }
        if (!result.isSuccess()) {
            log.error("{} 更新员工扩展表失败！店铺ID={}，员工ID={}", DUBBO, shopManagerExtVO.getShopId(), shopManagerExtVO.getManagerId());
            return Result.wrapErrorResult("", "");
        }
        return Result.wrapSuccessfulResult("");
    }

    @Override
    public Result addShopManagerExtInfo(ShopManagerExtVO shopManagerExtVO, Long creator) {
        ShopManagerExtensionDTO shopManagerExtensionDTO = BdUtil.bo2do(shopManagerExtVO, ShopManagerExtensionDTO.class);
        shopManagerExtensionDTO.setModifier(creator);
        com.tqmall.core.common.entity.Result<Integer> result = null;
        try {
            result = rpcShopManagerExtService.createShopManagerExtension(shopManagerExtensionDTO);
            log.info("{} 创建员工扩展信息，flag={}", DUBBO, result.isSuccess());
        } catch (Exception e) {
            log.error("调用DUBBO接口添加员工扩展信息失败！e={}", e);
            return Result.wrapErrorResult("", "添加员工扩展信息失败！");
        }
        return Result.wrapSuccessfulResult(result);
    }

    @Override
    public Result modifyShopManagerExtList(List<ShopManagerExtVO> shopManagerExtVOList) {
        List<ShopManagerExtensionDTO> shopManagerExtensionDTOList = BdUtil.bo2do4List(shopManagerExtVOList, ShopManagerExtensionDTO.class);
        com.tqmall.core.common.entity.Result<Integer> result = null;
        try {
            result = rpcShopManagerExtService.modifyShopManagerExtInfos(shopManagerExtensionDTOList);
            log.info("{} 批量修改员工扩展信息，flag={}", DUBBO, result.isSuccess());
        } catch (Exception e) {
            log.error("调用Magic dubbo接口修改员工扩展信息失败！e={}", e);
            return Result.wrapErrorResult("", "修改员工扩展信息失败！");
        }
        return Result.wrapSuccessfulResult("");
    }


    @Override
    public Result<ShopManagerExtVO> getShopManagerExtInfo(Long shopId, Long managerId) {
        log.info("{} 获取员工扩展信息详情，参数shopId={},managerId={}", DUBBO, shopId, managerId);
        ShopManagerExtensionDTO shopManagerExtensionDTO = null;
        try {
            com.tqmall.core.common.entity.Result<ShopManagerExtensionDTO> result = rpcShopManagerExtService.getShopManagerExtensionBy2Id(shopId, managerId);
            shopManagerExtensionDTO = result.getData();
            log.info("{} 获取员工扩展信息 End，flag={}", result.isSuccess());
        } catch (Exception e) {
            log.error("调用DUBBO接口获取员工扩展信息失败！e={}", e);
            return Result.wrapErrorResult("", "获取员工扩展信息失败！");
        }
        if (null != shopManagerExtensionDTO) {
            ShopManagerExtVO shopManagerExtVO = BdUtil.bo2do(shopManagerExtensionDTO, ShopManagerExtVO.class);
            return Result.wrapSuccessfulResult(shopManagerExtVO);
        } else {
            log.info("{} 获取员工扩展信息返回值为空。", DUBBO);
            return Result.wrapSuccessfulResult(null);
        }
    }

    @Override
    public Result<List<ShopManagerExtVO>> getUngroupedShopManagerExt(Long shopId) {
        List<ShopManagerExtensionDTO> shopManagerExtensionDTOList = null;
        List<ShopManagerExtVO> shopManagerExtVOList = null;
        try {
            com.tqmall.core.common.entity.Result<List<ShopManagerExtensionDTO>> result = rpcShopManagerExtService.getShopManagerExtensionByShopIdAndTeamId(shopId, 0L);
            log.info("{} 获取没被分组的员工扩展列表，shopId={},flag={}", shopId, result.isSuccess());
            shopManagerExtensionDTOList = result.getData();
        } catch (Exception e) {
            return Result.wrapErrorResult("", "获取未被分组的员工信息列表失败！shopId=" + shopId);
        }
        if (!CollectionUtils.isEmpty(shopManagerExtensionDTOList)) {
            shopManagerExtVOList = BdUtil.bo2do4List(shopManagerExtensionDTOList, ShopManagerExtVO.class);
        }
        return Result.wrapSuccessfulResult(shopManagerExtVOList);
    }

    @Override
    public Result<Boolean> modifyShopManagerExtStatus(Long shopId, Long managerId, Long currentUser, Integer status, WorkOrderProcessRelVo currentProcess) {
        ShopManagerExtensionDTO shopManagerExtensionDTO = null;
        try {
            shopManagerExtensionDTO = rpcShopManagerExtService.getShopManagerExtensionBy2Id(shopId, managerId).getData();
        } catch (Exception e) {
            log.error("调用Magic DUBBO获取员工扩展信息失败！shopId={},managerId={},e={}", shopId, managerId, e);
            return Result.wrapErrorResult("", "更新员工扩展信息空闲/忙碌状态，查询信息失败！");
        }
        shopManagerExtensionDTO.setFreeStatus(status);
        shopManagerExtensionDTO.setModifier(currentUser);
        shopManagerExtensionDTO.setFreeTime(new Date());
        try {
            rpcShopManagerExtService.modifyShopManagerExtension(shopManagerExtensionDTO);
        } catch (Exception e) {
            log.error("调用Magic DUBBO接口更新员工扩展信息空闲/忙碌状态失败！e={}", e);
            return Result.wrapErrorResult("", "更新员工扩展信息空闲/忙碌状态失败！");
        }
        //修改当前工序的实际操作人为这个扫码的技师
        if (currentProcess != null) {
            WorkOrderProcessRelDTO workOrderProcessRelDTO = BdUtil.bo2do(currentProcess, WorkOrderProcessRelDTO.class);
            try {
                rpcWorkOrderService.modifyWorkOrderProcessStatus(workOrderProcessRelDTO);
            } catch (Exception e) {
                log.error("调用Magic DUBBO接口修改施工单工序管理信息状态失败！e={}", e);
                return Result.wrapErrorResult("", "更新员工扩展信息空闲/忙碌状态，更新工序实际操作人员信息失败！");
            }
        }
        return Result.wrapSuccessfulResult(true);

    }

    @Override
    public Result<ShopManagerExtVO> getShopManagerExtByCarNum(Long shopId, String carNum) {
        ShopManagerExtensionDTO shopManagerExtensionDTO = null;
        ShopManagerExtVO shopManagerExtVO = null;
        try {
            shopManagerExtensionDTO = rpcShopManagerExtService.getShopManagerExtByShopIdAndCardNum(shopId, carNum).getData();
            shopManagerExtVO = BdUtil.bo2do(shopManagerExtensionDTO, ShopManagerExtVO.class);
        } catch (Exception e) {
            log.error("调用DUBBO接口 获取员工扩展详情失败！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.GET_MANAGER_EXT_INFO_ERROR.getCode(), LegendErrorCode.GET_MANAGER_EXT_INFO_ERROR.getErrorMessage());
        }
        return Result.wrapSuccessfulResult(shopManagerExtVO);
    }

    @Override
    public Result<List<ShopManagerExtVO>> getShopManagerExtInfoByTeamId(Long shopId, Long teamId) {
        List<ShopManagerExtensionDTO> shopManagerExtensionDTOList = null;
        try {
            shopManagerExtensionDTOList = rpcShopManagerExtService.getShopManagerExtensionByShopIdAndTeamId(shopId, teamId).getData();
        } catch (Exception e) {
            log.error("调用Magic DUBBO接口通过teamId和shopId获取员工扩展信息失败！e={}", e);
            return Result.wrapErrorResult("", "获取员工扩展信息失败！");
        }
        List<ShopManagerExtVO> shopManagerExtVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shopManagerExtensionDTOList)) {
            for (ShopManagerExtensionDTO shopManagerExtensionDTO : shopManagerExtensionDTOList) {
                ShopManagerExtVO shopManagerExtVO = new ShopManagerExtVO();
                BeanUtils.copyProperties(shopManagerExtensionDTO, shopManagerExtVO);
                shopManagerExtVOList.add(shopManagerExtVO);
            }
        }
        return Result.wrapSuccessfulResult(shopManagerExtVOList);
    }

    @Override
    public Result<List<ShopManagerExtVO>> getOnWorkShopManagerExtInfoByTeamId(Long shopId, Long teamId) {
        List<ShopManagerExtensionDTO> shopManagerExtensionDTOList = null;
        try {
            shopManagerExtensionDTOList = rpcShopManagerExtService.getOnWorkShopManagerExtensionByIds(shopId, teamId).getData();
        } catch (Exception e) {
            log.error("调用Magic DUBBO接口获取在岗员工扩展信息失败！e={}", e);
            return Result.wrapErrorResult("", "获取在岗员工扩展信息失败！");
        }
        List<ShopManagerExtVO> shopManagerExtVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shopManagerExtensionDTOList)) {
            for (ShopManagerExtensionDTO shopManagerExtensionDTO : shopManagerExtensionDTOList) {
                ShopManagerExtVO shopManagerExtVO = new ShopManagerExtVO();
                BeanUtils.copyProperties(shopManagerExtensionDTO, shopManagerExtVO);
                shopManagerExtVOList.add(shopManagerExtVO);
            }
        }
        return Result.wrapSuccessfulResult(shopManagerExtVOList);
    }


    @Override
    public Result<Integer> removeShopMnagerExt(Long shopId, Long managerId, Long modifier) {
        com.tqmall.core.common.entity.Result<Integer> result = null;
        Integer flag = 0;
        try {
            result = rpcShopManagerExtService.removeShopManagerExtsionBy2Id(shopId, managerId);
            log.info("调用Magic DUBBO接口删除员工扩展信息,返回结果={}", result.isSuccess());
            flag = result.getData();
        } catch (Exception e) {
            log.error("调用Magic DUBBO接口删除员工扩展信息失败！e={}", e);
            return Result.wrapErrorResult("", "删除员工扩展信息失败！");
        }
        return Result.wrapSuccessfulResult(flag);
    }
}
