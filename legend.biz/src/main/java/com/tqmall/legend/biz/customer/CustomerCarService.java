package com.tqmall.legend.biz.customer;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.AppointAppVo;
import com.tqmall.legend.entity.customer.CarConcision;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarBo;
import com.tqmall.legend.entity.customer.CustomerCarByModel;
import com.tqmall.legend.entity.customer.CustomerCarComm;
import com.tqmall.legend.entity.insurance.InsuranceBill;
import com.tqmall.legend.entity.order.OrderInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by litan on 14-11-3.
 */
public interface CustomerCarService {

    /**
     * 添加客户车辆信息
     */
    public Result add(CustomerCar car, UserInfo userInfo);

    /**
     * @param userInfo
     * @param customerCar
     * @return
     * @throws BizException
     */
    CustomerCar addOrUpdate(UserInfo userInfo, CustomerCar customerCar) throws BizException;

    /**
     * 业务接口： 提供对外的无登陆信息的客户新增编辑接口
     *
     * @return
     * @throws BizException
     */
    Result addOrUpdate(AppointAppVo appoint) throws BizException;

    /**
     * 修改/删除客户车辆信息
     */
    public Result update(CustomerCar customerCar, UserInfo userInfo);

    /**
     * 修改/删除客户车辆信息
     */
    public Integer update(CustomerCar customerCar);

    /**
     * 新删除车辆接口逻辑
     * 1.删除车辆
     * 2.删除车辆客户关联关系表的相关信息
     */
    public void deleteCar(Long shopId, Long userId, Long carId) throws Exception;

    /**
     * 更换车主
     *
     * @return
     */
    public Result changeCustomer(String mobile, UserInfo userInfo , Long carId);

    /**
     * get customerCarInfo
     *
     * @param id 主键
     */
    Optional<CustomerCar> getCustomerCar(Long id);

    /**
     * 批量查询车辆信息
     */
    public List<CustomerCar> select(Map<String, Object> map);

    /**
     * 通过车牌号和门店ID更新
     */
    public Integer updateByLicenseAndShopId(CustomerCar customerCar);

    /**
     * 通过车牌号和门店ID获取一条记录
     */
    public CustomerCar selectByLicenseAndShopId(String license, Long shopId);

    /**
     * 查询记录数量
     */
    public Integer selectCount(Map<String, Object> params);


    /**
     * 批量插入车辆
     */
    public Integer initCustomerCar(List<CustomerCar> customerCarItem);


    /**
     * 通过customerIds 获得所有车型
     */
    public List<CustomerCar> selectByCustomerId(Map<String, Object> paramMap);

    /**
     * 根据id查询
     */
    public CustomerCar selectById(Long id);

    /**
     * 根据licenses,mobile查询
     */
    public List<Long> selectByLicensesAndMobile(List<String> licenseList, String mobile);

    /**
     * 查询客户历史信息
     */
    public List<CustomerCarBo> selectCustomerHistoryList(Map<String, Object> params);

    /**
     * 列表分页数据
     */
    public Page<CustomerCarBo> getPage(Pageable pageable, Map<String, Object> searchParams);

    /**
     * 常用车辆品牌
     */
    public List<CustomerCarComm> queryCustomerCar(Map<String, Object> paramMap);

    /**
     * 常用车型
     */
    public List<CustomerCarByModel> queryCustomerCarByModel(Map<String, Object> paramMap);

    /*
     * 填充数据库的CarLevelData的对外接口
     */
    public Result flushCarLevelToDB();

    /**
     * 车辆类型
     */
    public Result flushCarTypeToDB();

    /**
     * 根据车牌查找车辆信息,如车辆不存在,则新增
     */
    CustomerCar selectIfNoExistInsert(UserInfo userInfo, String carLicense, String ver, String refer) throws BizException;

    /**
     * 查询客户信息
     */
    Result selectCustomerCar(Long shopId, Long carId);

    /**
     * copy customer's property to order <p/> TODO 冗余字段
     *
     * @param userInfo   当前用户
     * @param order      工单信息
     * @param carLicense 车牌
     */
    void copyCustomerProperty(UserInfo userInfo, OrderInfo order, String carLicense);

    /**
     * copy customer's property to insurance bill <p/> TODO 冗余字段
     *
     * @param userInfo   当前用户
     * @param bill       服务单
     * @param carLicense 车牌
     */
    void copyCustomerProperty(UserInfo userInfo, InsuranceBill bill, String carLicense);

    /**
     * 客户预约次数+1
     *
     * @param carLicense 车牌号
     * @param shopId     门店ID
     * @return {1:'成功';0:'失败'}
     */
    int appointNumberAddOne(String carLicense, Long shopId);

    /**
     * 获取推荐服务
     *
     * @return
     */
    public Result getRecommendService(Long carId);


    Page<CustomerCar> getCustomerPageByParam(Pageable pageable, Map<String, Object> param);


    /**
     * 获取有正确手机号的客户数
     *
     * @param shopId
     * @return
     */
    Integer getCustomerHasMobileNum(Long shopId);

    /**
     * 获取门店全部的记录数,包括isDeleted = Y
     *
     * @param shopId
     * @return
     */
    Integer countByShopId(Long shopId);

    List<CustomerCar> listByCustomerId(Long shopId, Long customerId);

    /**
     * 根据车牌号获取车辆信息
     *
     * @param shopId  门店id
     * @param license 车牌号
     * @return
     */
    List<CustomerCar> findCustomerCarsByLicense(Long shopId, String... license);

    void batchSave(List<CustomerCar> customerCars);

    /**
     *
     * @param shopId
     * @param customerIds
     * @return key=customerId, value=licenses(if multi, separate by comma)
     */
    Map<Long, String> mapCustomerId2Licenses(Long shopId, List<Long> customerIds);

    void save(CustomerCar customerCar);

    /**
     * 根据ids查车辆
     * @param carIds
     * @return
     */
    List<CustomerCar> selectByIds(List<Long> carIds);


    List<CustomerCar> findCustomerCarByIds(Long shopId,List<Long> carIds);

    /**
     * 根据车牌号，判断车主是否车主（车主名和手机号都为空表示不存在）
     * @param shopId
     * @param carLicense
     * @return
     */
    boolean hasExistCustomer(Long shopId,String carLicense);


}
