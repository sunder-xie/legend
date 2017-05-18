package com.tqmall.legend.facade.insurance;

import com.tqmall.legend.facade.insurance.bo.InsuranceDicBO;

import java.util.List;

/**
 * Created by zxg on 17/1/20.
 * 15:30
 * no bug,以后改代码的哥们，祝你好运~！！
 * 保险的字典字段
 */
public interface AnxinInsuranceDicFacade {

    // 获得模式列表，例如模式一 买保险送奖励金
    List<InsuranceDicBO> getCooperationModeList();

    // 获得结算项目列表，例如：商业险返利
    List<InsuranceDicBO> getSettleProjectList();
}
