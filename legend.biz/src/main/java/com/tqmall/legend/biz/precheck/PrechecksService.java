package com.tqmall.legend.biz.precheck;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.api.entity.ApiCarVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.precheck.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by guangxue on 14/10/28.
 */
public interface PrechecksService {

     List<Prechecks> getAllCarPreCheckHeads(Map<String, Object> params);

     Prechecks selectById(Long id);

     PrecheckVO select(Map<String, Object> searchMap);


    /**
     * 新增预检单
     * @param prechecks
     * @param precheckDetailsList
     * @param precheckRequestList
     * @return
     * @throws Exception
     */
     Boolean addNewPrecheckOrder(Prechecks prechecks, List<PrecheckDetails> precheckDetailsList, List<PrecheckRequest> precheckRequestList, UserInfo userInfo) throws Exception;

    /**
     * @param precheckHead
     * @param precheckDetailsList
     * @param precheckRequestList
     * @return
     */

    Boolean updatePrecheckOrder(Prechecks precheckHead, List<PrecheckDetails> precheckDetailsList, List<PrecheckRequest> precheckRequestList) throws Exception;

    /**
     * @param params
     * @return
     */
     List<PrecheckDetails> getListedPrecheckDetails(Map<String, Object> params);

     List<PrecheckRequest> getListedPrecheckRequests(Map<String, Object> params);

     Page<PrecheckDetailsVO> getPrecheckDetailsList(Pageable pageable, Map<String, Object> params);

    /**
     * params map:
     * shopId 店铺Id
     * start  提醒开始日期
     * days   提醒的天数
     *
     * @return
     */
     List<PrecheckDetailsVO> getPrecheckRemindList(Long shopId, String start, Integer days);

     List<PrecheckValue> getItemValuesByType(Long valueType);

     Map<Long, Map<String, String>> getAllPrecheckValues();

     Map<Long, Map<String, String>> getAllPrecheckItems();

     Long batchInsertDetails(List<PrecheckDetails> precheckDetailsList) throws Exception;

     List<Prechecks> getAPrecheck(Map<String, Object> params);


     Boolean deletePrechecks(Long precheckId,UserInfo userInfo);

    /**
     * APP客户车辆信息
     * @param customerCar
     * @return
     */
     void toAppCustomerCarUpdate(Long shopId,ApiCarVo customerCar);

     List<PrecheckDetailsVO> toAppGetPrecheckDetailsList(Map<String, Object> params);

    /**
     * 查询预检车辆数,以车为维度
     * @param params
     * @return
     */
    public Integer countPrecheckCar(Map<String, Object> params);
}
