package com.tqmall.legend.facade.insurance.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.finance.model.param.pay.OfferListFormParam;
import com.tqmall.finance.model.param.pay.OfferListLianFormParam;
import com.tqmall.finance.service.pay.WebPayService;
import com.tqmall.insurance.domain.param.insurance.InsuranceCalculateParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceVirtualBasicParam;
import com.tqmall.insurance.domain.result.InsuranceServicePackageFeeDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualBasicDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualFormDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualItemDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceRegionService;
import com.tqmall.insurance.service.insurance.RpcInsuranceUserService;
import com.tqmall.insurance.service.insurance.RpcInsuranceVirtualFormService;
import com.tqmall.insurance.service.insurance.pay.RpcUserServicePackagePayService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.base.BaseEnumBo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.insurance.AnxinInsuranceVirtualFlowFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceCategoryVo;
import com.tqmall.legend.facade.insurance.vo.InsuranceItemCoverageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/9/17.
 */
@Service
@Slf4j
public class AnxinInsuranceVirtualFlowFacadeImpl implements AnxinInsuranceVirtualFlowFacade {
    @Resource
    private WebPayService webPayService;
    @Resource
    private RpcInsuranceVirtualFormService rpcInsuranceVirtualFormService;
    @Resource
    private RpcUserServicePackagePayService rpcUserServicePackagePayService;
    @Resource
    private RpcInsuranceRegionService rpcInsuranceRegionService;
    @Resource
    private ShopService shopService;
    @Resource
    private RpcInsuranceUserService rpcInsuranceUserService;

    @Override
    public String selectShopNameById(Long shopId) {
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            throw new BizException("门店信息不存在");
        }
        return shop.getName();
    }

    @Override
    public List<BaseEnumBo> selectVirtualStauts() {
        Result<Map<Integer, String>> result = rpcInsuranceVirtualFormService.getVirtualInsuranceStatus();
        if (!result.isSuccess()) {
            log.error("[DUBBO]获取虚拟投保状态失败,失败原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取虚拟投保状态失败");
        }
        if (result.getData() == null) {
            return new ArrayList<>();
        }
        Map<Integer, String> param = result.getData();
        List<BaseEnumBo> boList = new ArrayList<>();
        for (Integer code : param.keySet()) {
            BaseEnumBo bo = new BaseEnumBo();
            bo.setCode(code);
            if (param.get(code) != null) {
                bo.setName(param.get(code));
            }
            boList.add(bo);
        }
        return boList;
    }

    @Override
    public InsuranceServicePackageFeeDTO selectPayment(BigDecimal packageFee) {
        Result<InsuranceServicePackageFeeDTO> result = rpcInsuranceVirtualFormService.showInsuranceServicePackageFee(packageFee);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用安心保险计算预付金额接口,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取金额失败");
        }
        return result.getData();
    }

    @Override
    public BigDecimal selectPrePayAmount(Integer ucShopId, Integer id) {
        log.info("[DUBBO]获取预付金额,服务包Id:{},门店ID:{}", id, ucShopId);
        Result<BigDecimal> result = rpcInsuranceUserService.getPrePayAmount(ucShopId, id);
        if (!result.isSuccess()) {
            log.error("[DUBBO]获取预付金额失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    @Override
    public InsuranceVirtualBasicDTO selectVirtualDetail(Integer id, Integer shopId) {
        log.info("[DUBBO]调用安心保险获取虚拟投保单详情,虚拟投保单号:{},shopId:{}", id, shopId);
        Result<InsuranceVirtualBasicDTO> result = rpcInsuranceVirtualFormService.getVirtualInsuranceFormDetailByBasicId(id, shopId);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用安心保险获取虚拟投保单详情失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取详情失败");
        }
        return result.getData();
    }

    @Override
    public DefaultPage<InsuranceVirtualFormDTO> selectVirtualList(InsuranceVirtualBasicParam param) {
        log.info("[DUBBO]调用安心保险查询虚拟投保单列表,虚拟投保单查询条件:{}", LogUtils.objectToString(param));
        Result<PageEntityDTO<InsuranceVirtualFormDTO>> result = rpcInsuranceVirtualFormService.getVirtualList(param);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用安心保险查询虚拟投保单列表失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("虚拟投保单列表初始化失败");
        }
        PageEntityDTO pageEntity = result.getData();
        if (pageEntity == null || CollectionUtils.isEmpty(pageEntity.getRecordList())) {
            return new DefaultPage<>(new ArrayList<InsuranceVirtualFormDTO>());
        }
        PageRequest pageRequest = new PageRequest(param.getPageNum(), param.getPageSize());
        DefaultPage<InsuranceVirtualFormDTO> page = new DefaultPage<>(pageEntity.getRecordList(), pageRequest, pageEntity.getTotalNum());

        return page;
    }

    @Override
    public void updateVirtualBasicService(Integer formId, Integer packageId, BigDecimal servicePackageFee, Integer ucShopId, Integer colourType) {
        log.info("[DUBBO]调用安心保险更新虚拟投保单服务包接口,商业保险单号:{}", formId);
        Result result = rpcInsuranceVirtualFormService.createUserServicePackage(packageId, formId, servicePackageFee, ucShopId, colourType);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用安心保险更新虚拟投保单服务包接口失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public void updateVirtualInfo(InsuranceCalculateParam param) {
        log.info("[DUBBO]调用安心保险更新虚拟投保单接口,虚拟投保单号:{}", param.getId());
        Result result = rpcInsuranceVirtualFormService.updateVirtualInsuranceFee(param);
        if (!result.isSuccess()) {
            log.error("[DUBBO]确认投保方案异常:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }

    }


    @Override
    public List<InsuranceCategoryVo> getInsuranceCategoryHasChoose(InsuranceVirtualBasicDTO insuranceBasic,List<InsuranceCategoryVo> categoryVoList) {
        if(insuranceBasic == null){
            return categoryVoList;
        }
        List<InsuranceVirtualFormDTO> insuranceFormList = insuranceBasic.getInsuranceVirtualFormDTOList();

        if(insuranceFormList == null){
            return categoryVoList;
        }
        //商业险form  若商业险只选
        InsuranceVirtualFormDTO insuranceForm = null;
        //交强险
        InsuranceVirtualFormDTO insuranceFormJQX = null;
        for(int i = 0 ; i< insuranceFormList.size();i++){
            if(insuranceFormList.get(i).getInsuranceType().compareTo(2) == 0){
                insuranceForm = insuranceFormList.get(i);
            }
            if(insuranceFormList.get(i).getInsuranceType().compareTo(1) == 0){
                insuranceFormJQX = insuranceFormList.get(i);
            }
        }
        if(insuranceForm == null && insuranceFormJQX == null){
                return categoryVoList;
        }
        List<InsuranceCategoryVo> needList = getCategoryListUpdated(categoryVoList, insuranceForm,insuranceFormJQX);

        return needList;
    }

    private List<InsuranceCategoryVo> getCategoryListUpdated(List<InsuranceCategoryVo> categoryVoList, InsuranceVirtualFormDTO insuranceForm,InsuranceVirtualFormDTO insuranceFormJQX) {
        //此处不用Maps.uniqueIndex方法是为有序返回
        Map<String, InsuranceCategoryVo> insuranceCategoryMap = Maps.newLinkedHashMap();
        for(InsuranceCategoryVo insuranceCategory :categoryVoList){
            insuranceCategoryMap.put(insuranceCategory.getInsuranceCategoryCode(),insuranceCategory);
        }

        List<InsuranceVirtualItemDTO> itemList = insuranceForm.getInsuranceVirtualItemDTOList();
        if(insuranceFormJQX!=null){
            itemList.add(0,insuranceFormJQX.getInsuranceVirtualItemDTOList().get(0));
        }
        List<InsuranceCategoryVo>  needList = Lists.newArrayList();
        for(InsuranceVirtualItemDTO insuranceItem : itemList){
            //获取需要赋值的险种对象
            String insuranceCategoryCode = insuranceItem.getInsuranceCategoryCode();
            InsuranceCategoryVo insuranceCategoryVo = insuranceCategoryMap.get(insuranceCategoryCode);
            if(insuranceCategoryVo == null){
                continue;
            }
            //如果勾选了不计免赔
            if(insuranceItem.getIsDeductible().compareTo(0) == 0){
                insuranceCategoryVo.setIsDeductible(true);
            }
            insuranceCategoryVo.setIsCheck(true);
            String coverageValueDisplay = insuranceItem.getItemCoverageValueDisplay();
            //如果选择具体险别种类
            if(coverageValueDisplay != null){
                List<InsuranceItemCoverageVo> itemCoverageList = insuranceCategoryVo.getInsuranceItemCoverageVoList();
                for(InsuranceItemCoverageVo itemCoverage :itemCoverageList){
                    if(itemCoverage.getInsuranceItemCoverageDisplay().equals(coverageValueDisplay)){
                        itemCoverage.setIsCheck(true);
                        itemCoverage.setInsuranceCategoryId(insuranceItem.getInsuranceItemCoverageId());
                        itemCoverage.setInsuranceCategoryCode(insuranceItem.getInsuranceCategoryCode());
                        itemCoverage.setInsuranceItemCoverageDisplay(coverageValueDisplay);
                        itemCoverage.setInsuranceItemCoverageValue(insuranceItem.getInsuranceItemCoverageValue());
                    }
                }
            }
        }
        for (Map.Entry<String, InsuranceCategoryVo> m :insuranceCategoryMap.entrySet())  {
            needList.add(m.getValue());
        }
        return needList;
    }


    @Override
    public Integer saveVirtualInfo(InsuranceCalculateParam param) {
        param.setSystemSource(Constants.CUST_SOURCE);
        log.info("[DUBBO]调用安心保险新增虚拟投保单接口,ucShopId:{}", param.getAgentId());
        Result<Integer> result = rpcInsuranceVirtualFormService.virtualInsuranceFeeCalculate(param);
        if (!result.isSuccess()) {
            log.error("[DUBBO]保存虚拟投保单失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    @Override
    public void checkPayFee(Integer ucShopId, String orderSn, BigDecimal payFee) {
        Result<Boolean> result = rpcUserServicePackagePayService.checkPrePayAmount(orderSn, payFee, ucShopId);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用安心保险验证预付保费接口失败,失败原因:{}", LogUtils.objectToString(result));
            throw new BizException("需支付金额与虚拟投保单信息不符");
        }
        if (!result.getData()) {
            throw new BizException("需支付金额与虚拟投保单信息不符");
        }
    }

    @Override
    public String unionpay(OfferListLianFormParam param) {
        param.setSource(Constants.CUST_SOURCE);
        param.setSubject("买服务包送保险");
        param.setBody("");
        //预支付
        param.setPayType(1);
        log.info("[DUBBO]获取连连支付表单接口,参数:{}", LogUtils.objectToString(param));
        Result<String> result = webPayService.getLianPayParam4Insurance(param);
        if (!result.isSuccess()) {
            log.error("[DUBBO]获取连连支付信息失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取连连支付信息失败");
        }
        return result.getData();
    }

    @Override
    public void verifyUnionpay(Map<String, String[]> param) {
        log.info("[DUBBO]调用连连支付验证接口:{}", LogUtils.objectToString(param));
        try {
            Result<String> result = webPayService.verifyLianReturnInsurance(Constants.CUST_SOURCE, param);
            if (!result.isSuccess()) {
                log.error("[DUBBO]调用连连支付验证接口,失败原因:{}", LogUtils.objectToString(result));
            }
        } catch (Exception e) {
            log.error("[DUBBO]调用连连支付验证接口,失败原因:", e);
        }
    }

    @Override
    public String alipay(OfferListFormParam param) {
        param.setSubject("买服务包送保险");
        param.setSource(Constants.CUST_SOURCE);
        param.setPayType(1);
        log.info("[DUBBO]获取支付宝支付接口,ucShopId:{},订单编号:{}", param.getUid(), param.getSn());
        Result<String> result = webPayService.getAliPayParam4Insurance(param);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用支付宝支付接口失败,失败原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取支付信息失败");
        }
        return result.getData();
    }

    //**********************支付宝付款
    @Override
    public void verifyAliPay(Map<String, String[]> param) {
        log.info("[DUBBO]调用支付宝验证接口:{}", LogUtils.objectToString(param));
        try {
            Result<String> result = webPayService.verifyAliReturnInsurance(Constants.CUST_SOURCE, param);
            if (!result.isSuccess()) {
                log.error("[DUBBO]调用支付宝验证接口,失败原因:{}", LogUtils.objectToString(result));
            }
        } catch (Exception e) {
            log.error("[DUBBO]调用支付宝验证接口,失败原因:", e);
        }
    }

    @Override
    public boolean isInsuranceAvaiableInRegion(Integer cityCode) {
        log.info("[DUBBO] 调用insurance根据城市判断是否开通保险,参数:cityCode:{}", cityCode);
        Result<Boolean> result;
        try {
            result = rpcInsuranceRegionService.isInsuranceAvaiableInRegion(cityCode);
        } catch (Exception e) {
            log.error("[DUBBO] 调用insurance根据城市判断是否开通保险失败,错误原因:", e);
            return false;
        }
        if (!result.isSuccess()) {
            log.info("[DUBBO] 调用insurance根据城市判断是否开通保险失败,错误原因:{}", LogUtils.objectToString(result));
            return false;
        }
        if (result.getData() == null) {
            return false;
        }
        return result.getData();
    }
}
