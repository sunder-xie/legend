package com.tqmall.legend.web.insurance.common;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.insurance.AnxinInsuranceDicFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zxg on 17/1/20.
 * 16:31
 * no bug,以后改代码的哥们，祝你好运~！！
 * 保险字典公共类
 */
@RestController
public class AnxinInsuranceDicController {

    @Autowired
    private AnxinInsuranceDicFacade insuranceDicFacade;

    // 获得模式列表，例如模式一 买保险送奖励金
    @RequestMapping("getCooperationModeList")
    public Result getCooperationModeList() {
        return Result.wrapSuccessfulResult(insuranceDicFacade.getCooperationModeList());
    }
    // 获得结算项目列表，例如：商业险返利
    @RequestMapping("getSettleProjectList")
    public Result getSettleProjectList() {
        return Result.wrapSuccessfulResult(insuranceDicFacade.getSettleProjectList());
    }

}
