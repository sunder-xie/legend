package com.tqmall.legend.entity.customer;

import lombok.Data;

import java.io.Serializable;

/**
 * 常用车品牌
 */
@Data
public class CustomerCarComm implements Serializable{

    private Long carBrandId;
    private String carBrand;
    private Long carSeriesId;
    private String carSeries;
    private Long carModelId;
    private String carModel;
    private String importInfo;

    private String logo;
    public String getLogo() {
        return this.logo != null?(!this.logo.startsWith("http:")?"http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/" + this.logo:this.logo):null;
    }


}
