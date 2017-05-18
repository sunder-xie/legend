package com.tqmall.legend.facade.insurance.bo;

import com.tqmall.insurance.domain.param.insurance.ConsumeServiceParam;
import lombok.Data;

/**
 * Created by zsy on 16/9/20.
 */
@Data
public class ConsumeServiceParamBo extends ConsumeServiceParam {
    private String itemName;//服务项名称
}
