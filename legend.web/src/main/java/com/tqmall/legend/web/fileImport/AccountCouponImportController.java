package com.tqmall.legend.web.fileImport;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.util.ExcelReader;
import com.tqmall.legend.common.fileImport.CommonFileImportHandle;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.FileUtil;
import com.tqmall.legend.web.fileImport.vo.AccountCouponImportContext;
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
 * Created by twg on 16/12/7.
 */
@Slf4j
@Controller
@RequestMapping("init/import")
public class AccountCouponImportController extends BaseController {
    @Resource(name = "accountCouponFileImport")
    private CommonFileImportHandle fileImportHandle;


    @RequestMapping("accountCoupon")
    public String importAccountCoupon() {
        return "yqx/page/setting/accountCoupon/accountCoupon-import";
    }

    @RequestMapping(value = "accountCoupon/import_file", method = RequestMethod.POST)
    @ResponseBody
    public Result<AccountCouponImportContext> importAccountCouponFile(@RequestParam(value = "excelFile") MultipartFile fileUpload) {
        AccountCouponImportContext couponImportContext = new AccountCouponImportContext();
        couponImportContext.setShopId(UserUtils.getShopIdForSession(request));
        couponImportContext.setUserId(UserUtils.getUserIdForSession(request));
        try {
            Boolean flag = ExcelReader.readHeader(fileUpload.getInputStream(), AccountCoupon.class);
            if (!flag) {
                return Result.wrapErrorResult("", "导入的模板格式有误，请核对后导入！");
            }
            //1.上传文件
            com.tqmall.legend.common.Result<String> result = FileUtil.fileUploadExcel(fileUpload, request);
            if (!result.isSuccess()) {
                log.error("【优惠券导入】文件上传失败");
                return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
            }
            String sourceFilePath = result.getData();
            fileImportHandle.validation(sourceFilePath, couponImportContext);

            Map<Integer, List<String>> rowFailedMessages = couponImportContext.getRowFaildMessages();
            if (!rowFailedMessages.isEmpty()) {
                String suffix = sourceFilePath.substring(sourceFilePath.lastIndexOf("."));
                Result<String> res = FileUtil.createExcelFile("导入优惠券错误信息", request, suffix);
                if (res.isSuccess()) {
                    String targetFilePath = res.getData();
                    fileImportHandle.handleFailedMessage(4, sourceFilePath, targetFilePath, couponImportContext);
                }
            }
        } catch (IOException e) {
            log.error("优惠券导入异常：", e);
            return Result.wrapErrorResult("", "优惠券导入异常");
        } catch (Throwable e) {
            log.error("优惠券导入异常：", e);
            return Result.wrapErrorResult("", "优惠券导入异常");
        }
        return Result.wrapSuccessfulResult(couponImportContext);
    }


    /**
     * 下载优惠券模板
     */
    @RequestMapping("accountCoupon/template-download")
    @ResponseBody
    public void downloadAccountCouponTemplate(HttpServletResponse response) {
        String[] headers = { "车主电话(必填)", "优惠券名称(必填)", "优惠券数量(必填)", "过期时间(必填)(2018/01/01)" };
        FileUtil.downExcel(response, headers, "导入优惠券模版");
    }
}
