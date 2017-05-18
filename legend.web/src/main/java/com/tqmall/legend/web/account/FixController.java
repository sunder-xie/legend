package com.tqmall.legend.web.account;

import com.tqmall.legend.biz.account.datafix.MemberDetailFix;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.zenith.errorcode.support.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by majian on 16/7/20.
 */
@RequestMapping("account/fix")
@Controller
public class FixController extends BaseController{
    @Autowired
    private MemberDetailFix memberDetailFix;

    @RequestMapping("member")
    @ResponseBody
    public Result fixCardIdOfMemberDetail() {
        memberDetailFix.fix();
        return Result.wrapSuccessfulResult("数据修复成功");
    }
}
