package com.tqmall.legend.facade.magic.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 油漆使用记录明细
 * Created by shulin on 16/11/14.
 */
@Setter
@Getter
public class PaintRecordDetailVo {
    private Long id;
    private Integer seqno;  //序号(导出明细)
    private Long shopId;//门店id
    private Long goodsId;//商品id
    private Long useRecordId;//记录id
    private String paintLevel;//油漆等级（水性漆，油性漆）
    private String paintType;//油漆类型（弱色系，珍珠，珍珠和彩色金属，素色，银粉）
    private BigDecimal warehouseOutWeight;//出库重量（单位：g）
    private BigDecimal salePrice;//销售价(g)
    private BigDecimal warehouseOutAmount;//出库金额=出库重量*销售价
    private String detailRemark;  //备注
}
