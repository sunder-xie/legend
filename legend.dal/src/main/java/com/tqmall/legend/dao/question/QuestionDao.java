package com.tqmall.legend.dao.question;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.question.Question;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface QuestionDao extends BaseDao<Question> {

    public int getIWantQuestionCount(Map<String,Object> param);

    public List<Question> getIWantQuestions(Map<String,Object> param);
}
