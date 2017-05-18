package com.tqmall.legend.facade.appoint;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.AppointAppVo;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.customer.AppointVo;
import com.tqmall.legend.facade.appoint.vo.AppointDetailFacVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 16/4/8.
 */
public interface AppointFacade {
    /**
     * 获取预约单详细信息(包括关联的服务名称,物料信息),searchParams必须含有shopId,一次最多查询5K条数据
     * @param searchParams
     * @return
     */
    public List<Appoint> getAppointInfoList(Map<String, Object> searchParams);

    /**
     * 取消预约单
     * @param shopId
     * @param appointId
     * @param userId
     * @param cancelReason
     * @param status
     * @return
     */
    public Result<Appoint> cancelAppoint(Long shopId, Long appointId,Long userId, String cancelReason, Long status);

    /**
     * 取消预约单(外部使用)
     * @param appointAppVo
     * @return
     */
    public Result<Appoint> cancelAppointByOuter(AppointAppVo appointAppVo);

    /**
     * 门店web端新建预约单
     * @param appoint
     * @param appointServicesList
     * @return
     */
    public Result createAppointByShopWeb(Appoint appoint,List<AppointServiceVo> appointServicesList);


    Result updateAppointAndService(Appoint appoint,List<AppointServiceVo> appointServicesList,UserInfo userInfo);

    /**
     * 获取预约单大对象
     * @param appointId
     * @param shopId
     * @return
     */
    public AppointDetailFacVo getAppointDetail(Long appointId, Long shopId);

    /**
     * 确认预约单(门店web,商家app)
     * @param appointId
     * @param shopId
     * @param userId
     * @return
     */
    public Result<String> confirmAppoint(Long appointId, Long shopId, Long userId);

    /**
     * 单表查询,代码中拼接视图对象(VO)
     * @param pageable
     * @param searchParams
     * @return
     */
    public Page<AppointVo> getAppointVoPage(Pageable pageable, Map<String, Object> searchParams);

    /**
     * 单表查询,代码中拼接视图对象(VO)
     * @param searchParams
     * @return
     */
    public List<AppointVo> getAppointVoList(Map<String, Object> searchParams);

    /**
     * 校验预约单的预定金是否有效
     * @param appointId
     * @param downPayment
     * @return
     */
    boolean checkDownPaymentIsValid(Long appointId,BigDecimal downPayment);

    /**
     * 获取服务被预约的次数
     *
     * @param serviceIds
     * @return MAP key:serviceId,value:预约单数
     */
    public Map<Long, Integer> getAppointCount(Long... serviceIds);

    /**
     * 根据工单id、门店id获取预约单信息
     * @param orderId
     * @return
     */
    Appoint getAppointByOrderIdAndShopId(Long orderId,Long shopId);

    /**
     * 根据id查询预约单
     * @param id
     * @return
     */
    Appoint getAppointById(Long id);
}
