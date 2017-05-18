package com.tqmall.legend.server.account.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.object.result.account.MemberCardInfoDTO;

/**
 * Created by majian on 16/9/7.
 */
public class CardInfoConverter implements Converter<MemberCardInfo,MemberCardInfoDTO> {
    @Override
    public MemberCardInfoDTO convert(MemberCardInfo source) {
        MemberCardInfoDTO destination = new MemberCardInfoDTO();
        BeanUtils.copyProperties(source,destination);
        destination.setDiscountTypeDescriptionDetail(source.getDiscountDescription());
        int discountType = source.getDiscountType() == null ? 0 : source.getDiscountType();
        int serviceDiscountType = source.getServiceDiscountType() == null ? 0 : source.getServiceDiscountType();
        int goodsDiscountType = source.getGoodDiscountType() == null ? 0 : source.getGoodDiscountType();
        switch (discountType) {
            case 0:
                destination.setDiscountTypeDescription("无折扣");
                break;
            case 1:
                destination.setDiscountTypeDescription("全部工单折扣");
                break;
            case 2:
                String serviceDiscountDescription = getServiceDiscountDescription(serviceDiscountType);
                destination.setDiscountTypeDescription(serviceDiscountDescription);
                break;
            case 3:
                String goodsDiscountDescription = getGoodsDiscountDescription(goodsDiscountType);
                destination.setDiscountTypeDescription(goodsDiscountDescription);
                break;
            case 4:
                String description = getServiceDiscountDescription(serviceDiscountType) + "\n" + getGoodsDiscountDescription(goodsDiscountType);
                destination.setDiscountTypeDescription(description);
                break;
            default:
                break;

        }
        return destination;
    }

    private String getServiceDiscountDescription(int serviceDiscountType) {
        if (serviceDiscountType == 1) {
            return "全部服务折扣";
        } else if (serviceDiscountType == 2) {
            return "部分服务折扣";
        }
        return "服务无折扣";
    }

    private String getGoodsDiscountDescription(int goodsDiscountType) {
        if (goodsDiscountType == 1) {
            return "全部配件折扣";
        } else if (goodsDiscountType == 2) {
            return "部分配件折扣";
        }
        return "配件无折扣";
    }
}
