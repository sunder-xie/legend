package com.tqmall.legend.web.buy.vo;

import com.tqmall.legend.common.Result;
import com.tqmall.tqmallstall.domain.result.OrderGoodDTO;

public class OrderGoodDTOVO extends OrderGoodDTO {
	  private Result GoodsResult;
	  private String brandName;
	  
		  public String getBrandName() {
			return brandName;
		}
	
		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}

	

	    public Result getGoodsResult() {
	        return GoodsResult;
	    }

	    public void setGoodsResult(Result goodsResult) {
	        GoodsResult = goodsResult;
	    }

}
