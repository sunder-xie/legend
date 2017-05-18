package com.tqmall.legend.biz.settlement.impl;

import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.settlement.PaymentDao;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.enums.settlement.PaymentShowStatusEnum;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/12/26.
 */
@Service
public class PaymentServiceImpl extends BaseServiceImpl implements PaymentService {
	Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
	@Autowired
	public PaymentDao paymentDao;
    @Autowired
    private CacheComponent<List<Payment>> cacheComponent;

	/**
	 * TODO not redirect null
	 *
	 * @param shopId
	 *            店铺id 根据shopId查询 店铺的付款方式
	 */
	@Override
	public List<Payment> getPaymentsByShopId(long shopId) {
		if (Constants.SHOP_ID_FLAG > shopId) {
			logger.error("shopId错误:" + "shopId" + shopId);
			return null;
		}
		List<Payment> paymentList = cacheComponent.getCache(CacheKeyConstant.PAYMENT);
		List<Payment> result = new ArrayList<>();
		Map<String,Payment> paymentMap = new HashMap<>();
		//先取标准结算方式
		for (Payment payment : paymentList) {
			if(payment.getShopId().equals(0l) && PaymentShowStatusEnum.SHOW.getCode().equals(payment.getShowStatus())){
				String name = payment.getName();
				if(!paymentMap.containsKey(name)){
					paymentMap.put(name,payment);
				}
			}
		}
		//再取门店的结算方式
		for (Payment payment : paymentList) {
			if(payment.getShopId().equals(shopId) && PaymentShowStatusEnum.SHOW.getCode().equals(payment.getShowStatus())){
				String name = payment.getName();
				if(!paymentMap.containsKey(name)){
					paymentMap.put(name,payment);
				}
			}
		}
		//将不重复的结算方式放在list中
		for(Payment payment : paymentMap.values()){
			result.add(payment);
		}
		//以payment_tag正序排序
		try {
			Integer size = result.size();
			for (int i = 0; i < size; i++){
                for (int j = size-1; j > i; j--){
                    Payment payment = result.get(i);
                    Payment lastPayment = result.get(j);
                    Integer paymentTag = payment.getPaymentTag();
                    Integer lasePaymentTag = lastPayment.getPaymentTag();
					//标准结算类目1现金2刷卡3会员卡4第三方支付5转账6支票7其它，1优先级最高
                    if(paymentTag.compareTo(lasePaymentTag) > 0){
                        Payment temp = new Payment();
                        BeanUtils.copyProperties(temp,payment);
                        BeanUtils.copyProperties(payment,lastPayment);
                        BeanUtils.copyProperties(lastPayment,temp);
                    }
                }
            }
		} catch (Exception e) {
			logger.error("【获取结算方式】：对象转换出错{}",e);
		}
		return result;
	}

	/**
	 * @param id
	 *            付款方式 根据付款方式id查询付款方式
	 */
	@Override
	public Payment selectPaymentById(long id) {
		List<Payment> paymentList = cacheComponent.getCache(CacheKeyConstant.PAYMENT);
		for (Payment payment : paymentList) {
			if(payment.getId().equals(id)){
				return payment;
			}
		}
		return null;
	}

	@Override
	public List<Payment> searchPayments(Map<String, Object> paramMap) {
		//通过缓存获取数据
		List<Payment> paymentList = cacheComponent.getCache(CacheKeyConstant.PAYMENT);
		List<Payment> result = new ArrayList<>();
		//通过内存进行数据筛选
		if (paramMap.get("shopId") != null && !paramMap.get("shopId").equals(Long.parseLong("0"))) {	
			for (Payment payment : paymentList) {
				if(payment.getShopId().equals(paramMap.get("shopId")) || payment.getShopId().equals(0l)){
					if(paramMap.get("name")!=null){
						if(!paramMap.get("name").toString().equals(payment.getName())){
							continue;
						}
					}
					if(paramMap.get("likename")!=null){
//						if(payment.getName()==null||!payment.getName().contains(paramMap.get("likename").toString())){
//							continue;
//						}
						if(payment.getName()==null){
							continue;
						}
					}
					result.add(payment);
				}
			}
			return result;
		} else {
			for (Payment payment : paymentList) {
				if(payment.getShopId().equals(0l)){
					result.add(payment);
				}
			}
			return result;
		}
	}

    /**
     *
     * @param searchParams
     * @return
     */
    @Override
    public List<Payment> select(Map<String, Object> searchParams) {
        return paymentDao.select(searchParams);
    }

	/**
	 * 批量添加结算方式
	 * @param paymentList
	 * @param userInfo
	 * @return
	 */
    @Override
	@Transactional
    public Result batchInsertPayment(List<Payment> paymentList, UserInfo userInfo) {
		Long shopId = userInfo.getShopId();
		for (Payment payment : paymentList) {
			payment.setShopId(shopId);
		}
		Map<String, Object> map = new HashMap<>();
		//查询门店的结算方式
		map.put("shopId", shopId);
		List<Payment> payments = paymentDao.select(map);
		//查询公共的结算方式
		map.put("shopId",0);
		List<Payment> tempPayments = paymentDao.select(map);
		//合并结算方式
		payments.addAll(tempPayments);
		Map<String,Payment> existPaymentMap = new HashMap<>();
		for(Payment payment : payments){
			existPaymentMap.put(payment.getName(), payment);
		}
		Map<String,Payment> insertMap = new HashMap<>();
		for(Payment payment : paymentList){
			String name = payment.getName();
			//若数据库不存在，且需要的添加的数据不存在，则添加到需要添加的map中
			if(!existPaymentMap.containsKey(name) && !insertMap.containsKey(name)){
				insertMap.put(name,payment);
			}
		}
		//判断数据是否为空
		if (!CollectionUtils.isEmpty(insertMap)) {
			List<Payment> insertPaymentList = new ArrayList<>();
			for (Payment payment : insertMap.values()) {
				payment.setCreator(userInfo.getUserId());
				payment.setModifier(userInfo.getUserId());
				insertPaymentList.add(payment);
			}
			logger.info("批量添加门店id{}的支付方式,数据为{}",shopId,insertPaymentList);
			super.batchInsert(paymentDao,insertPaymentList,300);
			return Result.wrapSuccessfulResult(true);
		}else{
			return Result.wrapErrorResult("","数据已存在，没有数据可添加");
		}
    }

	@Override
	public Integer update(Payment payment) {
		return paymentDao.updateById(payment);
	}
}
