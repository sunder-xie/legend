package com.tqmall.legend.web.portal;

import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.article.ArticleCatService;
import com.tqmall.legend.biz.article.ArticleService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.notice.NoticeService;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.entity.article.Article;
import com.tqmall.legend.entity.article.ArticleCat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 登录前访问的帮助中心
 * Created by nawks on 3/12/15.
 */
@Controller
@RequestMapping("portal/help")
public class HelpCenterController extends PortalBaseController implements InitializingBean {

    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleCatService articleCatService;
    @Autowired
    NoticeService noticeService;

    @RequestMapping
    public String index(Model model, @RequestParam(value = "id", required = false) Long id) {
        model.addAttribute("portalNav", "help");
        //获取分类信息
        int shopLevel = 0;
        List<ArticleCat> articleCatList = articleCatService.getMenuList(0,shopLevel);

        //获取文章信息
        Article article = new Article();
        if (id != null) {
            article = articleService.selectById(id);
            if (article != null) {
                if (article.getStatus() == 0) {
                    return "redirect:help";
                }
            } else {
                return "redirect:help";
            }
        } else {
            //第一篇文章选中
            if (articleCatList != null && articleCatList.size() > 0) {
                Integer articleCatListSize = articleCatList.size();
                for (int i = 0; i < articleCatListSize; i++) {
                    //判断父分类是否有文章
                    if (articleCatList.get(i).getArticleList() != null && articleCatList.get(i).getArticleList().size() > 0) {
                        article = articleCatList.get(i).getArticleList().get(0);
                        break;
                    } else if (articleCatList.get(i).getChildCatList() != null && articleCatList.get(i).getChildCatList().size() > 0) {
                        //遍历子分类
                        Integer childCatListSize = articleCatList.get(i).getChildCatList().size();
                        for (int j = 0; j < childCatListSize; j++) {
                            if (articleCatList.get(i).getChildCatList().get(j).getArticleList().size() > 0) {
                                article = articleCatList.get(i).getChildCatList().get(j).getArticleList().get(0);
                                break;
                            }
                        }
                        if (article.getId() != null) {
                            break;
                        }
                    }
                }
            }
        }
        if(article.getId() == null){
            model.addAttribute("isHelp", true);
        }
        model.addAttribute("articleDetail", article);
        model.addAttribute("menuList", articleCatList);
        //增加一个是公告还是 文章请求的标志
        model.addAttribute("articleType", true);
        if (article.getId() != null) {
            List<ArticleCat> catList = articleCatService.getArticleCatTitleAndIdByArticleId(article.getId());
            model.addAttribute("catList", catList);
        }
        return "portal/help/index";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setPageName("help");
    }
}
