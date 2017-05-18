package com.tqmall.legend.dao.customer;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.customer.CarConcision;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarBo;
import com.tqmall.legend.entity.customer.CustomerCarByModel;
import com.tqmall.legend.entity.customer.CustomerCarComm;
import com.tqmall.legend.entity.customer.CustomerCarVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@MyBatisRepository
public interface CustomerCarDao extends BaseDao<CustomerCar> {

	public Integer getCustomerCarCount(Map<String, Object> parameters);

	public List<CustomerCar> getCustomerCarList(Map<String, Object> parameters);

	public CustomerCar getCustomerCarInfo(Map<String, Object> parameters);

	Integer deleteInfo(@Param("shopId") Long shopId, @Param("userId") Long userId, @Param("id") Long id);

	public Integer updateByLicenseAndShopId(CustomerCar customerCar);

	public CustomerCar selectByLicenseAndShopId(@Param("license") String license, @Param("shopId") Long shopId);

	public List<CustomerCar> getChainCarList(Map<String, Object> parameters);

	public List<CustomerCar> getChainCarByNameLicense(Map<String, Object> parameters);

	public List<CustomerCarVo> selectOwnCars(Map<String,Object> paramMap);

	public List<CustomerCar> selectByCustomerId(Map<String,Object> paramMap);

    /**
     * 常用车辆品牌
     */
	public List<CustomerCarComm> queryCustomerCar(Map<String,Object> paramMap);

    /**
     * 常用车型
     */
    public List<CustomerCarByModel> queryCustomerCarByModel(Map<String,Object> paramMap);


	public String selectOpenIdByLicense(String license);

	/**
	 * 查询客户历史信息
	 * @param params
	 * @return
	 */
	public List<CustomerCarBo> selectCustomerHistoryList(Map<String,Object> params);
	

   public List<CustomerCar> select(Map<String,Object> params);
   
   public Integer selectCount();

	//TODO 修复车辆级别使用,上线后删除

	/**
	 * 获取需要更新车辆级别的车辆信息
	 * @return
	 */
	List<CustomerCar> getCarLevelExistsCars();

	/**
	 * 更新车辆级别信息
	 * @param paramMap
	 */
	void updateCarLevelById(Map<String,Object> paramMap);
    /**
	 * 更新车辆类型信息
	 * @param paramMap
	 */
	void updateCarTypeById(Map<String,Object> paramMap);

	/**
	 * 查询门店所有车辆的简要信息
	 * @param shopId
	 * @param offset
	 *@param limit @return
     */
	List<CarConcision> selectAllCarConcisions(@Param("shopId") Long shopId,
											  @Param("offset") Integer offset,
											  @Param("limit") Integer limit);

	/**
	 * 获取能发送短信的客户车辆数
	 * @param shopId
	 * @return
     */
	Integer getCustomerHasMobileNum(@Param("shopId")Long shopId);

	/**
	 * 获取门店全部的记录数,包括isDeleted = Y
	 * @param shopId
	 * @return
	 */
	Integer countByShopId(@Param("shopId")Long shopId);

	List<CustomerCar> selectByIdss(@Param("shopId")Long shopId, @Param("ids")Set<Long> ids);

	List<CustomerCar> selectByIdss(@Param("shopId")Long shopId, @Param("ids")List<Long> ids);

    /**
     * 根据车牌号获取车辆信息
     * @param shopId 门店id
     * @param license 车牌号
     * @return
     */
    List<CustomerCar> findCustomerCarsByLicense(@Param("shopId")Long shopId,@Param("licenses")String... licenses);

	List<CustomerCar> listByCustomerIds(@Param("shopId") Long shopId, @Param("customerIds") List<Long> customerIds);
}
