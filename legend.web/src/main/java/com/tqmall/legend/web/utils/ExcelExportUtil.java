package com.tqmall.legend.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by sven on 16/8/11.
 */
@Slf4j
public class ExcelExportUtil {
    public static ModelAndView export(List<?> list, HttpServletResponse response, String path, String fileName,long startTime) {
        ModelAndView view = new ModelAndView(path);
        view.addObject("viewList", list);
        // 设置响应头
        response.setContentType("application/x-msdownload");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("{}导出URLEncoder转义不正确", fileName);
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        long time = System.currentTimeMillis()-startTime;
        log.info("导出时间:{}毫秒", time);
        return view;
    }
}
