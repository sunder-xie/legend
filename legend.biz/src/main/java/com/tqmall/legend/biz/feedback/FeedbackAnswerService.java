package com.tqmall.legend.biz.feedback;


import com.tqmall.legend.entity.feedback.FeedbackAnswer;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/08/04.
 */
public interface FeedbackAnswerService {

    /**
     * 获取回答、追问信息
     * @return
     */
    List<FeedbackAnswer> select(Map<String, Object> searchParam);

    /**
     * 根据id查询反馈问题
     * @param id
     * @return
     */
    FeedbackAnswer selectById(Long id);

    /**
     * 新增回复信息
     * @param feedbackAnswer
     * @return
     */
    int insertFeedBackAnswer(FeedbackAnswer feedbackAnswer);


}
