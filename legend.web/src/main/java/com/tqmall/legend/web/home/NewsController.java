package com.tqmall.legend.web.home;

import com.google.common.collect.Lists;
import com.tqmall.legend.biz.article.NewsService;
import com.tqmall.legend.biz.article.NewsTypeService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.article.News;
import com.tqmall.legend.entity.article.NewsType;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/7/24.
 */
@Controller
@Slf4j
@RequestMapping("home/news")
public class NewsController extends BaseController {
    @Resource
    private NewsService newsService;
    @Resource
    private NewsTypeService newsTypeService;

    /**
     * 资讯列表页面初始化
     *
     * @return
     */
    @RequestMapping(value = "news-list", method = RequestMethod.GET)
    public String index(Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("limit", 6);
        param.put("offset", 0);
        List<NewsType> newsTypeList = newsTypeService.select(param);
        model.addAttribute("newsTypeList", newsTypeList);
        model.addAttribute("moduleUrl", ModuleUrlEnum.NEWS.getModuleUrl());
        getPopularNews(model);
        return "/yqx/page/home/news/news-list";
    }

    /**
     * 资讯列表数据初始化
     *
     * @param pageable
     * @param newsTypeId
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody

    public Result list(@PageableDefault(page = 1, value = 10, sort = "modified_time", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(value = "newsTypeId", required = false) Long newsTypeId) {
        Map<String, Object> param = new HashMap<>();
        if (newsTypeId != null && newsTypeId != 0L) {
            param.put("newsTypeId", newsTypeId);
        }
        DefaultPage<News> page = newsService.getPage(param, pageable);
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 资讯详情
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "news-detail", method = RequestMethod.GET)
    public String detail(@RequestParam(value = "id") Long id, Model model) {
        if (id == null) {
            return "common/error";
        }
        News news = newsService.selectById(id);
        if (news == null) {
            return "common/error";
        }
        long total = newsService.addViewTimes(id);
        model.addAttribute("news", news);
        model.addAttribute("moduleUrl", ModuleUrlEnum.NEWS.getModuleUrl());
        getPopularNews(model);
        return "/yqx/page/home/news/news-detail";
    }

    private void getPopularNews(Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("sorts", Lists.newArrayList("news_sort asc", "view_times desc"));
        param.put("offset", 0);
        param.put("limit", 5);
        List<News> newsList = newsService.select(param);
        model.addAttribute("popularNewsList", newsList);
    }
}
