package com.tqmall.legend.service.service;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.service.*;
import com.tqmall.legend.object.result.order.WorkerDTO;
import com.tqmall.legend.object.result.service.*;
import com.tqmall.legend.object.result.shop.ShopPageDTO;

import java.util.List;

/**
 * Created by zsy on 15/12/15.
 */
public interface RpcShopServiceInfoService {
    /**
     * 根据服务id获取城市站
     *
     * @param param  注：需要传来源和服务id
     * @return
     */
    public Result<ShopServiceInfoDTO> getRegionById(ShopServiceInfoParam param);

    /**
     * 根据serviceId、cityId、status获取门店
     * cityId默认：383
     * status默认：0
     * limit默认：10
     * offset默认：0
     * page默认：1
     *
     * @param param
     * @return
     */
    public Result<ShopPageDTO> getShopPage(ShopServiceInfoParam param);

    /**
     * 分页查询门店服务实例列表
     * @param param
     * @return
     */
    public Result<ShopServiceInfoPageDTO> getServiceInfoPage(ShopServiceInfoParam param);

    /**
     * 分页查询门店服务模版列表
     * @param param
     * @return
     */
    public Result<ServiceTemplatePageDTO> getServiceTemplatePage(ServiceTemplateParam param);

    /**
     * 初始化钣喷服务
     * @param shopId 门店id
     * @param userId 后台用户id
     * @return
     */
    Result<Boolean> initBpService(Long shopId,Long userId);

    /**
     * 查询在车主端(门店微信公众号)展示的分好类的服务列表
     *
     * @param userGlobalId
     * @return
     */
    Result<List<AppServiceGroupDTO>> getAppServiceGroup(Long userGlobalId);

    /**
     * 服务类目统计
     * @return
     */
    Result<List<ServiceStatisDTO>> statisServiceUsage();

    /**
     * 某服务类目下门店服务情况统计
     * @return
     * @param catId
     * @param shopName
     */
    Result<ServiceCatStatisDTO> statisServiceUsageByCat(Long catId, String shopName, Integer pageIndex, Integer pageSize);

    /**
     * 某门店的服务情况统计
     * @param ucShopId
     * @param catId
     * @return
     */
    Result<ServiceShopStatisDTO> statisServiceUsageByShop(Long ucShopId, Long catId, Integer pageIndex, Integer pageSize);

    /**
     * 获取更多洗车服务
     *
     * @return
     */
    Result<List<ShopServiceInfoDTO>> gerMoreCarWashService(Long shopId);

    /**
     * 添加门店自定义服务
     *
     * @return
     */
    Result<ShopServiceInfoDTO> insertCustomShopServiceInfo(ServiceInfoParam serviceInfoParam);


    /**
     * 添加门店服务（服务带配件）
     * @param serviceGoodsParam
     * @return
     */
    Result<Boolean> insertShopServiceInfo(ServiceGoodsParam serviceGoodsParam);

    /**
     * 获取门店热门服务
     * 调用cube获取热门服务数据（最多20项）
     * @param shopId
     * @return
     */
    Result<List<ShopServiceInfoDTO>> getHotService(Long shopId);

    /**
     * 获取标准洗车服务
     * @param shopId
     * @return
     */
    Result<ShopWashCarServiceDTO> getBZCarWashService(Long shopId);

    /**
     * 搜索获取门店服务
     * @return
     */
    Result<List<ShopServiceInfoExtDTO>> getShopServiceFromSearch(ServiceSearchParam serviceSearchParam);

    /**
     * 获取服务套餐
     * @param shopId
     * @param serviceParam
     * @return
     */
    Result<ServicePackageDTO> getPackageByServiceId(Long shopId, String serviceParam);


}
