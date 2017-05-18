package com.tqmall.legend.biz.remote.dandelion;

import com.google.gson.Gson;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.biz.bo.dandelion.TaoqiBaseCouponParam;
import com.tqmall.legend.biz.bo.dandelion.TaoqiCouponParam;
import com.tqmall.common.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lixiao on 15/7/21.
 */
@Component
public class RemoteCouponHttp {

    Logger logger = LoggerFactory.getLogger(RemoteCouponHttp.class);

    //蒲公英项目URL
    @Value("${dandelion.url}")
    private String dandelionUrl;

    /**
	 * TODO 代码重构
	 *
     * 优惠券校验接口
     * @param baseCouponParam
     * @return
     */
    public Map<String, Object> couponCheck(TaoqiBaseCouponParam baseCouponParam) {
        String requestUrl = dandelionUrl + "coupon/check";
        try {
            String paramStr = new Gson().toJson(baseCouponParam);
			logger.info("dandelion优惠券校验接口,url:{},param:{}",requestUrl,paramStr);
			String result = HttpUtil.sendPost(requestUrl, paramStr);
            logger.info("优惠券校验接口,传参:"+paramStr);
            if (StringUtils.isBlank(result)) {
                logger.warn("调用dandelion结算优惠券校验接口超时，传输数据信息为：" + result);
            } else {
                logger.info("调用dandelion结算优惠券校验接口成功，传输数据信息为：" + result);
                Map<String, Object> resultMap = JSONUtil.getMapper().readValue(result, Map.class);//String转化为map
                if (!CollectionUtils.isEmpty(resultMap)) {
                    Boolean success = (Boolean) resultMap.get("success");
                    if (success != null && success) {
                        logger.info("调用dandelion结算优惠券校验接口成功，传输数据信息为：" + result);
                        return resultMap;
                    } else {
                        logger.error("调用dandelion结算优惠券校验接口失败，当前数据信息为：" + resultMap);
                        return resultMap;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("结算优惠券校验，JSON转化出错！", e);


        }
        return null;
    }


    /**
     * 优惠券结算接口
     * @param couponParam
     * @return
     */
    public Map<String, Object> couponSettle(TaoqiCouponParam couponParam){
        String requestUrl = dandelionUrl + "coupon/settle";
        String paramStr = new Gson().toJson(couponParam);
        String result = HttpUtil.sendPost(requestUrl, paramStr);
        logger.info("优惠券结算接口,传参:"+paramStr);
        String errorMsg = "淘汽优惠券核销失败，请稍后再试";
        if (StringUtils.isBlank(result)) {
            logger.warn("调用dandelion优惠券结算接口超时，传输数据信息为：" + result);
        } else {
            logger.info("调用dandelion优惠券结算接口成功，传输数据信息为：" + result);
            Map<String, Object> resultMap = null;//String转化为map
            try {
                resultMap = JSONUtil.getMapper().readValue(result, Map.class);
            } catch (IOException e) {
               logger.error("调用dandelion优惠券结算接口,json转换出错{}",e);
            }
            if (!CollectionUtils.isEmpty(resultMap)) {
                Boolean success = (Boolean) resultMap.get("success");
                if (success != null && success) {
                    logger.info("调用dandelion优惠券结算接口成功，传输数据信息为：" + result);
                    return resultMap;
                } else {
                    String code = (String) resultMap.get("code");
                    if(StringUtils.isNotBlank(code)){
                        //蒲公英错误信息参数code：
                        //"50110022": "工单模板ID为空!"
                        //"50110023": "工单不能使用此优惠券!"
                        if(code.equals("50110022") || code.equals("50110023")){
                            errorMsg = (String) resultMap.get("errorMsg");
                        }
                    }
                    logger.error("调用dandelion优惠券结算接口失败，当前数据信息为：" + resultMap);
                }
            }
        }
        //优惠券结算失败，事务回滚
        throw new RuntimeException(errorMsg);
    }


    /**
     * 工单重新结算接口
     * @param couponParam
     * @return
     */
    public Map couponResettle(TaoqiCouponParam couponParam) {
        String requestUrl = dandelionUrl + "coupon/resettle";
        try {
            String paramStr = new Gson().toJson(couponParam);
            String result = HttpUtil.sendPost(requestUrl, paramStr);
            logger.info("优惠券重新结算接口,传参:"+paramStr);
            if (StringUtils.isBlank(result)) {
                logger.warn("调用dandelion优惠券重新结算接口超时，传输数据信息为：" + result);
            } else {
                logger.info("调用dandelion优惠券重新结算接口成功，传输数据信息为：" + result);
                Map<String, Object> resultMap = JSONUtil.getMapper().readValue(result, Map.class);//String转化为map
                if (!CollectionUtils.isEmpty(resultMap)) {
                    Boolean success = (Boolean) resultMap.get("success");
                    if (success != null && success) {
                        logger.info("调用dandelion优惠券重新结算接口成功，传输数据信息为：" + result);
                        return resultMap;
                    } else {
                        logger.error("调用dandelion优惠券重新结算接口失败，当前数据信息为：" + resultMap);
                        return resultMap;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("优惠券重新结算，JSON转化出错！", e);


        }
        return null;
    }


}
