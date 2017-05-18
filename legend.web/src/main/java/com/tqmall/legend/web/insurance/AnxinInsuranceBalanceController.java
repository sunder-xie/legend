package com.tqmall.legend.web.insurance;

import com.google.common.base.Preconditions;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.insurance.domain.param.insurance.AgentCheckAccountParam;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.insurance.AnxinInsuranceBalanceFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceServicePackageBalanceStatisVo;
import com.tqmall.legend.facade.insurance.vo.InsuranceServicePackageBalanceVo;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 安心保险对账
 * Created by lixiao on 16/9/14.
 */
@Slf4j
@Controller
@RequestMapping("insurance/anxin/balance")
public class AnxinInsuranceBalanceController extends BaseController {
    @Autowired
    private AnxinInsuranceBalanceFacade anxinInsuranceBalanceFacade;

    @RequestMapping
    public String index() {
        return "yqx/page/ax_insurance/balance/balance-index";
    }

    //获取淘汽对账保单统计信息
    @RequestMapping("balance-total/{type}")
    @ResponseBody
    public Result<InsuranceServicePackageBalanceStatisVo> getBalanceTotal(@PathVariable final String type) {
        return new ApiTemplate<InsuranceServicePackageBalanceStatisVo>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                String userGlobalId = UserUtils.getUserGlobalIdForSession(request);
                Preconditions.checkArgument(StringUtils.isNotBlank(userGlobalId),"门店userGlobalId为空");
            }

            @Override
            protected InsuranceServicePackageBalanceStatisVo process() throws BizException {
                String userGlobalId = UserUtils.getUserGlobalIdForSession(request);
                if (type.equals("insurance_service_package")) {
                    return anxinInsuranceBalanceFacade.getInsuranceServicePackage(userGlobalId);
                } else if (type.equals("service_package_insurance")) {
                    return anxinInsuranceBalanceFacade.getServicePackageInsurance(userGlobalId);
                }
                return null;
            }
        }.execute();
    }

    //获取淘汽对账保单列表信息（?isCanSettle=true 表示选中了本月可结算）
    @RequestMapping("balance-list/{type}")
    @ResponseBody
    public Result<DefaultPage<InsuranceServicePackageBalanceVo>> getBalanceList(@PathVariable final String type,
                                                                             @RequestParam(value = "isCanSettle", required = false) final String isCanSettle,
                                                                             @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) final Pageable pageable) {
        return new ApiTemplate<DefaultPage<InsuranceServicePackageBalanceVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                String userGlobalId = UserUtils.getUserGlobalIdForSession(request);
                Preconditions.checkArgument(StringUtils.isNotBlank(userGlobalId),"门店userGlobalId为空");
            }

            @Override
            protected DefaultPage<InsuranceServicePackageBalanceVo> process() throws BizException {
                String userGlobalId = UserUtils.getUserGlobalIdForSession(request);

                AgentCheckAccountParam agentCheckAccountParam = new AgentCheckAccountParam();
                agentCheckAccountParam.setShopId(Integer.valueOf(userGlobalId));
                agentCheckAccountParam.setPageSize(pageable.getPageSize());
                agentCheckAccountParam.setPageNum(pageable.getPageNumber());
                if (StringUtils.isNotBlank(isCanSettle) && isCanSettle.equals("true")) {
                    agentCheckAccountParam.setCanSettlement(true);
                }
                PageEntityDTO<InsuranceServicePackageBalanceVo> insuranceToServicePageEntityDTO = null;
                if (type.equals("insurance_service_package")) {
                    insuranceToServicePageEntityDTO = anxinInsuranceBalanceFacade.getInsuranceServicePackageList(agentCheckAccountParam);
                }else if(type.equals("service_package_insurance")){
                    insuranceToServicePageEntityDTO = anxinInsuranceBalanceFacade.getServicePackageInsuranceList(agentCheckAccountParam);
                }
                if(insuranceToServicePageEntityDTO == null){
                    throw new BizException("获取淘汽对账列表信息失败");
                }
                List<InsuranceServicePackageBalanceVo> insuranceToServicePageEntityDTORecordList = insuranceToServicePageEntityDTO.getRecordList();
                Integer totalNum = insuranceToServicePageEntityDTO.getTotalNum();
                PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<InsuranceServicePackageBalanceVo> resultPage = new DefaultPage<InsuranceServicePackageBalanceVo>(insuranceToServicePageEntityDTORecordList, pageRequest, totalNum);
                return resultPage;
            }
        }.execute();

    }
}
