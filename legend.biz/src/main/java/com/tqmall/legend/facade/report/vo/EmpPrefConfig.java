package com.tqmall.legend.facade.report.vo;

import com.tqmall.cube.shop.result.shop.GatherPerfConfigVO;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/10/17.
 */
public class EmpPrefConfig {
    private Long shopId;//门店id
    private Integer configType;//配置类型0：服务 1：物料
    private Long relId;//config_type为0时为服务id，1时为goods_id
    private String relName;//config_type为0时为服务名，1时为物料名
    private Integer percentageType;//提成类型，0为默认比例提成，1为按特殊比例提成，2为按单数(数量)提成
    private BigDecimal percentageRate;//提成比例
    private BigDecimal percentageAmount;//每单(条)提成金额
    private BigDecimal price;//单价
    private Integer percentageMethod;//提成方式，0按营业额提成，1按毛利率提成
    private String measureUnit;//单位

    private GatherPerfConfigVO gatherPerfConfigVO;//加点绩效提成规则

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getConfigType() {
        return configType;
    }

    public void setConfigType(Integer configType) {
        this.configType = configType;
    }

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    public String getRelName() {
        return relName;
    }

    public void setRelName(String relName) {
        this.relName = relName;
    }

    public Integer getPercentageType() {
        return percentageType;
    }

    public void setPercentageType(Integer percentageType) {
        this.percentageType = percentageType;
    }

    public BigDecimal getPercentageRate() {
        return percentageRate;
    }

    public void setPercentageRate(BigDecimal percentageRate) {
        this.percentageRate = percentageRate;
    }

    public BigDecimal getPercentageAmount() {
        return percentageAmount;
    }

    public void setPercentageAmount(BigDecimal percentageAmount) {
        this.percentageAmount = percentageAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPercentageMethod() {
        return percentageMethod;
    }

    public void setPercentageMethod(Integer percentageMethod) {
        this.percentageMethod = percentageMethod;
    }

    public GatherPerfConfigVO getGatherPerfConfigVO() {
        return gatherPerfConfigVO;
    }

    public void setGatherPerfConfigVO(GatherPerfConfigVO gatherPerfConfigVO) {
        this.gatherPerfConfigVO = gatherPerfConfigVO;
    }
}
