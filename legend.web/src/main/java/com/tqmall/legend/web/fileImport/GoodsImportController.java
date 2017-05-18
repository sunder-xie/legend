package com.tqmall.legend.web.fileImport;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.util.ExcelReader;
import com.tqmall.legend.common.fileImport.CommonFileImportHandle;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.FileUtil;
import com.tqmall.legend.web.fileImport.vo.GoodsImportContext;
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
public class GoodsImportController extends BaseController {
    @Resource(name = "goodsImport")
    private CommonFileImportHandle fileImportHandle;

    /**
     * 配件导入入口
     *
     * @return
     */
    @RequestMapping("goods")
    public String importGoods() {
        return "yqx/page/setting/goods/goods-import";
    }

    /*配件导入*/
    @RequestMapping(value = "goods/import_file", method = RequestMethod.POST)
    @ResponseBody
    public Result<GoodsImportContext> importGoodsFile(@RequestParam(value = "excelFile") MultipartFile fileUpload) {
        GoodsImportContext goodsImportContext = new GoodsImportContext();
        goodsImportContext.setShopId(UserUtils.getShopIdForSession(request));
        goodsImportContext.setUserId(UserUtils.getUserIdForSession(request));
        try {
            Boolean flag = ExcelReader.readHeader(fileUpload.getInputStream(), Goods.class);
            if (!flag) {
                return Result.wrapErrorResult("", "导入的模板格式有误，请核对后导入！");
            }
            //1.上传文件
            com.tqmall.legend.common.Result<String> result = FileUtil.fileUploadExcel(fileUpload, request);
            if (!result.isSuccess()) {
                log.error("【配件导入】文件上传失败");
                return Result.wrapErrorResult(result.getCode(), result.getErrorMsg());
            }
            String sourceFilePath = result.getData();
            fileImportHandle.validation(sourceFilePath, goodsImportContext);

            Map<Integer, List<String>> rowFailedMessages = goodsImportContext.getRowFaildMessages();
            if (!rowFailedMessages.isEmpty()) {
                String suffix = sourceFilePath.substring(sourceFilePath.lastIndexOf("."));
                Result<String> res = FileUtil.createExcelFile("导入配件错误信息", request, suffix);
                if (res.isSuccess()) {
                    String targetFilePath = res.getData();
                    fileImportHandle.handleFailedMessage(12, sourceFilePath, targetFilePath, goodsImportContext);
                }
            }
        } catch (IOException e) {
            log.error("配件导入异常：", e);
            return Result.wrapErrorResult("", "配件导入异常");
        } catch (Throwable e) {
            log.error("配件导入异常：", e);
            return Result.wrapErrorResult("", "配件导入异常");
        }
        return Result.wrapSuccessfulResult(goodsImportContext);
    }

    /****************************************************
     * *************************************************
     * *******************模板下载***********************
     *
     * **************************************************
     */

    /**
     * 下载物料模板
     *
     * @param response
     */
    @RequestMapping("goods/template-download")
    @ResponseBody
    public void dowloadGoodsTemplate(HttpServletResponse response) {
        String[] headers = { "*配件名称", "*零件号", "类别(默认为通用配件)", "单位(默认为个)", "品牌", "销售价格", "库存", "成本单价", "缺货临界数量", "仓位信息", "适用车型", "适用部位" };
        FileUtil.downExcel(response, headers, "导入配件模板");
    }
}
