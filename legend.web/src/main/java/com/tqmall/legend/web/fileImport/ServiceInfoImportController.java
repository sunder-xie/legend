package com.tqmall.legend.web.fileImport;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.util.ExcelReader;
import com.tqmall.legend.common.fileImport.CommonFileImportHandle;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.FileUtil;
import com.tqmall.legend.web.fileImport.vo.ServiceInfoImportContext;
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
 * Created by twg on 16/12/5.
 */
@Slf4j
@Controller
@RequestMapping("init/import")
public class ServiceInfoImportController extends BaseController {
    @Resource(name = "serviceInfoImport")
    private CommonFileImportHandle fileImportHandle;

    /**
     * 服务导入入口
     *
     * @return
     */
    @RequestMapping("serviceInfo")
    public String importServiceInfo() {
        return "yqx/page/setting/serviceInfo/serviceInfo-import";
    }

    /*服务导入*/
    @RequestMapping(value = "serviceInfo/import_file", method = RequestMethod.POST)
    @ResponseBody
    public Result<ServiceInfoImportContext> importServiceInfoFile(@RequestParam(value = "excelFile") MultipartFile fileUpload) {
        ServiceInfoImportContext serviceInfoImportContext = new ServiceInfoImportContext();
        serviceInfoImportContext.setShopId(UserUtils.getShopIdForSession(request));
        serviceInfoImportContext.setUserId(UserUtils.getUserIdForSession(request));
        try {
            Boolean flag = ExcelReader.readHeader(fileUpload.getInputStream(), ShopServiceInfo.class);
            if (!flag) {
                return Result.wrapErrorResult("", "导入的模板格式有误，请核对后导入！");
            }
            //1.上传文件
            com.tqmall.legend.common.Result<String> result = FileUtil.fileUploadExcel(fileUpload, request);
            if (!result.isSuccess()) {
                log.error("【服务导入】文件上传失败");
                return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
            }
            String sourceFilePath = result.getData();
            fileImportHandle.validation(sourceFilePath, serviceInfoImportContext);

            Map<Integer, List<String>> rowFailedMessages = serviceInfoImportContext.getRowFaildMessages();
            if (!rowFailedMessages.isEmpty()) {
                String suffix = sourceFilePath.substring(sourceFilePath.lastIndexOf("."));
                Result<String> res = FileUtil.createExcelFile("导入服务错误信息",request, suffix);
                if (res.isSuccess()) {
                    String targetFilePath = res.getData();
                    fileImportHandle.handleFailedMessage(6, sourceFilePath, targetFilePath, serviceInfoImportContext);
                }
            }
        } catch (IOException e) {
            log.error("服务导入异常：", e);
            return Result.wrapErrorResult("","服务导入异常");
        } catch (Throwable e){
            log.error("服务导入异常：", e);
            return Result.wrapErrorResult("","服务导入异常");
        }
        return Result.wrapSuccessfulResult(serviceInfoImportContext);
    }

    /****************************************************
     * *************************************************
     * *******************模板下载***********************
     *
     * **************************************************
     */

    /**
     * 下载服务模板
     *
     * @param response
     */
    @RequestMapping("serviceInfo/template-download")
    @ResponseBody
    public void DownloadServiceInfoTemplate(HttpServletResponse response) {
        String[] headers = { "*服务名称", "服务价格", "服务类型(默认为小修)", "车辆级别", "服务单位(默认为工时)", "服务编号" };
        FileUtil.downExcel(response, headers, "导入服务模板");
    }
}
