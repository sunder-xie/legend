package com.tqmall.legend.entity.privilege;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;

/**
 * Created by lilige on 16/1/4.
 */
@Data
public class Technician extends BaseEntity {

    private Long shopId;//门店id
    private Long managerId;//门店人员id
    private Integer seniority;//'维修工龄:
    private String adeptRepair;//擅长维修品牌
    private Integer technicianLevel;//'技师等级：1-初级
    private Integer verifyStatus;//'认证状态：0-未认证

    private Integer education;//学历，冗余字段
    private String graduateSchool;//毕业学校，冗余字段

}
