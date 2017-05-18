package com.tqmall.legend.web.fileImport;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.util.ExcelReader;
import com.tqmall.legend.common.fileImport.CommonFileImportHandle;
import com.tqmall.legend.entity.shop.Supplier;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.FileUtil;
import com.tqmall.legend.web.fileImport.vo.SupplierImportContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/12/12.
 */
@Slf4j
@Controller
@RequestMapping("init/import")
public class SupplierImportController extends BaseController {
    @Resource(name = "supplierImport")
    private CommonFileImportHandle fileImportHandle;

    /**
     * 供应商导入入口
     *
     * @return
     */
    @RequestMapping("supplier")
    public String importSupplier() {
        return "yqx/page/setting/supplier/supplier-import";
    }


    /*供应商导入*/
    @RequestMapping(value = "supplier/import_file", method = RequestMethod.POST)
    @ResponseBody
    public Result<SupplierImportContext> importSupplierFile(@RequestParam(value = "excelFile") MultipartFile fileUpload) {
        SupplierImportContext supplierImportContext = new SupplierImportContext();
        supplierImportContext.setShopId(UserUtils.getShopIdForSession(request));
        supplierImportContext.setUserId(UserUtils.getUserIdForSession(request));
        try {
            Boolean flag = ExcelReader.readHeader(fileUpload.getInputStream(), Supplier.class);
            if (!flag) {
                return Result.wrapErrorResult("", "导入的模板格式有误，请核对后导入！");
            }
            //1.上传文件
            com.tqmall.legend.common.Result<String> result = FileUtil.fileUploadExcel(fileUpload, request);
            if (!result.isSuccess()) {
                log.error("【供应商导入】文件上传失败");
                return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
            }
            String sourceFilePath = result.getData();
            fileImportHandle.validation(sourceFilePath, supplierImportContext);

            Map<Integer, List<String>> rowFailedMessages = supplierImportContext.getRowFaildMessages();
            if (!rowFailedMessages.isEmpty()) {
                String suffix = sourceFilePath.substring(sourceFilePath.lastIndexOf("."));
                Result<String> res = FileUtil.createExcelFile("导入供应商错误信息", request, suffix);
                if (res.isSuccess()) {
                    String targetFilePath = res.getData();
                    fileImportHandle.handleFailedMessage(7, sourceFilePath, targetFilePath, supplierImportContext);
                }
            }
        } catch (IOException e) {
            log.error("供应商导入异常：", e);
            return Result.wrapErrorResult("", "供应商导入异常");
        } catch (Throwable e) {
            log.error("供应商导入异常：", e);
            return Result.wrapErrorResult("", "供应商导入异常");
        }
        return Result.wrapSuccessfulResult(supplierImportContext);
    }


    /**
     * 下载供应商模板
     *
     * @param response
     */
    @RequestMapping("supplier/template-download")
    @ResponseBody
    public void DownloadSupplierTemplate(HttpServletResponse response) {
        String[] headers = { "*供应商编号", "*供应商名称", "固定电话(区号-号码)", "供应商地址", "联系人", "联系人电话", "主营业务" };
        FileUtil.downExcel(response, headers, "导入供应商模板");
    }

}
