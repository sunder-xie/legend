package com.tqmall.legend.biz.customer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.bo.AccountAndCarBO;
import com.tqmall.legend.biz.base.BizJunitBase;
import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarRel;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 17/2/27.
 */
public class CustomerAndAccountInfoTest extends BizJunitBase {
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerCarRelService customerCarRelService;

    /**
     * 客户车辆绑定账户信息单元测试
     * case1：车牌已存在车主不更换车主时，建立关联关系。
     * case2：车牌已存在车主更换车主时，建立拥有关系，并删除关联关系。
     * case2：车牌无车主时，添加车辆，车主为当前车主。
     */
    @Test
    public void testCustomerCarBundAccountInfo(){
        Long accountId = 452392L;
        Long shopId = 1L;
        String license = "浙abc123";
        boolean isChanger = true;//是否更换车主 false 否
        ShopManagerLogin shopManagerLogin = userInfoService.getUserInfoByMobile("18057108183");

        UserInfo userInfo = new UserInfo();
        userInfo.setShopId(shopManagerLogin.getShopId());
        userInfo.setUserId(shopManagerLogin.getId());
        userInfo.setManagerLoginId(shopManagerLogin.getManagerId());


        Map<String, Object> param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("license", license);
        List<CustomerCar> customerCars = customerCarService.select(param);
        //车牌无车主时
        if (CollectionUtils.isEmpty(customerCars)) {
            InsertCustomerAndAccountInfo(license,accountId, shopId,userInfo.getUserId());
        }else {
            AccountInfo accountInfo = accountInfoService.getAccountInfoById(accountId);
            CustomerCar customerCar = customerCars.get(0);
            AccountAndCarBO accountAndCarBO = new AccountAndCarBO();
            accountAndCarBO.setShopId(shopId);
            accountAndCarBO.setUserId(userInfo.getUserId());
            accountAndCarBO.setCustomerId(accountInfo.getCustomerId());//409672   409677
            accountAndCarBO.setCustomerCarId(customerCar.getId());
            accountAndCarBO.setAccountId(accountInfo.getId());

            if (!isChanger && !accountInfo.getCustomerId().equals(customerCar.getCustomerId())){
                //有车主存在，不更换车主时
                CustomerCarRel customerCarRel = customerCarRelService.findByCustomerIdAndCarId(accountAndCarBO);
                if (customerCarRel == null){
                    customerCarRel = new CustomerCarRel();
                    customerCarRel.setShopId(shopId);
                    customerCarRel.setAccountId(accountInfo.getId());
                    customerCarRel.setCreator(accountAndCarBO.getUserId());
                    customerCarRel.setCustomerCarId(customerCar.getId());
                    customerCarRel.setCustomerId(accountInfo.getCustomerId());
                    customerCarRelService.save(customerCarRel);
                }
            }else if (!accountInfo.getCustomerId().equals(customerCar.getCustomerId())){
                //有车主存在，更换车主时，建立拥有关系，并删除关联关系。
                customerCarRelService.delete(accountAndCarBO);
                customerCar.setCustomerId(accountInfo.getCustomerId());
                customerCarService.update(customerCar);
            }

        }
    }

    /**
     * 客户车辆解绑单元测试
     * case1：如果指定车辆与车主是拥有关系时，并新建客户信息和账户信息，车辆绑定在该车主下。
     * case2：如果指定车辆与车主是关联关系时，删除关联关系表就可以了。
     *
     */
    @Test
    public void testCustomerCarUnBundAccountInfo(){
        Long carId = 343717L;
        Long accountId = 452392L;
        ShopManagerLogin shopManagerLogin = userInfoService.getUserInfoByMobile("18057108183");
        CustomerCar customerCar = customerCarService.selectById(carId);
        AccountInfo accountInfo = accountInfoService.getAccountInfoById(accountId);
        AccountAndCarBO accountAndCarBO = new AccountAndCarBO();
        accountAndCarBO.setShopId(accountInfo.getShopId());
        accountAndCarBO.setUserId(1L);
        accountAndCarBO.setCustomerId(accountInfo.getCustomerId());//409672
        accountAndCarBO.setCustomerCarId(customerCar.getId());
        accountAndCarBO.setAccountId(accountInfo.getId());
        //拥有关系409677
        if (customerCar.getCustomerId().equals(accountInfo.getCustomerId())){
            //1219--要求解绑车辆时新插入一条信息为空的客户信息,否则影响开单
            Customer customer = new Customer();
            customer.setShopId(customerCar.getShopId());
            customer.setCreator(1L);
            this.customerService.insertCustomerAndAccountInfo(customer);
            customerCar.setCustomerId(customer.getId());
            customerCarService.update(customerCar);
        }else {
            customerCarRelService.delete(accountAndCarBO);
        }
    }


    private Customer InsertCustomerAndAccountInfo(String license,Long accountId,Long shopId,Long userId) {
        AccountInfo accountInfo = accountInfoService.getAccountInfoById(accountId);
        Customer customer = accountInfo.getCustomer();
        CustomerCar customerCar = new CustomerCar();
        customerCar.setRefer("0");
        customerCar.setCreator(userId);
        customerCar.setLicense(license);
        customerCar.setMobile(customer.getMobile());
        customerCar.setShopId(customer.getShopId());
        customerCar.setCustomerId(customer.getId());
        customerCar.setCustomerName(customer.getCustomerName());
        customerCarService.save(customerCar);
        return customer;
    }

    @Test
    public void testCollectionUnion(){
        List<CustomerCar> customerCars1 = Lists.newArrayList();
        List<CustomerCar> customerCars2 = Lists.newArrayList();
        CustomerCar car = new CustomerCar();
        car.setLicense("ces");
        customerCars1.add(car);

        for (int i = 0; i < 5; i++) {
            CustomerCar c = new CustomerCar();
            c.setLicense("ces"+i);
            customerCars2.add(c);
        }
List<CustomerCar> n = Lists.newArrayList(customerCars1);
        n.addAll(customerCars2);
//        List<CustomerCar> cars = (List<CustomerCar>) org.apache.commons.collections.CollectionUtils.union(customerCars1,customerCars2);
        Assert.assertEquals(n.size(),6);
    }

    @Test
    public void testGetBindAccountInfosByCarId(){
        List<AccountInfo> accountInfos = accountInfoService.getBindAccountInfosByCarId(280L, 343711L);
        Assert.assertEquals(accountInfos.size(),3);

    }


}
