package com.tqmall.legend.biz.feedback;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.feedback.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by lilige on 16/7/18.
 */
public interface FeedbackService {

    /**
     * 添加反馈信息
     * @param feedback
     * @return
     */
    Result saveFeedback(Feedback feedback);

    /**
     * 获取反馈信息
     * @return
     */
    List<Feedback> select(Map<String,Object> searchParam);

    /**
     * 分页
     * @param pageable
     * @param searchParam
     * @return
     */
    Page<Feedback> getFeedbackPage(Pageable pageable , Map<String, Object> searchParam);

    /**
     * 根据id和shopId获取门店反馈信息
     *
     * @param id
     * @param shopId
     * @return
     */
    Feedback getFeedbackByIdAndShopId(Long id,Long shopId);

    /**
     * 获取最新的反馈信息
     * @param shopId
     * @return
     */
    Feedback getNewFeedback(Long shopId);
}
