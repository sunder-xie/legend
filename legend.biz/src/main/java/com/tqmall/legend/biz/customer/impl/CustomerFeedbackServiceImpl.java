package com.tqmall.legend.biz.customer.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.CustomerFeedbackService;
import com.tqmall.legend.biz.customer.bo.CustomerFeedbackBO;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.NoteType;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.customer.CustomerCarDao;
import com.tqmall.legend.dao.customer.CustomerDao;
import com.tqmall.legend.dao.customer.CustomerFeedbackDao;
import com.tqmall.legend.dao.order.OrderInfoDao;
import com.tqmall.legend.dao.shop.NoteInfoDao;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerFeedback;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.shop.NoteInfo;
import com.tqmall.legend.log.ShopNoteInfoLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2015-01-14 11:03
 */
@Slf4j
@Service
public class CustomerFeedbackServiceImpl extends BaseServiceImpl implements CustomerFeedbackService {

    @Autowired
    CustomerFeedbackDao customerFeedbackDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    CustomerCarDao customerCarDao;

    @Autowired
    OrderInfoDao orderInfoDao;
    @Autowired
    NoteInfoDao noteInfoDao;
    @Autowired
    private OrderServicesService orderServicesService;

    @Override
    public Result addAndUpdateNoteInfo(CustomerFeedback customerFeedback,Long noteInfoId,UserInfo userInfo) {
        Result result = add(customerFeedback);
        log.info("客户回访记录保存，返回信息：{}，回访对象：{}，noteInfoId：{}", LogUtils.objectToString(result), LogUtils.objectToString(customerFeedback),noteInfoId);
        if(result.isSuccess()){
            if (noteInfoId != null && noteInfoId > 0) {
                NoteInfo noteInfo = new NoteInfo();
                noteInfo.setId(noteInfoId);
                noteInfo.setNoteFlag(1);
                noteInfo.setNoteWay(1);
                noteInfo.setOperator(userInfo.getName());
                noteInfo.setOperatorTime(new Date());
                int updateResult = noteInfoDao.updateById(noteInfo);
                if (updateResult == 1) {
                    // 打印日志
                    log.info(ShopNoteInfoLog.handleNoteLog(userInfo.getShopId()));
                }
            }
        }
        return result;
    }

    @Override
    public Page<CustomerFeedback> getPage(Pageable pageable, Map<String, Object> searchParams) {
        return super.getPage(customerFeedbackDao,pageable, searchParams);
    }

    /**
     * 查询客户回访总数
     *
     * @param shopId
     * @param visitTimeGt
     * @param visitTimeLt
     * @param visitMethod
     * @return
     */
    @Override
    public int countCustomerFeedback(final Long shopId, final String visitTimeGt, final String visitTimeLt, final String visitMethod) {
        return new BizTemplate<Integer>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                if (shopId == null || shopId <= 0) {
                    throw new IllegalArgumentException("shopId错误");
                }
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Integer process() throws BizException {
                Map<String, Object> searchParam = Maps.newHashMap();
                searchParam.put("shopId", shopId);
                searchParam.put("visitTimeGt", DateUtil.getStartTime(DateUtil.convertStringToDateYMD(visitTimeGt)));
                searchParam.put("visitTimeLt", DateUtil.getEndTime(DateUtil.convertStringToDateYMD(visitTimeLt)));
                searchParam.put("visitMethod", visitMethod);
                List<String> sorts = Lists.newArrayList("visit_time desc");
                searchParam.put("sorts", sorts);
                return customerFeedbackDao.selectCount(searchParam);
            }
        }.execute();
    }

    @Override
    public List<CustomerFeedbackBO> getCustomerFeedbackList(final Long shopId, final String visitTimeGt, final String visitTimeLt, final String visitMethod, final int page, final int pageSize) {
        return new BizTemplate<List<CustomerFeedbackBO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                if (shopId == null || shopId <= 0) {
                    throw new IllegalArgumentException("shopId错误");
                }
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<CustomerFeedbackBO> process() throws BizException {
                int offset = (page - 1) * pageSize;
                int limit = pageSize;
                Map<String, Object> searchParam = Maps.newHashMap();
                searchParam.put("shopId", shopId);
                searchParam.put("visitTimeGt", DateUtil.getStartTime(DateUtil.convertStringToDateYMD(visitTimeGt)));
                searchParam.put("visitTimeLt", DateUtil.getEndTime(DateUtil.convertStringToDateYMD(visitTimeLt)));
                searchParam.put("visitMethod", visitMethod);
                List<String> sorts = Lists.newArrayList("visit_time desc");
                searchParam.put("sorts", sorts);
                searchParam.put("offset", offset);
                searchParam.put("limit", limit);
                List<CustomerFeedback> customerFeedbackList = customerFeedbackDao.select(searchParam);
                if (CollectionUtils.isEmpty(customerFeedbackList)) {
                    return Collections.emptyList();
                }
                List<CustomerFeedbackBO> customerFeedbackBOList = Lists.newArrayListWithCapacity(customerFeedbackList.size());
                List<Long> orderIdList = Lists.newArrayList();
                for (CustomerFeedback customerFeedback : customerFeedbackList) {
                    CustomerFeedbackBO customerFeedbackBO = new CustomerFeedbackBO();
                    BeanUtils.copyProperties(customerFeedback, customerFeedbackBO);
                    customerFeedbackBOList.add(customerFeedbackBO);

                    Integer noteType = customerFeedback.getNoteType();
                    if (noteType != null && noteType.equals(NoteType.VISIT_NOTE_TYPE)) {
                        orderIdList.add(customerFeedback.getOrderId());
                    }
                }
                Map<Long, String> serviceNamesMap = orderServicesService.getServiceNamesByOrderIds(shopId, orderIdList);
                if (!CollectionUtils.isEmpty(serviceNamesMap)) {
                    for (CustomerFeedbackBO customerFeedbackBO : customerFeedbackBOList) {
                        Integer noteType = customerFeedbackBO.getNoteType();
                        if (noteType != null && noteType.equals(NoteType.VISIT_NOTE_TYPE)) {
                            String serviceNames = serviceNamesMap.get(customerFeedbackBO.getOrderId());
                            if (StringUtil.isNotStringEmpty(serviceNames)) {
                                customerFeedbackBO.setOrderServiceNames(serviceNames);
                            }
                        }
                    }
                }
                return customerFeedbackBOList;
            }
        }.execute();
    }


    @Override
    public Result add(CustomerFeedback customerFeedback) {
        int flag = customerFeedbackDao.insert(customerFeedback);
        if (flag > 0) {
            return Result.wrapSuccessfulResult("回访保存成功");
        } else {
            return Result.wrapErrorResult("-1", "添加失败");
        }
    }

    /**
     * 保存回访单
     *
     * @param customerFeedback
     * @return
     */
    @Override
    public Long save(CustomerFeedback customerFeedback) {
        customerFeedbackDao.insert(customerFeedback);
        return customerFeedback.getId();
    }

    @Override
    public Result update(CustomerFeedback customerFeedback) {
        customerFeedbackDao.updateById(customerFeedback);
        return Result.wrapSuccessfulResult(customerFeedback.getId());
    }

    @Override
    public CustomerFeedback selectById(Long id) {
        return customerFeedbackDao.selectById(id);
    }

    @Override
    public List<CustomerFeedback> select(Map<String, Object> params) {
        return customerFeedbackDao.select(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {
        return customerFeedbackDao.selectCount(params);
    }

    @Override
    public List<CustomerFeedback> selectLastFeedback(Long shopId, Date nextVisitTimeGt, Date nextVisitTimeLt) {
        return customerFeedbackDao.selectLastFeedback(shopId, nextVisitTimeGt, nextVisitTimeLt);
    }

    @Transactional
    @Override
    public Result feedback(CustomerFeedback customerFeedback) {
        Long shopId = customerFeedback.getShopId();
        Long orderId = customerFeedback.getOrderId();

        CustomerFeedback customerFeedbackOld = customerFeedbackDao.selectByOrderIdAndShopId(orderId, shopId);

        //判断客户回访信息是否已经存在
        if (customerFeedbackOld != null) {
            // 设置回访相关字段并更新数据
            CustomerFeedback customerFeedbackUpt = new CustomerFeedback();
            customerFeedbackUpt.setId(customerFeedbackOld.getId());
            customerFeedbackUpt.setReceptionStar(customerFeedback.getReceptionStar());
            customerFeedbackUpt.setRepairStar(customerFeedback.getRepairStar());
            customerFeedbackUpt.setSendcarStar(customerFeedback.getSendcarStar());
            customerFeedbackUpt.setTotalStar(customerFeedback.getTotalStar());
            customerFeedbackUpt.setCustomerFeedback(customerFeedback.getCustomerFeedback());
            customerFeedbackUpt.setVisitorId(customerFeedback.getVisitorId());
            customerFeedbackUpt.setVisitorName(customerFeedback.getVisitorName());
            customerFeedbackUpt.setVisitMethod(customerFeedback.getVisitMethod());
            customerFeedbackUpt.setVisitTime(customerFeedback.getVisitTime());
            customerFeedbackUpt.setRefer(customerFeedback.getRefer());
            customerFeedbackUpt.setVer(customerFeedback.getVer());

            Integer result = customerFeedbackDao.updateById(customerFeedbackUpt);
            if (result > 0) {
                return Result.wrapSuccessfulResult(customerFeedbackOld);
            } else {
                return Result.wrapErrorResult("-1", "更新数据失败");
            }
        } else {
            //工单信息
            OrderInfo orderInfo = orderInfoDao.selectById(orderId);

            //获取工单信息 + 回访信息 并插入数据

            Long customerCarId = orderInfo.getCustomerCarId();
            CustomerCar customerCar = customerCarDao.selectById(customerCarId);
            Long customerId = customerCar.getCustomerId();
            Customer customer = customerDao.selectById(customerId);

            customerFeedback.setCustomerId(customerId);
            customerFeedback.setCustomerCarId(customerCarId);
            customerFeedback.setCarLicense(customerCar.getLicense());
            customerFeedback.setCustomerName(customer.getCustomerName());
            customerFeedback.setCarBrandId(customerCar.getCarBrandId());
            customerFeedback.setCarBrandName(customerCar.getCarBrand());
            customerFeedback.setCarSeriesId(customerCar.getCarSeriesId());
            customerFeedback.setCarSeriesName(customerCar.getCarSeries());
            customerFeedback.setCarAlias(customerCar.getByName());
            customerFeedback.setMobile(customer.getMobile());
            customerFeedback.setFinishTime(orderInfo.getFinishTime());

            Integer result = customerFeedbackDao.insert(customerFeedback);
            if (result > 0) {
                return Result.wrapSuccessfulResult(customerFeedback);
            } else {
                return Result.wrapErrorResult("-1", "插入数据失败");
            }
        }
    }

    @Override
    public CustomerFeedback selectByOrderIdAndShopId(Long orderId, Long shopId) {
        if (orderId == null || shopId == null) {
            return null;
        }
        return customerFeedbackDao.selectByOrderIdAndShopId(orderId, shopId);
    }

}