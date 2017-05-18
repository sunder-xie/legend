package com.tqmall.legend.facade.marketing.gather.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.RpcCustomerInfoService;
import com.tqmall.cube.shop.RpcTagService;
import com.tqmall.cube.shop.param.TagCustomerParam;
import com.tqmall.cube.shop.result.customer.SimpleCustomerInfoDTO;
import com.tqmall.cube.shop.result.tag.CustomerInfoDTO;
import com.tqmall.cube.shop.result.tag.TagSummaryDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopDTO;
import com.tqmall.dandelion.wechat.client.wechat.shop.WeChatShopService;
import com.tqmall.legend.biz.account.AccountCouponService;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.CouponInfoService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerFeedbackService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.marketing.gather.GatherCouponConfigService;
import com.tqmall.legend.biz.marketing.gather.GatherCouponFlowDetailService;
import com.tqmall.legend.biz.marketing.gather.GatherCustomerNoteService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.CustomerInfoService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.NoteType;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.entity.account.vo.AccountCouponVo;
import com.tqmall.legend.entity.account.vo.CouponVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerFeedback;
import com.tqmall.legend.entity.marketing.gather.GatherCouponConfig;
import com.tqmall.legend.entity.marketing.gather.GatherCouponFlowDetail;
import com.tqmall.legend.entity.marketing.gather.GatherCustomerNote;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.NoteInfo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.enums.coupon.AccountCouponSourceEnum;
import com.tqmall.legend.enums.marketing.gather.GatherTypeEnum;
import com.tqmall.legend.enums.marketing.gather.OperateTypeEnum;
import com.tqmall.legend.enums.marketing.gather.ReceiveCouponResultEnum;
import com.tqmall.legend.enums.wechat.ShopWechatStatusEnum;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.facade.customer.vo.AllotUserVo;
import com.tqmall.legend.facade.marketing.gather.GatherPlanFacade;
import com.tqmall.legend.facade.marketing.gather.adaptor.CustomerTypeNumConvertor;
import com.tqmall.legend.facade.marketing.gather.adaptor.GatherCouponConfigConvertor;
import com.tqmall.legend.facade.marketing.gather.adaptor.GatherCustomerConvertor;
import com.tqmall.legend.facade.marketing.gather.bo.GatherCustomerNoteBO;
import com.tqmall.legend.facade.marketing.gather.param.FeedbackByPhoneParam;
import com.tqmall.legend.facade.marketing.gather.vo.CustomerTypeNum;
import com.tqmall.legend.facade.marketing.gather.vo.GatherCouponConfigVo;
import com.tqmall.legend.facade.marketing.gather.vo.GatherCustomerVO;
import com.tqmall.legend.facade.marketing.gather.vo.ReceiveCouponResultVo;
import com.tqmall.legend.facade.sms.SmsSendFacade;
import com.tqmall.legend.facade.sms.bo.MarketingSmsTempBO;
import com.tqmall.legend.facade.sms.newsms.SendPositionEnum;
import com.tqmall.legend.facade.sms.newsms.SmsCenter;
import com.tqmall.legend.facade.sms.newsms.param.PreSendParam;
import com.tqmall.legend.facade.sms.newsms.param.SendParam;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.cubecustomerinfo.param.CubeCustomerInfoRequest;
import com.tqmall.search.dubbo.client.legend.cubecustomerinfo.result.CubeCustomerInfoDTO;
import com.tqmall.search.dubbo.client.legend.cubecustomerinfo.service.CubeCustomerInfoService;
import com.tqmall.wheel.support.rpc.result.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by xin on 2016/12/16.
 */
@Slf4j
@Service
public class GatherPlanFacadeImpl implements GatherPlanFacade {

    @Autowired
    private RpcTagService rpcTagService;
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private ShopNoteInfoService shopNoteInfoService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private CustomerFeedbackService customerFeedbackService;
    @Autowired
    private GatherCustomerNoteService gatherCustomerNoteService;
    @Autowired
    private RpcCustomerInfoService rpcCustomerInfoService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private GatherCouponConfigService gatherCouponConfigService;
    @Autowired
    private CustomerUserRelFacade customerUserRelFacade;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private GatherCouponFlowDetailService gatherCouponFlowDetailService;
    @Autowired
    private WeChatShopService weChatShopService;
    @Autowired
    private SmsSendFacade smsSendFacade;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private SmsCenter smsCenter;
    @Autowired
    private CubeCustomerInfoService cubeCustomerInfoService;
    @Autowired
    private ShopManagerService shopManagerService;

    /**
     * 查询SA下不同分类客户数
     *
     * @param shopId
     * @param userId
     * @return
     */
    @Override
    public CustomerTypeNum getCustomerTypeNum(final Long shopId, final Long userId) {

        return new BizTemplate<CustomerTypeNum>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected CustomerTypeNum process() throws BizException {
                RpcResult<TagSummaryDTO> result = rpcTagService.getAllSummary(shopId, userId);
                if (result == null || !result.isSuccess()) {
                    throw new BizException("查询不同分类客户数异常");
                }
                return CustomerTypeNumConvertor.convert(result.getData());
            }
        }.execute();
    }

    /**
     * 查询不同分类客户分页列表
     *
     * @param shopId
     * @param userId
     * @param customerType
     * @return
     */
    @Override
    public Page<GatherCustomerVO> getGatherCustomerPage(final Long shopId, final Long userId, final String customerType, final Pageable pageable) {
        return new BizTemplate<Page<GatherCustomerVO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Page<GatherCustomerVO> process() throws BizException {
                TagCustomerParam param = new TagCustomerParam();
                param.setShopId(shopId);
                param.setUserId(userId);
                param.setPageNum(pageable.getPageNumber());
                param.setPageSize(pageable.getPageSize());

                Page<GatherCustomerVO> page = new DefaultPage<>(Collections.<GatherCustomerVO>emptyList());
                if (customerType == null) {
                    return page;
                }
                com.tqmall.wheel.support.data.Page<CustomerInfoDTO> customerInfoDTOPage = getCustomerInfoByType(param, customerType);;
                return GatherCustomerConvertor.convertPage(customerInfoDTOPage);
            }
        }.execute();
    }

    private com.tqmall.wheel.support.data.Page<CustomerInfoDTO> getCustomerInfoByType(TagCustomerParam param, String customerType) {
        RpcResult<com.tqmall.wheel.support.data.Page<CustomerInfoDTO>> pageRpcResult = null;
        switch (customerType) {
            case "hasMobileNum" ://有车主电话客户
                pageRpcResult = rpcTagService.listMobileCustomer(param);
                break;
            case "hasMemberNum" ://会员客户
                pageRpcResult = rpcTagService.listMemberCustomer(param);
                break;
            case "noneMemberNum" ://非会员客户
                pageRpcResult = rpcTagService.listNonMemberCustomer(param);
                break;
            case "sleepNum" ://休眠客户
                pageRpcResult = rpcTagService.listLazyCustomer(param);
                break;
            case "lostNum" ://流失客户
                pageRpcResult = rpcTagService.listLostCustomer(param);
                break;
            case "activeNum" ://活跃客户
                pageRpcResult = rpcTagService.listActiveCustomer(param);
                break;
            case "auditingNoteNum" ://年检提醒
                pageRpcResult = rpcTagService.listCheckupNoteCustomer(param);
                break;
            case "maintainNoteNum" ://保养提醒
                pageRpcResult = rpcTagService.listKeepUpNoteCustomer(param);
                break;
            case "insuranceNoteNum" ://保险提醒
                pageRpcResult = rpcTagService.listInsuranceNoteCustomer(param);
                break;
            case "birthdayNoteNum" : //生日提醒
                pageRpcResult = rpcTagService.listBirthDayNoteCustomer(param);
                break;
            case "lostNoteNum" : //流失客户提醒
                pageRpcResult = rpcTagService.listLostNoteCustomer(param);
                break;
            case "visitNoteNum" : //回访提醒数
                pageRpcResult = rpcTagService.listVisitBackCustomer(param);
                break;
            default:
                return null;
        }
        if (pageRpcResult == null || !pageRpcResult.isSuccess()) {
            throw new BizException("查询客户列表异常");
        }
        return pageRpcResult.getData();
    }

    /**
     * 电话回访
     *
     * @param param
     * @param userInfo
     * @return
     */
    @Override
    @Transactional
    public Boolean feedbackByPhone(final FeedbackByPhoneParam param, final UserInfo userInfo) {
        return new BizTemplate<Boolean>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(userInfo, "电话回访操作人信息不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Boolean process() throws BizException {
                Long customerCarId = param.getCustomerCarId();
                Long noteInfoId = param.getNoteInfoId();
                Long accountId = param.getAccountId();
                Long couponInfoId = param.getCouponInfoId();
                Long shopId = userInfo.getShopId();

                CustomerInfo customerInfo = customerInfoService.getCustomerInfo(shopId, customerCarId);
                if (customerInfo == null) {
                    throw new BizException("客户车辆不存在");
                }

                // 创建回访单
                Long feedbackId = saveFeedback(customerInfo, param, userInfo);
                if (feedbackId == null || feedbackId <= 0) {
                    throw new BizException("创建回访单失败");
                }
                // 发券
                Long accountCouponId = null;
                if (couponInfoId != null && couponInfoId > 0 && accountId != null && accountId > 0) {
                    AccountCoupon accountCoupon = grantCoupon(accountId, couponInfoId, userInfo, AccountCouponSourceEnum.GATHER_PANHUO_PHONE);
                    if (accountCoupon != null) {
                        accountCouponId = accountCoupon.getId();
                    }
                }

                // 处理提醒
                if (noteInfoId != null && noteInfoId > 0) {
                    List<Long> noteInfoIdList = Lists.newArrayList(noteInfoId);
                    shopNoteInfoService.batchHandleNoteInfo(shopId, noteInfoIdList, 1, userInfo.getName());
                }

                // 记录集客行为盘活客户
                GatherCustomerNoteBO gatherCustomerNoteBO = new GatherCustomerNoteBO();
                gatherCustomerNoteBO.setCustomerCarId(customerCarId);
                gatherCustomerNoteBO.setGatherTypeEnum(GatherTypeEnum.PANHUO);
                gatherCustomerNoteBO.setOperateTypeEnum(OperateTypeEnum.PHONE);
                gatherCustomerNoteBO.setRelId(feedbackId);
                gatherCustomerNoteBO.setAccountCouponId(accountCouponId);
                gatherCustomerNoteBO.setUserInfo(userInfo);
                GatherCustomerNote gatherCustomerNote = saveGatherCustomer(gatherCustomerNoteBO);
                return gatherCustomerNote.getId() != null && gatherCustomerNote.getId() > 0;
            }
        }.execute();
    }

    /**
     * 发送短信
     *
     * @param key
     * @param noteType
     * @return
     */
    @Override
    @Transactional
    public Boolean sendSms(final String key, final Long couponInfoId, final Integer noteType, final UserInfo userInfo) {
        return new BizTemplate<Boolean>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Boolean process() throws BizException {
                Long shopId = userInfo.getShopId();

                SendParam sendParam = new SendParam(shopId, userInfo.getUserId(), userInfo.getName(), key);
                // 发送短信
                Map<String, Long> carLicenseSmsMap = smsSendFacade.sendForGather(sendParam);
                if (CollectionUtils.isEmpty(carLicenseSmsMap)) {
                    return false;
                }

                Set<String> carLicenseSet = carLicenseSmsMap.keySet();

                // 处理提醒
                if (noteType != null) {
                    List<NoteInfo> noteInfos = shopNoteInfoService.listUnhandled(shopId, noteType, carLicenseSet);
                    List<Long> noteInfoIds = Lists.transform(noteInfos, new Function<NoteInfo, Long>() {
                        @Override
                        public Long apply(NoteInfo input) {
                            return input.getId();
                        }
                    });
                    shopNoteInfoService.batchHandleNoteInfo(shopId,noteInfoIds ,0, sendParam.getOperator());
                }

                String[] carLicenseArr = new String[carLicenseSet.size()];
                carLicenseArr = carLicenseSet.toArray(carLicenseArr);
                List<CustomerCar> customerCarList = customerCarService.findCustomerCarsByLicense(shopId, carLicenseArr);
                if (!CollectionUtils.isEmpty(customerCarList)) {
                    for (CustomerCar customerCar : customerCarList) {
                        String license = customerCar.getLicense();
                        Long customerId = customerCar.getCustomerId();
                        Long customerCarId = customerCar.getId();

                        // 发券
                        Long accountCouponId = null;
                        if (couponInfoId != null && couponInfoId > 0) {
                            AccountInfo accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
                            if (accountInfo != null) {
                                AccountCoupon accountCoupon = grantCoupon(accountInfo.getId(), couponInfoId, userInfo, AccountCouponSourceEnum.GATHER_PANHUO_SMS);
                                if (accountCoupon != null) {
                                    accountCouponId = accountCoupon.getId();
                                }
                            }
                        }

                        // 记录集客行为盘活客户
                        GatherCustomerNoteBO gatherCustomerNoteBO = new GatherCustomerNoteBO();
                        gatherCustomerNoteBO.setCustomerCarId(customerCarId);
                        gatherCustomerNoteBO.setAllotSn(UUID.randomUUID().toString());
                        gatherCustomerNoteBO.setGatherTypeEnum(GatherTypeEnum.PANHUO);
                        gatherCustomerNoteBO.setOperateTypeEnum(OperateTypeEnum.SMS);
                        gatherCustomerNoteBO.setRelId(carLicenseSmsMap.get(license));
                        gatherCustomerNoteBO.setAccountCouponId(accountCouponId);
                        gatherCustomerNoteBO.setUserInfo(userInfo);
                        saveGatherCustomer(gatherCustomerNoteBO);
                    }
                }
                return true;
            }
        }.execute();
    }

    /**
     * 计算全部客户所需发送短信数
     *
     * @param shopId
     * @param userId
     * @param customerType
     * @param template
     * @return
     */
    @Override
    public MarketingSmsTempBO getAllCustomerSms(final Long shopId, final Long userId, final String customerType, final String template) {
        return new BizTemplate<MarketingSmsTempBO>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0);
                Assert.hasText(template, "模板不能为空");
                Assert.hasText(customerType, "客户类型不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected MarketingSmsTempBO process() throws BizException {
                List<Long> totalCustomerCarIdList = Lists.newArrayList();
                int pageNum = 1;
                TagCustomerParam param = new TagCustomerParam();
                while (true) {
                    param.setShopId(shopId);
                    param.setUserId(userId);
                    param.setPageNum(pageNum);
                    param.setPageSize(100);
                    com.tqmall.wheel.support.data.Page<CustomerInfoDTO> page = getCustomerInfoByType(param, customerType);
                    if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                        break;
                    }
                    List<Long> customerCarIdList = Lists.transform(page.getRecords(), new Function<CustomerInfoDTO, Long>() {
                        @Override
                        public Long apply(CustomerInfoDTO customerInfoDTO) {
                            return customerInfoDTO.getCarId();
                        }
                    });
                    totalCustomerCarIdList.addAll(customerCarIdList);
                    pageNum++;
                    if (pageNum > page.getTotalPages() || totalCustomerCarIdList.size() >= page.getTotalNum()) {
                        break;
                    }
                }
                PreSendParam preSendParam = new PreSendParam(template, shopId, totalCustomerCarIdList, SendPositionEnum.GATHER_PALN_SMS.getCode());
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    /**
     * 计算搜索客户所需发送短信数
     *
     * @param shopId
     * @param userId
     * @param searchKey
     * @param template
     * @return
     */
    @Override
    public MarketingSmsTempBO getSearchCustomerSms(final Long shopId, final Long userId, final String searchKey, final String template) {
        return new BizTemplate<MarketingSmsTempBO>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected MarketingSmsTempBO process() throws BizException {
                List<Long> totalCustomerCarIdList = Lists.newArrayList();

                CubeCustomerInfoRequest cubeCustomerInfoRequest = new CubeCustomerInfoRequest();
                cubeCustomerInfoRequest.setShopId(shopId);
                if (userId != null && userId > 0) {
                    cubeCustomerInfoRequest.setUserIds(Lists.newArrayList(userId));
                }
                if (StringUtil.isNotStringEmpty(searchKey)) {
                    cubeCustomerInfoRequest.setSearchKey(searchKey);
                }
                int pageNum = 0; // 搜索page从0开始
                int pageSize = 100;
                while (true) {
                    PageableRequest pageableRequest = new PageableRequest(pageNum, pageSize);
                    com.tqmall.search.common.result.Result<Page<CubeCustomerInfoDTO>> result = cubeCustomerInfoService.getCubeCustomerInfos(cubeCustomerInfoRequest, pageableRequest);
                    if (result == null || !result.isSuccess()) {
                        throw new BizException("盘活客户搜索客户异常");
                    }
                    Page<CubeCustomerInfoDTO> page = result.getData();
                    if (page == null || CollectionUtils.isEmpty(page.getContent())) {
                        break;
                    }
                    List<Long> customerCarIdList = Lists.transform(page.getContent(), new Function<CubeCustomerInfoDTO, Long>() {
                        @Override
                        public Long apply(CubeCustomerInfoDTO cubeCustomerInfoDTO) {
                            return cubeCustomerInfoDTO.getCustomerCarId();
                        }
                    });

                    totalCustomerCarIdList.addAll(customerCarIdList);
                    pageNum++;
                    if (pageNum >= page.getTotalPages() || totalCustomerCarIdList.size() >= page.getTotalElements()) {
                        break;
                    }
                }
                PreSendParam preSendParam = new PreSendParam(template, shopId, totalCustomerCarIdList, SendPositionEnum.GATHER_PALN_SMS.getCode());
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    /**
     * 发券
     */
    private AccountCoupon grantCoupon(Long accountId, Long couponInfoId, UserInfo userInfo, AccountCouponSourceEnum accountCouponSourceEnum) {
        AccountCouponVo accountCouponVo = new AccountCouponVo();
        accountCouponVo.setAccountId(accountId);
        accountCouponVo.setShopId(userInfo.getShopId());
        accountCouponVo.setCreator(userInfo.getUserId());
        CouponVo couponVo = new CouponVo();
        couponVo.setId(couponInfoId);
        couponVo.setNum(1);
        List<CouponVo> couponVos = Lists.newArrayList(couponVo);
        accountCouponVo.setCouponVos(couponVos);
        return accountCouponService.grant(accountCouponVo, accountCouponSourceEnum);
    }

    /**
     * 保存集客信息
     *
     * @param gatherCustomerNoteBO
     * @return 主键id
     */
    @Override
    public GatherCustomerNote saveGatherCustomer(final GatherCustomerNoteBO gatherCustomerNoteBO) {
        return new BizTemplate<GatherCustomerNote>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(gatherCustomerNoteBO);
                Assert.isTrue(gatherCustomerNoteBO.getCustomerCarId() != null && gatherCustomerNoteBO.getCustomerCarId() > 0);
                Assert.notNull(gatherCustomerNoteBO.getGatherTypeEnum());
                Assert.notNull(gatherCustomerNoteBO.getOperateTypeEnum());
                Assert.notNull(gatherCustomerNoteBO.getUserInfo());
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected GatherCustomerNote process() throws BizException {
                UserInfo userInfo = gatherCustomerNoteBO.getUserInfo();
                Long shopId = userInfo.getShopId();
                Long customerCarId = gatherCustomerNoteBO.getCustomerCarId();
                List<Long> customerCarIds = Lists.newArrayList(customerCarId);
                Result<Map<Long, SimpleCustomerInfoDTO>> result = rpcCustomerInfoService.getSimpleCustomerInfoByCarIds(shopId, customerCarIds);
                if (result == null || !result.isSuccess()) {
                    throw new BizException("客户车辆不存在");
                }
                Map<Long, SimpleCustomerInfoDTO> simpleCustomerInfoDTOMap = result.getData();
                if (CollectionUtils.isEmpty(simpleCustomerInfoDTOMap)) {
                    throw new BizException("客户车辆不存在");
                }
                SimpleCustomerInfoDTO simpleCustomerInfo = simpleCustomerInfoDTOMap.get(customerCarId);
                if (simpleCustomerInfo == null) {
                    throw new BizException("客户车辆不存在");
                }

                GatherCustomerNote gatherCustomerNote = new GatherCustomerNote();
                gatherCustomerNote.setCreator(userInfo.getUserId());
                gatherCustomerNote.setModifier(userInfo.getUserId());
                gatherCustomerNote.setCreatorName(userInfo.getName());
                gatherCustomerNote.setShopId(userInfo.getShopId());
                gatherCustomerNote.setCustomerCarId(simpleCustomerInfo.getCustomerCarId());
                gatherCustomerNote.setCustomerId(simpleCustomerInfo.getCustomerId());
                gatherCustomerNote.setCustomerName(simpleCustomerInfo.getCustomerName());
                gatherCustomerNote.setCustomerMobile(simpleCustomerInfo.getCustomerMobile());
                gatherCustomerNote.setCarLicense(simpleCustomerInfo.getCarLicense());
                if (StringUtil.isStringEmpty(gatherCustomerNoteBO.getAllotSn())) {
                    gatherCustomerNote.setAllotSn(UUID.randomUUID().toString());
                } else {
                    gatherCustomerNote.setAllotSn(gatherCustomerNoteBO.getAllotSn());
                }
                gatherCustomerNote.setGatherType(gatherCustomerNoteBO.getGatherTypeEnum().getValue());
                gatherCustomerNote.setOperateType(gatherCustomerNoteBO.getOperateTypeEnum().getValue());
                gatherCustomerNote.setGmtCreate(new Date());

                // 查询车辆对应的服务顾问
                AllotUserVo allotUser = customerUserRelFacade.getAllotUserByCarId(customerCarId);
                if (allotUser != null) {
                    gatherCustomerNote.setUserId(allotUser.getUserId());
                    gatherCustomerNote.setUserName(allotUser.getUserName());
                }

                gatherCustomerNote.setRelId(gatherCustomerNoteBO.getRelId());
                gatherCustomerNote.setAccountCouponId(gatherCustomerNoteBO.getAccountCouponId());
                return gatherCustomerNoteService.save(gatherCustomerNote);
            }
        }.execute();

    }

    /**
     * 新建回访单
     * @param param
     * @param userInfo
     * @return
     */
    private Long saveFeedback(CustomerInfo customerInfo, FeedbackByPhoneParam param, UserInfo userInfo) {
        Long noteInfoId = param.getNoteInfoId();
        String nextVisitTime = param.getNextVisitTime();
        String content = param.getContent();
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();

        CustomerFeedback customerFeedback = new CustomerFeedback();
        customerFeedback.setShopId(shopId);
        customerFeedback.setCreator(userId);
        customerFeedback.setModifier(userId);
        customerFeedback.setCustomerCarId(customerInfo.getCustomerCarId());
        customerFeedback.setCustomerFeedback(content);
        customerFeedback.setCarLicense(customerInfo.getCarLicense());
        customerFeedback.setCarBrandId(customerInfo.getCarBrandId());
        customerFeedback.setCarBrandName(customerInfo.getCarBrand());
        customerFeedback.setCarSeriesId(customerInfo.getCarSeriesId());
        customerFeedback.setCarSeriesName(customerInfo.getCarSeries());
        customerFeedback.setMobile(customerInfo.getMobile());
        customerFeedback.setCustomerId(customerInfo.getCustomerId());
        customerFeedback.setCustomerName(customerInfo.getCustomerName());

        Long orderId = 0L;
        if (noteInfoId != null && noteInfoId > 0) {
            NoteInfo noteInfo = shopNoteInfoService.getById(shopId, noteInfoId);
            if (noteInfo != null) {
                if (noteInfo.getNoteType().equals(NoteType.VISIT_NOTE_TYPE)) {
                    orderId = noteInfo.getRelId();
                    if (orderId != null && orderId > 0) {
                        OrderInfo orderInfo = orderInfoService.selectById(orderId, shopId);
                        if (orderInfo != null) {
                            customerFeedback.setFinishTime(orderInfo.getFinishTime());
                        }
                    }
                }
                customerFeedback.setNoteInfoId(noteInfoId);
                customerFeedback.setNoteType(noteInfo.getNoteType());
            }
        }
        customerFeedback.setOrderId(orderId);
        customerFeedback.setVisitorName(userInfo.getName());
        customerFeedback.setVisitMethod("电话回访");
        customerFeedback.setVisitTime(new Date());
        if (StringUtils.isNotBlank(nextVisitTime)) {
            customerFeedback.setNextVisitTime(DateUtil.convertStringToDateYMD(nextVisitTime));
        }
        return customerFeedbackService.save(customerFeedback);
    }


    @Override
    public GatherCouponConfig saveGatherCouponConfig(GatherCouponConfig gatherCouponConfig, UserInfo userInfo) {
        Assert.notNull(gatherCouponConfig);
        if(gatherCouponConfig.getId()==null){
            //新增
            gatherCouponConfig.setCreator(userInfo.getUserId());
            gatherCouponConfig.setShopId(userInfo.getShopId());
            gatherCouponConfig.setGainNum(0);//初始已经领取数量为0
            gatherCouponConfig.setAccessStatus(0);//访问状态:0未访问,1已访问
        }
        gatherCouponConfig.setModifier(userInfo.getUserId());
        if (gatherCouponConfig.getId()==null){
            //新增
            gatherCouponConfigService.insert(gatherCouponConfig);
        } else {
            //更新
            gatherCouponConfigService.updateById(gatherCouponConfig);
        }
        return gatherCouponConfig;
    }

    @Override
    public GatherCouponConfigVo getGatherCouponInfo(Long gatherCouponConfigId) {
        GatherCouponConfig gatherCouponConfig = gatherCouponConfigService.selectById(gatherCouponConfigId);
        if(gatherCouponConfig==null){
            return null;
        }
        GatherCouponConfigVo gatherCouponConfigVo = new GatherCouponConfigVo();
        BeanUtils.copyProperties(gatherCouponConfig, gatherCouponConfigVo);
        Long shopId = gatherCouponConfig.getShopId();
        //.获取门店信息
        Shop shop = shopService.selectById(shopId);
        if(shop==null){
            throw new BizException("id为"+shopId+"的门店不存在");
        }
        gatherCouponConfigVo.setShopName(shop.getName());
        gatherCouponConfigVo.setShopAddress(shop.getAddress());

        //.获取门店微信公众号二维码
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        Result<ShopDTO> shopDTOResult = weChatShopService.selectShopByUcShopId(userGlobalId);
        log.info("[dubbo]调ddlwechat查询门店信息,userGlobalId:{},success:{}", userGlobalId, shopDTOResult.isSuccess());
        ShopDTO shopDTO = shopDTOResult.getData();
        if (shopDTO != null && ShopWechatStatusEnum.REGISTERED.getValue().equals(shopDTO.getShopStatus())) {
            gatherCouponConfigVo.setQrCode(shopDTO.getShopQrcode());
        }

        //.获取优惠券信息
        Long couponInfoId = gatherCouponConfig.getCouponInfoId();
        CouponInfo couponInfo = couponInfoService.selectById(couponInfoId,shopId);
        if(couponInfo==null){
            throw new BizException("优惠券类型信息不存在");
        }
        gatherCouponConfigVo.setCouponName(couponInfo.getCouponName());
        gatherCouponConfigVo.setValidityPeriodStr(couponInfo.getValidityPeriodStr());
        return gatherCouponConfigVo;
    }

    @Override
    public ReceiveCouponResultVo grantCoupon(String mobile, Long gatherCouponConfigId) {
        GatherCouponConfig gatherCouponConfig = gatherCouponConfigService.selectById(gatherCouponConfigId);
        if (gatherCouponConfig == null) {
            throw new BizException("缺少优惠券信息");
        }
        //.查询优惠券
        Long couponInfoId = gatherCouponConfig.getCouponInfoId();
        Long shopId = gatherCouponConfig.getShopId();
        CouponInfo couponInfo = couponInfoService.selectById(couponInfoId, shopId);
        if (couponInfo == null) {
            throw new BizException("优惠券信息不存在");
        }

        //.客户信息处理
        List<Customer> customerList = customerService.getCustomerByMobile(mobile,shopId);
        Customer receiveCustomer = null;//当前领券客户
        Integer isNewCustomer = 1;//是否新客户:0否1是
        if(CollectionUtils.isEmpty(customerList)){
            //.新客户,创建客户信息
            isNewCustomer = 1;
            receiveCustomer = new Customer();
            receiveCustomer.setMobile(mobile);
            receiveCustomer.setShopId(shopId);
            receiveCustomer.setCustomerName("");
            log.info("[车主领券]客户信息不存在,创建客户信息,入参:{}", LogUtils.objectToString(receiveCustomer));
            customerService.add(receiveCustomer);
        } else {
            //查出多条客户信息时取最近创建的1条
            Collections.sort(customerList, new Comparator<Customer>() {
                @Override
                public int compare(Customer o1, Customer o2) {
                    return o2.getGmtCreate().compareTo(o1.getGmtCreate());
                }
            });
            receiveCustomer = customerList.get(0);
            isNewCustomer = 0;
        }

        //.处理账户信息
        Long receiveCustomerId = receiveCustomer.getId();
        AccountInfo accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, receiveCustomerId);
        if(accountInfo==null){
            log.info("[车主领券]账户信息不存在,创建账户信息,shopId:{},customerId{}",shopId,receiveCustomerId);
            com.tqmall.legend.common.Result result= accountInfoService.generateAccountInfo(shopId,receiveCustomerId);
            if(!result.isSuccess()){
                log.error("[车主领券]账户信息不存在,创建账户信息失败,shopId:{},customerId{},result:{}",shopId,receiveCustomerId,result);
                throw new BizException("创建账户失败");
            }
            accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, receiveCustomerId);
        }

        //.领取集客优惠券限制检查
        ReceiveCouponResultVo checkResult = checkGatherCoupon(mobile,gatherCouponConfig);
        if (checkResult != null) {
            //不为空表示校验不通过
            return checkResult;
        }

        //.领取优惠券
        Long accountId = accountInfo.getId();
        AccountCouponVo accountCouponVo = new AccountCouponVo();
        accountCouponVo.setShopId(shopId);
        final CouponVo couponVo = new CouponVo();
        couponVo.setId(couponInfoId);
        couponVo.setNum(1);//领取一张
        List<CouponVo> couponVoList = new ArrayList<CouponVo>() {{
            add(couponVo);
        }};
        accountCouponVo.setCouponVos(couponVoList);
        accountCouponVo.setAccountId(accountId);
        AccountCoupon accountCoupon = accountCouponService.grant(accountCouponVo, AccountCouponSourceEnum.GATHER_NEW_CUSTOMER);

        //.更新领取数量
        gatherCouponConfig.setGainNum(gatherCouponConfig.getGainNum() + 1);
        gatherCouponConfigService.updateById(gatherCouponConfig);

        //.计入集客优惠券流水表
        Customer referCustomer = customerService.selectById(gatherCouponConfig.getCustomerId());//源客户
        GatherCouponFlowDetail gatherCouponFlowDetail = GatherCouponConfigConvertor.convert(gatherCouponConfig, referCustomer, receiveCustomer, accountCoupon.getId(),isNewCustomer);
        gatherCouponFlowDetailService.add(gatherCouponFlowDetail);

        //.组装返回结果
        ReceiveCouponResultVo receiveCouponResultVo = new ReceiveCouponResultVo();
        receiveCouponResultVo.setResultCode(ReceiveCouponResultEnum.SUCCESS.getValue());
        receiveCouponResultVo.setMobile(mobile);
        receiveCouponResultVo.setPerAccountNum(gatherCouponConfig.getPerAccountNum());
        int surplusNum = gatherCouponConfig.getTotalCouponNum() - gatherCouponConfig.getGainNum();//券剩余数量
        receiveCouponResultVo.setSurplusNum(surplusNum);
        if(referCustomer.getId().compareTo(receiveCustomer.getId())==0){
            //源客户是当前领券客户
            receiveCouponResultVo.setIsSourceCustomer(1);
        }
        return receiveCouponResultVo;
    }

    /**
     * 领取集客优惠券限制检查
     * @param mobile
     * @param gatherCouponConfig
     * @return
     */
    private ReceiveCouponResultVo checkGatherCoupon(String mobile, GatherCouponConfig gatherCouponConfig) {
        ReceiveCouponResultVo receiveCouponResultVo = new ReceiveCouponResultVo();
        int surplusNum = gatherCouponConfig.getTotalCouponNum() - gatherCouponConfig.getGainNum();//券剩余数量
        if (surplusNum < 1) {
            //余券不足
            receiveCouponResultVo.setResultCode(ReceiveCouponResultEnum.GET_OVER.getValue());
            receiveCouponResultVo.setMobile(mobile);
            receiveCouponResultVo.setSurplusNum(0);
            return receiveCouponResultVo;
        }
        int perAccountNum = gatherCouponConfig.getPerAccountNum();
        int receivedNum = gatherCouponFlowDetailService.selectCount(mobile, gatherCouponConfig.getId());
        if (perAccountNum <= receivedNum) {
            //达到单用户领取上限
            receiveCouponResultVo.setResultCode(ReceiveCouponResultEnum.OVER_ACCOUNT_LIMIT.getValue());
            receiveCouponResultVo.setMobile(mobile);
            receiveCouponResultVo.setSurplusNum(surplusNum);
            return receiveCouponResultVo;
        }
        return null;
    }

    @Override
    public void accessDeal(Long gatherCouponConfigId) {
        GatherCouponConfig gatherCouponConfig = gatherCouponConfigService.selectById(gatherCouponConfigId);
        if (gatherCouponConfig == null) {
            return;
        }
        if (gatherCouponConfig.getAccessStatus() != null && gatherCouponConfig.getAccessStatus() == 1) {
            //.非首次访问,不需要处理
            return;
        }

        //.插集客信息表
        long shopId = gatherCouponConfig.getShopId();
        long userId = gatherCouponConfig.getCreator();
        ShopManager shopManager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
        UserInfo userInfo = new UserInfo();
        userInfo.setShopId(shopId);
        userInfo.setUserId(userId);
        userInfo.setName(shopManager.getName());
        GatherCustomerNoteBO gatherCustomerNoteBO = GatherCouponConfigConvertor.convert(gatherCouponConfig, userInfo);
        GatherCustomerNote gatherCustomerNote = this.saveGatherCustomer(gatherCustomerNoteBO);
        if (gatherCustomerNote == null) {
            throw new BizException("保存信息出错");
        }
        gatherCouponConfig.setGatherTime(gatherCustomerNote.getGmtCreate());
        gatherCouponConfig.setGatherCustomerNoteId(gatherCustomerNote.getId());

        //.更新集客优惠券的访问状态
        gatherCouponConfig.setAccessStatus(1);//访问状态:0未访问,1已访问
        gatherCouponConfigService.updateById(gatherCouponConfig);
    }
}
