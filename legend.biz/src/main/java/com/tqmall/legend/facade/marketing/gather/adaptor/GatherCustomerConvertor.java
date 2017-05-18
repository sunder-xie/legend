package com.tqmall.legend.facade.marketing.gather.adaptor;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.tag.CustomerInfoDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.marketing.gather.vo.GatherCustomerVO;
import com.tqmall.wheel.utils.DateFormatUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2016/12/16.
 */
public class GatherCustomerConvertor {

    public static GatherCustomerVO convert(CustomerInfoDTO customerInfoDTO) {
        if (customerInfoDTO == null) {
            return null;
        }
        GatherCustomerVO gatherCustomerVO = new GatherCustomerVO();
        gatherCustomerVO.setCustomerCarId(customerInfoDTO.getCarId());
        gatherCustomerVO.setCustomerId(customerInfoDTO.getCustomerId());
        gatherCustomerVO.setCarLicense(customerInfoDTO.getLicense());
        gatherCustomerVO.setCarModel(customerInfoDTO.getCarModel());
        gatherCustomerVO.setCustomerName(customerInfoDTO.getCustomerName());
        gatherCustomerVO.setCustomerMobile(customerInfoDTO.getMobile());
        gatherCustomerVO.setUserId(customerInfoDTO.getUserId());
        gatherCustomerVO.setUserName(customerInfoDTO.getUserName());
        if (customerInfoDTO.getLastPayTime() != null) {
            gatherCustomerVO.setLastConsumeTime(DateFormatUtils.toYMD(customerInfoDTO.getLastPayTime()));
        }
        gatherCustomerVO.setTotalConsumeAmount(customerInfoDTO.getCumulativeAmount());
        gatherCustomerVO.setTotalConsumeCount(customerInfoDTO.getCumulativeTimes());
        gatherCustomerVO.setNoteInfoId(customerInfoDTO.getNoteId());
        if (customerInfoDTO.getNoteTime() != null) {
            gatherCustomerVO.setNoteTime(DateFormatUtils.toYMD(customerInfoDTO.getNoteTime()));
        }
        return gatherCustomerVO;
    }

    public static List<GatherCustomerVO> convertList(List<CustomerInfoDTO> customerInfoDTOList) {
        if (CollectionUtils.isEmpty(customerInfoDTOList)) {
            return Collections.emptyList();
        }
        List<GatherCustomerVO> gatherCustomerVOList = Lists.newArrayListWithCapacity(customerInfoDTOList.size());
        for (CustomerInfoDTO customerInfoDTO : customerInfoDTOList) {
            GatherCustomerVO gatherCustomerVO = convert(customerInfoDTO);
            if (gatherCustomerVO != null) {
                gatherCustomerVOList.add(gatherCustomerVO);
            }
        }
        return gatherCustomerVOList;
    }

    public static Page<GatherCustomerVO> convertPage(com.tqmall.wheel.support.data.Page<CustomerInfoDTO> customerInfoDTOPage) {
        Page<GatherCustomerVO> page = new DefaultPage<>(Collections.<GatherCustomerVO>emptyList());
        if (customerInfoDTOPage == null) {
            return page;
        }
        int total = customerInfoDTOPage.getTotalNum();
        List<CustomerInfoDTO> records = customerInfoDTOPage.getRecords();
        List<GatherCustomerVO> gatherCustomerVOList = convertList(records);
        PageRequest pageRequest = new PageRequest(customerInfoDTOPage.getCurrentPageNum() - 1, customerInfoDTOPage.getPageSize());
        page = new DefaultPage<>(gatherCustomerVOList, pageRequest, total);
        return page;
    }
}
