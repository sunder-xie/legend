package com.tqmall.legend.object.param.ranking;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by macx on 16/3/17.
 */
@Data
public class GameRankingParam extends BaseRpcParam {

    private static final long serialVersionUID = 2012682003360197791L;
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
}
