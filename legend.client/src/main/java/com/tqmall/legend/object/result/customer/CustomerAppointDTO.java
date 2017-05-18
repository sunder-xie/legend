package com.tqmall.legend.object.result.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lifeilong on 2016/3/22.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerAppointDTO implements Serializable{
    private static final long serialVersionUID = -4113968224545637029L;

    //预约单
    private Date appointTime;//预约时间
    private Long appointId; //预约单id
    private Long appointStatus; 	//0待确认, 1 预约成功, 2 工单生成  3 车主取消, 4 门店取消 5 微信端取消 ',
    private String appointStatusName;   //0待确认, 1 预约成功, 2 工单生成  3 车主取消, 4 门店取消 5 微信端取消 ',
}
