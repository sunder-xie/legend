package com.tqmall.legend.facade.insurance.bo;

import lombok.Data;

import java.util.List;

/**
 * Created by zsy on 16/9/20.
 */
@Data
public class ConsumeServiceBo {
    private List<ConsumeServiceParamBo> consumeServiceParamBoList;//核销对象
    private String mobile;//手机号
    private String code;//验证码
}
