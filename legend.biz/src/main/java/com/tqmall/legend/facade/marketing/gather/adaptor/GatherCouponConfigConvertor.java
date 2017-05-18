package com.tqmall.legend.facade.marketing.gather.adaptor;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.marketing.gather.GatherCouponConfig;
import com.tqmall.legend.entity.marketing.gather.GatherCouponFlowDetail;
import com.tqmall.legend.enums.marketing.gather.GatherTypeEnum;
import com.tqmall.legend.enums.marketing.gather.OperateTypeEnum;
import com.tqmall.legend.facade.customer.vo.CubeCustomerInfoVo;
import com.tqmall.legend.facade.marketing.gather.bo.GatherCustomerNoteBO;
import com.tqmall.legend.facade.marketing.gather.vo.GatherCustomerVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by wushuai on 2016/12/20.
 */
public class GatherCouponConfigConvertor {

    public static GatherCustomerVO convert(CubeCustomerInfoVo cubeCustomerInfoVo) {
        if (cubeCustomerInfoVo == null) {
            return null;
        }
        GatherCustomerVO gatherCustomerVO = new GatherCustomerVO();
        gatherCustomerVO.setCustomerCarId(cubeCustomerInfoVo.getCustomerCarId());
        gatherCustomerVO.setCustomerId(cubeCustomerInfoVo.getCustomerId());
        gatherCustomerVO.setCarLicense(cubeCustomerInfoVo.getCarLicense());
        gatherCustomerVO.setCustomerName(cubeCustomerInfoVo.getCustomerName());
        gatherCustomerVO.setCustomerMobile(cubeCustomerInfoVo.getMobile());
        gatherCustomerVO.setUserId(cubeCustomerInfoVo.getUserId());
        gatherCustomerVO.setUserName(cubeCustomerInfoVo.getUserName());
        if (StringUtils.isNotBlank(cubeCustomerInfoVo.getLastPayTime())) {
            gatherCustomerVO.setLastConsumeTime(cubeCustomerInfoVo.getLastPayTime());
        }
        gatherCustomerVO.setTotalConsumeAmount(cubeCustomerInfoVo.getTotalAmount());
        if (cubeCustomerInfoVo.getTotalNumber() != null) {
            gatherCustomerVO.setTotalConsumeCount(cubeCustomerInfoVo.getTotalNumber());
        }
        return gatherCustomerVO;
    }


    public static List<GatherCustomerVO> convertList(List<CubeCustomerInfoVo> cubeCustomerInfoVoList) {
        if (CollectionUtils.isEmpty(cubeCustomerInfoVoList)) {
            return Collections.emptyList();
        }
        List<GatherCustomerVO> gatherCustomerVOList = Lists.newArrayListWithCapacity(cubeCustomerInfoVoList.size());
        for (CubeCustomerInfoVo cubeCustomerInfoVo : cubeCustomerInfoVoList) {
            GatherCustomerVO gatherCustomerVO = convert(cubeCustomerInfoVo);
            if (gatherCustomerVO != null) {
                gatherCustomerVOList.add(gatherCustomerVO);
            }
        }
        return gatherCustomerVOList;
    }

    public static Page<GatherCustomerVO> convertPage(Page<CubeCustomerInfoVo> cubeCustomerInfoVoPage) {
        Page<GatherCustomerVO> page = new DefaultPage<>(Collections.<GatherCustomerVO>emptyList());
        if (cubeCustomerInfoVoPage == null) {
            return page;
        }
        long total = cubeCustomerInfoVoPage.getTotalElements();
        List<CubeCustomerInfoVo> content = cubeCustomerInfoVoPage.getContent();
        List<GatherCustomerVO> gatherCustomerVOList = convertList(content);
        int pageSize = cubeCustomerInfoVoPage.getSize() < 1 ? 1 : cubeCustomerInfoVoPage.getSize();
        PageRequest pageRequest = new PageRequest(cubeCustomerInfoVoPage.getNumber(), pageSize);
        page = new DefaultPage<>(gatherCustomerVOList, pageRequest, total);
        return page;
    }


    public static GatherCustomerNoteBO convert(GatherCouponConfig gatherCouponConfig, UserInfo userInfo) {
        if (gatherCouponConfig == null || userInfo == null) {
            return null;
        }
        GatherCustomerNoteBO  gatherCustomerNoteBO = new GatherCustomerNoteBO();
        gatherCustomerNoteBO.setCustomerCarId(gatherCouponConfig.getCustomerCarId());
        gatherCustomerNoteBO.setGatherTypeEnum(GatherTypeEnum.LAXIN);
        gatherCustomerNoteBO.setOperateTypeEnum(OperateTypeEnum.WX_COUPON);
        gatherCustomerNoteBO.setUserInfo(userInfo);
        return gatherCustomerNoteBO;
    }

    public static GatherCouponFlowDetail convert(GatherCouponConfig gatherCouponConfig, Customer referCustomer, Customer receiveCustomer, Long accountCouponId, Integer isNewCustomer) {
        if(gatherCouponConfig==null){
            return null;
        }
        GatherCouponFlowDetail gatherCouponFlowDetail = new GatherCouponFlowDetail();
        gatherCouponFlowDetail.setShopId(gatherCouponConfig.getShopId());
        gatherCouponFlowDetail.setGatherConfigId(gatherCouponConfig.getId());
        gatherCouponFlowDetail.setGatherCustomerNoteId(gatherCouponConfig.getGatherCustomerNoteId());
        gatherCouponFlowDetail.setGatherTime(gatherCouponConfig.getGatherTime());
        gatherCouponFlowDetail.setReferUserId(gatherCouponConfig.getUserId());
        gatherCouponFlowDetail.setReferCustomerId(gatherCouponConfig.getCustomerId());
        gatherCouponFlowDetail.setReferCustomerCarId(gatherCouponConfig.getCustomerCarId());
        if (referCustomer != null) {
            gatherCouponFlowDetail.setReferCustomerName(referCustomer.getCustomerName());
        }
        if (receiveCustomer != null) {
            gatherCouponFlowDetail.setCustomerId(receiveCustomer.getId());
        }
        gatherCouponFlowDetail.setCustomerMobile(receiveCustomer.getMobile());
        gatherCouponFlowDetail.setAccountCouponId(accountCouponId);
        gatherCouponFlowDetail.setIsNew(isNewCustomer);
        return gatherCouponFlowDetail;
    }
}
