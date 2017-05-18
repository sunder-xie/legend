package com.tqmall.legend.web.car;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.tqmall.athena.client.car.CarCategoryService;
import com.tqmall.athena.client.car.CarServiceExtend;
import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.athena.domain.result.carcategory.CarListDTO;
import com.tqmall.athena.domain.result.carcategory.HotCarBrandDTO;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.HttpUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.core.utils.SerializeUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.support.elasticsearch.ELResult3;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerCarByModel;
import com.tqmall.legend.entity.customer.CustomerCarComm;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.customer.vo.CarModelVoForVin;
import com.tqmall.search.dubbo.client.goods.car.result.CarSearchResult;
import com.tqmall.search.dubbo.client.goods.car.result.CarSixLevelResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by lixiao on 14-11-14.
 */
@Slf4j
@Controller
@RequestMapping("shop/car_category")
public class CarCategoryController extends BaseController {

    @Value("${new.i.search.url}")
    private String iSearchUrl;

    @Autowired
    private CarCategoryService carCategoryService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CarServiceExtend carServiceExtend;
    @Autowired
    private  com.tqmall.search.dubbo.client.goods.car.service.CarCategoryService rpcCarCategoryService;

    /**
     * 从新接口获取数据
     * 如果pid=-1传入老接口会报错,只能传新接口
     * @param pid
     * @return
     */
    @RequestMapping("ng")
    @ResponseBody
    public Result carCategoryNg(Integer pid) {
        com.tqmall.core.common.entity.Result<List<CarCategoryDTO>> result = carServiceExtend.carListByPid(pid);
        if(log.isDebugEnabled()) {
            log.debug("从数据团队获取车型数据.{}", LogUtils.funToString(pid, result));
        }
        if(result != null && result.isSuccess() && result.getData() != null){
            List<CarCategoryDTO> data = result.getData();
            return Result.wrapSuccessfulResult(data);
        }
        log.error("从数据团队获取车型信息失败.{}", LogUtils.funToString(pid, result));
        return Result.wrapErrorResult("9999", "获取车型失败");
    }

    @RequestMapping("")
    @ResponseBody
    public Result carCategory(Integer pid) {
        com.tqmall.core.common.entity.Result<List<CarListDTO>> result = this.carCategoryService.categoryCarInfo(pid);

        if(log.isDebugEnabled()){
            log.debug("从数据团队获取车型数据.{}", LogUtils.funToString(pid, result));
        }
        if(result != null && result.isSuccess() && result.getData() != null){
            List<CarListDTO> carCatDTOs = result.getData();
            if (Integer.valueOf(0).equals(pid)) {
                carCatDTOs.add(_newOther());
            }

            /**
             * 在新增客户弹框中,车型选择必须为二级,为兼容,对其他车型增加其他二级分类
             */
            if (Integer.valueOf(-1).equals(pid)) {
                carCatDTOs = new LinkedList<>();
                CarListDTO carCategoryDTO1 = new CarListDTO();
                carCategoryDTO1.setId(-2);
                carCategoryDTO1.setPid(-1);
                carCategoryDTO1.setBrand("未知");
                carCategoryDTO1.setName("其他");
                carCategoryDTO1.setImportInfo("");
                carCategoryDTO1.setFirstWord("Q");
                carCategoryDTO1.setLevel(0);
                carCatDTOs.add(carCategoryDTO1);
            }
            return Result.wrapSuccessfulResult(carCatDTOs);

        }else {
            log.error("从数据团队获取车型信息失败.{}", LogUtils.funToString(pid, result));
            return Result.wrapErrorResult("9999", "获取车型失败");
        }
    }

    @RequestMapping("hot")
    @ResponseBody
    public Result hot() {
        com.tqmall.core.common.entity.Result<List<HotCarBrandDTO>> result = this.carCategoryService.getHotCarBrandList(383);
        if(result != null && result.isSuccess()){
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            log.error("get hot car category data error, the result is:{}",LogUtils.funToString(383,result));
        }
        return Result.wrapErrorResult("9999","获取热门车型失败.");
    }

    @RequestMapping("brand_letter")
    @ResponseBody
    public Result all() {
        com.tqmall.core.common.entity.Result<List<CarCategoryDTO>> result = this.carServiceExtend.carListByPid(0);
        if (result != null && result.isSuccess()) {
            Map < String, List < CarCategoryDTO >> carCatMap = new TreeMap<>();
            for (CarCategoryDTO carCategoryDTO : result.getData()) {
                List<CarCategoryDTO> carCatList = carCatMap.get(carCategoryDTO.getFirstWord());
                if (carCatList == null) {
                    carCatList = new LinkedList<>();
                    carCatMap.put(carCategoryDTO.getFirstWord(), carCatList);
                }
                carCatList.add(carCategoryDTO);
            }
            if(log.isDebugEnabled()){
                log.debug("调用接口获取车型成功,{}", LogUtils.funToString(0,result));
            }
            return Result.wrapSuccessfulResult(carCatMap);
        } else {
            log.error("调用接口获取车型失败,{}", LogUtils.funToString(0,result));
        }
        return Result.wrapErrorResult("9999", "获取车型分组失败.");
    }


    //add by twg 2015-10-22
    @RequestMapping("comm_brand")
    @ResponseBody
    public Result carCommBrand(HttpServletRequest request){
        Long shopId = UserUtils.getShopIdForSession(request);
        List<CustomerCarComm> commBrand = cacheManage(Constants.COMM_CAR_BRAND_PREFIX, "comm_car_brand_"+shopId,shopId);

        return Result.wrapSuccessfulResult(commBrand);
    }

    private List<CustomerCarComm> cacheManage(String cacheKey,String key,Long shopId){
        Map map = Maps.newConcurrentMap();
        map.put("shopId", shopId);
        map.put("offset", 0);
        map.put("limit", 50);
        Jedis jedis = null;
        List<CustomerCarComm> commBrand = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            byte[] data = jedis.hget(cacheKey.getBytes("UTF-8"), key.getBytes("UTF-8"));
            if (data != null) {
                Object object = SerializeUtil.unserialize(data);
                return (List<CustomerCarComm>)object;
            }else{
                commBrand = selectCommCarBrand(map);
                if(!CollectionUtils.isEmpty(commBrand)){
                    data = SerializeUtil.serialize(commBrand);
                    jedis.hset(cacheKey.getBytes("UTF-8"), key.getBytes("UTF-8"), data);
                }
            }
        } catch (Exception e) {
            log.error("从redis获取常用车品牌信息异常,参数:cacheKey={},key={},shopId={},异常信息:{}",cacheKey,key,shopId, e);
            commBrand = selectCommCarBrand(map);
            if(!CollectionUtils.isEmpty(commBrand)){
                try {
                    jedis.hset(cacheKey.getBytes("UTF-8"), key.getBytes("UTF-8"), SerializeUtil.serialize(commBrand));
                } catch (UnsupportedEncodingException e1) {
                    log.error("从数据库获取常用车品牌{},设置redis信息异常,{}", JSONUtil.object2Json(commBrand),e1);
                }
            }
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return commBrand;
    }

    /**
     * 常用车型
     * @param cacheKey
     * @param key
     * @param shopId
     * @return
     */
    private List<CustomerCarByModel> cacheManageToModel(String cacheKey,String key,Long shopId){
        Map map = Maps.newConcurrentMap();
        map.put("shopId", shopId);
        map.put("offset", 0);
        map.put("limit", 50);
        Jedis jedis = null;
        List<CustomerCarByModel> commBrand = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            byte[] data = jedis.hget(cacheKey.getBytes("UTF-8"), key.getBytes("UTF-8"));
            if (data != null) {
                Object object = SerializeUtil.unserialize(data);
                return (List<CustomerCarByModel>)object;
            }else{
                commBrand = selectCommCarByModel(map);
                if(!CollectionUtils.isEmpty(commBrand)){
                    data = SerializeUtil.serialize(commBrand);
                    jedis.hset(cacheKey.getBytes("UTF-8"), key.getBytes("UTF-8"), data);
                    jedis.expire(cacheKey, CacheConstants.COMM_CART_MODEL_KEY_EXP_TIME);
                }
            }
        } catch (Exception e) {
            log.error("从redis获取常用车型信息异常,参数:cacheKey={},key={},shopId={},异常信息:{}",cacheKey,key,shopId, e);
            commBrand = selectCommCarByModel(map);
            if(!CollectionUtils.isEmpty(commBrand)){
                try {
                    jedis.hset(cacheKey.getBytes("UTF-8"), key.getBytes("UTF-8"), SerializeUtil.serialize(commBrand));
                    jedis.expire(cacheKey.getBytes("UTF-8"), CacheConstants.COMM_CART_MODEL_KEY_EXP_TIME);
                } catch (UnsupportedEncodingException e1) {
                    log.error("从数据库获取常用车型{},设置redis信息异常,{}", JSONUtil.object2Json(commBrand),e1);
                }
            }
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return commBrand;
    }

    private  List<CustomerCarComm> selectCommCarBrand(Map map){
        List<CustomerCarComm> commBrand = customerCarService.queryCustomerCar(map);
        for (CustomerCarComm customerCarComm : commBrand) {
            com.tqmall.core.common.entity.Result<CarCategoryDTO> result = carCategoryService.getCarCategoryByPrimaryId(customerCarComm.getCarBrandId().intValue());
            if(result.getData()!= null && result.isSuccess()){
                CarCategoryDTO carCategoryDTOs = result.getData();
                customerCarComm.setLogo(carCategoryDTOs.getLogo());
            }else {
                log.error("调用接口获取车型失败,{}", LogUtils.funToString(customerCarComm.getCarBrandId(), result));
            }
        }
        return commBrand;
    }

    private  List<CustomerCarByModel> selectCommCarByModel(Map map){
        List<CustomerCarByModel> commBrand = customerCarService.queryCustomerCarByModel(map);
        return commBrand;
    }


    /**
     * 目前车型库不能包含所有车型,淘汽云修增加id为-1的其他分类
     * @return
     */
    private CarListDTO _newOther(){
        CarListDTO carCategoryDTO = new CarListDTO();
        carCategoryDTO.setId(-1);
        carCategoryDTO.setName("其他");
        carCategoryDTO.setBrand("未知");
        carCategoryDTO.setImportInfo("");
        carCategoryDTO.setFirstWord("Q");
        carCategoryDTO.setLevel(0);
        return carCategoryDTO;
    }

    /**
     * 新车型组件搜索
     * @param q
     * @return
     */
    @RequestMapping("car_model")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List> getCarCateList(@RequestParam("q") final String q, @RequestParam(value="model_id",required = false) final Integer modelId) {
        return new ApiTemplate<List>(){

            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                if (StringUtils.isEmpty(q) && modelId == null) {
                    List<CustomerCarByModel> carComm = cacheManageToModel(Constants.COMM_CAR_BRAND_PREFIX, "comm_car_model_" + shopId, shopId);
                    return carComm;
                }
                Integer level = null;
                if (null != modelId) {
                    level = 6;
                }
                /**
                 * 原有 默认显示10条
                 */
                int size = 10;
                log.info("[从搜索获取车辆信息] q:{},level:{},modelId:{},size:{}", q, level, modelId, size);
                com.tqmall.search.common.result.Result<CarSearchResult<CarSixLevelResult>> searchResult = rpcCarCategoryService.carModelHalfSearch(q, level, modelId, size);
                if (null == searchResult || !searchResult.isSuccess()) {
                    log.error("[从搜索获取车辆信息失败] q:{},level:{},modelId:{},size:{},searchResult:{}", q, level, modelId, size, LogUtils.objectToString(searchResult));
                    throw new BizException("调用搜索引擎失败");
                }
                return searchResult.getData().getList();
            }
        }.execute();
    }

    /**
     * 新车型组件搜索
     * @param q
     * @return
     */
    @RequestMapping("car_model_by_vin")
    @ResponseBody
    public Result getCarCateListByVin(@RequestParam("q")String q,
                                      HttpServletRequest request) {

        Result result = null;

        String responseText = null;
//        iSearchUrl = "http://search.epei360.com/";
        String searchURL = iSearchUrl + "elasticsearch/goods/vin";
        StringBuffer qParam = new StringBuffer();
        if(StringUtils.isEmpty(q) ){
            return Result.wrapErrorResult("","vin码不能为空");
        }
        try {
            qParam = qParam.append("q=").append(UriUtils.encodeQueryParam(q, "utf-8"));

            responseText = HttpUtil.sendGet(searchURL, qParam.toString());
            if(log.isInfoEnabled()){
                log.info("从搜索获取车辆信息,URL{},Param:{},responseText{}",searchURL, qParam.toString(), responseText);
            }
            if(StringUtils.isBlank(responseText)){
                result = Result.wrapErrorResult("","调用搜索引擎错误");
                log.error(String.format("invoke isearch:%s,query param:%s, error.",iSearchUrl,qParam.toString()));
            } else {
                ELResult3<CarModelVoForVin> elResult = JSON.parseObject(responseText,
                        new TypeReference<ELResult3<CarModelVoForVin>>() {
                        });
                if(elResult == null || elResult.getResponse() == null){
                    result = Result.wrapErrorResult("","找不到相关数据");
                    return result;
                }
                List<CarModelVoForVin> list = elResult.getResponse().getList();
                if (CollectionUtils.isEmpty(list)) {
                    result = Result.wrapErrorResult("","找不到相关数据");
                }else {
                    result = Result.wrapSuccessfulResult(elResult.getResponse().getList());
                }
            }

        } catch (Exception e) {
            log.error(String.format("invoke isearch:%s,query param:%s, error.",iSearchUrl,qParam.toString()),e);
            result = Result.wrapErrorResult("","系统错误");
        }
        return result;

    }



}
