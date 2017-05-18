package com.tqmall.legend.facade.tech;

import com.google.common.base.Optional;
import com.tqmall.legend.client.service.dto.PageDto;
import com.tqmall.legend.client.service.dto.TopicBookCatalogueDto;
import com.tqmall.legend.client.service.dto.TopicCatDto;
import com.tqmall.legend.client.service.dto.TopicDto;
import com.tqmall.legend.client.service.dto.TopicPageDto;
import com.tqmall.legend.client.service.dto.TopicParam;
import com.tqmall.legend.entity.topic.TopicTypeEnum;

import java.util.List;

/**
 * 资料中心-主题 interface
 */
public interface ITopicFacade {


    /**
     * 获取主题分类
     *
     * @param topicTypeEnum 主题类型
     * @return List<TopicCatDto>
     */
    List<TopicCatDto> getTopicCategory(TopicTypeEnum topicTypeEnum);


    /**
     * 获取推荐主题
     *
     * @param topicTypeEnum 主题类型
     * @param shopId        门店ID
     * @return List<TopicDto>
     */
    List<TopicDto> getRecommendTopic(TopicTypeEnum topicTypeEnum, Long shopId);

    /**
     * 分页获取主题
     *
     * @param pageableRequest
     * @param searchParams
     * @return PageDto<TopicDto>
     */
    PageDto<TopicDto> getTopicPage(PageDto pageableRequest, TopicParam searchParams);

    /**
     * 获取主题的目录
     *
     * @param id 主题ID
     * @return List<TopicBookCatalogueDto>
     */
    List<TopicBookCatalogueDto> selectTopicBookCatalogue(Long id);

    /**
     * 分页获取主题的资料页
     *
     * @param pageableRequest
     * @param searchParams
     * @return PageDto<TopicPageDto>
     */
    PageDto<TopicPageDto> getTopicBookPage(PageDto pageableRequest, TopicParam searchParams);

    /**
     * 更新主题
     *
     * @param topic
     */
    void update(TopicDto topic);

    /**
     * 获取主题
     *
     * @param id 主题ID
     * @return Optional<TopicDto>
     */
    Optional<TopicDto> selectTopic(Long id);
}
