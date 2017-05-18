package com.tqmall.legend.web.report;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcCardCouponRechargeService;
import com.tqmall.cube.shop.param.account.CardCouponRechargeParam;
import com.tqmall.cube.shop.result.CardCouponRechargeDTO;
import com.tqmall.cube.shop.result.CardCouponRechargeVO;
import com.tqmall.cube.shop.result.TotalReportDataDTO;
import com.tqmall.legend.annotation.Condition;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.account.ComboInfoService;
import com.tqmall.legend.biz.account.CouponInfoService;
import com.tqmall.legend.biz.account.CouponSuiteService;
import com.tqmall.legend.biz.account.MemberCardInfoService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.account.ComboInfo;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.entity.account.CouponSuite;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.report.export.vo.ComboRechargeCommission;
import com.tqmall.legend.web.report.export.vo.CouponRechargeCommission;
import com.tqmall.legend.web.report.export.vo.MemberCardRechargeCommission;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/7/25.
 */
@Controller
@Slf4j
@RequestMapping("/shop/stats/card/coupon-recharge")
public class CardCouponRechargeReportController extends BaseController {
    @Autowired
    private MemberCardInfoService memberCardInfoService;
    @Autowired
    private ComboInfoService comboInfoService;
    @Autowired
    private CouponSuiteService couponSuiteService;
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private ShopService shopService;

    @Autowired
    private RpcCardCouponRechargeService rpcCardCouponRechargeService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping
    @HttpRequestLog
    public String index(Model model,
                        @RequestParam(value = "tab", required = false) Integer tab,
                        @RequestParam(value = "startTime", required = false) String startTime,
                        @RequestParam(value = "endTime", required = false) String endTime,
                        @RequestParam(value = "consumeTypeId", required = false) Integer consumeTypeId,
                        @RequestParam(value = "consumeTypeName", required = false) String consumeTypeName) {
        model.addAttribute("moduleUrlTab", "card-coupon-recharge");

        // 营业报表跳转过来
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = df.parse(startTime);
                Date endDate = df.parse(endTime);
                if (startDate.after(new Date()) || startDate.after(endDate)) {
                    return "yqx/page/report/statistics/card/coupon-recharge";
                }
            } catch (ParseException e) {
                log.error("营业报表跳转卡券充值报表,日期格式错误,参数:startTime={}, endTime={}, 异常信息:", startTime, endTime, e);
                return "yqx/page/report/statistics/card/coupon-recharge";
            }
            if (tab == 0) {
                model.addAttribute("cardStartTime", startTime);
                model.addAttribute("cardEndTime", endTime);
                model.addAttribute("consumeTypeId", consumeTypeId);
                model.addAttribute("consumeTypeName", consumeTypeName);
            } else if (tab == 1) {
                model.addAttribute("comboStartTime", startTime);
                model.addAttribute("comboEndTime", endTime);
            } else if (tab == 2) {
                model.addAttribute("couponStartTime", startTime);
                model.addAttribute("couponEndTime", endTime);
            }
            model.addAttribute("tab", tab);
        }
        return "yqx/page/report/statistics/card/coupon-recharge";
    }

    //优惠券
    @RequestMapping("getCouponInfo")
    @ResponseBody
    public Result getCouponInfo(){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        List<CouponInfo> couponInfoList = couponInfoService.select(param);
        return Result.wrapSuccessfulResult(couponInfoList);
    }

    //优惠券来源
    @RequestMapping("getSuiteInfo")
    @ResponseBody
    public Result getSuiteInfo(){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        List<CouponSuite> couponSuiteList = Lists.newArrayList();
        CouponSuite couponSuite = new CouponSuite();
        couponSuite.setId(-1L);
        couponSuite.setSuiteName("赠送");
        couponSuiteList.add(couponSuite);
        couponSuite = new CouponSuite();
        couponSuite.setId(-2L);
        couponSuite.setSuiteName("微信公众号");
        couponSuiteList.add(couponSuite);
        List<CouponSuite> couponInfoList = couponSuiteService.select(param);
        couponSuiteList.addAll(couponInfoList);
        return Result.wrapSuccessfulResult(couponSuiteList);
    }


    //计次卡类型
    @RequestMapping("getComboInfo")
    @ResponseBody
    public Result getComboInfo(){
        Long shopId = UserUtils.getShopIdForSession(request);
        List<ComboInfo> comboInfoList = comboInfoService.listComboInfo(shopId);
        return Result.wrapSuccessfulResult(comboInfoList);
    }

    //会员卡类型
    @RequestMapping("getMemberCardInfo")
    @ResponseBody
    public Result getMemberCardInfo(){
        Long shopId = UserUtils.getShopIdForSession(request);
        List<MemberCardInfo> memberCardInfos = memberCardInfoService.findAllByShopId(shopId, null);
        return Result.wrapSuccessfulResult(memberCardInfos);
    }





    @RequestMapping("list")
    @ResponseBody
    @HttpRequestLog(conditions = {@Condition(name = "search_tradeType",aliasName = "tradeType"),@Condition(name = "search_cardCouponTypeId",aliasName = "cardCouponTypeId"),
            @Condition(name = "search_cardNumber",aliasName = "cardNumber"),@Condition(name = "search_license",aliasName = "license"),
            @Condition(name = "search_mobile",aliasName = "mobile"),@Condition(name = "search_suiteId",aliasName = "suiteId")})
    public Object list(@PageableDefault(page = 1, value = 10, sort = "gmt_create",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Long shopId = UserUtils.getShopIdForSession(request);

        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        CardCouponRechargeParam cardCouponRechargeParam = setQueryParam(searchParams);
        cardCouponRechargeParam.setShopId(shopId);
        cardCouponRechargeParam.setPageNum(pageable.getPageNumber());
        cardCouponRechargeParam.setPageSize(pageable.getPageSize());
        Integer tradeType = cardCouponRechargeParam.getTradeType();
        if (tradeType == null) {
            return Result.wrapErrorResult("", "请选择需要查询的tab");
        }
        if (shopId == null) {
            return Result.wrapErrorResult("", "门店信息不能为空");
        }
        Result<CardCouponRechargeDTO> result = null;
        try{
            result =  rpcCardCouponRechargeService.getCardCouponRechargeInfo(cardCouponRechargeParam);
        }catch (Exception e){
            log.error("调用cube获取卡券充值记录接口出错,{}", e);
            return Result.wrapErrorResult("","获取数据失败");
        }

        CardCouponRechargeDTO cardCouponRechargeDTO = result.getData();

        List<CardCouponRechargeVO> cardCouponSearchVOs = cardCouponRechargeDTO.getDataList();

        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        DefaultPage<CardCouponRechargeVO> page = new DefaultPage<CardCouponRechargeVO>(cardCouponSearchVOs, pageRequest, cardCouponRechargeDTO.getTotalNum());


        return  Result.wrapSuccessfulResult(page);

    }

    @RequestMapping("totalMember")
    @ResponseBody
    public Object totalMember() {
        Long shopId = UserUtils.getShopIdForSession(request);

        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        CardCouponRechargeParam cardCouponRechargeParam = setQueryParam(searchParams);
        cardCouponRechargeParam.setShopId(shopId);
        Integer tradeType = cardCouponRechargeParam.getTradeType();
        if (tradeType == null) {
            return Result.wrapErrorResult("", "请选择需要查询的tab");
        }
        if (shopId == null) {
            return Result.wrapErrorResult("", "门店信息不能为空");
        }
        Result<TotalReportDataDTO> result = null;
        try{
            result = rpcCardCouponRechargeService.getTotalCardMemberRechargeInfo(cardCouponRechargeParam);
        } catch (Exception e){
            log.error("调用cube获取卡券充值记录接口出错,{}", e);
            return Result.wrapErrorResult("","获取数据失败");
        }
        return  Result.wrapSuccessfulResult(result.getData());
    }



    private CardCouponRechargeParam setQueryParam(Map<String,Object> searchMap){
        CardCouponRechargeParam cardCouponRechargeParam = new CardCouponRechargeParam();

        for (String key : searchMap.keySet()) {

            if(key.equals("sTime")){
                String startTime = searchMap.get("sTime")+"";
                startTime = startTime+" 00:00:00";
                cardCouponRechargeParam.setSTime(startTime);
            }

            if(key.equals("eTime")){
                String endTime = searchMap.get("eTime")+"";
                endTime = endTime+" 23:59:59";
                cardCouponRechargeParam.setETime(endTime);
            }

            if(key.equals("tradeType")){
                cardCouponRechargeParam.setTradeType(Long.valueOf(searchMap.get("tradeType")+"").intValue());
            }

            if(key.equals("cardCouponTypeId")){
                cardCouponRechargeParam.setCardCouponTypeId((Long.valueOf(searchMap.get("cardCouponTypeId")+"")));
            }

            if(key.equals("consumeTypeId")){
                cardCouponRechargeParam.setConsumeTypeId(Integer.parseInt(searchMap.get("consumeTypeId")+""));
            }

            if(key.equals("cardNumber")){
                cardCouponRechargeParam.setCardNumber(searchMap.get("cardNumber")+"");
            }

            if(key.equals("license")){
                cardCouponRechargeParam.setLicense(searchMap.get("license")+"");
            }

            if(key.equals("mobile")){
                cardCouponRechargeParam.setMobile(searchMap.get("mobile")+"");
            }
            if(key.equals("suiteId")){
                cardCouponRechargeParam.setSuiteId(Integer.valueOf(searchMap.get("suiteId")+""));
            }

        }
        return cardCouponRechargeParam;
    }

    @RequestMapping("get_excel")
    @ResponseBody
    public void getExcelList(@PageableDefault(page = 1, value = 500, sort = "gmt_create",
            direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request,
                               HttpServletResponse response, Model model
    ) throws Exception {
        Long sTime = System.currentTimeMillis();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        CardCouponRechargeParam cardCouponRechargeParam = setQueryParam(searchParams);
        cardCouponRechargeParam.setShopId(shopId);
        cardCouponRechargeParam.setPageNum(pageable.getPageNumber());
        cardCouponRechargeParam.setPageSize(pageable.getPageSize());
        Result<CardCouponRechargeDTO> result = null;
        try{
            result =  rpcCardCouponRechargeService.getCardCouponRechargeInfo(cardCouponRechargeParam);
        }catch (Exception e){
            log.error("调用cube获取卡券充值记录接口出错,{}", e);
        }

        CardCouponRechargeDTO cardCouponRechargeDTO = result.getData();
        ExcelExportor exportor = null;
        Integer tradeType = cardCouponRechargeParam.getTradeType();
        try {
            int recordSize = 0;
            if(tradeType == 3){
                exportor = ExcelHelper.createDownloadExportor(response, "会员卡充值明细表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
                Shop shop = shopService.selectById(userInfo.getShopId());
                String headline = shop.getName() + "——会员卡充值明细表";
                exportor.writeTitle(null, headline, MemberCardRechargeCommission.class);
            }else if(tradeType ==2){
                exportor = ExcelHelper.createDownloadExportor(response, "计次卡充值明细表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
                Shop shop = shopService.selectById(userInfo.getShopId());
                String headline = shop.getName() + "——计次卡充值明细表";
                exportor.writeTitle(null, headline, ComboRechargeCommission.class);
            }else if(tradeType ==1){
                exportor = ExcelHelper.createDownloadExportor(response, "优惠券充值明细表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
                Shop shop = shopService.selectById(userInfo.getShopId());
                String headline = shop.getName() + "——优惠券充值明细表";
                exportor.writeTitle(null, headline, CouponRechargeCommission.class);
            }
            while(cardCouponRechargeDTO != null && Langs.isNotEmpty(cardCouponRechargeDTO.getDataList())) {
                List<CardCouponRechargeVO> resultlist = cardCouponRechargeDTO.getDataList();
                recordSize += resultlist.size();
                List<Object> commissionList = converter(resultlist,tradeType);
                exportor.write(commissionList);
                pageable = new PageableRequest(pageable.getPageNumber()+1,pageable.getPageSize(),Sort.Direction.DESC,new String[]{"gmt_create"});
                cardCouponRechargeParam.setPageNum(pageable.getPageNumber());
                result =  rpcCardCouponRechargeService.getCardCouponRechargeInfo(cardCouponRechargeParam);
                cardCouponRechargeDTO = result.getData();
            }
            if(tradeType == 3){
                com.tqmall.core.common.entity.Result<TotalReportDataDTO> reportDataDTOResult = rpcCardCouponRechargeService.getTotalCardMemberRechargeInfo(cardCouponRechargeParam);
                if (reportDataDTOResult.isSuccess()) {
                    TotalReportDataDTO zj = reportDataDTOResult.getData();

                    Row row = exportor.createRow();
                    Cell cell = row.createCell(0);
                    cell.setCellValue("总计");
                    if (null != zj){
                        Cell cell1 = row.createCell(7);
                        cell1.setCellValue(zj.getRechargeAmount().doubleValue());
                    }else {
                        Cell cell1 = row.createCell(7);
                        cell1.setCellValue("0");
                    }
                }
            }
            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("充值导出", userInfo, recordSize, exportTime);
            log.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    public List<Object> converter(List<CardCouponRechargeVO> cardCouponConsumeVOs, Integer tradeType){
        List<Object> resultList = Lists.newArrayList();
        if(null != cardCouponConsumeVOs && !CollectionUtils.isEmpty(cardCouponConsumeVOs)){
            for(CardCouponRechargeVO detailVO : cardCouponConsumeVOs){
                if(null != detailVO){
                    if(tradeType == 3){
                        MemberCardRechargeCommission commission = new MemberCardRechargeCommission();
                        BeanUtils.copyProperties(detailVO , commission);
                        resultList.add(commission);
                    }else if(tradeType ==2){
                        ComboRechargeCommission commission = new ComboRechargeCommission();
                        BeanUtils.copyProperties(detailVO , commission);
                        resultList.add(commission);
                    }else if(tradeType ==1){
                        CouponRechargeCommission commission = new CouponRechargeCommission();
                        BeanUtils.copyProperties(detailVO , commission);
                        resultList.add(commission);
                    }
                }
            }
        }
        return resultList;
    }


}
