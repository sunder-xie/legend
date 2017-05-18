package com.tqmall.legend.facade.sell.impl;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.holy.provider.entity.crm.user.ShopSalesDTO;
import com.tqmall.holy.provider.param.legend.OpenLegendSystemParam;
import com.tqmall.holy.provider.service.crm.RpcCustomerCommonService;
import com.tqmall.holy.provider.service.legend.RpcLegendSystemDiscountService;
import com.tqmall.holy.provider.service.legend.RpcLegendSystemSalesService;
import com.tqmall.legend.biz.sell.SellOrderPayService;
import com.tqmall.legend.biz.sell.SellOrderService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.entity.sell.SellOrder;
import com.tqmall.legend.entity.sell.SellOrderPay;
import com.tqmall.legend.entity.sell.SellOrderPayLog;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.enums.sell.SellOrderHandleStatusEnum;
import com.tqmall.legend.enums.sell.SellOrderPayStatusEnum;
import com.tqmall.legend.enums.sell.ShopSellEnum;
import com.tqmall.legend.enums.shop.ShopLevelEnum;
import com.tqmall.legend.facade.sell.SellOrderPayFaced;
import com.tqmall.legend.facade.sell.SellOrderPayTslService;
import com.tqmall.legend.pojo.sell.SellOrderPayStatusChangeVO;
import com.tqmall.legend.pojo.sell.SellOrderSaveVO;
import com.tqmall.legend.pojo.sell.SellShopTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xiangdong.qu on 17/2/23 10:09.
 */
@Service
@Slf4j
public class SellOrderPayFacedImpl implements SellOrderPayFaced {

    @Autowired
    private ShopService shopService;

    @Autowired
    private SnFactory snFactory;

    @Autowired
    private SellOrderService sellOrderService;

    @Autowired
    private SellOrderPayService sellOrderPayService;

    @Autowired
    private SellOrderPayTslService sellOrderPayTslService;

    @Autowired
    private RpcLegendSystemSalesService rpcLegendSystemSalesService;

    @Autowired
    private RpcLegendSystemDiscountService rpcLegendSystemDiscountService;

    @Autowired
    private RpcCustomerCommonService rpcCustomerCommonService;

    /**
     * 创建订单记录
     *
     * @param sellOrderSaveVO
     *
     * @return
     */
    @Override
    public SellOrder sellOrderSave(final SellOrderSaveVO sellOrderSaveVO) {
        return new BizTemplate<SellOrder>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(sellOrderSaveVO, "参数为空");
                Assert.isTrue(!StringUtils.isBlank(sellOrderSaveVO.getMobile()), "购买电话为空");
                Assert.notNull(sellOrderSaveVO.getShopLevel(), "购买版本为空");
                Assert.isTrue(sellOrderSaveVO.getShopLevel() > 0, "购买版本信息错误");
                Assert.notNull(sellOrderSaveVO.getSellAmount(), "购买金额为空");
                Assert.isTrue(sellOrderSaveVO.getSellAmount().compareTo(BigDecimal.ZERO) > 0, "购买金额为负");
            }

            @Override
            protected SellOrder process() throws BizException {
                String mobile = sellOrderSaveVO.getMobile();
                Integer shopLevel = sellOrderSaveVO.getShopLevel();

                //电话号码是否已开通门店判断
                List<Shop> shopList = shopService.getShopsByMobile(mobile);
                if (!CollectionUtils.isEmpty(shopList)) {
                    throw new BizException("当前手机号已开通门店:" + mobile);
                }
                //购买版本号校验
                ShopLevelEnum shopLevelEnum = ShopLevelEnum.getLeveEnum(shopLevel);
                if (null == shopLevelEnum) {
                    log.error("[云修系统售卖在线支付] 购买版本号错误. sellOrderSaveVO:{}", sellOrderSaveVO);
                    throw new BizException("购买版本号错误");
                }
                //购买金额校验
                BigDecimal costPrice = ShopSellEnum.getPriceByShopLevel(shopLevel);
                //判断是否是测试电话号码
                if (isTestMobile(mobile)) {
                    costPrice = getTestBigDecimal(shopLevel);
                }

                if (costPrice == null) {
                    log.error("[云修系统售卖在线支付] 购买版本号错误. sellOrderSaveVO:{}", sellOrderSaveVO);
                    throw new BizException("购买版本号错误");
                }
                //获取折扣信息
                Result<BigDecimal> sellDiscountResult = getSellDiscount(mobile);
                if (!sellDiscountResult.isSuccess()) {
                    throw new BizException("订单下单异常,请联系销售人员");
                }
                BigDecimal sellDiscount = sellDiscountResult.getData();
                BigDecimal sellPrice = costPrice.multiply(sellDiscount).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (sellPrice.compareTo(sellOrderSaveVO.getSellAmount()) != 0) {
                    throw new BizException("订单下单异常,请联系销售人员");
                }

                //获取店铺销售
                Result<ShopSalesDTO> shopSalesDTOResult = rpcCustomerCommonService.getShopSalesInfoByMobile(mobile);
                if (!shopSalesDTOResult.isSuccess() || null == shopSalesDTOResult.getData()) {
                    log.error("[云修系统售卖在线支付] 根据客户手机号码获取直销信息失败. mobile:{},msg:{}", mobile, shopSalesDTOResult.getMessage());
                    throw new BizException("订单下单异常,请联系销售人员");
                }

                ShopSalesDTO shopSalesDTO = shopSalesDTOResult.getData();

                //生成订单号
                String sellOrderSn = snFactory.generateSellOrderSn(0l);

                //构造订单
                SellOrder sellOrder = new SellOrder();
                //门店名称
                sellOrder.setShopName(shopSalesDTO.getCompanyName());
                //直销信息
                sellOrder.setSalesmanId(shopSalesDTO.getSalesId());
                sellOrder.setSalesmanName(shopSalesDTO.getSalesName());
                sellOrder.setSalesmanProvince(shopSalesDTO.getOrgName());
                sellOrder.setSalesmanProvinceId(shopSalesDTO.getOrgId());
                //门店省市信息
                sellOrder.setShopCity(shopSalesDTO.getCityName());
                sellOrder.setShopCityId(shopSalesDTO.getCityId());
                sellOrder.setShopProvince(shopSalesDTO.getProvinceName());
                sellOrder.setShopProvinceId(shopSalesDTO.getProvinceId());

                sellOrder.setBuyMobile(mobile);
                sellOrder.setSellAmount(sellPrice);
                sellOrder.setCostAmount(costPrice);
                sellOrder.setDiscount(sellDiscount);
                sellOrder.setSellOrderSn(sellOrderSn);
                sellOrder.setShopLevel(shopLevel);
                sellOrder.setShopLevelName(shopLevelEnum.getName());
                sellOrder.setPayStatus(SellOrderPayStatusEnum.PAY_STATUS_NO.getPayStatus());
                sellOrder.setHandleStatus(SellOrderHandleStatusEnum.HANDLE_STATUS_NO.getHandleStatus());
                sellOrderService.saveSellOrder(sellOrder);
                return sellOrder;
            }
        }.execute();
    }

    /**
     * 根据购买单号,创建支付空记录
     *
     * @param sellOrderSn 购买订单号
     * @param payOrderSn  支付商品单号
     *
     * @return
     */
    @Override
    public SellOrderPay createSellOrderPay(final String sellOrderSn, final String payOrderSn) {
        return new BizTemplate<SellOrderPay>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!StringUtils.isBlank(sellOrderSn), "购买订单号为空");
                Assert.isTrue(!StringUtils.isBlank(payOrderSn), "支付订单号为空");
            }

            @Override
            protected SellOrderPay process() throws BizException {
                //获取售卖订单详情
                SellOrder sellOrder = sellOrderService.getSellOrderBySn(sellOrderSn);
                if (null == sellOrder) {
                    throw new BizException("获取订单信息有误");
                }

                //生成支付记录
                SellOrderPay sellOrderPayCreate = new SellOrderPay();
                sellOrderPayCreate.setPayOrderSn(payOrderSn);
                sellOrderPayCreate.setSellOrderId(sellOrder.getId());
                sellOrderPayCreate.setSellOrderSn(sellOrder.getSellOrderSn());

                SellOrderPay sellOrderPay = sellOrderPayService.createSellOrderPay(sellOrderPayCreate);
                if (null == sellOrderPay) {
                    throw new BizException("创建支付信息失败");
                }
                return sellOrderPay;
            }
        }.execute();
    }

    /**
     * 连连返回的结果 调用账务校验后 使用
     *
     * @param sellOrderPayStatusChangeVO
     *
     * @return
     */
    @Override
    public Boolean updatePayStatusForSyn(final SellOrderPayStatusChangeVO sellOrderPayStatusChangeVO) {
        final Integer checkStatus = sellOrderPayStatusChangeVO.getCheckStatus();
        return new BizTemplate<Boolean>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(sellOrderPayStatusChangeVO, "参数错误");
                Assert.notNull(sellOrderPayStatusChangeVO.getCheckStatus(), "校验结果参数为空");
                Assert.notNull(sellOrderPayStatusChangeVO.getPayAmount(), "支付金额为空");
                Assert.isTrue(!StringUtils.isBlank(sellOrderPayStatusChangeVO.getPayNo()), "支付流水号为空");
                Assert.isTrue(!StringUtils.isBlank(sellOrderPayStatusChangeVO.getPayOrderSn()), "支付单号为空");
                Assert.notNull(sellOrderPayStatusChangeVO.getPayTime(), "支付时间为空");
                Assert.isTrue(checkStatus.equals(1) || checkStatus.equals(2), "校验结果参数错误");
            }

            @Override
            protected Boolean process() throws BizException {
                log.info("[云修系统售卖在线支付] 校验连连返回后,操作支付状态. sellOrderPayStatusChangeVO:{}", sellOrderPayStatusChangeVO);
                String payOrderSn = sellOrderPayStatusChangeVO.getPayOrderSn();
                SellOrderPay sellOrderPay = sellOrderPayService.getSellOrderPayBySn(payOrderSn);
                if (null != sellOrderPay.getPayResult() && sellOrderPay.getPayResult().equals(SellOrderPayStatusEnum.PAY_STATUS_SUCCESS.getPayStatus())) {
                    log.info("[云修系统售卖在线支付] 当前支付记录已经是支付成功,不需要再处理. payOrderSn:{}", payOrderSn);
                    return Boolean.TRUE;
                }
                //获取购买订单
                SellOrder sellOrder = sellOrderService.selectById(sellOrderPay.getSellOrderId());

                //构造更新数据
                Integer payStatus = 0;
                if (checkStatus.equals(1)) {
                    payStatus = SellOrderPayStatusEnum.PAY_STATUS_FALSE.getPayStatus();
                } else if (checkStatus.equals(2)) {
                    payStatus = SellOrderPayStatusEnum.PAY_STATUS_SUCCESS.getPayStatus();
                }
                //商品订单数据
                SellOrder sellOrderTemp = new SellOrder();
                sellOrderTemp.setId(sellOrder.getId());
                sellOrderTemp.setPayStatus(payStatus);
                //支付记录数据
                SellOrderPay sellOrderPayTemp = new SellOrderPay();
                sellOrderPayTemp.setId(sellOrderPay.getId());
                sellOrderPayTemp.setPayAmount(sellOrderPayStatusChangeVO.getPayAmount());
                sellOrderPayTemp.setPayNo(sellOrderPayStatusChangeVO.getPayNo());
                sellOrderPayTemp.setPayResult(payStatus);
                if (payStatus.equals(SellOrderPayStatusEnum.PAY_STATUS_SUCCESS.getPayStatus())) {
                    sellOrderPayTemp.setPayTime(sellOrderPayStatusChangeVO.getPayTime());
                }

                //支付状态变更记录
                SellOrderPayLog sellOrderPayLog = new SellOrderPayLog();
                sellOrderPayLog.setPayResult(payStatus);
                sellOrderPayLog.setPayTime(sellOrderPayStatusChangeVO.getPayTime());
                sellOrderPayLog.setPayNo(sellOrderPayStatusChangeVO.getPayNo());
                sellOrderPayLog.setPayAmount(sellOrderPayStatusChangeVO.getPayAmount());
                sellOrderPayLog.setCheckStatus(sellOrderPayStatusChangeVO.getCheckStatus());
                sellOrderPayLog.setSellOrderPayId(sellOrderPay.getId());
                sellOrderPayLog.setMsgSource(0);
                sellOrderPayLog.setPayOrderSn(payOrderSn);
                try {
                    sellOrderPayTslService.updateSellOrderPayStatus(sellOrderTemp, sellOrderPayTemp, sellOrderPayLog);
                } catch (RuntimeException e) {
                    log.error("[云修系统售卖在线支付] 更新支付状态失败. payOrderSn:" + payOrderSn, e);
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            }
        }.execute();
    }


    /**
     * 根据订单号 获取订单详情
     *
     * @param sellOrderSn 购买订单单号
     *
     * @return
     */
    @Override
    public SellOrder getSellOrderByOrderSnAndCheckDiscount(final String sellOrderSn) {
        return new BizTemplate<SellOrder>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!StringUtils.isEmpty(sellOrderSn), "订单号信息错误");
            }

            @Override
            protected SellOrder process() throws BizException {
                SellOrder sellOrder = sellOrderService.getSellOrderBySn(sellOrderSn);
                if (null == sellOrder) {
                    log.error("[云修系统售卖在线支付] 查询订单不存在:{}", sellOrderSn);
                    throw new BizException("查询订单不存在");
                }
                //获取折扣信息
                Result<BigDecimal> sellDiscountResult = getSellDiscount(sellOrder.getBuyMobile());
                if (!sellDiscountResult.isSuccess()) {
                    throw new BizException("订单下单异常,请联系销售人员");
                }
                BigDecimal sellDiscount = sellDiscountResult.getData();
                if (sellDiscount.compareTo(sellOrder.getDiscount()) != 0) {
                    throw new BizException("订单下单异常,请联系销售人员");
                }
                return sellOrder;
            }
        }.execute();

    }

    /**
     * 根据支付订单号 获取订单详情.
     *
     * @param payOrderSn 支付订单单号
     *
     * @return
     */
    @Override
    public SellOrder getSellOrderByPayOrderSn(final String payOrderSn) {
        return new BizTemplate<SellOrder>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!StringUtils.isEmpty(payOrderSn), "支付订单号信息错误");
            }

            @Override
            protected SellOrder process() throws BizException {
                SellOrderPay sellOrderPay = sellOrderPayService.getSellOrderPayBySn(payOrderSn);
                if (null == sellOrderPay) {
                    log.error("[云修系统售卖在线支付] 查询支付订单不存在.payOrderSn:{}", payOrderSn);
                    throw new BizException("查询支付订单不存在");
                }
                SellOrder sellOrder = sellOrderService.selectById(sellOrderPay.getSellOrderId());
                if (null == sellOrder) {
                    log.error("[云修系统售卖在线支付] 查询订单不存在.sellOrderId:{}", sellOrderPay.getSellOrderId());
                    throw new BizException("查询订单不存在");
                }
                return sellOrder;
            }
        }.execute();
    }

    /**
     * 根据订单号 获取订单详情.
     *
     * @param sellOrderSn 购买订单单号
     *
     * @return
     */
    @Override
    public SellOrder getSellOrderByOrder(final String sellOrderSn) {
        return new BizTemplate<SellOrder>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!StringUtils.isEmpty(sellOrderSn), "订单号信息错误");
            }

            @Override
            protected SellOrder process() throws BizException {
                SellOrder sellOrder = sellOrderService.getSellOrderBySn(sellOrderSn);
                if (null == sellOrder) {
                    log.error("[云修系统售卖在线支付] 查询订单不存在:{}", sellOrderSn);
                    throw new BizException("查询订单不存在");
                }
                return sellOrder;
            }
        }.execute();
    }

    /**
     * 根据电话号码和订单号 获取订单详情
     *
     * @param mobile      购买电话号
     * @param sellOrderSn 购买订单单号
     *
     * @return
     */
    @Override
    public SellOrder getSellOrder(final String mobile, final String sellOrderSn) {
        return new BizTemplate<SellOrder>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!StringUtils.isBlank(mobile), "参数错误");
                Assert.isTrue(!StringUtils.isBlank(sellOrderSn), "参数错误");
            }

            @Override
            protected SellOrder process() throws BizException {
                return sellOrderService.getSellOrderByMobileAndSn(mobile, sellOrderSn);
            }
        }.execute();
    }

    /**
     * 根据电话号码 获取订单列表
     *
     * @param mobile 购买电话号
     *
     * @return
     */
    @Override
    public List<SellOrder> getSellOrder(final String mobile) {
        return new BizTemplate<List<SellOrder>>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!StringUtils.isBlank(mobile), "参数错误");
            }

            @Override
            protected List<SellOrder> process() throws BizException {
                return sellOrderService.getSellOrderListByMobile(mobile);
            }
        }.execute();
    }

    /**
     * 获取售卖门店各版本列表
     *
     * @return
     */
    @Override
    public List<SellShopTypeVO> getSellShopTypeList(final String mobile) {
        return new BizTemplate<List<SellShopTypeVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<SellShopTypeVO> process() throws BizException {
                List<SellShopTypeVO> sellShopTypeVOList = new ArrayList<>();

                Result<BigDecimal> discountResult = getSellDiscount(mobile);
                if (discountResult == null || !discountResult.isSuccess() || discountResult.getData() == null) {
                    throw new BizException("获取版本价格信息异常,请联系销售人员!");
                }
                BigDecimal discount = discountResult.getData();

                for (ShopSellEnum shopSellEnum : ShopSellEnum.values()) {
                    SellShopTypeVO sellShopTypeVO = new SellShopTypeVO();
                    sellShopTypeVO.setShopLevel(shopSellEnum.getShopLevel());
                    sellShopTypeVO.setName(shopSellEnum.getName());
                    //todo 测试价格,上线验证后删除
                    if (isTestMobile(mobile)) {
                        sellShopTypeVO.setOriginalPrice(getTestBigDecimal(shopSellEnum.getShopLevel()));
                        sellShopTypeVO.setPrice(getTestBigDecimal(shopSellEnum.getShopLevel()).multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        sellShopTypeVO.setOriginalPrice(shopSellEnum.getPrice());
                        sellShopTypeVO.setPrice(shopSellEnum.getPrice().multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    //sellShopTypeVO.setPrice(shopSellEnum.getPrice().multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP));
                    if (discount.compareTo(BigDecimal.ONE) == 0) {
                        sellShopTypeVO.setIsShowDiscountPrice(false);
                    }
                    sellShopTypeVOList.add(sellShopTypeVO);
                }
                return sellShopTypeVOList;
            }
        }.execute();
    }

    /**
     * 获取售卖门店的信息详情
     *
     * @param level 门店版本
     *
     * @return
     */
    @Override
    public SellShopTypeVO getSellShopTypeDetail(Integer level, String mobile) {
        Result<BigDecimal> discountResult = getSellDiscount(mobile);
        if (discountResult == null || !discountResult.isSuccess() || discountResult.getData() == null) {
            throw new BizException("获取版本价格信息异常,请联系销售人员!");
        }
        BigDecimal discount = discountResult.getData();

        SellShopTypeVO sellShopTypeVO = new SellShopTypeVO();
        sellShopTypeVO.setShopLevel(level);
        sellShopTypeVO.setName(ShopSellEnum.getNameByShopLevel(level));
        sellShopTypeVO.setOriginalPrice(ShopSellEnum.getPriceByShopLevel(level));
        //todo 测试价格,上线验证后删除
        if (isTestMobile(mobile)) {
            sellShopTypeVO.setPrice(getTestBigDecimal(level).multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            sellShopTypeVO.setPrice(ShopSellEnum.getPriceByShopLevel(level).multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        //sellShopTypeVO.setPrice(ShopSellEnum.getPriceByShopLevel(level).multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP));
        Date now = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(DateUtil.convertDate(now));
        stringBuffer.append('至');
        stringBuffer.append(DateUtil.convertDate(DateUtil.addYear(now, Constants.SELL_SHOP_EFFECTIVE_YEAR)));
        sellShopTypeVO.setEffectiveStr(stringBuffer.toString());
        return sellShopTypeVO;
    }

    /**
     * 根据售卖订单开通门店
     *
     * @param sellOrderId 售卖订单Id
     *
     * @return
     */
    @Override
    public Long createShop(final Long sellOrderId) {
        return new BizTemplate<Long>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(sellOrderId, "售卖订单Id为空");
                Assert.isTrue(sellOrderId > 0, "售卖订单Id错误");
            }

            @Override
            protected Long process() throws BizException {
                SellOrder sellOrder = sellOrderService.selectById(sellOrderId);
                if (sellOrder == null) {
                    throw new BizException("获取售卖订单失败.sellOrderId:" + sellOrderId);
                } else if (sellOrder.getPayStatus() == null
                        || !sellOrder.getPayStatus().equals(SellOrderPayStatusEnum.PAY_STATUS_SUCCESS.getPayStatus())) {
                    throw new BizException("订单支付信息有误,无法开通门店.sellOrderId:" + sellOrderId);
                } else if (sellOrder.getHandleStatus() != null
                        && sellOrder.getHandleStatus().equals(SellOrderHandleStatusEnum.HANDLE_STATUS_SUCCESS.getHandleStatus())) {
                    throw new BizException("该订单已开通门店.sellOrderId:" + sellOrderId);
                }
                SellOrderPay sellOrderPay = sellOrderPayService.getSellOrderPayBySellOrderId(sellOrderId);
                if (null == sellOrderPay) {
                    throw new BizException("获取订单支付信息失败.sellOrderId:" + sellOrderId);
                }

                OpenLegendSystemParam openLegendSystemParam = new OpenLegendSystemParam();
                openLegendSystemParam.setMobile(sellOrder.getBuyMobile());
                openLegendSystemParam.setRefer(1);
                openLegendSystemParam.setSystemValidYear(Constants.SELL_SHOP_EFFECTIVE_YEAR);
                //销售订单单号
                openLegendSystemParam.setTransactionSerialNumber(sellOrder.getSellOrderSn());
                openLegendSystemParam.setTransactionAmount(sellOrderPay.getPayAmount());
                openLegendSystemParam.setLegendSystemType(sellOrder.getShopLevel());
                log.info("[云修系统在线销售] 调用crm系统开通门店. openLegendSystemParam:{}", JSONUtil.object2Json(openLegendSystemParam));
                Result<Long> result = rpcLegendSystemSalesService.openLegendSystemFromCRM(openLegendSystemParam);
                log.info("[云修系统在线销售] 调用crm系统开通门店返回. openLegendSystemParam:{},result:{}", JSONUtil.object2Json(openLegendSystemParam), JSONUtil.object2Json(result));

                if (null == result) {
                    throw new BizException("门店开通失败");
                } else if (!result.isSuccess()) {
                    log.error("[云修系统在线销售] 调用crm系统开通门店失败. mobile:{},msg:{}", openLegendSystemParam.getMobile(), result.getMessage());
                    throw new BizException("门店开通失败");
                }
                return result.getData();
            }
        }.execute();
    }

    /**
     * 根据电话号码获取折扣信息
     *
     * @param mobile 购买电话号码
     *
     * @return 0-1
     */
    @Override
    public Result<BigDecimal> getSellDiscount(String mobile) {
        try {
            log.info("[云修系统在线销售] 调用crm接口获取客户的折扣信息. mobile:{}", mobile);
            Result<BigDecimal> discountResult = rpcLegendSystemDiscountService.getDiscountByMobile(mobile);
            log.info("[云修系统在线销售] 调用crm接口获取客户的折扣信息. mobile:{},discountResult:{}", mobile, JSONUtil.object2Json(discountResult));
            if (null == discountResult) {
                log.error("[云修系统在线销售] 调用crm接口获取客户的折扣信息失败.mobile:{}", mobile);
                return Result.wrapErrorResult("", "获取版本价格信息异常,请联系销售人员!");
            } else if (!discountResult.isSuccess()) {
                log.error("[云修系统在线销售] 调用crm接口获取客户的折扣信息失败.mobile:{},msg:{}", mobile, discountResult.getMessage());
                return Result.wrapErrorResult("", "获取版本价格信息异常,请联系销售人员!");
            }
            BigDecimal discount = discountResult.getData();
            if (discount == null) {
                return Result.wrapSuccessfulResult(BigDecimal.ONE);
            } else if (discount.compareTo(BigDecimal.valueOf(0.5)) < 0 || discount.compareTo(BigDecimal.valueOf(1)) > 0) {
                log.error("[云修系统在线销售] 调用crm接口获取客户的折扣信息.mobile:{},discount:{}", mobile, discount);
                return Result.wrapErrorResult("", "获取版本价格信息异常,请联系销售人员!");
            }
            return Result.wrapSuccessfulResult(discount);
        } catch (Exception e) {
            log.error("[云修系统在线销售] 调用crm接口获取客户的折扣信息失败.mobile:" + mobile, e);
            return Result.wrapErrorResult("", "获取版本价格信息异常,请联系销售人员!");
        }
    }

    /**
     * 是否是测试手机
     * todo 上线删除!
     *
     * @return
     */
    private boolean isTestMobile(String mobile) {
        List<String> mobileList = new ArrayList<>();
        mobileList.add("18768107319");
        mobileList.add("15158116453");
        if (mobileList.contains(mobile)) {
            return true;
        }
        return false;
    }

    /**
     * 测试价格
     * todo 上线删除!
     *
     * @return
     */
    private BigDecimal getTestBigDecimal(Integer level) {
        if (ShopSellEnum.BASE.getShopLevel().equals(level)) {
            return BigDecimal.valueOf(0.02);
        }
        if (ShopSellEnum.STANDARD.getShopLevel().equals(level)) {
            return BigDecimal.valueOf(0.04);
        }
        if (ShopSellEnum.PROFESSION.getShopLevel().equals(level)) {
            return BigDecimal.valueOf(0.06);
        }
        return BigDecimal.valueOf(100000);
    }
}
