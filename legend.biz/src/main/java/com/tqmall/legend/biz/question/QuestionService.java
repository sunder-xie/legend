package com.tqmall.legend.biz.question;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by macx on 15/10/28.
 */
public interface QuestionService {
    /**
     * 分页搜索问题
     *
     * @param params
     * @param pageable
     * @return
     */
    public Result<Page<Question>> getPage(Map<String, Object> params, Pageable pageable);

    /**
     * 问题数量
     * @param params
     * @return
     */
    public Integer questionCount(Map<String,Object> params);

    /**
     * 问题列表查询
     * @param params
     * @return
     */
    public List<Question> questionQuery(Map<String,Object> params);

    /**
     * 问题更新
     * @param question
     */
    public int questionUpdate(Question question);

    public Question getQuestionById(Question question);
}
