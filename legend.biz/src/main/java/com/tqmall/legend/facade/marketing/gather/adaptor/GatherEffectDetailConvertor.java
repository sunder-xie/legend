package com.tqmall.legend.facade.marketing.gather.adaptor;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.marketing.gather.GatherEffectDetailDTO;
import com.tqmall.legend.enums.marketing.gather.GatherTypeEnum;
import com.tqmall.legend.enums.marketing.gather.OperateTypeEnum;
import com.tqmall.legend.facade.marketing.gather.vo.GatherEffectDetailVO;
import com.tqmall.wheel.utils.DateFormatUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2016/12/22.
 */
public class GatherEffectDetailConvertor {

    public static GatherEffectDetailVO convert(GatherEffectDetailDTO gatherEffectDetailDTO) {
        if (gatherEffectDetailDTO == null) {
            return null;
        }
        GatherEffectDetailVO gatherEffectDetailVO = new GatherEffectDetailVO();
        gatherEffectDetailVO.setId(gatherEffectDetailDTO.getId());
        gatherEffectDetailVO.setOrderId(gatherEffectDetailDTO.getOrderId());
        gatherEffectDetailVO.setCustomerId(gatherEffectDetailDTO.getCustomerId());
        gatherEffectDetailVO.setCustomerCarId(gatherEffectDetailDTO.getCustomerCarId());
        gatherEffectDetailVO.setCarLicense(gatherEffectDetailDTO.getCarLicense());
        gatherEffectDetailVO.setCustomerName(gatherEffectDetailDTO.getCustomerName());
        gatherEffectDetailVO.setCustomerMobile(gatherEffectDetailDTO.getCustomerMobile());
        gatherEffectDetailVO.setUserId(gatherEffectDetailDTO.getUserId());
        gatherEffectDetailVO.setUserName(gatherEffectDetailDTO.getUserName());
        gatherEffectDetailVO.setGatherTime(DateFormatUtils.toYMDHM(gatherEffectDetailDTO.getGatherTime()));
        gatherEffectDetailVO.setGatherType(GatherTypeEnum.getNameByValue(gatherEffectDetailDTO.getGatherType()));
        gatherEffectDetailVO.setCreator(gatherEffectDetailDTO.getCreator());
        gatherEffectDetailVO.setCreatorName(gatherEffectDetailDTO.getCreatorName());
        gatherEffectDetailVO.setOperateType(OperateTypeEnum.getNameByValue(gatherEffectDetailDTO.getOperateType()));
        gatherEffectDetailVO.setGatherConsumeTime(DateFormatUtils.toYMDHM(gatherEffectDetailDTO.getGatherConsumeTime()));
        gatherEffectDetailVO.setGatherConsumeAmount(gatherEffectDetailDTO.getGatherConsumeAmount());
        return gatherEffectDetailVO;
    }

    public static List<GatherEffectDetailVO> convertList(List<GatherEffectDetailDTO> gatherEffectDetailDTOList) {
        if (CollectionUtils.isEmpty(gatherEffectDetailDTOList)) {
            return Collections.emptyList();
        }
        List<GatherEffectDetailVO> gatherEffectDetailVOList = Lists.newArrayList();
        for (GatherEffectDetailDTO gatherEffectDetailDTO : gatherEffectDetailDTOList) {
            GatherEffectDetailVO gatherEffectDetailVO = convert(gatherEffectDetailDTO);
            if (gatherEffectDetailVO != null) {
                gatherEffectDetailVOList.add(gatherEffectDetailVO);
            }
        }
        return gatherEffectDetailVOList;
    }
}
