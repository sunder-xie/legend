package com.tqmall.legend.object.param.ranking;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by macx on 16/3/9.
 */
@Data
public class ShopCorrectFeedbackParam extends BaseRpcParam{

    private static final long serialVersionUID = -2975482874516781129L;
    private Long shopId;//云修门店
    private BigDecimal storeArea;//门店面积
    private Integer manageCnt;//员工数量
    private Integer stationCnt;//工位数量
    private Integer technicianCnt;//注册技师数量
    private String companyRepairIntelligence;//门店维修资质
    private String majorCarBrand;//主修汽车品牌
    private String mainBusiness;//主营业务
    private String serviceItems;//服务项目
    private Integer paintCnt;//烤漆房
    private List<String> imgUrl;//门店照片
}
