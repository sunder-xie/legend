package com.tqmall.legend.facade.sms;

import com.tqmall.common.Constants;
import com.tqmall.legend.biz.base.BizJunitBase;
import com.tqmall.legend.biz.sms.MobileVerifyRecordService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.entity.sms.MobileVerifyRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feilong.li on 17/2/23.
 */
@Slf4j
public class SmsSendFacadeTest extends BizJunitBase {

    @Autowired
    private MobileVerifyRecordService mobileVerifyRecordService;

    @Autowired
    private SmsSendFacade smsSendFacade;

    /**
     *发送手机验证码对象是否存在验证
     * @throws Exception
     */
    @Test
    public void sendMobileCoreTest_01() throws Exception{
        String mobile = "13255711271";
        MobileVerifyRecord mobileVerifyRecord = mobileVerifyRecordService.getMobileVerifyRecord(mobile);
        Assert.assertNotNull(mobileVerifyRecord);
    }

    /**
     * 发送手机验证码测试
     */
    @Test
    public void sendMobileCoreTest_02(){
        String mobile = "13255711271";
        String sendCore = "123456";
        SmsBase smsBase = new SmsBase();
        smsBase.setAction(Constants.SESSION_MOBILE_LOGIN);
        smsBase.setMobile(mobile);
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("code", sendCore);
        smsBase.setData(smsMap);

        Boolean success = smsSendFacade.sendMobileCore(mobile, sendCore, smsBase, 60 * 1000);
        Assert.assertTrue(success);
        MobileVerifyRecord mobileVerifyRecord = mobileVerifyRecordService.getMobileVerifyRecord(mobile);
        Assert.assertTrue(mobileVerifyRecord.getCode().equals(sendCore));
    }



}
