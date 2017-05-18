package com.tqmall.legend.facade.magic.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 油漆使用记录，包含明细列表
 * <p/>
 * Created by shulin on 16/11/15.
 */
@Setter
@Getter
public class PaintUseRecordVo {
    private Long id;
    private String useRecordSn;//油漆使用记录单号
    private Date warehouseOutTime;//出库时间
    private Long operatorId;//出库人id
    private String operatorName;//出库人名称
    private String warehouseOutType;//出库类型
    private Long painterId;//领料人id（油漆工id）
    private String painterName;//领料人姓名（油漆工姓名）
    private String recordRemark;//备注
    private BigDecimal totalWeight;//出库总重量
    private BigDecimal totalAmount;//出库总金额
    private String warehouseOutTimeStr;
    private List<PaintRecordDetailVo> paintRecordDetailVoList;
}
