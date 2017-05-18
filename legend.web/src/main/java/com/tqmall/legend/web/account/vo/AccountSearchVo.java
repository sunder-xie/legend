package com.tqmall.legend.web.account.vo;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.tqmall.search.dubbo.client.legend.account.result.LegendMemberCarDTO;
import com.tqmall.search.dubbo.client.legend.account.result.LegendMemberCardDTO;
import com.tqmall.wheel.lang.Langs;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xin on 2017/3/6.
 */
@Getter
@Setter
public class AccountSearchVo {
    private Long accountId;
    private Long customerId;
    private String customerName;
    private String mobile;
    private Long shopId;
    private List<LegendMemberCardDTO> memberCards;
    private List<LegendMemberCarDTO> memberCars;

    public List<String> getLicenseList() {
        if (Langs.isEmpty(memberCars)) {
            return Collections.emptyList();
        }

        Collections.sort(memberCars, new Comparator<LegendMemberCarDTO>() {
            @Override
            public int compare(LegendMemberCarDTO o1, LegendMemberCarDTO o2) {
                Boolean o1Extends = o1.getIsExtends();
                Boolean o2Extends = o2.getIsExtends();
                if (o1Extends == null || o2Extends == null || o1Extends.equals(o2Extends)) {
                    return 0;
                } else {
                    if (o1Extends) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        });

        List<String> licenseList = Lists.newArrayList();
        for (LegendMemberCarDTO memberCar : memberCars) {
            licenseList.add(memberCar.getLicense());
        }
        return licenseList;
    }

    public Collection<LegendMemberCardDTO> getUnExpireMemberCards() {
        if (Langs.isEmpty(memberCards)) {
            return Collections.emptyList();
        }

        Collection<LegendMemberCardDTO> memberCardDTOS = Collections2.filter(memberCards, new Predicate<LegendMemberCardDTO>() {
            @Override
            public boolean apply(LegendMemberCardDTO legendMemberCardDTO) {
                return !legendMemberCardDTO.getExpire();
            }
        });
        return Lists.newArrayList(memberCardDTOS);
    }
}
