package com.tqmall.legend.web.tech.topic;

import com.google.common.base.Optional;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.client.service.dto.PageDto;
import com.tqmall.legend.client.service.dto.TopicDto;
import com.tqmall.legend.client.service.dto.TopicPageDto;
import com.tqmall.legend.client.service.dto.TopicParam;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.topic.TopicStatusEnum;
import com.tqmall.legend.entity.topic.TopicTypeEnum;
import com.tqmall.legend.facade.tech.ITopicFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 技术中心——技术主题 controller
 */
@Slf4j
@Controller
@RequestMapping("shop/tech/topic")
public class TopicController extends BaseController {

    @Autowired
    ITopicFacade topicFacade;

    /**
     * 技术主题 页面
     *
     * @param model
     * @param type  {1:维修案例,2:基础资料}
     * @return
     */
    @RequestMapping
    public String index(Model model,
                        @RequestParam(value = "type", required = false) Integer type) {

        // 资料类型
        type = (type == null) ? TopicTypeEnum.REPAIRCASE.getCode() : type;
        String topicTypeEnumStr = TopicTypeEnum.getMesByCode(type);
        if (topicTypeEnumStr == null) {
            return "redirect:/shop/tech/topic";
        }
        TopicTypeEnum typeEnum = TopicTypeEnum.getTopicTypeEnum(type);

        // 获取主题分类
        model.addAttribute("topicCatList", topicFacade.getTopicCategory(typeEnum));
        // 查询推荐专题
        model.addAttribute("topicList", topicFacade.getRecommendTopic(typeEnum, UserUtils.getShopIdForSession(request)));
        model.addAttribute("topicTypeEnums", TopicTypeEnum.getMessages());
        model.addAttribute("moduleUrl", "tech");
        model.addAttribute("techTab", "topic");
        model.addAttribute("defaultType", type);

        return "tech/topic/topic_list";
    }

    /**
     * 主题列表数据
     *
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object bookList(@PageableDefault(page = 1, value = 9, sort = "gmt_modified", direction = Sort.Direction.DESC) Pageable pageable,
                           HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        PageDto<TopicDto> page = null;

        // 页参数(页码\每页条数\排序)
        PageDto pageableRequest = new PageDto();
        pageableRequest.setPageNum(pageable.getPageNumber());
        pageableRequest.setPageSize(pageable.getPageSize());
        Set<String> sortSet = new HashSet<String>(1);
        sortSet.add("gmt_modified desc");
        pageableRequest.setSort(sortSet);

        // 请求参数实体
        TopicParam topicParam = new TopicParam();
        // 发布状态
        topicParam.setStatus(TopicStatusEnum.PUBLISH.getCode());
        topicParam.setShopId(UserUtils.getShopIdForSession(request));
        // 1:维修案例，2:基础资料
        if (searchParams.containsKey("type")) {
            topicParam.setType(Integer.parseInt(searchParams.get("type") + ""));
        }
        // 分类查询
        if (searchParams.containsKey("catId")) {
            topicParam.setCatId(Long.parseLong(searchParams.get("catId") + ""));
        }
        page = topicFacade.getTopicPage(pageableRequest, topicParam);
        long totalNumLong = Long.parseLong(page.getTotalNum() + "");
        DefaultPage defaultPage = new DefaultPage<TopicDto>(page.getRecordList(), pageable, totalNumLong);

        return Result.wrapSuccessfulResult(defaultPage);
    }


    /**
     * 主题详情页
     *
     * @param model
     * @param id    主题ID
     * @return
     */
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public String info(Model model,
                       @RequestParam(value = "id", required = true) Long id) {
        model.addAttribute("moduleUrl", "tech");
        Optional<TopicDto> topicDtoOptional = topicFacade.selectTopic(id);
        if (!topicDtoOptional.isPresent()) {
            return "redirect:/shop/tech/topic";
        }
        TopicDto topic = topicDtoOptional.get();
        Integer status = topic.getStatus();
        if (status != null && !status.equals(TopicStatusEnum.PUBLISH.getCode())) {
            return "redirect:/shop/tech/topic";
        }
        model.addAttribute("topic", topic);
        if (StringUtils.isBlank(topic.getContent())) {
            model.addAttribute("topicBookCatalogueList", topicFacade.selectTopicBookCatalogue(id));
            // 书籍预览
            return "tech/topic/topic_book_info";
        }

        // 视频预览
        return "tech/topic/topic_repair_info";
    }

    /**
     * 主题书籍每页
     *
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping(value = "page_list", method = RequestMethod.GET)
    @ResponseBody
    public Object topicBookPageList(@PageableDefault(page = 1, value = 1, sort = "page", direction = Sort.Direction.ASC) Pageable pageable,
                                    HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        PageDto<TopicPageDto> page = null;

        // 页参数(页码\每页条数\排序)
        PageDto pageableRequest = new PageDto();
        pageableRequest.setPageNum(pageable.getPageNumber());
        pageableRequest.setPageSize(pageable.getPageSize());
        Set<String> sortSet = new HashSet<String>(1);
        sortSet.add("page asc");
        pageableRequest.setSort(sortSet);

        // 请求参数(门店ID\主题ID)
        TopicParam topicParam = new TopicParam();
        topicParam.setShopId(UserUtils.getShopIdForSession(request));
        // 主题ID
        if (searchParams.containsKey("topicId")) {
            topicParam.setTopicId(Long.parseLong(searchParams.get("topicId") + ""));
        }
        page = topicFacade.getTopicBookPage(pageableRequest, topicParam);
        long totalNumLong = Long.parseLong(page.getTotalNum() + "");
        DefaultPage defaultPage = new DefaultPage<TopicPageDto>(page.getRecordList(), pageable, totalNumLong);

        return Result.wrapSuccessfulResult(defaultPage);
    }

    /**
     * 喜欢
     *
     * @param id 主题ID
     * @return
     */
    @RequestMapping(value = "doLike", method = RequestMethod.GET)
    @ResponseBody
    public Result doLike(@RequestParam(value = "id", required = true) Long id) {

        Optional<TopicDto> topicDtoOptional = topicFacade.selectTopic(id);
        if (!topicDtoOptional.isPresent()) {
            return Result.wrapErrorResult("", "操作失败,主题不存在");
        }
        TopicDto topic = topicDtoOptional.get();
        long likeNum = topic.getLikeNum() + 1;
        topic.setLikeNum(likeNum);
        try {
            topicFacade.update(topic);
            return Result.wrapSuccessfulResult(likeNum);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.wrapErrorResult("", "系统繁忙");
        }
    }

    /**
     * 不喜欢
     *
     * @param id 主题ID
     * @return
     */
    @RequestMapping(value = "doUnlike", method = RequestMethod.GET)
    @ResponseBody
    public Result doUnlike(@RequestParam(value = "id", required = true) Long id) {
        Optional<TopicDto> topicDtoOptional = topicFacade.selectTopic(id);
        if (!topicDtoOptional.isPresent()) {
            return Result.wrapErrorResult("", "操作失败,主题不存在");
        }
        TopicDto topic = topicDtoOptional.get();
        long unlikeNum = topic.getUnlikeNum() + 1;
        topic.setUnlikeNum(unlikeNum);
        try {
            topicFacade.update(topic);
            return Result.wrapSuccessfulResult(unlikeNum);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.wrapErrorResult("", "系统繁忙");
        }
    }


    /**
     * 全预览
     *
     * @param model
     * @param id    主题ID
     * @param page
     * @return
     */
    @RequestMapping(value = "preview_full", method = RequestMethod.GET)
    public String previewFull(Model model,
                              @RequestParam(value = "id", required = true) Long id,
                              @RequestParam(value = "page", defaultValue = "1") Integer page) {
        model.addAttribute("moduleUrl", "tech");
        Optional<TopicDto> topicDtoOptional = topicFacade.selectTopic(id);
        if (!topicDtoOptional.isPresent()) {
            return "redirect:";
        }
        TopicDto topic = topicDtoOptional.get();
        Integer status = topic.getStatus();
        if (status != null && status.equals(TopicStatusEnum.COMPLETEDRAFT.getCode())) {
            return "redirect:";
        }

        model.addAttribute("page", page);
        model.addAttribute("topic", topic);

        return "tech/topic/topic_preview_full";

    }
}
