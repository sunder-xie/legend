package com.tqmall.legend.biz.feedback.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.feedback.FeedbackAnswerService;
import com.tqmall.legend.dao.feedback.FeedbackAnswerDao;
import com.tqmall.legend.entity.feedback.FeedbackAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/8/4.
 */
@Service
public class FeedbackAnswerServiceImpl extends BaseServiceImpl implements FeedbackAnswerService {
    @Autowired
    private FeedbackAnswerDao feedbackAnswerDao;

    @Override
    public List<FeedbackAnswer> select(Map<String, Object> searchParam) {
        List<FeedbackAnswer> feedbackAnswerList = feedbackAnswerDao.select(searchParam);
        return feedbackAnswerList;
    }

    @Override
    public FeedbackAnswer selectById(Long id) {
        FeedbackAnswer feedbackAnswer = feedbackAnswerDao.selectById(id);
        return feedbackAnswer;
    }

    @Override
    public int insertFeedBackAnswer(FeedbackAnswer feedbackAnswer) {
        return feedbackAnswerDao.insert(feedbackAnswer);
    }
}
