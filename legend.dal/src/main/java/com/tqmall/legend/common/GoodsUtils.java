package com.tqmall.legend.common;

import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.entity.goods.GoodsCar;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存帮助类
 * Created by sven on 16/8/29.
 */
public class GoodsUtils {
    /**
     * 车辆信息Json转换
     *
     * @param jsonInfo
     * @return
     */
    public static String carInfoTranslate(String jsonInfo) {
        StringBuffer info = new StringBuffer();
        if (StringUtils.isBlank(jsonInfo)) {
            return "";
        }
        if ("0".equals(jsonInfo)) {
            return "通用配件";
        }
        try {
            List<GoodsCar> goodsCarList = JSONUtil.jsonStr2List(jsonInfo, GoodsCar.class);
            if (CollectionUtils.isEmpty(goodsCarList)) {
                return jsonInfo;
            }
            for (GoodsCar car : goodsCarList) {
                if (car.getCarBrandName() != null) {
                    info.append(car.getCarBrandName());
                }
                if (car.getCarSeriesName() != null) {
                    info.append(car.getCarSeriesName());
                }
                if (car.getCarAlias() != null) {
                    info.append(car.getCarAlias());
                }
                info.append(" |");
            }
        } catch (Exception e) {
            return jsonInfo;
        }
        return info.toString().substring(0,info.length()-1);
    }

    /**
     * 以逗号分隔的id
     *
     * @param ids
     * @return
     */
    public static List<Long> idsToList(String ids) {
        if (StringUtils.isBlank(ids)) {
            return null;
        }
        List<Long> idList = new ArrayList<>();
        String[] idArray = ids.split(",");
        for (String goodsId : idArray) {
            idList.add(Long.parseLong(goodsId));
        }

        return idList;
    }


}