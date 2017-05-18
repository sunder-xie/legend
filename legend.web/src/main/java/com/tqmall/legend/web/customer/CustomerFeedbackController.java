package com.tqmall.legend.web.customer;

import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerFeedbackService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerFeedback;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2015-01-20 17:10
 */
@Controller
@Slf4j
@RequestMapping("shop/customer_feedback")
public class CustomerFeedbackController extends BaseController {

    @Autowired
    CustomerFeedbackService customerFeedbackService;

    /**
     * 获取回访记录
     *
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping("getListByCustomerCarId")
    @ResponseBody
    public Object getListByCustomerCarId(
            @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        searchParams.put("visitTimeLt",new Date());
        DefaultPage<CustomerFeedback> page = (DefaultPage<CustomerFeedback>) customerFeedbackService.getPage(pageable,
                searchParams);
        page.setPageUri(request.getRequestURI());
        page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(page);
    }
}
