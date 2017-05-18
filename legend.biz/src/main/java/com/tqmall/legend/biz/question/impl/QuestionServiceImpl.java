package com.tqmall.legend.biz.question.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.question.QuestionService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.question.QuestionDao;
import com.tqmall.legend.entity.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by macx on 15/10/28.
 */
@Service
public class QuestionServiceImpl extends BaseServiceImpl implements QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Override
    public Result getPage(Map<String, Object> params, Pageable pageable) {
        int count = questionDao.getIWantQuestionCount(params);
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
        if(pageable.getSort() != null){
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            params.put("sorts", sorts);
        }
        params.put("limit", pageRequest.getPageSize());
        params.put("offset", pageRequest.getOffset());
        List<Question> questions = questionDao.getIWantQuestions(params);
        DefaultPage<Question> page = new DefaultPage<>(questions, pageRequest,count);
        return Result.wrapSuccessfulResult(page);
    }

    @Override
    public Integer questionCount(Map<String, Object> params) {
        return questionDao.selectCount(params);
    }

    @Override
    public List<Question> questionQuery(Map<String, Object> params) {
        return questionDao.select(params);
    }

    @Override
    public int questionUpdate(Question question) {
        return questionDao.updateById(question);
    }

    @Override
    public Question getQuestionById(Question question) {
        return questionDao.selectById(question);
    }
}

