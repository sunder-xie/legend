package com.tqmall.legend.biz.component.converter;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.bo.attendance.SignTime;
import com.tqmall.legend.biz.component.DataConverter;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;

import java.util.List;

/**
 * Created by twg on 16/4/18.
 */
public class DataSignTimeConverter<T> implements DataConverter<T> {
    @Override
    public List<T> encode(T t) {
        return null;
    }

    @Override
    public T decode(List list) {
        SignTime time = new SignTime();
        for (Object o : list) {
            ShopConfigure shopConfigure = (ShopConfigure) o;
            if (shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.COMMUTETIME.getCode() && shopConfigure.getConfKey().equals("3")) {
                time.setSignInTime(DateUtil.convertStringToHMS(shopConfigure.getConfValue()));
            }else if(shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.COMMUTETIME.getCode() && shopConfigure.getConfKey().equals("4")) {
                time.setSignOffTime(DateUtil.convertStringToHMS(shopConfigure.getConfValue()));
            }
            else if(shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.COMMUTETIME.getCode() && shopConfigure.getConfKey().equals("5")) {
                time.setNoonBreakStartTime(DateUtil.convertStringToHMS(shopConfigure.getConfValue()));
            }
            else if(shopConfigure.getConfType().intValue() == ShopConfigureTypeEnum.COMMUTETIME.getCode() && shopConfigure.getConfKey().equals("6")) {
                time.setNoonBreakEndTime(DateUtil.convertStringToHMS(shopConfigure.getConfValue()));
            }
        }
        return (T) time;
    }
}
