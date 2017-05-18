package com.tqmall.legend.biz.customer;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.customer.bo.CustomerFeedbackBO;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2015-01-14 11:20:20
 */
public interface CustomerFeedbackService {

    /**
     * 添加客户回访信息
     * @param customerFeedback
     * @return
     */
    public Result add(CustomerFeedback customerFeedback);

    /**
     * 保存回访单
     * @param customerFeedback
     * @return
     */
    Long save(CustomerFeedback customerFeedback);

    /**
     * 更新客户回访信息
     * @param customerFeedback
     * @return
     */
    public Result update(CustomerFeedback customerFeedback);

    /**
     * 根据ID获取记录
     * @param id
     * @return
     */
    public CustomerFeedback selectById(Long id);

    /**
     * 条件查询列表
     * @param params
     * @return
     */
    public List<CustomerFeedback> select(Map<String, Object> params);

    /**
     * 查询记录数量
     * @param params
     * @return
     */
    public Integer selectCount(Map<String, Object> params);

    /**
     * 添加/更新回访信息
     * @param customerFeedback
     * @return
     */
    public Result feedback(CustomerFeedback customerFeedback);

    /**
     * 根据订单ID和门店ID获取记录
     * @param orderId
     * @param shopId
     * @return
     */
    public CustomerFeedback selectByOrderIdAndShopId(Long orderId, Long shopId);

    public List<CustomerFeedback> selectLastFeedback(Long shopId, Date nextVisitTimeGt, Date nextVisitTimeLt);

    /**
     * 保存回访记录并更新提醒信息为已处理
     */
    public Result addAndUpdateNoteInfo(CustomerFeedback customerFeedback,Long noteInfoId,UserInfo userInfo);

    /**
     * 获取回访记录
     */
    public Page<CustomerFeedback> getPage(Pageable pageable, Map<String, Object> searchParams);

    /**
     * 查询客户回访总数
     * @param shopId
     * @param visitTimeGt
     * @param visitTimeLt
     * @param visitMethod
     * @return
     */
    int countCustomerFeedback(Long shopId, String visitTimeGt, String visitTimeLt, String visitMethod);

    List<CustomerFeedbackBO> getCustomerFeedbackList(Long shopId, String visitTimeGt, String visitTimeLt, String visitMethod, int page, int pageSize);

}
