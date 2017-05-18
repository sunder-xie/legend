package com.tqmall.legend.web.account.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by twg on 17/2/28.
 */
@Getter
@Setter
public class AccountAndCarVO  implements Serializable{
    @NotEmpty(message = "车牌号不能为空")
    private String license;//车牌号
    @NotNull(message = "账户信息不能为空")
    private Long accountId;//账户id
    @NotNull(message = "是否变更车主不能为空")
    private boolean change;//是否变更车主

}
