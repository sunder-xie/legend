package com.tqmall.legend.web.fileImport;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.util.ExcelReader;
import com.tqmall.legend.common.fileImport.CommonFileImportHandle;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.FileUtil;
import com.tqmall.legend.web.fileImport.vo.CustomerCarImportContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/12/5.
 */
@Slf4j
@Controller
@RequestMapping("init/import")
public class CustomerCarImportController extends BaseController {
    @Resource(name = "customerCarImport")
    private CommonFileImportHandle fileImportHandle;

    @RequestMapping("customerCar")
    public String importCustomerCar() {
        return "yqx/page/setting/customerCar/customerCar-import";
    }

    @RequestMapping(value = "customerCar/import_file", method = RequestMethod.POST)
    @ResponseBody
    public Result<CustomerCarImportContext> importCustomerCarFile(@RequestParam(value = "excelFile") MultipartFile fileUpload) {
        CustomerCarImportContext customerCarImportContext = new CustomerCarImportContext();
        customerCarImportContext.setShopId(UserUtils.getShopIdForSession(request));
        customerCarImportContext.setUserId(UserUtils.getUserIdForSession(request));

        try {
            Boolean flag = ExcelReader.readHeader(fileUpload.getInputStream(), CustomerCar.class);
            if (!flag) {
                return Result.wrapErrorResult("", "导入的模板格式有误，请核对后导入！");
            }
            //1.上传文件
            com.tqmall.legend.common.Result<String> result = FileUtil.fileUploadExcel(fileUpload, request);
            if (!result.isSuccess()) {
                log.error("【客户车辆导入】文件上传失败");
                return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
            }
            String sourceFilePath = result.getData();
            int rowSize = ExcelReader.getSize(sourceFilePath);//需要循环的次数
            //限制早上9点~18点如果超过3600行不让导入及3601，3601/1800 循环3次
            String hour = DateFormatUtils.format(new Date(), "HH");
            if (8 < Integer.parseInt(hour) && Integer.parseInt(hour) < 18 && rowSize >= 3) {
                return Result.wrapErrorResult("", LegendErrorCode.IMPORT_DATE_ERROR.getErrorMessage());
            }
            fileImportHandle.validation(sourceFilePath, customerCarImportContext);
            Map<Integer, List<String>> rowFailedMessages = customerCarImportContext.getRowFaildMessages();
            if (!rowFailedMessages.isEmpty()) {
                String suffix = sourceFilePath.substring(sourceFilePath.lastIndexOf("."));
                Result<String> res = FileUtil.createExcelFile("导入客户车辆错误信息", request, suffix);
                if (res.isSuccess()) {
                    String targetFilePath = res.getData();
                    fileImportHandle.handleFailedMessage(27, sourceFilePath, targetFilePath, customerCarImportContext);
                }
            }
        } catch (Exception e) {
            log.error("客户车辆导入异常.", e);
            return Result.wrapErrorResult("", "客户车辆导入异常");
        } catch (Throwable e) {
            log.error("客户车辆导入异常.", e);
            return Result.wrapErrorResult("", "客户车辆导入异常");
        }

        return Result.wrapSuccessfulResult(customerCarImportContext);
    }

    /**
     * 下载客户车辆模板
     */
    @RequestMapping("customerCar/template-download")
    @ResponseBody
    public void downloadCustomerCarTemplate(HttpServletResponse response) {
        String[] headers = { "*车牌", "车主(允许为公司名称)", "车主电话", "驾驶证号", "客户地址", "客户生日(1900/01/01)", "客户单位", "身份证", "驾驶证年检日期(1900/01/01)", "驾驶证领证日期(1900/01/01)", "联系人(驾驶员)", "客户电话", "备注", "VIN码", "发动机号", "车品牌", "车系列", "车别名", "厂家", "保险时间(1900/01/01)", "购车时间(1900/01/01)", "年审日期(1900/01/01)", "保险公司", "车身颜色", "车牌类型(本地/外地)", "行驶里程/公里", "固定电话(区号-号码)" };
        FileUtil.downExcel(response, headers, "导入客户车辆模版");
    }
}
