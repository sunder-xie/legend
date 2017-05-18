package com.tqmall.legend.web.common;

import com.tqmall.core.common.entity.Result;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lilige on 15/11/16.
 * 对上传文件，下载文件的抽取
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);


    /**
     * 上传文件
     *
     * @param fileUpload
     * @return
     */
    public static com.tqmall.legend.common.Result fileUploadExcel(MultipartFile fileUpload, HttpServletRequest request) {
        try {
            String fileName = fileUpload.getOriginalFilename();
            String[] suffixs = fileName.split("\\.");
            String suffix = suffixs[suffixs.length - 1];
            String suffixLc = suffix.toLowerCase();
            if (suffixLc.equals("xls") || suffixLc.equals("xlsx")) {
                byte[] bytes = fileUpload.getBytes();
                Long fileSize = fileUpload.getSize();
                // TODO: 16/8/25 文件大小改成可配置
                if (fileSize <= 5 * 1024 * 1024) {
                    UUID uuid = UUID.randomUUID();
                    String uuidStr = uuid.toString().replace("-", "");
                    String uploadFilePath = "/upload/excel/" + uuidStr + "." + suffixLc;
                    ServletContext servletContext = request.getSession().getServletContext();
                    String path = servletContext.getRealPath(uploadFilePath);
                    File fileObj = new File(path);
                    FileCopyUtils.copy(bytes, fileObj);
                    return com.tqmall.legend.common.Result.wrapSuccessfulResult(path);
                } else {
                    return com.tqmall.legend.common.Result.wrapErrorResult("-1", "文件大小超过5M，文件内容可能包含其它格式，或进行多次导入");
                }
            } else {
                return com.tqmall.legend.common.Result.wrapErrorResult("-2", "文件类型错误，只支持xls或者xlsx文件");
            }
        } catch (Exception e) {
            logger.error("[上传文件错误]", e);
            return com.tqmall.legend.common.Result.wrapErrorResult("-3", "未知错误");
        }
    }

    public static com.tqmall.core.common.entity.Result<String> createExcelFile(String fileName,HttpServletRequest request,String suffix) {
        String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSS");
        StringBuilder sb = new StringBuilder("/upload/excel/");
        sb.append(fileName).append(time).append(suffix);
        ServletContext servletContext = request.getSession().getServletContext();
        String path = servletContext.getRealPath(sb.toString());
        return Result.wrapSuccessfulResult(path);
    }

    public static void downExcel(HttpServletResponse response, String[] headers, String fileName) {
        response.setContentType("application/binary;charset=utf-8");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        HSSFRow row = sheet.createRow(0);
        Integer i;
        for (i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
            workbook.write(outputStream);
        } catch (Exception e) {
            logger.error("下载 excel 异常，异常信息", e);
        }
    }

}
