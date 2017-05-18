package com.tqmall.common.template;

import com.tqmall.common.exception.BizException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by xin on 16/9/22.
 */
public abstract class DownloadApiTemplate extends ApiTemplate<String> {

    public void download(String fileName, HttpServletResponse response) throws IOException {
        download(fileName, response, true);
    }

    public void download(String fileName, HttpServletResponse response, boolean delete) throws IOException {
        try {
            checkParams();
        } catch (IllegalArgumentException e) {
            logger.debug("check param failed:{}", e);
            throw e;
        }
        long start = System.currentTimeMillis();
        try {
            String filePath = process();
            Path path = Paths.get(filePath);
            File file = path.toFile();
            if (file.exists() && file.isFile()) {
                try {
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    logger.error("文件下载, 文件名encode异常, 文件名: {}", fileName, e);
                }
                String mimeType = URLConnection.guessContentTypeFromName(fileName);
                if (mimeType == null || "".equals(mimeType)) {
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setContentLength((int) file.length());
                try (ServletOutputStream out = response.getOutputStream()) {
                    Files.copy(path, out);
                } catch (IOException ex) {
                    logger.error("文件下载异常, 文件路径: {}", filePath, ex);
                    throw ex;
                }
                if (delete) {
                    Files.delete(path);
                }
            } else {
                logger.error("文件下载, 文件不存在, 文件路径: {}", filePath);
                throw new FileNotFoundException("文件不存在");
            }
            onSuccess();
        } catch (BizException biz) {
            onError(biz);
            throw biz;
        } catch (Throwable e) {
            onError(e);
            throw e;
        } finally {
            afterProcess();
            long totalTime = System.currentTimeMillis() - start;
            if (totalTime > THRESHOLD) {
                logger.warn("This method used too long time please check and optimize it:{}ms", totalTime);
            }
        }
    }
}
