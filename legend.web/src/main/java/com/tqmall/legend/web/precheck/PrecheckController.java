package com.tqmall.legend.web.precheck;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.customer.LicenseMatchCityService;
import com.tqmall.legend.biz.insurance.InsuranceCompanyService;
import com.tqmall.legend.biz.precheck.PrechecksService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Constants;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.precheck.PrecheckDetails;
import com.tqmall.legend.entity.precheck.PrecheckItemTypeEnum;
import com.tqmall.legend.entity.precheck.PrecheckRequest;
import com.tqmall.legend.entity.precheck.Prechecks;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.precheck.PrechecksFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sven.zhang on 2016/4/7.
 */
@Controller
@Slf4j
@RequestMapping(value = "/shop/precheck")
public class PrecheckController extends BaseController {
    @Autowired
    private PrechecksService prechecksService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private InsuranceCompanyService insuranceCompanyService;
    @Autowired
    private LicenseMatchCityService licenseMatchCityService;
    @Autowired
    private PrechecksFacade prechecksFacade;

    /**
     * 预检单初始化
     *
     * @param id      车辆id  若存在，则带入车辆信息填充页面
     * @param model
     * @param request
     * @return
     */

    @RequestMapping(value = "/precheck", method = RequestMethod.GET)
    public String showPrecheck(@RequestParam(value = "cid", required = false) Long id,
                               Model model, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        initModelAttrs(model, shopId);
        String license = licenseMatchCityService.getLicenseCityByShopId(shopId);
        model.addAttribute("license", license);
        if (id != null && id > 0) {
            getCustomerCarInfo(model, id);
        }
        return "yqx/page/precheck/precheck";
    }

    /**
     * 编辑之前填充数据
     *
     * @param id    预检单ID
     * @param model
     * @return
     */
    @RequestMapping(value = "before-edit", method = RequestMethod.GET)
    public String preEdit(@RequestParam(value = "id", required = false) Long id,
                          Model model) {

        Long shopId = UserUtils.getShopIdForSession(request);
        initModelAttrs(model, shopId);
        Map<String, Object> params = new HashMap<>();
        //根据预检单Id,获得车辆订单的header和详情
        params.put("id", id);
        params.put("shopId", shopId);
        List<Prechecks> prechecks = prechecksService.getAPrecheck(params);
        Prechecks precheck;
        if (!CollectionUtils.isEmpty(prechecks)) {
            precheck = prechecks.get(0);
            precheck.setGmtCreateStr(DateUtil.convertDate(precheck.getGmtCreate()));
            precheck.setDueDateStr(DateUtil.convertDateToYMD(precheck.getDueDate()));
            if (null != precheck.getNextTime()) {
                precheck.setNextTime(precheck.getNextTime().substring(0, 10).trim());
            }
            getCustomerCarInfo(model, precheck.getCustomerCarId());
            model.addAttribute("act", "edit");
            model.addAttribute("precheckHead", precheck);
            params.remove("id");
            params.put("precheckId", id);
            List<PrecheckDetails> precheckDetailsList = prechecksService.getListedPrecheckDetails(params);
            List<PrecheckRequest> precheckRequestList = prechecksService.getListedPrecheckRequests(params);
            if (!CollectionUtils.isEmpty(precheckRequestList)) {
                model.addAttribute("requestList", precheckRequestList);
            }
            countData(model, precheckDetailsList);
        }
        return "yqx/page/precheck/precheck";
    }


    /**
     * 新增预检单
     *
     * @param precheckHead        预检单头部信息
     * @param appearance          外观检测
     * @param customerRequest     客户需求
     * @param goodsInCar          随车物品
     * @param precheckOther       存入预检单的数据预计耗费工时，预计费用
     * @param precheckOtherDetail 其他要存入预检单详情的数据
     * @return
     */
    @RequestMapping(value = "/precheck-save", method = RequestMethod.POST)
    @ResponseBody
    public Result precheckAdd(@RequestParam("precheckHead") String precheckHead,
                              @RequestParam("appearance") String appearance,
                              @RequestParam("customerRequest") String customerRequest,
                              @RequestParam("goodsInCar") String goodsInCar,
                              @RequestParam("precheckOther") String precheckOther,
                              @RequestParam("precheckOtherDetail") String precheckOtherDetail) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        String userName = userInfo.getName();
        Prechecks precheck = new Prechecks();
        precheck.setShopId(shopId);
        Map<String, Object> map;
        try {
            ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            precheck = mapper.readValue(precheckHead, Prechecks.class);
            //预检单登记人
            precheck.setCreator(userId);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> precheckMap = objectMapper.readValue(precheckOther, new TypeReference<Map<String, String>>() {
            });
            if (!CollectionUtils.isEmpty(precheckMap)) {
                precheck.setManHour(precheckMap.get("manHour"));
                if (precheckMap.get("expFee") != null) {
                    precheck.setExpFee(BigDecimal.valueOf(Double.parseDouble(precheckMap.get("expFee"))));
                }
            }
            dataValidation(precheck);
            map = prechecksFacade.addAndUpdateShare(appearance, precheckOtherDetail, goodsInCar, customerRequest, shopId);
        } catch (IOException e) {
            log.error("新增预检单数据转换失败!\n预检单信息:{}c\n错误信息:{}" + precheckHead, e);
            return Result.wrapErrorResult("", "获取预检单信息失败!");
        }
        //根据车牌号码获得车辆信息的id
        Map<String, Object> params = new HashMap<>();
        params.put("license", precheck.getPlateNumber());
        params.put("shopId", shopId);
        List<CustomerCar> carList = customerCarService.select(params);
        CustomerCar carnew;
        if (!CollectionUtils.isEmpty(carList))
            carnew = carList.get(0);
        else {
            log.error("用户{}新增预检单详情失败!客户车辆不存在", userName);
            return Result.wrapErrorResult(LegendError.COMMON_EMPTY_LIST);
        }

        //无论前台填入的用户和手机码是哪个, 此处均会填入根据车牌号码查出来的客户数据
        precheck.setShopId(shopId);
        precheck.setCustomerCarId(carnew.getId());
        precheck.setInsurance(carnew.getInsuranceCompany());
        precheck.setInsuranceId(carnew.getInsuranceId());
        precheck.setCustomerId(carnew.getCustomerId());
        precheck.setColor(carnew.getColor());


        try {
            prechecksService.addNewPrecheckOrder(precheck, (List<PrecheckDetails>) map.get("precheckDetailsList"), (List<PrecheckRequest>) map.get("precheckRequestList"), userInfo);
        } catch (Exception e) {
            log.error("用户{}新增预检单失败!错误原因", userName, e);
            return Result.wrapErrorResult("", "预检单数据保存到数据库失败!");
        }
        return Result.wrapSuccessfulResult(precheck.getId());
    }


    /**
     * 编辑预检单
     *
     * @param precheckHead        预检单头部信息
     * @param appearance          外观检测
     * @param customerRequest     客户需求
     * @param goodsInCar          随车物品
     * @param precheckOther       存入预检单的数据预计耗费工时，预计费用
     * @param precheckOtherDetail 其他要存入预检单详情的数据
     * @return
     */
    @RequestMapping(value = "/precheck-update", method = RequestMethod.POST)
    @ResponseBody
    public Result precheckUpdate(@RequestParam("precheckHead") String precheckHead,
                                 @RequestParam("appearance") String appearance,
                                 @RequestParam("customerRequest") String customerRequest,
                                 @RequestParam("goodsInCar") String goodsInCar,
                                 @RequestParam("precheckOther") String precheckOther,
                                 @RequestParam("precheckOtherDetail") String precheckOtherDetail) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Prechecks precheck = new Prechecks();
        Map<String, Object> map = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            precheck = mapper.readValue(precheckHead, Prechecks.class);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> precheckMap = objectMapper.readValue(precheckOther, new TypeReference<Map<String, String>>() {
            });

            if (!CollectionUtils.isEmpty(precheckMap)) {
                precheck.setManHour(precheckMap.get("manHour"));
                if (precheckMap.get("expFee") != null) {
                    precheck.setExpFee(BigDecimal.valueOf(Double.parseDouble(precheckMap.get("expFee"))));
                }

            }
            dataValidation(precheck);
            map = prechecksFacade.addAndUpdateShare(appearance, precheckOtherDetail, goodsInCar, customerRequest, shopId);
        } catch (IOException e) {
            log.error("预检单数据转换失败!\n预检单信息:{}错误信息:{}", precheckHead, e);
        }
        precheck.setShopId(shopId);
        try {
            prechecksService.updatePrecheckOrder(precheck, (List<PrecheckDetails>) map.get("precheckDetailsList"), (List<PrecheckRequest>) map.get("precheckRequestList"));
        } catch (Exception e) {
            log.error("更新预检单数据失败!\n预检单编号:{},错误信息:{}", precheck.getId(), e);
        }

        return Result.wrapSuccessfulResult(precheck.getId());
    }

    /**
     * 删除预检单
     *
     * @param precheckId 预检单ID
     * @return
     */
    @RequestMapping(value = "/precheck-delete", method = RequestMethod.POST)
    @ResponseBody
    public Result precheckDelete(@RequestParam("precheckId") Long precheckId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        log.info("用户{}删除id为{}的预检单", userInfo.getName(), precheckId);
        Boolean flag = prechecksService.deletePrechecks(precheckId, userInfo);
        if (!flag) {
            log.warn("删除id为{}的预检单失败!", precheckId);
            return Result.wrapErrorResult(LegendError.PRECHECK_DEL_ERROR);
        }
        return Result.wrapSuccessfulResult("删除预检单成功!");
    }

    /**
     * 预检单打印
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "precheck-print", method = RequestMethod.GET)
    public String precheckPrint(@RequestParam(value = "id", required = false) Long id,
                                Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        if (shopId != null && shopId > 0) {
            Shop shop = shopService.selectById(shopId);
            model.addAttribute("shop", shop);
        }
        initModelAttrs(model, shopId);

        Map<String, Object> params = new HashMap<>();
        //根据预检单Id,获得车辆订单的header和详情

        params.put("id", id);
        params.put("shopId", shopId);
        List<Prechecks> prechecks = prechecksService.getAPrecheck(params);
        Prechecks precheck;
        if (!CollectionUtils.isEmpty(prechecks)) {
            precheck = prechecks.get(0);
            precheck.setGmtCreateStr(DateUtil.convertDate(precheck.getGmtCreate()));
            precheck.setDueDateStr(DateUtil.convertDateToYMD(precheck.getDueDate()));
            if (null != precheck.getNextTime()) {
                precheck.setNextTime(precheck.getNextTime().substring(0, 10).trim());
            }
            model.addAttribute("precheckHead", precheck);
        } else {
            return "print/precheck_print_blank";
        }
        //根据shopid和车辆预检信息获得开单人信息
        ShopManager shopManager =
                shopManagerService.selectByShopIdAndManagerId(shopId, precheck.getCreator());
        if (null != shopManager) {
            model.addAttribute("shopManager", shopManager);
        }
        //获得customer_car_信息
        CustomerCar customerCar =
                customerCarService.selectById(precheck.getCustomerCarId());
        if (null != customerCar) {
            precheck.setPlateNumber(customerCar.getLicense());
        }
        params.clear();
        params.put("shopId", shopId);
        params.put("precheckId", id);
        log.info("打印预检单编号{},获取预检单详情", id);
        List<PrecheckDetails> precheckDetailsList =
                prechecksService.getListedPrecheckDetails(params);
        log.info("打印预检单编号{}获取用户检测需求", id);
        //设置预检信息
        countData(model, precheckDetailsList);
        if (!CollectionUtils.isEmpty(precheckDetailsList)) {
            for (PrecheckDetails tmp : precheckDetailsList) {
                model.addAttribute(Constants.ftlItemMapping.get(tmp.getPrecheckItemId()), tmp);
            }
        }
        List<PrecheckRequest> precheckRequestList = prechecksService.getListedPrecheckRequests(params);
        model.addAttribute("requestList", precheckRequestList);
        params.clear();
        params.put("shopId", shopId);

        params.put("license", customerCar.getLicense());
        log.info("打印预检单获取车信息，车牌:{}", customerCar.getLicense());
        List<CustomerCar> carList = customerService.getCustomerCarList(params);
        if (!CollectionUtils.isEmpty(carList)) {
            model.addAttribute("customerInfo", carList.get(0));
        }
        //获得联系人信息
        params.remove("license");

        params.put("customerId", customerCar.getCustomerId());
        log.info("打印预检单获取联系人，车ID:{}", customerCar.getId());
        List<Customer> contactsList = customerService.getCustomerByCarId(params);
        if (!CollectionUtils.isEmpty(contactsList)) {
            model.addAttribute("contacts", contactsList.get(0));
        }
        return "yqx/page/precheck/precheck-print";
    }

    /**
     * 预检单详情（只删除预检单记录）
     *
     * @param id    预检单ID
     * @param model
     * @return
     */
    @RequestMapping(value = "/precheck-detail", method = RequestMethod.GET)
    public String precheckDetail(@RequestParam(value = "id", required = false) Long id, Model model) {

        Long shopId = UserUtils.getShopIdForSession(request);
        Map<Long, Map<String, String>> precheckItems = prechecksFacade.getPrecheckItemsMap();
        model.addAttribute("precheckItems", precheckItems);
        initModelAttrs(model, shopId);
        Map<String, Object> params = new HashMap<>();
        //根据预检单Id,获得车辆订单的header和详情
        params.put("id", id);
        params.put("shopId", shopId);
        List<Prechecks> prechecks = prechecksService.getAPrecheck(params);
        Prechecks precheck;
        if (!CollectionUtils.isEmpty(prechecks)) {
            precheck = prechecks.get(0);
            precheck.setGmtCreateStr(DateUtil.convertDate(precheck.getGmtCreate()));
            precheck.setDueDateStr(DateUtil.convertDateToYMD(precheck.getDueDate()));
            if (null != precheck.getNextTime()) {
                precheck.setNextTime(precheck.getNextTime().substring(0, 10).trim());
            }
            CustomerCar customerCar = customerCarService.selectById(precheck.getCustomerCarId());
            model.addAttribute("precheck", precheck);
            model.addAttribute("customerCar", customerCar);
            ShopManager shopManager =
                    shopManagerService.selectByShopIdAndManagerId(shopId, precheck.getCreator());
            if (null != shopManager) {
                model.addAttribute("shopManager", shopManager);
            }
            params.clear();
            params.put("shopId", shopId);
            params.put("precheckId", id);
            List<PrecheckDetails> precheckDetailsList = prechecksService.getListedPrecheckDetails(params);
            List<PrecheckRequest> precheckRequestList = prechecksService.getListedPrecheckRequests(params);
            model.addAttribute("requestList", precheckRequestList);
            countData(model, precheckDetailsList);
        }
        return "yqx/page/precheck/precheck-detail";
    }

    private void initModelAttrs(Model model, Long shopId) {
        model.addAttribute("insuranceValues", insuranceCompanyService.select(null));
        //外观检测, 油表油量
        model.addAttribute("outlineValues", prechecksService.getItemValuesByType(PrecheckItemTypeEnum.OUTLINECODE.getIndex()));
        model.addAttribute("oilMeterValues", prechecksService.getItemValuesByType(PrecheckItemTypeEnum.OILCODE.getIndex()));
        model.addAttribute("moduleUrl", "reception");

    }

    /**
     * 对不同预检类型的统计
     */
    private void countData(Model model, List<PrecheckDetails> precheckDetailsList) {
        Integer outlineCount = 0;
        Integer tyreCount = 0;
        Integer otherCount = 0;
        Integer carFacilityCount = 0;
        Integer engineCount = 0;
        Integer meterCount = 0;
        Integer chassisCount = 0;
        Integer oilCount = 0;
        for (PrecheckDetails tmp : precheckDetailsList) {
            if (PrecheckItemTypeEnum.OUTLINECODE.getIndex().equals(tmp.getPrecheckItemType())) {
                outlineCount++;
            } else if (PrecheckItemTypeEnum.TYRECODE.getIndex().equals(tmp.getPrecheckItemType())) {
                tyreCount++;
            } else if (PrecheckItemTypeEnum.OTHERCODE.getIndex().equals(tmp.getPrecheckItemType())) {
                otherCount++;
            } else if (PrecheckItemTypeEnum.CARFACILITY.getIndex().equals(tmp.getPrecheckItemType())) {
                carFacilityCount++;
            } else if (PrecheckItemTypeEnum.METERCODE.getIndex().equals(tmp.getPrecheckItemType())) {
                meterCount++;
            } else if (PrecheckItemTypeEnum.ENGINECODE.getIndex().equals(tmp.getPrecheckItemType())) {
                engineCount++;
            } else if (PrecheckItemTypeEnum.CHASSISCODE.getIndex().equals(tmp.getPrecheckItemType())) {
                chassisCount++;
            } else if (PrecheckItemTypeEnum.OILCODE.getIndex().equals(tmp.getPrecheckItemType())) {
                oilCount++;
            }
            tmp.setFtlId(Constants.ftlItemMapping.get(tmp.getPrecheckItemId()));
        }
        model.addAttribute("precheckDetailsList", precheckDetailsList);
        model.addAttribute("appearanceCount", outlineCount);
        model.addAttribute("tyreCount", tyreCount);
        model.addAttribute("otherCount", otherCount);
        model.addAttribute("carFacilityCount", carFacilityCount);
        model.addAttribute("meterCount", meterCount);
        model.addAttribute("engineCount", engineCount);
        model.addAttribute("chassisCount", chassisCount);
        model.addAttribute("oilCount", oilCount);
    }

    /**
     * 获取车辆信息，联系人信息
     */
    public void getCustomerCarInfo(Model model, Long id) {
        if (id != null) {
            CustomerCar selectedCar = customerCarService.selectById(id);
            Customer customer = customerService.selectById(selectedCar.getCustomerId());
            if (customer != null) {
                //若联系人，联系电话不存在则取车主，车主电话
                if (StringUtil.isNotStringEmpty(customer.getContact())) {
                    selectedCar.setCustomerName(customer.getContact());
                } else {
                    selectedCar.setCustomerName(customer.getCustomerName());
                }
                if (StringUtil.isNotStringEmpty(customer.getContactMobile())) {
                    selectedCar.setMobile(customer.getContactMobile());
                } else {
                    selectedCar.setMobile(customer.getMobile());
                }

            }
            model.addAttribute("customerCar", selectedCar);
        }
    }

    /**
     * 预检单数据校验
     */
    public Result dataValidation(Prechecks precheck) {
        Boolean flag;
        flag = StringUtil.isMobileNO(precheck.getMobile());
        if (!flag) {
            return Result.wrapErrorResult("", "手机号码无效");
        }
        flag = StringUtil.checkCarLicense(precheck.getPlateNumber());
        if (!flag) {
            return Result.wrapErrorResult("", "车牌无效");
        }
        return null;
    }

    @RequestMapping("clear")
    @ResponseBody
    public Result clearPrecheckRedis(){
       return Result.wrapSuccessfulResult(prechecksFacade.clearPrecheckRedis());
    }
}
