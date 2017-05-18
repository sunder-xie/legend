package com.tqmall.legend.facade.customer.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.BdUtil;
import com.tqmall.cube.shop.RpcCustomerInfoService;
import com.tqmall.cube.shop.result.CustomerInfoDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarRelService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.customer.CustomerTagService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerTag;
import com.tqmall.legend.facade.customer.CustomerCarFacade;
import com.tqmall.legend.facade.customer.vo.CarDetailVo;
import com.tqmall.legend.facade.customer.vo.CustomerCarVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.customercar.param.LegendCustomerCarRequest;
import com.tqmall.search.dubbo.client.legend.customercar.result.LegendCustomerCarDTO;
import com.tqmall.search.dubbo.client.legend.customercar.service.LegendCustomerCarSerivce;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by lilige on 16/4/10.
 */
@Slf4j
@Service
public class CustomerCarFacadeImpl implements CustomerCarFacade {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CustomerTagService customerTagService;
    @Autowired
    private RpcCustomerInfoService rpcCustomerInfoService;
    @Autowired
    private LegendCustomerCarSerivce legendCustomerCarSerivce;
    @Autowired
    private CustomerCarRelService customerCarRelService;
    @Autowired
    private OrderInfoService orderInfoService;

    @Override
    public CarDetailVo getCarInfo(Long carId, Long shopId) {
        // 查询车辆基本信息
        CarDetailVo carVo = getSimpleCar(carId , shopId);
        if (null == carVo){
            return null ;
        }
        // 获取客户标签信息
        List<CustomerTag> carTagList = customerTagService.getCustomerTagByCarId(shopId,carId);
        carVo.setCustomerTagList(carTagList);
        // 获取客户统计数据
        getCubeCustomerInfo(carId, shopId, carVo);
        return carVo;
    }

    @Override
    public CustomerCar getCustomerCarByLicense(String license, Long shopId) {
        CustomerCar customerCar = customerCarService.selectByLicenseAndShopId(license,shopId);
        return customerCar;
    }

    /**
     * TODO 目前接口只能取得维修次数，后续需要补上其它统计数据
     * 调用cube的dubbbo接口获取车辆统计信息
     *
     * @param carId
     * @param shopId
     */
    private void getCubeCustomerInfo(Long carId, Long shopId,CarDetailVo carVo) {
        try {
            com.tqmall.core.common.entity.Result<CustomerInfoDTO> result = rpcCustomerInfoService.getCustomerInfo(shopId, carId);
            if(result.isSuccess()){
                CustomerInfoDTO customerInfoDTO = result.getData();
                carVo.setValidOrderCount(customerInfoDTO.getTotalNumber());//消费次数
                carVo.setTotalOrderCount(customerInfoDTO.getArriveNumbers());//到店次数
                carVo.setRecent6MonthAmount(customerInfoDTO.getLastSixMonthTotalAmount());//最近6个月消费总额
                carVo.setSuspendPaymentCount(customerInfoDTO.getSignTotalOrderNumbers());//挂帐工单数
                carVo.setSuspendAmount(customerInfoDTO.getSignTotalAmount());//挂帐总金额
            }
        } catch (Exception e) {
            log.error("【dubbo】:调用cube获取客户统计信息异常",e);
        }
    }

    @Override
    public CarDetailVo getSimpleCar(Long carId, Long shopId) {
        CarDetailVo carVo = new CarDetailVo();
        Map<String,Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("id", carId);
        List<CustomerCar> customerCarList = customerCarService.select(param);
        if (CollectionUtils.isEmpty(customerCarList)){
            return null;
        }
        Long customerId = customerCarList.get(0).getCustomerId();
        Customer customer = customerService.selectById(customerId,shopId);
        carVo.setCustomerCar(customerCarList.get(0));
        carVo.setCustomer(customer);
        return carVo;
    }

    @Override
    public List<CarDetailVo> getSimpleCarByIds(List<Long> ids, Long shopId) {
        Map params = new HashMap();
        params.put("ids",ids);
        params.put("shopId",shopId);
        List<CustomerCar> carList = customerCarService.select(params);
        if (CollectionUtils.isEmpty(carList)){
            return null;
        }
        Set<Long> customerIds = new HashSet();
        for (CustomerCar car : carList){
            customerIds.add(car.getCustomerId());
        }
        Long[] cusIds = customerIds.toArray(new Long[customerIds.size()]);

        List<Customer> customerList = customerService.selectByIds(cusIds);

        Map<Long , Customer> customerMap = new HashMap<>();
        for (Customer customer : customerList){
            customerMap.put(customer.getId() , customer);
        }
        List<CarDetailVo> voList = new ArrayList();
        for (CustomerCar car : carList){
            CarDetailVo vo = new CarDetailVo();
            vo.setCustomerCar(car);
            Long customerId = car.getCustomerId();
            vo.setCustomer(customerMap.get(customerId));
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public CustomerCar getProxyCustomerCar(Long shopId,Long customerCarId, String license, String contactName, String contactMobile,UserInfo userInfo) {
        CustomerCar customerCar;
        Map carMap = new HashMap(2);
        carMap.put("shopId", shopId);
        carMap.put("license", license);
        List<CustomerCar> customerCarList = customerCarService.select(carMap);
        if(CollectionUtils.isEmpty(customerCarList)){
            //新增
            customerCar = new CustomerCar();
            customerCar.setLicense(license);
            //查询委托方车辆，同步数据到受托方门店
            CustomerCar proxyedCustomerCar = customerCarService.selectById(customerCarId);
            if(proxyedCustomerCar != null){
                BeanUtils.copyProperties(proxyedCustomerCar, customerCar);
                customerCar.setId(null);
                customerCar.setCustomerId(null);
                customerCar.setShopId(shopId);
                customerCar.setCustomerName("");
                customerCar.setMobile("");
                customerCar.setContact("");
                customerCar.setContactMobile("");
                customerCarService.addOrUpdate(userInfo,customerCar);
            }
        }else{
            //设置车辆
            customerCar = customerCarList.get(0);
        }
        return customerCar;
    }

    @Override
    public DefaultPage getCustomerCarsFromSearch(PageableRequest pageableRequest, LegendCustomerCarRequest customerCarRequest) {
        List<CustomerCarVo> customerCarVos = Lists.newArrayList();
        com.tqmall.search.common.result.Result<Page<LegendCustomerCarDTO>> pageResult = legendCustomerCarSerivce.queryCustomerCarList(customerCarRequest, pageableRequest);
        if(!pageResult.isSuccess() || pageResult.getData() == null || CollectionUtils.isEmpty(pageResult.getData().getContent())){
            log.info("通过searchKey= {},license= {},mobile= {},customerName= {},从搜索获取客户车辆信息失败",customerCarRequest.getSearchKey(),customerCarRequest.getLicense(),customerCarRequest.getMobile(),customerCarRequest.getCustomerName());
            return new DefaultPage<CustomerCarVo>(customerCarVos,new PageRequest(1,10),0L);
        }
        PageRequest pageRequest = new PageRequest(pageResult.getData().getNumber() < 1? 1:pageResult.getData().getNumber() - 1,pageResult.getData().getSize());
        List<LegendCustomerCarDTO> customerCarDTOs = (List<LegendCustomerCarDTO>)pageResult.getData().getContent();
        customerCarVos = BdUtil.do2bo4List(customerCarDTOs, CustomerCarVo.class);
        return new DefaultPage<CustomerCarVo>(customerCarVos,pageRequest,pageResult.getData().getTotalElements());
    }

    @Override
    public List<Map<String, Object>> getMapsFromSearch(String searchKey, String shopId, PageableRequest pageableRequest) {
        com.tqmall.search.common.result.Result<List<Map<String, Object>>> listResult = legendCustomerCarSerivce.searchKey(searchKey, shopId, pageableRequest);
        return listResult.getData();
    }

    @Override
    public Boolean checkVinIsExist(Long shopId, String vin, Long customerCarId) {
        if (StringUtils.isBlank(vin)) {
            return false;
        }
        if (customerCarId != null) {
            CustomerCar customerCar = customerCarService.selectById(customerCarId);
            if (customerCar != null && vin.equals(customerCar.getVin())) {
                //vin码未改
                return false;
            }
        }
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId",shopId);
        searchMap.put("vin",vin);
        List<CustomerCar> customerCarList = customerCarService.select(searchMap);
        if(CollectionUtils.isEmpty(customerCarList)){
            return false;
        }
        Boolean isExistVin = false;
        for(CustomerCar customerCar : customerCarList){
            Long id = customerCar.getId();
            if(customerCarId == null || Long.compare(id, customerCarId) != 0){
                isExistVin = true;
                break;
            }
        }
        return isExistVin;
    }

}
