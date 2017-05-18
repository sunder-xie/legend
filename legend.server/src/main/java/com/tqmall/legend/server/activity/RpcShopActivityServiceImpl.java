package com.tqmall.legend.server.activity;

import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.activity.ActivityTemplateService;
import com.tqmall.legend.biz.activity.IShopActivityService;
import com.tqmall.legend.biz.activity.IShopActivityServiceRelService;
import com.tqmall.legend.biz.api.ILotteryService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.entity.activity.ActivityTemplate;
import com.tqmall.legend.entity.activity.ShopActivity;
import com.tqmall.legend.entity.activity.ShopActivityServiceRel;
import com.tqmall.legend.entity.lottery.Activity;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.object.param.activity.ShopActivityParam;
import com.tqmall.legend.object.result.activity.ActivityDTO;
import com.tqmall.legend.object.result.activity.ActivityTemplateDTO;
import com.tqmall.legend.object.result.activity.ShopActivityDTO;
import com.tqmall.legend.object.result.activity.ShopActivityPageDTO;
import com.tqmall.legend.object.result.service.ShopServiceInfoDTO;
import com.tqmall.legend.service.activity.RpcShopActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wushuai on 16/7/29.
 */
@Slf4j
@Service("rpcShopActivityService")
public class RpcShopActivityServiceImpl implements RpcShopActivityService {

    @Autowired
    private IShopActivityService shopActivityService;
    @Autowired
    private IShopActivityServiceRelService shopActivityServiceRelService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ActivityTemplateService activityTemplateService;

    @Autowired
    ILotteryService lotteryService;

    @Override
    public Result<ShopActivityPageDTO> getShopActivityPage(ShopActivityParam param) {
        if(param==null){
            log.error("[dubbo]查询活动实体列表失败,参数为空");
            return Result.wrapErrorResult("-1","参数为空");
        }
        ShopActivityPageDTO  shopActivityPageDTO = new ShopActivityPageDTO();
        Integer offset = 0;
        Integer limit = 10;
        if(param.getLimit()!=null){
            limit = param.getLimit();
        }
        if(param.getOffset()!=null){
            offset = param.getOffset();
        }
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("offset",offset);
        searchMap.put("limit",limit);
        if(param.getActTplId()!=null){
            searchMap.put("actTplId",param.getActTplId());
        }
        if(!CollectionUtils.isEmpty(param.getSorts())){
            searchMap.put("sorts",param.getSorts());
        }
        if(param.getActStatus()!=null){
            searchMap.put("actStatus",param.getActStatus());
        }
        if(param.getUserGlobalId()!=null){
            Shop shop = shopService.getShopByUserGlobalId(param.getUserGlobalId());
            if (shop != null) {
                searchMap.put("shopId", shop.getId());
            } else {
                shopActivityPageDTO.setTotal(0);
                shopActivityPageDTO.setContent(new ArrayList<ShopActivityDTO>());
                return Result.wrapSuccessfulResult(shopActivityPageDTO);
            }
        }
        List<ShopActivity> shopActivityList = shopActivityService.select(searchMap);
        Integer total = shopActivityService.selectCount(searchMap);
        List<Long> shopIds = new ArrayList<>();
        Map<Long,Long> shopMap = new HashMap<>();
        List<ShopActivityDTO> content = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shopActivityList)) {
            for (ShopActivity shopActivity:shopActivityList){
                ShopActivityDTO shopActivityDTO = new ShopActivityDTO();
                BeanUtils.copyProperties(shopActivity,shopActivityDTO);
                content.add(shopActivityDTO);
                shopIds.add(shopActivity.getShopId());
            }
        }
        //查询门店得到userGlobalIds
        if(!CollectionUtils.isEmpty(shopIds)){
            Map<String,Object> shopSearchParam = new HashMap<>();
            shopSearchParam.put("shopIds",shopIds);
            List<Shop> shopList = shopService.select(shopSearchParam);
            if(!CollectionUtils.isEmpty(shopList)) {
                for(Shop shop:shopList) {
                    if(StringUtils.isNotBlank(shop.getUserGlobalId())){
                        Long userGlobalId = Long.parseLong(shop.getUserGlobalId());
                        shopMap.put(shop.getId(),userGlobalId);
                    }
                }
            }
        }
        //给ShopActivityDTO设置userGlobalId
        if(!CollectionUtils.isEmpty(content)){
            for(ShopActivityDTO shopActivityDTO:content){
                Long userGlobalId = shopMap.get(shopActivityDTO.getShopId());
                shopActivityDTO.setUserGlobalId(userGlobalId);
            }
        }
        shopActivityPageDTO.setContent(content);
        shopActivityPageDTO.setTotal(total);
        return Result.wrapSuccessfulResult(shopActivityPageDTO);
    }

    @Override
    public Result<ShopActivityDTO> getShopActivity(ShopActivityParam param) {
        if (param==null||param.getActId()==null){
            log.error("[dubbo]查询活动实体详情失败,参数非法{}", LogUtils.objectToString(param));
            return Result.wrapErrorResult("-1","参数错误");
        }
        Long actId = param.getActId();
        ShopActivity shopActivity = shopActivityService.selectById(actId);
        if (shopActivity==null){
            return Result.wrapSuccessfulResult(null);
        }
        ShopActivityDTO shopActivityDTO = new ShopActivityDTO();
        BeanUtils.copyProperties(shopActivity,shopActivityDTO);
        Long shopId = shopActivity.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (shop != null && StringUtils.isNotBlank(shop.getUserGlobalId())) {
            Long userGlobalId = Long.parseLong(shop.getUserGlobalId());
            shopActivityDTO.setUserGlobalId(userGlobalId);
        }
        //查询活动实体和服务实体的关系
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("shopActId",actId);
        List<ShopActivityServiceRel> shopActivityServiceRelList = shopActivityServiceRelService.select(searchMap);
        List<Long> serviceIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(shopActivityServiceRelList)){
            for (ShopActivityServiceRel shopActivityServiceRel:shopActivityServiceRelList) {
                if (shopActivityServiceRel.getServiceId()!=null){
                    serviceIds.add(shopActivityServiceRel.getServiceId());
                }
            }
        }
        if (!CollectionUtils.isEmpty(serviceIds)) {
            Map<String,Object> serviceInfoParam = new HashMap<>();
            serviceInfoParam.put("ids",serviceIds);
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.select(serviceInfoParam);
            if(!CollectionUtils.isEmpty(shopServiceInfoList)){
                List<ShopServiceInfoDTO> serviceInfoDTOList = new ArrayList<>();
                for(ShopServiceInfo shopServiceInfo:shopServiceInfoList){
                    ShopServiceInfoDTO shopServiceInfoDTO = new ShopServiceInfoDTO();
                    BeanUtils.copyProperties(shopServiceInfo,shopServiceInfoDTO);
                    serviceInfoDTOList.add(shopServiceInfoDTO);
                }
                shopActivityDTO.setServiceInfoDTOList(serviceInfoDTOList);
            }
        }
        return Result.wrapSuccessfulResult(shopActivityDTO);
    }

    @Override
    public Result<ActivityTemplateDTO> getActTplByActId(Long actId) {
        ShopActivity shopActivity = shopActivityService.selectById(actId);
        if(shopActivity==null ||shopActivity.getActTplId()==null){
            return Result.wrapSuccessfulResult(null);
        }
        Long actTplId = shopActivity.getActTplId();
        ActivityTemplate activityTemplate = activityTemplateService.getById(actTplId);
        if(activityTemplate==null){
            return Result.wrapSuccessfulResult(null);
        }
        ActivityTemplateDTO activityTemplateDTO = new ActivityTemplateDTO();
        BeanUtils.copyProperties(activityTemplate,activityTemplateDTO);
        return Result.wrapSuccessfulResult(activityTemplateDTO);
    }

    @Override
    public Result<List<ActivityDTO>> gainOnlineActivity() {
        try {
            com.tqmall.legend.common.Result<List<Activity>> result = lotteryService.getCurrentOnlineActivity();
            if (!result.isSuccess()){
                return Result.wrapErrorResult(result.getCode(),result.getErrorMsg());
            }
            List<ActivityDTO> activityDTOs = BdUtil.bo2do4List(result.getData(),ActivityDTO.class);
            return Result.wrapSuccessfulResult(activityDTOs);

        } catch (Exception e) {
            log.error("[API]获取当前活动异常", e);
            Result errorResult = Result.wrapErrorResult("", "获取当前活动异常");
            return errorResult;
        }
    }

    @Override
    public Result<ActivityDTO> gainActivity(Long activityId) {
        try {
            com.tqmall.legend.common.Result<Activity> result =  lotteryService.getActivity(activityId);
            if (!result.isSuccess()){
                return Result.wrapErrorResult(result.getCode(),result.getErrorMsg());
            }
            ActivityDTO activityDTO = BdUtil.bo2do(result.getData(),ActivityDTO.class);
            return Result.wrapSuccessfulResult(activityDTO);

        } catch (Exception e) {
            log.error("[API]获取活动异常", e);
            Result errorResult = Result.wrapErrorResult("", "获取活动异常");
            return errorResult;
        }
    }
}
