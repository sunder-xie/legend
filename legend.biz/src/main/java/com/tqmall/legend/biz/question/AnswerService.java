package com.tqmall.legend.biz.question;

import com.tqmall.legend.dao.question.AnswerDao;
import com.tqmall.legend.entity.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by macx on 16/1/19.
 */
public interface AnswerService {

    public List<Answer> queryAnswers(Map<String,Object> params);
}
