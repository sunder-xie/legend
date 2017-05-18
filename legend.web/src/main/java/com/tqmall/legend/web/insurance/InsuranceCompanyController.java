package com.tqmall.legend.web.insurance;

import com.tqmall.legend.biz.insurance.InsuranceCompanyService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.insurance.InsuranceCompany;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 保险公司Controller
 * <p/>
 * Created by dongc on 15/5/27.
 */
@Controller
@RequestMapping("insurance")
public class InsuranceCompanyController extends BaseController {

    @Autowired
    InsuranceCompanyService insuranceCompanyService;


    /**
     * 查询保险公司列表
     *
     * @param request
     * @param response
     * @return JSON
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(HttpServletRequest request, HttpServletResponse response) {
        List<InsuranceCompany> insuranceCompanyList = insuranceCompanyService.select(null);
        return Result.wrapSuccessfulResult(insuranceCompanyList);
    }

}
