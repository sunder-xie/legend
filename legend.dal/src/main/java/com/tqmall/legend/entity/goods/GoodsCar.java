package com.tqmall.legend.entity.goods;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class GoodsCar extends BaseEntity {

    private Long goodsId;
    //品牌
    private Long carBrandId;
    private String carBrandName;
    //车型
    private Long carSeriesId;
    private String carSeriesName;
	//车系
	private Long carTypeId;
	private String carTypeName;

	//车详情
	private Long carDetailId;
	private String carDetailName;

    //排量
	@Deprecated
    private Long carPowerId;
	@Deprecated
    private String carPowerName;
    //年份
	@Deprecated
    private Long carYearId;
	@Deprecated
    private String carYearName;
    
    private String carAlias;
    private Integer isOriginal;
    private Integer tqmallStatus;
    private Long shopId;
    private String importInfo;
    
    public String getPk(){
		StringBuffer sb = new StringBuffer();
		sb.append(emptyValue(this.carBrandId)).append('-');
		sb.append(emptyValue(this.carBrandName)).append('-');
		sb.append(emptyValue(this.carSeriesId)).append('-');
		sb.append(emptyValue(this.carSeriesName)).append('-');
		sb.append(emptyValue(this.carPowerId)).append('-');
		sb.append(emptyValue(this.carPowerName)).append('-');
		sb.append(emptyValue(this.carYearId)).append('-');
		sb.append(emptyValue(this.carYearName)).append('-');
		sb.append(emptyValue(this.carAlias));
		return sb.toString();
	}
    private String emptyValue(Object v){
    	if(v != null && StringUtils.isNoneBlank(v.toString())){
    		return v.toString();
    	} else {
    		return "null";
    	}
    }
    public String getCarInfo(){
        StringBuffer sb = new StringBuffer();
        if(getCarBrandName() != null){
            sb.append(getCarBrandName());
        }
        if(StringUtils.isNotBlank(getImportInfo())){
            sb.append('(').append(getImportInfo()).append(')');
        }
        if(StringUtils.isNotBlank(getCarTypeName())){
            sb.append(' ').append(getCarTypeName());
        } else if(StringUtils.isNotBlank(getCarSeriesName())){
            sb.append(' ').append(getCarSeriesName());
        }

        return sb.toString();
    }
}
