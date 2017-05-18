package com.tqmall.legend.web.warehouse;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.inventory.InventoryService;
import com.tqmall.legend.biz.maxsn.MaxSnService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.inventory.InventoryRecord;
import com.tqmall.legend.entity.inventory.InventoryStock;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.warehouse.InventoryFormBo;
import com.tqmall.legend.entity.warehouse.InventoryGoodsBo;
import com.tqmall.legend.entity.warehouse.InventoryStatusEnum;
import com.tqmall.legend.entity.warehouse.StockStatusOfOrder;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.goods.GoodsFacade;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ExcelExportUtil;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.legend.web.warehouse.export.vo.SearchGoodsCommission;
import com.tqmall.search.common.data.FieldsSort;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.goods.param.LegendShortageGoodsRequest;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 仓库.库存管理
 */
@Controller
@RequestMapping("shop/warehouse/stock")
@Slf4j
public class StockController extends BaseController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private MaxSnService maxSnService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private GoodsFacade goodsFacade;


    /**
     * 库存查询列表页
     *
     * @param model
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "stock-query", method = RequestMethod.GET)
    public String query(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        return "yqx/page/warehouse/stock/stock-query";
    }


    /**
     * 库存盘点列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "stock-inventory", method = RequestMethod.GET)
    public String inventory(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        return "yqx/page/warehouse/stock/stock-inventory";
    }

    /**
     * 进入库存盘点新增页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "stock-inventory-add", method = RequestMethod.GET)
    public String toInventoryAdd(Model model,
                                 @RequestParam(value = "goodsids", required = false) String goodsIdsStr) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());

        UserInfo currentLoginUser = UserUtils.getUserInfo(request);
        Long shopId = currentLoginUser.getShopId();
        model.addAttribute("userInfo", currentLoginUser);
        // 预生成盘点编号
        model.addAttribute("inventorySN", maxSnService.getMaxSn(shopId, "PD"));

        // 选中的配件集合
        if (!StringUtils.isEmpty(goodsIdsStr)) {
            Set<Long> inventoryGoodsIds = new HashSet<Long>();
            String[] goodsIds = goodsIdsStr.split(",");
            for (String goodsId : goodsIds) {
                inventoryGoodsIds.add(Long.parseLong(goodsId));
            }
            // 选中配件
            Long[] selectedGoodsIds = inventoryGoodsIds.toArray(new Long[inventoryGoodsIds.size()]);
            List<Goods> selectedGoods = goodsService.selectByIds(selectedGoodsIds);
            model.addAttribute("selectedGoods", selectedGoods);
        }

        return "yqx/page/warehouse/stock/stock-inventory-add";
    }


    /**
     * 进入库存盘点详情页面
     *
     * @param itemId 主键ID
     * @param model
     * @return
     */
    @RequestMapping(value = "stock-inventory-detail", method = RequestMethod.GET)
    public String toInventoryDetail(@RequestParam(value = "itemid", required = true) Long itemId, Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());

        Long shopId = UserUtils.getShopIdForSession(request);

        // 获取盘点表记录
        Optional<InventoryRecord> inventoryRecordOptional = inventoryService.get(itemId, shopId);
        if (inventoryRecordOptional.isPresent()) {
            InventoryRecord inventoryRecord = inventoryRecordOptional.get();
            model.addAttribute("record", inventoryRecord);

            // 获取配件盘点
            List<InventoryStock> inventoryStockList = getInventoryStockList(itemId, shopId);
            if (!CollectionUtils.isEmpty(inventoryStockList)) {
                model.addAttribute("stocks", inventoryStockList);
            }
        }

        return "yqx/page/warehouse/stock/stock-inventory-detail";
    }


    /**
     * 打印盘点相应页
     *
     * @param id      盘点记录ID
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "inventory_print", method = RequestMethod.GET)
    public String inventoryPrint(@RequestParam(value = "id", required = true) Long id,
                                 Model model, HttpServletRequest request) {

        Long shopId = UserUtils.getShopIdForSession(request);
        model.addAttribute("shop", shopService.selectById(shopId));

        // 盘点记录
        Optional<InventoryRecord> inventoryRecordOptional = inventoryService.get(id, shopId);
        if (inventoryRecordOptional.isPresent()) {
            model.addAttribute("record", inventoryRecordOptional.get());
        }

        // 盘点明细
        List<InventoryStock> inventoryStocks = getInventoryStockList(id, shopId);
        BigDecimal totalCurrentInventoryAmount = BigDecimal.ZERO;
        for (InventoryStock inventoryStock : inventoryStocks) {
            BigDecimal currentStock = inventoryStock.getCurrentStock();
            currentStock = (currentStock == null) ? BigDecimal.ZERO : currentStock;
            BigDecimal inventoryPrice = inventoryStock.getInventoryPrice();
            inventoryPrice = (inventoryPrice == null) ? BigDecimal.ZERO : inventoryPrice;
            totalCurrentInventoryAmount = totalCurrentInventoryAmount.add(currentStock.multiply(inventoryPrice));
        }
        model.addAttribute("totalCurrentInventoryAmount", totalCurrentInventoryAmount);
        model.addAttribute("stocks", inventoryStocks);

        return "yqx/page/warehouse/stock/inventory_print";

    }


    /**
     * 根据盘点记录 获取盘点配件
     *
     * @param inventoryRecordId 盘点记录ID
     * @param shopId
     * @return List<InventoryStock>
     */
    private List<InventoryStock> getInventoryStockList(Long inventoryRecordId, Long shopId) {

        // 获取被盘点配件集合
        List<InventoryStock> inventoryStocks = inventoryService.getInventoryStocks(inventoryRecordId, shopId);

        // 获取最新配件信息
        Map<Long, Goods> goodsMap = reGetGoods(inventoryStocks);
        // 计算盘点盈亏\获取最新仓位
        for (InventoryStock inventoryStock : inventoryStocks) {
            // 计算盘点差价
            BigDecimal currentStock = inventoryStock.getCurrentStock();
            // 当前库存
            currentStock = (currentStock == null) ? BigDecimal.ZERO : currentStock;
            // 配件成本价
            BigDecimal inventoryPrice = inventoryStock.getInventoryPrice();
            inventoryPrice = (inventoryPrice == null) ? BigDecimal.ZERO : inventoryPrice;
            // 配件当前成本总额
            BigDecimal currentInventoryAmount = currentStock.multiply(inventoryPrice);
            inventoryStock.setCurrentInventoryAmount(currentInventoryAmount);
            // 配件真实库存 IF 实盘库存为null,未做盘点 THEN 不计算配件真实成本,盘点差价
            BigDecimal realStock = inventoryStock.getRealStock();
            if (realStock != null) {
                // 配件真实成本总额
                BigDecimal realInventoryAmount = realStock.multiply(inventoryPrice);
                inventoryStock.setRealInventoryAmount(realInventoryAmount);
                // 配件盘点差价
                BigDecimal diffInventoryAmount = realInventoryAmount.subtract(currentInventoryAmount);
                inventoryStock.setDiffInventoryAmount(diffInventoryAmount);
            }

            // 获取最新仓位
            Long goodsId = inventoryStock.getGoodsId();
            Goods goods = goodsMap.get(goodsId);
            if (goods != null) {
                inventoryStock.setDepot(goods.getDepot());
                String goodsCat = goods.getCat2Name();
                inventoryStock.setCatName((goodsCat == null) ? "" : goodsCat);
                // 获取最新更新时间
                inventoryStock.setLastInTimeStr(goods.getLastInTimeStr());
            } else {
                inventoryStock.setDepot("");
                inventoryStock.setCatName("");
                inventoryStock.setLastInTimeStr("");
            }
        }

        return inventoryStocks;
    }


    /**
     * 获取最新配件信息
     *
     * @param inventoryStocks 被盘点配件集合
     * @return
     */
    private Map<Long, Goods> reGetGoods(List<InventoryStock> inventoryStocks) {
        Set<Long> inventoryGoodsIds = new HashSet<Long>();
        for (InventoryStock inventoryStock : inventoryStocks) {
            inventoryGoodsIds.add(inventoryStock.getGoodsId());
        }
        Long[] goodsIds = inventoryGoodsIds.toArray(new Long[inventoryGoodsIds.size()]);
        List<Goods> goodses = goodsService.selectByIds(goodsIds);
        Map<Long, Goods> goodsMap = new HashMap<Long, Goods>(goodses.size());
        for (Goods goods : goodses) {
            goodsMap.put(goods.getId(), goods);
        }

        return goodsMap;
    }


    /**
     * 库存预警列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "stock-warning", method = RequestMethod.GET)
    public String warning(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        return "yqx/page/warehouse/stock/stock-warning";
    }


    /**
     * generate inventory record
     *
     * @param inventoryFormBo
     * @return
     */
    @RequestMapping(value = "stock-inventory-generate", method = RequestMethod.POST)
    @ResponseBody
    public Result generate(InventoryFormBo inventoryFormBo) {
        UserInfo currentLoginUser = UserUtils.getUserInfo(request);

        // check property
        Long inventoryCheckerId = inventoryFormBo.getInventoryCheckerId();
        if (inventoryCheckerId == null || inventoryCheckerId < 0l) {
            return Result.wrapErrorResult("", "未选择'盘点人'");
        }

        // 盘点配件
        String inventoryGoodsJSON = inventoryFormBo.getInventoryGoodsJSON();
        if (StringUtils.isEmpty(inventoryGoodsJSON)) {
            return Result.wrapErrorResult("", "未选择盘点的配件");
        }
        Gson gson = new Gson();
        List<InventoryGoodsBo> inventoryGoodsBos = new ArrayList<InventoryGoodsBo>();
        try {
            inventoryGoodsBos = gson.fromJson(inventoryGoodsJSON,
                    new TypeToken<List<InventoryGoodsBo>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            log.error("创建盘点单异常,配件JSON转对象失败 配件JSON:{} 异常信息:{}", inventoryGoodsJSON, e);
            return Result.wrapErrorResult("", "创建盘点单失败！");
        }
        if (CollectionUtils.isEmpty(inventoryGoodsBos)) {
            return Result.wrapErrorResult("", "未选择盘点的配件");
        }
        inventoryFormBo.setInventoryGoodsBos(inventoryGoodsBos);

        // 如果生成正式盘点,校验配件的实盘库存是否存在
        int inventoryStatus = inventoryFormBo.getStatus();
        if (inventoryStatus == 2 && checkExistUnfillGoods(inventoryGoodsBos)) {
            return Result.wrapErrorResult("", "存在物料,未填写'实盘库存'");
        }

        // 配件盘点时:被删除的情况
        String deletedGoodsStr = checkGoodsUnExpectedDeleted(inventoryGoodsBos);
        if (!StringUtils.isEmpty(deletedGoodsStr)) {
            return Result.wrapErrorResult("deleted", deletedGoodsStr);
        }

        Result result = null;
        try {
            result = inventoryService.generate(inventoryFormBo, currentLoginUser);
        } catch (Exception e) {
            log.error("创建配件盘点异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "创建配件盘点失败");
        }

        return result;
    }


    /**
     * 变革盘点表 由草稿->正式
     *
     * @param itemId 主键ID
     * @return
     */
    @RequestMapping(value = "stock-inventory-toformal", method = RequestMethod.GET)
    @ResponseBody
    public Result toFormal(@RequestParam(value = "itemid", required = true) Long itemId) {
        UserInfo currentLoginUser = UserUtils.getUserInfo(request);

        // get 盘点记录
        Optional<InventoryRecord> inventoryRecordOptional = inventoryService.get(itemId, currentLoginUser.getShopId());
        if (!inventoryRecordOptional.isPresent()) {
            return Result.wrapErrorResult("", "生成盘点失败,盘点记录不存在");
        }
        InventoryRecord inventoryRecord = inventoryRecordOptional.get();

        // check 是否已盘点
        Integer status = inventoryRecord.getStatus();
        if (status != null && status == InventoryStatusEnum.FORMAL.getCode()) {
            return Result.wrapErrorResult("", "已经生成过盘点记录,无须重复生成");
        }

        // 盘点记录ID
        Long inventoryRecordId = inventoryRecord.getId();
        List<InventoryStock> inventoryStocks = inventoryService.getInventoryStocks(inventoryRecordId, currentLoginUser.getShopId());

        // 是否存在配件未填写'实盘库存'
        int existUnfillGoods = 1;
        // TODO: 16/11/9 判断list是否为空
        for (InventoryStock inventoryStock : inventoryStocks) {
            BigDecimal realStock = inventoryStock.getRealStock();
            if (realStock == null) {
                existUnfillGoods = existUnfillGoods & 0;
                break;
            }
        }
        if (existUnfillGoods == 0) {
            return Result.wrapErrorResult("", "存在物料未填写'实盘库存' <br/> 请进入编辑页面 填写.");
        }

        // 配件被意外删除的情况
        Set<Long> goodsIdSet = new HashSet<Long>();
        for (InventoryStock inventoryStock : inventoryStocks) {
            goodsIdSet.add(inventoryStock.getGoodsId());
        }
        Long[] goodsIds = goodsIdSet.toArray(new Long[goodsIdSet.size()]);
        List<Goods> deletedGoodsList = goodsService.selectDeletedGoods(goodsIds);
        if (!CollectionUtils.isEmpty(deletedGoodsList)) {
            StringBuffer deletedGoods = new StringBuffer("");
            int deletedGoodsTotal = deletedGoodsList.size();
            for (int goodsIndex = 0; goodsIndex < deletedGoodsTotal; goodsIndex++) {
                deletedGoods.append(deletedGoodsList.get(goodsIndex).getFormat());
                if (goodsIndex != deletedGoodsTotal - 1) {
                    deletedGoods.append(" | ");
                }
            }

            return Result.wrapErrorResult("deleted", deletedGoods.toString());
        }

        Result result = null;
        try {
            result = inventoryService.turnIntoFormal(inventoryRecord, currentLoginUser);
        } catch (Exception e) {
            log.error("生成盘点记录异常,异常信息:{}", e);
            return Result.wrapErrorResult("", "生成盘点失败");
        }

        return result;
    }


    /**
     * 进入库存盘单编辑页面
     *
     * @param itemId 主键ID
     * @param model
     * @return
     */
    @RequestMapping(value = "stock-inventory-edit", method = RequestMethod.GET)
    public String toInventoryEdit(@RequestParam(value = "itemid", required = true) Long itemId, Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());

        Long shopId = UserUtils.getShopIdForSession(request);

        Optional<InventoryRecord> inventoryRecordOptional = inventoryService.get(itemId, shopId);
        if (inventoryRecordOptional.isPresent()) {
            InventoryRecord inventoryRecord = inventoryRecordOptional.get();
            inventoryRecord.setGmtCreate(new Date());
            model.addAttribute("record", inventoryRecord);

            // 获取配件盘点
            List<InventoryStock> inventoryStockList = getInventoryStockList(itemId, shopId);
            if (!CollectionUtils.isEmpty(inventoryStockList)) {
                model.addAttribute("stocks", inventoryStockList);
            }
        }

        return "yqx/page/warehouse/stock/stock-inventory-edit";
    }

    /**
     * update inventory record
     *
     * @param inventoryFormBo
     * @return
     */
    @RequestMapping(value = "stock-inventory-update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(InventoryFormBo inventoryFormBo) {
        UserInfo currentLoginUser = UserUtils.getUserInfo(request);

        // check property
        Long recordId = inventoryFormBo.getRecordId();
        if (recordId == null || recordId < 0l) {
            return Result.wrapErrorResult("", "盘点记录不存在");
        }
        Long inventoryCheckerId = inventoryFormBo.getInventoryCheckerId();
        if (inventoryCheckerId == null || inventoryCheckerId < 0l) {
            return Result.wrapErrorResult("", "未选择'盘点人'");
        }

        // 盘点配件
        String inventoryGoodsJSON = inventoryFormBo.getInventoryGoodsJSON();
        if (StringUtils.isEmpty(inventoryGoodsJSON)) {
            return Result.wrapErrorResult("", "未选择盘点的配件");
        }
        Gson gson = new Gson();
        List<InventoryGoodsBo> inventoryGoodsBos = new ArrayList<InventoryGoodsBo>();
        try {
            inventoryGoodsBos = gson.fromJson(inventoryGoodsJSON,
                    new TypeToken<List<InventoryGoodsBo>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            log.error("创建盘点单异常,配件JSON转对象失败 配件JSON:{}", inventoryGoodsJSON);
            return Result.wrapErrorResult("", "创建盘点单失败！");
        }
        if (CollectionUtils.isEmpty(inventoryGoodsBos)) {
            return Result.wrapErrorResult("", "未选择盘点的配件");
        }
        inventoryFormBo.setInventoryGoodsBos(inventoryGoodsBos);

        // 如果生成正式盘点,校验配件的实盘库存是否存在
        int inventoryStatus = inventoryFormBo.getStatus();
        if (inventoryStatus == 2 && checkExistUnfillGoods(inventoryGoodsBos)) {
            return Result.wrapErrorResult("", "存在物料,未填写'实盘库存'");
        }

        // 配件盘点时:被删除的情况
        String deletedGoodsStr = checkGoodsUnExpectedDeleted(inventoryGoodsBos);
        if (!StringUtils.isEmpty(deletedGoodsStr)) {
            return Result.wrapErrorResult("deleted", deletedGoodsStr);
        }

        Result result = null;
        // TODO: 16/11/9 不需要捕获异常,不要facade,service不要用result 返回
        try {
            result = inventoryService.updateRecordAndStock(inventoryFormBo, currentLoginUser);
        } catch (Exception e) {
            log.error("创建配件盘点异常,异常信息:", e);
            return Result.wrapErrorResult("", "创建配件盘点失败");
        }

        return result;
    }


    /**
     * 检查 盘点过程中配件被意外删除的情况
     *
     * @param inventoryGoodsBos 被盘点派件集合
     * @return 配件1 | 配件2 |
     */
    private String checkGoodsUnExpectedDeleted(List<InventoryGoodsBo> inventoryGoodsBos) {
        // 被删除配件ID
        String deletedGoods = null;
        Set<Long> goodsIdSet = new HashSet<Long>();
        for (InventoryGoodsBo inventoryStock : inventoryGoodsBos) {
            goodsIdSet.add(inventoryStock.getGoodsId());
        }
        Long[] goodsIds = goodsIdSet.toArray(new Long[goodsIdSet.size()]);
        List<Goods> deletedGoodsList = goodsService.selectDeletedGoods(goodsIds);
        if (!CollectionUtils.isEmpty(deletedGoodsList)) {
            StringBuffer deletedGoodsSB = new StringBuffer("");
            int deletedGoodsTotal = deletedGoodsList.size();
            for (int goodsIndex = 0; goodsIndex < deletedGoodsTotal; goodsIndex++) {
                deletedGoodsSB.append(deletedGoodsList.get(goodsIndex).getFormat());
                if (goodsIndex != deletedGoodsTotal - 1) {
                    deletedGoodsSB.append(" | ");
                }
            }

            deletedGoods = deletedGoodsSB.toString();
        }

        return deletedGoods;
    }

    /**
     * 校验是否存在未填写'实盘库存'的配件
     *
     * @param inventoryGoodsBos 被盘点配件集合
     * @return
     */
    private boolean checkExistUnfillGoods(List<InventoryGoodsBo> inventoryGoodsBos) {
        for (InventoryGoodsBo inventoryGoods : inventoryGoodsBos) {
            BigDecimal realStock = inventoryGoods.getRealStock();
            if (realStock == null) {
                return Boolean.TRUE;
            }
        }

        return false;
    }


    /**
     * 计算盈亏金额
     *
     * @param itemIds 被盘点记录集合
     * @return
     */
    @RequestMapping(value = "stock-inventory-calculate", method = RequestMethod.GET)
    @ResponseBody
    public Result calculateInventoryAmount(@RequestParam(value = "itemids", required = true) String itemIds) {
        UserInfo currentLoginUser = UserUtils.getUserInfo(request);

        if (StringUtils.isEmpty(itemIds)) {
            return Result.wrapErrorResult("", "被盘点记录不存在");
        }
        String[] inventoryRecordStrIds = itemIds.split(",");
        Set<Long> inventoryRecordIdSet = new HashSet<Long>(inventoryRecordStrIds.length);
        for (String inventoryRecordId : inventoryRecordStrIds) {
            inventoryRecordIdSet.add(Long.parseLong(inventoryRecordId));
        }

        Long[] recordIds = inventoryRecordIdSet.toArray(new Long[inventoryRecordIdSet.size()]);
        Result result = null;
        try {
            result = inventoryService.calculateInventoryAmount(recordIds, currentLoginUser);
        } catch (Exception e) {
            log.error("calculate inventory amount failure", e);
            return Result.wrapErrorResult("", "");
        }

        return result;
    }


    /**
     * 获取云修采购价
     *
     * @param itemIds 配件列表IDS
     * @return
     */
    @RequestMapping(value = "stock-warning-inventoryprice", method = RequestMethod.GET)
    @ResponseBody
    public Result getGoodsInventoryPrice(@RequestParam(value = "itemids", required = true) String itemIds) {
        UserInfo currentLoginUser = UserUtils.getUserInfo(request);

        if (StringUtils.isEmpty(itemIds)) {
            return Result.wrapErrorResult("", "查询配件不存在");
        }
        String[] goodsIdStrs = itemIds.split(",");
        Set<Long> goodsSet = new HashSet<Long>(goodsIdStrs.length);
        for (String goodsId : goodsIdStrs) {
            goodsSet.add(Long.parseLong(goodsId));
        }

        Long[] goodsIds = goodsSet.toArray(new Long[goodsSet.size()]);
        Result result = null;
        try {
            result = inventoryService.batchGetGoodsInventoryPrice(goodsIds, currentLoginUser);
        } catch (Exception e) {
            log.error("get goods inventory price failure", e);
            return Result.wrapErrorResult("", "");
        }

        return result;
    }


    /**
     * 库存预警 商品分页搜索
     * <p/>
     * [应用场景]
     * 1. 仓库-库存预警
     * 2. 淘汽采购-缺件配件
     *
     * @param request
     * @param pageable
     * @return
     */
    @RequestMapping(value = "stockwarning/search/json", method = RequestMethod.GET)
    @ResponseBody
    public Result stockWarningSearchJSON(HttpServletRequest request,
                                         @PageableDefault(page = 0, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        // 请求参数
        LegendShortageGoodsRequest goodsRequest = wrapperStockWarningSearchParam(request);

        // 页参数(页号,每页个数,排序)
        int pageNum = pageable.getPageNumber() - 1;
        int pageSize = pageable.getPageSize();
        Sort sort = pageable.getSort();
        FieldsSort fieldsSort = new FieldsSort(sort);
        PageableRequest pageableRequest = new PageableRequest(pageNum, pageSize, fieldsSort);
        DefaultPage<SearchGoodsVo> searchPage = goodsFacade.goodsPageSearchInStockWarning(goodsRequest, pageableRequest);
        searchPage.setPageUri(request.getRequestURI());
        searchPage.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(searchPage);
    }


    /**
     * 库存预警导出
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "stockwarning_export", method = RequestMethod.GET)
    @ResponseBody
    public void stockWarningExport(HttpServletRequest request, HttpServletResponse response) throws Exception{

        UserInfo userInfo = UserUtils.getUserInfo(request);
        long sTime = System.currentTimeMillis();
        // 请求参数
        LegendShortageGoodsRequest goodsRequest = wrapperStockWarningSearchParam(request);
        // 导出最大500条
        PageableRequest pageableRequest = new PageableRequest(0, 500, Sort.Direction.DESC, new String[]{"id"});
        List<SearchGoodsVo> searGoodsVO = queryGoodsVO(request, goodsRequest, pageableRequest);
        ExcelExportor exportor = null;

        try {
            exportor = ExcelHelper.createDownloadExportor(response, "仓库预警列表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            int recordSize = 0;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——仓库预警列表";
            exportor.writeTitle(null, headline, SearchGoodsCommission.class);
            while(searGoodsVO != null && Langs.isNotEmpty(searGoodsVO)) {
                recordSize += searGoodsVO.size();
                List<SearchGoodsCommission> commissionList =converter(searGoodsVO);
                exportor.write(commissionList);
                pageableRequest = new PageableRequest(pageableRequest.getPageNumber()+1,pageableRequest.getPageSize(),Sort.Direction.DESC,new String[]{"id"});
                goodsRequest = wrapperStockWarningSearchParam(request);
                searGoodsVO = queryGoodsVO(request, goodsRequest, pageableRequest);
            }
            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("仓库预警列表导出", userInfo, recordSize, exportTime);
            log.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    private List<SearchGoodsVo> queryGoodsVO(HttpServletRequest request, LegendShortageGoodsRequest goodsRequest, PageableRequest pageableRequest) {
        DefaultPage<SearchGoodsVo> searchPage = null;
        try {
            searchPage = goodsFacade.goodsPageSearchInStockWarning(goodsRequest, pageableRequest);
        } catch (Exception e) {
            log.error("根据关键字获取物料列表信息异常，异常信息{}", e);
        }
        List<SearchGoodsVo> orderInfoDetailDTO = searchPage.getContent();

        if (!CollectionUtils.isEmpty(orderInfoDetailDTO)) {
            inventoryService.batchGetGoodsInventoryPrice(orderInfoDetailDTO, UserUtils.getUserInfo(request));
        } else {
            orderInfoDetailDTO = new ArrayList<>();
        }
        return orderInfoDetailDTO;
    }

    private List<SearchGoodsCommission> converter(List<SearchGoodsVo> searchGoodsVos){
        List<SearchGoodsCommission> commissions = Lists.newArrayList();
        if(null != searchGoodsVos || !CollectionUtils.isEmpty(searchGoodsVos)){
            for(SearchGoodsVo goodsVo : searchGoodsVos){
                SearchGoodsCommission commission = new SearchGoodsCommission();
                BeanUtils.copyProperties(goodsVo,commission);
                commissions.add(commission);
            }
        }
        return commissions;
    }


    /**
     * 包装库存预警列表查询参数实体
     *
     * @param request
     * @return
     */
    private LegendShortageGoodsRequest wrapperStockWarningSearchParam(HttpServletRequest request) {

        Long shopId = UserUtils.getShopIdForSession(request);

        // 拼凑查询条件
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        // 预警库存范围
        Object shortageNumberScopeObj = searchParams.get("shortageNumberScope");
        if (shortageNumberScopeObj != null && shortageNumberScopeObj instanceof String) {
            String shortageNumberScope = (String) shortageNumberScopeObj;
            // 预警起始值
            String shortageNumberStart = null;
            // 预警结束值
            String shortageNumberEnd = null;
            if (shortageNumberScope.indexOf("-") != -1) {
                shortageNumberStart = shortageNumberScope.split("-")[0];
                shortageNumberEnd = shortageNumberScope.split("-")[1];
                searchParams.put("shortageNumberEnd", shortageNumberEnd);
            } else {
                shortageNumberStart = shortageNumberScope;
            }
            searchParams.put("shortageNumberStart", shortageNumberStart);
        }

        // 查询参数实体
        LegendShortageGoodsRequest goodsRequest = new LegendShortageGoodsRequest();
        // 门店 1:在售的 0:实际库存
        goodsRequest.setShopId(shopId);
        goodsRequest.setOnsaleStatus(1);
        goodsRequest.setGoodsType(0);

        // 配件分类 标准配件|自定义配件 [[
        if (searchParams.containsKey("catId")) {
            goodsRequest.setCatId(Long.valueOf(searchParams.get("catId") + ""));
        }
        if (searchParams.containsKey("stdCatId")) {
            goodsRequest.setStdCatId(Integer.valueOf(searchParams.get("stdCatId") + ""));
        }

        // 品牌
        if (searchParams.containsKey("brandId")) {
            goodsRequest.setBrandId(Long.valueOf(searchParams.get("brandId") + ""));
        }

        // 配件名称
        if (searchParams.containsKey("goodsNameLike")) {
            goodsRequest.setName((String) searchParams.get("goodsNameLike"));
        }

        // 适配车型
        if (searchParams.containsKey("carInfoLike")) {
            goodsRequest.setCarInfo((String) searchParams.get("carInfoLike"));
        }
        // 配件名称、零件号、配件编码 模糊查询
        if (searchParams.containsKey("likeKeyWords")) {
            goodsRequest.setLikeKeyWords((String) searchParams.get("likeKeyWords"));
        }
        // 预警库存范围
        if (searchParams.containsKey("shortageNumberStart")) {
            goodsRequest.setShortageNumberStart(Integer.parseInt(searchParams.get("shortageNumberStart") + ""));
        }
        if (searchParams.containsKey("shortageNumberEnd")) {
            goodsRequest.setShortageNumberEnd(Integer.parseInt(searchParams.get("shortageNumberEnd") + ""));
        }
        if (searchParams.containsKey("tqmallStatus")) {
            String tqmallStatusStr =(String)searchParams.get("tqmallStatus");
            String[] tqmallStatusArr =tqmallStatusStr.split(",");
            List<Integer> tqmallStatuses =new ArrayList<Integer>(tqmallStatusArr.length);
            for(String tqmallStatus: tqmallStatusArr){
                tqmallStatuses.add(Integer.valueOf(tqmallStatus));
            }
            goodsRequest.setTqmallStatus(tqmallStatuses);
        }
        return goodsRequest;
    }


    /**
     * 批量判断工单出库状态
     *
     * @param itemIds 配件列表IDS
     * @return
     */
    @RequestMapping(value = "getstockstatus", method = RequestMethod.GET)
    @ResponseBody
    public Result getStockStatus(@RequestParam(value = "itemids", required = true) String itemIds) {
        Long shopId = UserUtils.getShopIdForSession(request);

        if (StringUtils.isEmpty(itemIds)) {
            return Result.wrapErrorResult("", "查询工单不存在");
        }
        String[] orderIdStrs = itemIds.split(",");
        Set<Long> orderSet = new HashSet<Long>(orderIdStrs.length);
        for (String goodsId : orderIdStrs) {
            orderSet.add(Long.parseLong(goodsId));
        }

        Long[] orderIds = orderSet.toArray(new Long[orderSet.size()]);
        try {
            List<StockStatusOfOrder> stockStatusOfOrders = inventoryService.assertStockStatus(orderIds, shopId);
            return Result.wrapSuccessfulResult(stockStatusOfOrders);
        } catch (Exception e) {
            log.error("批量判断工单出库状态异常, 异常信息:{}", e);
            return Result.wrapErrorResult("", "");
        }
    }

    /**
     * 盘点记录列表数据
     *
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping(value = "records_list", method = RequestMethod.GET)
    @ResponseBody
    public Result recordList(@PageableDefault(page = 1, value = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {

        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        // 盘点单号
        if (searchParams.containsKey("recordSnLike")) {
            searchParams.put("recordSnLike", searchParams.get("recordSnLike").toString().trim());
        }

        // 开始日期
        if (searchParams.containsKey("startTime")) {
            searchParams.put("startTime", searchParams.get("startTime") + " 00:00:00");
        }

        // 结束日期
        if (searchParams.containsKey("endTime")) {
            searchParams.put("endTime", searchParams.get("endTime") + " 23:59:59");
        }

        // 配件名称
        if (searchParams.containsKey("goodsNameLike")) {
            searchParams.put("goodsNameLike", searchParams.get("goodsNameLike").toString().trim());
        }

        DefaultPage<InventoryRecord> page = null;
        page = (DefaultPage<InventoryRecord>) inventoryService.getInventoryRecordPage(pageable, searchParams);
        page.setPageUri(request.getRequestURI());
        page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 删除盘点记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "record_del", method = RequestMethod.POST)
    @ResponseBody
    public Result recordDel(Long id) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        return inventoryService.recordDel(id, userInfo);
    }


    /**
     * 导出被盘点商品
     *
     * @param id       盘点记录ID
     * @param response
     * @return
     */
    @RequestMapping(value = "inventory-export", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView stockExport(Long id, HttpServletResponse response) {
        Long shopId = UserUtils.getShopIdForSession(request);
        long sTime = System.currentTimeMillis();
        List<InventoryStock> inventoryStockList = getInventoryStockList(id, shopId);
        String path = "yqx/page/warehouse/stock/inventory-export";
        ModelAndView view = ExcelExportUtil.export(inventoryStockList, response, path, "库存盘点信息", sTime);
        return view;
    }
}
