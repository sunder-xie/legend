package com.tqmall.legend.entity.customer;

import lombok.Data;

import java.io.Serializable;

/**
 * 常用车型
 */
@Data
public class CustomerCarByModel implements Serializable{

    private Long brandId;
    private String brand;
    private Long seriesId;
    private String series;
    private Long modelId;
    private String model;
    private String importInfo;

    private String logo;
    public String getLogo() {
        return this.logo != null?(!this.logo.startsWith("http:")?"http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/" + this.logo:this.logo):null;
    }


}
