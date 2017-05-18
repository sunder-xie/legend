package com.tqmall.legend.facade.magic.vo;

import com.tqmall.legend.biz.bo.goods.GoodsBo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 配件资料（油漆）扩展信息 ，兼容配件资料信息
 * Created by shulin on 16/11/9.
 */
@Setter
@Getter
public class GoodsExtVo {
    private GoodsBo goodsBo;
    private Long goodsId;
    private String paintLevel;//油漆等级（水性漆，油性漆）
    private String paintType;//油漆类型（弱色系，珍珠，珍珠和彩色金属，素色，银粉）
    private BigDecimal netWeight;//净重
    private BigDecimal bucketWeight;//带桶重量=净重+桶重（包含桶盖重量）
    private BigDecimal stirWeight;//'带桶和搅拌头的重量
}
