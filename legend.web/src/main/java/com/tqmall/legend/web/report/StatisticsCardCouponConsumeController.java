package com.tqmall.legend.web.report;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcCardCouponConsumeService;
import com.tqmall.cube.shop.param.account.CardCouponConsumeParam;
import com.tqmall.cube.shop.result.CardCouponConsumeDTO;
import com.tqmall.cube.shop.result.CardCouponConsumeVO;
import com.tqmall.cube.shop.result.TotalReportDataDTO;
import com.tqmall.legend.biz.account.ComboInfoService;
import com.tqmall.legend.biz.account.CouponInfoService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.dao.account.MemberCardInfoDao;
import com.tqmall.legend.entity.account.ComboInfo;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.report.export.vo.ComboConsumeCommission;
import com.tqmall.legend.web.report.export.vo.CouponConsumeCommission;
import com.tqmall.legend.web.report.export.vo.MemberCardConsumeCommission;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghao on 16/5/19.
 */
@Controller
@RequestMapping(value = "/shop/stats/card/coupon-consume")
public class StatisticsCardCouponConsumeController extends BaseController {
    Logger logger = LoggerFactory.getLogger(StatisticsCardCouponConsumeController.class);

    @Autowired
    private RpcCardCouponConsumeService rpcCardCouponConsumeService;
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private ComboInfoService comboInfoService;
    @Autowired
    private MemberCardInfoDao memberCardInfoDao;
    @Autowired
    private ShopService shopService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping("")
    public String index_ng(Model model, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (null == userInfo) {
            logger.error("用户信息错误");
            return "yqx/page/report/statistics/card/coupon-consume";
        }
        long shopId = userInfo.getShopId();

        Shop shop = shopService.selectById(shopId);
        if (null == shop) {
            logger.error("店铺信息错误", shop);
            return "yqx/page/report/statistics/card/coupon-consume";
        }
        model.addAttribute("shop", shop);
        model.addAttribute("moduleUrlTab", "card-consume");
        return "yqx/page/report/statistics/card/coupon-consume";
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
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        List<MemberCardInfo> memberCardInfos = memberCardInfoDao.select(param);
        return Result.wrapSuccessfulResult(memberCardInfos);
    }

    @RequestMapping("list")
    @ResponseBody
    public Result getList(@PageableDefault(page = 1, value = 10, sort = "oi.create_time",
            direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request,
                                   HttpServletResponse response, Model model) {

        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        CardCouponConsumeParam cardCouponConsumeParam = setQueryParam(searchParams,userInfo,pageable);
        Result<CardCouponConsumeDTO> result = null;
        try{
            result = rpcCardCouponConsumeService.getCardCouponConsume(cardCouponConsumeParam);
        }catch (Exception e){
            logger.error("调用cube获取卡券消费信息接口出错,{}", e);
            return Result.wrapErrorResult("","获取数据失败");
        }
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        CardCouponConsumeDTO cardCouponConsumeDTO = result.getData();
        List<CardCouponConsumeVO> resultlist = cardCouponConsumeDTO.getDataList();
        Long totalSize = cardCouponConsumeDTO.getSize();


        DefaultPage<CardCouponConsumeVO> page = new DefaultPage<CardCouponConsumeVO>(resultlist, pageRequest, totalSize);


        return Result.wrapSuccessfulResult(page);
    }

    @RequestMapping("totalMember")
    @ResponseBody
    public Result totalMember() {

        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        CardCouponConsumeParam cardCouponConsumeParam = setQueryParam(searchParams,userInfo,null);
        Result<TotalReportDataDTO> result = null;
        try{
            result = rpcCardCouponConsumeService.getTotalMemberCardConsume(cardCouponConsumeParam);
        }catch (Exception e){
            logger.error("调用cube获取卡券消费信息接口出错,{}", e);
            return Result.wrapErrorResult("","获取数据失败");
        }
        return Result.wrapSuccessfulResult(result.getData());
    }

    @RequestMapping("get_excel")
    @ResponseBody
    public Object getExcelList(@PageableDefault(page = 1, value = 500, sort = "oi.create_time",
            direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request,
                                   HttpServletResponse response, Model model
                                   ) throws Exception {

        Long sTime = System.currentTimeMillis();
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        CardCouponConsumeParam cardCouponConsumeParam = setQueryParam(searchParams,userInfo,pageable);
        Result<CardCouponConsumeDTO> result = null;
        try{
            result = rpcCardCouponConsumeService.getCardCouponConsume(cardCouponConsumeParam);
        }catch (Exception e){
            logger.error("调用cube获取卡券消费详情接口出错,{}", e);
            return com.tqmall.legend.web.common.Result.wrapErrorResult("","获取数据失败");
        }


        Integer tradeType = cardCouponConsumeParam.getTradeType();

        CardCouponConsumeDTO cardCouponConsumeDTO = result.getData();
        ExcelExportor exportor = null;

        try {
            int recordSize = 0;
            if(tradeType == 3){
                exportor = ExcelHelper.createDownloadExportor(response, "会员卡消费明细表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
                Shop shop = shopService.selectById(userInfo.getShopId());
                String headline = shop.getName() + "——会员卡消费明细表";
                exportor.writeTitle(null, headline, MemberCardConsumeCommission.class);
            }else if(tradeType ==2){
                exportor = ExcelHelper.createDownloadExportor(response, "计次卡消费明细表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
                Shop shop = shopService.selectById(userInfo.getShopId());
                String headline = shop.getName() + "——计次卡消费明细表";
                exportor.writeTitle(null, headline, ComboConsumeCommission.class);
            }else if(tradeType ==1){
                exportor = ExcelHelper.createDownloadExportor(response, "优惠券消费明细表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
                Shop shop = shopService.selectById(userInfo.getShopId());
                String headline = shop.getName() + "——优惠券消费明细表";
                exportor.writeTitle(null, headline, CouponConsumeCommission.class);
            }
            while(cardCouponConsumeDTO != null && Langs.isNotEmpty(cardCouponConsumeDTO.getDataList())) {
                List<CardCouponConsumeVO> resultlist = cardCouponConsumeDTO.getDataList();
                recordSize += resultlist.size();
                List<Object> commissionList= converter(resultlist,tradeType);
                exportor.write(commissionList);
                pageable = new PageableRequest(pageable.getPageNumber()+1,pageable.getPageSize(),Sort.Direction.DESC,new String[]{"oi.create_time"});
                cardCouponConsumeParam = setQueryParam(searchParams,userInfo,pageable);
                result = rpcCardCouponConsumeService.getCardCouponConsume(cardCouponConsumeParam);
                cardCouponConsumeDTO = result.getData();
            }
            if(tradeType == 3){
                com.tqmall.core.common.entity.Result<TotalReportDataDTO> reportDataDTOResult = rpcCardCouponConsumeService.getTotalMemberCardConsume(cardCouponConsumeParam);
                if (reportDataDTOResult.isSuccess()) {
                    TotalReportDataDTO zj = reportDataDTOResult.getData();

                    Row row = exportor.createRow();
                    Cell cell = row.createCell(0);
                    cell.setCellValue("总计");
                    if (null != zj){
                        Cell cell1 = row.createCell(8);
                        cell1.setCellValue(zj.getCostAmount().doubleValue());
                        Cell cell2 = row.createCell(9);
                        cell2.setCellValue(zj.getDiscountAmount().doubleValue());
                    }else {
                        Cell cell1 = row.createCell(8);
                        cell1.setCellValue("0");
                        Cell cell2 = row.createCell(9);
                        cell2.setCellValue("0");
                    }
                }
            }
            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("消费报表导出", userInfo, recordSize, exportTime);
            logger.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        return null;
    }

    public List<Object> converter(List<CardCouponConsumeVO> cardCouponConsumeVOs, Integer tradeType){
        List<Object> resultList = Lists.newArrayList();
        if(null != cardCouponConsumeVOs && !CollectionUtils.isEmpty(cardCouponConsumeVOs)){
            for(CardCouponConsumeVO detailVO : cardCouponConsumeVOs){
                if(null != detailVO){
                    if(tradeType == 3){
                        MemberCardConsumeCommission commission = new MemberCardConsumeCommission();
                        BeanUtils.copyProperties(detailVO , commission);
                        resultList.add(commission);
                    }else if(tradeType ==2){
                        ComboConsumeCommission commission = new ComboConsumeCommission();
                        BeanUtils.copyProperties(detailVO , commission);
                        resultList.add(commission);
                    }else if(tradeType ==1){
                        CouponConsumeCommission commission = new CouponConsumeCommission();
                        BeanUtils.copyProperties(detailVO , commission);
                        resultList.add(commission);
                    }
                }
            }
        }
        return resultList;
    }

    private CardCouponConsumeParam setQueryParam(Map<String,Object> searchMap,UserInfo userInfo,Pageable pageable){
        CardCouponConsumeParam cardCouponConsumeParam = new CardCouponConsumeParam();
        cardCouponConsumeParam.setShopId(userInfo.getShopId());
        if (pageable != null) {
            cardCouponConsumeParam.setPageSize(pageable.getPageSize());
            cardCouponConsumeParam.setPageNum(pageable.getPageNumber());
        }

        for (String key : searchMap.keySet()) {
            if(key.equals("license")){
                cardCouponConsumeParam.setLicense((String) searchMap.get("license"));
            }

            if(key.equals("STime")){
                Date createTimeStart = DateUtil.convertStringToDateYMD(searchMap.get("STime")+"");
                createTimeStart = DateUtil.getStartTime(createTimeStart);
                cardCouponConsumeParam.setSTime(DateUtil.convertDateYMDHMS(createTimeStart));
            }

            if(key.equals("ETime")){
                Date createTimeEnd = DateUtil.convertStringToDateYMD(searchMap.get("ETime")+"");
                createTimeEnd = DateUtil.getEndTime(createTimeEnd);
                cardCouponConsumeParam.setETime(DateUtil.convertDateYMDHMS(createTimeEnd));
            }

            if(key.equals("cardCouponTypeId")){
                cardCouponConsumeParam.setCardCouponTypeId(Long.valueOf(searchMap.get("cardCouponTypeId")+""));
            }

            if(key.equals("cardNum")){
                cardCouponConsumeParam.setCardNum(searchMap.get("cardNum")+"");
            }

            if(key.equals("couponCode")){
                cardCouponConsumeParam.setCouponCode(searchMap.get("couponCode")+"");
            }

            if(key.equals("mobile")){
                cardCouponConsumeParam.setMobile(searchMap.get("mobile")+"");
            }

            if(key.equals("orderSn")){
                cardCouponConsumeParam.setOrderSn(searchMap.get("orderSn")+"");
            }
            if(key.equals("serviceId")){
                cardCouponConsumeParam.setServiceId(Long.parseLong(searchMap.get("serviceId")+""));
            }
            if(key.equals("tradeType")){
                cardCouponConsumeParam.setTradeType(Integer.parseInt(searchMap.get("tradeType")+""));
            }

        }
        return cardCouponConsumeParam;
    }

}
