package com.tqmall.legend.entity.pub.member;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by litan on 14-11-3.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MemberCarVo {

    //手机号
    private String mobile;

    private List<MemberCar> list;




}
