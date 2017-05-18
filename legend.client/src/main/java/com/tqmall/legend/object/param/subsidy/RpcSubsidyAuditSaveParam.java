package com.tqmall.legend.object.param.subsidy;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiangDong.qu on 16/2/25.
 */
@Data
public class RpcSubsidyAuditSaveParam implements Serializable {

    private static final long serialVersionUID = -2456463625777185344L;

    private Long userId;         //操作用户id
    private Long shopId;         //店铺id
    private Long subsidyActId;   //补贴包id
    private Integer handleStatus;//审核状态  1:审核通过2:审核不通过
    private List<Long> userBalanceLogIds;//申领记录id列表
}
