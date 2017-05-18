package com.tqmall.legend.web.fileImport;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.util.ExcelReader;
import com.tqmall.legend.common.fileImport.CommonFileImportHandle;
import com.tqmall.legend.entity.order.OrderHistory;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.FileUtil;
import com.tqmall.legend.web.fileImport.vo.OrderHistoryImportContext;
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
 * Created by twg on 16/12/6.
 */
@Slf4j
@Controller
@RequestMapping("init/import")
public class OrderHistoryImportController extends BaseController {
    @Resource(name = "orderHistoryImport")
    private CommonFileImportHandle fileImportHandle;

    @RequestMapping("orderHistory")
    public String importOrderHistory() {
        return "yqx/page/setting/orderHistory/orderHistory-import";
    }


    @RequestMapping(value = "orderHistory/import_file", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderHistoryImportContext> importOrderHistoryFile(@RequestParam(value = "excelFile") MultipartFile fileUpload) {
        OrderHistoryImportContext orderHistoryImportContext = new OrderHistoryImportContext();
        orderHistoryImportContext.setShopId(UserUtils.getShopIdForSession(request));
        orderHistoryImportContext.setUserId(UserUtils.getUserIdForSession(request));
        try {
            Boolean flag = ExcelReader.readHeader(fileUpload.getInputStream(), OrderHistory.class);
            if (!flag) {
                return Result.wrapErrorResult("", "导入的模板格式有误，请核对后导入！");
            }
            //1.上传文件
            com.tqmall.legend.common.Result<String> result = FileUtil.fileUploadExcel(fileUpload, request);
            if (!result.isSuccess()) {
                log.error("【维修历史导入】文件上传失败");
                return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
            }
            String sourceFilePath = result.getData();
            fileImportHandle.validation(sourceFilePath, orderHistoryImportContext);

            Map<Integer, List<String>> rowFailedMessages = orderHistoryImportContext.getRowFaildMessages();
            if (!rowFailedMessages.isEmpty()) {
                String suffix = sourceFilePath.substring(sourceFilePath.lastIndexOf("."));
                Result<String> res = FileUtil.createExcelFile("导入维修历史错误信息", request, suffix);
                if (res.isSuccess()) {
                    String targetFilePath = res.getData();
                    fileImportHandle.handleFailedMessage(31, sourceFilePath, targetFilePath, orderHistoryImportContext);
                }
            }
        } catch (IOException e) {
            log.error("维修历史导入异常：", e);
            return Result.wrapErrorResult("", "维修历史导入异常");
        } catch (Throwable e) {
            log.error("维修历史导入异常：", e);
            return Result.wrapErrorResult("", "维修历史导入异常");
        }
        return Result.wrapSuccessfulResult(orderHistoryImportContext);
    }


    /**
     * 下载维修历史模板
     */
    @RequestMapping("orderHistory/template-download")
    @ResponseBody
    public void downloadOrderHistoryTemplate(HttpServletResponse response) {
        String[] headers = { "*工单编号", "*车牌", "开单时间(1900/01/01)", "接车人", "工单类型", "工单状态", "车辆型号", "里程数", "VIN码", "发动机号", "客户姓名", "客户手机", "联系人姓名", "联系人手机", "服务项目", "所需物料", "服务费合计", "服务费优惠", "物料费合计", "物料费优惠", "应付金额合计", "优惠金额", "实际应付金额", "实付金额", "挂账金额", "开单人", "维修工", "结算人", "完工时间(1900/01/01)", "结算时间(1900/01/01)", "备注" };
        FileUtil.downExcel(response, headers, "导入维修历史模板");
    }

}
