package com.tqmall.legend.biz.component.converter;

import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.component.DataConverter;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.object.param.note.ShopConfigParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by twg on 16/4/6.
 * 对外BUBBO接口，保存更新的实现
 */
@Slf4j
public class DataShopConfigParamConverter<T> implements DataConverter<T> {

    @Override
    public T decode(List list) {
        return null;
    }

    @Override
    public List<T> encode(T noteShopConfig) {
        ShopConfigParam note = (ShopConfigParam) noteShopConfig;
        Long shopId = note.getShopId();
        Integer confType = note.getConfType();
        if (shopId == null) {
            throw new BizException("shopId不能为空");
        }
        if (confType == null) {
            throw new BizException("confType不能为空");
        }
        List configures = Lists.newArrayList();

        if (confType.intValue() == ShopConfigureTypeEnum.APPOINTPRINT.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.HUIFANGPRINT.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.BAOXIANTIXING.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.NIANJIANTIXING.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.BAOYANGTIXING.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.BIRTHDAYTIXING.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.LOSTCUSTOMERTIXING.getCode()) {

            String firstValue = note.getFirstValue();
            if (StringUtils.isNotBlank(firstValue)) {
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfKey("0");
                shopConfigure.setConfType(Long.valueOf(confType));
                shopConfigure.setConfValue(firstValue);
                configures.add(shopConfigure);
            }
            String secondValue = note.getSecondValue();
            if (StringUtils.isNotBlank(secondValue)) {
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfKey("2");
                shopConfigure.setConfType(Long.valueOf(confType));
                shopConfigure.setConfValue(secondValue);
                configures.add(shopConfigure);
            }
            String invalidValue = note.getInvalidValue();
            if (StringUtils.isNotBlank(invalidValue)) {
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfKey("1");
                shopConfigure.setConfType(Long.valueOf(confType));
                shopConfigure.setConfValue(invalidValue);
                configures.add(shopConfigure);
            }
        } else if (confType.intValue() == ShopConfigureTypeEnum.COMMUTETIME.getCode()) {
            String firstValue = note.getFirstValue();
            if (StringUtils.isNotBlank(firstValue)) {
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfKey("3");
                shopConfigure.setConfType(Long.valueOf(confType));
                shopConfigure.setConfValue(firstValue);
                configures.add(shopConfigure);
            }
            String secondValue = note.getSecondValue();
            if (StringUtils.isNotBlank(secondValue)) {
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfKey("4");
                shopConfigure.setConfType(Long.valueOf(confType));
                shopConfigure.setConfValue(secondValue);
                configures.add(shopConfigure);
            }

        } else {
            String firstValue = note.getFirstValue();
            if (StringUtils.isNotBlank(firstValue)) {
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfType(Long.valueOf(confType));
                shopConfigure.setConfValue(firstValue);
                configures.add(shopConfigure);
            }

        }

        return configures;
    }
}
