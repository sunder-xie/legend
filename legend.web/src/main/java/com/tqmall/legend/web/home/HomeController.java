package com.tqmall.legend.web.home;

import com.tqmall.common.Constants;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.activity.BannerConfigService;
import com.tqmall.legend.biz.article.IProductAlertService;
import com.tqmall.legend.biz.article.NewsService;
import com.tqmall.legend.entity.activity.BannerConfig;
import com.tqmall.legend.entity.article.News;
import com.tqmall.legend.entity.article.ProductAlert;
import com.tqmall.legend.enums.activity.BannerPositionEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.statistics.StatisticsHomeFacade;
import com.tqmall.legend.facade.statistics.vo.StatisticsHomeVo;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by xin on 16/7/22.
 */
@Controller
@RequestMapping("home")
public class HomeController extends BaseController{

    @Autowired
    private IProductAlertService productAlertService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private BannerConfigService bannerConfigService;
    @Autowired
    private StatisticsHomeFacade statisticsHomeFacade;


    /**
     * 首页
     * @param model
     * @return
     */
    @HttpRequestLog
    @RequestMapping("")
    public String newHomePage(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.NEWHOME.getModuleUrl());

        // 查询咨询
        List<News> newsList = newsService.selectHomeNews(6);
        model.addAttribute("newsList", newsList);

        // 查询产品
        List<ProductAlert> productAlertList = productAlertService.getTop3();
        model.addAttribute("productAlertList", productAlertList);

        // banner
        List<BannerConfig> bannerList = bannerConfigService.getListByPostion(BannerPositionEnum.INDEX.getposition());
        model.addAttribute("bannerList", bannerList);

        // 档口版门店设置统计信息
        Integer shopLevel = UserUtils.getShopLevelForSession(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        if(shopLevel != null && shopLevel.equals(Constants.SHOP_LEVEL_TQMALL_VERSION)){
            StatisticsHomeVo statisticsHomeVo = statisticsHomeFacade.getStatisticsHome(shopId);
            model.addAttribute("statisticsHome", statisticsHomeVo);
        }
        return "/yqx/page/home/home";
    }

    /**
     * 加盟云修宣传页面
     * @return
     */
    @RequestMapping("join")
    public String join() {
        return "yqx/page/home/tqmall-join";
    }
}
