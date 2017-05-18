package com.tqmall.legend.biz.customer;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.AppointAppVo;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2015-01-14 10:58
 */
public interface AppointService {

    /**
     * 添加客户预约信息
     *
     * @param appoint
     * @return
     */
    @Deprecated
    public Result addAppointAndService(Appoint appoint, List<AppointServiceVo> list);

    /**
     * 预约单消息推送
     * @param type
     * @param appoint
     */
    void pushMsg(String type, Appoint appoint);

    /**
     * 添加来自APP客户预约信息
     *
     * @param appoint
     * @return
     */
    @Deprecated
    public Result addAppointApp(AppointAppVo appoint);

    /**
     * update客户预约信息
     *
     * @param appoint
     * @return
     */
    public Result updateAppointAndService(Appoint appoint, List<AppointServiceVo> list, UserInfo userInfo);

    /**
     * 更新客户预约信息
     *
     * @param appoint
     * @return
     */
    @Deprecated
    public Result update(Appoint appoint);


    /**
     * 根据ID获取记录
     *
     * @param id
     * @return
     */
    public Appoint selectById(Long id);

    /**
     * 条件查询列表
     *
     * @param params
     * @return
     */
    public List<Appoint> select(Map<String, Object> params);


    /**
     * 查询记录数量
     *
     * @param params
     * @return
     */
    public Integer selectCount(Map<String, Object> params);

    /**
     * 根据ID软删除
     *
     * @param id
     * @return
     */
    public Integer deleteById(Long id);


    /**
     * 列表分页数据
     *
     * @param pageable
     * @param searchParams
     * @return
     */
    public Page<Appoint> getPage(Pageable pageable, Map<String, Object> searchParams);


    /**
     * 柯昌强 2015-12-28
     * 新增来自橙牛预约单和预约单服务
     */
    public Result addAppoint(AppointAppVo appoint);

    /**
     * 推送消息至cube
     *
     * @param type
     * @param appoint
     * @return
     */
    public Result pushMsgToCube(String type, Appoint appoint);

    /**
     * app端 客户档案 车辆预约信息
     *
     * @param searchParams
     * @return
     */
    public List<Appoint> getAppointRecordApp(Map<String, Object> searchParams);

    /**
     * 把预约单置为取消状态
     * @param shopId
     * @param appointId
     * @param userId
     * @param cancelReason
     * @param status
     * @return
     */
    public Result<Appoint> cancelAppoint(Long shopId,Long appointId,Long userId, String cancelReason, Long status);

    /**
     * 通用的新建预约单方法(事务控制) appointServicesList(对应的shopServiceInfo.suiteNum需为0或1)<br/>
     * 包括如下内容:
     * <ul>
     *     <li>入参非空检查</li>
     *     <li>新增预约单appoint</li>
     *     <li>预约单关联预约服务</li>
     *     <li>新增预约服务appointService</li>
     *     <li>更新customerCar预约信息</li>
     * </ul>
     * @param appoint
     * @param appointServicesList(对应的shopServiceInfo.suiteNum需为0或1)
     * @return
     */
    public Result<Appoint> insertAppointAndService(Appoint appoint, List<AppointServiceVo> appointServicesList);

    /**
     * 创建预约单(不需要支付时)或者预约单支付成功时的消息处理
     * @param appoint
     */
    void sendAppointMsg(Appoint appoint);

    /**
     * 重载com.tqmall.legend.biz.customer.AppointService#insertAppointAndService(com.tqmall.legend.entity.customer.Appoint, java.util.List)
     * @param serviceIds
     * @param appoint
     * @return
     */
    public Result<Appoint> insertAppointAndService(List<Long> serviceIds,Appoint appoint);

    public Integer updateById(Appoint appoint);

    /**
     * 判断预约单号是否存在
     * @param shopId
     * @param appointSn
     * @return
     */
    public boolean isExistAppointSn(Long shopId,String appointSn);

    /**
     * 逻辑删除预约单(只有已作废的预约单才能删除)
     * @param shopId
     * @param appointId
     * @param userId 操作人Id
     * @return
     */
    public Result deleteAppoint(Long shopId, Long appointId,Long userId);

    /**
     * 作废预约单(只有已取消的预约单才能作废)
     * @param shopId
     * @param appointId
     * @param userId 操作人Id
     * @return
     */
    public Result invalidAppoint(Long shopId, Long appointId,Long userId);

    /**
     * 操作预约单后发送短信给SA(门店接待人员)
     *
     * @param type
     */
    void sendMsgToSA(Long shopId, String appointSn, String appointMobile, String cancelReason, String type);

    /**
     * 外部操作预约单后发送短信给SA(门店接待人员)
     * @param type
     */
     void sendMsgToSA(AppointAppVo appointAppVo, String type);
}
