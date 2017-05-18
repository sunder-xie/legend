package com.tqmall.legend.facade.magic.vo;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.magic.object.param.proxy.ProxyPageParam;
import com.tqmall.magic.object.param.proxy.ProxyParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TonyStarkSir on 16/7/12.
 */
@Slf4j
public class ProxySettlementUtils {

    /**
     * 获取对账单查询参数
     *
     * @param request
     * @param proxyShopId //委托方或者受托方门店id
     * @return
     */
    public static ProxySettlementParamVO getProxySettlementParam(HttpServletRequest request, String proxyStartTime, String proxyEndTime, String completeStartTime, String completeEndTime, String carLicense, Long proxyShopId, Integer shopFlag) {
        log.info("[委托对账单参数] 参数： proxyStartTime={},proxyEndTime={},completeStartTime={},completeEndTime={},carLicense={},proxyShopId={},shopFlag={}", proxyStartTime, proxyEndTime, carLicense, completeStartTime, completeEndTime, proxyShopId, shopFlag);
        if (shopFlag == null || shopFlag != 1 && shopFlag != 2) {
            log.error("[委托对账单参数]无法判断是委托方还是受托方");
            return null;
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        ProxySettlementParamVO proxySettlementParamVO = new ProxySettlementParamVO();
        if (proxyStartTime != null && proxyStartTime.length() != 0) {
            proxySettlementParamVO.setProxyStartTime(proxyStartTime + " 00:00:00");
        }
        if (proxyEndTime != null && proxyEndTime.length() != 0) {
            proxySettlementParamVO.setProxyEndTime(proxyEndTime + " 23:59:59");
        }
        if (completeStartTime != null && completeStartTime.length() != 0) {
            proxySettlementParamVO.setCompleteStartTime(completeStartTime + " 00:00:00");
        }
        if (completeEndTime != null && completeEndTime.length() != 0) {
            proxySettlementParamVO.setCompleteEndTime(completeEndTime + " 23:59:59");
        }
        if (shopFlag == 2) {
            //说明是受托方用户查询
            proxySettlementParamVO.setShopId(proxyShopId);
            proxySettlementParamVO.setProxyShopId(shopId);
        } else {
            //委托方对账单查询
            proxySettlementParamVO.setShopId(shopId);
            proxySettlementParamVO.setProxyShopId(proxyShopId);
        }
        return proxySettlementParamVO;
    }

    /**
     * 获取ProxyPageParam对象
     *
     * @param proxySettlementParamVO
     * @param pageable
     * @return
     */
    public static ProxyPageParam getProxyPageParam(ProxySettlementParamVO proxySettlementParamVO, String carLicense, @PageableDefault(page = 1, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("[获取ProxyPageParam对象]  参数：proxySettlementParamVO={},pageable={}", proxySettlementParamVO, pageable);
        ProxyPageParam proxyPageParam = new ProxyPageParam();
        proxyPageParam.setPageSize(pageable.getPageSize());
        proxyPageParam.setPageNum(pageable.getPageNumber());
        if (proxySettlementParamVO.getProxyShopId() != null && proxySettlementParamVO.getProxyShopId() != 0) {
            proxyPageParam.setProxyShopId(proxySettlementParamVO.getProxyShopId());
        }
        if (proxySettlementParamVO.getShopId() != null && proxySettlementParamVO.getShopId() != 0) {
            proxyPageParam.setShopId(proxySettlementParamVO.getShopId());
        }
        if (carLicense != null && carLicense.length() != 0) {
            proxyPageParam.setCarLicense(carLicense);
        }
        proxyPageParam.setProxyStartTime(proxySettlementParamVO.getProxyStartTime());
        proxyPageParam.setProxyEndTime(proxySettlementParamVO.getProxyEndTime());
        proxyPageParam.setCompleteStartTime(proxySettlementParamVO.getCompleteStartTime());
        proxyPageParam.setCompleteEndTime(proxySettlementParamVO.getCompleteEndTime());
        return proxyPageParam;
    }

    /**
     * 获取proxyParam对象
     *
     * @param proxySettlementParamVO
     * @param carLicense
     * @return
     */
    public static ProxyParam getProxyParam(ProxySettlementParamVO proxySettlementParamVO, String carLicense) {
        log.info("[获取proxyParam对象] 参数： proxySettlementParamVO={},carLicense", proxySettlementParamVO, carLicense);
        ProxyParam proxyParam = new ProxyParam();
        if (proxySettlementParamVO.getProxyShopId() != null && proxySettlementParamVO.getProxyShopId() != 0) {
            proxyParam.setProxyShopId(proxySettlementParamVO.getProxyShopId());
        }
        if (proxySettlementParamVO.getShopId() != null && proxySettlementParamVO.getShopId() != 0) {
            proxyParam.setShopId(proxySettlementParamVO.getShopId());
        }
        if (carLicense != null && carLicense.length() != 0) {
            proxyParam.setCarLicense(carLicense);
        }
        proxyParam.setProxyStartTime(proxySettlementParamVO.getProxyStartTime());
        proxyParam.setProxyEndTime(proxySettlementParamVO.getProxyEndTime());
        proxyParam.setCompleteStartTime(proxySettlementParamVO.getCompleteStartTime());
        proxyParam.setCompleteEndTime(proxySettlementParamVO.getCompleteEndTime());
        return proxyParam;
    }


    public static ProxySettlementParamVO getProxyShopType(HttpServletRequest request, Integer shopFlag, Long proxyShopId) {
        log.info("[获取对账汇总对象] 参数： shopFlag={}", shopFlag);
        ProxySettlementParamVO proxySettlementParamVO = new ProxySettlementParamVO();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (shopFlag == null) {
            return null;
        }
        if (shopFlag == 1) {
            //说明是委托方对账单汇总
            proxySettlementParamVO.setShopId(shopId);
            if (proxyShopId != null && proxyShopId != 0) {
                proxySettlementParamVO.setProxyShopId(proxyShopId);
            }
        } else if (shopFlag == 2) {
            //说明是受托方对账单
            proxySettlementParamVO.setProxyShopId(shopId);
            if (proxyShopId != null && proxyShopId != 0) {
                proxySettlementParamVO.setShopId(proxyShopId);
            }
        } else {
            return null;
        }
        return proxySettlementParamVO;
    }
}
