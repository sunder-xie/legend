package com.tqmall.legend.web.account.vo;

import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.customer.Customer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by twg on 17/2/28.
 */
@Getter
@Setter
public class CustomerAccountVO implements Serializable {
    private Customer customer;
    private List<MemberCard> memberCards;
}
