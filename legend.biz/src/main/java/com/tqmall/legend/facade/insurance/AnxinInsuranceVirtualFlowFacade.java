package com.tqmall.legend.facade.insurance;


import com.tqmall.finance.model.param.pay.OfferListFormParam;
import com.tqmall.finance.model.param.pay.OfferListLianFormParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceCalculateParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceVirtualBasicParam;
import com.tqmall.insurance.domain.result.InsuranceServicePackageFeeDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualBasicDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualFormDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.base.BaseEnumBo;
import com.tqmall.legend.facade.insurance.vo.InsuranceCategoryVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/9/17.
 * 虚拟保单
 * 支付
 */


public interface AnxinInsuranceVirtualFlowFacade {
    /**
     * 支付宝支付
     *
     * @return
     */
    String alipay(OfferListFormParam param);

    /**
     * 支付宝验证
     */
    void verifyAliPay(Map<String, String[]> param);

    /**
     * 连连支付回调验证
     *
     * @param param
     */
    void verifyUnionpay(Map<String, String[]> param);


    /**
     * 连连支付
     *
     * @return
     */
    String unionpay(OfferListLianFormParam param);

    /**
     * 验证支付金额是否与虚拟投保预支付金额一致
     *
     * @param orderSn
     * @param payFee
     * @return
     */
    void checkPayFee(Integer ucShopId, String orderSn, BigDecimal payFee);

    /**
     * 保存虚拟保单
     *
     * @param param
     * @return
     */
    Integer saveVirtualInfo(InsuranceCalculateParam param);

    /**
     * 更新虚拟保单
     *
     * @param param
     * @return
     */
    void updateVirtualInfo(InsuranceCalculateParam param);

    /**
     * 虚拟投保返回修改 获取已选中的险种的信息
     *
     * @param insuranceBasic
     * @param categoryVoList
     * @return
     */
    List<InsuranceCategoryVo> getInsuranceCategoryHasChoose(InsuranceVirtualBasicDTO insuranceBasic, List<InsuranceCategoryVo> categoryVoList);

    /**
     * 确认投保方案,更新对应虚拟投保单服务包
     * @param formId
     * @param packageId
     * @param servicePackageFee
     * @param ucShopId
     * @param colourType
     */
    void updateVirtualBasicService(Integer formId, Integer packageId, BigDecimal servicePackageFee, Integer ucShopId, Integer colourType);

    /**
     * 虚拟投保单列表初始化
     *
     * @param param
     * @return
     */
    DefaultPage<InsuranceVirtualFormDTO> selectVirtualList(InsuranceVirtualBasicParam param);

    /**
     * 查询虚拟投保单详情
     *
     * @param id
     * @param shopId
     * @return
     */
    InsuranceVirtualBasicDTO selectVirtualDetail(Integer id, Integer shopId);

    /**
     * 根据总金额计算预付款金额
     *
     * @param packageFee
     * @return
     */
    InsuranceServicePackageFeeDTO selectPayment(BigDecimal packageFee);

    /**
     * 根据服务包ID获取预付金额
     *
     * @param id
     * @param ucShopId
     * @return
     */
    BigDecimal selectPrePayAmount(Integer ucShopId, Integer id);

    /**
     * 查询虚拟投保单状态
     *
     * @return
     */
    List<BaseEnumBo> selectVirtualStauts();

    /**
     * 查询门店名称
     *
     * @param shopId
     * @return
     */
    String selectShopNameById(Long shopId);

    /**
     * 城市是否开通保险业务
     *
     * @param cityCode
     * @return
     */
    boolean isInsuranceAvaiableInRegion(Integer cityCode);
}