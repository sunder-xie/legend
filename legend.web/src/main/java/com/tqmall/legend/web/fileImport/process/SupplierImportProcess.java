package com.tqmall.legend.web.fileImport.process;

import com.tqmall.legend.biz.shop.SupplierService;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.shop.Supplier;
import com.tqmall.legend.web.fileImport.vo.SupplierImportContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by twg on 16/12/12.
 */
@Slf4j
@Component
public class SupplierImportProcess implements FileImportProcess {
    @Autowired
    private SupplierService supplierService;

    @Override
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()){
            log.debug("This is SupplierImportProcess");
        }
        SupplierImportContext supplierImportContext = (SupplierImportContext) fileImportContext;
        List<Supplier> suppliers = supplierImportContext.getExcelContents();
        supplierService.batchSave(suppliers);
        supplierImportContext.setSuccessNum(suppliers.size());
    }
}
