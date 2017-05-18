package com.tqmall.legend.web.goods.vo;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by xin on 2017/2/7.
 */
@Excel
@Getter
@Setter
public class GoodsExportVo {
    @ExcelCol(value = 0, title = "配件编号", width = 20)
    private String goodsSn;
    @ExcelCol(value = 1, title = "配件名称", width = 30)
    private String name;
    @ExcelCol(value = 2, title = "零件号", width = 30)
    private String format;
    @ExcelCol(value = 3, title = "配件类别", width = 8)
    private String goodsCat;
    @ExcelCol(value = 4, title = "配件品牌", width = 10)
    private String goodsBrand;
    @ExcelCol(value = 5, title = "适配车型", width = 30)
    private String carInfoStr;
    @ExcelCol(value = 6, title = "库存数量", width = 8)
    private Double stock;
    @ExcelCol(value = 7, title = "单位", width = 8)
    private String measureUnit;
    @ExcelCol(value = 8, title = "销售单价", width = 8)
    private Double price;
    @ExcelCol(value = 9, title = "成本单价", width = 8)
    private Double inventoryPrice;
    @ExcelCol(value = 10, title = "上次采购价", width = 8)
    private Double lastInPrice;
    @ExcelCol(value = 11, title = "上次入库日期", width = 16)
    private String lastInTimeStr;
    @ExcelCol(value = 12, title = "仓库货位", width = 8)
    private String depot;
}
