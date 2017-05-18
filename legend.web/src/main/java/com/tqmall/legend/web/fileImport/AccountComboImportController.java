package com.tqmall.legend.web.fileImport;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.util.ExcelReader;
import com.tqmall.legend.common.fileImport.CommonFileImportHandle;
import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.FileUtil;
import com.tqmall.legend.web.fileImport.vo.AccountComboImportContext;
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
 * Created by twg on 16/12/8.
 */
@Slf4j
@Controller
@RequestMapping("init/import")
public class AccountComboImportController extends BaseController {
    @Resource(name = "accountComboFileImport")
    private CommonFileImportHandle fileImportHandle;

    @RequestMapping("accountCombo")
    public String importAccountCombo() {
        return "yqx/page/setting/accountCombo/accountCombo-import";
    }

    @RequestMapping(value = "accountCombo/import_file", method = RequestMethod.POST)
    @ResponseBody
    public Result<AccountComboImportContext> importAccountComboFile(@RequestParam(value = "excelFile") MultipartFile fileUpload) {
        AccountComboImportContext comboImportContext = new AccountComboImportContext();
        comboImportContext.setShopId(UserUtils.getShopIdForSession(request));
        comboImportContext.setUserId(UserUtils.getUserIdForSession(request));
        try {
            Boolean flag = ExcelReader.readHeader(fileUpload.getInputStream(), AccountCombo.class);
            if (!flag) {
                return Result.wrapErrorResult("", "导入的模板格式有误，请核对后导入！");
            }
            //1.上传文件
            com.tqmall.legend.common.Result<String> result = FileUtil.fileUploadExcel(fileUpload, request);
            if (!result.isSuccess()) {
                log.error("【计次卡导入】文件上传失败");
                return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
            }
            String sourceFilePath = result.getData();
            fileImportHandle.validation(sourceFilePath, comboImportContext);

            Map<Integer, List<String>> rowFailedMessages = comboImportContext.getRowFaildMessages();
            if (!rowFailedMessages.isEmpty()) {
                String suffix = sourceFilePath.substring(sourceFilePath.lastIndexOf("."));
                Result<String> res = FileUtil.createExcelFile("导入计次卡错误信息", request, suffix);
                if (res.isSuccess()) {
                    String targetFilePath = res.getData();
                    fileImportHandle.handleFailedMessage(5, sourceFilePath, targetFilePath, comboImportContext);
                }
            }
        } catch (IOException e) {
            log.error("计次卡导入异常：", e);
            return Result.wrapErrorResult("", "计次卡导入异常");
        } catch (Throwable e) {
            log.error("计次卡导入异常：", e);
            return Result.wrapErrorResult("", "计次卡导入异常");
        }
        return Result.wrapSuccessfulResult(comboImportContext);
    }


    /**
     * 下载计次卡模板
     */
    @RequestMapping("accountCombo/template-download")
    @ResponseBody
    public void downloadAccountCouponTemplate(HttpServletResponse response) {
        String[] headers = { "车主电话(必填)", "计次卡名称(必填)", "服务名称(必填)", "服务次数(必填)", "过期时间(必填)(2018/01/01)" };
        FileUtil.downExcel(response, headers, "导入计次卡模版");
    }


}
