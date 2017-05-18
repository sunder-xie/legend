package com.tqmall.legend.facade.customer;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.facade.customer.vo.CarDetailVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.customercar.param.LegendCustomerCarRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by lilige on 16/4/10.
 */
public interface CustomerCarFacade {

    /**
     * 提供给客户详情页的 车辆详情接口
     * @param carId
     * @param shopId
     * @return
     */
    public CarDetailVo getCarInfo(Long carId,Long shopId);

    /**
     * 根据车牌获取门店车辆信息
     *
     * @param license
     * @param shopId
     * @return
     */
    CustomerCar getCustomerCarByLicense(String license, Long shopId);

    /**
     * 获取简单的vo对象，更新客户等业务场景只需要 customer 和customercar
     * @param carId
     * @param shopId
     * @return
     */
    public CarDetailVo getSimpleCar(Long carId,Long shopId);

    /**
     * 获取简单的vo对象集合 vo只有customer 和customercar
     * @param ids
     * @param shopId
     * @return
     */
    public List<CarDetailVo> getSimpleCarByIds(List<Long> ids , Long shopId);

    /**
     * 共享在中心委托单转工单时，若车辆不存在此门店，则新增车辆
     * @param shopId 登录门店id
     * @param customerCarId  委托方门店车辆id
     * @param contactName  委托方联系人
     * @param contactMobile  委托方联系电话
     * @param license
     * @param userInfo
     * @return
     */
    public CustomerCar getProxyCustomerCar(Long shopId,Long customerCarId, String license, String contactName, String contactMobile, UserInfo userInfo);

    /**
     * 通过搜索获取客户车辆
     * @param pageableRequest
     * @param customerCarRequest
     * @return
     */
    DefaultPage getCustomerCarsFromSearch(PageableRequest pageableRequest,LegendCustomerCarRequest customerCarRequest);

    /**
     * 接车首页 根据输入的关键字 匹配车主，车主电话，车牌
     * @param searchKey
     * @param shopId
     * @param pageableRequest
     * @return
     */
    List<Map<String,Object>> getMapsFromSearch(String searchKey, String shopId, PageableRequest pageableRequest);

    /**
     * 校验vin码是否存在
     *
     * @param vin
     * @param customerCarId
     * @return
     */
    Boolean checkVinIsExist(Long shopId, String vin, Long customerCarId);

}
