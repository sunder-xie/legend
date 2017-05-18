package com.tqmall.yunxiu.web.pub;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by zsy on 16/7/9.
 * 条形码等生成接口
 */
@Slf4j
@Controller
@RequestMapping("pub/createImg")
public class PubCreateImgController {

    /**
     * 生成条形码
     *
     * @param text
     * @param width
     * @param height
     * @param response
     */
    @RequestMapping
    @ResponseBody
    public void orderImg(@RequestParam(value = "text", required = true) String text, @RequestParam(value = "width", required = false) Integer width, @RequestParam(value = "height", required = false) Integer height, HttpServletResponse response) {

        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        if (width == null) {
            width = 300;
        }
        if (height == null) {
            height = 50;
        }

        codeWidth = Math.max(codeWidth, width);

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, codeWidth, height, null);
            MatrixToImageWriter.writeToStream(bitMatrix, "jpeg", response.getOutputStream());
            response.setContentType("image/jpeg");
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            log.error("【条形码生成异常】，传入参数text:{}", text, e);
        }

    }
}
