package com.tqmall.legend.object.param.appoint;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by wushuai on 17/2/13.
 */
@Getter
@Setter
public class CancelAppointParam implements Serializable {
    private static final long serialVersionUID = 2593127676276434131L;
    private Long appointId;         //预约单id
    private String cancelReason;    //取消原因
    private Integer channel;        //渠道
    private Long userId;            //操作员工id
}
