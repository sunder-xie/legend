package com.tqmall.legend.web.appoint;

import com.alibaba.dubbo.common.json.ParseException;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.customer.LicenseMatchCityService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.*;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.enums.appoint.AppointChannelEnum;
import com.tqmall.legend.object.enums.appoint.AppointStatusEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.appoint.AppointFacade;
import com.tqmall.legend.facade.appoint.vo.AppointDetailFacVo;
import com.tqmall.legend.facade.customer.CustomerCarFacade;
import com.tqmall.legend.facade.customer.vo.CarDetailVo;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wushuai on 16/4/6.
 */
@Slf4j
@Controller
@RequestMapping("/shop/appoint")
public class ShopAppointController  extends BaseController {

    @Autowired
    private AppointService appointService;
    @Autowired
    private CustomerCarFacade customerCarFacade;
    @Autowired
    private AppointFacade appointFacade;
    @Autowired
    private LicenseMatchCityService licenseMatchCityService;
    @Autowired
    private SnFactory snFactory;
    @Autowired
    private ShopService shopService;


    /**
     * 转到预约单查询页
     * @param model
     * @return
     */
    @RequestMapping("/appoint-list")
    public String list(Model model){
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());//接车维修子菜单下
        return "yqx/page/appoint/appoint-list";
    }

    /**
     * 转到新增预约单页面
     * @param model
     * @return
     */
    @RequestMapping("/appoint-edit")
    public String edit(Model model,@RequestParam(value = "appointId", required = false) Long appointId,
                       @RequestParam(value = "reAppointId", required = false) Long reAppointId,
                       @RequestParam(value = "customerCarId", required = false) Long customerCarId){
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());//接车维修子菜单下

        AppointDetailFacVo appointDetailFacVo = null;
        if(appointId!=null&&appointId!=0){//编辑
            appointDetailFacVo = appointFacade.getAppointDetail(appointId,shopId);
            if(appointDetailFacVo!=null){
                AppointVo appointVo = appointDetailFacVo.getAppointVo();
                if(appointVo!=null&&StringUtils.isBlank(appointVo.getLicense())){
                    String license = licenseMatchCityService.getLicenseCityByShopId(shopId);
                    appointVo.setLicense(license);//按门店所在城市设置门店头两位车牌号
                }
            }
        } else {//新增
            if(reAppointId!=null){//由原预约单复制
                appointDetailFacVo = appointFacade.getAppointDetail(reAppointId,shopId);
                if(appointDetailFacVo!=null){
                    AppointVo appointVo = appointDetailFacVo.getAppointVo();
                    if(appointVo!=null){
                        appointVo.setId(null);
                        appointVo.setDownPayment(null);
                        appointVo.setAppointSn(snFactory.generateAppointSn(shopId));
                    }
                }
            } else {
                appointDetailFacVo = new AppointDetailFacVo();
                AppointVo appointVo = new AppointVo();
                String license = licenseMatchCityService.getLicenseCityByShopId(shopId);
                appointVo.setLicense(license);//按门店所在城市设置门店头两位车牌号
                appointVo.setAppointSn(snFactory.generateAppointSn(shopId));
                if(customerCarId!=null){
                    CarDetailVo carDetailVo = customerCarFacade.getCarInfo(customerCarId,shopId);
                    if(carDetailVo!=null){
                        Customer customer = carDetailVo.getCustomer();
                        CustomerCar customerCar = carDetailVo.getCustomerCar();
                        if(customer!=null){
                            appointVo.setCustomerName(customer.getCustomerName());
                            appointVo.setMobile(customer.getMobile());
                        }
                        if(customerCar!=null){
                            appointVo.setCustomerCarId(customerCar.getId());
                            appointVo.setLicense(customerCar.getLicense());
                            appointVo.setCarBrandName(customerCar.getCarBrand());
                            appointVo.setCarBrandId(customerCar.getCarBrandId());
                            appointVo.setCarSeriesName(customerCar.getCarSeries());
                            appointVo.setCarSeriesId(customerCar.getCarSeriesId());
                            appointVo.setCarAlias(customerCar.getByName());
                        }
                    }
                }
                appointDetailFacVo.setAppointVo(appointVo);
            }
        }
        model.addAttribute("appointDetailFacVo", appointDetailFacVo);
        return "yqx/page/appoint/appoint-edit";
    }

    /**
     * 转到预约单详情页
     * @param model
     * @return
     */
    @RequestMapping("/appoint-detail")
    public String detail(Model model,@RequestParam(value = "appointId", required = false) Long appointId){
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());//接车维修子菜单下
        Long shopId = UserUtils.getShopIdForSession(request);
        if(appointId!=null){
            AppointDetailFacVo appointDetailFacVo = appointFacade.getAppointDetail(appointId,shopId);
            model.addAttribute("appointDetailFacVo", appointDetailFacVo);
            try {
                AppointVo appointVo = appointDetailFacVo.getAppointVo();
                appointVo.setPreviewType(1l);//设置为已查看
                appointService.update(appointVo);
            } catch (Exception e) {
                log.error("更新预约单previewType异常:{}", e);
            }
        }
        return "yqx/page/appoint/appoint-detail";
    }

    /**
     * 查询/导出预约信息
     * @param pageable
     * @return
     */
    @ResponseBody
    @RequestMapping("/appoint-list/get")
    public Object getAppointList( @PageableDefault(page = 1, value = 12) Pageable pageable, HttpServletResponse response){
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        searchParams.put("shopId", shopId);
        searchParams.put("pushStatus", 1);//1表示不需要下推的或者已经下推的
        ArrayList<String> sorts = new ArrayList<>();
        sorts.add("appoint_time desc");
        searchParams.put("sorts", sorts);
        if (searchParams.containsKey("startTime")) {
            searchParams.put("appointTimeGt", searchParams.get("startTime") + " 00:00:00");
        }
        if (searchParams.containsKey("endTime")) {
            searchParams.put("appointTimeLt", searchParams.get("endTime") + " 23:59:59");
        }

        String formType = "get";
        if(searchParams.containsKey("formType")) {
            formType = (String) searchParams.get("formType");
        }
        if ("get".equals(formType)) {//查询
            log.info("预约单查询-查询预约单条件:{}", searchParams);
            DefaultPage<AppointVo> page = (DefaultPage<AppointVo>) appointFacade.getAppointVoPage(pageable, searchParams);
            page.setPageUri(request.getRequestURI());
            page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
            return Result.wrapSuccessfulResult(page);
        } else {//导出 type=export;
            //appoint-export.flt为模板
            log.info("导出预约单开始,shopId:{},时间:{},导出条件:{}", shopId, DateUtil.convertDateToYMDHMS(new Date()),searchParams);
            if("Y".equals(searchParams.get("exportCurPage"))){//是否导出到当前页
                PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                                pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                searchParams.put("limit", pageRequest.getPageSize());
                searchParams.put("offset", pageRequest.getOffset());
            }
            List<Appoint> appointInfoList = appointFacade.getAppointInfoList(searchParams);
            ModelAndView view = new ModelAndView("yqx/page/appoint/appoint-export");
            view.addObject("appointInfoList",appointInfoList);
            // 设置响应头
            response.setContentType("application/x-msdownload");
            String filename = "appoint_list";
            try {
                filename = URLEncoder.encode("预约单列表", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("预约单导出URLEncoder转义不正确");
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
            log.info("导出预约单结束,shopId:{},时间:{}", shopId, DateUtil.convertDateToYMDHMS(new Date()));
            return view;
        }
    }

    /**
     * 预约单导出
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/appoint-list/export")
    public void exportAppointList(HttpServletRequest request,
                                  HttpServletResponse response) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        searchParams.put("shopId", shopId);
        searchParams.put("pushStatus", 1);//1表示不需要下推的或者已经下推的
        ArrayList<String> sorts = new ArrayList<>();
        sorts.add("appoint_time desc");
        searchParams.put("sorts", sorts);
        if (searchParams.containsKey("startTime")) {
            searchParams.put("appointTimeGt", searchParams.get("startTime") + " 00:00:00");
        }
        if (searchParams.containsKey("endTime")) {
            searchParams.put("appointTimeLt", searchParams.get("endTime") + " 23:59:59");
        }
        int limit = 500;
        int offset = 0;
        searchParams.put("limit", limit);

        String fileName = "预约单列表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        long startTime = System.currentTimeMillis();

        ExcelExportor exportor = null;
        int totalSize = 0;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——预约单列表";
            exportor.writeTitle(null, headline, Appoint.class);
            while (true) {
                searchParams.put("offset", offset);
                List<Appoint> appointInfoList = appointFacade.getAppointInfoList(searchParams);
                if (CollectionUtils.isEmpty(appointInfoList)) {
                    break;
                }
                exportor.write(appointInfoList);
                totalSize += appointInfoList.size();
                offset += limit;
            }
        } catch (Exception e) {
            log.error("预约单导出异常，门店id：{}", shopId, e);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog("预约单列表", userInfo, totalSize, endTime - startTime));
    }

    /**
     * 取消预约单
     * @param appointId
     * @param cancelReason
     * @return
     */
    @RequestMapping(value = "/cancel-appoint",method = RequestMethod.POST)
    @ResponseBody
    public Result cancelAppoint(@RequestParam(value = "appointId", required = true)Long appointId,@RequestParam(value = "cancelReason", required = true)String cancelReason) {
        log.info("门店取消预约单:id{},取消原因{}", appointId, cancelReason);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        Long status = AppointStatusEnum.SHOP_CANCEL.getIndex().longValue();//门店取消预约单
        Result result = appointFacade.cancelAppoint(shopId,appointId,userId, cancelReason, status);
        if (null != result) {
            if (result.isSuccess()) {
                return Result.wrapSuccessfulResult("取消预约单成功");
            } else {
                log.info("取消预约失败:{}", result.getErrorMsg());
                return Result.wrapErrorResult("-1",result.getErrorMsg());
            }
        } else{
            log.error("取消预约异常,result为null");
            return Result.wrapErrorResult("-1","取消预约异常!");
        }
    }

    /**
     * 确认预约单
     * @param appointId
     * @return
     */
    @RequestMapping(value = "/confirm-appoint", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> confirmApponit(@RequestParam(value = "appointId", required = true)Long appointId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        log.info("门店Web预约单确认shopId{},appointId{},userId{}", shopId,appointId,userId);
        return appointFacade.confirmAppoint(appointId,shopId,userId);
    }

    /**
     * 作废预约单
     * @param appointId
     * @return
     */
    @RequestMapping(value = "/invalid-appoint", method = RequestMethod.POST)
    @ResponseBody
    public Result invalidApponit(@RequestParam(value = "appointId", required = true)Long appointId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        log.info("门店Web预约单作废shopId{},appointId{},userId{}", shopId,appointId,userId);
        return appointService.invalidAppoint(shopId, appointId, userId);
    }

    /**
     * 删除预约单(逻辑删)
     * @param appointId
     * @return
     */
    @RequestMapping(value = "/delete-appoint", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteApponit(@RequestParam(value = "appointId", required = true)Long appointId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        log.info("门店Web预约单删除shopId{},appointId{},userId{}", shopId,appointId,userId);
        return appointService.deleteAppoint(shopId, appointId, userId);
    }

    /**
     * 门店web新增或者修改预约单
     * @param appointEntityVo
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody AppointEntityVo appointEntityVo) throws ParseException {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (userInfo == null) {
            return Result.wrapErrorResult("", "用户登录失效，请登录后再尝试");
        }
        Appoint appoint = appointEntityVo.getAppoint();//预约单的主题信息
        if (null == appoint) {
            return Result.wrapErrorResult("", "预约单信息不能为空");
        }
        String license = appoint.getLicense();//车牌
        if(StringUtils.isEmpty(license)){
            return Result.wrapErrorResult("", "车牌不能为空");
        }
        if(!StringUtil.checkCarLicense(license)){
            return Result.wrapErrorResult("", "车牌不合法");
        }
        String customerName = appoint.getCustomerName();
        if (StringUtil.isStringEmpty(customerName)) {
            return Result.wrapErrorResult("", "车主不能为空");
        }
        String mobile = appoint.getMobile();
        if(StringUtils.isEmpty(mobile)){
            return Result.wrapErrorResult("", "车主电话不能为空");
        }
        if(!StringUtil.isMobileNO(mobile)){
            return Result.wrapErrorResult("", "车主电话格式不正确");
        }
        if (appoint.getRegistrantId() == null || 0l == appoint.getRegistrantId()) {
            return Result.wrapErrorResult("", "预约登记人不能为空");
        }
        String appointSn = appoint.getAppointSn();
        if (StringUtil.isStringEmpty(appointSn)) {
            return Result.wrapErrorResult("", "预约单号不能为空");
        }
        if (StringUtil.isStringEmpty(appoint.getAppointTimeFormat())) {
            return Result.wrapErrorResult("", "预约时间不能为空");
        }
        if (StringUtil.isStringEmpty(appoint.getAppointContent())) {
            return Result.wrapErrorResult("", "预约内容不能为空");
        }
        String appointServiceStr = appointEntityVo.getAppointServiceJson();//预约单的服务信息
        List<AppointServiceVo> appointServicesList = new ArrayList<>();
        if (StringUtil.isNotStringEmpty(appointServiceStr)) {
            //Json字符串转为对象
            JSONArray jsonArray = JSONArray.fromObject(appointServiceStr);
            appointServicesList = (List<AppointServiceVo>) JSONArray.toCollection(jsonArray, AppointServiceVo.class);
        }
        Result result = null;
        // 时间字符串转Date
        Date appointTime = DateUtil.convertStrToDate(appoint.getAppointTimeFormat(), DateUtil.DATE_FORMAT_YMDHM);
        appoint.setAppointTime(appointTime);
        if(appoint.getId()==null){//新增
            //新增则查询预约单号是否已存在
            Map<String,Object> searchMap = Maps.newHashMap();
            searchMap.put("shopId",UserUtils.getShopIdForSession(request));
            searchMap.put("appointSn",appointSn);
            Integer appointCount = appointService.selectCount(searchMap);
            if(appointCount > 0 ){
                return Result.wrapErrorResult("", "预约单号已存在，请重新编辑预约单号或刷新页面获取预约单号");
            }
            log.info("新增预约单参数,联系人:{} 手机号:{} 预约单SN:{} 车牌号{},预约服务{}", customerName, mobile, appointSn,license,appointServicesList);
            Long shopId = userInfo.getShopId();//门店ID
            appoint.setShopId(shopId);
            appoint.setCreator(userInfo.getUserId());
            appoint.setChannel(AppointChannelEnum.WEB.getChannelId().longValue());
            appoint.setStatus(AppointStatusEnum.APPOINT_SUCCESS.getIndex().longValue());//门店预约 status = 1 确认预约
            appoint.setPushStatus(AppointChannelEnum.WEB.getPushStatus().longValue());
            if(!CollectionUtils.isEmpty(appointServicesList)){
                for (AppointServiceVo as : appointServicesList) {
                    if (null != as.getServiceId() && 0l != as.getServiceId()) {
                        as.setAppointSn(appoint.getAppointSn());
                        as.setShopId(appoint.getShopId());
                        as.setCreator(appoint.getCreator());
                    }
                }
            }
            log.info("门店web新增预约单和服务的关系!Appoint{},AppointService List:{}",appoint, appointServicesList);
            result = appointFacade.createAppointByShopWeb(appoint, appointServicesList);
        } else{//更新
            appoint.setAppointSn(null);//预约单号不可编辑
            appoint.setShopId(userInfo.getShopId());//预约单号不可编辑
            appoint.setStatus(AppointStatusEnum.APPOINT_SUCCESS.getIndex().longValue());//门店修改后设置状态为已确认(预约成功)
            log.info("门店web更新预约单和服务的关系!Appoint{},AppointService List:{}",appoint, appointServicesList);
            result = appointFacade.updateAppointAndService(appoint, appointServicesList,userInfo);
        }
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(appoint);
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
    }

    /**
     * 判断预约单号是否存在(存在时返回data:ture)
     * @param appointSn
     * @return
     */
    @RequestMapping(value = "/is-exist-appointSn")
    @ResponseBody
    public Result isExistAppointSn(@RequestParam(value = "appointSn", required = true)String appointSn) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        boolean isExist = appointService.isExistAppointSn(shopId,appointSn);
        return Result.wrapSuccessfulResult(isExist);
    }

    /**
     * 客户详情接口
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping("customer/list")
    @ResponseBody
    public Object getList(@PageableDefault(page = 1, value = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {
        // 查询车辆预检列表
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        DefaultPage<Appoint> page = (DefaultPage<Appoint>) appointService.getPage(pageable, searchParams);
        page.setPageUri(request.getRequestURI());
        page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(page);
    }
}
