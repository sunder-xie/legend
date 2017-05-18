package com.tqmall.legend.web.fileImport;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.util.ExcelReader;
import com.tqmall.legend.common.fileImport.CommonFileImportHandle;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.FileUtil;
import com.tqmall.legend.web.fileImport.vo.MemberCardImportContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/12/6.
 */
@Slf4j
@Controller
@RequestMapping("init/import")
public class MemberCardImportController extends BaseController {
    @Resource(name = "memberCardFileImport")
    private CommonFileImportHandle fileImportHandle;

    @RequestMapping("memberCard")
    public String importCustomerCar() {
        return "yqx/page/setting/memberCard/memberCard-import";
    }

    @RequestMapping(value = "memberCard/import_file", method = RequestMethod.POST)
    @ResponseBody
    public Result<MemberCardImportContext> importMemberCardFile(@RequestParam(value = "excelFile") MultipartFile fileUpload) {
        MemberCardImportContext memberCardImportContext = new MemberCardImportContext();
        memberCardImportContext.setShopId(UserUtils.getShopIdForSession(request));
        memberCardImportContext.setUserId(UserUtils.getUserIdForSession(request));
        memberCardImportContext.setUserName(UserUtils.getUserInfo(request).getName());
        try {
            Boolean flag = ExcelReader.readHeader(fileUpload.getInputStream(), MemberCard.class);
            if (!flag) {
                return Result.wrapErrorResult("", "导入的模板格式有误，请核对后导入！");
            }
            //1.上传文件
            com.tqmall.legend.common.Result<String> result = FileUtil.fileUploadExcel(fileUpload, request);
            if (!result.isSuccess()) {
                log.error("【导入会员卡】文件上传失败");
                return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
            }
            String sourceFilePath = result.getData();
            fileImportHandle.validation(sourceFilePath, memberCardImportContext);

            Map<Integer, List<String>> rowFailedMessages = memberCardImportContext.getRowFaildMessages();
            if (!rowFailedMessages.isEmpty()) {
                String suffix = sourceFilePath.substring(sourceFilePath.lastIndexOf("."));
                Result<String> res = FileUtil.createExcelFile("导入会员卡错误信息", request, suffix);
                if (res.isSuccess()) {
                    String targetFilePath = res.getData();
                    fileImportHandle.handleFailedMessage(7, sourceFilePath, targetFilePath, memberCardImportContext);
                }
            }
        } catch (Exception e) {
            log.error("导入会员卡异常.", e);
            return Result.wrapErrorResult("", "导入会员卡异常");
        } catch (Throwable e) {
            log.error("导入会员卡异常.", e);
            return Result.wrapErrorResult("", "导入会员卡异常");
        }
        return Result.wrapSuccessfulResult(memberCardImportContext);
    }


    /**
     * 下载会员卡模板
     */
    @RequestMapping("memberCard/template-download")
    @ResponseBody
    public void downloadMemberCardTemplate(HttpServletResponse response) {
        String[] headers = { "车主电话(必填)", "会员卡类型(必填)", "会员卡号(必填)", "失效时间(必填)(2018/01/01)", "余额", "积分", "累计充值金额" };
        FileUtil.downExcel(response, headers, "导入会员卡模版");
    }

}
