package com.tqmall.legend.biz.warehouse.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.warehouse.WarehouseShareService;
import com.tqmall.legend.dao.warehouseshare.WarehouseShareDao;
import com.tqmall.legend.entity.shop.CustomerJoinAudit;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.warehouseshare.WarehouseShare;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareCountVO;
import com.tqmall.legend.pojo.warehouseshare.WarehouseShareGoodsDetail;
import com.tqmall.legend.pojo.warehouseshare.WarehouseShareVO;
import com.tqmall.legend.rpc.crm.CrmCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tanghao on 16/11/12.
 */
@Service
@Slf4j
public class WarehouseShareServiceImpl implements WarehouseShareService {
    @Autowired
    private WarehouseShareDao warehouseShareDao;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CrmCustomerService crmCustomerService;

    @Override
    public Integer batchInsert(final List<WarehouseShare> warehouseShares) {
        /**
         * 校验参数
         */
        return new BizTemplate<Integer>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notEmpty(warehouseShares,"待发布对象不能为空");
                for(WarehouseShare ws : warehouseShares){
                    Assert.notNull(ws.getShopId(),"门店id不能为空.");
                    Assert.notNull(ws.getGoodsName(),"商品名称不能为空.");
                    Assert.notNull(ws.getGoodsId(),"商品id不能为空.");
                    Assert.notNull(ws.getGoodsPrice(),"商品售价不能为空.");
                    Assert.notNull(ws.getGoodsStock(),"商品数量不能为空.");
                    Assert.notNull(ws.getInventoryPrice(),"商品成本不能为空.");
                }
            }

            @Override
            protected Integer process() throws BizException {
                return warehouseShareDao.batchInsert(warehouseShares);
            }
        }.execute();
    }

    @Override
    public Integer update(final WarehouseShare warehouseShare) {
        return new BizTemplate<Integer>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(warehouseShare,"更新对象不能为空.");
                Assert.notNull(warehouseShare.getId(),"id不能为空.");
            }

            @Override
            protected Integer process() throws BizException {
                return warehouseShareDao.updateById(warehouseShare);
            }
        }.execute();
    }

    @Override
    public List<WarehouseShare> querySaleListByGoodsStatus(final Long shopId, final Integer goodsStatus, final Integer page, final Integer size) {
        return new BizTemplate<List<WarehouseShare>>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId,"门店id不能为空.");
                Assert.notNull(page,"页数不能为空.");
            }

            @Override
            protected List<WarehouseShare> process() throws BizException {
                Map param = new HashMap<>();
                param.put("shopId",shopId);
                param.put("goodsStatus",goodsStatus);
                Set orderSet = Sets.newHashSet();
                orderSet.add("gmt_modified desc");
                param.put("sorts",orderSet);
                param.put("offset",page <= 1 ? 0 :(page-1)* size);
                param.put("limit",size);
                return warehouseShareDao.select(param);
            }
        }.execute();
    }

    @Override
    public Integer queryCountForSaleListByGoodsStatus(final Long shopId, final Integer goodsStatus) {

        return new BizTemplate<Integer>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId,"门店id不能为空.");
            }

            @Override
            protected Integer process() throws BizException {
                Map param = Maps.newHashMap();
                param.put("shopId",shopId);
                param.put("goodsStatus",goodsStatus);
                return warehouseShareDao.selectCount(param);
            }
        }.execute();

    }

    /**
     * 查询库存共享配件列表
     *
     * @param goodsCate 配件类型
     * @param goodsName 配件名称
     * @return
     */
    @Override
    public Page<WarehouseShareVO> getWarehouseShareGoodsListByCondition(final Long shopId, final String goodsCate, final String goodsName, final int region, final Pageable pageable) {
        return new BizTemplate<Page<WarehouseShareVO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Page<WarehouseShareVO> process() throws BizException {
                Long provinceId = null;
                Long cityId = null;
                if (region > 1) {
                    Shop shop = shopService.selectById(shopId);
                    if (shop == null) {
                        throw new BizException("门店不存在");
                    }
                    if (region == 2) {
                        provinceId = shop.getProvince();
                    } else if (region == 3) {
                        provinceId = shop.getProvince();
                        cityId = shop.getCity();
                    }
                }
                int total = warehouseShareDao.countByCondition(goodsCate, goodsName, provinceId, cityId);

                int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();
                int limit = pageable.getPageSize();
                List<WarehouseShareVO> warehouseShareVOList = warehouseShareDao.getListByCondition(goodsCate, goodsName, provinceId, cityId, offset, limit);
                return new DefaultPage<>(warehouseShareVOList, pageable, total);
            }
        }.execute();
    }

    /**
     * 查询库存共享配件详情
     *
     * @param id
     * @return
     */
    @Override
    public WarehouseShareGoodsDetail getWarehouseShareGoodsDetail(Long id, Long shopId) {
        WarehouseShareGoodsDetail warehouseShareGoodsDetail = warehouseShareDao.getWarehouseShareGoodsDetail(id);
        if (warehouseShareGoodsDetail == null) {
            throw new BizException("配件不存在");
        }
        List<Long> shopIds = Lists.newArrayList(shopId, warehouseShareGoodsDetail.getShopId());
        List<Shop> shopList = shopService.getListByIds(shopIds);
        if (CollectionUtils.isEmpty(shopList)) {
            throw new BizException("门店不存在");
        }
        for (Shop shop : shopList) {
            try {
                CustomerJoinAudit customerJoinAudit = crmCustomerService.showShopInformation(Long.valueOf(shop.getUserGlobalId()));
                String longitude = customerJoinAudit.getLongitude();
                String latitude = customerJoinAudit.getLatitude();
                if (shop.getId().equals(warehouseShareGoodsDetail.getShopId())) {
                    warehouseShareGoodsDetail.setLongitude(longitude);
                    warehouseShareGoodsDetail.setLatitude(latitude);
                }
                if (shop.getId().equals(shopId)) {
                    warehouseShareGoodsDetail.setSelfLongitude(longitude);
                    warehouseShareGoodsDetail.setSelfLatitude(latitude);
                }
            } catch (BizException e) {
                log.error(e.getErrorMessage(), e);
            } catch (Exception e) {
                log.error("[调用crm-dubbo接口失败]:",e);
            }
        }

        return warehouseShareGoodsDetail;
    }

    @Override
    public WarehouseShareCountVO querySaleCount(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空.");
        return warehouseShareDao.querySaleCount(shopId);
    }

    @Override
    public List<Long> queryExistGoodsId(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空.");
        return warehouseShareDao.queryExistGoodsId(shopId);
    }

    /**
     * 查询库存共享配件列表
     *
     * @param goodsCate 配件类型
     * @param goodsName 配件名称
     * @param shopId
     * @param status    审核状态
     * @param pageNum
     * @param pageSize  @return
     */
    @Override
    public Page<WarehouseShare> searchWarehouseSharePage(String goodsCate, String goodsName, Long shopId, Integer status, Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = new PageRequest((pageNum < 1 ? 1 : pageNum) - 1, pageSize < 1 ? 1 : pageSize);
        int total = warehouseShareDao.countByParam(goodsCate, goodsName, shopId, status);
        List<WarehouseShare> list = warehouseShareDao.searchListByParam(goodsCate, goodsName, shopId, status, pageRequest.getOffset(), pageRequest.getPageSize());
        return new DefaultPage<>(list, pageRequest, total);
    }

    @Override
    public boolean checkPass(Long id) {
        int result = warehouseShareDao.checkPass(id);
        return result == 1;
    }

    @Override
    public boolean checkNotPass(Long id, String remark) {
        int result = warehouseShareDao.checkNotPass(id, remark);
        return result == 1;
    }
}
