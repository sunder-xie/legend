package com.tqmall.legend.object.result.ranking;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by macx on 16/3/7.
 */
@Data
public class PowerRankingDTO implements Serializable{
    private static final long serialVersionUID = -1255838180177477514L;
    private Long shopId;//'云修门店
    private String shopName;//门店名称
    private String shopArea;//门店所在地区
    private String mainBusiness;//主营业务
    private Integer rankType;//'数据类型
    private String reportMonth;//'报表月份
    private BigDecimal storeArea;//门店面积
    private Integer manageCnt;//员工数量
    private Integer stationCnt;//工位数量
    private Integer technicianCnt;//注册技师数量
    private String companyRepairIntelligence;//门店维修资质
    private String majorCarBrand;//主修汽车品牌
    private BigDecimal monthlyTurnover;//月营业额
    private BigDecimal carOutput;//客单价
    private Integer memberCnt;//客户数量
    private Integer orderCnt;//工单数量
    private BigDecimal manageOutput;//员工产值
    private BigDecimal areaOutput;//亩产
    private String storeAreaPerStr;//门店面积排名
    private String manageCntPerStr;//员工数量排名
    private String stationCntPerStr;//工位数量排名
    private String technicianCntPerStr;//注册技师数量排名
    private String companyRepairIntelligencePerStr;//门店维修资质相同比
    private String majorCarBrandPerStr;//主修品牌价位相同比
    private String monthlyTurnoverPerStr;//月营业额排名
    private String carOutputPerStr;//客单价排名
    private String memberCntPerStr;//客户数量排名
    private String orderCntPerStr;//工单数量排名
    private String manageOutputPerStr;//员工产值排名
    private String areaOutputPerStr;//亩产排名
    private String storeAreaPointStr;//门店面积分数
    private String manageCntPointStr;//员工数量分数
    private String stationCntPointStr;//工位数量分数
    private String technicianCntPointStr;//注册技师数量分数
    private String companyRepairIntelligencePointStr;//门店维修资质相同比分数
    private String majorCarBrandPointStr;//主修品牌价位相同比分数
    private String monthlyTurnoverPointStr;//月营业额分数
    private String carOutputPointStr;//客单价分数
    private String memberCntPointStr;//客户数量分数
    private String orderCntPointStr;//工单数量分数
    private String manageOutputPointStr;//员工产值分数
    private String areaOutputPointStr;//亩产分数
    private String totalPerStr;//总排名
    private String totalPointStr;//总分数

}
