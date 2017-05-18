package com.tqmall.legend.biz.goods.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.itemcenter.object.result.goods.*;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.bo.goods.GoodsBo;
import com.tqmall.legend.biz.bo.goods.GoodsDTO;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.goods.FinalInventoryDao;
import com.tqmall.legend.dao.goods.GoodsDao;
import com.tqmall.legend.entity.goods.*;
import com.tqmall.legend.facade.goods.*;
import com.tqmall.legend.object.enums.warehouse.ZeroStockRangeEnum;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 配件service
 */
@Service
@Slf4j
public class GoodsServiceImpl extends BaseServiceImpl implements GoodsService {

    @Autowired
    GoodsDao goodsDao;
    @Autowired
    private FinalInventoryDao finalInventoryDao;
    @Autowired
    ShopService shopService;
    @Autowired
    GoodsBrandFacade goodsBrandFacade;
    @Autowired
    GoodsAttrbuteFacade goodsAttrbuteFacade;
    @Autowired
    GoodsUnitFacade goodsUnitFacade;
    @Autowired
    GoodsAttrRelFacade goodsAttrRelFacade;
    @Autowired
    GoodsCarFacade goodsCarFacade;

    @Override
    public Optional<Goods> selectById(@NotNull Long id) {
        Goods goods = goodsDao.selectById(id);
        return Optional.fromNullable(goods);
    }


    /**
     * 根据goodsIds查询
     *
     * @param goodsIds
     * @return
     */
    @Override
    public List<Goods> selectByIds(Long[] goodsIds) {
        List<Goods> goodses = null;
        if (goodsIds == null || goodsIds.length == 0) {
            goodses = new ArrayList<Goods>();
        } else {
            goodses = goodsDao.selectByIds(goodsIds);
        }
        return goodses;
    }

    @Transactional
    @Override
    public Result addWithAttrCar(GoodsBo goodsBo) {
        Goods goods = goodsBo.getGoods();
        Long shopId = goodsBo.getShopId();
        Long userId = goodsBo.getUserId();
        Long shortageNum = goods.getShortageNumber() == null ? 0l : goods.getShortageNumber();

        goods.setShopId(shopId);
        goods.setCreator(userId);
        goods.setModifier(userId);
        goods.setStock(BigDecimal.ZERO);
        goods.setShortageNumber(shortageNum);

        if (goods.getBrandId() == null) {
            goods.setBrandId(0L);
        }
        List<Goods> goodsList = null;
        /**
         * 如果是淘汽商品,则sn没有唯一性,而是sn+format+name作为唯一性
         */
        if (goods.getTqmallGoodsSn() != null && !"0".equals(goods.getTqmallGoodsSn())) {
            goodsList = selectByTqmallGoods(new String[]{goods.getTqmallGoodsSn()}, goods.getName(), goods.getFormat(), shopId);
        } else {
            goodsList = goodsDao.selectByGoodsSnAndShopId(goods.getGoodsSn(), goods.getTqmallGoodsSn(), shopId);

        }
        if (!CollectionUtils.isEmpty(goodsList)) {
            return Result.wrapErrorResult("-2", "商品已经存在");
        }

        List<GoodsCarDTO> goodsCarList = goodsBo.getGoodsCarList();

        if (!CollectionUtils.isEmpty(goodsCarList)) {
            goods.setCarInfo(new Gson().toJson(goodsCarList));
        }

        // 添加品牌到goods_brand表开始
        if (goods.getTqmallStatus() == 1 || goods.getTqmallStatus() == 3) {
            GoodsBrandDTO goodsBrand = addShopSelfBrand(goods, shopId, userId);
            goods.setBrandId(goodsBrand.getId());
        }

        // 如果分类ID为-1新增分类
        Long catId = goods.getCatId() == null ? (goods.getStdCatId() == null ? 0L : goods.getStdCatId()) : goods.getCatId();

        // 添加单位到goods_unit表，做提示用
        String measureUnit = goods.getMeasureUnit();
        if (StringUtils.isNotBlank(measureUnit)) {
            GoodsUnitDTO goodsUnit = new GoodsUnitDTO();
            goodsUnit.setShopId(shopId);
            goodsUnit.setCreator(userId);
            goodsUnit.setName(goods.getMeasureUnit());
            goodsUnitFacade.addWithoutRepeat(goodsUnit);
        }
        //通过brandId获取brandName
        Integer goodsResult = goodsDao.insert(goods);

        Long goodsId = goods.getId();

        List<GoodsAttrRelDTO> goodsAttrRelList = goodsBo.getGoodsAttrRelList();

        if (!CollectionUtils.isEmpty(goodsAttrRelList)) {
            for (GoodsAttrRelDTO goodsAttrRel : goodsAttrRelList) {

                // 属性ID不存在，说明是新增属性，需要添加到goods_attribute表中
                Long attrId = goodsAttrRel.getAttrId();
                if (attrId == null) {
                    GoodsAttributeDTO goodsAttribute = new GoodsAttributeDTO();

                    // 将value处理成数组形式，并转换成json字符串
                    List<String> attrValueList = new ArrayList<>();
                    attrValueList.add(goodsAttrRel.getAttrValue());
                    String attrValueStr = new Gson().toJson(attrValueList);

                    // 这里分类ID就用分类ID和goods_category表的goods_type_id暂时没有关系
                    goodsAttribute.setGoodsTypeId(Integer.valueOf(catId + ""));
                    goodsAttribute.setCreator(userId);
                    //如果goodsAttrRel.getAttrName()为null则存入""
                    goodsAttribute.setAttrName(goodsAttrRel.getAttrName() == null ? "" : goodsAttrRel.getAttrName());
                    goodsAttribute.setAttrValue(attrValueStr);
                    goodsAttribute.setAttrInputType(0);
                    goodsAttribute.setShopId(shopId);
                    goodsAttribute.setTqmallGoodsAttrId(goodsAttrRel.getTqmallGoodsAttrId());
                    goodsAttribute.setTqmallStatus(goodsAttrRel.getTqmallStatus());
                    goodsAttrbuteFacade.add(goodsAttribute);
                    // 获取插入的ID并设置到关联表中
                    Long newAttrId = goodsAttribute.getId();
                    goodsAttrRel.setAttrId(newAttrId);
                    // 如果goods_attribute里面有记录，更新attr_value
                } else {
                    // 自定义配件，添加商品属性到goods_attribute
                    if (goods.getTqmallStatus() == 4) {
                        GoodsAttributeDTO goodsAttribute = goodsAttrbuteFacade.selectById(attrId);
                        List<String> attrValueList = goodsAttribute.getAttrValueList();
                        if (!attrValueList.contains(goodsAttrRel.getAttrValue())) {
                            attrValueList.add(goodsAttrRel.getAttrValue());
                            goodsAttribute.setAttrValue(new Gson().toJson(attrValueList));
                            goodsAttrbuteFacade.updateById(goodsAttribute);
                        }
                    }

                }

                goodsAttrRel.setShopId(shopId);
                goodsAttrRel.setGoodsId(goodsId);
                goodsAttrRel.setCreator(userId);
                goodsAttrRelFacade.save(goodsAttrRel);
            }
        }

        if (!CollectionUtils.isEmpty(goodsCarList)) {
            for (GoodsCarDTO goodsCar : goodsCarList) {
                goodsCar.setShopId(shopId);
                goodsCar.setGoodsId(goodsId);
                goodsCar.setCreator(userId);
                goodsCarFacade.save(goodsCar);
            }
        }
        if (goodsResult > 0) {
            return Result.wrapSuccessfulResult(goods);
        } else {
            return Result.wrapErrorResult("-1", "新增失败");
        }
    }

    @Transactional
    @Override
    public Result updateWithAttrCar(GoodsBo goodsBo) {
        Goods goods = goodsBo.getGoods();
        Long shopId = goodsBo.getShopId();
        Long userId = goodsBo.getUserId();
        Long shortageNum = goods.getShortageNumber() == null ? Long.getLong("0") : goods.getShortageNumber();

        goods.setShopId(shopId);
        goods.setModifier(userId);
        goods.setShortageNumber(shortageNum);

        if (goods.getBrandId() == null) {
            goods.setBrandId(0l);
        }

        // 添加品牌到goods_brand表开始
        if (goods.getTqmallStatus() == 1 || goods.getTqmallStatus() == 3) {
            GoodsBrandDTO goodsBrand = addShopSelfBrand(goods, shopId, userId);
            goods.setBrandId(goodsBrand.getId());
        }

        // 如果分类ID为-1新增分类
        Long catId = goods.getCatId() == null ? goods.getStdCatId() : goods.getCatId();

        List<GoodsCarDTO> goodsCarList = goodsBo.getGoodsCarList();

        if (!CollectionUtils.isEmpty(goodsCarList)) {
            goods.setCarInfo(new Gson().toJson(goodsCarList));
        }
        //如果carInfo为null，则设置成""
        if (goods.getCarInfo() == null) {
            goods.setCarInfo("");
        }
        //这边根据开始跟新
        Integer goodsResult = goodsDao.updateById(goods);

        Long goodsId = goods.getId();

        goodsAttrRelFacade.deleteByGoodsIdAndShopId(goodsId, shopId);
        List<GoodsAttrRelDTO> goodsAttrRelList = goodsBo.getGoodsAttrRelList();
        if (!CollectionUtils.isEmpty(goodsAttrRelList)) {
            for (GoodsAttrRelDTO goodsAttrRel : goodsAttrRelList) {
                // 属性ID不存在，说明是新增属性，需要添加到goods_attribute表中
                Long attrId = goodsAttrRel.getAttrId();
                if (attrId == null) {
                    GoodsAttributeDTO goodsAttribute = new GoodsAttributeDTO();

                    // 将value处理成数组形式，并转换成json字符串
                    List<String> attrValueList = new ArrayList<>();
                    attrValueList.add(goodsAttrRel.getAttrValue());
                    String attrValueStr = new Gson().toJson(attrValueList);

                    // 这里分类ID就用分类ID和goods_category表的goods_type_id暂时没有关系
                    goodsAttribute.setGoodsTypeId(Integer.valueOf(catId + ""));
                    goodsAttribute.setCreator(userId);
                    goodsAttribute.setAttrName(goodsAttrRel.getAttrName() == null ? "" : goodsAttrRel.getAttrName());
                    goodsAttribute.setAttrValue(attrValueStr);
                    goodsAttribute.setAttrInputType(0);
                    goodsAttribute.setShopId(shopId);
                    goodsAttribute.setTqmallGoodsAttrId(0L);
                    goodsAttribute.setTqmallStatus(2);
                    goodsAttrbuteFacade.add(goodsAttribute);
                    // 获取插入的ID并设置到关联表中
                    Long newAttrId = goodsAttribute.getId();
                    goodsAttrRel.setAttrId(newAttrId);
                    // 如果goods_attribute里面有记录，更新attr_value
                } else {
                    // 自定义配件，添加商品属性到goods_attribute
                    if (goods.getTqmallStatus() == 4) {
                        GoodsAttributeDTO goodsAttribute = goodsAttrbuteFacade.selectById(attrId);
                        List<String> attrValueList = goodsAttribute.getAttrValueList();
                        if (!attrValueList.contains(goodsAttrRel.getAttrValue())) {
                            attrValueList.add(goodsAttrRel.getAttrValue());
                            goodsAttribute.setAttrValue(new Gson().toJson(attrValueList));
                            goodsAttrbuteFacade.updateById(goodsAttribute);
                        }
                    }

                }

                goodsAttrRel.setShopId(shopId);
                goodsAttrRel.setGoodsId(goodsId);
                goodsAttrRel.setCreator(userId);
                goodsAttrRelFacade.save(goodsAttrRel);
            }
        }

        goodsCarFacade.deleteByGoodsIdAndShopId(goodsId, shopId);

        if (!CollectionUtils.isEmpty(goodsCarList)) {
            for (GoodsCarDTO goodsCar : goodsCarList) {
                goodsCar.setShopId(shopId);
                goodsCar.setGoodsId(goodsId);
                goodsCar.setModifier(userId);
                goodsCarFacade.save(goodsCar);
            }
        }

        // 添加单位到goods_unit表，做提示用
        String measureUnit = goods.getMeasureUnit();
        if (StringUtils.isNotBlank(measureUnit)) {
            GoodsUnitDTO goodsUnit = new GoodsUnitDTO();
            goodsUnit.setShopId(shopId);
            goodsUnit.setCreator(userId);
            goodsUnit.setName(goods.getMeasureUnit());
            goodsUnitFacade.addWithoutRepeat(goodsUnit);
        }

        if (goodsResult > 0) {
            return Result.wrapSuccessfulResult(true);
        } else {
            return Result.wrapErrorResult("-1", "更新失败");
        }
    }

    @Override
    public Integer updateById(Goods goods) {
        return goodsDao.updateById(goods);
    }

    @Override
    @Transactional
    public Result deleteByIdAndShopId(Long id, Long shopId) {
        Integer goodsResult = goodsDao.deleteByIdAndShopId(id, shopId);
        Integer attrRelResult = goodsAttrRelFacade.deleteByGoodsIdAndShopId(id, shopId);
        Integer carResult = goodsCarFacade.deleteByGoodsIdAndShopId(id, shopId);
        if (goodsResult + attrRelResult + carResult > 0) {
            return Result.wrapSuccessfulResult(true);
        } else {
            return Result.wrapErrorResult("-1", "更新失败");
        }
    }

    @Override
    public Optional<Goods> selectById(Long id, Long shopId) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>(2);
        paramMap.put("id", id);
        paramMap.put("shopId", shopId);

        List<Goods> goodsList = null;
        try {
            goodsList = goodsDao.select(paramMap);
        } catch (Exception e) {
            log.error("[DB]获取配件失败,异常原因:{}", e);
            return Optional.absent();
        }

        if (CollectionUtils.isEmpty(goodsList)) {
            return Optional.absent();
        }

        return Optional.fromNullable(goodsList.get(0));
    }


    @Override
    public List<Goods> queryGoods(@NotNull GoodsQueryParam queryParam) {

        log.info("[DB]查询配件 入参:{}", ObjectUtils.objectToJSON(queryParam));

        // 参数
        HashMap<String, Object> paramMap = new HashMap<String, Object>();

        // 门店ID
        Long shopId = queryParam.getShopId();
        if (shopId != null) {
            paramMap.put("shopId", shopId);
        }
        // 配件型号(精准)
        String format = queryParam.getFormat();
        if (!StringUtils.isEmpty(format)) {
            paramMap.put("format", format);
        }
        // 配件型号(模糊)
        String goodsFormatLike = queryParam.getGoodsFormatLike();
        if (!StringUtils.isEmpty(goodsFormatLike)) {
            paramMap.put("goodsFormatLike", goodsFormatLike);
        }
        // 淘汽商品ID
        Long tqmallGoodsId = queryParam.getTqmallGoodsId();
        if (tqmallGoodsId != null) {
            paramMap.put("tqmallGoodsId", tqmallGoodsId);
        }
        // 是否删除
        DBStatusEnum dbStatusEnum = queryParam.getDbStatusEnum();
        if (dbStatusEnum != null) {
            paramMap.put("isDeleted", dbStatusEnum.getCode());
        }
        // 配件类别
        GoodsTagEnum goodsTagEnum = queryParam.getGoodsTagEnum();
        if (goodsTagEnum != null) {
            paramMap.put("goodsTag", goodsTagEnum.getCode());
        }
        // 配件上下架状态
        GoodsOnsaleEnum onsaleEnum = queryParam.getOnsaleEnum();
        if (onsaleEnum != null) {
            paramMap.put("onsaleStatus", onsaleEnum.getCode());
        }
        // 库存状态
        ZeroStockRangeEnum zeroStockRangeEnum = queryParam.getZeroStockRangeEnum();
        if (zeroStockRangeEnum != null) {
            paramMap.put("zeroStockRange", zeroStockRangeEnum.getKey());
        }
        // 仓位(模糊)
        String depotLike = queryParam.getDepotLike();
        if (!StringUtils.isEmpty(depotLike)) {
            paramMap.put("depotLike", depotLike);
        }
        // 品牌
        String brandId = queryParam.getBrandId();
        if (!StringUtils.isEmpty(brandId)) {
            paramMap.put("brandId", brandId);
        }
        // 排序
        List<String> sorts = queryParam.getSorts();
        if (!CollectionUtils.isEmpty(sorts)) {
            paramMap.put("sorts", sorts);
        }
        // 查询记录起始位置
        String offset = queryParam.getOffset();
        if (!StringUtils.isEmpty(offset)) {
            paramMap.put("offset", offset);
        }
        // 查询记录长度
        String limit = queryParam.getLimit();
        if (!StringUtils.isEmpty(limit)) {
            paramMap.put("limit", limit);
        }
        // 配件分类集合
        Long[] catIds = queryParam.getCatIds();
        if (catIds != null && catIds.length > 0) {
            paramMap.put("catIds", catIds);
        }
        // 查询记录长度
        String carInfoLike = queryParam.getCarInfoLike();
        if (!StringUtils.isEmpty(carInfoLike)) {
            paramMap.put("carInfoLike", carInfoLike);
        }

        List<Goods> goodses = null;
        try {
            goodses = goodsDao.select(paramMap);
        } catch (Exception e) {
            log.error("[DB]查询配件异常,异常原因:{}", e);
            return new ArrayList<Goods>();
        }

        if (goodses == null) {
            return new ArrayList<Goods>();
        }

        return goodses;
    }


    @Override
    public List<Goods> selectByTqmallIds(Long[] tqmallGoodsId, @NotNull Long shopId) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>(2);
        paramMap.put("tqmallGoodsIds", tqmallGoodsId);
        paramMap.put("shopId", shopId);

        List<Goods> goodses = null;
        try {
            goodses = goodsDao.select(paramMap);
        } catch (Exception e) {
            log.error("[DB]获取配件失败,异常原因:{}", e);
            return new ArrayList<Goods>();
        }

        if (goodses == null) {
            return new ArrayList<Goods>();
        }

        return goodses;
    }


    @Override
    public List<Goods> selectByTqmallGoods(String[] tqmallGoodsSns, String name, String format, Long shopId) {
        List<Goods> goodses = null;
        try {
            goodses = goodsDao.selectByTqmallGoodsSnsAndShopId(tqmallGoodsSns, shopId, name, format);
        } catch (Exception e) {
            log.error("[DB]获取配件异常,异常信息:{}", e);
            return new ArrayList<Goods>();
        }

        if (goodses == null) {
            goodses = new ArrayList<Goods>();
        }

        return goodses;
    }

    //零件号是否存在

    private String checkFormat(Long shopId, String format) {
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("format", format);
        int count = goodsDao.selectCount(param);
        if (count > 0) {
            return "该零件号配件已存在";
        }
        return null;
    }

    @Transactional
    @Override
    public GoodsDTO addBasicGoods(GoodsDTO goods, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        //若品牌不存在则创建
        Long brandId = saveGoodsBrand(goods, userInfo);
        goods.setBrandId(brandId);
        String info = checkFormat(shopId, goods.getFormat());
        if (info != null) {
            throw new BizException(info);
        }
        // 判断该商品编号是否已存在
        List<Goods> goodsList = goodsDao.selectByGoodsSnAndShopId(goods.getGoodsSn(), goods.getTqmallGoodsSn(), shopId);
        if (!CollectionUtils.isEmpty(goodsList)) {
            throw new BizException("商品已经存在");
        }
        Long userId = userInfo.getUserId();
        // wrapper Goods entity
        Long shortageNum = goods.getShortageNumber() == null ? Long.getLong("0") : goods.getShortageNumber();
        goods.setShopId(shopId);
        goods.setCreator(userId);
        goods.setStock(BigDecimal.ZERO);
        goods.setShortageNumber(shortageNum);
        goods.setInventoryPrice(BigDecimal.ZERO);
        if (StringUtils.isNotBlank(goods.getCarInfo())) {
            String carInfo = "[" + goods.getCarInfo() + "]";
            goods.setCarInfo(carInfo);
        }
        Goods saveGoods = new Goods();
        BeanUtils.copyProperties(goods, saveGoods);
        goodsDao.insert(saveGoods);
        goods.setId(saveGoods.getId());
        String measureUnitName = goods.getMeasureUnit();
        if (StringUtils.isNotEmpty(measureUnitName)) {
            boolean unitIsExit = goodsUnitFacade.checkGoodsUnitIsExist(measureUnitName, shopId);
            if (unitIsExit == false) {
                GoodsUnitDTO goodsUnit = new GoodsUnitDTO();
                goodsUnit.setShopId(shopId);
                goodsUnit.setCreator(userId);
                goodsUnit.setName(goods.getMeasureUnit());
                // 插入数量单位，如果有异常，捕获掉，确保商品正常保存
                try {
                    goodsUnitFacade.addWithoutRepeat(goodsUnit);
                } catch (Exception e) {
                    log.error("商品单位保存异常,异常信息", e);
                }
            }
        }
        Long goodsId = saveGoods.getId();
        goodsCarFacade.deleteByGoodsIdAndShopId(goodsId, shopId);


        String carInfo = goods.getCarInfo();
        if (StringUtils.isBlank(carInfo)) {
            return goods;
        }
        try {
            List<GoodsCarDTO> goodsCarList = new Gson().fromJson(carInfo, new TypeToken<List<GoodsCarDTO>>() {
            }.getType());
            goods.setCarInfoList(goodsCarList);
            if (!CollectionUtils.isEmpty(goodsCarList)) {
                GoodsCarDTO goodsCar = goodsCarList.get(0);
                goodsCar.setShopId(shopId);
                goodsCar.setGoodsId(goodsId);
                goodsCar.setModifier(userId);
                goodsCarFacade.save(goodsCar);
            }
        } catch (Exception e) {
            log.error("配件车型保存异常,车型信息{},异常信息", carInfo, e);
        }
        return goods;
    }

    @Override
    public List<Goods> selectPaintByIds(List<Long> goodsIds) {
        return goodsDao.selectPaintByIds(goodsIds);
    }


    /**
     * 添加门店自己的物料品牌 如果已经存在则不添加
     *
     * @param goods
     * @param shopId
     * @param userId
     */
    private GoodsBrandDTO addShopSelfBrand(Goods goods, Long shopId, Long userId) {
        GoodsBrandDTO goodsBrand = new GoodsBrandDTO();
        goodsBrand.setShopId(shopId);
        goodsBrand.setCreator(userId);
        goodsBrand.setTqmallBrandId(goods.getBrandId());
        goodsBrand.setBrandName(goods.getBrandName());
        goodsBrand.setBrandDesc(goods.getBrandName());
        goodsBrand.setFirstLetter("");
        goodsBrand.setSort(0);
        goodsBrand.setTqmallStatus(1);
        return goodsBrandFacade.addWithoutRepeat(goodsBrand);
    }

    @Override
    public int updateStock(Long goodsId, BigDecimal stock, Long optUserId) {
        return goodsDao.updateStock(goodsId, stock, optUserId);
    }


    @Override
    public List<Goods> selectByIdsAndShopId(List<Long> goodsIdsList, Long shopId) {
        return goodsDao.selectByIdsAndShopId(goodsIdsList, shopId);
    }

    @Override
    @Transactional
    public Result mergeGoods(Long[] srcGoodsIds, Long destGoodsId, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();

        // 1.校验数据
        List<Goods> srcGoodses = goodsDao.selectByIdsAndShopId(Arrays.asList(srcGoodsIds), shopId);
        if (CollectionUtils.isEmpty(srcGoodses)
                || srcGoodsIds.length != srcGoodses.size()) {
            log.error("[配件合并]配件合并失败,源配件不存在,配件IDs:{}", srcGoodsIds);
            return Result.wrapErrorResult("", "配件合并失败,被合并的配件不存在");
        }
        Optional<Goods> goodsOptional = this.selectById(destGoodsId, shopId);
        if (!goodsOptional.isPresent()) {
            log.error("[配件合并]配件合并失败,目标配件不存在,配件ID:{}", destGoodsId);
            return Result.wrapErrorResult("", "配件合并失败,目标配件不存在");
        }
        Goods destGoods = goodsOptional.get();

        // 2. 累计被合并配件的总库存\总成本价
        BigDecimal srcStockTotal = BigDecimal.ZERO;
        BigDecimal srcInventoryAmountTotal = BigDecimal.ZERO;
        // 仓库结存
        List<FinalInventory> finalInventoryList = new ArrayList<FinalInventory>();
        FinalInventory finalInventory = null;
        for (Goods srcGoods : srcGoodses) {
            BigDecimal stock = srcGoods.getStock();
            stock = (stock == null) ? BigDecimal.ZERO : stock;
            srcStockTotal = srcStockTotal.add(stock);
            BigDecimal inventoryPrice = srcGoods.getInventoryPrice();
            inventoryPrice = (inventoryPrice == null) ? BigDecimal.ZERO : inventoryPrice;
            srcInventoryAmountTotal = srcInventoryAmountTotal.add(stock.multiply(inventoryPrice));
            log.info("[配件合并]源配件 配件ID:{} stock:{} inventoryPrice:{}", srcGoods.getId(), stock, inventoryPrice);

            //结存记录
            finalInventory = new FinalInventory();
            finalInventory.setGoodsId(srcGoods.getId());
            finalInventory.setInventoryType("MERGE");
            finalInventory.setFinalDate(new Date());
            finalInventory.setGoodsCount(BigDecimal.ZERO);
            finalInventory.setGoodsFinalPrice(BigDecimal.ZERO);
            finalInventory.setShopId(shopId);
            finalInventory.setCreator(userId);
            finalInventory.setModifier(userId);
            finalInventoryList.add(finalInventory);
        }

        // 3. 计算目标配件总库存\总成本
        BigDecimal destOldStock = destGoods.getStock();
        destOldStock = (destOldStock == null) ? BigDecimal.ZERO : destOldStock;
        BigDecimal destOldInventoryPrice = destGoods.getInventoryPrice();
        destOldInventoryPrice = (destOldInventoryPrice == null) ? BigDecimal.ZERO : destOldInventoryPrice;
        log.info("[配件合并]目标配件 配件ID:{} stock:{} inventoryPrice:{}", destGoods.getId(), destOldStock, destOldInventoryPrice);

        // 目标配件总库存 = 被合并配件总库存+ 目标配件库存
        BigDecimal destStock = srcStockTotal.add(destOldStock);
        // 目标配件总成本 = 被合并配件总成本+ 目标配件总成本
        BigDecimal destInventoryAmountTotal = srcInventoryAmountTotal.add(destOldStock.multiply(destOldInventoryPrice));
        // 目标配件新成本价 = 合并后总成本/总库存
        BigDecimal destInventoryPrice = BigDecimal.ZERO;
        if (destStock.compareTo(BigDecimal.ZERO) != 0
                && destInventoryAmountTotal.compareTo(BigDecimal.ZERO) != 0) {
            destInventoryPrice = destInventoryAmountTotal.divide(destStock, 8, RoundingMode.HALF_UP);
        }
        destGoods.setStock(destStock);
        destGoods.setInventoryPrice(destInventoryPrice);
        destGoods.setOnsaleStatus(GoodsOnsaleEnum.UPSHELF.getCode());

        // 目标配件的结存记录
        FinalInventory destFinalInventory = new FinalInventory();
        destFinalInventory.setGoodsId(destGoodsId);
        // TODO 梳理配件结存类型
        destFinalInventory.setInventoryType("MERGE");
        destFinalInventory.setFinalDate(new Date());
        destFinalInventory.setGoodsCount(destStock);
        destFinalInventory.setGoodsFinalPrice(destInventoryPrice);
        destFinalInventory.setShopId(shopId);
        destFinalInventory.setCreator(userId);
        destFinalInventory.setModifier(userId);
        finalInventoryList.add(destFinalInventory);

        // 5. 被合并的配件下架{库存:0 ;成本价:0 ; 下架}
        goodsDao.batchDownShelf(Arrays.asList(srcGoodsIds), shopId);
        // 6. 目标配件更新(新库存\新成本价)
        goodsDao.updateById(destGoods);
        // 7. 批量保存盘点记录
        finalInventoryDao.batchInsert(finalInventoryList);
        log.info("[配件合并]合并成功 目标配件ID:{} stock:{} inventoryPrice:{}", destGoodsId, destStock, destInventoryPrice);

        return Result.wrapSuccessfulResult(true);
    }


    @Override
    public int statisticsWarningTotal(@NotNull Long shopId) {

        Map<String, Object> searchParams = new HashMap<String, Object>(4);
        // 1:在售的 AND 库存<预警数量
        searchParams.put("onsaleStatus", 1);
        searchParams.put("onlywarning", Boolean.TRUE);
        searchParams.put("shopId", shopId);
        List<String> sorts = new ArrayList<String>(1);
        sorts.add("id DESC");
        searchParams.put("sorts", sorts);

        List<Goods> goodses = null;
        try {
            goodses = goodsDao.select(searchParams);
        } catch (Exception e) {
            log.error("查询配件异常,异常信息", e);
            return 0;
        }
        if (CollectionUtils.isEmpty(goodses)) {
            return 0;
        }

        return goodses.size();
    }

    @Override
    public Integer getNMonthUnChangeGoodsNum(Long shopId, Integer monthNum) {
        Assert.notNull(shopId, "门店id不能为空.");
        Assert.notNull(monthNum, "未改变月数不能为空");
        return goodsDao.getNMonthUnChangeGoodsNum(shopId, monthNum);
    }

    @Override
    public Goods getNMonthUnChangeGoodsCostAndPrice(Long shopId, Integer monthNum) {
        Assert.notNull(shopId, "门店id不能为空.");
        Assert.notNull(monthNum, "未改变的月数不能为空.");
        return goodsDao.getNMonthUnChangeGoodsCostAndPrice(shopId, monthNum);
    }

    @Override
    public BigDecimal getAvgGoodsStock(Long shopId, Integer monthNum) {
        Assert.notNull(shopId, "门店id不能为空.");
        Assert.notNull(monthNum, "未改变的月数不能为空");
        StringBuilder tableName = new StringBuilder("legend_goods_log_");
        String date = DateUtil.getLastMonthDay(-monthNum);
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        tableName.append(year);
        tableName.append("_");
        tableName.append(month);
        return goodsDao.getAvgGoodsStock(shopId, tableName.toString());
    }

    @Override
    public List<Goods> selectDeletedGoods(Long[] goodsIds) {
        List<Goods> goodsList = null;
        try {
            goodsList = goodsDao.batchQueryDeletedIds(goodsIds);
        } catch (Exception e) {
            log.error("[DB]查询被删除的配件异常,异常信息:{}", e);
            return new ArrayList<Goods>();
        }

        if (goodsList == null) {
            goodsList = new ArrayList<Goods>();
        }

        return goodsList;
    }

    @Override
    public List<Goods> selectByShopId(Long shopId) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("shopId", shopId);
        List<Goods> goodses = null;
        try {
            goodses = goodsDao.select(param);
        } catch (Exception e) {
            log.error("[DB]获取门店配件异常 异常信息:{}", e);
            return new ArrayList<Goods>();
        }
        if (goodses == null) {
            goodses = new ArrayList<Goods>();
        }

        return goodses;
    }

    @Override
    public String generatGoodsSn(int i, int size) {
        String goodsSn = "";
        if (Long.valueOf(size).compareTo(1000000L) > -1) {
            goodsSn = size + i + 1 + "";
        } else {
            goodsSn = String.format("%06d", Long.valueOf(size + i));
        }
        return goodsSn;
    }

    private Long saveGoodsBrand(GoodsDTO goods, UserInfo userInfo) {
        if (goods.getBrandId() != null) {
            return goods.getBrandId();
        }
        if (StringUtils.isBlank(goods.getBrandName())) {
            return 0L;
        }
        if (goods.getBrandName().length() > 50) {
            throw new BizException("品牌名称过长，限长50字");
        }
        GoodsBrandDTO goodsBrand = new GoodsBrandDTO();
        goodsBrand.setBrandName(goods.getBrandName());
        goodsBrand.setShopId(userInfo.getShopId());
        goodsBrand.setFirstLetter("");
        goodsBrand.setBrandDesc("");
        goodsBrand.setTqmallBrandId(0L);
        goodsBrand.setTqmallStatus(4);
        goodsBrand.setCreator(userInfo.getUserId());
        Long brandId = goodsBrandFacade.addDefinedBrand(goodsBrand);
        return brandId;
    }

    @Override
    public List<Goods> findGoodsByFormat(Long shopId, String... format) {
        return goodsDao.findGoodsByFormat(shopId, format);
    }

    @Override
    public void batchSave(List<Goods> goodses) {
        super.batchInsert(goodsDao, goodses, 1000);
    }

    @Override
    public List<Goods> getGoods(Long shopId, GoodsOnsaleEnum onsaleEnum, ZeroStockRangeEnum zeroStockRangeEnum) {
        // 按照仓位排序
        List<String> sorts = new ArrayList<String>(1);
        sorts.add(" depot ");

        GoodsQueryParam queryParam = new GoodsQueryParam();
        queryParam.setShopId(shopId);
        queryParam.setOnsaleEnum(onsaleEnum);
        queryParam.setZeroStockRangeEnum(zeroStockRangeEnum);
        queryParam.setSorts(sorts);

        return this.queryGoods(queryParam);
    }
    
    @Override
    public List<String> getGoodsLocation(Long shopId) {
        List<String> list = goodsDao.getGoodsLocation(shopId);
        return list;
    }

    @Override
    public List<Goods> findByTqmallGoodsIds(Long shopId, Collection<Long> tqmallGoodsIds) {
        Assert.notNull(shopId);
        if (Langs.isEmpty(tqmallGoodsIds)) {
            return Lists.newArrayList();
        }

        return this.goodsDao.selectByTqmallGoodsIds(shopId, tqmallGoodsIds);
    }
}

