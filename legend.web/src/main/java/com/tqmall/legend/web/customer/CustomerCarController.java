package com.tqmall.legend.web.customer;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by litan on 14-11-9.
 */
@Controller
@RequestMapping("shop/customer_car")
public class CustomerCarController extends BaseController {

    @Autowired
    private CustomerCarService customerCarService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private ShopConfigureService shopConfigureService;

    @RequestMapping("list")
    @ResponseBody
    public Result car_list(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", shopId);
        List<CustomerCar> list = customerCarService.select(map);
        return Result.wrapSuccessfulResult(list);
    }

    @RequestMapping("get_car_by_license")
    @ResponseBody
    public Result getCarByLicense(String carLicense) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", shopId);
        map.put("license", carLicense);
        List<CustomerCar> list = customerCarService.select(map);
        if (!CollectionUtils.isEmpty(list)) {
            CustomerCar customerCar = list.get(0);
            Customer customer = customerService.selectById(customerCar.getCustomerId());
            if (customer != null) {
                customerCar.setCustomerName(customer.getCustomerName());
                customerCar.setMobile(customer.getMobile());
                customerCar.setCustomerAddr(customer.getCustomerAddr());
                // 关联客户单位
                customerCar.setCompany(customer.getCompany());
                if (StringUtil.isStringEmpty(customer.getContact())) {
                    customerCar.setContact(customer.getCustomerName());
                }else {
                    customerCar.setContact(customer.getContact());
                }
                if (StringUtil.isStringEmpty(customer.getContactMobile())) {
                    customerCar.setContactMobile(customer.getMobile());
                }else {
                    customerCar.setContactMobile(customer.getContactMobile());
                }
                customerCar.setIdentityCard(customer.getIdentityCard());
                // 获得车辆的未计算信息
                map.put("payStatus", 1);
                map.put("carLicense", carLicense);
                if (orderInfoService.selectCount(map) > 0) {
                    customerCar.setHasPayable(Boolean.TRUE);
                } else {
                    customerCar.setHasPayable(Boolean.FALSE);
                }
            }
            return Result.wrapSuccessfulResult(customerCar);
        } else {
            return Result.wrapErrorResult("", "无结果");
        }
    }

    /**
     * 保险或年检或保养即将到期车辆列表
     *
     * @param request
     * @param type
     *            insurance | auditing | keepup 保险或年检或保养
     * @return
     */

    @RequestMapping(value = "recent_expire_list", method = RequestMethod.GET)
    @ResponseBody
    public Result recentInsuranceExpireList(HttpServletRequest request,
            @RequestParam("type") String type) {

        long shopId = UserUtils.getShopIdForSession(request);
        int day = 0;
        if (type.equals("insurance")) {
            type = "insurance";
            day = Constants.INSURANCE_NOTICE_DAY;
            Map searchParams = new HashMap();
            searchParams.put("shopId", shopId);
            searchParams.put("confType", 4);
            // 设置提前时间值
            List<ShopConfigure> shopConfigureList = shopConfigureService.select(searchParams);
            if (shopConfigureList != null && !shopConfigureList.isEmpty()) {
                if (!StringUtil.isStringEmpty(shopConfigureList.get(0).getConfValue())) {
                    day = Integer.parseInt(shopConfigureList.get(0).getConfValue());
                }
            }

        } else if (type.equals("auditing")) {
            type = "auditing";
            day = Constants.CAR_AUDIT_NOTICE_DAY;
            Map searchParams = new HashMap();
            searchParams.put("shopId", shopId);
            searchParams.put("confType", 5);
            // 设置提前时间值
            List<ShopConfigure> shopConfigureList = shopConfigureService.select(searchParams);
            if (shopConfigureList != null && !shopConfigureList.isEmpty()) {
                if (!StringUtil.isStringEmpty(shopConfigureList.get(0).getConfValue())) {
                    day = Integer.parseInt(shopConfigureList.get(0).getConfValue());
                }
            }
        } else if (type.equals("keepup")) {
            type = "keepup";
            day = Constants.KEEPUP_NOTICE_DAY;
            Map searchParams = new HashMap();
            searchParams.put("shopId", shopId);
            searchParams.put("confType", 6);
            // 设置提前时间值
            List<ShopConfigure> shopConfigureList = shopConfigureService.select(searchParams);
            if (shopConfigureList != null && !shopConfigureList.isEmpty()) {
                if (!StringUtil.isStringEmpty(shopConfigureList.get(0).getConfValue())) {
                    day = Integer.parseInt(shopConfigureList.get(0).getConfValue());
                }
            }
        }

        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        Date date = DateUtil.getStartTime();
        searchParams.put("shopId", shopId);
        searchParams.put(type + "TimeGe", date);
        searchParams.put(type + "TimeLe", DateUtils.addDays(date, day));
        ArrayList<String> sorts = new ArrayList<>();
        sorts.add(type + "_time asc");
        searchParams.put("sorts", sorts);
        searchParams.put("isDeleted", "N");

        List<CustomerCar> customerCarList = customerCarService.select(searchParams);

        // 查询车主姓名电话数据开始
        List<Long> customerIdsList = new ArrayList<>();
        Long[] customerIds;
        for (CustomerCar customerCar : customerCarList) {
            customerIdsList.add(customerCar.getCustomerId());
        }
        if (!CollectionUtils.isEmpty(customerIdsList)) {
            customerIds = customerIdsList.toArray(new Long[customerIdsList.size()]);
            List<Customer> customerList = customerService.selectByIds(customerIds);

            for (CustomerCar customerCar : customerCarList) {
                for (Customer customer : customerList) {
                    if (customerCar.getCustomerId().equals(customer.getId())) {
                        customerCar.setCustomerName(customer.getCustomerName());
                        customerCar.setMobile(customer.getMobile());
                    }
                }
            }
        }
        // 查询车主姓名电话数据结束
        return Result.wrapSuccessfulResult(customerCarList);
    }

    @RequestMapping("get_by_id")
    @ResponseBody
    public Result getCarById(@RequestParam("id") Long id) {
        return Result.wrapSuccessfulResult(this.customerCarService.selectById(id));
    }

    /**
     * 根据车牌号，判断该车牌是否存在车主
     * @return
     */
    @RequestMapping("has-exist-customer")
    @ResponseBody
    public Result hasExistCustomer(@RequestParam(value = "carLicense")String carLicense){
        Long shopId = UserUtils.getShopIdForSession(request);
        if (customerCarService.hasExistCustomer(shopId,carLicense)){
            return Result.wrapSuccessfulResult("车主信息已存在");
        }
        return Result.wrapErrorResult("", "车主信息不存在");
    }

    /**
     * 删除车辆
     * @param id 车辆ID
     * @return
     */
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> deleteCarInfo(final Long id) {
        return new ApiTemplate<Boolean>(){

            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id,"车辆参数为空!");
            }

            /**
             * 有工单车辆不能删除
             * 删除车辆时同时删除车辆客户关联关系表
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Long userId = UserUtils.getUserIdForSession(request);
                logger.info("[开始删除车辆操作]操作人:{},门店ID:{},车辆ID:{}", userId, shopId, id);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("customerCarId", id);
                params.put("shopId", shopId);
                if (!CollectionUtils.isEmpty(orderInfoService.select(params))) {
                    throw new BizException("此车辆存在工单，不能删除！");
                }

                try {
                    customerCarService.deleteCar(shopId, userId, id);
                    return true;
                } catch (Exception e) {
                    logger.error("[删除车辆失败]操作人:{},门店ID:{},车辆ID:{}", userId, shopId, id, e);
                    throw new BizException("删除车辆异常,请联系我们!");
                }
            }
        }.execute();
    }
}
