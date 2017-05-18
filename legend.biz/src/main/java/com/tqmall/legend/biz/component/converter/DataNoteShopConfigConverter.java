package com.tqmall.legend.biz.component.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.biz.component.DataConverter;
import com.tqmall.legend.entity.shop.NoteShopConfig;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

/**
 * Created by twg on 16/4/6.
 */
@Slf4j
public class DataNoteShopConfigConverter<T> implements DataConverter<T> {

    @Override
    public T decode(List list) {
        NoteShopConfig note = new NoteShopConfig();
        for (Object o : list) {
            ShopConfigure shopConfigure = (ShopConfigure) o;
            note.setShopId(shopConfigure.getShopId());
            note.setConfType(shopConfigure.getConfType().intValue());
            if (shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.APPOINTPRINT.getCode() ||
                    shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.HUIFANGPRINT.getCode() ||
                    shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.BAOXIANTIXING.getCode() ||
                    shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.NIANJIANTIXING.getCode() ||
                    shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.BAOYANGTIXING.getCode() ||
                    shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.BIRTHDAYTIXING.getCode() ||
                    shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.LOSTCUSTOMERTIXING.getCode()) {
                if (shopConfigure.getConfKey() != null && shopConfigure.getConfKey().equals("1") && StringUtils.isNotBlank(shopConfigure.getConfValue()) &&
                        NumberUtils.isNumber(shopConfigure.getConfValue())) {
                    note.setInvalidValue(Integer.parseInt(shopConfigure.getConfValue()));
                } else if (shopConfigure.getConfKey() != null && shopConfigure.getConfKey().equals("1") && (StringUtils.isBlank(shopConfigure.getConfValue()) || !NumberUtils.isNumber(shopConfigure.getConfValue()))) {
                    note.setInvalidValue(15);
                }
                if (shopConfigure.getConfKey() != null && shopConfigure.getConfKey().equals("2") && StringUtils.isNotBlank(shopConfigure.getConfValue()) &&
                        NumberUtils.isNumber(shopConfigure.getConfValue())) {
                    note.setSecondValue(Integer.parseInt(shopConfigure.getConfValue()));
                } else if (shopConfigure.getConfKey() != null && shopConfigure.getConfKey().equals("2") && (StringUtils.isBlank(shopConfigure.getConfValue()) || !NumberUtils.isNumber(shopConfigure.getConfValue()))) {
                    note.setSecondValue(15);
                }
                if (shopConfigure.getConfKey() != null && shopConfigure.getConfKey().equals("0") && StringUtils.isNotBlank(shopConfigure.getConfValue()) &&
                        NumberUtils.isNumber(shopConfigure.getConfValue())) {
                    note.setFirstValue(Integer.parseInt(shopConfigure.getConfValue()));
                } else if (shopConfigure.getConfKey() != null && shopConfigure.getConfKey().equals("0") && (StringUtils.isBlank(shopConfigure.getConfValue()) || !NumberUtils.isNumber(shopConfigure.getConfValue()))) {
                    note.setFirstValue(15);
                }
            }
        }

        return (T) note;
    }

    @Override
    public List<T> encode(T noteShopConfig) {
        NoteShopConfig note = (NoteShopConfig) noteShopConfig;
        Long shopId = note.getShopId();
        Integer confType = note.getConfType();
        if (shopId == null) {
            throw new RuntimeException("shopId不能为空");
        }
        if (confType == null) {
            throw new RuntimeException("confType不能为空");
        }
        List configures = Lists.newArrayList();

        if (confType.intValue() == ShopConfigureTypeEnum.APPOINTPRINT.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.HUIFANGPRINT.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.BAOXIANTIXING.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.NIANJIANTIXING.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.BAOYANGTIXING.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.BIRTHDAYTIXING.getCode() ||
                confType.intValue() == ShopConfigureTypeEnum.LOSTCUSTOMERTIXING.getCode()) {

            Integer firstValue = note.getFirstValue();
            if (firstValue != null) {
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfKey("0");
                shopConfigure.setConfType(Long.valueOf(confType));
                shopConfigure.setConfValue(firstValue.toString());
                configures.add(shopConfigure);
            }
            Integer secondValue = note.getSecondValue();
            if (secondValue != null) {
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfKey("2");
                shopConfigure.setConfType(Long.valueOf(confType));
                shopConfigure.setConfValue(secondValue.toString());
                configures.add(shopConfigure);
            }
            Integer invalidValue = note.getInvalidValue();
            if (invalidValue != null) {
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfKey("1");
                shopConfigure.setConfType(Long.valueOf(confType));
                shopConfigure.setConfValue(invalidValue.toString());
                configures.add(shopConfigure);
            }
        }
        return configures;
    }
}
