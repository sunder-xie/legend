package com.tqmall.legend.facade.service;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.bo.ServiceStatisBo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.magic.vo.ProxyServicesVo;
import com.tqmall.legend.facade.service.vo.AppServiceVo;
import com.tqmall.legend.facade.service.vo.SaveShopServiceInfoVo;
import com.tqmall.legend.facade.service.vo.ServiceTemplateVo;
import com.tqmall.legend.facade.service.vo.ShopServiceInfoVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.shopservice.param.LegendShopServiceParam;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by zsy on 16/5/18.
 */
public interface ShopServiceInfoFacade {
    /**
     * 获取同类别服务list
     *
     * @param serviceId
     * @param shopId
     * @return
     */
    public Result<List<ShopServiceInfo>> getSameServiceInfo(Long serviceId, Long shopId);

    /**
     * 根据服务模版保存门店服务实体<br>
     *     目前只能编辑价格
     * @param saveShopServiceInfoVo
     * @param userInfo
     * @return
     */
    public ShopServiceInfo save(SaveShopServiceInfoVo saveShopServiceInfoVo, UserInfo userInfo);

    /**
     * 根据委托服务获取钣喷中心对应的服务
     * @param proxyServicesVo
     * @return
     */
    OrderServices getOrderServicesByProxyServices(Long shopId, ProxyServicesVo proxyServicesVo);

    /**
     * 初始化钣喷服务
     */
    void initBpService(Long shopId,Long userId);

    /**
     * 服务搜素接口
     *
     * @param serviceSn
     * @param serviceName
     * @param type
     * @param suiteNumLT
     * @param categoryId
     * @param shopId
     * @return
     */
    String getShopService(String serviceSn, String serviceName, String type, String suiteNumLT, Long categoryId,Long shopId);

    /**
     * 根据cateTag获取门店服务列表
     * @return
     */
    List<ShopServiceInfo> getShopServiceInfoByCateTag(Long shopId, Integer cateTag, String nameLike);

    /**
     * 按一级类别查询车主服务
     *
     * @param shopId
     * @param parentAppCateId 一级类别id
     * @param isRecommend     是否置顶的服务
     * @return
     */
    List<AppServiceVo> getPublishedAppService(Long shopId, Long parentAppCateId, Integer isRecommend);

    /**
     * 批量处理服务的套餐价格
     *
     * @param shopServiceInfoList
     */
    void setSuitServicePrice(List<ShopServiceInfo> shopServiceInfoList);

    /**
     * 按一级类别查询待发布的车主服务
     *
     * @param shopId
     * @param parentAppCateId
     * @param isRecommend
     * @param limit
     * @param offset
     * @return
     */
    Page<AppServiceVo> getPrepublishAppServicePage(Long shopId, Long parentAppCateId, Integer isRecommend, Integer limit, Long offset);

    /**
     * 取消将服务发布到车主端
     *
     * @param userInfo
     * @param serviceId
     * @return
     */
    boolean cancelPublishAppService(UserInfo userInfo, Long serviceId);

    /**
     * 保存车主服务列表并调ddl-wechat接口获取查看地址
     *
     * @param appServiceVoList
     * @param userInfo
     * @return
     */
    String saveAppServiceListAndGetViewUrl(List<AppServiceVo> appServiceVoList, UserInfo userInfo);

    /**
     * 获取需要统计的服务列表,填充如下字段
     * Long shopId;
     * Long topCatId;
     * Long catId;
     * Long serviceId;
     * @return
     * @param shopId
     * @param catId
     * if (shopId == null && catId == null) 服务类目统计
     * if (shopId != null && catId == null) 某门店的服务情况统计
     * if (shopId == null && catId != null) 某服务类目下门店服务情况统计
     *
     */
    List<ServiceStatisBo> getServiceStatisBase(Long shopId, Long catId);

    List<ServiceStatisBo> attachPointUsages(List<ServiceStatisBo> serviceStatisBoList);

    List<ServiceStatisBo> attachOrderUsages(List<ServiceStatisBo> serviceStatisBoList);

    List<ServiceStatisBo> attachOrderConfrimUsages(List<ServiceStatisBo> serviceStatisBoList);

    /**
     * 更新门店标准服务
     * @param shopServiceInfoList
     * @param userInfo
     */
    void updateNormalService(List<ShopServiceInfo> shopServiceInfoList, UserInfo userInfo);

    /**
     * 校验重复，如名称、编号等
     * @param shopId
     * @param key
     * @param value
     * @return
     */
    boolean checkWithShopId(Long shopId, String key, String value);

    List<ShopServiceInfo> selectServiceByIds(List<Long> ids);

    /**
     * 根据ID和shopId删除服务资料(包括套餐信息ServiceGoodsSuite)
     *
     * @param id
     * @return
     */
    void deleteByIdAndShopId(long id, long shopId);

    void deleteShopServiceCate(long id, long shopId);
}
