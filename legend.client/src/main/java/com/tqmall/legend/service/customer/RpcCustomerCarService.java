package com.tqmall.legend.service.customer;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.customer.CustomerCarParam;
import com.tqmall.legend.object.param.customer.ReceiveCouponParam;
import com.tqmall.legend.object.result.customer.ApiBaseCarVoDTO;
import com.tqmall.legend.object.result.customer.ApiCarVoDTO;
import com.tqmall.legend.object.result.customer.ApiCustomerCarVoDTO;
import com.tqmall.legend.object.result.customer.ApiCustomerSearchVoDTO;
import com.tqmall.legend.object.result.customer.CustomerCarBoDTO;
import com.tqmall.legend.object.result.customer.CustomerCarDTO;
import com.tqmall.legend.object.result.customer.CustomerRecordDTO;
import com.tqmall.legend.object.result.customer.ReceiveCouponDTO;

import java.util.List;

/**
 * Created by zsy on 16/1/5.
 */
public interface RpcCustomerCarService {
    /**
     * 根据传递的客户信息创建客户提供给legendM预约创建用
     *
     * @param param
     * @return
     */
    public Result<Long> addCustomerCar(CustomerCarParam param);

    /**
     * 创建或更新客户车辆 mace创建客户用
     * @param param
     * @return
     */
    Result<CustomerCarDTO> addOrUpdateCustomerCar(CustomerCarParam param);

    /**
     * 获取客户预约单列表
     * @param shopId 店铺id
     * @param customerCarId  客户车辆id
     * @param page
     * @param size
     * @return
     */
    public com.tqmall.zenith.errorcode.support.Result<CustomerRecordDTO> getCustomerAppointRecord(Long shopId, Long customerCarId, Integer page, Integer size) ;

    /**
     * 获取客户预检单(车况)列表
     * @param shopId  店铺id
     * @param customerCarId 客户车辆id
     * @param page
     * @param size
     * @Since app3.0
     */
    public com.tqmall.zenith.errorcode.support.Result<CustomerRecordDTO> getCustomerPrechecksRecord(Long shopId, Long customerCarId, Integer page, Integer size);

    /**
     * 根据车牌号获取车辆信息
     * @param carLicense 车牌号
     * @param shopId 门店id
     * @return
     */
    public Result<CustomerCarDTO> getCustomerCarByCarLicense(String carLicense, Long shopId);

    /**
     * 领取优惠券
     * @return
     */
    public Result<ReceiveCouponDTO> receiveCoupon(ReceiveCouponParam param);

    /**
     * 根据手机号获取车牌列表
     * @param userGlobalId
     * @param mobile
     * @return
     */
    public Result<List<String>> getCarLicenseByMobileAndUserGlobalId(Long userGlobalId, String mobile);

    /**
     * 获取车辆信息 by mace
     * @param shopId
     * @param license
     * @return
     */
    public Result<ApiBaseCarVoDTO> getCustomerCar(Long shopId,String license);

    /**
     * 检索客户信息 by mace
     * @param shopId
     * @param key
     * @param licenseNot
     * @return
     */
    public Result<List<ApiCustomerSearchVoDTO>> customerSearch(Long shopId,String key,String licenseNot,Integer page,Integer size);

    /**
     * 查询客户历史信息列表 by mace
     * @param shopId
     * @param keyWord
     * @return
     */
    public Result<List<CustomerCarBoDTO>> showCustomerHistoryList(Long shopId,String keyWord,Integer page,Integer size);

    /**
     * 更新车辆信息 by mace
     * @param shopId
     * @param apiCarVoDTO
     * @return
     */
    public Result updateCustomerCar(Long shopId,ApiCarVoDTO apiCarVoDTO);

    /**
     * 获取车辆相关信息 by mace
     * @param shopId
     * @param carId
     * @return
     */
    public Result<ApiCustomerCarVoDTO> getCarInfo(Long shopId,Long carId);

    /**
     *  app获取预检单的编号
     * @param shopId
     * @param userId
     * @param serialType
     * @return
     */
    public Result<String> getSerialNumber(Long shopId,Long userId,String serialType);

}
