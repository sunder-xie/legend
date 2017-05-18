package com.tqmall.legend.server.appoint;

import com.google.common.collect.Lists;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.error.LegendErrorHelper;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.shop.NoteShopConfig;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.appoint.AppointChannelEnum;
import com.tqmall.legend.facade.appoint.AppointFacade;
import com.tqmall.legend.facade.appoint.vo.AppointDetailFacVo;
import com.tqmall.legend.object.enums.appoint.AppointStatusEnum;
import com.tqmall.legend.object.param.appoint.*;
import com.tqmall.legend.object.param.customer.CustomerCarParam;
import com.tqmall.legend.object.result.appoint.AppointDTO;
import com.tqmall.legend.object.result.appoint.AppointDetailDTO;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.object.result.customer.CustomerCarDTO;
import com.tqmall.legend.object.result.service.ShopServiceInfoDTO;
import com.tqmall.legend.server.appoint.adaptor.AppointConvertor;
import com.tqmall.legend.service.appoint.RpcAppointService;
import com.tqmall.zenith.errorcode.ErrorCodeBuilder;
import com.tqmall.zenith.errorcode.PlatformErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by zsy on 16/1/12.
 */
@Slf4j
@Service ("rpcAppointService")
public class RpcAppointServiceImpl implements RpcAppointService {

    @Autowired
    private AppointService appointService;
    @Autowired
    private NoteConfigureService noteConfigureService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private AppointFacade appointFacade;
    @Autowired
    private SnFactory snFactory;
    @Autowired
    private CustomerCarService customerCarService;

    /**
     * 推送消息至cube，目前管理后台消费此服务
     *
     * @param type
     * @param appointParam
     *
     * @return
     */
    @Override
    public Result pushMsg(String type, AppointParam appointParam) {
        Result returnResult;
        Result checkResult = checkParam(appointParam);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        //设置预约单对象
        Appoint appoint = new Appoint();
        appoint.setId(appointParam.getId());
        appoint.setShopId(appointParam.getShopId());
        appoint.setAppointTime(appointParam.getAppointTime());
        appoint.setAppointTimeFormat(appointParam.getAppointTimeFormat());
        appoint.setAppointContent(appointParam.getAppointContent());
        appoint.setMobile(appointParam.getMobile());
        String source = appointParam.getSource();
        try {
            log.info("来源：{},【dubbo发送消息】：RpcAppointServiceImpl.pushMsg,入参{},{}", source, type, appointParam);
            com.tqmall.legend.common.Result result = appointService.pushMsgToCube(type, appoint);
            //返回值为true,所以直接获取data
            returnResult = Result.wrapSuccessfulResult(result.getData());
        } catch (Exception e) {
            log.error("【dubbo发送消息】：RpcAppointServiceImpl.pushMsg,预约单下推至门店，发送消息失败，异常信息{}", e);
            returnResult = Result.wrapErrorResult(LegendErrorHelper.newLegendError().setFatal().setDetailCode("0022").buildErrorCode(), "消息发送失败");
        }
        return returnResult;
    }

    /**
     * 查询客户的待处理的预约单列表(待确认,已确认状态,提醒设置按店铺来)
     *
     * @param shopId
     * @param customerCarId
     * @param page
     * @param size
     *
     * @return
     */
    @Override
    public Result getCustomerAppointList(Long shopId, Long customerCarId, Integer page, Integer size) {
        if (null == page || page < 1) {
            page = 1;
        }
        if (null == size || size < 1) {
            size = 10;
        }

        //获取店铺预约单配置列表
        int appointInvalidDate = 0;
        int appointValidDate = 0;
        NoteShopConfig appointShopConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_APPOINT_CONF_TYPE);
        if (null != appointShopConfig) {
            appointValidDate = appointShopConfig.getFirstValue();
            appointInvalidDate = appointShopConfig.getInvalidValue();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("isDeleted", "N");
        param.put("pushStatus", 1);
        param.put("appointTimeGt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), -appointInvalidDate)));
        param.put("appointTimeLt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), appointValidDate)));
        param.put("customerCarId", customerCarId);
        //待确认，已确认
        param.put("statusList", new Integer[] { 0, 1 });
        Integer totalCount = appointService.selectCount(param);
        param.put("offset", (page - 1) * size);
        param.put("limit", size);
        param.put("sorts", new String[] { "appoint_time asc" });
        List<Appoint> appointList = appointService.select(param);

        PageEntityDTO<AppointDTO> pageEntityDTO = new PageEntityDTO();
        pageEntityDTO.setPageNum(page);
        pageEntityDTO.setTotalNum(totalCount);
        List<AppointDTO> appointDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appointList)) {
            for (Appoint appoint : appointList) {
                AppointDTO appointDTO = appointDTOAssemble(shopId, appoint);
                appointDTOList.add(appointDTO);
            }
        }
        pageEntityDTO.setRecordList(appointDTOList);
        return Result.wrapSuccessfulResult(pageEntityDTO);
    }

    /**
     * 查询客户的有效预约单列表(预约时间大于当前,待确认,已确认状态)
     * mace用
     *
     * @param shopId 店铺id
     * @param page   页数
     * @param size   单页大小
     */
    @Override
    public Result<PageEntityDTO<AppointDTO>> getShopAppointList(Long shopId, Integer page, Integer size) {
        if (null == page || page < 1) {
            page = 1;
        }
        if (null == size || size < 1) {
            size = 10;
        }

        //获取店铺预约单配置列表
        int appointInvalidDate = 0;
        int appointValidDate = 0;
        NoteShopConfig appointShopConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_APPOINT_CONF_TYPE);
        if (null != appointShopConfig) {
            appointValidDate = appointShopConfig.getFirstValue();
            appointInvalidDate = appointShopConfig.getInvalidValue();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("isDeleted", "N");
        param.put("pushStatus", 1);
        param.put("appointTimeGt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), -appointInvalidDate)));
        param.put("appointTimeLt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), appointValidDate)));

        //待确认，已确认
        param.put("statusList", new Integer[] { 0, 1 });
        Integer totalCount = appointService.selectCount(param);
        param.put("offset", (page - 1) * size);
        param.put("limit", size);
        param.put("sorts", new String[] { "appoint_time asc" });
        List<Appoint> appointList = appointService.select(param);

        PageEntityDTO<AppointDTO> pageEntityDTO = new PageEntityDTO();
        pageEntityDTO.setPageNum(page);
        pageEntityDTO.setTotalNum(totalCount);
        List<AppointDTO> appointDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appointList)) {
            for (Appoint appoint : appointList) {
                AppointDTO appointDTO = appointDTOAssemble(shopId, appoint);
                appointDTOList.add(appointDTO);
            }
        }
        pageEntityDTO.setRecordList(appointDTOList);
        return Result.wrapSuccessfulResult(pageEntityDTO);
    }
    //appointDTO返回结果组装
    private AppointDTO appointDTOAssemble(Long shopId, Appoint appoint) {
        AppointDTO appointDTO = new AppointDTO();
        BeanUtils.copyProperties(appoint, appointDTO);
        Map<String, Object> carParams = new HashMap<>();
        carParams.put("shopId", shopId);
        carParams.put("id", appoint.getCustomerCarId());
        List<CustomerCar> customerCarList = customerCarService.select(carParams);
        if (!CollectionUtils.isEmpty(customerCarList)) {
            CustomerCar customerCar = customerCarList.get(0);
            appointDTO.setCarInfo(customerCar.getCarInfo());
        }
        return appointDTO;
    }

    /**
     * 查询客户的有效预约单计数(预约时间大于当前,待确认,已确认状态)
     * mace用
     *
     * @param shopId
     *
     * @return
     */
    @Override
    public Result<Integer> getShopAppointCount(Long shopId) {
        Map<String, Object> param = new HashMap<>();
        //获取店铺预约单配置列表
        int appointInvalidDate = 0;
        int appointValidDate = 0;
        NoteShopConfig appointShopConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_APPOINT_CONF_TYPE);
        if (null != appointShopConfig) {
            appointValidDate = appointShopConfig.getFirstValue();
            appointInvalidDate = appointShopConfig.getInvalidValue();
        }
        param.put("shopId", shopId);
        param.put("appointTimeGt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), -appointInvalidDate)));
        param.put("appointTimeLt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), appointValidDate)));
        param.put("shopId", shopId);
        param.put("isDeleted", "N");
        //待确认，已确认
        param.put("statusList", new Integer[] { 0, 1 });
        Integer totalCount = appointService.selectCount(param);
        return Result.wrapSuccessfulResult(totalCount);
    }


    /**
     * 校验数据
     *
     * @param appointParam
     *
     * @return
     */
    private Result checkParam(AppointParam appointParam) {
        String errorCode = ErrorCodeBuilder.newError(PlatformErrorCode.COMMON, "00").setFatal().setDetailCode("0008").buildErrorCode();
        if (appointParam == null) {
            return Result.wrapErrorResult(errorCode, "参数为空");
        }
        String source = appointParam.getSource();
        if (StringUtils.isBlank(source)) {
            return Result.wrapErrorResult(errorCode, "系统来源为空");
        }
        Long id = appointParam.getId();
        if (id == null) {
            return Result.wrapErrorResult(errorCode, "id不能为空");
        }
        String appointTimeFormat = appointParam.getAppointTimeFormat();
        if (StringUtils.isBlank(appointTimeFormat)) {
            return Result.wrapErrorResult(errorCode, "预约时间为空");
        }
        String appointContent = appointParam.getAppointContent();
        if (StringUtils.isBlank(appointContent)) {
            return Result.wrapErrorResult(errorCode, "预约内容为空");
        }
        String mobile = appointParam.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return Result.wrapErrorResult(errorCode, "联系人手机号为空");
        }
        return Result.wrapSuccessfulResult(true);
    }

    @Override
    public Result<AppointDTO> addAppoint(AddAppointParam addAppointParam) {
        //.参数校验
        if(addAppointParam==null){
            log.error("[dubbo]新增预约单失败,参数为空");
            return Result.wrapErrorResult("-1","参数为空");
        }
        if ( addAppointParam.getChannel()==null
            ||AppointChannelEnum.getAppointChannelEnum(addAppointParam.getChannel().intValue())==null) {
            log.error("[dubbo]新增预约单失败,未知的渠道Channel,入参{}",LogUtils.objectToString(addAppointParam));
            return Result.wrapErrorResult("-1","未知的预约渠道");
        }
        if(addAppointParam.getAppointTime()==null){
            log.error("[dubbo]新增预约单失败,预约时间为空,入参{}",LogUtils.objectToString(addAppointParam));
            return Result.wrapErrorResult("-1","预约时间为空");
        }
        Long userGlobalId = addAppointParam.getUserGlobalId();
        if(addAppointParam.getUserGlobalId()==null){
            log.error("[dubbo]新增预约单失败,UserGlobalId为空,入参{}",LogUtils.objectToString(addAppointParam));
            return Result.wrapErrorResult("-1","门店信息为空");
        }
        Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
        if(shop==null){
            log.error("[dubbo]新增预约单失败,查询不到门店信息,入参{}",LogUtils.objectToString(addAppointParam));
            return Result.wrapErrorResult("-1","查询不到门店信息");
        }
        Long shopId = shop.getId();
        String mobile = addAppointParam.getMobile();
        if(!StringUtil.isMobileNO(mobile)){
            log.error("[dubbo]新增预约单失败,手机号码格式错误,入参{}",LogUtils.objectToString(addAppointParam));
            return Result.wrapErrorResult("-1","手机号码格式错误");
        }
        CustomerCarParam carParam = addAppointParam.getCarParam();
        if (carParam==null) {
            log.error("[dubbo]新增预约单失败,却少车辆信息,入参{}",LogUtils.objectToString(addAppointParam));
            return Result.wrapErrorResult("-1","缺少车辆信息");
        }
        String license = addAppointParam.getCarParam().getLicense();
        if (!StringUtil.isCarLicense(license)) {
            log.error("[dubbo]新增预约单失败,车牌号格式错误,入参{}",LogUtils.objectToString(addAppointParam));
            return Result.wrapErrorResult("-1","车牌号格式错误");
        }
        //.组装参数
        Appoint appoint = new Appoint();
        BeanUtils.copyProperties(addAppointParam,appoint);
        appoint.setShopId(shopId);
        appoint.setAppointSn(snFactory.generateAppointSn(shopId));
        if (addAppointParam.getCustomerName()==null){
            appoint.setCustomerName("");
        }
        //.设置车辆信息
        appoint.setLicense(carParam.getLicense());
        appoint.setCarBrandId(carParam.getCarBrandId());
        appoint.setCarBrandName(carParam.getCarBrand());
        appoint.setCarSeriesId(carParam.getCarSeriesId());
        appoint.setCarSeriesName(carParam.getCarSeries());
        appoint.setVin(carParam.getVin());
        appoint.setImportInfo(carParam.getImportInfo());
        appoint.setByName(carParam.getByName());

        if (!CollectionUtils.isEmpty(addAppointParam.getServiceIds())) {
            appoint.setAppointAmount(addAppointParam.getAppointAmount());
        }
        appoint.setDownPayment(addAppointParam.getDownPayment());
        if(addAppointParam.isNeedDownPay()){
            appoint.setPayStatus(0);//支付状态，0支付失败(或未支付)，1支付成功
        }
        //.新增预约单及预约服务
        com.tqmall.legend.common.Result<Appoint> appointResult = appointService.insertAppointAndService(addAppointParam.getServiceIds(), appoint);
        log.info("[dubbo]新增预约单{}",LogUtils.funToString(addAppointParam,appointResult));
        if(!appointResult.isSuccess()){
            log.error("[dubbo]新增预约单失败{}",LogUtils.funToString(addAppointParam,appointResult));
            return Result.wrapErrorResult("-1",appointResult.getErrorMsg());
        }
        //.获取预约单详情
        Long appointId = appointResult.getData().getId();
        AppointDetailFacVo appointDetailFacVo = appointFacade.getAppointDetail(appointId,shop.getId());
        if (appointDetailFacVo==null ||appointDetailFacVo.getAppointVo()==null){
            return Result.wrapErrorResult("-1","系统错误");
        }
        //.组装返回结果
        List<AppointServiceVo> appointServiceVoList = appointDetailFacVo.getAppointServiceVoList();
        AppointDTO appointDTO = new AppointDTO();
        List<ShopServiceInfoDTO> shopServiceInfoDTOList = new ArrayList<>();
        if (appointDetailFacVo.getAppointVo()!=null) {
            BeanUtils.copyProperties(appointDetailFacVo.getAppointVo(),appointDTO);
        }
        if (!CollectionUtils.isEmpty(appointServiceVoList)) {
            for (AppointServiceVo appointServiceVo:appointServiceVoList) {
                ShopServiceInfoDTO serviceInfoDTO = new ShopServiceInfoDTO();
                ShopServiceInfo shopServiceInfo = appointServiceVo.getShopServiceInfo();
                if (shopServiceInfo!=null) {
                    BeanUtils.copyProperties(shopServiceInfo,serviceInfoDTO);
                }
                shopServiceInfoDTOList.add(serviceInfoDTO);
            }
        }
        appointDTO.setServiceInfoDTOList(shopServiceInfoDTOList);
        return Result.wrapSuccessfulResult(appointDTO);
    }

    /**
     * 查询门店指定预约时间范围内的预约单
     * @param appointDateParam
     * @return
     */
    @Override
    public Result<List<AppointDTO>> getShopDateAppointList(AppointDateParam appointDateParam) {
        log.info("[指定预约时间范围预约单列表]获取门店预约单列表,[入参]appointDateParam:{}", appointDateParam);
        if (null == appointDateParam) {
            log.info("[指定预约时间范围预约单列表] 获取数据失败,参数错误, appointDateParam==null");
            return Result.wrapErrorResult("", "参数错误");
        }
        if (null == appointDateParam.getShopId() || appointDateParam.getShopId() < 1) {
            log.info("[指定预约时间范围预约单列表] 获取数据失败,参数错误, shopId==null", appointDateParam.getShopId());
            return Result.wrapErrorResult("", "门店信息错误");
        }
        Map<String, Object> searchParams = new HashMap<>();
        buildDateParam(searchParams, appointDateParam);
        List<Appoint> appointList = appointService.select(searchParams);
        List<AppointDTO> appointDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appointList)) {
            for (Appoint appoint : appointList) {
                AppointDTO appointDTO = appointDTOAssemble(appoint.getShopId(), appoint);
                appointDTO.setStatusName(AppointStatusEnum.getNameByStatus(appoint.getStatus().intValue()));
                appointDTOList.add(appointDTO);
            }
        }
        return Result.wrapSuccessfulResult(appointDTOList);
    }
    //参数组装
    private void buildDateParam(Map<String, Object> searchParams, AppointDateParam appointDateParam) {
        searchParams.put("shopId", appointDateParam.getShopId());
        if (StringUtils.isNotBlank(appointDateParam.getStartTime())) {
            searchParams.put("appointTimeGt", appointDateParam.getStartTime());
        }
        if (StringUtils.isNotBlank(appointDateParam.getEndTime())) {
            searchParams.put("appointTimeLt", appointDateParam.getEndTime());
        }
        Integer pageNum = appointDateParam.getPageNum();
        Integer pageSize = appointDateParam.getPageSize();
        pageNum = pageNum == null ? 1 : pageNum;
        //一次查询记录小于或等于1000条
        pageSize = pageSize == null ? 10 : pageSize > Constants.MAX_PAGE_SIZE ? Constants.MAX_PAGE_SIZE : pageSize;
        searchParams.put("offset", (pageNum - 1) * pageSize);
        searchParams.put("limit", pageSize);
        if (!CollectionUtils.isEmpty(appointDateParam.getSorts())) {
            searchParams.put("sorts", appointDateParam.getSorts());
        } else {
            searchParams.put("sorts", new String[]{ "id desc"});
        }
        if (null != appointDateParam.getStatusArr() && appointDateParam.getStatusArr().length > 0) {
            searchParams.put("statusList", appointDateParam.getStatusArr());
        }
        if (null != appointDateParam.getPushStatus()) {
            searchParams.put("pushStatus", appointDateParam.getPushStatus());
        }
    }

    @Override
    public Result<Boolean> cancelAppoint(CancelAppointParam cancelAppointParam) {
        if (cancelAppointParam == null) {
            log.error("[dubbo]取消预约单失败,参数为空");
            return Result.wrapErrorResult("-1","取消预约单失败,参数为空");
        }
        Long appointId = cancelAppointParam.getAppointId();
        String cancelReason = cancelAppointParam.getCancelReason();
        Integer channel = cancelAppointParam.getChannel();
        Long userId = cancelAppointParam.getUserId();
        Integer status = AppointStatusEnum.CHEZHU_CANCEL.getIndex();//默认为 3车主取消
        if (AppointChannelEnum.LOAM.getChannelId().equals(channel)) {
            status = AppointStatusEnum.WECHAT_CANCEL.getIndex();// 微信端取消
        } else if (AppointChannelEnum.MACE.getChannelId().equals(channel)) {//商家app
            status = AppointStatusEnum.SHOP_CANCEL.getIndex();// 商家app端取消
        }
        log.info("[dubbo]取消预约单,cancelAppointParam:{}", cancelAppointParam);
        com.tqmall.legend.common.Result<Appoint> cancelAppointResult = appointFacade.cancelAppoint(null, appointId, userId, cancelReason, status.longValue());
        if (!cancelAppointResult.isSuccess()) {
            log.info("[dubbo]取消预约单失败,cancelAppointParam:{},cancelAppointResult:{}", LogUtils.objectToString(cancelAppointParam), LogUtils.objectToString(cancelAppointResult));
            return Result.wrapErrorResult(cancelAppointResult.getCode(),cancelAppointResult.getErrorMsg());
        }
        Appoint appoint = cancelAppointResult.getData();
        try{
            //获得SA电话,然后发送短信
            if (null != appoint) {
                Long shopId = appoint.getShopId();
                String appointSn = appoint.getAppointSn();
                String appointMobile  = appoint.getMobile();
                appointService.sendMsgToSA(shopId, appointSn, appointMobile, cancelReason, Constants.APP_APPOINT_CANCEL_SMS_TPL);
            }
        } catch (Exception e){
            log.error("预约单取消成功,发短信给SA失败,Appoint:{}",LogUtils.objectToString(appoint));
        }
        return Result.wrapSuccessfulResult(true);
    }

    @Override
    public Result<Boolean> confirmAppoint(Long appointId, Long userGlobalId, Long userId) {
        Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
        if (shop == null) {
            log.info("[dubbo]userGlobalId为{}的门店不存在", userGlobalId);
            return Result.wrapErrorResult("-1", "门店不存在");
        }
        Long shopId = shop.getId();
        com.tqmall.legend.common.Result<String> result = appointFacade.confirmAppoint(appointId, shopId, userId);
        log.info("[dubbo]确认预约单appointId:{},shopId:{},userId:{},result:{}", appointId, shopId, userId, LogUtils.objectToString(result));
        if (!result.isSuccess()) {
            return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
        }
        return Result.wrapSuccessfulResult(true);
    }

    @Override
    public PagingResult<AppointDTO> getAppointPage(SearchAppointPageParam searchAppointPageParam) {
        if (searchAppointPageParam == null) {
            log.info("[dubbo]分页查询预约单,入參为空");
            return PagingResult.wrapSuccessfulResult(new ArrayList<AppointDTO>(), 0);
        }

        Long shopId = null;
        if(searchAppointPageParam.getUserGlobalId()!=null){
            Shop shop = shopService.getShopByUserGlobalId(searchAppointPageParam.getUserGlobalId());
            if (shop == null) {
                log.info("[dubbo]分页查询预约单门店信息不存在,入參searchAppointPageParam:{}", LogUtils.objectToString(searchAppointPageParam));
                return PagingResult.wrapSuccessfulResult(new ArrayList<AppointDTO>(), 0);
            }
            shopId = shop.getId();
        }
        int offset = 0;
        int limit = 10;
        if(searchAppointPageParam.getOffset()!=null){
            offset = searchAppointPageParam.getOffset();
        }
        if(searchAppointPageParam.getLimit()!=null){
            limit = searchAppointPageParam.getLimit();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("keyWord", searchAppointPageParam.getKeyWord());
        params.put("shopId", shopId);
        params.put("pushStatus", 1);
        params.put("status", searchAppointPageParam.getStatus());
        params.put("offset", offset);
        params.put("limit", limit);
        String sortBy = searchAppointPageParam.getSortBy();
        String sortType = searchAppointPageParam.getSortType();
        if (StringUtils.isNotEmpty(sortBy) && StringUtils.isNotEmpty(sortType)) {
            params.put("sorts", Lists.newArrayList(sortBy + " " + sortType));
        }
        List<Appoint> appointList = appointService.select(params);
        int count = appointService.selectCount(params);
        List<AppointDTO> appointDTOList = AppointConvertor.convert(appointList);
        return PagingResult.wrapSuccessfulResult(appointDTOList, count);
    }

    @Override
    public Result<AppointDetailDTO> getAppointDetail(Long appointId, Long userGlobalId) {
        Assert.notNull(appointId,"预约单ID不能为空");
        Assert.notNull(userGlobalId,"userGlobalId不能为空");
        Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
        if (shop == null) {
            log.info("[dubbo]分页查询预约单门店信息不存在,userGlobalId:{}", userGlobalId);
            return Result.wrapSuccessfulResult(null);
        }
        Long shopId = shop.getId();
        AppointDetailFacVo appointDetailFacVo = appointFacade.getAppointDetail(appointId, shopId);
        AppointDetailDTO appointDetailDTO = AppointConvertor.convert(appointDetailFacVo);
        //.获取预约单对应的车辆信息
        Long customerCarId = appointDetailDTO.getCustomerCarId();
        CustomerCar customerCar = customerCarService.selectById(customerCarId);
        if (customerCar != null) {
            CustomerCarDTO customerCarDTO = new CustomerCarDTO();
            BeanUtils.copyProperties(customerCar, customerCarDTO);
            appointDetailDTO.setCustomerCarDTO(customerCarDTO);
        }
        return Result.wrapSuccessfulResult(appointDetailDTO);
    }
}
