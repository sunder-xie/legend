package com.tqmall.legend.object.result.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by lifeilong on 2016/3/15.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderReceiverDTO implements Serializable{
    private static final long serialVersionUID = 4141081100652318638L;

    private Long receiver; //服务顾问id
    private String receiverName; //服务顾问人姓名
}
