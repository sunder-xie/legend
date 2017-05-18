package com.tqmall.legend.web.buy.vo;

import com.tqmall.legend.common.Result;
import com.tqmall.tqmallstall.domain.result.Legend.LegendOrderDTO;

import java.math.BigDecimal;

/**
 * @author wjc
 *
 *         2015年9月9日下午2:31:43
 */
public class LegendOrderDTOVO extends LegendOrderDTO {
    private Result WarehouseResult;
    
    //添加标志  是否显示一键插入
    private Boolean showBatchAdd;

    private BigDecimal rewardAmount;


	public Boolean getShowBatchAdd() {
		return showBatchAdd;
	}

	public void setShowBatchAdd(Boolean showBatchAdd) {
		this.showBatchAdd = showBatchAdd;
	}

	public Result getWarehouseResult() {
        return WarehouseResult;
    }

    public void setWarehouseResult(Result warehouseResult) {
        WarehouseResult = warehouseResult;
    }

    

}
