package com.tqmall.legend.web.insurance.offlineinsurnace;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.insurance.domain.param.insurance.offline.InsuranceOffLineTempInfoParam;
import com.tqmall.insurance.domain.param.insurance.offline.SearchTempInfoListParam;
import com.tqmall.insurance.domain.result.cashcoupon.CashCouponDetailDTO;
import com.tqmall.insurance.domain.result.offline.InsuranceOfflineTempInfoDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.insurance.InsuranceOfflineFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created by zwb on 17/3/23.
 * 线下录入保单Controller
 */
@Slf4j
@Controller
@RequestMapping("insurance/offline")
public class InsuranceOfflineController extends BaseController {


    @Autowired
    private InsuranceOfflineFacade insuranceOfflineFacade;


    @Autowired
    private ShopService shopService;


    /**
     * 跳转到线下录入保单列表页面
     *
     * @return
     */
    @RequestMapping(value = "/list")
    public String list(Model model) {
        return "yqx/page/ax_insurance/offline_insurance/entering-list";
    }


    /**
     * 分页获取线下录单保单列表内容
     *
     * @return
     */
    @RequestMapping("list/show")
    @ResponseBody
    public Result queryCashCouponPage(@PageableDefault(page = 1, value = 10  , sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        String shopId = UserUtils.getUserGlobalIdForSession(request);
        if (shopId == null || "".equals(shopId)) {
            log.error("获取保单门店id取得失败：shopId为空");
            return Result.wrapErrorResult("", "系统错误");
        }

        String vehicleSn = request.getParameter("vehicleSn");
        String gmtAuditStart = request.getParameter("gmtAuditStart");
        String gmtAuditEnd = request.getParameter("gmtAuditEnd");
        String auditStatus = request.getParameter("auditStatus");

        SearchTempInfoListParam  param = new SearchTempInfoListParam();
        param.setVehicleSn(vehicleSn);
        if(auditStatus != null  && !"".equals(auditStatus)){
            param.setAuditStatus(Integer.valueOf(auditStatus));
        }
        if(gmtAuditStart != null && !"".equals(gmtAuditStart)){
            Date dayStart = DateUtil.getDayStartAndEnd(DateUtil.long2Date(Long.valueOf(gmtAuditStart)),0);
            param.setGmtCreateStart(dayStart);
        }
        if(gmtAuditEnd != null && !"".equals(gmtAuditEnd)){
            Date dayEnd = DateUtil.getDayStartAndEnd(DateUtil.long2Date(Long.valueOf(gmtAuditEnd)),1);
            param.setGmtCreateEnd(dayEnd);
        }
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        param.setAgentId(Integer.valueOf(ucShopId));
        param.setSource("LEGEND");
        param.setPageNum(pageable.getPageNumber());
        param.setPageSize(pageable.getPageSize());
        PagingResult<InsuranceOfflineTempInfoDTO> pagingResult = insuranceOfflineFacade.offlineInsuranceList(param);
        DefaultPage<CashCouponDetailDTO> page = new DefaultPage(pagingResult.getList(),pageable,pagingResult.getTotal());
        return Result.wrapSuccessfulResult(page);

    }

    /**
     * 跳转到保单录入页面
     *
     * @return
     */
    @RequestMapping(value = "entering")
    public String entering(Model model) {
        Map<Integer, String> offlineInsuranceStatus = insuranceOfflineFacade.getOfflineInsuranceStatus();
        model.addAttribute("statusMap", offlineInsuranceStatus);
        return "yqx/page/ax_insurance/offline_insurance/new-create";
    }


    /**
     * 保存录入保单
     *
     * @return
     */
    @RequestMapping("entering/save")
    @ResponseBody
    public Result insuranceSave(InsuranceOffLineTempInfoParam param) {
        if (param.getVehicleSn() == null || "".equals(param.getVehicleSn())) {
            return Result.wrapErrorResult("0001", "车牌号不能为空");
        }
        if (param.getCommercialFormNo() != null && param.getCommercialFee() == null) {
            return Result.wrapErrorResult("0002", "商业险保费不能为空");
        }
        if (param.getForcibleFormNo() != null && param.getForcibleFee() == null) {
            return Result.wrapErrorResult("0003", "交强险保费不能为空");
        }
        if (param.getForcibleFormNo() != null && param.getVesselTaxFee() == null) {
            return Result.wrapErrorResult("0004", "车船税不能为空");
        }
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        param.setAgentId(Integer.valueOf(ucShopId.toString()));
        param.setAgentName(shop.getName());
        param.setInsuranceCompanyId(2);
        Result result = insuranceOfflineFacade.saveOfflineInsurance(param);
        return result;
    }


}
