package com.tqmall.legend.dao.shop;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ShopServiceInfoDao extends BaseDao<ShopServiceInfo> {

	/**
	 * 查询所有的服务
	 *
	 * @param ids
	 * @return
	 */
	public List<ShopServiceInfo> selectAllByIds(Long[] ids);

	/**
	 * 查询所有的服务
	 *
	 * @param id
	 * @param shopId
	 * @return
	 */
	public ShopServiceInfo selectByIdAndShopId(@Param("id") Long id, @Param("shopId") Long shopId);

	/**
	 * 获取特定活动下的有效服务
	 *
	 * @param activityId 活动ID
	 * @return List<ShopServiceInfo>
	 */
	List<ShopServiceInfo> queryActivityServiceList(Long activityId);

	/**
	 * 查询全部状态的服务列表
	 *
	 * @param map
	 * @return
	 */
	public List<ShopServiceInfo> selectAllStatus(Map map);

	/**
	 * 批量查询服务
	 * @param shopId
	 * @param ids
     * @return
     */
	List<ShopServiceInfo> selectByIdss(@Param("shopId") Long shopId,
									  @Param("ids") Collection<Long> ids);

	/**
	 * 可查询全部状态的服务列表
	 *
	 * @param param
	 * @return
	 */
	Integer selectCountAllStatus(Map<String, Object> param);

	List<ShopServiceInfo> selectByCatIds(@Param("catIds") Collection<Long> catIds);

    List<ShopServiceInfo> findServiceInfoByNames(@Param("shopId")Long shopId, @Param("names")String... names);
}
