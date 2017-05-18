package com.tqmall.manage.web.appoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.HttpUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.shop.ServiceGoodsSuiteService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.shop.PackageVo;
import com.tqmall.legend.entity.shop.ServiceGoodsSuite;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.web.common.BaseController;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 16/1/27.
 */
@Controller
@RequestMapping("appoint_create")
public class AppointController extends BaseController {

    private  static final Logger logger = LoggerFactory.getLogger(AppointController.class);

    @Autowired
    private AppointService appointService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerCarService customerCarService;

    @Autowired
    ShopServiceInfoService shopServiceInfoService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ShopServiceCateService shopServiceCateService;

    @Autowired
    ServiceGoodsSuiteService serviceGoodsSuiteService;

    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;

    @Value("${i.search.url}")
    private String iSearchUrl;

    /** create by dingbao 2016-01-26
     *  门店添加预约登记服务端接收数据
     *
     * @param appoint
     * @return
     */
    @RequestMapping(value = "addAppoint/ng",method = RequestMethod.POST)
    @ResponseBody
    public Result addAppointNg(@RequestBody Appoint appoint) {
        if (null == appoint) {
            return Result.wrapErrorResult("", "预约单信息不能为空");
        }
        String customerName = appoint.getCustomerName();
        String mobile = appoint.getMobile();
        String appointSn = appoint.getAppointSn();
        logger.info("新增预约单参数,联系人:{} 手机号:{} 预约单SN:{}", customerName, mobile, appointSn);
        //后台检验
        if (StringUtil.isStringEmpty(appointSn)) {
            return Result.wrapErrorResult("", "预约单号不能为空");
        } else if (StringUtil.isStringEmpty(customerName)) {
            return Result.wrapErrorResult("", "联系人不能为空");
        } else if (StringUtil.isStringEmpty(mobile)) {
            return Result.wrapErrorResult("", "联系电话不能为空");
        } else if (StringUtil.isStringEmpty(appoint.getAppointTimeFormat())) {
            return Result.wrapErrorResult("", "预约时间不能为空");
        } else if (appoint.getRegistrantId() == null) {
            return Result.wrapErrorResult("", "预约登记人不能为空");
        } else if (StringUtil.isStringEmpty(appoint.getAppointContent())) {
            return Result.wrapErrorResult("", "预约内容不能为空");
        }

        String appointServiceStr = appoint.getAppointServiceJson();//预约单的服务信息
        List<AppointServiceVo> appointServicesList = new ArrayList<>();
        if (StringUtil.isNotStringEmpty(appointServiceStr)) {
            //Json字符串转为对象
            JSONArray jsonArray = JSONArray.fromObject(appointServiceStr);
            appointServicesList = (List<AppointServiceVo>) JSONArray.toCollection(jsonArray, AppointServiceVo.class);
        }

        String license = appoint.getLicense();//车牌

        appoint.setCreator(0l);
        // 时间字符串转Date
        Date appointTime = DateUtil.convertStrToDate(appoint.getAppointTimeFormat(), DateUtil.DATE_FORMAT_YMDHM);
        appoint.setAppointTime(appointTime);

        Result result = appointService.addAppointAndService(appoint, appointServicesList);
        if (result.isSuccess()) {
            //customer_car表中预约统计信息修改
            try {
                if (StringUtils.isNotEmpty(license)) {
                    CustomerCar customerCar = customerCarService.selectById(appoint.getCustomerCarId());
                    if (null != customerCar) {
                        customerCar.setLatestAppoint(new Date());
                        customerCar.setAppointCout(customerCar.getAppointCout() + 1);
                        customerCar.setShopId(appoint.getShopId());
                        customerCar.setLicense(license);
                        customerCar.setId(customerCar.getId());
                        customerCarService.update(customerCar);
                        logger.info("更新客户车辆信息成功! customerId:" + customerCar.getId() + " carId:" +
                                customerCar.getCarId() + " license:" + customerCar.getLicense());
                    }
                }
            } catch (Exception e) {
                logger.info("更新客户车辆信息异常!{}", e);
            }
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }

    /**
     * legendm调用
     *
     * @param shopId
     * @param comLicense
     * @return
     */
    @RequestMapping("search/customerInfo")
    @ResponseBody
    public Object customerInfo(@RequestParam(value = "shopId",required = false) Long shopId,@RequestParam(value = "com_license",required = false) String comLicense) {

        Map<String, Object> params = new HashMap<String, Object>();
        if(shopId > 0){
            params.put("shopId", shopId);
        }
        params.put("licenseLike", comLicense);
        params.put("offset", 0);
        params.put("limit", 10);
        params.put("sorts", new String[]{"gmt_modified desc"});
        List<CustomerCar> list = customerService.searchCarList(params);
        return Result.wrapSuccessfulResult(list);
    }

    /**
     * get shopService recommendAppService
     * <p/>
     * TODO encapsulation common elasticService
     *
     * @param serviceSn   服务编号
     * @param serviceName 服务名称
     * @param type        服务类型 {1：基本服务；2：附加服务}
     * @param suiteNumLT  {0:单个服务;1:为带配件服务;2:服务大套餐}
     * @return json
     */
    @RequestMapping(value = "/json", method = RequestMethod.GET)
    @ResponseBody
    public String json(
            @RequestParam(value = "serviceSn", required = false) String serviceSn,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "suiteNumLT", required = false) String suiteNumLT,
            @RequestParam(value = "shopId",required = false) Long shopId) {
        String json = shopServiceInfoFacade.getShopService(serviceSn,serviceName,type,suiteNumLT,null,shopId);
        return json;
    }

    /**
     * 服务套餐
     *
     * @param serviceId
     * @return
     */
    @RequestMapping("getPackageByServiceId")
    @ResponseBody
    public Result getPackageByServiceId(Long serviceId,@RequestParam(value = "shopId",required = false)Long shopId) {
        ShopServiceInfo shopServiceInfo = shopServiceInfoService.selectById(serviceId, shopId);
        PackageVo packageVo = new PackageVo();

        if (shopServiceInfo != null) {

            if(!shopServiceInfo.getSuiteNum().equals(2L)){

                ShopServiceCate shopServiceCate = shopServiceCateService.selectById(shopServiceInfo
                        .getCategoryId());
                if (shopServiceCate != null) {
                    shopServiceInfo.setServiceCatName(shopServiceCate.getName());
                }
                shopServiceInfo.setServiceNum(1L);
                packageVo.setShopServiceInfo(shopServiceInfo);
                return Result.wrapSuccessfulResult(packageVo);
            }

            ServiceGoodsSuite serviceGoodsSuite = serviceGoodsSuiteService.selectByServiceId(
                    serviceId, shopId);
            if(serviceGoodsSuite == null){
                return Result.wrapErrorResult("","服务套餐不存在");
            }
            packageVo.setShopServiceInfo(shopServiceInfo);
            String goodsInfo = serviceGoodsSuite.getGoodsInfo();
            String serviceInfo = serviceGoodsSuite.getServiceInfo();

            List<Goods> goodsMapList = new Gson().fromJson(goodsInfo, new TypeToken<List<Goods>>() {
            }.getType());
            List<Goods> goodsList = new ArrayList<>();
            // 存放goodsId
            StringBuffer goodsIdSb = new StringBuffer();
            if (!CollectionUtils.isEmpty(goodsMapList)) {
                for (int i = 0; i < goodsMapList.size(); i++) {
                    Long goodsId = goodsMapList.get(i).getId();
                    goodsIdSb.append(goodsId).append(",");
                    Optional<Goods> goodsOptional = goodsService.selectById(goodsId, shopId);
                    if (goodsOptional.isPresent()) {
                        Goods goods = goodsOptional.get();
                        goods.setGoodsNum(goodsMapList.get(i).getGoodsNum());
                        goodsList.add(goods);
                    }
                }
                packageVo.setGoodsList(goodsList);
            }

            List<ShopServiceInfo> shopServiceInfoList = new ArrayList<>();

            if (shopServiceInfo.getType() == 1 && shopServiceInfo.getSuiteNum() == 1) {

                ShopServiceCate shopServiceCate = shopServiceCateService.selectById(shopServiceInfo
                        .getCategoryId());
                if (shopServiceCate != null) {
                    shopServiceInfo.setServiceCatName(shopServiceCate.getName());
                }
                shopServiceInfo.setServiceNum(1L);
                // 获得服务对应goodsId
                if (StringUtil.isNotStringEmpty(goodsIdSb.toString())) {
                    shopServiceInfo.setGoodsIdStr((goodsIdSb.deleteCharAt(goodsIdSb.length() - 1))
                            .toString());// 截掉最后一位逗号
                }
                shopServiceInfoList.add(shopServiceInfo);
                packageVo.setShopServiceInfoList(shopServiceInfoList);
                return Result.wrapSuccessfulResult(packageVo);

            } else if (shopServiceInfo.getType() == 1 && shopServiceInfo.getSuiteNum() == 2) {
                List<ShopServiceInfo> shopServiceMapList = new Gson().fromJson(serviceInfo,
                        new TypeToken<List<ShopServiceInfo>>() {
                        }.getType());
                if (!CollectionUtils.isEmpty(shopServiceMapList)) {
                    for (int j = 0; j < shopServiceMapList.size(); j++) {
                        Long shopServiceId = shopServiceMapList.get(j).getId();
                        ShopServiceInfo shopServiceInfo1 = shopServiceInfoService.selectById(
                                shopServiceId, shopId);
                        if (shopServiceInfo1 != null) {

                            // 获得服务对应的goodsId 为了前端删除服务的时候联动删除配件
                            ServiceGoodsSuite serviceGoodsSuite1 = serviceGoodsSuiteService
                                    .selectByServiceId(shopServiceId, shopId);
                            if (null != serviceGoodsSuite1) {
                                String goodsInfo1 = serviceGoodsSuite1.getGoodsInfo();
                                List<Goods> goodsMapList1 = new Gson().fromJson(goodsInfo1,
                                        new TypeToken<List<Goods>>() {
                                        }.getType());

                                if (!CollectionUtils.isEmpty(goodsMapList1)) {
                                    StringBuffer goodsIdSb1 = new StringBuffer();
                                    for (int i = 0; i < goodsMapList1.size(); i++) {
                                        Long goodsId1 = goodsMapList1.get(i).getId();
                                        goodsIdSb1.append(goodsId1).append(",");
                                    }
                                    if (StringUtil.isNotStringEmpty(goodsIdSb1.toString())) {
                                        shopServiceInfo1.setGoodsIdStr((goodsIdSb1
                                                .deleteCharAt(goodsIdSb1.length() - 1)).toString());
                                    }
                                }
                            }
                            if(shopServiceInfo1.getCategoryId() != null){
                                ShopServiceCate shopServiceCate1 = shopServiceCateService
                                        .selectById(shopServiceInfo1.getCategoryId());
                                if (shopServiceCate1 != null) {
                                    shopServiceInfo1.setServiceCatName(shopServiceCate1.getName());
                                }
                            }
                        }
                        if(shopServiceMapList.get(j) != null){
                            shopServiceInfo1.setServiceNum(shopServiceMapList.get(j).getServiceNum());
                        }
                        shopServiceInfoList.add(shopServiceInfo1);
                    }
                }
                packageVo.setShopServiceInfoList(shopServiceInfoList);
            }
            return Result.wrapSuccessfulResult(packageVo);
        }
        return Result.wrapErrorResult("", "服务不存在");
    }

    @RequestMapping("getCarByLicenseAndShopId")
    @ResponseBody
    public Result getCarByLicenseAndShopId(String carLicense,Long shopId) {
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
                int count = orderInfoService.selectCount(map);
                if (count > 0) {
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

    @RequestMapping(value = "insertCustomer", method = RequestMethod.POST)
    @ResponseBody
    public Result inertCustomer(@RequestBody CustomerCar customerCar) {

        // 数据有效性判断
        // 1、判断车牌号
        /*Result result = validateData(customerCar, true);
        if (!result.isSuccess()) {
            return result;
        }*/

        // 判断车牌是否存在
        String license = customerCar.getLicense().replace(" ", "").toUpperCase();
        /*Result check = checkLicense(license, "0");
        if (!check.isSuccess()) {
            return check;
        }*/
        customerCar.setLicense(license);
        Result resultCar = null;
        UserInfo userInfo = new UserInfo();
        userInfo.setShopId(customerCar.getShopId());
        userInfo.setUserId(0l);
        try {
            resultCar = customerCarService.add(customerCar, userInfo);

            // 如果新增客户车辆成功之后,更新预约单车辆内容
            if (resultCar.isSuccess()) {
                Long appointId = customerCar.getAppointId();
                // 正常流程
                if (null != appointId) {
                    Appoint appoint = appointService.selectById(appointId);
                    if (null != appoint) {
                        appoint.setLicense(customerCar.getLicense());
                        appoint.setCustomerCarId(customerCar.getId());
                        appoint.setCarBrandId(customerCar.getCarBrandId());
                        appoint.setCarBrandName(customerCar.getCarBrand());
                        appoint.setCarSeriesId(customerCar.getCarSeriesId());
                        appoint.setCarSeriesName(customerCar.getCarSeries());
                        if (null != customerCar.getContact() && !"".equals(customerCar.getContact())) {
                            appoint.setCustomerName(customerCar.getContact());
                        }
                        if (null != customerCar.getContactMobile()
                                && !"".equals(customerCar.getContactMobile())) {
                            appoint.setMobile(customerCar.getContactMobile());
                        }
                        appoint.setCustomerCarId(customerCar.getId());
                        appointService.update(appoint);
                        // 预约单用户新增之后,更新customerCar中appointCount的预约次数
                        updateCustomerCarAppointCnt(customerCar.getId());

                    }
                } else {
                    // 有可能在做预约单的时候,从别的地方新增客户,所有新增客户之后要根据车牌回填更新appoint的customerCarId
                    Long shopId = customerCar.getShopId();
                    Map map = new HashMap(2);
                    map.put("shopId", shopId);
                    map.put("license", customerCar.getLicense());
                    List<Appoint> appointList = appointService.select(map);
                    if (!CollectionUtils.isEmpty(appointList)) {
                        for (Appoint appoint : appointList) {
                            Long customerCarId = appoint.getCustomerCarId();
                            if (null == customerCarId || 0l == customerCarId) {

                                appoint.setLicense(customerCar.getLicense());
                                appoint.setCustomerCarId(customerCar.getId());
                                appoint.setCarBrandId(customerCar.getCarBrandId());
                                appoint.setCarBrandName(customerCar.getCarBrand());
                                appoint.setCarSeriesId(customerCar.getCarSeriesId());
                                appoint.setCarSeriesName(customerCar.getCarSeries());
                                if (null != customerCar.getContact()
                                        && !"".equals(customerCar.getContact())) {
                                    appoint.setCustomerName(customerCar.getContact());
                                }
                                if (null != customerCar.getContactMobile()
                                        && !"".equals(customerCar.getContactMobile())) {
                                    appoint.setMobile(customerCar.getContactMobile());
                                }
                                appoint.setCustomerCarId(customerCar.getId());
                                appointService.update(appoint);
                            }
                        }
                        // 预约单用户新增之后,更新customerCar中appointCount的预约次数
                        updateCustomerCarAppointCnt(customerCar.getId());
                    }
                }
            }

        } catch (Exception e) {
            logger.error("新增车辆信息异常", e);
            return Result.wrapErrorResult("", "新增车辆信息异常");
        }
        return resultCar;
    }

    /**
     * create by jason zheng 2015-09-06 预约单用户新增之后,更新customerCar中appointCount的预约次数
     *
     */
    private void updateCustomerCarAppointCnt(Long customerCarId) {
        // 更新customerCar预约信息
        CustomerCar car = customerCarService.selectById(customerCarId);
        if (null != car) {
            car.setAppointCout(car.getAppointCout() + 1);// 预约单入口新增客户的时候,appointCount默认+1
            car.setLatestAppoint(new Date());// 更新下预约时间
            customerCarService.update(car);

        }
    }

    /**
     * @param license
     * @param id
     * @return
     */
    public Result checkLicense(String license, String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("license", license.replace(" ", "").toUpperCase());
        params.put("shopId", UserUtils.getShopIdForSession(request));
        List<CustomerCar> list = customerCarService.select(params);
        if (!CollectionUtils.isEmpty(list)) {
            if (!StringUtils.isBlank(id) && !StringUtils.equals(list.get(0).getId() + "", id)) {
                return Result.wrapErrorResult("", "车牌已存在，请勿重复添加！");
            }

        }
        return Result.wrapSuccessfulResult(true);

    }
}
