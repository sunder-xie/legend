package com.tqmall.legend.server.account.converter;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.legend.facade.discount.bo.AccountComboDiscountBo;
import com.tqmall.legend.object.result.account.DiscountComboDTO;
import com.tqmall.wheel.lang.Langs;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2017/3/16.
 */
public class ComboDiscountConverter {

    public static List<DiscountComboDTO> convertList(List<AccountComboDiscountBo> comboDiscountBoList) {
        if (Langs.isEmpty(comboDiscountBoList)) {
            return Collections.emptyList();
        }
        return Lists.transform(comboDiscountBoList, new Function<AccountComboDiscountBo, DiscountComboDTO>() {
            @Override
            public DiscountComboDTO apply(AccountComboDiscountBo comboDiscountBo) {
                return convert(comboDiscountBo);
            }
        });
    }

    public static DiscountComboDTO convert(AccountComboDiscountBo comboDiscountBo) {
        if (comboDiscountBo == null) {
            return null;
        }
        DiscountComboDTO discountComboDTO = new DiscountComboDTO();
        discountComboDTO.setAccountComboId(comboDiscountBo.getComboId());
        discountComboDTO.setComboName(comboDiscountBo.getComboName());
        discountComboDTO.setServiceId(comboDiscountBo.getComboServiceId());
        discountComboDTO.setServiceName(comboDiscountBo.getServiceName());
        discountComboDTO.setServiceCount(comboDiscountBo.getCount());
        discountComboDTO.setAvailable(comboDiscountBo.isAvailable());
        discountComboDTO.setSelected(comboDiscountBo.isSelected());
        discountComboDTO.setUseCount(comboDiscountBo.getUseCount());
        discountComboDTO.setExpireDate(comboDiscountBo.getExpireDate());
        discountComboDTO.setDiscountAmount(comboDiscountBo.getDiscount());
        return discountComboDTO;
    }
}
