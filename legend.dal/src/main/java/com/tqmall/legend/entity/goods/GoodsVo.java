package com.tqmall.legend.entity.goods;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * GoodsBo的转换接受类
 * 
 * @Author 王佳超<jiachao.wang@tqmall.com>
 * @Create 2015年4月28日下午7:39:36
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GoodsVo extends Goods{
    private String goodsAttrRelList;
    private String goodsCarList;
    private String ownCarIdList;
    private String paintLevel;
    private String paintType;
    private BigDecimal netWeight;//净重
    private BigDecimal bucketWeight;//带桶重量=净重+桶重（包含桶盖重量）
    private BigDecimal stirWeight;//'带桶和搅拌头的重量
    public String toString(){
		return isDeleted;
    }
}
