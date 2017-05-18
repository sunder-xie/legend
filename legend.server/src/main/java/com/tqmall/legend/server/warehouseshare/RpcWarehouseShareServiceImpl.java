package com.tqmall.legend.server.warehouseshare;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.warehouse.WarehouseShareService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.warehouseshare.WarehouseShare;
import com.tqmall.legend.object.param.warehouseshare.WarehouseShareSearchParam;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.object.result.warehouseshare.WarehouseShareDTO;
import com.tqmall.legend.service.warehouseshare.RpcWarehouseShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by xin on 2016/11/23.
 */
@Slf4j
@Service("rpcWarehouseShareService")
public class RpcWarehouseShareServiceImpl implements RpcWarehouseShareService {

    @Autowired
    private WarehouseShareService warehouseShareService;
    @Autowired
    private ShopService shopService;

    /**
     * 查询库存共享配件列表
     *
     * @param param
     * @return
     */
    @Override
    public Result<PageEntityDTO<WarehouseShareDTO>> searchWarehouseSharePage(WarehouseShareSearchParam param) {
        Page<WarehouseShare> page = warehouseShareService.searchWarehouseSharePage(param.getGoodsCate(), param.getGoodsName(), param.getShopId(), param.getStatus(), param.getPageNum(), param.getPageSize());
        if (page == null) {
            return Result.wrapErrorResult("", "没有配件信息");
        }
        int totalNum = (int) page.getTotalElements();
        int pageNum = param.getPageNum();
        List<WarehouseShare> content = page.getContent();
        List<WarehouseShareDTO> warehouseShareDTOList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(content)) {
            List<Long> shopIds = Lists.transform(content, new Function<WarehouseShare, Long>() {
                @Override
                public Long apply(WarehouseShare warehouseShare) {
                    return warehouseShare.getShopId();
                }
            });

            List<Shop> shopList = shopService.getListByIds(shopIds);
            if (!CollectionUtils.isEmpty(shopList)) {
                Map<Long, String> shopNameMap = Maps.newHashMap();
                for (Shop shop : shopList) {
                    shopNameMap.put(shop.getId(), shop.getName());
                }

                for (WarehouseShare warehouseShare : content) {
                    WarehouseShareDTO warehouseShareDTO = new WarehouseShareDTO();
                    warehouseShareDTO.setId(warehouseShare.getId());
                    warehouseShareDTO.setShopId(warehouseShare.getShopId());
                    warehouseShareDTO.setShopName(shopNameMap.get(warehouseShare.getShopId()));
                    warehouseShareDTO.setGoodsId(warehouseShare.getGoodsId());
                    warehouseShareDTO.setGoodsName(warehouseShare.getGoodsName());
                    warehouseShareDTO.setGoodsStock(warehouseShare.getGoodsStock());
                    warehouseShareDTO.setMeasureUnit(warehouseShare.getMeasureUnit());
                    warehouseShareDTO.setInventoryPrice(warehouseShare.getInventoryPrice());
                    warehouseShareDTO.setSaleNumber(warehouseShare.getSaleNumber());
                    warehouseShareDTO.setGoodsPrice(warehouseShare.getGoodsPrice());
                    warehouseShareDTO.setGoodsStatus(warehouseShare.getGoodsStatus());
                    warehouseShareDTO.setGoodsRemark(warehouseShare.getGoodsRemark());
                    warehouseShareDTOList.add(warehouseShareDTO);
                }
            }
        }
        PageEntityDTO<WarehouseShareDTO> pageEntityDTO = new PageEntityDTO<>();
        pageEntityDTO.setPageNum(pageNum);
        pageEntityDTO.setTotalNum(totalNum);
        pageEntityDTO.setRecordList(warehouseShareDTOList);
        return Result.wrapSuccessfulResult(pageEntityDTO);
    }

    /**
     * 审核通过
     *
     * @param id
     * @return
     */
    @Override
    public Result<Boolean> checkPass(Long id, Long userId) {
        boolean result = warehouseShareService.checkPass(id);
        if (result) {
            if (log.isInfoEnabled()) {
                log.info("后台用户[{}]审核库存共享配件[{}]通过", userId, id);
            }
        }
        return Result.wrapSuccessfulResult(result);
    }

    /**
     * 审核不通过
     *
     * @param id
     * @param remark
     * @return
     */
    @Override
    public Result<Boolean> checkNotPass(Long id, String remark, Long userId) {
        boolean result = warehouseShareService.checkNotPass(id, remark);
        if (result) {
            if (log.isInfoEnabled()) {
                log.info("后台用户[{}]审核库存共享配件[{}]不通过", userId, id);
            }
        }
        return Result.wrapSuccessfulResult(result);
    }
}
