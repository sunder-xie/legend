package com.tqmall.legend.facade.account.vo;

import com.tqmall.legend.entity.account.MemberCard;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xin on 2017/3/13.
 */
@Getter
@Setter
public class MemberCardBo extends MemberCard {
    /**
     * 是否属于他人（不是归属和关联账户下的卡）
     */
    private boolean belongOther;
}
