package com.tqmall.legend.web.customer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.athena.client.car.CarCategoryService;
import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.*;
import com.tqmall.legend.biz.insurance.InsuranceCompanyService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.precheck.PrechecksService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarFile;
import com.tqmall.legend.entity.customer.LicenseCity;
import com.tqmall.legend.entity.customer.SearchVo;
import com.tqmall.legend.entity.insurance.InsuranceCompany;
import com.tqmall.legend.entity.precheck.PrecheckDetailsVO;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.tqcheck.TqCheckLog;
import com.tqmall.legend.enums.activity.CarDetailReferEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.customer.CustomerCarFacade;
import com.tqmall.legend.facade.customer.vo.CarDetailVo;
import com.tqmall.legend.facade.customer.vo.CustomerCarVo;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.object.result.tqcheck.TqCheckDetailListDTO;
import com.tqmall.legend.service.tqcheck.RpcTqCheckService;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.customer.vo.CustomerTagVo;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.customercar.param.LegendCustomerCarRequest;
import com.tqmall.wheel.component.excel.export.DefaultExcelExportor;
import com.tqmall.wheel.exception.WheelException;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by litan on 14-11-3.
 */
@Controller
@RequestMapping("shop/customer")
@Slf4j
public class CustomerController extends BaseController {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerCarService customerCarService;

    @Autowired
    PrechecksService prechecksService;

    @Autowired
    ShopService shopService;

    @Autowired
    ShopManagerService shopManagerService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    private InsuranceCompanyService insuranceCompanyService;

    @Autowired
    private AppointService appointService;

    @Autowired
    private LicenseMatchCityService licenseMatchCityService;

    @Autowired
    private CarCategoryService carCategoryService;

    @Autowired
    private CustomerCarFacade customerCarFacade;

    @Autowired
    private CustomerCarFileService customerCarFileService;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private RpcTqCheckService rpcTqCheckService;
    @Autowired
    private CustomerTagService customerTagService;

    /**
     * 跳转到客户编辑页面
     *
     * @param id
     * @return
     */
    @HttpRequestLog
    @RequestMapping("edit")
    public String edit(Model model, Long id,  @RequestParam(value="license",required = false)String license,String refer) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        UserInfo userInfo = UserUtils.getUserInfo(request);
        //如果是预约单来源，要在客户编辑页面添加返回按钮
        if (StringUtils.isNotBlank(refer)) {
            model.addAttribute("refer",refer);
        }

        //如果传递车牌
        if(StringUtils.isBlank(license)) {
            license = licenseMatchCityService.getLicenseCityByShopId(userInfo.getShopId());
        }
        model.addAttribute("license", license);
        //新增
        if(null == id){
            return "yqx/page/customer/edit";
        }
        //编辑
        CarDetailVo carDetailVo = customerCarFacade.getSimpleCar(id,userInfo.getShopId());
        if (null == carDetailVo){
            return "redirect:edit";
        }
        model.addAttribute("carDetailVo", carDetailVo);
        model.addAttribute("license", carDetailVo.getCustomerCar().getLicense());
        return "yqx/page/customer/edit";
    }

    @HttpRequestLog
    @RequestMapping("list")
    public String list(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        return "yqx/page/customer/list";
    }

    /**
     * @param model
     * @param id
     * @param refer
     * @return
     */
    @RequestMapping("info")
    public String getCustomerInfo(Model model, @RequestParam(value="cid", required = true)Long id,
                                  @RequestParam(value="refer", required = false)String refer) {
        return this.getCarDetail(model, id,refer);
    }



    @RequestMapping("search/list")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<DefaultPage> list(@PageableDefault(page = 0, value = 12, sort = "gmtModified", direction = Sort.Direction.DESC) final Pageable pageable) {
        return new ApiTemplate<DefaultPage>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected DefaultPage process() throws BizException {
                Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
                Long shopId = UserUtils.getShopIdForSession(request);
                searchParams.put("shopId", shopId);
                LegendCustomerCarRequest customerCarRequest = new LegendCustomerCarRequest();
                int page = pageable.getPageNumber() - 1;
                PageableRequest pageableRequest = new PageableRequest(page,pageable.getPageSize());
                customerCarRequest.setShopId(shopId.toString());
                // 是否来自公共搜索
                buildCommonSearch(searchParams, customerCarRequest);
                DefaultPage defaultPage = customerCarFacade.getCustomerCarsFromSearch(pageableRequest,customerCarRequest);
                return defaultPage;
            }
        }.execute();

    }

    private void buildCommonSearch(Map<String, Object> searchParams,LegendCustomerCarRequest customerCarRequest) {
        if(searchParams.containsKey("searchKey")){
            customerCarRequest.setSearchKey(searchParams.get("searchKey").toString());
        }
        if(searchParams.containsKey("license")){
            customerCarRequest.setLicense(searchParams.get("license").toString());
        }
        if(searchParams.containsKey("licenseNot")){
            customerCarRequest.setLicenseNot(searchParams.get("licenseNot").toString());
        }
        if(searchParams.containsKey("mobile")){
            customerCarRequest.setMobile(searchParams.get("mobile").toString());
        }
        if(searchParams.containsKey("customerName")){
            customerCarRequest.setCustomerName(searchParams.get("customerName").toString());
        }
        if(searchParams.containsKey("company")){
            customerCarRequest.setCompany(searchParams.get("company").toString());
        }
        if(searchParams.containsKey("vin")){
            customerCarRequest.setVin(searchParams.get("vin").toString());
        }
        if(searchParams.containsKey("carInfo")){
            customerCarRequest.setCarInfo(searchParams.get("carInfo").toString());
        }
        if(searchParams.containsKey("contact")){
            customerCarRequest.setContact(searchParams.get("contact").toString());
        }

    }


    /**
     * 车辆搜索接口--用的地方
     * 新建：洗车单、快修快保、综合维修、销售、预检单、预约单、会员卡,计次卡
     *
     * @return
     */
    @RequestMapping("search/mobile")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<CustomerCarVo>> mobile(@PageableDefault(page = 1, value = 10, sort = "gmtModified", direction = Sort.Direction.DESC) final Pageable pageable) {
        return new ApiTemplate<List<CustomerCarVo>>() {
            String license = request.getParameter("com_license");
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected List process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                LegendCustomerCarRequest customerCarRequest = new LegendCustomerCarRequest();
                customerCarRequest.setShopId(shopId.toString());
                customerCarRequest.setLicense(license);
                int page = pageable.getPageNumber() - 1;
                PageableRequest pageableRequest = new PageableRequest(page,pageable.getPageSize());
                DefaultPage defaultPage = customerCarFacade.getCustomerCarsFromSearch(pageableRequest,customerCarRequest);
                return defaultPage.getContent();
            }
        }.execute();
    }

    @RequestMapping("search/key")
    @ResponseBody
    public Object key(@PageableDefault(page = 0, value = 12, sort = "gmtModified", direction = Sort.Direction.DESC) final Pageable pageable) {
        return new ApiTemplate<List>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List process() throws BizException {
                String key = request.getParameter("com_license");
                String shopId = UserUtils.getShopIdForSession(request).toString();
                PageableRequest pageableRequest = new PageableRequest(pageable);
                List<Map<String,Object>> resultMap = customerCarFacade.getMapsFromSearch(key, shopId, pageableRequest);
                if (!CollectionUtils.isEmpty(resultMap)){
                    List<SearchVo> value = Lists.newArrayList();
                    for (Map<String, Object> m : resultMap) {
                        SearchVo search = new SearchVo();
                        search.setId(Long.parseLong(m.get("id").toString()));
                        search.setSearchValue(m.get("searchValue").toString());
                        value.add(search);
                    }
                    return value;
                }
                return resultMap;
            }
        }.execute();


    }

    /**
     * 根据公司名称查询
     *
     * @param company
     * @return
     */
    @RequestMapping("search/company")
    @ResponseBody
    public Result company(String company) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Map searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("company", company);
        List<Customer> list = customerService.searchCompanyList(searchMap);
        return Result.wrapSuccessfulResult(list);
    }

    @HttpRequestLog
    @RequestMapping("search/common")
    public String common(HttpServletRequest request, Model model) {

        CustomerCar customerCar = new CustomerCar();
        customerCar.setLicense(request.getParameter("keyword") == null ? "" : request.getParameter(
                "keyword").toString());
        model.addAttribute("customerCar", customerCar);
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        return "yqx/page/customer/list";
    }


    @RequestMapping("carInfo")
    @ResponseBody
    public Object getCustomerCarInfo(
            @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {

        // 查询车辆预检列表
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        DefaultPage<PrecheckDetailsVO> page = (DefaultPage<PrecheckDetailsVO>) prechecksService
                .getPrecheckDetailsList(pageable, searchParams);
        page.setPageUri(request.getRequestURI());
        page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(page);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Result updateCustomerInfo(CustomerCar customerCar, HttpServletRequest request) {
        // 数据有效性判断
        // 1、判断车牌号
        Result result = validateData(customerCar, false);
        if (!result.isSuccess()) {
            return result;
        }
        // 判断车牌是否存在
        String license = customerCar.getLicense().replace(" ", "").toUpperCase();
        Result check = checkLicense(license, "" + customerCar.getId());
        if (!check.isSuccess()) {
            return check;
        }
        customerCar.setLicense(license);
        // vin码大写
        String vin = customerCar.getVin();
        if(StringUtils.isNotBlank(vin)){
            vin = vin.toUpperCase();
            customerCar.setVin(vin);
            // 判断vin码是否存在
            Long shopId = UserUtils.getShopIdForSession(request);
            Boolean bool = customerCarFacade.checkVinIsExist(shopId, vin,customerCar.getId());
            if(bool){
                return Result.wrapErrorResult("","vin码已存在，请勿重复添加！");
            }
        }
        return customerCarService.update(customerCar, UserUtils.getUserInfo(request));
    }

    /**
     * 对外调整数据库的接口（填充数据）
     * @return
     */
    @RequestMapping(value = "putCarLevelToDB")
    @ResponseBody
    public Result carLevelData()
    {
        Result result = null;
        try {
            long sTime = System.currentTimeMillis();
            result = customerCarService.flushCarLevelToDB();
            log.info("初始化车辆级别耗时:{}",System.currentTimeMillis() - sTime);
        } catch (Exception e) {
            log.error("初始化车辆级别失败.",e);
            result = Result.wrapErrorResult("","初始化车辆级别信息失败.");
        }
        return result;
    }


    /**
     * 对外调整数据库的接口（填充数据）
     * @return
     */
    @RequestMapping(value = "putCarTypeToDB")
    @ResponseBody
    public Result carTypeData()
    {
        Result result = null;
        try {
            long sTime = System.currentTimeMillis();
            result = customerCarService.flushCarTypeToDB();
            log.info("初始化车辆类型耗时:{}",System.currentTimeMillis() - sTime);
        } catch (Exception e) {
            log.error("初始化车辆类型失败.",e);
            result = Result.wrapErrorResult("","初始化车辆类型信息失败.");
        }
        return result;
    }

    @RequestMapping("check_mobile")
    @ResponseBody
    public Result checkMobile(String mobile){
        if (StringUtils.isEmpty(mobile)){
            return Result.wrapSuccessfulResult("手机号码不存在");
        }
        List<Customer> customer = customerService.getCustomerByMobile(mobile, UserUtils.getShopIdForSession(request));
        if (CollectionUtils.isEmpty(customer)){
            return Result.wrapSuccessfulResult(true);
        }
        return Result.wrapErrorResult("", "号码已存在");
    }

    /**
     * 更换车主
     * 如果车主电话已存在,直接更新到车主下
     * 如果车主电话不存在,新建车主,并绑定到车主下
     * @param mobile
     * @param carId
     * @return
     */
    @RequestMapping("change_customer")
    @ResponseBody
    public Result changeMobile(String mobile,Long carId){
        UserInfo userInfo = UserUtils.getUserInfo(request);
        return customerCarService.changeCustomer(mobile,userInfo,carId);
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    public Result addCustomerInfo ( CustomerCar customerCar, HttpServletRequest request){

        // 数据有效性判断
        // 1、判断车牌号
        Result result = validateData(customerCar, true);
        if (!result.isSuccess()) {
            return result;
        }

        // 判断车牌是否存在
        String license = customerCar.getLicense().replace(" ", "").toUpperCase();
        Result check = checkLicense(license, "0");
        if (!check.isSuccess()) {
            return check;
        }
        customerCar.setLicense(license);
        // vin码大写
        String vin = customerCar.getVin();
        if(StringUtils.isNotBlank(vin)){
            vin = vin.toUpperCase();
            customerCar.setVin(vin);
            // 判断vin码是否存在
            Long shopId = UserUtils.getShopIdForSession(request);
            Boolean bool = customerCarFacade.checkVinIsExist(shopId, vin, customerCar.getId());
            if(bool){
                return Result.wrapErrorResult("","vin码已存在，请勿重复添加！");
            }
        }
        Result resultCar = null;
        try {
            resultCar = customerCarService.add(customerCar, UserUtils.getUserInfo(request));
        } catch (Exception e) {
            log.error("新增车辆信息异常", e);
            return Result.wrapErrorResult("", "新增车辆信息异常");
        }
        return resultCar;
    }

    @Deprecated
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @ResponseBody
    public Result inertCustomerInfo(CustomerCar customerCar, HttpServletRequest request) {

        // 数据有效性判断
        // 1、判断车牌号
        Result result = validateData(customerCar, true);
        if (!result.isSuccess()) {
            return result;
        }

        // 判断车牌是否存在
        String license = customerCar.getLicense().replace(" ", "").toUpperCase();
        Result check = checkLicense(license, "0");
        if (!check.isSuccess()) {
            return check;
        }
        customerCar.setLicense(license);
        Result resultCar = null;
        try {
            resultCar = customerCarService.add(customerCar, UserUtils.getUserInfo(request));

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
                        if (Langs.isNotBlank(customerCar.getContact())) {
                            appoint.setCustomerName(customerCar.getContact());
                        }
                        if (Langs.isNotBlank(customerCar.getContactMobile())) {
                            appoint.setMobile(customerCar.getContactMobile());
                        }
                        appoint.setCustomerCarId(customerCar.getId());
                        appointService.update(appoint);
                        // 预约单用户新增之后,更新customerCar中appointCount的预约次数
                        updateCustomerCarAppointCnt(customerCar.getId());

                    }
                } else {
                    // 有可能在做预约单的时候,从别的地方新增客户,所有新增客户之后要根据车牌回填更新appoint的customerCarId
                    Long shopId = UserUtils.getShopIdForSession(request);
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
                                if (Langs.isNotBlank(customerCar.getContact())) {
                                    appoint.setCustomerName(customerCar.getContact());
                                }
                                if (Langs.isNotBlank(customerCar.getContactMobile())) {
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
            log.error("新增车辆信息异常", e);
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

    // 检测车牌是否存在
    @RequestMapping(value = "checkLicense", method = RequestMethod.POST)
    @ResponseBody
    public Result checkLicense(@RequestParam(value = "license") String license, @RequestParam(value = "id", required = false,defaultValue = "0") Long id) {
        return checkLicense(license.replace(" ", "").toUpperCase(), id.toString());
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
            if (StringUtils.isNotBlank(id) && !StringUtils.equals(list.get(0).getId() + "", id)) {
                return Result.wrapErrorResult("", "车牌已存在，请勿重复添加！");
            }

        }
        return Result.wrapSuccessfulResult(true);

    }

    /**
     * 校验收据有效性
     *
     * @param customerCar
     * @param isCheckLicense
     * @return
     */
    private Result validateData(CustomerCar customerCar, boolean isCheckLicense) {
        if (null == customerCar) {
            return Result.wrapErrorResult("", "车辆信息不能为空");
        }
        if (StringUtils.isBlank(customerCar.getCustomerName())) {
            return Result.wrapErrorResult("", "车主不能为空");
        }
        if (isCheckLicense) {
            if (!StringUtil.checkCarLicense(customerCar.getLicense())) {
                return Result.wrapErrorResult("", "车牌输入非法");
            }
        }
        //手机号码非必填,去掉手机号的校验
        if (StringUtils.isNotBlank(customerCar.getMobile()) && !StringUtil.isMobileNO(customerCar.getMobile())){
            return Result.wrapErrorResult("", "车主电话输入不正确");
        }
        if (!StringUtil.isStringEmpty(customerCar.getVin())
                && !StringUtil.isVinString(customerCar.getVin())) {
            return Result.wrapErrorResult("", "VIN码输入不正确");
        }

        if (!StringUtil.isSpecialString(customerCar.getEngineNo())) {
            return Result.wrapErrorResult("", "发动机号输入不正确");
        }
        return Result.wrapSuccessfulResult(true);
    }

    //删除客户和帐户
    @RequestMapping(value = "delete_account",method = RequestMethod.POST)
    @ResponseBody
    public Result deleteCustomerInfo(@RequestParam("customerId")Long customerId) {
        if(customerId ==null || customerId <=0){
            return Result.wrapErrorResult("", "非法的customerId参数！");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Customer customer = customerService.selectById(customerId);
        //如果车主电话不为空，删账户
        if(customer != null && StringUtils.isNotBlank(customer.getMobile())) {
            Result result = accountInfoService.deleteAccountInfo(shopId, customerId);
            if (!result.isSuccess()) {
                return result;
            }
        }
        Result re = customerService.deleteById(customerId);
        return re;
    }

    /**
     * 判断帐户是否可以删除
     * @param customerId
     * @return
     */
    @RequestMapping("is_can_delete")
    @ResponseBody
    public Result canDelAccount(Long customerId,Long customerCarId){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customerCarId", customerCarId);
        params.put("shopId", shopId);
        if (!CollectionUtils.isEmpty(orderInfoService.select(params))) {
            return Result.wrapErrorResult("", "此车辆存在工单，不能删除！");
        }
        Map<String, Object> countParams = new HashMap<String, Object>();
        countParams.put("shopId",shopId);
        countParams.put("customerId",customerId);
        Integer count = customerCarService.selectCount(countParams);
        if(count > 1){
            return Result.wrapErrorResult("", "此车辆账户绑定多辆车，请先解绑后再删除！");
        }
        Result result = accountInfoService.isDeleteAccountInfo(shopId, customerId);
        return result;
    }
    // 保险公司列表
    @RequestMapping("insurance_company_list")
    @ResponseBody
    public Result list() {
        List<InsuranceCompany> insuranceCompanyList = insuranceCompanyService.select(null);
        return Result.wrapSuccessfulResult(insuranceCompanyList);
    }

    // 获取车牌号
    @RequestMapping("get_license_prefix")
    @ResponseBody
    public Result getLicensePrefix(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        String license = null;
        /**
         *
         */
        Shop shop = this.shopService.selectById(shopId);
        if (shop != null && shop.getCity() != null) {
            license = getLicenseByCityId(shop.getCity());
        } else {
            /**
             * 如果获取不到门店的cityId,则根据ip自动定位城市id,并获取响应的车牌, 目前暂时不加这块逻辑.
             */
        }
        if (license == null) {
            license = "浙A";
            log.error("city:{}, id: {} can't mapping license." + shop.getCityName()
                    + shop.getCity());
        }
        return Result.wrapSuccessfulResult(license);
    }

    private String getLicenseByCityId(Long cityId) {
        if (cityId != null) {
            Map<String, Object> paramMap = Maps.newConcurrentMap();
            paramMap.put("cityId", cityId);
            List<LicenseCity> licenseCities = licenseMatchCityService.select(paramMap);
            /**
             * 如果是直辖市,会存在多个车牌情况,默认取第一个即可
             */
            if (licenseCities != null && licenseCities.size() > 0) {
                return licenseCities.get(0).getLicense();
            }
        }
        return null;
    }

    @RequestMapping("get_by_license")
    @ResponseBody
    public Result getCustomerByLicense(@RequestParam("license") String license) {
        Customer customer = null;
        CustomerCar customerCar = this.customerCarService.selectByLicenseAndShopId(license,
                UserUtils.getShopIdForSession(request));
        if (customerCar != null) {
            customer = this.customerService.selectById(customerCar.getCustomerId());
        }
        return Result.wrapSuccessfulResult(customer);
    }

    @RequestMapping("get_car_level")
    @ResponseBody
    public Result getCarLevel(@RequestParam("id") Integer id){
        com.tqmall.core.common.entity.Result<CarCategoryDTO> carCategory = carCategoryService.getCarCategoryByPrimaryId(id);
        if(carCategory==null || !carCategory.isSuccess()) {
            return Result.wrapErrorResult("", "查询不到此ID对应的车辆级别");
        }else{
            return Result.wrapSuccessfulResult(carCategory.getData());
        }
    }

    /**
     * 接车全流程：客户车辆详情
     *
     * @param id   客户id
     * @param refer 来源标签，用于从哪个页面跳转过来，选中效果
     * @return
     */
    @RequestMapping("car-detail")
    public String getCarDetail(Model model,
                               @RequestParam(value = "id", required = true) Long id,
                               @RequestParam(value = "refer", required = false) String refer) {
        //获取客户详情
        CarDetailVo carDetailVo = customerCarFacade.getCarInfo(id, UserUtils.getShopIdForSession(request));
        //设置客户车辆详细信息
        if(carDetailVo == null){
            //跳至客户信息
            return "redirect:/shop/customer/edit";
        }else{
            model.addAttribute("carDetailVo", carDetailVo);
        }
        //设置基本信息
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        if (CarDetailReferEnum.getCodeByMessage(refer) == null) {
            refer = CarDetailReferEnum.CUSTOMER.getMessage();
        }
        //设置是否有帐户信息
        AccountInfo accountInfo = accountInfoService.getAccountInfoByCarIdAndShopId(UserUtils.getShopIdForSession(request),id);
        if (null != accountInfo){
            model.addAttribute("accountInfo", accountInfo);
        }
        model.addAttribute("refer", refer);
        return "yqx/page/customer/car-detail";
    }

    /**
     * 查询门店所有自定义的标签
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "customize-tag", method = RequestMethod.GET)
    public Result getCustomizeTag(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<String> tagNameList = customerTagService.listCustomizeTag(shopId);
        List<CustomerTagVo> customerTagList = Lists.newArrayList();
        if (Langs.isNotEmpty(tagNameList)) {
            for (String name : tagNameList) {
                CustomerTagVo customerTag = new CustomerTagVo();
                customerTag.setName(name);
                customerTagList.add(customerTag);
            }
        }
        return Result.wrapSuccessfulResult(customerTagList);
    }

    /**
     * 获取推荐服务
     * @param carId
     * @return
     */
    @RequestMapping("get_service")
    @ResponseBody
    public Result getRecommendService(@RequestParam(value = "carId", required = false)Long carId) {
        try {
            return customerCarService.getRecommendService(carId);
        } catch (Exception e) {
            log.error("【dubbo:根据年款id、输入里程数查询推荐保养】出现异常", e);
            return Result.wrapErrorResult(LegendErrorCode.CAR_SERVICE_EMPTY_EX.getCode(), LegendErrorCode.CAR_SERVICE_EMPTY_EX.getErrorMessage());
        }
    }

    /**
     * 客户数据导出
     * @param pageable
     * @param response
     * @return
     */
    @RequestMapping(value = "/export")
    @ResponseBody
    public void customerExport(@PageableDefault(page = 1, value = 5000, sort = "gmtModified", direction = Sort.Direction.DESC) final Pageable pageable, final HttpServletResponse response)throws Exception {

        long startTime = System.currentTimeMillis();
        UserInfo userInfo = UserUtils.getUserInfo(request);

        DefaultExcelExportor exportor = null;
        int totalSize = 0;
        try {
            String fileName = "客户信息-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            /**
             * 初始化参数
             */
            Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
            Long shopId = UserUtils.getShopIdForSession(request);
            LegendCustomerCarRequest customerCarRequest = new LegendCustomerCarRequest();
            int page = pageable.getPageNumber() - 1;
            PageableRequest pageableRequest = new PageableRequest(page, pageable.getPageSize());
            customerCarRequest.setShopId(shopId.toString());
            buildCommonSearch(searchParams, customerCarRequest);

            /**
             * 循环写Excel
             */
            DefaultPage defaultPage = null;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——客户信息";
            exportor.writeTitle(null, headline, CustomerCarVo.class);
            while (true) {
                defaultPage = customerCarFacade.getCustomerCarsFromSearch(pageableRequest, customerCarRequest);
                if (Langs.isNull(defaultPage) || Langs.isEmpty(defaultPage.getContent())) {
                    break;
                }
                exportor.write(defaultPage.getContent());
                totalSize += defaultPage.getContent().size();
                if (totalSize >= defaultPage.getTotalElements()) {
                    break;
                }
                pageableRequest = new PageableRequest(pageableRequest.getPageNumber() + 1, pageableRequest.getPageSize());
            }
        } catch (IOException | WheelException e) {
            log.error("导出车辆Excel异常",e);
            throw e;
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog("客户信息", userInfo, totalSize, endTime - startTime));
    }

    /**
     * 获取客户详情的车辆图片
     * @param customerCarId
     * @return
     */
    @RequestMapping("get-car-file")
    @ResponseBody
    public Result getCarFile(@RequestParam(value = "customerCarId")Long customerCarId){
        if(customerCarId == null){
            return Result.wrapErrorResult(LegendErrorCode.CAR_IMG_EX.getCode(),LegendErrorCode.CAR_IMG_EX.getErrorMessage());
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String,Object> customerCarFileMap = Maps.newHashMap();
        customerCarFileMap.put("shopId",shopId);
        customerCarFileMap.put("relId",customerCarId);
        customerCarFileMap.put("relType",1);
        customerCarFileMap.put("fileType", 5);
        List<CustomerCarFile> customerCarFileList = customerCarFileService.select(customerCarFileMap);
        if(CollectionUtils.isEmpty(customerCarFileList)){
            return Result.wrapErrorResult(LegendErrorCode.CAR_IMG_EX.getCode(),LegendErrorCode.CAR_IMG_EX.getErrorMessage());
        }
        return Result.wrapSuccessfulResult(customerCarFileList);
    }

    /**
     *获取淘汽检测单
     * @return
     */
    @RequestMapping("get-tqCheck")
    @ResponseBody
    public Result getCarTqCheck(@PageableDefault(page = 1, value = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,Long customerCarId ){
        UserInfo user = UserUtils.getUserInfo(request);
        Map<String , Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("customerCarId",customerCarId);
        params.put("pageable",pageable);
        com.tqmall.zenith.errorcode.support.Result result = rpcTqCheckService.getTqCheckLogList(params);
        if (null == result){
            log.info("［未搜索到车辆淘汽检测单］shopId:{},carId:{}",user.getShopId(),customerCarId);
            DefaultPage page = new DefaultPage(new ArrayList());
            return Result.wrapSuccessfulResult(page);
        }
        DefaultPage page = (DefaultPage) result.getData();
        List<TqCheckLog> tqCheckLogList = page.getContent();
        List<TqCheckDetailListDTO> detailList = new ArrayList<>();
        try {
            for (TqCheckLog tqCheckLog: tqCheckLogList){
                com.tqmall.zenith.errorcode.support.Result<TqCheckDetailListDTO> re = rpcTqCheckService.getTqCheckDetailList(user.getShopId(),null,customerCarId,tqCheckLog.getId());
                if (result.isSuccess()){
                    re.getData().setGmtCreate(tqCheckLog.getGmtCreate());
                    re.getData().setGmtCreateStr(re.getData().getGmtCreateStr());
                    detailList.add(re.getData());
                }
            }
            PageRequest pageRequest = new PageRequest(page.getNumber(),page.getSize());
            DefaultPage tqPage = new DefaultPage(detailList, pageRequest, page.getTotalElements());
            return Result.wrapSuccessfulResult(tqPage);
        } catch (Exception e) {
            log.error("［获取淘汽车辆检测记录错误］:shopId:{},carId:{}",user.getShopId(),customerCarId,e);
        }
        return Result.wrapErrorResult("","获取淘汽检测单失败");
    }

    @RequestMapping("is-exist-vin")
    @ResponseBody
    public Result<Boolean> isExistVin(@RequestParam(value = "vin") final String vin,@RequestParam(value = "customerCarId") final Long customerCarId){
        com.tqmall.core.common.entity.Result<Boolean> result = new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(vin,"请输入vin码");
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return customerCarFacade.checkVinIsExist(shopId, vin, customerCarId);
            }
        }.execute();
        if(result.isSuccess()){
            Boolean bool = result.getData();
            if(bool){
                return Result.wrapErrorResult("", "vin码已存在，请勿重复添加！");
            }
            return Result.wrapSuccessfulResult(true);
        }
        return Result.wrapErrorResult(result.getCode(), result.getMessage());
    }
}
