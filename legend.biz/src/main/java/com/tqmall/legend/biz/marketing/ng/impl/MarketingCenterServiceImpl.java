package com.tqmall.legend.biz.marketing.ng.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.cube.shop.param.marketing.accurate.AccurateParam;
import com.tqmall.cube.shop.provider.marketing.accurate.RpcAccurateService;
import com.tqmall.cube.shop.result.marketing.accurate.CustomerAccurateDTO;
import com.tqmall.legend.biz.marketing.ng.MarketingCenterService;
import com.tqmall.legend.biz.marketing.ng.adaptor.CustomerInfoConverter;
import com.tqmall.legend.biz.marketing.ng.adaptor.PagedParamConverter;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.wheel.lang.Langs;
import com.tqmall.wheel.support.rpc.result.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by wanghui on 2/25/16.
 */
@Service
@Slf4j
public class MarketingCenterServiceImpl implements MarketingCenterService {

    @Autowired
    private RpcAccurateService rpcAccurateService;

    @Override
    public Page<CustomerInfo> selectAccurate(final Map<String, Object> params, final Pageable pageable) {
        return new BizTemplate<Page<CustomerInfo>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notEmpty(params);
                Long shopId = (Long) params.get("shopId");
                Assert.isTrue(shopId != null && shopId > 0);
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Page<CustomerInfo> process() throws BizException {
                AccurateParam param = mapToAccurateParam(params);
                PagedParamConverter.convert(pageable, param);
                RpcResult<com.tqmall.wheel.support.data.DefaultPage<CustomerAccurateDTO>> result = rpcAccurateService.getAccurateCustomer(param);
                if (result == null || !result.isSuccess()) {
                    log.error("精准营销查询客户失败，参数: param = {}, 返回值: result = {}", JSONUtil.object2Json(param), JSONUtil.object2Json(result));
                    throw new BizException("精准营销查询客户失败");
                }
                return CustomerInfoConverter.convertAccuratePage(result.getData());
            }
        }.execute();
    }

    private AccurateParam mapToAccurateParam(Map<String, Object> params) {
        if (params == null) {
            throw new BizException("参数为空");
        }
        AccurateParam param = new AccurateParam();
        Long shopId = (Long) params.get("shopId");
        param.setShopId(shopId);
        Object minAmountObj = params.get("minAmount");
        if (Langs.isNotNull(minAmountObj)) {
            String minAmountStr = String.valueOf(minAmountObj);
            if (Langs.isNotBlank(minAmountStr)) {
                param.setMinAmount(new BigDecimal(minAmountStr));
            }
        }
        Object maxAmountObj = params.get("maxAmount");
        if (Langs.isNotNull(maxAmountObj)) {
            String maxAmountStr = String.valueOf(maxAmountObj);
            if (Langs.isNotBlank(maxAmountStr)) {
                param.setMaxAmount(new BigDecimal(maxAmountStr));
            }
        }
        Object minAverageObj = params.get("minAverage");
        if (Langs.isNotNull(minAverageObj)) {
            String minAverageStr = String.valueOf(minAverageObj);
            if (Langs.isNotBlank(minAverageStr)) {
                param.setMinAverage(new BigDecimal(minAverageStr));
            }
        }
        Object maxAverageObj = params.get("maxAverage");
        if (Langs.isNotNull(maxAverageObj)) {
            String maxAverageStr = String.valueOf(maxAverageObj);
            if (Langs.isNotBlank(maxAverageStr)) {
                param.setMaxAverage(new BigDecimal(maxAverageStr));
            }
        }
        Object numberSignObj = params.get("numberSign");
        if (Langs.isNotNull(numberSignObj)) {
            String numberSign = String.valueOf(numberSignObj);
            if (Langs.isNotBlank(numberSign)) {
                param.setNumberSign(numberSign);
            }
        }
        Object numberObj = params.get("number");
        if (Langs.isNotNull(numberObj)) {
            String numberStr = String.valueOf(numberObj);
            if (Langs.isNotBlank(numberStr)) {
                param.setNumber(Integer.parseInt(numberStr));
            }
        }
        Object daySignObj = params.get("daySign");
        if (Langs.isNotNull(daySignObj)) {
            String daySignStr = String.valueOf(daySignObj);
            if (Langs.isNotBlank(daySignStr)) {
                param.setDaySign(daySignStr);
            }
        }
        Object dayObj = params.get("day");
        if (Langs.isNotNull(dayObj)) {
            String dayStr = String.valueOf(dayObj);
            if (Langs.isNotBlank(dayStr)) {
                param.setDay(Integer.parseInt(dayStr));
            }
        }
        Object carLevelTagObj = params.get("carLevelTag");
        if (Langs.isNotNull(carLevelTagObj)) {
            String carLevelTagStr = String.valueOf(carLevelTagObj);
            if (Langs.isNotBlank(carLevelTagStr)) {
                param.setCarLevelTag(Integer.parseInt(carLevelTagStr));
            }
        }
        Object minMileageObj = params.get("minMileage");
        if (Langs.isNotNull(minMileageObj)) {
            String minMileageStr = String.valueOf(minMileageObj);
            if (Langs.isNotBlank(minMileageStr)) {
                param.setMinMileage(Long.parseLong(minMileageStr));
            }
        }
        Object maxMileageObj = params.get("maxMileage");
        if (Langs.isNotNull(maxMileageObj)) {
            String maxMileageStr = String.valueOf(maxMileageObj);
            if (Langs.isNotBlank(maxMileageStr)) {
                param.setMaxMileage(Long.parseLong(maxMileageStr));
            }
        }
        Object memberLevelIdObj = params.get("memberLevelId");
        if (Langs.isNotNull(memberLevelIdObj)) {
            String memberLevelIdStr = String.valueOf(memberLevelIdObj);
            if (Langs.isNotBlank(memberLevelIdStr)) {
                param.setMemberLevelId(Long.parseLong(memberLevelIdStr));
            }
        }
        Object carLicenseObj = params.get("carLicense");
        if (Langs.isNotNull(carLicenseObj)) {
            String carLicense = String.valueOf(carLicenseObj);
            if (Langs.isNotBlank(carLicense)) {
                param.setCarLicense(carLicense);
            }
        }
        Object carTypeObj = params.get("carType");
        if (Langs.isNotNull(carTypeObj)) {
            String carType = String.valueOf(carTypeObj);
            if (Langs.isNotBlank(carType)) {
                param.setCarType(carType);
            }
        }
        Object mobileObj = params.get("mobile");
        if (Langs.isNotNull(mobileObj)) {
            String mobile = String.valueOf(mobileObj);
            if (Langs.isNotBlank(mobile)) {
                param.setMobile(mobile);
            }
        }
        Object tagObj = params.get("tag");
        if (Langs.isNotNull(tagObj)) {
            String tag = String.valueOf(tagObj);
            if (Langs.isNotBlank(tag)) {
                param.setTag(tag);
            }
        }
        Object customerTagObj = params.get("customerTag");
        if (Langs.isNotNull(customerTagObj)) {
            String customerTag = String.valueOf(customerTagObj);
            if (Langs.isNotBlank(customerTag)) {
                param.setCustomerTag(customerTag);
            }
        }
        Object sTimeObj = params.get("sTime");
        if (Langs.isNotNull(sTimeObj)) {
            String sTime = String.valueOf(sTimeObj);
            if (Langs.isNotBlank(sTime)) {
                param.setSTime(sTime);
            }
        }
        Object eTimeObj = params.get("eTime");
        if (Langs.isNotNull(eTimeObj)) {
            String eTime = String.valueOf(eTimeObj);
            if (Langs.isNotBlank(eTime)) {
                param.setETime(eTime);
            }
        }
        Object customerCompanyObj = params.get("customerCompany");
        if (Langs.isNotNull(customerCompanyObj)) {
            String customerCompany = String.valueOf(customerCompanyObj);
            if (Langs.isNotBlank(customerCompany)) {
                param.setCustomerCompany(customerCompany);
            }
        }
        Object allotObj = params.get("allot");
        if (Langs.isNotNull(allotObj)) {
            String allotStr = String.valueOf(allotObj);
            if (Langs.isNotBlank(allotStr)) {
                param.setAllot(Boolean.valueOf(allotStr));
            }
        }
        return param;
    }
}
