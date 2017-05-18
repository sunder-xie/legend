package com.tqmall.legend.facade.tech.impl;

import com.google.common.base.Optional;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.client.service.TopicBookCatalogueServiceRPC;
import com.tqmall.legend.client.service.TopicBookPageServiceRPC;
import com.tqmall.legend.client.service.TopicCatServiceRPC;
import com.tqmall.legend.client.service.TopicServiceRPC;
import com.tqmall.legend.client.service.dto.PageDto;
import com.tqmall.legend.client.service.dto.TopicBookCatalogueDto;
import com.tqmall.legend.client.service.dto.TopicCatDto;
import com.tqmall.legend.client.service.dto.TopicDto;
import com.tqmall.legend.client.service.dto.TopicPageDto;
import com.tqmall.legend.client.service.dto.TopicParam;
import com.tqmall.legend.entity.topic.TopicStatusEnum;
import com.tqmall.legend.entity.topic.TopicTypeEnum;
import com.tqmall.legend.facade.tech.ITopicFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 资料中心-主题 interface
 */
@Slf4j
@Service
public class TopicFacadeImpl implements ITopicFacade {

    @Resource
    TopicServiceRPC topicServiceRPC;
    @Resource
    TopicBookCatalogueServiceRPC topicBookCatalogueServiceRPC;
    @Resource
    TopicBookPageServiceRPC topicBookPageServiceRPC;
    @Resource
    TopicCatServiceRPC topicCatServiceRPC;


    @Override
    public List<TopicCatDto> getTopicCategory(TopicTypeEnum topicTypeEnum) {
        //请求实体
        TopicParam topicParam = new TopicParam();
        topicParam.setType(topicTypeEnum.getCode());
        topicParam.setStatus(TopicStatusEnum.PUBLISH.getCode());
        log.info("[velly平台]dubbo获取主题分类,入参:{}", ObjectUtils.objectToJSON(topicParam));
        Result<List<TopicCatDto>> result = null;
        try {
            result = topicCatServiceRPC.select(topicParam);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取主题分类 异常:", e);
            return new ArrayList<TopicCatDto>();
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取主题分类 失败:", result.getMessage());
            return new ArrayList<TopicCatDto>();
        }

        return result.getData();
    }

    @Override
    public List<TopicDto> getRecommendTopic(TopicTypeEnum topicTypeEnum, Long shopId) {

        //请求实体
        TopicParam topicParam = new TopicParam();
        topicParam.setType(topicTypeEnum.getCode());
        topicParam.setStatus(TopicStatusEnum.PUBLISH.getCode());
        topicParam.setRecommend(1);
        log.info("[velly平台]dubbo获取主题列表,入参:{}", ObjectUtils.objectToJSON(topicParam));
        Result<List<TopicDto>> result = null;
        try {
            result = topicServiceRPC.getTopicList(topicParam);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取主题列表 异常:", e);
            return new ArrayList<TopicDto>();
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取主题列表 失败,失败原因:{}", result.getMessage());
            return new ArrayList<TopicDto>();
        }

        return result.getData();

    }

    @Override
    public PageDto<TopicDto> getTopicPage(PageDto pageableRequest, TopicParam searchParams) {

        log.info("[velly平台]dubbo获取主题列表,入参:{}", ObjectUtils.objectToJSON(searchParams));
        Result<PageDto<TopicDto>> result = null;
        try {
            result = topicServiceRPC.getPage(pageableRequest, searchParams);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取主题列表 异常:", e);
            PageDto<TopicDto> pageDto = new PageDto<TopicDto>();
            pageDto.setRecordList(new ArrayList<TopicDto>());
            return pageDto;
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取主题列表 失败,失败原因:{}", result.getMessage());
            PageDto<TopicDto> pageDto = new PageDto<TopicDto>();
            pageDto.setRecordList(new ArrayList<TopicDto>());
            return pageDto;
        }

        return result.getData();
    }

    @Override
    public List<TopicBookCatalogueDto> selectTopicBookCatalogue(@NotNull Long id) {
        log.info("[velly平台]dubbo获取主题的目录,主题ID:{}", id);
        Result<List<TopicBookCatalogueDto>> result = null;
        try {
            result = topicBookCatalogueServiceRPC.selectSecondList(id);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取主题的目录 异常:", e);
            return new ArrayList<TopicBookCatalogueDto>();
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取主题的目录 失败,失败原因:{}", result.getMessage());
            return new ArrayList<TopicBookCatalogueDto>();
        }

        return result.getData();
    }

    @Override
    public PageDto<TopicPageDto> getTopicBookPage(PageDto pageableRequest, TopicParam searchParams) {

        log.info("[velly平台]dubbo获取主题的页列表,入参:{}", ObjectUtils.objectToJSON(searchParams));
        Result<PageDto<TopicPageDto>> result = null;
        try {
            result = topicBookPageServiceRPC.getPage(pageableRequest, searchParams);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取主题的页列表 异常:", e);
            PageDto<TopicPageDto> pageDto = new PageDto<TopicPageDto>();
            pageDto.setRecordList(new ArrayList<TopicPageDto>());
            return pageDto;
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取主题的页列表 失败,失败原因:{}", result.getMessage());
            PageDto<TopicPageDto> pageDto = new PageDto<TopicPageDto>();
            pageDto.setRecordList(new ArrayList<TopicPageDto>());
            return pageDto;
        }

        return result.getData();
    }

    @Override
    public void update(TopicDto topic) {
        topicServiceRPC.update(topic);
    }

    @Override
    public Optional<TopicDto> selectTopic(@NotNull Long id) {
        log.info("[velly平台]dubbo获取单个主题,主题ID:{}", id);
        Result<TopicDto> result = null;
        try {
            result = topicServiceRPC.selectById(id);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取单个主题 异常:", e);
            return Optional.absent();
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取单个主题 失败,失败原因:{}", result.getMessage());
            return Optional.absent();
        }

        return Optional.fromNullable(result.getData());
    }
}
