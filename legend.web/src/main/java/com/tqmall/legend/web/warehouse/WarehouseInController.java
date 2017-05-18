package com.tqmall.legend.web.warehouse;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tqmall.common.UpperNumbers;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.GoodsUtils;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.warehousein.WarehouseIn;
import com.tqmall.legend.entity.warehousein.WarehouseInDetail;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.warehouse.WarehouseInStatusEnum;
import com.tqmall.legend.facade.warehouse.WarehouseInFacade;
import com.tqmall.legend.facade.warehouse.bo.WarehouseInDetailBo;
import com.tqmall.legend.facade.warehouse.vo.LegendWarehouseInDTOVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseInDetailVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseInVo;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.legend.web.warehouse.convert.WarehouseInExportConvert;
import com.tqmall.legend.web.warehouse.vo.WarehouseInExportVO;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.warehousein.param.LegendWarehouseInParam;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.exception.WheelException;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by sven on 16/08/09.
 */
@Controller
@RequestMapping("/shop/warehouse/in")
@Slf4j
public class WarehouseInController extends BaseController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private WarehouseInFacade warehouseInFacade;

    @Autowired
    private GoodsService goodsService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return ModuleUrlEnum.WAREHOUSE.getModuleUrl();
    }

    /**
     * 详情页
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "in-detail", method = RequestMethod.GET)
    public String detail(@RequestParam(value = "id") Long id, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        WarehouseInVo warehouseInVo = warehouseInFacade.select(id, userInfo.getShopId());
        model.addAttribute("warehouseInVo", warehouseInVo);
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        return "yqx/page/warehouse/in/in-detail";
    }

    /**
     * @param source 草稿,蓝字入库blue  草稿编辑draft
     * @param model
     * @return
     */
    @RequestMapping(value = "in-edit/{source}", method = RequestMethod.GET)
    public String edit(@PathVariable("source") String source,
                       @RequestParam(value = "goodsIds", required = false) String goodsIds,
                       @RequestParam(value = "id", required = false) Long id, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        List<Long> ids = GoodsUtils.idsToList(goodsIds);
        if (!CollectionUtils.isEmpty(ids)) {
            List<Goods> goodsList = goodsService.selectByIds(ids.toArray(new Long[ids.size()]));
            model.addAttribute(goodsList);
        }
        if ("blue".equals(source)) {
            String sn = warehouseInFacade.getSn(shopId, "LR");
            model.addAttribute("warehouseInSn", sn);
            model.addAttribute("operatorName", userInfo.getName());
        } else if ("draft".equals(source) && id != null) {
            WarehouseInVo warehouseInVo = warehouseInFacade.select(id, shopId);
            model.addAttribute("warehouseInVo", warehouseInVo);
        } else {
            return "common/error";
        }
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        return "yqx/page/warehouse/in/in-edit";
    }

    /**
     * 红字入库
     *
     * @param
     * @param model
     * @return
     */
    @RequestMapping(value = "in-red", method = RequestMethod.GET)
    public String red(@RequestParam(value = "id") Long id, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (id == null) {
            return "common/error";
        }
        String sn = warehouseInFacade.getSn(shopId, "HR");
        WarehouseInVo warehouseInVo = warehouseInFacade.select(id, shopId);
        if (warehouseInVo == null || !WarehouseInStatusEnum.LZRK.name().equals(warehouseInVo.getStatus())) {
            return "common/error";
        }
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        model.addAttribute("warehouseInVo", warehouseInVo);
        model.addAttribute("operatorName", userInfo.getName());
        model.addAttribute("warehouseInSn", sn);
        return "yqx/page/warehouse/in/in-red";
    }

    /**
     * 草稿保存/编辑
     *
     * @param action
     * @return
     */
    @RequestMapping(value = "in-edit/draft/{action}", method = RequestMethod.POST)
    @ResponseBody
    public Result saveDraft(@PathVariable("action") String action, @RequestParam("warehouseIn") String warehouseIn, @RequestParam("warehouseInDetails") String warehouseInDetails) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        WarehouseIn warehouse = translateWarehouseIn(warehouseIn);
        List<WarehouseInDetail> warehouseInDetailList = translateDetail(warehouseInDetails);
        String message = checkData(warehouse, warehouseInDetailList);
        if (message != null) {
            return Result.wrapErrorResult("", message);
        }
        Long id = 0L;
        if ("save".equals(action)) {
            id = warehouseInFacade.saveDraft(warehouse, warehouseInDetailList, userInfo);
        } else if ("update".equals(action)) {
            warehouseInFacade.updateDraft(warehouse, warehouseInDetailList, userInfo);
            id = warehouse.getId();
        }
        return Result.wrapSuccessfulResult(id);


    }

    /**
     * 编辑转入库
     *
     * @return
     */
    @RequestMapping("in-edit/draft/stock")
    @ResponseBody
    public Result draftToStock(@RequestParam("warehouseIn") String warehouseIn, @RequestParam("warehouseInDetails") String warehouseInDetails) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        WarehouseIn warehouse = translateWarehouseIn(warehouseIn);
        List<WarehouseInDetail> warehouseInDetailList = translateDetail(warehouseInDetails);
        String message = checkData(warehouse, warehouseInDetailList);
        if (message != null) {
            return Result.wrapErrorResult("", message);
        }
        warehouseInFacade.draftToStock(warehouse, warehouseInDetailList, userInfo);
        return Result.wrapSuccessfulResult(warehouse.getId());
    }

    /**
     * 详情转入库
     *
     * @return
     */
    @RequestMapping("in-detail/draft/stock")
    @ResponseBody
    public Result draftToStock(@RequestParam("id") Long id) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        warehouseInFacade.draftToStock(id, userInfo, true);
        return Result.wrapSuccessfulResult(id);
    }

    /**
     * 删除草稿
     * 删除作废入库记录
     *
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteDraft(@RequestParam("id") Long id) {
        if (id == null) {
            return Result.wrapErrorResult("", "id不存在,删除失败");
        }
        Long shopId = UserUtils.getShopIdForSession(request);

        boolean flag = warehouseInFacade.delete(id, shopId);
        if (!flag) {
            return Result.wrapErrorResult("", "删除失败");
        }
        return Result.wrapSuccessfulResult(id);
    }

    /**
     * 红字入库
     *
     * @param warehouseIn
     * @param warehouseInDetails
     * @return
     */
    @RequestMapping(value = "in-edit/red/save", method = RequestMethod.POST)
    @ResponseBody
    public Result saveRed(@RequestParam("warehouseIn") String warehouseIn, @RequestParam("warehouseInDetails") String warehouseInDetails) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        WarehouseIn warehouse = translateWarehouseIn(warehouseIn);
        List<WarehouseInDetail> warehouseInDetailList = translateDetail((warehouseInDetails));

        String message = checkData(warehouse, warehouseInDetailList);
        if (message != null) {
            return Result.wrapErrorResult("", message);
        }
        Long id = warehouseInFacade.stockRefund(warehouse, warehouseInDetailList, userInfo);
        return Result.wrapSuccessfulResult(id);
    }

    /**
     * 蓝字入库
     *
     * @param warehouseIn
     * @param warehouseInDetails
     * @return
     */
    @RequestMapping(value = "in-edit/blue/save", method = RequestMethod.POST)
    @ResponseBody
    public Result<Long> saveBlue(@RequestParam("warehouseIn") String warehouseIn, @RequestParam("warehouseInDetails") String warehouseInDetails) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        WarehouseIn warehouse = translateWarehouseIn(warehouseIn);
        List<WarehouseInDetail> warehouseInDetailList = translateDetail(warehouseInDetails);
        String message = checkData(warehouse, warehouseInDetailList);
        if (message != null) {
            return Result.wrapErrorResult("", message);
        }
        Long id = warehouseInFacade.stock(warehouse, warehouseInDetailList, userInfo);
        return Result.wrapSuccessfulResult(id);
    }

    //转换入库单
    private WarehouseIn translateWarehouseIn(String warehouseIn) {
        WarehouseIn warehouse = new Gson().fromJson(warehouseIn, new TypeToken<WarehouseIn>() {
        }.getType());
        warehouse.setInTime(DateUtil.convertStringToDate1(warehouse.getInTimeStr()));
        return warehouse;
    }

    //转换入库单详情
    private List<WarehouseInDetail> translateDetail(String detail) {
        List<WarehouseInDetail> warehouseInDetailList = new Gson().fromJson(detail, new TypeToken<List<WarehouseInDetail>>() {
        }.getType());
        return warehouseInDetailList;
    }


    /**
     * 作废
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "abolish", method = RequestMethod.POST)
    @ResponseBody
    public Result abolish(@RequestParam("id") Long id) {
        if (id == null) {
            return Result.wrapErrorResult("作废失败", "id不存在");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            warehouseInFacade.abolishStock(id, userInfo);
        } catch (BizException e) {
            log.error("作废失败,原因{}", e);
            return Result.wrapErrorResult("", e.getMessage());
        } catch (Exception e1) {
            log.error("作废失败,原因{}", e1);
            return Result.wrapErrorResult("", "作废失败");
        }
        return Result.wrapSuccessfulResult(id);
    }

    //打印
    @RequestMapping(value = "print", method = RequestMethod.GET)
    public String print(@RequestParam("id") Long id, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Shop shop = shopService.selectById(userInfo.getShopId());
        model.addAttribute("shop", shop);
        BigDecimal totalCount = BigDecimal.ZERO;
        WarehouseInVo warehouseInVo = warehouseInFacade.select(id, userInfo.getShopId());
        if (warehouseInVo == null) {
            return "common/error";
        }
        if (!CollectionUtils.isEmpty(warehouseInVo.getDetailList())) {
            for (WarehouseInDetailVo warehouseInDetailVo : warehouseInVo.getDetailList()) {
                if (warehouseInDetailVo.getGoodsCount() == null) {
                    warehouseInDetailVo.setGoodsCount(BigDecimal.ZERO);
                }
                totalCount = totalCount.add(warehouseInDetailVo.getGoodsCount().abs());
            }
        }
        model.addAttribute("totalCount", totalCount);
        if (warehouseInVo.getTotalAmount() == null) {
            warehouseInVo.setTotalAmount(BigDecimal.ZERO);
        }
        model.addAttribute("chineseTotalAmount", UpperNumbers.toChinese(warehouseInVo.getTotalAmount().doubleValue() + ""));
        model.addAttribute("warehouseInVo", warehouseInVo);
        return "yqx/page/warehouse/in/in-print";
    }

    //列表初始化
    @HttpRequestLog
    @RequestMapping(value = "in-list", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        return "yqx/page/warehouse/in/in-list";
    }

    /**
     * 列表数据获取
     *
     * @param type  list 列表
     * @param pageable
     * @return
     */
    @RequestMapping(value = "in-list/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Object getList(@PathVariable("type") String type,
                          @PageableDefault(value = 1, size = 6, sort = "in_time desc,id desc", direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> searchParam = ServletUtils.getParametersMapStartWith(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        PageableRequest page = pageTranslate(pageable, type);
        LegendWarehouseInParam param = makeParam(searchParam, userInfo.getShopId());
        DefaultPage<LegendWarehouseInDTOVo> defaultPage = warehouseInFacade.select(param, page);
        return Result.wrapSuccessfulResult(defaultPage);
    }

    /**
     * 入库单 全量导出
     */
    @RequestMapping("all-export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response){
        long startTime = System.currentTimeMillis();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Map<String, Object> searchParam = ServletUtils.getParametersMapStartWith(request);
        PageableRequest page = null;
        LegendWarehouseInParam param = makeParam(searchParam, userInfo.getShopId());
        int totalSize = 0;
        BigDecimal totalGoodsCount = BigDecimal.ZERO;
        BigDecimal totalAmount = null;
        int pageNum = 0;
        int pageSize = 500;
        String fileName = "入库单-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——入库明细报表";
            String startTimeStr = (String) searchParam.get("startTime");
            String endTimeStr = (String) searchParam.get("endTime");

            exportTitle(exportor, headline, startTimeStr, endTimeStr);
            while (true) {
                page = new PageableRequest(pageNum, pageSize, Sort.Direction.DESC, "in_time", "id");
                DefaultPage<LegendWarehouseInDTOVo> defaultPage = warehouseInFacade.select(param, page);
                if (defaultPage == null){
                    break;
                }
                List<LegendWarehouseInDTOVo> legendWarehouseInDTOVos = defaultPage.getContent();
                if (CollectionUtils.isEmpty(legendWarehouseInDTOVos)){
                    break;
                }
                List<WarehouseInExportVO> exportVOList = new ArrayList<>();
                for (LegendWarehouseInDTOVo warehouseInVo : legendWarehouseInDTOVos) {
                    exportVOList.addAll(WarehouseInExportConvert.convert(warehouseInVo));
                }
                for (WarehouseInExportVO warehouseInExportVO : exportVOList) {
                    totalGoodsCount = totalGoodsCount.add(warehouseInExportVO.getGoodsCount());
                }
                if (totalAmount == null) {
                    Map<String, Object> otherData = defaultPage.getOtherData();
                    if (otherData != null && otherData.containsKey("totalAmountStatistics")) {
                        String totalAmountStatistics = (String) otherData.get("totalAmountStatistics");
                        if (Langs.isNotBlank(totalAmountStatistics)) {
                            totalAmount = new BigDecimal(totalAmountStatistics);
                        }
                    }
                }
                exportor.write(exportVOList);
                totalSize += exportVOList.size();
                pageNum++;
            }
            //总计
            totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
            exportTotal(exportor, totalGoodsCount, totalAmount);
        } catch (Exception e) {
            log.error("[全量导出入库单列表错误]shopId:{}",userInfo.getShopId(),e);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog("入库单", userInfo, totalSize, endTime - startTime));
    }

    private void exportTitle(ExcelExportor exportor, String title, String startTime, String endTime) throws WheelException {
        Row row = exportor.createRow();
        Cell cell0 = row.createCell(0);
        cell0.setCellValue(title);
        exportor.mergeCell(0, row.getRowNum(), row.getRowNum(), row.getFirstCellNum(), 12);

        Cell cell13 = row.createCell(13);
        cell13.setCellValue(startTime);

        Cell cell14 = row.createCell(14);
        cell14.setCellValue("至");

        Cell cell15 = row.createCell(15);
        cell15.setCellValue(endTime);
    }

    private void exportTotal(ExcelExportor exportor, BigDecimal totalGoodsCount, BigDecimal totalAmount) throws WheelException {
        Row row = exportor.createRow();
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("合计");
        Cell cell7 = row.createCell(7);
        cell7.setCellValue(totalGoodsCount.toString());
        Cell cell9 = row.createCell(9);
        cell9.setCellValue(totalAmount.toString());
    }

    /**
     * 接车维修转入库
     * 批量入库
     */
    @RequestMapping(value = "blue/batch", method = RequestMethod.POST)
    @ResponseBody
    public Result batchStock(@RequestBody List<WarehouseInDetailBo> warehouseInDetails) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        warehouseInFacade.batchStock(warehouseInDetails, userInfo);
        // 查询商品新库存信息
        Set<Long> goodsSet = new HashSet<Long>();
        for (WarehouseInDetailBo detailBo : warehouseInDetails) {
            goodsSet.add(detailBo.getGoodsId());
        }
        List<Goods> goodses = new ArrayList<Goods>();
        if (goodsSet.size() > 0) {
            goodses = goodsService.selectByIds(goodsSet.toArray(new Long[goodsSet.size()]));
        }

        return Result.wrapSuccessfulResult(goodses);
    }

    /**
     * 淘汽采购入库
     *
     * @param orderSn
     * @return
     */
    @RequestMapping("tqmall/stock")
    public String tqmallStock(@RequestParam(value = "uid") Long uid,
                              @RequestParam(value = "orderSn") String orderSn, Model model) {
        if (uid == null || StringUtils.isBlank(orderSn)) {
            log.error("根据用户Id= {},订单号orderSn={},获取采购入库信息异常", uid, orderSn);
            return "common/error";
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        WarehouseInVo warehouseInVo = warehouseInFacade.getTqmallStockInfo(orderSn, uid, userInfo);
        model.addAttribute("warehouseInSn", warehouseInFacade.getSn(userInfo.getShopId(), "LR"));
        model.addAttribute("warehouseInVo", warehouseInVo);
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        return "yqx/page/warehouse/in/in-edit";
    }

    //保存数据校验
    private String checkData(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList) {
        if (CollectionUtils.isEmpty(warehouseInDetailList)) {
            return "配件不存在,操作失败";
        }
        if (warehouseIn == null) {
            return "参数不能为空";
        }
        if (warehouseIn.getTotalAmount() == null) {
            return "总金额不能为空";
        }
        if (warehouseIn.getPurchaseAgent() == null) {
            return "采购人不能为空";
        }
        if (warehouseIn.getSupplierId() == null) {
            return "该供应商不存在有误";
        }
        return null;
    }

    /**
     * 组装查询条件
     *
     * @param searchParam
     * @param shopId
     * @return
     */
    private LegendWarehouseInParam makeParam(Map<String, Object> searchParam, Long shopId) {
        LegendWarehouseInParam param = new LegendWarehouseInParam();
        param.setShopId(shopId.toString());
        if (searchParam.containsKey("endTime")) {
            param.setEndTime(searchParam.get("endTime") + " 23:59:59");
        }
        if (searchParam.containsKey("startTime")) {
            param.setStartTime(searchParam.get("startTime") + " 00:00:00");
        }
        if (searchParam.containsKey("warehouseInSn")) {
            param.setWarehouseInSn(searchParam.get("warehouseInSn").toString());
        }
        if (searchParam.containsKey("carInfo")) {
            param.setCarInfo(searchParam.get("carInfo").toString());
        }
        if (searchParam.containsKey("conditionLike")) {
            param.setConditionLike(searchParam.get("conditionLike").toString());
        }
        if (searchParam.containsKey("purchaseAgent")) {
            param.setPurchaseAgent(searchParam.get("purchaseAgent").toString());
        }
        if (searchParam.containsKey("supplierId")) {
            param.setSupplierId(searchParam.get("supplierId").toString());
        }
        List<String> statusList = new ArrayList<>();
        if (searchParam.containsKey("status")) {
            statusList.add(searchParam.get("status").toString());
        } else {
            statusList = Lists.newArrayList("LZRK", "LZZF", "HZRK", "HZZF");
        }
        param.setStatusList(statusList);
        return param;
    }

    /**
     * 分页条件组装
     *
     * @param pageable
     * @param type
     * @return
     */
    private PageableRequest pageTranslate(Pageable pageable, String type) {
        int pageNumber = pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber() - 1;
        PageableRequest page = new PageableRequest(pageNumber, pageable.getPageSize(), Sort.Direction.DESC, "in_time", "id");
        if ("export".equals(type)) {
            page = new PageableRequest(0, 1000, Sort.Direction.DESC, "in_time", "id");
        }
        if ("oldList".equals(type)) {
            page = new PageableRequest(pageNumber, 10, Sort.Direction.DESC, "in_time", "id");
        }
        return page;
    }

}
