package com.tqmall.legend.web.customer;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerTagService;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerTag;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zsy on 16/4/13.
 */
@Slf4j
@Controller
@RequestMapping("shop/customer/tag")
public class CustomerTagController extends BaseController {
    @Autowired
    private CustomerTagService customerTagService;
    @Autowired
    private CustomerCarService customerCarService;

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestParam(value = "id",required = true)Long id){
        CustomerTag customerTag = customerTagService.selectById(id);
        //自定义标签才能删除
        if(customerTag != null && customerTag.getTagRefer().equals(1)){
            try {
                customerTagService.deleteById(id);
                return Result.wrapSuccessfulResult(true);
            } catch (Exception e) {
                log.error("删除客户标签出现异常",e);
            }
        }
        return Result.wrapErrorResult(LegendErrorCode.CAR_TAG_DELETE_EX.getCode(),LegendErrorCode.CAR_TAG_DELETE_EX.getErrorMessage());
    }

    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    public Result edit(CustomerTag customerTag){
        if(customerTag == null || StringUtils.isBlank(customerTag.getTagName())){
            return Result.wrapErrorResult(LegendErrorCode.EMPTY_CONTENT.getCode(),LegendErrorCode.EMPTY_CONTENT.getErrorMessage());
        }
        //数据校验
        String tagName = customerTag.getTagName();
        if(tagName.length() > 6){
            return Result.wrapErrorResult(LegendErrorCode.CAR_CHECK_EX.getCode(),LegendErrorCode.CAR_CHECK_EX.getErrorMessage("标签字数不能超过6各字"));
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Long userId = UserUtils.getUserIdForSession(request);
        Long carId = customerTag.getCustomerCarId();
        CustomerCar customerCar = customerCarService.selectById(carId);
        if(customerCar == null){
            return Result.wrapErrorResult(LegendErrorCode.CAR_NULL_EX.getCode(),LegendErrorCode.CAR_NULL_EX.getErrorMessage());
        }
        List<CustomerTag> customerTagList = customerTagService.getNoCustomCarTagByCarId(shopId, carId);
        if(!CollectionUtils.isEmpty(customerTagList)){
            //自定义标签最多2个
            //todo 返回数据目前有2个系统标签,所以标签数不能超过4
            if(customerTagList.size() >= 4){
                return Result.wrapErrorResult(LegendErrorCode.CAR_CHECK_EX.getCode(),LegendErrorCode.CAR_CHECK_EX.getErrorMessage("自定义标签最多能添加2个"));
            }
            for(CustomerTag temp : customerTagList){
                String tempTagName = temp.getTagName();
                if(tagName.equals(tempTagName)){
                    return Result.wrapErrorResult(LegendErrorCode.CAR_CHECK_EX.getCode(),LegendErrorCode.CAR_CHECK_EX.getErrorMessage("标签名称重复"));
                }
            }
        }
        //添加
        customerTag.setShopId(shopId);
        customerTag.setCreator(userId);
        customerTag.setCustomerId(customerCar.getCustomerId());
        customerTag.setTagRefer(1);//自定义标签
        try {
            customerTagService.insert(customerTag);
            return Result.wrapSuccessfulResult(customerTag.getId());
        } catch (Exception e) {
            log.error("添加客户标签出现异常",e);
        }
        return Result.wrapErrorResult(LegendErrorCode.CAR_TAG_ADD_EX.getCode(),LegendErrorCode.CAR_TAG_ADD_EX.getErrorMessage());
    }


    @RequestMapping("value_list")
    @ResponseBody
    public Object listTagValues() {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<String> tagList = customerTagService.listTag(shopId);
        return Result.wrapSuccessfulResult(tagList);
    }
}
