package com.tqmall.legend.facade.insurance.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zwb on 16/8/22.
 */
@Setter
@Getter
public class InsuranceItemVo {
    /**主键ID**/
    private Integer id;

    /**创建时间**/
    private Date gmtCreate;

    /**创建人ID**/
    private Integer creator;

    /**更新时间**/
    private Date gmtModified;

    /**更新人ID**/
    private Integer modifier;

    /**是否删除,Y删除,N未删除**/
    private String isDeleted;

    /**保单项目id, 关联insurance_form.id**/
    private Integer insuranceFormId;

    /**是否不计免赔:0表示不记免赔,1表示记录免赔**/
    private Integer isDeductible;

    /**保险类别:1表示交强险,2表示商业险**/
    private Integer insuranceType;

    /**险别代码，表insurance_category的相应字段**/
    private String insuranceCategoryCode;

    /**关联insurance_item_coverage.id**/
    private Integer insuranceItemCoverageId;

    /**险别种类value**/
    private String insuranceItemCoverageValue;

    /**险别名称**/
    private String insuranceName;

    /**保险额度**/
    private BigDecimal insuranceAmount;

    /**保费**/
    private BigDecimal insuranceFee;

    /**险种二级分类,0: 无第二分类 1:主险 2:附加险**/
    private Integer insuranceSubcategoryType;

    /**险别种类value对应的险别类型展示**/
    private String itemCoverageValueDisplay;




}
