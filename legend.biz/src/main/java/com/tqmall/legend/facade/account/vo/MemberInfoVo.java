package com.tqmall.legend.facade.account.vo;

import com.tqmall.legend.entity.account.CardGoodsRel;
import com.tqmall.legend.entity.account.CardServiceRel;
import com.tqmall.legend.entity.account.MemberCardInfo;
import lombok.Data;

import java.util.List;

/**
 * Created by wanghui on 6/2/16.
 */
@Data
public class MemberInfoVo {
    private MemberCardInfo memberCardInfo;
    private List<CardServiceRel> cardServiceRelList;
    private List<CardGoodsRel> cardGoodsRelList;

}
