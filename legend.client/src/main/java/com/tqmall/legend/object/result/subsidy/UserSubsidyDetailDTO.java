package com.tqmall.legend.object.result.subsidy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xiangDong.qu on 16/2/25.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserSubsidyDetailDTO implements Serializable {
    private static final long serialVersionUID = -3933897684740706933L;
    private String userName;  //用户名
    private Long applyId;     //用户申领id legend_user_balance_log id
    private String applyTime; //申领时间
    private Date createTime; //申领创建时间
    private Long applyNum;    //本次申领补贴数量
    private Long auditedNum;  //已领数量
    private Integer handleStatus; //处理状态  1:审核通过2:审核不通过0:待审核
}
