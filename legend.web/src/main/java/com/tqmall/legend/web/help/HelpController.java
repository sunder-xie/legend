package com.tqmall.legend.web.help;

import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.article.ArticleCatService;
import com.tqmall.legend.biz.article.ArticleService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.notice.NoticeService;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.entity.article.Article;
import com.tqmall.legend.entity.article.ArticleCat;
import com.tqmall.legend.entity.notice.NoticeEntity;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 14/10/29.
 */
@Controller
@RequestMapping("shop/help")
public class HelpController extends BaseController {

    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleCatService articleCatService;

    @Autowired
    NoticeService noticeService;

    @HttpRequestLog
    @RequestMapping
    public String index(Model model, @RequestParam(value = "id", required = false) Long id, @RequestParam(value = "type", required = false) Integer type) {
        //获取分类信息
        if (id != null) {
            Article searchArticleById = articleService.selectById(id);
            if (searchArticleById == null || searchArticleById.getStatus() == 0) {
                return "redirect:help";
            }
            model.addAttribute("articleDetail", searchArticleById);
            type = searchArticleById.getType();
            List<ArticleCat> catList = articleCatService.getArticleCatTitleAndIdByArticleId(id);
            model.addAttribute("catList", catList);
        } else {
            return "yqx/page/help/notice";
        }
        model.addAttribute("type", type);
        model.addAttribute("moduleUrl", "help");
        return "yqx/page/help/index";
    }

    @RequestMapping("notice")
    public String index(Model model) {
        model.addAttribute("moduleUrl", "help");
        return "yqx/page/help/notice";
    }

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    @RequestMapping("getArticle")
    @ResponseBody
    public Result getArticleById(Long id) {
        Article article = articleService.selectById(id);
        if (article == null) {
            return Result.wrapErrorResult("", "没有数据");
        }
        return Result.wrapSuccessfulResult(article);
    }

    /**
     * 根据文章id获取分类名称和id
     *
     * @param id
     * @return
     */
    @RequestMapping("getArticleCatTitleAndId")
    @ResponseBody
    public Result getArticleCatTitleAndIdByArticleId(Long id) {
        if (id != null) {
            List<ArticleCat> articleCatList = articleCatService.getArticleCatTitleAndIdByArticleId(id);
            if (!CollectionUtils.isEmpty(articleCatList)) {
                return Result.wrapSuccessfulResult(articleCatList);
            } else {
                return Result.wrapErrorResult("", "此文章没有上级分类");
            }
        } else {
            return Result.wrapErrorResult("", "catId不存在");
        }
    }

    /*
 *获取公告列表信息，按什么字段排序呢,一般是按照发布时间去排序
 */
    @RequestMapping("notice_list")
    @ResponseBody
    public Result<DefaultPage<NoticeEntity>> listNotice(@PageableDefault(page = 0, value = 3, direction = Sort.Direction.DESC) Pageable pageable) {
        Result<DefaultPage<NoticeEntity>> result = null;
        try {
            Map<String, Object> map = ServletUtils.getParametersMapStartWith(request);
            UserInfo userInfo = UserUtils.getUserInfo(request);
            map.put("shopLevel",userInfo.getLevel());
            DefaultPage<NoticeEntity> page = noticeService.getNoticeByPage(pageable, map);
            result = Result.wrapSuccessfulResult(page);
        } catch (Exception e) {
            result = Result.wrapErrorResult("", "获取公告列表数据出错");
        }
        return result;
    }

    /*
     * 或许最新公告数据，后台根据用户是否上次登录时间小于最新公告发布的时间
     */
    @RequestMapping("latest_notice")
    @ResponseBody
    public Result<NoticeEntity> getLatestNotice(HttpServletRequest request, HttpServletResponse response) {
        Result<NoticeEntity> result = null;
        long userId = UserUtils.getUserIdForSession(request);
        Cookie cookie = CookieUtils.getCookieByName(request, Constants.PUBLIC_NOTICE_TIME + userId);
        String lastNoticeTime = null;
        if (null != cookie) {
            lastNoticeTime = cookie.getValue();
        }
        try {
            Integer shopLevel = UserUtils.getShopLevelForSession(request);
            NoticeEntity noticeEntity = noticeService.getLastPublicNotice(userId, lastNoticeTime, shopLevel);
            if (null != noticeEntity) {
                CookieUtils.addCookie(response, Constants.PUBLIC_NOTICE_TIME + userId, noticeEntity.getPublishTime(), 86400);
            }
            result = Result.wrapSuccessfulResult(noticeEntity);
        } catch (Exception e) {
            result = Result.wrapSuccessfulResult(null);
        }
        return result;
    }


    @RequestMapping("get-menu")
    @ResponseBody
    public Result getMenu(){
        Integer shopLevel = UserUtils.getShopLevelForSession(request);
        List<ArticleCat> articleCatList = articleCatService.getMenuList(1,shopLevel);
        return Result.wrapSuccessfulResult(articleCatList);
    }
}
