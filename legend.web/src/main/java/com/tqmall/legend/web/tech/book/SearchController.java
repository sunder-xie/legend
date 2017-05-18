package com.tqmall.legend.web.tech.book;

import com.tqmall.common.util.HttpUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 资料库搜索
 * TODO 2016-11-29 legend->search 获取技术中心书籍 替换dubbo接口
 */
@Slf4j
@Controller
@RequestMapping("shop/tech/search")
public class SearchController extends BaseController {

    @Value("${i.search.url}")
    private String searchUrl;

    /**
     * 搜索页面
     *
     * @param model
     * @param keywords
     * @return
     */
    @RequestMapping
    public String index(Model model, @RequestParam(value = "keywords", required = false) String keywords) {
        if (keywords != null) {
            model.addAttribute("keywords", keywords);
        }
        return "tech/book/search_list";
    }


    /**
     * 获取搜索内容
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object searchList(HttpServletRequest request, Integer page, Integer size) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        // 参数集合
        StringBuffer requestParam = new StringBuffer("1=1");
        if (searchParams.containsKey("keywords")) {
            String keyWords = searchParams.get("keywords").toString();
            if (!StringUtil.isNull(keyWords)) {
                requestParam.append("&searchKeys=" + keyWords);
            }
        }
        if (page != null) {
            requestParam.append("&page=" + page);
        }
        if (size != null) {
            requestParam.append("&size=" + size);
        }
        String requestUrl = searchUrl + "elasticsearch/cloudRepair/legendLibrary/library";
        log.info("[搜索平台]HTTP获取技术中心书籍 url:{} 参数:{}", requestUrl, requestParam);
        String result = null;
        try {
            result = HttpUtil.sendGet(requestUrl, requestParam.toString(), 1000, 6000);
        } catch (Exception e) {
            log.error("[搜索平台]HTTP获取技术中心书籍 异常:{}", e);
            return Result.wrapErrorResult("", "获取技术中心书籍失败");
        }
        if (StringUtils.isEmpty(result)) {
            log.error("[搜索平台]HTTP获取技术中心书籍数据,返回结果为空");
            return Result.wrapErrorResult("", "获取技术中心书籍失败");
        }

        Object librarys = null;
        try {
            librarys = JSONUtil.getMapper().readValue(result, Map.class);
        } catch (IOException e) {
            log.error("[搜索平台]HTTP获取技术中心书籍数据,转JSON出错:{}", e);
            return Result.wrapErrorResult("", "获取技术中心书籍失败");
        }

        return Result.wrapSuccessfulResult(librarys);
    }
}
