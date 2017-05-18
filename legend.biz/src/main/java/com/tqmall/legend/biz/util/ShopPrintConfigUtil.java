package com.tqmall.legend.biz.util;

import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.setting.ConfigFieldVO;
import com.tqmall.legend.entity.setting.PrintConfigField;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilige on 16/11/13.
 */
public class ShopPrintConfigUtil {

    public static ConfigFieldVO wrapperField(ConfigFieldVO configFieldVO, Object object) {
        List<PrintConfigField> carInfoVO = configFieldVO.getCarInfoVO();
        Class clz = object.getClass();
        for (PrintConfigField configField : carInfoVO) {
            String fieldName = configField.getField();
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                Method method = clz.getDeclaredMethod(methodName, null);
                Object value = method.invoke(object);
                configField.setFieldValue(value);
            } catch (Exception e) {
                continue;
            }
        }
        return configFieldVO;
    }

}
