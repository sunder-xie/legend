package com.tqmall.legend.facade.settlement.impl;


import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.billcenter.client.RpcPayService;
import com.tqmall.legend.billcenter.client.dto.PayBillDTO;
import com.tqmall.legend.billcenter.client.dto.PayBillFlowDTO;
import com.tqmall.legend.billcenter.client.param.PayParam;
import com.tqmall.legend.billcenter.client.result.DefaultResult;
import com.tqmall.legend.billcenter.client.result.PayResult;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.settlement.WarehouseInPaymentService;
import com.tqmall.legend.biz.settlement.vo.PayBillVo;
import com.tqmall.legend.biz.settlement.vo.PayResultVo;
import com.tqmall.legend.biz.warehousein.WarehouseInService;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.settlement.WarehouseInPayment;
import com.tqmall.legend.entity.shop.SupplierSettlementVO;
import com.tqmall.legend.entity.warehousein.WarehouseIn;
import com.tqmall.legend.enums.settlement.PayStatusEnum;
import com.tqmall.legend.facade.settlement.PayFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by sven on 16/6/8.
 */
@Service
@Slf4j
public class PayFacadeImpl implements PayFacade {

    @Autowired
    private WarehouseInPaymentService warehouseInPaymentService;

    @Autowired
    private WarehouseInService warehouseInService;
    @Autowired
    private RpcPayService rpcPayService;
    @Autowired
    private ShopManagerService shopManagerService;

    @Override
    public List<WarehouseInPayment> getPaidSupplierPaymentLog(Map<String, Object> params) {
        return warehouseInPaymentService.select(params);
    }

    @Override
    public List<SupplierSettlementVO> getSupplierAomountList(Map<String, Object> searchParams) {
        return warehouseInService.statsSuppliersAmount(searchParams);
    }

    @Override
    public Long saveBill(PayBillVo payBillVo, UserInfo userInfo) {
        //付款单
        PayBillDTO payBill = new PayBillDTO();
        payBill.setBillName(payBillVo.getBillName());
        payBill.setBillTime(payBillVo.getBillTime() + ":00");
        payBill.setBillType(0L);//0:收款,1:退款
        payBill.setCreator(userInfo.getUserId());
        payBill.setModifier(userInfo.getUserId());
        payBill.setOperatorName(payBillVo.getOperatorName());
        payBill.setPaidAmount(payBillVo.getAmount());
        payBill.setPayeeName(payBillVo.getPayeeName());
        payBill.setPayStatus(PayStatusEnum.ZFWC.name());
        payBill.setPayTypeId(payBillVo.getPayTypeId());
        payBill.setShopId(userInfo.getShopId());
        payBill.setUnpaidAmount(BigDecimal.ZERO);
        payBill.setTotalAmount(payBillVo.getAmount());
        payBill.setRemark(payBillVo.getRemark());
        //流水
        PayBillFlowDTO payBillFlowDTO = new PayBillFlowDTO();
        payBillFlowDTO.setCreator(userInfo.getUserId());
        payBillFlowDTO.setModifier(userInfo.getUserId());
        payBillFlowDTO.setPaymentId(payBillVo.getPaymentId());
        payBillFlowDTO.setPaymentName(payBillVo.getPaymentName());
        payBillFlowDTO.setRemark(payBillVo.getRemark());
        payBillFlowDTO.setShopId(userInfo.getShopId());
        payBillFlowDTO.setPayAmount(payBillVo.getAmount());
        payBillFlowDTO.setFlowTime(payBillVo.getBillTime() + ":00");
        List<PayBillFlowDTO> payBillFlowDTOs = new ArrayList<>();
        payBillFlowDTOs.add(payBillFlowDTO);
        Result<Long> flowResult = rpcPayService.saveBill(payBill, payBillFlowDTOs);
        if (flowResult.isSuccess()) {
            log.info("[DUBBO] 调用结算中心付款单新增接口成功");
        } else {
            log.error("[DUBBO] 调用结算中心付款单新增接口失败,返回参数:{}", JSONUtil.object2Json(flowResult));
            throw new RuntimeException("[DUBBO] 调用结算中心付款单新增接口失败");
        }
        return flowResult.getData();
    }

    @Override
    public DefaultPage<PayResultVo> getPayFlowPage(final PayParam payParam) {
        return new BizTemplate<DefaultPage<PayResultVo>>() {
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
            protected DefaultPage<PayResultVo> process() throws BizException {
                log.info("[DUBBO]:调用结算中心获取流水记录接口");
                com.tqmall.core.common.entity.Result<DefaultResult<PayResult>> result = rpcPayService.queryFlow(payParam);
                if (result == null || !result.isSuccess()) {
                    log.error("[DUBBO]:调用结算中心获取流水记录接口失败,错误原因:{}", JSONUtil.object2Json(result));
                    throw new BizException("查询付款流水记录失败");
                }

                DefaultResult<PayResult> data = result.getData();
                List<PayResultVo> payResultVos = new ArrayList<>();
                PageRequest pageRequest = new PageRequest(payParam.getPageNum(), payParam.getPageSize());
                if (data == null) {
                    return new DefaultPage<>(payResultVos, pageRequest, 0);
                }
                List<PayResult> payResults = data.getContent();
                Set<Long> userIds = new HashSet<>();
                if (!CollectionUtils.isEmpty(payResults)) {
                    for (PayResult payResult : payResults) {
                        String flowTime = payResult.getFlowTime();
                        if (flowTime != null) {
                            Date date = DateUtil.convertStringToDate(flowTime);
                            payResult.setFlowTime(DateUtil.convertDateToYMDHHmm(date));
                        }
                        userIds.add(payResult.getCreator());
                        PayResultVo payResultVo = new PayResultVo();
                        BeanUtils.copyProperties(payResult, payResultVo);
                        payResultVos.add(payResultVo);
                    }
                }
                if(!CollectionUtils.isEmpty(userIds)) {
                    Long[] idsArr = userIds.toArray(new Long[userIds.size()]);
                    List<ShopManager> shopManagerList = shopManagerService.selectByIds(idsArr);
                    Map<Long,ShopManager> shopManagerMap = Maps.uniqueIndex(shopManagerList, new Function<ShopManager, Long>() {
                        @Override
                        public Long apply(ShopManager shopManager) {
                            return shopManager.getId();
                        }
                    });
                    for (PayResultVo payResultVo : payResultVos) {
                        if(shopManagerMap.containsKey(payResultVo.getCreator())){
                            payResultVo.setCreatorName(shopManagerMap.get(payResultVo.getCreator()).getName());
                        }
                    }
                }
                Integer total = data.getTotal();
                return new DefaultPage<>(payResultVos, pageRequest, total);
            }
        }.execute();
    }

    @Override
    @Transactional
    public void batchUpdate(List<WarehouseIn> warehouseInList, List<PayBillDTO> payBillDTOList, PayBillVo payBillVo, UserInfo userInfo) {
        List<PayBillFlowDTO> payBillFlowDTOs = new ArrayList<>();
        List<WarehouseInPayment> warehouseInPaymentList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(warehouseInList)) {
            for (WarehouseIn warehouseIn : warehouseInList) {
                //付款单流水组装
                for (PayBillDTO payBill : payBillDTOList) {
                    if (payBill.getRelId().equals(warehouseIn.getId())) {
                        PayBillFlowDTO payBillFlowDTO = new PayBillFlowDTO();
                        payBillFlowDTO.setBillId(payBill.getId());
                        payBillFlowDTO.setCreator(userInfo.getUserId());
                        payBillFlowDTO.setFlowType(0l);//付款类型为付款
                        payBillFlowDTO.setModifier(userInfo.getUserId());
                        payBillFlowDTO.setPaymentId(payBillVo.getPaymentId());
                        payBillFlowDTO.setPaymentName(payBillVo.getPaymentName());
                        payBillFlowDTO.setRemark(payBillVo.getRemark());
                        payBillFlowDTO.setShopId(userInfo.getShopId());
                        if (warehouseInList.size() == 1) {
                            payBillFlowDTO.setPayAmount(payBillVo.getAmount());
                        } else {
                            payBillFlowDTO.setPayAmount(payBill.getUnpaidAmount());
                        }
                        //组装legend_warehouse_in_payment
                        WarehouseInPayment payment = makeWarehouseInPayment(payBillFlowDTO, warehouseIn, payBillVo);
                        warehouseInPaymentList.add(payment);
                        payBillFlowDTOs.add(payBillFlowDTO);
                        break;
                    }
                }
            }
            for (WarehouseIn warehouseIn : warehouseInList) {
                Map<String, Object> map = new HashMap<>();
                map.put("amountPaid", warehouseIn.getAmountPaid());
                map.put("amountPayable", warehouseIn.getAmountPayable());
                map.put("modifier", userInfo.getUserId());
                map.put("shopId", userInfo.getShopId());
                map.put("id", warehouseIn.getId());
                map.put("paymentStatus", warehouseIn.getPaymentStatus());
                map.put("paymentComment", warehouseIn.getPaymentComment());
                warehouseInService.update(map);
            }
            //数据双写 插入legend_warehouse_in_payment
            warehouseInPaymentService.batchInsert(warehouseInPaymentList);

            log.info("[DUBBO] 调用结算中心付款接口 start 传入参数ppayBillFlowDTOs:{}", payBillFlowDTOs);

            Result result = rpcPayService.batchUpdateBill(payBillFlowDTOs);
            if (!result.isSuccess()) {
                log.error("[DUBBO] 调用结算中心付款接口失败,返回参数:{}", JSONUtil.object2Json(result));
                throw new RuntimeException("[DUBBO]:结算中心付款付款单,付款流水批量更新失败失败");
            }
            log.info("[DUBBO] 调用结算中心付款接口成功");
        }
    }



    /**
     * 组装WarehouseInPayment
     *
     * @return
     */
    private WarehouseInPayment makeWarehouseInPayment(PayBillFlowDTO payBillFlow, WarehouseIn warehouseIn, PayBillVo payBillVo) {
        WarehouseInPayment payment = new WarehouseInPayment();
        payment.setModifier(payBillFlow.getModifier());
        payment.setCreator(payBillFlow.getCreator());

        payment.setPayer(payBillVo.getOperatorName());
        payment.setPaymentId(payBillFlow.getPaymentId());
        payment.setPaymentName(payBillFlow.getPaymentName());
        payment.setShopId(payBillFlow.getShopId());
        payment.setPayAmount(payBillFlow.getPayAmount());
        payment.setWarehouseInId(warehouseIn.getId());

        return payment;
    }
}
