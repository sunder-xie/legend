package com.tqmall.legend.web.fileImport.process;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.legend.biz.order.OrderHistoryService;
import com.tqmall.legend.common.fileImport.ImportFailedMessages;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.order.OrderHistory;
import com.tqmall.legend.web.fileImport.vo.OrderHistoryImportContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by twg on 16/12/10.
 */
@Slf4j
@Component
public class OrderHistoryImportValidationProcess implements FileImportProcess {
    @Autowired
    private OrderHistoryService orderHistoryService;

    @Override
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is OrderHistoryImportValidationProcess");
        }
        OrderHistoryImportContext orderHistoryImportContext = (OrderHistoryImportContext) fileImportContext;
        Long shopId = orderHistoryImportContext.getShopId();
        Long userId = orderHistoryImportContext.getUserId();
        List<OrderHistory> orderHistories = orderHistoryImportContext.getExcelContents();
        List<String> faildMessages = orderHistoryImportContext.getFaildMessage();
        Map<Integer, List<String>> rowFaildMessages = orderHistoryImportContext.getRowFaildMessages();

        List<String> orderSns = getOrderHistoryList(shopId, orderHistories);

        Map<String, OrderHistory> orderHistoryParamMap = Maps.newHashMap();
        List<OrderHistory> orderHistoryList = Lists.newArrayList();
        for (int i = 0; i < orderHistories.size(); i++) {
            OrderHistory orderHistory = orderHistories.get(i);
            int rowNumber = orderHistory.getRowNumber() + 1;
            String orderSn = orderHistory.getOrderSn();
            String carLicense = orderHistory.getCarLicense().toUpperCase();
            if (orderHistoryParamMap.containsKey(orderSn) || orderSns.contains(orderSn)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_REPEAT_MESSAGE, rowNumber, orderSn, "工单编号");
                String faildMes = String.format(ImportFailedMessages.FAILED_REPEAT_MESSAGE, orderSn, "工单编号");
                faildMessages.add(faildMessage);
                setRowFalidMessage(rowFaildMessages, rowNumber, faildMes);
                continue;
            }
            orderHistory.setShopId(shopId);
            orderHistory.setCreator(userId);
            if (orderHistory.getCarMileage() != null) {
                orderHistory.setMileage(orderHistory.getCarMileage().toString());
            }
            orderHistory.setCarLicense(carLicense);
            orderHistoryList.add(orderHistory);
            orderHistoryParamMap.put(orderSn, orderHistory);
        }
        orderHistoryImportContext.setExcelContents(orderHistoryList);
    }

    private void setRowFalidMessage(Map<Integer, List<String>> rowFaildMessages, int rowNumber, String faildMessage) {
        if (rowFaildMessages.containsKey(rowNumber)) {
            List<String> faild = rowFaildMessages.get(rowNumber);
            faild.add(faildMessage);
        } else {
            List<String> failds = Lists.newArrayList();
            failds.add(faildMessage);
            rowFaildMessages.put(rowNumber, failds);
        }
    }

    private List<String> getOrderHistoryList(Long shopId, List<OrderHistory> orderHistories) {
        Set<String> orderSn = Sets.newHashSet();
        for (OrderHistory orderHistory : orderHistories) {
            orderSn.add(orderHistory.getOrderSn());
        }
        if (CollectionUtils.isEmpty(orderSn)) {
            return Lists.newArrayList();
        }
        List<OrderHistory> orderHistoryList = orderHistoryService.findOrderHistoryByOrderSn(shopId, orderSn);
        return Lists.transform(orderHistoryList, new Function<OrderHistory, String>() {
            @Override
            public String apply(OrderHistory input) {
                return input.getOrderSn();
            }
        });
    }
}
