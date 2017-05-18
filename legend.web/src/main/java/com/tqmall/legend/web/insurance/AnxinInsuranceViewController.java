package com.tqmall.legend.web.insurance;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.exception.BusinessCheckFailException;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendInsuranceErrorCode;
import com.tqmall.finance.model.param.AccountPayRecordParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceBasicParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceFormParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceVirtualBasicParam;
import com.tqmall.insurance.domain.result.*;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.insurance.AnxinInsuranceVirtualFlowFacade;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.facade.insurance.vo.*;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 安心保险查看
 * Created by lixiao on 16/9/14.
 */
@Slf4j
@Controller
@RequestMapping("insurance/anxin/view")
public class AnxinInsuranceViewController extends BaseController {
    @Autowired
    private InsuranceFormFacade insuranceFormFacade;
    @Autowired
    private AnxinInsuranceVirtualFlowFacade anxinInsuranceVirtualFlowFacade;

    /**
     * 跳转到保单或投保单列表
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "insure-list", method = RequestMethod.GET)
    public String insuranceList() {
        return "yqx/page/ax_insurance/query/list";
    }

    /*
      * 获取保单List
      *
      * @return
      */
    @RequestMapping(value = "getBaoDanList")
    @ResponseBody
    public Result getBaoDanList(HttpServletRequest request, @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        if (StringUtils.isBlank(ucShopId)) {
            log.error("获取保单门店id取得失败：shopId为空");
            return Result.wrapErrorResult("", "系统错误");
        }

        String peopleName = request.getParameter("peopleName");
        String carNumber = request.getParameter("carNumber");
        String insuranceStatus = request.getParameter("insuranceStatus");
        InsuranceBasicParam param = new InsuranceBasicParam();

        if (insuranceStatus != null && !insuranceStatus.equals("")) {
            param.setQuitStatus(Integer.valueOf(insuranceStatus));
        }
        if (peopleName != null && !peopleName.equals("")) {
            param.setInsuredName(peopleName);
        }
        if (carNumber != null && !carNumber.equals("")) {
            param.setVehicleSn(carNumber);
        }
        param.setAgentId(Integer.valueOf(ucShopId));
        param.setPageNum(pageable.getPageNumber());
        param.setPageSize(pageable.getPageSize());
        try {
            Result<PageEntityDTO<InsuranceFormVo>> page = insuranceFormFacade.getInsuranceList(param);
            if (page != null && page.isSuccess()) {
                List<InsuranceFormVo> InsuranceFormVos = page.getData().getRecordList();
                Integer totalNum = page.getData().getTotalNum();
                PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<InsuranceFormVo> resultPage = new DefaultPage<>(InsuranceFormVos, pageRequest, totalNum);
                return Result.wrapSuccessfulResult(resultPage);
            } else {
                return Result.wrapErrorResult(LegendInsuranceErrorCode.TBD_LIST_SEARCH_ERROR.getCode(), LegendInsuranceErrorCode.TBD_LIST_SEARCH_ERROR.getErrorMessage());

            }
        } catch (Exception e) {
            log.error("获取保单列表失败。request:{}", JSON.toJSONString(request), e);
            return Result.wrapErrorResult(LegendInsuranceErrorCode.TBD_LIST_SEARCH_ERROR.getCode(), LegendInsuranceErrorCode.TBD_LIST_SEARCH_ERROR.getErrorMessage());
        }
    }

    /*
      * 获取投保单List
      *
      * @return
      */
    @RequestMapping(value = "getTouBaoDanList")
    @ResponseBody
    public Result getTouBaoDanList(HttpServletRequest request, @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        if (StringUtils.isBlank(ucShopId) || ("0").equals(ucShopId)) {
            log.error("获取投保单List门店id取得失败：shopId为空");
            return Result.wrapErrorResult("", "系统错误");
        }
        String peopleName = request.getParameter("peopleName");
        String carNumber = request.getParameter("carNumber");
        String insuranceStatus = request.getParameter("insuranceStatus");

        InsuranceBasicParam param = new InsuranceBasicParam();
        if (peopleName != null && !peopleName.equals("")) {
            param.setInsuredName(peopleName);
        }
        if (carNumber != null && !carNumber.equals("")) {
            param.setVehicleSn(carNumber);
        }
        if (insuranceStatus != null && !insuranceStatus.equals("")) {
            param.setInsureStatus(Integer.valueOf(insuranceStatus));
        }


        param.setAgentId(Integer.valueOf(ucShopId));
        param.setPageNum(pageable.getPageNumber());
        param.setPageSize(pageable.getPageSize());

        try {
            Result<PageEntityDTO<InsuranceFormVo>> page = insuranceFormFacade.getApplyNoInsuranceList(param);
            if (page.isSuccess() && page != null) {
                List<InsuranceFormVo> InsuranceFormVos = page.getData().getRecordList();
                Integer totalNum = page.getData().getTotalNum();
                PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<InsuranceFormVo> resultPage = new DefaultPage<>(InsuranceFormVos, pageRequest, totalNum);
                return Result.wrapSuccessfulResult(resultPage);
            } else {
                return Result.wrapErrorResult(LegendInsuranceErrorCode.TBD_LIST_SEARCH_ERROR.getCode(), LegendInsuranceErrorCode.TBD_LIST_SEARCH_ERROR.getErrorMessage());

            }
        } catch (Exception e) {
            log.error("ajax获取保单列表数据失败。request:{}", JSON.toJSONString(request), e);
            return Result.wrapErrorResult(LegendInsuranceErrorCode.TBD_LIST_SEARCH_ERROR.getCode(), LegendInsuranceErrorCode.TBD_LIST_SEARCH_ERROR.getErrorMessage());
        }
    }

    /**
     * 跳转到保单或者投保单详情
     *
     * @param model
     * @param formId
     * @return
     */
    @RequestMapping(value = "insurance-detail", method = RequestMethod.GET)
    public String insuranceListDetail(Model model, @RequestParam(value = "formId") Integer formId,
                                      @RequestParam(value = "type", required = false) String type) {
        String agentId = UserUtils.getUserGlobalIdForSession(request);
        if (StringUtils.isBlank(agentId)) {
            throw new BusinessCheckFailException("", "系统错误");
        }

        Integer ucShopId = Integer.parseInt(agentId);
        com.tqmall.legend.common.Result<InsuranceFormVo> result = insuranceFormFacade.getInsuranceFormDetail(formId, ucShopId);
        if (!result.isSuccess() || result.getData() == null) {
            return "yqx/page/ax_insurance/query/list";
        }
        InsuranceFormVo insuranceForm = result.getData();
        InsuranceFeeDTO insuranceFeeDTO = insuranceFormFacade.selectPayInfoByOrderSn(ucShopId, insuranceForm.getOrderSn());
        if (insuranceFeeDTO != null) {
            model.addAttribute("uploadPhotos", insuranceFeeDTO.getUploadPhotos());
        }
        model.addAttribute("InsuranceFormDetail", insuranceForm);
        List<InsuranceItemVo> list = insuranceForm.getItemVoList();
        List<InsuranceItemVo> mainList = Lists.newArrayList();
        List<InsuranceItemVo> subList = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(list)) {
            for (InsuranceItemVo item : list) {
                if (item.getInsuranceSubcategoryType().compareTo(1) == 0) {
                    mainList.add(item);
                }
                if (item.getInsuranceSubcategoryType().compareTo(2) == 0) {
                    subList.add(item);
                }
            }
        }
        model.addAttribute("mainList", mainList);
        model.addAttribute("subList", subList);

        if (type.equals("tbd")) {
            return "yqx/page/ax_insurance/query/detailTBD";
        }
        if (type.equals("bd")) {
            return "yqx/page/ax_insurance/query/detailBD";
        }
        return "common/error";

    }

    /**
     * 跳转到奖励金页面
     *
     * @param
     * @return
     */
    @RequestMapping(value = "bounty", method = RequestMethod.GET)
    public String bounty(Model model) {
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        if (StringUtils.isBlank(ucShopId) || ucShopId.equals("0")) {
            return "common/error";
        }
        BonusAllocationRatioVo ratio = insuranceFormFacade.selectRatio(Integer.parseInt(ucShopId));
        model.addAttribute("ratio", ratio);
        return "yqx/page/ax_insurance/reward/list";
    }


    /**
     * 获取待生效余额，和已生效余额
     *
     * @return
     */
    @RequestMapping(value = "getReward")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Map<String, BigDecimal>> getReward() {
        return new ApiTemplate<Map<String, BigDecimal>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                String ucShopId = UserUtils.getUserGlobalIdForSession(request);
                Preconditions.checkArgument(StringUtils.isNotBlank(ucShopId), "门店userGlobalId为空");
            }

            @Override
            protected Map<String, BigDecimal> process() throws BizException {
                String ucShopId = UserUtils.getUserGlobalIdForSession(request);
                BigDecimal noEffectiveNum = BigDecimal.ZERO;
                BigDecimal surplusRewardNum = BigDecimal.ZERO;
                InsuranceAccountAmountVo resultAccountAmount = insuranceFormFacade.getSurplusReward("LEGEND", Integer.parseInt(ucShopId));
                CheckAcountDTO result = insuranceFormFacade.getWaitEffectReward(Integer.parseInt(ucShopId));
                if (result.getRemainRewardFeeAmount() != null) {
                    noEffectiveNum = noEffectiveNum.add(result.getRemainRewardFeeAmount());
                }
                if (resultAccountAmount.getAmount() != null) {
                    surplusRewardNum = surplusRewardNum.add(resultAccountAmount.getAmount());
                }
                Map<String, BigDecimal> map = new HashMap<>();
                map.put("noEffectiveNum", noEffectiveNum);
                map.put("surplusRewardNum", surplusRewardNum);
                return map;
            }
        }.execute();
    }


    /**
     * 获取待生效奖励金
     *
     * @return
     */
    @RequestMapping(value = "getWaitEffectReward")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<DefaultPage<InsuranceFormVo>> getWaitEffectReward(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) final Pageable pageable) {
        return new ApiTemplate<DefaultPage<InsuranceFormVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                String ucShopId = UserUtils.getUserGlobalIdForSession(request);
                Preconditions.checkArgument(StringUtils.isNotBlank(ucShopId), "门店userGlobalId为空");
            }

            @Override
            protected DefaultPage<InsuranceFormVo> process() throws BizException {
                String ucShopId = UserUtils.getUserGlobalIdForSession(request);
                InsuranceFormParam param = new InsuranceFormParam();
                param.setAgentId(Integer.valueOf(ucShopId));
                param.setPageNum(pageable.getPageNumber());
                param.setPageSize(pageable.getPageSize());
                PageEntityDTO<InsuranceFormVo> page = insuranceFormFacade.getNotEffectiveReward(param);
                List<InsuranceFormVo> InsuranceFormVos = page.getRecordList();
                Integer totalNum = page.getTotalNum();
                PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<InsuranceFormVo> resultPage = new DefaultPage<>(InsuranceFormVos, pageRequest, totalNum);
                return resultPage;
            }
        }.execute();

    }

    /**
     * 获取已生效和支出奖励金
     *
     * @return
     */
    @RequestMapping(value = "getFinanceReward")
    @ResponseBody
    public com.tqmall.core.common.entity.Result getFinanceReward(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) final Pageable pageable) {

        return new ApiTemplate<DefaultPage<InsuranceAccountPayRecordVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                String ucShopId = UserUtils.getUserGlobalIdForSession(request);
                Preconditions.checkArgument(StringUtils.isNotBlank(ucShopId), "门店userGlobalId为空");
            }

            @Override
            protected DefaultPage<InsuranceAccountPayRecordVo> process() throws BizException {
                String ucShopId = UserUtils.getUserGlobalIdForSession(request);
                Long userId = UserUtils.getUserIdForSession(request);
                String type = request.getParameter("payType");
                if (null == type || (!type.equals("1") && !type.equals("2"))) {
                    log.error("获取已生效和支出：未传payType");
                    throw new BizException("缺少必要payType参数");
                }
                AccountPayRecordParam param = new AccountPayRecordParam();
                param.setSource("LEGEND");
                param.setUid(userId.intValue());
                param.setPayType(Integer.valueOf(type));
                param.setShopId(Integer.valueOf(ucShopId));
                Integer start = (pageable.getPageNumber() - 1) * pageable.getPageSize();
                Integer limit = pageable.getPageSize();
                param.setStart(start);
                param.setLimit(limit);
//                PagingResult<InsuranceAccountPayRecordVo> accountPayResult = insuranceFormFacade.getAccountRewardPayRecord(param);
                PagingResult<InsuranceAccountPayRecordVo> accountPayResult = new PagingResult<>();
                Integer totalSize = accountPayResult.getTotal();
                PageRequest pageRequest =
                        new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                                pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<InsuranceAccountPayRecordVo> page = new DefaultPage<>(accountPayResult.getList(), pageRequest, totalSize);
                return page;
            }
        }.execute();
    }

    /**
     * 虚拟列表数据初始化
     *
     * @return
     */
    @RequestMapping(value = "virtual-list", method = RequestMethod.GET)
    @ResponseBody
    public Result virtualList(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) final Pageable pageable) {
        String userGlobalId = UserUtils.getUserGlobalIdForSession(request);
        String vehicleOwnerName = request.getParameter("vehicleOwnerName");
        InsuranceVirtualBasicParam param = new InsuranceVirtualBasicParam();
        if (judgment(vehicleOwnerName)) {
            param.setVehicleOwnerName(vehicleOwnerName);
        }
        String vehicleSn = request.getParameter("vehicleSn");
        if (judgment(vehicleSn)) {
            param.setVehicleSn(vehicleSn);
        }
        String insuranceVirtualStatus = request.getParameter("insuranceVirtualStatus");
        if (judgment(insuranceVirtualStatus)) {
            param.setInsuranceVirtualStatus(Integer.parseInt(insuranceVirtualStatus));
        }
        String gmtStartUserNotified = request.getParameter("gmtStartUserNotified");
        if (judgment(gmtStartUserNotified)) {
            param.setGmtStartUserNotified(DateUtil.convertStringToDate(gmtStartUserNotified + " 00:00:00"));
        }
        String gmtEndUserNotified = request.getParameter("gmtEndUserNotified");
        if (judgment(gmtEndUserNotified)) {
            param.setGmtEndUserNotified(DateUtil.convertStringToDate(gmtEndUserNotified + " 23:59:59"));
        }
        Integer pageNum = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 1;
        Integer limit = pageable.getPageSize();
        param.setPageSize(limit);
        param.setPageNum(pageNum);

        param.setAgentId(Integer.parseInt(userGlobalId));
        DefaultPage<InsuranceVirtualFormDTO> page = anxinInsuranceVirtualFlowFacade.selectVirtualList(param);
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 删除form
     *
     * @return
     */
    @RequestMapping("delForm")
    @ResponseBody
    public Result<Boolean> delForm(Integer formId) {
        return insuranceFormFacade.deleteByFormId(formId);
    }

    /**
     * 虚拟保单详情
     *
     * @return
     */
    @RequestMapping(value = "virtual-detail", method = RequestMethod.GET)
    public String virtualDetail(@RequestParam("id") Integer id, Model model) {
        String agentId = UserUtils.getUserGlobalIdForSession(request);
        if (agentId == null) {
            return "common/error";
        }
        InsuranceVirtualBasicDTO virtualBasic = anxinInsuranceVirtualFlowFacade.selectVirtualDetail(id, Integer.parseInt(agentId));
        if (virtualBasic == null) {
            return "common/error";
        }
        model.addAttribute("virtualBasic", virtualBasic);
        //设置商业险：主险和附加险
        List<InsuranceVirtualItemDTO> insuranceVirtualItemList = Lists.newArrayList();
        List<InsuranceVirtualFormDTO> insuranceVirtualFormDTOList = virtualBasic.getInsuranceVirtualFormDTOList();
        for (InsuranceVirtualFormDTO insuranceVirtualFormDTO : insuranceVirtualFormDTOList) {
            Integer insuranceType = insuranceVirtualFormDTO.getInsuranceType();
            if (Integer.compare(insuranceType, 2) == 0) {
                List<InsuranceVirtualItemDTO> insuranceVirtualItemDTOList = insuranceVirtualFormDTO.getInsuranceVirtualItemDTOList();
                if (CollectionUtils.isNotEmpty(insuranceVirtualFormDTOList)) {
                    insuranceVirtualItemList.addAll(insuranceVirtualItemDTOList);
                }
            }
        }
        List<InsuranceVirtualItemDTO> mainList = Lists.newArrayList();
        List<InsuranceVirtualItemDTO> subList = Lists.newArrayList();
        for (InsuranceVirtualItemDTO insuranceVirtualFormDTO : insuranceVirtualItemList) {
            Integer insuranceSubcategoryType = insuranceVirtualFormDTO.getInsuranceSubcategoryType();
            if (Integer.compare(insuranceSubcategoryType, 1) == 0) {
                mainList.add(insuranceVirtualFormDTO);
            }
            if (Integer.compare(insuranceSubcategoryType, 2) == 0) {
                subList.add(insuranceVirtualFormDTO);
            }
        }
        model.addAttribute("mainList", mainList);
        model.addAttribute("subList", subList);
        return "yqx/page/ax_insurance/query/virtual-detail";
    }

    private boolean judgment(Object object) {
        if (object == null) {
            return false;
        }
        if ("".equals(object)) {
            return false;
        }
        return true;
    }
}

