package com.tqmall.legend.web.fileImport.process;

import com.tqmall.legend.biz.order.OrderHistoryService;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.order.OrderHistory;
import com.tqmall.legend.web.fileImport.vo.OrderHistoryImportContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by twg on 16/12/10.
 */
@Slf4j
@Component
public class OrderHistoryImportProcess implements FileImportProcess {
    @Autowired
    private OrderHistoryService orderHistoryService;
    @Override
    public void process(CommonFileImportContext fileImportContext) {
        OrderHistoryImportContext orderHistoryImportContext = (OrderHistoryImportContext) fileImportContext;
        List<OrderHistory> orderHistoryList = orderHistoryImportContext.getExcelContents();
        if (!CollectionUtils.isEmpty(orderHistoryList)) {
            orderHistoryService.batchSave(orderHistoryList);
            orderHistoryImportContext.setSuccessNum(orderHistoryList.size());
        }
    }
}
