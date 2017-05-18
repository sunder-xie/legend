package com.tqmall.legend.biz.sms;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.base.BizJunitBase;
import com.tqmall.legend.entity.sms.MobileVerifyRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by feilong.li on 17/2/28.
 */
public class MobileVerifyRecordServiceTest extends BizJunitBase {

    @Autowired
    private MobileVerifyRecordService mobileVerifyRecordService;

    /**
     * 手机验证码输入错误测试
     * @throws BizException
     */
    @Test(expected = BizException.class)
    public void checkSMSCodeThrowExceptionTest_01() throws BizException{
        String mobile = "13255711271";
        String sendCode = "111222";
        int validTime = 1000;

        mobileVerifyRecordService.checkSMSCodeThrowException(mobile, sendCode, validTime);
    }

    /**
     * 获取手机验证码对象返回结果测试
     * @throws BizException
     */
    @Test
    public void getMobileVerifyRecordTest_01() throws BizException{
        String mobile = "13255711271";
        MobileVerifyRecord mobileVerifyRecord = mobileVerifyRecordService.getMobileVerifyRecord(mobile);
        //Assert.assertNotNull(mobileVerifyRecord);
    }

}
