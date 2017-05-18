package com.tqmall.legend.biz.marketing.ng.bo;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xin on 16/9/21.
 */
@Excel
@Getter
@Setter
public class CustomerAnalysisBO {
    private Long customerId;
    private Long customerCarId;
    @ExcelCol(value = 0, title = "车牌", width = 10)
    private String carLicense;
    @ExcelCol(value = 1, title = "车主", width = 10)
    private String customerName;
    @ExcelCol(value = 2, title = "车主电话", width = 16)
    private String mobile;
    private Long carBrandId;
    private String carBrand;
    private Long carSeriesId;
    private String carSeries;
    private Long carModelId;
    @ExcelCol(value = 3, title = "车型", width = 20)
    private String carModel;
    @ExcelCol(value = 4, title = "最近消费时间", datePattern = "yyyy-MM-dd", width = 12)
    private Date lastPayTime;
    @ExcelCol(value = 5, title = "累计消费金额", width = 12)
    private BigDecimal totalAmount;
    @ExcelCol(value = 6, title = "累计消费次数", width = 12)
    private Integer totalNumber;
}
