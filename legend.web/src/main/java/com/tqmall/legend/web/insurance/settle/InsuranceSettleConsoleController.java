package com.tqmall.legend.web.insurance.settle;

import com.google.common.base.Preconditions;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.BdUtil;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.insurance.AnxinManaSettleFacade;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.insurance.vo.SettleCheckDetailSearchVO;
import com.tqmall.legend.web.insurance.vo.SettleCheckDetailShowVO;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.mana.client.beans.settle.SettleServiceCheckDetailDTO;
import com.tqmall.mana.client.beans.settle.SettleShopDTO;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.PageableResponseExtend;
import com.tqmall.search.dubbo.client.mana.param.SettleCheckDetailRequest;
import com.tqmall.search.dubbo.client.mana.result.SettleCheckDetailResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 17/1/13.
 * 14:42
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Controller
@RequestMapping("insurance/settle/console")
public class InsuranceSettleConsoleController extends BaseController {

    @Autowired
    private AnxinManaSettleFacade anxinManaSettleFacade;
    @Autowired
    private InsuranceFormFacade insuranceFormFacade;

    // 获得收入分页的数据
    @RequestMapping("getDetailData")
    @ResponseBody
    public Result getDetailPageList(@PageableDefault(page = 1, size = 15, sort = "id") final Pageable pageable) {
        return new ApiTemplate<DefaultPage<SettleCheckDetailShowVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                String ucShopId = UserUtils.getUserGlobalIdForSession(request);
                Preconditions.checkArgument(StringUtils.isNotBlank(ucShopId), "门店userGlobalId为空");
            }

            @Override
            protected DefaultPage<SettleCheckDetailShowVO> process() throws BizException {

                String shopId = UserUtils.getUserGlobalIdForSession(request);
//                 shopId = "22929";

                //获得前端传递过来的参数
                Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

                SettleCheckDetailSearchVO settleCheckDetailRequest = getSearchRequestFromParams(searchParams);
                if (settleCheckDetailRequest == null) {
                    throw new IllegalArgumentException("缺少对账类型，无法进行查询");
                }
                settleCheckDetailRequest.setAgentId(Integer.valueOf(shopId));
                PageableRequest pageableRequest = new PageableRequest(pageable.getPageNumber() - 1, pageable.getPageSize());

                PagingResult<SettleCheckDetailShowVO> settleCheckDetailPage = getShowPage(settleCheckDetailRequest, pageableRequest);
                Integer totalSize = settleCheckDetailPage.getTotal();
                PageRequest pageRequest =
                        new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                                pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<SettleCheckDetailShowVO> page = new DefaultPage<>(settleCheckDetailPage.getList(), pageRequest, totalSize);
                return page;
            }
        }.execute();

    }




    // 下载数据
    @RequestMapping(value = "download/excel", method = RequestMethod.GET)
    public void downloadDetailExcel(@RequestParam(value = "type", defaultValue = "1") final Integer settleRuleType,
                                    HttpServletRequest request, final HttpServletResponse response) throws IOException {


    }

    // 获得现金的合计
    @RequestMapping("getCashAmount")
    @ResponseBody
    public Result<String> getCashAmount() {
        String shopId = UserUtils.getUserGlobalIdForSession(request);

        SettleShopDTO shopDetail = anxinManaSettleFacade.getSettleShopDetailByShopId(Integer.valueOf(shopId));

        if (shopDetail == null) {
            return null;
        }

        BigDecimal settledCashAmount = shopDetail.getSettledCashAmount() == null ? BigDecimal.ZERO : shopDetail.getSettledCashAmount();
        BigDecimal payableCashAmount = shopDetail.getPayableCashAmount() == null ? BigDecimal.ZERO : shopDetail.getPayableCashAmount();

        return Result.wrapSuccessfulResult(settledCashAmount.add(payableCashAmount).toString());
    }


    // 获得服务包的合计
    @RequestMapping("getPackageAmount")
    @ResponseBody
    public Result<String> getPackageAmount() {
        String shopId = UserUtils.getUserGlobalIdForSession(request);

        SettleShopDTO shopDetail = anxinManaSettleFacade.getSettleShopDetailByShopId(Integer.valueOf(shopId));

        if (shopDetail == null) {
            return null;
        }
        Integer waitPackageNum = shopDetail.getWaitPackageNum() == null ? 0 : shopDetail.getWaitPackageNum();
        Integer sendPackageNum = shopDetail.getSendPackageNum() == null ? 0 : shopDetail.getSendPackageNum();
        Integer receivePackageNum = shopDetail.getReceivePackageNum() == null ? 0 : shopDetail.getReceivePackageNum();

        return Result.wrapSuccessfulResult(String.valueOf(waitPackageNum + sendPackageNum + receivePackageNum));
    }

    /*==== private ==========*/

    private SettleCheckDetailSearchVO getSearchRequestFromParams(Map<String, Object> searchParams) {
        SettleCheckDetailSearchVO resultRequest = new SettleCheckDetailSearchVO();
        // 必须有对账类型
        if (!searchParams.containsKey("settleRule")) {
            return null;
        }
        resultRequest.setSettleRuleType(Integer.valueOf((String) searchParams.get("settleRule")));
        if (searchParams.containsKey("settleStartTime") && !StringUtils.isEmpty((String) searchParams.get("settleStartTime"))) {
            resultRequest.setSettleConditionTimeStart((String) searchParams.get("settleStartTime"));
        }
        if (searchParams.containsKey("settleEndTime") && !StringUtils.isEmpty((String) searchParams.get("settleEndTime"))) {
            resultRequest.setSettleConditionTimeEnd((String) searchParams.get("settleEndTime"));
        }
        //模式
        if (searchParams.containsKey("cooperationId") && !((String) searchParams.get("cooperationId")).equals("-1")) {
            resultRequest.setCooperationModeId(Integer.valueOf((String) searchParams.get("cooperationId")));
        }
        //车牌
        if (searchParams.containsKey("license")) {
            String licenseNo = ((String) searchParams.get("license")).replaceAll(" +", "");
            if (!StringUtils.isEmpty(licenseNo)) {
                resultRequest.setVehicleSn(licenseNo);
            }
        }
        //车主姓名
        if (searchParams.containsKey("carOwner")) {
            String carOwner = ((String) searchParams.get("carOwner")).replaceAll(" +", "");
            if (!StringUtils.isEmpty(carOwner)) {
                resultRequest.setCarOwnerName(carOwner);
            }
        }
        //保险公司
        if (searchParams.containsKey("company")) {
            String company = ((String) searchParams.get("company")).replaceAll(" +", "");
            if (!StringUtils.isEmpty(company)) {
                resultRequest.setInsuranceCompanyName(company);
            }
        }
        //结算项目
        if (searchParams.containsKey("settleProjectId") && !((String) searchParams.get("settleProjectId")).equals("-1")) {
            resultRequest.setSettleProjectId(Integer.valueOf((String) searchParams.get("settleProjectId")));
        }
        //结算项目
        if (searchParams.containsKey("isPackageCheck")) {
            resultRequest.setIsPackageCheck(true);
        }


        return resultRequest;

    }

    //获得前端页面展示数据对账
    private PagingResult<SettleCheckDetailShowVO> getShowPage(SettleCheckDetailSearchVO detailSearchVO,
                                                          PageableRequest pageableRequest) {
        SettleCheckDetailRequest settleCheckDetailRequest = BdUtil.bo2do(detailSearchVO, SettleCheckDetailRequest.class);
        PageableResponseExtend<SettleCheckDetailResult> settleCheckDetailPage = anxinManaSettleFacade.getSettleCheckDetailPage(settleCheckDetailRequest, pageableRequest);
        if (settleCheckDetailPage == null) {
            throw new IllegalArgumentException("查询分页失败");
        }
        BigDecimal pageAmount = BigDecimal.ZERO;
        Map<String, Object> extend = settleCheckDetailPage.getExtend();
        if(!CollectionUtils.isEmpty(extend)){
            Object sum_settle_fee = extend.get("sum_settle_fee");
            if(sum_settle_fee != null){
                pageAmount = new BigDecimal((Double)sum_settle_fee);
            }
        }

        boolean isPackageCheck = detailSearchVO.getIsPackageCheck();
        List<SettleCheckDetailShowVO> resultList = new ArrayList<>();
        for (SettleCheckDetailResult detailResult : settleCheckDetailPage.getContent()) {
            SettleCheckDetailShowVO settleCheckDetailShowVO = BdUtil.bo2do(detailResult,SettleCheckDetailShowVO.class);
            settleCheckDetailShowVO.setPageAmount(pageAmount);
            //服务包模式
            if(isPackageCheck && detailResult.getSettleProjectId().equals(SettleCheckDetailShowVO.SETTLE_PACKAGE_PROJECT_ID)){
                SettleServiceCheckDetailDTO serviceDTO = anxinManaSettleFacade.getServicePackageDetailByInsuranceSn(detailResult.getInsuranceOrderSn());
                if(serviceDTO != null){
                    settleCheckDetailShowVO.setSettlePackageName(serviceDTO.getSettlePackageName());
                    settleCheckDetailShowVO.setSettlePackageStatus(serviceDTO.getSettlePackageStatus());
                }

            }

            resultList.add(settleCheckDetailShowVO);
        }

        return PagingResult.wrapSuccessfulResult(resultList, (int) settleCheckDetailPage.getTotalElements());

    }


}