package com.tqmall.legend.service.appoint;

import com.tqmall.legend.object.param.appoint.AppAppointParam;
import com.tqmall.legend.object.param.appoint.AppointSearchParams;
import com.tqmall.legend.object.result.appoint.AppointResultDTO;
import com.tqmall.legend.object.result.appoint.AppointServiceDTO;
import com.tqmall.zenith.errorcode.support.Result;

/**
 * Created by zsy on 16/3/27.
 */
public interface RpcAppAppointService {
    /**
     * 根据活动id校验活动是否有效
     *
     * @param source
     * @param actTplId
     * @return
     */
    public Result<Boolean> check(String source,Integer actTplId);

    /**
     * 根据模板服务id获取参加报名的门店
     *
     * @param appointSearchParams
     * @return
     */
    public Result<AppointServiceDTO> getServiceList(AppointSearchParams appointSearchParams);

    /**
     * app下预约单接口
     *
     * @param appAppointParam
     * @return
     */
    public Result<AppointResultDTO> insertAppAppoint(AppAppointParam appAppointParam);

    /**
     *
     * @param appointSn 预约单编号
     * @return 如果原来数据就是支付成功的不需要更新直接返回success
     */
    public com.tqmall.core.common.entity.Result<Boolean> updatePayStatus(String appointSn , Long shopId);
}
