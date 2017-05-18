package com.tqmall.legend.web.vo.shopmember;

import lombok.Data;

/**
 * Created by Mokala on 8/17/15.
 */
@Data
public class ShopLevelTabVo {
    private String levelName;
    private Integer count;
    private Integer minPoint;
    private Integer maxPoint;

    public ShopLevelTabVo(){

    }
    public ShopLevelTabVo(String levelName, Integer count,Integer minPoint,Integer maxPoint){
        this.levelName = levelName;
        this.count = count;
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }
}
