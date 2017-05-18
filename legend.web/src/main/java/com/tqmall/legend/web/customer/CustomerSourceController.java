package com.tqmall.legend.web.customer;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.customer.CustomerSourceService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2015/6/8.
 */
@Controller
@RequestMapping("shop/customer_source")
public class CustomerSourceController {
    Logger logger = LoggerFactory.getLogger(CustomerSourceController.class);

    @Autowired
    private CustomerSourceService customerSourceService;

    /**
     * 获取店铺对应的客户来源列表
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(HttpServletRequest request) {
        long shopId = UserUtils.getShopIdForSession(request);
        if(shopId < 1){
            logger.error("店铺信息错误");
            return Result.wrapErrorResult("","获取失败");
        }
        Map<String,Object> param = new HashMap<>();
        param.put("shopId",shopId);

        List<CustomerSource> customerSourceList = customerSourceService.getCustomerSourceList(param);

        return Result.wrapSuccessfulResult(customerSourceList);
    }


}
