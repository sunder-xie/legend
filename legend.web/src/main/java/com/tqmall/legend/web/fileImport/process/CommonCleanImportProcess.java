package com.tqmall.legend.web.fileImport.process;

import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by twg on 16/12/16.
 */
@Slf4j
@Component
public class CommonCleanImportProcess implements FileImportProcess {
    @Override
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()){
            log.debug("This is CommonCleanImportProcess");
        }
        fileImportContext.setExcelContents(null);
    }
}
