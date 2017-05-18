package com.tqmall.legend.biz.account.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by twg on 17/2/28.
 */
@Getter
@Setter
public class BaseBO implements Serializable {
    private Long shopId;
    private Long userId;
}
