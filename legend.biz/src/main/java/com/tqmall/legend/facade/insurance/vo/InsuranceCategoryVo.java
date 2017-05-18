package com.tqmall.legend.facade.insurance.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by zwb on 16/8/18.
 */
@Setter
@Getter
public class InsuranceCategoryVo {
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

    /**险种一级分类,1:交通强制险 2:商业保险**/
    private Integer insuranceCategoryType;

    /**险种一级分类名称**/
    private String insuranceCategoryTypeName;

    /**险种二级分类,0: 无第二分类 1:主险 2:附加险**/
    private Integer insuranceSubcategoryType;

    /**险种二级分类名称**/
    private String insuranceSubcategoryName;

    /**保险项目编码**/
    private String insuranceCategoryCode;

    /**保险项目名称**/
    private String insuranceCategoryName;

    private List<InsuranceItemCoverageVo> insuranceItemCoverageVoList;

    //是否勾选
    private Boolean isCheck=Boolean.FALSE;
    //是否记录免赔
    private Boolean isDeductible=Boolean.FALSE;
}
