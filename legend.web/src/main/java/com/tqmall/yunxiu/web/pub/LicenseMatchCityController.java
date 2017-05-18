package com.tqmall.yunxiu.web.pub;

import com.google.common.collect.Maps;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.customer.LicenseMatchCityService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.customer.LicenseCity;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/6.
 * <p/>
 * 通过车牌号定位城市或者通过城市获取车牌号
 */
@Controller
@RequestMapping("/pub/license/city")
public class LicenseMatchCityController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(LicenseMatchCityController.class);

    @Autowired
    private LicenseMatchCityService licenseMatchCityService;
    @Autowired
    private ShopService shopService;

    /**
     * 通过城市id，获取车牌号对应城市信息
     * @param cityId 城市id
     * @return 返回车牌号对应城市信息
     */
    @RequestMapping(value = "/cur_license/{cityId}",method = RequestMethod.GET)
    @ResponseBody
    public Result getLicense (@PathVariable("cityId") Long cityId){
        Map<String,Object> paramMap = Maps.newConcurrentMap();
        paramMap.put("cityId",cityId);
        List<LicenseCity> licenseCities = licenseMatchCityService.select(paramMap);
        if(!CollectionUtils.isEmpty(licenseCities)){
            logger.info("通过城市id为{"+cityId+"}获取车牌对应城市信息成功");
            return Result.wrapSuccessfulResult(licenseCities.get(0));
        }
        logger.error("通过城市id为{"+cityId+"}获取车牌对应城市信息失败");
        return Result.wrapErrorResult("9999", "无法获取该城市站的车牌");
    }

    /**
     * 获取车牌号省份信息
     * @return 返回车牌号省份信息列表
     */
    @ResponseBody
    @RequestMapping("/get_province")
    public Result getProvince(){
        List<LicenseCity> licenseCities = licenseMatchCityService.getByProvince();
        if(!CollectionUtils.isEmpty(licenseCities)){
            logger.info("获取车牌省份信息成功");
            return Result.wrapSuccessfulResult(licenseCities);
        }
        logger.error("获取车牌省份信息失败");
        return Result.wrapErrorResult("9999", "无法获取车牌省份信息");
    }

    /**
     * 通过当前省份获取车牌号信息
     * @param parentId 当前省份id
     * @return 返回车牌号信息列表
     */
    @RequestMapping(value = "/cur_province/{parentId}",method = RequestMethod.GET)
    @ResponseBody
    public Result getLicenceByProvince(@PathVariable("parentId")String parentId){
        Map<String,Object> paramMap = Maps.newConcurrentMap();
        paramMap.put("parentId",parentId);
        List<LicenseCity> licenseCities = licenseMatchCityService.getLicenseByProvince(paramMap);
        if(!CollectionUtils.isEmpty(licenseCities)){
            logger.info("通过省份id为{"+parentId+"}获取车牌信息成功");
            return Result.wrapSuccessfulResult(licenseCities);
        }
        logger.info("通过省份id为{" + parentId + "}获取车牌信息失败");
        return Result.wrapErrorResult("9999", "无法获取当前省份的车牌信息");
    }


    /**
     * 自动获取门店
     * @return
     */
    @RequestMapping("/get_license")
    @ResponseBody
    public Result getLicense(){
        Long shopId = UserUtils.getShopIdForSession(request);
        String license = "浙A";
        /**
         *
         */
        Shop shop = this.shopService.selectById(shopId);
        if(shop != null && shop.getCity() != null) {
            license = getLicenseByCityId(shop.getCity());
        } else {
            /**
             * 如果获取不到门店的cityId,则根据ip自动定位城市id,并获取响应的车牌
             */
        }

        return Result.wrapSuccessfulResult(license);

    }

    private String getLicenseByCityId(Long cityId) {
        if(cityId != null){
            Map<String, Object> paramMap = Maps.newConcurrentMap();
            paramMap.put("cityId", cityId);
            List<LicenseCity> licenseCities = licenseMatchCityService.select(paramMap);
            if(licenseCities != null && licenseCities.size() == 1){
                return licenseCities.get(0).getLicense();
            }
        }
        return null;
    }
}
