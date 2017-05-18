package com.tqmall.legend.biz.question.impl;

import com.tqmall.legend.biz.question.AnswerService;
import com.tqmall.legend.dao.question.AnswerDao;
import com.tqmall.legend.entity.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by macx on 16/1/19.
 */
@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerDao answerDao;

    @Override
    public List<Answer> queryAnswers(Map<String, Object> params) {
        return answerDao.select(params);
    }
}
