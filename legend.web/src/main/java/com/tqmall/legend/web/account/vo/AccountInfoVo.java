package com.tqmall.legend.web.account.vo;

import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import lombok.Data;

import java.util.List;

/**
 * Created by tanghao on 16/6/15.
 */
@Data
public class AccountInfoVo {
    private AccountInfo accountInfo;
    private Customer customer;
    private List<CustomerCar> customerCarList;
    private List<MemberCard> memberCardList;
}
