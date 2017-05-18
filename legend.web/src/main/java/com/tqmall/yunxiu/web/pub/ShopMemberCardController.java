package com.tqmall.yunxiu.web.pub;

import com.tqmall.legend.biz.account.AccountCouponService;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.MemberCardService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.app.member.ShopMemberCard;
import com.tqmall.legend.entity.app.member.ShopMemberCardService;
import com.tqmall.legend.entity.app.member.ShopMemberCardServiceDetail;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.pub.member.MemberCar;
import com.tqmall.legend.entity.pub.member.MemberCarVo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by jason on 15/9/10.
 * app端门店会员卡券包信息controller
 * 会员信息
 */
@RequestMapping("pub/member")
@Controller
public class ShopMemberCardController extends BaseController {

    Logger logger = LoggerFactory.getLogger(ShopMemberCardController.class);


    @Autowired
    private CustomerCarService customerCarService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private MemberCardService memberCardService;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private AccountCouponService accountCouponService;


    /**
     * 获得会员信息
     * @param licenseList
     * @param mobile 手机号
     * @return
     */
    @RequestMapping(value = "info",method = RequestMethod.GET)
    @ResponseBody
    public Result getUserInfo(@RequestParam(value = "mobile", required = true )String mobile,
                              @RequestParam(value = "licenseList[]", required = true )List<String> licenseList){
        logger.info("进入获得会员信息接口!mobile:{},licenseList:{}",mobile,licenseList);
        if ( StringUtil.isStringEmpty(mobile) || "null".equals(mobile)) {
            return Result.wrapErrorResult("", "手机号不正确!");
        }
        MemberCarVo memberCarVo = new MemberCarVo();
        List<MemberCar> memberCarList = new ArrayList<>();
        //licenseList为空不执行
        if (!CollectionUtils.isEmpty(licenseList)) {
            Map<String, CustomerCar> carMap = new HashMap<>();

            Map map = new HashMap(1);
            mobile = mobile.trim();
            map.put("contactMobile",mobile);
            List<Customer> customerList = customerService.select(map);//客户信息
            if (!CollectionUtils.isEmpty(customerList)) {

                List customerIdsList = new ArrayList();
                //把customerId放到一个List中
                for (Customer customer : customerList) {
                    customerIdsList.add(customer.getId());
                }

                Map paramMap = new HashMap();
                paramMap.put("customerIds",customerIdsList);
                //根据客户IDs获得客户车辆信息,里面的车辆信息可能会重复
                List<CustomerCar> customerCarList = customerCarService.selectByCustomerId(paramMap);

                if (!CollectionUtils.isEmpty(customerCarList)) {
                    for (CustomerCar customerCar : customerCarList) {
                        //传递过来的车牌是否包含根据customerIds获得车牌信息
                        String license = customerCar.getLicense();
                        if (null != license) {
                            if (licenseList.contains(license)) {
                                //剔除重复的车辆信息
                                if (!carMap.containsKey(license)) {
                                    carMap.put(license, customerCar);
                                }
                            }
                        }
                    }
                }
                //循环carMap
                for (Map.Entry<String, CustomerCar> entry : carMap.entrySet()) {
                    CustomerCar customerCar = entry.getValue();
                    MemberCar memberCar = new MemberCar();
                    memberCar.setLicense(customerCar.getLicense());
                    memberCar.setCarBrandName(customerCar.getCarBrand());
                    memberCar.setCarSeriesName(customerCar.getCarSeries());
                    memberCar.setCarCompany(customerCar.getCarCompany());
                    memberCar.setImportInfo(customerCar.getImportInfo());
                    memberCar.setMileage(customerCar.getMileage());
                    memberCar.setLatestMaintain(customerCar.getLatestMaintain());
                    memberCarList.add(memberCar);
                }
                memberCarVo.setMobile(mobile);
                memberCarVo.setList(memberCarList);
            }

        }

        if (!CollectionUtils.isEmpty(memberCarVo.getList())) {
            return Result.wrapSuccessfulResult(memberCarVo);
        } else {
            memberCarVo.setList(memberCarList);//会员信息为空的时候 list传个空过去
            return Result.wrapSuccessfulResult(memberCarVo);
        }
    }

    /**
     * create by jason 2015-09-11
     * 根据mobile和车牌获得会员卡列表信息
     * @param mobile 手机号
     * @param licenseList 车牌
     *
     */
    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public Result getMemberCardList(@RequestParam(value = "mobile",required = true)String mobile,
                                    @RequestParam(value = "license[]",required = true)List<String> licenseList){

        logger.info("进入获得会员卡券包信息列表! mobile:{},license list:{}",mobile,licenseList);
        try {
            //根据车牌,手机号获得客户车辆ID
            List<Long> carIdList =  customerCarService.selectByLicensesAndMobile(licenseList, mobile);
            if(CollectionUtils.isEmpty(carIdList)){
                return Result.wrapSuccessfulResult(null);
            }
            /**
             * 查询车辆列表
             */
            Map<String, Object> param = new HashMap<>();
            param.put("ids", carIdList);
            List<CustomerCar> customerCars = customerCarService.select(param);
            /**
             * 查询门店信息
             */
            Map<Long, Shop> shopMap = shopService.getAllShopMap();
            /**
             * 查询账户列表
             */
            List<Long> customerIds = new LinkedList<>();
            for (CustomerCar customerCar : customerCars) {
                customerIds.add(customerCar.getCustomerId());
            }
            List<AccountInfo> accounts = accountInfoService.getInfoByCustomerIds(customerIds);
            List<Long> accountIds = new LinkedList<>();
            for (AccountInfo account : accounts) {
                accountIds.add(account.getId());
            }
            /**
             * 查询优惠券列表
             */
            Map<String, Object> param2 = new HashMap<>();
            param2.put("accountIds",accountIds);
            List<AccountCoupon> accountCoupons = accountCouponService.select(param2);
            Map<Long,List<AccountCoupon>> couponMap = new HashMap<>();
            for (AccountCoupon accountCoupon : accountCoupons) {
                Long accountId = accountCoupon.getAccountId();
                List<AccountCoupon> coupons = couponMap.get(accountId);
                if(CollectionUtils.isEmpty(coupons)){
                    coupons = new LinkedList<>();
                    couponMap.put(accountId, coupons);
                }
                coupons.add(accountCoupon);
            }
            /**
             * 查询会员
             */
            List<ShopMemberCard> memberCardList = new LinkedList<>();
            if (!CollectionUtils.isEmpty(accountIds)) {
                logger.info("根据车牌,手机号获得客户账户ID:{}", accountIds);
                List<MemberCard> memberCards = memberCardService.findByAccountIds(accountIds);
                for (MemberCard memberCard : memberCards) {
                    Shop shop = shopMap.get(memberCard.getShopId());
                    ShopMemberCard shopMemberCard = new ShopMemberCard();
                    shopMemberCard.setMemberId(memberCard.getId());
                    shopMemberCard.setAmount(memberCard.getBalance());
                    shopMemberCard.setCardNumber(memberCard.getCardNumber());
                    if(shop != null) {
                        shopMemberCard.setUserGlobalId(shop.getUserGlobalId());
                        shopMemberCard.setShopName(shop.getName());
                    }
                    /**
                     * 组装优惠券列表
                     */
                    List<AccountCoupon> coupons = couponMap.get(memberCard.getAccountId());
                    if(!CollectionUtils.isEmpty(coupons)){
                        List<ShopMemberCardService> serviceList = new LinkedList();
                        for (AccountCoupon coupon : coupons) {
                            ShopMemberCardService shopMemberCardService = new ShopMemberCardService();
                            shopMemberCardService.setExpireTime(coupon.getExpireDate());
                            shopMemberCardService.setGmtCreate(coupon.getGmtCreate());
                            shopMemberCardService.setServiceCount(1l);
                            shopMemberCardService.setServiceId(coupon.getId());
                            shopMemberCardService.setServiceName(coupon.getCouponName());
                            shopMemberCardService.setType(Long.valueOf(coupon.getCouponType()));
                            serviceList.add(shopMemberCardService);
                        }
                        shopMemberCard.setServiceList(serviceList);
                    }
                    memberCardList.add(shopMemberCard);
                }
            }
            return Result.wrapSuccessfulResult(memberCardList);
        } catch (Exception e) {
            logger.error("获得会员卡券包列表信息异常!" + e);
            return Result.wrapErrorResult("","false");
        }
    }


    /**
     * create by jason 2015-09-11
     * 根据mobile和车牌获得会员卡详情信息
     * @param memberId 会员卡ID
     * @param type 1未使用 2过期 3已使用
     *
     */
    @RequestMapping(value = "detail",method = RequestMethod.GET)
    @ResponseBody
    public Result getMemberCardDetail(@RequestParam(value = "memberId",required = true)Long memberId,
                                      @RequestParam(value = "type",required = true)Long type,
                                      @RequestParam(value = "offset", required = true)Long offset,
                                      @RequestParam(value = "limit", required = false )Long limit){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("memberId",memberId);
        paramMap.put("offset",offset);
        if (limit == null || limit < 0l) {
            limit = 10l;
        }
        paramMap.put("limit",limit);
        logger.info("进入获得会员卡券包信息详情!参数:{}",paramMap);
        ShopMemberCardServiceDetail cardServiceDetail = new ShopMemberCardServiceDetail();
        try {
            MemberCard memberCard = memberCardService.findById(memberId);
            cardServiceDetail.setMemberId(memberCard.getId());
            cardServiceDetail.setAmount(memberCard.getBalance());
            cardServiceDetail.setCardNumber(memberCard.getCardNumber());
            Shop shop = shopService.selectById(memberCard.getShopId());
            cardServiceDetail.setUserGlobalId(shop.getUserGlobalId());
            cardServiceDetail.setShopName(shop.getName());
            paramMap.put("accountId", memberCard.getAccountId());
            Date now = new Date();
            if(type == 3l){
                paramMap.put("usedStatus",1l);
            }else if(type == 2l){
                paramMap.put("ltExpireDate",now);
                paramMap.put("gtEffectiveDate",now);
                paramMap.put("usedStatus",0l);
            }else if(type == 1l){
                paramMap.put("gtExpireDate",now);
                paramMap.put("ltEffectiveDate",now);
                paramMap.put("usedStatus",0l);
            }
            List<AccountCoupon> accountCoupons = accountCouponService.select(paramMap);
            List<ShopMemberCardService> serviceList = new LinkedList();
            for (AccountCoupon coupon : accountCoupons) {
                ShopMemberCardService shopMemberCardService = new ShopMemberCardService();
                shopMemberCardService.setExpireTime(coupon.getExpireDate());
                shopMemberCardService.setGmtCreate(coupon.getGmtCreate());
                shopMemberCardService.setServiceCount(1l);
                shopMemberCardService.setServiceId(coupon.getId());
                shopMemberCardService.setServiceName(coupon.getCouponName());
                shopMemberCardService.setType(Long.valueOf(coupon.getCouponType()));
                serviceList.add(shopMemberCardService);
            }
            cardServiceDetail.setServiceList(serviceList);
            logger.info("进入获得会员卡券包信息详情成功:{}",cardServiceDetail);
            return Result.wrapSuccessfulResult(cardServiceDetail);
        } catch (Exception e) {
            logger.error("获得会员卡券包信息详情异常!" + e);
            return Result.wrapErrorResult("","false");
        }
    }

    /**
     * 柯昌强 2015-11-24
     * 获取门店所有顾客手机号列表的集合
     * @param userGlobalId
     */
    @RequestMapping(value = "info_list",method = RequestMethod.GET)
    @ResponseBody
    public Result getMemberMobileList(@RequestParam(value = "userGlobalId", required = true)String userGlobalId){
        logger.info("获取门店所有会员手机号集合传入的userGlobalId：{}", userGlobalId);
        Map map = new HashMap();
        if(StringUtil.isStringEmpty(userGlobalId)){
            return Result.wrapErrorResult("", "userGlobalId为空!");
        }
        //去掉两端的空字符
        userGlobalId = userGlobalId.trim();
        map.put("userGlobalId", userGlobalId);
        //用userGlobalId查出shop的集合
        List<Shop> shopList = shopService.select(map);
        //验证userGlobalId是否有对应的shop对象
        if(CollectionUtils.isEmpty(shopList)){
            return Result.wrapErrorResult("", "userGlobalId没有对应的门店");
        }

        //门店id
        Long shopId = shopList.get(0).getId();
        map.clear();
        map.put("shopId", shopId);
        List<Customer> customerList = customerService.select(map);
        if(CollectionUtils.isEmpty(customerList)){
            return Result.wrapErrorResult("","门店没有客户信息");
        }
        //车主手机号集合
        List mobileList = new ArrayList();
        for(Customer customer : customerList){
            //取得customer中的手机号
            String mobile = customer.getMobile();
            //验证mobileList中是否已包含该手机号，去除空的手机号
            if(!mobileList.contains(mobile) && !StringUtil.isStringEmpty(mobile)){
                mobileList.add(mobile);
            }
        }
        return Result.wrapSuccessfulResult(mobileList);
    }
}
