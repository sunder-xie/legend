package com.tqmall.legend.web.smart;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.insurance.domain.param.smart.SmartSearchParam;
import com.tqmall.insurance.domain.result.InsuranceBasicDTO;
import com.tqmall.insurance.domain.result.InsuranceFormDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualBasicDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualFormDTO;
import com.tqmall.legend.facade.insurance.AnxinInsuranceVirtualFlowFacade;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceCategoryVo;
import com.tqmall.legend.facade.smart.BihuSmartSearchFacade;
import com.tqmall.legend.facade.smart.result.SmartInsureCategoryDO;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by zwb on 16/12/19.
 */
@Slf4j
@Controller
@RequestMapping("smart/bihu/flow")
public class BihuSmartFlowController extends BaseController {
    @Autowired
    private InsuranceFormFacade insuranceFormFacade;
    @Autowired
    private BihuSmartSearchFacade bihuSmartSearchFacade;
    @Autowired
    private AnxinInsuranceVirtualFlowFacade anxinInsuranceVirtualFlowFacade;

    /**
     * 智能投保流程 1-3步  从输入基本信息 输入车型 到保费计算
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "bihu-flow", method = RequestMethod.GET)
    public String bihuListPage(Model model) {
        model.addAttribute("bihu", 1);
        return "yqx/page/smart/smart-billing";
    }

    @RequestMapping(value = "bihu-flwo-two", method = RequestMethod.POST)
    public String bihuFlow(Model model, @RequestBody String data) {
        String url = "yqx/page/smart/smart-billing-two";
        try {
            data = URLDecoder.decode(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (!data.contains("{")) {
            return "common/error";
        }

        SmartInsureCategoryDO smartInsureCategoryDO = JSONUtil.readValue(data.substring(data.indexOf("{")), SmartInsureCategoryDO.class);
        model.addAttribute("insuranceBasic", smartInsureCategoryDO.getInsuranceBasicDTO());
        model.addAttribute("listInsuranceCategory", smartInsureCategoryDO.getInsuranceCategoryVoList());

        return url;
    }

    /**
     * 智能投保壁虎搜索接口
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "bihuSearch", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Result bihuSearch(@RequestBody SmartSearchParam param) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        param.setAgentId(Integer.valueOf(ucShopId));
        param.setUid(userInfo.getUserId().intValue());
        return bihuSmartSearchFacade.bihuSearch(param);
    }

    /**
     * 点击返回修改返回保单详情
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "back-update", method = RequestMethod.GET)
    public String backUpdate(Model model, @RequestParam(value = "id", required = false) Integer id) {
        InsuranceBasicDTO insurance = new InsuranceBasicDTO();
        List<InsuranceCategoryVo> insuranceList;
        if (id == null) {
            insuranceList = insuranceFormFacade.getInsuranceCategory();
        } else {
            String userGlobalId = UserUtils.getUserGlobalIdForSession(request);
            insurance = insuranceFormFacade.backUpdateInfo(id, Integer.valueOf(userGlobalId));
            insuranceList = insuranceFormFacade.getInsuranceCategoryHasChoose(insurance);

            List<InsuranceFormDTO> insuranceFormList = insurance.getInsuranceFormDTOList();
            if (insuranceFormList != null) {
                //商业险form  若商业险只选
                InsuranceFormDTO insuranceForm = null;
                //交强险form
                InsuranceFormDTO insuranceFormJQX = null;
                for (int i = 0; i < insuranceFormList.size(); i++) {
                    if (insuranceFormList.get(i).getInsuranceType().compareTo(2) == 0) {
                        insuranceForm = insuranceFormList.get(i);
                    }
                    if (insuranceFormList.get(i).getInsuranceType().compareTo(1) == 0) {
                        insuranceFormJQX = insuranceFormList.get(i);
                    }
                }
                if (insuranceForm != null) {
                    Date packageStartTime = insuranceForm.getPackageStartTime();
                    setTime(insuranceForm, packageStartTime);
                }
                if (insuranceFormJQX != null) {
                    Date packageStartTime = insuranceFormJQX.getPackageStartTime();
                    setTime(insuranceFormJQX, packageStartTime);
                }
            }
        }
        model.addAttribute("insuranceBasic", insurance);
        model.addAttribute("listInsuranceCategory", insuranceList);
        return "yqx/page/smart/smart-billing-two";
    }

    /**
     * 跳转到确认虚拟投保方案页面
     *
     * @return
     */
    @RequestMapping(value = "virtual-plan", method = RequestMethod.GET)
    public String virtualPlan(@RequestParam("id") Integer id, Model model) {
        String ucId = UserUtils.getUserGlobalIdForSession(request);
        Integer ucShopId = getUserGlobalShopId(ucId);
        InsuranceVirtualBasicDTO virtualBasic = anxinInsuranceVirtualFlowFacade.selectVirtualDetail(id, ucShopId);
        model.addAttribute("virtualBasic", virtualBasic);
        return "yqx/page/ax_insurance/virtual/virtual-plan";
    }


    @RequestMapping(value = "virtual-modify", method = RequestMethod.GET)
    public String virtualModify(@RequestParam("id") Integer id, Model model) {
        String ucId = UserUtils.getUserGlobalIdForSession(request);
        Integer ucShopId = getUserGlobalShopId(ucId);
        InsuranceVirtualBasicDTO virtualBasic = anxinInsuranceVirtualFlowFacade.selectVirtualDetail(id, ucShopId);
        List<InsuranceCategoryVo> categoryVoList = insuranceFormFacade.getInsuranceCategory();
        if (virtualBasic != null) {
            categoryVoList = anxinInsuranceVirtualFlowFacade.getInsuranceCategoryHasChoose(virtualBasic, categoryVoList);

            List<InsuranceVirtualFormDTO> insuranceFormList = virtualBasic.getInsuranceVirtualFormDTOList();
            if (insuranceFormList != null) {
                //商业险form  若商业险只选
                InsuranceVirtualFormDTO insuranceForm = null;
                //交强险form
                InsuranceVirtualFormDTO insuranceFormJQX = null;
                for (int i = 0; i < insuranceFormList.size(); i++) {
                    if (insuranceFormList.get(i).getInsuranceType().compareTo(2) == 0) {
                        insuranceForm = insuranceFormList.get(i);
                    }
                    if (insuranceFormList.get(i).getInsuranceType().compareTo(1) == 0) {
                        insuranceFormJQX = insuranceFormList.get(i);
                    }
                }
                if (insuranceForm != null) {
                    Date packageStartTime = insuranceForm.getPackageStartTime();
                    setVirtualTime(insuranceForm, packageStartTime);
                }
                if (insuranceFormJQX != null) {
                    Date packageStartTime = insuranceFormJQX.getPackageStartTime();
                    setVirtualTime(insuranceFormJQX, packageStartTime);
                }
            }
        }
        model.addAttribute("listInsuranceCategory", categoryVoList);
        model.addAttribute("insuranceBasic", virtualBasic);
        model.addAttribute("mode", 3);
        return "yqx/page/smart/smart-billing-two";
    }

    private void setTime(InsuranceFormDTO insuranceFormJQX, Date packageStartTime) {
        if (packageStartTime != null) {
            Map map = DateUtil.insuranceDate(packageStartTime);
            insuranceFormJQX.setPackageStartTime((Date) map.get("startTime"));
            insuranceFormJQX.setPackageEndTime((Date) map.get("endTime"));
        }
    }

    private void setVirtualTime(InsuranceVirtualFormDTO insuranceFormJQX, Date packageStartTime) {
        if (packageStartTime != null) {
            Map map = DateUtil.insuranceDate(packageStartTime);
            insuranceFormJQX.setPackageStartTime((Date) map.get("startTime"));
            insuranceFormJQX.setPackageEndTime((Date) map.get("endTime"));
        }
    }

    private Integer getUserGlobalShopId(String ucShopId) {
        if (StringUtils.isBlank(ucShopId)) {
            throw new BizException("门店信息不存在");
        }
        return Integer.parseInt(ucShopId);

    }

    /**
     * 跳转到确认保费页面
     *
     * @return
     */
    @RequestMapping(value = "confirm-info", method = RequestMethod.GET)
    public String confirmInfo() {
        return "yqx/page/ax_insurance/create/confirmInfo";
    }

    // 第五步结果页
    @RequestMapping(value = "insurance-result", method = RequestMethod.GET)
    public String insuranceResult(String orderSn, Model model) {
        if(StringUtils.isBlank(orderSn)){
            return "common/error";
        }
        model.addAttribute("orderSn", orderSn);
        return "yqx/page/ax_insurance/create/whetherSuc";
    }
}
