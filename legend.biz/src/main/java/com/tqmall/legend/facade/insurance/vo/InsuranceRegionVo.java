package com.tqmall.legend.facade.insurance.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zwb on 16/8/18.
 */
@Setter
@Getter
public class InsuranceRegionVo {
    /**主键ID**/
    private Integer id;

    /**创建时间**/
    private java.util.Date gmtCreate;

    /**创建人ID**/
    private Integer creator;

    /**更新时间**/
    private java.util.Date gmtModified;

    /**更新人ID**/
    private Integer modifier;

    /**是否删除,Y删除,N未删除**/
    private String isDeleted;

    /**区域编号，可能因为保险公司的不同而不同**/
    private String regionCode;

    /**区域名**/
    private String regionName;

    /**区域级别:1代表省（包括直辖市），2代表市，3代表县（区）**/
    private Integer regionLevel;

    /**父id**/
    private Integer parentId;

    /**区域所属父级code**/
    private String regionParentCode;

    /**区域是否开放，0开放，1不开放**/
    private Integer isOpen;

    /**区域配置所属保险公司,为兼容不同公司的区域信息而设置**/
    private Integer insuranceCompanyId;
}
