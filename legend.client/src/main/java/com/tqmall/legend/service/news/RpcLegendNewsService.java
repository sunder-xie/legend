package com.tqmall.legend.service.news;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.base.PageEntityDTO;
import com.tqmall.legend.object.result.news.LegendNewsDTO;
import com.tqmall.legend.object.result.news.NewTypeDTO;

import java.util.List;

/**
 * Created by feilong.li on 17/4/11.
 */
public interface RpcLegendNewsService {

    /**
     * 获取所有资讯的分类
     * @return
     */
    public Result<List<NewTypeDTO>> getAllNewsType();


    /**
     * 获取资讯列表
     * @param newsTypeId 分类id
     * @return
     */
    public Result<PageEntityDTO<LegendNewsDTO>> getNewsList(Long newsTypeId);

    /**
     * 获取资讯详情
     * @param id
     * @return
     */
    public Result<LegendNewsDTO> getNewsDetail(Long id);


    /**
     * 添加用户阅读资讯记录
     * @param id
     * @return
     */
    public Result updateNewsAppPv(Long id);

    /**
     * 同步资讯浏览量
     * @return
     */
    public Result<Boolean> syncPv();

    /**
     * 更新资讯点赞次数
     * @param id
     * @param type 0减去点赞次数(取消点赞) 1增加点赞次数
     * @return 更新后点赞数量
     */
    public Result<Long> uptNewsPraise(Long id, Integer type);

}
