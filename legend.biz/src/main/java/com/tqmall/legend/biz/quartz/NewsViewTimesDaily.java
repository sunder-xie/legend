package com.tqmall.legend.biz.quartz;

import com.tqmall.legend.biz.article.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sven on 16/7/26.
 */
@Component
@Slf4j
public class NewsViewTimesDaily {
    @Autowired
    private NewsService newsService;

    public void execute() {
        newsService.batchUpdateViewTimes();

    }
}
