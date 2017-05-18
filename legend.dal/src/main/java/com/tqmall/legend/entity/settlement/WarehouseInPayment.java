package com.tqmall.legend.entity.settlement;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseInPayment extends BaseEntity {

    private Long shopId;
    private Long paymentId;
    private String paymentName;
    private Long warehouseInId;
    private BigDecimal payAmount;
    private String payer;
    private String gmtCreateStr;

    public String getGmtCreateStr() {
        if (gmtCreate == null)
            return DateUtil.convertDateToYMDHM(new Date());
        else {
            return DateUtil.convertDateToYMDHM(gmtCreate);
        }
    }
}

