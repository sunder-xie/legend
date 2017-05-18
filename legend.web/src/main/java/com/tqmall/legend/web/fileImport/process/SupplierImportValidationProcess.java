package com.tqmall.legend.web.fileImport.process;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.shop.SupplierService;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.common.fileImport.ImportFailedMessages;
import com.tqmall.legend.entity.shop.Supplier;
import com.tqmall.legend.web.fileImport.vo.SupplierImportContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/12/12.
 */
@Slf4j
@Component
public class SupplierImportValidationProcess implements FileImportProcess {
    @Autowired
    private SupplierService supplierService;

    @Override
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is SupplierImportValidationProcess");
        }
        SupplierImportContext supplierImportContext = (SupplierImportContext) fileImportContext;
        Long shopId = supplierImportContext.getShopId();
        Long userId = supplierImportContext.getUserId();
        List<Supplier> suppliers = supplierImportContext.getExcelContents();
        List<String> faildMessages = supplierImportContext.getFaildMessage();
        Map<Integer, List<String>> rowFaildMessages = supplierImportContext.getRowFaildMessages();

        List<Supplier> supplierList = getSuppliers(shopId, suppliers);
        List<String> supplierSnList = Lists.transform(supplierList, new Function<Supplier, String>() {
            @Override
            public String apply(Supplier input) {
                return input.getSupplierSn();
            }
        });


        Map<String, Supplier> supplierMap = Maps.newHashMap();
        List<Supplier> supplierParamList = Lists.newArrayList();
        for (int i = 0; i < suppliers.size(); i++) {
            Supplier supplier = suppliers.get(i);
            int rowNumber = supplier.getRowNumber() + 1;
            String supplierSn = supplier.getSupplierSn();

            if (supplierMap.containsKey(supplierSn) || supplierSnList.contains(supplierSn)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_REPEAT_MESSAGE, rowNumber, supplierSn, "供应商编号");
                String faildMes = String.format(ImportFailedMessages.FAILED_REPEAT_MESSAGE, supplierSn, "供应商编号");
                faildMessages.add(faildMessage);
                setRowFalidMessage(rowFaildMessages, rowNumber, faildMes);
                continue;
            }
            supplier.setCategory(1);
            supplier.setPayMethod(1);
            supplier.setInvoiceType(1);
            supplier.setShopId(shopId);
            supplier.setCreator(userId);
            supplierParamList.add(supplier);
            supplierMap.put(supplierSn, supplier);
        }
        supplierImportContext.setExcelContents(supplierParamList);
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

    private List<Supplier> getSuppliers(Long shopId, List<Supplier> suppliers) {
        List<String> supplierSns = Lists.transform(suppliers, new Function<Supplier, String>() {
            @Override
            public String apply(Supplier input) {
                return input.getSupplierSn();
            }
        });

        return supplierService.findBySupplierSns(shopId, supplierSns);
    }
}
