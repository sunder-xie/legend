package com.tqmall.legend.biz.shop;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.activity.ShopActivityServiceListVO;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.entity.shop.ShopServiceTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zwb on 14/10/29.
 */
public interface ShopServiceInfoService {
	/**
	 * 获取列表
	 *
	 * @param searchMap
	 * @return
	 */
	public List<ShopServiceInfo> select(Map<String, Object> searchMap);
	/**
	 * 获取带所有状态的服务列表
	 *
	 * @param searchMap
	 * @return
	 */
	public List<ShopServiceInfo> selectAllStatus(Map<String, Object> searchMap);

	/**
	 * 添加记录
	 *
	 * @param shopServiceInfo
	 * @param userInfo
	 * @return
	 */
	public Result<ShopServiceInfo> add(ShopServiceInfo shopServiceInfo, UserInfo userInfo);

	/**
	 * 添加服务资料
	 * @param shopServiceInfo
	 * @param shopId
	 * @param userId
	 */
	Result<ShopServiceInfo> saveShopServiceInfo(ShopServiceInfo shopServiceInfo, Long shopId, Long userId);

	/**
	 * 通过门店Id和服务编号，判断是否存在
	 * @param shopId
	 * @param serviceSn
	 * @return
	 */
	boolean isExistByServiceSnAndShopId(@NotNull Long shopId,@NotNull String serviceSn);

	/**
	 * TODO 待重构：合并add方法。新增功能后，返回新增的实体对象。 添加记录 override
	 *
	 * @param shopServiceInfo
	 * @param userInfo
	 * @return
	 */
	public Result addInOrder(ShopServiceInfo shopServiceInfo, UserInfo userInfo);

	/**
	 * 更新记录，不更新null的字段
	 *
	 * @param shopServiceInfo
	 * @param userInfo
	 * @return
	 */
	public Result update(ShopServiceInfo shopServiceInfo, UserInfo userInfo);

	/**
	 * 获取店铺同一个app车主服务的大套餐数量
	 *
	 * @param searchMap
	 * @return
	 */
	public Integer selectCount(Map<String, Object> searchMap);

	/**
	 * @param servicesIds
	 * @return
	 */
	public List<ShopServiceInfo> selectByIds(Long[] servicesIds);

	/**
	 * @param servicesId
	 * @return
	 */
	public ShopServiceInfo selectById(Long servicesId);

	/**
	 * @param serviceId
	 * @param shopId
	 * @return
	 */
	public ShopServiceInfo selectById(Long serviceId, Long shopId);

	/**
	 * 查询门店服务集合
	 *
	 * @param shopId              门店ID
	 * @param shopServiceTypeEnum 服务类型{1:常规服务,2:其它费用服务}
	 * @param flags               管理费标签
	 * @return List&lt;ShopServiceInfo&gt;
	 */
	List<ShopServiceInfo> queryShopServiceList(Long shopId, ShopServiceTypeEnum shopServiceTypeEnum, String flags);

	/**
	 * create by zsy 2015-09-15
	 * 门店初始化，添加标准化服务
	 *
	 * @param shopServiceInfoList
	 * @return
	 */
	public Integer initNormalShopServiceInfo(List<ShopServiceInfo> shopServiceInfoList);

	public List<ShopServiceInfo> selectByIds(List<Long> servicesIds);

	/**
	 * 获得所有的预约单服务下架或者不产品都要
	 */
	public List<ShopServiceInfo> selectAllByIds(Collection<Long> servicesIds);

	/**
	 * 根据服务ID和shopId获取服务数据
	 */
	public ShopServiceInfo selectByIdAndShopId(Long id, Long shopId);

	/**
	 * 获得所有的预约单服务下架或者不产品都要
	 * 然后还要组装服务类别和价格
	 *
	 * @param servicesIds 服务IDs
	 */
	public List<ShopServiceInfo> wrapServiceCateAndSuitePrice(List<Long> servicesIds);


	/**
	 * 修改
	 *
	 * @param shopServiceInfo
	 * @return
	 */
	public Integer update(ShopServiceInfo shopServiceInfo);

	/**
	 * 获取标准洗车服务列表
	 *
	 * @param shopId
	 * @return
	 */
	public List<ShopServiceInfo> getBZCarWashList(Long shopId);

	/**
	 * 保持标准洗车服务列表
	 *
	 * @param shopServiceInfoList
	 * @return
	 */
	public Result saveWashCarServiceList(List<ShopServiceInfo> shopServiceInfoList, Long shopId);

	/**
	 * 通用搜索
	 *
	 * @param shopId
	 * @param serviceSn
	 * @param serviceName
	 * @param type
	 * @param suiteNumLT
	 * @param size
	 * @return
	 */
	public Result search(Long shopId, String serviceSn, String serviceName, String type, String suiteNumLT, Integer size);

	/**
	 * 获取套餐
	 *
	 * @param shopId
	 * @param serviceParam
	 * @return
	 */
	public Result getPackageByServiceId(Long shopId, String serviceParam);


	/**
	 * get shopService
	 *
	 * @param flag   标识
	 * @param shopId 门店ID
	 * @return
	 */
	List<ShopServiceInfo> getServiceByFlag(String flag, Long shopId);


	/**
	 * get ShopServiceInfo
	 *
	 * @param serviceId primary key
	 * @return Optional<ShopServiceInfo>
	 */
	Optional<ShopServiceInfo> get(Long serviceId);


	/**
	 * 获取门店活动服务列表
	 *
	 * @param shopId             门店ID
	 * @param activityTemplateId 活动模板ID
	 * @return ShopActivityServiceListVO
	 */
	ShopActivityServiceListVO getShopActivityServiceList(Long shopId, Long activityTemplateId) throws BusinessCheckedException;

	/**
	 * 获取门店活动下服务列表
	 *
	 * @param activityId 活动ID
	 * @return List<ShopServiceInfo>
	 */
	List<ShopServiceInfo> getActivityServiceList(Long activityId);

	/**
	 * 获取优惠券对应的服务
	 * 一个优惠券码:一条服务
	 *
	 * @param couponCode 优惠券码
	 * @param shopId     门店ID
	 * @return ShopActivityServiceListVO
	 */
	Optional<ShopServiceInfo> getCouponActivityService(String couponCode, Long shopId);

	/**
	 * 根据服务模板,获取服务列表
	 *
	 * @param templateId 服务模板ID
	 * @param shopId     门店ID
	 * @return List<ShopServiceInfo>
	 */
	List<ShopServiceInfo> getServicelist(Integer templateId, Long shopId);

	/**
	 * 批量添加服务
	 * @param shopServiceInfoList
	 * @return
	 */
	Integer batchInsert(List<ShopServiceInfo> shopServiceInfoList);
	/**
	 * 根据ids查服务
	 * @param shopId
	 * @param ids
     * @return
     */
	List<ShopServiceInfo> list(Long shopId, Collection<Long> ids);

	/**
	 * 查找失效服务的id集合
	 * @param shopId
	 * @param ids
     * @return
     */
	Set<Long> listInvalidIds(Long shopId, Collection<Long> ids);

	/**
	 * 根据服务模版id查服务实例,有多条的情况下只返回最先生成的一条
	 * @param shopId
	 * @param serviceTplId
	 * @param status 服务状态:com.tqmall.legend.enums.serviceInfo.ServiceInfoStatusEnum,传null则查全部
     * @return
     */
	ShopServiceInfo getByTplId(Long shopId,Long serviceTplId,Integer status) throws IllegalArgumentException;

	/**
	 * 可进行多状态(status)的计数查询
	 *
	 * @param qryServiceInfoParam
	 * @return
	 */
	int selectCountAllStatus(Map<String, Object> qryServiceInfoParam);

	/**
	 * 原始的update方法
	 *
	 * @param shopServiceInfo
	 * @param userInfo
	 * @return
	 */
	int updateById(ShopServiceInfo shopServiceInfo, UserInfo userInfo);

	List<ShopServiceInfo> selectByCatIds(Collection<Long> catIds);

	/***
	 * 批量设置套餐服务的套餐价
	 * @param shopServiceInfoList
     */
	void setServiceSuitAmount(Collection<ShopServiceInfo> shopServiceInfoList);

	/**
	 * 批量将套餐价设置到服务价格字段上
	 * @param shopServiceInfoList
     */
	public void setSuitAmount2ServicePrice(Collection<ShopServiceInfo> shopServiceInfoList);

    /**
     * 通过门店服务名，获取服务信息
     * @param shopId 门店id
     * @param names 服务名
     * @return
     */
    List<ShopServiceInfo> findServiceInfoByNames(Long shopId,String... names);

    int getServiceInfoCount(Long shopId);
}
