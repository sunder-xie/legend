package com.tqmall.legend.biz.insurance;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.insurance.InsuranceBill;
import com.tqmall.legend.entity.order.InsuranceOrderStatusEnum;
import com.tqmall.legend.entity.order.OrderInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * insuranceBill service interface
 */
public interface IInsuranceBillService {


	/**
	 * get InsuranceBill
	 *
	 * @param billId primary key
	 * @return
	 */
	Optional<InsuranceBill> get(Long billId);


	/**
	 * save
	 *
	 * @param fromEntity
	 * @param userInfo
	 * @return
	 * @throws BusinessCheckedException
	 */
	Result save(InsuranceBill insuranceBill, UserInfo userInfo) throws BusinessCheckedException;

	/**
	 * update
	 *
	 * @param insuranceBill
	 * @param userInfo
	 * @return
	 * @throws BusinessCheckedException
	 */
	Result update(InsuranceBill insuranceBill, UserInfo userInfo) throws BusinessCheckedException;

	/**
	 * 批量修改字段，底层可扩展
	 * @return
	 * @throws BusinessCheckedException
	 */
	Result batchUpdate(Map<String,Object> param) throws BusinessCheckedException;


	/**
	 * page
	 *
	 * @param pageable
	 * @param searchParams
	 * @return
	 */
	Page<InsuranceBill> getOrderInfoPage(Pageable pageable, Map<String, Object> searchParams);

	/**
	 * 引流活动对账分页
	 * @param pageable
	 * @param searchParams
	 * @return
	 */
	Page<InsuranceBill> getActivitySettlePage(Pageable pageable, Map<String, Object> searchParams);

	/**
	 * 条件查询
	 *
	 * @param searchParams
	 * @return
	 */
	public List<InsuranceBill> select(Map<String, Object> searchParams);

	/**
	 *
	 * 2016-06-14 引流活动 结算改版
	 *
	 * submit insurance bill
	 *
	 * @param orderInfo
	 * @param userInfo
	 * @return
	 * @throws BusinessCheckedException
	 */
	Result submit(InsuranceBill orderInfo, UserInfo userInfo) throws BusinessCheckedException;


	/**
	 * re-submit insurance bill
	 *
	 * @param orderInfo 保险单实体
	 * @param userInfo  当前操作人
	 * @return
	 */
	Result reSubmit(InsuranceBill orderInfo, UserInfo userInfo) throws BusinessCheckedException;


	/**
	 * create服务单
	 *
	 * @param orderInfo
	 * @param userInfo
	 * @return
	 */
	Result create(InsuranceBill bill, UserInfo userInfo);


	/**
	 * 2016-06-14 引流活动 结算改版
	 *
	 * 参加天猫活动 提交审核
	 *
	 * @param bill
	 * @param userInfo
	 * @return
	 */
	Result adult(InsuranceBill bill, UserInfo userInfo) throws BusinessCheckedException;


	/**
	 * 更新服务单
	 *
	 * @param bill
	 * @param userInfo
	 * @return
	 */
	Result<InsuranceBill> modify(InsuranceBill bill, UserInfo userInfo) throws BusinessCheckedException;

	/**
	 * 创建服务单
	 *
	 * @param userInfo   当前用户
	 * @param orderInfo  工单
	 * @param CouponCode 优惠券码
	 */
	void create(UserInfo userInfo, OrderInfo orderInfo, String CouponCode, InsuranceOrderStatusEnum insuranceOrderStatusEnum);

	/**
	 * 获取门店引流活动收入汇总账单
	 *
	 * @return
	 */
	public Result getInsuranceBillSettleList(Map<String,Object> searchParams);

	/**
	 * 校验核销码,是否在系统中被使用过
	 *
	 * @param verificationCode
	 * @return
	 */
	boolean checkVerificationCodeIsUsed(String verificationCode);

	/**
	 * 校验核销码,是否在系统中被使用过
	 *
	 * @param verificationCode
	 * @return
	 */
	boolean checkVerificationCodeIsUsed(Long billId,String verificationCode);


	Result deleteBillByOrderId(Long orderId, UserInfo userInfo);
}
