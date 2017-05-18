package com.tqmall.legend.web.marketing.gather.allot;

import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.customer.CubeCustomerInfoFacade;
import com.tqmall.legend.facade.customer.vo.CubeCustomerInfoVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by zsy on 16/12/14.
 * 客户归属
 */
@RequestMapping("marketing/gather/allot/customer-allot-list")
@Controller
public class GatherCustomerAllotController extends BaseController {
    @Autowired
    private CubeCustomerInfoFacade cubeCustomerInfoFacade;

    /**
     * 客户归属页
     *
     * @return
     */
    @RequestMapping
    public String customerAllotList(@RequestParam(value = "userId",required = false) String userId, Model model) {
        model.addAttribute("userId", userId);
        return "yqx/page/marketing/gather/allot/customer-allot-list";
    }

    /**
     * 客户归属列表
     *
     * @param pageable
     * @return
     */
    @RequestMapping("get-list")
    @ResponseBody
    public Result<DefaultPage<CubeCustomerInfoVo>> getList(@PageableDefault(page = 1, value = 10) Pageable pageable) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        DefaultPage<CubeCustomerInfoVo> customerInfoDTODefaultPage = cubeCustomerInfoFacade.getCubeCustomerInfoFromSearch(pageable, searchParams);
        return Result.wrapSuccessfulResult(customerInfoDTODefaultPage);
    }

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "marketing";
    }

    @ModelAttribute("subModule")
    public String subModule() {
        return "gather-allot";
    }
}