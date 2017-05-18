package com.tqmall.legend.web.settlement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.billcenter.client.RpcPayService;
import com.tqmall.legend.billcenter.client.dto.PayBillDTO;
import com.tqmall.legend.billcenter.client.dto.PayTypeDTO;
import com.tqmall.legend.billcenter.client.enums.PayTypeEnum;
import com.tqmall.legend.billcenter.client.param.PayDeleteParam;
import com.tqmall.legend.billcenter.client.param.PayInvalidParam;
import com.tqmall.legend.billcenter.client.param.PayParam;
import com.tqmall.legend.billcenter.client.result.PayBillAndFlowResult;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.settlement.vo.PayBillVo;
import com.tqmall.legend.biz.settlement.vo.PayResultVo;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.SupplierService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.biz.warehousein.WarehouseInService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.Supplier;
import com.tqmall.legend.entity.shop.SupplierSettlementVO;
import com.tqmall.legend.entity.warehousein.WarehouseIn;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.settlement.PayStatusEnum;
import com.tqmall.legend.facade.settlement.PayFacade;
import com.tqmall.legend.facade.warehouse.vo.WarehouseInStatistics;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sven.zhang on 16/06/01.
 * 付款结算
 */
@Controller
@RequestMapping("shop/settlement/pay")
@Slf4j
public class PayController extends BaseController {
    @Autowired
    private PayFacade payFacade;
    @Autowired
    private WarehouseInService warehouseInService;
    @Autowired
    private SupplierService supplierService;
    @Resource
    private RpcPayService rpcPayService;
    @Autowired
    private ShopManagerService shopManagerService;
    //支付状态为1-开启
    private final static Integer OPEN_STATUS = 1;
    @Autowired
    private ShopService shopService;

    /**
     * 跳转到供应商付款
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/pay-supplier", method = RequestMethod.GET)
    public String paySupplier(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        return "yqx/page/settlement/pay/pay-supplier";
    }

    /**
     * 供应商付款查询
     *
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/pay-supplier/summary", method = RequestMethod.GET)
    @ResponseBody
    public Result paySupplier(@PageableDefault(page = 1, value = 12,
            direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        //整理查询条件
        boolean flag = modifyParams(searchParams, pageable);
        if (!flag) {
            DefaultPage page = new DefaultPage(new ArrayList());
            return Result.wrapSuccessfulResult(page);
        }

        List<SupplierSettlementVO> supplierSettlementVOs = payFacade.getSupplierAomountList(searchParams);
        //不分页,获取所有
        searchParams.remove("limit");
        searchParams.remove("offset");
        List<SupplierSettlementVO> supplierSettlementVOList = payFacade.getSupplierAomountList(searchParams);
        long totalSize = warehouseInService.getSupplierCountWarehouseIn(searchParams);
        if (CollectionUtils.isEmpty(supplierSettlementVOs)) {
            DefaultPage page = new DefaultPage(new ArrayList());
            return Result.wrapSuccessfulResult(page);
        }
        BigDecimal amountPayableStatistics = BigDecimal.ZERO;
        BigDecimal totalAmountStatistics = BigDecimal.ZERO;
        BigDecimal amountPaidStatistics = BigDecimal.ZERO;
        BigDecimal goodsAmountStatistics = BigDecimal.ZERO;
        for (SupplierSettlementVO vo : supplierSettlementVOList) {
            amountPayableStatistics = amountPayableStatistics.add(vo.getAmountPayable());
            totalAmountStatistics = totalAmountStatistics.add(vo.getTotalAmount());
            goodsAmountStatistics = goodsAmountStatistics.add(vo.getGoodsAmount());
            amountPaidStatistics = amountPaidStatistics.add(vo.getAmountPaid());
        }
        Map<String, Object> param = getStatisticsMap(amountPayableStatistics, totalAmountStatistics, amountPaidStatistics, goodsAmountStatistics);
        DefaultPage<SupplierSettlementVO> page = new DefaultPage(supplierSettlementVOs, pageable, totalSize);
        page.setOtherData(param);
        Result result = handlePage(page);
        return result;
    }

    private Map<String, Object> getStatisticsMap(BigDecimal amountPayableStatistics, BigDecimal totalAmountStatistics, BigDecimal amountPaidStatistics, BigDecimal goodsAmountStatistics) {
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("amountPayableStatistics", amountPayableStatistics.setScale(2, BigDecimal.ROUND_HALF_UP));
        param.put("totalAmountStatistics", totalAmountStatistics.setScale(2, BigDecimal.ROUND_HALF_UP));
        param.put("amountPaidStatistics", amountPaidStatistics.setScale(2, BigDecimal.ROUND_HALF_UP));
        param.put("goodsAmountStatistics", goodsAmountStatistics.setScale(2, BigDecimal.ROUND_HALF_UP));
        return param;
    }

    /**
     * 供应商付款excel导出
     */
    @RequestMapping(value = "/pay-supplier/export", method = RequestMethod.GET)
    @ResponseBody
    public void paySupplierExport(@PageableDefault(page = 1, value = 500, direction = Sort.Direction.DESC) Pageable pageable,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        String fileName = "供应商付款-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        //整理查询条件
        boolean flag = modifyParams(searchParams, pageable);

        long startTime = System.currentTimeMillis();
        ExcelExportor exportor = null;
        int totalSize = 0;
        int offset = 0;
        int step = 500;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——供应商付款";
            exportor.writeTitle(null, headline, WarehouseIn.class);
            while (flag) {
                List<WarehouseIn> warehouseInList = warehouseInService.select(searchParams);
                if (CollectionUtils.isEmpty(warehouseInList)) {
                    break;
                }
                exportor.write(warehouseInList);
                totalSize += warehouseInList.size();
                offset += step;
                searchParams.put("offset", offset);
            }
        } catch (Exception e) {
            log.error("供应商付款导出异常，门店id：{}", userInfo.getShopId(), e);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog("供应商付款", userInfo, totalSize, endTime - startTime));
    }

    /**
     * 供应商付款明细列表
     *
     * @param model
     * @param supplierId 供应商Id
     * @return
     */
    @RequestMapping(value = "/pay-supplier-list", method = RequestMethod.GET)
    public String paySupplierList(Model model, @RequestParam(value = "supplierId", required = false) Long supplierId,
                                  @RequestParam(value = "supplierName", required = false) String supplierName,
                                  @RequestParam(value = "startInTime", required = false) String startInTime,
                                  @RequestParam(value = "endInTime", required = false) String endInTime) {
        Map<String, Object> params = new HashMap<>();
        //将查询时间存入隐藏域
        if (!StringUtil.isNull(startInTime)) {
            startInTime = startInTime + " 00:00:00";
            params.put("startInTime", startInTime);
            model.addAttribute("startInTime", startInTime);
        }
        if (!StringUtil.isNull(endInTime)) {
            endInTime = endInTime + " 23:59:59";
            params.put("endInTime", endInTime);
            model.addAttribute("endInTime", endInTime);
        }
        params.put("supplierId", supplierId);
        params.put("supplierName", supplierName);
        params.put("shopId", UserUtils.getShopIdForSession(request));

        params.put("statusList", Lists.newArrayList("LZRK", "HZRK"));
        List<SupplierSettlementVO> supplierSettlementVOs = payFacade.getSupplierAomountList(params);
        if (!CollectionUtils.isEmpty(supplierSettlementVOs)) {
            model.addAttribute("supplierSettlementVO", supplierSettlementVOs.get(0));
        }
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        return "yqx/page/settlement/pay/pay-supplier-list";
    }

    /**
     * 全部收款
     */
    @RequestMapping(value = "/pay-supplier-list/all", method = RequestMethod.GET)
    @ResponseBody
    public Result payAll() {
        Map<String, Object> param = ServletUtils.getParametersMapStartWith(request);
        if (param.containsKey("supplierName")) {
            param.put("supplierName", StringEscapeUtils.unescapeHtml((String) param.get("supplierName")));
        }
        param.put("isDeleted", "N");
        param.put("statusList", Lists.newArrayList("LZRK", "HZRK"));
        if (param.containsKey("paymentStatus")) {
            List<String> paymentStatusList = Lists.newArrayList(PayStatusEnum.BFZF.name(), PayStatusEnum.DDZF.name());
            param.put("paymentStatusList", paymentStatusList);
        }
        param.remove("paymentStatus");
        List<WarehouseIn> warehouseInList = warehouseInService.select(param);
        if (CollectionUtils.isEmpty(warehouseInList)) {
            return Result.wrapErrorResult("", "不存在需付款的记录");
        }
        List<Long> ids = new ArrayList<>();
        for (WarehouseIn warehouseIn : warehouseInList) {
            ids.add(warehouseIn.getId());
        }
        return Result.wrapSuccessfulResult(ids.toArray());
    }

    /**
     * 供应商付款明细列表数据
     *
     * @param pageable
     * @return
     */

    @RequestMapping(value = "/pay-supplier-list/list", method = RequestMethod.GET)
    @ResponseBody
    public Result paySupplierList(@PageableDefault(page = 1, value = 12,
            direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> params = ServletUtils.getParametersMapStartWith(request);
        if (params.containsKey("supplierName")) {
            params.put("supplierName", StringEscapeUtils.unescapeHtml((String) params.get("supplierName")));
        }
        //查询未支付完成的入库单
        if (params.containsKey("paymentStatus")) {
            List<String> paymentStatusList = Lists.newArrayList(PayStatusEnum.BFZF.name(), PayStatusEnum.DDZF.name());
            params.put("paymentStatusList", paymentStatusList);
        }
        params.remove("paymentStatus");
        pageAndSort(params, pageable);
        params.put("statusList", Lists.newArrayList("LZRK", "HZRK"));
        List<WarehouseIn> warehouseInList = warehouseInService.select(params);
        int totalSize = warehouseInService.selectCount(params);
        if (CollectionUtils.isEmpty(warehouseInList)) {
            DefaultPage page = new DefaultPage(new ArrayList());
            return Result.wrapSuccessfulResult(page);
        }
        BigDecimal amountPayableStatistics = BigDecimal.ZERO;
        BigDecimal totalAmountStatistics = BigDecimal.ZERO;
        BigDecimal amountPaidStatistics = BigDecimal.ZERO;
        BigDecimal goodsAmountStatistics = BigDecimal.ZERO;
        //若入库时间不存在则使用创建时间
        for (WarehouseIn warehouseIn : warehouseInList) {
            if (warehouseIn.getInTime() == null) {
                warehouseIn.setInTime(warehouseIn.getGmtCreate());
            }
            warehouseIn.setInTimeStr(DateUtil.convertDateToYMD(warehouseIn.getInTime()));
            amountPayableStatistics = amountPayableStatistics.add(warehouseIn.getAmountPayable());
            totalAmountStatistics = totalAmountStatistics.add(warehouseIn.getTotalAmount());
            goodsAmountStatistics = goodsAmountStatistics.add(warehouseIn.getGoodsAmount());
            amountPaidStatistics = amountPaidStatistics.add(warehouseIn.getAmountPaid());
        }
        Map<String, Object> param = getStatisticsMap(amountPayableStatistics, totalAmountStatistics, amountPaidStatistics, goodsAmountStatistics);

        DefaultPage<WarehouseIn> page = new DefaultPage(warehouseInList, pageable, totalSize);
        page.setOtherData(param);
        Result result = handlePage(page);
        return result;
    }

    /**
     * 付款验证
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/pay-supplier-behavior/before", method = RequestMethod.GET)
    @ResponseBody
    public Result paySupplierBehaviorBefore(@RequestParam(value = "ids") String ids) {
        try {
            Map<String, Object> wareHouseInMap = getWareHouseIn(ids);
            List<WarehouseIn> warehouseInList = warehouseInService.select(wareHouseInMap);
            if (CollectionUtils.isEmpty(warehouseInList)) {
                return Result.wrapErrorResult("", "亲，您所选择的单据未付金额为0，无需进行付款操作");
            }
        } catch (IOException e) {
            log.error("数据转换异常,错误原因:{}", e);
            return Result.wrapErrorResult("", "数据转换异常");
        }
        return Result.wrapSuccessfulResult("数据验证成功");
    }

    /**
     * 供应商付款操作页面初始化
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/pay-supplier-behavior", method = RequestMethod.GET)
    public Object paySupplierBehavior(Model model, @RequestParam("ids") String ids) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            Map<String, Object> params = getWareHouseIn(ids);
            List<SupplierSettlementVO> supplierSettlementVOs = payFacade.getSupplierAomountList(params);
            List<WarehouseIn> warehouseInList = warehouseInService.select(params);
            //大于1 批量收款
            if (!CollectionUtils.isEmpty(warehouseInList) && warehouseInList.size() > 1) {
                model.addAttribute("modifyFlag", true);
            }
            if (!CollectionUtils.isEmpty(supplierSettlementVOs)) {
                model.addAttribute("supplierSettlementVO", supplierSettlementVOs.get(0));
            }
            if (!CollectionUtils.isEmpty(warehouseInList)) {
                List<Long> idList = new ArrayList<>();
                for (WarehouseIn warehouseIn : warehouseInList) {
                    idList.add(warehouseIn.getId());
                }
                model.addAttribute("ids", idList.toString());
            }
        } catch (IOException e) {
            log.error("数据转换异常,错误原因:{}", e);
            return "common/error";
        }
        model.addAttribute("operator", userInfo.getName());
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        return "yqx/page/settlement/pay/pay-supplier-behavior";
    }

    /**
     * 供应商付款操作页面表格数据初始化
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/pay-supplier-behavior/list", method = RequestMethod.GET)
    @ResponseBody
    public Result paySupplierBehaviorList(@RequestParam(value = "ids") String ids) {
        Map<String, Object> wareHouseInMap;
        try {
            wareHouseInMap = getWareHouseIn(ids);
        } catch (IOException e) {
            log.error("数据转换异常,错误原因:{}", e);
            return Result.wrapErrorResult("", "数据转换异常");
        }
        List<WarehouseIn> warehouseInList = warehouseInService.select(wareHouseInMap);
        if (CollectionUtils.isEmpty(warehouseInList)) {
            return Result.wrapErrorResult("", "查询不到对应的记录");
        }
        //统计数据
        WarehouseInStatistics statistics = new WarehouseInStatistics();
        statistics.setWarehouseInList(warehouseInList);
        List<WarehouseInStatistics> statisticsList = new ArrayList<>();
        statisticsList.add(statistics);
        DefaultPage<WarehouseInStatistics> page = new DefaultPage(statisticsList);
        Result result = handlePage(page);
        return result;
    }

    /**
     * 供应商付款操作页面 保存
     *
     * @param payBillVo
     * @return
     */
    @RequestMapping(value = "/pay-supplier-behavior/operate", method = RequestMethod.POST)
    @ResponseBody
    public Result paySupplierBehaviorOperate(@BeanParam PayBillVo payBillVo) {
        if (payBillVo == null) {
            return Result.wrapErrorResult("", "付款参数有误");
        }
        Map<String, Object> params;
        try {
            params = getWareHouseIn(payBillVo.getIds());
        } catch (IOException e) {
            log.error("数据转换异常,错误原因:{}", e);
            return Result.wrapErrorResult("", "数据转换异常");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        List<WarehouseIn> warehouseInList = warehouseInService.select(params);
        List<SupplierSettlementVO> supplierSettlementVOs = payFacade.getSupplierAomountList(params);
        //批量付款付款不可修改金额
        if (!CollectionUtils.isEmpty(warehouseInList) && warehouseInList.size() > 1) {
            if (!CollectionUtils.isEmpty(supplierSettlementVOs)) {
                if (payBillVo.getAmount().compareTo(supplierSettlementVOs.get(0).getAmountPayable()) != 0) {
                    return Result.wrapErrorResult("", "批量付款不允许修改付款金额");
                }
                for (WarehouseIn warehouseIn : warehouseInList) {
                    warehouseIn.setAmountPaid(warehouseIn.getTotalAmount());
                    warehouseIn.setGmtModified(new Date());
                    warehouseIn.setLatestPayTime(new Date());
                    warehouseIn.setAmountPayable(BigDecimal.ZERO);
                    warehouseIn.setPaymentStatus(PayStatusEnum.ZFWC.name());
                    warehouseIn.setModifier(userInfo.getUserId());
                    warehouseIn.setPaymentComment(payBillVo.getRemark());
                }
            }
        } else if (warehouseInList.size() == 1) { //单个付款可修改金额
            for (WarehouseIn warehouseIn : warehouseInList) {
                if (warehouseIn != null) {
                    //付款为负,应付为正
                    if (BigDecimal.ZERO.compareTo(payBillVo.getAmount()) > 0 && BigDecimal.ZERO.compareTo(warehouseIn.getAmountPayable()) < 0) {
                        return Result.wrapErrorResult("", "付款金额在" + warehouseIn.getAmountPayable() + "与0之间");
                    }
                    //付款为正,应付为负
                    if (BigDecimal.ZERO.compareTo(payBillVo.getAmount()) < 0 && BigDecimal.ZERO.compareTo(warehouseIn.getAmountPayable()) > 0) {
                        return Result.wrapErrorResult("", "付款金额在0与" + warehouseIn.getAmountPayable() + "之间");
                    }
                    //负数金额
                    if (BigDecimal.ZERO.compareTo(payBillVo.getAmount()) > 0) {
                        if (payBillVo.getAmount().compareTo(warehouseIn.getAmountPayable()) < 0) {
                            return Result.wrapErrorResult("", "付款金额在" + warehouseIn.getAmountPayable() + "与0之间");
                        }
                    }
                    //正数金额
                    if (BigDecimal.ZERO.compareTo(payBillVo.getAmount()) < 0) {
                        if (payBillVo.getAmount().compareTo(warehouseIn.getAmountPayable()) > 0) {
                            return Result.wrapErrorResult("", "付款金额在0与" + warehouseIn.getAmountPayable() + "之间");
                        }
                    }
                    if (BigDecimal.ZERO.compareTo(payBillVo.getAmount()) == 0) {
                        return Result.wrapErrorResult("", "金额为0,不需要付款");
                    }
                    warehouseIn.setModifier(userInfo.getUserId());
                    warehouseIn.setGmtModified(new Date());
                    warehouseIn.setLatestPayTime(new Date());
                    warehouseIn.setPaymentComment(payBillVo.getRemark());
                    warehouseIn.setAmountPaid(warehouseIn.getAmountPaid().add(payBillVo.getAmount()));
                    if (BigDecimal.ZERO.compareTo(warehouseIn.getAmountPayable().subtract(payBillVo.getAmount())) == 0) {
                        warehouseIn.setPaymentStatus(PayStatusEnum.ZFWC.name());
                    } else {
                        warehouseIn.setPaymentStatus(PayStatusEnum.BFZF.name());
                    }
                    warehouseIn.setAmountPayable(warehouseIn.getAmountPayable().subtract(payBillVo.getAmount()));
                }
            }

        } else {
            return Result.wrapErrorResult("", "对应的记录不存在");
        }
        List<Long> relIdList = (List) params.get("ids");
        log.info("[DUBBO] 调用结算中心付款接口 传入参数relIdList: {},payTypeId:{},shopId:{} ", JSONUtil.object2Json(relIdList), 1L, userInfo.getShopId());
        com.tqmall.core.common.entity.Result<List<PayBillDTO>> payBillResult = rpcPayService.selectPayBill(relIdList, 1L, userInfo.getShopId());
        log.info("[DUBBO] 调用结算中心付款接口 返回结果success:{} ", payBillResult.isSuccess());
        List<PayBillDTO> payBillDTOList = payBillResult.getData();
        if (CollectionUtils.isEmpty(payBillDTOList)) {
            return Result.wrapErrorResult("", "查询不到对应的记录信息");
        }
        try {
            payFacade.batchUpdate(warehouseInList, payBillDTOList, payBillVo, userInfo);
        } catch (Exception e) {
            return Result.wrapErrorResult("", "批量付款失败");
        }
        return Result.wrapSuccessfulResult("付款成功");
    }

    /**
     * 获取付款类型
     *
     * @return
     */
    @RequestMapping(value = "/get-pay-type", method = RequestMethod.GET)
    @ResponseBody
    public Result getPayType(@RequestParam(value = "action", required = false) Boolean action) {
        Long shopId = UserUtils.getShopIdForSession(request);
        log.info("[DUBBO]:调用结算中心接口获取付款类型,入参门店ID:{}", shopId);
        List<Long> shopIds = Lists.newArrayList(shopId);
        try {
            com.tqmall.core.common.entity.Result<List<PayTypeDTO>> result = rpcPayService.selectPayType(shopIds, OPEN_STATUS);
            if (!result.isSuccess()) {
                log.error("[DUBBO]:调用结算中心接口获取付款方式失败,错误原因:{}", JSONUtil.object2Json(result));
                return Result.wrapErrorResult("", "付款方式初始化失败");
            }
            log.info("[DUBBO]:调用结算中心接口获取付款方式成功");
            List<PayTypeDTO> payTypeDTOs = result.getData();
            if (action != null && action) {
                if (!CollectionUtils.isEmpty(payTypeDTOs)) {
                    Iterator it = payTypeDTOs.iterator();
                    while (it.hasNext()) {
                        PayTypeDTO typeDTO = (PayTypeDTO) it.next();
                        if (typeDTO != null) {
                            if (PayTypeEnum.SUPPLIER.getCode().equals(typeDTO.getId()) || PayTypeEnum.MESSAGE.getCode().equals(typeDTO.getId())) {
                                it.remove();
                            }
                        }
                    }
                }
            }
            return Result.wrapSuccessfulResult(payTypeDTOs);
        } catch (Exception e) {
            log.error("[DUBBO]:调用结算中心接口获取付款方式失败,错误原因:{}", e);
            return Result.wrapErrorResult("", "付款方式初始化失败");
        }
    }

    /**
     * 添加付款类型,支持批量
     *
     * @param payTypeStr
     * @return
     */
    @RequestMapping("/pay-type-save")
    @ResponseBody
    public Result batchSavePayType(@RequestParam("payTypeStr") String payTypeStr) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            if (StringUtils.isNotBlank(payTypeStr)) {
                List<PayTypeDTO> payTypeList = new Gson().fromJson(payTypeStr, new TypeToken<List<PayTypeDTO>>() {
                }.getType());
                if (!CollectionUtils.isEmpty(payTypeList)) {
                    for (PayTypeDTO payType : payTypeList) {
                        payType.setShopId(userInfo.getShopId());
                    }
                    com.tqmall.core.common.entity.Result<Integer> result = rpcPayService.batchSavePayType(payTypeList);
                    if (!result.isSuccess()) {
                        log.error("[DUBBO]调用billcenter接口批量保存付款类型失败, 参数:{},调用结果:{}", new Gson().toJson(payTypeList), new Gson().toJson(result));
                        return Result.wrapErrorResult("", result.getMessage());
                    }
                    //工单类型重复不处理
                }
            }
        } catch (Exception e) {
            log.error("添加付款类型失败", e);
            return Result.wrapErrorResult("", "保存付款类型失败");
        }
        //跳转至服务类别页面
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 新增付款单页面初始化
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/pay-add", method = RequestMethod.GET)
    public String payAdd(Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        model.addAttribute("operatorName", userInfo.getName());
        return "yqx/page/settlement/pay/pay-add";
    }

    /**
     * 保存付款单数据
     *
     * @param payForm
     * @return
     */
    @RequestMapping(value = "/pay-add/save", method = RequestMethod.POST)
    @ResponseBody
    public Result paySave(@RequestParam(value = "payForm") String payForm) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            PayBillVo payBillVo = mapper.readValue(payForm, PayBillVo.class);
            if (payBillVo.getAmount() == null || BigDecimal.ZERO.compareTo(payBillVo.getAmount()) >= 0) {
                return Result.wrapErrorResult(" ", "付款失败,金额必须大于等于0");
            }
            Long id = payFacade.saveBill(payBillVo, userInfo);
            return Result.wrapSuccessfulResult(id);
        } catch (IOException e) {
            log.error("操作人:{},新建付款单失败,错误:{}", userInfo.getName(), e);
            return Result.wrapErrorResult("", "付款单保存失败");
        }
    }

    /**
     * 流水列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/pay-flow", method = RequestMethod.GET)
    public String payList(Model model,
                          @RequestParam(value = "startTime", required = false) String startTime,
                          @RequestParam(value = "endTime", required = false) String endTime) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());

        // 营业报表跳转过来
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date startDate = df.parse(startTime);
                Date endDate = df.parse(endTime);
                if (startDate.after(new Date()) || startDate.after(endDate)) {
                    return "yqx/page/settlement/pay/pay-flow";
                }
            } catch (ParseException e) {
                log.error("营业报表跳转付款单流水表,日期格式错误:参数:startTime={}, endTime={}, 异常信息:", startTime, endTime, e);
                return "yqx/page/settlement/pay/pay-flow";
            }
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
        }
        return "yqx/page/settlement/pay/pay-flow";
    }

    /**
     * 流水列表数据初始化以及excel导出
     *
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/pay-flow-list", method = RequestMethod.GET)
    @ResponseBody
    public Object payFlowList(@PageableDefault(page = 1, value = 12) Pageable pageable) {
        PayParam payParam = buildPayParam(pageable);
        DefaultPage<PayResultVo> page = payFacade.getPayFlowPage(payParam);
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 付款流水导出
     * @param pageable
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/export-pay-flow-list", method = RequestMethod.GET)
    @ResponseBody
    public void exportFlowList(@PageableDefault(page = 1, value = 12) Pageable pageable,
                               HttpServletResponse response) {
        PayParam payParam = buildPayParam(pageable);
        payParam.setPageSize(Constants.MAX_PAGE_SIZE);

        UserInfo userInfo = UserUtils.getUserInfo(request);
        String fileName = "付款流水信息-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");

        long startTime = System.currentTimeMillis();

        ExcelExportor exportor = null;
        int pageNum = 1;
        int totalSize = 0;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——付款流水信息";
            exportor.writeTitle(null, headline, PayResultVo.class);
            while (true) {
                payParam.setPageNum(pageNum++);
                DefaultPage<PayResultVo> page = payFacade.getPayFlowPage(payParam);
                if (page == null) {
                    break;
                }
                List<PayResultVo> content = page.getContent();
                if (CollectionUtils.isEmpty(content)) {
                    break;
                }
                exportor.write(content);
                totalSize += content.size();
                if (totalSize >= page.getTotalElements()) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("付款流水信息导出异常，门店id：{}", userInfo.getShopId(), e);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog("付款流水信息", userInfo, totalSize, endTime - startTime));
    }

    private PayParam buildPayParam(Pageable pageable) {
        Map<String, Object> searchParam = ServletUtils.getParametersMapStartWith(request);
        PayParam payParam = new PayParam();
        payParam.setShopId(Long.valueOf(searchParam.get("shopId").toString()));
        if (searchParam.containsKey("conditionLike")) {
            payParam.setConditionLike(searchParam.get("conditionLike").toString());
        }
        if (searchParam.containsKey("startTime")) {
            payParam.setStartTime(searchParam.get("startTime").toString() + " 00:00:00");
        }
        if (searchParam.containsKey("endTime")) {
            payParam.setEndTime(searchParam.get("endTime").toString() + " 23:59:59");
        }
        if (searchParam.containsKey("payTypeId")) {
            payParam.setPayTypeId(Long.valueOf(searchParam.get("payTypeId").toString()));
        }
        if (searchParam.containsKey("paymentId")) {
            payParam.setPaymentId(Long.valueOf(searchParam.get("paymentId").toString()));
        }
        payParam.setPageNum(pageable.getPageNumber());
        payParam.setPageSize(pageable.getPageSize());
        return payParam;
    }

    /**
     * 付款单详情页初始化
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/pay-detail", method = RequestMethod.GET)
    public String payDetail(@RequestParam("id") Long id, Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        boolean flag = wrapperPayDetail(id,shopId,model);
        if (!flag){
            return "common/error";
        }
        return "yqx/page/settlement/pay/pay-detail";
    }

    /**
     * 付款单详情打印
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/print/payment", method = RequestMethod.GET)
    public String payDetailPrint(@RequestParam("id") Long id, Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        boolean flag = wrapperPayDetail(id,shopId,model);
        if (!flag){
            return "common/error";
        }
        return "yqx/page/settlement/pay/print/payment";
    }

    private boolean wrapperPayDetail(Long id , Long shopId , Model model){
        List<Long> ids = Lists.newArrayList(id);
        //设置门店信息
        Shop shop = shopService.selectById(shopId);
        model.addAttribute("shop",shop);
        log.info("[DUBBO]获取付款单详情传入参数:{}", shopId);
        com.tqmall.core.common.entity.Result<List<PayBillAndFlowResult>> result = null;
        try {
            result = rpcPayService.queryBill(ids, shopId);
            if (result == null || !result.isSuccess()) {
                log.error("[DUBBO]获取付款单详情失败,错误原因:{}", JSONUtil.object2Json(result));
                return false;
            }
        } catch (Exception e) {
            log.error("[DUBBO]获取付款单详情接口异常,错误原因:{}", e);
            return false;
        }
        if (CollectionUtils.isEmpty(result.getData())) {
            return false;
        }
        PayBillAndFlowResult payBillAndFlowResult = result.getData().get(0);

        if (payBillAndFlowResult != null) {
            model.addAttribute("payBillAndFlowResult", payBillAndFlowResult);
            //获取流水创建人，收银人
            if (Langs.isNotEmpty(payBillAndFlowResult.getPayBillFlowDTOList())) {
                Long flowCreator = payBillAndFlowResult.getPayBillFlowDTOList().get(0).getCreator();
                ShopManager shopManager = shopManagerService.selectById(flowCreator);
                if(shopManager !=  null){
                    model.addAttribute("flowCreatorName" , shopManager.getName());
                }
                model.addAttribute("flowBillTime", payBillAndFlowResult.getPayBillFlowDTOList().get(0).getGmtCreate());
            }

            PayBillDTO payBill = payBillAndFlowResult.getPayBillDTO();
            if (payBill != null) {
                //判断是否可以无效
                if (!PayTypeEnum.MESSAGE.getCode().equals(payBill.getPayTypeId()) &&
                        !PayTypeEnum.SUPPLIER.getCode().equals(payBill.getPayTypeId()) && payBill.getRelId() == 0L) {
                    List<Long> relIds = Lists.newArrayList(payBill.getId());
                    log.info("[DUBBO]调用结算中心查询付款记录接口,入参 relIds:{},payTypeId:{},shopId:{}", relIds, payBill.getPayTypeId(), shopId);
                    com.tqmall.core.common.entity.Result<List<PayBillDTO>> billResult = null;
                    try {
                        //是否已经无效操作
                        billResult = rpcPayService.selectPayBill(relIds, payBill.getPayTypeId(), shopId);
                        if (!billResult.isSuccess()) {
                            log.error("[DUBBO]调用结算中心查询付款记录接口失败,错误原因:{}", JSONUtil.object2Json(billResult));
                        }
                        if (billResult.isSuccess() && CollectionUtils.isEmpty(billResult.getData())) {
                            model.addAttribute("isInvalid", true);
                        } else {
                            model.addAttribute("isDeleted", true);
                        }
                    } catch (Exception e) {
                        log.error("[DUBBO]调用结算中心查询付款记录接口异常,错误原因:{}", e);
                    }
                }
            }
        }
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        return true;
    }

    //付款单无效
    @RequestMapping(value = "pay-invalid")
    @ResponseBody
    public Result payInvalid(@RequestParam("id") Long id) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        PayInvalidParam param = new PayInvalidParam();
        param.setShopId(userInfo.getShopId());
        param.setCreator(userInfo.getUserId());
        param.setBillId(id);
        param.setOperatorName(userInfo.getName());
        try {
            log.info("[DUBBO]调用结算中心付款单无效接口,入参:{}", JSONUtil.object2Json(param));
            com.tqmall.core.common.entity.Result<Integer> result = rpcPayService.invalidPayBill(param);
            if (!result.isSuccess()) {
                log.error("[DUBBO]调用结算中心付款单无效接口失败,错误原因:{}", JSONUtil.object2Json(result));
                return Result.wrapErrorResult("", "无效失败");
            }
        } catch (Exception e) {
            log.error("[DUBBO]调用结算中心付款单无效接口异常,错误原因:{}", e);
            return Result.wrapErrorResult("", "无效失败");
        }
        return Result.wrapSuccessfulResult("无效成功");
    }

    /**
     * 付款单删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "pay-delete", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> payDelete(@RequestParam("id") final Long id) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "付款单id为空");
            }

            @Override
            protected Boolean process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                PayDeleteParam param = new PayDeleteParam();
                param.setShopId(userInfo.getShopId());
                param.setModifier(userInfo.getUserId());
                param.setBillId(id);
                param.setOperatorName(userInfo.getName());
                try {
                    log.info("[DUBBO]调用结算中心付款单删除接口,入参:{}", LogUtils.objectToString(param));
                    com.tqmall.core.common.entity.Result<Boolean> result = rpcPayService.deletePayBill(param);
                    if (!result.isSuccess()) {
                        log.error("[DUBBO]调用结算中心付款单删除接口失败,错误原因:{}", LogUtils.objectToString(result));
                        throw new BizException("删除失败");
                    }
                    return true;
                } catch (Exception e) {
                    log.error("[DUBBO]调用结算中心付款单删除接口异常,错误原因:{}", e);
                    throw new BizException("删除失败");
                }
            }
        }.execute();
    }

    /**
     * 组装查询入库的条件
     *
     * @param ids
     * @return
     * @throws IOException
     */
    private Map<String, Object> getWareHouseIn(String ids) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();
        Long shopId = UserUtils.getShopIdForSession(request);
        params.put("shopId", shopId);
        params.put("statusList", Lists.newArrayList("LZRK", "HZRK"));
        if (!StringUtil.isNull(ids)) {
            List<Long> idsList = objectMapper.readValue(ids, new TypeReference<List<Long>>() {
            });
            params.put("ids", idsList);
            List<String> paymentStatusList = Lists.newArrayList(PayStatusEnum.BFZF.name(), PayStatusEnum.DDZF.name());
            params.put("paymentStatusList", paymentStatusList);
            return params;
        }
        return null;
    }

    /**
     * 处理请求参数条件
     */

    private boolean modifyParams(Map<String, Object> searchParams, Pageable pageable) {
        if (searchParams.containsKey("startInTime")) {
            searchParams.put("startInTime", searchParams.get("startInTime").toString() + " 00:00:00");
        }
        if (searchParams.containsKey("endInTime")) {
            searchParams.put("endInTime", searchParams.get("endInTime").toString() + " 23:59:59");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", searchParams.get("shopId"));
        if (searchParams.containsKey("supplierId")) {
            map.put("id", searchParams.get("supplierId"));
        }
        boolean existFlag = getSupplierParam(searchParams, map);
        pageAndSort(searchParams, pageable);
        searchParams.put("statusList", Lists.newArrayList("LZRK", "HZRK"));
        return existFlag;
    }

    /**
     * 创建日期降序排列,分页
     *
     * @param searchParams
     * @param pageable
     */
    private void pageAndSort(Map<String, Object> searchParams, Pageable pageable) {
        ArrayList<String> sorts = new ArrayList<String>();
        sorts.add("gmt_create desc");
        searchParams.put("sorts", sorts);
        searchParams.put("isDeleted", "N");
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize());
        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());
    }

    /**
     * 供应商与付款方式处理
     */

    private boolean getSupplierParam(Map<String, Object> searchParams, Map<String, Object> map) {
        if (searchParams.containsKey("payMethod")) {
            if (searchParams.containsKey("supplierId")) {
                searchParams.remove("supplierId");
            }
            map.put("payMethod", searchParams.get("payMethod"));
            List<Supplier> supplierList = supplierService.select(map);
            List<Long> supplierIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(supplierList)) {
                for (Supplier supplier : supplierList) {
                    supplierIds.add(supplier.getId());
                }
                searchParams.put("supplierIds", supplierIds);
            } else {
                return false;
            }

        }
        return true;
    }

    private Result handlePage(DefaultPage page) {
        if (!CollectionUtils.isEmpty(page.getContent())) {
            page.setPageUri(request.getRequestURI());
            page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        }
        return Result.wrapSuccessfulResult(page);
    }

}

