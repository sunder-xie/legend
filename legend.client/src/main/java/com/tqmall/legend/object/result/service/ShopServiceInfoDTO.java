package com.tqmall.legend.object.result.service;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import com.tqmall.legend.object.result.region.RegionDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by zsy on 15/12/15.
 */
@Data
public class ShopServiceInfoDTO extends BaseEntityDTO implements Serializable {
    private static final long serialVersionUID = -7854747158310875807L;
    private Long id;//服务id
    private String name;
    private String serviceSn;
    private BigDecimal servicePrice;
    private String flags;                               //“BZFW”：标准服务,“TQFW”：淘汽服务
    private String serviceNote;
    private Integer status;
    private String imgUrl;
    private String thirdImgUrl;
    private Integer sort;
    private Integer priceType;
    private String serviceInfo;
    private Long cateId;//类别id(名称统一，以前的字段，先保留)
    private Long categoryId;//类别id
    private String categoryName;//服务名称
    private Integer cateTag;                            //标准服务标签，默认0，标准7大类：1保养2洗车3美容4检修5钣喷6救援7其他
    private BigDecimal marketPrice;//市场价格，仅做车主展示
    private BigDecimal downPayment;//预付定金
    private Integer type;//服务类型：1:常规服务（包括大套餐用suite_num=2区分） 2:其它费用服务
    private Long suiteNum;//服务套餐数量,suiteNum=0单个服务,suiteNum=1为带配件服务,suiteNum=2为服务大套餐
    private String serviceUnit;//单位

    private Map<String, RegionDTO> regionDTOMap;
}
