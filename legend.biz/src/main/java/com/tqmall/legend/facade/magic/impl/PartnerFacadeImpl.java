package com.tqmall.legend.facade.magic.impl;

import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.magic.PartnerFacade;
import com.tqmall.legend.enums.magic.PartnerStatusEnum;
import com.tqmall.legend.facade.magic.vo.ShopPartnerSimVO;
import com.tqmall.legend.facade.magic.vo.ShopPartnerVO;
import com.tqmall.magic.object.param.partner.ShopPartnerParam;
import com.tqmall.magic.object.param.partner.ShopPartnerSimParam;
import com.tqmall.magic.object.result.common.PageEntityDTO;
import com.tqmall.magic.object.result.partner.ShopPartnerDTO;
import com.tqmall.magic.service.partner.RpcShopPartnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by macx on 16/5/17.
 */
@Service
@Slf4j
public class PartnerFacadeImpl implements PartnerFacade {

    @Autowired
    private RpcShopPartnerService rpcShopPartnerService;
    @Autowired
    private ShopService shopService;

    /**
     * 获取股东列表(分页)
     * @param pageable
     * @param shopId
     * @param name
     * @param partnerStatus
     * @return
     */
    @Override
    public Result getPartnerPage(Pageable pageable,Long shopId, String name, Integer partnerStatus) {
        ShopPartnerParam shopPartnerParam = new ShopPartnerParam();
        shopPartnerParam.setShopId(shopId);
        shopPartnerParam.setPageNum(pageable.getPageNumber());
        shopPartnerParam.setPageSize(pageable.getPageSize());
        shopPartnerParam.setName(name);
        shopPartnerParam.setPartnerStatus(partnerStatus);
        try {
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.getPartnerList获取分页股东列表,参数:shopId={}", shopId);
            Result<PageEntityDTO<ShopPartnerDTO>> pageEntityDTOResult = rpcShopPartnerService.getPartnerList(shopPartnerParam);
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.getPartnerList获取分页股东列表,返回:success={}", pageEntityDTOResult.isSuccess());
            if (pageEntityDTOResult.isSuccess()) {
                PageEntityDTO<ShopPartnerDTO> pageEntityDTO = pageEntityDTOResult.getData();
                List<ShopPartnerDTO> shopPartnerDTOList = pageEntityDTO.getRecordList();
                Integer totalNum = pageEntityDTO.getTotalNum();
                List<ShopPartnerVO> shopPartnerVOs = new ArrayList<>();
                for (ShopPartnerDTO shopPartnerDTO : shopPartnerDTOList) {
                    ShopPartnerVO shopPartnerVO = new ShopPartnerVO();
                    BeanUtils.copyProperties(shopPartnerDTO, shopPartnerVO);
                    Shop shop = null;
                    try {
                        shop = shopService.selectById(shopPartnerDTO.getPartnerId());
                    } catch (Exception e) {
                        log.error("查询门店信息失败",e);
                        return Result.wrapErrorResult("","查询门店信息失败");
                    }
                    if (shop != null){
                        shopPartnerVOs.add(shopPartnerVO);
                    }
                }
                PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<ShopPartnerVO> page = new DefaultPage<>(shopPartnerVOs, pageRequest, totalNum);
                return Result.wrapSuccessfulResult(page);
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), LegendErrorCode.SHARE_DUBBO_ERROR.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[共享中心] 获取股东分页列表异常 e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PAGE_ERROR.getCode(), LegendErrorCode.SHARE_PARTNER_PAGE_ERROR.getErrorMessage());
    }

    /**
     * 获取门店未存在股东
     * @param shop
     * @param name
     * @return
     */
    @Override
    public Result getAddPartnerList(Shop shop, String name) {
        ShopPartnerSimParam shopPartnerSimParam = new ShopPartnerSimParam();
        shopPartnerSimParam.setShopId(shop.getId());
        try {
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.getAllPartnerList获取已存在股东列表,参数:shopId={}", shop.getId());
            Result<List<ShopPartnerDTO>> result = rpcShopPartnerService.getAllPartnerList(shopPartnerSimParam);
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.getAllPartnerList获取已存在股东列表,返回:success={}", result.isSuccess());
            if (result.isSuccess()) {
                List<Long> addIds = new ArrayList<>();
                List<ShopPartnerDTO> shopPartnerDTOs = result.getData();
                for (ShopPartnerDTO shopPartnerDTO : shopPartnerDTOs) {
                    addIds.add(shopPartnerDTO.getPartnerId());
                }
                Map<String, Object> param = new HashMap<>();
                param.put("province", shop.getProvince());
                param.put("city", shop.getCity());
                param.put("nameLike", name);
                List<Shop> shops = shopService.select(param);
                List<ShopPartnerSimVO> unAddPartnerSimVOs = new ArrayList<>();
                for (Shop unAddShop : shops) {
                    if (!addIds.contains(unAddShop.getId())) {
                        ShopPartnerSimVO unAddPartnerSimVO = new ShopPartnerSimVO();
                        unAddPartnerSimVO.setId(unAddShop.getId());
                        unAddPartnerSimVO.setName(unAddShop.getName());
                        unAddPartnerSimVOs.add(unAddPartnerSimVO);
                    }
                }
                return Result.wrapSuccessfulResult(unAddPartnerSimVOs);
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), LegendErrorCode.SHARE_DUBBO_ERROR.getErrorMessage());
            }
        }catch (Exception e) {
            log.error("[共享中心] 获取门店未存在股东异常 e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PAGE_ERROR.getCode(), LegendErrorCode.SHARE_PARTNER_PAGE_ERROR.getErrorMessage());
    }

    /**
     * 更新股东信息
     * @param shopPartnerVO
     * @param shopId
     * @param userId
     * @return
     */
    @Override
    public Result updatePartnerInfo(ShopPartnerVO shopPartnerVO,Long shopId,Long userId) {
        ShopPartnerParam shopPartnerParam = new ShopPartnerParam();
        shopPartnerParam.setId(shopPartnerVO.getId());
        shopPartnerParam.setRate(shopPartnerVO.getRate());
        shopPartnerParam.setMobile(shopPartnerVO.getMobile());
        shopPartnerParam.setContactName(shopPartnerVO.getContactName());
        shopPartnerParam.setAddress(shopPartnerVO.getAddress());
        shopPartnerParam.setNote(shopPartnerVO.getNote());
        shopPartnerParam.setShopId(shopId);
        shopPartnerParam.setModifier(userId);
        try {
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.updatePartnerInfo更新股东信息,参数:shopId={}", shopId);
            Result result = rpcShopPartnerService.updatePartnerInfo(shopPartnerParam);
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.updatePartnerInfo更新股东信息,返回:success={}", result.isSuccess());
            if (result.isSuccess()) {
                return Result.wrapSuccessfulResult("保存成功");
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), LegendErrorCode.SHARE_DUBBO_ERROR.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[共享中心] 保存股东信息异常 e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_SAVE_ERROR.getCode(), LegendErrorCode.SHARE_PARTNER_SAVE_ERROR.getErrorMessage());
    }

    /**
     * 退出股东
     * @param id
     * @param shopId
     * @param userId
     * @param reason
     * @return
     */
    @Override
    public Result quitShopPartner(Long id, Long shopId, Long userId, String reason) {
        try {
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.quitPartnerInfo退出股东,参数:id={};shopId={};userId={};reason={}", id, shopId, userId, reason);
            Result result = rpcShopPartnerService.quitPartnerInfo(id, shopId, userId, reason);
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.quitPartnerInfo退出股东,返回:success={}", result.isSuccess());
            if (result.isSuccess()) {
                return Result.wrapSuccessfulResult("退出成功");
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), LegendErrorCode.SHARE_DUBBO_ERROR.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[共享中心] 退出股东异常 e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_QUIT_ERROR.getCode(), LegendErrorCode.SHARE_PARTNER_QUIT_ERROR.getErrorMessage());
    }

    /**
     * 加入股东
     * @param id
     * @param shopId
     * @param userId
     * @return
     */
    @Override
    public Result joinShopPartner(Long id, Long shopId, Long userId) {
        try {
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.joinPartner加入股东,参数:id={};shopId={};userId={}", id, shopId, userId);
            Result result = rpcShopPartnerService.joinPartner(id, shopId, userId);
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.joinPartner加入股东,返回:success={}", result.isSuccess());
            if (result.isSuccess()) {
                return Result.wrapSuccessfulResult("加入成功");
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), LegendErrorCode.SHARE_DUBBO_ERROR.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[共享中心] 加入股东异常 e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_JOIN_ERROR.getCode(), LegendErrorCode.SHARE_PARTNER_JOIN_ERROR.getErrorMessage());
    }

    /**
     * 获取股东信息
     * @param id
     * @param shopId
     * @return
     */
    @Override
    public Result getPartnerInfo(Long id, Long shopId) {
        try {
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.getPartnerInfo获取股东信息,参数:id={};shopId={};", id, shopId);
            Result<ShopPartnerDTO> result = rpcShopPartnerService.getPartnerInfo(id, shopId);
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.getPartnerInfo获取股东信息,返回:success={}", result.isSuccess());
            if (result.isSuccess()) {
                ShopPartnerDTO shopPartnerDTO = result.getData();
                ShopPartnerVO shopPartnerVO = new ShopPartnerVO();
                BeanUtils.copyProperties(shopPartnerDTO,shopPartnerVO);
                return Result.wrapSuccessfulResult(shopPartnerVO);
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), LegendErrorCode.SHARE_DUBBO_ERROR.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[共享中心] 获取股东信息异常 e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_INFO_ERROR.getCode(), LegendErrorCode.SHARE_PARTNER_INFO_ERROR.getErrorMessage());
    }

    /**
     * 添加股东
     * @param partnerIdArray
     * @param shopId
     * @param userId
     * @return
     */
    @Override
    public Result addShopPartner(String[] partnerIdArray, Long shopId, Long userId) {
        List<ShopPartnerParam> shopPartnerParams = new ArrayList<>();
        for (String partnerId : partnerIdArray) {
            ShopPartnerParam shopPartnerParam = new ShopPartnerParam();
            shopPartnerParam.setShopId(shopId);
            shopPartnerParam.setPartnerId(Long.parseLong(partnerId));
            Shop shop = shopService.selectById(Long.parseLong(partnerId));
            if (shop == null) {
                log.error("[共享中心] 添加股东,门店不存在id={}", partnerId);
                return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getCode(),LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getErrorMessage());
            }
            shopPartnerParam.setName(shop.getName());
            shopPartnerParam.setContactName(shop.getContact());
            shopPartnerParam.setMobile(shop.getTel());
            shopPartnerParam.setAddress(shop.getAddress());
            shopPartnerParams.add(shopPartnerParam);
        }
        try {
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.addPartner添加股东,参数:shopId={}", shopId);
            Result result = rpcShopPartnerService.addPartner(shopPartnerParams);
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.addPartner添加股东,返回:success={}", result.isSuccess());
            if (result.isSuccess()) {
                return Result.wrapSuccessfulResult("添加成功");
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), LegendErrorCode.SHARE_DUBBO_ERROR.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("[共享中心] 添加股东异常 e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_ADD_ERROR.getCode(), LegendErrorCode.SHARE_PARTNER_ADD_ERROR.getErrorMessage());
    }

    /**
     * 获取未加入股东(相同地市门店-股东表存在的门店)
     * @param pageable
     * @param shop
     * @param name
     * @return
     */
    @Override
    public Result getUnJoinPartnerPage(Pageable pageable, Shop shop, String name) {
        ShopPartnerSimParam shopPartnerSimParam = new ShopPartnerSimParam();
        shopPartnerSimParam.setShopId(shop.getId());
        try {
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.getAllPartnerList获取已存在股东列表,参数:shopId={}", shop.getId());
            Result<List<ShopPartnerDTO>> result = rpcShopPartnerService.getAllPartnerList(shopPartnerSimParam);
            log.info("[共享中心] DUBBO接口rpcShopPartnerService.getAllPartnerList获取已存在股东列表,返回:success={}", result.isSuccess());
            if (result.isSuccess()) {
                List<Long> addIds = new ArrayList<>();
                List<ShopPartnerDTO> shopPartnerDTOs = result.getData();
                for (ShopPartnerDTO shopPartnerDTO : shopPartnerDTOs) {
                    addIds.add(shopPartnerDTO.getPartnerId());
                }
                Map<String, Object> param = new HashMap<>();
                param.put("province", shop.getProvince());
                param.put("city", shop.getCity());
                param.put("nameLike", name);
                List<Shop> shops = shopService.select(param);
                List<ShopPartnerVO> shopPartnerVOs = new ArrayList<>();
                for (Shop unAddShop : shops) {
                    if (!addIds.contains(unAddShop.getId())) {
                        ShopPartnerVO shopPartnerVO = new ShopPartnerVO();
                        shopPartnerVO.setName(unAddShop.getName());
                        shopPartnerVO.setAddress(unAddShop.getAddress());
                        shopPartnerVO.setContactName(unAddShop.getContact());
                        shopPartnerVO.setMobile(unAddShop.getMobile());
                        shopPartnerVO.setPartnerStatus(PartnerStatusEnum.UNJOIN.getCode());
                        shopPartnerVOs.add(shopPartnerVO);
                    }
                }
                PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                Integer totalCount = shopPartnerVOs.size();
                List<ShopPartnerVO> shopPartnerVOPage = splitList(shopPartnerVOs,pageable.getPageSize(),pageable.getPageNumber());
                DefaultPage<ShopPartnerVO> page = new DefaultPage<>(shopPartnerVOPage,pageRequest,totalCount);
                return Result.wrapSuccessfulResult(page);
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), LegendErrorCode.SHARE_DUBBO_ERROR.getErrorMessage());
            }
        }catch (Exception e) {
            log.error("[共享中心] 获取门店未加入股东异常 e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PAGE_ERROR.getCode(), LegendErrorCode.SHARE_PARTNER_PAGE_ERROR.getErrorMessage());
    }

    /**
     * list分页
     * @param list
     * @param pageSize
     * @param pageNum
     * @return
     */
    public List<ShopPartnerVO> splitList(List<ShopPartnerVO> list, int pageSize,int pageNum)
    {
        int listSize = list.size();
        int start = (pageNum-1) * pageSize;
        int end = pageNum * pageSize;
        if (start > listSize){
            return Collections.EMPTY_LIST;
        }
        if (end >= listSize){
            return list.subList(start,listSize);
        }
        return list.subList(start,end);
    }
}
