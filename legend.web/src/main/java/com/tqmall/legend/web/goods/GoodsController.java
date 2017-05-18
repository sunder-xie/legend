package com.tqmall.legend.web.goods;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.itemcenter.object.result.goods.*;
import com.tqmall.legend.biz.bo.goods.GoodsBo;
import com.tqmall.legend.biz.bo.goods.GoodsDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.base.BaseEnumBo;
import com.tqmall.legend.entity.goods.*;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.goods.*;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import com.tqmall.legend.facade.goods.vo.TqmallAndLegendGoodsVo;
import com.tqmall.legend.facade.magic.BPGoodsFacade;
import com.tqmall.legend.facade.magic.vo.GoodsExtVo;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.goods.bo.GoodsSearchParam;
import com.tqmall.legend.web.goods.converter.GoodsExportConverter;
import com.tqmall.legend.web.goods.vo.GoodsExportVo;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.magic.object.result.goods.GoodsPaintExtDTO;
import com.tqmall.magic.service.goods.RpcGoodsPaintExtService;
import com.tqmall.search.common.data.FieldsSort;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.goods.param.LegendGoodsRequest;
import com.tqmall.tqmallstall.domain.param.goods.GoodsListQueryRequest;
import com.tqmall.tqmallstall.domain.result.goods.GoodsListForSearchBaseDTO;
import com.tqmall.tqmallstall.domain.result.goods.GoodsListForSearchDTO;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 配件管理
 */
@Controller
@RequestMapping("shop/goods")
@Slf4j
public class GoodsController extends BaseController {

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderInfoService orderInfoService;
    @Autowired
    ShopManagerService shopManagerService;
    @Autowired
    OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsFacade goodsFacade;
    @Autowired
    private SnFactory snFactory;
    @Autowired
    GoodsAttrRelFacade goodsAttrRelFacade;
    @Autowired
    private com.tqmall.tqmallstall.service.goods.GoodsService goodsServices;
    @Autowired
    private RpcGoodsPaintExtService rpcGoodsPaintExtService;
    @Autowired
    private BPGoodsFacade bpGoodsFacade;
    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    WarehouseOutService warehouseOutService;
    @Autowired
    GoodsBrandFacade goodsBrandFacade;
    @Autowired
    GoodsCarFacade goodsCarFacade;
    @Autowired
    GoodsCategoryFacade goodsCategoryFacade;
    @Autowired
    private ShopService shopService;

    /**
     * [应用场景] 淘汽采购页面-签收入库
     *
     * @param goodses 签收入库的配件集合
     * @return
     */
    @RequestMapping(value = "batch_add_with_attr_car/ng")
    @ResponseBody
    public Result batchAddGoods(@RequestBody List<GoodsVo> goodses) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Long userId = UserUtils.getUserIdForSession(request);

        // 1.判断签收入库配件是否存在
        if (CollectionUtils.isEmpty(goodses)) {
            return Result.wrapErrorResult("", "未选择签收入库的配件");
        }

        // 2.包装需要签收入库配件
        List<GoodsBo> goodsBos = new ArrayList<GoodsBo>(goodses.size());
        GoodsBo goodsBo = null;
        for (GoodsVo goodsVo : goodses) {
            goodsBo = wrapperGoodsBO(goodsVo, shopId, userId);
            goodsBos.add(goodsBo);
        }

        // 3.批量签收入库
        try {
            goodsFacade.purchaseTqmallGoodses(goodsBos);
        } catch (Exception e) {
            log.error("[淘汽采购-签收入库]批量入库商品异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "签收入库失败");
        }

        return Result.wrapSuccessfulResult("批量添加商品成功");

    }


    /**
     * 钣喷中心-新增油漆资料
     * <p/>
     * [应用场景]
     * <p/>
     * 1. [老版] 淘汽配件采购页面       对象: 油漆资料 + 自定义配件
     * 2. [新版] 淘汽配件采购页面       对象: 油漆资料 + 自定义配件
     *
     * @param goodsVo 配件资料
     * @return
     */
    @RequestMapping(value = "paint/save", method = RequestMethod.POST)
    @ResponseBody
    public Result paintSave(GoodsVo goodsVo) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Long userId = UserUtils.getUserIdForSession(request);


        // 1.校验:配件基本信息,油漆属性
        if (goodsVo == null) {
            return Result.wrapErrorResult("", "新增油漆资料失败");
        }

        // 净重(必填)
        BigDecimal netWeight = goodsVo.getNetWeight();
        // 带桶重量(必填)
        BigDecimal bucketWeight = goodsVo.getBucketWeight();
        // 带桶和搅拌头的重量(必填)
        BigDecimal stirWeight = goodsVo.getStirWeight();
        if (netWeight == null || bucketWeight == null) {
            return Result.wrapErrorResult("", "油漆类型必填字段不能为空！");
        }

        // 2.油漆资料
        GoodsExtVo goodsExtVo = new GoodsExtVo();
        GoodsBo goodsBo = wrapperGoodsBO(goodsVo, shopId, userId);
        // {1:标记油漆}
        goodsBo.getGoods().setGoodsTag(GoodsTagEnum.PAINT.getCode());
        goodsExtVo.setGoodsBo(goodsBo);
        goodsExtVo.setNetWeight(netWeight);
        goodsExtVo.setBucketWeight(bucketWeight);
        goodsExtVo.setStirWeight(stirWeight);
        goodsExtVo.setPaintLevel(goodsVo.getPaintLevel());
        goodsExtVo.setPaintType(goodsVo.getPaintType());

        try {
            bpGoodsFacade.addBPGoodsInfo(goodsExtVo);
        } catch (Exception e) {
            log.error("[配件创建]创建油漆资料异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "新增油漆资料失败");
        }

        return Result.wrapSuccessfulResult("新增油漆资料成功");
    }


    /**
     * 新增普通配件资料
     * <p/>
     * [应用场景]
     * <p/>
     * 1. [新版] 自定义配件新增页面     对象: 自定义配件
     * 2. [老版] 自定义配件新增页面     对象: 自定义配件
     *
     * @param goodsVo 配件资料
     * @return
     */
    @RequestMapping(value = "common/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(GoodsVo goodsVo) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Long userId = UserUtils.getUserIdForSession(request);

        GoodsBo goodsBo = wrapperGoodsBO(goodsVo, shopId, userId);
        try {
            goodsService.addWithAttrCar(goodsBo);
        } catch (Exception e) {
            log.error("[配件创建]新建配件异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "新建配件异常");
        }

        return Result.wrapSuccessfulResult("新建配件成功");
    }


    /**
     * 商品基本信息保存
     *
     * @param goods
     * @return
     */
    @RequestMapping(value = "basicgoods/save", method = RequestMethod.POST)
    @ResponseBody
    public Result basicGoodsSave(GoodsDTO goods) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        GoodsDTO newGoods = goodsService.addBasicGoods(goods, userInfo);
        return Result.wrapSuccessfulResult(newGoods);
    }


    /**
     * 普通配件 更新入口
     *
     * @param goodsVo
     * @return
     */
    @RequestMapping(value = "common/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(GoodsVo goodsVo) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Long userId = UserUtils.getUserIdForSession(request);

        // 1.校验
        if (goodsVo == null) {
            return Result.wrapErrorResult("", "配件更新失败");
        }

        // 2.wrapper实体
        GoodsBo goodsBo = wrapperGoodsBO(goodsVo, shopId, userId);

        // 3.更新配件
        Result result = null;
        try {
            result = goodsService.updateWithAttrCar(goodsBo);
        } catch (Exception e) {
            log.error("[配件更新]配件信息异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "配件更新异常");
        }

        return result;
    }


    /**
     * 淘汽配件搜索接口
     *
     * @param goodsId    配件ID
     * @param catId      类别ID
     * @param customCatId  自定义类别ID，新版本类别弹框有自定义类别，所以如果传了次字段，则返回空数据
     * @param brandId    品牌ID
     * @param carModelId 车型
     * @param keyword    关键字
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value = "tqmall/search/json", method = RequestMethod.GET)
    @ResponseBody
    public Result tqmallGoodsSearch(@RequestParam(value = "search_goodsId", required = false) Long goodsId,
                                    @RequestParam(value = "search_stdCatId", required = false) Long catId,
                                    @RequestParam(value = "search_catId", required = false) Long customCatId,
                                    @RequestParam(value = "search_brandId", required = false) Long brandId,
                                    @RequestParam(value = "search_carModelId", required = false) Long carModelId,
                                    @RequestParam(value = "search_keyword", required = false, defaultValue = "") String keyword,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        if (customCatId != null) {
            return Result.wrapSuccessfulResult(new DefaultPage(Lists.newArrayList()));
        }
        // 每页显示条数
        int limit = 5;
        // 开启页码
        int start = (page - 1) * limit;

        GoodsListQueryRequest goodsListQuery = new GoodsListQueryRequest();
        goodsListQuery.setSource("legend");
        if (goodsId != null) {
            goodsListQuery.setGoodsId(String.valueOf(goodsId));
        }
        if (catId != null) {
            goodsListQuery.setCatId(Integer.valueOf(String.valueOf(catId)));
        }

        if (brandId != null) {
            goodsListQuery.setBrandId(String.valueOf(brandId));
        }
        if (carModelId != null) {
            goodsListQuery.setCarModel(Integer.valueOf(String.valueOf(carModelId)));
        }
        goodsListQuery.setKey(keyword);
        goodsListQuery.setOnSale("ALL");
        goodsListQuery.setIsDelete("ALL");
        goodsListQuery.setStart(start);
        goodsListQuery.setLimit(limit);
        String flag = (String) request.getAttribute(Constants.BPSHARE);
        com.tqmall.core.common.entity.Result<GoodsListForSearchDTO> result = null;

        log.info("[档口配件]获取档口配件,入参:{}", ObjectUtils.objectToJSON(goodsListQuery));
        try {
            if ("true".equals(flag)) {
                // IF 钣喷店 THEN 获取钣喷商品
                result = goodsServices.queryGoods4Spray(goodsListQuery);
            } else {
                result = goodsServices.queryGoodsListBySearch4Legend(goodsListQuery);
            }
        } catch (Exception e) {
            log.error("[档口配件]获取档口配件异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "列表为空");
        }

        if (result == null) {
            log.error("[档口配件]获取档口配件, 返回值为空");
            return Result.wrapErrorResult("", "列表为空");
        }

        if (!result.isSuccess()) {
            log.error("[档口配件]获取档口配件失败,失败信息:{}", result.getMessage());
            return Result.wrapErrorResult("", "列表为空");
        }
        Object data = result.getData();
        if (data == null) {
            return Result.wrapErrorResult("", "列表为空");
        }
        GoodsListForSearchDTO goodsListForSearchDTO = (GoodsListForSearchDTO) data;
        List<GoodsListForSearchBaseDTO.GoodsListDTO> tqmallGoods = goodsListForSearchDTO.getGoods();

        // 档口配件关联到已采购的配件
        List<TqmallAndLegendGoodsVo> tqmallAndLegendGoodses = null;
        try {
            tqmallAndLegendGoodses = tqmallReferToLegendGoods(shopId, tqmallGoods);
        } catch (Exception e) {
            log.error("[档口配件]档口配件关联已采购的配件异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "列表为空");
        }

        PageRequest pageRequest = new PageRequest(page - 1, 5);
        DefaultPage<TqmallAndLegendGoodsVo> defaultPage = new DefaultPage<TqmallAndLegendGoodsVo>(tqmallAndLegendGoodses, pageRequest,
                goodsListForSearchDTO.getTotal().longValue());
        defaultPage.setPageUri(request.getRequestURI());
        defaultPage.setSearchParam(ServletUtils.getParametersStringStartWith(request));

        return Result.wrapSuccessfulResult(defaultPage);
    }

    /**
     * TODO 2017-01-05 refactor code tangle
     * 1. 删除钣喷
     * 2. 删除普通配件
     *
     * @param id 配件ID
     * @return
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public Result delete(@RequestParam(required = true) Long id) {
        Long shopId = UserUtils.getShopIdForSession(request);

        // 判断配件是否被使用过: 使用过,建议下架操作
        Result<String> goodsDetailStatusResult = goodsFacade.getGoodsDetailStatus(id, shopId);
        if (!goodsDetailStatusResult.isSuccess()) {
            return goodsDetailStatusResult;
        }

        // 钣喷中心—油量 删除
        if (shopFunFacade.isBpShare(request, shopId)) {
            Optional<Goods> goodsOptional = goodsService.selectById(id, shopId);
            if (!goodsOptional.isPresent()) {
                log.error("删除失败,被删除的配件不存在, 配件ID:{}", id);
                return Result.wrapErrorResult("", "删除油漆资料失败");
            }
            Goods goods = goodsOptional.get();
            if (goods.getGoodsTag() == 1) {
                return this.removeBPGoods(id, shopId);
            }
        }

        // 普通配件 删除
        Result result = goodsService.deleteByIdAndShopId(id, shopId);
        if (result != null && result.isSuccess()) {
            return Result.wrapSuccessfulResult(true);
        } else {
            if (result == null) {
                return Result.wrapErrorResult("", "获取结果失败");
            }
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }


    /**
     * 配件搜索: 1页 50条 无序
     * <p/>
     * [应用场景]
     * 1. 工单创建: 添加配件选择框
     * <p/>
     * TODO 2017-01-11 refactor
     * 1. 统一前端入参名称: 如 "goodsSn" 与 "search_goodsSn"
     * 2. 废除该接口,迁移到'search/json'
     * <p/>
     *
     * @param request
     * @param goodsSn     商品编码
     * @param goodsName   商品名称
     * @param goodsFormat 商品型号
     * @param goodsType   商品类型
     * @param carInfoLike 适配车型
     * @return
     */
    @RequestMapping(value = "/json", method = RequestMethod.GET)
    @ResponseBody
    public Result json(HttpServletRequest request,
                       @RequestParam(value = "goodsSn", required = false) String goodsSn,
                       @RequestParam(value = "goodsName", required = false) String goodsName,
                       @RequestParam(value = "goodsFormat", required = false) String goodsFormat,
                       @RequestParam(value = "goodsType", required = false) String goodsType,
                       @RequestParam(value = "carInfoLike", required = false) String carInfoLike,
                       @RequestParam(value = "goodsBrandLike", required = false) String goodsBrandLike,
                       @RequestParam(value = "depotLike", required = false) String depotLike,
                       @RequestParam(value = "zeroStockRange", required = false) String zeroStockRange,
                       @RequestParam(value = "onsaleStatus", required = false) Integer onsaleStatus,
                       @RequestParam(value = "goodsCatLike", required = false) String goodsCatLike,
                       @RequestParam(value = "brandId", required = false) Integer brandId) {
        //盘点查询全部传 2;
        onsaleStatus = onsaleStatus == null ? 1 : onsaleStatus;
        if (onsaleStatus == 2) {
            onsaleStatus = null;
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        LegendGoodsRequest goodsRequest = new LegendGoodsRequest();
        goodsRequest.setShopId(shopId.toString());
        if (onsaleStatus != null) {
            goodsRequest.setOnsaleStatus(onsaleStatus);
        }
        if (!StringUtils.isBlank(goodsCatLike)) {
            goodsRequest.setGoodsCat(goodsCatLike);
        }
        if (!StringUtils.isBlank(goodsSn)) {
            goodsRequest.setGoodsSn(goodsSn);
        }
        if (!StringUtils.isBlank(zeroStockRange)) {
            Integer zeroStockRangeInt = Integer.parseInt(zeroStockRange);
            goodsRequest.setZeroStockRange(zeroStockRangeInt);
        }
        if (!StringUtils.isBlank(depotLike)) {
            goodsRequest.setDepot(depotLike);
        }
        if (!StringUtils.isBlank(goodsName)) {
            goodsRequest.setGoodsName(goodsName);
        }
        if (!StringUtils.isBlank(goodsType)) {
            goodsRequest.setGoodsType(goodsType);
        }
        if (!StringUtils.isBlank(goodsFormat)) {
            goodsRequest.setGoodsFormat(goodsFormat);
        }
        if (!StringUtils.isBlank(carInfoLike)) {
            goodsRequest.setCarInfoLike(carInfoLike);
        }
        if (!StringUtils.isBlank(goodsBrandLike)) {
            goodsRequest.setGoodsBrand(goodsBrandLike);
        }
        if (brandId != null) {
            goodsRequest.setBrandId(brandId);
        }

        // 1页 50条 无序
        PageableRequest pageableRequest = new PageableRequest(0, 50);
        DefaultPage<SearchGoodsVo> searchPage = goodsFacade.goodsPageSearch(goodsRequest, pageableRequest);
        return Result.wrapSuccessfulResult(searchPage.getContent());
    }


    /**
     * 配件分页搜索
     *
     * @param request
     * @param pageable
     * @return
     */
    @RequestMapping(value = "search/json", method = RequestMethod.GET)
    @ResponseBody
    public Result searchJSON(HttpServletRequest request,
                             @PageableDefault(page = 0, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        // 请求参数
        LegendGoodsRequest goodsRequest = wrapperGoodsSearchParam(request);

        // 页参数(页码\每页条数\排序)
        int pageNum = pageable.getPageNumber() - 1;
        int pageSize = pageable.getPageSize();
        Sort sort = pageable.getSort();
        FieldsSort fieldsSort = new FieldsSort(sort);
        PageableRequest pageableRequest = new PageableRequest(pageNum, pageSize, fieldsSort);
        DefaultPage<SearchGoodsVo> searchPage = goodsFacade.goodsPageSearch(goodsRequest, pageableRequest);
        searchPage.setPageUri(request.getRequestURI());
        searchPage.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(searchPage);
    }


    /**
     * 商品分页列表数据导出
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "search_export", method = RequestMethod.GET)
    @ResponseBody
    public void searchExport(HttpServletRequest request, HttpServletResponse response) throws Exception {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        String fileName = "配件库存-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        long startTime = System.currentTimeMillis();

        // 请求参数
        LegendGoodsRequest goodsRequest = wrapperGoodsSearchParam(request);

        ExcelExportor exportor = null;
        int totalSize = 0;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——配件库存";
            exportor.writeTitle(null, headline, GoodsExportVo.class);
            int pageNum = 0;
            int pageSize = 500;
            String[] orderBy = {"id"};
            while (true) {
                PageableRequest pageableRequest = new PageableRequest(pageNum, pageSize, Sort.Direction.DESC, orderBy);
                DefaultPage<SearchGoodsVo> searchPage = goodsFacade.goodsPageSearch(goodsRequest, pageableRequest);
                if (searchPage == null) {
                    break;
                }
                List<GoodsExportVo> goodsExportVoList = GoodsExportConverter.convertList(searchPage.getContent());
                if (CollectionUtils.isEmpty(goodsExportVoList)) {
                    break;
                }
                exportor.write(goodsExportVoList);
                totalSize += goodsExportVoList.size();
                if (totalSize >= searchPage.getTotalElements()) {
                    break;
                }
                pageNum++;
            }
        } catch (Exception e) {
            log.error("库存导出异常，门店id：{}", userInfo.getShopId(), e);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog("配件库存", userInfo, totalSize, endTime - startTime));
    }


    /**
     * 获取门店所有上架的配件
     *
     * @param goodsSearchParam
     * @return
     */
    @RequestMapping(value = "all/json", method = RequestMethod.GET)
    @ResponseBody
    public Result goodsAll(GoodsSearchParam goodsSearchParam) {
        Long shopId = UserUtils.getShopIdForSession(request);
        LegendGoodsRequest goodsRequest = new LegendGoodsRequest();
        BdUtil.do2bo(goodsSearchParam, goodsRequest);
        goodsRequest.setShopId(shopId.toString());
        goodsRequest.setGoodsCat(goodsSearchParam.getGoodsCatLike());
        goodsRequest.setGoodsBrand(goodsSearchParam.getGoodsBrandLike());
        // 1页 500条 id倒序
        int page = 0;
        int size = 500;
        String[] orderBy = {"depot"};
        List<SearchGoodsVo> goodsList = Lists.newArrayList();
        while (true) {
            PageableRequest pageableRequest = new PageableRequest(page, size, Sort.Direction.DESC, orderBy);
            DefaultPage<SearchGoodsVo> searchPage = goodsFacade.goodsPageSearch(goodsRequest, pageableRequest);
            List<SearchGoodsVo> searchGoodsVoList = searchPage.getContent();
            if (CollectionUtils.isEmpty(searchGoodsVoList)) {
                break;
            }
            goodsList.addAll(searchGoodsVoList);
            if (searchGoodsVoList.size() < size) {
                break;
            }
            page++;
        }
        if (CollectionUtils.isEmpty(goodsList)) {
            return Result.wrapSuccessfulResult(goodsList);
        }
        for (SearchGoodsVo searchGoodsVo : goodsList) {
            if (searchGoodsVo.getInventoryPrice() == null) {
                searchGoodsVo.setInventoryPrice(0d);
            }
        }
        return Result.wrapSuccessfulResult(goodsList);
    }

    /**
     * 判断配件零件号是否存在
     *
     * @param goodsFormat 零件号
     * @return {'false':已存在; 'true':不存在}
     */
    @RequestMapping(value = "formatisexist")
    @ResponseBody
    public Result formatIsExist(@RequestParam(value = "goodsFormat", required = true) String goodsFormat) {
        Long shopId = UserUtils.getShopIdForSession(request);
        GoodsQueryParam queryParam =new GoodsQueryParam();
        queryParam.setFormat(goodsFormat);
        queryParam.setShopId(shopId);
        List<Goods> goodsList = goodsService.queryGoods(queryParam);
        if (CollectionUtils.isEmpty(goodsList)) {
            return Result.wrapSuccessfulResult(true);
        } else {
            return Result.wrapSuccessfulResult(false);
        }
    }


    /**
     * 检查物料详细状态
     *
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "checkGoodsDetailStatus", method = RequestMethod.GET)
    @ResponseBody
    public Result checkGoodsDetailStatus(Long goodsId) {
        try {
            Long shopId = UserUtils.getShopIdForSession(request);
            return goodsFacade.getGoodsDetailStatus(goodsId, shopId);
        } catch (Exception e) {
            log.error("检查物料详细状态出错,异常信息{}", e);
            return Result.wrapErrorResult(LegendError.COMMON_ERROR);
        }
    }


    /**
     * 根据goodsIds获取物料列表
     *
     * @param goodsIds
     * @return
     */
    @RequestMapping(value = "getGoodsListByIds", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<Goods>> getOrderListByIds(Long[] goodsIds) {
        try {
            if (goodsIds == null || goodsIds.length == 0) {
                return Result.wrapErrorResult("", "传递参数有误");
            }
            List<Long> goodsIdsList = Arrays.asList(goodsIds);
            Long shopId = UserUtils.getShopIdForSession(request);
            List<Goods> goodsList = goodsService.selectByIdsAndShopId(goodsIdsList, shopId);
            return Result.wrapSuccessfulResult(goodsList);
        } catch (Exception e) {
            log.error("根据goodsIds获取物料列表异常,{}", e);
            return Result.wrapErrorResult("", "根据goodsIds获取物料列表异常");
        }
    }


    /**
     * 合并重复配件
     * <p/>
     * 原配件集合->目标配件
     *
     * @param oldGoodsIds 原配件集合
     * @param newGoodsId  目标配件
     * @return
     */
    @RequestMapping(value = "do_merge_goods", method = RequestMethod.POST)
    @ResponseBody
    public Result doMergeGoods(Long[] oldGoodsIds, Long newGoodsId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        // 1.校验参数
        if (oldGoodsIds == null || oldGoodsIds.length == 0) {
            return Result.wrapErrorResult("", "传递参数有误");
        }
        if (newGoodsId == null || newGoodsId <= 0) {
            return Result.wrapErrorResult("", "传递参数有误");
        }

        // 源配件集合
        Set<Long> srcGoodsSet = new HashSet<Long>(Arrays.asList(oldGoodsIds));

        // 过滤掉目标配件
        if (srcGoodsSet.contains(newGoodsId)) {
            srcGoodsSet.remove(newGoodsId);
        }

        try {
            return goodsService.mergeGoods(srcGoodsSet.toArray(new Long[srcGoodsSet.size()]), newGoodsId, userInfo);
        } catch (Exception e) {
            log.error("[配件合并]合并重复配件异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "合并重复配件失败");
        }
    }


    /**
     * 跳转到油漆资料编辑页面
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "editBPGoodsPage", method = RequestMethod.GET)
    public String toBPGoodsAddOrEditPage(Model model, @RequestParam(value = "id", required = true) Long id) {
        Long shopId = UserUtils.getShopIdForSession(request);
        try {
            model = wrapperGoodsInfo(model, id, shopId);
        } catch (Exception e) {
            log.error("[油漆编辑页面] 获取油漆信息失败！goodsId:{},shopId:{} 异常信息:{}", id, shopId, e);
        }
        com.tqmall.core.common.entity.Result<GoodsPaintExtDTO> result = null;
        try {
            result = rpcGoodsPaintExtService.getGoodsPaintExtInfo(id, shopId);
        } catch (Exception e) {
            log.error("[油漆编辑页面] magic获取油漆扩展属性失败！goodsId:{},shopId:{} 异常信息:{}", id, shopId, e);
        }
        if (result != null) {
            GoodsPaintExtDTO goodsPaintExtDTO = result.getData();
            model.addAttribute("extInfo", goodsPaintExtDTO);
        }
        return "yqx/page/magic/paint/paintGoodsEdit";
    }


    /**
     * 增加油漆资料
     *
     * @param goodsVo
     * @return
     */
    @RequestMapping(value = "addBPGoods", method = RequestMethod.POST)
    @ResponseBody
    public Result addBPGood(@RequestBody GoodsVo goodsVo) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Long userId = UserUtils.getUserIdForSession(request);

        if (goodsVo == null) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }

        // 1.包装配件基本信息
        GoodsBo goodsBo = wrapperGoodsBO(goodsVo, shopId, userId);
        // 标注为油漆
        goodsBo.getGoods().setGoodsTag(GoodsTagEnum.PAINT.getCode());

        // 2.判断权限 {钣喷店&油漆}
        boolean isBpShare = shopFunFacade.isBpShare(request, shopId);
        boolean isPaint = (GoodsTagEnum.PAINT.getCode()).equals(goodsVo.getGoodsTag()) ? true : false;
        if (!(isBpShare && isPaint)) {
            return Result.wrapErrorResult("", "无权限！");
        }

        // 3.数据校验(必填)
        // 净重
        BigDecimal netWeight = goodsVo.getNetWeight();
        // 带桶重量=净重+桶重（包含桶盖重量）
        BigDecimal bucketWeight = goodsVo.getBucketWeight();
        // 带桶和搅拌头的重量
        BigDecimal stirWeight = goodsVo.getStirWeight();
        if (netWeight == null) {
            return Result.wrapErrorResult("", "油漆类型-净重 不能为空");
        }
        if (bucketWeight == null) {
            return Result.wrapErrorResult("", "油漆类型-带桶质量 不能为空");
        }
        if (stirWeight == null) {
            return Result.wrapErrorResult("", "油漆类型-带桶和搅拌头的质量 不能为空");
        }
        // 油漆属性
        GoodsExtVo goodsExtVo = new GoodsExtVo();
        goodsExtVo.setGoodsBo(goodsBo);
        goodsExtVo.setNetWeight(netWeight);
        goodsExtVo.setBucketWeight(bucketWeight);
        goodsExtVo.setStirWeight(stirWeight);
        goodsExtVo.setPaintLevel(goodsVo.getPaintLevel());
        goodsExtVo.setPaintType(goodsVo.getPaintType());

        boolean flag = false;
        try {
            flag = bpGoodsFacade.addBPGoodsInfo(goodsExtVo);
        } catch (Exception e) {
            log.error("[油漆资料] 新增油漆资料失败！shopId=" + shopId, e);
            return Result.wrapErrorResult("", "新增油漆资料失败！");
        }
        if (flag) {
            return Result.wrapSuccessfulResult("新增油漆资料成功！");
        } else {
            return Result.wrapErrorResult("", "新增油漆资料失败！");
        }
    }

    /**
     * 更新油漆资料-钣喷专用
     *
     * @param goodsVo
     * @return
     */
    @RequestMapping(value = "/modifyBPGoods", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> modifyBPGoods(@RequestBody GoodsVo goodsVo) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Long userId = UserUtils.getUserIdForSession(request);
        GoodsBo goodsBo = wrapperGoodsBO(goodsVo, shopId, userId);
        GoodsExtVo goodsExtVo = BdUtil.bo2do(goodsVo, GoodsExtVo.class);
        goodsExtVo.setGoodsBo(goodsBo);
        goodsExtVo.setGoodsId(goodsBo.getGoods().getId());
        if (!checkGoodsExtVoParam(goodsExtVo)) {
            return Result.wrapErrorResult("", "必填参数不能为空！");
        }

        boolean flag = false;
        try {
            flag = bpGoodsFacade.modifyBPGoodsInfo(goodsExtVo);
        } catch (Exception e) {
            log.error("[油漆资料] 更新失败！shopId=" + shopId + ",goodsId=" + goodsVo.getId(), e);
            return Result.wrapErrorResult("", "更新油漆资料失败！");
        }
        if (flag) {
            return Result.wrapSuccessfulResult("更新油漆资料成功！");
        } else {
            return Result.wrapErrorResult("", "更新油漆资料失败！");
        }
    }

    /**
     * 删除油漆资料
     *
     * @param id
     * @return
     */
    private Result<String> removeBPGoods(Long id, Long shopId) {
        boolean flag = false;
        try {
            flag = bpGoodsFacade.removeBPGoodsInfo(id, shopId);
        } catch (Exception e) {
            log.error("[油漆资料] 删除失败！", e);
            return Result.wrapErrorResult("", "删除失败！");
        }
        if (flag) {
            return Result.wrapSuccessfulResult("删除成功！");
        } else {
            return Result.wrapErrorResult("", "删除失败！");
        }
    }


    /**
     * 参数校验
     *
     * @param param
     * @return
     */
    public boolean checkGoodsExtVoParam(GoodsExtVo param) {
        if (param == null) {
            return false;
        } else if (param.getGoodsBo() == null) {
            return false;
        } else if (param.getBucketWeight() == null || param.getNetWeight() == null ||
                param.getPaintLevel() == null || param.getPaintType() == null ||
                param.getStirWeight() == null) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 判断配件是否为油漆件(条件: 钣喷店&油漆类型)
     *
     * @param goodsId 配件ID
     * @return {true:是油漆; false:不是油漆件}
     */
    @RequestMapping("isBPPrintGood")
    @ResponseBody
    public Result<Boolean> isBPPrintGood(Long goodsId) {
        if (shopFunFacade.isBpShare(request, UserUtils.getShopIdForSession(request))) {
            return bpGoodsFacade.isBPGoods(goodsId);
        }
        return Result.wrapSuccessfulResult(false);
    }


    /**
     * 跳转到油漆资料新增页面
     *
     * @return
     */
    @RequestMapping("toPaintGoodsAdd")
    public String toPaintGoodsAdd(Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        model.addAttribute("goodsSn", snFactory.generateSn(SnFactory.GOODS_SN_INCREMENT, shopId, SnFactory.GOODS));
        return "yqx/page/magic/paint/paintGoodsAdd";
    }


    /**
     * [新版] 配件新增页面
     *
     * @return
     */
    @RequestMapping(value = "goods-toadd", method = RequestMethod.GET)
    public String toAdd(Model model) {
        // 配件编码
        Long shopId = UserUtils.getShopIdForSession(request);
        model.addAttribute("goodsSn", snFactory.generateSn(SnFactory.GOODS_SN_INCREMENT, shopId, SnFactory.GOODS));
        return "yqx/page/goods/goods-toadd";
    }


    /**
     * [新版] 配件编辑页面
     *
     * @param goodsId 配件ID
     * @return
     */
    @RequestMapping(value = "goods-toedit", method = RequestMethod.GET)
    public String toEdit(Model model, @RequestParam(value = "goodsid", required = true) Long goodsId) {

        // 配件信息
        Long shopId = UserUtils.getShopIdForSession(request);
        Optional<Goods> goodsOptional = goodsService.selectById(goodsId, shopId);
        if (!goodsOptional.isPresent()) {
            return "common/error";
        }
        Goods goods =goodsOptional.get();
        model.addAttribute("goods", goods);

        // 淘汽配件
        Long tqmallGoodsId =goods.getTqmallGoodsId();
        Long brandId = goods.getBrandId();
        if(tqmallGoodsId !=null && tqmallGoodsId >0l){
            GoodsBrandDTO goodsBrand = goodsBrandFacade.selectById(brandId);
            if (goodsBrand != null) {
                brandId = goodsBrand.getTqmallBrandId();
            }
        }
        model.addAttribute("brandId", brandId);

        // 获取车型
        List<GoodsCarDTO> goodsCarList = goodsCarFacade.selectByGoodsIdAndShopId(goodsId, shopId);
        // 如果goods_car中没有车型相关数据，取goods.car_info字段转换为goodsCarList
        if (CollectionUtils.isEmpty(goodsCarList)) {
            String carInfo = goods.getCarInfo();
            if (!StringUtils.isEmpty(carInfo) && !"0".equals(carInfo) && !"[]".equals(carInfo)) {
                GoodsCarDTO goodsCar = new GoodsCarDTO();
                goodsCar.setCarAlias(carInfo);
                goodsCarList.add(goodsCar);
            }
        }
        model.addAttribute("goodsCarList", goodsCarList);

        // 配件属性
        model.addAttribute("goodsAttrRelList", goodsAttrRelFacade.selectByGoodsIdAndShopId(goodsId, shopId));

        return "yqx/page/goods/goods-toedit";
    }

    /**
     * [新版] 配件列表页面
     *
     * @return
     */
    @RequestMapping(value = "goods-list", method = RequestMethod.GET)
    public String listPage(Model model) {
        // 是否有配件删除权限
        model.addAttribute("hasDelPartPrv", checkDeleteGoodsPermission());
        return "yqx/page/goods/goods-list";
    }

    /**
     * [新版] 淘汽采购列表页面
     *
     * @return
     */
    @RequestMapping(value = "goods-tqmall-list", method = RequestMethod.GET)
    public String tqmalLiistPage(Model model, @RequestParam(value = "goodsId", required = false) Long goodsId) {
        if (goodsId != null && goodsId > 0) {
            model.addAttribute("goodsId", goodsId);
        }
        return "yqx/page/goods/goods-tqmall-list";
    }


    /**
     * 配件上架
     *
     * @param goodsId 配件ID
     * @return
     */
    @RequestMapping(value = "upshelf", method = RequestMethod.GET)
    @ResponseBody
    public Result upShelf(@RequestParam(value = "goodsid", required = true) Long goodsId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Optional<Goods> goodsOptional = goodsService.selectById(goodsId, shopId);
        if (!goodsOptional.isPresent()) {
            return Result.wrapErrorResult("", "上架失败,该配件不存在");
        }
        Goods goods = goodsOptional.get();
        goods.setOnsaleStatus(GoodsOnsaleEnum.UPSHELF.getCode());
        try {
            goodsService.updateById(goods);
        } catch (Exception e) {
            log.error("配件下架异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "上架失败");
        }

        return Result.wrapSuccessfulResult("");
    }


    /**
     * 配件下架
     *
     * @param goodsId 配件ID
     * @return
     */
    @RequestMapping(value = "downshelf", method = RequestMethod.GET)
    @ResponseBody
    public Result downShelf(@RequestParam(value = "goodsid", required = true) Long goodsId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Optional<Goods> goodsOptional = goodsService.selectById(goodsId, shopId);
        if (!goodsOptional.isPresent()) {
            return Result.wrapErrorResult("", "下架失败,该配件不存在");
        }
        Goods goods = goodsOptional.get();
        goods.setOnsaleStatus(GoodsOnsaleEnum.DOWNSHELF.getCode());
        try {
            goodsService.updateById(goods);
        } catch (Exception e) {
            log.error("配件下架异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "下架失败");
        }

        return Result.wrapSuccessfulResult("");
    }


    /**
     * 判断是否有删除配件权限
     * 管理员 OR 被赋予删除权限
     *
     * @return {'true':有删除权限; 'false':无删除权限}
     */
    private Boolean checkDeleteGoodsPermission() {
        // 当前用户权限列表
        Object permissionListObj = request.getAttribute(Constants.SESSION_USER_ROLE_FUNC);
        // 当前用户是否管理员
        Object isAdmin = request.getAttribute(Constants.SESSION_USER_IS_ADMIN);
        Boolean delPermission = Boolean.FALSE;
        if (isAdmin != null && isAdmin.equals("1")) {
            return Boolean.TRUE;
        }
        if (permissionListObj != null) {
            List<FuncF> permissionList = (List<FuncF>) permissionListObj;
            for (FuncF funcF : permissionList) {
                // TODO 2017-01-11 权限通过汉字判断,不靠谱
                if ("配件删除".equals(funcF.getName())) {
                    delPermission = Boolean.TRUE;
                    break;
                }
            }
        }

        return delPermission;
    }

    /**
     * 包装配件信息
     *
     * @param goodsVo 前端配件信息
     * @param shopId  当前门店ID
     * @param userId  当前操作人
     * @return
     */
    private GoodsBo wrapperGoodsBO(GoodsVo goodsVo, Long shopId, Long userId) {
        GoodsBo goodsBo = new GoodsBo();
        goodsBo.setShopId(shopId);
        goodsBo.setUserId(userId);

        // 配件关联属性
        List<GoodsAttrRelDTO> goodsAttrRelList = new Gson().fromJson(goodsVo.getGoodsAttrRelList(),
                new TypeToken<List<GoodsAttrRelDTO>>() {
                }.getType());
        if (!CollectionUtils.isEmpty(goodsAttrRelList)) {
            goodsBo.setGoodsAttrRelList(goodsAttrRelList);
        }

        // 适用车型
        List<GoodsCarDTO> goodsCarList = new Gson().fromJson(goodsVo.getGoodsCarList(), new TypeToken<List<GoodsCarDTO>>() {
        }.getType());
        if (!CollectionUtils.isEmpty(goodsCarList)) {
            goodsBo.setGoodsCarList(goodsCarList);
        }

        goodsBo.setGoods(goodsVo);
        return goodsBo;
    }


    /**
     * 配件是否油料
     *
     * @param goodsId 配件ID
     * @return
     */
    @RequestMapping(value = "ispaint", method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> isPaint(@RequestParam(value = "goodsId", required = true) Long goodsId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        boolean isPaint = Boolean.FALSE;

        Optional<Goods> goodsOptional = goodsService.selectById(goodsId, shopId);
        if (!goodsOptional.isPresent()) {
            return Result.wrapErrorResult("", "配件不存在");
        }
        Goods goods = goodsOptional.get();
        Integer goodsTag = goods.getGoodsTag();
        isPaint = (goodsTag == 1) ? Boolean.TRUE : Boolean.FALSE;

        return Result.wrapSuccessfulResult(isPaint);
    }

    @RequestMapping(value = "get_depot_list",method = RequestMethod.GET)
    @ResponseBody
    public Result<List<BaseEnumBo>> depot(){
        Long shopId = UserUtils.getShopIdForSession(request);
        List<BaseEnumBo> depotList= goodsFacade.getGoodsLocation(shopId);
        return  Result.wrapSuccessfulResult(depotList);
    }

    /**
     * 档口配件 关联 已采购配件
     *
     * @param shopId
     * @param tqmallGoods 档口配件
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private List<TqmallAndLegendGoodsVo> tqmallReferToLegendGoods(Long shopId, List<GoodsListForSearchBaseDTO.GoodsListDTO> tqmallGoods) throws IllegalAccessException, InvocationTargetException {
        if(CollectionUtils.isEmpty(tqmallGoods)){
            return Lists.newArrayList();
        }
        // 包装后集合
        List<TqmallAndLegendGoodsVo> wrapperedTqmallGoodses = new ArrayList<TqmallAndLegendGoodsVo>();
        TqmallAndLegendGoodsVo wrapperedTqmallGoods = null;

        //获取 goodsIdsList
        Collection<Long> tqmallGoodsIds = Lists.transform(tqmallGoods,
                new Function<GoodsListForSearchBaseDTO.GoodsListDTO, Long>() {
                    @Override
                    public Long apply(GoodsListForSearchBaseDTO.GoodsListDTO input) {
                        return input.getGoodsId().longValue();
                    }
                }
        );
        // 获取关联的已采购配件
        List<Goods> legendGoodses = goodsService.findByTqmallGoodsIds(shopId, tqmallGoodsIds);
        //获取 goodsMap
        Map<Long, Goods> goodsMap = Maps.uniqueIndex(legendGoodses, new Function<Goods, Long>() {
            @Override
            public Long apply(Goods input) {
                return input.getTqmallGoodsId();
            }
        });
        for (GoodsListForSearchBaseDTO.GoodsListDTO tqmallGood : tqmallGoods) {
            wrapperedTqmallGoods = BdUtil.bo2do(tqmallGood, TqmallAndLegendGoodsVo.class);
            Long tqmallGoodsId = tqmallGood.getGoodsId().longValue();

            if (goodsMap != null && goodsMap.containsKey(tqmallGoodsId)) {
                Goods goods = goodsMap.get(tqmallGoodsId);
                // 配件ID
                wrapperedTqmallGoods.setLegendGoodsId(goods.getId());
                // 采购价
                wrapperedTqmallGoods.setLegendGoodsPrice(goods.getPrice());
                // 库位
                String depot = goods.getDepot();
                if (!StringUtils.isBlank(depot)) {
                    wrapperedTqmallGoods.setLegendGoodsDepot(depot);
                }
            }
            wrapperedTqmallGoodses.add(wrapperedTqmallGoods);
        }
        return wrapperedTqmallGoodses;
    }

    /**
     * 配件列表 请求参数
     *
     * @param request
     * @return
     */
    private LegendGoodsRequest wrapperGoodsSearchParam(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);

        // searchForm 条件
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        LegendGoodsRequest goodsRequest = new LegendGoodsRequest();
        goodsRequest.setShopId(shopId.toString());

        if (searchParams.containsKey("catId")) {
            goodsRequest.setCatId(Long.valueOf(searchParams.get("catId") + ""));
        }

        // 标准分类
        if (searchParams.containsKey("stdCatId")) {
            goodsRequest.setStdCatId(Integer.valueOf(searchParams.get("stdCatId") + ""));
        }

        // 品牌
        if (searchParams.containsKey("brandId")) {
            Object brandIdParam = searchParams.get("brandId");
            goodsRequest.setBrandId(Integer.valueOf(brandIdParam + ""));
        }

        // 非0库存
        if (searchParams.containsKey("zeroStockRange")) {
            Object zeroStockRangeParam = searchParams.get("zeroStockRange");
            goodsRequest.setZeroStockRange(Integer.valueOf(zeroStockRangeParam + ""));
        }

        // 是否上下架
        if (searchParams.containsKey("onsaleStatus")) {
            Object onsaleStatusParam = searchParams.get("onsaleStatus");
            Integer onsaleStatus = Integer.valueOf(onsaleStatusParam + "");
            if (onsaleStatus == 2) {
                // null:全部工单
                /**
                 * TODO 2017-01-13 refactor
                 * 1. 查询全部工单时 ,不建议传入null 建议定义个具体值
                 */
                onsaleStatus = null;
            }
            goodsRequest.setOnsaleStatus(onsaleStatus);
        }

        if (searchParams.containsKey("carInfoLike")) {
            Object carInfoLikeParam = searchParams.get("carInfoLike");
            goodsRequest.setCarInfoLike((String) carInfoLikeParam);
        }

        // 配件名称
        if (searchParams.containsKey("goodsNameLike")) {
            Object goodsNameParam = searchParams.get("goodsNameLike");
            goodsRequest.setGoodsName((String) goodsNameParam);
        }

        // 零件号
        if (searchParams.containsKey("goodsFormatLike")) {
            Object goodsFormatParam = searchParams.get("goodsFormatLike");
            goodsRequest.setGoodsFormat((String) goodsFormatParam);
        }

        // 库位
        if (searchParams.containsKey("depotLike")) {
            Object depotLikeParam = searchParams.get("depotLike");
            goodsRequest.setDepot((String) depotLikeParam);
        }
        // 关键字查询:搜索goods_sn_like,name_like,format_like
        if (searchParams.containsKey("likeKeyWords")) {
            Object likeKeyWordsParam = searchParams.get("likeKeyWords");
            goodsRequest.setLikeKeyWords((String) likeKeyWordsParam);
        }
        // 物料类型
        if (searchParams.containsKey("goodsType")) {
            Object goodsTypeParam = searchParams.get("goodsType");
            goodsRequest.setGoodsType((String) goodsTypeParam);
        }
        if (searchParams.containsKey("goodsTag")) {
            Object goodsTagParam = searchParams.get("goodsTag");
            goodsRequest.setGoodsTag((Integer) goodsTagParam);
        }

        return goodsRequest;
    }

    /**
     * 包装配件信息
     *
     * @param model
     * @param goodsId 配件ID
     * @param shopId
     * @return
     */
    private Model wrapperGoodsInfo(Model model, Long goodsId, Long shopId) {
        Optional<Goods> goodsOptional = goodsService.selectById(goodsId, shopId);
        if (goodsOptional.isPresent()) {
            Goods goods = goodsOptional.get();
            model.addAttribute("goods", goods);

            // 获取车型
            List<GoodsCarDTO> goodsCarList = goodsCarFacade.selectByGoodsIdAndShopId(goodsId, shopId);
            // [[ 豪德数据处理开始
            // 如果goods_car中没有车型相关数据，取goods.car_info字段转换为goodsCarList
            if (CollectionUtils.isEmpty(goodsCarList)) {
                String carInfo = goods.getCarInfo();
                if (!StringUtils.isEmpty(carInfo) && !"0".equals(carInfo) && !"[]".equals(carInfo)) {
                    GoodsCarDTO goodsCar = new GoodsCarDTO();
                    goodsCar.setCarAlias(carInfo);
                    goodsCarList.add(goodsCar);
                }
            }
            // ]]
            model.addAttribute("goodsCarList", goodsCarList);

            // 获取配件属性
            List<GoodsAttrRelDTO> goodsAttrRelList = goodsAttrRelFacade.selectByGoodsIdAndShopId(goodsId, shopId);
            model.addAttribute("goodsAttrRelList", goodsAttrRelList);

            // 车型列表为空,而且carInfo字段值为0才是通用配件
            if ("0".equals(goods.getCarInfo())) {
                model.addAttribute("isCommonParts", 1);
            } else {
                model.addAttribute("isCommonParts", 0);
            }
        }

        return model;
    }

}
