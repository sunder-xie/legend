package com.tqmall.legend.entity.balance;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by lifeilong on 2016/5/4.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class WithdrawCheckLog extends BaseEntity {
    private Integer balanceLogId;   //提现记录id
    private Integer currentNode;    //当前节点 1:小秘书2:市场部3:业管中心4:财务
    private Integer currentStatus;  //审核状态 0:审核失败 1:审核通过
    private String userName;        //审核人姓名
    private String checkComment;    //审核成功或失败原因
    private String currentNodeName; //当前节点名称

}
