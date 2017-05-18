package com.tqmall.legend.biz.shop.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.shop.ServiceTemplateService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.dao.shop.ShopServiceCateDao;
import com.tqmall.legend.entity.pub.service.ServiceCateVo;
import com.tqmall.legend.entity.shop.ServiceTemplate;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2014-12-23 17:12
 */
@Slf4j
@Service
public class ShopServiceCateServiceImpl extends BaseServiceImpl implements ShopServiceCateService{

    @Autowired
    ShopServiceCateDao shopServiceCateDao;
    @Autowired
    ShopDao shopDao;
    @Autowired
    private CacheComponent<List<ShopServiceCate>> cacheComponent;
    @Autowired
    private ServiceTemplateService serviceTemplateService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;

    @Override
    public List<ShopServiceCate> select(Map<String, Object> searchMap) {
    	List<ShopServiceCate> list = cacheComponent.getCache(CacheKeyConstant.SERVICE_CATEGORY);
    	List<ShopServiceCate> result = new LinkedList<ShopServiceCate>();
    	if(searchMap != null){
    		if(searchMap.get("shopId") != null){
    			String shopId = searchMap.get("shopId").toString();
    			//模糊查询name
    			String name = new String();
    			if(searchMap.get("nameLike") != null){
    				name = searchMap.get("nameLike").toString();
    			}
                //cateType 0门店服务，1淘汽、车主服务，2，标准服务类别
    			for (ShopServiceCate shopServiceCate : list) {
                    int cateType = shopServiceCate.getCateType();
                    String cateName = shopServiceCate.getName();
					if(shopId.equals(shopServiceCate.getShopId().toString()) || 2 == cateType){
                        if(cateName.contains(name)){
                            String parentId = String.valueOf(searchMap.get("parentId"));//传过来的值
                            String pid = String.valueOf(shopServiceCate.getParentId());//cache中的值
                            if(StringUtils.equals(parentId,pid)){
                                result.add(shopServiceCate);
                            } else {
                                result.add(shopServiceCate);
                            }
                        }
					}
				}
    		}
    	}
        return result;
    }

    /**2C-APP接口
     * 获得车主服务一级类别信息
     * create by jason 2015-07-16
     */
    @Override
    public List<ServiceCateVo> selectFirstCate() {
        List<ServiceCateVo> resultList = new ArrayList<>();
        try {
            //从redis中获取数据
            List<ShopServiceCate> cacheList = cacheComponent.getCache(CacheKeyConstant.SERVICE_CATEGORY);
            if (!CollectionUtils.isEmpty(cacheList)) {
                for (ShopServiceCate shopServiceCate : cacheList) {
                    Integer cateType = shopServiceCate.getCateType();
                    Long parentId = shopServiceCate.getParentId();
                    //车主一级服务类别
                    if (cateType == 1 && parentId == 0l) {
                        ServiceCateVo serviceCateVo = new ServiceCateVo();
                        serviceCateVo.setId(shopServiceCate.getId());
                        serviceCateVo.setName(shopServiceCate.getName());
                        serviceCateVo.setCateSort(shopServiceCate.getCateSort());
                        resultList.add(serviceCateVo);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取车主一级服务类别异常:{}",e);
            //从redis中获取数据异常,直接从数据库中取
            resultList = shopServiceCateDao.selectFirstCate();
        }
        return resultList;
    }

    @Override
    public List<ShopServiceCate> selectNoCache(Map<String, Object> searchMap) {
        List<ShopServiceCate> shopServiceCateList = shopServiceCateDao.select(searchMap);
        return shopServiceCateList;
    }

    @Override
    public Result addWithoutRepeat(ShopServiceCate shopServiceCate) {
        Long shopId = shopServiceCate.getShopId();
        String name = shopServiceCate.getName();

        Map<String, Object> params = new HashMap<>();
        List<Long> shopIds = new ArrayList<>();
        shopIds.add(shopId);
        shopIds.add(0l);
        //类别类型，0门店服务，1淘汽、车主服务，2，标准服务类别
        List<Long> cateTypes = new ArrayList<>();
        cateTypes.add(0l);
        cateTypes.add(2l);
        params.put("shopIds", shopIds);
        params.put("cateTypes", cateTypes);
        params.put("isDeleted", "N");
        params.put("name", name);

        List<ShopServiceCate> shopServiceCateList = shopServiceCateDao.select(params);
        if (!CollectionUtils.isEmpty(shopServiceCateList)){
            ShopServiceCate serviceCate = shopServiceCateList.get(0);
            return Result.wrapSuccessfulResult(serviceCate);
        } else {
            shopServiceCate.setCateTag(7);//自定义的类别标其他类目
            shopServiceCateDao.insert(shopServiceCate);
            cacheComponent.reload(CacheKeyConstant.SERVICE_CATEGORY);
            return Result.wrapSuccessfulResult(shopServiceCate);
        }
    }

    public ShopServiceCate selectById(Long id){
        return shopServiceCateDao.selectById(id);
    }

    @Override
    public List<ShopServiceCate> selectByIds(List<Long> ids) {
        return super.selectByIds(shopServiceCateDao, ids);
    }

    @Override
    public List<ShopServiceCate> getNormalService(Long shopId) {
        //查询标准化服务的类别
        Map<String,Object> map = new HashMap<>();
        map.put("cateType",2);
        List<ShopServiceCate> shopServiceCateList = selectNoCache(map);
        //查询门店服务，标准化服务若存在则有选中效果，形式”parentId，shopServiceInfo“
        map.clear();
        map.put("shopId", shopId);
        map.put("searchBZFW",0);
        List<ShopServiceInfo> checkList = shopServiceInfoService.select(map);
        Map<Long,ShopServiceInfo> checkMap = new HashMap<>();
        for(ShopServiceInfo shopServiceInfo : checkList){
            checkMap.put(shopServiceInfo.getParentId(),shopServiceInfo);
        }
        //查询标准服务
        map.clear();
        map.put("flags","BZFW");
        List<ServiceTemplate> serviceTemplateList = serviceTemplateService.select(map);
        Map<Long,List<ServiceTemplate>> serviceTemplateMap = new HashMap<>();
        for(ServiceTemplate serviceTemplate : serviceTemplateList){
            Long cateId = serviceTemplate.getCateId();
            Long id = serviceTemplate.getId();
            if(checkMap.containsKey(id)){
                ShopServiceInfo shopServiceInfo = checkMap.get(id);
                BigDecimal servicePrice = shopServiceInfo.getServicePrice();
                serviceTemplate.setCheck(true);
                serviceTemplate.setServicePrice(servicePrice);
            }
            if(serviceTemplateMap.containsKey(cateId)){
                List<ServiceTemplate> tempList = serviceTemplateMap.get(cateId);
                tempList.add(serviceTemplate);
            }else{
                List<ServiceTemplate> tempList = new ArrayList<>();
                tempList.add(serviceTemplate);
                serviceTemplateMap.put(cateId,tempList);
            }
        }
        //返回数据，形式“类别（List<ServiceTemplate>）”
        for(ShopServiceCate shopServiceCate : shopServiceCateList){
            Long cateId = shopServiceCate.getId();
            if(serviceTemplateMap.containsKey(cateId)){
                List<ServiceTemplate> tempList = serviceTemplateMap.get(cateId);
                shopServiceCate.setServiceTemplateList(tempList);
            }
        }
        return shopServiceCateList;
    }

    @Override
    public int insert(ShopServiceCate shopServiceCate) {
        return shopServiceCateDao.insert(shopServiceCate);
    }

    @Override
    public List<ShopServiceCate> list(Long shopId, Collection<Long> serviceCatIds) {
        if (CollectionUtils.isEmpty(serviceCatIds)) {
            return Lists.newArrayList();
        }
        return shopServiceCateDao.selectByIds2(shopId, serviceCatIds);
    }

    @Override
    public Map<Long, Long> getSecondCate() {
        Map param = Maps.newHashMap();
        param.put("cate_type", 1);
        param.put("shop_id", 0);
        List<ShopServiceCate> cateList = shopServiceCateDao.select(param);

        Map<Long, Long> catIdlevelMap = Maps.newHashMap();
        for (ShopServiceCate shopServiceCate : cateList) {
            if (!shopServiceCate.isTop()) {
                catIdlevelMap.put(shopServiceCate.getId(), shopServiceCate.getParentId());
            }
        }

        return catIdlevelMap;
    }

    /**
     * create by jason 2015-09-17
     * 处理服务类别 cate_type = 1 车主服务类别
     */
    @Override
    public Map<Long, ShopServiceCate> dealCateInfo() {

        Map<Long, ShopServiceCate> serviceCateMap = null;
        try {
            List<ShopServiceCate> cacheList = cacheComponent.getCache(CacheKeyConstant.SERVICE_CATEGORY);
            //组装数据，形式为”类别id，类别对象“
            serviceCateMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(cacheList)) {
                for (ShopServiceCate serviceCate : cacheList) {
                    Integer cateType = serviceCate.getCateType();
                    //车主服务类别
                    if (cateType == 1) {
                        serviceCateMap.put(serviceCate.getId(), serviceCate);
                    }
                }
                //把一级类别的名称set到二级类别对象中去
                if (!CollectionUtils.isEmpty(serviceCateMap)) {
                    for (Map.Entry<Long, ShopServiceCate> entry : serviceCateMap.entrySet()) {
                        ShopServiceCate serviceCate = entry.getValue();
                        Long parentId = serviceCate.getParentId();
                        ShopServiceCate parentServiceCate = serviceCateMap.get(parentId);
                        //CZFW和TQFW类别 有一级类目
                        if (parentId > 0l && parentServiceCate!= null) {
                            serviceCate.setFirstCateName(parentServiceCate.getName());
                            if(StringUtils.isBlank(serviceCate.getDefaultImgUrl())){
                                serviceCate.setDefaultImgUrl(parentServiceCate.getDefaultImgUrl());
                            }
                            if(StringUtils.isBlank(serviceCate.getIconUrl())){
                                serviceCate.setIconUrl(parentServiceCate.getIconUrl());
                            }
                        }
                    }
                }
            } else {
                log.error("从redis获得服务类别数据位空!");
            }
        } catch (Exception e) {
            log.error("处理服务类别异常!" + e);
        }
        return serviceCateMap;
    }

    /**
     * create by jason 2015-11-11
     * 组装车主一级服务类别map
     *
     * @return Map<Long, ShopServiceCate>
     */
    @Override
    public Map<Long, ServiceCateVo> warpFirstCateInfo() {
        //车主一级服务类别Map
        Map<Long, ServiceCateVo> firstCateMap = new HashMap<>();

        List<ServiceCateVo> firstCateList =  selectFirstCate();
        if (!CollectionUtils.isEmpty(firstCateList)) {

            for (ServiceCateVo cateVo : firstCateList) {
                Long cateId = cateVo.getId();
                if (!firstCateMap.containsKey(cateId)) {
                    firstCateMap.put(cateId, cateVo);
                }
            }
        }
        return firstCateMap;
    }

    @Override
    public List<ShopServiceCate> findServiceCatesByCatNames(Long shopId, Integer cateType, String... catNames) {
        return shopServiceCateDao.findServiceCatesByCatNames(shopId, cateType, catNames);
    }

    @Override
    public void batchSave(List<ShopServiceCate> shopServiceCates) {
        super.batchInsert(shopServiceCateDao,shopServiceCates,1000);
    }
}
