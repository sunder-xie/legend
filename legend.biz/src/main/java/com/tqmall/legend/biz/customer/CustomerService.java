package com.tqmall.legend.biz.customer;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerC;
import com.tqmall.legend.entity.customer.CustomerCar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by litan on 14-11-3.
 */
public interface CustomerService {

    /**
     * 添加客户信息
     * @param customer
     * @return
     */
    public Integer add(Customer customer);

    /**
     * 添加客户信息
     * @param customer
     * @return
     */
    public Integer update(Customer customer);


    /**
     * 根据车辆信息查询最近的4个联系人
     * @param params
     * @returnar
     */
    public List<Customer> getCustomerByCarId(Map<String,Object> params);
    /**
     * 搜索客户列表
     * @param pageable
     * @param searchParams
     * @return
     */
    Page<CustomerCar> getPageCancelCustomer(Pageable pageable,Map<String,Object> searchParams);

    public List<Customer> getAllCustomers(Long shopId);

    public Customer selectById(Long id);

    List<CustomerCar> getCustomerCarList(Map<String, Object> params);

    List<CustomerCar> searchCarList(Map<String, Object> params);

    public List<Customer> selectByIds(Long[] ids);

    public List<Customer> select(Map<String,Object> searchParams);

    public Integer selectCount(Map<String,Object> searchParams);

    /**
     * 根据用户名或者手机模糊搜索用户信息
     * @param searchParams
     * @return 返回用户信息列表
     */
    public List<Customer> getCustomerByNameMobile(Map<String,Object> searchParams);

    public List<Customer> getCustomerByMobile(String mobile , Long shopId);

    /**
     * 批量插入客户
     * @param customerListItem 导入的数据
     * @param shopId 门店ID
     * @param uuid 导入的批次号
     * @return
     */
    public List<Customer> initCustomer(List<Customer> customerListItem,Long shopId,String uuid);

    /**
     * 初始化导入客户和车辆
     * @param excelList
     * @param shopId
     * @param user
     * @param existSize 数据库已经存在的数据数量
     * @return
     */
    public Integer insertCustomerAndCar(List<List<Object>> excelList,Long shopId,UserInfo user,Integer existSize);

    /**
     * 根据shopId删除，重新初始化使用
     * @param shopId
     * @return
     */
    public Integer deleteByShopId(Long shopId);

    /**
     * 查询C 客户列表
     * @param searchParams
     * @return
     */
    public List<CustomerC> selectCustomerCList(Map<String,Object> searchParams);

    /**
     * get customer
     *
     * @param customerId 主键ID
     * @return Optional<Customer>
     */
    Optional<Customer> getCustomer(Long customerId);

    /**
     * 查询单位列表
     * @param searchMap
     * @return
     */
    public List<Customer> searchCompanyList(Map<String,Object> searchMap);


    /**
     * 获取客户信息
     *
     * @param customerCarId 客户车辆ID
     * @param shopId        门店ID
     * @return Optional<Customer>
     */
    Optional<Customer> getCustomer(Long customerCarId, Long shopId);

    /**
     * 删除客户
     * @param customerId
     * @return
     */
    Result deleteById(Long customerId);

    List<Customer> getCustomerByGroupMobile(Map param);

    /**
     * 根据手机号批量查询
     * @param mobiles
     * @param shopId
     * @return
     */
    List<Customer> getCustomerByMobiles(Set<String> mobiles, Long shopId);

    Multimap<String, Long> getPhoneCustomerIdMap(Long shopId);

    /**
     * 手机号是否存在 true:存在,false:不存在
     * @param shopId
     * @param mobile
     * @return
     */
    boolean isExistMobile(Long shopId, String mobile);

    /**
     * 将微信端账户添加到云修系统
     * @param shopId
     * @param mobile
     * @return
     */
    Customer addCustomerFromWeChat(Long shopId,String mobile);

    /**
     *
     * @param shopId
     * @param mobile
     * @return
     */
    List<String> getLicenseByMobile(Long shopId,String mobile);

    Customer selectById(Long customerId, Long shopId);

    List<Customer> listByIds(Long shopId, Collection<Long> customerIds);

    ImmutableMap<Long,Customer> getCustomerId2CustomerMap(Long shopId, Collection<Long> customerIds);

    void batchSave(List<Customer> customers);

    void insertCustomerAndAccountInfo(Customer customer);
}
