package com.tqmall.legend.entity.shop;

import lombok.Data;

/**
 * Created by twg on 16/3/20.
 */
@Data
public class NoteInfoVO {
    private Integer appointCount = 0;//预约数
    private Integer appointNumber;//累计消费次数
    private Integer maintainCount = 0;//保养数
    private Integer maintainNumber;//累计消费次数
    private Integer insuranceCount = 0;//保险数
    private Integer insuranceNumber;//累计消费次数
    private Integer auditingCount = 0;//年检数
    private Integer auditingNumber;//累计消费次数
    private Integer visitCount = 0;//回访数
    private Integer visitNumber;//累计消费次数
    private Integer birthdayCount = 0;//生日数
    private Integer birthdayNumber;//累计消费次数
    private Integer lostCustomerCount = 0;//客户流失数
    private Integer lostCustomerNumber;//累计消费次数

}
