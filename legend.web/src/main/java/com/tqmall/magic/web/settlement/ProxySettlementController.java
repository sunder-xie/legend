package com.tqmall.magic.web.settlement;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopTagRelService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.enums.magic.ProxyStatusEnum;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.legend.facade.magic.BoardFacade;
import com.tqmall.legend.facade.magic.ProxyFacade;
import com.tqmall.legend.facade.magic.vo.*;
import com.tqmall.legend.facade.settlement.ShareSettlementFacade;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.magic.object.param.proxy.ProxyPageParam;
import com.tqmall.magic.object.param.proxy.ProxyParam;
import com.tqmall.magic.object.result.common.PageEntityDTO;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import com.tqmall.magic.service.proxy.RpcProxyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by TonyStarkSir on 16/5/11.
 */
@Controller
@Slf4j
@RequestMapping("/proxy/settlement")
public class ProxySettlementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShareSettlementFacade shareSettlementFacade;
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    private BoardFacade boardFacade;
    @Autowired
    private ProxyFacade proxyFacade;

    @Autowired
    private ShopTagRelService shopTagRelService;

    /**
     * 跳转到委托对账单
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model modelUrl) {
        modelUrl.addAttribute("moduleUrl", "settlement");
        return "yqx/page/magic/settlement/proxy_settlement";
    }

    /**
     * 跳转到委托对账单汇总
     *
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String toProxySettlementInfo(Model modelUrl) {
        modelUrl.addAttribute("moduleUrl", "settlement");
        return "yqx/page/magic/settlement/proxy_settlement_info";
    }

    /**
     * 对账单单个结算
     *
     * @param id      //委托单id
     * @param request
     * @return
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.legend.common.Result confirmStatement(Long id, HttpServletRequest request) {
        log.info("[共享中心] 委托方对账单单个结算,参数:id={}", id);
        Result result = null;
        try {
            result = rpcProxyService.getProxyInfoById(id);
        } catch (Exception e) {
            log.error("根据id获取委托单失败");
            return com.tqmall.legend.common.Result.wrapErrorResult("", "获取委托单失败，操作失败");
        }

        ProxyDTO proxyDTO = (ProxyDTO) result.getData();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        com.tqmall.legend.common.Result results = shareSettlementFacade.shareOrderSettle(proxyDTO, userInfo);
        if (results.isSuccess() == true) {
            ProxyDTO newProxyDTO = new ProxyDTO();
            newProxyDTO.setId(id);
            newProxyDTO.setProxyStatus(ProxyStatusEnum.YJQ.getCode());
            newProxyDTO.setOrderStatus(OrderNewStatusEnum.DDYFK.getOrderStatus());
            newProxyDTO.setAccountTime(new Date());
            Result myResult = null;
            try {
                myResult = rpcProxyService.updateProxyOrder(newProxyDTO);
            } catch (Exception e) {
                log.error("委托对账单结算失败");
                return com.tqmall.legend.common.Result.wrapErrorResult("", "结算失败");
            }
            if (myResult.isSuccess() == true) {
                if (shopFunFacade.isUseWorkshop(userInfo.getShopId())) {
                    boardFacade.sendMessage(userInfo.getShopId(), Lists.newArrayList(proxyDTO.getProxyId()));
                }
                return com.tqmall.legend.common.Result.wrapSuccessfulResult("结算成功");
            } else {
                return com.tqmall.legend.common.Result.wrapErrorResult("", "结算失败");
            }
        } else {
            return results;
        }
    }

    /**
     * 对账单批量结算
     *
     * @param ids     //委托单id集合
     * @param request
     * @return
     */
    @RequestMapping(value = "all/confirm", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.legend.common.Result confirmStatements(String ids, HttpServletRequest request) {
        log.info("[共享中心] 委托方对账单批量结算,参数:ids={}", ids);
        String[] sourceStrArray = ids.split(",");
        Integer successTotal = 0;
        Integer failTotal = 0;
        UserInfo userInfo = UserUtils.getUserInfo(request);
        List<Long> proxyIds = new ArrayList<>();//受托方工单id
        for (int i = 0; i < sourceStrArray.length; i++) {
            Long id = Long.valueOf(sourceStrArray[i]);
            Result result = null;
            try {
                result = rpcProxyService.getProxyInfoById(id);
            } catch (Exception e) {
                log.error("获取委托单失败");
                return com.tqmall.legend.common.Result.wrapErrorResult("", "获取委托单失败，操作失败");
            }

            ProxyDTO proxyDTO = (ProxyDTO) result.getData();
            com.tqmall.legend.common.Result results = shareSettlementFacade.shareOrderSettle(proxyDTO, userInfo);
            if (results.isSuccess() == true) {
                ProxyDTO newProxyDTO = new ProxyDTO();
                newProxyDTO.setId(id);
                newProxyDTO.setProxyStatus(ProxyStatusEnum.YJQ.getCode());
                newProxyDTO.setOrderStatus(OrderNewStatusEnum.DDYFK.getOrderStatus());
                newProxyDTO.setAccountTime(new Date());
                Result myResult = null;
                try {
                    myResult = rpcProxyService.updateProxyOrder(newProxyDTO);
                } catch (Exception e) {
                    log.error("委托单结算失败");
                    return com.tqmall.legend.common.Result.wrapErrorResult("", "结算失败");
                }

                if (myResult.isSuccess() == true) {
                    proxyIds.add(proxyDTO.getProxyId());
                    successTotal = successTotal + 1;
                } else {
                    failTotal = failTotal + 1;
                }
            } else {
                log.info("错误详情=" + results.getErrorMsg());
                failTotal = failTotal + 1;
            }
        }
        if (!CollectionUtils.isEmpty(proxyIds)) {
            if (shopFunFacade.isUseWorkshop(userInfo.getShopId())) {
                boardFacade.sendMessage(userInfo.getShopId(), proxyIds);
            }
        }
        return com.tqmall.legend.common.Result.wrapSuccessfulResult("结算成功数量:" + successTotal + ",结算失败数量:" + failTotal);
    }

    /**
     * 对账单展示
     *
     * @param request
     * @param proxyShopId //委托方或者受托方门店id
     * @param shopFlag    //委托方还是受托方标识 1：委托方，2受托方
     * @param paymentType //账单类型 0：本月交车待结算;1:本月交车已结算;2：本月开单未交车;3:历史交车未结算;4:历史交车本月结算
     * @param pageable    //
     * @return
     */
    @RequestMapping(value = "/commision/bill", method = RequestMethod.GET)
    @ResponseBody
    public Result getCommisionBill(HttpServletRequest request, String proxyStartTime, String proxyEndTime, String completeStartTime, String completeEndTime, String carLicense, Long proxyShopId, Integer shopFlag, Integer paymentType, @PageableDefault(page = 1, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("[共享中心] 委托对账单查询展示,参数:proxyStartTime={},proxyEndTime={},completeStartTime={},completeEndTime={},carLicense={},proxyShopId={},shopFlag={},paymentType={},page={},size={}", proxyStartTime, proxyEndTime, completeStartTime, completeEndTime, carLicense, proxyShopId, shopFlag, paymentType, pageable.getPageNumber(), pageable.getPageSize());
        ProxySettlementParamVO proxySettlementParamVO = ProxySettlementUtils.getProxySettlementParam(request, proxyStartTime, proxyEndTime, completeStartTime, completeEndTime, carLicense, proxyShopId, shopFlag);
        if (proxySettlementParamVO == null) {
            log.error("[共享中心] 判断委托方还是受托方失败");
            return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_SUMMARY_TYPE_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_SUMMARY_TYPE_ERROR.getErrorMessage());
        }
        ProxyPageParam proxyPageParam = ProxySettlementUtils.getProxyPageParam(proxySettlementParamVO, carLicense, pageable);
        if (paymentType == 0) {
            //交车待结算
            proxyPageParam.setProxyStatus(ProxyStatusEnum.YJC.getCode());
            Result<PageEntityDTO<ProxyDTO>> result = null;
            try {
                result = rpcProxyService.getProxyListByPage(proxyPageParam);
            } catch (Exception e) {
                log.error("[共享中心] 获取本月交车待结算委托单失败,DUBBO接口报错");
                return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getErrorMessage());
            }
            DefaultPage defaultPage = new DefaultPage(result.getData().getRecordList(), pageable, result.getData().getTotalNum());
            return Result.wrapSuccessfulResult(defaultPage);
        } else if (paymentType == 1) {
            //交车已结算
            proxyPageParam.setProxyStatus(ProxyStatusEnum.YJQ.getCode());
            Result<PageEntityDTO<ProxyDTO>> result = null;
            try {
                result = rpcProxyService.getProxyListByPage(proxyPageParam);
            } catch (Exception e) {
                log.error("[共享中心] 获取本月交车已结算委托单失败，DUBBO接口报错");
                return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getErrorMessage());
            }
            DefaultPage defaultPage = new DefaultPage(result.getData().getRecordList(), pageable, result.getData().getTotalNum());
            return Result.wrapSuccessfulResult(defaultPage);
        } else if (paymentType == 2) {
            //开单未交车
            List<String> status = new ArrayList<>();
            status.add(ProxyStatusEnum.YWT.getCode());
            status.add(ProxyStatusEnum.YJD.getCode());
            status.add(ProxyStatusEnum.YYC.getCode());
            status.add(ProxyStatusEnum.YPG.getCode());
            status.add(ProxyStatusEnum.YWG.getCode());
            proxyPageParam.setProxyStatusList(status);
            Result<PageEntityDTO<ProxyDTO>> result = null;
            try {
                result = rpcProxyService.getProxyListByPage(proxyPageParam);
            } catch (Exception e) {
                log.error("[共享中心] 获取本月开单未交车委托单失败，DUBBO接口报错");
                return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getErrorMessage());
            }
            DefaultPage defaultPage = new DefaultPage(result.getData().getRecordList(), pageable, result.getData().getTotalNum());
            return Result.wrapSuccessfulResult(defaultPage);
        } else if (paymentType == 3) {
            //全部
            Result<PageEntityDTO<ProxyDTO>> result = null;
            try {
                result = rpcProxyService.getProxyListByPage(proxyPageParam);
            } catch (Exception e) {
                log.error("[共享中心] 获取历史交车未结算委托单失败,DUBBO接口报错");
                return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getErrorMessage());
            }
            DefaultPage defaultPage = new DefaultPage(result.getData().getRecordList(), pageable, result.getData().getTotalNum());
            return Result.wrapSuccessfulResult(defaultPage);
        } else {
            log.error("[共享中心] 判断委托单类型失败 paymentType={}", paymentType);
            return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_TYPE_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_TYPE_ERROR.getErrorMessage());
        }

    }

    /**
     * 对账单导出
     *
     * @param request
     * @param proxyStartTime    //委托开始时间
     * @param proxyEndTime      //委托结束时间
     * @param completeStartTime //交车开始时间
     * @param completeEndTime   //交车结束时间
     * @param carLicense        //车牌
     * @param proxyShopId       //委托方或者受托方门店id
     * @param shopFlag          //判断是受托方还是委托方
     * @param paymentType       //委托单类型
     * @param response
     * @return
     */
    @RequestMapping(value = "/export/proxy", method = RequestMethod.GET)
    @ResponseBody
    public Result exportProxy(HttpServletRequest request, String proxyStartTime, String proxyEndTime, String completeStartTime, String completeEndTime, String carLicense, Long proxyShopId, Integer shopFlag, Integer paymentType, HttpServletResponse response) {
        log.info("[共享中心] 对账单导出,参数:proxyStartTime={},proxyEndTime={},completeStartTime={},completeEndTime={},carLicense={},proxyShopId={},shopFlag={},paymentType={}", proxyStartTime, proxyEndTime, completeStartTime, completeEndTime, carLicense, proxyShopId, shopFlag, paymentType);
        Long consumeStart = new Date().getTime();
        ProxySettlementParamVO proxySettlementParamVO = ProxySettlementUtils.getProxySettlementParam(request, proxyStartTime, proxyEndTime, completeStartTime, completeEndTime, carLicense, proxyShopId, shopFlag);
        if (proxySettlementParamVO == null) {
            log.error("[共享中心] 判断委托方还是受托方失败");
            return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_SUMMARY_TYPE_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_SUMMARY_TYPE_ERROR.getErrorMessage());
        }
        ProxyParam proxyParam = ProxySettlementUtils.getProxyParam(proxySettlementParamVO, carLicense);
        Result<List<ProxyDTO>> proxyDTOList = null;
        String name = "";
        if (paymentType == 0) {
            //交车待结算
            name = "交车待结算对账单" + DateUtil.getDateStr(new Date(), 0);
            proxyParam.setProxyStatus(ProxyStatusEnum.YJC.getCode());
            try {
                proxyDTOList = rpcProxyService.exportProxyList(proxyParam);
            } catch (Exception e) {
                log.error("[共享中心] 导出本月交车待结算对账单失败，DUBBO接口报错");
                return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getErrorMessage());
            }
        } else if (paymentType == 1) {
            //交车已经结清
            name = "交车已结算对账单" + DateUtil.getDateStr(new Date(), 0);
            proxyParam.setProxyStatus(ProxyStatusEnum.YJQ.getCode());
            try {
                proxyDTOList = rpcProxyService.exportProxyList(proxyParam);
            } catch (Exception e) {
                log.error("[共享中心] 导出本月已经结清的对账单失败，DUBBO接口报错");
                return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getErrorMessage());
            }
        } else if (paymentType == 2) {
            //开单未交车
            name = "开单未交车对账单" + DateUtil.getDateStr(new Date(), 0);
            List<String> status = new ArrayList<>();
            status.add(ProxyStatusEnum.YWT.getCode());
            status.add(ProxyStatusEnum.YJD.getCode());
            status.add(ProxyStatusEnum.YYC.getCode());
            status.add(ProxyStatusEnum.YPG.getCode());
            status.add(ProxyStatusEnum.YWG.getCode());
            proxyParam.setProxyStatusList(status);
            try {
                proxyDTOList = rpcProxyService.exportProxyList(proxyParam);
            } catch (Exception e) {
                log.error("[共享中心] 导出本月开单还未交车的对账单失败，DBBO接口报错");
                return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getErrorMessage());
            }
        } else if (paymentType == 3) {
            //全部
            name = "全部对账单" + DateUtil.getDateStr(new Date(), 0);
            try {
                proxyDTOList = rpcProxyService.exportProxyList(proxyParam);
            } catch (Exception e) {
                log.error("[共享中心] 导出历史交车还未结清的对账单失败，DUBBO接口报错");
                return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getErrorMessage());
            }
        } else if (paymentType == 4) {
            //全部
            name = "交车已挂账对账单" + DateUtil.getDateStr(new Date(), 0);
            proxyParam.setProxyStatus(ProxyStatusEnum.YGZ.getCode());
            try {
                proxyDTOList = rpcProxyService.exportProxyList(proxyParam);
            } catch (Exception e) {
                log.error("[共享中心] 导出历史交车还未结清的对账单失败，DUBBO接口报错");
                return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_LIST_ERROR.getErrorMessage());
            }
        } else {
            log.error("[共享中心] 判断委托单输出类型失败 paymentType={}", paymentType);
            return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_TYPE_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_TYPE_ERROR.getErrorMessage());
        }
        List<ProxyDTO> proxyDTOLists = proxyDTOList.getData();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        HSSFRow row = sheet.createRow(0);
        String[] headers = null;
        if (shopFlag == 1) {
            ////委托方表头
            headers = new String[]{"完工时间", "委托时间", "委托单号", "总金额", "服务类型", "服务项目", "工时", "委托金额", "服务面数", "服务顾问",
                    "车牌", "车辆信息", "委托方手机号", "委托方联系人",  "委托方门店", "委托方门店地址","受托方名称", "受托方联系人", "受托方电话", "受托方门店地址"};
        } else {
            //受托方表头
            headers = new String[]{"完工时间", "委托时间", "委托单号", "总金额", "服务类型", "服务项目", "工时", "委托金额", "服务面数", "服务顾问",
                    "车牌", "车辆信息", "委托方手机号", "委托方联系人",  "委托方门店", "委托方门店地址", "受托方门店","受托方联系人", "受托方电话", "受托方门店地址"};
        }
        int i = 0;
        for (i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        i = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String completeDate = "";
        for (ProxyDTO proxyDTO : proxyDTOLists) {
            row = sheet.createRow(i);
            Date completeTime = proxyDTO.getCompleteTime();
            if (completeTime != null) {
                completeDate = sdf.format(completeTime);
            }
            row.createCell(0).setCellValue(completeDate);
            row.createCell(1).setCellValue(proxyDTO.getProxyTimeStr());
            row.createCell(2).setCellValue(proxyDTO.getProxySn());
            row.createCell(3).setCellValue(proxyDTO.getProxyAmount().toString());
            row.createCell(4).setCellValue(proxyDTO.getServiceType());
            row.createCell(5).setCellValue(proxyDTO.getServiceName());
            row.createCell(6).setCellValue(String.valueOf(proxyDTO.getServiceHour()));
            row.createCell(7).setCellValue(String.valueOf(proxyDTO.getServiceProxyAmount()));
            row.createCell(8).setCellValue(String.valueOf(proxyDTO.getSurfaceNum()));
            row.createCell(9).setCellValue(proxyDTO.getServiceSa());
            row.createCell(10).setCellValue(proxyDTO.getCarLicense());
            row.createCell(11).setCellValue(proxyDTO.getCarInfo());
            row.createCell(12).setCellValue(proxyDTO.getContactMobile());
            row.createCell(13).setCellValue(proxyDTO.getContactName());
            row.createCell(14).setCellValue(proxyDTO.getShopName());
            row.createCell(15).setCellValue(proxyDTO.getProxyAddress());
            row.createCell(16).setCellValue(proxyDTO.getProxyShopName());
            row.createCell(17).setCellValue(proxyDTO.getShareName());
            row.createCell(18).setCellValue(proxyDTO.getShareMobile());
            row.createCell(19).setCellValue(proxyDTO.getShareAddr());
            i++;
        }
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String((name).getBytes(), "ISO8859_1");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
            workbook.write(outputStream);
            Long consumeEnd = new Date().getTime();
            Long consumeTime = (consumeEnd - consumeStart) / 1000;
            log.info("[共享中心]：导出委托报表消耗时间=" + consumeTime + "秒");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.wrapSuccessfulResult("委托对账单导出成功");
    }


    /**
     * 委托方门店列表
     *
     * @param request
     * @param shopNameKey
     * @return
     */
    @RequestMapping(value = "/shop/list", method = RequestMethod.GET)
    @ResponseBody
    public Result getShopList(HttpServletRequest request, String shopNameKey) {
        log.info("[共享中心] 委托方门店列表 start,参数:shopNameKey={}", shopNameKey);
        Map<String, Object> map = new HashMap<>();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        Long provinceId = shop.getProvince();
        Long cityId = shop.getCity();
        map.put("province", provinceId);
        map.put("city", cityId);
        map.put("nameLike", shopNameKey);
        List<Shop> shops = shopService.select(map);
        List shopList = new ArrayList();
        for (Shop thisShop : shops) {
            Map<String, Object> shopMap = new HashMap<>();
            shopMap.put("id", thisShop.getId());
            shopMap.put("name", thisShop.getName());
            shopList.add(shopMap);
        }
        log.info("[共享中心] 委托方门店列表 end 返回成功");
        return Result.wrapSuccessfulResult(shopList);
    }


    /**
     * 受托方门店列表
     *
     * @return
     */
    @RequestMapping(value = "/share/list", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<BPShopTagRelVo>> getShareShopList(String shopNameKey) {
        log.info("【共享中心】获取受托方列表 getShareShopList.shopNameKey={}", shopNameKey);
        List<BPShopTagRelVo> result = null;
        try {
            result = shopTagRelService.searchBPShopTagRelByName(shopNameKey);
        } catch (Exception e) {
            log.error("【共享中心】查询受托方门店列表失败！",e);
            return Result.wrapErrorResult("","列表查询失败！");
        }
        return Result.wrapSuccessfulResult(result);
    }

    /**
     * 对账单汇总
     *
     * @param request
     * @param shopFlag
     * @param proxyShopId
     * @param proxyShopName
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public Result settlementSummary(HttpServletRequest request, Integer shopFlag, Long proxyShopId, String proxyShopName, String startDate, String endDate) {
        log.info("[共享中心] 委托对账单汇总展示, 参数:shopFlag={},proxyShopId={},proxyShopName={},startDate={},endDate={},page={},size={}", shopFlag, proxyShopId, proxyShopName, startDate, endDate);
        if (StringUtil.isNull(startDate) && StringUtil.isNull(endDate)) {
            return Result.wrapErrorResult("", "请选择时间区间");
        }
        ProxySettlementParamVO proxySettlementParamVO = ProxySettlementUtils.getProxyShopType(request, shopFlag, proxyShopId);
        if (proxySettlementParamVO == null) {
            log.error("[共享中心] 判断委托方还是受托方失败");
            return Result.wrapErrorResult(LegendErrorCode.SETTLEMENT_PROXY_SUMMARY_TYPE_ERROR.getCode(), LegendErrorCode.SETTLEMENT_PROXY_SUMMARY_TYPE_ERROR.getErrorMessage());
        }
        ProxySummaryVO proxySummaryVO = new ProxySummaryVO();
        ProxyParam proxyParam = new ProxyParam();
        if (StringUtil.isNull(startDate) == false) {
            proxyParam.setProxyStartTime(startDate + " 00:00:00");
        }
        if (StringUtil.isNull(endDate) == false) {
            proxyParam.setProxyEndTime(endDate + " 23:59:59");
        }
        proxyParam.setShopId(proxySettlementParamVO.getShopId());
        proxyParam.setProxyShopId(proxySettlementParamVO.getProxyShopId());
        //总金额
        List<String> statusList = new ArrayList<>();
        statusList.add(ProxyStatusEnum.YJD.getCode());
        statusList.add(ProxyStatusEnum.YYC.getCode());
        statusList.add(ProxyStatusEnum.YPG.getCode());
        statusList.add(ProxyStatusEnum.YWG.getCode());
        //接单未交车
        proxyParam.setProxyStatusList(statusList);
        Integer orderTakeNoCompleteCount = 0;
        try {
            orderTakeNoCompleteCount = rpcProxyService.getProxyCount(proxyParam).getData();
        } catch (Exception e) {
            log.error("接单未交车失败，DUBBO接口报错");
        }
        proxySummaryVO.setOrderTakeNoCompleteCount(orderTakeNoCompleteCount);
        statusList.add(ProxyStatusEnum.YJC.getCode());
        statusList.add(ProxyStatusEnum.YJQ.getCode());
        proxyParam.setProxyStatusList(statusList);
        BigDecimal totalAmount = new BigDecimal(0);
        try {
            totalAmount = rpcProxyService.getRealPayAmount(proxyParam);
        } catch (Exception e) {
            log.error("获取总金额失败，DUBBO接口报错");
        }
        proxySummaryVO.setTotalAmount(totalAmount);
        //接车总数量
        proxyParam.setProxyStatusList(statusList);
        Integer orderTakeCount = 0;
        try {
            orderTakeCount = rpcProxyService.getProxyCount(proxyParam).getData();
        } catch (Exception e) {
            log.error("获取接单总数量失败，DUBBO接口报错");
        }
        proxySummaryVO.setOrderTakeCount(orderTakeCount);
        ProxyParam summaryParam = new ProxyParam();
        if (StringUtil.isNull(startDate) == false) {
            summaryParam.setProxyStartTime(startDate + " 00:00:00");
        }
        if (StringUtil.isNull(endDate) == false) {
            summaryParam.setProxyEndTime(endDate + " 23:59:59");
        }
        summaryParam.setShopId(proxySettlementParamVO.getShopId());
        summaryParam.setProxyShopId(proxySettlementParamVO.getProxyShopId());
        //未接车数量
        summaryParam.setProxyStatus(ProxyStatusEnum.YWT.getCode());
        Integer orderRefusedCount = 0;
        try {
            orderRefusedCount = rpcProxyService.getProxyCount(summaryParam).getData();
        } catch (Exception e) {
            log.error("未接车数量失败，DUBBO接口报错");
        }
        proxySummaryVO.setOrderRefusedCount(orderRefusedCount);
        //交车已经结算的委托单
        summaryParam.setProxyStatus(ProxyStatusEnum.YJQ.getCode());
        Integer balanceCount = 0;
        try {
            balanceCount = rpcProxyService.getProxyCount(summaryParam).getData();
        } catch (Exception e) {
            log.error("交车已结算数量失败，DUBBO接口报错");
        }
        proxySummaryVO.setBalanceProxyCount(balanceCount);
        //交车未结算的委托单数量
        summaryParam.setProxyStatus(ProxyStatusEnum.YJC.getCode());
        Integer completeNoBalanceCount = 0;
        try {
            completeNoBalanceCount = rpcProxyService.getProxyCount(summaryParam).getData();
        } catch (Exception e) {
            log.error("交车未结算数量失败，DUBBO接口报错");
        }
        proxySummaryVO.setCompleteNoBalanceCount(completeNoBalanceCount);
        //应付金额
        BigDecimal realAmount = new BigDecimal(0);
        try {
            realAmount = rpcProxyService.getRealPayAmount(summaryParam);
        } catch (Exception e) {
            log.error("获取应付金额失败，DUBBO接口报错");
        }
        proxySummaryVO.setRealAmount(realAmount);
        //交车总数量
        List<String> status = new ArrayList<>();
        status.add(ProxyStatusEnum.YJC.getCode());
        status.add(ProxyStatusEnum.YJQ.getCode());
        summaryParam.setProxyStatus(null);
        summaryParam.setProxyStatusList(status);
        Integer completeCount = 0;
        try {
            completeCount = rpcProxyService.getProxyCount(summaryParam).getData();
        } catch (Exception e) {
            log.error("获取交车总数量失败，DUBBO接口报错");
        }
        proxySummaryVO.setCompleteCount(completeCount);
        return Result.wrapSuccessfulResult(proxySummaryVO);
    }

}
