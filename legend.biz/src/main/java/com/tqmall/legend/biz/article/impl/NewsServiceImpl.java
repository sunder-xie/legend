package com.tqmall.legend.biz.article.impl;

import com.google.common.collect.Lists;
import com.tqmall.legend.biz.article.NewsService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.dao.article.NewsDao;
import com.tqmall.legend.entity.article.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/7/22.
 */
@Service
@Slf4j
public class NewsServiceImpl extends BaseServiceImpl implements NewsService {
    @Autowired
    private NewsDao newsDao;
    @Autowired
    private JedisClient jedisClient;
    private final static String NEWS_VIEW_TIMES = "news_view_times";
    private final static String NEWS_APP_PV = "news_app_pv";

    @Override
    public DefaultPage<News> getPage(Map<String, Object> param, Pageable pageable) {
        DefaultPage<News> page = (DefaultPage<News>) super.getPage(newsDao, pageable, param);
        if (page != null && !CollectionUtils.isEmpty(page.getContent())) {
            makeNews(page.getContent());
        }
        return page;
    }

    @Override
    public List<News> select(Map<String, Object> param) {
        List<News> newsList = newsDao.select(param);
        makeNews(newsList);
        return newsList;
    }

    @Override
    public int selectCount(Map<String, Object> param) {
        return newsDao.selectCount(param);
    }

    @Override
    public News selectById(Long id) {
        return newsDao.selectById(id);
    }

    @Override
    public List<News> selectHomeNews(int size) {
        Map<String, Object> param = new HashMap<>();
        List<String> sorts = Lists.newArrayList("is_top desc", "gmt_modified desc");
        param.put("sorts", sorts);
        param.put("offset", 0);
        param.put("limit", size);
        return newsDao.select(param);
    }

    /**
     * 若缓存浏览次数小于数据库浏览次数则更新缓存,相等不更新,否则更新数据库
     */
    @Override
    public void batchUpdateViewTimes() {
        Map<String, Object> param = new HashMap<>();
        int page = 0;
        int pageSize = 500;
        int total = newsDao.selectCount(null);
        log.info("开始同步云修资讯浏览次数");
        Map<String, String> map = jedisClient.getKeyAll(NEWS_VIEW_TIMES);
        Map<String, String> appMap = jedisClient.getKeyAll(NEWS_APP_PV);
        while (total > 0) {
            param.put("limit", pageSize);
            param.put("offset", page * pageSize);
            List<News> newsList = newsDao.select(param);
            for (News news : newsList) {
                News n = null;
                //pc浏览量
                if (map.containsKey("id:" + news.getId())) {
                    long count = map.get("id:" + news.getId()) == null ? 0L : Long.parseLong(map.get("id:" + news.getId()));
                    if (news.getViewTimes() > count) {
                        String times = news.getViewTimes() == null ? "0" : news.getViewTimes().toString();
                        jedisClient.hset(NEWS_VIEW_TIMES, "id:" + news.getId(), times);

                    } else if (news.getViewTimes() < count) {
                        n = new News();
                        n.setId(news.getId());
                        n.setViewTimes(count);
                    }
                }
                //app浏览量
                if (appMap.containsKey("id:" + news.getId())) {
                    long appCount = appMap.get("id:" + news.getId()) == null ? 0L : Long.parseLong(appMap.get("id:" + news.getId()));
                    if (news.getAppPv() > appCount) {
                        String times = news.getAppPv() == null ? "0" : news.getAppPv().toString();
                        jedisClient.hset(NEWS_APP_PV, "id:" + news.getId(), times);

                    } else if (news.getAppPv() < appCount) {
                        if (n == null) {
                            n = new News();
                        }
                        n.setId(news.getId());
                        n.setAppPv(appCount);
                    }
                }
                if (n != null && n.getId() != null) {
                    newsDao.updateById(n);
                }
            }
            total -= 500;
            page++;
        }
        log.info("云修资讯浏览次数同步结束");
    }


    @Override
    public long addViewTimes(Long id) {
        return jedisClient.hinCrBy(NEWS_VIEW_TIMES, "id:" + id, 1);
    }

    @Override
    public long addAppPv(Long id) {
        return jedisClient.hinCrBy(NEWS_APP_PV, "id:" + id, 1);
    }

    @Override
    public int updateById(News news) {
        return newsDao.updateById(news);
    }

    /**
     * 组装浏览次数
     *
     * @param newsList
     */
    private void makeNews(List<News> newsList) {
        for (News news : newsList) {
            if (news.getId() != null) {
                String temp = (String) jedisClient.hget(NEWS_VIEW_TIMES, "id:" + news.getId());
                String appTemp = (String) jedisClient.hget(NEWS_APP_PV, "id" + news.getId());
                long times = (temp == null || "".equals(temp)) ? 0L : Long.parseLong(temp);
                long appTimes = (appTemp == null || "".equals(appTemp)) ? 0L : Long.parseLong(appTemp);
                news.setViewTimes(times);
                news.setAppPv(appTimes);
            }
        }
    }
}
