package com.tqmall.legend.facade.marketing.gather.bo;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.enums.marketing.gather.GatherTypeEnum;
import com.tqmall.legend.enums.marketing.gather.OperateTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xin on 2016/12/19.
 */
@Getter
@Setter
public class GatherCustomerNoteBO {
    private Long customerCarId;
    private String allotSn;
    private GatherTypeEnum gatherTypeEnum;
    private OperateTypeEnum operateTypeEnum;
    private Long relId;
    private Long accountCouponId;
    private UserInfo userInfo;
}
