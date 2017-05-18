package com.tqmall.legend.biz.feedback.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.feedback.FeedbackAnswerService;
import com.tqmall.legend.biz.feedback.FeedbackService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.feedback.FeedbackDao;
import com.tqmall.legend.entity.feedback.Feedback;
import com.tqmall.legend.entity.feedback.FeedbackAnswer;
import com.tqmall.legend.enums.feedback.AnswerTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lilige on 16/7/18.
 */
@Service
public class FeedbackServiceImpl extends BaseServiceImpl implements FeedbackService{
    @Autowired
    private FeedbackDao feedbackDao;
    @Autowired
    private FeedbackAnswerService feedbackAnswerService;

    /**
     * 添加反馈信息
     *
     * @param feedback
     * @return
     */
    @Override
    public Result saveFeedback(Feedback feedback) {
        feedbackDao.insert(feedback);
        return Result.wrapSuccessfulResult(feedback);
    }

    /**
     * 获取反馈信息
     *
     * @param searchParam
     * @return
     */
    @Override
    public List<Feedback> select(Map<String, Object> searchParam) {
        List<Feedback> feedbackList = feedbackDao.select(searchParam);
        return feedbackList;
    }

    /**
     * 问题反馈分页
     * @param pageable
     * @param searchParam
     * @return
     */
    @Override
    public Page<Feedback> getFeedbackPage(Pageable pageable, Map<String, Object> searchParam) {
        Page page = super.getPage(feedbackDao,pageable,searchParam);
        List<Feedback> feedbackList = page.getContent();
        if(CollectionUtils.isNotEmpty(feedbackList)){
            //不为空，则查询最新的一条回复信息(以创建时间倒序查询，保证是最新的一条回复)
            Map<String,Object> searchMap = Maps.newHashMap();
            List<Long> feedbackIds = Lists.newArrayList();
            for(Feedback feedback : feedbackList){
                Long id = feedback.getId();
                feedbackIds.add(id);
            }
            searchMap.put("feedbackIds",feedbackIds);
            searchMap.put("answerType", AnswerTypeEnum.ANSWER.getCode());
            searchMap.put("sorts",new String[]{"gmt_create desc"});
            List<FeedbackAnswer> feedbackAnswerList = feedbackAnswerService.select(searchMap);
            Map<Long,FeedbackAnswer> feedbackAnswerMap = Maps.newHashMap();
            for(FeedbackAnswer feedbackAnswer : feedbackAnswerList){
                Long feedbackId = feedbackAnswer.getFeedbackId();
                if(!feedbackAnswerMap.containsKey(feedbackId)){
                    feedbackAnswerMap.put(feedbackId,feedbackAnswer);
                }
            }
            for(Feedback feedback : feedbackList){
                Long id = feedback.getId();
                if(feedbackAnswerMap.containsKey(id)){
                    FeedbackAnswer feedbackAnswer = feedbackAnswerMap.get(id);
                    feedback.setNewFeedbackAnswer(feedbackAnswer);
                }
            }
        }
        return page;
    }

    /**
     * 根据id和shopId获取门店反馈信息
     *
     * @param id
     * @param shopId
     * @return
     */
    @Override
    public Feedback getFeedbackByIdAndShopId(Long id, Long shopId) {
        Feedback feedback = selectByIdAndShopId(feedbackDao, id, shopId);
        return feedback;
    }

    /**
     * 获取最新的反馈信息
     * @param shopId
     * @return
     */
    @Override
    public Feedback getNewFeedback(Long shopId) {
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId",shopId);
        searchMap.put("sorts", new String[] { "gmt_create desc" });
        searchMap.put("offset",0);
        searchMap.put("limit",1);
        List<Feedback> feedbackList = feedbackDao.select(searchMap);
        if(CollectionUtils.isNotEmpty(feedbackList)){
            Feedback feedback = feedbackList.get(0);
            //查询对应的追问信息
            Map<String,Object> searchAnswerMap = Maps.newHashMap();
            searchAnswerMap.put("feedbackId", feedback.getId());
            searchAnswerMap.put("answerType", AnswerTypeEnum.ANSWER.getCode());
            searchAnswerMap.put("sorts", new String[] { "gmt_create desc" });
            searchAnswerMap.put("offset",0);
            searchAnswerMap.put("limit",1);
            List<FeedbackAnswer> feedbackAnswerList = feedbackAnswerService.select(searchAnswerMap);
            if(CollectionUtils.isNotEmpty(feedbackAnswerList)){
                feedback.setNewFeedbackAnswer(feedbackAnswerList.get(0));
            }
            return feedback;
        }
        return null;
    }
}
