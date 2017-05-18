package com.tqmall.legend.facade.insurance.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by zwb on 16/8/22.
 */
@Setter
@Getter
public class InsuranceItemCoverageVo {

    /**主键ID**/
    private Integer id;

    /**创建人ID**/
    private Integer creator;

    /**更新人ID**/
    private Integer modifier;

    /**创建时间**/
    private Date gmtCreate;

    /**更新时间**/
    private Date gmtModified;

    /**是否删除,Y删除,N未删除**/
    private String isDeleted;

    /**险别基本信息表ID, 关联insurance_category.id**/
    private Integer insuranceCategoryId;

    /**险别编码**/
    private String insuranceCategoryCode;

    /**险别类型value，值可以是金额等级类型，也可以是玻璃类型等**/
    private String insuranceItemCoverageValue;

    /**险别类型显示**/
    private String insuranceItemCoverageDisplay;

    /**如果是0默认选择展示,否则为1'*/
    private Integer defaultChoose;

    //壁虎车险使用
    private Boolean isCheck=Boolean.FALSE;
}
