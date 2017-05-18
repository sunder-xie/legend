package com.tqmall.legend.server.news;


import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.article.NewsService;
import com.tqmall.legend.biz.article.NewsTypeService;
import com.tqmall.legend.entity.article.News;
import com.tqmall.legend.entity.article.NewsType;
import com.tqmall.legend.object.result.base.PageEntityDTO;
import com.tqmall.legend.object.result.news.LegendNewsDTO;
import com.tqmall.legend.object.result.news.NewClassifyEnum;
import com.tqmall.legend.object.result.news.NewTypeDTO;
import com.tqmall.legend.service.news.RpcLegendNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feilong.li on 17/4/11.
 */
@Service("rpcLegendNewsService")
@Slf4j
public class RpcLegendNewsServiceImpl implements RpcLegendNewsService {

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsTypeService newsTypeService;

    /**
     * 获取所有资讯的分类
     *
     * @return
     */
    @Override
    public Result<List<NewTypeDTO>> getAllNewsType() {
        return new ApiTemplate<List<NewTypeDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<NewTypeDTO> process() throws BizException {

                List<NewsType> newsTypeList = newsTypeService.select(new HashMap<String, Object>());
                if (!CollectionUtils.isEmpty(newsTypeList)) {
                    List<NewTypeDTO> resultNewsTypeList = new ArrayList<>();
                    for (NewsType temp : newsTypeList) {
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("newsTypeId", temp.getId());
                        if (newsService.selectCount(param) > 0) {
                            NewTypeDTO newTypeDTO = new NewTypeDTO();
                            BeanUtils.copyProperties(temp, newTypeDTO);
                            newTypeDTO.setClassifyType(NewClassifyEnum.NEWS.getCode());
                            resultNewsTypeList.add(newTypeDTO);
                        }
                    }
                    return resultNewsTypeList;
                }
                return null;
            }
        }.execute();
    }

    @Override
    public Result<PageEntityDTO<LegendNewsDTO>> getNewsList(final Long newsTypeId) {
        return new ApiTemplate<PageEntityDTO<LegendNewsDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected PageEntityDTO<LegendNewsDTO> process() throws BizException {
                Map<String, Object> searchParams = Maps.newHashMap();
                //已发布的记录
                searchParams.put("isPublished", 1);
                if (newsTypeId != null && newsTypeId > 0) {
                    searchParams.put("newsTypeId", newsTypeId);
                }
                /*
                 * 商家app不需要total字段,其他系统调用需要自行去掉注释!
                 */
                //Integer total = newsService.selectCount(searchParams);
                List<News> newsList = newsService.select(searchParams);
                List<LegendNewsDTO> newsDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(newsList)) {
                    for (News news : newsList) {
                        LegendNewsDTO newsDTO = new LegendNewsDTO();
                        converterNewsDTO(news, newsDTO);
                        newsDTOList.add(newsDTO);
                    }
                }
                PageEntityDTO<LegendNewsDTO> pageEntityDTO = new PageEntityDTO<LegendNewsDTO>();
                pageEntityDTO.setContent(newsDTOList);
                return pageEntityDTO;
            }
        }.execute();
    }

    private void converterNewsDTO(News news, LegendNewsDTO newsDTO) {
        newsDTO.setId(news.getId());
        newsDTO.setGmtCreate(news.getModifiedTime());
        newsDTO.setNewsTitle(news.getNewsTitle());
        newsDTO.setNewsSource(news.getNewsSource());
        newsDTO.setAppTag(news.getAppTag());
        newsDTO.setShowStyle(news.getShowStyle());
        newsDTO.setImgThumbUrl(news.getImgThumbUrl());
    }

    @Override
    public Result<LegendNewsDTO> getNewsDetail(final Long id) {
        return new ApiTemplate<LegendNewsDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "参数错误");
            }

            @Override
            protected LegendNewsDTO process() throws BizException {
                News news = newsService.selectById(id);
                if (null == news) {
                    log.info("[资讯详情] 没有找到资讯详情信息, id:{}", id);
                    throw new BizException("没有找到资讯信息");
                }
                LegendNewsDTO newsDTO = new LegendNewsDTO();
                converterNewsDTO(news, newsDTO);
                newsDTO.setNewsContent(news.getNewsContent());
                Long viewTimes = news.getViewTimes();
                Long appPv = news.getAppPv();
                viewTimes = viewTimes == null ? Long.valueOf(0) : viewTimes;
                appPv = appPv == null ? Long.valueOf(0) : appPv;
                newsDTO.setTotalViewTimes(viewTimes + appPv);
                newsDTO.setTotalUpvoteNum(news.getAppUpvoteCount());
                return newsDTO;
            }
        }.execute();
    }

    @Override
    public Result updateNewsAppPv(final Long id) {
        return new ApiTemplate<Long>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "参数错误");
            }

            @Override
            protected Long process() throws BizException {
                return newsService.addAppPv(id);
            }
        }.execute();
    }

    /**
     * 同步资讯浏览量
     *
     * @return
     */
    @Override
    public Result<Boolean> syncPv() {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected Boolean process() throws BizException {
                newsService.batchUpdateViewTimes();
                return true;
            }
        }.execute();
    }

    @Override
    public Result<Long> uptNewsPraise(final Long id, final Integer type) {

        return new ApiTemplate<Long>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "记录参数错误");
                Assert.notNull(type, "参数错误");
            }

            @Override
            protected Long process() throws BizException {
                log.info("[更新资讯点赞] id:{}, type:{}", id, type);
                News news = newsService.selectById(id);
                if (null != news) {
                    News newsUpt = new News();
                    newsUpt.setId(id);
                    if (type == 0) {
                        newsUpt.setAppUpvoteCount(news.getAppUpvoteCount() - 1);
                        newsService.updateById(newsUpt);
                        return newsUpt.getAppUpvoteCount();
                    } else if (type == 1) {
                        newsUpt.setAppUpvoteCount(news.getAppUpvoteCount() + 1);
                        newsService.updateById(newsUpt);
                        return newsUpt.getAppUpvoteCount();
                    }
                }

                throw new BizException("更新点赞失败");
            }
        }.execute();
    }
}
