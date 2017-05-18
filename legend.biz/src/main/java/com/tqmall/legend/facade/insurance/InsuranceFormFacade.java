package com.tqmall.legend.facade.insurance;

import com.tqmall.common.UserInfo;
import com.tqmall.insurance.domain.param.insurance.*;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.SearchFormForCashCouponParam;
import com.tqmall.insurance.domain.result.*;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.insurance.vo.*;

import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 16/8/18.
 */
public interface InsuranceFormFacade {
    /**
     * 投保地区省市
     *
     * @param regionParentCode
     * @return
     */
    List<InsuranceRegionVo> getRegionByCode(String regionParentCode);

    /**
     * 收货地址省市
     *
     * @param regionParentCode
     * @return
     */
    List<InsuranceRegionVo> getRegionByCodeAndIsOpen(String regionParentCode);

    List<InsuranceCarModelVo> getCarModel(InsuranceCarModelParam insuranceCarModelParam);

    Map<Integer, String> getInsuredCertType();

    List<InsuranceCategoryVo> getInsuranceCategory();

    /**
     * 返回修改时获取已经选择的保险
     *
     * @return
     */
    List<InsuranceCategoryVo> getInsuranceCategoryHasChoose(InsuranceBasicDTO insuranceBasic);

    Result<InsuranceCalculateDTO> insuranceFeeCalculate(InsuranceCalculateParam insuranceCalculateParam);

    Result<InsuranceCalculateDTO> repeatFeeCalculate(Integer basicId);

    Result deleteByFormId(Integer formId);


    Result<InsuranceBasicDTO> updateInsuranceFee(InsuranceCalculateParam insuranceCalculateParam);

    Result<InsuranceBasicDTO> updateInsuranceFeePackage(InsuranceCalculateParam insuranceCalculateParam);

    /**
     * 获取投保单
     *
     * @param insuranceBasicParam
     * @return
     */
    Result<PageEntityDTO<InsuranceFormVo>> getApplyNoInsuranceList(InsuranceBasicParam insuranceBasicParam);

    /**
     * 获取保单
     *
     * @param insuranceBasicParam
     * @return
     */
    Result<PageEntityDTO<InsuranceFormVo>> getInsuranceList(InsuranceBasicParam insuranceBasicParam);

    Result<InsuranceFormVo> getInsuranceFormDetail(Integer id, Integer agentId);

    /**
     * 真实保单返回修改获取信息
     *
     * @param basicId
     * @param agentId
     * @return
     */
    InsuranceBasicDTO backUpdateInfo(Integer basicId, Integer agentId);


    /**
     * 获取未生效奖励金
     *
     * @param insuranceFormParam
     * @return
     */
    PageEntityDTO<InsuranceFormVo> getNotEffectiveReward(InsuranceFormParam insuranceFormParam);

    /**
     * 获取剩余奖励金
     *
     * @param source
     * @param shopId
     * @return
     */
    InsuranceAccountAmountVo getSurplusReward(String source, Integer shopId);

    VehicleInformationDTO getVehicleInformation(String carNo, String ownerName);

    /**
     * 获取投保单状态
     *
     * @return
     */
    Map<Integer, String> getInsureStatus();

    /**
     * 获取保单状态
     *
     * @return
     */
    Map<Integer, String> getQuitStatus();

    /**
     * 获取待生效奖励金
     *
     * @param
     * @return
     */
    CheckAcountDTO getWaitEffectReward(Integer agentId);

    /**
     * 获取奖励金分配比例
     */
    BonusAllocationRatioVo selectRatio(Integer ucShopId);


    /*=======by zxg for 门店奖励金对账 ====*/


    boolean uploadIdentityInfo(IdentityInfoParam param);

    /**
     * 去获取适用优惠券的保单号
     *
     * @param
     * @return
     */
    Result<List<InsuranceCashCouponFormDTO>> getInsuranceFormForCashCoupon(SearchFormForCashCouponParam param);


    /**
     * 调用mace上传图片到安心
     *
     * @param orderSn
     * @param urls
     * @param userInfo
     * @return
     */
    boolean uploadImgToAnxin(String orderSn, String urls, UserInfo userInfo);

    /**
     * 查询支付保费信息
     *
     * @param ucShopId
     * @param orderSn
     */
    InsuranceFeeDTO selectPayInfoByOrderSn(Integer ucShopId, String orderSn);
}
